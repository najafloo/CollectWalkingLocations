package com.shahpar.locationrecorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.shahpar.locationrecorder.R;
import com.shahpar.locationrecorder.models.LocationInfo;

import java.util.ArrayList;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    ArrayList<LocationInfo> locationInfoList;
    private ViewHolderListener viewHolderListener;

    public PointsAdapter(ArrayList<LocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    public interface ViewHolderListener {
        void onItemClicked(int itemPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationInfo item = locationInfoList.get(position);
        holder.txt_vh_phone.setText(item.getAddress());
        holder.txt_vh_latitude.setText(item.getLatitude());
        holder.txt_vh_longitude.setText(item.getLongitude());
    }

    @Override
    public int getItemCount() {
        return locationInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView txt_vh_latitude;
        AppCompatTextView txt_vh_longitude;
        AppCompatTextView txt_vh_phone;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txt_vh_latitude = itemView.findViewById(R.id.txt_vh_latitude);
            txt_vh_longitude = itemView.findViewById(R.id.txt_vh_longitude);
            txt_vh_phone = itemView.findViewById(R.id.txt_vh_title);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            viewHolderListener.onItemClicked(position);
        }
    }
}
