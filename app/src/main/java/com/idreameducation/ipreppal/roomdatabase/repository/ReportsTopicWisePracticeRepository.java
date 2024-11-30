package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWisePracticeModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsTopicWisePracticeRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase reportsTopicWisePracticeDatabase;
    private final Global global;
    private List<ReportsTopicWisePracticeModel> list;

    public ReportsTopicWisePracticeRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsTopicWisePracticeDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertPracticeDetails(final ReportsTopicWisePracticeModel reportsTopicWisePracticeModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWisePracticeDatabase.daoAccess().insertReportsTopicWisePractice(reportsTopicWisePracticeModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsTopicWisePracticeModel> getDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang) {
        if(subject == null && topicId == null){
            return reportsTopicWisePracticeDatabase.daoAccess().fetchReportsTopicWisePracticeDetailData(userId, board, sClass, type,lang);
        }else{
            return reportsTopicWisePracticeDatabase.daoAccess().fetchReportsTopicWisePracticeDetail(userId, board, sClass, subject, type, topicId,lang);
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
                    exists[0] = reportsTopicWisePracticeDatabase.daoAccess().isReportsTopicWisePracticeDataExist(userId, board, sClass, subject, type, topicId, date, time,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time, String name, String currentLevel, String pDate, String mastery, String streakProgress, String topicName, String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWisePracticeDatabase.daoAccess().updateReportsTopicWisePractice(userId, board, sClass, subject, type, topicId, date, time, name, currentLevel, pDate, mastery,
                        streakProgress, topicName,lang);
                return null;
            }
        }.execute();
    }

}
