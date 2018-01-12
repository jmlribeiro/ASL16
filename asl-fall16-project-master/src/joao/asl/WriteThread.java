package joao.asl;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.logging.Logger;

/* Write threads are asynchronous threads
 * responsible for delete and set requests.
 * They also support replication, and forward
 * back to the client a "successful" response
 * if and only if all writes were successful.
 * */

public class WriteThread extends Thread{
	LinkedBlockingQueue<RequestSocketPair> write_queue;
	List<String> server_addresses;
	int replication_factor;
	int primary_server;
	int number_of_servers;
	Logger logger;
	
	WriteThread(LinkedBlockingQueue<RequestSocketPair> q, List<String> w, 
			int r, int s,int t, Logger logger){
		this.write_queue=q;
		this.server_addresses=w;
		this.replication_factor=r;
		this.primary_server=s;
		this.number_of_servers=t;
		this.logger=logger;
	}
	
	public static Boolean all_non_empty(
			List<Queue<ResponseTimePair>> w, int r){
		Boolean bool = true;
		for(int i=0;i<r;i++){
			bool&=(w.get(i).size()!=0);
		}
		return bool;
	}
	
	public void run(){
		try{
			
			// Each server has a corresponding socket and response queue
			List<SocketChannel> socket_list = new ArrayList<>();
			List<Queue<ResponseTimePair>> response_queue_list = new ArrayList<>();
			
			// Open selector to register read events from all sockets
			Selector selector = Selector.open();
			
			// Hash map associates each socket channel to a specific queue to store responses from it
			HashMap<SocketChannel,Queue<ResponseTimePair>> hash_map = new HashMap<>();
			
			for(int i=0;i<replication_factor;i++){
				
				// Create socket to connect to respective server, one for each replica
				SocketChannel socket = SocketChannel.open();
				
				// Non-blocking
				socket.configureBlocking(false);
				
				// Parse string with server address (ip:port) of i-th replica into ip and port
				String[] split_address = 
						server_addresses.get((primary_server+i)%number_of_servers).split(":");
				
				// Connect to relevant server
				socket.connect(new InetSocketAddress(split_address[0],Integer.parseInt(split_address[1])));
				while(!socket.finishConnect()){
				    // wait for socket to finish connecting   
				}
				
				// Open selector and register read events for relevant server
				socket.register(selector, SelectionKey.OP_READ);
				System.out.println("Socket "+i+" registered");
				
				// Add socket to list of sockets, one for each server we are writing to
				socket_list.add(socket);
				
				// Create queue for responses received from this server
				Queue<ResponseTimePair> q = new LinkedList<>();
				response_queue_list.add(q);
				
				// Associate socket with specific queue
				hash_map.put(socket_list.get(i),response_queue_list.get(i));
			}
			
			System.out.println("Sockets connected and registered!");
			
			// Queue for storing sent requests and respective sockets
			Queue<RequestSocketPair> sent_queue = new LinkedList<>();
			
			// Create large enough byte buffer to write to/read from servers, write to clients
			ByteBuffer buf = ByteBuffer.allocate(1500);
			
			while(true){

				// Poll write queue with a given timeout and write if not null
				//RequestSocketPair pair = write_queue.poll(10,TimeUnit.MILLISECONDS);
				if(!write_queue.isEmpty()){
					
					RequestSocketPair pair = write_queue.remove();
					
					// Set dequeue time
					pair.dequeue_time=System.currentTimeMillis();

					// Put request into buffer
					buf.clear();
					buf.put(pair.request);

					for(int i=0;i<replication_factor;i++){
						// Write to request to each server included in the replication
						buf.flip();
						socket_list.get(i).write(buf);

						// Set "sent to server" time for first write
						if(i==0){
							pair.to_server_time=System.currentTimeMillis();
						}
					}

					// Add request+socket pair to queue of sent requests
					sent_queue.add(pair);
				}

				// Select ready events with timeout
				selector.selectNow();

				Set<SelectionKey> selectedKeys = selector.selectedKeys();	
				Iterator<SelectionKey> it = selectedKeys.iterator();

				while(it.hasNext()){
					SelectionKey key = (SelectionKey)it.next();

					if((key.readyOps() & SelectionKey.OP_READ)
							== SelectionKey.OP_READ){

						// Get channel to read from
						SocketChannel sc = (SocketChannel)key.channel();

						// Read from channel to byte buffer and get size of response
						buf.clear();
						int response_size=sc.read(buf);

						// Set arrival time of responses (there may be several in the same packet)
						long arrival_time = System.currentTimeMillis();

						// Convert to string: may get several answers in the same string
						byte[] unsplit_bytes = new byte[response_size];
						buf.flip();
						buf.get(unsplit_bytes);
						String unsplit_server_answer = new String(unsplit_bytes,"ASCII");

						// Split string into all included answers
						String[] server_answers = unsplit_server_answer.split("\\r?\\n");

						for(int j=0;j<server_answers.length;j++){
							// Send responses and times into respective queue
							hash_map.get(sc).add(
									new ResponseTimePair(server_answers[j]+"\r\n",
											arrival_time));
						}

						while(all_non_empty(response_queue_list,replication_factor)){

							/* if all queues are non-empty then first element of each 
							 * response_queue must be a response to the head request 
							 * of sent_queue */
							RequestSocketPair sent_pair = sent_queue.remove();

							/* Check if all answers are
							 * STORED for set requests
							 * DELETED or NOT_FOUND for delete requests
							 * If not, print error*/
							Boolean isSuccessful=true;
							long max_time=0;
							for(int j=0;j<replication_factor;j++){

								// Get head of response queue, extract response and arrival time
								ResponseTimePair response_time=response_queue_list.get(j).remove();
								String response = response_time.response;
								long arr_time = response_time.arrival_time;

								// Want global max arrival time
								if(max_time<arr_time){
									max_time=arr_time;
								}

								if(!response.equals("STORED\r\n") & !response.equals("DELETED\r\n")
										& !response.equals("NOT_FOUND\r\n") & isSuccessful){

									/* if response from each server is not one of the above
									 * then something weird happened (e.g. malformed request
									 * or problem with memcached server)*/

									// Output error to console
									System.out.println("Set/Delete request error: "+response);

									// Log error
									logger.info("Set/Delete request error: "+response);

									// Put error response in buffer and write to client
									buf.clear();
									buf.put(response.getBytes());
									buf.flip();
									sent_pair.socket.write(buf);

									// Set success to false
									isSuccessful=false;

									// Set end time
									sent_pair.end_time=System.currentTimeMillis();

									// Set success flag
									sent_pair.success=false;
								}

								if(j==replication_factor-1 & isSuccessful){

									/* If answers are of the correct form
									 * then forward answer to client */

									buf.clear();
									buf.put(response.getBytes());
									buf.flip();
									sent_pair.socket.write(buf);

									// Set end time
									sent_pair.end_time=System.currentTimeMillis();

									// Set success flag
									sent_pair.success=true;
								}
							}

							// Set arrival time from server to max time computed above
							sent_pair.from_server_time=max_time;

							// Log info every 100 set/delete
							if(sent_pair.log){
								logger.info(String.format("%s, %d, %d, %d, %b",
										sent_pair.op,
										sent_pair.getTotalTime(),
										sent_pair.getQueueTime(),
										sent_pair.getServerTime(),
										sent_pair.success));
							}
						}
					}

					// Remove key so we don't process it again
					it.remove();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}