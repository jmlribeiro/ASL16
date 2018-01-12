import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

// Load balancing experiment

public class HashTest {

	public static double statDist(double[] freqs){
	    // Compute statistical distance to uniform distribution
		double d = 0;
		for(int i=0;i<freqs.length;i++){
			d+=0.5*Math.abs(freqs[i]-1.0/freqs.length);
		}
		return d;
	}
	
	public static int hash(byte[] key, int num){
		// Hashes the key
		String string_key="";
		try{
			string_key = new String(key,"ASCII");
		}catch(UnsupportedEncodingException e){
			System.out.println("Unsupported encoding!");
			System.exit(-1);
		}
		int h = string_key.hashCode()%num;
		if(h<0){
			return num+h;
		}
		else{
			return h;
		}
	}
	
	
	static double[] testHash(int n,double total){
        int[] counts = new int[n];
        double[] freqs = new double[n];
        
        for(int i=0;i<n;i++){
        	counts[i]=0;
        }

        for (int i = 0; i<total; i++){
            // Generate unif. random 16 byte string from 128-bit integer and hash
            Random rnd = new Random();
            byte[] randStr = new BigInteger(128, rnd).toByteArray();
            int hash = hash(randStr,n);
            
            // Update frequencies
            counts[hash]++;
        }
        for (int i=0;i<n;i++){
            // Compute induced distribution
        	freqs[i]=counts[i]/total;
        }
        
        return freqs;
    }
	
	public static void main(String[] args){
		
		// Run experiment for 2,...,7 servers, 5 repetitions, 100.000 strings each
		
		double[] max_sd = new double[6];
		
		for(int i=2;i<=7;i++){
			double max=0;
			for(int j=0;j<5;j++){
				double temp = statDist(testHash(i,100000));
				if(temp>max){
					max=temp;
				}
			}
			max_sd[i-2]=max;
		}
		for(int i=0;i<max_sd.length;i++){
			System.out.println(max_sd[i]);
		}
	}
	
}
