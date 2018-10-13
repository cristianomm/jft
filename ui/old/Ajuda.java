/**
 * 
 */
package com.cmm.jft.ui;

/**
 * <p><code>Ajuda.java</code></p>
 * @author Cristiano
 * @version 1 de set de 2015 15:37:02
 *
 */
public class Ajuda {

	public static void main(String[] args){
		String[] keys = {
		"abstract", 	
		"continue", 	
		"for", 	
		"new", 	
		"switch", 
		"assert", 
		"default", 
		"goto", 
		"package", 	
		"synchronized", 
		"boolean", 
		"do", 
		"if", 
		"private", 
		"this", 
		"break", 
		"double", 
		"implements", 	
		"protected", 
		"throw", 
		"byte", 
		"else", 
		"import", 
		"public", 
		"throws", 
		"case", 
		"enum", 
		"instanceof", 
		"return", 
		"transient", 
		"catch", 
		"extends", 
		"int", 
		"short", 
		"try", 
		"char", 
		"final", 
		"interface", 
		"static", 
		"void", 
		"class", 
		"finally", 
		"long", 
		"strictfp", 
		"volatile", 
		"const", 
		"float", 
		"native", 
		"super", 
		"while"};
	
		int c=100;
		for(String k:keys){
			System.out.println("public static int " + k.toUpperCase()+" = " + c++ + ";");
		}
		
		for(String k:keys){
			System.out.println(k + "\t\t{return " + k.toUpperCase() + ";}");
		}
		
		
		
	}
	
	
	
}
