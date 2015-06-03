/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading.securities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;


/**
 *
 * <p>
 * <code>Sector</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Sector")
@NamedQueries({
		@NamedQuery(name = "Sector.findAll", query = "SELECT s FROM Sector s"),
		@NamedQuery(name = "Sector.findBySectorID", query = "SELECT s FROM Sector s WHERE s.sectorID = :sectorID"),
		@NamedQuery(name = "Sector.findBySectorName", query = "SELECT s FROM Sector s WHERE s.sectorName = :sectorName") })
public class Sector implements DBObject<Sector> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SECTOR_SEQ", sequenceName = "SECTOR_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SECTOR_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sectorID", nullable = false)
	private Long sectorID;
	
	@Column(name = "SectorName", length = 100)
	private String sectorName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sectorID", fetch = FetchType.LAZY)
	private Set<Subsector> subsectorSet;

	public Sector() {
		this.subsectorSet = new HashSet<Subsector>();
	}

	public Sector(Long sectorID) {
		this.sectorID = sectorID;
		this.subsectorSet = new HashSet<Subsector>();
	}

	public Long getSectorID() {
		return sectorID;
	}

	public void setSectorID(Long sectorID) {
		this.sectorID = sectorID;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public Set<Subsector> getSubsectorSet() {
		return subsectorSet;
	}

	public void setSubsectorSet(Set<Subsector> subsectorSet) {
		this.subsectorSet = subsectorSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (sectorID != null ? sectorID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Sector)) {
			return false;
		}
		Sector other = (Sector) object;
		if ((this.sectorID == null && other.sectorID != null)
				|| (this.sectorID != null && !this.sectorID
						.equals(other.sectorID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.cmm.jft.core.Sector[ sectorID=" + sectorID + " ]";
	}

}
