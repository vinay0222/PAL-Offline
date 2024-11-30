package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class RateModel implements Serializable {


    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;


}
