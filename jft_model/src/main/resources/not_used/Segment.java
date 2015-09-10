/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>Segment</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Segment")
@NamedQueries({
		@NamedQuery(name = "Segment.findAll", query = "SELECT s FROM Segment s"),
		@NamedQuery(name = "Segment.findBySegmentID", query = "SELECT s FROM Segment s WHERE s.segmentID = :segmentID"),
		@NamedQuery(name = "Segment.findBySegmentName", query = "SELECT s FROM Segment s WHERE s.segmentName = :segmentName") })
public class Segment implements DBObject<Segment> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SEGMENT_SEQ", sequenceName = "SEGMENT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SEGMENT_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "segmentID", nullable = false)
	private Long segmentID;
	
	@Column(name = "SegmentName", length = 100)
	private String segmentName;
	
	@ManyToMany(mappedBy = "segmentSet", fetch = FetchType.LAZY)
	private Set<Company> companySet;
	
	@JoinColumn(name = "subsectorID", referencedColumnName = "subsectorID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Subsector subsectorID;

	public Segment() {
		this.companySet = new HashSet<Company>();
	}

	public Segment(Long segmentID) {
		this.segmentID = segmentID;
		this.companySet = new HashSet<Company>();
	}

	public Long getSegmentID() {
		return segmentID;
	}

	public void setSegmentID(Long segmentID) {
		this.segmentID = segmentID;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public Set<Company> getCompanySet() {
		return companySet;
	}

	public void setCompanySet(Set<Company> companySet) {
		this.companySet = companySet;
	}

	public Subsector getSubsectorID() {
		return subsectorID;
	}

	public void setSubsectorID(Subsector subsectorID) {
		this.subsectorID = subsectorID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (segmentID != null ? segmentID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Segment)) {
			return false;
		}
		Segment other = (Segment) object;
		if ((this.segmentID == null && other.segmentID != null)
				|| (this.segmentID != null && !this.segmentID
						.equals(other.segmentID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.cmm.jft.core.Segment[ segmentID=" + segmentID + " ]";
	}
	
}
