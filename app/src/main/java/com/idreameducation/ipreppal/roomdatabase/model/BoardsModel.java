package com.idreameducation.ipreppal.roomdatabase.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class BoardsModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String boardID;
    private String boardName;
    private String boardIcon;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardIcon() {
        return boardIcon;
    }

    public void setBoardIcon(String boardIcon) {
        this.boardIcon = boardIcon;
    }
}
