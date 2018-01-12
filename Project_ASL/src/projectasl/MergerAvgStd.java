package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MergerAvgStd {

public static void main(String[] args) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader("baseline_1h20_avg.txt"));
		//BufferedReader br2 = new BufferedReader(new FileReader("local_log_1h_32t_2_avg.txt"));
		//BufferedReader br3 = new BufferedReader(new FileReader("local_log_1h_32t_3_avg.txt"));
		BufferedReader br4 = new BufferedReader(new FileReader("baseline_1h20_std.txt"));
		//BufferedReader br5 = new BufferedReader(new FileReader("local_log_1h_32t_2_std.txt"));
		//BufferedReader br6 = new BufferedReader(new FileReader("local_log_1h_32t_3_std.txt"));
		FileWriter writer1 = new FileWriter("baseline_no_mw_avg_std.csv");
		FileWriter writer2 = new FileWriter("baseline_no_mw_avg_std.txt");
		
		writer1.write("Time (sec), Avg. Response Time (us), Standard deviation (us)"+"\n");
		
		for(int i=0;i<500;i++){
			br1.readLine();
			//br2.readLine();
			//br3.readLine();
			//br4.readLine();
			//br5.readLine();
			//br6.readLine();
		}
		
		for(int i=500;i<4100;i++){
			System.out.println(i-499);
			double temp1=Double.parseDouble(br1.readLine());
			double temp4=Double.parseDouble(br4.readLine());
			/*double temp2=Double.parseDouble(br2.readLine());
			double temp3=Double.parseDouble(br3.readLine());
			double temp4=Double.parseDouble(br4.readLine());
			double temp5=Double.parseDouble(br5.readLine());
			double temp6=Double.parseDouble(br6.readLine());*/
			
			double temp_avg = temp1;
			double temp_std = temp4;
			
			writer1.write((i-499)+","+temp_avg+","+temp_std+"\n");
			writer2.write((i-499)+","+temp_avg+","+temp_std+"\n");
		}
		
		writer1.close();
		writer2.close();
		br1.close();
		//br2.close();
		//br3.close();
		br4.close();
		//br5.close();
		//br6.close();
	}
	
}
