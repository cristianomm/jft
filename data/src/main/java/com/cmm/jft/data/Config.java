/**
 * 
 */
package com.cmm.jft.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.cmm.jft.data.exceptions.InvalidConfigurationException;

/**
 * <p>
 * <code>Config.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/09/2013 14:11:08
 *
 */
public class Config {

	private Properties properties;

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	public Config(String propertiesFile) throws FileNotFoundException,
			IOException {
		this.properties = new Properties();
		this.properties.load(new FileInputStream(propertiesFile));
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return this.properties;
	}

	public String getConfig(String configName)
			throws InvalidConfigurationException {
		if (properties.containsKey(configName)) {
			return properties.getProperty(configName);
		}
		throw new InvalidConfigurationException("" + configName);
	}

}
