package com.idreameducation.ipreppal.pal.videocontroller;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.FragmentActivity;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.ActivityVideosListingActivity;
import com.idreameducation.ipreppal.pal.activity.StemProjectsListingActivity;

public class ActivityVideosFullScreenController extends MediaController {

    private ImageView actionButton;
    private boolean isFullScreen;
    private VideoView videoView;
    private Context context;
    private FragmentActivity activity;
    private Uri uri;
    private String videoName;
    private String topicId;

    public ActivityVideosFullScreenController(Context context, Uri uri, boolean isFullScreen, VideoView videoView, FragmentActivity activity,
                                              String videoName, String topicId) {
        super(context);
        this.context = context;
        this.isFullScreen = isFullScreen;
        this.videoView = videoView;
        this.activity = activity;
        this.uri = uri;
        this.videoName = videoName;
        this.topicId = topicId;
    }

    public ActivityVideosFullScreenController(Context context, Uri uri, boolean isFullScreen, VideoView videoView,
                                              String videoName, String topicId) {
        super(context);
        this.context = context;
        this.isFullScreen = isFullScreen;
        this.videoView = videoView;
        this.activity = activity;
        this.uri = uri;
        this.videoName = videoName;
        this.topicId = topicId;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        //image button for full screen to be added to media controller
        actionButton = new ImageView(super.getContext());
        LayoutParams params = new LayoutParams(60, 60);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;
        params.topMargin = 10;
        addView(actionButton, params);
        actionButton.setImageResource(R.mipmap.full_screen);
        actionButton.setTag("fullscreen");
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionButton.getTag() == "cross"){
                    actionButton.setImageResource(R.mipmap.full_screen);
                    actionButton.setTag("fullscreen");
                    try {
                        ((ActivityVideosListingActivity) context).backToNormalView();
                    } catch (Exception e) {
                        ((StemProjectsListingActivity) context).backToNormalView();
                        e.printStackTrace();
                    }
                }else if(actionButton.getTag() == "fullscreen"){
                    actionButton.setImageResource(R.mipmap.full_screen);
                    actionButton.setTag("cross");
                    try {
                        ((ActivityVideosListingActivity) context).openVimeoVideoFullScreenFragment();
                    } catch (Exception e) {
                        ((StemProjectsListingActivity) context).openVimeoVideoFullScreenFragment();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
