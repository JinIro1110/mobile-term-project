package com.example.dailywater;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetSharedPreferences {

    private SharedPreferences sharedPreferences;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public SetSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public void saveName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public void saveWeight(int weight) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("weight", weight);
        editor.apply();
    }

    public void saveStartDate(Date startDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (startDate != null) {
            String startDateStr = dateFormat.format(startDate);
            editor.putString("startDate", startDateStr);
        }
        editor.apply();
    }

    public void saveEndDate(Date endDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (endDate != null) {
            String endDateStr = dateFormat.format(endDate);
            editor.putString("endDate", endDateStr);
        }
        editor.apply();
    }

    public void saveLiters(int liters) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("liters", liters);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", null);
    }

    public int getWeight() {
        return sharedPreferences.getInt("weight", 0); // -1 as default value
    }

    public Date getStartDate() {
        String dateStr = sharedPreferences.getString("startDate", null);
        if (dateStr != null) {
            try {
                return dateFormat.parse(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Date getEndDate() {
        String dateStr = sharedPreferences.getString("endDate", null);
        if (dateStr != null) {
            try {
                return dateFormat.parse(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getLiters() {
        return sharedPreferences.getInt("liters", 0);
    }
}
