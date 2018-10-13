/**
 * 
 */
package com.cmm.jft.data.series;

import java.util.Date;

import com.cmm.jft.data.enums.CandleType;

/**
 * <p><code>MarketPoint.java</code></p>
 * @author Cristiano
 * @version 12/04/2015 00:25:22
 *
 */
public class MarketPoint extends OHLC {
		
	private int volume;
	private int tradeCount;
	private double financialVolume;
	private CandleType type;
	
	/**
	 * @param open
	 * @param close
	 * @param max
	 * @param min
	 * @param openDateTime
	 * @param closeDateTime
	 */
	public MarketPoint(double open, double close, double max, double min,
			Date openDateTime, Date closeDateTime, int volume, 
			double financialVolume, int tradeCount) {
		
		super(open, close, max, min, openDateTime, closeDateTime);
		
		this.volume = volume;
		this.financialVolume = financialVolume;
		this.tradeCount = tradeCount;
		
		if(close>open) {
			this.type = CandleType.UP;
		}
		else if(close<open) {
			this.type = CandleType.DOW;
		}
		else {
			this.type = CandleType.UNDEFINED;
		}
		
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.financialVolume);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + this.tradeCount;
		result = prime * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
		result = prime * result + this.volume;
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
		MarketPoint other = (MarketPoint) obj;
		if (Double.doubleToLongBits(this.financialVolume) != Double
				.doubleToLongBits(other.financialVolume))
			return false;
		if (this.tradeCount != other.tradeCount)
			return false;
		if (this.type != other.type)
			return false;
		if (this.volume != other.volume)
			return false;
		return true;
	}

	/**
	 * @return the financialVolume
	 */
	public double getFinancialVolume() {
		return this.financialVolume;
	}
	
	/**
	 * @return the tradeCount
	 */
	public int getTradeCount() {
		return this.tradeCount;
	}
	
	/**
	 * @return the type
	 */
	public CandleType getType() {
		return this.type;
	}
	
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return this.volume;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MarketPoint [volume=" + this.volume + ", tradeCount="
				+ this.tradeCount + ", financialVolume=" + this.financialVolume
				+ ", type=" + this.type + ", getOpen()=" + this.getOpen()
				+ ", getClose()=" + this.getClose() + ", getMax()="
				+ this.getMax() + ", getMin()=" + this.getMin()
				+ ", getOpenDateTime()=" + this.getOpenDateTime()
				+ ", getCloseDateTime()=" + this.getCloseDateTime() + "]";
	}
	
}
