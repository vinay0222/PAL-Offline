package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class StudentDetailsModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "ngo_id")
    private String ngoId;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "student_name")
    private String studentName;
    @ColumnInfo(name = "student_password")
    private String studentPassword;
    @ColumnInfo(name = "username")
    private String userName;
    @ColumnInfo(name = "sClass")
    private String sClass;
    @ColumnInfo(name = "board")
    private String board;
    @ColumnInfo(name = "language")
    private String language;

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

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSClass() {
        return sClass;
    }

    public void setSClass(String sClass) {
        this.sClass = sClass;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
