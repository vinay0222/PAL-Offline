package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetTopicLevelsDetails {

   public static void task(Context context, String sClass, String topicId, Global global, String board, ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository){
       if(Util.isOfflineMode(context)){
           showPracticeOrTestTask task = new showPracticeOrTestTask(context, sClass, topicId, global, board, reportsDiagnosticTestCompleteRepository);
           task.execute();
       }else{
           global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   try {
                       if (snapshot.getValue() != null) {
                           HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                           String next_topic_id = detailHashMap.get("Next_topic_id");
                           String foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                           String topic_id = detailHashMap.get("TopicID");
                           String next_chapter_name = detailHashMap.get("next_chapter_name");
                           if (next_topic_id.equalsIgnoreCase("Not Available")) {
                               setNextTopicAvailable(false);
                           } else {
                               setNextTopicAvailable(true);
                               setNextDiagnosticTestAvailable(global, next_topic_id, context, board, sClass, reportsDiagnosticTestCompleteRepository);
                           }

                           setJuniorTopicAvailable(!foundational_Topic_ID.equalsIgnoreCase("Not Available") && !foundational_Topic_ID.equalsIgnoreCase("Not Available "));

                           setSameTopicAvailable(topic_id != null && topic_id.length() > 0);
                           setNextTopicId(next_topic_id);
                           setNextTopicName(next_chapter_name);
                       }else{
                           setNextTopicAvailable(false);
                           setJuniorTopicAvailable(false);
                           setSameTopicAvailable(false);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
   }

    private static class showPracticeOrTestTask extends AsyncTask<Void, Void, Void> {

        private Context context = null;
        private String sClass = null;
        private String topicId = null;
        private Global global = null;
        private String board = null;
        private final ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;

        private showPracticeOrTestTask(Context context, String sClass, String topicId, Global global, String board, ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository) {
            this.context = context;
            this.sClass = sClass;
            this.topicId = topicId;
            this.global = global;
            this.board = board;
            this.reportsDiagnosticTestCompleteRepository = reportsDiagnosticTestCompleteRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(topicId);
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String next_topic_id = object_______.getString("Next_topic_id");
                String next_chapter_name = object_______.getString("next_chapter_name");
                String StreakCount = object_______.getString("StreakCount");
                String foundational_class = object_______.getString("foundational_class");
                String incorrectStreak = object_______.getString("incorrectStreak");
                String topic_id = object_______.getString("TopicID");

                setJuniorTopicAvailable(!Foundational_Topic_ID.equalsIgnoreCase("Not Available") && !Foundational_Topic_ID.equalsIgnoreCase("Not Available "));

                if (next_topic_id.equalsIgnoreCase("Not Available")) {
                    setNextTopicAvailable(false);
                } else {
                    setNextTopicAvailable(true);
                    setNextDiagnosticTestAvailable(global, next_topic_id, context, board, sClass, reportsDiagnosticTestCompleteRepository);
                }

                setSameTopicAvailable(topic_id != null && topic_id.length() > 0);
                setNextTopicId(next_topic_id);
                setNextTopicName(next_chapter_name);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private static boolean isNextTopicAvailable = false;
    private static boolean isJuniorTopicAvailable = false;
    private static boolean isSameTopicAvailable = false;
    private static boolean isNextDiagnosticTestAvailable = false;
    private static String topicId = null;
    private static String topicName = null;

    private static void setNextTopicAvailable(boolean isNextTopicAvailable){
       GetTopicLevelsDetails.isNextTopicAvailable = isNextTopicAvailable;
    }

    public static boolean isNextTopicAvailable(){
       return isNextTopicAvailable;
    }

    private static void setJuniorTopicAvailable(boolean isJuniorTopicAvailable){
        GetTopicLevelsDetails.isJuniorTopicAvailable = isJuniorTopicAvailable;
    }

    public static boolean isJuniorTopicAvailable(){
        return isJuniorTopicAvailable;
    }

    private static void setSameTopicAvailable(boolean isSameTopicAvailable){
        GetTopicLevelsDetails.isSameTopicAvailable = isSameTopicAvailable;
    }

    public static boolean isSameTopicAvailable(){
        return isSameTopicAvailable;
    }

    private static void setNextTopicId(String topicId){
        GetTopicLevelsDetails.topicId = topicId;
    }

    public static String getNextTopicId(){
        return topicId;
    }

    private static void setNextTopicName(String topicName){
        GetTopicLevelsDetails.topicName = topicName;
    }

    public static String getNextTopicName(){
        return topicName;
    }

    private static void setNextDiagnosticTestAvailable(Global global, String topicId, Context context, String board, String sClass, ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository){
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){

            DiagnosticCompleteTask diagnosticCompleteTask = new DiagnosticCompleteTask(context, Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, reportsDiagnosticTestCompleteRepository);
            diagnosticCompleteTask.execute();

//            List<ReportsDiagnosticCompleteModel> list = reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
//            if(list != null && list.size() > 0){
//                try {
//                    GetTopicLevelsDetails.isNextDiagnosticTestAvailable = list.get(0).isComplete();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }else{
//                GetTopicLevelsDetails.isNextDiagnosticTestAvailable = false;
//            }
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            GetTopicLevelsDetails.isNextDiagnosticTestAvailable = (boolean) snapshot.getValue();
                        }else{
                            GetTopicLevelsDetails.isNextDiagnosticTestAvailable = false;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public static boolean isNextDiagnosticTestAvailable(){
        return isNextDiagnosticTestAvailable;
    }

    private static class DiagnosticCompleteTask extends AsyncTask<Void, Void, ArrayList<ReportsDiagnosticCompleteModel>> {

        Context context;
        String userId;
        String board;
        String sClass;
        String subject;
        String topicId;
        private final ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;

        private DiagnosticCompleteTask(Context context, String userId, String board, String sClass, String subject, String topicId, ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository) {
            this.context = context;
            this.userId = userId;
            this.board = board;
            this.sClass = sClass;
            this.subject = subject;
            this.topicId = topicId;
            this.reportsDiagnosticTestCompleteRepository = reportsDiagnosticTestCompleteRepository;
        }

        @Override
        protected ArrayList<ReportsDiagnosticCompleteModel> doInBackground(Void... voids) {
            return (ArrayList<ReportsDiagnosticCompleteModel>) reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
        }

        @Override
        protected void onPostExecute(ArrayList<ReportsDiagnosticCompleteModel> list) {
            super.onPostExecute(list);
            if(list != null && list.size() > 0){
                try {
                    GetTopicLevelsDetails.isNextDiagnosticTestAvailable = list.get(0).isComplete();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                GetTopicLevelsDetails.isNextDiagnosticTestAvailable = false;
            }
        }
    }
}
