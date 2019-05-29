/**
 * 
 */
package com.cmm.jft.model.security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cmm.jft.model.security.enums.SecurityEventTypes;
import com.cmm.jft.model.util.JpaConverters;

/**
 * <p>
 * <code>SecurityEvent.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 17, 2019
 * 
 */
@Entity
@Table(name="SecurityEvent", schema = "Security")
public class SecurityEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long securityEventId;
	
	@JoinColumn(name="securityId", referencedColumnName="securityId", nullable=false)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Security securityId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="Type", nullable = false)
	private SecurityEventTypes type;
	
	@Column(name="Date")
	@Convert(converter=JpaConverters.LocalDateTimeConverter.class)
	private LocalDateTime date;
		
	@Column(name="Description", length=200)
	private String description;
	
	@Column(name="Value", precision=19, scale=6)
	private double value;
	
}
