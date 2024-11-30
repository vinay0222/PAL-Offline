package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class AssignTopicModel implements Serializable {


    private String topicId;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    private String topicName;

    public String getFacilitatorId() {
        return facilitatorId;
    }

    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    private String facilitatorId;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    private String Flag;


}
