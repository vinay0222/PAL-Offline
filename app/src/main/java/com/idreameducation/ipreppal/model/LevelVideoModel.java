package com.idreameducation.ipreppal.model;

public class LevelVideoModel {

    String AssessmentTopicID,detail,name,offlineLink,offlineThumbnail,onlineLink,thumbnail,topicName;
    String key;

    public LevelVideoModel(String assessmentTopicID, String detail, String name, String offlineLink, String offlineThumbnail, String onlineLink, String thumbnail, String topicName) {
        AssessmentTopicID = assessmentTopicID;
        this.detail = detail;
        this.name = name;
        this.offlineLink = offlineLink;
        this.offlineThumbnail = offlineThumbnail;
        this.onlineLink = onlineLink;
        this.thumbnail = thumbnail;
        this.topicName = topicName;
    }

    public LevelVideoModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAssessmentTopicID() {
        return AssessmentTopicID;
    }

    public void setAssessmentTopicID(String assessmentTopicID) {
        AssessmentTopicID = assessmentTopicID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfflineLink() {
        return offlineLink;
    }

    public void setOfflineLink(String offlineLink) {
        this.offlineLink = offlineLink;
    }

    public String getOfflineThumbnail() {
        return offlineThumbnail;
    }

    public void setOfflineThumbnail(String offlineThumbnail) {
        this.offlineThumbnail = offlineThumbnail;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
