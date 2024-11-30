package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by apple on 06/02/18.
 */

public class FacilitatorEnableModel implements Serializable {
    private String lastActiveSection;
    private boolean isEnabled;

    public String getLastActiveSection() {
        return lastActiveSection;
    }

    public void setLastActiveSection(String lastActiveSection) {
        this.lastActiveSection = lastActiveSection;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
