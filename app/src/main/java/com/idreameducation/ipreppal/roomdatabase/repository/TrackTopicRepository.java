package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrackTopicRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase trackTopicDatabase;
    private Global global;

    public TrackTopicRepository(Context context) {
        global = (Global) context.getApplicationContext();
        trackTopicDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertDetails(final TrackTopicModel trackTopicModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                trackTopicDatabase.daoAccess().insertTopicDetailsForTracking(trackTopicModel);
                return null;
            }
        }.execute();
    }

    public List<TrackTopicModel> getDetail(String userId, String subject, String board, String language, String foundationalTopicId) {
        return trackTopicDatabase.daoAccess().fetchTopicDetailsForTracking(userId, subject, board, language, foundationalTopicId);
    }

    public boolean isDataExist(String userId, String subject, String board, String language, String foundationalTopicId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = trackTopicDatabase.daoAccess().isTopicExistForTracking(userId, subject, board, language, foundationalTopicId);
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

    public void updateField(String userId, String subject, String board, String seniorTopicId, String seniorClass, String seniorTopicName, String foundationalTopicId, String language){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                trackTopicDatabase.daoAccess().updateTopicDetailsForTracking(userId, subject, board, seniorTopicId, seniorClass, seniorTopicName, foundationalTopicId, language);
                return null;
            }
        }.execute();
    }

    public void delete(String userId, String subject, String board, String language, String foundationalTopicId){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                trackTopicDatabase.daoAccess().deleteTopic(userId, subject, board, language, foundationalTopicId);
                return null;
            }
        }.execute();
    }
}