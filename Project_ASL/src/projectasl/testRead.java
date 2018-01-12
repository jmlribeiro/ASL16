package projectasl;

import java.io.*;

class data
{
	double average;
	double std;
	double tps;
	public data()
	{
		
	}
	
	public data(double a, double b, double c)
	{
		average = a;
		std = b;
		tps = c;
	}
}
public class testRead {
	public static void main(String[] args) throws IOException {
		
		int max_number_of_clients_for_test = 128;
		
		data[][][]  tensor = new data[2][max_number_of_clients_for_test / 8][5];
		
		for(int i = 1; i <= 2; i++)
		{
			for(int j = 1; j <= Math.floor(max_number_of_clients_for_test / 8); j++)
			{
				for(int k = 1; k <= 5; k++)
				{
					BufferedReader br = new BufferedReader(new FileReader("fabio/client"+i+"_"+(4*j)+"-"+k+".log"));
					
					while(true)
					{
						String s = br.readLine();
						if(s == null)
							break;
						
						if(s.contains("Total Statistics ("))
						{
							data to_add = new data();
							for(int tmp = 0; tmp < 2; tmp++)
								br.readLine();
							
							s = br.readLine();
							String[] s_splitted = s.split(" ");
							to_add.average = Double.parseDouble(s_splitted[s_splitted.length - 1]);
							
							s = br.readLine();
							
							s = br.readLine();
							s_splitted = s.split(" ");
							to_add.std = Double.parseDouble(s_splitted[s_splitted.length - 1]);
							
							while(true)
							{
								s = br.readLine();
								if(s.contains("TPS:"))
								{
									s_splitted = s.split(" ");
									to_add.tps = Double.parseDouble(s_splitted[6]);
									break;
								}
							}
							System.out.println(to_add.average + " " + to_add.std + " " + to_add.tps);
							tensor[i-1][j-1][k-1] = to_add;
						}
					}
				}
			}
		}
		
		double[] avg = new double[16];
		double[] std = new double[16];
		double[] tps = new double[16];
		double[] tpsStd = new double[16];
		
		for(int j=0;j<16;j++){
			
			double avg_sum=0;
			double tps_sum=0;
			double std_sum=0;
			
			for(int k=0;k<5;k++){
				for(int i=0;i<2;i++){
					avg_sum+=tensor[i][j][k].average;
					tps_sum+=tensor[i][j][k].tps;
					std_sum+=tensor[i][j][k].std;
				}
			}
			
			avg[j]=avg_sum/10.0;
			tps[j]=tps_sum/5.0;
			std[j]=std_sum/10.0;
			
			double tpsStdSum=0;
			
			for(int k=0;k<5;k++){
				double tpsRun=0;
				for(int i=0;i<2;i++){
					tpsRun+=tensor[i][j][k].tps;
				}
				tpsStdSum+=(tpsRun-tps[j])*(tpsRun-tps[j]);
			}
			
			tpsStd[j]=Math.sqrt(tpsStdSum/5.0);
		}
		
		for(int k=0;k<5;k++){
			FileWriter writer = new FileWriter("baseline_tps"+(k+1)+".csv");
			writer.write("nr. clients,TPS (ops/sec)"+"\n");
			for(int j=0;j<16;j++){
				System.out.println("test"+j);
				double temp = tensor[0][j][k].tps+tensor[1][j][k].tps;
				writer.write(8*(j+1)+","+temp+"\n");
			}
			writer.close();
			
			writer = new FileWriter("baseline_avg"+(k+1)+".csv");
			writer.write("nr. clients,Avg. Response Time (us)"+"\n");
			for(int j=0;j<16;j++){
				double temp = 0.5*(tensor[0][j][k].average+tensor[1][j][k].average);
				writer.write(8*(j+1)+","+temp+"\n");
			}
			writer.close();
			
			writer = new FileWriter("baseline_std"+(k+1)+".csv");
			writer.write("nr. clients,Standard deviation (us)"+"\n");
			for(int j=0;j<16;j++){
				double temp = 0.5*(tensor[0][j][k].std+tensor[1][j][k].std);
				writer.write(8*(j+1)+","+temp+"\n");
			}
			writer.close();
			
			writer = new FileWriter("fabio_avg_std"+(k+1)+".csv");
			writer.write("nr. clients,Avg. Response Time (us)"+","+"Standard deviation (us)"+"\n");
			for(int j=0;j<16;j++){
				double temp1 = 0.5*(tensor[0][j][k].average+tensor[1][j][k].average);
				double temp2 = 0.5*(tensor[0][j][k].std+tensor[1][j][k].std);
				writer.write(8*(j+1)+","+temp1+","+temp2+"\n");
			}
			writer.close();
		}
		
		FileWriter writer = new FileWriter("fabio_tps.csv");
		writer.write("nr. clients,TPS (ops/sec)"+"\n");
		for(int i=0;i<16;i++) {
		  writer.write(8*(i+1)+","+tps[i]+"\n");
		}
		writer.close();
		
		writer = new FileWriter("fabio_avg.csv");
		writer.write("nr. clients,Avg. Response Time (us)"+"\n");
		for(int i=0;i<16;i++) {
			  writer.write(8*(i+1)+","+Double.toString(avg[i])+"\n");
		}
		writer.close();

		writer = new FileWriter("fabio_std.csv");
		writer.write("nr. clients,Standard deviation (us)"+"\n");
		for(int i=0;i<16;i++) {
			  writer.write(8*(i+1)+","+std[i]+"\n");
		}
		writer.close();
		
		writer = new FileWriter("fabio_avg_with_std.csv"); 
		writer.write("nr. clients,Avg. Response Time (us)"+","+"Standard deviation (us)"+"\n");
		for(int i=0;i<16;i++) {
			  writer.write(8*(i+1)+","+Double.toString(avg[i])+","+Double.toString(std[i])+"\n");
		}
		writer.close();
		
		writer = new FileWriter("fabio_tps_with_std.csv");
		writer.write("nr. clients,TPS (ops/sec),Standard deviation (ops/sec)"+"\n");
		for(int i=0;i<16;i++) {
			  writer.write(8*(i+1)+","+Double.toString(tps[i])+","+Double.toString(tpsStd[i])+"\n");
		}
		writer.close();
	}
}
