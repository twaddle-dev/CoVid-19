package com.twaddle.covid19.model;

import com.google.gson.annotations.SerializedName;

public class PeriodicLocations {
    @SerializedName( "timestamp" )
    private long timestamp;
    @SerializedName( "latitude" )
    private double latitude;
    @SerializedName( "longitude" )
    private double longitude;

    public PeriodicLocations(long timestamp, double latitude, double longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
