package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsLatestDataTestRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase reportsLatestDataTestDatabase;
    private Global global;
    private List<ReportsLatestDataTestModel> list;

    public ReportsLatestDataTestRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsLatestDataTestDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final ReportsLatestDataTestModel reportsLatestDataTestModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataTestDatabase.daoAccess().insertReportsLatestDataTest(reportsLatestDataTestModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsLatestDataTestModel> getDetail(String userId, String board, String sClass, String subject, String topicId) {
        if(topicId == null){
            return reportsLatestDataTestDatabase.daoAccess().fetchReportsLatestDataDetail(userId, board, sClass, subject);
        }else{
            return reportsLatestDataTestDatabase.daoAccess().fetchReportsLatestDataTopicDetail(userId, board, sClass, subject, topicId);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String type, String topicId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = reportsLatestDataTestDatabase.daoAccess().isReportsLatestDataExist(userId, board, sClass, subject, type, topicId);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsLatestDataTestDatabase.daoAccess().updateReportsLatestData(userId, board, sClass, subject, type, topicId, list, date, questionsAttempted, scores, topicName, totalQuestions, totalScores, percentageScored);
                return null;
            }
        }.execute();
    }
}