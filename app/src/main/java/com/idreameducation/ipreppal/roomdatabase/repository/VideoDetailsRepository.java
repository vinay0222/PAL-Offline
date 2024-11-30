package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VideoDetailsRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase videoDatabase;
    private final Global global;
    private List<VideoModel> list;

    public VideoDetailsRepository(Context context) {
        global = (Global) context.getApplicationContext();
        videoDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoDetails(final VideoModel videoModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                videoDatabase.daoAccess().insertVideoDetails(videoModel);

                return null;
            }
        }.execute();
    }

    public boolean isDataExist(String userId, String board, String sClass, String subject, String topicId, String videoName, String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = videoDatabase.daoAccess().isVideoExist(userId, board, sClass, subject, topicId, videoName,lang);
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exists[0];
    }

    public List<VideoModel> getVideoDetails(String userId, String board, String sClass, String subject) {
        return videoDatabase.daoAccess().fetchVideoDetail(userId, board, sClass, subject);
    }

    public void updateField(String userId, String board, String sClass, String subject,String video_name,String totalTime,String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                videoDatabase.daoAccess().updateVideoDetail(userId, board, sClass, subject,video_name,totalTime,lang);
                return null;
            }
        }.execute();
    }

}
