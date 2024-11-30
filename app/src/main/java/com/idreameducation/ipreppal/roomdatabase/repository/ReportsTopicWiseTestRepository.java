package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseTestModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsTopicWiseTestRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase reportsTopicWiseTestDatabase;
    private final Global global;
    private List<ReportsTopicWiseTestRepository> list;

    public ReportsTopicWiseTestRepository(Context context) {
        global = (Global) context.getApplicationContext();
        reportsTopicWiseTestDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final ReportsTopicWiseTestModel reportsTopicWiseTestModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWiseTestDatabase.daoAccess().insertReportsTopicWiseTest(reportsTopicWiseTestModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsTopicWiseTestModel> getDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang) {
        if(subject == null && topicId == null){
            return reportsTopicWiseTestDatabase.daoAccess().fetchReportsTopicWiseTestDetailData(userId,type,lang);
        }else{
            return reportsTopicWiseTestDatabase.daoAccess().fetchReportsTopicWiseTestDetail(userId, board, sClass, subject, type, topicId,lang);
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
                    exists[0] = reportsTopicWiseTestDatabase.daoAccess().isReportsTopicWiseTestExist(userId, board, sClass, subject, type, topicId, date, time,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String type, String topicId, String testDate, long time, String name, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored, String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reportsTopicWiseTestDatabase.daoAccess().updateReportsTopicWiseTest(userId, board, sClass, subject, type, topicId, testDate, time, name, list, date, questionsAttempted, scores, topicName, totalQuestions, totalScores, percentageScored,lang);
                return null;
            }
        }.execute();
    }

}
