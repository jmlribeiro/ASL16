package projectasl;

import java.io.*;

public class Merger {
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader("proc_tps_1.txt"));
		BufferedReader br2 = new BufferedReader(new FileReader("proc_tps_2.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("proc_tps_3.txt"));
		FileWriter writer1 = new FileWriter("trace_tps.csv");
		FileWriter writer2 = new FileWriter("trace_tps.txt");
		
		writer1.write("Time (sec), TPS(ops/sec)"+"\n");
		
		/*for(int i=0;i<500;i++){
			br1.readLine();
			br2.readLine();
			br3.readLine();
		}*/
		
		for(int i=0;i<4200;i++){
			System.out.println((i+1));
			String s1=br1.readLine();
			String s2=br2.readLine();
			String s3=br3.readLine();
			
			/*String[] split1 = s1.split(",");
			String[] split2 = s2.split(",");
			String[] split3 = s3.split(",");*/
			
			double tps1=Double.parseDouble(s1);
			double tps2=Double.parseDouble(s2);
			double tps3=Double.parseDouble(s3);
			
			/*double std1=Double.parseDouble(split1[1]);
			double std2=Double.parseDouble(split2[1]);
			double std3=Double.parseDouble(split3[1]);*/
			
			double tps = (tps1+tps2+tps3);
			//double std= (std1+std2+std3)/3;
			
			writer1.write((i+1)+","+tps+"\n");
			writer2.write((i+1)+","+tps+"\n");
		}
		
		writer1.close();
		writer2.close();
		br1.close();
		br2.close();
		br3.close();
	}
	
}
