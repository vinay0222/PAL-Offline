package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataVideoModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsLatestDataVideoRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase reportsLatestDataVideoDatabase;
    private final Global global;
    private List<ReportsLatestDataVideoModel> list;

    public ReportsLatestDataVideoRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsLatestDataVideoDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoDetails(final ReportsLatestDataVideoModel reportsLatestDataVideoModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataVideoDatabase.daoAccess().insertReportsLatestDataVideo(reportsLatestDataVideoModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsLatestDataVideoModel> getDetail(String userId, String board, String sClass, String subject, String type, String topicId) {
        if(topicId == null){
            return reportsLatestDataVideoDatabase.daoAccess().fetchReportsLatestVideoDataDetail(userId, board, sClass, subject, type);
        }else{
            return reportsLatestDataVideoDatabase.daoAccess().fetchReportsLatestVideoDataTopicDetail(userId, board, sClass, subject, type, topicId);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = reportsLatestDataVideoDatabase.daoAccess().isReportsLatestVideoDataExist(userId, board, sClass, subject, type, topicId,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String vId, String url, String vTime, String topicName, String totalTime, String videoName, String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataVideoDatabase.daoAccess().updateReportsLatestDataVideo(userId, board, sClass, subject, type, topicId, vId, url, vTime, topicName,
                        totalTime, videoName,lang);
                return null;
            }
        }.execute();
    }
}