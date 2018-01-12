package projectasl;

import java.io.*;
import java.util.*;

public class Clipper {
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("local_log_1h_32t_3_tps.txt"));
		FileWriter writer1 = new FileWriter("local_tps3.csv");
		FileWriter writer2 = new FileWriter("local_tps3.txt");
		
		writer1.write("Time (sec), TPS (ops/sec)"+"\n");
		
		for(int i=0;i<600;i++){
			br.readLine();
		}
		
		for(int i=600;i<3300;i++){
			System.out.println(i);
			String s = br.readLine(); 
			writer1.write((i-599)+","+s+"\n");
			writer2.write(s+"\n");
		}
		
		br.close();
		writer1.close();
		writer2.close();
		
	}
}
