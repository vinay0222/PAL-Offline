package com.idreameducation.ipreppal.pal.activity;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataTypeConverter {
    private static Gson gson = new Gson();
    @TypeConverter
    public static List<ReportsTestDetailReviewModel> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ReportsTestDetailReviewModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<ReportsTestDetailReviewModel> someObjects) {
        return gson.toJson(someObjects);
    }
}
