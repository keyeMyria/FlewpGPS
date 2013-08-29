package com.flewp.gps;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class GPSService extends Service {

	public static Context ctx;
	public static LocationManager lm;
	public static final int ONE_SECOND = 1000;
	public static final long MIN_UPDATE_TIME = 30*ONE_SECOND;
	public static final float MIN_UPDATE_DISTANCE = 50;
	
	public static Location currLocation;
	public static float currBestAccuracy;
	public static boolean hasNewLocation;
	public static boolean started;
	private static Timer timer;
	public static GPSDatabase db;
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "Started FlewpGPS Service", Toast.LENGTH_SHORT).show();
		if(!started) {
			ctx = this;
			db = new GPSDatabase(this);
			lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, new GPSLocationListener());
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, new GPSLocationListener());
			currLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			started = true;
			//numAttempts = 0;
			//json = null;
			
			timer = new Timer();
			timer.scheduleAtFixedRate(new GPSTimerTask(), 1000, 60000);
		}
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Stopped FlewpGPS Service", Toast.LENGTH_SHORT).show();
		started = false;
		timer.cancel();
		db.closeGPSDB();
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
	
	public static void updateLocation(Location l) {
		if(l.getAccuracy() < currLocation.getAccuracy()) {
			currLocation = l;
			hasNewLocation = true;
		}
	}

	public static int getNumLocations() {
		if(db != null) {
			return db.getAllGPSData().size();
		}
		return 0;
	}
	
	public static ArrayList<GPSData> getAllGPSData() {
		if(db != null) {
			return db.getAllGPSData();
		}
		
		return null;
	}
}
