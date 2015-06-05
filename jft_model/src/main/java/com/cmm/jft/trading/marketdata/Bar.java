/**
 * 
 */
package com.cmm.jft.trading.marketdata;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.trading.enums.Duration;

/**
 * <p>
 * <code>Bar.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 11/08/2013 23:24:42
 *
 */
@Entity
@Table(name = "Bar")
// @DiscriminatorValue(value="1")
public class Bar {

	// @Id
	// @TableGenerator(name = "BAR_SEQ", table = "SEQUENCE", allocationSize = 1,
	// initialValue = 1)
	// @GeneratedValue(generator = "BAR_SEQ", strategy = GenerationType.TABLE)
	// @Basic(optional = false)
	// @Column(name = "barID", nullable = false)
	// private Long barID;

	@Enumerated(EnumType.STRING)
	@Column(name = "BarSize", nullable = false)
	private Duration barSize;

	@Column(name = "Open", precision = 19, scale = 6, nullable = false)
	private BigDecimal open;

	@Column(name = "Close", precision = 19, scale = 6, nullable = false)
	private BigDecimal close;

	@Column(name = "High", precision = 19, scale = 6, nullable = false)
	private BigDecimal high;

	@Column(name = "Low", precision = 19, scale = 6, nullable = false)
	private BigDecimal low;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OpenDateTime")
	private Date openDateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CloseDateTime")
	private Date closeDateTime;

	public Bar() {

	}

	/**
	 * @param barSize
	 * @param open
	 * @param close
	 * @param high
	 * @param low
	 */
	public Bar(Date eventDateTime, long volume, Duration barSize,
			BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low) {
		super();
		// this.eventDateTime = eventDateTime;
		// this.volume = volume;
		this.barSize = barSize;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
	}

	// /**
	// * @return the barID
	// */
	// public Long getBarID() {
	// return barID;
	// }

	/**
	 * @return the barSize
	 */
	public Duration getBarSize() {
		return this.barSize;
	}

	/**
	 * @param barSize
	 *            the barSize to set
	 */
	public void setBarSize(Duration barSize) {
		this.barSize = barSize;
	}

	/**
	 * @return the open
	 */
	public BigDecimal getOpen() {
		return this.open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * @return the close
	 */
	public BigDecimal getClose() {
		return this.close;
	}

	/**
	 * @param close
	 *            the close to set
	 */
	public void setClose(BigDecimal close) {
		this.close = close;
	}

	/**
	 * @return the high
	 */
	public BigDecimal getHigh() {
		return this.high;
	}

	/**
	 * @param high
	 *            the high to set
	 */
	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public BigDecimal getLow() {
		return this.low;
	}

	/**
	 * @param low
	 *            the low to set
	 */
	public void setLow(BigDecimal low) {
		this.low = low;
	}

}
