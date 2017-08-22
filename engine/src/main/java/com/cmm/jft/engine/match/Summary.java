/**
 * 
 */
package com.cmm.jft.engine.match;

/**
 * <p>
 * <code>Summary.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 21/08/2017 10:33:12
 *
 */
public class Summary {

    private double price;
    private int orderCount;
    private int orderVolume;

    /**
     * 
     */
    public Summary(double price, int numOrders, int volume) {
	this.price = price;
	this.orderCount = numOrders;
	this.orderVolume = volume;
    }

    public void incrementOrder(){
	orderCount++;
    }

    public void incrementVolume(double volume){
	this.orderVolume += volume;
    }

    public void decrementOrder(){
	if(orderCount>0){
	    orderCount--;
	}
    }

    public void decrementVolume(double volume){
	if(orderVolume >= volume){
	    orderVolume -= volume;
	}
    }
    
    /**
     * @return the orderCount
     */
    public int getOrderCount() {
	return orderCount;
    }
    
    /**
     * @return the orderVolume
     */
    public int getOrderVolume() {
	return orderVolume;
    }
    
    /**
     * @return the price
     */
    public double getPrice() {
	return price;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    protected Summary clone() throws CloneNotSupportedException {
	return new Summary(this.price, this.orderCount, this.orderVolume);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Summary [price=" + price + ", orderCount=" + orderCount + ", orderVolume=" + orderVolume + "]";
    }


}
