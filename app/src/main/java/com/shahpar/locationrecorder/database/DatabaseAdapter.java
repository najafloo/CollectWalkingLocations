package com.shahpar.locationrecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.shahpar.locationrecorder.models.LocationInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends DataBaseManager{

    public DatabaseAdapter(@Nullable Context context) {
        super(context);
    }

    public long insertNewRow(LocationInfo locationInfo) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseMetaData.POINT_ADDRESS_KEY, locationInfo.getAddress());
        contentValues.put(DatabaseMetaData.POINT_PHONE_KEY, locationInfo.getPhone());
        contentValues.put(DatabaseMetaData.POINT_LATITUDE_KEY, locationInfo.getLatitude());
        contentValues.put(DatabaseMetaData.POINT_LONGITUDE_KEY, locationInfo.getLongitude());
        contentValues.put(DatabaseMetaData.POINT_DESCRIPTION_KEY, locationInfo.getDescription());

        return db.insert(DatabaseMetaData.TABLE_POINTS, null, contentValues);
    }

    public List<LocationInfo> getAllLocations() {
        List<LocationInfo> locationInfoList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseMetaData.TABLE_POINTS;
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMetaData.POINT_ADDRESS_KEY));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMetaData.POINT_PHONE_KEY));
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMetaData.POINT_LATITUDE_KEY));
            String longitude = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMetaData.POINT_LONGITUDE_KEY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMetaData.POINT_DESCRIPTION_KEY));
            locationInfoList.add(new LocationInfo(address, phone, description, latitude, longitude));
        }
        cursor.close();

        return locationInfoList;
    }

    public int getDataCount() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseMetaData.TABLE_POINTS;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        Log.d("SANDBADCELL", "count of data = " + count);
        return count;
    }
}
