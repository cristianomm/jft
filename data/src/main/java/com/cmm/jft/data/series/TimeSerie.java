/**
 * 
 */
package com.cmm.jft.data.series;

import java.util.Date;
import java.util.TreeMap;

/**
 * <p><code>TimeSerie.java</code></p>
 * @author Cristiano
 * @version 12/04/2015 00:55:06
 *
 */
public class TimeSerie {

	
	private TreeMap<Date, SeriePoint> points;
	
	
	/**
	 * 
	 */
	public TimeSerie() {
		this.points = new TreeMap<Date, SeriePoint>();
	}
	
	/**
	 * @return the points
	 */
	public TreeMap<Date, SeriePoint> getPoints() {
		return this.points;
	}
	
	public void addPoint(Date time, SeriePoint point){
		points.put(time, point);
	}
	
	public SeriePoint getPoint(Date time){
		return points.get(time);
	}
	
}
