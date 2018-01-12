package projectasl;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ReadThread extends Thread{
	ArrayBlockingQueue<String> read_queue;
	
	ReadThread(ArrayBlockingQueue<String> q){
		this.read_queue=q;
	}
}
