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
public class ReportsLatestDataTestModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
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
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @ColumnInfo(name = "list_data")
    @TypeConverters(DataTypeConverter.class)
    private List<ReportsTestDetailReviewModel> list = null;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    private ReportsTestScoreModel reportsTestScoreModel;
    @ColumnInfo(name = "lang")
    private String lang;

    @ColumnInfo(name = "timeTaken")
    private String timeTaken;

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
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