package com.shahpar.locationrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shahpar.locationrecorder.adapters.PointsAdapter;
import com.shahpar.locationrecorder.baseclass.MyApplication;
import com.shahpar.locationrecorder.models.LocationInfo;
import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    RecyclerView rcv_point_items;
    PointsAdapter pointsAdapter;

    List<LocationInfo> locationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        rcv_point_items = findViewById(R.id.rcv_point_items);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_point_items.setLayoutManager(linearLayoutManager);

        locationInfoList = new ArrayList<>();
        locationInfoList = MyApplication.databaseAdapter.getAllLocations();

        if (!locationInfoList.isEmpty()) {
            pointsAdapter = new PointsAdapter((ArrayList<LocationInfo>) locationInfoList);
            pointsAdapter.setViewHolderListener(new PointsAdapter.ViewHolderListener() {
                @Override
                public void onItemClicked(int itemPosition) {
                    Log.d("SANDBADCELL", "item clicked " + itemPosition);

                    Intent intent = new Intent(getApplicationContext(), ShowDetailActivity.class);

                    intent.putExtra("info", locationInfoList.get(itemPosition));

                    startActivity(intent);
                }
            });
            rcv_point_items.setAdapter(pointsAdapter);
        }
    }
}