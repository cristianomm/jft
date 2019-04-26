/**
 * 
 */
package test.services.trading;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.services.trading.OrderService;

/**
 * <p><code>OrderServiceTest.java</code></p>
 * @author cristiano
 * @version Feb 9, 2017 11:30:45 AM
 *
 */
public class OrderServiceTest {


    private Security security = SecurityService.getInstance().provideSecurity("WDOH17");

    /**
     * Test method for {@link com.cmm.jft.services.trading.OrderService#getInstance()}.
     */
    @Test
    public void testGetInstance() {

	assertNotNull(OrderService.getInstance());
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.OrderService#newOrder(com.cmm.jft.model.trading.enums.OrderTypes, com.cmm.jft.model.security.Security, com.cmm.jft.model.trading.enums.Side, double, double, double, double, double)}.
     */
    @Test
    public void testNewOrder() {

	Side side = Side.BUY;
	int volume = 1;
	double price = 3129.5;
	double stopLoss = 3128.5;
	double stopGain = 3130.5;

	for (OrderTypes type : OrderTypes. values()) {
	    /*
			Order type.
			Valid values:
			1 - Market
			2 - Limit
			3 - Stop Loss
			4 - Stop Limit
			K - Market With Leftover As Limit
	     */
	    if(type == OrderTypes.Market || type == OrderTypes.Limit || 
		    type == OrderTypes.Stop || type ==  OrderTypes.StopLimit || 
		    type == OrderTypes.MarketWithLeftOverAsLimit){

		for(Orders o : OrderService.getInstance().newOrder(
			type, security, side, 
			volume, price, stopLoss, stopGain
			)){



		}
	    }

	}

    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.OrderService#newLimitOrder(com.cmm.jft.model.security.Security, com.cmm.jft.model.trading.enums.Side, double, double)}.
     */
    @Test
    public void testNewLimitOrder() {

	int volume = 1;
	double price = 3214;
	double stopLoss = 0;
	double stopGain = 0;

	List<Orders> ordrs = OrderService.getInstance().newOrder(OrderTypes.Limit, security , Side.BUY, 
		volume, price, stopLoss, stopGain);
	assertEquals(1, ordrs.size());


	price = 3214;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Limit, security , Side.SELL, 
		volume, price, stopLoss, stopGain);
	assertEquals(1, ordrs.size());

    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.OrderService#newStopOrder(com.cmm.jft.model.security.Security, com.cmm.jft.model.trading.enums.Side, double, double)}.
     */
    @Test
    public void testNewStopOrder() {
	int volume = 1;
	double price = 3214;
	double stopLoss = 0;
	double stopGain = 0;

	List<Orders> ordrs = OrderService.getInstance().newOrder(OrderTypes.Stop, security , Side.BUY, 
		volume, price, stopLoss, stopGain);
	assertEquals(0, ordrs.size());

	price = 0;
	stopGain = 3215;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Stop, security , Side.BUY, 
		volume, price, stopLoss, stopGain);
	assertEquals(0, ordrs.size());

	stopLoss = 3212;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Stop, security , Side.BUY, 
		volume, price, stopLoss, stopGain);
	assertEquals(1, ordrs.size());


	price = 3214;
	stopLoss = 0;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Stop, security , Side.SELL, 
		volume, price, stopLoss, stopGain);
	assertEquals(0, ordrs.size());
    }

    /**
     * Test method for {@link com.cmm.jft.services.trading.OrderService#newMarketOrder(com.cmm.jft.model.security.Security, com.cmm.jft.model.trading.enums.Side, double)}.
     */
    @Test
    public void testNewMarketOrder() {
	int volume = 1;
	double price = 0;
	double stopLoss = 0;
	double stopGain = 0;

	List<Orders> ordrs = OrderService.getInstance().newOrder(OrderTypes.Market, security , Side.BUY, 
		volume, price, stopLoss, stopGain);
	assertEquals(1, ordrs.size());


	stopLoss = 3214;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Market, security , Side.SELL, 
		volume, price, stopLoss, stopGain);
	assertEquals(2, ordrs.size());

	stopLoss = 3214;
	stopGain = 3217;
	ordrs = OrderService.getInstance().newOrder(OrderTypes.Market, security , Side.SELL, 
		volume, price, stopLoss, stopGain);
	assertEquals(3, ordrs.size());

    }

}
