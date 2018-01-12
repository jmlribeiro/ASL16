package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class M31Busy {
public static void main(String[] args) throws IOException {
		
		List<Long> busy = new LinkedList<>();
		
		BufferedReader br = new BufferedReader(new FileReader("M32/log_7_7_1_10.log"));
		br.readLine();
		int count=0;
		while(true){
			String s = br.readLine();
			if(s == null)
				break;
			
			String[] split_s=s.split(", ");
			
			count++;
			if(split_s[0].equals("get")){
				System.out.println(count);
				
				busy.add(Long.parseLong(split_s[11]));
				//service_time.add(Long.parseLong(split_s[9])-Long.parseLong(split_s[6]));
			}
		}
		
		long sum=0;
		
		System.out.println(busy.size());
		
		for(int i=(int) Math.ceil(0.25*busy.size()); i<Math.floor(0.75*busy.size());i++){
			sum+=busy.get(i)+1;
			
			if(i%10000==0){
				System.out.println(i);
			}
		}
		
		System.out.println(sum/(Math.floor(0.75*busy.size())-Math.ceil(0.25*busy.size())));
		
		br.close();
	}
}
