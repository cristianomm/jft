/**
 * 
 */
package com.cmm.jft.messaging.fix50sp2;

import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.security.Security;

import quickfix.Message;
import quickfix.field.SecurityReqID;
import quickfix.field.SecurityRequestResult;
import quickfix.field.SecurityResponseID;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;
import quickfix.fix44.SequenceReset;

/**
 * <p><code>Fix50SP2MDMessageEncoder.java</code></p>
 * @author cristiano
 * @version Feb 26, 2016 4:56:04 PM
 *
 */
public class Fix50SP2MDMessageEncoder implements MarketDataMessageEncoder {
	
	
	private static Fix50SP2MDMessageEncoder instance;
	
	
	private Fix50SP2MDMessageEncoder(){
		
	}
	
	public static synchronized Fix50SP2MDMessageEncoder getInstance(){
		if(instance == null){
			instance = new Fix50SP2MDMessageEncoder();
		}
		
		return instance;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#sequenceReset()
	 */
	@Override
	public SequenceReset sequenceReset() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#securityList()
	 */
	@Override
	public SecurityList securityList(Security security) {
		
		SecurityList list = null;
		try{
			list = new SecurityList();
			
			list.setInt(393, 1);
			list.setString(SecurityReqID.FIELD, "0");
			list.setString(SecurityResponseID.FIELD, "0");
			list.setInt(SecurityRequestResult.FIELD, SecurityRequestResult.VALID_REQUEST);
			
			SecurityList.NoRelatedSym g = new SecurityList.NoRelatedSym();
			g.setString(55, security.getSymbol());
			g.setInt(48, security.getSecurityID());
			g.setString(22, "8");
			g.setString(207, "BVMF");
			g.setString(107, security.getDescription());
			list.addGroup(g);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#mdIncrementalRefresh()
	 */
	@Override
	public MarketDataIncrementalRefresh mdIncrementalRefresh() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#mdSnapShotFullRefresh()
	 */
	@Override
	public MarketDataSnapshotFullRefresh mdSnapShotFullRefresh() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#securityStatus()
	 */
	@Override
	public SecurityStatus securityStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#nonFixData()
	 */
	@Override
	public Message nonFixData() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MessageEncoder#heartbeat()
	 */
	@Override
	public Message heartbeat() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
