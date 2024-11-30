package com.idreameducation.ipreppal.videoPlayer;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.backPressed;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.current_duration;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.currentvideo_url;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.palContentListingActivity;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.scrollToCurrentLevel;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.videoListAdapter;
import static com.idreameducation.ipreppal.videoPlayer.IPrepFullScreenMediaController.speed;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.fragments.PalDikshaContentFragment;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class iPrepVideoPlayerActivity extends Fragment {

    public Context context;

    public static VideoView videoView;
    private ImageView imageViewCross;
    public ImageView imageViewCrossVideo;
    private RelativeLayout reletive;
    private boolean isFullScreenModeOn;
    private ProgressBar mProgressBar;
    private String type;
    private Global global;
    private String board;
    private View view;
    private String sClass;
    private String subject;
    private String topicID;
    public static String videoID_ForReports;
    private String icon;
    private String language;
    private int timeforVideo = 0;
    private int timeTOsync;
    public long updatedTime = 0L;

    public static long startTime = 0L;
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

    private YouTubePlayerSupportFragment youtubeFragment;
    private YouTubePlayerTracker tracker;
    private FullScreenHelper fullScreenHelper;
    private float duration=0;
    private boolean isFullScreen;
    ImageView imageViewCrossVideo2;
    private boolean isloading=true;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    private int totalDuration = 3000;
    private boolean isVideoCompleted;
    public static String videoName;
    private String offlineLink;
    private String topicName,subjectName,videoid_for_reports;
    private long endTime = 0L;
    private final int videoPosition = 0;
    private int videoCount = 0;
    boolean clickednext = false;
    private int totalTime;
    private long timeget;
    private long videoNumber;

    boolean video_view_visible=true;
    boolean youtube_view_visible=false;
    public static MediaController mediaController;
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

    public iPrepVideoPlayerActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_i_prep_video_player, container, false);
        assignIds(view, savedInstanceState);

        if (mStartTime == 0L) {
            mStartTime = SystemClock.uptimeMillis();
//            palContentListingActivity.videoStartTime=mStartTime;
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 100);
        }

        view.findViewById(R.id.textViewNextVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        });

        view.findViewById(R.id.textViewPractice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type != null && type.equals("foundationalTopicVideos")){
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ((ExtraContentListingActivity) context).getPracticeContent();
                } else{
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                    ((PalContentListingActivity) context).startPractice();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            timeforVideo = videoView.getCurrentPosition();
            videoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

        try {
            countDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mediaController.hide();
        }catch (Exception e){}

        try
        {
            if(mediaController!=null)
            {
                mediaController.removeAllViews();
                mediaController.hide();
            }

        }catch (Exception f){}

        if(video_view_visible)
        {
            try {
                PalContentListingActivity.duration=videoView.getDuration();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(youtube_view_visible)
        {
            try {
                PalContentListingActivity.duration=time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            timeforVideo = videoView.getCurrentPosition();
            videoView.pause();
            saveData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try
        {
            current_duration=0;
            videoView.clearFocus();
            videoView.stopPlayback();
            videoView.suspend();
//            videoView.seekTo(PalContentListingActivity.current_duration);

        }catch (Exception r){r.printStackTrace();}

        super.onDestroy();
    }

    @Override
    public void onPause() {
        isActive=false;
        try {
            mediaController.hide();
        }catch (Exception e){}

        try {
            if(iPrepVideoPlayerActivity.videoView.getCurrentPosition()!=0)
            {
                seektime=""+videoView.getCurrentPosition();
                current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(video_view_visible)
        {
            PalContentListingActivity.duration=videoView.getDuration();
            videoView.clearFocus();
            videoView.stopPlayback();
            videoView.suspend();
        }
        else if(youtube_view_visible)
        {
            try {
                PalContentListingActivity.duration=time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /** clear selected video & update videolist  */
//        PalContentListingActivity.keys="";
//        scrollToCurrentLevel(context);

        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onPause();
    }

    @Override
    public void onResume() {

        PalContentListingActivity.playedDuration=innerplayedDuration;
        isActive=true;
        try {
            mHandler.postDelayed(mUpdateTimeTask, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            videoView.seekTo(PalContentListingActivity.current_duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(backPressed)
        {
            System.out.println( "----- backpresed");
            if(!video_view_visible)
            {

                try {
                    duration=PalContentListingActivity.current_duration;
                    if (youTubePlayerView != null) {
//                        youTubePlayerView.release();
                        youTubePlayerView.invalidate();
                    }
                    initYouTubePlayerView(Yt_url);
                    System.out.println( "------ done ");
                }catch (Exception df)
                {
                    df.printStackTrace();
                    try
                    {
                        if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                        else ((PalContentListingActivity) context).removeFragment();
                    }catch (Exception ff){ff.printStackTrace();}
                }

            }
        }


        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(video_view_visible)
        {
            videoView.clearFocus();
            videoView.stopPlayback();
            videoView.suspend();
            PalContentListingActivity.duration=videoView.getDuration();
        }
        else if(youtube_view_visible)
        {
            PalContentListingActivity.duration=time;
        }

        try {
            timeforVideo = videoView.getCurrentPosition();
            videoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignIds(View view, Bundle savedInstanceState) {
        context = getActivity();
        global = (Global) context.getApplicationContext();
        board = Util.getSelectedBoard(context);
        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);
        language = Util.getSelectedLanguage(context);
        icon = PalContentListingActivity.icon;
        this.view=view;
        imageViewBackButton2=view.findViewById(R.id.imageViewCrossVideo3);
        fullscreen_btn=view.findViewById(R.id.fullscreen_btn);
        imageViewCrossVideo = view.findViewById(R.id.imageViewCrossVideo);
        currentvideo_url = getArguments().getString("url");
        topicID = getArguments().getString("topicID");
        videoName = getArguments().getString("videoName");
        offlineLink = getArguments().getString("offlineLink");
        topicName = getArguments().getString("topicName");
        videoID_ForReports = getArguments().getString("videoid_for_reports");
        isFullScreen = getArguments().getBoolean("isFullScreen");
        isLocalFile = getArguments().getBoolean("isLocalFile");
        type = getArguments().getString("type");

        start_practice_text=view.findViewById(R.id.start_practice_text);
        next_sec_text=view.findViewById(R.id.next_sec_text);

        start_practice_text.setVisibility(View.GONE);
        next_sec_text.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            time = savedInstanceState.getInt("time");
        } else {
            time = 0;
        }

        slow_internet_Text = view.findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = view.findViewById(R.id.imageViewCrossVideo2);
        linearLayoutBottom = view.findViewById(R.id.linearLayoutBottom);
        linearLayoutBottom.setVisibility(View.VISIBLE);
        videoView = view.findViewById(R.id.videoView);
//        reletive = view.findViewById(R.id.reletive);
        imageViewCross = view.findViewById(R.id.imageViewCross);
        mProgressBar = view.findViewById(R.id.progressBar);

        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseVideoRepository = new ReportsDateWiseVideoRepository(context);
        reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);
        videoDetailsRepository = new VideoDetailsRepository(context);


        yt_video_layout = view.findViewById(R.id.yt_video_layout);
        reletiveVideoView = view.findViewById(R.id.reletiveVideoView);
        tracker = new YouTubePlayerTracker();
        fullScreenHelper = new FullScreenHelper(getActivity());

        isFullScreen = getArguments().getBoolean("isFullScreen");


        textViewPractice = view.findViewById(R.id.textViewPractice);
        textViewPractice.setVisibility(View.GONE);
        textViewNextVideo = view.findViewById(R.id.textViewNextVideo);
        textViewNextVideo.setVisibility(View.GONE);

        imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                PalContentListingActivity.keys="";
                try {
//            keys="";
                    PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                else ((PalContentListingActivity) context).removeFragment();
                Util.setIsFullScreen(context,false);
            }
        });

        imageViewCrossVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);

                if(type.equals("level_practice_videos"))
                {
                    PalContentListingActivity.keys="";
                    QuizActivity.removeVideoLayout(context);
                    Util.setIsFullScreen(context,false);
                }
                else {
                    if(Util.isPortraitMode(context))
                    {
                        if(Util.getIsFullScreen(context))
                        {
                            PalTopicListingActivity.palTopicListingActivity.rotate_screen();
                            Util.setIsFullScreen(context,false);
                        }

                        PalContentListingActivity.keys="";
                        try {
                            PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
//                         PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        PalTopicListingActivity.palTopicListingActivity.removeFragment();
                        Util.setIsFullScreen(context,false);
                    }
                    else
                    {
                        PalContentListingActivity.keys="";
                        try {
                            videoListAdapter.notifyDataSetChanged();
//                         PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                        else ((PalContentListingActivity) context).removeFragment();
                        Util.setIsFullScreen(context,false);
                    }

                }


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
//            checkPermissions(offlineLink);
        }
        else
        {
            try {
                getVideoCount();
                getTime();

                if(type.equals("level_practice_videos"))
                {

                    String type="";

                    if(!currentvideo_url.contains("https://vimeo.com/")) {
                        type="diksha_content";
                    }

                    if(type != null && type.equals("diksha_content")){
                    playVideo(currentvideo_url,false,time, view);
                    }else{
                    if(currentvideo_url != null){
                        if(isLocalFile){
                            playVideo(currentvideo_url,true,time, view);
                        }else{
                            initializePlayer(currentvideo_url.replace("https://vimeo.com/",""), time, view);
                        }
                    }
                }
                }
                else
                {
                    if(type != null && type.equals("diksha_content")){
                        playVideo(currentvideo_url,false,time, view);
                    }else{
                        if(currentvideo_url != null){
                            if(isLocalFile){
                                playVideo(currentvideo_url,true,time, view);
                            }else{
                                initializePlayer(currentvideo_url.replace("https://vimeo.com/",""), time, view);
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
                            imageViewCrossVideo2.setVisibility(View.GONE);
                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                            imageViewCrossVideo2.setVisibility(View.GONE);
                        }

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
                        imageViewCrossVideo2.setVisibility(View.GONE);
                    }
                }
            }, 10000);//time in milisecond
        }
        else
        {
            mProgressBar.setVisibility(View.GONE);
            slow_internet_Text.setVisibility(View.GONE);
            imageViewCrossVideo2.setVisibility(View.GONE);
        }
    }

    String seektime="";
    boolean videoisLoaded=false;
    boolean isActive=false;

    private void saveData() {

        innerplayedDuration=PalContentListingActivity.playedDuration;
        PalContentListingActivity.playedDuration=0;

        if(!videoisLoaded) return;

        time = videoView.getCurrentPosition();

        endTime = System.currentTimeMillis();

        String ddf=innerplayedDuration+"000";

        innerplayedDuration=Integer.parseInt(ddf);

        if(youtube_view_visible) {
            String ddfd=totalTimee+"000";
            totalTime= Integer.parseInt(ddfd);
        }

        long timeTosync = innerplayedDuration;

        UserActivities.updateSubjectTime(Util.getSubject(context),String.valueOf(timeTosync));

        long ttime=timeTosync;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;
        System.out.println("__________ timeTosync 2 "+timeTosync);


        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(innerplayedDuration + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setSeektime(seektime);
        scoreModel.setDate("" + date);
        scoreModel.setSubjectName(subject);

        String category_name;

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
        row_usage.put("content_name", videoName.trim());
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
        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(timeget + innerplayedDuration);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("video_lessons").setValue(videoNumber);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("video_lessons").child(Util.getSubjectName(context)).child(date).child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        if (type.equals("diksha_content")) {

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("diksha_content").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("diksha_content").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        }
        else
        {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("video_lessons").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).child("video_lessons").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        }


        //For Time Spent
        if(reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, subject,Util.getSelectedLanguage(context))){
            reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, subject, timeTosync,Util.getSelectedLanguage(context));
        }else{
            ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
            reportsTimeSpentModel.setUserId(Util.getUserId(context));
            reportsTimeSpentModel.setBoard(board);
            reportsTimeSpentModel.setSClass(sClass);
            reportsTimeSpentModel.setSubject(subject);
            reportsTimeSpentModel.setDate(date);
            reportsTimeSpentModel.setTime(timeTosync);
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
            reportsTopicWiseVideoModel.setVideoId(videoID_ForReports);
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
            videoModel.setTime(""+current_duration);
            videoDetailsRepository.insertVideoDetails(videoModel);
        }
        else {
            if(current_duration!=0)
            {
                videoDetailsRepository.updateField(Util.getUserId(context),board,sClass,subject,videoName,current_duration+"",Util.getSelectedLanguage(context));
            }
        }

        if(!type.equals("level_practice_videos"))
        {
            final Handler handler = new Handler();
            try {
                handler.postDelayed(() -> {
                    if(Util.isPortraitMode(context)) PalContentListingActivity_Mobile.palContentListingActivityMobile.getTopicSeenVideoListing(true);
                    else ((PalContentListingActivity)context).getTopicSeenVideoListing(true);
                }, 3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(currentvideo_url != null && Util.isOfflineMode(context) && isLocalFile) {
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
        } else {
            if(type != null && type.equals("video_lessons")){
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
            else if(type != null && type.equals("diksha_content")){
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
        scoreModel.setTime(innerplayedDuration + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate(""+date);
        scoreModel.setSubjectName(subject);
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;


        if(type != null && type.equals("video_lessons")){
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
        else if(type != null && type.equals("diksha_content")){
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

    private void getTime() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, subject, date, "video_lessons", "timeTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue() != null) {
                                timeget = (long) snapshot.getValue();
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

    ImageView imageViewBackButton2,fullscreen_btn;

    private static String Yt_url;
    YouTubePlayerView youTubePlayerView;

    @SuppressLint("ClickableViewAccessibility")
    public void initYouTubePlayerView(String url_) {

        System.out.println("----- url_ " + url_);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        Yt_url = url_;
        video_view_visible = false;
        youtube_view_visible = true;
//        ActivityVideosFullScreenController mediacontroller = new ActivityVideosFullScreenController(context,filePath,true,videoView,getActivity(),videoName,topicID);
//        mediacontroller.setAnchorView(youTubePlayerView);
        yt_video_layout.setVisibility(View.VISIBLE);
        reletiveVideoView.setVisibility(View.GONE);
//        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setScrollBarFadeDuration(PalContentListingActivity.duration);
//        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.setEnableAutomaticInitialization(false);
        if (isFullScreen) {
//            youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        }
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {
                super.onApiChange(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                try {
                    time = (int) tracker.getCurrentSecond();
                    PalContentListingActivity.duration = time;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("---- duration " + duration);
                System.out.println("---- time " + time);
                if (tracker.getVideoDuration() == time) {
                    System.out.println("------- video completed");
                    isVideoCompleted = true;
                }
            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate);
            }

            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                if (!isFullScreen) {
                    duration = 0;
                }
                videoisLoaded=true;
                duration = PalContentListingActivity.duration;

                youTubePlayer.addListener(tracker);
                YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), url_, duration
                );
                showPracticeTimer();
                try {
                    time = (int) tracker.getCurrentSecond();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // addFullScreenListenerToPlayer();
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
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
                            innerplayedDuration=PalContentListingActivity.playedDuration;
                            saveData();
                            stopTimer();
                            PalContentListingActivity.current_duration = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        onVideoComplete();


                        if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                        else ((PalContentListingActivity) context).removeFragment();
                        break;

                }

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                try {
                    Log.i("Total duration :", duration + "");
                    totalTimee = (int) duration;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String videoId) {
                super.onVideoId(youTubePlayer, videoId);
            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float loadedFraction) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
            }
        });

        imageViewBackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                PalContentListingActivity.keys="";
                if(type.equals("level_practice_videos"))
                {
                    QuizActivity.removeVideoLayout(context);
                }
                else
                {
                    try {
                        videoListAdapter.notifyDataSetChanged();
//                         PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                }

                Util.setIsFullScreen(context, false);
            }
        });

        fullscreen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(type.equals("level_practice_videos"))
                    {
                        QuizActivity.gotoFullScreen(context);
                    }
                    else
                    {
                        PalContentListingActivity.openVimeoVideoFullScreenFragment();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


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
            playVideofromSDCard(path);

        }
    }

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timeToSendTofirebase = "" + minutes + ":" + String.format("%02d", seconds);
            timeToSendTofirebase_ = String.valueOf(millis);
            mHandler.postDelayed(this, 100);
        }
    };

    private void playVideofromSDCard(String path) {
        Uri filePath;

        System.out.println( "----- Util.getSDCardPath(context) "+Util.getSDCardPath(context));

        if (Util.getSelectedLanguagePackage(context).equalsIgnoreCase("English")) {
            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
            File file = new File(filePath.toString());
            if (file.exists()) {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
            } else {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
            }
        } else {
            filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
            File file = new File(filePath.toString());
            if (file.exists()) {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink);
            } else {
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/multimediaE/" + offlineLink);
            }
        }

        getTime();

        String path_;
        serviceIntent = new Intent(context, SocketService.class);
        context.startService(serviceIntent);
//        context.stopService(serviceIntent);
        path_ = "http://localhost:7453" + filePath;

        File file = new File(filePath.toString());
        if (file.exists()) {
            videoView.setVideoPath(path_);
            if(type.equals("level_practice_videos")) mediaController = new IPrepFullScreenMediaController(context,type, filePath, false, videoView, getActivity(), videoName, topicID,videoQuality_data,isLocalFile,time);
            else mediaController = new IPrepFullScreenMediaController(context, filePath, false, videoView, getActivity(), videoName, topicID,videoQuality_data,isLocalFile,time,type);
//            MediaController mediaController = new IPrepFullScreenMediaController(context, filePath, false, videoView, getActivity(), videoName, topicID,videoQuality_data,isLocalFile,time);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();

            getCurrentPosition(videoView);

            videoCount = (int) (videoNumber + 1);

            totalDuration = videoView.getDuration();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    videoView.setOnInfoListener(onInfoToPlayStateListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });

//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    isVideoCompleted = true;
//
//                    try {
//                        saveLatestDataOnly();
//                        stopTimer();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });


            //

            videoView.seekTo(PalContentListingActivity.duration);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isloading=false;
                    totalTime = mp.getDuration();
                    timeTOsync = mp.getDuration();
                    getTime();
                    videoisLoaded=true;
                    if(totalTime<=PalContentListingActivity.duration+2000)
                    {
                        videoView.seekTo(0);
                    }

                    videoView.start();
                    showPracticeTimer();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isVideoCompleted = true;
                    try {
                        PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
                        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
//                                        saveData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try
                    {
                        stopTimer();
                        PalContentListingActivity.duration=0;
                        PalContentListingActivity.current_duration=0;
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    mProgressBar.setVisibility(View.GONE);
                    slow_internet_Text.setVisibility(View.GONE);
                    imageViewCrossVideo2.setVisibility(View.GONE);

//                                    showNextVideoDialog();
                    onVideoComplete();
                }
            });

            Handler handler2=new Handler();

            mediaController.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    view.setEnabled(false);
//                    view.postDelayed(()-> view.setEnabled(true), 1000);
                    handler2.removeCallbacksAndMessages(null);

                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                                            linearLayoutBottom.setVisibility(View.GONE);
                            mediaController.hide();
                            setMargins(reletiveVideoView,0,0,0,0);
                        }
                    }, 3000);

                    return false;
                }
            });
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    view.setEnabled(false);
//                    view.postDelayed(()-> view.setEnabled(true), 1000);
                    handler2.removeCallbacksAndMessages(null);
                    if (mediaController != null) {
//                                        linearLayoutBottom.setVisibility(View.VISIBLE);
                        mediaController.show();
                        if(!Util.isPortraitMode(context) &&  Util.getIsFullScreen(context) && !type.equals("level_practice_videos"))
                        {
//                            setMargins(reletiveVideoView,0,0,0,60);
                        }

                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                                linearLayoutBottom.setVisibility(View.GONE);
                                mediaController.hide();
                                setMargins(reletiveVideoView,0,0,0,0);
                                handler2.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                    }
                    return true;
                }
            });

            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        System.out.println("---- mediaPlayer.getPlaybackParams() "+mediaPlayer.getPlaybackParams().getSpeed());

                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                    }

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
                            imageViewCrossVideo2.setVisibility(View.GONE);
                            isloading=false;
                            return true;
                        }
                    }
                    return false;
                }
            });

//                            iPrepVideoPlayerActivity.innerplayedDuration= 0;
//                            VideoView_Activity.innerplayedDuration=0;


            PalContentListingActivity.keys = videoID_ForReports;
            try {
                QuizActivity.notifyVideoList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scrollToCurrentLevel(context);

        } else {
            try {
                PalContentListingActivity.keys = "";
                ((PalContentListingActivity) context).removeFragment(true);
                videoListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                try {
                    PalContentListingActivity.keys = "";
                    palContentListingActivity.removeFragment(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

            Toast.makeText(context, "Video not available", Toast.LENGTH_LONG).show();
//            onVideoComplete();
//            playNextVideo();
        }
    }

    private void getCurrentPosition(VideoView videoView) {

        int initialtime = videoView.getCurrentPosition();
        int finalPosition = videoView.getDuration();

        if (finalPosition-initialtime==5000){
            Toast.makeText(context, "Practice started", Toast.LENGTH_SHORT).show();
            System.out.println("Time Remaing"+(finalPosition-initialtime));
        }

    }

    Map<String, String> videoQuality_data;
    Handler handler2=new Handler();
    RequestQueue requestQueue;
    private void initializePlayer(String vid_id, int time,View view) throws Exception {

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

                            playVideo(hdStream,isLocalFile,time,view);

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
        requestQueue.add(request);

    }

    private void playVideo(final String stream,boolean isLocalFile, int time,View view) {

        try {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        catch (Exception ff){ff.printStackTrace();}
        isloading=true;
        checkConnection(false);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint({"NewApi", "ClickableViewAccessibility"})
                @Override
                public void run() {

                    try {


                        if(stream.contains("youtu.be") || stream.contains("youtube.com")) {
                            initYouTubePlayerView(stream.replace("https://youtu.be/","").replace("https://www.youtube.com/watch?v=",""));
                        }
                        else {
                            video_view_visible=true;
                            youtube_view_visible=false;
                            Uri video = Uri.parse(stream);

                            if(type.equals("level_practice_videos")) mediaController = new IPrepFullScreenMediaController(context,type, video, false, videoView, getActivity(), videoName, topicID,videoQuality_data,isLocalFile,time);
                            else mediaController = new IPrepFullScreenMediaController(context, video, false, videoView, getActivity(), videoName, topicID,videoQuality_data,isLocalFile,time,type);
//                            mediaController = new MediaController(context);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);
//                            mediaController.hide();
                            videoView.setVideoURI(video);
                            videoView.seekTo(PalContentListingActivity.duration);
                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    isloading=false;
                                    totalTime = mp.getDuration();
                                    timeTOsync = mp.getDuration();
                                    getTime();
                                    videoisLoaded=true;
                                    if(totalTime<=PalContentListingActivity.duration+2000)
                                    {
                                        videoView.seekTo(0);
                                    }

                                    videoView.start();
                                    showPracticeTimer();
                                }
                            });
                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {

                                    try {
                                        PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
                                        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
//                                        saveData();
                                        stopTimer();
                                        PalContentListingActivity.duration=0;
                                        PalContentListingActivity.current_duration=0;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    mProgressBar.setVisibility(View.GONE);
                                    slow_internet_Text.setVisibility(View.GONE);
                                    imageViewCrossVideo2.setVisibility(View.GONE);

//                                    showNextVideoDialog();
                                    onVideoComplete();
                                }
                            });

                             videoView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    view.setEnabled(false);
                                    view.postDelayed(()-> view.setEnabled(true), 1000);
                                    handler2=new Handler();
                                    handler2.removeCallbacksAndMessages(null);
                                    if (mediaController != null) {
//                                        linearLayoutBottom.setVisibility(View.VISIBLE);
                                        mediaController.show();
                                        if(!Util.isPortraitMode(context) &&  Util.getIsFullScreen(context) && !type.equals("level_practice_videos"))
                                            setMargins(reletiveVideoView,0,0,0,60);


                                        handler2.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
//                                                linearLayoutBottom.setVisibility(View.GONE);
                                                mediaController.hide();
                                                setMargins(reletiveVideoView,0,0,0,0);
                                                handler2.removeCallbacksAndMessages(null);
                                            }
                                        }, 3000);
                                    }
                                    return true;
                                }
                            });

                            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {

                                    System.out.println("---- mediaPlayer.getPlaybackParams() "+mediaPlayer.getPlaybackParams().getSpeed());

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
                                            imageViewCrossVideo2.setVisibility(View.GONE);
                                            isloading=false;
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                            });

//                            iPrepVideoPlayerActivity.innerplayedDuration= 0;
//                            VideoView_Activity.innerplayedDuration=0;


                            PalContentListingActivity.keys = videoID_ForReports;
                            try {
                                QuizActivity.notifyVideoList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            scrollToCurrentLevel(context);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }

    ArrayList<Integer> videosidList=new ArrayList<>();

    TextView start_practice_text,next_sec_text;

    int videopos=0;
    CountDownTimer countDownTimer;
    private int innerplayedDuration=0;

    private void showPracticeTimer() {

        String htmlString="<u>Start Practice</u>";
        int dd=PalContentListingActivity.playedDuration;
        start_practice_text.setText(Html.fromHtml(htmlString));

        next_sec_text.setVisibility(View.GONE);
        start_practice_text.setVisibility(View.GONE);

        countDownTimer = new CountDownTimer(10000, 1000) {

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

                if((videoView.isPlaying() || youtube_view_visible) && isActive) {
                    PalContentListingActivity.playedDuration++;
                    System.out.println("======== PalContentListingActivity.playedDuration "+PalContentListingActivity.playedDuration);
                }


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
                PalContentListingActivity.current_duration=videopos;
                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.checkAndStartPractice();
                    else ((PalContentListingActivity) PalContentListingActivity.context).checkAndStartPractice();
            }
        });

    }

    private void onVideoComplete() {

        if(Util.isPortraitMode(context))
        {
            onVideoCompleteMobile();
            return;
        }

        if(type.equals("level_practice_videos")) {
            QuizActivity.playNextVideo(context);
            return;
        }

        ArrayList<HashMap<String, Object>> videoList;

        if(type!=null && (type.equals("video_lessons")||type.equals("diksha_content"))) {
            videoList=PalContentListingActivity.videoContentArrayList;
        }
        else {
            videoList=PalContentListingActivity.dikshaContentArrayList_;
        }

        /** Checking Same Level video is available or not*/
        if (videoList != null && PalContentListingActivity.videoKey != null) {
            String[] videoKeySeparated = PalContentListingActivity.videoKey.split("-");
            int videoLevel = Integer.parseInt(videoKeySeparated[0]);
            int videoPosition = Integer.parseInt(videoKeySeparated[1])+1;

            for (int i = 0; i < videoList.size(); i++) {
                if (videoList.get(i).containsKey(videoLevel + "-" + videoPosition)) {
                    PalContentListingActivity.keys=videoLevel + "-" + videoPosition;
                    /** Adding Delay before playing next video */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /** Play next video */
                            playNextVideo();
                        }
                    },1000);

                    break;
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

//                                if(Util.getLevel(context)<videoLevel)
//                                {
//                                    /** show no more level video available */
//                                    showNextVideoDialog(false);
//                                }
//                                else
//                                {
//                                    /** show practice current level  */
//                                    showNextVideoDialog(true);
//                                }

                                break;
                            } else {

                                if(j==videoList.size()-1)
                                {

                                    /** show practice current level  */
                                    showNextVideoDialog(false);
                                    PalContentListingActivity.keys="";
                                    try {

                                        PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                                        videoListAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                System.out.println("========= removing ");
//                                                ((PalContentListingActivity_Mobile) context).removeFragment();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    },2000);
                                }


                            }
                        }

                    }

                }
            }
        }

    }

    private void onVideoCompleteMobile() {


        if(type.equals("level_practice_videos")) {
            QuizActivity.playNextVideo(context);
            return;
        }

        ArrayList<HashMap<String, Object>> videoList = null;

        if(type!=null) {
            if(Util.isPortraitMode(context)) videoList=PalContentListingActivity_Mobile.videoContentArrayList;
            else videoList=PalContentListingActivity.videoContentArrayList;
        }

        /** Checking Same Level video is available or not*/
        if (videoList != null && PalContentListingActivity_Mobile.videoKey != null) {
            String[] videoKeySeparated = PalContentListingActivity_Mobile.videoKey.split("-");
            int videoLevel = Integer.parseInt(videoKeySeparated[0]);
            int videoPosition = Integer.parseInt(videoKeySeparated[1])+1;

            for (int i = 0; i < videoList.size(); i++) {
                if (videoList.get(i).containsKey(videoLevel + "-" + videoPosition)) {
                    PalContentListingActivity_Mobile.keys=videoLevel + "-" + videoPosition;
                    /** Adding Delay before playing next video */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /** Play next video */
                            playNextVideo();
                        }
                    },1000);

                    break;
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

//                                if(Util.getLevel(context)<videoLevel)
//                                {
//                                    /** show no more level video available */
//                                    showNextVideoDialog(false);
//                                }
//                                else
//                                {
//                                    /** show practice current level  */
//                                    showNextVideoDialog(true);
//                                }

                                break;
                            } else {

                                if(j==videoList.size()-1)
                                {

                                    /** show practice current level  */
                                    showNextVideoDialog(false);
                                    PalContentListingActivity_Mobile.keys="";
                                    try {

                                        PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                                        videoListAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                System.out.println("========= removing ");
//                                                ((PalContentListingActivity_Mobile) context).removeFragment();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    },2000);
                                }


                            }
                        }

                    }

                }
            }
        }

    }

    private void showNextVideoDialog(boolean showNextVideo) {

        if(Util.isPortraitMode(context))
        {
            showNextVideoDialogMobile(showNextVideo);
            return;
        }

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
            if(!showNextVideo) nextVideo = "Start Practice";
            else nextVideo = "Next Level Video";
            practiceText="Practice";
            closeText="Close";
            secondsRemainingText ="Second remaining: ";
            if(!showNextVideo) nextVideoPlayingText ="Playing next Level Video";
            else nextVideoPlayingText ="Start Practice";
            if(!showNextVideo) videoCompletedText ="Level Video Completed\nStart Practice";
            else videoCompletedText ="Level Video Completed";
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


        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickednext=true;
                if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                {
                    // Util.showToast(context, foundationalText);
                    Util.openGifDialogue(context, foundationalText);
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                }else {
                    if(type != null && type.equals("foundationalTopicVideos")){
                        ((ExtraContentListingActivity) context).getPracticeContent();
                        ((ExtraContentListingActivity) context).removeFragment(true);

                    }else{
                        ((PalContentListingActivity) context).startPractice();
//                        ((PalContentListingActivity) context).removeFragment();
                    }
                }

                clickednext=true;
                dialog.dismiss();
            }
        });
        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext=true;
                PalContentListingActivity.keys="";
                try {
//            keys="";
                    PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                    videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                else ((PalContentListingActivity) context).removeFragment();

            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext =true;
                dialog.dismiss();
                playNextVideo();

            }
        });
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);

        if(!showNextVideo)
        {
            textViewOkay.setVisibility(View.GONE);
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    understandMasteryText.setText(secondsRemainingText + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    if(!clickednext)
                    {

                        System.out.println( "------------- c ");
                        if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                        {
                            // Util.showToast(context, foundationalText);
                            Util.openGifDialogue(context, foundationalText);
                        }else {
                            if(type != null && type.equals("foundationalTopicVideos")){
                                ((ExtraContentListingActivity) context).removeFragment(true);
                                ((ExtraContentListingActivity) context).getPracticeContent();
                            }else{
                                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                                else ((PalContentListingActivity) context).removeFragment();
                                ((PalContentListingActivity) context).startPractice();
                            }
                        }

                        clickednext=true;
                        dialog.dismiss();
                    }
                }

            }.start();
        }
        else {
            new CountDownTimer(5000, 1000) {

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

        }

        /** show the dialog */
        dialog.show();
        /** Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    private void showNextVideoDialogMobile(boolean showNextVideo) {
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
            if(!showNextVideo) nextVideo = "Start Practice";
            else nextVideo = "Next Level Video";
            practiceText="Practice";
            closeText="Close";
            secondsRemainingText ="Second remaining: ";
            if(!showNextVideo) nextVideoPlayingText ="Playing next Level Video";
            else nextVideoPlayingText ="Start Practice";
            if(!showNextVideo) videoCompletedText ="Level Video Completed\nStart Practice";
            else videoCompletedText ="Level Video Completed";
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


        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickednext=true;
                if (PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
                {
                    // Util.showToast(context, foundationalText);
                    Util.openGifDialogue(context, foundationalText);
                    PalTopicListingActivity.palTopicListingActivity.removeFragment();
                }else {
                    if(type != null && type.equals("foundationalTopicVideos")){
                        ((ExtraContentListingActivity) context).getPracticeContent();
                        ((ExtraContentListingActivity) context).removeFragment(true);

                    }else{
                        ((PalContentListingActivity) context).startPractice();
//                        ((PalContentListingActivity) context).removeFragment();
                    }
                }

                clickednext=true;
                dialog.dismiss();
            }
        });
        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext=true;
                PalContentListingActivity.keys="";
                try {
//            keys="";
                    PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                    videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
                PalTopicListingActivity.palTopicListingActivity.removeFragment();


            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext =true;
                dialog.dismiss();
                playNextVideo();

            }
        });
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);

        if(!showNextVideo)
        {
            textViewOkay.setVisibility(View.GONE);
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    understandMasteryText.setText(secondsRemainingText + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    if(!clickednext)
                    {

                        System.out.println( "------------- c ");
                        if (PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
                        {
                            // Util.showToast(context, foundationalText);
                            Util.openGifDialogue(context, foundationalText);
                        }else {
                            if(type != null && type.equals("foundationalTopicVideos")){
                                ((ExtraContentListingActivity) context).removeFragment(true);
                                ((ExtraContentListingActivity) context).getPracticeContent();
                            }else{
                                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                                else ((PalContentListingActivity) context).removeFragment();
                                ((PalContentListingActivity) context).startPractice();
                            }
                        }

                        clickednext=true;
                        dialog.dismiss();
                    }
                }

            }.start();
        }
        else {
            new CountDownTimer(5000, 1000) {

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

        }

        /** show the dialog */
        dialog.show();
        /** Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    private void showPracticeDialog(boolean showNextVideo) {
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
            if(!showNextVideo) nextVideo = "Start Practice";
            else nextVideo = "Next Level Video";
            practiceText="Practice";
            closeText="Close";
            secondsRemainingText ="Second remaining: ";
            if(!showNextVideo) nextVideoPlayingText ="Playing next Level Video";
            else nextVideoPlayingText ="Start Practice";
            if(!showNextVideo) videoCompletedText ="Level Video Completed\nStart Practice";
            else videoCompletedText ="Level Video Completed";
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


        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickednext=true;
                if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                {
                    // Util.showToast(context, foundationalText);
                    Util.openGifDialogue(context, foundationalText);
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                }else {
                    if(type != null && type.equals("foundationalTopicVideos")){
                        ((ExtraContentListingActivity) context).getPracticeContent();
                        ((ExtraContentListingActivity) context).removeFragment(true);

                    }else{
                        ((PalContentListingActivity) context).startPractice();
//                        ((PalContentListingActivity) context).removeFragment();
                    }
                }

                clickednext=true;
                dialog.dismiss();
            }
        });
        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext=true;
                PalContentListingActivity.keys="";
                try {
//            keys="";
                    PalDikshaContentFragment.videoListAdapter.notifyDataSetChanged();
                    videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                else ((PalContentListingActivity) context).removeFragment();

            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Dismis MidwayDialog */
                clickednext =true;
                dialog.dismiss();
                playNextVideo();

            }
        });
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);

        if(!showNextVideo)
        {
            textViewOkay.setVisibility(View.GONE);
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    understandMasteryText.setText(secondsRemainingText + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    if(!clickednext)
                    {

                        System.out.println( "------------- c ");
                        if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                        {
                            // Util.showToast(context, foundationalText);
                            Util.openGifDialogue(context, foundationalText);
                        }else {
                            if(type != null && type.equals("foundationalTopicVideos")){
                                ((ExtraContentListingActivity) context).removeFragment(true);
                                ((ExtraContentListingActivity) context).getPracticeContent();
                            }else{
                                if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                                else ((PalContentListingActivity) context).removeFragment();
                                ((PalContentListingActivity) context).startPractice();
                            }
                        }

                        clickednext=true;
                        dialog.dismiss();
                    }
                }

            }.start();
        }
        else {
            new CountDownTimer(5000, 1000) {

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

        }

        /** show the dialog */
        dialog.show();
        /** Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    private void playNextVideo() {

        if(Util.isPortraitMode(context)) {
            playNextVideoMobile();
            return;
        }

        if(Util.checkInternetConnection(context) || Util.isOfflineMode(context)) {
            mProgressBar.setVisibility(View.VISIBLE);
            isloading=true;
            checkConnection(false);
            try {
                videoView.clearFocus();
                videoView.stopPlayback();
                videoView.suspend();
            }catch (Exception e)
            {}
            video_view_visible=false;
            youtube_view_visible=false;
//            current_duration=0;
            if(type != null && type.equals("foundationalTopicVideos")){
                if(ExtraContentListingActivity.videoKey != null){
                    String[] videoKeySeparated = ExtraContentListingActivity.videoKey.split("-");
                    int videoLevel = Integer.parseInt(videoKeySeparated[0]);
                    int videoPosition = Integer.parseInt(videoKeySeparated[1]) + 1;
                    ExtraContentListingActivity.videoKey = videoLevel + "-" + videoPosition;
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ExtraContentListingActivity.openVideoView(ExtraContentListingActivity.videoKey);
                }
            }else{
                if(PalContentListingActivity.videoKey != null){
                    String[] videoKeySeparated = PalContentListingActivity.videoKey.split("-");
                    int videoLevel = Integer.parseInt(videoKeySeparated[0]);
                    int videoPosition = Integer.parseInt(videoKeySeparated[1]) + 1;
                    PalContentListingActivity.videoKey = videoLevel + "-" + videoPosition;
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                    ((PalContentListingActivity) context).openVideoView(PalContentListingActivity.videoKey,type);
                }
            }
        }
    }

    private void playNextVideoMobile() {
        if(Util.checkInternetConnection(context) || Util.isOfflineMode(context)) {
            mProgressBar.setVisibility(View.VISIBLE);
            isloading=true;
            checkConnection(false);
            try {
                videoView.clearFocus();
                videoView.stopPlayback();
                videoView.suspend();
            }catch (Exception e)
            {}
            video_view_visible=false;
            youtube_view_visible=false;
//            current_duration=0;
            if(type != null && type.equals("foundationalTopicVideos")){
                if(ExtraContentListingActivity.videoKey != null){
                    String[] videoKeySeparated = ExtraContentListingActivity.videoKey.split("-");
                    int videoLevel = Integer.parseInt(videoKeySeparated[0]);
                    int videoPosition = Integer.parseInt(videoKeySeparated[1]) + 1;
                    ExtraContentListingActivity.videoKey = videoLevel + "-" + videoPosition;
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ExtraContentListingActivity.openVideoView(ExtraContentListingActivity.videoKey);
                }
            }else{
                if(PalContentListingActivity_Mobile.videoKey != null){
                    String[] videoKeySeparated = PalContentListingActivity_Mobile.videoKey.split("-");
                    int videoLevel = Integer.parseInt(videoKeySeparated[0]);
                    int videoPosition = Integer.parseInt(videoKeySeparated[1]) + 1;
                    PalContentListingActivity_Mobile.videoKey = videoLevel + "-" + videoPosition;
                    if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.removeFragment();
                    else ((PalContentListingActivity) context).removeFragment();
                    PalTopicListingActivity.palTopicListingActivity.openVideoView(PalContentListingActivity_Mobile.videoKey,type);
                }
            }
        }
    }

    public void stopTimer() {
        updatedTime = 0L;
        startTime = 0L;
        timeSwapBuff = 0L;
        customHandler.removeCallbacks(updateTimerThread);
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

}
