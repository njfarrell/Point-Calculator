/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.caculators;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.njfarrell.app.pointcalc.dialog.DropdownDialog;
import com.njfarrell.app.pointcalc.preferences.PointCalculatorPrefs;
import com.njfarrell.app.pointcalc.R;
import com.njfarrell.app.pointcalc.account.UserAccountFragment;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class DailyPointCalcFragment extends Fragment implements View.OnClickListener,
        DropdownDialog.DropdownDialogListener {

    private static final String DROPDOWN_GENDER = "dropdown_gender";
    private static final String DROPDOWN_WEIGHT = "dropdown_weight";
    private static final String DROPDOWN_HEIGHT = "dropdown_height";

    //Gender constants
    private static final String MALE = "male";
    private static final String FEMALE = "female";

    //Weight unit constants
    private static final String POUND = "lb";

    //Height unit constants
    private static final String INCH = "in";
    private static final String FOOT = "ft";

    EditText mUserView;
    EditText mWeightView;
    EditText mAgeView;
    EditText mHeightView;
    String mWeightUnit;
    String mHeightUnit;
    String mGender;
    String mDropdownSelected;

    boolean mIsEditMode = false;

    PointCalculatorPrefs mPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPrefs = PointCalculatorPrefs.newInstance(getActivity());
        final String username = mPrefs.getStringValue(PointCalculatorPrefs.KEY_USER_NAME, null);
        mGender = mPrefs.getStringValue(PointCalculatorPrefs.KEY_USER_GENDER, null);
        if (mGender == null) {
            mGender = "male";
        }
        mWeightUnit = mPrefs.getStringValue(PointCalculatorPrefs.KEY_WEIGHT_UNIT, null);
        if (mWeightUnit == null) {
            mWeightUnit = "kg";
        }
        mHeightUnit = mPrefs.getStringValue(PointCalculatorPrefs.KEY_HEIGHT_UNIT, null);
        if (mHeightUnit == null) {
            mHeightUnit = "m";
        }
        final double weight = mPrefs.getDoubleValue(PointCalculatorPrefs.KEY_USER_WEIGHT, 0.0D);
        final double height = mPrefs.getDoubleValue(PointCalculatorPrefs.KEY_USER_HEIGHT, 0.0D);
        final int age = mPrefs.getIntValue(PointCalculatorPrefs.KEY_USER_AGE, 0);

        View v = inflater.inflate(R.layout.layout_daily_point_calculator, null);

        if (v != null) {
            mUserView = (EditText) v.findViewById(R.id.username);
            if (username != null) {
                mIsEditMode = true;
                mUserView.setText(username);
            }
            mWeightView = (EditText) v.findViewById(R.id.user_weight);
            if (weight > 0) {
                mWeightView.setText("" + weight);
            }
            mAgeView = (EditText) v.findViewById(R.id.user_age);
            if (age > 0) {
                mAgeView.setText("" + age);
            }
            mHeightView = (EditText) v.findViewById(R.id.user_height);
            if (height > 0) {
                mHeightView.setText("" + height);
            }

            RelativeLayout genderLayout = (RelativeLayout) v.findViewById(R.id.user_gender);
            genderLayout.setOnClickListener(this);
            TextView genderText = (TextView) genderLayout.findViewById(R.id.gender_text);
            if (mGender != null) {
                genderText.setText(mGender);
            }

            RelativeLayout weightUnitLayout = (RelativeLayout) v.findViewById(R.id.weight_unit);
            weightUnitLayout.setOnClickListener(this);
            TextView weightUnitText = (TextView) weightUnitLayout.findViewById(R.id.weight_text);
            if(mWeightUnit != null) {
                weightUnitText.setText(mWeightUnit);
            }

            RelativeLayout heightUnitLayout = (RelativeLayout) v.findViewById(R.id.height_unit);
            heightUnitLayout.setOnClickListener(this);
            TextView heightUnitText = (TextView) heightUnitLayout.findViewById(R.id.height_text);
            if(mHeightUnit != null) {
                heightUnitText.setText(mHeightUnit);
            }

            Button submitButton = (Button) v.findViewById(R.id.submit_button);
            submitButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                String name = mUserView.getText().toString();
                String weightHolder = mWeightView.getText().toString();
                String ageHolder = mAgeView.getText().toString();
                String heightHolder = mHeightView.getText().toString();

                if (name != null && weightHolder != null && ageHolder != null && heightHolder != null) {
                    double weight = 0.0;
                    double height = 0.0;
                    int age = 0;
                    if (!weightHolder.equals("")) {
                        weight = Double.valueOf(weightHolder);
                    }
                    if (!heightHolder.equals("")) {
                        height = Double.valueOf(heightHolder);
                    }
                    if (!ageHolder.equals("")) {
                        age = Integer.valueOf(ageHolder);
                    }

                    if (weight > 0 && height > 0 && age > 0) {
                        mPrefs.setUserAccountInfo(name, mGender, mWeightUnit, mHeightUnit, weight, height, age);
                        calcDailyPoints(weight, height, age);
                    } else {
                        Toast.makeText(getActivity(), R.string.required_fields, Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.required_fields, Toast.LENGTH_SHORT ).show();
                }
                break;
            case R.id.user_gender:
                mDropdownSelected = DROPDOWN_GENDER;
                DropdownDialog genderDialog = new DropdownDialog(getActivity(), "male", "female", null,
                        null, this);
                genderDialog.show();
                break;
            case R.id.weight_unit:
                mDropdownSelected = DROPDOWN_WEIGHT;
                DropdownDialog weightDialog = new DropdownDialog(getActivity(), "kg", "lb", null,
                    null, this);
                weightDialog.show();
                break;
            case R.id.height_unit:
                mDropdownSelected = DROPDOWN_HEIGHT;
                DropdownDialog heightDialog = new DropdownDialog(getActivity(), "m", "in", "ft",
                        null, this);
                heightDialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Handle image coming back from intent
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void calcDailyPoints(double weight, double height, int age) {
        int calculation;

        if (POUND.equals(mWeightUnit)) {
            weight = weight / 2.2046;
        }

        if (INCH.equals(mHeightUnit)) {
            height = height / 39.370;
        } else if (FOOT.equals(mHeightUnit)) {
            height = height / 3.2808;
        }

        double result = 0.0;
        if (MALE.equals(mGender)) {
            result = (864 - (9.72 * age) + 1.12 * (14.2 * weight + 503.0 * height));
        } else if (FEMALE.equals(mGender)) {
            result = (387 - (7.31 * age) + 1.14 * (10.9 * weight + 660.7 * height));
        }

        result = 0.9 * result + 200;
        calculation = (int) Math.min(
                Math.max(
                Math.round(
                Math.max(
                        result - 1000, 1000) / 35) -7 - 4, 26), 71);

        mPrefs.setDailyCalculation(calculation);
        if (mIsEditMode) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new UserAccountFragment())
                    .commit();
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onDialogItemSelected(String value) {
        if (DROPDOWN_GENDER.equals(mDropdownSelected)) {
            mGender = value;
            ((TextView) getActivity().findViewById(R.id.gender_text)).setText(value);
        } else if (DROPDOWN_WEIGHT.equals(mDropdownSelected)) {
            mWeightUnit = value;
            ((TextView) getActivity().findViewById(R.id.weight_text)).setText(value);
        } else if (DROPDOWN_HEIGHT.equals(mDropdownSelected)) {
            mHeightUnit = value;
            ((TextView) getActivity().findViewById(R.id.height_text)).setText(value);
        }
    }
}
