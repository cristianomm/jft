/**
 * 
 */
package com.cmm.jft.data.series;

import java.util.Date;


/**
 * <p><code>OHLC.java</code></p>
 * @author Cristiano M Martins
 * @version Jul 20, 2013 2:45:45 AM
 *
 */
public class OHLC implements SeriePoint{

	private double open;
	private double close;
	private double max;
	private double min;
	private Date openDateTime;
	private Date closeDateTime;

	/**
	 * @param open
	 * @param close
	 * @param max
	 * @param min
	 * @param datetime
	 */
	public OHLC(double open, double close, double max, double min, Date openDateTime, Date closeDateTime) {
		super();
		this.open = open;
		this.close = close;
		this.max = max;
		this.min = min;
		this.openDateTime = openDateTime;
		this.closeDateTime = closeDateTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.close);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((this.closeDateTime == null) ? 0 : this.closeDateTime
						.hashCode());
		temp = Double.doubleToLongBits(this.max);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.min);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.open);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((this.openDateTime == null) ? 0 : this.openDateTime
						.hashCode());
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
		OHLC other = (OHLC) obj;
		if (Double.doubleToLongBits(this.close) != Double
				.doubleToLongBits(other.close))
			return false;
		if (this.closeDateTime == null) {
			if (other.closeDateTime != null)
				return false;
		} else if (!this.closeDateTime.equals(other.closeDateTime))
			return false;
		if (Double.doubleToLongBits(this.max) != Double
				.doubleToLongBits(other.max))
			return false;
		if (Double.doubleToLongBits(this.min) != Double
				.doubleToLongBits(other.min))
			return false;
		if (Double.doubleToLongBits(this.open) != Double
				.doubleToLongBits(other.open))
			return false;
		if (this.openDateTime == null) {
			if (other.openDateTime != null)
				return false;
		} else if (!this.openDateTime.equals(other.openDateTime))
			return false;
		return true;
	}



	/**
	 * @return the open
	 */
	public double getOpen() {
		return this.open;
	}
	/**
	 * @return the close
	 */
	public double getClose() {
		return this.close;
	}
	/**
	 * @return the max
	 */
	public double getMax() {
		return this.max;
	}
	/**
	 * @return the min
	 */
	public double getMin() {
		return this.min;
	}
	/**
	 * @return the openDateTime
	 */
	public Date getOpenDateTime() {
		return this.openDateTime;
	}
	/**
	 * @return the closeDateTime
	 */
	public Date getCloseDateTime() {
		return this.closeDateTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OHLC [open=" + this.open + ", close=" + this.close + ", max="
				+ this.max + ", min=" + this.min + ", openDateTime="
				+ this.openDateTime + ", closeDateTime=" + this.closeDateTime
				+ "]";
	}

}
