package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.VideoReportsModel;

import java.util.List;


public class VideoReportRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;

    public VideoReportRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoReports(final VideoReportsModel videoReportsModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertVideoReports(videoReportsModel);

                return null;
            }
        }.execute();
    }


    public LiveData<List<VideoReportsModel>> getVideoReports(String userID , String category , String videoID) {
        return categoryDatabase.daoAccess().fetchVideoReports(userID , category , videoID);
    }


}

