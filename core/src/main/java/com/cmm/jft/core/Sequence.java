package com.cmm.jft.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Sequence")
public class Sequence {

	@Id
	@Column(nullable = false)
	private String sequence_name;

	@Column
	private Long sequence_next_hi_value;

}
