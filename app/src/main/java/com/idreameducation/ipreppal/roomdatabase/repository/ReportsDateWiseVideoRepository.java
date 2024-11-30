package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseVideoModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsDateWiseVideoRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase reportsDateWiseVideoDatabase;
    private Global global;
    private List<ReportsDateWiseVideoModel> list;

    public ReportsDateWiseVideoRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsDateWiseVideoDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertVideoDetails(final ReportsDateWiseVideoModel reportsDateWiseVideoModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWiseVideoDatabase.daoAccess().insertReportsDateWiseVideo(reportsDateWiseVideoModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsDateWiseVideoModel> getDetail(String userId, String board, String sClass, String subject, String type) {
        if(subject == null){
            return reportsDateWiseVideoDatabase.daoAccess().fetchReportsDateWiseVideoDataDetailData(userId, board, sClass, type);
        }else{
            return reportsDateWiseVideoDatabase.daoAccess().fetchReportsDateWiseVideoDataDetail(userId, board, sClass, subject, type);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String type, String date, long time) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = reportsDateWiseVideoDatabase.daoAccess().isReportsDateWiseVideoDataExist(userId, board, sClass, subject, type, date, time);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String date, long time, String vTime, String topicName, String totalTime, String videoName){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWiseVideoDatabase.daoAccess().updateReportsDateWiseDataVideo(userId, board, sClass, subject, type, date, time, vTime, topicName,
                        totalTime, videoName);
                return null;
            }
        }.execute();
    }

}
