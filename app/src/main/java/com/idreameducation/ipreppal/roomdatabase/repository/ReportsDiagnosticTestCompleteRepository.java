package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsDiagnosticTestCompleteRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase diagnosticTestDatabase;
    private final Global global;
    private List<ReportsDiagnosticCompleteModel> list;

    public ReportsDiagnosticTestCompleteRepository(Context context) {
        global = (Global) context.getApplicationContext();
        diagnosticTestDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                diagnosticTestDatabase.daoAccess().insertReportsDiagnosticComplete(reportsDiagnosticCompleteModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsDiagnosticCompleteModel> getDetail(String userId, String board, String sClass, String subject, String topicId) {
        if(topicId == null){
            return diagnosticTestDatabase.daoAccess().fetchReportsDiagnosticTestDetail(userId, board, sClass, subject);
        }else{
            return diagnosticTestDatabase.daoAccess().fetchReportsDiagnosticTestTopicWiseDetail(userId, board, sClass, subject, topicId);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String topicId,String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = diagnosticTestDatabase.daoAccess().isCompleteFieldExist(userId, board, sClass, subject, topicId,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String topicId, boolean complete,String lang){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                diagnosticTestDatabase.daoAccess().updateCompleteField(userId, board, sClass, subject, topicId, complete,lang);
                return null;
            }
        }.execute();
    }
}
