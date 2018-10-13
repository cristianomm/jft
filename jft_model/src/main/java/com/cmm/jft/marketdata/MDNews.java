/**
 * 
 */
package com.cmm.jft.marketdata;

import java.time.LocalDateTime;
import java.util.Date;

import com.cmm.jft.vo.Extractable;

/**
 * <p><code>NewsVO.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 10:12:55 PM
 * @updated 04-nov-2015 13:06:59
 */
public class MDNews implements Extractable {

	private String newsID;
	private LocalDateTime origTime;
	private String newsSrc;
	private String headLine;
	private String text;
	private String url;
	
	
	/**
	 * 
	 * @param newsID
	 * @param dateTime
	 * @param newsSrc
	 * @param headLine
	 * @param text
	 */
	public MDNews(String newsID, LocalDateTime dateTime, String newsSrc, String headLine, String text) {
		super();
		this.newsID = newsID;
		this.origTime = dateTime;
		this.newsSrc = newsSrc;
		this.text = text;
		this.headLine = headLine;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNewsID() {
		return newsID;
	}

	public LocalDateTime getOrigTime() {
		return origTime;
	}

	public String getNewsSrc() {
		return newsSrc;
	}

	public String getHeadLine() {
		return headLine;
	}

	public String getText() {
		return text;
	}
	
	
}
