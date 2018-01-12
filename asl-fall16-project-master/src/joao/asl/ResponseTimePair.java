package joao.asl;

/* Pair of server response and time received.
 * Used in write threads for dealing with
 * replication.
 * */

public class ResponseTimePair {

	String response;
	long arrival_time;
	
	public ResponseTimePair(String r, long t){
		this.response=r;
		this.arrival_time=t;
	}
	
}
