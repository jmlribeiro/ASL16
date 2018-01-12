package joao.asl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.*;
import java.util.logging.Logger;

/* Collection of synchronous read threads with 
 * access to a common read queue.
 * */

public class ThreadPool {
	int number_of_threads;
	String server_address;
	List<ReadThread> threads = new ArrayList<ReadThread>();
	
	ThreadPool(LinkedBlockingQueue<RequestSocketPair> q, int n,String s, Logger logger){
		this.number_of_threads=n;
		this.server_address=s;
		
		// Initialize read threads in pool
		for(int i=0;i<number_of_threads;i++){
			ReadThread t=new ReadThread(q,server_address,logger);
			threads.add(t);
			t.start();
		}
	}
}
