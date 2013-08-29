package com.flewp.gps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GPSArrayAdapter extends ArrayAdapter<GPSData>{

	private Activity activity;
	private ViewHolder vh;
	
	public ArrayList<GPSData> gpsList;
	
	public static class ViewHolder {
		public TextView locationView;
		public TextView dateView;
	}
	public GPSArrayAdapter(Activity act, List<GPSData> list) {
		super(act, com.flewp.gps.R.layout.listview_gps);
		if(list != null) {
			gpsList = new ArrayList<GPSData>(list);
		} else {
			gpsList = new ArrayList<GPSData>();
		}
		activity = act;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		GPSData gps = gpsList.get(position);
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
	        row = inflater.inflate(R.layout.listview_gps, null);
	        vh = new ViewHolder();
	        vh.locationView = (TextView) row.findViewById(R.id.gps_location);
	        vh.dateView = (TextView) row.findViewById(R.id.gps_time);
	        row.setTag(vh);
		} else {
			row.setTag(vh);
		}
		
		vh.locationView.setText("(" + gps.latitude + ", " + gps.longitude + ")");
		vh.dateView.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US).format(gps.date));
		return row;
	}
	
	@Override
    public int getCount() {
        return gpsList.size();
    }

    @Override
    public GPSData getItem(int position) {
        return gpsList.get(position);
    }
}
