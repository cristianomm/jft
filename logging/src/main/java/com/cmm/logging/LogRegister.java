/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.logging;

import java.io.Serializable;

/**
 *
 * <p><code>LogRegister</code></p>
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
//@Entity
//@Table(name = "LogRegister")
//@NamedQueries({
//	@NamedQuery(name = "LogRegister.findAll", query = "SELECT l FROM LogRegister l"),
//	@NamedQuery(name = "LogRegister.findByLogRegisterID", query = "SELECT l FROM LogRegister l WHERE l.logRegisterID = :logRegisterID"),
//	@NamedQuery(name = "LogRegister.findByLDateTime", query = "SELECT l FROM LogRegister l WHERE l.lDateTime = :lDateTime"),
//	@NamedQuery(name = "LogRegister.findByDescription", query = "SELECT l FROM LogRegister l WHERE l.description = :description")})
public class LogRegister implements Serializable {
	private static final long serialVersionUID = 1L;
//	@Id
//	@SequenceGenerator(name = "LOGREGISTER_SEQ", sequenceName="LOGREGISTER_SEQ", allocationSize = 1, initialValue = 1) 
//	@GeneratedValue(generator = "LOGREGISTER_SEQ", strategy = GenerationType.AUTO)
//	@Basic(optional = false)
//	@Column(name = "logRegisterID", nullable = false)
	private Long logRegisterID;
//	@Column(name = "LDateTime", length = 10)
	private String lDateTime;
//	@Column(name = "Description", length = 1)
	private String description;

	public LogRegister() {
	}

	public LogRegister(Long logRegisterID) {
		this.logRegisterID = logRegisterID;
	}

	public Long getLogRegisterID() {
		return logRegisterID;
	}

	public void setLogRegisterID(Long logRegisterID) {
		this.logRegisterID = logRegisterID;
	}

	public String getLDateTime() {
		return lDateTime;
	}

	public void setLDateTime(String lDateTime) {
		this.lDateTime = lDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (logRegisterID != null ? logRegisterID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LogRegister)) {
			return false;
		}
		LogRegister other = (LogRegister) object;
		if ((this.logRegisterID == null && other.logRegisterID != null) || (this.logRegisterID != null && !this.logRegisterID.equals(other.logRegisterID))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LogRegister ["
				+ (this.logRegisterID != null ? "logRegisterID="
						+ this.logRegisterID + ", " : "")
				+ (this.lDateTime != null ? "lDateTime=" + this.lDateTime
						+ ", " : "")
				+ (this.description != null ? "description=" + this.description
						: "") + "]";
	}


	//    /* (non-Javadoc)
	//     * @see com.cmm.jft_core.DBObject#add()
	//     */
	//    @Override
	//    public LogRegister add() throws DataBaseException {
	//	return (LogRegister) DBFacade.getInstance()._persist(this);
	//    }
	//
	//    /* (non-Javadoc)
	//     * @see com.cmm.jft_core.DBObject#update()
	//     */
	//    @Override
	//    public LogRegister update() throws DataBaseException {
	//	return (LogRegister) DBFacade.getInstance()._update(this);
	//    }
	//
	//    /* (non-Javadoc)
	//     * @see com.cmm.jft_core.DBObject#remove()
	//     */
	//    @Override
	//    public LogRegister remove() throws DataBaseException {
	//	return (LogRegister) DBFacade.getInstance()._remove(this);
	//    }
	//
	//    /* (non-Javadoc)
	//     * @see com.cmm.jft_core.DBObject#loadByKey(java.lang.Object)
	//     */
	//    @Override
	//    public LogRegister loadByKey(Object key) throws DataBaseException {
	//	return (LogRegister) DBFacade.getInstance()._findByKey(getClass(), key);
	//    }

}
