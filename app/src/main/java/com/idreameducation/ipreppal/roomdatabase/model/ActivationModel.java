package com.idreameducation.ipreppal.roomdatabase.model;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ActivationModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tabID;
    private String appID;
    private long serverTime;
    private long localTime;
    private long endDate;
    private String days;
    private String userid;
    private long startDate;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTabID() {
        return tabID;
    }

    public void setTabID(String tabID) {
        this.tabID = tabID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(long localTime) {
        this.localTime = localTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}

//public class ActivationModel implements Serializable {
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    private String tabID;
//    private String appID;
//    private long serverTime;
//    private long localTime;
//    private String days;
//
//    public long getServerTime() {
//        return serverTime;
//    }
//
//    public void setServerTime(long serverTime) {
//        this.serverTime = serverTime;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTabID() {
//        return tabID;
//    }
//
//    public void setTabID(String tabID) {
//        this.tabID = tabID;
//    }
//
//    public String getAppID() {
//        return appID;
//    }
//
//    public void setAppID(String appID) {
//        this.appID = appID;
//    }
//
//
//    public long getLocalTime() {
//        return localTime;
//    }
//
//    public void setLocalTime(long localTime) {
//        this.localTime = localTime;
//    }
//
//    public String getDays() {
//        return days;
//    }
//
//    public void setDays(String days) {
//        this.days = days;
//    }
//}
