package com.njfarrell.app.pointcalc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.njfarrell.app.pointcalc.database.tables.PointsPlusDataTable;

import java.util.ArrayList;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class PointsPlusDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Points_Plus_Database";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    public PointsPlusDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized boolean insertRow(String table, ContentValues values){
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            result = db.insertOrThrow(table, null, values) >= 0;
            db.setTransactionSuccessful();
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }

    public synchronized Cursor queryTableByColumn(String table, String column,
                                                  String[] selectionArgs, String sortBy) {
        Cursor result = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = null;
        if (column != null && column.length() > 0) {
            selection = formQuerySelection(column,
                    selectionArgs);
        } else {
            selectionArgs = null;
        }
        try {
            result = db.query(table, null, selection, selectionArgs, null, null, sortBy);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return result;
    }

    public static String formQuerySelection(String column, String[] selectionArgs) {
        String selection = null;
        if (selectionArgs != null && selectionArgs.length > 0) {
            selection = column + " IN (?";
            for (int i = 1; i < selectionArgs.length; i++) {
                selection += ", ?";
            }
            selection += ")";
        }
        return selection;
    }

    public synchronized void deleteItem(String tableName, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + tableName + " WHERE " + PointsPlusDataTable.COL_ID
                + " = '" + id + "'";
        db.execSQL(sql);
    }

    public synchronized ArrayList<ContentValues> getContentValuesAL(
            Cursor cursor, boolean closeDb) {
        ArrayList<ContentValues> result = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new ArrayList<ContentValues>();
                do {
                    final ContentValues values = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, values);
                    result.add(values);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (closeDb) {
            close();
        }
        return result;
    }
}
