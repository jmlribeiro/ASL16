package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class M23MemaslapParser {

	public static void main(String[] args) throws IOException{
		
		List<Double> avgGetTpsList = new LinkedList<>();
		List<Double> avgGetStdList = new LinkedList<>();
		List<Double> avgGetRepStdList = new LinkedList<>();
		
		List<Double> avgGetRtList = new LinkedList<>();
		List<Double> avgGetRtStdList = new LinkedList<>();
		
		List<Integer> seq = new LinkedList<>();
		
		
		int num_servers=7;
		int write_prop=1;
		int reps=4;
		
		seq.add(1);
		seq.add(num_servers);
		
		for(int i=0;i<2;i++){
			
			double avg_tps=0.0;
			double avg_std=0.0;
			double avg_rt=0.0;
			double avg_rt_std=0.0;
			
			List<Double> repClientAvgTps = new LinkedList<>();
			
			for(int rep=1;rep<=reps;rep++){
				
				
				double rep_avg_tps=0.0;
				double rep_avg_std=0.0;
				double rep_avg_rt=0.0;
				double rep_avg_rt_std=0.0;
				
				for(int client=1;client<=3;client++){
					List<Integer> tpsList = new LinkedList<>();
					List<Integer> rtList = new LinkedList<>();
					List<Double> rtStdList = new LinkedList<>();
					BufferedReader br = new BufferedReader(new FileReader("M23/m2_3_"+num_servers+"/log_"+client+"_"+num_servers+"_"+seq.get(i)+"_"+rep+"_"+write_prop));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Get Statistics") && count>30 && count<=90)
						{
							br.readLine();
							s=br.readLine();
							int placeholder=31;
							char c = s.charAt(placeholder);
							String temp = "";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							tpsList.add(Integer.parseInt(temp));
							
							placeholder=86;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							rtList.add(Integer.parseInt(temp));
							
							placeholder=97;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							if(!temp.equals("-nan")){
								rtStdList.add(Double.parseDouble(temp));
							}
							
							count++;
						}else if(s.contains("Get Statistics")){
							count++;
						}
					}
					
					br.close();
					
					double tps_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						tps_sum+=tpsList.get(j);
					}
					
					double rt_sum=0.0;
					for(int j=0;j<rtList.size();j++){
						rt_sum+=rtList.get(j);
					}
					
					double rt_std_sum=0.0;
					for(int j=0;j<rtStdList.size();j++){
						rt_std_sum+=rtStdList.get(j);
					}
					
					double avg_client_rt = rt_sum/60.0;
					rep_avg_rt+=0.33*avg_client_rt;
					
					double avg_client_rt_std = rt_std_sum/60.0;
					rep_avg_rt_std+=0.33*avg_client_rt_std*0.33*avg_client_rt_std;
					
					double avg_client_tps=tps_sum/60.0;
					rep_avg_tps+=avg_client_tps;
					
					double sq_diff_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						sq_diff_sum+=(tpsList.get(j)-avg_client_tps)*(tpsList.get(j)-avg_client_tps);
					}
					
					double client_std=Math.sqrt(sq_diff_sum/59.0);
					rep_avg_std+=client_std*client_std;
					
				}
				avg_tps+=rep_avg_tps/(reps);
				avg_rt+=rep_avg_rt/(reps);
				avg_rt_std+=Math.sqrt(rep_avg_rt_std)/(reps);
				repClientAvgTps.add(rep_avg_tps);
				avg_std+=Math.sqrt(rep_avg_std)/(reps);
			
			}
			
			double rep_std=0.0;
			for(int j=0;j<repClientAvgTps.size();j++){
				rep_std+=(repClientAvgTps.get(j)-avg_tps)*(repClientAvgTps.get(j)-avg_tps)/(reps-1.0);
			}
			
			
			avgGetRepStdList.add(Math.sqrt(rep_std));
			avgGetTpsList.add(avg_tps);
			avgGetStdList.add(avg_std);
			avgGetRtList.add(avg_rt);
			avgGetRtStdList.add(avg_rt_std);
		
		}
		
		System.out.println("##### Get TPS #####");
		
		FileWriter writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_tps_"+num_servers+"_get_"+write_prop+".csv"); 
		writer.write("rep. factor,Get Avg. Agg. TPS (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgGetTpsList.get(i));
			writer.write(seq.get(i)+","+avgGetTpsList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Get STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_std_"+num_servers+"_get_"+write_prop+".csv");
		writer.write("rep. factor,Get STDev (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgGetStdList.get(i));
			writer.write(seq.get(i)+","+avgGetStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Get REP STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rep_std_"+num_servers+"_get_"+write_prop+".csv"); 
		writer.write("rep. factor,Get STDev Repetitions (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgGetRepStdList.get(i));
			writer.write(seq.get(i)+","+avgGetRepStdList.get(i)+"\n");
		}
		writer.close();
		
		
		System.out.println("##### Get AVG RT #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_"+num_servers+"_get_"+write_prop+".csv"); 
		writer.write("rep. factor,Get Avg. Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgGetRtList.get(i));
			writer.write(seq.get(i)+","+avgGetRtList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Get RT STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_std_"+num_servers+"_get_"+write_prop+".csv"); 
		writer.write("rep. factor,Get STDev Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgGetRtStdList.get(i));
			writer.write(seq.get(i)+","+avgGetRtStdList.get(i)+"\n");
		}
		writer.close();
		
		
		
		List<Double> avgSetTpsList = new LinkedList<>();
		List<Double> avgSetStdList = new LinkedList<>();
		List<Double> avgSetRepStdList = new LinkedList<>();
		List<Double> avgSetRtList = new LinkedList<>();
		List<Double> avgSetRtStdList = new LinkedList<>();
		
		for(int i=0;i<2;i++){
			
			double avg_tps=0.0;
			double avg_std=0.0;
			double avg_rt=0.0;
			double avg_rt_std=0.0;
			
			List<Double> repClientAvgTps = new LinkedList<>();
			
			for(int rep=1;rep<=reps;rep++){
				
				double rep_avg_tps=0.0;
				double rep_avg_std=0.0;
				double rep_avg_rt=0.0;
				double rep_avg_rt_std=0.0;
				
				for(int client=1;client<=3;client++){
					List<Integer> tpsList = new LinkedList<>();
					List<Integer> rtList = new LinkedList<>();
					List<Double> rtStdList = new LinkedList<>();
					BufferedReader br = new BufferedReader(new FileReader("M23/m2_3_"+num_servers+"/log_"+client+"_"+num_servers+"_"+seq.get(i)+"_"+rep+"_"+write_prop));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Set Statistics") && count>30 && count<=90)
						{
							br.readLine();
							s=br.readLine();
							int placeholder=31;
							char c = s.charAt(placeholder);
							String temp = "";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							tpsList.add(Integer.parseInt(temp));
							
							
							placeholder=86;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							rtList.add(Integer.parseInt(temp));
							
							placeholder=97;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							if(!temp.equals("-nan")){
								rtStdList.add(Double.parseDouble(temp));
							}
							
							
							count++;
						}else if(s.contains("Set Statistics")){
							count++;
						}
					}
					
					br.close();
					
					double tps_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						tps_sum+=tpsList.get(j);
					}
					
					double avg_client_tps=tps_sum/60.0;
					rep_avg_tps+=avg_client_tps;
					
					double rt_sum=0.0;
					for(int j=0;j<rtList.size();j++){
						rt_sum+=rtList.get(j);
					}
					
					double rt_std_sum=0.0;
					for(int j=0;j<rtStdList.size();j++){
						rt_std_sum+=rtStdList.get(j);
					}
					
					double avg_client_rt = rt_sum/60.0;
					rep_avg_rt+=0.33*avg_client_rt;
					
					double avg_client_rt_std = rt_std_sum/60.0;
					rep_avg_rt_std+=0.33*avg_client_rt_std*0.33*avg_client_rt_std;
					
					double sq_diff_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						sq_diff_sum+=(tpsList.get(j)-avg_client_tps)*(tpsList.get(j)-avg_client_tps);
					}
					
					double client_std=Math.sqrt(sq_diff_sum/59.0);
					rep_avg_std+=client_std*client_std;
					
				}
				avg_tps+=rep_avg_tps/(reps);
				avg_rt+=rep_avg_rt/(reps);
				avg_rt_std+=Math.sqrt(rep_avg_rt_std)/(reps);
				repClientAvgTps.add(rep_avg_tps);
				avg_std+=Math.sqrt(rep_avg_std)/(reps);
			
			}
			
			double rep_std=0.0;
			for(int j=0;j<repClientAvgTps.size();j++){
				rep_std+=(repClientAvgTps.get(j)-avg_tps)*(repClientAvgTps.get(j)-avg_tps)/(reps-1.0);
			}
			
			
			avgSetRepStdList.add(Math.sqrt(rep_std));
			avgSetTpsList.add(avg_tps);
			avgSetStdList.add(avg_std);
			avgSetRtList.add(avg_rt);
			avgSetRtStdList.add(avg_rt_std);
		
		}
		
		System.out.println("##### Set TPS #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_tps_"+num_servers+"_set_"+write_prop+".csv"); 
		writer.write("rep. factor,Set Avg. Agg. TPS (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgSetTpsList.get(i));
			writer.write(seq.get(i)+","+avgSetTpsList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Set STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_std_"+num_servers+"_set_"+write_prop+".csv");
		writer.write("rep. factor,Set STDev (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgSetStdList.get(i));
			writer.write(seq.get(i)+","+avgSetStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Set REP STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rep_std_"+num_servers+"_set_"+write_prop+".csv"); 
		writer.write("rep. factor,Set STDev Repetitions (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgSetRepStdList.get(i));
			writer.write(seq.get(i)+","+avgSetRepStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Set AVG RT #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_"+num_servers+"_set_"+write_prop+".csv"); 
		writer.write("rep. factor,Set Avg. Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgSetRtList.get(i));
			writer.write(seq.get(i)+","+avgSetRtList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Set RT STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_std_"+num_servers+"_set_"+write_prop+".csv"); 
		writer.write("rep. factor,Set STDev Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgSetRtStdList.get(i));
			writer.write(seq.get(i)+","+avgSetRtStdList.get(i)+"\n");
		}
		writer.close();
		
		List<Double> avgTpsList = new LinkedList<>();
		List<Double> avgStdList = new LinkedList<>();
		List<Double> avgRepStdList = new LinkedList<>();
		List<Double> avgRtList = new LinkedList<>();
		List<Double> avgRtStdList = new LinkedList<>();
		
		for(int i=0;i<2;i++){
			
			
			
			double avg_tps=0.0;
			double avg_std=0.0;
			double avg_rt=0.0;
			double avg_rt_std=0.0;
			
			List<Double> repClientAvgTps = new LinkedList<>();
			
			for(int rep=1;rep<=reps;rep++){
				
				
				double rep_avg_tps=0.0;
				double rep_avg_std=0.0;
				double rep_avg_rt=0.0;
				double rep_avg_rt_std=0.0;
				
				for(int client=1;client<=3;client++){
					List<Integer> tpsList = new LinkedList<>();
					List<Integer> rtList = new LinkedList<>();
					List<Double> rtStdList = new LinkedList<>();
					BufferedReader br = new BufferedReader(new FileReader("M23/m2_3_"+num_servers+"/log_"+client+"_"+num_servers+"_"+seq.get(i)+"_"+rep+"_"+write_prop));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Total Statistics") && count>30 && count<=90)
						{
							br.readLine();
							s=br.readLine();
							int placeholder=31;
							char c = s.charAt(placeholder);
							String temp = "";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							tpsList.add(Integer.parseInt(temp));
							
							
							placeholder=86;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							rtList.add(Integer.parseInt(temp));
							
							placeholder=97;
							c = s.charAt(placeholder);
							temp="";
							while(c!=' '){
								temp+=c;
								placeholder++;
								c=s.charAt(placeholder);
							}
							if(!temp.equals("-nan")){
								rtStdList.add(Double.parseDouble(temp));
							}
							
							
							count++;
						}else if(s.contains("Total Statistics")){
							count++;
						}
					}
					
					br.close();
					
					double tps_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						tps_sum+=tpsList.get(j);
					}
					
					double avg_client_tps=tps_sum/60.0;
					rep_avg_tps+=avg_client_tps;
					
					double rt_sum=0.0;
					for(int j=0;j<rtList.size();j++){
						rt_sum+=rtList.get(j);
					}
					
					double rt_std_sum=0.0;
					for(int j=0;j<rtStdList.size();j++){
						rt_std_sum+=rtStdList.get(j);
					}
					
					double avg_client_rt = rt_sum/60.0;
					rep_avg_rt+=0.33*avg_client_rt;
					
					double avg_client_rt_std = rt_std_sum/60.0;
					rep_avg_rt_std+=0.33*avg_client_rt_std;
					
					double sq_diff_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						sq_diff_sum+=(tpsList.get(j)-avg_client_tps)*(tpsList.get(j)-avg_client_tps);
					}
					
					double client_std=Math.sqrt(sq_diff_sum/59.0);
					rep_avg_std+=client_std*client_std;
					
				}
				System.out.println("TPS "+seq.get(i)+" "+rep+": "+rep_avg_tps);
				System.out.println("RT "+seq.get(i)+" "+rep+": "+rep_avg_rt);
				avg_tps+=rep_avg_tps/(reps);
				avg_rt+=rep_avg_rt/(reps);
				avg_rt_std+=rep_avg_rt_std/(reps);
				repClientAvgTps.add(rep_avg_tps);
				avg_std+=Math.sqrt(rep_avg_std)/(reps);
			
			}
			
			double rep_std=0.0;
			for(int j=0;j<repClientAvgTps.size();j++){
				rep_std+=(repClientAvgTps.get(j)-avg_tps)*(repClientAvgTps.get(j)-avg_tps)/(reps-1.0);
			}
			
			
			avgRepStdList.add(Math.sqrt(rep_std));
			avgTpsList.add(avg_tps);
			avgStdList.add(avg_std);
			avgRtList.add(avg_rt);
			avgRtStdList.add(avg_rt_std);
		
		}
		
		System.out.println("##### Total TPS #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_tps_"+num_servers+"_total_"+write_prop+".csv"); 
		writer.write("rep. factor,Total Avg. Agg. TPS (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgTpsList.get(i));
			writer.write(seq.get(i)+","+avgTpsList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Total STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_std_"+num_servers+"_total_"+write_prop+".csv");
		writer.write("rep. factor,Total STDev (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgStdList.get(i));
			writer.write(seq.get(i)+","+avgStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Total REP STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rep_std_"+num_servers+"_total_"+write_prop+".csv"); 
		writer.write("rep. factor,STDev Repetitions (ops/sec)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgRepStdList.get(i));
			writer.write(seq.get(i)+","+avgRepStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Total AVG RT #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_"+num_servers+"_total_"+write_prop+".csv"); 
		writer.write("rep. factor,Total Avg. Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgRtList.get(i));
			writer.write(seq.get(i)+","+avgRtList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### Total RT STD #####");
		
		writer = new FileWriter("M23/m2_3_"+num_servers+"/avg_rt_std_"+num_servers+"_total_"+write_prop+".csv"); 
		writer.write("rep. factor,Total STDev Resp. Time (us)\n");
		for(int i=0;i<2;i++) {
			System.out.println(avgRtStdList.get(i));
			writer.write(seq.get(i)+","+avgRtStdList.get(i)+"\n");
		}
		writer.close();
		
	}
	
	
	
}
