package com.twaddle.covid19.model;

import com.google.gson.annotations.SerializedName;

public class UserDetails {
    @SerializedName( "username" )
    private String UUID;
    @SerializedName( "mail" )
    private String mail;
    @SerializedName( "name" )
    private String name;
    @SerializedName( "age" )
    private int age;
    @SerializedName( "latitude" )
    private double latitude;
    @SerializedName( "longitude" )
    private double longitude;

    public UserDetails(String UUID, String mail, String name, int age, double latitude, double longitude) {
        this.UUID = UUID;
        this.mail = mail;
        this.name = name;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}
