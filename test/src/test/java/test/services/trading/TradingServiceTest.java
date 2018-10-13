/**
 * 
 */
package test.services.trading;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.log4j.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.services.trading.OrderService;
import com.cmm.jft.services.trading.TradingService;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.logging.Logging;

/**
 * <p><code>TradingServiceTest.java</code></p>
 * @author cristiano
 * @version Feb 12, 2017 9:08:20 PM
 *
 */
public class TradingServiceTest {


    private Security security; 

    private SecurityService securityService;
    private OrderService orderService;
    private TradingService tradingService;

    /**
     * @throws java.lang.Exception
     */
    @After
    public void setUp() throws Exception {
	orderService = OrderService.getInstance();
	tradingService = TradingService.getInstance();
	securityService = SecurityService.getInstance();
	security = securityService.provideSecurity("WDOH17");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#getInstance()}.
     */
    @Test
    public void testGetInstance() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#getOrdersData()}.
     */
    @Test
    public void testGetOrdersData() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#loadOpenTrades()}.
     */
    @Test
    public void testLoadOpenTrades() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#newOrder(com.cmm.jft.trading.enums.OrderTypes, com.cmm.jft.trading.enums.Side, java.lang.String, double, double, double, double, java.util.Date, com.cmm.jft.trading.enums.TradeTypes, com.cmm.jft.trading.enums.OrderValidityTypes, java.lang.String)}.
     */
    @Test(timeout=100)
    public void testNewOrder() {

	try{
	    OrderTypes orderType = OrderTypes.Market;
	    String symbol = security.getSymbol();
	    int volume = 1;
	    double price = 0;
	    //double limitPrice = spnLimit.getValue();
	    double stopLoss = 0;
	    double stopGain = 0;
	    LocalDateTime duration = LocalDateTime.now();
	    TradeTypes tradeType = TradeTypes.DAY_TRADE;
	    OrderValidityTypes validityType = OrderValidityTypes.DAY;
	    String comment = "";

	    tradingService.newOrder(
		    orderType, Side.BUY, symbol, 
		    volume, price, stopLoss, stopGain, 
		    duration, tradeType, validityType, comment
		    );

	}catch(Exception e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#closePosition(com.cmm.jft.trading.Position)}.
     */
    @Test
    public void testClosePosition() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#cancelOrder(java.lang.String)}.
     */
    @Test
    public void testCancelOrder() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#changePrice(java.lang.String, double, double)}.
     */
    @Test
    public void testChangePrice() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#changeVolume(java.lang.String, int)}.
     */
    @Test
    public void testChangeVolume() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#addExecution(java.lang.String, java.util.Date, int, double)}.
     */
    @Test
    public void testAddExecution() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.TradingService#registerTrade(com.cmm.jft.trading.Position)}.
     */
    @Test
    public void testRegisterTrade() {
	fail("Not yet implemented");
    }

}
