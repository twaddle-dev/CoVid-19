package com.twaddle.covid19.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.twaddle.covid19.utils.Constants.PREF_NAME;

public class PrefManager {
    private final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // shared pref mode

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
}
