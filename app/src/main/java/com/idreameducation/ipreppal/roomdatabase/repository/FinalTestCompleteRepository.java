package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.FinalTestCompleteModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FinalTestCompleteRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase finalTestCompleteDatabase;
    private final Global global;
    private List<FinalTestCompleteModel> list;

    public FinalTestCompleteRepository(Context context) {
        global = (Global) context.getApplicationContext();
        finalTestCompleteDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final FinalTestCompleteModel finalTestCompleteModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                finalTestCompleteDatabase.daoAccess().insertFinalTestCompleteDetails(finalTestCompleteModel);

                return null;
            }
        }.execute();
    }

    public List<FinalTestCompleteModel> getDetail(String userId, String board, String sClass, String subject) {
        return finalTestCompleteDatabase.daoAccess().fetchFinalTestCompleteDetail(userId, board, sClass, subject);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject, String topicId, String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = finalTestCompleteDatabase.daoAccess().isFinalTestTopicIdExist(userId, board, sClass, subject, topicId,lang);
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

    public void updateField(String userId, String board, String sClass, String subject, String topicId){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                finalTestCompleteDatabase.daoAccess().updateFinalTestCompleteData(userId, board, sClass, subject, topicId);
                return null;
            }
        }.execute();
    }
}
