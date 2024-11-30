package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 23/08/18.
 */

public class AndroidIdModel implements Serializable {
private String userId;
private String androidId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    private boolean isLogin;
}
