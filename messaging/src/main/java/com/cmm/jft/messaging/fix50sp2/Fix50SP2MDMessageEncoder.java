/**
 * 
 */
package com.cmm.jft.messaging.fix50sp2;

import java.util.HashMap;
import java.util.Map.Entry;

import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.security.Security;

import quickfix.Field;
import quickfix.FieldMap;
import quickfix.Message;
import quickfix.field.NewSeqNo;
import quickfix.field.SecurityReqID;
import quickfix.field.SecurityRequestResult;
import quickfix.field.SecurityResponseID;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataIncrementalRefresh.NoMDEntries;
import quickfix.fix50sp2.MarketDataSnapshotFullRefresh;
import quickfix.fix50sp2.SecurityList;
import quickfix.fix50sp2.SecurityStatus;
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
		return new SequenceReset(new NewSeqNo(1));
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#securityList()
	 */
	@Override
	public SecurityList securityList(Security[] securities) {
		
		SecurityList list = null;
		try{
			list = new SecurityList();
			
			list.setString(SecurityReqID.FIELD, "0");
			list.setString(SecurityResponseID.FIELD, "0");
			list.setInt(SecurityRequestResult.FIELD, SecurityRequestResult.VALID_REQUEST);
			
			for (int i = 0; (i < securities.length && securities[i]!=null) ; i++) {
				SecurityList.NoRelatedSym g = new SecurityList.NoRelatedSym();
				g.setString(55, securities[i].getSymbol());
				g.setInt(48, securities[i].getSecurityID());
				g.setString(22, "8");
				g.setString(207, "BVMF");
				g.setString(107, securities[i].getDescription());
				list.addGroup(g);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MarketDataMessageEncoder#mdIncrementalRefresh()
	 */
	@Override
	public MarketDataIncrementalRefresh mdIncrementalRefresh(HashMap<Integer, Object> values[]) {
		
		MarketDataIncrementalRefresh refresh = new MarketDataIncrementalRefresh();
		
		for (int i = 0; i < values.length; i++) {
			MarketDataIncrementalRefresh.NoMDEntries entry = new NoMDEntries();
			for(Entry<Integer, Object> e : values[i].entrySet()){
				/*
				new int[]{
						279, 269, 83, 48, 22, 207, 1500, 270, 271, 
						1500, 37014, 272, 273, 37016, 37017, 274, 
						277, 336, 288, 289, 451, 287, 1020, 1003
						}
				*/
			}
			
			refresh.addGroup(entry);
		}
		
		
		return refresh;
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
		
		return null;
	}

	
}
