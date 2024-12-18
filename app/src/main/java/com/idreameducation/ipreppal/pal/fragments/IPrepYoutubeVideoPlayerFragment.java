package com.idreameducation.ipreppal.pal.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity.clickedVideoID;
import static com.idreameducation.ipreppal.pal.activity.ActivityVideosListingActivity.currentVideoID;
import static com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity.mediaController;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.activity.ActivityVideosListingActivity;
import com.idreameducation.ipreppal.pal.activity.StemProjectsListingActivity;
import com.idreameducation.ipreppal.pal.videocontroller.ActivityVideosFullScreenController;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseVideoRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoReportRepository;
import com.idreameducation.ipreppal.services.SocketService;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenHelper;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.IPrepFullScreenMediaController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class IPrepYoutubeVideoPlayerFragment extends Fragment {

    //    public YouTubePlayer youtubePlayer;
    private Context context;
    private int time = 0;
    private String type = "Project Videos";
    private String videoName;
    private ImageView imageViewCrossVideo, imageViewCrossVideo_;
    private Global global;
    private String board;
    private String sClass;
    private String subject;
    private int adapterPos, position;
    private String topicID,clickedVideoID11;
    private String icon;
    private String language;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerSupportFragment youtubeFragment;
    private YouTubePlayerTracker tracker;
    private FullScreenHelper fullScreenHelper;
    private long totalTimee;
    private long classTime;
    private long seconds, minutes, milliseconds;
    private String topicSelected;
    private String category;
    private String url;
    private String topicName;
    private boolean isFullScreen, canSaveReports=true;
    private float duration;
    public YouTubePlayer youtubePlayer;
    private String timeToSendTofirebase_;
    private long timeget, timeget2;
    private int timeTOsync;
    public long updatedTime = 0L;
    private long startTime = 0L;
    public long timeInMilliseconds = 0L;
    public long timeSwapBuff = 0L;
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
    private VideoReportRepository videoReportRepository;
    private RelativeLayout yt_video_layout, reletiveVideoView;
    private VideoView videoView;
    ArrayList<HashMap<String, String>> videoList;
    ArrayList<HashMap<String, String>> videoList_;
    public ArrayList<HashMap<String, Object>> contentArrayList_;


    private String offlineLink;
    private long endTime = 0L;
    Uri filePath;
    private ProgressBar mProgressBar;
    private int id;


    private long mStartTime = 0L;
    private TextView mTimeLabel;

    //handler to handle the message to the timer task
    private final Handler mHandler = new Handler();


    final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    mProgressBar.setVisibility(View.GONE);
                    isloading = false;
                    hideconnection_layout();
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    mProgressBar.setVisibility(View.VISIBLE);
                    isloading = true;
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    mProgressBar.setVisibility(View.GONE);
                    isloading = false;
                    hideconnection_layout();
                    return true;
                }
            }
            return false;
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        system_millis = String.valueOf(System.currentTimeMillis());

        if (mStartTime == 0L) {
            mStartTime = SystemClock.uptimeMillis();
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 100);
        }
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iprep_youtube_video_player_fragment, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        slow_internet_Text = view.findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = view.findViewById(R.id.imageViewCrossVideo2);


        assignIds(view, savedInstanceState);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            youtubePlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String timeToSendTofirebase;
    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;
            timeToSendTofirebase_ = String.valueOf(millis);
            seconds = millis / 1000;
            minutes = seconds / 60;
            seconds = seconds % 60;
            //String text = "" + minutes + ":" + String.format("%02d", seconds);
            timeToSendTofirebase = "" + minutes + ":" + String.format("%02d", seconds);
            //mTimeLabel.setText("" + minutes + ":" + String.format("%02d", seconds));
            mHandler.postDelayed(this, 100);
        }
    };


    private boolean isloading = true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler = new Handler();

    private void checkConnection(boolean first) {

        if (!Util.isOfflineMode(context)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);

                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        }

                    } else {
                        hideconnection_layout();
                    }
                }
            }, 10000);//time in milisecond
        } else {
            hideconnection_layout();
        }

    }

    private void hideconnection_layout() {
        isloading = false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);

    }


    private void assignIds(View view, Bundle savedInstanceState) {
        context = getActivity();
        global = (Global) context.getApplicationContext();
        board = Util.getSelectedBoard(context);
        sClass = Util.getSelectedClass(context);
        subject = Util.getSubject(context);
        subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
        language = Util.getSelectedLanguage(context);
        startTime = System.currentTimeMillis();
        system_millis = String.valueOf(System.currentTimeMillis());

        videoView = view.findViewById(R.id.videoView);

        topicName = getArguments().getString("topicName");
        url = getArguments().getString("url");
//        topicID = ge /tArguments().getString("id");
        videoName = getArguments().getString("videoName");
        offlineLink = getArguments().getString("offlineLink");
        isFullScreen = getArguments().getBoolean("isFullScreen");
        duration = getArguments().getFloat("duration");
        id = getArguments().getInt("id");
        adapterPos = getArguments().getInt("adapterPos");
        position = getArguments().getInt("position");
        clickedVideoID11 = getArguments().getString("clickedVideoID");
        timestamp = getArguments().getString("timestamp");

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        tracker = new YouTubePlayerTracker();
        fullScreenHelper = new FullScreenHelper(getActivity());
        yt_video_layout = view.findViewById(R.id.yt_video_layout);
        reletiveVideoView = view.findViewById(R.id.reletiveVideoView);

        imageViewCrossVideo = view.findViewById(R.id.imageViewCrossVideo);
        imageViewCrossVideo_ = view.findViewById(R.id.imageViewCrossVideo_);
        imageViewCrossVideo.setVisibility(View.VISIBLE);
        imageViewCrossVideo_.setVisibility(View.VISIBLE);

        try {
            contentArrayList_ = ((ActivityVideosListingActivity) context).contentArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                contentArrayList_ = ((StemProjectsListingActivity) context).contentArrayList;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        videoReportRepository = new VideoReportRepository(getApplicationContext());
        imageViewCrossVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isPortraitMode(context))
                    ((Project_SubTopic_Activity) context).remove_videoLayout();
                else try {
                    ((ActivityVideosListingActivity) context).removeFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((StemProjectsListingActivity) context).removeFragment();
                }
            }
        });
        imageViewCrossVideo_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isPortraitMode(context))
                    ((Project_SubTopic_Activity) context).remove_videoLayout();
                else try {
                    ((ActivityVideosListingActivity) context).removeFragment();
                } catch (Exception e) {
                    ((StemProjectsListingActivity) context).removeFragment();
                }

            }
        });

        view.findViewById(R.id.fullscreen_bbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.isPortraitMode(context)) {
                    ((Project_SubTopic_Activity)context).rotate_screen();
                } else {
                    try {

                        if(((ActivityVideosListingActivity) context).isVideoFullScreen)
                        {
                            ((ActivityVideosListingActivity) context).showSmallScreenVideo();
                        }
                        else {
                            ((ActivityVideosListingActivity) context).showFullScreenVideo();
                        }

                    } catch (Exception e) {

                        ((StemProjectsListingActivity) context).backToNormalView();
                    }

                }


            }
        });


        isloading = true;
        checkConnection(false);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(onInfoToPlayStateListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        hideStatusBar();

        try {
            getTotalTime();
            getTime();
            getTime2();
            t_time();
            tsubject_time();
            getTimevideo_lessons();
            totalcount();
//            getClassTime(url);

            if (offlineLink != null) {
                reletiveVideoView.setVisibility(View.VISIBLE);
                yt_video_layout.setVisibility(View.GONE);
                filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/videos/" + offlineLink);
                File file = new File(filePath.toString());
                if (file.exists()) {
                    serviceIntent = new Intent(context, SocketService.class);
                    context.startService(serviceIntent);
                    checkPermissions(offlineLink);
                } else {
                    if (Util.isOfflineMode(context))
                        Toast.makeText(context, "Video Not Available", Toast.LENGTH_SHORT).show();
                    reletiveVideoView.setVisibility(View.VISIBLE);
                    yt_video_layout.setVisibility(View.GONE);
//                    initializePlayer(url,0,null);

                    initYouTubePlayerView();
                    reletiveVideoView.setVisibility(View.GONE);
                    yt_video_layout.setVisibility(View.VISIBLE);

                }
            } else {
                    initYouTubePlayerView();
                reletiveVideoView.setVisibility(View.GONE);
                yt_video_layout.setVisibility(View.VISIBLE);
//                initializePlayer(url,0,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getTimeforfirebase();

    }


    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


            getActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );


        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        try {
//            if (Util.isOfflineMode(context)) {
//                context.stopService(serviceIntent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


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
            playVideofromSDCard(path);
        } else {
            // check if first time registration is required
            playVideofromSDCard(path);

        }
    }

    private int totalDuration = 3000;
    private boolean isVideoCompleted;
    private Intent serviceIntent;

    private void playVideofromSDCard(String path) {
        Uri filePath;
        isloading = true;
        checkConnection(false);
        filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/videos/" + path);
        getTotalTime();
        getTime();
        t_time();
        tsubject_time();

        String path_;
//        if (global.isEncryptedContent()) {

//        serviceIntent = new Intent(context, SocketService.class);
//        context.startService(serviceIntent);
        path_ = "http://localhost:7453" + filePath;
        //   path_ =filePath.toString();


//        } else {
//            path_ = filePath.toString();
//        }
        File file = new File(filePath.toString());
        if (file.exists()) {
            videoView.setVideoPath(path_);
            ActivityVideosFullScreenController mediacontroller = new ActivityVideosFullScreenController(context, filePath, true, videoView, getActivity(), videoName, topicID);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.start();
            totalDuration = videoView.getDuration();

            hideconnection_layout();


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
                    playVideofromSDCard(path);
                    //          ((StemProjectsListingActivity)context).removeFragment();
                    return true;
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    hideconnection_layout();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isVideoCompleted = true;
                    context.stopService(serviceIntent);
                    try {
                        int time = videoView.getCurrentPosition();
                        syncClassTime_();
                        syncTotalTime(time);
                        stopTimer();
                        //                      completitionofvideo();
                        if (Util.isPortraitMode(context))
                            ((Project_SubTopic_Activity) context).remove_videoLayout();
                        else try {
                            ((ActivityVideosListingActivity) context).removeFragment();
                        } catch (Exception e) {
                            ((StemProjectsListingActivity) context).removeFragment();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//                    if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
//                        isFullScreenModeEnabled = false;
//                        try {
//                            if (pos == global.getVideoArrayList().size() - 1) {
//                                pos = 0;
//                            } else {
//                                pos++;
//                            }
//                            timeforVideo = 0;
//                            try {
//                                videoType = global.getVideoArrayList().get(pos).get("type");
//                                videoType = "Vimeo";
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            url = global.getVideoArrayList().get(pos).get("videoUrl");
//                            videoID = global.getVideoArrayList().get(pos).get("onlineLink");
//                            offlinePath = global.getVideoArrayList().get(pos).get("offlineLink");
//                            videoName = global.getVideoArrayList().get(pos).get("name");
//                            getClassTime(videoID);
//                            getTime();
//                            textViewReadMore = findViewById(R.id.textViewReadMore);
//                            textViewDetail.setText(global.getVideoArrayList().get(pos).get("detail"));
//                            textViewName.setText(global.getVideoArrayList().get(pos).get("name"));
//                            mProgressBar.setVisibility(View.VISIBLE);
//                            videoView.setVisibility(View.VISIBLE);
//                            container.setVisibility(View.GONE);
//                            videoView.pause();
//                            videoView.stopPlayback();
//
//                            playVideofromSDCard(offlinePath);
//
//                            videoListSmallAdapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        isFullScreenModeEnabled = true;
//                    }


                }
            });

        } else {

            Toast.makeText(context, "Not Working", Toast.LENGTH_SHORT).show();
//            finish();
//            Util.showToast(context, filenotexist);
        }
    }

    public void stopTimer() {
        updatedTime = 0L;
        startTime = 0L;
        timeSwapBuff = 0L;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child("count").child(date).child("activityvideos").addValueEventListener(new ValueEventListener() {
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


    private long total_time_spent;
    private long total_time_spent_video_lessons;

    private void getTime() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(board).child(Util.getSelectedLanguage(getApplicationContext())).child("Total_time").child("total_time_spent").addValueEventListener(new ValueEventListener() {
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

    private long t_timing;

    private void t_time() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(board).child(Util.getSelectedLanguage(getApplicationContext())).child("stem_videos").child("overall").child(videoName).child("t_time").addValueEventListener(new ValueEventListener() {
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
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child("Reports").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(board).child(Util.getSelectedLanguage(getApplicationContext())).child("stem_videos").child(Util.getSubject(context)).child(videoName).child("t_time").addValueEventListener(new ValueEventListener() {
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


    private void getTimevideo_lessons() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(board).child(Util.getSelectedLanguage(getApplicationContext())).child("stem_videos").child(Util.getSubject(context)).child("Total_time").child("total_time_spent_video_lessons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue() != null) {
                            total_time_spent_video_lessons = (long) snapshot.getValue();
                        } else {
                            total_time_spent_video_lessons = 0;
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


    private long total_no_of_videos_played;

    private void totalcount() {

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("IprepTv").child(Util.getNGOID(getApplicationContext())).child(Util.getUserId(context)).child(Util.getSelectedClass(getApplicationContext())).child(Util.getSelectedBoard(getApplicationContext())).child(Util.getSelectedLanguage(getApplicationContext())).child("totalCount").child("stem_videos").child("totalcount").child("Total_no_of_count").addValueEventListener(new ValueEventListener() {
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

    private void getTime2() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
//            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubjectName(context), date, "video_lessons", "timeTask");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child("Project Videos".toLowerCase().replace(" ", "_")).addValueEventListener(new ValueEventListener() {
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


    String date;

    private void getTimeforfirebase() {
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        date = Util.getCurrentDateWithDifferentFormat();
        if (Util.isOfflineMode(context)) {
            getList(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), type, date).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            timeTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsTimeSpentModel> list = (ArrayList<ReportsTimeSpentModel>) o;
                            if (list != null && list.size() > 0) {
                                for (ReportsTimeSpentModel item : list) {
                                    timeget = item.getTime();
                                }
                            } else {
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
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS)
                    .child(Util.getUserId(context))
                    .child(Util.getSelectedBoard(context))
                    .child(sClass)
                    .child("time_spent")
                    .child(date)
                    .child("Project Videos")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    if (snapshot.getValue() != null) {
                                        timeget = (long) snapshot.getValue();
                                        timeget2 = (long) snapshot.getValue();
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

    private void syncClassTime_() {

        if(canSaveReports) {

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            long syncClassTime = updatedTime + totalTimee;
            HashMap<String, String> hashMapToSync = new HashMap<>();
            hashMapToSync.put("time", "" + syncClassTime);
            hashMapToSync.put("name", videoName);
            hashMapToSync.put("username", Util.getUsername(context));
            hashMapToSync.put("userclass", "6");
            hashMapToSync.put("watchedTime", System.currentTimeMillis() + "");
            hashMapToSync.put("totalDuration", timeToSendTofirebase + "");
            hashMapToSync.put("finaltime", milliseconds + "");
            hashMapToSync.put("topicName", id + "");
            hashMapToSync.put("date", formattedDate);


            String date = Util.getCurrentDateWithDifferentFormat();
            endTime = System.currentTimeMillis();
            long timeTosync = endTime - startTime;
            long timeTosync2;
            long ttime = timeTosync;
            videoNumber = videoNumber + 1;

            timeTosync2 = timeTosync + timeget;


            timeTosync = timeTosync + timeget2;


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


            row_usage.put("category", "activity_videos");
            row_usage.put("class", Util.getSelectedClass(context));
            row_usage.put("content_name", videoName);
            row_usage.put("topic_name", topicName);
            row_usage.put("district", Util.getDistrict(context));
            row_usage.put("schoolID", Util.getSchoolId(context));
            row_usage.put("schoolName", Util.getSchoolName(context));
            row_usage.put("projectId", Util.getProjectId(context));
            row_usage.put("state", Util.getSelectedState(context));
            row_usage.put("subject", Util.getSubjectId(context));// subject id
            row_usage.put("subject_name", Util.getSubject(context));
            row_usage.put("language", Util.getSelectedLanguage(context));
            row_usage.put("time_spent", timeToSendTofirebase_ + "");
            row_usage.put("topic", "" + id);
            row_usage.put("userId", Util.getUserId(context));
            row_usage.put("username", Util.getUsernameShowable(context));
            // row_usage.put("userIdFirebase", Util.getLoginUserId(context));
            row_usage.put("board", Util.getSelectedBoard(context));
            row_usage.put("category_name", "Project Videos");
            row_usage.put("userType", "students");
            row_usage.put("app_id", Util.getAPPID(context));

            globalxq.getDatabaseReference().child(Util.rawUsageNode)
                    .child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
            global.getDatabaseReference().child(Util.segmentedRawUsageNode)
                    .child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);

            global.getDatabaseReference().child(ApplicationConstants.REPORTS)
                    .child(Util.getUserId(context))
                    .child(Util.getSelectedBoard(context))
                    .child(sClass).child("time_spent")
                    .child(date)
                    .child("Project Videos")
                    .setValue(timeTosync);

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child("Project Videos".replace(" ", "_")).setValue(timeTosync2);
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("activityvideos").setValue(videoNumber);


            HashMap<String, String> hashmapForTimeAndCount = new HashMap<>();
            hashmapForTimeAndCount.put("date", formattedDate);
            hashmapForTimeAndCount.put("totalDuration", timeToSendTofirebase_ + "");

        }
    }


    public static RequestQueue requestQueue;

    private void initializePlayer(String vid_id, int time,View view) throws Exception {

        Map<String, String> aa = new HashMap<>();
        String hdStream;

        requestQueue = Volley.newRequestQueue(context);

//        String url = "https://learn.iprep.in/api/video?videoid=" + vid_id + "&origin=vimeo.com";
        String url = "https://backend.iprep.in/api/video_data_app/?onlineLink="+vid_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {

                            if(response.getString("messgage").equals("Success")) {

                                JSONArray data = response.getJSONArray("data");

                                JSONObject dataMap = (JSONObject) data.get(0);

                                String url = dataMap.getString("url");


                                playVideo(url,false,time,view);

                            }
                            else {

                                Toast.makeText(context, "No playback URL found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
//                        Toast.makeText(context, error + "", Toast.LENGTH_SHORT).show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    Handler handler2=new Handler();
    boolean isVideoLoaded =  false;

    String timestamp="";
    private void playVideo(final String stream,boolean isLocalFile, int time,View view) {

        if(!currentVideoID.equals(timestamp)) return;

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
//                        video_view_visible=true;
//                        youtube_view_visible=false;
                        Uri video = Uri.parse(stream);

                        mediaController = new IPrepFullScreenMediaController(context,"ActivityVideos", video, false, videoView, getActivity(), videoName, topicID,null,isLocalFile,time);
//                            mediaController = new MediaController(context);
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
//                            mediaController.hide();
                        videoView.setVideoURI(video);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                isloading=false;
                                isVideoLoaded=true;
                                canSaveReports=true;
//                                totalTime = mp.getDuration();
                                timeTOsync = mp.getDuration();
                                getTime();
//                                videoisLoaded=true;
//                                if(totalTime<=PalContentListingActivity.duration+2000) {
//                                    videoView.seekTo(0);
//                                }
                                isVideoLoaded=true;
                                videoView.start();
//                                showPracticeTimer();
                            }
                        });
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                try {
//                                    PalContentListingActivity.instance.setPath(board, sClass, Util.getSubjectName(context), topicID, "V");
//                                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child(topicID).child("video_lessons").setValue("V");
//                                        saveData();
                                    stopTimer();
//                                    PalContentListingActivity.duration=0;
//                                    PalContentListingActivity.current_duration=0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mProgressBar.setVisibility(View.GONE);
                                slow_internet_Text.setVisibility(View.GONE);
                                imageViewCrossVideo2.setVisibility(View.GONE);

//                                    showNextVideoDialog();
//                                onVideoComplete();
                            }
                        });

                        videoView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent event) {
                                try {
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
                                }
                                catch (Exception r) {

                                }
                                return true;
                            }
                        });

                        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {

                                System.out.println("---- mediaPlayer.getPlaybackParams() "+mediaPlayer.getPlaybackParams().getSpeed());

//                                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));

                                switch (what) {
                                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                        mProgressBar.setVisibility(View.GONE);
                                        slow_internet_Text.setVisibility(View.GONE);
                                        isVideoLoaded=true;
                                        isloading=false;
                                        canSaveReports=true;
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
                                        canSaveReports=true;
                                        isVideoLoaded=true;
                                        return true;
                                    }
                                }
                                return false;
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }


    private void setMargins (View view, int left, int top, int right, int bottom) {
//        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//            p.setMargins(left, top, right, bottom);
//            view.requestLayout();
//        }
    }


    private void initYouTubePlayerView() {
        if (Util.checkInternetConnection(context)) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            yt_video_layout.setVisibility(View.VISIBLE);
            reletiveVideoView.setVisibility(View.GONE);
            getLifecycle().addObserver(youTubePlayerView);
//            youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
//            youTubePlayerView.getPlayerUiController().showBufferingProgress(false);
            youTubePlayerView.setEnableAutomaticInitialization(false);
            if (isFullScreen) {
//                youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
            }


            IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(1)
                    .autoplay(0)
                    .ccLoadPolicy(0)
                    .ivLoadPolicy(0)
                    .modestBranding(0)
                    .mute(0)
                    .build();

            youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                @Override
                public void onApiChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                    super.onApiChange(youTubePlayer);
                }

                @Override
                public void onCurrentSecond(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float second) {
                    super.onCurrentSecond(youTubePlayer, second);
                    try {
                        time = (int) tracker.getCurrentSecond();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    syncTotalTime(time);
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

                    System.out.println("- clickedVideoID11  "+clickedVideoID11);

                    if(clickedVideoID.equals(clickedVideoID11)) {

                        canSaveReports= true;

                        if (!isFullScreen) {
                            duration = 0;
                        }


                        youTubePlayer.addListener(tracker);
                        YouTubePlayerUtils.loadOrCueVideo(
                                youTubePlayer,
                                getLifecycle(),
                                getYouTubeId(url),
                                duration
                        );

                        try {
                            time = (int) tracker.getCurrentSecond();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        hideconnection_layout();
                        syncTotalTime(time);
                        addFullScreenListenerToPlayer();

                    }
                    else {
                        System.out.println("----------- \n\n\n\n\\n\n\\n\n\n\n cancled video ");
                    }

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
                            getTotalTime();
                            getTime();
                            t_time();
                            tsubject_time();
                            getTimevideo_lessons();
                            syncTotalTime(time);
                            syncClassTime_();
                            if (Util.isPortraitMode(context))
                                ((Project_SubTopic_Activity) context).remove_videoLayout();
                            else try {
                                ((ActivityVideosListingActivity) context).removeFragment();
                            } catch (Exception e) {
                                ((StemProjectsListingActivity) context).removeFragment();
                            }
                            //completitionofvideo();
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
            },iFramePlayerOptions);

            youTubePlayerView.addFullscreenListener(new FullscreenListener() {
                @Override
                public void onEnterFullscreen(@NonNull View fullscreenView, @NonNull Function0<Unit> exitFullscreen) {

                    try {
                        ((Project_SubTopic_Activity)context).rotate_screen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onExitFullscreen() {

                    try {
                        ((Project_SubTopic_Activity)context).rotate_screen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            //     videoView.setVisibility(View.GONE);
            String errorWhile;
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                errorWhile = "कृपया इस वीडियो को देखने के लिए इंटरनेट से जुड़ें";
            } else {
                errorWhile = "Please Connect with Internet to watch this video";
            }
            Util.openGifDialogue(context, errorWhile);
            // Toast.makeText(context,"Please Connect with Internet to watch this video",Toast.LENGTH_SHORT).show();
            if (Util.isPortraitMode(context))
                ((Project_SubTopic_Activity) context).remove_videoLayout();
            else try {
                ((ActivityVideosListingActivity) context).removeFragment();
            } catch (Exception e) {
                ((StemProjectsListingActivity) context).removeFragment();
            }
            //           completitionofvideoWithoutInternet();
        }
    }

    protected String system_millis = "0";

    boolean once = true;

    private void syncTotalTime(int time) {

        if (system_millis.equals("0")) return;

        time = videoView.getCurrentPosition();
        updatedTime = time;
        Log.i("Updated Time : ", updatedTime + "");
        long syncTotalTime = updatedTime + totalTimee;
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("totalTime")
                .child(Util.getUserId(context))
                .child("totaltimeactivityvideos").setValue("" + syncTotalTime);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        long ttime = timeTosync;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;
//        timeTosync = timeTosync + timeget;

        long syncClassTime = updatedTime + classTime;

        //timeToSendTofirebase_
        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(topicName);
        scoreModel.setTime(timeToSendTofirebase_ + "");
        scoreModel.setTotalTime(ttime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate("" + date);
        scoreModel.setSubjectName(subject);


        try {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise")
                    .child(Util.getUserId(context)).child(board).child(sClass)
                    .child(Util.getSelectedLanguage(context)).child(subject.toLowerCase().replace(" ", "_"))
                    .child("project_video").child(date).child(String.valueOf(topicName)).child("name")
                    .setValue(topicName);

            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise")
                    .child(Util.getUserId(context)).child(board).child(sClass)
                    .child(Util.getSelectedLanguage(context)).child(subject.toLowerCase().replace(" ", "_"))
                    .child("project_video").child(date).child(String.valueOf(topicName)).child("detail")
                    .child(String.valueOf(id)).child(system_millis).setValue(scoreModel);

            ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;

            reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);

            if (once) {
                if (reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "project_video", String.valueOf(id), date, System.currentTimeMillis(), Util.getSelectedLanguage(context))) {
                    reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "project_video", String.valueOf(id),
                            date, Long.parseLong(timeToSendTofirebase_), topicName, time + "", topicName, ttime + "", videoName, Util.getSelectedLanguage(context));
                    once = false;
                } else {
                    ReportsTopicWiseVideoModel reportsTopicWiseVideoModel = new ReportsTopicWiseVideoModel();
                    reportsTopicWiseVideoModel.setUserId(Util.getUserId(context));
                    reportsTopicWiseVideoModel.setBoard(board);
                    reportsTopicWiseVideoModel.setSClass(sClass);
                    reportsTopicWiseVideoModel.setSubject(subject);
                    reportsTopicWiseVideoModel.setDate(date);
                    reportsTopicWiseVideoModel.setName(topicName);
                    reportsTopicWiseVideoModel.setTime(Long.parseLong(timeToSendTofirebase_));
                    reportsTopicWiseVideoModel.setType("project_video");
                    reportsTopicWiseVideoModel.setTopicId(String.valueOf(id));
                    reportsTopicWiseVideoModel.setTopicName(topicName);
                    reportsTopicWiseVideoModel.setTotalTime(total_time_spent_video_lessons + "");
                    reportsTopicWiseVideoModel.setVTime(ttime + "");
                    reportsTopicWiseVideoModel.setVideoName(videoName);
                    reportsTopicWiseVideoModel.setVideoId(String.valueOf(id));
                    reportsTopicWiseVideoModel.setLang(Util.getSelectedLanguage(context));
                    reportsTopicWiseVideoModel.setSubjectName(subject);
                    reportsTopicWiseVideoRepository.insertVideoDetails(reportsTopicWiseVideoModel);
                    once = false;
                }

                sClass = Util.getSelectedClass(context);

                //For Time Spent
                if (reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, type, Util.getSelectedLanguage(context))) {
                    reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, type, timeTosync, Util.getSelectedLanguage(context));
                } else {
                    ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
                    reportsTimeSpentModel.setUserId(Util.getUserId(context));
                    reportsTimeSpentModel.setBoard(board);
                    reportsTimeSpentModel.setSClass(sClass);
                    reportsTimeSpentModel.setSubject(type);
                    reportsTimeSpentModel.setDate(date);
                    reportsTimeSpentModel.setTime(timeTosync);
                    reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
                    reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
                }
            }

        } catch (Exception rr) {
            rr.printStackTrace();
        }
    }

    private ReportsTimeSpentRepository reportsTimeSpentRepository;

    private Disposable timeTaskDisposable;

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date) {
        return Observable.fromCallable(() -> {
            //do something, get your Data object
            return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, subject, Util.getSelectedLanguage(context));
        });

    }
//    private void syncTotalTime(int time) {
//        updatedTime = time;
//        Log.i("Updated Time : ", updatedTime + "");
//        long syncTotalTime = updatedTime + totalTimee;
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("totalTime").child(Util.getUserId(context)).child("totaltimeactivityvideos").setValue("" + syncTotalTime);
//    }

    private void getTotalTime() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("totalTime").child(Util.getUserId(context)).child("totaltimeactivityvideos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        String time = (String) dataSnapshot.getValue();
                        totalTimee = Long.parseLong(time);
                        //Toast.makeText(context, "Time" + time, Toast.LENGTH_SHORT).show();
                    } else {
                        totalTimee = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addFullScreenListenerToPlayer() {
//        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
//            @Override
//            public void onYouTubePlayerEnterFullScreen() {
//
//                if (Util.isPortraitMode(context)) {
//                    ((Project_SubTopic_Activity) context).rotate_screen();
//                } else {
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    fullScreenHelper.enterFullScreen();
//                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
//                    try {
//                        ((ActivityVideosListingActivity) context).openVimeoVideoFullScreenFragment();
//                    } catch (Exception e) {
//                        ((StemProjectsListingActivity) context).openVimeoVideoFullScreenFragment();
//                    }
//
//                    syncTotalTime(time);
//                }
//
//
//                // addCustomActionsToPlayer();
//            }
//
//            @Override
//            public void onYouTubePlayerExitFullScreen() {
//
//                if (Util.isPortraitMode(context)) {
//                    ((Project_SubTopic_Activity) context).setlayout_forportrait();
//                    ((Project_SubTopic_Activity) context).rotate_screen();
//                } else {
//                    fullScreenHelper.exitFullScreen();
//                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
//                    try {
//                        ((ActivityVideosListingActivity) context).backToNormalView();
//                    } catch (Exception e) {
//                        ((StemProjectsListingActivity) context).backToNormalView();
//                    }
//                    syncTotalTime(time);
//                }
//
////                removeCustomActionsFromPlayer();
//            }
//        });
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return youTubeUrl;
        }
    }

    @Override
    public void onStop() {

//        syncClassTime_();
        syncTotalTime(time);
        videoView.pause();

        syncClassTime_();
        try {
            context.stopService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {

        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }

        try {
            videoView.pause();
            context.stopService(serviceIntent);
            if (Util.isOfflineMode(context)) {
                syncTotalTime(time);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            IPrepYoutubeVideoPlayerFragment.requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    Log.d("DEBUG", "request running: " + request.getTag().toString());
                    return true;
                }
            });

        }catch (Exception r){
            r.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            videoView.pause();
            context.stopService(serviceIntent);
            if (youTubePlayerView != null) {
                youTubePlayerView.release();
                //           syncClassTime_();
                syncTotalTime(time);
                //           context.stopService(serviceIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            IPrepYoutubeVideoPlayerFragment.requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    Log.d("DEBUG", "request running: " + request.getTag().toString());
                    return true;
                }
            });

        }catch (Exception r){
            r.printStackTrace();
        }

    }
}
