package com.example.dailywater.dto;

public class ToDoItem {
    private int id;
    private String activityName;
    private int activityStatus;
    private int waterReward;

    public ToDoItem(int id, String activityName, int activityStatus, int waterReward) {
        this.id = id;
        this.activityName = activityName;
        this.activityStatus = activityStatus;
        this.waterReward = waterReward;
    }

    public int getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public int getWaterReward() {
        return waterReward;
    }

    public void setActivityStatus(int status) {
        this.activityStatus = status;
    }
}

