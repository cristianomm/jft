/**
 * 
 */
package com.cmm.jft.vo;

import java.util.Date;

/**
 * <p><code>NewsVO.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 10:12:55 PM
 * @updated 04-nov-2015 13:06:59
 */
public class NewsVO implements Extractable {

	private String newsID;
	private Date dataTime;
	private String text;
	private String headLine;
	
	
	public NewsVO(String newsID, Date dataTime, String text, String headLine) {
		super();
		this.newsID = newsID;
		this.dataTime = dataTime;
		this.text = text;
		this.headLine = headLine;
	}


	/**
	 * @return the newsID
	 */
	public String getNewsID() {
		return newsID;
	}


	/**
	 * @return the dataTime
	 */
	public Date getDataTime() {
		return dataTime;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @return the headLine
	 */
	public String getHeadLine() {
		return headLine;
	}


	/**
	 * @param newsID the newsID to set
	 */
	public void setNewsID(String newsID) {
		this.newsID = newsID;
	}


	/**
	 * @param dataTime the dataTime to set
	 */
	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * @param headLine the headLine to set
	 */
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	
	
	
}
