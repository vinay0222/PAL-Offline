package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 09/08/18.
 */

public class CurrentPlanModel implements Serializable {
private String dateStarted;
private String planDuration;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(String planDuration) {
        this.planDuration = planDuration;
    }


}
