package com.idreameducation.ipreppal.roomdatabase.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoundationalTopicRepository {

    private final String DB_NAME = "db_Content";

    private final ContentDatabase foundationalTopicDatabase;
    private final Global global;
    private List<FoundationalTopicModel> list;

    public FoundationalTopicRepository(Context context) {
        global = (Global) context.getApplicationContext();
        foundationalTopicDatabase = global.getRoomDatabase();
//        categoryDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
    }


    public void insertFoundationalTopicDetails(final FoundationalTopicModel foundationalTopicModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foundationalTopicDatabase.daoAccess().insertFoundationalTopicDetails(foundationalTopicModel);
                return null;
            }
        }.execute();
    }

    public List<FoundationalTopicModel> getAllFoundationalTopicDetails(String userId) {
        return foundationalTopicDatabase.daoAccess().fetchAllTopicDetails(userId);
    }

    public LiveData<List<FoundationalTopicModel>> getFoundationalTopicsDetail(String userId, String subjectId, String seniorTopicId, String topicId, String seniorClass) {
//        if(topicId != null){
            return foundationalTopicDatabase.daoAccess().fetchTopicDetail(userId, subjectId, seniorTopicId, topicId);
//        }
//        else{
//            return foundationalTopicDatabase.daoAccess().fetchAllTopicDetails(seniorTopicId, seniorClass);
//        }
    }

    public boolean isDataExist(String userId, String subjectId, String seniorTopicId, String seniorClass, String topicId, String lang) {
        final boolean[] exists = new boolean[1];
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    exists[0] = foundationalTopicDatabase.daoAccess().isRowExist(userId, subjectId, seniorTopicId, seniorClass, topicId,lang);
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

    public void updateFields(String userId, String subjectId, String seniorTopicId, String seniorClass, String seniorTopicName, String topicId, String topicName, String sClass, String streakProgress, String streakCount, String incorrectStreak, String practiceType, boolean show, int videoLevel, int percentage,String lang) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foundationalTopicDatabase.daoAccess().updateFields(userId, subjectId, seniorTopicId, seniorClass, seniorTopicName, topicId, topicName, sClass, streakProgress, streakCount, incorrectStreak, practiceType, show, videoLevel, percentage,lang);
                return null;
            }
        }.execute();
    }

    public void updateField(String userId, String subjectId, String topicId, String seniorTopicId, boolean show){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foundationalTopicDatabase.daoAccess().updateShowField(userId, subjectId, topicId, seniorTopicId, show);
                return null;
            }
        }.execute();
    }

}
