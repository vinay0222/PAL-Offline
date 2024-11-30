package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.idreameducation.ipreppal.pal.activity.DataTypeConverter;

import java.io.Serializable;
import java.util.List;

@Entity
public class ReportsTopicWiseTestModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "board")
    private String board;
    @ColumnInfo(name = "sClass")
    private String sClass;
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "test_date")
    private String testDate;
    @ColumnInfo(name = "time")
    private long time;
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @ColumnInfo(name = "name")
    private String name;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @ColumnInfo(name = "list_data")
    @TypeConverters(DataTypeConverter.class)
    private List<ReportsTestDetailReviewModel> list = null;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    private ReportsTestScoreModel reportsTestScoreModel;
    @ColumnInfo(name = "lang")
    private String lang;
    @ColumnInfo(name = "subjectName")
    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSClass() {
        return sClass;
    }

    public void setSClass(String sClass) {
        this.sClass = sClass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportsTestDetailReviewModel> getList() {
        return list;
    }

    public void setList(List<ReportsTestDetailReviewModel> list) {
        this.list = list;
    }

    public ReportsTestScoreModel getReportsTestScoreModel() {
        return reportsTestScoreModel;
    }

    public void setReportsTestScoreModel(ReportsTestScoreModel reportsTestScoreModel) {
        this.reportsTestScoreModel = reportsTestScoreModel;
    }

}