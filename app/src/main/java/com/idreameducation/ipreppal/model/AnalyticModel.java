package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class AnalyticModel implements Serializable {


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTotalDurationOfQuestions() {
        return totalDurationOfQuestions;
    }

    public void setTotalDurationOfQuestions(String totalDurationOfQuestions) {
        this.totalDurationOfQuestions = totalDurationOfQuestions;
    }

    public String getTimeOfFeedBack() {
        return timeOfFeedBack;
    }

    public void setTimeOfFeedBack(String timeOfFeedBack) {
        this.timeOfFeedBack = timeOfFeedBack;
    }






    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    private String status;

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    private String correctOption;

    public String getChoosenOption() {
        return ChoosenOption;
    }

    public void setChoosenOption(String choosenOption) {
        ChoosenOption = choosenOption;
    }

    private String ChoosenOption;
    private String language;
    private String totalDurationOfQuestions;
    private String timeOfFeedBack;


}
