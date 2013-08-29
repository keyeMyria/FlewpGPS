package com.flewp.gps;

import java.util.Date;
import java.util.TimerTask;

import android.location.Location;

public class GPSTimerTask extends TimerTask{
	
	@Override
	public void run() {
		if(GPSService.db.getAllGPSData().size() == 0 || GPSService.hasNewLocation) {
			Location l = GPSService.currLocation;
			GPSData gps = new GPSData(new Date().getTime(), l.getLongitude(), l.getLatitude(), l.getAltitude(), false);
			GPSService.db.insertGPS(gps);
			GPSService.hasNewLocation = false;
		}
	}
}
