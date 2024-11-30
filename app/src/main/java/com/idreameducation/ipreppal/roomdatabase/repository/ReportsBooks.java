package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsBooks {

    private String DB_NAME = "db_Content";

    private ContentDatabase reportsTopicWiseVideoDatabase;
    private Global global;
    private List<ReportsTopicWiseVideoModel> list;

    public ReportsBooks(Context context) {
        global = (Global) context.getApplicationContext();
        reportsTopicWiseVideoDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoDetails(final ReportsTopicWiseVideoModel reportsTopicWiseVideoModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWiseVideoDatabase.daoAccess().insertReportsTopicWiseVideo(reportsTopicWiseVideoModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsTopicWiseVideoModel> getDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang) {
        if(subject == null && topicId == null){
            return reportsTopicWiseVideoDatabase.daoAccess().fetchReportsTopicWiseVideoDataDetailData(userId, board, sClass, type,lang);
        }else{
            return reportsTopicWiseVideoDatabase.daoAccess().fetchReportsTopicWiseVideoDataDetail(userId, board, sClass, subject, type, topicId,lang);
        }
//        list = new ArrayList<>();
//        if(topicId == null){
//            try {
//                new AsyncTask<Void, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... voids) {
//                        list = diagnosticTestDatabase.daoAccess().fetchReportsDiagnosticTestDetail(userId, board, sClass, subject);
//                        return null;
//                    }
//                }.execute().get();
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
//                        list = diagnosticTestDatabase.daoAccess().fetchReportsDiagnosticTestTopicWiseDetail(userId, board, sClass, subject, topicId);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time,String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = reportsTopicWiseVideoDatabase.daoAccess().isReportsTopicWiseVideoDataExist(userId, board, sClass, subject, type, topicId, date, time,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time, String name, String vTime, String topicName, String totalTime, String videoName, String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWiseVideoDatabase.daoAccess().updateReportsTopicWiseDataVideo(userId, board, sClass, subject, type, topicId, date, time, name, vTime, topicName,
                        totalTime, videoName,lang);
                return null;
            }
        }.execute();
    }

}
