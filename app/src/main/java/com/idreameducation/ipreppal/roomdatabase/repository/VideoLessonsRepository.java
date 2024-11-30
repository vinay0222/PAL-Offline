package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.VideoLessonModel;

import java.util.List;


public class VideoLessonsRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;

    public VideoLessonsRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
        //categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoLessons(final VideoLessonModel videoLessonModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertVideoLessons(videoLessonModel);

                return null;
            }
        }.execute();
    }

    public LiveData<List<VideoLessonModel>> getVideoLessons(String studentClass, String language, String board, String categoryID) {
        return categoryDatabase.daoAccess().fetchAllVideoLessons(studentClass, language , board , categoryID);
    }


}

