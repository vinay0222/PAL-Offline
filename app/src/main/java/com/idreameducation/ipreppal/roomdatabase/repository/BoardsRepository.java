package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.BoardsModel;

import java.util.List;


public class BoardsRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
    private Global global;

    public BoardsRepository(Context context) {

        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
        //= Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertBoards(final BoardsModel boardsModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertBoards(boardsModel);

                return null;
            }
        }.execute();
    }


    public LiveData<List<BoardsModel>> getBoards(String language) {
        return categoryDatabase.daoAccess().fetchBoards(language);
    }


}

