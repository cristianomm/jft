/**
 * 
 */
package com.cmm.jft.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * <code>MarketIndex.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 8, 2019
 * 
 */
@Entity
@Table(name = "MarketIndex", schema = "Security")
public class MarketIndex {
	
	@Id
	@Column(name = "marketIndexId", length = 5)
	private String marketIndexId;
	
	@Column(name = "Name", length = 100)
	private String name;
	
	@Column(name = "Description", length = 255)
	private String decription;
	
	@Column(name = "Provider", length = 100)
	private String provider;
	
	@Column(name = "ProviderLink", length = 255)
	private String providerLink;
	
	/**
	 * 
	 */
	public MarketIndex() {
		
	}
	
	public MarketIndex(String marketIndexCode) {
		this.marketIndexId = marketIndexCode;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 46;
		int result = 1;
		result = prime * result + ((marketIndexId == null) ? 0 : marketIndexId.hashCode());
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
		MarketIndex other = (MarketIndex) obj;
		if (marketIndexId == null) {
			if (other.marketIndexId != null)
				return false;
		} else if (!marketIndexId.equals(other.marketIndexId))
			return false;
		return true;
	}

	/**
	 * @return the marketIndexId
	 */
	public String getMarketIndexId() {
		return marketIndexId;
	}

	/**
	 * @param marketIndexId the marketIndexId to set
	 */
	public void setMarketIndexId(String marketIndexId) {
		this.marketIndexId = marketIndexId;
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
	 * @return the decription
	 */
	public String getDecription() {
		return decription;
	}

	/**
	 * @param decription the decription to set
	 */
	public void setDecription(String decription) {
		this.decription = decription;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the providerLink
	 */
	public String getProviderLink() {
		return providerLink;
	}

	/**
	 * @param providerLink the providerLink to set
	 */
	public void setProviderLink(String providerLink) {
		this.providerLink = providerLink;
	}
		
}
