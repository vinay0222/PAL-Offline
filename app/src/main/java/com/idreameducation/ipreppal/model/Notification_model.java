package com.idreameducation.ipreppal.model;

public class Notification_model {

    String messageTime,senderId,sender,notificationMessage,replyable,message,type,fileLink;
    boolean read=false;
    String key;

    public Notification_model(String messageTime, String senderId, String sender, String notificationMessage, String replyable, String message, String type) {
        this.messageTime = messageTime;
        this.senderId = senderId;
        this.sender = sender;
        this.notificationMessage = notificationMessage;
        this.replyable = replyable;
        this.message = message;
        this.type = type;
    }

    public Notification_model(String messageTime, String senderId, String sender, String notificationMessage, String replyable, String message, boolean read) {
        this.messageTime = messageTime;
        this.senderId = senderId;
        this.sender = sender;
        this.notificationMessage = notificationMessage;
        this.replyable = replyable;
        this.message = message;
        this.read = read;
    }

    public Notification_model(String messageTime, String senderId, String sender, String notificationMessage, String replyable, String message, String type, String fileLink, boolean read, String key) {
        this.messageTime = messageTime;
        this.senderId = senderId;
        this.sender = sender;
        this.notificationMessage = notificationMessage;
        this.replyable = replyable;
        this.message = message;
        this.type = type;
        this.fileLink = fileLink;
        this.read = read;
        this.key = key;
    }

    public Notification_model() {
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getReplyable() {
        return replyable;
    }

    public void setReplyable(String replyable) {
        this.replyable = replyable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

