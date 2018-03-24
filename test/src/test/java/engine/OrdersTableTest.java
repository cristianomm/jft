/**
 * 
 */
package engine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.cmm.jft.engine.match.OrdersTable;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.OrderException;

/**
 * <p>
 * <code>OrdersTableTest.java</code>
 * </p>
 *
 * @author cristiano
 * @version 15/08/2017 12:19:45
 *
 */
public class OrdersTableTest {


    private Security security;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

	security = SecurityService.getInstance().provideSecurity("WDOV17");

    }

    /**
     * Test method for {@link com.cmm.jft.engine.match.OrdersTable#OrdersTable()}.
     */
    @Test
    public void testOrdersTable() {

	OrdersTable ot = new OrdersTable(Side.BUY);

	assertNotNull(ot.getClordIDs());
	assertNotNull(ot.getOrderIDs());
	assertNotNull(ot.getOrders());
    }

    /**
     * Test method for {@link com.cmm.jft.engine.match.OrdersTable#add(com.cmm.jft.trading.Orders)}.
     */
    @Test
    public void testAdd() {

	long orderID = 1;
	String clOrdID = "10";
	String traderID = "21";
	String brokerID = "22";
	String senderLct = "00";
	Orders ord = null;
	try {
	    ord = new Orders(orderID++, clOrdID, security, Side.BUY, 
		    3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
	    ord.setBrokerID("308");

	    OrdersTable ot = new OrdersTable(Side.BUY);

	    assertTrue(ot.add(ord));

	} catch (OrderException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Test method for {@link com.cmm.jft.engine.match.OrdersTable#remove(com.cmm.jft.trading.Orders)}.
     */
    @Test
    public void testRemove() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.engine.match.OrdersTable#getClOrderID(java.lang.String)}.
     */
    @Test
    public void testGetClOrderID() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.engine.match.OrdersTable#getOrderID(long)}.
     */
    @Test
    public void testGetOrderID() {
	fail("Not yet implemented");
    }

    @Test
    public void testPerformance() {
	System.out.println("testPerformance()");
	OrdersTable ot = new OrdersTable(Side.BUY);
	long orderID = 1;
	String clOrdID = "10";
	String traderID = "21";
	String brokerID = "22";
	String senderLct = "00";
	Orders ord = null;
	try {
	    for(int i=0;i<1000000;i++) {
		ord = new Orders(orderID++, clOrdID+i, security, Side.BUY, 
			3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
		ord.setBrokerID("308");
		ot.add(ord);
	    }
	} catch (OrderException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
