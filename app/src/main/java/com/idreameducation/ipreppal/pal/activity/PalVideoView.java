package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class PalVideoView extends VideoView {
    public interface PlayPauseListener {
        void onPlay();
        void onPause();
    }

    private PlayPauseListener mListener;

    public PalVideoView(Context context) {
        super(context);
    }

    public PalVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PalVideoView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    @Override
    public void pause() {
        super.pause();
        if(mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if(mListener != null) {
            mListener.onPlay();
        }
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

}