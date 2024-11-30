package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWisePracticeModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsDateWisePracticeRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase reportsDateWisePracticeDatabase;
    private Global global;
    private List<ReportsDateWisePracticeModel> list;

    public ReportsDateWisePracticeRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsDateWisePracticeDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertPracticeDetails(final ReportsDateWisePracticeModel reportsDateWisePracticeModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWisePracticeDatabase.daoAccess().insertReportsDateWisePractice(reportsDateWisePracticeModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsDateWisePracticeModel> getDetail(String userId, String board, String sClass, String subject, String type) {
        if(subject == null){
            return reportsDateWisePracticeDatabase.daoAccess().fetchReportsDateWisePracticeDetailData(userId, board, sClass, type);
        }else{
            return reportsDateWisePracticeDatabase.daoAccess().fetchReportsDateWisePracticeDetail(userId, board, sClass, subject, type);
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
                    exists[0] = reportsDateWisePracticeDatabase.daoAccess().isReportsDateWisePracticeDataExist(userId, board, sClass, subject, type, date, time);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String date, long time, String currentLevel, String pDate, String mastery, String streakProgress, String topicName){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWisePracticeDatabase.daoAccess().updateReportsDataWisePractice(userId, board, sClass, subject, type, date, time, currentLevel, pDate, mastery,
                        streakProgress, topicName);
                return null;
            }
        }.execute();
    }

}
