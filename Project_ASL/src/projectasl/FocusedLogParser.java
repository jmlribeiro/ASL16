package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FocusedLogParser {

public static void main(String[] args) throws IOException{
		
		
		int num_threads=16;
		
		FileWriter TTotalwriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/ttotal_list_"+num_threads+".txt"); 
		FileWriter TQueuewriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/tqueue_list_"+num_threads+".txt"); 
		FileWriter TServerwriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/tserver_list_"+num_threads+".txt"); 
		
		
			List<Double> TTotal = new LinkedList<>();
			List<Double> TQueue = new LinkedList<>();
			List<Double> TServer = new LinkedList<>();
			
			for(int rep=1;rep<=4;rep++){
				
				List<Double> repTTotal = new LinkedList<>();
				List<Double> repTQueue = new LinkedList<>();
				List<Double> repTServer = new LinkedList<>();
				
				BufferedReader br = new BufferedReader(new FileReader("Fine_Grain/Threads_"+num_threads+"/log_16_40_"+rep+".log"));
				br.readLine();
				while(true){
					String s = br.readLine();
					if(s == null)
						break;

					String[] split_s=s.split(", ");

					if(split_s[0].equals("get")){
						repTTotal.add(Double.parseDouble(split_s[1]));
						repTQueue.add(Double.parseDouble(split_s[2]));
						repTServer.add(Double.parseDouble(split_s[3]));
					}

				}
				br.close();
				
				for(double i=Math.ceil(0.33*repTTotal.size());i<Math.floor(0.67*repTTotal.size());i++){
					TTotal.add(repTTotal.get((int)i));
				}
				
				for(double i=Math.ceil(0.33*repTQueue.size());i<Math.floor(0.67*repTQueue.size());i++){
					TQueue.add(repTQueue.get((int)i));
				}
				
				for(double i=Math.ceil(0.33*repTServer.size());i<Math.floor(0.67*repTServer.size());i++){
					TServer.add(repTServer.get((int)i));
				}
			}
			
			for(double i=0;i<TTotal.size();i++){
				TTotalwriter.write(TTotal.get((int) i)+"\n");
			}
			
			for(double i=0;i<TQueue.size();i++){
				TQueuewriter.write(TQueue.get((int) i)+"\n");
			}
			
			for(double i=0;i<TServer.size();i++){
				TServerwriter.write(TServer.get((int) i)+"\n");
			}
			
			TTotalwriter.close();
			TQueuewriter.close();
			TServerwriter.close();
		}
	
}
