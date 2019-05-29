/**
 * 
 */
package com.cmm.jft.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;

/**
 * <p>
 * <code>Isin.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 29/09/2013 03:45:42
 *
 */
@Entity
@Table(name = "Isin", schema="security", uniqueConstraints = { @UniqueConstraint(columnNames = {"ISIN"}) })
@NamedQueries({ @NamedQuery(name = "Isin.findAll", query = "SELECT i FROM Isin i") })
public class Isin implements DBObject<Isin> {

	@Id
	@Column(name = "ISIN", length = 12, unique=true)
	private String isin;

	@Column(name="CFICode", length = 6)
	private String CFICode;
	
	@Column(name = "Description", length = 120)
	private String description;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "Status", length = 50)
	private GeneralStatus status;

	public Isin() {
	}

	public Isin(String isin) {
		this.isin = isin;
	}
	
	/**
	 * @return the isin
	 */
	public String getIsin() {
		return this.isin;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public GeneralStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}


}
