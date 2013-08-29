package com.flewp.gps;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener{

	public static Context _this;
	private ArrayList<GPSData> gpsArray;
	private GPSArrayAdapter gpsAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.activity_main);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView lengthText = (TextView)findViewById(R.id.length_text);
		lengthText.setText("Number of Locations: " + GPSService.getNumLocations());
		initializeListView();
		startService(new Intent(MainActivity._this, GPSService.class));
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menuStart:
        	startService(new Intent(MainActivity._this, GPSService.class));
            return true;
        case R.id.menuStop:
        	stopService(new Intent(MainActivity._this, GPSService.class));
            return true;
        case R.id.menuRefresh:
        	TextView lengthText = (TextView)findViewById(R.id.length_text);
			lengthText.setText("Number of Locations: " + GPSService.getNumLocations());
			Toast.makeText(_this, "Refreshed", Toast.LENGTH_SHORT).show();
			
			initializeListView();
            return true;
        case R.id.menuSearchDate:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	private void initializeListView() {
		ListView gpsListView = (ListView)findViewById(R.id.gps_listview);
		gpsArray = GPSService.getAllGPSData();
		gpsAdapter = new GPSArrayAdapter(this, gpsArray);
		gpsListView.setEmptyView(findViewById(R.id.emptyList));
		gpsListView.setOnItemClickListener(this);
		gpsListView.setAdapter(gpsAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> list, View v, int position, long id) {
		GPSData gps = (GPSData)list.getItemAtPosition(position);
		String label = "Location";
		String uriBegin = "geo:" + gps.latitude + "," + gps.longitude;
		String query = gps.latitude + "," + gps.longitude + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
	}

}
