package joao.asl;

import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;
import java.nio.channels.*;
import java.nio.*;
import java.util.logging.Logger;

/* Read threads are synchronous threads which
 * process get requests. They are part of a
 * specific thread pool.
 * */

public class ReadThread extends Thread{
	
	LinkedBlockingQueue<RequestSocketPair> read_queue;
	String server_address;
	Logger logger;
	
	ReadThread(LinkedBlockingQueue<RequestSocketPair> q, String add, Logger logger){
		this.read_queue=q;
		this.server_address=add;
		this.logger=logger;
	}
	
	public void run(){
		try{
			
			// Create socket to connect to respective server
			SocketChannel socket = SocketChannel.open();
			// Parse server address (ip:port) into ip and port, then connects
			String[] split_address = server_address.split(":");
			socket.connect(
					new InetSocketAddress(split_address[0],
							Integer.parseInt(split_address[1])));
			
			// Create large enough buffer to write to/read from server, and write to client
			ByteBuffer buf = ByteBuffer.allocate(1500);

			while(true){
					// Dequeue and get request
					RequestSocketPair pair = read_queue.take();
					
					// Set dequeue time
					pair.dequeue_time=System.currentTimeMillis();
					
					// Put request in buffer
					buf.put(pair.request);
					
					// Send request to server
					buf.flip();
					socket.write(buf);
					buf.clear();
					
					// Set "sent to server" time
					pair.to_server_time=System.currentTimeMillis();
					
				    // Receive answer from memcached server
					socket.read(buf);
					
					// Set "receive answer from server" time
					pair.from_server_time=System.currentTimeMillis();
					
				    // Send answer back through socket
					buf.flip();
					pair.socket.write(buf);
					buf.clear();
					
					// Set end time
					pair.end_time=System.currentTimeMillis();
					// Set success flag
					pair.success=true;
					
					// Log info once every 100 gets
					if(pair.log){
						logger.info(String.format("%s, %d, %d, %d, %b",
				                pair.op,
				                pair.getTotalTime(),
				                pair.getQueueTime(),
				                pair.getServerTime(),
				                pair.success));
					}
				    
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

}