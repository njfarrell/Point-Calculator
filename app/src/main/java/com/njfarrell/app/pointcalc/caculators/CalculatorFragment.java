/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.caculators;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njfarrell.app.pointcalc.R;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_LAYOUT_ID = "layout_id";
    private static final String KEY_VALUE = "value";

    private CalculatorClickListener mCalcListener;
    private TextView mDisplayText;

    public static CalculatorFragment newInstance(int layoutId, String value) {
        CalculatorFragment frag = new CalculatorFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT_ID, layoutId);
        args.putString(KEY_VALUE, value);
        frag.setArguments(args);

        return frag;
    }

    public void setCalculatorClickListener(CalculatorClickListener listener) {
        mCalcListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_widget_calculator, null);

        int layoutId = R.id.carbs_layout;
        String value = "0";
        Bundle args = getArguments();
        if (args != null) {
            layoutId = args.getInt(KEY_LAYOUT_ID);
            value = args.getString(KEY_VALUE);
        }

        RelativeLayout background = (RelativeLayout) v.findViewById(R.id.background_layout);
        background.setBackgroundColor(getResources().getColor(R.color.deep_blue));

        mDisplayText = (TextView) v.findViewById(R.id.display_text);
        mDisplayText.setTextSize(22);
        mDisplayText.setText(value);

        LinearLayout gradientBackground = (LinearLayout) v.findViewById(R.id.calculator_background);
        switch (layoutId) {
            case R.id.carbs_layout:
                gradientBackground.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.carb_calculator_background));
                break;
            case R.id.fat_layout:
                gradientBackground.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.fat_calculator_background));
                break;
            case R.id.protein_layout:
                gradientBackground.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.protein_calculator_background));
                break;
            case R.id.fiber_layout:
                gradientBackground.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.fiber_calculator_background));
                break;
            default:
                gradientBackground.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.carb_calculator_background));
                break;
        }


        Button numberZero = (Button) v.findViewById(R.id.number_0);
        numberZero.setTextSize(20);
        numberZero.setOnClickListener(this);
        Button numberOne = (Button) v.findViewById(R.id.number_1);
        numberOne.setTextSize(20);
        numberOne.setOnClickListener(this);
        Button numberTwo = (Button) v.findViewById(R.id.number_2);
        numberTwo.setTextSize(20);
        numberTwo.setOnClickListener(this);
        Button numberThree = (Button) v.findViewById(R.id.number_3);
        numberThree.setTextSize(20);
        numberThree.setOnClickListener(this);
        Button numberFour = (Button) v.findViewById(R.id.number_4);
        numberFour.setTextSize(20);
        numberFour.setOnClickListener(this);
        Button numberFive = (Button) v.findViewById(R.id.number_5);
        numberFive.setTextSize(20);
        numberFive.setOnClickListener(this);
        Button numberSix = (Button) v.findViewById(R.id.number_6);
        numberSix.setTextSize(20);
        numberSix.setOnClickListener(this);
        Button numberSeven = (Button) v.findViewById(R.id.number_7);
        numberSeven.setTextSize(20);
        numberSeven.setOnClickListener(this);
        Button numberEight = (Button) v.findViewById(R.id.number_8);
        numberEight.setTextSize(20);
        numberEight.setOnClickListener(this);
        Button numberNine = (Button) v.findViewById(R.id.number_9);
        numberNine.setTextSize(20);
        numberNine.setOnClickListener(this);
        Button peroid = (Button) v.findViewById(R.id.period);
        peroid.setTextSize(20);
        peroid.setOnClickListener(this);
        Button delete = (Button) v.findViewById(R.id.delete);
        delete.setTextSize(20);
        delete.setOnClickListener(this);
        Button submit = (Button) v.findViewById(R.id.submit);
        submit.setTextSize(20);
        submit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        String value = mDisplayText.getText().toString();
        switch (v.getId()) {
            case R.id.number_0:
                handleNumberButtonClick(value, "0");
                break;
            case R.id.number_1:
                handleNumberButtonClick(value, "1");
                break;
            case R.id.number_2:
                handleNumberButtonClick(value, "2");
                break;
            case R.id.number_3:
                handleNumberButtonClick(value, "3");
                break;
            case R.id.number_4:
                handleNumberButtonClick(value, "4");
                break;
            case R.id.number_5:
                handleNumberButtonClick(value, "5");
                break;
            case R.id.number_6:
                handleNumberButtonClick(value, "6");
                break;
            case R.id.number_7:
                handleNumberButtonClick(value, "7");
                break;
            case R.id.number_8:
                handleNumberButtonClick(value, "8");
                break;
            case R.id.number_9:
                handleNumberButtonClick(value, "9");
                break;
            case R.id.period:
                if (value == null) {
                    value = "0.";
                } else if (!value.contains(".")) {
                    value += ".";
                }
                if (value.startsWith("0") && !value.contains(".")) {
                    value = value.substring(1, value.length());
                }
                mDisplayText.setText(value);
                break;
            case R.id.delete:
                if (value != null) {
                    if (value.length() <= 1) {
                        value = "0";
                    } else {
                        value = value.substring(0, value.length() - 1);
                        if (value.endsWith(".")) {
                            value = value.substring(0, value.length() - 1);
                        }
                    }
                }
                mDisplayText.setText(value);
                break;
            case R.id.submit:
                mCalcListener.onCalculatorSubmit(value);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }

    private void handleNumberButtonClick(String value, String number) {
        if (value == null) {
            value = number;
        } else {
            value += number;
        }
        if (value.startsWith("0") && !value.contains(".")) {
            value = value.substring(1, value.length());
        }
        mDisplayText.setText(value);
    }

    public interface CalculatorClickListener {
        void onCalculatorSubmit(String total);
    }
}
