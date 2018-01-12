package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class M23LogParser {
	
public static void main(String[] args) throws IOException{
		
		int num_servers=3;
		String op_type = "set";
		int write_prop=10;
		
		List<Integer> seq = new LinkedList<>();
		
		seq.add(1);
		seq.add(num_servers);
	
		FileWriter avgTTotalwriter = new FileWriter("M23/rev_ttotal_avg_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		avgTTotalwriter.write("rep. factor,Avg. T Total (ms)\n");
		FileWriter avgTQueuewriter = new FileWriter("M23/rev_tqueue_avg_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		avgTQueuewriter.write("rep. factor,Avg. T Queue (ms)\n");
		FileWriter avgTServerwriter = new FileWriter("M23/rev_tserver_avg_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		avgTServerwriter.write("rep. factor,Avg. T Server (ms)\n");
		
		
		
		FileWriter stdTTotalwriter = new FileWriter("M23/rev_ttotal_std_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		stdTTotalwriter.write("rep. factor,STDev T Total (ms)\n");
		FileWriter stdTQueuewriter = new FileWriter("M23/rev_tqueue_std_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		stdTQueuewriter.write("rep. factor,STDev T Queue (ms)\n");
		FileWriter stdTServerwriter = new FileWriter("M23/rev_tserver_std_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		stdTServerwriter.write("rep. factor,STDev T Server (ms)\n");
		
		
		FileWriter fiftyTTotalwriter = new FileWriter("M23/rev_ttotal_50p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		fiftyTTotalwriter.write("rep. factor,50 Percentile T Total (ms)\n");
		FileWriter fiftyTQueuewriter = new FileWriter("M23/rev_tqueue_50p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		fiftyTQueuewriter.write("rep. factor,50 Percentile T Queue (ms)\n");
		FileWriter fiftyTServerwriter = new FileWriter("M23/rev_tserver_50p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		fiftyTServerwriter.write("rep. factor,50 Percentile T Server (ms)\n");
		
		
		FileWriter ninetyTTotalwriter = new FileWriter("M23/rev_ttotal_90p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetyTTotalwriter.write("rep. factor,90 Percentile T Total (ms)\n");
		FileWriter ninetyTQueuewriter = new FileWriter("M23/rev_tqueue_90p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetyTQueuewriter.write("rep. factor,90 Percentile T Queue (ms)\n");
		FileWriter ninetyTServerwriter = new FileWriter("M23/rev_tserver_90p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetyTServerwriter.write("rep. factor,90 Percentile T Server (ms)\n");
		
		
		FileWriter ninetynineTTotalwriter = new FileWriter("M23/rev_ttotal_99p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetynineTTotalwriter.write("rep. factor,99 Percentile T Total (ms)\n");
		FileWriter ninetynineTQueuewriter = new FileWriter("M23/rev_tqueue_99p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetynineTQueuewriter.write("rep. factor,99 Percentile T Queue (ms)\n");
		FileWriter ninetynineTServerwriter = new FileWriter("M23/rev_tserver_99p_"+num_servers+"_"+write_prop+"_"+op_type+".csv"); 
		ninetynineTServerwriter.write("rep. factor,99 Percentile T Server (ms)\n");
		
		
		for(int i=0;i<2;i++){
			
			List<List<Double>> TTotal = new LinkedList<>();
			List<List<Double>> TQueue = new LinkedList<>();
			List<List<Double>> TServer = new LinkedList<>();
			
			for(int rep=1;rep<=4;rep++){
				
				
				List<Double> repTTotal = new LinkedList<>();
				List<Double> repTQueue = new LinkedList<>();
				List<Double> repTServer = new LinkedList<>();
				
				BufferedReader br = new BufferedReader(new FileReader("M23/log_"+num_servers+"_"+seq.get(i)+"_"+rep+"_"+write_prop+".log"));
				br.readLine();
				while(true){
					String s = br.readLine();
					if(s == null)
						break;

					String[] split_s=s.split(", ");

					if(op_type.equals("total") || split_s[0].equals(op_type)){
						repTTotal.add(Long.parseLong(split_s[1])/1000000.0);
						repTQueue.add(Long.parseLong(split_s[2])/1000000.0);
						repTServer.add(Long.parseLong(split_s[3])/1000000.0);
					}

				}
				TTotal.add(repTTotal);
				TQueue.add(repTQueue);
				TServer.add(repTServer);
				br.close();
			}
			
			List<Double> fullTTotal = new LinkedList<>();
			List<Double> fullTQueue = new LinkedList<>();
			List<Double> fullTServer = new LinkedList<>();
			for(int k=0;k<4;k++){
				for(double j=Math.ceil(0.29*TTotal.get(k).size());j<Math.floor(0.86*TTotal.get(k).size());j++){
					fullTTotal.add(TTotal.get(k).get((int) j));
					fullTQueue.add(TQueue.get(k).get((int) j));
					fullTServer.add(TServer.get(k).get((int) j));
				}
			}
			
			double ttotal_sum=0.0;
			double tqueue_sum=0.0;
			double tserver_sum=0.0;
				for(double j=0;j<fullTTotal.size();j++){
					ttotal_sum+=fullTTotal.get((int) j);
					tqueue_sum+=fullTQueue.get((int) j);
					tserver_sum+=fullTServer.get((int) j);
				}
			
			double total_num = fullTTotal.size();
			
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
			
			double ttotal_diff=0.0;
			double tqueue_diff=0.0;
			double tserver_diff=0.0;
			double sample_size=fullTTotal.size();
			for(double j=0;j<sample_size;j++){
				ttotal_diff+=(fullTTotal.get((int) j)-ttotal_avg)*(fullTTotal.get((int) j)-ttotal_avg);
				tqueue_diff+=(fullTQueue.get((int) j)-tqueue_avg)*(fullTQueue.get((int) j)-tqueue_avg);
				tserver_diff+=(fullTServer.get((int) j)-tserver_avg)*(fullTServer.get((int) j)-tserver_avg);
			}
			ttotal_std=Math.sqrt(ttotal_diff/(sample_size-1.0));
			tqueue_std=Math.sqrt(tqueue_diff/(sample_size-1.0));
			tserver_std=Math.sqrt(tserver_diff/(sample_size-1.0));
			
			System.out.println("T Total std: "+ttotal_std);
			stdTTotalwriter.write(seq.get(i)+","+ttotal_std+"\n");
			System.out.println("T Queue std: "+tqueue_std);
			stdTQueuewriter.write(seq.get(i)+","+tqueue_std+"\n");
			System.out.println("T Server std: "+tserver_std);
			stdTServerwriter.write(seq.get(i)+","+tserver_std+"\n");
			System.out.println("########################");
			
			
			double ttotal_fiftyp = 0.0;
			double ttotal_ninetyp = 0.0;
			double ttotal_ninetyninep = 0.0;
			
			double tqueue_fiftyp = 0.0;
			double tqueue_ninetyp = 0.0;
			double tqueue_ninetyninep = 0.0;
			
			double tserver_fiftyp = 0.0;
			double tserver_ninetyp = 0.0;
			double tserver_ninetyninep = 0.0;
			
				Collections.sort(fullTTotal);
				Collections.sort(fullTQueue);
				Collections.sort(fullTServer);
				
				ttotal_fiftyp=fullTTotal.get((int) Math.ceil(0.5*fullTTotal.size()));
				ttotal_ninetyp=fullTTotal.get((int) Math.ceil(0.9*fullTTotal.size()));
				ttotal_ninetyninep=fullTTotal.get((int) Math.ceil(0.99*fullTTotal.size()));
				
				tqueue_fiftyp=fullTQueue.get((int) Math.ceil(0.5*fullTQueue.size()));
				tqueue_ninetyp=fullTQueue.get((int) Math.ceil(0.9*fullTQueue.size()));
				tqueue_ninetyninep=fullTQueue.get((int) Math.ceil(0.99*fullTQueue.size()));
				
				tserver_fiftyp=fullTServer.get((int) Math.ceil(0.5*fullTServer.size()));
				tserver_ninetyp=fullTServer.get((int) Math.ceil(0.9*fullTServer.size()));
				tserver_ninetyninep=fullTServer.get((int) Math.ceil(0.99*fullTServer.size()));
			
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
