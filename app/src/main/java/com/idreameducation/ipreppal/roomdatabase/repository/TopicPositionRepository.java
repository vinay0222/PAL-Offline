package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.TopicPositionWiseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TopicPositionRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase topicPositionDatabase;
    private Global global;
    private List<TopicPositionWiseModel> list;

    public TopicPositionRepository(Context context) {
        global = (Global) context.getApplicationContext();
        topicPositionDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertTopicPositionWiseDetails(final TopicPositionWiseModel topicPositionWiseModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicPositionDatabase.daoAccess().insertTopicPositionWiseDetails(topicPositionWiseModel);

                return null;
            }
        }.execute();
    }

    public boolean isDataExist(String userId, String subjectId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = topicPositionDatabase.daoAccess().isTopicIdPositionWiseExist(userId, subjectId);
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

    public void updateField(String userId, String subjectId, int topicPosition, String topicId){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicPositionDatabase.daoAccess().updateTopicIdFieldPositionWise(userId, subjectId, topicPosition, topicId);
                return null;
            }
        }.execute();
    }

    public List<TopicPositionWiseModel> getDetail(String userId, String subjectId) {
        list = new ArrayList<>();
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    list = (List<TopicPositionWiseModel>) topicPositionDatabase.daoAccess().fetchTopicPositionWiseDetail(userId, subjectId);
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}
