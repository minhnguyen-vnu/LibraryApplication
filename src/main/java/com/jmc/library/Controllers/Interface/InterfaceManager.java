package com.jmc.library.Controllers.Interface;


public class InterfaceManager {
    private static InterfaceManager instance;


    private DashboardUpdateListener dashboardUpdateListener;



    public static synchronized InterfaceManager getInstance() {
        if (instance == null) instance = new InterfaceManager();
        return instance;
    }


    public void setDashboardUpdateListener(DashboardUpdateListener listener) {
        this.dashboardUpdateListener = listener;
    }

    public DashboardUpdateListener getDashboardUpdateListener() {
        return this.dashboardUpdateListener;
    }
}
