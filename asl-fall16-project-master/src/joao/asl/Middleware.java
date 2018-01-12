package joao.asl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

/* Main receiving thread of the middleware.
 * Responsible for accepting connections,
 * receiving requests and forwarding them
 * to the correct queues, while ensuring
 * close to uniform load balancing.
 * */

public class Middleware implements Runnable{
	
	String ip;
	int port;
	List<String> server_addresses;
	int threads_in_pool;
	int replication_factor;
	
	public Middleware(String ip, int port, List<String> w, int threads, int r){
		this.ip=ip;
		this.port=port;
		this.server_addresses=w;
		this.threads_in_pool=threads;
		this.replication_factor=r;
	}
	
	
	public static int hash(byte[] key, int num){
		// Hashes the key
		String string_key="";
		try{
			string_key = new String(key,"ASCII");
		}catch(UnsupportedEncodingException e){
			System.out.println("Unsupported encoding!");
			System.exit(-1);
		}
		int h = string_key.hashCode()%num;
		if(h<0){
			return num+h;
		}
		else{
			return h;
		}
	}
	
	public static byte[] getKey(byte[] request){
		// Get key from request
		byte[] key = new byte[16];
		if(request[0]=='g' || request[0]=='s'){
			// get/set requests
			System.arraycopy(request, 4, key, 0, 16);
		}
		else{
			// delete requests
			System.arraycopy(request, 7, key, 0, 16);
		}
		
		return key;
	}
	
	public static String getOp(byte[] request){
		// Gets operation (delete, get or set) from request
		byte[] op = new byte[3];
		System.arraycopy(request, 0, op, 0, 3);
		String string_op=new String(op);
		return string_op;
	}
	
	public void run() {
		
		
		// Start logger.
        Logger logger = Logger.getLogger(Middleware.class.getName());
        try
        {
        	// Remove console handler
        	logger.setUseParentHandlers(false);
        	
            FileHandler fileHandler = 
            		new FileHandler(new SimpleDateFormat("yy-MM-dd_HH:mm:ss").format(new Date()));
            fileHandler.setFormatter(new Formatter()
            {
                @Override
                public String format(LogRecord record)
                {
                    return record.getMessage() + '\n';
                }
            });
            logger.addHandler(fileHandler);
            logger.info("Op, T_total, T_queue, T_server, Success");
        }
        catch (IOException e)
        {
            System.out.println("Cannot create log file");
            System.exit(-1);
        }
		//////////////////////////////////////////////////////////////
		
		// Counters for get and set requests: each 100 counts log once
		int get_count=0;
		int set_count=0;
		
		// Get total number of servers
		int number_of_servers = server_addresses.size();
		
		// Define lists of queues, write threads and read thread pools required
		List<LinkedBlockingQueue<RequestSocketPair>> read_queues = new ArrayList<>();
		List<LinkedBlockingQueue<RequestSocketPair>> write_queues = new ArrayList<>();
		List<WriteThread> write_threads = new ArrayList<WriteThread>();
		List<ThreadPool> thread_pools = new ArrayList<ThreadPool>();
		
		System.out.println("Setting up queues and threads...");
		
		// Initialize read/write queues, read thread pool and write threads
		for(int i=0;i<number_of_servers;i++){
			read_queues.add(new LinkedBlockingQueue<RequestSocketPair>());
			write_queues.add(new LinkedBlockingQueue<RequestSocketPair>());
			thread_pools.add(
					new ThreadPool(read_queues.get(i),threads_in_pool,server_addresses.get(i),logger));
			// Primary server is i for i-th write thread
			WriteThread t=new WriteThread(write_queues.get(i),server_addresses,
					replication_factor,i,number_of_servers,logger);
			write_threads.add(t);
			t.start();
		}
		
		System.out.println("Queues and threads set up.");
		
		// Connection with Java NIO
		try{
			// Open non-blocking ServerSocketChannel
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			System.out.println("Socket open.");
			
			// All clients connect to socket below
			ssc.socket().bind(new InetSocketAddress(ip,port));
			Selector selector = Selector.open();
			
			// Register event for accept requests
			SelectionKey key = ssc.register( selector, SelectionKey.OP_ACCEPT );
			
			// Create large enough byte buffer to read from client sockets
			ByteBuffer buf = ByteBuffer.allocate(1500);
			
			while(true){

				// Check for ready events
				selector.select();

				// Select ready events and iterate
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();

				while (it.hasNext()) {

					key = (SelectionKey)it.next();

					if ((key.readyOps() & SelectionKey.OP_ACCEPT)
							== SelectionKey.OP_ACCEPT) {

						// Accept connection
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);

						// Register event for read requests
						sc.register(selector,SelectionKey.OP_READ);

					}else if((key.readyOps() & SelectionKey.OP_READ)
							== SelectionKey.OP_READ){

						// Read the data
						SocketChannel sc = (SocketChannel)key.channel();

						// Get request size and write request into byte array
						int request_size=sc.read(buf);
						
						// Memaslap clients send empty request when finished, so check and close socket
						if(request_size==-1){
							// Sleep for 10ms between closing sockets so that pending requests can be finished
							Thread.sleep(10);
							it.remove();
							sc.close();
							System.out.println("Socket closed!");
							continue;
						}
						
						// Get request from buffer
						byte[] request=new byte[request_size];
						buf.flip();
						buf.get(request);
						buf.clear();

						// Create request+socket pair
						RequestSocketPair pair = new RequestSocketPair(request,sc);

						// Set start time of request
						pair.start_time=System.currentTimeMillis();

						// Get key and operation from request
						byte[] request_key = getKey(request);
						String request_op = getOp(request);

						// Set operation of request
						pair.op=request_op;

						// Hash key to get server
						int hash = hash(request_key,number_of_servers);

						// Route request+socket to correct queue
						if(request_op.equals("get")){
							// Only get requests, set enqueue time and log flag
							if(get_count%100==0){
								pair.log=true;
							}
							get_count++;
							pair.enqueue_time=System.currentTimeMillis();
							
							// Enqueue request
							read_queues.get(hash).put(pair);
						}else{
							// Set/delete requests, set enqueue time and log flag
							if(set_count%100==0){
								pair.log=true;
							}
							set_count++;
							pair.enqueue_time=System.currentTimeMillis();
							
							// Enqueue request
							write_queues.get(hash).put(pair);
						}
					}
					// Remove event
					it.remove();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
