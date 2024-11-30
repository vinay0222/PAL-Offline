package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentDetailsRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase studentDetailsDatabase;
    private final Global global;
    private List<StudentDetailsModel> list;

    public StudentDetailsRepository(Context context) {
        global = (Global) context.getApplicationContext();
        studentDetailsDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertStudentDetails(final StudentDetailsModel studentDetailsModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    studentDetailsDatabase.daoAccess().insertStudentDetails(studentDetailsModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public List<StudentDetailsModel> getDetail(String userId, String ngoId) {
        if(userId == null){
            return studentDetailsDatabase.daoAccess().fetchStudentDetailsData(ngoId);
        }else{
            return studentDetailsDatabase.daoAccess().fetchStudentDetails(userId);
        }
//        list = new ArrayList<>();
//        try {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    list = studentDetailsDatabase.daoAccess().fetchStudentDetails(ngoId, userId);
//                    return null;
//                }
//            }.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return list;
    }

    public boolean isDataExist(String userId, String ngoId) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        exists[0] = studentDetailsDatabase.daoAccess().isStudentExist(ngoId, userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void updateField(String userId, String ngoId, String studentName, String studentPassword, String userName, String board, String sClass, String language){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                studentDetailsDatabase.daoAccess().updateStudentDetails(ngoId, userId, studentName, studentPassword, userName, sClass, board, language);
                return null;
            }
        }.execute();
    }
}