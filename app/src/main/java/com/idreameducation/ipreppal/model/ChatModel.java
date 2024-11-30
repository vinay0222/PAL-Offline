package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class ChatModel implements Serializable {


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String userType;
    private String stName;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ChatModel() {
    }

    public ChatModel(String time, String userType, String stName, String messageType, String to, String from, String message, String category, String type) {
        this.time = time;
        this.userType = userType;
        this.stName = stName;
        this.messageType = messageType;
        this.to = to;
        this.from = from;
        this.message = message;
        this.category = category;
        this.type = type;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    private String messageType;
    private String inputType;
    private String to;
    private String showType;

    public ChatModel(String time, String userType, String stName, String key, String messageType, String inputType, String to, String from, String message, String category, String type) {
        this.time = time;
        this.userType = userType;
        this.stName = stName;
        this.key = key;
        this.messageType = messageType;
        this.inputType = inputType;
        this.to = to;
        this.from = from;
        this.message = message;
        this.category = category;
        this.type = type;
    }

    public ChatModel(String time, String userType, String stName, String key, String messageType, String inputType, String to, String showType, String from, String message, String category, String type) {
        this.time = time;
        this.userType = userType;
        this.stName = stName;
        this.key = key;
        this.messageType = messageType;
        this.inputType = inputType;
        this.to = to;
        this.showType = showType;
        this.from = from;
        this.message = message;
        this.category = category;
        this.type = type;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;
    private String message;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

