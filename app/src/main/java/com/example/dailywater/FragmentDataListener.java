package com.example.dailywater;

import androidx.fragment.app.Fragment;

import com.example.dailywater.Routine;

import java.util.Date;
import java.util.List;

public interface FragmentDataListener {
    void setNameWeight(String userName, int Weight);
    void setLiters(int liters);
    void setDate(Date startDate, Date endDate);
    void nextFragment(Fragment fragment);
    void changeActivity1(List<Routine> targetList);
}