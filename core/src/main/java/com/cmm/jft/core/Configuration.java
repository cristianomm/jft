/**
 * 
 */
package com.cmm.jft.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Properties;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBObject;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>Configuration.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 09/08/2013 16:08:34
 *
 */
@Entity
@Table(name = "Configuration")
//@NamedQueries({
//		@NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c"),
//		@NamedQuery(name = "Configuration.findByConfigurationID", query = "SELECT c FROM Configuration c WHERE c.configurationID = :configurationID") })
public class Configuration implements DBObject<Configuration> {

	@Id
	@SequenceGenerator(name = "CONFIGURATION_SEQ", sequenceName = "CONFIGURATION_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "CONFIGURATION_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "configurationID", nullable = false)
	private Long configurationID;

	private String propertyFileName;
	private Properties properties;
	private static Configuration instance;
	
	/**
     * 
     */
	private Configuration() {
		this.properties = new Properties();
		try {
			Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("jftcfg.properties").toURI());
			
			propertyFileName = path.toString();
			this.properties.load(new FileInputStream(path.toFile()));
			
		} catch (FileNotFoundException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (IOException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (URISyntaxException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	public Object getConfiguration(String configName){
		Object config = null;
		
		if(properties.containsKey(configName)){
			config = properties.get(configName);
		}else{
			Logging.getInstance().log(getClass(), "Configuration: " + configName + " not found.", Level.WARN);
		}
		return config;
	}
	
	public void addConfiguration(String configName, Object object){
		properties.put(configName, object);
	}
	
	public void save(){
		try {
			properties.store(new FileOutputStream(new File(propertyFileName)), "Configuration File");
		} catch (FileNotFoundException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (IOException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}
	
	
}
