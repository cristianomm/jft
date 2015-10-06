/**
 * 
 */
package com.cmm.jft.connector.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * <p><code>ServerSocket.java</code></p>
 * @author cristiano
 * @version Oct 5, 2015 10:55:21 PM
 *
 */
public class Server {
	
	
	public static void main(String[] args){
		new Server().start();
	}
	
	
	private boolean started;
	private ServerSocket socketServer;
	
	
	public void start(){
		try {
			socketServer = new ServerSocket(3264);
			started = true;
			acceptConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void acceptConnections(){
		new Thread(new Acceptor()).start();
		System.out.println("Server started.");
	}
	
	private class Acceptor implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(started){
				try {
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					Socket s = socketServer.accept();
					s.getChannel().read(buffer);
					
					System.out.println(buffer.asCharBuffer().toString());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}
