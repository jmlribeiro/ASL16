package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MemaslapParser {

	public static void main(String[] args) throws IOException{
		
		List<Double> avgTpsList = new LinkedList<>();
		List<Double> avgStdList = new LinkedList<>();
		List<Double> avgRepStdList = new LinkedList<>();
		
		List<Double> avgRtList = new LinkedList<>();
		List<Double> avgRtStdList = new LinkedList<>();
		
		List<Integer> seq = new LinkedList<>();
		
		int num_threads = 10;

		seq.add(10);
		seq.add(30);
		//seq.add(34);
		//seq.add(36);
		//seq.add(38);
		//seq.add(40);
		//seq.add(42);
		//seq.add(44);
		//seq.add(46);
		//seq.add(48);
		//seq.add(4"+num_threads+");
		seq.add(50);
		//seq.add(60);
		seq.add(70);
		//seq.add(90);
		
		
		/*seq.add(42);
		seq.add(44);
		seq.add(46);
		seq.add(4"+num_threads+");
		seq.add(50);
		seq.add(90);*/
		
		for(int i=0;i<4;i++){
			
			double avg_tps=0.0;
			double avg_std=0.0;
			double avg_rt=0.0;
			double avg_rt_std=0.0;
			
			List<Double> repClientAvgTps = new LinkedList<>();
			
			for(int rep=1;rep<=4;rep++){
				
				
				double rep_avg_tps=0.0;
				double rep_avg_std=0.0;
				double rep_avg_rt=0.0;
				double rep_avg_rt_std=0.0;
				
				for(int client=1;client<=5;client++){
					List<Integer> tpsList = new LinkedList<>();
					List<Integer> rtList = new LinkedList<>();
					List<Double> rtStdList = new LinkedList<>();
					
					BufferedReader br = new BufferedReader(new FileReader("M21/m2_1/log_"+client+"_"+num_threads+"_"+seq.get(i)+"_"+rep+"_M3"));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Total Statistics") && count>60 && count<=120)
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
					
					double rt_sum=0.0;
					for(int j=0;j<rtList.size();j++){
						rt_sum+=rtList.get(j);
					}
					
					double rt_std_sum=0.0;
					for(int j=0;j<rtStdList.size();j++){
						rt_std_sum+=rtStdList.get(j);
					}
					
					double avg_client_tps=tps_sum/60.0;
					rep_avg_tps+=avg_client_tps;
					
					double avg_client_rt = rt_sum/60.0;
					rep_avg_rt+=0.2*avg_client_rt;
					
					double avg_client_rt_std = rt_std_sum/60.0;
					rep_avg_rt_std+=0.2*0.2*avg_client_rt_std*avg_client_rt_std;
					
					double sq_diff_sum=0.0;
					for(int j=0;j<tpsList.size();j++){
						sq_diff_sum+=(tpsList.get(j)-avg_client_tps)*(tpsList.get(j)-avg_client_tps);
					}
					
					double client_std=Math.sqrt(sq_diff_sum/59.0);
					rep_avg_std+=client_std*client_std;
					
				}
				avg_tps+=rep_avg_tps*0.25;
				avg_rt+=rep_avg_rt*0.25;
				avg_rt_std+=Math.sqrt(rep_avg_rt_std)*0.25;
				repClientAvgTps.add(rep_avg_tps);
				avg_std+=Math.sqrt(rep_avg_std)*0.25;
			
			}
			
			double rep_std=0.0;
			for(int j=0;j<repClientAvgTps.size();j++){
				rep_std+=0.33*(repClientAvgTps.get(j)-avg_tps)*(repClientAvgTps.get(j)-avg_tps);
			}
			
			
			avgRepStdList.add(Math.sqrt(rep_std));
			avgTpsList.add(avg_tps);
			avgStdList.add(avg_std);
			
			avgRtList.add(avg_rt);
			avgRtStdList.add(avg_rt_std);
		
		}
		
		System.out.println("##### TPS #####");
		
		FileWriter writer = new FileWriter("M21/m2_1/avg_tps_"+num_threads+".csv"); 
		writer.write("nr. clients,Avg. Agg. TPS (ops/sec)\n");
		for(int i=0;i<4;i++) {
			System.out.println(avgTpsList.get(i));
			writer.write(5*seq.get(i)+","+avgTpsList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### TPS STD #####");
		
		writer = new FileWriter("M21/m2_1/avg_std_"+num_threads+".csv");
		writer.write("nr. clients,STDev (ops/sec)\n");
		for(int i=0;i<4;i++) {
			System.out.println(avgStdList.get(i));
			writer.write(5*seq.get(i)+","+avgStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### TPS REP STD #####");
		
		writer = new FileWriter("M21/m2_1/avg_rep_std_"+num_threads+".csv"); 
		writer.write("nr. clients,STDev Repetitions (ops/sec)\n");
		for(int i=0;i<4;i++) {
			System.out.println(avgRepStdList.get(i));
			writer.write(5*seq.get(i)+","+avgRepStdList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### AVG RT #####");
		
		writer = new FileWriter("M21/m2_1/avg_rt_"+num_threads+".csv"); 
		writer.write("nr. clients,Avg. Resp. Time (us)\n");
		for(int i=0;i<4;i++) {
			System.out.println(avgRtList.get(i));
			writer.write(5*seq.get(i)+","+avgRtList.get(i)+"\n");
		}
		writer.close();
		
		System.out.println("##### RT STD #####");
		
		writer = new FileWriter("M21/m2_1/avg_rt_std_"+num_threads+".csv"); 
		writer.write("nr. clients,STDev Resp. Time (us)\n");
		for(int i=0;i<4;i++) {
			System.out.println(avgRtStdList.get(i));
			writer.write(5*seq.get(i)+","+avgRtStdList.get(i)+"\n");
		}
		writer.close();
		
		
	}
	
}
