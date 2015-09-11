/**
 * 
 */
package com.cmm.jft.financial;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p>
 * <code>CostCenter.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/02/2015 14:43:29
 */
@Entity
@Table(name = "CostCenter", schema="Financial")
public class CostCenter {

	@Id
	@SequenceGenerator(name = "COSTCENTER_SEQ", sequenceName = "COSTCENTER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "COSTCENTER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "costCenterID", nullable = false)
	private Long costCenterID;

	@Column(name = "Name", length = 250)
	private String name;

}
