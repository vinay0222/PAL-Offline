package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsCountRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase countReportsDatabase;
    private final Global global;
    private List<ReportsCountModel> list;
    private AsyncTask<Void, Void, Void> task;

    public ReportsCountRepository(Context context) {
        global = (Global) context.getApplicationContext();
        countReportsDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertCountDetails(final ReportsCountModel reportsCountModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                countReportsDatabase.daoAccess().insertReportsCount(reportsCountModel);

                return null;
            }
        }.execute();
    }

    public List<ReportsCountModel> getDetail(String userId, String board, String sClass, String date, String type) {
        if(type == null){
            return countReportsDatabase.daoAccess().fetchReportsCountDetailData(userId, board, sClass);
        }else{
            return countReportsDatabase.daoAccess().fetchReportsCountDetail(userId, board, sClass, date, type);
        }
//        list = new ArrayList<>();
//
//        if(topicId == null){
//            task = new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    list = topicPathDatabase.daoAccess().fetchReportsTopicPathDetail(userId, board, sClass, subject);
//                    return null;
//                }
//            };
//            try {
//                task.execute().get();
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
//                        list = topicPathDatabase.daoAccess().fetchReportsTopicWisePathDetail(userId, board, sClass, subject, topicId);
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

    public boolean isDataExist(String userId, String board, String sClass, String date, String type) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = countReportsDatabase.daoAccess().isCountFieldExist(userId, board, sClass, date, type);
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

    public void updateField(String userId, String board, String sClass, String date, String type, long count){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                countReportsDatabase.daoAccess().updateCount(userId, board, sClass, date, type, count);
                return null;
            }
        }.execute();
    }

}
