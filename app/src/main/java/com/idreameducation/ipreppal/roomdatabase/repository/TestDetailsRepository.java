package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.TestModel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestDetailsRepository {

    private String DB_NAME = "db_Content";

    private ContentDatabase testDatabase;
    private Global global;

    public TestDetailsRepository(Context context) {
        global = (Global) context.getApplicationContext();
        testDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertTestDetails(final TestModel testModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                testDatabase.daoAccess().insertTestDetails(testModel);

                return null;
            }
        }.execute();
    }

    public LiveData<List<TestModel>> getDetail(String userId, String subjectId, String topicId) {
        return testDatabase.daoAccess().fetchDetail(userId, subjectId, topicId);
    }

//    public String getDate(String topicId) {
//        final String[] date = new String[1];
//        try {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    date[0] = testDatabase.daoAccess().fetchDate(topicId);
//                    return null;
//                }
//            }.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return date[0];
//    }

    public boolean isDataExist(String userId, String subjectId, String topicId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = testDatabase.daoAccess().isTopicIdExist(userId, subjectId, topicId);
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

    public void updateField(String userId, String subjectId, String topicId, String date){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                testDatabase.daoAccess().updateTestDateField(userId, subjectId, topicId, date);
                return null;
            }
        }.execute();
    }
}