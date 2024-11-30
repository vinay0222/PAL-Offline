package com.idreameducation.ipreppal.roomdatabase.repository;

import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.CategoryModel;

import java.util.List;


public class CategoryRepository {


    private String DB_NAME = "db_Content";

    private ContentDatabase categoryDatabase;
private Global global;

    public CategoryRepository(Context context) {
        global = (Global) context.getApplicationContext();
        categoryDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }

    public void insertTask(String title,
                           String description) {
        insertTask(title, description, false, null);
    }

    public void insertTask(String name,
                           String icon,
                           boolean isEncrypted,
                           String id) {

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setIcon(icon);
        categoryModel.setCategoryID(id);
        categoryModel.setName(name);
        categoryModel.setEncryptedContent(isEncrypted);


        insertTask(categoryModel);
    }

    public void insertTask(final CategoryModel categoryModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                categoryDatabase.daoAccess().insertTask(categoryModel);
                return null;
            }
        }.execute();
    }


//    public LiveData<List<CategoryModel>> getTask() {
//        return categoryDatabase.daoAccess().getTask();
//    }

    public LiveData<List<CategoryModel>> getTasks(String studentClass, String language, String board) {
        return categoryDatabase.daoAccess().fetchAllTasks(studentClass , language , board);
    }
}

