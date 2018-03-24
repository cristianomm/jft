/**
 * 
 */
package analysis;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * <p>
 * <code>Wireshark.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Jan 31, 2017 9:24:56 AM
 *
 */
public class Wireshark {

    /**
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
	byte ip[] = new byte[] { (byte) 201, (byte) 159, (byte) 158, (byte) 137 };

	System.out.println(Inet4Address.getByAddress(ip).getCanonicalHostName());

    }
}
