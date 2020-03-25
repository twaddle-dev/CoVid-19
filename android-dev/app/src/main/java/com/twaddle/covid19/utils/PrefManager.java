package com.twaddle.covid19.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.twaddle.covid19.utils.Constants.PREF_NAME;

public class PrefManager {
    private final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private final String HOME_LATITUDE = "home_latitude";
    private final String HOME_LONGITUDE = "home_longitude";
    private final String IS_PRESENT_IN_HOME = "is_present_in_home";
    // shared pref mode

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }
    public boolean isPresentInHome() {
        return pref.getBoolean(IS_PRESENT_IN_HOME, true);
    }

    public void setPresentInHome(boolean isPresent) {
        editor.putBoolean(IS_PRESENT_IN_HOME, isPresent);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public Float getHomeLatitude() {
        return pref.getFloat(HOME_LATITUDE, 0);
    }
    public void setHomeLatitude(Float latitude) {
        editor.putFloat(HOME_LATITUDE, latitude);
        editor.commit();
    }

    public Float getHomeLongitude() {
        return pref.getFloat(HOME_LONGITUDE, 0);
    }
    public void setHomeLongitude(Float Longitude) {
        editor.putFloat(HOME_LONGITUDE, Longitude);
        editor.commit();
    }
}
