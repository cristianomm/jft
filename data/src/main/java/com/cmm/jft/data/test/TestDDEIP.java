package com.cmm.jft.data.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLConnection;

import sun.awt.im.InputMethodContext;

public class TestDDEIP {

	public static void main(String[] args) {
		
		try {
			Socket con = new Socket("189.90.10.144", 80);
			
			System.out.println(con.isConnected());
			
			InputStream is = con.getInputStream();
			while(is.available()>0){
				byte[] data = new byte[is.available()];
				is.read(data);
				System.out.println(new String(data));
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
