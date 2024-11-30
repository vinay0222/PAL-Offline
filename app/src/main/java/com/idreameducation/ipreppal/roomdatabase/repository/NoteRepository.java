//package com.idream.android.roomdatabase.repository;
//
//import androidx.lifecycle.LiveData;
//import android.arch.persistence.room.Room;
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.idream.android.roomdatabase.db.ReportDatabase;
//import com.idream.android.roomdatabase.model.Note;
//import com.idream.android.roomdatabase.util.AppUtils;
//
//import java.util.List;
//public class NoteRepository {
//
//
//    private String DB_NAME = "db_task";
//
//    private ReportDatabase noteDatabase;
//    public NoteRepository(Context context) {
//        noteDatabase = Room.databaseBuilder(context, ReportDatabase.class, DB_NAME).build();
//    }
//
//    public void insertTask(String title,
//                           String description) {
//
//        insertTask(title, description, false, null);
//    }
//
//    public void insertTask(String title,
//                           String description,
//                           boolean encrypt,
//                           String password) {
//
//        Note note = new Note();
//        note.setTitle(title);
//        note.setDescription(description);
//        note.setCreatedAt(AppUtils.getCurrentDateTime());
//        note.setModifiedAt(AppUtils.getCurrentDateTime());
//        note.setEncrypt(encrypt);
//
//
//        if(encrypt) {
//            note.setPassword(AppUtils.generateHash(password));
//        } else note.setPassword(null);
//
//        insertTask(note);
//    }
//
//    public void insertTask(final Note note) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                noteDatabase.daoAccess().insertTask(note);
//                return null;
//            }
//        }.execute();
//    }
//
//    public void updateTask(final Note note) {
//        note.setModifiedAt(AppUtils.getCurrentDateTime());
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                noteDatabase.daoAccess().updateTask(note);
//                return null;
//            }
//        }.execute();
//    }
//
//    public void deleteTask(final int id) {
//        final LiveData<Note> task = getTask(id);
//        if(task != null) {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    noteDatabase.daoAccess().deleteTask(task.getValue());
//                    return null;
//                }
//            }.execute();
//        }
//    }
//
//    public void deleteTask(final Note note) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                noteDatabase.daoAccess().deleteTask(note);
//                return null;
//            }
//        }.execute();
//    }
//
//    public LiveData<Note> getTask(int id) {
//        return noteDatabase.daoAccess().getTask(id);
//    }
//
//    public LiveData<List<Note>> getTasks() {
//        return noteDatabase.daoAccess().fetchAllTasks();
//    }
//}
