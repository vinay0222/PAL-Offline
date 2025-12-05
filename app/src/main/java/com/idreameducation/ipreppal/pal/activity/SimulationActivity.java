package com.idreameducation.ipreppal.pal.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoReportsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseVideoRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoReportRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SimulationActivity extends AppCompatActivity {

    WebView webView;
    String offlinePath;
    String sClass;
    String topicName,subjectName;
    String id;
    String filename;
    String bookName;
    String categoryID;
    Context context;
    ImageView imageViewBack;
    long updatedTime = 0L;
    private String timeToSendTofirebase;
    private Long seconds, minutes, MilliSeconds;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long startTime = 0L;
    private Long Seconds, Minutes;
    private String time = "0";
    private Handler customHandler = new Handler();
    private long totalTime;
    private long classTime;
    private long mStartTime = 0L;
    private Handler mHandler = new Handler();
    private Global global;
    private String timeToSendTofirebase_;
    ImageView iprep_logo;
    private RelativeLayout second_layout;
    List<String> pages = new ArrayList<>();
    int pageNumber = 0;
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private long totalTimee = 0;
    private long endTime = 0L;
    private Long millis;
    private long total_time_spent_video_lessons;
    private long total_no_of_videos_played;
    private VideoReportRepository videoReportRepository;
    private long total_time_spent;
    private long total_timing;
    private int videoCount = 0;
    private String subject, name;

    private String onlinePath="https://download.iprep.in/idream_content/simulation/";

    private final Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;
            long secs = (long) (updatedTime / 1000);
            long mins = secs / 60;
            secs = secs % 60;
            customHandler.postDelayed(this, 1);
        }
    };
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        Util.setLandscapeView(this);
        setContentView(R.layout.activity_simulation);
        Util.handleNotch(this);
        context = this;
        assignIds(savedInstanceState);
        listener();

        if (mStartTime == 0L) {
            mStartTime = SystemClock.uptimeMillis();
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveReports();
    }

    boolean once=true;

    private void saveReports() {

        String board = Util.getSelectedBoard(context);
        String videoName = getIntent().getExtras().getString("videoName");
        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);
        subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
        String language = Util.getSelectedLanguage(context);
        startTime = System.currentTimeMillis();
        String system_millis= String.valueOf(System.currentTimeMillis());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        long ttime=timeTosync;
        videoNumber = videoNumber + 1;
//        timeTosync = timeTosync + timeget;
//        timeTosync = timeTosync + timeget;

        long syncClassTime = updatedTime + classTime;

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(topicName);
        scoreModel.setTime(timeToSendTofirebase_ + "");
        scoreModel.setTotalTime(ttime+ "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate("" + date);
        scoreModel.setSubjectName(subjectName);


        try {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise")
                    .child(Util.getUserId(context)).child(board).child(sClass)
                    .child(Util.getSelectedLanguage(context)).child(subject.toLowerCase().replace(" ","_"))
                    .child("simulation_content").child(date).child(String.valueOf(topicName)).child("name")
                    .setValue(name);

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise")
                    .child(Util.getUserId(context)).child(board).child(sClass)
                    .child(Util.getSelectedLanguage(context)).child(subject.toLowerCase().replace(" ","_"))
                    .child("simulation_content").child(date).child(String.valueOf(topicName)).child("detail")
                    .child(String.valueOf(id)).child(system_millis).setValue(scoreModel);

            ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;

            reportsTopicWiseVideoRepository=new ReportsTopicWiseVideoRepository(context);

            if(once) {
                if(reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "simulation_content", String.valueOf(id), date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
                    reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "simulation_content", String.valueOf(id),
                            date, System.currentTimeMillis(), name, timeToSendTofirebase_ + "", topicName, timeTosync + "", videoName,Util.getSelectedLanguage(context));
                    once=false;
                }else{
                    ReportsTopicWiseVideoModel reportsTopicWiseVideoModel = new ReportsTopicWiseVideoModel();
                    reportsTopicWiseVideoModel.setUserId(Util.getUserId(context));
                    reportsTopicWiseVideoModel.setBoard(board);
                    reportsTopicWiseVideoModel.setSClass(sClass);
                    reportsTopicWiseVideoModel.setSubject(Util.getSubjectId(context));
                    reportsTopicWiseVideoModel.setDate(date);
                    reportsTopicWiseVideoModel.setName(name);
                    reportsTopicWiseVideoModel.setTime(System.currentTimeMillis());
                    reportsTopicWiseVideoModel.setType("simulation_content");
                    reportsTopicWiseVideoModel.setTopicId(String.valueOf(id));
                    reportsTopicWiseVideoModel.setTopicName(topicName);
                    reportsTopicWiseVideoModel.setTotalTime(timeToSendTofirebase_ + "");
                    reportsTopicWiseVideoModel.setVTime(timeToSendTofirebase_ + "");
                    reportsTopicWiseVideoModel.setVideoName(videoName);
                    reportsTopicWiseVideoModel.setVideoId(String.valueOf(id));
                    reportsTopicWiseVideoModel.setLang(Util.getSelectedLanguage(context));
                    reportsTopicWiseVideoModel.setSubjectName(subject);
                    reportsTopicWiseVideoRepository.insertVideoDetails(reportsTopicWiseVideoModel);
                    once=false;
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    syncClassTime_();
                }
            },1000);

        }
        catch (Exception rr )
        {
            rr.printStackTrace();
        }
    }

    private void syncClassTime_() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

//        long syncClassTime = updatedTime + totalTimee;
//        HashMap<String, String> hashMapToSync = new HashMap<>();
//        hashMapToSync.put("time", "" + syncClassTime);
//        hashMapToSync.put("name", videoName);
//        hashMapToSync.put("username", Util.getUsername(context));
//        hashMapToSync.put("userclass", "6");
//        hashMapToSync.put("watchedTime", System.currentTimeMillis()+"");
//        hashMapToSync.put("totalDuration", timeToSendTofirebase + "");
//        hashMapToSync.put("finaltime",milliseconds+"");
//        hashMapToSync.put("topicName", id + "");
//        hashMapToSync.put("date", formattedDate);


        String board = Util.getSelectedBoard(context);
        String videoName = getIntent().getExtras().getString("videoName");
        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);
        subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
        String language = Util.getSelectedLanguage(context);

        String date = Util.getCurrentDateWithDifferentFormat();
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        long timeTosync2 ;
        long ttime=timeTosync;
        videoNumber = videoNumber + 1;

        timeTosync2 = Long.parseLong(timeToSendTofirebase_)+ timeget;


        HashMap<String, String> row_usage = new HashMap<>();
//        row_usage.put("category","activity_videos");
//        row_usage.put("class",Util.getSelectedClass(context));
//        row_usage.put("content_name",videoName);
//        row_usage.put("topic_name",Util.getTopicNameAlt(context));
//        row_usage.put("district",Util.getDistrict(context));
//        row_usage.put("timePlayed",""+timeTosync);
//        row_usage.put("schoolID",Util.getSchoolId(context));
//        row_usage.put("schoolName",Util.getSchoolName(context));
//        row_usage.put("projectId",Util.getProjectId(context));
//        row_usage.put("score",time + "");
//        row_usage.put("state",Util.getSelectedState(context));
//        row_usage.put("subject",Util.getSubject(context));
//        row_usage.put("subject_name",subject);
//        row_usage.put("time_spent",ttime + "");
//        row_usage.put("topic",""+topicID);
//        row_usage.put("language",""+Util.getSelectedLanguage(context));
//        row_usage.put("userId",Util.getUserId(context));
//        row_usage.put("username",Util.getUsername(context));
//        row_usage.put("userIdFirebase",Util.getLoginUserId(context));


        row_usage.put("category", "simulation_content");
        row_usage.put("class", Util.getSelectedClass(context));
        row_usage.put("content_name", videoName);
        row_usage.put("topic_name", topicName);
        row_usage.put("district", Util.getDistrict(context));
        row_usage.put("schoolID", Util.getSchoolId(context));
        row_usage.put("schoolName", Util.getSchoolName(context));
        row_usage.put("projectId", Util.getProjectId(context));
        row_usage.put("state", Util.getSelectedState(context));
        row_usage.put("subject", Util.getSubjectId(context));// subject id
        row_usage.put("subject_name", subjectName);
        row_usage.put("language", Util.getSelectedLanguage(context));
        row_usage.put("time_spent", timeToSendTofirebase_ + "");
        row_usage.put("topic", "" + id);
        row_usage.put("userId", Util.getUserId(context));
        row_usage.put("username", Util.getUsernameShowable(context));
        // row_usage.put("userIdFirebase", Util.getLoginUserId(context));
        row_usage.put("board",Util.getSelectedBoard(context));
        row_usage.put("category_name","Simulation");
        row_usage.put("userType", "students");
        row_usage.put("app_id", Util.getAPPID(context));

        global.getDatabaseReference().child(Util.rawUsageNode)
                .child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode)
                .child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);

        global.getDatabaseReference().child(ApplicationConstants.REPORTS)
                .child(Util.getUserId(context))
                .child(Util.getSelectedBoard(context))
                .child(sClass).child("time_spent")
                .child(date)
                .child("Simulation_project")
                .setValue(timeTosync2);

        String name="Simulation";
        if(Util.getSelectedLanguage(context).equals("hindi")) {
            name="सिमुलेशन";
        }

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(sClass).child("time_spent").child(date).child(name).setValue(timeTosync2);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(sClass).child("count").child(date).child("activityvideos").setValue(videoNumber);


        HashMap<String, String> hashmapForTimeAndCount = new HashMap<>();
        hashmapForTimeAndCount.put("date", formattedDate);
        hashmapForTimeAndCount.put("totalDuration", timeToSendTofirebase_ + "");

        System.out.println("--- uid "+Util.getUserId(context));

    }


    private long timeget;

    private void getTime2() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
//            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubjectName(context), date, "video_lessons", "timeTask");
        }else{

            String name="Simulation";
            if(Util.getSelectedLanguage(context).equals("hindi")) {
                name="सिमुलेशन";
            }
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(sClass).child("time_spent").child(date).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue() != null) {
                                timeget = (long) snapshot.getValue();
//                                Toast.makeText(context, "time" + timeget, Toast.LENGTH_SHORT).show();
                            } else {
                                timeget = 0;
                            }
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


    private void listener() {
        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeToSendTofirebase.equalsIgnoreCase("0:00")){
                    onBackPressed();
                }else {
//                    syncClassTime();
                    onBackPressed();
                }
            }
        });
    }


//    private Runnable mUpdateTimeTask = new Runnable() {
//        public void run() {
//            final long start = mStartTime;
//            long millis = SystemClock.uptimeMillis() - start;
//            timeToSendTofirebase_ = String.valueOf(millis);
//            seconds = (long) (millis / 1000);
//            minutes = seconds / 60;
//            seconds = seconds % 60;
//            //String text = "" + minutes + ":" + String.format("%02d", seconds);
//            timeToSendTofirebase = "" + minutes + ":" + String.format("%02d", seconds);
//            //mTimeLabel.setText("" + minutes + ":" + String.format("%02d", seconds));
//            handler.postDelayed(this, 100);
//        }
//    };

    public Runnable runnable = new Runnable() {
        public void run() {
            final long start = mStartTime;
            MillisecondTime = (SystemClock.uptimeMillis() - start);
            timeToSendTofirebase_ = String.valueOf(MillisecondTime);
            UpdateTime = TimeBuff + MillisecondTime + Long.parseLong(time);
//            time = String.valueOf(UpdateTime);
            Seconds = (long) (MillisecondTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            timeToSendTofirebase = "" + Minutes + ":" + String.format("%02d", Seconds);
            MilliSeconds = (long) (UpdateTime % 1000);
            handler.postDelayed(this, 1000);
        }

    };

    @SuppressLint("SetJavaScriptEnabled")
    private void assignIds(Bundle savedInstanceState) {

        handler = new Handler();
        global = (Global) getApplicationContext();
        database = FirebaseDatabase.getInstance();
        startTime = System.currentTimeMillis();
        databaseReference = database.getReference();

        webView = findViewById(R.id.custom_WebView);
        imageViewBack = findViewById(R.id.imageViewBack);
        iprep_logo = findViewById(R.id.iprep_logo);

        topicName = getIntent().getStringExtra("topicName");
        id = getIntent().getStringExtra("id");
        filename = getIntent().getStringExtra("onlineLink");
        name = getIntent().getStringExtra("name");
        categoryID = "simulation_content";
        offlinePath = getIntent().getStringExtra("offlineLink");
        subjectName = getIntent().getStringExtra("subjectName");

        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);

        second_layout = findViewById(R.id.second_layout);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) return false;
                if (url.startsWith("intent")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            webView.loadUrl(fallbackUrl);
                            webView.setWebChromeClient(new WebChromeClient() {
                                @Override
                                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                                    callback.invoke(origin, true, false);
                                }
                            });
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            return true;
                        }
                    } catch (URISyntaxException e) {
                        //not an intent uri
                    }
                    return true;//do nothing in other cases
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });


        videoReportRepository = new VideoReportRepository(getApplicationContext());

        getTime2();
        startSimulation();

    }

    private void startSimulation() {

        if(Util.isOfflineMode(context)) {
            String filePath = (Util.getSDCardPath(context) + "/.iDream_content/simulation/" + offlinePath);
            File file  = new File(filePath);
            if (file.exists()) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("file:///" + String.valueOf(filePath));

                File file1 = new File(filePath.toString());
                if (file1.exists()) {
                    try {
                        getVideoCount();
                        getTime();
                        getTimevideo_lessons();
                        totalcount();
                        subjectdetaills();
                        t_time();
                        tsubject_time();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            else playOnline();
        }
        else playOnline();
    }

    private void playOnline() {

        if(!Util.checkInternetConnection(context)) {
            webView.setVisibility(View.GONE);
            Toast.makeText(context, "Internet not available", Toast.LENGTH_SHORT).show();
            return;
        }
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(onlinePath+offlinePath);

        System.out.println("--------- link "+onlinePath+offlinePath);

        try {
            getVideoCount();
            getTime();
            getTimevideo_lessons();
            totalcount();
            subjectdetaills();
            t_time();
            tsubject_time();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("reports_count").child(Util.getUserId(context)).child("videolessons").child("count").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    if (snapshot.getValue() != null) {
//                        videoNumber = (long) snapshot.getValue();
////                        Util.setVideoTotalCount(context, (int) videoNumber);
//                    } else {
//                        videoNumber = 0;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    private void getTime() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("Total_time").child("total_time_spent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            total_time_spent = (long) snapshot.getValue();
                            Log.v("total_time_spent", String.valueOf(total_time_spent));
                        } else {
                            total_time_spent = 0;
                        }
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

    private void getTimevideo_lessons() {
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(getApplicationContext())).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child("Total_time").child("total_time_spent_video_lessons").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    if (snapshot.getValue() != null) {
//                        if (snapshot.getValue() != null) {
//                            total_time_spent_video_lessons = (long) snapshot.getValue();
//                        } else {
//                            total_time_spent_video_lessons = 0;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void totalcount() {

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("totalCount").child(categoryID).child("totalcount").child("Total_no_of_count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            total_no_of_videos_played = (long) snapshot.getValue();
                        } else {
                            total_no_of_videos_played = 0;
                        }
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

    private void subjectdetaills() {

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(getApplicationContext())).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(getApplicationContext())).child(Util.getSelectedLanguage(getApplicationContext())).child("Subject").child(Util.getSubject(getApplicationContext())).child("total_timing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            total_timing = (long) snapshot.getValue();
                        } else {
                            total_timing = 0;
                        }
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

    private long t_timing;

    private void t_time() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child("overall").child(id).child("t_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            t_timing = (long) snapshot.getValue();
                        } else {
                            t_timing = 0;
                        }
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

    private void tsubject_time() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(id).child("t_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            t_timing = (long) snapshot.getValue();
                        } else {
                            t_timing = 0;
                        }
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

    private void syncClassTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String date = Util.getCurrentDateWithDifferentFormat();

        Log.i("Updated Time : ", updatedTime + "");
        long syncClassTime = updatedTime + totalTimee;
        HashMap<String, String> hashMapToSync = new HashMap<>();
        hashMapToSync.put("name", name);
        hashMapToSync.put("username", Util.getUsername(context));
        hashMapToSync.put("userclass", Util.getSelectedClass(context));
        hashMapToSync.put("totalDuration", timeToSendTofirebase + "");
        hashMapToSync.put("finaltime", millis + "");
        hashMapToSync.put("topicName", topicName + "");
        hashMapToSync.put("topicID", id + "");
        hashMapToSync.put("userSelectedClass", Util.getSelectedClass(context));
        hashMapToSync.put("date", formattedDate);
        hashMapToSync.put("subjectName", subject);

        hashMapToSync.put("totalMinutes", "2000 Mins");

        Log.e("userid", Util.getUserId(context));

        HashMap<String, String> subjecthashmap = new HashMap<>();
        subjecthashmap.put("subjectname", Util.getSubject(getApplicationContext()));

        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        long finalt_timg = t_timing + Seconds;


        timeTosync = timeTosync + total_time_spent;


        long total_time_spent_firebase = total_time_spent + Seconds;
        long total_time_spent_video_lessons_firebase = total_time_spent_video_lessons + Seconds;

        long totalCount = total_no_of_videos_played + 1;

//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("Subject").child(Util.getSubject(context)).setValue(subjecthashmap);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("Total_time").child("total_time_spent").setValue(total_time_spent_firebase);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child("Total_time").child("total_time_spent_video_lessons").setValue(total_time_spent_video_lessons_firebase);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("totalCount").child(categoryID).child("totalcount").child("Total_no_of_count").setValue(totalCount);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(date).child(topicName).child(id).child("" + System.currentTimeMillis()).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(date).child(topicName).child(id).child("count").setValue(videoNumber);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("Unique Days").child(date).setValue(videoCount);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child("Date_Wise").child(date).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(topicName).child(id).child("" + System.currentTimeMillis()).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child("Topic_Wise").child(id).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(topicName).child(id).child("" + System.currentTimeMillis()).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child("overall").child(id).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(id).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("variable").child(categoryID).child("overall").child(id).child(date + System.currentTimeMillis()).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child("variable").child(categoryID).child(Util.getSubject(context)).child(id).child(date + System.currentTimeMillis()).setValue(hashMapToSync);
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child("overall").child(id).child("t_time").setValue(finalt_timg);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguage(getApplicationContext())).child(categoryID).child(Util.getSubject(context)).child(id).child("t_time").setValue(finalt_timg);


        HashMap<String, String> hashmapForTimeAndCount = new HashMap<>();
        hashmapForTimeAndCount.put("date", formattedDate);
        hashmapForTimeAndCount.put("totalDuration", timeToSendTofirebase + "");

        VideoReportsModel videoReportsModel = new VideoReportsModel();
        videoReportsModel.setVideoID("" + id);
        videoReportsModel.setCategory("simulation_content");
        videoReportsModel.setTime("" + time);
//        videoReportsModel.setDuration("" + timeToSendTofirebase);
        videoReportsModel.setBoard(Util.getSelectedBoard(context));
        videoReportsModel.setUserID(Util.getUserId(context));
        videoReportsModel.setStudentClass(sClass);
        videoReportsModel.setType("ClassTime");
        videoReportsModel.setSession(System.currentTimeMillis());
        videoReportRepository.insertVideoReports(videoReportsModel);



    }


    public void updateTask() {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {


//                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .taskDao()
//                        .updatesimulation(1);


                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                TotalTimeOfCategory();
            }

        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void TotalTimeOfCategory() {
        class TotalTimeOfCategory extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
//                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .taskDao()
//                        .updateTotalTimeCount(1, Integer.parseInt(timeToSendTofirebase_));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

        }

        TotalTimeOfCategory totalTimeOfCategory = new TotalTimeOfCategory();
        totalTimeOfCategory.execute();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        syncClassTime();
        overridePendingTransition(R.anim.slide_in_rever, R.anim.slide_out_rever);
    }

}