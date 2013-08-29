package com.flewp.gps;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GPSDatabase extends SQLiteOpenHelper {

	private SQLiteDatabase db;
	
	private static int DB_VERSION = 1;
	private static String DB_NAME = "gps.db";
	private static String DB_TABLE = "gps_table";
	
	private static String KEY_ROW_ID = "row_id";
	private static String KEY_LAT = "lat";
	private static String KEY_LONG = "long";
	private static String KEY_ALT = "alt";
	private static String KEY_TIME = "time";
	private static String KEY_UPLOADED = "uploaded";
	
	public GPSDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		openGPSDB();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SECTION_TABLE = "CREATE TABLE " + DB_TABLE + "(" + KEY_ROW_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LAT + " REAL," + KEY_LONG
                + " REAL," + KEY_ALT + " REAL," + KEY_TIME + " INTEGER, " + KEY_UPLOADED + " INTEGER)";
        db.execSQL(CREATE_SECTION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
	}
	
	private void openGPSDB() {
		if(db == null) {
			db = getWritableDatabase();
		}
	}
	
	public void closeGPSDB() {
		if(db != null) {
			db.close();
			db = null;
		}
	}
	
	private GPSData cursorToGPSData(Cursor c) {
		double lat = c.getDouble(1);
		double lon = c.getDouble(2);
		double alt = c.getDouble(3);
		long time = c.getLong(4);
		boolean uploaded = c.getInt(5) == 1;
		return new GPSData(time, lon, lat, alt, uploaded);
	}
	
	public int insertGPS(GPSData gps) {
        openGPSDB();
        ContentValues values = new ContentValues();
        values.put(KEY_LONG, gps.longitude);
        values.put(KEY_LAT, gps.latitude);
        values.put(KEY_ALT, gps.altitude);
        values.put(KEY_TIME, gps.time);
        values.put(KEY_UPLOADED, gps.uploaded);
        int rowID = (int) db.insert(DB_TABLE, null, values);

        return rowID;
    }
	
	public int updateGPS(GPSData gps) {
		ContentValues values = new ContentValues();
		values.put(KEY_LONG, gps.longitude);
        values.put(KEY_LAT, gps.latitude);
        values.put(KEY_ALT, gps.altitude);
        values.put(KEY_TIME, gps.time);
        values.put(KEY_UPLOADED, gps.uploaded);
        String where = KEY_TIME = "=?";
        String[] whereArgs = {String.valueOf(gps.time)};
		return db.update(DB_TABLE, values, where, whereArgs);
	}
	
	public GPSData getGPSByTimeStamp(long timeStamp) {
		openGPSDB();
		String select = KEY_TIME + "=?";
        String[] match = { String.valueOf(timeStamp) };
        Cursor cursor = db.query(DB_TABLE, null, select, match, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            GPSData gps = cursorToGPSData(cursor);
            cursor.close();
            return gps;
        }
		return null;
	}
	
	public ArrayList<GPSData> getAllGPSData() {
		openGPSDB();
        ArrayList<GPSData> allSections = new ArrayList<GPSData>();
        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GPSData gps = cursorToGPSData(cursor);
            allSections.add(gps);
            cursor.moveToNext();
        }
        cursor.close();
        Collections.sort(allSections);
        return allSections;
	}
}
