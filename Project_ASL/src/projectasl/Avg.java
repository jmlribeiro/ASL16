package projectasl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Avg {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br1 = new BufferedReader(new FileReader("trace_avg.txt"));
		double sum=0;
		for(int i=0;i<3600;i++){
			double temp = Double.parseDouble(br1.readLine());
			sum+=temp;
		}
		br1.close();
		
		System.out.println(sum/3600.0);
	}

}
