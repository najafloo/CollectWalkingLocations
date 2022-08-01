package com.shahpar.locationrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shahpar.locationrecorder.models.LocationInfo;

public class ShowDetailActivity extends AppCompatActivity {

    AppCompatTextView txt_detail_address;
    AppCompatTextView txt_detail_phone;
    AppCompatTextView txt_detail_latitude;
    AppCompatTextView txt_detail_longitude;
    AppCompatTextView txt_detail_description;

    AppCompatImageView img_call;
    AppCompatImageView img_share;

    final int CALL_REQUEST_CODE = 12;
    final static String GOOGLE_MAP_PRE_LINK = "http://maps.google.com/maps?q=loc:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        txt_detail_address = findViewById(R.id.txt_detail_address);
        txt_detail_phone = findViewById(R.id.txt_detail_phone);
        txt_detail_latitude = findViewById(R.id.txt_detail_latitude);
        txt_detail_longitude = findViewById(R.id.txt_detail_longitude);
        txt_detail_description = findViewById(R.id.txt_detail_description);
        img_call = findViewById(R.id.img_call);
        img_share = findViewById(R.id.img_share);

        LocationInfo locationInfo = getIntent().getExtras().getParcelable("info");

        txt_detail_address.setText(locationInfo.getAddress());
        txt_detail_phone.setText(locationInfo.getPhone());
        txt_detail_latitude.setText(locationInfo.getLatitude());
        txt_detail_longitude.setText(locationInfo.getLongitude());
        txt_detail_description.setText(locationInfo.getDescription());

        img_call.setOnClickListener(view -> {
            if (hasCallPermission()) {
                Log.d("SANDBADCELL", "================= has permission");
                makeAcall();
            }
        });

        img_share.setOnClickListener(view -> {
            if (hasCallPermission()) {
                Log.d("SANDBADCELL", "================= has permission");
                shareLocation();
            }
        });

        Log.d("SANDBADCELL", locationInfo.getAddress() + " , " + locationInfo.getDescription());
    }

    public boolean hasCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            return true;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeAcall();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void makeAcall() {
        if (txt_detail_phone.getText() != null && !txt_detail_phone.getText().toString().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txt_detail_phone.getText().toString()));
            startActivity(intent);
        }
    }

    private void shareLocation() {

        String info = "";
        if (txt_detail_address.getText() != null && !txt_detail_address.getText().toString().isEmpty())
            info = txt_detail_address.getText() + "\n";

        String link = GOOGLE_MAP_PRE_LINK + txt_detail_latitude.getText() + "," + txt_detail_longitude.getText();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, info + link);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}