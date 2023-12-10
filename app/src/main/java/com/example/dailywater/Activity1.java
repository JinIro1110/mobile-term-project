package com.example.dailywater;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class Activity1 extends AppCompatActivity {

    private setSharedPreferences sharedPreferencesManager;
    private TextView drinkAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_1);


        // '달성률보기' 버튼 클릭 시 Fragment 표시
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터베이스에서 날짜 조회
                ArrayList<String> greenDays = getDaysFromDatabase(1);
                ArrayList<String> redDays = getDaysFromDatabase(0);

                // Bundle을 생성하여 데이터를 담습니다.
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("greenDays", greenDays);
                bundle.putStringArrayList("redDays", redDays);

                // Fragment 생성 및 Bundle 설정
                AchievementFragment achievementFragment = new AchievementFragment();
                achievementFragment.setArguments(bundle);

                // Fragment 트랜잭션
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, achievementFragment);

                // FrameLayout 컨테이너에 Fragment 추가하는 코드는 여기서 제거됩니다.
                // 이전에 fragmentTransaction.replace가 두 번 호출되었는데, 이를 한 번만 호출하도록 수정합니다.

                fragmentTransaction.commit();

                // 다른 뷰들을 숨기기
                findViewById(R.id.imageButton).setVisibility(View.GONE);
                findViewById(R.id.textView).setVisibility(View.GONE);
                findViewById(R.id.drinkAmountTextView).setVisibility(View.GONE);
                findViewById(R.id.waterBottleImageView).setVisibility(View.GONE);
                findViewById(R.id.add).setVisibility(View.GONE);
                findViewById(R.id.remove).setVisibility(View.GONE);
                findViewById(R.id.startButton).setVisibility(View.GONE);

                // 필요하다면 FrameLayout의 크기를 조정하여 전체 화면을 차지하게 할 수 있다.
                FrameLayout frameLayout = findViewById(R.id.frameLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                frameLayout.setLayoutParams(params);
                frameLayout.setVisibility(View.VISIBLE); // FrameLayout을 보이게 설정
            }
        });


        sharedPreferencesManager = new setSharedPreferences(this);

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
    private void updateDrinkAmount() {
        int milliliters = sharedPreferencesManager.getLiters();
        double liters = milliliters / 1000.0;
        drinkAmountTextView.setText(String.format("%.1fL", liters));
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