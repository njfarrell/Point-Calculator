/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.widget;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class DataPreferences {

    private static final String PREFS_WIGET_DATA = "widget_data_prefs";
    public static final String KEY_TOTAL_CARBS = "total_carbs";
    public static final String KEY_TOTAL_FAT = "total_fat";
    public static final String KEY_TOTAL_PROTEIN = "total_protein";
    public static final String KEY_TOTAL_FIBER = "total_fiber";
    public static final String DATA_KEY = "data_key";

    private static final String ZERO_VALUE = "0";

    private static DataPreferences sSelf;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEd;

    private DataPreferences(Context context) {
        mPref = context.getSharedPreferences(PREFS_WIGET_DATA, Context.MODE_PRIVATE);
        mEd = mPref.edit();
    }

    public static DataPreferences newInstance(Context context) {
        if (sSelf == null) {
            sSelf = new DataPreferences(context);
        }
        return sSelf;
    }

    public boolean saveValue(String key, String value) {
        mEd.putString(key, value);
        return mEd.commit();
    }

    public String getValue(String key, String defVal) {
        return mPref.getString(key, defVal);
    }

    public boolean resetAllData() {
        mEd.putString(KEY_TOTAL_CARBS, ZERO_VALUE);
        mEd.putString(KEY_TOTAL_FAT, ZERO_VALUE);
        mEd.putString(KEY_TOTAL_PROTEIN, ZERO_VALUE);
        mEd.putString(KEY_TOTAL_FIBER, ZERO_VALUE);
        return mEd.commit();
    }
}
