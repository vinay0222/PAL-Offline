package com.idreameducation.ipreppal.roomdatabase.model;

import java.util.HashMap;

public  class GsonModel {
    public HashMap<String, Object> getTestMap() {
        return testMap;
    }

    public void setTestMap(HashMap<String, Object> testMap) {
        this.testMap = testMap;
    }

    private HashMap<String , Object> testMap;
}
