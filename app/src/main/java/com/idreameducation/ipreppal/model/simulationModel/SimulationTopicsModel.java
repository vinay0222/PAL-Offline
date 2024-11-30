package com.idreameducation.ipreppal.model.simulationModel;

public class SimulationTopicsModel {

    String detail,id,name,offlineLink,offlineThumbnail,onlineLink,subjectID,thumbnail,topicName,subjectName;

    public SimulationTopicsModel(String detail, String id, String name, String offlineLink, String offlineThumbnail, String onlineLink, String subjectID, String thumbnail, String topicName) {
        this.detail = detail;
        this.id = id;
        this.name = name;
        this.offlineLink = offlineLink;
        this.offlineThumbnail = offlineThumbnail;
        this.onlineLink = onlineLink;
        this.subjectID = subjectID;
        this.thumbnail = thumbnail;
        this.topicName = topicName;
    }

    public SimulationTopicsModel() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
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
