package com.idreameducation.ipreppal.pal.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoDetailsRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenHelper;

import com.idreameducation.ipreppal.util.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

public class PalAssignedVideoPlayerActivity extends AppCompatActivity {
    private Context context;
    private PalVideoView videoView;
    private int time = 0;
    private ImageView imageViewCross;
    private ImageView imageViewFullScreen;
    private ImageView imageViewBackButton;
    private RelativeLayout reletive;
    private boolean isFullScreenModeOn;
    private ProgressBar mProgressBar;
    private TextView videoNameTextView;
    private RelativeLayout linearLayoutBottom;
    private String type;
    private Global global;
    private String board;
    private String sClass;
    private String subject;
    private String topicID;
    private String url;
    private VideoDetailsRepository videoDetailsRepository;

    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer ytubePlayer;
    private String vType;

    private Disposable timeTaskDisposable;
    private Disposable videoCountTaskDisposable;

    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;

    final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    mProgressBar.setVisibility(View.GONE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    mProgressBar.setVisibility(View.VISIBLE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    mProgressBar.setVisibility(View.GONE);
                    return true;
                }
            }
            return false;
        }
    };

    public PalAssignedVideoPlayerActivity() {

    }

    private void listners() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_assigned_video_player);
        assignIds(savedInstanceState);
        listners();
    }

    private void assignIds(Bundle savedInstanceState) {
        context = this;
        global = (Global) context.getApplicationContext();
        board = Util.getSelectedBoard(context);//((PalContentListingActivity) context).board;
        sClass = Util.getSelectedClass(context); //((PalContentListingActivity) context).sClass;

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullScreenHelper = new FullScreenHelper(PalAssignedVideoPlayerActivity.this);
        if (savedInstanceState != null) {
            time = savedInstanceState.getInt("time");
        } else {
            time = 0;
        }

        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);

        videoDetailsRepository = new VideoDetailsRepository(context);
        imageViewFullScreen = findViewById(R.id.imageViewFullScreen);
        linearLayoutBottom = findViewById(R.id.linearLayoutBottom);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        imageViewBackButton = findViewById(R.id.imageViewBackButton);
        url = getIntent().getStringExtra("url");
        videoName = getIntent().getStringExtra("videoName");
        topicID = getIntent().getStringExtra("topicID");
        subject = getIntent().getStringExtra("subject");
        videoNameTextView.setText(videoName);
        linearLayoutBottom.setVisibility(View.GONE);
        isAssigned = getIntent().getStringExtra("isAssigned");
        batchId = getIntent().getStringExtra("batchId");
        keyTo = getIntent().getStringExtra("key");
        datetostore = getIntent().getStringExtra("date");
        teacherID = getIntent().getStringExtra("teacherID");

        videoView = findViewById(R.id.videoView);
        reletive = findViewById(R.id.reletive);
        imageViewCross = findViewById(R.id.imageViewCross);
        mProgressBar = findViewById(R.id.progressBar);
        imageViewFullScreen.setVisibility(View.GONE);


//        imageViewFullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isFullScreenModeOn) {
//                    isFullScreenModeOn = false;
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                        }
//                    }, 2000);
//                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
//                    param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    param.setMargins(5, 5, 5, 5);
//                    videoView.setLayoutParams(param);
//                } else {
//                    isFullScreenModeOn = true;
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                        }
//                    }, 2000);
//                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    param.setMargins(5, 5, 5, 5);
//                    videoView.setLayoutParams(param);
//                }
//            }
//        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                
            }
        });
//        imageViewCross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                
//            }
//        });
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(onInfoToPlayStateListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            getVideoCount();
            getTime();
            initializePlayer(url, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer(String vid_id, int time) throws Exception {
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(vid_id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                Map<String, String> aa = video.getStreams();
                String hdStream = aa.get("360p");
                if (hdStream != null) {
                    playVideo(hdStream, time);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("Error: ", throwable.getLocalizedMessage());
            }
        });
    }

    private int totalTime;

    private void playVideo(final String stream, int time) {
        mProgressBar.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {

                try {
                    final Uri video = Uri.parse(stream);
                    MediaController mediaController = new MediaController(context);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(video);

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            totalTime = mp.getDuration();
                            videoView.start();

                            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                    mediaController.setAnchorView(videoView);
                                    ((ViewGroup) mediaController.getParent()).removeView(mediaController);
                                    ((FrameLayout) findViewById(R.id.videoViewWrapper))
                                            .addView(mediaController);
                                }
                            });
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            finish();
                        }
                    });
                    videoView.setPlayPauseListener(new PalVideoView.PlayPauseListener() {
                        @Override
                        public void onPlay() {
                            linearLayoutBottom.setVisibility(View.GONE);
                            mediaController.setVisibility(View.GONE);
                        }
                        @Override
                        public void onPause() {
                        }
                    });
                    videoView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            if (mediaController != null) {
                                linearLayoutBottom.setVisibility(View.VISIBLE);
                                mediaController.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayoutBottom.setVisibility(View.GONE);
                                        mediaController.setVisibility(View.GONE);
                                    }
                                }, 3000);
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

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
        saveData();
    }


    private String videoName;
    private final long startTime = 0L;
    private long endTime = 0L;

    private void saveData() {
        time = videoView.getCurrentPosition();

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(time + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;

//        VideoModel videoModel = new VideoModel();
//        videoModel.setUserId(Util.getUserId(context));
//        videoModel.setBoard(board);
//        videoModel.setSClass(sClass);
//        videoModel.setSubject(subject);
//        videoModel.setTopicId(topicID);
//        videoModel.setVideoName(videoName);
//        videoDetailsRepository.insertVideoDetails(videoModel);

//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(subject).setValue(timeTosync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("video").setValue(videoNumber);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child("video").child(subject).child(date).child("" + System.currentTimeMillis()).setValue(scoreModel);
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(date).child(topicID).child("detail").child("" + System.currentTimeMillis()).setValue(scoreModel);
//
//
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(topicID).child("detail").child(url).setValue(scoreModel);
//


        double percetage = ((double)time / (double)totalTime) * 100;

        int per = (int)percetage;
        // will only work if the content is assigned
        if (isAssigned != null) {
            global.getDatabaseReference().child("content_assignement_batch_wise").child(teacherID).child(batchId).child(datetostore).child(keyTo).child("student").child(Util.getUserId(context)).child("progress").setValue("" + per);
        }

//        time = 0;
//        totalTime = 0;
//        timeget = 0;


    }

    private String teacherID;
    private String batchId;
    private String isAssigned;
    private String datetostore;
    private String keyTo;

    private long timeget;

    private void getTime() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "video_lessons", "timeTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(subject).addValueEventListener(new ValueEventListener() {
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

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "video_lessons", "videoCountTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child("count").child(date).child("video_lessons").addValueEventListener(new ValueEventListener() {
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



    public void playVideo(String url, String videoName_, String topicID_) throws Exception {
        try {
            saveData();
            videoName = videoName_;
            topicID = topicID_;
            getVideoCount();
            getTime();
            initializePlayer(url, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initYouTubePlayerView() {
        // initPlayerMenu();
        String code = getYouTubeId(url);

        mProgressBar.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onApiChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onApiChange(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
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
                ytubePlayer = youTubePlayer;
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        code,
                        0f
                );
                addFullScreenListenerToPlayer();

            }

            @Override
            public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                switch (state) {
                    case UNSTARTED:

                        break;

                    case ENDED:
                        break;

                }

            }

            @Override
            public void onVideoDuration(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                try {
                    //  Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
                    Log.i("Total duration :", duration + "");
                    totalTime = (int) duration;
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

    }


    private void addFullScreenListenerToPlayer() {
//        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
//            @Override
//            public void onYouTubePlayerEnterFullScreen() {
//                // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //  fullScreenHelper.enterFullScreen();
//                ((PalContentListingActivity) context).fullScreen();
//
//                // addCustomActionsToPlayer();
//            }
//
//            @Override
//            public void onYouTubePlayerExitFullScreen() {
//                //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                // fullScreenHelper.exitFullScreen();
//                ((PalContentListingActivity) context).smallScreen(true);
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
            return "error";
        }
    }


}
