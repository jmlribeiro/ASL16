package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ThinkTime {
	
public static void main(String[] args) throws IOException {
		
		List<Long> think_time = new LinkedList<>();
		
		BufferedReader br = new BufferedReader(new FileReader("think-time/think_time_2m_5"));
		br.readLine();
		int count=0;
		while(true){
			String s = br.readLine();
			if(s == null)
				break;
			
			String[] split_s=s.split(", ");
			
			count++;
				
			String s2=br.readLine();
				
			String[] split_s2=s2.split(", ");
			
			if(Long.parseLong(split_s2[4])-Long.parseLong(split_s[9])>=0){
				think_time.add(Long.parseLong(split_s2[4])-Long.parseLong(split_s[9]));
			}
			//service_time.add(Long.parseLong(split_s[9])-Long.parseLong(split_s[6]));
		}
		
		long sum=0;
		
		System.out.println(think_time.size());
		
		List<Long> new_think_time = new LinkedList<>();
		
		for(int i=(int) Math.ceil(0.25*think_time.size()); i<Math.floor(0.75*think_time.size());i++){
			sum+=think_time.get(i);
			new_think_time.add(think_time.get(i));
			if(i%10000==0){
				System.out.println(i);
			}
		}
		
		
		double avg=sum/(1000000.0*(Math.floor(0.75*think_time.size())-Math.ceil(0.25*think_time.size())));
		System.out.println(avg);
		
		long sq_diff=0;
		for(int i=(int) Math.ceil(0.25*think_time.size()); i<Math.floor(0.75*think_time.size());i++){
			sq_diff+=((think_time.get(i)-avg)/1000000.0)*((think_time.get(i)-avg)/1000000.0);
			if(i%10000==0){
				System.out.println(i);
			}
		}
		
		double var = sq_diff/(new_think_time.size()-1.0);
		double std=Math.sqrt(var);
		System.out.println(std);
		
		Collections.sort(new_think_time);
		System.out.println(new_think_time.get((int) Math.ceil(0.25*new_think_time.size()))/1000000.0);
		System.out.println(new_think_time.get((int) Math.ceil(0.5*new_think_time.size()))/1000000.0);
		System.out.println(new_think_time.get((int) Math.ceil(0.75*new_think_time.size()))/1000000.0);
		System.out.println(new_think_time.get((int) Math.ceil(0.9*new_think_time.size()))/1000000.0);
		System.out.println(new_think_time.get((int) Math.ceil(0.95*new_think_time.size()))/1000000.0);
		
		br.close();
	}

}
