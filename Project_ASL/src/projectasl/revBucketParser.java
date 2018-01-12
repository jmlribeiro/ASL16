package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class revBucketParser {

public static void main(String[] args) throws IOException{
		
		int num_threads=24;
	
		FileWriter fiftywriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/rev_bucket_50p_"+num_threads+".csv"); 
		fiftywriter.write("nr. clients,50 Percentile Resp. Time (us)\n");
		
		FileWriter ninetywriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/rev_bucket_90p_"+num_threads+".csv"); 
		ninetywriter.write("nr. clients,90 Percentile Resp.Time (us)\n");
		
		FileWriter ninetyninewriter = new FileWriter("Fine_Grain/Threads_"+num_threads+"/rev_bucket_99p_"+num_threads+".csv"); 
		ninetyninewriter.write("nr. clients,99 Percentile Resp. Time (us)\n");
		
		List<Integer> seq = new LinkedList<>();
		
		/*seq.add(10);
		seq.add(30);
		seq.add(40);
		seq.add(46);
		seq.add(50);
		seq.add(60);
		seq.add(70);*/
		
		seq.add(36);
		seq.add(38);
		seq.add(40);
		seq.add(42);
		seq.add(44);
		seq.add(46);
		seq.add(48);
		seq.add(50);
		//seq.add(60);
		//seq.add(70);
		//seq.add(90);
		
		for(int i=0;i<8;i++){
			
			double fiftyp = 0;
			double ninetyp = 0;
			double ninetyninep=0;
			
			int[] bucketList = new int[16];
			for(int j=0;j<16;j++){
				bucketList[j]=0;
			}
			
			for(int rep=1;rep<=4;rep++){

				for(int client=1;client<=5;client++){
					BufferedReader br = new BufferedReader(new FileReader("Fine_Grain/Threads_"+num_threads+"/m2_1/log_"+client+"_"+num_threads+"_"+seq.get(i)+"_"+rep+"_3m"));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Log2 Dist") & count==0){
							count++;
							for(int j=0;j<4;j++){
								s = br.readLine();
								String[] split_s=s.split("\\s+");
								//System.out.println(split_s.length);
								for(int k=2;k<split_s.length;k++){
									//System.out.println(split_s[k]);
									bucketList[4*j+k-2]+=Integer.parseInt(split_s[k]);
								}
								//System.out.println("########");
							}
						}else if(s.contains("Log2 Dist") & count==1){
							count++;
						}
					}
					br.close();
				}
				
			}
			
			int total_num=0;
			
			for(int j=0;j<bucketList.length;j++){
				total_num+=bucketList[j];
			}
			
			double partial_sum=0.0;
			boolean isSet = false;
			
			for(int j=0;j<bucketList.length;j++){
				if(partial_sum>=Math.ceil(0.5*total_num) && !isSet){
					fiftyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=bucketList[j];
			}
			
			partial_sum=0.0;
			isSet = false;
			
			for(int j=0;j<bucketList.length;j++){
				if(partial_sum>=Math.ceil(0.9*total_num) && !isSet){
					ninetyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=bucketList[j];
			}
			
			partial_sum=0.0;
			isSet = false;
			
			for(int j=0;j<bucketList.length;j++){
				if(partial_sum>=Math.ceil(0.99*total_num) && !isSet){
					ninetyninep=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=bucketList[j];
			}
			
			System.out.println("CLIENTS: "+5*seq.get(i));
			System.out.println("#########################");
			
			System.out.println("50th percentile: "+fiftyp);
			fiftywriter.write(5*seq.get(i)+","+fiftyp+"\n");
			System.out.println("90th percentile: "+ninetyp);
			ninetywriter.write(5*seq.get(i)+","+ninetyp+"\n");
			System.out.println("99th percentile: "+ninetyninep);
			ninetyninewriter.write(5*seq.get(i)+","+ninetyninep+"\n");
			
		}
		
		fiftywriter.close();
		ninetywriter.close();
		ninetyninewriter.close();
	}
	
}
