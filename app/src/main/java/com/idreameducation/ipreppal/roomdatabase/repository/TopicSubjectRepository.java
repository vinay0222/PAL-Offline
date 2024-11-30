package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.TopicSubjectWiseModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TopicSubjectRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase topicSubjectDatabase;
    private Global global;
    private List<TopicSubjectWiseModel> list;

    public TopicSubjectRepository(Context context) {
        global = (Global) context.getApplicationContext();
        topicSubjectDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertTopicSubjectWiseDetails(final TopicSubjectWiseModel topicSubjectWiseModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicSubjectDatabase.daoAccess().insertTopicSubjectWiseDetails(topicSubjectWiseModel);
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
                    exists[0] = topicSubjectDatabase.daoAccess().isTopicIdSubjectWiseExist(userId, subjectId);
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

    public void updateField(String userId, String subjectId, String topicId){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicSubjectDatabase.daoAccess().updateTopicIdFieldSubjectWise(userId, subjectId, topicId);
                return null;
            }
        }.execute();
    }

    public List<TopicSubjectWiseModel> getDetail(String userId, String subjectId) {
        return topicSubjectDatabase.daoAccess().fetchTopicSubjectWiseDetail(userId, subjectId);
//        list = new ArrayList<>();
//        try {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    list = topicSubjectDatabase.daoAccess().fetchTopicSubjectWiseDetail(userId, subjectId);
//                    return null;
//                }
//            }.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return list;
    }

}
