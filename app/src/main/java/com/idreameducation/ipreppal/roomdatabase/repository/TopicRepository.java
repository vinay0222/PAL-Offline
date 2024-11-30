package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.TopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TopicRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase topicDatabase;
    private Global global;
    private List<TopicModel> list;

    public TopicRepository(Context context) {
        global = (Global) context.getApplicationContext();
        topicDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertTopicDetails(final TopicModel topicModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicDatabase.daoAccess().insertTopicDetails(topicModel);

                return null;
            }
        }.execute();
    }

    public boolean isDataExist(String userId, String topicId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = topicDatabase.daoAccess().isTopicExist(userId, topicId);
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

    public List<TopicModel> getDetail(String userId) {
        list = new ArrayList<>();
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    list = (List<TopicModel>) topicDatabase.daoAccess().getTopicDetail(userId);
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
