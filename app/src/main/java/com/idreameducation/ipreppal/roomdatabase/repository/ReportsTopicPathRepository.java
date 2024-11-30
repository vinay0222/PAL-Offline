package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicPathModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsTopicPathRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase topicPathDatabase;
    private final Global global;
    private List<ReportsTopicPathModel> list;
    private AsyncTask<Void, Void, Void> task;

    public ReportsTopicPathRepository(Context context) {
        global = (Global) context.getApplicationContext();
        topicPathDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertPathDetails(final ReportsTopicPathModel reportsTopicPathModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    topicPathDatabase.daoAccess().insertReportsTopicPath(reportsTopicPathModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public List<ReportsTopicPathModel> getDetail(String userId, String board, String sClass, String subject, String topicId) {
        if(topicId == null){
            return topicPathDatabase.daoAccess().fetchReportsTopicPathDetail(userId, board, sClass, subject);
        }else{
            return topicPathDatabase.daoAccess().fetchReportsTopicWisePathDetail(userId, board, sClass, subject, topicId);
        }
//        list = new ArrayList<>();
//
//        if(topicId == null){
//            task = new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    list = topicPathDatabase.daoAccess().fetchReportsTopicPathDetail(userId, board, sClass, subject);
//                    return null;
//                }
//            };
//            try {
//                task.execute().get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }else{
//            try {
//                new AsyncTask<Void, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... voids) {
//                        list = topicPathDatabase.daoAccess().fetchReportsTopicWisePathDetail(userId, board, sClass, subject, topicId);
//                        return null;
//                    }
//                }.execute().get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
    }

    public boolean isDataExist(String userId, String board, String sClass, String subject, String topicId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = topicPathDatabase.daoAccess().isPathExist(userId, board, sClass, subject, topicId);
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

    public void updateField(String userId, String board, String sClass, String subject, String topicId, String typeV){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                topicPathDatabase.daoAccess().updatePath(userId, board, sClass, subject, topicId, typeV);
                return null;
            }
        }.execute();
    }
}