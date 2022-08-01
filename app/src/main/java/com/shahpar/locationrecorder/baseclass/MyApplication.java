package com.shahpar.locationrecorder.baseclass;

import android.app.Application;
import android.content.Context;

import com.shahpar.locationrecorder.database.DatabaseAdapter;

public class MyApplication extends Application {

    public static DatabaseAdapter databaseAdapter;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        databaseAdapter = new DatabaseAdapter(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
