package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class M21 {
	
	public static void main(String[] args) throws IOException{
		
		List<Double> avgtpsList = new LinkedList<>();
		
		for(int i=2;i<=7;i++){
			double sum=0;
			List<Double> tpsList = new LinkedList<>();
			for(int rep=1;rep<=4;rep++){
				double rep_sum=0.0;
				for(int client=1;client<=5;client++){
					BufferedReader br = new BufferedReader(new FileReader("m2_1/log_"+client+"_"+10*i+"_"+rep+"_3m"));
					int count=0;
					while(true){
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Total Statistics") && count==1)
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
							rep_sum+=Double.parseDouble(temp);
							count++;
						}
						else if(s.contains("Total Statistics")){
							count++;
						}
						
					}
					br.close();
				}
				tpsList.add(rep_sum);
				sum+=rep_sum;
			}
			
			
			double avg_sum=sum*0.25;
			avgtpsList.add(avg_sum);
			System.out.println("#### Clients "+10*i);
			System.out.println("Avg. TPS "+avg_sum);
			double pre_std=0.0;
			for(double x: tpsList){
				pre_std+=(x-avg_sum)*(x-avg_sum);
			}
			double std=Math.sqrt(pre_std*0.25);
			System.out.println("Std "+std);
		}
		
		
		FileWriter writer = new FileWriter("avg_tps.txt"); 
		for(double x: avgtpsList) {
		  writer.write(x+"\n");
		}
		writer.close();
	}
}
