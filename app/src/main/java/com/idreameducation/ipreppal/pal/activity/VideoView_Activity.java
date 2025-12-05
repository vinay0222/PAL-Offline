
package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.current_duration;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.currentvideo_url;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.duration;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.hideNavigationBar;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.remove_fragment;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.videoKey;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.scrollToCurrentLevel;
import static com.idreameducation.ipreppal.videoPlayer.IPrepFullScreenMediaController.speed;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.fragments.PalDikshaContentFragment;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;
import com.idreameducation.ipreppal.pal.videocontroller.ActivityVideosFullScreenController;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDateWiseVideoRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataVideoRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseVideoRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoReportRepository;
import com.idreameducation.ipreppal.services.SocketService;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenHelper;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.IPrepFullScreenMediaController;
import com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoView_Activity extends AppCompatActivity{

    private Context context;
    public static VideoView videoView;
    private ImageView imageViewCross;
    private ImageView imageViewCrossVideo;
    private RelativeLayout reletive;
    private boolean isFullScreenModeOn;
    private ProgressBar mProgressBar;
    private String type;
    private Global global;
    private String board;
    private String sClass;
    private String subject;
    private String topicID,currentDate,videoID_ForReports;
    private String icon;
    private int videoClosedTiming = 0;
    private String language;
    private int timeforVideo = 0;
    private int timeTOsync;
    public long updatedTime = 0L;
    private long startTime = 0L;
    public long timeInMilliseconds = 0L;
    public long timeSwapBuff = 0L;
    private int time = 0;
    private String timeToSendTofirebase;
    private String timeToSendTofirebase_="0";
    private long totalTimee = 0;
    private final Handler customHandler = new Handler();
    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            customHandler.postDelayed(this, 1);
        }
    };
    private ReportsLatestDataVideoRepository reportsLatestDataVideoRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWiseVideoRepository reportsDateWiseVideoRepository;
    private ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;
    private Disposable timeTaskDisposable;
    private Disposable videoCountTaskDisposable;

    private VideoReportRepository videoReportRepository;
    private VideoDetailsRepository videoDetailsRepository;

    private boolean isLocalFile;
    private long mStartTime = 0L;
    private TextView mTimeLabel;
    private RelativeLayout linearLayoutBottom;
    private TextView textViewNextVideo;
    private TextView textViewPractice;
    //handler to handle the message to the timer task
    private final Handler mHandler = new Handler();


    Uri filePath;
    private Intent serviceIntent;

    private RelativeLayout yt_video_layout, reletiveVideoView;

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerSupportFragment youtubeFragment;
    private YouTubePlayerTracker tracker;
    private FullScreenHelper fullScreenHelper;
    private boolean isFullScreen;
    ImageView imageViewCrossVideo2;
    private boolean isloading=true;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    String system_millis;
    ProgressBar progressBar;
    final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    mProgressBar.setVisibility(View.GONE);
                    slow_internet_Text.setVisibility(View.GONE);
                    isloading=false;
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    mProgressBar.setVisibility(View.VISIBLE);
                    isloading=true;
                    checkConnection(false);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    mProgressBar.setVisibility(View.GONE);
                    slow_internet_Text.setVisibility(View.GONE);
                    isloading=false;
                    return true;
                }
            }
            return false;
        }
    };

    public static VideoView_Activity videoView_activity;

    public VideoView_Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        videoView_activity=this;
        system_millis= String.valueOf(System.currentTimeMillis());
        startTime=System.currentTimeMillis();
        if(mStartTime==0L){
            mStartTime=SystemClock.uptimeMillis();
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask,100);
        }

//        if(startTime==0L){
//            startTime=SystemClock.uptimeMillis();
//        }

        assignIds(savedInstanceState);
        hideNavigationBar(getWindow());


    }

    private void assignIds(Bundle savedInstanceState) {
        context = this;
        global = (Global) context.getApplicationContext();
        board = Util.getSelectedBoard(context);
        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);
        language = Util.getSelectedLanguage(context);

        progressBar=findViewById(R.id.progressBar);
        slow_internet_Text=findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=findViewById(R.id.imageViewCrossVideo2);

        imageViewCrossVideo = findViewById(R.id.imageViewCrossVideo);
        goto_smallscreen=false;
        Bundle bundle=getIntent().getExtras();

        currentvideo_url = bundle.getString("url");
        topicID = bundle.getString("topicID");
        currentDate = bundle.getString("currentDate");
        videoName = bundle.getString("videoName");
        offlineLink = bundle.getString("offlineLink");
        topicName = bundle.getString("topicName");
        videoID_ForReports = bundle.getString("videoid_for_reports");
        isFullScreen = bundle.getBoolean("isFullScreen");
        isLocalFile = bundle.getBoolean("isLocalFile");
        type = bundle.getString("type");
        innerplayedDuration=PalContentListingActivity.playedDuration;


        from = bundle.getString("from");

        if(from==null) from="";

        boolean from_smallScreen = bundle.getBoolean("from_smallScreen");

        System.out.println("______ previous  "+innerplayedDuration);

        if(!from_smallScreen) {
            startTime=iPrepVideoPlayerActivity.startTime;
        }

        palContentListingActivity=PalContentListingActivity.palContentListingActivity;

        if (savedInstanceState != null) {
            time = savedInstanceState.getInt("time");
        } else {
            time = 0;
        }

        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);
        linearLayoutBottom = findViewById(R.id.linearLayoutBottom);
        linearLayoutBottom.setVisibility(View.VISIBLE);
        videoView = findViewById(R.id.videoView);
        reletive = findViewById(R.id.reletive);
        imageViewCross = findViewById(R.id.imageViewCross);
        mProgressBar = findViewById(R.id.progressBar);

        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseVideoRepository = new ReportsDateWiseVideoRepository(context);
        reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);
        videoDetailsRepository = new VideoDetailsRepository(context);


        yt_video_layout = findViewById(R.id.yt_video_layout);
        reletiveVideoView = findViewById(R.id.reletiveVideoView);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        tracker = new YouTubePlayerTracker();
        fullScreenHelper = new FullScreenHelper(VideoView_Activity.this);
        isFullScreen = bundle.getBoolean("isFullScreen");


        textViewPractice = findViewById(R.id.textViewPractice);
        textViewPractice.setVisibility(View.GONE);
        textViewNextVideo = findViewById(R.id.textViewNextVideo);
        textViewNextVideo.setVisibility(View.GONE);

        findViewById(R.id.textViewNextVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        });

        findViewById(R.id.textViewPractice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type != null && type.equals("foundationalTopicVideos")){
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ((ExtraContentListingActivity) context).getPracticeContent();
                }else{
                    palContentListingActivity.removeFragment();
                    palContentListingActivity.startPractice();
//                    ((PalContentListingActivity) context).viewPager.setCurrentItem(1);
                }
            }
        });

        imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                current_duration= VideoView_Activity.videoView.getCurrentPosition();
                int duration2=VideoView_Activity.videoView.getCurrentPosition();
                goto_smallscreen=true;
                Util.setIsFullScreen(context,false);

                VideoView_Activity.videoView.pause();
                iPrepVideoPlayerActivity.videoView.setVideoURI(Uri.parse(String.valueOf(VideoView_Activity.video)));
                iPrepVideoPlayerActivity.videoView.seekTo(duration2);
                iPrepVideoPlayerActivity.videoView.start();
                onBackPressed();
                PalContentListingActivity.backToNormalView();
            }
        });

        imageViewCrossVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_smallscreen=true;
                Util.preventTwoClick(v);

                onBackPressed();

            }
        });

        linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewCrossVideo.performClick();
            }
        });

        videoReportRepository = new VideoReportRepository(getApplicationContext());



        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(onInfoToPlayStateListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        isloading=true;
        checkConnection(false);


        if(Util.isOfflineMode(context)) {
            playVideofromSDCard(offlineLink);
        }
        else {
            try {
                getVideoCount();
                getTime();
                if(type != null && type.equals("diksha_content") && !currentvideo_url.contains("https://vimeo.com/")){
                    playVideo(currentvideo_url,false,time);
                }else{
                    if(currentvideo_url != null){
                        if(isLocalFile){
                            playVideo(currentvideo_url,true,time);
                        }else{
                            if(currentvideo_url.contains("https://vimeo.com/")) {
                                initializePlayer(currentvideo_url.replace("https://vimeo.com/",""), time);
                            }
                            else {

                                try {
                                    int id = Integer.parseInt(currentvideo_url);
                                    initializePlayer(currentvideo_url, time);
                                }
                                catch (Exception e) {

                                    playVideo(currentvideo_url,true,time);

                                }


                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void checkConnection(boolean first) {
        if(!Util.isOfflineMode(context)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load video\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);
//                            imageViewCrossVideo2.setVisibility(View.GONE);
                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
//                        imageViewCrossVideo2.setVisibility(View.GONE);
                    }
                }
            }, 10000);//time in milisecond
        }
        else
        {
            mProgressBar.setVisibility(View.GONE);
            slow_internet_Text.setVisibility(View.GONE);
//            imageViewCrossVideo2.setVisibility(View.GONE);
        }
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    private int totalTime;

    boolean videoisLoaded=false;
    boolean isActive=false;
    String from;

    private void saveData() {

        if(from.equals("practice")) return;
//        if(type.equals("level_practice_videos")) return;
        if(!videoisLoaded) return;

        System.out.println( "____ type "+type);

        if(type.equals("foundation")||type.equals("foundationalTopicVideos")) return;

        if(youtube_view_visible)
        {
            String ddfd=totalTimee+"000";
            totalTime= Integer.parseInt(ddfd);
        }
        System.out.println( "____ saving ");
        time = videoView.getCurrentPosition();

        endTime = System.currentTimeMillis();
//        long timeTosync = endTime - startTime;

        innerplayedDuration=Integer.parseInt(innerplayedDuration+"000");
        System.out.println("____ innerplayedDuration "+innerplayedDuration);

        long timeTosync = innerplayedDuration;
        long ttime=timeTosync;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;

        System.out.println("____ timeTosync "+timeTosync);

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(innerplayedDuration + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate("" + date);
        scoreModel.setSubjectName(subject);

        String category_name;

        System.out.println("========= type "+type);

        if(type.equals("diksha_content"))
        {
            category_name="Diksha Videos";
        }
        else
        {
            category_name="Video Lessons";
        }

        HashMap<String, String> row_usage = new HashMap<>();
        row_usage.put("category", type);
        row_usage.put("class", Util.getSelectedClass(context));
        row_usage.put("content_name", videoName);
        row_usage.put("topic_name", Util.getTopicNameAlt(context).trim());
        row_usage.put("district", Util.getDistrict(context));
        row_usage.put("schoolID", Util.getSchoolId(context));
        row_usage.put("schoolName", Util.getSchoolName(context));
        row_usage.put("projectId", Util.getProjectId(context));
        row_usage.put("state", Util.getSelectedState(context));
        row_usage.put("subject", Util.getSubject(context));
        row_usage.put("subject_name", Util.getSubjectName(context));
        row_usage.put("language", Util.getSelectedLanguage(context));
        row_usage.put("time_spent", innerplayedDuration + "");
        row_usage.put("topic", "" + topicID);
        row_usage.put("userId", Util.getUserId(context));
        row_usage.put("username", Util.getUsernameShowable(context));
        // row_usage.put("userIdFirebase", Util.getLoginUserId(context));
        row_usage.put("board",Util.getSelectedBoard(context));
        row_usage.put("category_name",category_name);
        row_usage.put("userType", "students");
        row_usage.put("app_id", Util.getAPPID(context));


        if(currentDate==null) {
            currentDate =  Util.getCurrentDate();
        }


        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + currentDate).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + currentDate).setValue(row_usage);

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(timeget + innerplayedDuration);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("video_lessons").setValue(videoNumber);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("video_lessons").child(Util.getSubjectName(context)).child(date).child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        if (type.equals("diksha_content")) {

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("diksha_content").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("diksha_content").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("video_lessons").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("video_lessons").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        }

        //For Time Spent
        if(reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, subject,Util.getSelectedLanguage(context))){
            reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, subject, innerplayedDuration,Util.getSelectedLanguage(context));
        }else{
            ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
            reportsTimeSpentModel.setUserId(Util.getUserId(context));
            reportsTimeSpentModel.setBoard(board);
            reportsTimeSpentModel.setSClass(sClass);
            reportsTimeSpentModel.setSubject(subject);
            reportsTimeSpentModel.setDate(date);
            reportsTimeSpentModel.setTime(innerplayedDuration);
            reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
            reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
        }

        //For Count
        if(reportsCountRepository.isDataExist(Util.getUserId(context), board, sClass, date, "video_lessons")){
            reportsCountRepository.updateField(Util.getUserId(context), board, sClass, date, "video_lessons", videoNumber);
        }else{
            ReportsCountModel reportsCountModel = new ReportsCountModel();
            reportsCountModel.setUserId(Util.getUserId(context));
            reportsCountModel.setBoard(board);
            reportsCountModel.setSClass(sClass);
            reportsCountModel.setDate(date);
            reportsCountModel.setType("video_lessons");
            reportsCountModel.setCount(videoNumber);
            reportsCountRepository.insertCountDetails(reportsCountModel);
        }

        //For Date Wise
        if(reportsDateWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "video_lessons", date, System.currentTimeMillis())){
            reportsDateWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "video_lessons",
                    date, System.currentTimeMillis(), innerplayedDuration + "", Util.getTopicNameAlt(context), totalTime + "", videoName);
        }else{
            ReportsDateWiseVideoModel reportsDateWiseVideoModel = new ReportsDateWiseVideoModel();
            reportsDateWiseVideoModel.setUserId(Util.getUserId(context));
            reportsDateWiseVideoModel.setBoard(board);
            reportsDateWiseVideoModel.setSClass(sClass);
            reportsDateWiseVideoModel.setSubject(subject);
            reportsDateWiseVideoModel.setDate(date);
            reportsDateWiseVideoModel.setTime(System.currentTimeMillis());
            reportsDateWiseVideoModel.setTopicName(Util.getTopicNameAlt(context));
            reportsDateWiseVideoModel.setType("video_lessons");
            reportsDateWiseVideoModel.setVideoName(videoName);
            reportsDateWiseVideoModel.setVTime(innerplayedDuration + "");
            reportsDateWiseVideoModel.setTotalTime(totalTime + "");
            reportsDateWiseVideoRepository.insertVideoDetails(reportsDateWiseVideoModel);
        }

        //For Topic Wise
        if(reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "video_lessons", topicID, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
            reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "video_lessons", topicID,
                    date, System.currentTimeMillis(), Util.getTopicNameAlt(context), innerplayedDuration + "", Util.getTopicNameAlt(context), totalTime + "", videoName,Util.getSelectedLanguage(context));
        }else{
            ReportsTopicWiseVideoModel reportsTopicWiseVideoModel = new ReportsTopicWiseVideoModel();
            reportsTopicWiseVideoModel.setUserId(Util.getUserId(context));
            reportsTopicWiseVideoModel.setBoard(board);
            reportsTopicWiseVideoModel.setSClass(sClass);
            reportsTopicWiseVideoModel.setSubject(subject);
            reportsTopicWiseVideoModel.setDate(date);
            reportsTopicWiseVideoModel.setName(Util.getTopicNameAlt(context));
            reportsTopicWiseVideoModel.setTime(System.currentTimeMillis());
            reportsTopicWiseVideoModel.setType("video_lessons");
            reportsTopicWiseVideoModel.setTopicId(topicID);
            reportsTopicWiseVideoModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTopicWiseVideoModel.setTotalTime(totalTime + "");
            reportsTopicWiseVideoModel.setVTime(innerplayedDuration + "");
            reportsTopicWiseVideoModel.setVideoName(videoName);
            reportsTopicWiseVideoModel.setLang(Util.getSelectedLanguage(context));
            reportsTopicWiseVideoModel.setSubjectName(Util.getSubjectName(context));
            reportsTopicWiseVideoRepository.insertVideoDetails(reportsTopicWiseVideoModel);
        }

        if(!videoDetailsRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicID, videoName,Util.getSelectedLanguage(context))){
            VideoModel videoModel = new VideoModel();
            videoModel.setUserId(Util.getUserId(context));
            videoModel.setBoard(board);
            videoModel.setSClass(sClass);
            videoModel.setSubject(subject);
            videoModel.setTopicId(topicID);
            videoModel.setVideoName(videoName);
            videoModel.setLang(Util.getSelectedLanguage(context));
            videoModel.setTime(""+innerplayedDuration);
            videoDetailsRepository.insertVideoDetails(videoModel);
        }
        else {
            if(innerplayedDuration!=0)
                videoDetailsRepository.updateField(Util.getUserId(context),board,sClass,subject,videoName,innerplayedDuration+"",Util.getSelectedLanguage(context));

        }

        if(!type.equals("level_practice_videos")) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                palContentListingActivity.getTopicSeenVideoListing(true);
            }, 3000);
        }

        if(currentvideo_url != null && Util.isOfflineMode(context) && isLocalFile){
            String filename = currentvideo_url.substring(currentvideo_url.lastIndexOf("/") + 1);
//            String name = filename.substring(0, filename.lastIndexOf("."));
            String name = videoID_ForReports;
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("video_lessons").child(topicID).child("detail").child(name).setValue(scoreModel);
            if(reportsLatestDataVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID,Util.getSelectedLanguage(context))){
                reportsLatestDataVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID, name, null, innerplayedDuration + "", Util.getTopicNameAlt(context),
                        totalTime + "", videoName,Util.getSelectedLanguage(context));
            }else{
                ReportsLatestDataVideoModel reportsLatestDataVideoModel = new ReportsLatestDataVideoModel();
                reportsLatestDataVideoModel.setUserId(Util.getUserId(context));
                reportsLatestDataVideoModel.setBoard(board);
                reportsLatestDataVideoModel.setSClass(sClass);
                reportsLatestDataVideoModel.setSubject(Util.getSubjectName(context));
                reportsLatestDataVideoModel.setType("video_lessons");
                reportsLatestDataVideoModel.setTopicId(topicID);
                reportsLatestDataVideoModel.setVId(name);
                reportsLatestDataVideoModel.setUrl(null);
                reportsLatestDataVideoModel.setVTime(innerplayedDuration + "");
                reportsLatestDataVideoModel.setTopicName(Util.getTopicNameAlt(context));
                reportsLatestDataVideoModel.setTotalTime(totalTime + "");
                reportsLatestDataVideoModel.setVideoName(videoName);
                reportsLatestDataVideoModel.setLang(Util.getSelectedLanguage(context));
                reportsLatestDataVideoRepository.insertVideoDetails(reportsLatestDataVideoModel);
            }
        }else{
            if(type != null ){
                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("video_lessons").child(topicID).child("detail").child(videoID_ForReports).setValue(scoreModel);
                if(reportsLatestDataVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID,Util.getSelectedLanguage(context))){
                    reportsLatestDataVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID, null, currentvideo_url, innerplayedDuration + "", Util.getTopicNameAlt(context),
                            totalTime + "", videoName,Util.getSelectedLanguage(context));
                }else{
                    ReportsLatestDataVideoModel reportsLatestDataVideoModel = new ReportsLatestDataVideoModel();
                    reportsLatestDataVideoModel.setUserId(Util.getUserId(context));
                    reportsLatestDataVideoModel.setBoard(board);
                    reportsLatestDataVideoModel.setSClass(sClass);
                    reportsLatestDataVideoModel.setSubject(Util.getSubjectName(context));
                    reportsLatestDataVideoModel.setType("video_lessons");
                    reportsLatestDataVideoModel.setTopicId(topicID);
                    reportsLatestDataVideoModel.setVId(null);
                    reportsLatestDataVideoModel.setUrl(currentvideo_url);
                    reportsLatestDataVideoModel.setVTime(innerplayedDuration + "");
                    reportsLatestDataVideoModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsLatestDataVideoModel.setTotalTime(totalTime + "");
                    reportsLatestDataVideoModel.setVideoName(videoName);
                    reportsLatestDataVideoModel.setLang(Util.getSelectedLanguage(context));
                    reportsLatestDataVideoRepository.insertVideoDetails(reportsLatestDataVideoModel);
                }
            }
        }

        saveAssignedReport();

        time = 0;
        totalTime = 0;
        timeget = 0;

    }

    private void saveAssignedReport() {
        String assignedData,assignedKey,batchID;

        ArrayList<HashMap<String,Object>> recentAssignment=UserActivities.getLocalAssignment();

        for(int i=0;i<=recentAssignment.size()-1;i++) {
            HashMap<String,Object> map=recentAssignment.get(i);
            if(map.get("type").equals("video")) {
                if(map.get("key").equals(videoID_ForReports)) {
                    assignedData = map.get("startDate").toString();
                    assignedKey = map.get("assignedKey").toString();
                    batchID=map.get("batchID").toString();

                    HashMap<String,String> report=new HashMap<>();
                    report.put("vid_TotalTime", String.valueOf(totalTime));
                    report.put("vid_WatchTime", String.valueOf(innerplayedDuration));
                    report.put("timestamp", Util.getCurrentDateWithDifferentFormat());
                    report.put("userName",Util.getUsername(context));
                    global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                            .child(assignedData).child(assignedKey).child("report").setValue(report);


                }
            }
        }

    }

    private void saveLatestDataOnly() {

        time = videoView.getCurrentPosition();

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(timeToSendTofirebase_ + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate(""+date);
        scoreModel.setSubjectName(subject);
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;


        if(type != null ){
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("video_lessons").child(topicID).child("detail").child(videoID_ForReports).setValue(scoreModel);
            if(reportsLatestDataVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID,Util.getSelectedLanguage(context))){
                reportsLatestDataVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubjectName(context), "video_lessons", topicID, null, currentvideo_url, innerplayedDuration + "", Util.getTopicNameAlt(context),
                        totalTime + "", videoName,Util.getSelectedLanguage(context));
            }else {
                ReportsLatestDataVideoModel reportsLatestDataVideoModel = new ReportsLatestDataVideoModel();
                reportsLatestDataVideoModel.setUserId(Util.getUserId(context));
                reportsLatestDataVideoModel.setBoard(board);
                reportsLatestDataVideoModel.setSClass(sClass);
                reportsLatestDataVideoModel.setSubject(Util.getSubjectName(context));
                reportsLatestDataVideoModel.setType("video_lessons");
                reportsLatestDataVideoModel.setTopicId(topicID);
                reportsLatestDataVideoModel.setVId(null);
                reportsLatestDataVideoModel.setUrl(currentvideo_url);
                reportsLatestDataVideoModel.setVTime(innerplayedDuration + "");
                reportsLatestDataVideoModel.setTopicName(Util.getTopicNameAlt(context));
                reportsLatestDataVideoModel.setTotalTime(totalTime + "");
                reportsLatestDataVideoModel.setVideoName(videoName);
                reportsLatestDataVideoModel.setLang(Util.getSelectedLanguage(context));
                reportsLatestDataVideoRepository.insertVideoDetails(reportsLatestDataVideoModel);
            }
        }

    }

    private long timeget;

    private void getTime() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubjectName(context), date, "video_lessons", "timeTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).addValueEventListener(new ValueEventListener() {
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

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubjectName(context), date, "video_lessons", "videoCountTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(subject.toLowerCase().replace(" ","_")).child("count").child(date).child("video_lessons").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            videoNumber = (long) snapshot.getValue();
                        } else {
                            videoNumber = 0;
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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String date, String testType, String type){
        if(type.equals("timeTask")){
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            timeTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsTimeSpentModel> list = (ArrayList<ReportsTimeSpentModel>) o;
                            if(list != null && list.size() > 0){
                                for(ReportsTimeSpentModel item: list){
                                    timeget = item.getTime();
                                }
                            }else{
                                timeget = 0;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            timeTaskDisposable.dispose();
                        }
                    });
        }else if(type.equals("videoCountTask")){
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            videoCountTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsCountModel> list = (ArrayList<ReportsCountModel>) o;
                            if(list != null && list.size() > 0){
                                for(ReportsCountModel item: list){
                                    videoNumber = item.getCount();
                                }
                            }else{
                                videoNumber = 0;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            videoCountTaskDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date, String testType, String type){
        if(type.equals("timeTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, subject,Util.getSelectedLanguage(context));
            });
        }else if(type.equals("videoCountTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsCountRepository.getDetail(userId, board, sClass, date, testType);
            });
        }
        return null;
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    ImageView imageViewBackButton2;

    AbstractYouTubePlayerListener abstractYouTubePlayerListener;

    private void initYouTubePlayerView(String url_) {

        System.out.println("------ url_ "+url_);


        video_view_visible=false;
        youtube_view_visible=true;
        imageViewBackButton2=findViewById(R.id.imageViewCrossVideo2);
        ActivityVideosFullScreenController mediacontroller = new ActivityVideosFullScreenController(context,filePath,true,videoView,VideoView_Activity.this,videoName,topicID);
        mediacontroller.setAnchorView(youTubePlayerView);
        yt_video_layout.setVisibility(View.VISIBLE);
        reletiveVideoView.setVisibility(View.GONE);
        setMargins(reletiveVideoView,0,0,150,0);
        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.setEnableAutomaticInitialization(false);
        if (isFullScreen) {
//            youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        }

        System.out.println( "-------------=== "+youTubePlayerView);

        abstractYouTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onApiChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onApiChange(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                try {
                    time = (int) tracker.getCurrentSecond();
                    PalContentListingActivity.duration=time;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(tracker.getVideoDuration()==time)
                {
                    System.out.println("------- video completed");
                    isVideoCompleted = true;
                }
                //                syncTotalTime(time);
            }

            @Override
            public void onError(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }

            @Override
            public void onPlaybackQualityChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
            }

            @Override
            public void onPlaybackRateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate);
            }

            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                if (!isFullScreen) {
                    //                    duration = 0;
                }
                showPracticeTimer();
                youTubePlayer.addListener(tracker);
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        url_,
                        duration
                );

                try {
                    time = (int) tracker.getCurrentSecond();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // addFullScreenListenerToPlayer();
            }

            @Override
            public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                switch (state) {
                    case UNSTARTED:

                        break;

                    case ENDED:


                        try {
                            time = (int) tracker.getCurrentSecond();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //                        getTotalTime();
                        getTime();
                        //                        syncTotalTime(time);

                        try {
                            PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
                            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
                            //                            saveData();
                            stopTimer();
                            PalContentListingActivity.current_duration=0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        showNextVideoDialog();
                        onVideoComplete();
                        //                        palContentListingActivity.removeFragment();
                        break;

                }

            }

            @Override
            public void onVideoDuration(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                try {
                    Log.i("Total duration :", duration + "");
                    totalTimee = (int) duration;
                    //syncTotalTime(timeTOsync);
                    int totalDuration = (int) duration;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVideoId(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, String videoId) {
                super.onVideoId(youTubePlayer, videoId);
            }

            @Override
            public void onVideoLoadedFraction(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float loadedFraction) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
            }

        };



        try {
            youTubePlayerView.initialize(abstractYouTubePlayerListener);
        } catch (Exception e) {

            e.printStackTrace();
        }


        imageViewBackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                onBackPressed();
            }
        });
        imageViewCrossVideo2.setVisibility(View.VISIBLE);

    }

    private void addFullScreenListenerToPlayer() {
//        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
//            @Override
//            public void onYouTubePlayerEnterFullScreen() {
//                Toast.makeText(context, "Can't Open youtube Videos in Full screen", Toast.LENGTH_SHORT).show();
////                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////                fullScreenHelper.enterFullScreen();
////                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
////                ((PalContentListingActivity)context).openYoutubeVideoFragment(fragment, url, videoName, tracker.getCurrentSecond());
//            }
//
//            @Override
//            public void onYouTubePlayerExitFullScreen() {
//                fullScreenHelper.exitFullScreen();
//            }
//        });
    }


    private void checkPermissions(String path) {
        int app_version = Build.VERSION.SDK_INT;
        if (app_version > Build.VERSION_CODES.LOLLIPOP_MR1
                && (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.REORDER_TASKS) != PackageManager.PERMISSION_GRANTED)) {
            // Opens a Dialog Requesting for Permissions
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.REORDER_TASKS,

                    },
                    0);
        } else {
            // check if first time registration is required
            playVideofromSDCard(path);

        }
    }

    private final int totalDuration = 3000;
    private boolean isVideoCompleted;

    MediaController mediaController;
    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            //String text = "" + minutes + ":" + String.format("%02d", seconds);
            timeToSendTofirebase = "" + minutes + ":" + String.format("%02d", seconds);
            timeToSendTofirebase_ = String.valueOf(millis);
            //mTimeLabel.setText("" + minutes + ":" + String.format("%02d", seconds));
            mHandler.postDelayed(this, 100);
        }
    };
    String path_;

    private void playVideofromSDCard(String path) {
        Uri filePath;

        System.out.println( "----- Util.getSDCardPath(context) "+Util.getSDCardPath(context));

        filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
        File file3 = new File(filePath.toString());
        if (file3.exists()) {
            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
        } else {
            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
            file3 = new File(filePath.toString());
            if (file3.exists()) {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
            }
            else {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaEE/" + offlineLink);
                file3 = new File(filePath.toString());
                if (file3.exists()) {
                    filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaEE/" + offlineLink);
                }
            }
        }

//        if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
//        }else
//        {
//            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
//        }
        getTime();


        serviceIntent = new Intent(context, SocketService.class);
        context.startService(serviceIntent);
        path_ = "http://localhost:7453" + filePath;

        File file = new File(filePath.toString());
        if (file.exists()) {
//            videoView.setVideoPath(path_);
            video_view_visible=true;
//            video = Uri.parse(path_);
//                          MediaController mediaController = new MediaController(context);

            Uri finalFilePath = filePath;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MediaController mediaController ;
                    mediaController = new IPrepFullScreenMediaController(context, finalFilePath, false, videoView, VideoView_Activity.this, videoName, topicID,videoQuality_data,isLocalFile,time,type);
                    mediaController.setAnchorView(videoView);

                    videoView.setMediaController(mediaController);
                    videoView.setVideoPath(path_);
                    videoView.seekTo(PalContentListingActivity.current_duration);
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

//                                            innerplayedDuration= 0;
//                                            VideoView_Activity.innerplayedDuration=0;

                            isloading=false;
                            totalTime = mp.getDuration();
                            timeTOsync = mp.getDuration();
                            getTime();
                            if(totalTime<=PalContentListingActivity.current_duration+2000)
                            {
                                videoView.seekTo(0);
                            }

                            iPrepVideoPlayerActivity.startTime=System.currentTimeMillis();
                            startTime=System.currentTimeMillis();
                            videoisLoaded=true;
                            showPracticeTimer();
                            videoView.seekTo(PalContentListingActivity.current_duration);
                            videoView.start();

                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {


                            try {
                                PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
                                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
//                                                saveData();
                                stopTimer();
                                PalContentListingActivity.current_duration=0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child("cbse").child("6").child("Math").child("topicID").child("video").setValue(totalTime);


//                                            showNextVideoDialog();

                            onVideoComplete();

                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setVisibility(View.GONE);
//                                            imageViewCrossVideo2.setVisibility(View.GONE);

                            // ((PalContentListingActivity) context).removeFragment();

                        }
                    });
                    videoView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            handler2.removeCallbacksAndMessages(null);

                            if (mediaController != null) {
                                try {
                                    linearLayoutBottom.setVisibility(View.VISIBLE);


                                    if(Util.getIsFullScreen(context))
                                    {
//                                        setMargins(reletiveVideoView,0,0,150,0);
                                    }

                                    if(!mediaController.isShowing())
                                    {
                                        mediaController.show();
                                    }

                                    handler2.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                                            linearLayoutBottom.setVisibility(View.GONE);
                                            mediaController.hide();
//                                            setMargins(reletiveVideoView,0,0,0,0);
                                        }
                                    }, 3000);


                                }
                                catch (Exception r)
                                {
                                    r.printStackTrace();
                                }
                            }
                            return true;
                        }
                    });
                    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {
                            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));

                            switch (what) {
                                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                    mProgressBar.setVisibility(View.GONE);
                                    slow_internet_Text.setVisibility(View.GONE);


                                    isloading=false;
                                    return true;
                                }
                                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    isloading=true;
                                    checkConnection(false);
                                    return true;
                                }
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                    mProgressBar.setVisibility(View.GONE);
                                    slow_internet_Text.setVisibility(View.GONE);
                                    isloading=false;
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    mediaController.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {

                            handler2.removeCallbacksAndMessages(null);

                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                                    linearLayoutBottom.setVisibility(View.GONE);
                                    mediaController.hide();
//                                    setMargins(reletiveVideoView,0,0,0,0);
                                }
                            }, 3000);

                            return false;
                        }
                    });
                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                            videoView.setVideoURI(video);
                            videoView.seekTo(PalContentListingActivity.current_duration);

                            return false;
                        }
                    });

                    PalContentListingActivity.keys = videoID_ForReports;
                    try {
                        QuizActivity.notifyVideoList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    scrollToCurrentLevel(context);
                }
            },2000);


        } else {
            try {
                ((PalContentListingActivity) context).removeFragment(true);
            } catch (Exception e) {
                try {
                    palContentListingActivity.removeFragment(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            Toast.makeText(context, "Video not available", Toast.LENGTH_SHORT).show();
            onVideoComplete();
        }
    }

    //    private void syncClassTime_() {
//
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//        String formattedDate = df.format(c);
//
//        String date = Util.getCurrentDateWithDifferentFormat();
//
//        Log.i("Updated Time : ", updatedTime + "");
//        long syncClassTime = updatedTime + totalTimee;
//        HashMap<String, String> hashMapToSync = new HashMap<>();
//        hashMapToSync.put("time", "" + syncClassTime);
//        hashMapToSync.put("name", videoName);
//        hashMapToSync.put("username", Util.getUsername(context));
//        hashMapToSync.put("userclass", Util.getSelectedClass(context));
//        hashMapToSync.put("watchedTime", System.currentTimeMillis() + "");
//        hashMapToSync.put("totalDuration", timeToSendTofirebase + "");
//        hashMapToSync.put("topicName", topicID + "");
//        hashMapToSync.put("topicNameSend", topicName + "");
//        hashMapToSync.put("date", formattedDate);
//        hashMapToSync.put("attempts", "10");
//        hashMapToSync.put("subjectName", subject);
//        hashMapToSync.put("totalMinutes", "2000 Mins");
//
//        Log.e("userid", Util.getUserId(context));
//
//        endTime = System.currentTimeMillis();
//        long timeTosync = endTime - startTime;
//        videoNumber = videoNumber + 1;
//        timeTosync = timeTosync + timeget;
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(subject).setValue(timeTosync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child("video").setValue(videoNumber);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child("video").child(subject).child(date).child("" + System.currentTimeMillis()).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(date).child(topicID).child("name").setValue(topicName);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(date).child(topicID).child("detail").child("" + System.currentTimeMillis()).setValue(hashMapToSync);
//
//        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child("History").child("ClassTime").child(Util.getSelectedClass(context)).child("videoLessons").child(Util.getSubject(context)).child(topicID).setValue(hashMapToSync);
//        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child("History").child("ClassTime").child(Util.getSelectedClass(context)).child("videoLessons").child("overall").child(topicID).setValue(hashMapToSync);
//
//
//        HashMap<String, String> hashmapForTimeAndCount = new HashMap<>();
//        hashmapForTimeAndCount.put("date", formattedDate);
//        hashmapForTimeAndCount.put("totalDuration", timeToSendTofirebase + "");
//        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child("History").child("ClassTime").child(Util.getSelectedClass(context)).child("videoLessons").child("overall").child(topicID).child("attempts").push().setValue(hashmapForTimeAndCount);
//        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child("History").child("ClassTime").child(Util.getSelectedClass(context)).child("videoLessons").child(Util.getSubject(context)).child(topicID).child("attempts").push().setValue(hashmapForTimeAndCount);
//
//
//
//
//    }
    Map<String, String> videoQuality_data;
    RequestQueue requestQueue;
    private void initializePlayer(String vid_id, int time) throws Exception {
        System.out.println("----- init player");

        Map<String, String> aa = new HashMap<>();
        String hdStream;

        requestQueue = Volley.newRequestQueue(context);

        String url = "https://learn.iprep.in/api/video?videoid=" + vid_id + "&origin=vimeo.com";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray hashMap = new JSONArray();
                            hashMap = response.getJSONArray("files");
                            for (int i = 0; i < hashMap.length(); i++) {
                                JSONObject ad = (JSONObject) hashMap.get(i);
                                aa.put(((JSONObject) hashMap.get(i)).get("rendition").toString(), ((JSONObject) hashMap.get(i)).get("link").toString());
                            }

                            String hdStream = aa.get("360p");

                            if (aa.containsKey(Util.getVideoQuality(context)))
                                hdStream = aa.get(Util.getVideoQuality(context));

                            if (hdStream == null) {
                                hdStream = ((JSONObject) hashMap.get(0)).get("link").toString();
                            }
                            playVideo(hdStream,isLocalFile,time);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(context, error + "", Toast.LENGTH_SHORT).show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }
    Handler handler2 = new Handler();
    public static Uri video;

    private void playVideo(String stream,boolean isLocalFile, int time) {
        try {
            mProgressBar.setVisibility(View.VISIBLE);
        }catch (Exception rr){rr.printStackTrace();}
        isloading=true;
        checkConnection(false);
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @SuppressLint({"NewApi", "ClickableViewAccessibility"})
                @Override
                public void run() {

                    try {

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("-------- stream "+stream);

                                if(stream.contains("youtu.be") || stream.contains("youtube.com"))
                                {
                                    initYouTubePlayerView(stream.replace("https://youtu.be/","").replace("https://www.youtube.com/watch?v=",""));
                                }
                                else
                                {
                                    video_view_visible=true;
                                    video = Uri.parse(stream);
//                          MediaController mediaController = new MediaController(context);

                                    MediaController mediaController ;
                                    mediaController = new IPrepFullScreenMediaController(context, video, false, videoView, VideoView_Activity.this, videoName, topicID,videoQuality_data,isLocalFile,time,type);
                                    mediaController.setAnchorView(videoView);

                                    videoView.setMediaController(mediaController);
                                    videoView.setVideoURI(video);
                                    videoView.seekTo(PalContentListingActivity.current_duration);
                                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {

//                                            innerplayedDuration= 0;
//                                            VideoView_Activity.innerplayedDuration=0;

                                            isloading=false;
                                            totalTime = mp.getDuration();
                                            timeTOsync = mp.getDuration();
                                            getTime();
                                            if(totalTime<=PalContentListingActivity.current_duration+2000)
                                            {
                                                videoView.seekTo(0);
                                            }

                                            iPrepVideoPlayerActivity.startTime=System.currentTimeMillis();
                                            startTime=System.currentTimeMillis();
                                            videoisLoaded=true;
                                            showPracticeTimer();
                                            videoView.start();

                                        }
                                    });
                                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {


                                            try {
                                                PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
                                                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
//                                                saveData();
                                                stopTimer();
                                                PalContentListingActivity.current_duration=0;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            //global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child("cbse").child("6").child("Math").child("topicID").child("video").setValue(totalTime);


//                                            showNextVideoDialog();

                                            onVideoComplete();

                                            mProgressBar.setVisibility(View.GONE);
                                            slow_internet_Text.setVisibility(View.GONE);
//                                            imageViewCrossVideo2.setVisibility(View.GONE);

                                            // ((PalContentListingActivity) context).removeFragment();

                                        }
                                    });
                                    videoView.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {


                                            handler2.removeCallbacksAndMessages(null);

                                            if (mediaController != null) {
                                                try {
                                                    linearLayoutBottom.setVisibility(View.VISIBLE);


                                                    if(Util.getIsFullScreen(context))
                                                    {
//                                                        setMargins(reletiveVideoView,0,0,250,0);
                                                    }

                                                    if(!mediaController.isShowing())
                                                    {
                                                        mediaController.show();
                                                    }

                                                    handler2.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                                            linearLayoutBottom.setVisibility(View.GONE);
                                                            mediaController.hide();
//                                                            setMargins(reletiveVideoView,0,0,0,0);
                                                        }
                                                    }, 3000);


                                                }
                                                catch (Exception r)
                                                {
                                                    r.printStackTrace();
                                                }
                                            }
                                            return true;
                                        }
                                    });
                                    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                        @Override
                                        public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {
                                            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));

                                            switch (what) {
                                                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    slow_internet_Text.setVisibility(View.GONE);


                                                    isloading=false;
                                                    return true;
                                                }
                                                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                                    mProgressBar.setVisibility(View.VISIBLE);
                                                    isloading=true;
                                                    checkConnection(false);
                                                    return true;
                                                }
                                                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    slow_internet_Text.setVisibility(View.GONE);
                                                    isloading=false;
                                                    return true;
                                                }
                                            }
                                            return false;
                                        }
                                    });
                                    mediaController.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {

                                            handler2.removeCallbacksAndMessages(null);

                                            handler2.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    linearLayoutBottom.setVisibility(View.GONE);
                                                    mediaController.hide();
//                                                    setMargins(reletiveVideoView,0,0,0,0);
                                                }
                                            }, 3000);

                                            return false;
                                        }
                                    });

                                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                        @Override
                                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                                            videoView.setVideoURI(video);
                                            videoView.seekTo(PalContentListingActivity.current_duration);

                                            return false;
                                        }
                                    });

                                    PalContentListingActivity.keys = videoID_ForReports;
                                    try {
                                        QuizActivity.notifyVideoList();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    scrollToCurrentLevel(context);
                                }
                            }
                        }, 2000 );//time in milisecond



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }
    boolean clickednext = false;
    PalContentListingActivity palContentListingActivity;

    private void onVideoComplete() {

        if(type.equals("level_practice_videos"))
        {
            if(!QuizActivity.playNextVideo(context)){
                goto_smallscreen=false;
                iPrepVideoPlayerActivity.mediaController.hide();
                Util.setIsFullScreen(context,false);
                onBackPressed();
            }

            return;
        }

        video_completed=true;
        ArrayList<HashMap<String, Object>> videoList;
        PalContentListingActivity.playedDuration=0;
        if(type!=null && (type.equals("video_lessons")||type.equals("diksha_content"))) {
            videoList=PalContentListingActivity.videoContentArrayList;
        }
        else if(type.equals("foundation")||type.equals("foundationalTopicVideos"))
        {
//            videoList=PalContentListingActivity.dikshaContentArrayList_;

            System.out.println("====== type "+type);
            if(ExtraContentListingActivity.videoKey != null){
                String[] videoKeySeparated = ExtraContentListingActivity.videoKey.split("-");
                int videoLevel = Integer.parseInt(videoKeySeparated[0]);
                int videoPosition = Integer.parseInt(videoKeySeparated[1]) + 1;
                ExtraContentListingActivity.videoKey = videoLevel + "-" + videoPosition;
//                ((ExtraContentListingActivity) context).removeFragment(true);
                ExtraContentListingActivity.openVideoView(ExtraContentListingActivity.videoKey);
            }

            return;
        }
        else
        {
            videoList=ExtraContentListingActivity.contentArrayList_;
        }
        System.out.println("====== type 2 "+type);

        if (videoList != null && PalContentListingActivity.videoKey != null) {
            String[] videoKeySeparated = PalContentListingActivity.videoKey.split("-");
            int videoLevel = Integer.parseInt(videoKeySeparated[0]);
            int videoPosition = Integer.parseInt(videoKeySeparated[1])+1;


            for (int i = 0; i < videoList.size(); i++) {
                if (videoList.get(i).containsKey(videoLevel + "-" + videoPosition)) {
                    /** Adding Delay before playing next video */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /** Play next video */
                            playNextVideo();
                        }
                    },1000);break;
                } else {
                    /** show dialog */
                    if (i == videoList.size() - 1) {
                        videoLevel = Integer.parseInt(videoKeySeparated[0]) + 1;
                        videoPosition = 0;
                        for (int j = 0; j < videoList.size(); j++) {
                            if (videoList.get(j).containsKey(videoLevel + "-" + videoPosition)) {
                                if(Util.getUnlockVideoLevel(context,topicID)<videoLevel)
                                    Util.setUnlockVideoLevel(context,topicID,videoLevel);
                                /** Play next video */
                                playNextVideo();

                                break;
                            } else {
                                if(j==videoList.size()-1)
                                {
                                    /** show no more level video available */
                                    PalContentListingActivity.keys="";
//                                    Util.openGifDialogue(context, fourteen);
                                    showNextVideoDialog();
////                                Toast.makeText(context, fourteen, Toast.LENGTH_LONG).show();
//                                    clickednext=true;
////                                dialog.dismiss();
//                                    remove_fragment=true;
//                                    PalContentListingActivity.keys="";
//                                    try {
////            keys="";
//                                        PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
//                                        PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    onBackPressed();
//                                    try {
//                                        ((PalContentListingActivity) context).removeFragment(true);
//                                    } catch (Exception e) {
//                                        try {
//                                            palContentListingActivity.removeFragment(true);
//                                        } catch (Exception ex) {
//                                            ex.printStackTrace();
//                                        }
//                                        e.printStackTrace();
//                                    }

                                }

                            }
                        }

                    }

                }
            }
        }

    }

    TextView start_practice_text,next_sec_text;

    int videopos=0;
    CountDownTimer CountDownTimer;

    private int innerplayedDuration=0;

    private void showPracticeTimer() {



        String htmlString="<u>Start Practice</u>";
        start_practice_text=findViewById(R.id.start_practice_text);
        next_sec_text=findViewById(R.id.next_sec_text);
        start_practice_text.setText(Html.fromHtml(htmlString));

        CountDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println("===== time left "+((videoView.getDuration()-videoView.getCurrentPosition())/1000));
                if(videoView.getDuration()-videoView.getCurrentPosition()<=5000)
                {
                    if(!type.equals("level_practice_videos")){
                        System.out.println("========== ended in "+((videoView.getDuration()-videoView.getCurrentPosition())/1000));
                        next_sec_text.setText("Next video in "+((videoView.getDuration()-videoView.getCurrentPosition())/1000)+"s..  or ");
                        next_sec_text.setVisibility(View.VISIBLE);
                        start_practice_text.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    next_sec_text.setVisibility(View.GONE);
                    start_practice_text.setVisibility(View.GONE);
                }

                if((videoView.isPlaying() || youtube_view_visible) && isActive) innerplayedDuration++;

            }

            public void onFinish() {
                if(isActive) showPracticeTimer();
            }

        }.start();

        start_practice_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                videopos=videoView.getCurrentPosition();
                ((PalContentListingActivity) PalContentListingActivity.context).checkAndStartPractice();
            }
        });

    }

    private void playNextVideo() {

        Util.setContext(context);

        if(Util.checkInternetConnection(context) || Util.isOfflineMode(context))
        {
            mProgressBar.setVisibility(View.VISIBLE);
            isloading=true;
            video_view_visible=false;
            checkConnection(false);
            if(type != null && type.equals("foundationalTopicVideos")){
                if(ExtraContentListingActivity.videoKey != null){
                    String[] videoKeySeparated = ExtraContentListingActivity.videoKey.split("-");
                    int videoLevel = parseInt(videoKeySeparated[0]);
                    int videoPosition = parseInt(videoKeySeparated[1]) + 1;
                    ExtraContentListingActivity.videoKey = videoLevel + "-" + videoPosition;
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ExtraContentListingActivity.openVideoView(ExtraContentListingActivity.videoKey);
                }
            }else{
                if(videoKey != null){
                    String[] videoKeySeparated = videoKey.split("-");
                    int videoLevel = parseInt(videoKeySeparated[0]);
                    int videoPosition = parseInt(videoKeySeparated[1]) + 1;
                    videoKey = videoLevel + "-" + videoPosition;
//                    ((PalContentListingActivity) context).removeFragment();
                    palContentListingActivity.openVideoView(videoKey,type);
                }
            }
        }

        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
//        imageViewCrossVideo2.setVisibility(View.GONE);
    }

    private void showNextVideoDialog() {
        clickednext=false;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_autoplay);
        dialog.setCancelable(false);
        /** Hook up the UI element in the Dialog */
        TextView textViewClose = dialog.findViewById(R.id.textViewClose);
        TextView understandMasteryHeading = dialog.findViewById(R.id.understandMasteryHeading);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        TextView textViewPractice = dialog.findViewById(R.id.textViewPractice);
        TextView understandMasteryText = dialog.findViewById(R.id.understandMasteryText);

        String nextVideo,practiceText,closeText, secondsRemainingText, nextVideoPlayingText,videoCompletedText;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            nextVideo = "अगला वीडियो";
            practiceText="अभ्यास करें";
            closeText="बंद करें";
            secondsRemainingText ="सेकंड शेष: ";
            nextVideoPlayingText ="अगला वीडियो चला रहे हैं";
            videoCompletedText ="वीडियो पूरा हुआ";
        }else {
            nextVideo = "Next Video";
            practiceText="Practice";
            closeText="Close";
            secondsRemainingText ="Second remaining: ";
            nextVideoPlayingText ="Playing next Video";
            videoCompletedText ="Video Completed";
        }


        textViewOkay.setText(nextVideo);
        textViewPractice.setText(practiceText);
        textViewClose.setText(closeText);
        understandMasteryHeading.setText(videoCompletedText);

        String foundationalText;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            foundationalText = "कृपया पहले मुलभुत अध्याय का परिक्षण करें";
        }else {
            foundationalText = "Please do foundational practice first";
        }

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                understandMasteryText.setText(secondsRemainingText + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(!clickednext)
                {
                    understandMasteryText.setText(nextVideoPlayingText);
                    playNextVideo();
                    dialog.dismiss();
                }
            }

        }.start();

        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
//        imageViewCrossVideo2.setVisibility(View.GONE);

        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickednext=true;

//                textViewClose.performLongClick();

                try {
                    if (palContentListingActivity.completeType.equalsIgnoreCase("FoundationalPractice"))
                    {
                        // Util.showToast(context, foundationalText);
                        Util.openGifDialogue(context, foundationalText);
                    }else {
                        if(type != null && type.equals("foundationalTopicVideos")){
                            ((ExtraContentListingActivity) context).removeFragment(true);
                            ((ExtraContentListingActivity) context).getPracticeContent();
                        }else{

                            try {
                                ((PalContentListingActivity) context).removeFragment(true);
                            } catch (Exception e) {
                                try {
                                    palContentListingActivity.removeFragment(true);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                e.printStackTrace();
                            }

                            try {
                                iPrepVideoPlayerActivity.mediaController.hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            gotoPractice=true;
                            palContentListingActivity.removeFragment();
                            palContentListingActivity.startPractice();
                        }
                    }
                }
                catch (Exception r)
                {
                    Util.openGifDialogue(context, foundationalText);
                    r.printStackTrace();
                }

                dialog.dismiss();

            }
        });
        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext=true;
                dialog.dismiss();
                remove_fragment=true;
                PalContentListingActivity.keys="";
                try {
//            keys="";
                    PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                    PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                onBackPressed();
                try {
                    ((PalContentListingActivity) context).removeFragment(true);
                } catch (Exception e) {
                    try {
                        palContentListingActivity.removeFragment(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
//                ((PalContentListingActivity) context).removeFragment();

            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                iPrepVideoPlayerActivity.startTime=System.currentTimeMillis();
                startTime=System.currentTimeMillis();
                clickednext =true;
                remove_fragment=false;
                dialog.dismiss();
                playNextVideo();

            }
        });
        /** show the dialog */
        dialog.show();
        /** Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    public void stopTimer() {
        updatedTime = 0L;
        startTime = 0L;
        timeSwapBuff = 0L;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private String videoName;
    private String offlineLink;
    private String topicName,subjectName,videoid_for_reports;
    private long endTime = 0L;
//    private long timeget;


    //    private void getTime() {
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("totalTime").child(Util.getUserId(context)).child("totaltimevideos").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    if (snapshot.getValue() != null) {
//                        if (snapshot.getValue() != null) {
//                            timeget = (long) snapshot.getValue();
//                        } else {
//                            timeget = 0;
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
//    }
//
//
//    private long videoNumber;
    private String totalTimeSpent;

    private final int videoPosition = 0;
    private final int videoCount = 0;

    public static boolean goto_smallscreen=false;

    @Override
    public void onDestroy() {

        try {
            CountDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            timeforVideo = videoView.getCurrentPosition();
            if(!goto_smallscreen) {
                PalContentListingActivity.keys="";
//                innerplayedDuration=PalContentListingActivity.playedDuration;
                saveData();
            }
            else {
                /** clear selected video & update videolist  */
//                PalContentListingActivity.keys="";
                scrollToCurrentLevel(context);
            }
            videoView.pause();
            videoView.clearFocus();
            videoView.suspend();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            videoView.clearFocus();
            videoView.stopPlayback();
            videoView.suspend();
        }catch (Exception r){r.printStackTrace();}
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive=false;
        mHandler.removeCallbacks(mUpdateTimeTask);

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoClosedTiming = videoView.getCurrentPosition();
        if(!video_completed) PalContentListingActivity.playedDuration=innerplayedDuration;

//        PalContentListingActivity.playedDuration=0;
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive=true;
        try {
//            videoView.seekTo(videopos);
//            videoView.pause();
            videoView.seekTo(videoClosedTiming);
            videoView.start();
            innerplayedDuration=PalContentListingActivity.playedDuration;

        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.postDelayed(mUpdateTimeTask, 100);

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            timeforVideo = videoView.getCurrentPosition();
            videoView.pause();
//            context.stopService(serviceIntent);
            // syncTotalTime(timeforVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean video_view_visible=true;
    boolean youtube_view_visible=false;
    boolean video_completed=false;
    boolean gotoPractice=false;

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(type.equals("level_practice_videos"))
        {
            if(goto_smallscreen)
            {
                current_duration= VideoView_Activity.videoView.getCurrentPosition();
                int duration2=VideoView_Activity.videoView.getCurrentPosition();

                Util.setIsFullScreen(context,false);
                QuizActivity.gotoSmallScreen(context,VideoView_Activity.videoView.getCurrentPosition());

            }
        }
        else
        {
            if(!gotoPractice)
            {
                current_duration= VideoView_Activity.videoView.getCurrentPosition();
                int duration2=VideoView_Activity.videoView.getCurrentPosition();

                try {
                    iPrepVideoPlayerActivity.startTime=startTime;
                    iPrepVideoPlayerActivity.videoID_ForReports=videoID_ForReports;
                    iPrepVideoPlayerActivity.videoName=videoName;
                }catch (Exception r){
                    r.printStackTrace();
                }

                Util.setIsFullScreen(context,false);


                if(video_view_visible)
                {
                    try {
                        if(!video_completed)
                        {
                            PalContentListingActivity.duration=videoView.getDuration();
                            PalContentListingActivity.duration=videoView.getDuration();
                            VideoView_Activity.videoView.pause();
                            if(Util.isOfflineMode(context)) iPrepVideoPlayerActivity.videoView.setVideoPath(path_);
                            else iPrepVideoPlayerActivity.videoView.setVideoURI(Uri.parse(String.valueOf(VideoView_Activity.video)));
                            iPrepVideoPlayerActivity.videoView.seekTo(duration2);
                            iPrepVideoPlayerActivity.startTime=startTime;
                            iPrepVideoPlayerActivity.videoID_ForReports=videoID_ForReports;
                            iPrepVideoPlayerActivity.videoName=videoName;
                            iPrepVideoPlayerActivity.videoView.start();
                        }

                    }catch (Exception r){}
                }
                else if(youtube_view_visible)
                {
                    PalContentListingActivity.duration=time;
                    PalContentListingActivity.current_duration=time;
                }

            }
            else
            {
                try {
                    iPrepVideoPlayerActivity.mediaController.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    
}