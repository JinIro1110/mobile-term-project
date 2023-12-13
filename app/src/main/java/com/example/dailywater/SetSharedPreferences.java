package com.example.dailywater;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.example.dailywater.config.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SetSharedPreferences {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public SetSharedPreferences(Context context) {
        this.context = context;
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

    public void incrementLiters() {
        int currentMilliliters = sharedPreferences.getInt("liters", 0);
        currentMilliliters += 100; // 100ml 증가
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("liters", currentMilliliters);
        editor.apply();
    }

    public void decrementLiters() {
        int currentMilliliters = sharedPreferences.getInt("liters", 0);
        if (currentMilliliters >= 100) { // 100ml 이상일 때만 감소
            currentMilliliters -= 100; // 100ml 감소
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("liters", currentMilliliters);
            editor.apply();
        }
    }

    public int calculateConsecutiveSuccessDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date currentDate = calendar.getTime();

        int consecutiveDays = 0;
        while (!currentDate.before(getStartDate())) {
            if (isSuccessOnDate(currentDate)) {
                consecutiveDays++;
                currentDate = getPreviousDate(currentDate);
            } else {
                break;
            }
        }
        return consecutiveDays;
    }

    private boolean isSuccessOnDate(Date date) {
        SQLiteDatabase db = context.openOrCreateDatabase("HYDRATE_DB", Context.MODE_PRIVATE, null);
        String dateString = dateFormat.format(date);
        Cursor cursor = db.rawQuery("SELECT success FROM DailyResult WHERE date = ?", new String[]{dateString});

        boolean isSuccess = false;
        if (cursor.moveToFirst()) {
            int successIndex = cursor.getColumnIndex("success");
            if (successIndex != -1) {
                isSuccess = cursor.getInt(successIndex) == 1;
            }
        }

        cursor.close();
        db.close();
        return isSuccess;
    }

    private Date getPreviousDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public float calculateSuccessPercentage() {
        Date startDate = getStartDate();
        Date endDate = getEndDate();

        if (startDate == null || endDate == null) {
            // 시작 또는 종료 날짜가 설정되지 않았으면 0 반환
            return 0;
        }

        // 현재 날짜가 종료 날짜를 넘었다면, 종료 날짜를 현재 날짜로 설정
        Date today = new Date();
        if (endDate.after(today)) {
            endDate = today;
        }

        // 시작 날짜부터 종료 날짜까지의 총 일수 계산
        long totalDays = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000) + 1;

        // 시작 날짜부터 오늘(또는 종료 날짜)까지의 성공한 일수 계산
        int successDays = calculateConsecutiveSuccessDays();

        // 퍼센티지 계산
        return (float) successDays / totalDays * 100;
    }


    public void incrementLitersByAmount(int amount) {
        int currentLiters = sharedPreferences.getInt("liters", 0);
        sharedPreferences.edit().putInt("liters", currentLiters + amount).apply();
    }

    public void decrementLitersByAmount(int amount) {
        int currentLiters = sharedPreferences.getInt("liters", 0);
        int newLiters = Math.max(currentLiters - amount, 0); // 리터 값이 음수가 되지 않도록 합니다.
        sharedPreferences.edit().putInt("liters", newLiters).apply();
    }
}
