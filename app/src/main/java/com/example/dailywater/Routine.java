package com.example.dailywater;

public class Routine {
    private String routineName;
    private int routineLiter;

    public Routine(String routineName, int routineLiter) {
        this.routineName = routineName;
        this.routineLiter = routineLiter;
    }

    public String getRoutineName() {
        return routineName;
    }

    public int getRoutineLiter() {
        return routineLiter;
    }

    public void setRoutineName(String newRoutineName) {
        this.routineName = newRoutineName;
    }

    public void setRoutineLiter(int newRoutineLiter) {
        this.routineLiter = newRoutineLiter;
    }
}
