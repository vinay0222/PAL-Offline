package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsTimeSpentRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase timeSpentReportsDatabase;
    private final Global global;
    private List<ReportsTimeSpentModel> list;
    private AsyncTask<Void, Void, Void> task;

    public ReportsTimeSpentRepository(Context context) {
        global = (Global) context.getApplicationContext();
        timeSpentReportsDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertTimeSpentDetails(final ReportsTimeSpentModel reportsTimeSpentModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                timeSpentReportsDatabase.daoAccess().insertReportsTimeSpent(reportsTimeSpentModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsTimeSpentModel> getDetail(String userId, String board, String sClass, String date, String subject,String lang) {
        if(subject == null){
            return timeSpentReportsDatabase.daoAccess().fetchReportsTimeSpentDetailData(userId, board, sClass,lang);
        }else{
            return timeSpentReportsDatabase.daoAccess().fetchReportsTimeSpentDetail(userId, board, sClass, date, subject,lang);
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

    public boolean isDataExist(String userId, String board, String sClass, String date, String subject,String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = timeSpentReportsDatabase.daoAccess().isTimeFieldExist(userId, board, sClass, date, subject,lang);
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

    public void updateField(String userId, String board, String sClass, String date, String subject, long time,String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                timeSpentReportsDatabase.daoAccess().updateTime(userId, board, sClass, date, subject, time,lang);
                return null;
            }
        }.execute();
    }

}
