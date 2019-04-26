package com.cmm.jft.model.partner;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.partner.enums.PartnerTypes;

@Entity
@Table(name = "BusinessPartner", schema="Partner")
public class BusinessPartner implements DBObject<BusinessPartner> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long partnerId;
	
	@Column(name="Name", length=150)
	private String name;
	
	@Column(name="ID_Number", length=30, unique = true, nullable = false)
	private String idNumber;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "PartnerType")
	private PartnerTypes partnerType;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="partnerId")
	private List<PartnerAccount> accounts;
	
	
	public BusinessPartner() {
		this.accounts = new ArrayList<>();
	}
	
	public BusinessPartner(String name, String idNumber, PartnerTypes partnerType) {
		this.name = name;
		this.idNumber = idNumber;
		this.partnerType = partnerType;
		this.accounts = new ArrayList<>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * @return the partnerType
	 */
	public PartnerTypes getPartnerType() {
		return partnerType;
	}

	/**
	 * @param partnerType the partnerType to set
	 */
	public void setPartnerType(PartnerTypes partnerType) {
		this.partnerType = partnerType;
	}

	/**
	 * @return the partnerId
	 */
	public Long getPartnerId() {
		return partnerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
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
		BusinessPartner other = (BusinessPartner) obj;
		if (idNumber == null) {
			if (other.idNumber != null)
				return false;
		} else if (!idNumber.equals(other.idNumber))
			return false;
		return true;
	}
	
}
