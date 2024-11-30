package com.idreameducation.ipreppal.model;

public class BookRep_Model {

    String  finaltime;
    String  pageRead;
    String  name;
    String  time;
    String  watchedTime;
    String  username;
    String  userclass;
    String  date;
    String  subject;
    String  topicName;
    String  topicId;
    String  bookId;
    String  offlineLink;
    String  onlineLink;
    String  categoryID;


    public BookRep_Model(String finaltime, String pageRead, String name, String time, String watchedTime, String username, String userclass, String date, String subject, String topicName, String topicId, String bookId, String offlineLink, String onlineLink, String categoryID) {
        this.finaltime = finaltime;
        this.pageRead = pageRead;
        this.name = name;
        this.time = time;
        this.watchedTime = watchedTime;
        this.username = username;
        this.userclass = userclass;
        this.date = date;
        this.subject = subject;
        this.topicName = topicName;
        this.topicId = topicId;
        this.bookId = bookId;
        this.offlineLink = offlineLink;
        this.onlineLink = onlineLink;
        this.categoryID = categoryID;
    }

    public BookRep_Model() {
    }

    public String getFinaltime() {
        return finaltime;
    }

    public void setFinaltime(String finaltime) {
        this.finaltime = finaltime;
    }

    public String getPageRead() {
        return pageRead;
    }

    public void setPageRead(String pageRead) {
        this.pageRead = pageRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(String watchedTime) {
        this.watchedTime = watchedTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserclass() {
        return userclass;
    }

    public void setUserclass(String userclass) {
        this.userclass = userclass;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOfflineLink() {
        return offlineLink;
    }

    public void setOfflineLink(String offlineLink) {
        this.offlineLink = offlineLink;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
