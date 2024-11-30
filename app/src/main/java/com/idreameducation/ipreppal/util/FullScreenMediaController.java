package com.idreameducation.ipreppal.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;

public class FullScreenMediaController extends MediaController {

    private ImageView fullScreen;
    private String isFullScreen;
    private Context context;
    private String type;

    public FullScreenMediaController(Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        //image button for full screen to be added to media controller
        if(type == null || !type.equals("foundationalTopicVideos")){
            try {
                fullScreen = new ImageView(super.getContext());

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(40, 40);
                params.gravity = Gravity.RIGHT;
                params.rightMargin = 30;
                params.topMargin = 20;
                addView(fullScreen, params);
                fullScreen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                try {
                    Glide.with(context)
                            .load(R.mipmap.full_screen)
                            .into(fullScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }



                //add listener to image button to handle full screen and exit full screen events
                fullScreen.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(context instanceof PalContentListingActivity){
                            if (((PalContentListingActivity) context).isFullScreen) {
                                ((PalContentListingActivity) context).smallScreen(true);
                            } else {
                                ((PalContentListingActivity) context).fullScreen();
                            }
                        }else if(context instanceof ExtraContentListingActivity){
                            if (((ExtraContentListingActivity) context).isFullScreen) {
                                ((ExtraContentListingActivity) context).smallScreen(true);
                            } else {
                                ((ExtraContentListingActivity) context).fullScreen();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
