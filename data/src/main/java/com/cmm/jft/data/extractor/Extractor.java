/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * Os objetos que implementam esa interface devem realizar a extracao de informacoes
 * contida em uma fonte, como por exemplo um arquivo csv, um FTP, uma pagina web...
 * o metodo <code>config()</code> e utilizado para passar parametros de configuracao
 * para o extrator tais como a URL ou nome do arquivo. Apos realizada a configuracao com sucesso
 * devese utilizar o metodo <code>exract()</code> para retornar as informacoes que 
 * os extratores concretos disponibilizarao atraves desse metodo.
 * 
 * <p><code>Extractor.java</code></p>
 * @author Cristiano M Martins
 * @version 02/03/2015 14:51:42
 *
 */
public interface Extractor {
	
	
	static boolean checkKeyWords(Properties properties, String ...keyWords){
		boolean ret = false;
		try{
			ret = properties.keySet().containsAll(Arrays.asList(keyWords));
		}catch(ClassCastException e){
			
		}catch(NullPointerException e){
			
		}
		
		return ret;
	}
	
	
	/**
	 * Realiza a configuracao do <code>Extractor</code> de acordo com as informacoes contidas
	 * no objeto <code>Properties</code>.
	 * @param properties objeto <code>Properties</code> contendo a informacao necessaria para realizar a extracao.
	 * @return <code>true</code> caso tenha realizado a configuracao do extrator com sucesso ou <code>false</code> caso contrario.
	 */
	boolean config(Properties properties);
	
	/**
	 * Realiza a extracao de informacoes de uma fonte, retornando uma lista com os
	 * objetos extraidos. Caso nao exista nada para ser extraido ou ocorra algum erro
	 * ira retornar uma lista vazia.
	 * @return <code>List</code> contendo os elementos extraidos ou uma lista vazia 
	 * caso nao exista nada para extrair ou ocorra erro.
	 */
	List<Extractable> extract();
	
}
