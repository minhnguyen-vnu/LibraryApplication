package com.jmc.library.Models;

import com.jmc.library.Assets.UserDashboardInfo;

public class DashboardModel {
    private static DashboardModel dashboardModel;
    private UserDashboardInfo userDashboardInfo;

    private DashboardModel() {
        userDashboardInfo = new UserDashboardInfo();
    }

    public static DashboardModel getInstance() {
        if (dashboardModel == null) dashboardModel = new DashboardModel();
        return dashboardModel;
    }

    public UserDashboardInfo getUserDashboardInfo() {
        return userDashboardInfo;
    }

    public void setUserDashboardInfo(UserDashboardInfo userDashboardInfo) {
        this.userDashboardInfo = userDashboardInfo;
    }
}
