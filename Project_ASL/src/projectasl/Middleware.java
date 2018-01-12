package projectasl;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.net.*;
import java.nio.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class Middleware {

	public static void main(String[] args) {
		int number_servers = Integer.parseInt(args[0]);
		int replication_factor = Integer.parseInt(args[1]);
		int number_read_threads = Integer.parseInt(args[2]);
		
		List<ArrayBlockingQueue<String>> read_queues = new ArrayList<ArrayBlockingQueue<String>>();
		List<ArrayBlockingQueue<String>> write_queues = new ArrayList<ArrayBlockingQueue<String>>();
		List<ThreadPool> thread_pools = new ArrayList<ThreadPool>();
		List<WriteThread> write_threads = new ArrayList<WriteThread>();
		
		for(int i=0;i<number_servers;i++){
			read_queues.add(new ArrayBlockingQueue<String>(1024));
			write_queues.add(new ArrayBlockingQueue<String>(1024));
			thread_pools.add(new ThreadPool(number_read_threads,read_queues.get(i)));
			write_threads.add(new WriteThread(write_queues.get(i)));
		}
		
		try{
			ServerSocketChannel client_listener = ServerSocketChannel.open();
			client_listener.socket().bind(new InetSocketAddress(1618));
			client_listener.configureBlocking(false);
			while(true){
				SocketChannel socket_client = client_listener.accept();
				if(socket_client!=null){
					//do something
				}
			}
		}catch(Exception e){
			System.out.println("Error");
		}
	}

}