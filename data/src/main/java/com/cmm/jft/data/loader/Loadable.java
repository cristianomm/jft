/**
 * 
 */
package com.cmm.jft.data.loader;


/**
 * <p>
 * <code>Loadable.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 07/08/2013 01:51:57
 *
 */
public interface Loadable {

	String importFiles(String dirName);

	/**
	 * Reads a file with contains data to load
	 * 
	 * @param fileName
	 *            String que contem o nome do arquivo a ser importado
	 * @return 0 caso o arquivo seja importado com sucesso ou -1 caso ocorra
	 *         algum erro ao importar o arquivo.
	 */
	int importData(String fileName);

}
