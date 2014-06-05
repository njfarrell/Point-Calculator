/**
 * Copyright 2013 MobileSmith, Inc. All Rights Reserved.
 */
package com.njfarrell.app.pointcalc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.njfarrell.app.pointcalc.R;

/**
 * @author Nate Farrell <nate.farrell@mobilesmith.com>
 */
public class PointCalcWidget extends AppWidgetProvider {

    // Intent actions for widget
    private static final String CARB_CLICKED = "carb_click";
    private static final String FAT_CLICKED = "fat_click";
    private static final String PROTEIN_CLICKED = "protein_click";
    private static final String FIBER_CLICKED = "fiber_click";


    // Intent actions for calculator
    private static final String SUBMIT_CLICKED = "submit_clicked";
    private static final String PERIOD_CLICKED = "period_clicked";
    private static final String ZERO_CLICKED = "zero_clicked";
    private static final String DELETE_CLICKED = "delete_clicked";
    private static final String ONE_CLICKED = "one_clicked";
    private static final String TWO_CLICKED = "two_clicked";
    private static final String THREE_CLICKED = "three_clicked";
    private static final String FOUR_CLICKED = "four_clicked";
    private static final String FIVE_CLICKED = "five_clicked";
    private static final String SIX_CLICKED = "six_clicked";
    private static final String SEVEN_CLICKED = "seven_clicked";
    private static final String EIGHT_CLICKED = "eight_clicked";
    private static final String NINE_CLICKED = "nine_clicked";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName widget = new ComponentName(context, PointCalcWidget.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        generateWidgetLayout(context, widget, remoteViews, appWidgetManager);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String value = null;
        DataPreferences prefs = DataPreferences.newInstance(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews;

        ComponentName widget = new ComponentName(context, PointCalcWidget.class);
        if (SUBMIT_CLICKED.equals(intent.getAction())) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            generateWidgetLayout(context, widget, remoteViews, appWidgetManager);
        } else if (CARB_CLICKED.equals(intent.getAction())) {
            value = prefs.getValue(DataPreferences.KEY_TOTAL_CARBS, null);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calculator);
            remoteViews.setInt(R.id.calculator_background, "setBackgroundResource", R.drawable.carb_calculator_background);
            generateCalculatorView(context, prefs, widget, remoteViews, appWidgetManager,
                    DataPreferences.KEY_TOTAL_CARBS, value);
        } else if (FAT_CLICKED.equals(intent.getAction())) {
            value = prefs.getValue(DataPreferences.KEY_TOTAL_FAT, null);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calculator);
            remoteViews.setInt(R.id.calculator_background, "setBackgroundResource", R.drawable.fat_calculator_background);
            generateCalculatorView(context, prefs, widget, remoteViews, appWidgetManager,
                    DataPreferences.KEY_TOTAL_FAT, value);
        } else if (PROTEIN_CLICKED.equals(intent.getAction())) {
            value = prefs.getValue(DataPreferences.KEY_TOTAL_PROTEIN, null);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calculator);
            remoteViews.setInt(R.id.calculator_background, "setBackgroundResource", R.drawable.protein_calculator_background);
            generateCalculatorView(context, prefs, widget, remoteViews, appWidgetManager,
                    DataPreferences.KEY_TOTAL_PROTEIN, value);
        } else if (FIBER_CLICKED.equals(intent.getAction())) {
            value = prefs.getValue(DataPreferences.KEY_TOTAL_FIBER, null);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calculator);
            remoteViews.setInt(R.id.calculator_background, "setBackgroundResource", R.drawable.fiber_calculator_background);
            generateCalculatorView(context, prefs, widget, remoteViews, appWidgetManager,
                    DataPreferences.KEY_TOTAL_FIBER, value);
        } else {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calculator);
            String preferenceKey = prefs.getValue(DataPreferences.DATA_KEY, null);
            if (preferenceKey != null) {
                value = prefs.getValue(preferenceKey, null);
            }
            if (PERIOD_CLICKED.equals(intent.getAction())) {
                if (value == null) {
                    value = "0.";
                } else if (!value.contains(".")) {
                    value += ".";
                }
                if (value.startsWith("0") && !value.contains(".")) {
                    value = value.substring(1, value.length());
                }
                prefs.saveValue(preferenceKey, value);
                remoteViews.setTextViewText(R.id.display_text, value);
                appWidgetManager.updateAppWidget(widget, remoteViews);
            } else if (ZERO_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "0", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (DELETE_CLICKED.equals(intent.getAction())) {
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
                prefs.saveValue(preferenceKey, value);
                remoteViews.setTextViewText(R.id.display_text, value);
                appWidgetManager.updateAppWidget(widget, remoteViews);
            } else if (ONE_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "1", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (TWO_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "2", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (THREE_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "3", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (FOUR_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "4", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (FIVE_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "5", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (SIX_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "6", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (SEVEN_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "7", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (EIGHT_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "8", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            } else if (NINE_CLICKED.equals(intent.getAction())) {
                handleNumberButtonClick(value, "9", preferenceKey, prefs, widget, remoteViews,
                        appWidgetManager);
            }
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void generateWidgetLayout(Context context, ComponentName widget,
                                      RemoteViews remoteViews, AppWidgetManager appWidgetManager) {
        DataPreferences prefs = DataPreferences.newInstance(context);
        String carb = prefs.getValue(DataPreferences.KEY_TOTAL_CARBS, null);
        String fat = prefs.getValue(DataPreferences.KEY_TOTAL_FAT, null);
        String protein = prefs.getValue(DataPreferences.KEY_TOTAL_PROTEIN, null);
        String fiber = prefs.getValue(DataPreferences.KEY_TOTAL_FIBER, null);
        int total = calculateTotalPoints(carb, fat, protein, fiber);


        remoteViews.setOnClickPendingIntent(R.id.carbs_layout, getPendingSelfIntent(context, CARB_CLICKED));
        if (carb != null) {
            remoteViews.setTextViewText(R.id.carbs_value, carb);
        }
        remoteViews.setOnClickPendingIntent(R.id.fat_layout, getPendingSelfIntent(context, FAT_CLICKED));
        if (fat != null) {
            remoteViews.setTextViewText(R.id.fat_value, fat);
        }
        remoteViews.setOnClickPendingIntent(R.id.protein_layout, getPendingSelfIntent(context, PROTEIN_CLICKED));
        if (protein != null) {
            remoteViews.setTextViewText(R.id.protein_value, protein);
        }
        remoteViews.setOnClickPendingIntent(R.id.fiber_layout, getPendingSelfIntent(context, FIBER_CLICKED));
        if (fiber != null) {
            remoteViews.setTextViewText(R.id.fiber_value, fiber);
        }

        remoteViews.setTextViewText(R.id.total_value, String.valueOf(total));

        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    private void generateCalculatorView(Context context, DataPreferences prefs,
                                        ComponentName widget, RemoteViews remoteViews,
                                        AppWidgetManager appWidgetManager, String dataKey,
                                        String value) {
        prefs.saveValue(DataPreferences.DATA_KEY, dataKey);
        remoteViews.setOnClickPendingIntent(R.id.period, getPendingSelfIntent(context, PERIOD_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_0, getPendingSelfIntent(context, ZERO_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.delete, getPendingSelfIntent(context, DELETE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_1, getPendingSelfIntent(context, ONE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_2, getPendingSelfIntent(context, TWO_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_3, getPendingSelfIntent(context, THREE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_4, getPendingSelfIntent(context, FOUR_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_5, getPendingSelfIntent(context, FIVE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_6, getPendingSelfIntent(context, SIX_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_7, getPendingSelfIntent(context, SEVEN_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_8, getPendingSelfIntent(context, EIGHT_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.number_9, getPendingSelfIntent(context, NINE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.submit, getPendingSelfIntent(context, SUBMIT_CLICKED));
        if (value != null && value.length() > 0) {
            remoteViews.setTextViewText(R.id.display_text, value);
        }
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    private void handleNumberButtonClick(String value, String number, String preferenceKey,
                                   DataPreferences prefs, ComponentName widget,
                                   RemoteViews remoteViews, AppWidgetManager appWidgetManager) {
        if (value == null) {
            value = number;
        } else {
            value += number;
        }
        if (value.startsWith("0") && !value.contains(".")) {
            value = value.substring(1, value.length());
        }
        prefs.saveValue(preferenceKey, value);
        remoteViews.setTextViewText(R.id.display_text, value);
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    private int calculateTotalPoints(String carbStr, String fatStr, String proteinStr,
                                     String fiberStr) {
        Double carb = 0d;
        Double fat = 0d;
        Double protein = 0d;
        Double fiber = 0d;
        if (carbStr != null && carbStr.length() > 0) {
            carb = Double.valueOf(carbStr);
        }
        if (fatStr != null && fatStr.length() > 0) {
            fat = Double.valueOf(fatStr);
        }
        if (proteinStr != null && proteinStr.length() > 0) {
            protein = Double.valueOf(proteinStr);
        }
        if (fiberStr != null && fiberStr.length() > 0) {
            fiber = Double.valueOf(fiberStr);
        }

        return (int) Math.max(Math.round(
                ((16 * protein) + (19 * carb) + (45 * fat) - (14 * fiber)) / 175), 0);
    }
}
