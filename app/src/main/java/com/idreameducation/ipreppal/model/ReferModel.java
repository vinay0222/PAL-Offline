package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 08/08/18.
 */

public class ReferModel implements Serializable {

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;
    private String dateStarted;

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    private String referUserId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String referUserName;

    public String getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }

    public String getReferUserName() {
        return referUserName;
    }

    public void setReferUserName(String referUserName) {
        this.referUserName = referUserName;
    }
}
