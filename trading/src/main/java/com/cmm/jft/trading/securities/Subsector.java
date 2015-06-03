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
 * <code>Subsector</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Subsector")
@NamedQueries({
		@NamedQuery(name = "Subsector.findAll", query = "SELECT s FROM Subsector s"),
		@NamedQuery(name = "Subsector.findBySubsectorID", query = "SELECT s FROM Subsector s WHERE s.subsectorID = :subsectorID"),
		@NamedQuery(name = "Subsector.findBySubsectorName", query = "SELECT s FROM Subsector s WHERE s.subsectorName = :subsectorName") })
public class Subsector implements DBObject<Subsector> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SUBSECTOR_SEQ", sequenceName = "SUBSECTOR_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SUBSECTOR_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "subsectorID", nullable = false)
	private Long subsectorID;
	
	@Column(name = "SubsectorName", length = 100)
	private String subsectorName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subsectorID", fetch = FetchType.LAZY)
	private Set<Segment> segmentSet;
	
	@JoinColumn(name = "sectorID", referencedColumnName = "sectorID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Sector sectorID;

	public Subsector() {
		this.segmentSet = new HashSet<Segment>();
	}

	public Subsector(Long subsectorID) {
		this.subsectorID = subsectorID;
		this.segmentSet = new HashSet<Segment>();
	}

	public Long getSubsectorID() {
		return subsectorID;
	}

	public void setSubsectorID(Long subsectorID) {
		this.subsectorID = subsectorID;
	}

	public String getSubsectorName() {
		return subsectorName;
	}

	public void setSubsectorName(String subsectorName) {
		this.subsectorName = subsectorName;
	}

	public Set<Segment> getSegmentSet() {
		return segmentSet;
	}

	public void setSegmentSet(Set<Segment> segmentSet) {
		this.segmentSet = segmentSet;
	}

	public Sector getSectorID() {
		return sectorID;
	}

	public void setSectorID(Sector sectorID) {
		this.sectorID = sectorID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (subsectorID != null ? subsectorID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Subsector)) {
			return false;
		}
		Subsector other = (Subsector) object;
		if ((this.subsectorID == null && other.subsectorID != null)
				|| (this.subsectorID != null && !this.subsectorID
						.equals(other.subsectorID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.cmm.jft.core.Subsector[ subsectorID=" + subsectorID + " ]";
	}

}
