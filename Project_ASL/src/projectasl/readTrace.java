package projectasl;

import java.io.*;
import java.util.*;



public class readTrace {

	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("M31/rev_trace_3"));
		
		List<String> tps_list = new LinkedList<>();
		List<String> avg_list = new LinkedList<>();
		List<String> std_list = new LinkedList<>();
		
		while(true)
		{
			String s = br.readLine();
			if(s == null)
				break;
			
			if(s.contains("Total Statistics") & tps_list.size()!=4200)
			{
				br.readLine();
				s=br.readLine();
				String temp = "";
				int placeholder = 31;
				char c = s.charAt(placeholder);
				while(c!=' '){
					temp+=c;
					placeholder++;
					c=s.charAt(placeholder);
				}
				tps_list.add(temp);
				
				placeholder=86;
				c = s.charAt(placeholder);
				temp="";
				while(c!=' '){
					temp+=c;
					placeholder++;
					c=s.charAt(placeholder);
				}
				avg_list.add(temp);
				
				placeholder=97;
				c = s.charAt(placeholder);
				temp="";
				while(c!=' '){
					temp+=c;
					placeholder++;
					c=s.charAt(placeholder);
				}
				std_list.add(temp);
				
				System.out.println(tps_list.size());
				
				
			}
		}
		FileWriter writer = new FileWriter("proc_tps_3.txt"); 
		for(int i=0; i<4200;i++) {
			writer.write(tps_list.get(i)+"\n");
		}
		writer.close();
		
		writer = new FileWriter("proc_avg_3.txt"); 
		for(int i=0; i<4200;i++) {
			writer.write(avg_list.get(i)+"\n");
		}
		writer.close();
		
		writer = new FileWriter("proc_std_3.txt"); 
		for(int i=0; i<4200;i++) {
			writer.write(std_list.get(i)+"\n");
		}
		writer.close();
		
		writer = new FileWriter("proc_avg_std_3.txt"); 
		for(int i=0;i<4200;i++) {
		  writer.write(avg_list.get(i)+","+std_list.get(i)+"\n");
		}
		writer.close();
		
		br.close();
	}

}
