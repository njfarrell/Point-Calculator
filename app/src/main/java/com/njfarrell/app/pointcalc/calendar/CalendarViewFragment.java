/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.njfarrell.app.pointcalc.PointsPlusActivity;
import com.njfarrell.app.pointcalc.R;
import com.njfarrell.app.pointcalc.database.tables.PointsPlusDataTable;
import com.njfarrell.app.pointcalc.preferences.PointCalculatorPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class CalendarViewFragment extends Fragment implements View.OnClickListener {
    private static final String tag = "MyCalendarActivity";

    private static final String mDateTemplate = "MMMM yyyy";
    private static final int MONTH_OFFSET = 1;

    private TextView mCurrentMonth;
    private ImageView mPrevMonth;
    private ImageView mNextMonth;
    private GridView mCalendarView;
    private GridCellAdapter mAdapter;
    private Calendar mCalendar;
    private int mMonth;
    private int mYear;
    private int mDailyPoints;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PointCalculatorPrefs prefs = PointCalculatorPrefs.newInstance(getActivity());
        mDailyPoints = prefs.getIntValue(PointCalculatorPrefs.KEY_DAILY_POINTS, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_calendar_view, null);
        mCalendar = Calendar.getInstance(Locale.getDefault());
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mYear = mCalendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + mMonth + " " + "Year: "
                + mYear);

        if (v != null) {
            mPrevMonth = (ImageView) v.findViewById(R.id.prevMonth);
            mPrevMonth.setOnClickListener(this);

            mCurrentMonth = (TextView) v.findViewById(R.id.currentMonth);
            mCurrentMonth.setText(DateFormat.format(mDateTemplate,
                    mCalendar.getTime()));

            mNextMonth = (ImageView) v.findViewById(R.id.nextMonth);
            mNextMonth.setOnClickListener(this);

            mCalendarView = (GridView) v.findViewById(R.id.calendar);

            // Initialised
            mAdapter = new GridCellAdapter(getActivity(), mMonth, mYear);
            mAdapter.notifyDataSetChanged();
            mCalendarView.setAdapter(mAdapter);
        }

        return v;
    }

    /**
     * Sets the calendar grid adapter with current date.
     *
     * @param month month
     * @param year year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        mAdapter = new GridCellAdapter(getActivity(), month, year);
        mCalendar.set(year, month - MONTH_OFFSET, mCalendar.get(Calendar.DAY_OF_MONTH));
        mCurrentMonth.setText(DateFormat.format(mDateTemplate,
                mCalendar.getTime()));
        mAdapter.notifyDataSetChanged();
        mCalendarView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mPrevMonth) {
            if (mMonth <= 1) {
                mMonth = 12;
                mYear--;
            } else {
                mMonth--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + mMonth + " Year: " + mYear);
            setGridCellAdapterToDate(mMonth, mYear);
        }
        if (v == mNextMonth) {
            if (mMonth > 11) {
                mMonth = 1;
                mYear++;
            } else {
                mMonth++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + mMonth + " Year: " + mYear);
            setGridCellAdapterToDate(mMonth, mYear);
        }

    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        private String currentMonthName;
        private int currentMonth;
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView mPointText;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month.
         *
         * @param mm month
         * @param yy year
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces;
            int daysInPrevMonth;
            int prevMonth;
            int prevYear;
            int nextMonth;
            int nextYear;

            currentMonth = mm - 1;
            currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.screen_gridcell, parent, false);

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            mPointText = (TextView) row.findViewById(R.id.calendar_day_point_value);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");

            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.darkgrey));
            }
            if (day_color[1].equals("BLUE") ) {
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                String monthName = getMonthAsString(month);
                if (monthName != null && monthName.equals(themonth)) {
                    gridcell.setTextColor(getResources().getColor(R.color.darkorange));
                } else {
                    gridcell.setTextColor(getResources().getColor(
                            R.color.lightgray02));
                }
            }

            ArrayList<ContentValues> valueAL = getCurrentDayValue((String) gridcell.getTag());
            if (valueAL != null && valueAL.size() > 0) {
                int pointValue = 0;
                for (ContentValues value : valueAL) {
                    pointValue += value.getAsInteger(PointsPlusDataTable.COL_CUR_POINTS);
                }
                String points = String.valueOf(pointValue);
                if (points != null) {
                    points += " Points";
                    mPointText.setText(points);
                    mPointText.setVisibility(View.VISIBLE);
                    if (pointValue < mDailyPoints) {
                        gridcell.setBackgroundDrawable(getResources().getDrawable(
                                R.drawable.calendar_button_positive_selector));
                    } else if (pointValue == mDailyPoints) {
                        gridcell.setBackgroundDrawable(getResources().getDrawable(
                                R.drawable.calendar_button_even_selector));
                    } else {
                        gridcell.setBackgroundDrawable(getResources().getDrawable(
                                R.drawable.calendar_button_negative_selector));
                    }
                }
            }

            return row;
        }

        private ArrayList<ContentValues> getCurrentDayValue(String dateString) {
            Date date;
            ArrayList<ContentValues> values = null;
            try {
                String accountName = PointsPlusActivity.getAccountName(getActivity());
                date = dateFormatter.parse(dateString);
                PointsPlusDataTable table = new PointsPlusDataTable(getActivity(), accountName,
                        date.getTime());
                table.createTableIfNotExist();
                values = table.getDailyPointsALByDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return values;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            Log.e("Selected date", date_month_year);
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, DayViewFragment.newInstance(date_month_year))
                        .addToBackStack(null)
                        .commit();
                Log.d(tag, "Parsed Date: " + parsedDate.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        public void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }
}