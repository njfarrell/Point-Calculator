/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.caculators;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.njfarrell.app.pointcalc.PointsPlusActivity;
import com.njfarrell.app.pointcalc.R;
import com.njfarrell.app.pointcalc.account.UserAccountFragment;
import com.njfarrell.app.pointcalc.database.tables.PointsPlusDataTable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class FoodPointCalcFragment extends Fragment implements View.OnClickListener,
        CalculatorFragment.CalculatorClickListener {

    public static final String KEY_DATE_STRING = "date_string";
    public static final String KEY_DATE_TIME = "date_time";

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, yyyy");

    private static final String KILO = "kg";
    private static final String OUNCE = "oz";
    private static final String POUND = "lb";

    EditText mProteinView;
    EditText mCarbView;
    EditText mFatView;
    EditText mFiberView;
    TextView mTotalPointView;
    Button mSubmitButton;
    Button mClearButton;
    Button mSaveButton;

    String mCarb;
    String mFat;
    String mProtein;
    String mFiber;

    String mProteinUnit;
    String mCarbUnit;
    String mFatUnit;
    String mFiberUnit;

    String mDateValue;
    long mDateTime;
    boolean mSaveValue = false;
    int mCalculation = -1;
    int mCurrentCalculation;

    public static FoodPointCalcFragment newInstance(Date date) {
        final Bundle args = new Bundle();
        args.putString(KEY_DATE_STRING, date.toString());
        args.putLong(KEY_DATE_TIME, date.getTime());

        FoodPointCalcFragment frag = new FoodPointCalcFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mDateValue = args.getString(KEY_DATE_STRING);
            mDateTime = args.getLong(KEY_DATE_TIME);
        }
        if (mDateValue != null) {
            mSaveValue = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_widget, null);

        LinearLayout foodLayout = (LinearLayout) v.findViewById(R.id.food_calculator_background);
        foodLayout.setBackgroundColor(getResources().getColor(R.color.deep_blue));

        RelativeLayout carbLayout = (RelativeLayout) v.findViewById(R.id.carbs_layout);
        carbLayout.setOnClickListener(this);
        ((TextView) v.findViewById(R.id.carbs_text)).setTextSize(20);
        TextView carbValue = (TextView) v.findViewById(R.id.carbs_value);
        carbValue.setTextSize(18);
        if (mCarb != null) {
            carbValue.setText(mCarb);
        }

        RelativeLayout fatLayout = (RelativeLayout) v.findViewById(R.id.fat_layout);
        fatLayout.setOnClickListener(this);
        ((TextView) v.findViewById(R.id.fat_text)).setTextSize(20);
        TextView fatValue = (TextView) v.findViewById(R.id.fat_value);
        fatValue.setTextSize(18);
        if (mFat != null) {
            fatValue.setText(mFat);
        }

        RelativeLayout proteinLayout = (RelativeLayout) v.findViewById(R.id.protein_layout);
        proteinLayout.setOnClickListener(this);
        ((TextView) v.findViewById(R.id.protein_text)).setTextSize(20);
        TextView proteinValue = (TextView) v.findViewById(R.id.protein_value);
        proteinValue.setTextSize(18);
        if (mProtein != null) {
            proteinValue.setText(mProtein);
        }

        RelativeLayout fiberLayout = (RelativeLayout) v.findViewById(R.id.fiber_layout);
        fiberLayout.setOnClickListener(this);
        ((TextView) v.findViewById(R.id.fiber_text)).setTextSize(20);
        TextView fiberValue = (TextView) v.findViewById(R.id.fiber_value);
        fiberValue.setTextSize(18);
        if (mFiber != null) {
            fiberValue.setText(mFiber);
        }

        if (mDateValue != null) {
            RelativeLayout totalLayout = (RelativeLayout) v.findViewById(R.id.total_layout);
            totalLayout.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.total_drawable));
            totalLayout.setOnClickListener(this);
            TextView totalText = (TextView) v.findViewById(R.id.total_text);
            totalText.setTextSize(22);
            totalText.setText("Calculate Points");

            TextView totalValue = (TextView) v.findViewById(R.id.total_unit);
            totalValue.setTextSize(18);
            totalValue.setText(mDateFormat.format(new Date(mDateTime)));

            v.findViewById(R.id.total_value).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.total_text)).setTextSize(22);
            TextView totalValue = (TextView) v.findViewById(R.id.total_value);
            totalValue.setTextSize(18);
            totalValue.setText(String.valueOf(calculateTotalPoints()));
        }

//        View v = inflater.inflate(R.layout.layout_food_point_calculator, null);
//
//        if (v != null) {
//            mProteinView = (EditText) v.findViewById(R.id.food_protein);
//            mCarbView = (EditText) v.findViewById(R.id.food_carbohydrate);
//            mFatView = (EditText) v.findViewById(R.id.food_fat);
//            mFiberView = (EditText) v.findViewById(R.id.food_fiber);
//
//            mSubmitButton = (Button) v.findViewById(R.id.submit_button);
//            mSubmitButton.setOnClickListener(this);
//
//            mClearButton = (Button) v.findViewById(R.id.clear_button);
//            mClearButton.setOnClickListener(this);
//
//            mSaveButton = (Button) v.findViewById(R.id.save_button);
//            mSaveButton.setOnClickListener(this);
//            if (mSaveValue) {
//                mSaveButton.setVisibility(View.VISIBLE);
//            } else {
//                mSaveButton.setVisibility(View.GONE);
//            }
//
//            final Spinner proteinUnit = (Spinner) v.findViewById(R.id.protein_weight_unit);
//            proteinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    mProteinUnit = proteinUnit.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    mProteinUnit = proteinUnit.getItemAtPosition(0).toString();
//                }
//            });
//
//            final Spinner carbUnit = (Spinner) v.findViewById(R.id.carb_weight_unit);
//            carbUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    mCarbUnit = carbUnit.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    mCarbUnit = carbUnit.getItemAtPosition(0).toString();
//                }
//            });
//
//            final Spinner fatUnit = (Spinner) v.findViewById(R.id.fat_weight_unit);
//            fatUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    mFatUnit = fatUnit.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    mFatUnit = fatUnit.getItemAtPosition(0).toString();
//                }
//            });
//
//            final Spinner fiberUnit = (Spinner) v.findViewById(R.id.fiber_weight_unit);
//            fiberUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    mFiberUnit = fiberUnit.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    mFiberUnit = fiberUnit.getItemAtPosition(0).toString();
//                }
//            });
//
//            mTotalPointView = (TextView) v.findViewById(R.id.total_points);
//        }
        return v;
    }

    @Override
    public void onClick(View v) {
//        if (v == mSubmitButton) {
//            String proteinHolder = mProteinView.getText().toString();
//            String carbHolder = mCarbView.getText().toString();
//            String fatHolder = mFatView.getText().toString();
//            String fiberHolder = mFiberView.getText().toString();
//
//            double protein = 0.0;
//            double carbs = 0.0;
//            double fat = 0.0;
//            double fiber = 0.0;
//
//            if (proteinHolder != null && carbHolder != null && fatHolder != null && fiberHolder != null) {
//                if (proteinHolder.length() > 0) {
//                    protein = Double.valueOf(proteinHolder);
//                }
//                if (carbHolder.length() > 0) {
//                    carbs = Double.valueOf(carbHolder);
//                }
//                if (fatHolder.length() > 0) {
//                    fat = Double.valueOf(fatHolder);
//                }
//                if (fiberHolder.length() > 0) {
//                    fiber = Double.valueOf(fiberHolder);
//                }
//            }
//            doFoodPointCalc(protein, carbs, fat, fiber);
//        } else if (v == mClearButton) {
//            mProteinView.setText("");
//            mCarbView.setText("");
//            mFatView.setText("");
//            mFiberView.setText("");
//            mTotalPointView.setText("");
//            mCalculation = -1;
//            mProteinView.requestFocus();
//        } else if (v == mSaveButton) {
//            if (mCalculation >= 0) {
//                saveDialog();
//            } else {
//                Toast.makeText(getActivity(), R.string.no_calculation, Toast.LENGTH_SHORT).show();
//            }
//        }
        if (v.getId() == R.id.total_layout) {
            saveDialog(calculateTotalPoints());
        } else {
            String value = "0";
            switch (v.getId()) {
                case R.id.carbs_layout:
                    mCurrentCalculation = R.id.carbs_layout;
                    value = ((TextView)getActivity().findViewById(R.id.carbs_value)).getText()
                            .toString();
                    break;
                case R.id.fat_layout:
                    mCurrentCalculation = R.id.fat_layout;
                    value = ((TextView)getActivity().findViewById(R.id.fat_value)).getText()
                            .toString();
                    break;
                case R.id.protein_layout:
                    mCurrentCalculation = R.id.protein_layout;
                    value = ((TextView)getActivity().findViewById(R.id.protein_value)).getText()
                            .toString();
                    break;
                case R.id.fiber_layout:
                    mCurrentCalculation = R.id.fiber_layout;
                    value = ((TextView)getActivity().findViewById(R.id.fiber_value)).getText()
                            .toString();
                    break;
            }
            CalculatorFragment frag = CalculatorFragment.newInstance(v.getId(), value);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .addToBackStack(null)
                    .commit();
            frag.setCalculatorClickListener(this);
        }
    }

    private void saveDialog(final int total) {
        String dialogMessage = "Weight Watchers Point Value:\n"
                + total + " points";

        dialogMessage += "\nWould you like to add this to your daily points?";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Food Points");
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mSaveValue) {
                    storeContentToDatabase(total);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO handle cancel pressed
            }
        });
        dialogBuilder.setIcon(R.drawable.points_plus_icon);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    private void doFoodPointCalc(double protein, double carbs, double fat, double fiber) {
        protein = convertValueToGrams(protein, mProteinUnit);
        carbs = convertValueToGrams(carbs, mCarbUnit);
        fat = convertValueToGrams(fat, mFatUnit);
        fiber = convertValueToGrams(fiber, mFiberUnit);

        mCalculation = (int) Math.max(Math.round(
                ((16 * protein) + (19 * carbs) + (45 * fat) - (14 * fiber)) / 175), 0);

        if (mTotalPointView != null) {
            mTotalPointView.setText(mCalculation + " Points");
        }
    }

    private double convertValueToGrams(double value, String unit) {
        double result = value;
        if(KILO.equals(unit)) {             //Convert kilograms to grams
            result = result * 1000;
        } else if(OUNCE.equals(unit)) {     //Convert ounces to grams
            result = result / 0.035274;
        } else if(POUND.equals(unit)) {     //Convert pounds to grams
            result = result / 0.0022046;
        }

        return result;
    }

    /**
     * Insert a new point value into the data table
     *
     * @param pointValue
     */
    private void storeContentToDatabase(int pointValue) {
        String accountName = PointsPlusActivity.getAccountName(getActivity());
        PointsPlusDataTable dataTable = new PointsPlusDataTable(getActivity(), accountName,
                mDateTime);
        dataTable.insertDailyPointDataTable(pointValue);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCalculatorSubmit(String total) {
        switch(mCurrentCalculation) {
            case R.id.carbs_layout:
                mCarb = total;
                break;
            case R.id.fat_layout:
                mFat = total;
                break;
            case R.id.protein_layout:
                mProtein = total;
                break;
            case R.id.fiber_layout:
                mFiber = total;
                break;
        }
    }

    private int calculateTotalPoints() {
        Double carb = 0d;
        Double fat = 0d;
        Double protein = 0d;
        Double fiber = 0d;
        if (mCarb != null && mCarb.length() > 0) {
            carb = Double.valueOf(mCarb);
        }
        if (mFat != null && mFat.length() > 0) {
            fat = Double.valueOf(mFat);
        }
        if (mProtein != null && mProtein.length() > 0) {
            protein = Double.valueOf(mProtein);
        }
        if (mFiber != null && mFiber.length() > 0) {
            fiber = Double.valueOf(mFiber);
        }

        return (int) Math.max(Math.round(
                ((16 * protein) + (19 * carb) + (45 * fat) - (14 * fiber)) / 175), 0);
    }
}
