package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class avgStdCsv{

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br1 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_21_avg.txt"));
		BufferedReader br2 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_22_avg.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_23_avg.txt"));
		
		BufferedReader br4 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_21_std.txt"));
		BufferedReader br5 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_22_std.txt"));
		BufferedReader br6 = new BufferedReader(new FileReader("trace_1h15_t32_no_timeout_S10_23_std.txt"));
		
		FileWriter writer1 = new FileWriter("trace10s_avg_std1.csv");
		FileWriter writer2 = new FileWriter("trace10s_avg_std2.csv");
		FileWriter writer3 = new FileWriter("trace10s_avg_std3.csv");
		
		writer1.write("Time (10 x sec), Avg. Response Time (us), Standard deviation (us)"+"\n");
		writer2.write("Time (10 x sec), Avg. Response Time (us), Standard deviation (us)"+"\n");
		writer3.write("Time (10 x sec), Avg. Response Time (us), Standard deviation (us)"+"\n");
		
		for(int i=0;i<50;i++){
			br1.readLine();
			br2.readLine();
			br3.readLine();
			br4.readLine();
			br5.readLine();
			br6.readLine();
		}
		
		for(int i=50;i<410;i++){
			System.out.println(i-49);
			double temp1=Double.parseDouble(br1.readLine());
			double temp2=Double.parseDouble(br2.readLine());
			double temp3=Double.parseDouble(br3.readLine());
			double temp4=Double.parseDouble(br4.readLine());
			double temp5=Double.parseDouble(br5.readLine());
			double temp6=Double.parseDouble(br6.readLine());
			
			writer1.write((i-49)+","+temp1+","+temp4+"\n");
			writer2.write((i-49)+","+temp2+","+temp5+"\n");
			writer3.write((i-49)+","+temp3+","+temp6+"\n");
		}
		
		writer1.close();
		writer2.close();
		writer3.close();
		br1.close();
		br2.close();
		br3.close();
		br4.close();
		br5.close();
		br6.close();
	}

}
