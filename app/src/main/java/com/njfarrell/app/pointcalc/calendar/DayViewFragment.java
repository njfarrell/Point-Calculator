/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.calendar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.njfarrell.app.pointcalc.PointsPlusActivity;
import com.njfarrell.app.pointcalc.R;
import com.njfarrell.app.pointcalc.caculators.FoodPointCalcFragment;
import com.njfarrell.app.pointcalc.database.tables.PointsPlusDataTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class DayViewFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemLongClickListener {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd-MMM-yyyy");

    private String mDateText;
    private Date mDate;
    private Button mCalculatePoints;
    private String mPoints;
    private PointsPlusDataTable mDataTable;
    private PointsArrayAdapter mPointsAdapter;
    private ListView mPointsList;
    private TextView mPointsView;

    public static DayViewFragment newInstance(String dateString) {
        final Bundle args = new Bundle();
        args.putString(FoodPointCalcFragment.KEY_DATE_STRING, dateString);

        final DayViewFragment frag = new DayViewFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mDateText = args.getString(FoodPointCalcFragment.KEY_DATE_STRING);
            try {
                mDate = dateFormatter.parse(mDateText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_day_view, null);

        if (v != null) {
            final TextView header = (TextView) v.findViewById(R.id.date_header);
            if (mDateText != null) {
                header.setText(mDateText);
            } else {
                header.setText("");
            }

            mPointsList = (ListView) v.findViewById(R.id.day_point_values);
            if (mPointsList != null) {
                mPointsAdapter = new PointsArrayAdapter(getActivity(),
                        getUpdatedItemsForList());
                mPointsList.setAdapter(mPointsAdapter);
                mPointsList.setOnItemLongClickListener(this);
            }

            mPointsView = (TextView) v.findViewById(R.id.day_point_value);
            if (mPoints != null && mPoints.length() > 0) {
                mPointsView.setText(mPoints);
            }

            if (System.currentTimeMillis() < mDate.getTime()) {
                mPointsList.setVisibility(View.GONE);
                mPointsView.setVisibility(View.GONE);
            }

            mCalculatePoints = (Button) v.findViewById(R.id.calculate_food_points);
            mCalculatePoints.setOnClickListener(this);
        }

        return v;
    }

    /**
     * Retrieve values from database
     */
    private String getTotalPointsValue(ArrayList<ContentValues> valuesAL) {
        String points = null;
        if (valuesAL != null && valuesAL.size() > 0) {
            int pointValue = 0;
            for(ContentValues value : valuesAL) {
                if (value != null && value.containsKey(PointsPlusDataTable.COL_CUR_POINTS)) {
                    Integer holder = value.getAsInteger(PointsPlusDataTable.COL_CUR_POINTS);
                    if (holder != null) {
                        pointValue += holder;
                    }
                }
            }
            points = String.valueOf(pointValue);
        }

        if (points == null) {
            points = "0";
        }
        points = "Total Points: " + points;
        return points;
    }

    private ArrayList<ContentValues> getUpdatedItemsForList() {
        ArrayList<ContentValues> pointsValuesAL = null;
        if (mDate != null) {
            pointsValuesAL = getContentALFromDatabase();
            mPoints = getTotalPointsValue(pointsValuesAL);
        }
        if (pointsValuesAL == null) {
            pointsValuesAL = new ArrayList<ContentValues>();
            ContentValues values = new ContentValues();
            values.put(PointsPlusDataTable.COL_CUR_POINTS, 0);
            pointsValuesAL.add(values);
        }
        return pointsValuesAL;
    }

    private ArrayList<ContentValues> getContentALFromDatabase() {
        String accountName = PointsPlusActivity.getAccountName(getActivity());
        mDataTable = new PointsPlusDataTable(getActivity(), accountName,
                mDate.getTime());
        return mDataTable.getDailyPointsALByDate();
    }

    @Override
    public void onClick(View v) {
        if (v == mCalculatePoints) {
            FoodPointCalcFragment frag;
            if (System.currentTimeMillis() >= mDate.getTime()) {
                frag = FoodPointCalcFragment.newInstance(mDate);

            } else {
                frag = new FoodPointCalcFragment();
            }
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int itemId = (Integer) view.getTag();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Delete Points");
        dialogBuilder.setMessage("Are you sure you would like to delete this item?");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDataTable != null) {
                    mDataTable.removeItemFromDataTable(itemId);
                    if (mPointsAdapter != null) {
                        mPointsAdapter = new PointsArrayAdapter(getActivity(),
                                getUpdatedItemsForList());
                        mPointsList.setAdapter(mPointsAdapter);
                        mPointsView.setText(mPoints);
                    }
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
        return false;
    }

    public class PointsArrayAdapter extends ArrayAdapter<ContentValues> {

        private ArrayList<ContentValues> mValuesAL;

        public PointsArrayAdapter(Context context, ArrayList<ContentValues> valuesAL) {
            super(context, 0, valuesAL);
            mValuesAL = valuesAL;
        }

        @Override
        public int getCount() {
            return mValuesAL.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;

            ContentValues values = null;
            if (mValuesAL != null && mValuesAL.size() > 0) {
                values = mValuesAL.get(position);
            }

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_day_view_row, null);
            if (row != null) {
                TextView pointTextView = (TextView) row.findViewById(R.id.point_text_view);
                if (values != null) {
                    String currentPoints = values.getAsString(PointsPlusDataTable.COL_CUR_POINTS);
                    pointTextView.setText(currentPoints + " Points");
                    row.setTag(values.getAsInteger(PointsPlusDataTable.COL_ID));
                }
            }

            return row;
        }
    }
}
