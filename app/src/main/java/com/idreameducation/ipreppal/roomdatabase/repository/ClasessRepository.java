package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ClassesModel;

import java.util.List;


public class ClasessRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;
    public ClasessRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
      //  categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertClasses(final ClassesModel classesModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertClasses(classesModel);

                return null;
            }
        }.execute();
    }


    public LiveData<List<ClassesModel>> getClasses(String language, String board) {
        return categoryDatabase.daoAccess().fetchClasses(language, board);
    }


}

