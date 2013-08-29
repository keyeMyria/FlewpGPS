package com.flewp.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
public class GPSLocationListener implements LocationListener{

	@Override
	public void onLocationChanged(Location location) {
		if(GPSService.hasNewLocation == false) {
			GPSService.currLocation = location;
			GPSService.currBestAccuracy = location.getAccuracy();
			GPSService.hasNewLocation = true;
		} 
		else {
			if(location.getAccuracy() < GPSService.currBestAccuracy) {
				GPSService.currLocation = location;
				GPSService.currBestAccuracy = location.getAccuracy();
				GPSService.hasNewLocation = true;
			}
		}
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
