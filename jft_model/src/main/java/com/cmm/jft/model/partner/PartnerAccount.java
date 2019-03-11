package com.cmm.jft.model.partner;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.Account;

@Entity
@Table(name="PartnerAccount", schema = "Partner")
public class PartnerAccount implements DBObject<PartnerAccount> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long partnerAccountId;
	
	@JoinColumn(name = "partnerId", referencedColumnName = "partnerId", nullable = true)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private BusinessPartner partnerId;
	
	@Column(name = "Balance", precision=19, scale=8)
	private BigDecimal balance;
	
	@JoinColumn(name = "creditAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Account creditAccountId;
	
	@JoinColumn(name = "debitAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Account debitAccountId;
	
	public PartnerAccount() {
		
	}

	/**
	 * @return the partnerId
	 */
	public BusinessPartner getPartnerId() {
		return partnerId;
	}

	/**
	 * @param partnerId the partnerId to set
	 */
	public void setPartnerId(BusinessPartner partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the creditAccountId
	 */
	public Account getCreditAccountId() {
		return creditAccountId;
	}

	/**
	 * @param creditAccountId the creditAccountId to set
	 */
	public void setCreditAccountId(Account creditAccountId) {
		this.creditAccountId = creditAccountId;
	}

	/**
	 * @return the debitAccountId
	 */
	public Account getDebitAccountId() {
		return debitAccountId;
	}

	/**
	 * @param debitAccountId the debitAccountId to set
	 */
	public void setDebitAccountId(Account debitAccountId) {
		this.debitAccountId = debitAccountId;
	}

	/**
	 * @return the partnerAccountId
	 */
	public Long getPartnerAccountId() {
		return partnerAccountId;
	}
	
}
