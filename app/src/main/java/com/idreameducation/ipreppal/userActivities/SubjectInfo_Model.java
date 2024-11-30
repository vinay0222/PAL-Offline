package com.idreameducation.ipreppal.userActivities;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectInfo_Model {

    private long lastUsageTimestamp;
    private String totalTimeUsage;
    private String currentWeekTimeUsage;
    private HashMap<String,String> dailyUsageReport;
    private ArrayList<String> weeklyReportList;
    private boolean currentWeekDiagnostic;
    private boolean currentWeekFinalTest;

    public SubjectInfo_Model(long lastUsageTimestamp, String totalTimeUsage, String currentWeekTimeUsage, HashMap<String, String> dailyUsageReport, ArrayList<String> weeklyReportList, boolean currentWeekDiagnostic, boolean currentWeekFinalTest) {
        this.lastUsageTimestamp = lastUsageTimestamp;
        this.totalTimeUsage = totalTimeUsage;
        this.currentWeekTimeUsage = currentWeekTimeUsage;
        this.dailyUsageReport = dailyUsageReport;
        this.weeklyReportList = weeklyReportList;
        this.currentWeekDiagnostic = currentWeekDiagnostic;
        this.currentWeekFinalTest = currentWeekFinalTest;
    }

    /** default Model */
    public SubjectInfo_Model() {
        this.lastUsageTimestamp = 0;
        this.totalTimeUsage="0";
        this.currentWeekTimeUsage="0";
        this.currentWeekDiagnostic=false;
        this.currentWeekFinalTest=false;
        this.weeklyReportList=new ArrayList<>();
        this.dailyUsageReport = new HashMap<String,String>();
    }

    public ArrayList<String> getWeeklyReportList() {
        if(weeklyReportList==null) weeklyReportList=new ArrayList<>();
        return weeklyReportList;
    }

    public void setWeeklyReportList(ArrayList<String> weeklyReportList) {
        this.weeklyReportList = weeklyReportList;
    }

    public boolean isCurrentWeekDiagnostic() {
        return currentWeekDiagnostic;
    }

    public void setCurrentWeekDiagnostic(boolean currentWeekDiagnostic) {
        this.currentWeekDiagnostic = currentWeekDiagnostic;
    }

    public boolean isCurrentWeekFinalTest() {
        return currentWeekFinalTest;
    }

    public void setCurrentWeekFinalTest(boolean currentWeekFinalTest) {
        this.currentWeekFinalTest = currentWeekFinalTest;
    }

    public long getLastUsageTimestamp() {
        return lastUsageTimestamp;
    }

    public void setLastUsageTimestamp(long lastUsageTimestamp) {
        this.lastUsageTimestamp = lastUsageTimestamp;
    }

    public String getTotalTimeUsage() {
        return totalTimeUsage;
    }

    public void setTotalTimeUsage(String totalTimeUsage) {
        this.totalTimeUsage = totalTimeUsage;
    }

    public String getCurrentWeekTimeUsage() {
        return currentWeekTimeUsage;
    }

    public void setCurrentWeekTimeUsage(String currentWeekTimeUsage) {
        this.currentWeekTimeUsage = currentWeekTimeUsage;
    }

    public HashMap<String, String> getDailyUsageReport() {
        return dailyUsageReport;
    }

    public void setDailyUsageReport(HashMap<String, String> dailyUsageReport) {
        this.dailyUsageReport = dailyUsageReport;
    }
}
