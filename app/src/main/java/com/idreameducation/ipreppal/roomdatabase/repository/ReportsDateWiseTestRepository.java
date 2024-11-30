package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseTestModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsDateWiseTestRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase reportsDateWiseTestDatabase;
    private Global global;
    private List<ReportsDateWiseTestModel> list;

    public ReportsDateWiseTestRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsDateWiseTestDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final ReportsDateWiseTestModel reportsDateWiseTestModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWiseTestDatabase.daoAccess().insertReportsDateWiseTest(reportsDateWiseTestModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsDateWiseTestModel> getDetail(String userId, String board, String sClass, String subject, String type) {
        if(subject == null){
            return reportsDateWiseTestDatabase.daoAccess().fetchReportsDateWiseTestDetailData(userId, board, sClass, type);
        }else{
            return reportsDateWiseTestDatabase.daoAccess().fetchReportsDateWiseTestDetail(userId, board, sClass, subject, type);
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
                    exists[0] = reportsDateWiseTestDatabase.daoAccess().isReportsDateWiseTestExist(userId, board, sClass, subject, type, date, time);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String testDate, long time, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsDateWiseTestDatabase.daoAccess().updateReportsDateWiseTest(userId, board, sClass, subject, type, testDate, time, list, date, questionsAttempted, scores, topicName, totalQuestions, totalScores, percentageScored);
                return null;
            }
        }.execute();
    }

}
