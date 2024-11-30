package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ReportsTestDetailReviewModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int testId;
    @ColumnInfo(name = "correct_feedback")
    private String correctFeedback;
    @ColumnInfo(name = "correct_option")
    private String correctOption;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "incorrect_feedback")
    private String incorrectFeedback;
    @ColumnInfo(name = "is_attempted")
    private String isAttempted;
    @ColumnInfo(name = "option1")
    private String option1;
    @ColumnInfo(name = "option2")
    private String option2;
    @ColumnInfo(name = "option3")
    private String option3;
    @ColumnInfo(name = "option4")
    private String option4;
    @ColumnInfo(name = "option_selected")
    private String optionSelected;
    @ColumnInfo(name = "question_id")
    private String questionId;
    @ColumnInfo(name = "question_q")
    private String questionQ;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "type1")
    private String type1;
    @ColumnInfo(name = "type2")
    private String type2;
    @ColumnInfo(name = "type3")
    private String type3;
    @ColumnInfo(name = "type4")
    private String type4;
    @ColumnInfo(name = "lang")
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getCorrectFeedback() {
        return correctFeedback;
    }

    public void setCorrectFeedback(String correctFeedback) {
        this.correctFeedback = correctFeedback;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIncorrectFeedback() {
        return incorrectFeedback;
    }

    public void setIncorrectFeedback(String incorrectFeedback) {
        this.incorrectFeedback = incorrectFeedback;
    }

    public String getIsAttempted() {
        return isAttempted;
    }

    public void setIsAttempted(String isAttempted) {
        this.isAttempted = isAttempted;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(String optionSelected) {
        this.optionSelected = optionSelected;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionQ() {
        return questionQ;
    }

    public void setQuestionQ(String questionQ) {
        this.questionQ = questionQ;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

}