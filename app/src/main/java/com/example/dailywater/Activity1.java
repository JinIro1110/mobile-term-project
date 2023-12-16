package com.example.dailywater;

import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Intent;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailywater.dto.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity1 extends AppCompatActivity implements OnDrinkAmountChangedListener {

    private SetSharedPreferences sharedPreferencesManager;
    private TextView drinkAmountTextView;
    private DrawerLayout drawerLayout;

    @Override
    public void onDrinkAmountChanged() {
        updateDrinkAmount();
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_1);
        drawerLayout = findViewById(R.id.drawerLayout);


        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 액티비티 재시작
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        ImageButton menuButton = findViewById(R.id.imageButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDrawerRecyclerView();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> greenDays = getDaysFromDatabase(1);
                ArrayList<String> redDays = getDaysFromDatabase(0);

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("greenDays", greenDays);
                bundle.putStringArrayList("redDays", redDays);

                AchievementFragment achievementFragment = new AchievementFragment();
                achievementFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, achievementFragment);

                fragmentTransaction.commit();

                findViewById(R.id.imageButton).setVisibility(View.GONE);
                findViewById(R.id.textView).setVisibility(View.GONE);
                findViewById(R.id.drinkAmountTextView).setVisibility(View.GONE);
                findViewById(R.id.waterBottleImageView).setVisibility(View.GONE);
                findViewById(R.id.add).setVisibility(View.GONE);
                findViewById(R.id.remove).setVisibility(View.GONE);
                findViewById(R.id.startButton).setVisibility(View.GONE);

                FrameLayout frameLayout = findViewById(R.id.frameLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                frameLayout.setLayoutParams(params);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        ImageButton settingsButton = findViewById(R.id.imageButtonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, editProfileFragment);

                fragmentTransaction.commit();

                findViewById(R.id.imageButton).setVisibility(View.GONE);
                findViewById(R.id.textView).setVisibility(View.GONE);
                findViewById(R.id.drinkAmountTextView).setVisibility(View.GONE);
                findViewById(R.id.waterBottleImageView).setVisibility(View.GONE);
                findViewById(R.id.add).setVisibility(View.GONE);
                findViewById(R.id.remove).setVisibility(View.GONE);
                findViewById(R.id.startButton).setVisibility(View.GONE);

                FrameLayout frameLayout = findViewById(R.id.frameLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                frameLayout.setLayoutParams(params);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        sharedPreferencesManager = new SetSharedPreferences(this);

        drinkAmountTextView = findViewById(R.id.drinkAmountTextView);

        updateDrinkAmount();

        ImageView addButton = findViewById(R.id.add);
        ImageView removeButton = findViewById(R.id.remove);

        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);

                sharedPreferencesManager.incrementLiters();
                updateDrinkAmount();
                Toast.makeText(Activity1.this, "100ml 추가됨", Toast.LENGTH_SHORT).show();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);
                sharedPreferencesManager.decrementLiters();
                updateDrinkAmount();
                Toast.makeText(Activity1.this, "100ml 감소됨", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // RecyclerView 설정 및 데이터 로드
    private void setupDrawerRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.navDrawerRecyclerView);
        ArrayList<ToDoItem> toDoItems = getToDoItemsFromDatabase();

        CustomMenuAdapter adapter = new CustomMenuAdapter(this, toDoItems, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<ToDoItem> getToDoItemsFromDatabase() {
        SQLiteDatabase db = openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM ToDo", null);
        ArrayList<ToDoItem> toDoItems = new ArrayList<>();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
            @SuppressLint("Range") String activityName = cursor.getString(cursor.getColumnIndex("activity_name"));
            @SuppressLint("Range") int activityStatus = cursor.getInt(cursor.getColumnIndex("activity_status"));
            @SuppressLint("Range") int waterReward = cursor.getInt(cursor.getColumnIndex("water_reward"));
            toDoItems.add(new ToDoItem(id, activityName, activityStatus, waterReward)); // ID도 함께 저장
        }

        cursor.close();
        db.close();
        return toDoItems;
    }



    private void updateDrinkAmount() {
        int milliliters = sharedPreferencesManager.getLiters();
        double liters = milliliters / 1000.0;
        drinkAmountTextView.setText(String.format("%.1fL", liters));

        // liters가 0이면 "완료!"로 표시, 그렇지 않으면 현재 리터값을 표시
        if (liters == 0) {
            drinkAmountTextView.setText("완료!");
            recordDailySuccess();
        } else {
            recordDailyFailure();
            drinkAmountTextView.setText(String.format("%.1fL", liters));
        }
    }

    private void recordDailySuccess() {
        SQLiteDatabase db = openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues values = new ContentValues();
        values.put("date", currentDate);
        values.put("success", 1);

        Cursor cursor = db.rawQuery("SELECT _id FROM DailyResult WHERE date = ?", new String[]{currentDate});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
            db.update("DailyResult", values, "_id = ?", new String[]{String.valueOf(id)});
        } else {
            db.insert("DailyResult", null, values);
        }

        cursor.close();
        db.close();
    }

    private void recordDailyFailure() {
        SQLiteDatabase db = openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues values = new ContentValues();
        values.put("date", currentDate);
        values.put("success", 0); // 실패 상황을 나타내기 위해 0으로 설정

        Cursor cursor = db.rawQuery("SELECT _id FROM DailyResult WHERE date = ?", new String[]{currentDate});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
            db.update("DailyResult", values, "_id = ?", new String[]{String.valueOf(id)});
        } else {
            db.insert("DailyResult", null, values);
        }

        cursor.close();
        db.close();
    }


    private ArrayList<String> getDaysFromDatabase(int successValue) {
        ArrayList<String> days = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT date FROM DailyResult WHERE success = ?", new String[]{String.valueOf(successValue)});

        int dateColumnIndex = cursor.getColumnIndex("date");
        if (dateColumnIndex != -1) {
            while (cursor.moveToNext()) {
                String dateString = cursor.getString(dateColumnIndex);
                days.add(dateString);
            }
        }
        cursor.close();
        db.close();
        return days;
    }

}