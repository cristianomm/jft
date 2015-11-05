/**
 * 
 */
package messaging;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DefaultMessageFactory;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.fix44.MessageFactory;

/**
 * <p><code>BuildMessage.java</code></p>
 * @author cristiano
 * @version Nov 5, 2015 1:03:38 AM
 *
 */
public class BuildMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String msg = "8=FIX.4.49=10535=A34=149=FIXGatewayDerivatives_MD52=20071106-19:12:37.79856=TradingEngineDerivativesA98=0108=12010=129";
		Message m;
		try {
			String p = "C:\\Disco\\Workspaces\\JFT\\jft_modules\\doc\\process\\bmfbovespa\\EntryPoint\\FIX44EntrypointGatewayDerivatives.xml";
			m = MessageUtils.parse(new DefaultMessageFactory(), new DataDictionary(p), msg);
			System.out.println(MessageUtils.getMessageType(msg));
		} catch (InvalidMessage | ConfigError e) {
			e.printStackTrace();
		}

	}

}
