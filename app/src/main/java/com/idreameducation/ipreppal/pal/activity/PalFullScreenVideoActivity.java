package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.current_duration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.fragments.PalCustomMediaController;
import com.idreameducation.ipreppal.pal.videocontroller.ActivityVideosFullScreenController;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseVideoModel;
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
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalFullScreenVideoActivity extends AppCompatActivity {
    private Context context;
    private VideoView videoView;
    private ProgressBar progressBar;
    private RelativeLayout linearLayoutBottom,videoLayout;
    private ImageView imageViewBackButton;
    private TextView videoNameTextView;
    private Global global;
    private String offlineLink;
    private String onlineLink;
    private String vidId;
    private String videoName;
    private Intent serviceIntent;
    private boolean isLocalFile;
    private PalCustomMediaController palCustomMediaController;
    private FrameLayout controllerAnchor;
    String assignedData,assignedKey,teacherID,assignmentKey,assignmentName,from;

    boolean howAppworks;


    final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    progressBar.setVisibility(View.GONE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    progressBar.setVisibility(View.VISIBLE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    progressBar.setVisibility(View.GONE);

                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.pal_full_screen_video_view);
        context = getApplicationContext();
        global= (Global) getApplicationContext();
        vidId = getIntent().getStringExtra("vidId");

        onlineLink = getIntent().getStringExtra("onlineLink");
        offlineLink = getIntent().getStringExtra("offlineLink");
        videoName = getIntent().getStringExtra("videoName");
        assignedData = getIntent().getStringExtra("assignedDate");
        assignedKey = getIntent().getStringExtra("assignedKey");
        teacherID = getIntent().getStringExtra("teacherID");
        from = getIntent().getStringExtra("from");

        if(from==null) from="";

        assignmentKey = getIntent().getStringExtra("assignmentKey");
        assignmentName = getIntent().getStringExtra("assignmentName");

        howAppworks = getIntent().getBooleanExtra("howAppworks",false);
        batchID=global.getBatchID();

        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseVideoRepository = new ReportsDateWiseVideoRepository(context);
        reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);
        videoDetailsRepository = new VideoDetailsRepository(context);

        if(!Util.checkInternetConnection(context)) {
            if (offlineLink==null) {
                Toast.makeText(context, "Video not available", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        videoView = findViewById(R.id.videoView);
        videoLayout = findViewById(R.id.videoLayout);
        progressBar = findViewById(R.id.progressBar);
        linearLayoutBottom = findViewById(R.id.linearLayoutBottom);
//        linearLayoutBottom.setVisibility(View.GONE);
        imageViewBackButton = findViewById(R.id.imageViewBackButton);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        videoNameTextView.setText(videoName);
        controllerAnchor = findViewById(R.id.controllerAnchor);
        palCustomMediaController = new PalCustomMediaController(this);
        palCustomMediaController.setAnchorView(controllerAnchor);

        videoView.setMediaController(palCustomMediaController);

        hideStatusBar();

        getTime();

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                palCustomMediaController.hide();
                controllerAnchor.removeView(palCustomMediaController);
                destroyService();
                finish();
            }
        });

        playContent();
    }

    private void playContent() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(onInfoToPlayStateListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            try{
                int v = Integer.parseInt(vidId);
                vidId = "https://vimeo.com/"+vidId;
            }catch (Exception r) {

            }

            if(onlineLink==null) onlineLink=vidId;

            if(Util.isOfflineMode(context)) {
                Uri filePath ;

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


                File file = new File(String.valueOf(filePath));
                if(file.exists()){
                    isLocalFile = true;
                    playVideo(file.toString());
                }else{
                    isLocalFile = false;
                    initializePlayer(vidId);
                }
            }
            else {
                if(onlineLink.contains("https://vimeo.com/")){
                    isLocalFile = false;
                    initializePlayer(onlineLink.replace("https://vimeo.com/",""));
                }
                else if(onlineLink != null){
                    isLocalFile = false;
                    playVideo(onlineLink);
                }
                else{

                    Uri filePath ;

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


                    File file = new File(String.valueOf(filePath));
                    if(file.exists()){
                        isLocalFile = true;
                        playVideo(file.toString());
                    }else{
                        isLocalFile = false;
                        initializePlayer(vidId);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            try{
                Uri filePath ;

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


                File file = new File(String.valueOf(filePath));
                if(file.exists()){
                    isLocalFile = true;
                    playVideo(file.toString());
                }else{
                    isLocalFile = false;
                    initializePlayer(vidId);
                }
            }catch (Exception r) {
                r.printStackTrace();
            }
        }
    }

    public void hideStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {


            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );


        }
    }
    Handler handler2 = new Handler();
    RelativeLayout reletiveVideoView;

    RequestQueue requestQueue;

    private void initializePlayer(String vid_id) throws Exception {

        if(!Util.checkInternetConnection(context)) {
            Toast.makeText(context, "Video Not available in storage", Toast.LENGTH_SHORT).show();
            finish();
        }
        HashMap<String, String> aa =  new HashMap<>();
        progressBar.setVisibility(View.VISIBLE);
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
                                aa.put(((JSONObject) hashMap.get(i)).get("rendition").toString(),((JSONObject) hashMap.get(i)).get("link").toString());
                            }

                            String hdStream = aa.get("360p");

                            if(aa.containsKey(Util.getVideoQuality(context))) hdStream = aa.get(Util.getVideoQuality(context));

                            if(hdStream==null) {
                                hdStream=((JSONObject) hashMap.get(0)).get("link").toString();
                            }

                            playVideo(hdStream);


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

    private void playVideo(final String stream) {
        reletiveVideoView=findViewById(R.id.reletiveVideoView);

        runOnUiThread(new Runnable() {
            @SuppressLint({"NewApi", "ClickableViewAccessibility"})
            @Override
            public void run() {
                try {
                    if(stream.contains("youtu.be") || stream.contains("youtube.com"))
                    {
                        initYouTubePlayerView(stream.replace("https://youtu.be/","").replace("https://www.youtube.com/watch?v=",""),Uri.parse(stream.replace("https://youtu.be/","").replace("https://www.youtube.com/watch?v=","")),"topic_id");
                    }
                    else
                    {

                        final Uri video = Uri.parse(stream);

                        if(isLocalFile){
                            serviceIntent = new Intent(context, SocketService.class);
                            context.startService(serviceIntent);
                            String path = "http://localhost:7453" + stream;


//                            SurfaceHolder surfaceholder = videoView.getHolder();
//                            surfaceholder.setFormat(PixelFormat.TRANSPARENT);
                            videoView.setVideoPath(path);
                        }else videoView.setVideoURI(video);

                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                videoView.start();
                                totalDuration=String.valueOf(videoView.getDuration());
//                                palCustomMediaController.setAnchorView(videoView);
//                                if(QuizActivity.instance != null){
//                                    QuizActivity.instance.showNextQuestionButton(true);
//                                }
                            }
                        });
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                try {
                                    palCustomMediaController.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                showVideoEndDialog();
                            }


                        });
                        videoView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

//                                setMargins(reletiveVideoView,0,0,0,60);
                                handler2.removeCallbacksAndMessages(null);

                                if (palCustomMediaController != null) {
                                    try {
                                        linearLayoutBottom.setVisibility(View.VISIBLE);
//                                        if(!Util.isPortraitMode(context)) palCustomMediaController.show();
                                        palCustomMediaController.show();
                                        handler2.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                linearLayoutBottom.setVisibility(View.GONE);
                                                palCustomMediaController.hide();
                                                setMarginFromBottom(reletiveVideoView , 0);
//                                                            linearLayoutBottom.setVisibility(View.GONE);
//                                                setMargins(reletiveVideoView,0,0,0,0);
                                            }
                                        }, 3000);

                                    }
                                    catch (Exception r) {
                                        r.printStackTrace();
                                    }
                                }
                                return true;
                            }
                        });
                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setMarginFromBottom(RelativeLayout layout, int marginInPixels) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.bottomMargin = marginInPixels;
        layout.setLayoutParams(layoutParams);
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) view.getLayoutParams();
        p.setMargins(left, top, right, bottom);
        view.requestLayout();
    }

    private void showVideoEndDialog() {
        Dialog dialog = new Dialog(PalFullScreenVideoActivity.this);
        dialog.setContentView(R.layout.dialog_replay_video);
        dialog.setCancelable(false);
        /* Hook up the UI element in the Dialog */
        TextView textViewClose = dialog.findViewById(R.id.textViewClose);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        TextView textViewPractice = dialog.findViewById(R.id.textViewPractice);
        TextView understandMasteryText = dialog.findViewById(R.id.understandMasteryText);

        if(from.equals("search")) textViewPractice.setVisibility(View.GONE);
        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                dialog.dismiss();
                palCustomMediaController.hide();
                controllerAnchor.removeView(palCustomMediaController);
                destroyService();
                finish();
            }
        });

        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Dismis MidwayDialog */
                dialog.dismiss();
                palCustomMediaController.hide();
                controllerAnchor.removeView(palCustomMediaController);
                destroyService();
                finish();

            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Dismis MidwayDialog */
                dialog.dismiss();
                isLocalFile = false;
                try {
                    playContent();
//                    initializePlayer(vidId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        /* show the dialog */
        dialog.show();
        /* Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }
    private RelativeLayout yt_video_layout;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerTracker tracker;
    private float duration;
    private final boolean isFullScreen=true;
    private int time = 0;
    ImageView imageViewBackButton2;

    String totalDuration,watchTime;
    String batchID;

    private ReportsLatestDataVideoRepository reportsLatestDataVideoRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWiseVideoRepository reportsDateWiseVideoRepository;
    private ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;
    private Disposable timeTaskDisposable;
    private Disposable videoCountTaskDisposable;

    private VideoReportRepository videoReportRepository;
    private VideoDetailsRepository videoDetailsRepository;

    private void initYouTubePlayerView(String url_,Uri filePath,String topicID) {

        yt_video_layout=findViewById(R.id.yt_video_layout);
        youTubePlayerView=findViewById(R.id.youtube_player_view);
        imageViewBackButton2=findViewById(R.id.imageViewBackButton2);
        tracker = new YouTubePlayerTracker();

        ActivityVideosFullScreenController mediacontroller = new ActivityVideosFullScreenController(context,filePath,true,videoView,this,videoName,topicID);
        mediacontroller.setAnchorView(videoView);
        yt_video_layout.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.setEnableAutomaticInitialization(false);
        if (isFullScreen) {
//            youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        }
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
                    duration = 0;
                }

                youTubePlayer.addListener(tracker);
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        url_,
                        duration
                );

                try {
                    totalDuration=String.valueOf(tracker.getVideoDuration());
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
//                        syncTotalTime(time);
//                        ((PalContentListingActivity) context).removeFragment();
                        break;

                }

            }

            @Override
            public void onVideoDuration(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                try {
                    Log.i("Total duration :", duration + "");
//                    totalTimee = (int) duration;
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
        });

        imageViewBackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void destroyService(){
        if(serviceIntent != null){
            context.stopService(serviceIntent);
        }
    }

    private void saveAssignedReport() {
        if(assignedKey==null || totalDuration==null) return;

        HashMap<String,String> report=new HashMap<>();
        report.put("vid_TotalTime",totalDuration);
        report.put("vid_WatchTime",watchTime);
        report.put("timestamp", Util.getCurrentDateWithDifferentFormat());
        report.put("userName",Util.getUsername(context));

        global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                .child(assignedData).child(assignedKey).child("report").setValue(report);

        global.getDatabaseReference().child("content_assignment_batch_student").child(teacherID)
                .child("assignments").child(assignedKey).child("st_list").child(Util.getUserId(context)).child("report").setValue(report);

        /** add reports in assigned content info in batch node */
        if(assignmentKey ==null) global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(assignedKey)
                .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);

        else global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(assignmentKey).child(assignmentName).child(assignedKey)
                .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);


//        saveData();
    }

    private void saveData() {

        if(from.equals("search")) return;

        time = videoView.getCurrentPosition();

        if(Util.getSubjectId(context)==null) Util.setSubjectId(context,Util.getSubject(context));

        String category_name=getIntent().getStringExtra("category_name");
        String type="video_lessons";
        String subject=getIntent().getStringExtra("subject");
        String topicID=getIntent().getStringExtra("topicID");
        String videoID_ForReports=getIntent().getStringExtra("videoID_ForReports");

        if(videoID_ForReports==null) return;

//        String ddf=watchTime+"000";
//
//        watchTime=Integer.parseInt(ddf);

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(watchTime + "");
        scoreModel.setTotalTime(totalDuration + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setSeektime(watchTime);
        scoreModel.setDate("" + date);
        scoreModel.setSubjectName(subject);

        if(type.equals("diksha_content")) {
            category_name="Diksha Videos";
        }
        else {
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

        row_usage.put("time_spent", watchTime + "");
        row_usage.put("subject", Util.getSubject(context).replace(" ","_").toLowerCase());


        row_usage.put("subject_name", Util.getSubjectInfo(Util.getSubjectId(context)).getName());
        row_usage.put("language", Util.getSelectedLanguage(context));
        row_usage.put("topic", "" + topicID);
        row_usage.put("userId", Util.getUserId(context));
        row_usage.put("username", Util.getUsernameShowable(context));
        // row_usage.put("userIdFirebase", Util.getLoginUserId(context));

        row_usage.put("board",Util.getSelectedBoard(context));
        row_usage.put("category_name",category_name);
        row_usage.put("userType", "students");
        String board=Util.getSelectedBoard(context);
        String sClass=Util.getSelectedClass(context);

        Long time= timeget + Long.valueOf(watchTime);

        ///

        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(time);

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
            reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, subject, time,Util.getSelectedLanguage(context));
        }else{
            ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
            reportsTimeSpentModel.setUserId(Util.getUserId(context));
            reportsTimeSpentModel.setBoard(board);
            reportsTimeSpentModel.setSClass(sClass);
            reportsTimeSpentModel.setSubject(subject);
            reportsTimeSpentModel.setDate(date);
            reportsTimeSpentModel.setTime(time);
            reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
            reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
        }

//        For Count
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

//        For Date Wise
        if(reportsDateWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "video_lessons", date, System.currentTimeMillis())){
            reportsDateWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "video_lessons",
                    date, System.currentTimeMillis(), time + "", Util.getTopicNameAlt(context), watchTime + "", videoName);
        }else {
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
            reportsDateWiseVideoModel.setVTime(time + "");
            reportsDateWiseVideoModel.setTotalTime(watchTime + "");
            reportsDateWiseVideoRepository.insertVideoDetails(reportsDateWiseVideoModel);
        }

        //For Topic Wise
        if(reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubjectInfo(Util.getSubjectId(context)).getName(), "video_lessons", topicID, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
            reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubjectInfo(Util.getSubjectId(context)).getName(), "video_lessons", topicID,
                    date, System.currentTimeMillis(), Util.getTopicNameAlt(context), time + "", Util.getTopicNameAlt(context), watchTime + "", videoName,Util.getSelectedLanguage(context));
        }else{
            ReportsTopicWiseVideoModel reportsTopicWiseVideoModel = new ReportsTopicWiseVideoModel();
            reportsTopicWiseVideoModel.setUserId(Util.getUserId(context));
            reportsTopicWiseVideoModel.setBoard(board);
            reportsTopicWiseVideoModel.setSClass(sClass);
            reportsTopicWiseVideoModel.setSubject(Util.getSubjectInfo(Util.getSubjectId(context)).getName());
            reportsTopicWiseVideoModel.setDate(date);
            reportsTopicWiseVideoModel.setName(Util.getTopicNameAlt(context));
            reportsTopicWiseVideoModel.setTime(System.currentTimeMillis());
            reportsTopicWiseVideoModel.setType("video_lessons");
            reportsTopicWiseVideoModel.setTopicId(topicID);
            reportsTopicWiseVideoModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTopicWiseVideoModel.setTotalTime(watchTime + "");
            reportsTopicWiseVideoModel.setVTime(time + "");
            reportsTopicWiseVideoModel.setVideoName(videoName);
            reportsTopicWiseVideoModel.setVideoId(videoID_ForReports);
            reportsTopicWiseVideoModel.setLang(Util.getSelectedLanguage(context));
            reportsTopicWiseVideoModel.setSubjectName(Util.getSubjectInfo(Util.getSubjectId(context)).getName());
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

//
//        ///
//
//        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).replace(" ","-").toLowerCase()).setValue(time);
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("video_lessons").child(Util.getSubjectName(context)).child(date).child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);
//
//        if (type.equals("diksha_content")) {
//
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("diksha_content").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));
//
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("diksha_content").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);
//        }
//        else {
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("video_lessons").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));
//
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("video_lessons").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);
//        }
//
//
//
//        if(currentvideo_url != null && Util.isOfflineMode(context) && isLocalFile){
//            String name = videoID_ForReports;
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("video_lessons").child(topicID).child("detail").child(name).setValue(scoreModel);
//        }else{
//            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("video_lessons").child(topicID).child("detail").child(videoID_ForReports).setValue(scoreModel);
//
//        }

//        time = 0;

        timeget = 0;

    }
    private long timeget,videoNumber;
    private void getTime() {
        String board=Util.getSelectedBoard(context);
        String sClass=Util.getSelectedClass(context);
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubjectName(context), date, "video_lessons", "timeTask");
        }else  {
            try {
                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child("time_spent").child(date).child(Util.getSubjectName(context).replace(" ","-").toLowerCase()).addValueEventListener(new ValueEventListener() {
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
                            timeget = 0;
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    @Override
    protected void onPause() {
        if(videoView!=null)
            if (videoView.isPlaying()) {
                videoView.pause();
                watchTime=String.valueOf(videoView.getCurrentPosition());
            }
            else try {
                watchTime=String.valueOf(tracker.getVideoDuration());
            }catch (NullPointerException n) {
                System.out.println("-------- tracker null ");
                watchTime="0";
                n.printStackTrace();
            }


        Util.preventPause(context,getTaskId());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        saveAssignedReport();
        try {
            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
        destroyService();
        try
        {
            videoView.clearFocus();
            videoView.stopPlayback();
            videoView.suspend();
        }catch (Exception r){r.printStackTrace();}
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoView != null) {
            videoView.stopPlayback();
        }
        destroyService();
        super.onBackPressed();
    }

//    @SuppressLint("NewApi")
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        int currentApiVersion = Build.VERSION.SDK_INT;
//        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }
}
