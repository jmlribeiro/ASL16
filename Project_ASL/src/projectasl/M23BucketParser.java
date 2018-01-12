package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class M23BucketParser {

	public static void main(String[] args) throws IOException{
		
		int num_servers=7;
		int write_prop=10;
		int reps=4;
		
		List<Integer> seq = new LinkedList<>();
		
		seq.add(1);
		seq.add(num_servers);
		
		FileWriter get_fiftywriter = new FileWriter("Rep4_105s_reps/rev_bucket_get_50p_"+num_servers+"_"+write_prop+".csv"); 
		get_fiftywriter.write("rep. factor,Get 50 Percentile Resp. Time (us)\n");
		
		FileWriter get_ninetywriter = new FileWriter("Rep4_105s_reps/rev_bucket_get_90p_"+num_servers+"_"+write_prop+".csv"); 
		get_ninetywriter.write("rep. factor,Get 90 Percentile Resp.Time (us)\n");
		
		FileWriter get_ninetyninewriter = new FileWriter("Rep4_105s_reps/rev_bucket_get_99p_"+num_servers+"_"+write_prop+".csv"); 
		get_ninetyninewriter.write("rep. factor,Get 99 Percentile Resp. Time (us)\n");
		
		FileWriter set_fiftywriter = new FileWriter("Rep4_105s_reps/rev_bucket_set_50p_"+num_servers+"_"+write_prop+".csv"); 
		set_fiftywriter.write("rep. factor,Set 50 Percentile Resp. Time (us)\n");
		
		FileWriter set_ninetywriter = new FileWriter("Rep4_105s_reps/rev_bucket_set_90p_"+num_servers+"_"+write_prop+".csv"); 
		set_ninetywriter.write("rep. factor,Set 90 Percentile Resp.Time (us)\n");
		
		FileWriter set_ninetyninewriter = new FileWriter("Rep4_105s_reps/rev_bucket_set_99p_"+num_servers+"_"+write_prop+".csv"); 
		set_ninetyninewriter.write("rep. factor,Set 99 Percentile Resp. Time (us)\n");
		
		for(int i=0;i<2;i++){

			double get_fiftyp = 0;
			double get_ninetyp = 0;
			double get_ninetyninep=0;

			double set_fiftyp=0;
			double set_ninetyp=0;
			double set_ninetyninep=0;

			int[] getBucketList = new int[16];
			for(int j=0;j<16;j++){
				getBucketList[j]=0;
			}

			int[] setBucketList = new int[16];
			for(int j=0;j<16;j++){
				setBucketList[j]=0;
			}

			for(int rep=1;rep<=reps;rep++){
				for(int client=1;client<=3;client++){
					BufferedReader br = new BufferedReader(new FileReader("Rep4_105s_reps/m2_3_"+num_servers+"/log_"+client+"_"+num_servers+"_"+seq.get(i)+"_"+rep+"_"+write_prop));
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
									getBucketList[4*j+k-2]+=Integer.parseInt(split_s[k]);
								}
								//System.out.println("########");
							}
						}else if(s.contains("Log2 Dist") & count==1){
							count++;
							for(int j=0;j<4;j++){
								s = br.readLine();
								String[] split_s=s.split("\\s+");
								//System.out.println(split_s.length);
								for(int k=2;k<split_s.length;k++){
									//System.out.println(split_s[k]);
									setBucketList[4*j+k-2]+=Integer.parseInt(split_s[k]);
								}
								//System.out.println("########");
							}
						}
					}
					br.close();
				}
			}

			int get_total_num=0;

			for(int j=0;j<getBucketList.length;j++){
				get_total_num+=getBucketList[j];
			}

			int set_total_num=0;

			for(int j=0;j<setBucketList.length;j++){
				set_total_num+=setBucketList[j];
			}

			double partial_sum=0.0;
			boolean isSet = false;

			for(int j=0;j<getBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.5*get_total_num) && !isSet){
					get_fiftyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=getBucketList[j];
			}

			partial_sum=0.0;
			isSet = false;

			for(int j=0;j<getBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.9*get_total_num) && !isSet){
					get_ninetyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=getBucketList[j];
			}

			partial_sum=0.0;
			isSet = false;

			for(int j=0;j<getBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.99*get_total_num) && !isSet){
					get_ninetyninep=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=getBucketList[j];
			}

			partial_sum=0.0;
			isSet=false;

			for(int j=0;j<setBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.5*set_total_num) && !isSet){
					set_fiftyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=setBucketList[j];
			}

			partial_sum=0.0;
			isSet = false;

			for(int j=0;j<setBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.9*set_total_num) && !isSet){
					set_ninetyp=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=setBucketList[j];
			}

			partial_sum=0.0;
			isSet = false;

			for(int j=0;j<setBucketList.length;j++){
				if(partial_sum>=Math.ceil(0.99*set_total_num) && !isSet){
					set_ninetyninep=Math.pow(2, 8+j);
					isSet=true;
				}
				partial_sum+=setBucketList[j];
			}

			System.out.println("Rep. factor: "+seq.get(i));
			System.out.println("##########GET############");

			System.out.println("50th percentile: "+get_fiftyp);
			get_fiftywriter.write(seq.get(i)+","+get_fiftyp+"\n");
			System.out.println("90th percentile: "+get_ninetyp);
			get_ninetywriter.write(seq.get(i)+","+get_ninetyp+"\n");
			System.out.println("99th percentile: "+get_ninetyninep);
			get_ninetyninewriter.write(seq.get(i)+","+get_ninetyninep+"\n");

			System.out.println("##########SET############");

			System.out.println("50th percentile: "+set_fiftyp);
			set_fiftywriter.write(seq.get(i)+","+set_fiftyp+"\n");
			System.out.println("90th percentile: "+set_ninetyp);
			set_ninetywriter.write(seq.get(i)+","+set_ninetyp+"\n");
			System.out.println("99th percentile: "+set_ninetyninep);
			set_ninetyninewriter.write(seq.get(i)+","+set_ninetyninep+"\n");

		}

		get_fiftywriter.close();
		get_ninetywriter.close();
		get_ninetyninewriter.close();
		set_fiftywriter.close();
		set_ninetywriter.close();
		set_ninetyninewriter.close();
	}

}
