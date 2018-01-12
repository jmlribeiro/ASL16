package projectasl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class M31LogParser {

	public static void main(String[] args) throws IOException {
		
		List<Long> service_time = new LinkedList<>();
		
		BufferedReader br = new BufferedReader(new FileReader("M23/log_7_7_4_10.log"));
		//FileWriter writer = new FileWriter("M31/trace.csv"); 
		int count=0;
		String s=br.readLine();
		//writer.write(s+"\n");
		//long min=100000000;
		while(true){
			s = br.readLine();
			if(s == null)
				break;
			count++;
			String[] split_s=s.split(", ");
				
				//service_time.add(Long.parseLong(split_s[3]));
/*				if(Long.parseLong(split_s[3])<min){
					min=Long.parseLong(split_s[3]);
				}*/
			if(split_s[0].equals("get")){
				service_time.add(Long.parseLong(split_s[5])-Long.parseLong(split_s[4]));
				//writer.write(s+"\n");
			}
		}
		
		long sum=0;
		
		System.out.println(service_time.size());
		List<Long> chopped_service_time = new LinkedList<>();
		for(int i=(int) Math.ceil(0.25*service_time.size()); i<Math.floor(0.75*service_time.size());i++){
			sum+=service_time.get(i);
			chopped_service_time.add(service_time.get(i));
			if(i%10000==0){
				System.out.println(i);
			}
		}
		
		double avg=sum/(1000000*(Math.floor(0.75*service_time.size())-Math.ceil(0.25*service_time.size())));
		System.out.println(sum/(1000000*(Math.floor(0.75*service_time.size())-Math.ceil(0.25*service_time.size()))));
		Collections.sort(chopped_service_time);
		//System.out.println(min/1000000000.0);
		System.out.println(chopped_service_time.get((int) Math.ceil(0.5*chopped_service_time.size()))/1000000000.0);
		System.out.println(chopped_service_time.get((int) Math.ceil(0.9*chopped_service_time.size()))/1000000000.0);
		
		double sq_diff=0;
		for(int i=0;i<chopped_service_time.size();i++){
			sq_diff+=(chopped_service_time.get(i)-avg)/1000000.0*(chopped_service_time.get(i)-avg)/1000000000.0;
		}
		
		System.out.println(Math.sqrt(sq_diff/(chopped_service_time.size()-1.0)));
		
		br.close();
	}

}
