/**
 * 
 */
package engine;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.cmm.jft.engine.Book;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.OrderException;

import quickfix.FixVersions;
import quickfix.SessionID;

/**
 * <p>
 * <code>BookTest.java</code>
 * </p>
 *
 * @author cristiano
 * @version 07/08/2017 02:43:01
 *
 */
public class BookTest {


    private Book book;
    private String symbol;
    private Security security;
    private SessionID sessionID;
    private HashSet<OrderTypes> orderTypes;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	symbol = "WDOV17";
	security = SecurityService.getInstance().provideSecurity(symbol);
	orderTypes = new HashSet<>();
	sessionID = new SessionID(FixVersions.BEGINSTRING_FIX44, "SENDER", "TARGET");
	
	book = new Book(symbol, .20);
	
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#Book(java.lang.String, java.util.HashSet, com.cmm.jft.engine.enums.MatchTypes, double)}.
     */
    @Test
    public void testBook() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#getSecurity()}.
     */
    @Test
    public void testGetSecurity() { 
	assertTrue(book.getSecurity().getSymbol().equalsIgnoreCase(symbol));
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#calculateAdjPrice()}.
     */
    @Test
    public void testCalculateAdjPrice() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#addOrder(com.cmm.jft.trading.Orders, quickfix.SessionID)}.
     */
    @Test
    public void testAddOrder() {

	try {
	    long orderID = 123456;
	    String clOrdID = "10";
	    String traderID = "21";
	    String brokerID = "22";
	    String senderLct = "00";
	    
	    Orders ord = new Orders(orderID++, "123456", security, Side.BUY, 
		    3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
	    ord.setBrokerID("308");
	    
	    boolean added = book.addOrder(ord);
	    
	    for(int i=0;i<10000;i++) {
		ord = new Orders(orderID++, "123457"+i, security, Side.BUY, 
			3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
		ord.setBrokerID("308");
		
		added = added && book.addOrder(ord);
	    }

	    assertTrue(added);

	} catch (OrderException e) {
	    e.printStackTrace();
	}
    }
    
    @Test
    public void testMatchOrder() {

	try {
	    long orderID = 123456;
	    String clOrdID = "10";
	    String traderID = "20";
	    String brokerID = "21";
	    String senderLct = "00";
	    
	    Orders ord = new Orders(orderID++, clOrdID, security, Side.BUY, 
		    3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
	    ord.setBrokerID("308");
	    
	    boolean added = book.addOrder(ord);
	    
	    ord = new Orders(orderID++, clOrdID, security, Side.SELL, 
			3321.5, 2, OrderTypes.Limit, traderID, brokerID, senderLct);
	    ord.setBrokerID("308");
		
	    added = added && book.addOrder(ord);
	    

	    assertTrue(added);

	} catch (OrderException e) {
	    e.printStackTrace();
	}
    }
    
        

    /**
     * Test method for {@link com.cmm.jft.engine.Book#cancelOrder(com.cmm.jft.trading.Orders)}.
     */
    @Test
    public void testCancelOrder() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#replaceOrder(com.cmm.jft.trading.Orders)}.
     */
    @Test
    public void testReplaceOrder() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#closeBook()}.
     */
    @Test
    public void testCloseBook() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#takeSnapshot()}.
     */
    @Test
    public void testTakeSnapshot() {
	fail("Not yet implemented"); // TODO
    }

}
