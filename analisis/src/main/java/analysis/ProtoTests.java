/**
 * 
 */
package analysis;

import java.util.Random;

import com.tictactec.ta.lib.Core;

/**
 * <p><code>ProtoTests.java</code></p>
 * @author cristiano
 * @version Oct 28, 2016 9:38:20 PM
 *
 */
public class ProtoTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int size = 10;
		double[] data = new double[size];
		Random rand = new Random(3);
		for (int i = 0; i < data.length; i++) {
			data[i] = rand.nextInt();
		}
		
		Core lib = new Core();
		
		
		

	}

}
