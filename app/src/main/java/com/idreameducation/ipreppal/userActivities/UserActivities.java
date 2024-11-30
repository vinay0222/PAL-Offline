package com.idreameducation.ipreppal.userActivities;


import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.idreameducation.ipreppal.model.CoreContent.PracticeDataModel;
import com.idreameducation.ipreppal.model.CoreContent.VideoDataModel;
import com.idreameducation.ipreppal.model.StudentInfoModel;
import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;
import com.idreameducation.ipreppal.services.MyActivitiesReceiver;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UserActivities {

    private static Context context;
    private static SharedPreferences.Editor sharedPreferences;
    private static SharedPreferences preferences;
    private static final String LOG="User Activities";
    public static String SHAREDPREF_KEY="KEY_SharedPref";    // Main Pref Key
    private static String LOGIN_KEY="KEY_Login";
    public static String FINAL_TEST="Final Test";
    public  static String LASTOPENED_KEY="KEY_last_opened";
    public static String DIAGNOSTIC_TEST="Diagnostic Test";
    public static String SUBJECT_MAP_KEY="KEY_subject_HashMap";
    public static String LOCAL_ASSIGNMENT_KEY="LocalAssignment";
    public static String WEEKLY_NOTIFICATION_KEY="KEY_WeeklyNotification";
    public static String CORECONTENT_NAMES_KEY="CoreContentNames";
    public static String CORECONTENT_DATA_KEY="CoreContentData";
    public static String VIDEO_CONTENT_KEY="VideoCoreContent";
    public static String PRACTICE_CONTENT_KEY="PracticeCoreContent";
    private static int LAST_OPENED_REQUESTCODE=022201;
    private static int SUBJECT_USAGE_REQUESTCODE=022202;

    /** use it for init both sharedPref and UserActivities
     * sharedPreferences => use it for set values
     * preferences => use it for get values */
    public UserActivities(Context context) {
        UserActivities.context =context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE);

        // set current timestamp
        sharedPreferences.putLong(LASTOPENED_KEY,System.currentTimeMillis());
        sharedPreferences.commit();

        syncDataToFirebase();
        firstTimeLogin(context);
        setBackgroundTasks();
        updatedRecentAssignments();

       //        throw new RuntimeException("Crashlytics Test");



    }

    public void fetchCoreContent() {
        if(Util.isOfflineMode(context)) getCoreContentOffline();
        else getCoreContentOnline();
    }


    /** use it for init sharedPref only */
    public UserActivities(Context context,boolean te) {
        UserActivities.context =context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE);
    }

    /** reset Preference */
    public static void resetPref() {
        sharedPreferences.putBoolean(LOGIN_KEY,false);
        sharedPreferences.commit();

        initSubject(new HashMap<String,SubjectInfo_Model>());
    }

    /** sync data to sharedPref from firebase */
    public static void syncDataFromFirebase() {
        FirebaseFirestore.getInstance().collection("UserActivities").document(Util.getUserId(context))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null){
                            HashMap<String, SubjectInfo_Model> subjectMap= (HashMap<String, SubjectInfo_Model>) documentSnapshot.get("subjectsInfo");
                            initSubject(subjectMap);
                        }
                    }
                });
    }

    /** first time login message & initialisations */
    private void firstTimeLogin(Context context) {
        if(!preferences.getBoolean(LOGIN_KEY, false)) {
            FirebaseMessaging.getInstance().subscribeToTopic(Util.getUserId(context));
            HashMap<String,String> data =new HashMap<>();
            data.put("message","Welcome");
            data.put("messageTime","");
            if(Util.getSelectedLanguage(context).equals("hindi")) data.put("notificationMessage","हैलो ! "+Util.getUsernameShowable(context)+" iPrep PAL में आपका स्वागत है- एक व्यक्तिगत अनुकूलित शिक्षण app जो आपकी समझ के स्तर का मूल्यांकन करता है और आपके लिए सभी विषयों, अध्यायों पर 100% महारत हासिल करने के लिए एक विशेष शिक्षण मार्ग तैयार करता है।");
            else data.put("notificationMessage","Hello "+Util.getUsernameShowable(context)+" Welcome to iPrep PAL- A Personalised Adaptive Learning App that diagnoses your learning levels and designs a special learning path for you to achieve 100% mastery on all chapters in all subjects. ");
            data.put("sender","iPrep UserActivities");
            data.put("studentId",Util.getUserId(context));
            data.put("type","welcome notification");
            Util.showNotification(context, "Welcome",data.get("notificationMessage"),new Intent(context, PalSplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),022201);
//            Util.sendNormalNotification(Util.getUserId(context),"Welcome",data.get("notificationMessage"));
            addNotificationMessage(data);
            sharedPreferences.putBoolean(LOGIN_KEY,true);
            sharedPreferences.commit();
        }
    }

    /** add multiple background checkers with different requestCode */
    private void setBackgroundTasks() {
//        setBackgroundChecker(LAST_OPENED_REQUESTCODE);

/*      you can add another background checker by adding different request code
        setBackgroundChecker("Request code"); */
    }

    /** setup background checker */
    private void setBackgroundChecker(int requestCode) {
        // Some time when you want to run
        Date when = new Date();
        when.setHours(10);
        when.setMinutes(0);
        when.setSeconds(0);
        int timeInterval = AlarmManager.RTC;
        try {
            /**  MyActivitiesReceiver => BroadcastReceiver */
            Intent someIntent = new Intent(context, MyActivitiesReceiver.class);
            someIntent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
            AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, someIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            someIntent.putExtra("requestCode",requestCode);
//            timeInterval= (int) 2 * 1000;
            timeInterval= 12 * (int)  AlarmManager.INTERVAL_HOUR;
            alarms.setRepeating(AlarmManager.RTC_WAKEUP, when.getTime(),timeInterval,pendingIntent);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** add notification message in user table */
    public void addNotificationMessage(HashMap<String, String> data) {
        if(Util.getUserId(context)==null) return;
        FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(context))
                .push().setValue(data);
    }

    /** add/update data of subject*/
    private static void initSubject(HashMap<String, SubjectInfo_Model> subjectMap) {
        String jsonString = new Gson().toJson(subjectMap);
        sharedPreferences.putString(SUBJECT_MAP_KEY, jsonString);
        sharedPreferences.apply();
    }

    /** get all subjects info */
    public static HashMap<String, SubjectInfo_Model> getSubjectInfo() {
        String defValue = new Gson().toJson(new HashMap<String, Object>());
        String json=preferences.getString(SUBJECT_MAP_KEY,defValue);
        TypeToken<HashMap<String,SubjectInfo_Model>> token = new TypeToken<HashMap<String,SubjectInfo_Model>>() {};
        HashMap<String,SubjectInfo_Model> retrievedMap=new Gson().fromJson(json,token.getType());
        return retrievedMap;
    }

    /** get subject info by subjectID
     * @param subjectID => Subject ID */
    @Nullable
    private static SubjectInfo_Model getSubjectInfo(String subjectID) {
        HashMap<String,SubjectInfo_Model> subjectMap= getSubjectInfo();
        if(subjectMap.containsKey(subjectID)) return subjectMap.get(subjectID);
        else return null;
    }

    /** add new subject if subject is already available its return its data
     * @param subjectID => Subject ID
     * @return SubjectInfo_Model => Subject Info  */
    public static SubjectInfo_Model addSubject(String subjectID) {

        SubjectInfo_Model subjectInfo_model=getSubjectInfo(subjectID);

        if(subjectInfo_model!=null) return subjectInfo_model;
        else subjectInfo_model=new SubjectInfo_Model();

        HashMap<String,SubjectInfo_Model> map=getSubjectInfo();
        map.put(subjectID,subjectInfo_model);

        UserActivities.initSubject(map);

        return subjectInfo_model;
    }

    /** update subject last usage timestamp */
    public static void updateSubjectTime(String subjectID,String timeUsage) {
        SubjectInfo_Model model;

        /** getting subject info */
        if(getSubjectInfo(subjectID)!=null) model=getSubjectInfo(subjectID);
        else model=addSubject(subjectID);

        /** old values of subject */
        String previousTime= model.getTotalTimeUsage();
        String currentWeekTime= model.getCurrentWeekTimeUsage();
        HashMap<String,String> previousDailyReport=model.getDailyUsageReport();

        /** update TotalTime & TotalWeekTime */
        long updatedTotalTime=(Long.parseLong(previousTime)+Long.parseLong(timeUsage));
        long updatedCurrentWeekTime=(Long.parseLong(currentWeekTime)+Long.parseLong(timeUsage));

        /** current date */
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String todayString = formatter.format(todayDate);

        /** update weekly report */
        if(previousDailyReport.containsKey(todayString)) {
            String todayTime=previousDailyReport.get(todayString);
            assert todayTime != null;
            long updatedTodayTime= Long.parseLong(todayTime)+Long.parseLong(timeUsage);
            previousDailyReport.put(todayString,String.valueOf(updatedTodayTime));
        }
        else previousDailyReport.put(todayString,timeUsage);

        /** set Updated values on Model */
        model.setLastUsageTimestamp(System.currentTimeMillis());
        model.setCurrentWeekTimeUsage(String.valueOf(updatedCurrentWeekTime));
        model.setTotalTimeUsage(String.valueOf(updatedTotalTime));
        model.setDailyUsageReport(previousDailyReport);

        updateSubjectInfo(subjectID,model);
    }

    /** set this week test complete of subject
     *  @param testType =>  DIAGNOSTIC_TEST / FINAL_TEST
     *  @param subjectID => Subject ID */
    public static void setTestDone(String testType,String subjectID) {
        SubjectInfo_Model model;

        /** getting subject info */
        if(getSubjectInfo(subjectID)!=null) model=getSubjectInfo(subjectID);
        else model=addSubject(subjectID);

        /** Updated values on Model */
        if(testType.equals(DIAGNOSTIC_TEST)) model.setCurrentWeekDiagnostic(true);
        else if(testType.equals(FINAL_TEST)) model.setCurrentWeekFinalTest(true);

        /** update in sharedPref */
        updateSubjectInfo(subjectID,model);
    }

    /** Update subject info
     * @param subjectID => Subject id
     * @param info_model =>  Subject model which contain updated data
     *
     * this function update subject info both in firebase and sharedPref */
    private static void updateSubjectInfo(String subjectID,SubjectInfo_Model info_model) {
        HashMap<String, SubjectInfo_Model> allSubjectInfo=getSubjectInfo();
        allSubjectInfo.put(subjectID,info_model);
        initSubject(allSubjectInfo);
        syncDataToFirebase();
    }

    /** clear weekly report */
    public static void clearWeeklyReport() {
        HashMap<String, SubjectInfo_Model> subjectInfo = getSubjectInfo();

        for(String subjectID: subjectInfo.keySet()) {
            SubjectInfo_Model subjectInfo_model = subjectInfo.get(subjectID);
            ArrayList<String> dateList=subjectInfo_model.getWeeklyReportList();
            String ttime =subjectInfo_model.getCurrentWeekTimeUsage();
            dateList.add(String.valueOf(ttime));
            subjectInfo_model.setDailyUsageReport(new HashMap<String,String>());
            subjectInfo_model.setWeeklyReportList(dateList);

            subjectInfo.put(subjectID,subjectInfo_model);
        }
        /** update subject info */
        initSubject(subjectInfo);
    }

    /** clear or reset weekly test report */
    public static void clearWeeklyTestReport() {
        HashMap<String, SubjectInfo_Model> subjectInfo = getSubjectInfo();

        for(String subjectID: subjectInfo.keySet()) {
            SubjectInfo_Model subjectInfo_model = subjectInfo.get(subjectID);
            subjectInfo_model.setCurrentWeekFinalTest(false);
            subjectInfo_model.setCurrentWeekDiagnostic(false);
            subjectInfo.put(subjectID,subjectInfo_model);
        }
        /** update subject info */
        initSubject(subjectInfo);
    }

    /** sync data to firebase from sharedPref */
    public static void syncDataToFirebase() {
        if(Util.getUserId(context)==null) return;
        HashMap<String,Object> data=new HashMap<>();
        data.put("subjectsInfo",getSubjectInfo());
        data.put("UserActiveOn",preferences.getLong(LASTOPENED_KEY,0));
        data.put("DataSyncOn",System.currentTimeMillis());
        FirebaseFirestore.getInstance().collection("UserActivities").document(Util.getUserId(context))
                .set(data);
    }

    /** update recent assignments in local every time when data is update on backend */
    public void updatedRecentAssignments() {
        if(context==null) return;
        else if(Util.getUserId(context)==null) return;
        FirebaseDatabase.getInstance().getReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child("assignments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    HashMap<String, Object> recentMessageHashMap = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<HashMap<String, Object>> recentMessagesArrayList = new ArrayList<>();
                    for (String key : recentMessageHashMap.keySet()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) recentMessageHashMap.get(key);
                        recentMessagesArrayList.add(data);
                    }
                    updateAssignmentsInPref(recentMessagesArrayList);
                }
                else updateAssignmentsInPref(new ArrayList<HashMap<String, Object>>());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /** Updating assignments in pref */
    public void updateAssignmentsInPref(ArrayList<HashMap<String, Object>> list) {
        if(sharedPreferences==null) return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.putString(LOCAL_ASSIGNMENT_KEY,json).commit();
    }

    /** get assignments from pref */
    public static ArrayList<HashMap<String, Object>> getLocalAssignment() {
        ArrayList<HashMap<String, Object>> messagesArrayList = new ArrayList<>();
        if(sharedPreferences==null) return messagesArrayList;
        Gson gson = new Gson();
        String json = preferences.getString(LOCAL_ASSIGNMENT_KEY, null);
        Type type = new com.google.gson.reflect.TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();
        messagesArrayList = gson.fromJson(json, type);
        return messagesArrayList;
    }


    /** fetch core content from firebase */
    private void getCoreContentOnline() {

//        ArrayList<VideoDataModel> list = searchFromName(VIDEO_CONTENT_KEY,"intro");
//
//        if(list.size()!=0) {
//            return;
//        }
//        else if(!Util.getclasschange(context)) {
//            return;
//        }

        ArrayList<String> videoNameList=new ArrayList<>();
        ArrayList<String> practiceNameList=new ArrayList<>();

        HashMap<String,VideoDataModel> videoDataModelHashMap=new HashMap<>();
        HashMap<String,PracticeDataModel> practiceDataModelHashMap=new HashMap<>();

        FirebaseDatabase.getInstance().getReference().child("core_content")
                .child(Util.getSelectedBoard(context))
                .child(Util.getSelectedClass(context))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.getValue()!=null) {

                            /** fetch Videos */
                            HashMap<String,Object> subjects= (HashMap<String, Object>) snapshot.child(Util.getSelectedLanguage(context).toLowerCase()).child("video_lessons").child("content").getValue();
                            for(String subject:subjects.keySet()) {

                                HashMap<String,Object> _topics= (HashMap<String, Object>) subjects.get(subject);
                                HashMap<String,Object> topics= (HashMap<String, Object>) _topics.get("topics");

                                for(String topicID:topics.keySet()) {

                                    topicID = topicID.trim();

                                    //ncert_eng_bst_12_01_05
                                    ArrayList<Object> levels= (ArrayList<Object>) topics.get(topicID);

                                    for(int i=1;i<=4;i++) {

                                        try {
                                            HashMap<Object,Object> videoss= (HashMap<Object, Object>) levels.get(i);

                                            for(Object videoKey:videoss.keySet()) {

                                                HashMap<String,String> videoInfo= (HashMap<String, String>) videoss.get(videoKey);

                                                VideoDataModel videoDataModel=new VideoDataModel();
                                                videoDataModel.setAssessmentTopicID(videoInfo.get("AssessmentTopicID"));
                                                videoDataModel.setDetail(videoInfo.get("detail"));
                                                videoDataModel.setName(videoInfo.get("name"));
                                                videoDataModel.setOfflineLink(videoInfo.get("offlineLink"));
                                                videoDataModel.setOfflineThumbnail(videoInfo.get("offlineThumbnail"));
                                                videoDataModel.setOnlineLink(videoInfo.get("onlineLink"));
                                                videoDataModel.setThumbnail(videoInfo.get("thumbnail"));
                                                videoDataModel.setTopicName(videoInfo.get("topicName"));

                                                videoDataModel.setSubjectID(subject);
                                                videoDataModel.setKey(String.valueOf(videoKey));

                                                if(!videoNameList.contains(videoDataModel.getName().toLowerCase().trim())) {
                                                    videoNameList.add(videoDataModel.getName().toLowerCase().trim());
                                                    videoDataModelHashMap.put(videoDataModel.getName().toLowerCase().trim(),videoDataModel);
                                                }

                                            }
                                        }catch (Exception r) {
                                            r.printStackTrace();
                                        }

                                    }

                                }


                            }

                            /** fetch Practice */
                            HashMap<String,Object> subjects2= (HashMap<String, Object>) snapshot.child(Util.getSelectedLanguage(context).toLowerCase()).child("practice").child("content").getValue();
                            for(String subject:subjects2.keySet()) {

                                HashMap<String,Object> _topics= (HashMap<String, Object>) subjects2.get(subject);
                                HashMap<String,Object> topics= (HashMap<String, Object>) _topics.get("topics");

                                for(String topicID:topics.keySet()) {

                                    HashMap<String,String> practiceInfo= (HashMap<String, String>) topics.get(topicID);

                                    PracticeDataModel practiceDataModel=new PracticeDataModel();
                                    practiceDataModel.setDisplay(practiceInfo.get("Display"));
                                    practiceDataModel.setFoundational_Topic_ID(practiceInfo.get("Foundational_Topic_ID"));
                                    practiceDataModel.setIsModelTestPaper(practiceInfo.get("IsModelTestPaper"));
                                    practiceDataModel.setLevels(practiceInfo.get("Levels"));
                                    practiceDataModel.setNext_topic_id(practiceInfo.get("Next_topic_id"));
                                    practiceDataModel.setStreakCount(practiceInfo.get("StreakCount"));
                                    practiceDataModel.setTName(practiceInfo.get("TName"));
                                    practiceDataModel.setTName_alt(practiceInfo.get("TName_alt"));
                                    practiceDataModel.setTopicID(practiceInfo.get("TopicID"));
                                    practiceDataModel.setFoundational_class(practiceInfo.get("foundational_class"));
                                    practiceDataModel.setIncorrectStreak(practiceInfo.get("incorrectStreak"));
                                    practiceDataModel.setIsAlternateLanguageAvailable(practiceInfo.get("isAlternateLanguageAvailable"));
                                    practiceDataModel.setNext_chapter_name(practiceInfo.get("next_chapter_name"));
                                    practiceDataModel.setNext_class(practiceInfo.get("next_class"));
                                    practiceDataModel.setNext_class_subject_name(practiceInfo.get("next_class_subject_name"));

                                    practiceDataModel.setSubjectID(subject);

                                    if(!practiceNameList.contains(practiceDataModel.getTName().toLowerCase().trim())) {
                                        practiceNameList.add(practiceDataModel.getTName().toLowerCase().trim());
                                        practiceDataModelHashMap.put(practiceDataModel.getTName().toLowerCase().trim(),practiceDataModel);
                                    }

                                }


                            }

                            /** updated Names in Pref */
                            updateCoreContent_SearchFormat_InPref(VIDEO_CONTENT_KEY,videoNameList,videoDataModelHashMap);
                            updateCoreContent_SearchFormat_InPref(PRACTICE_CONTENT_KEY,practiceNameList,practiceDataModelHashMap);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /** fetch core content from local storage */
    private void getCoreContentOffline() {

//        ArrayList<VideoDataModel> list = searchFromName(VIDEO_CONTENT_KEY,"intro");
//
//        if(list.size()!=0) {
//            return;
//        }
//        else if(!Util.getclasschange(context)) {
//            return;
//        }


        ArrayList<String> videoNameList=new ArrayList<>();
        ArrayList<String> practiceNameList=new ArrayList<>();

        HashMap<String,VideoDataModel> videoDataModelHashMap=new HashMap<>();
        HashMap<String,PracticeDataModel> practiceDataModelHashMap=new HashMap<>();

        String filePath = ".iDream_content/offlinetab_PAL/Class" + Util.getSelectedClass(context) + "_core_content.txt";
        JSONObject jsonObject = Util.readJsonFile(context, filePath);

        try {
            JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));

            /** fetch Videos */
            JSONObject object__1 = object__.getJSONObject("video_lessons");
            JSONObject object__2 = object__1.getJSONObject("content");

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> columnFilterMap = objectMapper.readValue(String.valueOf(object__2), new TypeReference<HashMap<String, Object>>() {});

            HashMap<String,Object> subjects= columnFilterMap;
            for(String subject:subjects.keySet()) {

                HashMap<String,Object> _topics= (HashMap<String, Object>) subjects.get(subject);
                HashMap<String,Object> topics= (HashMap<String, Object>) _topics.get("topics");

                for(String topicID:topics.keySet()) {

                    ArrayList<Object> levels = (ArrayList<Object>) topics.get(topicID);

                    for (int i = 1; i <= 4; i++) {

                        HashMap<Object, Object> videoss = (HashMap<Object, Object>) levels.get(i);

                        for (Object videoKey : videoss.keySet()) {

                            HashMap<String, String> videoInfo = (HashMap<String, String>) videoss.get(videoKey);

                            VideoDataModel videoDataModel = new VideoDataModel();
                            videoDataModel.setAssessmentTopicID(videoInfo.get("AssessmentTopicID"));
                            videoDataModel.setDetail(videoInfo.get("detail"));
                            videoDataModel.setName(videoInfo.get("name"));
                            videoDataModel.setOfflineLink(videoInfo.get("offlineLink"));
                            videoDataModel.setOfflineThumbnail(videoInfo.get("offlineThumbnail"));
                            videoDataModel.setOnlineLink(videoInfo.get("onlineLink"));
                            videoDataModel.setThumbnail(videoInfo.get("thumbnail"));
                            videoDataModel.setTopicName(videoInfo.get("topicName"));

                            videoDataModel.setSubjectID(subject);
                            videoDataModel.setKey(String.valueOf(videoKey));

                            if(!videoDataModel.getOfflineLink().equals("undefined"))
                            {
                                if (!videoNameList.contains(videoDataModel.getName().toLowerCase().trim())) {
                                    videoNameList.add(videoDataModel.getName().toLowerCase().trim());
                                    videoDataModelHashMap.put(videoDataModel.getName().toLowerCase().trim(), videoDataModel);
                                }
                            }
                        }

                    }

                }

            }

            /** fetch Practice */

            JSONObject object__11 = object__.getJSONObject("practice");
            JSONObject object__22 = object__11.getJSONObject("content");

            ObjectMapper objectMapper2 = new ObjectMapper();
            HashMap<String, Object> columnFilterMap2 = objectMapper2.readValue(String.valueOf(object__22), new TypeReference<HashMap<String, Object>>() {});

            HashMap<String,Object> subjects2= columnFilterMap2;

            for(String subject:subjects2.keySet()) {

                HashMap<String,Object> _topics= (HashMap<String, Object>) subjects2.get(subject);
                HashMap<String,Object> topics= (HashMap<String, Object>) _topics.get("topics");

                for(String topicID:topics.keySet()) {

                    HashMap<String,String> practiceInfo= (HashMap<String, String>) topics.get(topicID);

                    PracticeDataModel practiceDataModel=new PracticeDataModel();
                    practiceDataModel.setDisplay(practiceInfo.get("Display"));
                    practiceDataModel.setFoundational_Topic_ID(practiceInfo.get("Foundational_Topic_ID"));
                    practiceDataModel.setIsModelTestPaper(practiceInfo.get("IsModelTestPaper"));
                    practiceDataModel.setLevels(practiceInfo.get("Levels"));
                    practiceDataModel.setNext_topic_id(practiceInfo.get("Next_topic_id"));
                    practiceDataModel.setStreakCount(practiceInfo.get("StreakCount"));
                    practiceDataModel.setTName(practiceInfo.get("TName"));
                    practiceDataModel.setTName_alt(practiceInfo.get("TName_alt"));
                    practiceDataModel.setTopicID(practiceInfo.get("TopicID"));
                    practiceDataModel.setFoundational_class(practiceInfo.get("foundational_class"));
                    practiceDataModel.setIncorrectStreak(practiceInfo.get("incorrectStreak"));
                    practiceDataModel.setIsAlternateLanguageAvailable(practiceInfo.get("isAlternateLanguageAvailable"));
                    practiceDataModel.setNext_chapter_name(practiceInfo.get("next_chapter_name"));
                    practiceDataModel.setNext_class(practiceInfo.get("next_class"));
                    practiceDataModel.setNext_class_subject_name(practiceInfo.get("next_class_subject_name"));

                    practiceDataModel.setSubjectID(subject);

                    practiceNameList.add(practiceDataModel.getTName().toLowerCase().trim());
                    practiceDataModelHashMap.put(practiceDataModel.getTName().toLowerCase().trim(),practiceDataModel);
                }


            }

            /** updated Names in Pref */
            updateCoreContent_SearchFormat_InPref(VIDEO_CONTENT_KEY,videoNameList,videoDataModelHashMap);
            updateCoreContent_SearchFormat_InPref(PRACTICE_CONTENT_KEY,practiceNameList,practiceDataModelHashMap);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public void updateCoreContent_SearchFormat_InPref(@NonNull String contentType, ArrayList<String> list, Object dataHashMap) {

        if(contentType.equals(VIDEO_CONTENT_KEY)) {
            HashMap<String,VideoDataModel> videoDataModelHashMap= (HashMap<String, VideoDataModel>) dataHashMap;
            Gson gson = new Gson();
            String json = gson.toJson(videoDataModelHashMap);
            sharedPreferences.putString(CORECONTENT_DATA_KEY+contentType,json).commit();

        }
        else if(contentType.equals(PRACTICE_CONTENT_KEY)) {
            HashMap<String,PracticeDataModel> practiceDataModelHashMap= (HashMap<String, PracticeDataModel>) dataHashMap;
            Gson gson = new Gson();
            String json = gson.toJson(practiceDataModelHashMap);
            sharedPreferences.putString(CORECONTENT_DATA_KEY+contentType,json).commit();
        }
        else {
            Log.e(LOG,"not setting a valid contentType");
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.putString(CORECONTENT_NAMES_KEY+contentType,json).commit();

        Log.i(LOG,"CoreContent Updated!");
    }

    public static ArrayList<String> getCoreContent_Names(String contentType) {
        Gson gson = new Gson();
        String json = preferences.getString(CORECONTENT_NAMES_KEY+contentType, null);
        Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Nullable
    public static Object getCoreContent_DATA(String contentType) {
        Gson gson = new Gson();
        String json = preferences.getString(CORECONTENT_DATA_KEY+contentType, null);
        Type type ;
        if(contentType.equals(VIDEO_CONTENT_KEY)) type = new com.google.gson.reflect.TypeToken<HashMap<String,VideoDataModel>>() {}.getType();
        else if(contentType.equals(PRACTICE_CONTENT_KEY)) type = new com.google.gson.reflect.TypeToken<HashMap<String,PracticeDataModel>>() {}.getType();
        else { Log.e(LOG,"not getting a valid contentType");
            return null;
        }

        return gson.fromJson(json, type);
    }

    public static ArrayList<VideoDataModel> searchFromName(String contentType,String search) {

        ArrayList<String> dd=new ArrayList<>();
        ArrayList<VideoDataModel> aa=new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ArrayList<String> ll= getCoreContent_Names(contentType);
            if(ll!=null) dd = (ArrayList<String>) ll.stream().filter(p -> p.contains(search.toLowerCase().trim())).collect(Collectors.toList());

            HashMap<String,VideoDataModel> videoDataModelHashMap= (HashMap<String, VideoDataModel>) getCoreContent_DATA(contentType);

            for(String name:dd) {
                VideoDataModel videoDataModel = (VideoDataModel) getVideoDataFromName(contentType,name,videoDataModelHashMap);
                aa.add(videoDataModel);
            }
        }
        return aa;
    }

    public static ArrayList<PracticeDataModel> searchFromName2(String contentType, String search) {

        ArrayList<String> dd=new ArrayList<>();
        ArrayList<PracticeDataModel> aa=new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ArrayList<String> ll= getCoreContent_Names(contentType);
            dd = (ArrayList<String>) ll.stream().filter(p -> p.contains(search.toLowerCase().trim())).collect(Collectors.toList());

            HashMap<String,PracticeDataModel> videoDataModelHashMap= (HashMap<String, PracticeDataModel>) getCoreContent_DATA(contentType);

            for(String name:dd) {
                PracticeDataModel videoDataModel = (PracticeDataModel) getPracticeDataFromName(contentType,name,videoDataModelHashMap);
                aa.add(videoDataModel);
            }
        }
        return aa;
    }

    private static Object getVideoDataFromName(String contentType,String name,HashMap<String,VideoDataModel> videoDataModelHashMap) {
        if(contentType.equals(VIDEO_CONTENT_KEY)) return videoDataModelHashMap.get(name);
        return null;
    }

    private static Object getPracticeDataFromName(String contentType,String name,HashMap<String,PracticeDataModel> videoDataModelHashMap) {
        if(contentType.equals(PRACTICE_CONTENT_KEY)) return videoDataModelHashMap.get(name);
        return null;
    }

    public static void setUserInfo(StudentInfoModel userInfo){

        if(context==null) {
         if(Util.getContext()==null) return;
         else context=Util.getContext();
        }

        sharedPreferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE);
        /** getting all users from sharedPref */
        ArrayList<StudentInfoModel> userInfoList =getUserInfo();

        /** add current user in pref */
        userInfoList.add(userInfo);

        /** saving final pref */
        Gson gson = new Gson();
        String json = gson.toJson(userInfoList);
        try {
            sharedPreferences.putString("UserInfo_students",json).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<StudentInfoModel> getUserInfo() {
        if(context==null) {
            context=Util.getContext();
            if(context==null) {
               return new ArrayList<>();
            }
        }
        Gson gson = new Gson();
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE);

        String json = preferences.getString("UserInfo_students", new ArrayList<StudentInfoModel>().toString());

        Type type = new com.google.gson.reflect.TypeToken<ArrayList<StudentInfoModel>>() {}.getType();
        return gson.fromJson(json, type);

    }

}