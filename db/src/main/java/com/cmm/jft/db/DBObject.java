/**
 * 
 */
package com.cmm.jft.db;

import java.io.Serializable;

import com.cmm.jft.db.exceptions.DataBaseException;

/**
 * <p><code>DBObject.java</code></p>
 * @author Cristiano Martins
 * @version 07/08/2013 16:49:25
 *
 */

public interface DBObject<T> extends Serializable {
    
	/*
    T add() throws DataBaseException;
    
    T update() throws DataBaseException;
    
    T remove() throws DataBaseException;
    
    T loadByKey(Object key) throws DataBaseException;
    */
}
