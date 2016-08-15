/**
 * 
 */
package messaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DefaultMessageFactory;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.field.CheckSum;

/**
 * <p><code>BuildMessage.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 5, 2015 1:03:38 AM
 *
 */
public class BuildMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			String p = "C:\\Disco\\Workspaces\\JFT\\jft_modules\\doc\\process\\bmfbovespa\\EntryPoint\\FIX44EntrypointGatewayDerivatives.xml";
			DefaultMessageFactory dmf =  new DefaultMessageFactory();
			DataDictionary dd = new DataDictionary(p);
			Scanner sc = new Scanner(new File("C:\\Disco\\Bancos\\BM&FBovespa\\MarketData\\BMF\\apphmb\\intraday\\OFFERS.TXT"));
			String msg = "";
			while(sc.hasNextLine()){
				msg = sc.nextLine();
				Message m = MessageUtils.parse(dmf, dd, msg);
				System.out.println(MessageUtils.getMessageType(msg));
				//System.out.println(m);
			}
		} catch (InvalidMessage | ConfigError | FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	
	

}
