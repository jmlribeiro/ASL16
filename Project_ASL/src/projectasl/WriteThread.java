package projectasl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.*;

public class WriteThread extends Thread{
	
	ArrayBlockingQueue<String> write_queue;
	
	
	WriteThread(ArrayBlockingQueue<String> q){
		this.write_queue=q;
	}

}
