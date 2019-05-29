package com.cmm.jft.model.financial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name="BankAccount", schema="Financial")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long bankAccountId;
	
	@Column(name = "Agency", length=50, nullable = false)
	private String agency;
	
	@Column(name = "Digit")
	private char digit;
	
	private int bankId;
	
	private String accountId;
	
}
