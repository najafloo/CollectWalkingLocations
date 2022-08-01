package com.shahpar.locationrecorder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shahpar.locationrecorder.baseclass.MyApplication;
import com.shahpar.locationrecorder.customview.CustomMessageBox;
import com.shahpar.locationrecorder.models.LocationInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab_get_current_location;
    AppCompatTextView txt_latitude;
    AppCompatTextView txt_longitude;
    AppCompatEditText txt_address;
    AppCompatEditText txt_phone;
    AppCompatEditText txt_description;
    ProgressBar pb_progressBar;
    Toolbar toolbar;

    AppCompatButton btn_show;
    AppCompatButton btn_save;

    final static int STORAGE_EXTERNAL_CODE = 4455;
    final static int LOCATION_SERVICE_CODE = 10;

    final String inner_separator = "###\n";
    final String outer_separator = "----------\n";
    final String SUB_DIR_NAME = "export";

    double latitude = 0.0;
    double longitude = 0.0;

    Context context;
    String logFileName;
//    GPSTracker gps;

    LocationManager locationManager;
    LocationListener locationListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        txt_latitude = findViewById(R.id.txt_latitude);
        txt_longitude = findViewById(R.id.txt_longitude);
        txt_address = findViewById(R.id.txt_address);
        txt_phone = findViewById(R.id.txt_phone);
        txt_description = findViewById(R.id.txt_description);
        toolbar = findViewById(R.id.toolbar);
        pb_progressBar = findViewById(R.id.pb_progressBar);

        setSupportActionBar(toolbar);

        btn_show = findViewById(R.id.btn_show);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(view -> saveData());

        fab_get_current_location = findViewById(R.id.fab_get_current_location);
        fab_get_current_location.setOnClickListener(view -> {
            txt_latitude.setText(String.valueOf(latitude));
            txt_longitude.setText(String.valueOf(longitude));
        });

        btn_show.setOnClickListener(view -> {
            if (MyApplication.databaseAdapter.getDataCount() != 0) {
                Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                if (txt_longitude.getText().equals("0.0") || txt_latitude.getText().equals("0.0")) {
                    pb_progressBar.setVisibility(View.GONE);
                    txt_latitude.setText(String.valueOf(latitude));
                    txt_longitude.setText(String.valueOf(longitude));
                }

                Log.d("SANDBADCELL", "++++++++++++++ " + latitude + "," + longitude);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
                Log.d("SANDBADCELL", "error on location");
                showSettingsAlert();
            }
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, LOCATION_SERVICE_CODE);
            }
            return;
        } else {
            loadLocation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_export:
                if (hasStoragePermission()) {
                    exportData();
                }
                return true;
            case R.id.menu_clear:
                clearViewData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean hasStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_EXTERNAL_CODE);
        return false;
    }

    private boolean makeExportDirectory(String root) {
        File directory = new File(root + "/" + SUB_DIR_NAME);
        if (!directory.exists() && !directory.isDirectory()) {
            if (directory.mkdirs()) {
                Log.i("SANDBADCELL", "App dir created");
                return true;
            } else {
                Log.w("SANDBADCELL", "Unable to create app dir!");
                return false;
            }
        } else {
            Log.i("SANDBADCELL", "App dir already exists");
            return true;
        }
    }

    private boolean writeToFile(String baseDir, String subDir, String data) {
        if (!makeExportDirectory(baseDir)) {
            Toast.makeText(context, "Unable to create app dir!\n" + baseDir + "/" + SUB_DIR_NAME, Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            File exportFile = new File(baseDir + subDir);

            Log.d("SANDBADCELL","%%%%%%%%%%%%%%%% ============= output = " + data);

            FileOutputStream fo = new FileOutputStream(exportFile, true);
            try {
                fo.write(data.getBytes(), 0, data.getBytes().length);
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }

        return true;
    }

    private void exportData() {
        List<LocationInfo> locationInfoList = MyApplication.databaseAdapter.getAllLocations();

        if (locationInfoList != null && !locationInfoList.isEmpty()) {
            logFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            String subDir = "/" + SUB_DIR_NAME + "/" + logFileName + ".txt";

            boolean result = true;
            for (LocationInfo info : locationInfoList) {
                String output = info.getLatitude() + "," + info.getLongitude() + "\n" + inner_separator +
                        info.getAddress() + "\n" + inner_separator +
                        info.getPhone() + "\n" + inner_separator +
                        info.getDescription() + "\n" + inner_separator +
                        outer_separator;

                result &= writeToFile(baseDir, subDir, output);
            }

            if (result)
                Toast.makeText(context, getString(R.string.export_successful) + "\n" + baseDir + subDir, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_data_to_export, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_EXTERNAL_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportData();
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == LOCATION_SERVICE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLocation();
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void loadLocation() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    public void showSettingsAlert() {
        new CustomMessageBox.Builder(this)
                .message(getString(R.string.setting_message))
                .confirmText(getString(R.string.settings))
                .cancelText(getString(R.string.cancell))
                .messageDialogType(CustomMessageBox.MessageDialogType.MDP_WARRNING)
                .setListener(new CustomMessageBox.MessageBoxListener() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                    }
                })
                .build()
                .show();
    }

    void clearViewData() {
        txt_address.setText("");
        txt_phone.setText("");
        txt_description.setText("");
        txt_latitude.setText("0.0");
        txt_longitude.setText("0.0");
        latitude = 0.0;
        longitude = 0.0;
    }

    private void saveData() {
        String address = txt_address.getText().toString();
        String phone = txt_phone.getText().toString();
        String description = txt_description.getText().toString();
        String latitude = txt_latitude.getText().toString();
        String longitude = txt_longitude.getText().toString();

        Log.d("SANDBADCELL", address + " , " + description + " , " + latitude + " , " + longitude);

        if (latitude.equals("0.0") || longitude.equals("0.0")) {
            Toast.makeText(this, R.string.wait_gps, Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(this, R.string.fill_data, Toast.LENGTH_SHORT).show();
        } else {
            LocationInfo locationInfo = new LocationInfo(address, phone, description, latitude, longitude);
            if (MyApplication.databaseAdapter.insertNewRow(locationInfo) > 0) {
                Toast.makeText(this, R.string.saved_successful, Toast.LENGTH_SHORT).show();
                clearViewData();
            }
        }
    }
}