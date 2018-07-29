/**
 * 
 */
package com.cmm.jft.engine.marketdata.recovery;

import javax.management.JMException;

import org.apache.log4j.Level;
import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

import com.cmm.jft.engine.Service;

/**
 * <p>
 * <code>SnapshotRecoveryService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class SnapshotRecoveryService extends Service {
    
    private SnapshotRecoveryChannel channel;
    
    public SnapshotRecoveryService() {
	try {
	    
	    this.channel = SnapshotRecoveryChannel.getInstance();
	    
	    SessionSettings settings = new SessionSettings(Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream("SnapshotRecoveryService.cfg"));
	    
	    init(settings, channel);
	} catch (ConfigError | FieldConvertError | JMException e) {
	    logger.log(getClass(), e, Level.ERROR);
	    e.printStackTrace();
	}
    }
    
    

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.Service#start()
     */
    @Override
    public boolean start() {
	super.start();
	
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (started) {
		    //System.out.println("sending instruments...");
		    channel.sendSnapshot();
		    try {
			Thread.sleep(1000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();
        return started;
    }
    

    public static void main(String args[]) throws Exception {
	try {
	    SnapshotRecoveryService service = new SnapshotRecoveryService();
	    service.start();

	    System.out.println("press <enter> to quit");
	    System.in.read();

	    service.stop();
	} catch (Exception e) {
	    e.printStackTrace();
	    // log.error(e.getMessage());
	}
    }

}
