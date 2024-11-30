package com.idreameducation.ipreppal.model;

public class SupportChatModel {

    String from;
    String inputType;
    String message;
    String messageType;
    String type;
    String time;
    String userType;
    String to;
    String image;

    public SupportChatModel(String from, String inputType, String message, String messageType, String type, String time, String userType, String to, String image) {
        this.from = from;
        this.inputType = inputType;
        this.message = message;
        this.messageType = messageType;
        this.type = type;
        this.time = time;
        this.userType = userType;
        this.to = to;
        this.image = image;
    }

    public SupportChatModel() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
