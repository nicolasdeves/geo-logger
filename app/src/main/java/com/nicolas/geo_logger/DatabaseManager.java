package com.nicolas.geo_logger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "geo_logger_db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE location (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "latitude REAL, " +
                            "longitude REAL, " +
                            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS location;";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertLocation(double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO location (latitude, longitude) VALUES (?, ?)";
        db.execSQL(sql, new Object[]{latitude, longitude});
        db.close();
    }

    public int getQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM location";
        int count = 0;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return count;
    }

    public List<LatLng> getLastHourRoute() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT latitude, longitude FROM location WHERE timestamp >= DATETIME('now', '-1 hour') ORDER BY timestamp DESC";
        Cursor cursor = db.rawQuery(sql, null);

        List<LatLng> route = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                route.add(new LatLng(latitude, longitude));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return route;
    }

    public void cleanData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from location");
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SQL);
        onCreate(db);
    }
}

