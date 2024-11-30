package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.BooksReportsModel;

import java.util.List;


public class BooksReportRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;

    public BooksReportRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertBookReports(final BooksReportsModel booksReportsModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertBookReports(booksReportsModel);

                return null;
            }
        }.execute();
    }

    public LiveData<List<BooksReportsModel>> getBookReports(String userID , String category , String bookID) {
        return categoryDatabase.daoAccess().fetchBookReports(userID , category , bookID);
    }


}

