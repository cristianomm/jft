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
 * <code>Index</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Index")
@NamedQueries({
		@NamedQuery(name = "Index.findAll", query = "SELECT i FROM Index i"),
		@NamedQuery(name = "Index.findByIndexID", query = "SELECT i FROM Index i WHERE i.indexID = :indexID") })
public class Index implements DBObject<Index> {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name = "INDEX_SEQ", table = "SEQUENCE", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "INDEX_SEQ", strategy = GenerationType.TABLE)
	@Basic(optional = false)
	@Column(name = "indexID", nullable = false)
	private Long indexID;

	@Column(name = "Index", length = 12, unique = true)
	private String index;

	@Column(name = "IndexName", length = 100)
	private String indexName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "indexID", fetch = FetchType.LAZY, targetEntity = IndexComposition.class)
	private Set<IndexComposition> indexCompositionSet;

	public Index() {
		this.indexCompositionSet = new HashSet<IndexComposition>();
	}

	public Index(String index) {
		this.index = index;
		this.indexCompositionSet = new HashSet<IndexComposition>();
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return this.indexName;
	}

	/**
	 * @param indexName
	 *            the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the indexID
	 */
	public Long getIndexID() {
		return this.indexID;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return this.index;
	}

	/**
	 * @return the indexCompositionSet
	 */
	public Set<IndexComposition> getIndexCompositionSet() {
		return this.indexCompositionSet;
	}

}
