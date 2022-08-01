package com.shahpar.locationrecorder.database;

public class DatabaseMetaData {

    // DateBase info
    public final static int VERSION = 1;
    public final static String DB_NAME = "database.db";

    // Tables
    public final static String TABLE_POINTS = "tbl_points";

    // Points Table
    public static final String POINT_ID_KEY = "id";
    public static final String POINT_ADDRESS_KEY = "address";
    public static final String POINT_PHONE_KEY = "phone";
    public static final String POINT_LATITUDE_KEY = "latitude";
    public static final String POINT_LONGITUDE_KEY = "longitude";
    public static final String POINT_DESCRIPTION_KEY = "description";
}
