package com.example.dailywater;

import androidx.fragment.app.Fragment;

import java.util.Date;

public interface FragmentDataListener {
    void getNameWeight(String userName, int Weight);
    void getLiters(int liters);
    void getDate(Date startDate, Date endDate);
    void nextFragment(Fragment fragment);
}
