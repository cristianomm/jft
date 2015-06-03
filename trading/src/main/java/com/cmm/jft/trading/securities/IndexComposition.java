/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading.securities;

import java.io.Serializable;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>IndexComposition</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "IndexComposition", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"IndexDate", "indexID", "securityID" }) })
@NamedQueries({
		@NamedQuery(name = "IndexComposition.findAll", query = "SELECT i FROM IndexComposition i"),
		@NamedQuery(name = "IndexComposition.findByIndexCompID", query = "SELECT i FROM IndexComposition i WHERE i.indexCompID = :indexCompID"),
		@NamedQuery(name = "IndexComposition.findByIndexDate", query = "SELECT i FROM IndexComposition i WHERE i.indexDate = :indexDate") })
public class IndexComposition implements DBObject<IndexComposition> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "INDEXCOMPOSITION_SEQ", sequenceName = "INDEXCOMPOSITION_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "INDEXCOMPOSITION_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "indexCompID", nullable = false)
	private Long indexCompID;

	@Basic(optional = false)
	@Column(name = "IndexDate", nullable = false, length = 10)
	private String indexDate;

	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	@JoinColumn(name = "indexID", referencedColumnName = "indexID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Index indexID;

	public IndexComposition() {
	}

	public IndexComposition(Long indexCompID, String indexDate) {
		this.indexCompID = indexCompID;
		this.indexDate = indexDate;
	}

	/**
	 * @return the indexDate
	 */
	public String getIndexDate() {
		return this.indexDate;
	}

	/**
	 * @param indexDate
	 *            the indexDate to set
	 */
	public void setIndexDate(String indexDate) {
		this.indexDate = indexDate;
	}

	/**
	 * @return the securityID
	 */
	public Security getSecurityID() {
		return this.securityID;
	}

	/**
	 * @param securityID
	 *            the securityID to set
	 */
	public void setSecurityID(Security securityID) {
		this.securityID = securityID;
	}

	/**
	 * @return the indexID
	 */
	public Index getIndexID() {
		return this.indexID;
	}

	/**
	 * @param indexID
	 *            the indexID to set
	 */
	public void setIndexID(Index indexID) {
		this.indexID = indexID;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the indexCompID
	 */
	public Long getIndexCompID() {
		return this.indexCompID;
	}

}
