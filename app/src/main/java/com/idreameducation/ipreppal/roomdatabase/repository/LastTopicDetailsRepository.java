package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.LastTopicDetailsModel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LastTopicDetailsRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase lastTopicDatabase;
    private Global global;
    private List<LastTopicDetailsModel> list;

    public LastTopicDetailsRepository(Context context) {
        global = (Global) context.getApplicationContext();
        lastTopicDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertDetails(final LastTopicDetailsModel lastTopicDetailsModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lastTopicDatabase.daoAccess().insertLastTopicDetails(lastTopicDetailsModel);

                return null;
            }
        }.execute();
    }

    public List<LastTopicDetailsModel> getDetail(String userId, String board, String sClass, String subject, String language) {
        return lastTopicDatabase.daoAccess().fetchLastTopicDetail(userId, board, sClass, subject, language);
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

    public boolean isDataExist(String userId, String board, String sClass, String subject,String language) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = lastTopicDatabase.daoAccess().isLastTopicExist(userId, board, sClass, subject,language);
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

    public void updateField(String userId, String board, String sClass, String subject, String topicId, String topicName, String subjectName, String iconUrl, String color,String language){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lastTopicDatabase.daoAccess().updateLastTopic(userId, board, sClass, subject, topicId, topicName, subjectName, iconUrl, color, language);
                return null;
            }
        }.execute();
    }
}