package joao.asl;

import java.nio.channels.SocketChannel;

/* Pair of request and associated client socket.
 * Socket is required for the read/write threads
 * to know where to forward a response back to.
 * Also includes timestamps for logging.
 * */

public class RequestSocketPair {
	
	byte[] request;
	SocketChannel socket;
	
	String op;
	long enqueue_time;
	long dequeue_time;
	long to_server_time;
	long from_server_time;
	long start_time;
	long end_time;
	boolean log=false;
	boolean success=false;
	
	public RequestSocketPair(byte[] r, SocketChannel s){
		this.request=r;
		this.socket=s;
	}
	
	public long getQueueTime(){
		// Time a request spends in a read/write queue
		return dequeue_time - enqueue_time;
	}
	
	public long getServerTime(){
		// Time a request spends in the server.
		return from_server_time - to_server_time;
	}
	
	public long getTotalTime(){
		// Total time for processing request
		return end_time - start_time;
	}
	
}
