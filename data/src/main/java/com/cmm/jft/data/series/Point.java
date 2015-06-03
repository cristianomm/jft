/**
 * 
 */
package com.cmm.jft.data.series;

import java.util.Date;

/**
 * <p><code>Point.java</code></p>
 * @author Cristiano M Martins
 * @version Jul 22, 2013 1:27:33 AM
 *
 */
public class Point implements SeriePoint{

    private double value;
    private Date time;
    /**
     * @param value
     * @param datetime
     */
    public Point(double value, Date datetime) {
	super();
	this.value = value;
	this.time = datetime;
    }
            
    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.time == null) ? 0 : this.time.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (this.time == null) {
			if (other.time != null)
				return false;
		} else if (!this.time.equals(other.time))
			return false;
		if (Double.doubleToLongBits(this.value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

	/**
     * @return the value
     */
    public double getValue() {
        return this.value;
    }
    /**
     * @return the datetime
     */
    public Date getTime() {
        return this.time;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Point [value=" + this.value + ", "
		+ (this.time != null ? "time=" + this.time : "")
		+ "]";
    }
    
    
}
