/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class PointCalculatorPrefs {

    public static final String POINT_CALC_PREF_FILE = "point_calc_prefs";

    public static final String KEY_USER_NAME = "username";
    public static final String KEY_DAILY_POINTS = "daily_points";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_WEIGHT = "weight";
    public static final String KEY_USER_HEIGHT = "height";
    public static final String KEY_USER_AGE = "age";
    public static final String KEY_WEIGHT_UNIT = "weight_unit";
    public static final String KEY_HEIGHT_UNIT = "height_unit";

    static PointCalculatorPrefs sSelf;

    Context mContext;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEdit;

    public static PointCalculatorPrefs newInstance(Context context) {
        if (sSelf == null) {
            sSelf = new PointCalculatorPrefs(context);
        }
        return sSelf;
    }

    private PointCalculatorPrefs(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(POINT_CALC_PREF_FILE, Context.MODE_PRIVATE);
        mEdit = mPrefs.edit();
    }

    public void setDailyCalculation(int dailyPoints) {
        mEdit.putInt(KEY_DAILY_POINTS, dailyPoints);
        mEdit.commit();
    }

    public void setUserAccountInfo(String username, String gender, String weightUnit,
                                   String heightUnit, double weight, double height, int age) {
        mEdit.putString(KEY_USER_NAME, username);
        mEdit.putString(KEY_USER_GENDER, gender);
        mEdit.putString(KEY_WEIGHT_UNIT, weightUnit);
        mEdit.putString(KEY_HEIGHT_UNIT, heightUnit);
        mEdit.putLong(KEY_USER_WEIGHT, Double.doubleToLongBits(weight));
        mEdit.putLong(KEY_USER_HEIGHT, Double.doubleToLongBits(height));
        mEdit.putInt(KEY_USER_AGE, age);
        mEdit.commit();
    }

    public int getIntValue(String key, int defaultValue) {
        return mPrefs.getInt(key, defaultValue);
    }

    public String getStringValue(String key, String defaultValue) {
       return mPrefs.getString(key, defaultValue);
    }

    public double getDoubleValue(String key, double defaultValue) {
        return Double.longBitsToDouble(mPrefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
