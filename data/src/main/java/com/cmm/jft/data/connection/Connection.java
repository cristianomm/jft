/**
 * 
 */
package com.cmm.jft.data.connection;

import com.cmm.jft.data.exceptions.ConnectionException;

/**
 * <p><code>Connection.java</code></p>
 * @author COC-DELL-02
 * @version 11/02/2015 15:31:33
 *
 */
public interface Connection {
	
	/**
	 * Aguarda o recebimento de um acontecimento da conexao, retornando este
	 * acontecimento como um evento.
	 * 
	 * @return Informacao do evento ocorrido na conexao.
	 * @throws ConnectionException
	 */
	Event receiveEvent() throws ConnectionException;

	/**
	 * Envia um Evento para a conexao
	 * 
	 * @param event
	 *            Evento com todas informacoes necessarias para registro do
	 *            Evento na conexao.
	 * @return <code>Event</code> contendo a informacao do envio do Evento.
	 * @throws ConnectionException
	 *             Caso ocorra algum erro.
	 */
	Event sendEvent(Event event) throws ConnectionException;
	
	Event connect();
	
}
