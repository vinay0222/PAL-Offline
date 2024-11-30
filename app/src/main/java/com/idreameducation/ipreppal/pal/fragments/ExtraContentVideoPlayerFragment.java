package com.idreameducation.ipreppal.pal.fragments;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalVideoView;
import com.idreameducation.ipreppal.services.SocketService;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenHelper;
import com.idreameducation.ipreppal.util.FullScreenMediaController;
import com.idreameducation.ipreppal.util.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

public class ExtraContentVideoPlayerFragment extends Fragment {
    private Context context;
    private PalVideoView videoView;
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
    private String topicID;
    private String url;
    private String icon;
    private String language;
    private TextView textViewNextVideo;
    private TextView textViewPractice;
    private RelativeLayout linearLayoutBottom;
    private YouTubePlayerView youTubePlayerView;
    private TextView videoNameTextView;
    private FullScreenHelper fullScreenHelper;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer ytubePlayer;
    private String vType;
    private Intent serviceIntent;

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

    public ExtraContentVideoPlayerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pal_video_player, container, false);
        assignIds(view, savedInstanceState);
        listners();
        return view;
    }

    private void listners() {
        textViewPractice.findViewById(R.id.textViewPractice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PalContentListingActivity) context).viewPager.setCurrentItem(1);
            }
        });

    }


    private void assignIds(View view, Bundle savedInstanceState) {
        context = getActivity();
        global = (Global) context.getApplicationContext();
        board = ((ExtraContentListingActivity) context).board;
        sClass = ExtraContentListingActivity.sClass;
        subject = ExtraContentListingActivity.subject;
        language = ((ExtraContentListingActivity) context).language;
        icon = ExtraContentListingActivity.icon;
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        fullScreenHelper = new FullScreenHelper(getActivity());
        linearLayoutBottom = view.findViewById(R.id.linearLayoutBottom);
        videoNameTextView = view.findViewById(R.id.videoNameTextView);
        if (savedInstanceState != null) {
            time = savedInstanceState.getInt("time");
        } else {
            time = 0;
        }

        imageViewFullScreen = view.findViewById(R.id.imageViewFullScreen);
        imageViewBackButton = view.findViewById(R.id.imageViewBackButton);
        //type = getIntent().getStringExtra("type");
        url = getArguments().getString("url");

        videoName = getArguments().getString("videoName");
        vType = getArguments().getString("vType");
        videoNameTextView.setText(videoName);
        textViewPractice = view.findViewById(R.id.textViewPractice);
        textViewNextVideo = view.findViewById(R.id.textViewNextVideo);
        textViewPractice.setVisibility(View.GONE);
        textViewNextVideo.setVisibility(View.GONE);
        videoView = view.findViewById(R.id.videoView);
        reletive = view.findViewById(R.id.reletive);
        imageViewCross = view.findViewById(R.id.imageViewCross);
        mProgressBar = view.findViewById(R.id.progressBar);
        imageViewFullScreen.setVisibility(View.GONE);
        linearLayoutBottom.setVisibility(View.GONE);

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
//        imageViewCross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//                getActivity().
//            }
//        });
        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
//                getActivity().
                if(type != null && type.equals("foundationalTopicVideos")){
                    ((ExtraContentListingActivity) context).removeFragment(true);
                }else{
                    if(((ExtraContentListingActivity) context).isFullScreen){
                        ((ExtraContentListingActivity) context).smallScreen(true);
                    }
                    ((ExtraContentListingActivity) context).removeFragment(true);
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
            // getVideoCount();
            //getTime();

            if (vType.equalsIgnoreCase("youtube")) {
                initYouTubePlayerView();
            } else if(vType.equalsIgnoreCase("local")){
                playVideo(url, time, true, view);
            } else {
                topicID = getArguments().getString("topicID");
                initializePlayer(url, time, view);
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
            }
        });
    }

    private int totalTime;

    private void playVideo(final String stream, int time, boolean isLocalFile, View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {

                try {
                    final Uri video = Uri.parse(stream);
                    FullScreenMediaController mediaController = new FullScreenMediaController(context, null);
                    videoView.setMediaController(mediaController);
                    mediaController.setVisibility(View.GONE);
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
                            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicID).child("video").setValue("V");
                            saveData();
                            linearLayoutBottom.setVisibility(View.VISIBLE);
                            ((ExtraContentListingActivity) context).removeFragment(false);
                            linearLayoutBottom.setVisibility(View.VISIBLE);
                            mediaController.setVisibility(View.VISIBLE);
                            destroyService();
//                            Intent intent = new Intent();
//                            getActivity().setResult(123, intent);
//                            getActivity().finish();
//                            getActivity().

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
    public void onStop() {
        super.onStop();
        saveData();
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

//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).setValue(timeTosync);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("stem").setValue(videoNumber);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child("stem").child(date).child("" + System.currentTimeMillis()).setValue(scoreModel);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child("stem").child("name").setValue(videoName);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child("stem").child("detail").child("" + System.currentTimeMillis()).setValue(scoreModel);
//        String code = getYouTubeId(url);
//        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child("stem").child("detail").child(code).setValue(scoreModel);

        time = 0;
        totalTime = 0;
        timeget = 0;
    }

    private long timeget;

    private void getTime() {
        String date = Util.getCurrentDateWithDifferentFormat();
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

    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child("count").child(date).child("video").addValueEventListener(new ValueEventListener() {
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
                        saveData();
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
                Log.i("Fraction: ", loadedFraction + "");
            }
        });

    }


    private void addFullScreenListenerToPlayer() {
//        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
//            @Override
//            public void onYouTubePlayerEnterFullScreen() {
//                // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //  fullScreenHelper.enterFullScreen();
//                ((ExtraContentListingActivity) context).fullScreen();
//
//                // addCustomActionsToPlayer();
//            }
//
//            @Override
//            public void onYouTubePlayerExitFullScreen() {
//                //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                // fullScreenHelper.exitFullScreen();
//                ((ExtraContentListingActivity) context).smallScreen(true);
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
