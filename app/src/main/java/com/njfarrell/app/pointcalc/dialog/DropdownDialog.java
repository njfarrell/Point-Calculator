/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njfarrell.app.pointcalc.R;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class DropdownDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private DropdownDialogListener mDialogListener;

    public DropdownDialog(Context context, String firstSelector, String secondSelector,
                          String thirdSelector, String fourthSelector, DropdownDialogListener listener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dropdown);

        mContext = context;
        mDialogListener = listener;

        configureLayout(firstSelector, secondSelector, thirdSelector, fourthSelector);
    }

    private void configureLayout(String first, String second, String third, String fourth) {
        RelativeLayout firstSelectorLayout = (RelativeLayout) findViewById(R.id.first_selector);
        if (first != null) {
            firstSelectorLayout.setOnClickListener(this);
            TextView firstSelectorText = (TextView) findViewById(R.id.first_selector_text);
            firstSelectorText.setText(first);
            if (second == null && third == null && fourth == null) {
                findViewById(R.id.first_selector_divider).setVisibility(View.GONE);
            }
        } else {
            firstSelectorLayout.setVisibility(View.GONE);
        }

        RelativeLayout secondSelectorLayout = (RelativeLayout) findViewById(R.id.second_selector);
        if (second != null) {
            secondSelectorLayout.setOnClickListener(this);
            TextView secondSelectorText = (TextView) findViewById(R.id.second_selector_text);
            secondSelectorText.setText(second);
            if (third == null && fourth == null) {
                findViewById(R.id.second_selector_divider).setVisibility(View.GONE);
            }
        } else {
            secondSelectorLayout.setVisibility(View.GONE);
        }

        RelativeLayout thirdSelectorLayout = (RelativeLayout) findViewById(R.id.third_selector);
        if (third != null) {
            thirdSelectorLayout.setOnClickListener(this);
            TextView thirdSelectorText = (TextView) findViewById(R.id.third_selector_text);
            thirdSelectorText.setText(third);
            if (fourth == null) {
                findViewById(R.id.third_selector_divider).setVisibility(View.GONE);
            }
        } else {
            thirdSelectorLayout.setVisibility(View.GONE);
        }

        RelativeLayout fourthSelectorLayout = (RelativeLayout) findViewById(R.id.fourth_selector);
        if (fourth != null) {
            fourthSelectorLayout.setOnClickListener(this);
            TextView fourthSelectorText = (TextView) findViewById(R.id.fourth_selector_text);
            fourthSelectorText.setText(fourth);
        } else {
            fourthSelectorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        String value = null;
        switch(v.getId()) {
            case R.id.first_selector:
                value = ((TextView) findViewById(R.id.first_selector_text)).getText().toString();
                break;
            case R.id.second_selector:
                value = ((TextView) findViewById(R.id.second_selector_text)).getText().toString();
                break;
            case R.id.third_selector:
                value = ((TextView) findViewById(R.id.third_selector_text)).getText().toString();
                break;
            case R.id.fourth_selector:
                value = ((TextView) findViewById(R.id.fourth_selector_text)).getText().toString();
                break;
        }
        mDialogListener.onDialogItemSelected(value);
        dismiss();
    }

    public interface DropdownDialogListener {
        void onDialogItemSelected(String value);
    }
}
