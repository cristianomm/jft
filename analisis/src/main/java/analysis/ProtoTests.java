/**
 * 
 */
package analysis;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;

import analysis.ProtoTests.Doo;

//import com.tictactec.ta.lib.Core;

/**
 * <p><code>ProtoTests.java</code></p>
 * @author cristiano
 * @version Oct 28, 2016 9:38:20 PM
 *
 */
public class ProtoTests {
    
    
    
    class Doo implements Comparable<Doo>{
	int val;
	int pos;
	/**
	 * 
	 */
	public Doo(int pos, int val) {
	    this.pos = pos;
	    this.val = val;
	}
	
	public int compareTo(Doo doo) {
	    int ret = 0;
	    if(this.pos == doo.pos){
		ret = 0;
	    }
	    else if(this.pos > doo.pos){
		ret = 1;
	    }
	    else{
		ret = -1;
	    }
	    
	    return ret;
	};
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    Doo ot = (Doo)obj;
	    return (this.pos == ot.pos) && (this.val == ot.val);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "pos:" + pos + ", val:" + val;
	}
	
    }

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {

	Instant ins = Instant.ofEpochMilli(131852521);
	System.out.println(Date.from(ins));


	int size = 10;
	double[] data = new double[size];
	Random rand = new Random(3);
	for (int i = 0; i < data.length; i++) {
	    data[i] = rand.nextInt();
	}
	
	new ProtoTests().doo();

	//Core lib = new Core();
    }
    
    public void doo() throws InterruptedException{
	
	SortedMap<Integer, Doo> dos = new TreeMap<>();
	for(int i=0;i<10;i++){
	    dos.put(i, new Doo(i, i+1));
	}
	
	for(Doo o : dos.values()){
	    System.out.println(o);
	}
	
	System.out.println("\n");
	System.out.println(((TreeMap<Integer, Doo>)dos).pollFirstEntry());// remove(new Doo(3,4)));
	System.out.println(dos.remove(3));
	System.out.println("\n");
	
	for(Doo o : dos.values()){
	    System.out.println(o);
	}
	
	
	
    }




}
