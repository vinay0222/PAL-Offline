package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsLatestDataPracticeRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase reportsLatestDataPracticeDatabase;
    private final Global global;
    private List<ReportsLatestDataPracticeModel> list;

    public ReportsLatestDataPracticeRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsLatestDataPracticeDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertPracticeDetails(final ReportsLatestDataPracticeModel reportsLatestDataPracticeModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataPracticeDatabase.daoAccess().insertReportsLatestDataPractice(reportsLatestDataPracticeModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsLatestDataPracticeModel> getDetail(String userId, String board, String sClass, String subject, String type, String topicId) {
        if(topicId == null){
            return reportsLatestDataPracticeDatabase.daoAccess().fetchReportsLatestPracticeDataDetail(userId, board, subject, type);
        }else{
            return reportsLatestDataPracticeDatabase.daoAccess().fetchReportsLatestPracticeDataTopicDetail(userId, board, subject, type, topicId);
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
                    exists[0] = reportsLatestDataPracticeDatabase.daoAccess().isReportsLatestPracticeDataExist(userId, board, sClass, subject, type, topicId,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String currentLevel, String pDate, String mastery, String streakProgress, String topicName){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataPracticeDatabase.daoAccess().updateReportsLatestDataPractice(userId, board, sClass, subject, type, topicId, currentLevel, pDate, mastery,
                        streakProgress, topicName);
                return null;
            }
        }.execute();


    }

    public void DeleteFields(String userId, String board, String sClass, String subject, String type, String topicId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataPracticeDatabase.daoAccess().DeleteReportsLatestDataPractice(userId, board, sClass, subject, type, topicId);
                return null;
            }
        }.execute();
    }
}