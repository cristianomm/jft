/**
 * 
 */
package com.cmm.jft.ui.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <p><code>ImageIcons.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 2, 2015 2:15:14 AM
 *
 */
public class ImageIcons {
	
	private static ImageView imgDOM;
	private static ImageView imgBook;
	private static ImageView imgMoney;
	private static ImageView imgPlaceOrder;
	private static ImageView imgOrderManager;
	private static ImageView imgTimeSales;
	private static ImageView imgProgram;
	private static ImageView imgSecurity;
	private static ImageView imgChart;
	
	public static ImageView getDOMImage(){
		if(imgDOM==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/table.png"));
			imgDOM = new ImageView(img);
		}
		return imgDOM;
	}
	
	public static ImageView getBookImage(){
		if(imgBook==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/book.png"));
			imgBook = new ImageView(img);
		}
		return imgBook;
	}
	
	public static ImageView getPlaceOrderImage(){
		if(imgPlaceOrder==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/ticket.png"));
			imgPlaceOrder = new ImageView(img);
		}
		return imgPlaceOrder;
	}
	
	public static ImageView getOrderManagerImage(){
		if(imgOrderManager==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/clipboard-task.png"));
			imgOrderManager = new ImageView(img);
		}
		return imgOrderManager;
	}
	
	public static ImageView getTimeSalesImage(){
		if(imgTimeSales==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/bank.png"));
			imgTimeSales = new ImageView(img);
		}
		return imgTimeSales;
	}
	
	
	public static ImageView getMoneyImage(){
		if(imgMoney==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/money.png"));
			imgMoney = new ImageView(img);
		}
		return imgMoney;
	}

	public static ImageView getProgramImage(){
		if(imgProgram==null){
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/category.png"));
			imgProgram = new ImageView(img);
		}
		return imgProgram;
	}
	
	public static ImageView getSecurityImage(){
		if (imgSecurity == null) {
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/book-open-list.png"));
			imgSecurity = new ImageView(img);
		}
		return imgSecurity;
	}
	
	public static ImageView getChartImage(){
		if (imgChart == null) {
			Image img = new Image(ImageIcons.class.getResourceAsStream("../../../../../image/icons/chart-up.png"));
			imgChart = new ImageView(img);
		}
		return imgChart;
	}
	
}
