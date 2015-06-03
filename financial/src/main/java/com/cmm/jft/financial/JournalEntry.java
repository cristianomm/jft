/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import com.cmm.jft.financial.enums.JournalStatus;
import com.cmm.jft.db.DBObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * <p>
 * <code>JournalEntry</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "JournalEntry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "JournalEntry.findAll", query = "SELECT e FROM JournalEntry e"),
		@NamedQuery(name = "JournalEntry.findByEntryID", query = "SELECT e FROM JournalEntry e WHERE e.entryID = :entryID"),
		@NamedQuery(name = "JournalEntry.findByEntryDate", query = "SELECT e FROM JournalEntry e WHERE e.entryDate = :entryDate"),
		@NamedQuery(name = "JournalEntry.findByEntryClose", query = "SELECT e FROM JournalEntry e WHERE e.entryClose = :entryClose"),
		@NamedQuery(name = "JournalEntry.findByDescription", query = "SELECT e FROM JournalEntry e WHERE e.description = :description"),
		@NamedQuery(name = "JournalEntry.findByEntryStatus", query = "SELECT e FROM JournalEntry e WHERE e.journalStatus = :journalStatus") })
public class JournalEntry implements Serializable, DBObject<JournalEntry> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "JOURNALENTRY_SEQ", sequenceName = "JOURNALENTRY_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "JOURNALENTRY_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "entryID", nullable = false)
	private Long entryID;

	@Basic(optional = false)
	@Column(name = "EntryDate", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date entryDate;

	@Column(name = "EntryClose")
	@Temporal(TemporalType.TIMESTAMP)
	private Date entryClose;

	@Column(name = "Description", length = 255)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "JournalStatus")
	private JournalStatus journalStatus;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entryID", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterSet;

	public JournalEntry() {
		this.journalStatus = JournalStatus.OPEN;
		this.entryDate = new Date();
		this.entryRegisterSet = new HashSet<EntryRegister>();
	}

	public JournalEntry(Date entryDate) {
		this.journalStatus = JournalStatus.OPEN;
		this.entryDate = entryDate;
		this.entryRegisterSet = new HashSet<EntryRegister>();
	}

	/**
	 * @return the entryDate
	 */
	public Date getEntryDate() {
		return this.entryDate;
	}

	/**
	 * @param entryDate
	 *            the entryDate to set
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * @return the entryClose
	 */
	public Date getEntryClose() {
		return this.entryClose;
	}

	/**
	 * @param entryClose
	 *            the entryClose to set
	 */
	public void setEntryClose(Date entryClose) {
		this.entryClose = entryClose;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the journalStatus
	 */
	public JournalStatus getJournalStatus() {
		return this.journalStatus;
	}

	/**
	 * @param journalStatus
	 *            the journalStatus to set
	 */
	public void setJournalStatus(JournalStatus journalStatus) {
		this.journalStatus = journalStatus;
	}

	/**
	 * @return the entryID
	 */
	public Long getEntryID() {
		return this.entryID;
	}

	/**
	 * @return the entryRegisterSet
	 */
	public Set<EntryRegister> getEntryRegisterSet() {
		return this.entryRegisterSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JournalEntry ["
				+ (this.entryID != null ? "entryID=" + this.entryID + ", " : "")
				+ (this.entryDate != null ? "entryDate=" + this.entryDate
						+ ", " : "")
				+ (this.entryClose != null ? "entryClose=" + this.entryClose
						+ ", " : "")
				+ (this.description != null ? "description=" + this.description
						+ ", " : "")
				+ (this.journalStatus != null ? "journalStatus="
						+ this.journalStatus + ", " : "")
				+ (this.entryRegisterSet != null ? "entryRegisterSet="
						+ this.entryRegisterSet : "") + "]";
	}

}
