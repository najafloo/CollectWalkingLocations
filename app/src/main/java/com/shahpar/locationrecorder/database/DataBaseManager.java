package com.shahpar.locationrecorder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {

    public DataBaseManager(@Nullable Context context) {
        super(context, DatabaseMetaData.DB_NAME, null, DatabaseMetaData.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_POINTS_TABLE = "CREATE TABLE " + DatabaseMetaData.TABLE_POINTS +
                " (" +
                DatabaseMetaData.POINT_ID_KEY + " Integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DatabaseMetaData.POINT_ADDRESS_KEY + " Text NOT NULL, " +
                DatabaseMetaData.POINT_PHONE_KEY + " Text NOT NULL, " +
                DatabaseMetaData.POINT_LATITUDE_KEY + " Text NOT NULL, " +
                DatabaseMetaData.POINT_LONGITUDE_KEY + " Text NOT NULL, " +
                DatabaseMetaData.POINT_DESCRIPTION_KEY + " Text " +
                " )";

        Log.d("SANDBADCELL", "query" + CREATE_POINTS_TABLE);

        sqLiteDatabase.execSQL(CREATE_POINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseMetaData.TABLE_POINTS);
        }
    }
}
