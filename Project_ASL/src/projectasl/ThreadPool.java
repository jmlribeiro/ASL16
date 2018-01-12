package projectasl;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadPool {
	List<ReadThread> pool;
	
	ThreadPool(int number_threads, ArrayBlockingQueue q){
		for(int i=0;i<number_threads;i++){
			this.pool.add(new ReadThread(q));
		}
	}
}
