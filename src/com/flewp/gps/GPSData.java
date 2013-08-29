package com.flewp.gps;

import java.util.Date;

public class GPSData implements Comparable<GPSData>{
	public Date date;
	public long time;
	public double longitude;
	public double latitude;
	public double altitude;
	public boolean uploaded;
	public GPSData(long t, double lon, double lat, double alt, boolean u) {
		time = t;
		longitude = lon;
		latitude = lat;
		altitude = alt;
		uploaded = u;
		date = new Date(t);
	}
	@Override
	public int compareTo(GPSData arg0) {
		return (int)(this.time - arg0.time);
	}
}
