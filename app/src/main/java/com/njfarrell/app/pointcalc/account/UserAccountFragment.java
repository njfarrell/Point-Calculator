/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njfarrell.app.pointcalc.R;
import com.njfarrell.app.pointcalc.caculators.DailyPointCalcFragment;
import com.njfarrell.app.pointcalc.caculators.FoodPointCalcFragment;
import com.njfarrell.app.pointcalc.calendar.CalendarViewFragment;
import com.njfarrell.app.pointcalc.preferences.PointCalculatorPrefs;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class UserAccountFragment extends Fragment implements View.OnClickListener {

    private static final String POUND = "lb";
    private static final String INCH = "in";
    private static final String FOOT = "ft";

    String mUserName;
    int mDailyPoints;
    double mBMI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getPreferenceData();

        View v = inflater.inflate(R.layout.layout_user_account, null);

        if (v != null) {
            TextView nameView = (TextView) v.findViewById(R.id.user_account_name);
            if (mUserName != null) {
                String username = mUserName.substring(0,1).toUpperCase() + mUserName.substring(1, mUserName.length());
                nameView.setText(username);
            }
            TextView pointView = (TextView) v.findViewById(R.id.daily_points_value);
            if (mDailyPoints > 0) {
                pointView.setText(String.valueOf(mDailyPoints));
            }

            TextView bmiView = (TextView) v.findViewById(R.id.daily_bmi_value);
            if (mBMI > 0) {
                bmiView.setText(String.format("%.2f", mBMI));
            }

            RelativeLayout calculatePoints = (RelativeLayout) v.findViewById(R.id.calculate_food_points);
            calculatePoints.setOnClickListener(this);

            RelativeLayout viewCalendar = (RelativeLayout) v.findViewById(R.id.view_calendar);
            viewCalendar.setOnClickListener(this);

            RelativeLayout calculateBMI = (RelativeLayout) v.findViewById(R.id.calculate_bmi);
            calculateBMI.setOnClickListener(this);

            RelativeLayout switchAccount = (RelativeLayout) v.findViewById(R.id.switch_account);
            switchAccount.setOnClickListener(this);
        }

        return v;
    }

    private void getPreferenceData() {
        PointCalculatorPrefs prefs = PointCalculatorPrefs.newInstance(getActivity());
        mUserName = prefs.getStringValue(PointCalculatorPrefs.KEY_USER_NAME, null);
        mDailyPoints = prefs.getIntValue(PointCalculatorPrefs.KEY_DAILY_POINTS, 0);
        double weight = prefs.getDoubleValue(PointCalculatorPrefs.KEY_USER_WEIGHT, 0.0d);
        String weightUnit = prefs.getStringValue(PointCalculatorPrefs.KEY_WEIGHT_UNIT, null);
        double height = prefs.getDoubleValue(PointCalculatorPrefs.KEY_USER_HEIGHT, 0.0d);
        String heightUnit = prefs.getStringValue(PointCalculatorPrefs.KEY_HEIGHT_UNIT, null);
        calculateBMI(weight, weightUnit, height, heightUnit);
    }

    private void calculateBMI(double weight, String weightUnit, double height, String heightUnit) {
        double userWeight = convertUnit(weight, weightUnit);
        double userHeight = convertUnit(height, heightUnit);
        mBMI = (userWeight) / (userHeight * userHeight);
    }

    private double convertUnit(double value, String unit) {
        double result = value;
        if (POUND.equals(unit)) {
            result = result / 2.2046;
        }
        if (INCH.equals(unit)) {
            result = result / 39.370;
        }
        if (FOOT.equals(unit)) {
            result = result / 3.2808;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.calculate_food_points:
                getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FoodPointCalcFragment())
                            .addToBackStack(null)
                            .commit();
                break;
            case R.id.view_calendar:
                getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new CalendarViewFragment())
                            .addToBackStack(null)
                            .commit();
                break;
            case R.id.calculate_bmi:
                break;
            case R.id.switch_account:
                getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new DailyPointCalcFragment())
                            .addToBackStack(null)
                            .commit();
                break;
        }
    }

//    public class AccountGridAdapter extends BaseAdapter implements View.OnClickListener {
//
//        private int[] mImageResources = new int[] {
//                R.drawable.calculate_food_points,
//                R.drawable.view_calendar,
//                R.drawable.calculate_bmi,
//                R.drawable.switch_users
//        };
//        private Context mContext;
//
//        public AccountGridAdapter(Context context) {
//            super();
//            mContext = context;
//        }
//
//        @Override
//        public int getCount() {
//            return mImageResources.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View gridItem;
//            LayoutInflater inflater = (LayoutInflater) mContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            gridItem = inflater.inflate(R.layout.screen_accountgridcell, parent, false);
//
//            if (gridItem != null) {
//                final RelativeLayout gridView = (RelativeLayout) gridItem.findViewById(
//                        R.id.account_options_view);
//                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//                Display display = wm.getDefaultDisplay();
//                DisplayMetrics metrics = new DisplayMetrics();
//                display.getMetrics(metrics);
//                int width = metrics.widthPixels/2;
//                AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, width);
//                gridView.setLayoutParams(params);
//
//                final ImageView image = (ImageView) gridItem.findViewById(R.id.account_options_image);
//                image.setImageResource(mImageResources[position]);
//                image.setTag(position);
//                image.setOnClickListener(this);
//                image.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View view, MotionEvent motionEvent) {
//                        if (gridView != null) {
//                            switch (motionEvent.getAction()) {
//                                case MotionEvent.ACTION_DOWN:
//                                case MotionEvent.ACTION_MOVE:
//                                case MotionEvent.ACTION_HOVER_ENTER:
//                                case MotionEvent.ACTION_HOVER_MOVE:
//                                case MotionEvent.ACTION_HOVER_EXIT:
//                                    gridView.setAlpha(0.5f);
//                                    break;
//                                case MotionEvent.ACTION_UP:
//                                default:
//                                    gridView.setAlpha(1.0f);
//                                    break;
//                            }
//                        }
//                        return false;
//                    }
//                });
//            }
//
//            return gridItem;
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = (Integer) v.getTag();
//            switch (position) {
//                case 0:
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, new FoodPointCalcFragment())
//                            .addToBackStack(null)
//                            .commit();
//                    break;
//                case 1:
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, new CalendarViewFragment())
//                            .addToBackStack(null)
//                            .commit();
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, new DailyPointCalcFragment())
//                            .addToBackStack(null)
//                            .commit();
//                    break;
//            }
//        }
//    }
}
