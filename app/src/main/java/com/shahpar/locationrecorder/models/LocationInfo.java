package com.shahpar.locationrecorder.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationInfo implements Parcelable {
    String address;
    String phone;
    String description;
    String latitude;
    String longitude;

    protected LocationInfo(Parcel in) {
        address = in.readString();
        phone = in.readString();
        description = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public LocationInfo(String address, String phone, String description, String latitude, String longitude) {
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(description);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
    }
}
