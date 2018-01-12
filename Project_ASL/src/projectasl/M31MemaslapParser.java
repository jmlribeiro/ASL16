package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class M31MemaslapParser {
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader("M31/rev_trace_1"));
		BufferedReader br2 = new BufferedReader(new FileReader("M31/rev_trace_2"));
		BufferedReader br3 = new BufferedReader(new FileReader("M31/rev_trace_3"));
		
		int tps_sum=0;
		double rt_sum=0;
		double std_sum=0;
		int count=0;
		int max=0;
		while(true)
		{
			String s1 = br1.readLine();
			String s2 = br2.readLine();
			String s3 = br3.readLine();
			if(s1 == null)
				break;
			
			if(s1.contains("Total Statistics")){
				count++;
				if(count>=300 & count<3900){
					br1.readLine();
					br2.readLine();
					br3.readLine();
					s1=br1.readLine();
					s2=br2.readLine();
					s3=br3.readLine();
					String temp1 = "";
					String temp2 = "";
					String temp3 = "";
					int placeholder = 31;
					char c1 = s1.charAt(placeholder);
					while(c1!=' '){
						temp1+=c1;
						placeholder++;
						c1=s1.charAt(placeholder);
					}
					placeholder = 31;
					char c2 = s2.charAt(placeholder);
					while(c2!=' '){
						temp2+=c2;
						placeholder++;
						c2=s2.charAt(placeholder);
					}
					placeholder = 31;
					char c3 = s3.charAt(placeholder);
					while(c3!=' '){
						temp3+=c3;
						placeholder++;
						c3=s3.charAt(placeholder);
					}
					
					
					int tps1 = Integer.parseInt(temp1);
					int tps2 = Integer.parseInt(temp2);
					int tps3 = Integer.parseInt(temp3);
					
					tps_sum+=tps1+tps2+tps3;
					
					if(tps1+tps2+tps3>max){
						max=tps1+tps2+tps3;
					}
					
					placeholder=86;
					c1 = s1.charAt(placeholder);
					temp1="";
					while(c1!=' '){
						temp1+=c1;
						placeholder++;
						c1=s1.charAt(placeholder);
					}
					placeholder=86;
					c2 = s2.charAt(placeholder);
					temp2="";
					while(c2!=' '){
						temp2+=c2;
						placeholder++;
						c2=s2.charAt(placeholder);
					}
					placeholder=86;
					c3 = s3.charAt(placeholder);
					temp3="";
					while(c3!=' '){
						temp3+=c3;
						placeholder++;
						c3=s3.charAt(placeholder);
					}
					
					int rt1 = Integer.parseInt(temp1);
					int rt2 = Integer.parseInt(temp2);
					int rt3 = Integer.parseInt(temp3);
					rt_sum+=(rt1+rt2+rt3)/3.0;
					
					
					placeholder=97;
					c1 = s1.charAt(placeholder);
					temp1="";
					while(c1!=' '){
						temp1+=c1;
						placeholder++;
						c1=s1.charAt(placeholder);
					}
					
					placeholder=97;
					c2 = s2.charAt(placeholder);
					temp2="";
					while(c2!=' '){
						temp2+=c2;
						placeholder++;
						c2=s2.charAt(placeholder);
					}
					
					placeholder=97;
					c3= s3.charAt(placeholder);
					temp3="";
					while(c3!=' '){
						temp3+=c3;
						placeholder++;
						c3=s3.charAt(placeholder);
					}
					
					double std1 = Double.parseDouble(temp1);
					double std2 = Double.parseDouble(temp2);
					double std3 = Double.parseDouble(temp3);
					std_sum+=(std1+std2+std3)/3.0;
				}
			}
		}
		
		System.out.println(tps_sum/3600.0);
		System.out.println(max);
		
		double rho = (tps_sum/3600.0)/max;
		System.out.println(rho);
		
		System.out.println(rt_sum/3600.0);
		System.out.println(std_sum/3600.0);
		
		br1.close();
		br2.close();
		br3.close();
	}
	
}
