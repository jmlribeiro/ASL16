package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class M22LogParser {

public static void main(String[] args) throws IOException{
		
		int num_servers=3;
		String op_type = "get";
		
		List<Integer> seq = new LinkedList<>();
		
		seq.add(1);
		seq.add(2);
		seq.add(3);
	
		FileWriter avgTTotalwriter = new FileWriter("M22/ttotal_avg_"+num_servers+"_"+op_type+".csv"); 
		avgTTotalwriter.write("rep. factor,Avg. T Total (ms)\n");
		FileWriter avgTQueuewriter = new FileWriter("M22/tqueue_avg_"+num_servers+"_"+op_type+".csv"); 
		avgTQueuewriter.write("rep. factor,Avg. T Queue (ms)\n");
		FileWriter avgTServerwriter = new FileWriter("M22/tserver_avg_"+num_servers+"_"+op_type+".csv"); 
		avgTServerwriter.write("rep. factor,Avg. T Server (ms)\n");
		
		
		
		FileWriter stdTTotalwriter = new FileWriter("M22/ttotal_std_"+num_servers+"_"+op_type+".csv"); 
		stdTTotalwriter.write("rep. factor,STDev T Total (ms)\n");
		FileWriter stdTQueuewriter = new FileWriter("M22/tqueue_std_"+num_servers+"_"+op_type+".csv"); 
		stdTQueuewriter.write("rep. factor,STDev T Queue (ms)\n");
		FileWriter stdTServerwriter = new FileWriter("M22/tserver_std_"+num_servers+"_"+op_type+".csv"); 
		stdTServerwriter.write("rep. factor,STDev T Server (ms)\n");
		
		
		FileWriter fiftyTTotalwriter = new FileWriter("M22/ttotal_50p_"+num_servers+"_"+op_type+".csv"); 
		fiftyTTotalwriter.write("rep. factor,50 Percentile T Total (ms)\n");
		FileWriter fiftyTQueuewriter = new FileWriter("M22/tqueue_50p_"+num_servers+"_"+op_type+".csv"); 
		fiftyTQueuewriter.write("rep. factor,50 Percentile T Queue (ms)\n");
		FileWriter fiftyTServerwriter = new FileWriter("M22/tserver_50p_"+num_servers+"_"+op_type+".csv"); 
		fiftyTServerwriter.write("rep. factor,50 Percentile T Server (ms)\n");
		
		
		FileWriter ninetyTTotalwriter = new FileWriter("M22/ttotal_90p_"+num_servers+"_"+op_type+".csv"); 
		ninetyTTotalwriter.write("rep. factor,90 Percentile T Total (ms)\n");
		FileWriter ninetyTQueuewriter = new FileWriter("M22/tqueue_90p_"+num_servers+"_"+op_type+".csv"); 
		ninetyTQueuewriter.write("rep. factor,90 Percentile T Queue (ms)\n");
		FileWriter ninetyTServerwriter = new FileWriter("M22/tserver_90p_"+num_servers+"_"+op_type+".csv"); 
		ninetyTServerwriter.write("rep. factor,90 Percentile T Server (ms)\n");
		
		
		FileWriter ninetynineTTotalwriter = new FileWriter("M22/ttotal_99p_"+num_servers+"_"+op_type+".csv"); 
		ninetynineTTotalwriter.write("rep. factor,99 Percentile T Total (ms)\n");
		FileWriter ninetynineTQueuewriter = new FileWriter("M22/tqueue_99p_"+num_servers+"_"+op_type+".csv"); 
		ninetynineTQueuewriter.write("rep. factor,99 Percentile T Queue (ms)\n");
		FileWriter ninetynineTServerwriter = new FileWriter("M22/tserver_99p_"+num_servers+"_"+op_type+".csv"); 
		ninetynineTServerwriter.write("rep. factor,99 Percentile T Server (ms)\n");
		
		
		for(int i=0;i<3;i++){
			
			List<List<Double>> TTotal = new LinkedList<>();
			List<List<Double>> TQueue = new LinkedList<>();
			List<List<Double>> TServer = new LinkedList<>();
			
			for(int rep=1;rep<=5;rep++){
				
				List<Double> repTTotal = new LinkedList<>();
				List<Double> repTQueue = new LinkedList<>();
				List<Double> repTServer = new LinkedList<>();
				
				BufferedReader br = new BufferedReader(new FileReader("M22/log_"+num_servers+"_"+seq.get(i)+"_"+rep+".log"));
				br.readLine();
				while(true){
					String s = br.readLine();
					if(s == null)
						break;

					String[] split_s=s.split(", ");

					if(split_s[0].equals(op_type)){
						repTTotal.add(Double.parseDouble(split_s[1])/1000000.0);
						repTQueue.add(Double.parseDouble(split_s[2])/1000000.0);
						//System.out.println(Double.parseDouble(split_s[2])/1000000.0);
						repTServer.add(Double.parseDouble(split_s[3])/1000000.0);
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
			for(int k=0;k<5;k++){
				for(double j=Math.ceil(0.29*TTotal.get(k).size());j<Math.floor(0.86*TTotal.get(k).size());j++){
					ttotal_sum+=TTotal.get(k).get((int) j);
					tqueue_sum+=TQueue.get(k).get((int) j);
					tserver_sum+=TServer.get(k).get((int) j);
				}
			}
			
			double total_num = Math.floor(0.86*TTotal.get(0).size())
					+Math.floor(0.86*TTotal.get(1).size())+
					Math.floor(0.86*TTotal.get(2).size())+Math.floor(0.86*TTotal.get(3).size())+Math.floor(0.86*TTotal.get(4).size())-
					Math.ceil(0.29*TTotal.get(0).size())-Math.ceil(0.29*TTotal.get(1).size())-
					Math.ceil(0.29*TTotal.get(2).size())-Math.ceil(0.29*TTotal.get(3).size())-Math.ceil(0.29*TTotal.get(4).size());
			
			double ttotal_avg=ttotal_sum/total_num;
			double tqueue_avg=tqueue_sum/total_num;
			double tserver_avg=tserver_sum/total_num;
			
			System.out.println("Rep. factor: "+seq.get(i));
			System.out.println("########################");
			
			System.out.println("T Total avg: "+ttotal_avg);
			avgTTotalwriter.write(seq.get(i)+","+ttotal_avg+"\n");
			System.out.println("T Queue avg: "+tqueue_avg);
			avgTQueuewriter.write(seq.get(i)+","+tqueue_avg+"\n");
			System.out.println("T Server avg: "+tserver_avg);
			avgTServerwriter.write(seq.get(i)+","+tserver_avg+"\n");
			System.out.println("########################");
			
			double ttotal_std=0.0;
			double tqueue_std=0.0;
			double tserver_std=0.0;
			
			for(int k=0;k<5;k++){
				double ttotal_diff=0.0;
				double tqueue_diff=0.0;
				double tserver_diff=0.0;
				double sample_size=Math.floor(0.86*TTotal.get(k).size())-Math.ceil(0.29*TTotal.get(k).size());
				for(double j=Math.ceil(0.29*TTotal.get(k).size());j<Math.floor(0.86*TTotal.get(k).size());j++){
					ttotal_diff+=(TTotal.get(k).get((int) j)-ttotal_avg)*(TTotal.get(k).get((int) j)-ttotal_avg);
					tqueue_diff+=(TQueue.get(k).get((int) j)-tqueue_avg)*(TQueue.get(k).get((int) j)-tqueue_avg);
					tserver_diff+=(TServer.get(k).get((int) j)-tserver_avg)*(TServer.get(k).get((int) j)-tserver_avg);
				}
				ttotal_std+=0.2*Math.sqrt(ttotal_diff/(sample_size-1.0));
				tqueue_std+=0.2*Math.sqrt(tqueue_diff/(sample_size-1.0));
				tserver_std+=0.2*Math.sqrt(tserver_diff/(sample_size-1.0));
			}
			
			System.out.println("T Total std: "+ttotal_std);
			stdTTotalwriter.write(seq.get(i)+","+ttotal_std+"\n");
			System.out.println("T Queue std: "+tqueue_std);
			stdTQueuewriter.write(seq.get(i)+","+tqueue_std+"\n");
			System.out.println("T Server std: "+tserver_std);
			stdTServerwriter.write(seq.get(i)+","+tserver_std+"\n");
			System.out.println("########################");
			
			
			/*List<Double> fullTTotal = new LinkedList<>();
			List<Double> fullTQueue = new LinkedList<>();
			List<Double> fullTServer = new LinkedList<>();
			for(int k=0;k<4;k++){
				for(double j=Math.ceil(0.29*TTotal.get(k).size());j<Math.floor(0.86*TTotal.get(k).size());j++){
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
			
			for(int k=0;k<5;k++){
				Collections.sort(TTotal.get(k));
				Collections.sort(TQueue.get(k));
				Collections.sort(TServer.get(k));
				
				ttotal_fiftyp+=0.2*TTotal.get(k).get((int) Math.ceil(0.5*TTotal.get(k).size()));
				ttotal_ninetyp+=0.2*TTotal.get(k).get((int) Math.ceil(0.9*TTotal.get(k).size()));
				ttotal_ninetyninep+=0.2*TTotal.get(k).get((int) Math.ceil(0.99*TTotal.get(k).size()));
				
				tqueue_fiftyp+=0.2*TQueue.get(k).get((int) Math.ceil(0.5*TQueue.get(k).size()));
				tqueue_ninetyp+=0.2*TQueue.get(k).get((int) Math.ceil(0.9*TQueue.get(k).size()));
				tqueue_ninetyninep+=0.2*TQueue.get(k).get((int) Math.ceil(0.99*TQueue.get(k).size()));
				
				tserver_fiftyp+=0.2*TServer.get(k).get((int) Math.ceil(0.5*TServer.get(k).size()));
				tserver_ninetyp+=0.2*TServer.get(k).get((int) Math.ceil(0.9*TServer.get(k).size()));
				tserver_ninetyninep+=0.2*TServer.get(k).get((int) Math.ceil(0.99*TServer.get(k).size()));
			}
			
			System.out.println("50th percentile T Total: "+ttotal_fiftyp);
			fiftyTTotalwriter.write(seq.get(i)+","+ttotal_fiftyp+"\n");
			System.out.println("50th percentile T Queue: "+tqueue_fiftyp);
			fiftyTQueuewriter.write(seq.get(i)+","+tqueue_fiftyp+"\n");
			System.out.println("50th percentile T Server: "+tserver_fiftyp);
			fiftyTServerwriter.write(seq.get(i)+","+tserver_fiftyp+"\n");
			System.out.println("########################");
			
			System.out.println("90th percentile T Total: "+ttotal_ninetyp);
			ninetyTTotalwriter.write(seq.get(i)+","+ttotal_ninetyp+"\n");
			System.out.println("90th percentile T Queue: "+tqueue_ninetyp);
			ninetyTQueuewriter.write(seq.get(i)+","+tqueue_ninetyp+"\n");
			System.out.println("90th percentile T Server: "+tserver_ninetyp);
			ninetyTServerwriter.write(seq.get(i)+","+tserver_ninetyp+"\n");
			System.out.println("########################");
			
			System.out.println("99th percentile T Total: "+ttotal_ninetyninep);
			ninetynineTTotalwriter.write(seq.get(i)+","+ttotal_ninetyninep+"\n");
			System.out.println("99th percentile T Queue: "+tqueue_ninetyninep);
			ninetynineTQueuewriter.write(seq.get(i)+","+tqueue_ninetyninep+"\n");
			System.out.println("99th percentile T Server: "+tserver_ninetyninep);
			ninetynineTServerwriter.write(seq.get(i)+","+tserver_ninetyninep+"\n");
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
