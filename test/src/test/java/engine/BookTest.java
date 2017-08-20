/**
 * 
 */
package engine;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.cmm.jft.engine.Book;
import com.cmm.jft.engine.enums.MatchTypes;
import com.cmm.jft.trading.enums.OrderTypes;

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

    
    private String symbol;
    private HashSet<OrderTypes> orderTypes;
    
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	symbol = "WDOQ17";
	orderTypes = new HashSet<>();
	
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
	
	Book book = new Book(symbol, orderTypes, MatchTypes.FIFO, .20);
	
	assertTrue(book.getSecurity().getSymbol().equalsIgnoreCase(symbol));
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#getOrderCount()}.
     */
    @Test
    public void testGetOrderCount() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
	fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cmm.jft.engine.Book#getBookInfo()}.
     */
    @Test
    public void testGetBookInfo() {
	fail("Not yet implemented"); // TODO
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
	fail("Not yet implemented"); // TODO
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
