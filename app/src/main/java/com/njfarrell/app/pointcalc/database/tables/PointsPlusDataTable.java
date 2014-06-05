package com.njfarrell.app.pointcalc.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.njfarrell.app.pointcalc.PointsPlusActivity;
import com.njfarrell.app.pointcalc.database.PointsPlusDatabase;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class PointsPlusDataTable {

    public static final String TABLE_NAME = "user_points_data_table_%s_%s";

    public static final String COL_ID = "id";
    public static final String COL_CUR_POINTS = "currentPoints";

    private PointsPlusDatabase mDbHelper;
    private Context mContext;
    private String mTableName;

    public PointsPlusDataTable(Context context, String account, long date) {
        mContext = context;
        mTableName = String.format(TABLE_NAME, account.hashCode(), date);
        mDbHelper = new PointsPlusDatabase(context);
    }

    public void createTableIfNotExist() {
        final String sql = String.format("create table if not exists %s " +
                "(%s integer primary key autoincrement, " + "%s integer);",
                mTableName, COL_ID, COL_CUR_POINTS);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public boolean insertDailyPointDataTable(int points) {
        ContentValues accountValues = new ContentValues();
        accountValues.put(COL_CUR_POINTS, points);
        return insertDailyPointDataTable(accountValues);
    }

    public boolean insertDailyPointDataTable(ContentValues accountValues) {
        if (accountValues != null && accountValues.size() > 0) {
            if (mDbHelper.insertRow(mTableName, accountValues)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ContentValues> getDailyPointsALByDate() {
        final Cursor c = mDbHelper.queryTableByColumn(mTableName, null, null, null);
        final ArrayList<ContentValues> valuesAL = mDbHelper.getContentValuesAL(c, true);
        if (c != null) {
            c.close();
        }
        return valuesAL;
    }

    public void removeItemFromDataTable(int id) {
        mDbHelper.deleteItem(mTableName, id);
    }
}
