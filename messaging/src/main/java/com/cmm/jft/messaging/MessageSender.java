/**
 * 
 */
package com.cmm.jft.messaging;

import quickfix.Message;
import quickfix.SessionID;

/**
 * Os objetos que implementam esta interface sao responsaveis por enviar
 * mensagens para a instancia do objeto <code>MessageRepository</code>
 * 
 * <p><code>MessageSender.java</code></p>
 * @author Cristiano M Martins
 * @version 28 de jul de 2015 22:15:09
 *
 */
public interface MessageSender {

	
	/**
	 * Este metodo e responsavel por enviar mensagens para o 
	 * <code>MessageRepository</code>, retornando o status do envio da 
	 * mensagem para o repositorio.
	 * @return <code>true</code> caso a mensagem seja enviada ou <code>false</code>
	 * caso contrario.  
	 */
	boolean sendMessage(Message message, SessionID sessionID);
	
}
