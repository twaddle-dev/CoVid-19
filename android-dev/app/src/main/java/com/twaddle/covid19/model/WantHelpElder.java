package com.twaddle.covid19.model;

import com.google.gson.annotations.SerializedName;

public class WantHelpElder {
    @SerializedName( "latitude" )
    private double latitude;
    @SerializedName( "longitude" )
    private double longitude;
    @SerializedName( "item" )
    private String item;

    public WantHelpElder(double latitude, double longitude, String item) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.item = item;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

