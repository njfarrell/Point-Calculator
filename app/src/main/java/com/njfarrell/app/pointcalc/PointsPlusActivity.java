package com.njfarrell.app.pointcalc;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.njfarrell.app.pointcalc.account.UserAccountFragment;
import com.njfarrell.app.pointcalc.caculators.DailyPointCalcFragment;
import com.njfarrell.app.pointcalc.preferences.PointCalculatorPrefs;

import java.util.UUID;

public class PointsPlusActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_calculator);

        PointCalculatorPrefs prefs = PointCalculatorPrefs.newInstance(this);
        String username = prefs.getStringValue(PointCalculatorPrefs.KEY_USER_NAME, null);

        if (username == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DailyPointCalcFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new UserAccountFragment())
                    .commit();
        }
    }

    public static String generateGuid() {
        return UUID.randomUUID().toString();
    }

    public static String getAccountName(Context context) {
        PointCalculatorPrefs prefs = PointCalculatorPrefs.newInstance(context);
        return prefs.getStringValue(PointCalculatorPrefs.KEY_USER_NAME, null);
    }
}