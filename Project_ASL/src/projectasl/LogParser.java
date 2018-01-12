package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

public class LogParser {
	
	public static void main(String[] args) throws IOException{
		
		
		int num_threads=40;
		
		FileWriter avgTTotalwriter = new FileWriter("Threads_"+num_threads+"/ttotal_avg_"+num_threads+".csv"); 
		avgTTotalwriter.write("nr. clients,Avg. T Total (ms)\n");
		FileWriter avgTQueuewriter = new FileWriter("Threads_"+num_threads+"/tqueue_avg_"+num_threads+".csv"); 
		avgTQueuewriter.write("nr. clients,Avg. T Queue (ms)\n");
		FileWriter avgTServerwriter = new FileWriter("Threads_"+num_threads+"/tserver_avg_"+num_threads+".csv"); 
		avgTServerwriter.write("nr. clients,Avg. T Server (ms)\n");
		
		
		
		FileWriter stdTTotalwriter = new FileWriter("Threads_"+num_threads+"/ttotal_std_"+num_threads+".csv"); 
		stdTTotalwriter.write("nr. clients,STDev T Total (ms)\n");
		FileWriter stdTQueuewriter = new FileWriter("Threads_"+num_threads+"/tqueue_std_"+num_threads+".csv"); 
		stdTQueuewriter.write("nr. clients,STDev T Queue (ms)\n");
		FileWriter stdTServerwriter = new FileWriter("Threads_"+num_threads+"/tserver_std_"+num_threads+".csv"); 
		stdTServerwriter.write("nr. clients,STDev T Server (ms)\n");
		
		
		FileWriter fiftyTTotalwriter = new FileWriter("Threads_"+num_threads+"/ttotal_50p_"+num_threads+".csv"); 
		fiftyTTotalwriter.write("nr. clients,50 Percentile T Total (ms)\n");
		FileWriter fiftyTQueuewriter = new FileWriter("Threads_"+num_threads+"/tqueue_50p_"+num_threads+".csv"); 
		fiftyTQueuewriter.write("nr. clients,50 Percentile T Queue (ms)\n");
		FileWriter fiftyTServerwriter = new FileWriter("Threads_"+num_threads+"/tserver_50p_"+num_threads+".csv"); 
		fiftyTServerwriter.write("nr. clients,50 Percentile T Server (ms)\n");
		
		
		FileWriter ninetyTTotalwriter = new FileWriter("Threads_"+num_threads+"/ttotal_90p_"+num_threads+".csv"); 
		ninetyTTotalwriter.write("nr. clients,90 Percentile T Total (ms)\n");
		FileWriter ninetyTQueuewriter = new FileWriter("Threads_"+num_threads+"/tqueue_90p_"+num_threads+".csv"); 
		ninetyTQueuewriter.write("nr. clients,90 Percentile T Queue (ms)\n");
		FileWriter ninetyTServerwriter = new FileWriter("Threads_"+num_threads+"/tserver_90p_"+num_threads+".csv"); 
		ninetyTServerwriter.write("nr. clients,90 Percentile T Server (ms)\n");
		
		
		FileWriter ninetynineTTotalwriter = new FileWriter("Threads_"+num_threads+"/ttotal_99p_"+num_threads+".csv"); 
		ninetynineTTotalwriter.write("nr. clients,99 Percentile T Total (ms)\n");
		FileWriter ninetynineTQueuewriter = new FileWriter("Threads_"+num_threads+"/tqueue_99p_"+num_threads+".csv"); 
		ninetynineTQueuewriter.write("nr. clients,99 Percentile T Queue (ms)\n");
		FileWriter ninetynineTServerwriter = new FileWriter("Threads_"+num_threads+"/tserver_99p_"+num_threads+".csv"); 
		ninetynineTServerwriter.write("nr. clients,99 Percentile T Server (ms)\n");
		
		
		
		List<Integer> seq = new LinkedList<>();
		
		seq.add(10);
		seq.add(30);
		//seq.add(40);
		//seq.add(46);
		seq.add(50);
		//seq.add(60);
		seq.add(70);
		
		
		//seq.add(10);
		//seq.add(30);
		/*seq.add(36);
		seq.add(38);
		seq.add(40);
		seq.add(42);
		seq.add(44);
		seq.add(46);
		seq.add(48);
		seq.add(50);*/
		//seq.add(60);
		//seq.add(70);
		//seq.add(90);
		
		for(int i=0;i<4;i++){
			
			List<List<Double>> TTotal = new LinkedList<>();
			List<List<Double>> TQueue = new LinkedList<>();
			List<List<Double>> TServer = new LinkedList<>();
			
			for(int rep=1;rep<=4;rep++){
				
				List<Double> repTTotal = new LinkedList<>();
				List<Double> repTQueue = new LinkedList<>();
				List<Double> repTServer = new LinkedList<>();
				
				BufferedReader br = new BufferedReader(new FileReader("Threads_"+num_threads+"/log_"+num_threads+"_"+seq.get(i)+"_"+rep+".log"));
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
				TTotal.add(repTTotal);
				TQueue.add(repTQueue);
				TServer.add(repTServer);
				br.close();
			}
			
			double ttotal_sum=0.0;
			double tqueue_sum=0.0;
			double tserver_sum=0.0;
			for(int k=0;k<4;k++){
				for(double j=Math.ceil(0.33*TTotal.get(k).size());j<Math.floor(0.67*TTotal.get(k).size());j++){
					ttotal_sum+=TTotal.get(k).get((int) j);
					tqueue_sum+=TQueue.get(k).get((int) j);
					tserver_sum+=TServer.get(k).get((int) j);
				}
			}
			
			double total_num = Math.floor(0.67*TTotal.get(0).size())
					+Math.floor(0.67*TTotal.get(1).size())+
					Math.floor(0.67*TTotal.get(2).size())+Math.floor(0.67*TTotal.get(3).size())-
					Math.ceil(0.33*TTotal.get(0).size())-Math.ceil(0.33*TTotal.get(1).size())-
					Math.ceil(0.33*TTotal.get(2).size())-Math.ceil(0.33*TTotal.get(3).size());
			
			double ttotal_avg=ttotal_sum/total_num;
			double tqueue_avg=tqueue_sum/total_num;
			double tserver_avg=tserver_sum/total_num;
			
			System.out.println("Clients: "+5*seq.get(i));
			System.out.println("########################");
			
			System.out.println("T Total avg: "+ttotal_avg);
			avgTTotalwriter.write(5*seq.get(i)+","+ttotal_avg+"\n");
			System.out.println("T Queue avg: "+tqueue_avg);
			avgTQueuewriter.write(5*seq.get(i)+","+tqueue_avg+"\n");
			System.out.println("T Server avg: "+tserver_avg);
			avgTServerwriter.write(5*seq.get(i)+","+tserver_avg+"\n");
			System.out.println("########################");
			
			double ttotal_std=0.0;
			double tqueue_std=0.0;
			double tserver_std=0.0;
			
			for(int k=0;k<4;k++){
				double ttotal_diff=0.0;
				double tqueue_diff=0.0;
				double tserver_diff=0.0;
				double sample_size=Math.floor(0.67*TTotal.get(k).size())-Math.ceil(0.33*TTotal.get(k).size());
				for(double j=Math.ceil(0.33*TTotal.get(k).size());j<Math.floor(0.67*TTotal.get(k).size());j++){
					ttotal_diff+=(TTotal.get(k).get((int) j)-ttotal_avg)*(TTotal.get(k).get((int) j)-ttotal_avg);
					tqueue_diff+=(TQueue.get(k).get((int) j)-ttotal_avg)*(TQueue.get(k).get((int) j)-tqueue_avg);
					tserver_diff+=(TServer.get(k).get((int) j)-ttotal_avg)*(TServer.get(k).get((int) j)-tserver_avg);
				}
				ttotal_std+=0.25*Math.sqrt(ttotal_diff/(sample_size-1.0));
				tqueue_std+=0.25*Math.sqrt(tqueue_diff/(sample_size-1.0));
				tserver_std+=0.25*Math.sqrt(tserver_diff/(sample_size-1.0));
			}
			
			System.out.println("T Total std: "+ttotal_std);
			stdTTotalwriter.write(5*seq.get(i)+","+ttotal_std+"\n");
			System.out.println("T Queue std: "+tqueue_std);
			stdTQueuewriter.write(5*seq.get(i)+","+tqueue_std+"\n");
			System.out.println("T Server std: "+tserver_std);
			stdTServerwriter.write(5*seq.get(i)+","+tserver_std+"\n");
			System.out.println("########################");
			
			
			/*List<Double> fullTTotal = new LinkedList<>();
			List<Double> fullTQueue = new LinkedList<>();
			List<Double> fullTServer = new LinkedList<>();
			for(int k=0;k<4;k++){
				for(double j=Math.ceil(0.33*TTotal.get(k).size());j<Math.floor(0.67*TTotal.get(k).size());j++){
					fullTTotal.add(TTotal.get(k).get((int) j));
					fullTQueue.add(TQueue.get(k).get((int) j));
					fullTServer.add(TServer.get(k).get((int) j));
				}
			}*/
			
			double ttotal_fiftyp = 0.0;
			double ttotal_ninetyp = 0.0;
			double ttotal_ninetyninep = 0.0;
			
			double tqueue_fiftyp = 0.0;
			double tqueue_ninetyp = 0.0;
			double tqueue_ninetyninep = 0.0;
			
			double tserver_fiftyp = 0.0;
			double tserver_ninetyp = 0.0;
			double tserver_ninetyninep = 0.0;
			
			for(int k=0;k<4;k++){
				Collections.sort(TTotal.get(k));
				Collections.sort(TQueue.get(k));
				Collections.sort(TServer.get(k));
				
				ttotal_fiftyp+=0.25*TTotal.get(k).get((int) Math.ceil(0.5*TTotal.get(k).size()));
				ttotal_ninetyp+=0.25*TTotal.get(k).get((int) Math.ceil(0.9*TTotal.get(k).size()));
				ttotal_ninetyninep+=0.25*TTotal.get(k).get((int) Math.ceil(0.99*TTotal.get(k).size()));
				
				tqueue_fiftyp+=0.25*TQueue.get(k).get((int) Math.ceil(0.5*TQueue.get(k).size()));
				tqueue_ninetyp+=0.25*TQueue.get(k).get((int) Math.ceil(0.9*TQueue.get(k).size()));
				tqueue_ninetyninep+=0.25*TQueue.get(k).get((int) Math.ceil(0.99*TQueue.get(k).size()));
				
				tserver_fiftyp+=0.25*TServer.get(k).get((int) Math.ceil(0.5*TServer.get(k).size()));
				tserver_ninetyp+=0.25*TServer.get(k).get((int) Math.ceil(0.9*TServer.get(k).size()));
				tserver_ninetyninep+=0.25*TServer.get(k).get((int) Math.ceil(0.99*TServer.get(k).size()));
			}
			
			System.out.println("50th percentile T Total: "+ttotal_fiftyp);
			fiftyTTotalwriter.write(5*seq.get(i)+","+ttotal_fiftyp+"\n");
			System.out.println("50th percentile T Queue: "+tqueue_fiftyp);
			fiftyTQueuewriter.write(5*seq.get(i)+","+tqueue_fiftyp+"\n");
			System.out.println("50th percentile T Server: "+tserver_fiftyp);
			fiftyTServerwriter.write(5*seq.get(i)+","+tserver_fiftyp+"\n");
			System.out.println("########################");
			
			System.out.println("90th percentile T Total: "+ttotal_ninetyp);
			ninetyTTotalwriter.write(5*seq.get(i)+","+ttotal_ninetyp+"\n");
			System.out.println("90th percentile T Queue: "+tqueue_ninetyp);
			ninetyTQueuewriter.write(5*seq.get(i)+","+tqueue_ninetyp+"\n");
			System.out.println("90th percentile T Server: "+tserver_ninetyp);
			ninetyTServerwriter.write(5*seq.get(i)+","+tserver_ninetyp+"\n");
			System.out.println("########################");
			
			System.out.println("99th percentile T Total: "+ttotal_ninetyninep);
			ninetynineTTotalwriter.write(5*seq.get(i)+","+ttotal_ninetyninep+"\n");
			System.out.println("99th percentile T Queue: "+tqueue_ninetyninep);
			ninetynineTQueuewriter.write(5*seq.get(i)+","+tqueue_ninetyninep+"\n");
			System.out.println("99th percentile T Server: "+tserver_ninetyninep);
			ninetynineTServerwriter.write(5*seq.get(i)+","+tserver_ninetyninep+"\n");
			System.out.println("########################");

		}
		
		avgTTotalwriter.close();
		avgTQueuewriter.close();
		avgTServerwriter.close();
		stdTTotalwriter.close();
		stdTQueuewriter.close();
		stdTServerwriter.close();
		fiftyTTotalwriter.close();
		fiftyTQueuewriter.close();
		fiftyTServerwriter.close();
		ninetyTTotalwriter.close();
		ninetyTQueuewriter.close();
		ninetyTServerwriter.close();
		ninetynineTTotalwriter.close();
		ninetynineTQueuewriter.close();
		ninetynineTServerwriter.close();
	}

}
