package com.idreameducation.ipreppal.roomdatabase.repository;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.ActivationModel;

import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;


public class ActivationDetailsRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
    private Global global;

    public ActivationDetailsRepository(Context context) {
        global=(Global)context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
        //= Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();


    }


    public void insertActivationDetails(final ActivationModel activationModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertActivationDetails(activationModel);

                return null;
            }
        }.execute();
    }


    public LiveData<List<ActivationModel>> getActivationDetails() {
        return categoryDatabase.daoAccess().fetchActivationDetail();
    }


}

