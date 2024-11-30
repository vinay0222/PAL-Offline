package com.idreameducation.ipreppal.pal.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
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
import com.idreameducation.ipreppal.services.SocketService;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenHelper;
import com.idreameducation.ipreppal.util.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.HashMap;
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

public class PalVideoPlayerActivity extends Fragment {
    private Context context;
    private VideoView videoView;
    private int time = 0;
    private ImageView imageViewCross;
    private ImageView imageViewFullScreen;
    private ImageView imageViewBackButton;
    private RelativeLayout reletive;
    private boolean isFullScreenModeOn;
    private ProgressBar mProgressBar;
    private String type;
    private Global global;
    private String board;
    private String sClass;
    private String subject;
    private String topicID,videoID_ForReports;
    private String url;
    private String icon;
    private String language;
    private TextView textViewNextVideo;
    private TextView textViewPractice;
    private RelativeLayout linearLayoutBottom;
    private TextView videoNameTextView;
    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer ytubePlayer;
    private String vType;
    private Intent serviceIntent;
    private boolean isLocalFile;
    private VideoDetailsRepository videoDetailsRepository;
    private boolean isFullScreen;
    private ReportsLatestDataVideoRepository reportsLatestDataVideoRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWiseVideoRepository reportsDateWiseVideoRepository;
    private ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;
    private Disposable timeTaskDisposable;
    private Disposable videoCountTaskDisposable;

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

    public PalVideoPlayerActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pal_video_player, container, false);
        Util.setContext(context);
        assignIds(view, savedInstanceState);
        listners();
        return view;
    }

    private void listners() {
        textViewNextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        ((PalContentListingActivity) context).removeFragment(true);
                        ((PalContentListingActivity) context).openVideoView(PalContentListingActivity.videoKey);
                    }
                }
            }
        });


        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type != null && type.equals("foundationalTopicVideos")){
                    ((ExtraContentListingActivity) context).removeFragment(true);
                    ((ExtraContentListingActivity) context).getPracticeContent();
                }else{
                    ((PalContentListingActivity) context).removeFragment(true);
                    ((PalContentListingActivity) context).startPractice();
//                    ((PalContentListingActivity) context).viewPager.setCurrentItem(1);
                }
            }
        });

    }


    private void assignIds(View view, Bundle savedInstanceState) {
        context = getActivity();
        global = (Global) context.getApplicationContext();
        type = getArguments().getString("type");
        if(type != null && type.equals("foundationalTopicVideos")){
            board = ((ExtraContentListingActivity) context).board;
            sClass = ExtraContentListingActivity.sClass;
            subject = ExtraContentListingActivity.subject;
            language = ((ExtraContentListingActivity) context).language;
            icon = ExtraContentListingActivity.icon;
        }else{
            board = PalContentListingActivity.board;
            sClass = PalContentListingActivity.sClass;
            subject = PalContentListingActivity.subject;
            language = ((PalContentListingActivity) context).language;
            icon = PalContentListingActivity.icon;
        }
        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseVideoRepository = new ReportsDateWiseVideoRepository(context);
        reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        fullScreenHelper = new FullScreenHelper(getActivity());
        linearLayoutBottom = view.findViewById(R.id.linearLayoutBottom);
        videoNameTextView = view.findViewById(R.id.videoNameTextView);
        if (savedInstanceState != null) {
            time = savedInstanceState.getInt("time");
        } else {
            time = 0;
        }

        videoDetailsRepository = new VideoDetailsRepository(context);
        imageViewFullScreen = view.findViewById(R.id.imageViewFullScreen);
        imageViewBackButton = view.findViewById(R.id.imageViewBackButton);
        //type = getIntent().getStringExtra("type");
        url = getArguments().getString("url");
        isLocalFile = getArguments().getBoolean("isLocalFile");
        videoName = getArguments().getString("videoName");
        subjectName = getArguments().getString("subjectName");
        vType = getArguments().getString("vType");
        topicID = getArguments().getString("topicID");
        videoID_ForReports = getArguments().getString("videoid_for_reports");
        isFullScreen = getArguments().getBoolean("isFullScreen");

        videoNameTextView.setText(videoName);
        textViewPractice = view.findViewById(R.id.textViewPractice);
//        if(type != null && type.equals("foundationalTopicVideos")){
//            textViewPractice.setVisibility(View.GONE);
//        }else{
//            textViewPractice.setVisibility(View.VISIBLE);
//        }
        textViewNextVideo = view.findViewById(R.id.textViewNextVideo);
        videoView = view.findViewById(R.id.videoView);
        reletive = view.findViewById(R.id.reletive);
        imageViewCross = view.findViewById(R.id.imageViewCross);
        mProgressBar = view.findViewById(R.id.progressBar);
        imageViewFullScreen.setVisibility(View.GONE);
        linearLayoutBottom.setVisibility(View.GONE);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
//                getActivity().
                if(type != null && type.equals("foundationalTopicVideos")){
                    ((ExtraContentListingActivity) context).removeFragment(true);
                }else{
                    if(PalContentListingActivity.isFullScreen){
                        ((PalContentListingActivity) context).smallScreen(true);
                    }
                    ((PalContentListingActivity) context).removeFragment(true);
                }
            }
        });
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
            if(type != null && type.equals("diksha_content")){
                playVideo(url, time, false, view);
            }else{
                if(url != null){
                    if(isLocalFile){
                        playVideo(url, time, true, view);
                    }else{
                        initializePlayer(url, time, view);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer(String vid_id, int time, View view) throws Exception {
            VimeoExtractor.getInstance().fetchVideoWithIdentifier(vid_id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                Map<String, String> aa = video.getStreams();
                String hdStream = aa.get("360p");
                if (hdStream != null) {
                    playVideo(hdStream, time, false, view);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("Error: ", throwable.getLocalizedMessage());
                //For Diksha
//                playVideo(vid_id, time, false, view);
            }
        });
    }

    private int totalTime;

    private void playVideo(final String stream, int time, boolean isLocalFile,View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {

                try {
                    final Uri video = Uri.parse(stream);
//                    MediaController mediaController = new FullScreenMediaController(context, type);
                    MediaController mediaController = new MediaController(context);

                    videoView.setVideoURI(video);
                    videoView.setMediaController(mediaController);
//                    mediaController.setVisibility(View.GONE);
                    if(isLocalFile){
                        serviceIntent = new Intent(context, SocketService.class);
                        context.startService(serviceIntent);
                        String path = "http://localhost:7453" + stream;
                        videoView.setVideoPath(path);
                    }else{
                        videoView.setVideoURI(video);
                    }

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            totalTime = mp.getDuration();
                            videoView.start();
                            startTime = System.currentTimeMillis();
                            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                    mediaController.setAnchorView(videoView);
                                    ((ViewGroup) mediaController.getParent()).removeView(mediaController);
                                    ((FrameLayout) view.findViewById(R.id.videoViewWrapper))
                                            .addView(mediaController);
                                }
                            });
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
//                            PalContentListingActivity.instance.setPath(board, sClass, Util.getSubject(context), topicID, "V");
//                            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicID).child("video_lessons").setValue("V");
//                            saveData();
                            linearLayoutBottom.setVisibility(View.VISIBLE);
                            mediaController.setVisibility(View.VISIBLE);
                            destroyService();
//
//                            Intent intent = new Intent();
//                            getActivity().setResult(123, intent);
//                            getActivity().finish();
//                            getActivity().

                        }
                    });
//                    videoView.setPlayPauseListener(new PalVideoView.PlayPauseListener() {
//                        @Override
//                        public void onPlay() {
//                            linearLayoutBottom.setVisibility(View.GONE);
//                            mediaController.setVisibility(View.GONE);
//                        }
//                        @Override
//                        public void onPause() {
//                        }
//                    });
                    videoView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            if (mediaController != null) {
                                linearLayoutBottom.setVisibility(View.VISIBLE);
                                mediaController.setVisibility(View.VISIBLE);
                                setMargins(reletive,0,0,0,60);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayoutBottom.setVisibility(View.GONE);
                                        mediaController.setVisibility(View.GONE);
                                        setMargins(reletive,0,0,0,0);

                                    }
                                }, 6000);
                            }
                            return true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Util.setContext(context);
    }

    @Override
    public void onPause() {

        super.onPause();
//        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        saveData();
        destroyService();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private String videoName,subjectName;
    private long startTime = 0L;
    private long endTime = 0L;

    private void saveData() {
        time = videoView.getCurrentPosition();

        String date = Util.getCurrentDateWithDifferentFormat();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setTime(time + "");
        scoreModel.setTotalTime(totalTime + "");
        scoreModel.setVideoName(videoName);
        scoreModel.setDate(""+date);
        scoreModel.setSubjectName(subject);
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        videoNumber = videoNumber + 1;
        timeTosync = timeTosync + timeget;


        HashMap<String, String> row_usage = new HashMap<>();
        row_usage.put("category","video_lessons");
        row_usage.put("class",Util.getSelectedClass(context));
        row_usage.put("content_name",videoName.trim());
        row_usage.put("topic_name",Util.getTopicNameAlt(context).trim());
        row_usage.put("district",Util.getDistrict(context));
        row_usage.put("timePlayed",""+timeTosync);
        row_usage.put("schoolID",Util.getSchoolId(context));
        row_usage.put("schoolName",Util.getSchoolName(context));
        row_usage.put("projectId",Util.getProjectId(context));
        row_usage.put("score",time + "");
        row_usage.put("state",Util.getSelectedState(context));
        row_usage.put("videoID_ForReports",videoID_ForReports);
        row_usage.put("subject",Util.getSubject(context));
        row_usage.put("subject_name",Util.getSubjectName(context));
        row_usage.put("language",Util.getSelectedLanguage(context));
        row_usage.put("time_spent",timeTosync + "");
        row_usage.put("topic",""+topicID);
        row_usage.put("userId",Util.getUserId(context));
        row_usage.put("username",Util.getUsernameShowable(context));
        row_usage.put("userIdFirebase",Util.getLoginUserId(context));
        row_usage.put("userType", "students");
        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(timeTosync);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("video_lessons").setValue(videoNumber);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("video_lessons").child(Util.getSubjectName(context)).child(date).child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child("video_lessons").child(date).child(topicID).child("name").setValue(Util.getTopicNameAlt(context));
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child("video_lessons").child(date).child(topicID).child("detail").child(videoID_ForReports).child("" + System.currentTimeMillis()).setValue(scoreModel);

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
                    date, System.currentTimeMillis(), time + "", Util.getTopicNameAlt(context), totalTime + "", videoName);
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
            reportsDateWiseVideoModel.setVTime(time + "");
            reportsDateWiseVideoModel.setTotalTime(totalTime + "");
            reportsDateWiseVideoRepository.insertVideoDetails(reportsDateWiseVideoModel);
        }

        //For Topic Wise
        if(reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "video_lessons", topicID, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
            reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, sClass, subject, "video_lessons", topicID,
                    date, System.currentTimeMillis(), Util.getTopicNameAlt(context), time + "", Util.getTopicNameAlt(context), totalTime + "", videoName,Util.getSelectedLanguage(context));
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
            reportsTopicWiseVideoModel.setVTime(time + "");
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
            videoDetailsRepository.insertVideoDetails(videoModel);
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            ((PalContentListingActivity)context).getTopicSeenVideoListing(true);
        }, 1000);

        if(url != null && Util.isOfflineMode(context) && isLocalFile){
            String filename = url.substring(url.lastIndexOf("/") + 1);
//            String name = filename.substring(0, filename.lastIndexOf("."));
            String name = videoID_ForReports;
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video_lessons").child(topicID).child("detail").child(name).setValue(scoreModel);
            if(reportsLatestDataVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicID,Util.getSelectedLanguage(context))){
                reportsLatestDataVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicID, name, null, time + "", Util.getTopicNameAlt(context),
                        totalTime + "", videoName,Util.getSelectedLanguage(context));
            }else{
                ReportsLatestDataVideoModel reportsLatestDataVideoModel = new ReportsLatestDataVideoModel();
                reportsLatestDataVideoModel.setUserId(Util.getUserId(context));
                reportsLatestDataVideoModel.setBoard(board);
                reportsLatestDataVideoModel.setSClass(sClass);
                reportsLatestDataVideoModel.setSubject(Util.getSubject(context));
                reportsLatestDataVideoModel.setType("video_lessons");
                reportsLatestDataVideoModel.setTopicId(topicID);
                reportsLatestDataVideoModel.setVId(name);
                reportsLatestDataVideoModel.setUrl(null);
                reportsLatestDataVideoModel.setVTime(time + "");
                reportsLatestDataVideoModel.setTopicName(Util.getTopicNameAlt(context));
                reportsLatestDataVideoModel.setTotalTime(totalTime + "");
                reportsLatestDataVideoModel.setVideoName(videoName);
                reportsLatestDataVideoModel.setLang(Util.getSelectedLanguage(context));
                reportsLatestDataVideoRepository.insertVideoDetails(reportsLatestDataVideoModel);
            }
        }else{
            if(type != null && !type.equals("diksha_content")){
                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video_lessons").child(topicID).child("detail").child(url).setValue(scoreModel);
                if(reportsLatestDataVideoRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicID,Util.getSelectedLanguage(context))){
                    reportsLatestDataVideoRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicID, null, url, time + "", Util.getTopicNameAlt(context),
                            totalTime + "", videoName,Util.getSelectedLanguage(context));
                }else{
                    ReportsLatestDataVideoModel reportsLatestDataVideoModel = new ReportsLatestDataVideoModel();
                    reportsLatestDataVideoModel.setUserId(Util.getUserId(context));
                    reportsLatestDataVideoModel.setBoard(board);
                    reportsLatestDataVideoModel.setSClass(sClass);
                    reportsLatestDataVideoModel.setSubject(Util.getSubject(context));
                    reportsLatestDataVideoModel.setType("video_lessons");
                    reportsLatestDataVideoModel.setTopicId(topicID);
                    reportsLatestDataVideoModel.setVId(null);
                    reportsLatestDataVideoModel.setUrl(url);
                    reportsLatestDataVideoModel.setVTime(time + "");
                    reportsLatestDataVideoModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsLatestDataVideoModel.setTotalTime(totalTime + "");
                    reportsLatestDataVideoModel.setVideoName(videoName);
                    reportsLatestDataVideoModel.setLang(Util.getSelectedLanguage(context));
                    reportsLatestDataVideoRepository.insertVideoDetails(reportsLatestDataVideoModel);
                }
            }
        }

        time = 0;
        totalTime = 0;
        timeget = 0;


    }

    private long timeget;

    private void getTime() {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "video_lessons", "timeTask");
        }else {
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

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyService();
    }

    private void destroyService(){
        if(serviceIntent != null){
            context.stopService(serviceIntent);
        }
    }


    public void playVideo(String url, String videoName_, String topicID_) throws Exception {
        try {
            saveData();
            videoName = videoName_;
            topicID = topicID_;
            getVideoCount();
            getTime();
//            initializePlayer(url, time);
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
