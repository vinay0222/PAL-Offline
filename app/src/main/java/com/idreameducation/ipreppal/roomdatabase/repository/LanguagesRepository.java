package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.LanguageModel;

import java.util.List;


public class LanguagesRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;
    public LanguagesRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertLanguage(final LanguageModel languageModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertLanguages(languageModel);

                return null;
            }
        }.execute();
    }


    public LiveData<List<LanguageModel>> getLanguages() {
        return categoryDatabase.daoAccess().fetchLanguage();
    }


}

