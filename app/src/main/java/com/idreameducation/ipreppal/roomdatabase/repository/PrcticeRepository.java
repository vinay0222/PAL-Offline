package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.PracticeModel;

import java.util.List;

import androidx.lifecycle.LiveData;


public class PrcticeRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
    private Global global;

    public PrcticeRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
        //categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertPractice(final PracticeModel practiceModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertPractice(practiceModel);
                return null;
            }
        }.execute();
    }

//
//    public LiveData<List<PracticeModel>> getPractice(String studentClass, String language, String board, String categoryID ) {
//        return categoryDatabase.daoAccess().fetchPractice(studentClass, language , board , categoryID );
//    }


    public LiveData<List<PracticeModel>> getPracticeReports(String studentClass, String language, String board, String categoryID) {
        return categoryDatabase.daoAccess().fetchPracticeReport(studentClass, language, board, categoryID);
    }


    public LiveData<List<PracticeModel>> getPractice(String studentClass, String language, String board, String categoryID, String subject) {
        return categoryDatabase.daoAccess().fetchPractice(studentClass, language, board, categoryID, subject);
    }


}

