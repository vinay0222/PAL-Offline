package com.idreameducation.ipreppal.videoPlayer;

import static com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.current_duration;
import static com.idreameducation.ipreppal.pal.activity.VideoView_Activity.goto_smallscreen;
import static com.idreameducation.ipreppal.pal.activity.VideoView_Activity.videoView_activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.ActivityVideosListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.activity.StemProjectsListingActivity;
import com.idreameducation.ipreppal.pal.activity.VideoView_Activity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class IPrepFullScreenMediaController extends MediaController {

    public static ImageView actionButton;
    public static ImageView settings_button;
    private final boolean isFullScreen;
    private final VideoView videoView;
    private final Context context;
    private final FragmentActivity activity;
    private final Uri uri;
    private final String videoName;
    private final String topicId;
    ArrayList<String> textArraylist=new ArrayList<>();
    Map<String, String> videoQuality_data;
    boolean isLocalFile;
    int time;
    public static float speed = 0.8f;
    int speedpos=0;
    boolean quality_change=false;
    String selectedQuality,selectedQuality_videolink;
    int c_pos=0;

    int iconSize=50;

    String type;

    boolean isSmallScreen=false;

    /** Use This Constructor for normal MediaController */
    public IPrepFullScreenMediaController(Context context, Uri uri, boolean isFullScreen, VideoView videoView, FragmentActivity activity, String videoName, String topicId) {
        super(context);
        this.context = context;
        this.isFullScreen = isFullScreen;
        this.videoView = videoView;
        this.activity = activity;
        this.uri = uri;
        this.videoName = videoName;
        this.topicId = topicId;
    }

    /** Use This Constructor for showing Video Settings option in MediaController
      -  if videoQuality_data is null then its don't show quality options  */
    public IPrepFullScreenMediaController(Context context, Uri uri, boolean isFullScreen, VideoView videoView, FragmentActivity activity, String videoName, String topicId, Map<String, String> videoQuality_data, boolean isLocalFile,int time,String type) {
        super(context);
        this.context = context;
        this.isFullScreen = isFullScreen;
        this.videoView = videoView;
        this.activity = activity;
        this.uri = uri;
        this.videoName = videoName;
        this.topicId = topicId;
        this.isLocalFile = isLocalFile;
        this.time = time;
        this.videoQuality_data = videoQuality_data;
        this.type=type;
        switch (Util.getVideoSpeed(context))
        {

            case "0.5x" : speed=0.5f; speedpos=0; break;
            case "0.75x" : speed=0.75f; speedpos=1; break;
            case "1x Normal Speed" : speed=1f; speedpos=2; break;
            case "1x सामान्य स्पीड" : speed=1f; speedpos=2; break;
            case "1.2x" : speed=1.25f; speedpos=3; break;
            case "2x" : speed=2f; speedpos=4; break;

            default:speed=1f;speedpos=2;

        }

    }

    /** Use This Constructor for Practice level videos
      -  if videoQuality_data is null then its don't show quality options  */
    public IPrepFullScreenMediaController(Context context,String type, Uri uri, boolean isFullScreen, VideoView videoView, FragmentActivity activity, String videoName, String topicId, Map<String, String> videoQuality_data, boolean isLocalFile,int time) {
        super(context);
        this.context = context;
        this.isFullScreen = isFullScreen;
        this.videoView = videoView;
        this.activity = activity;
        this.uri = uri;
        this.videoName = videoName;
        this.topicId = topicId;
        this.isLocalFile = isLocalFile;
        this.time = time;
        this.videoQuality_data = videoQuality_data;
        this.type=type;

        this.iconSize=36;

        isSmallScreen=true;

        switch (Util.getVideoSpeed(context)) {

            case "0.5x" : speed=0.5f; speedpos=0; break;
            case "0.75x" : speed=0.75f; speedpos=1; break;
            case "1x Normal Speed" : speed=1f; speedpos=2; break;
            case "1x सामान्य स्पीड" : speed=1f; speedpos=2; break;
            case "1.2x" : speed=1.25f; speedpos=3; break;
            case "2x" : speed=2f; speedpos=4; break;

            default:speed=1f;speedpos=1;

        }
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        getTextList();

        /** image button for full screen to be added to media controller */
        actionButton = new ImageView(super.getContext());
        LayoutParams params = new LayoutParams(28, 28);

        if(Util.isPortraitMode(context)) params = new LayoutParams(48, 48);  params.gravity = Gravity.RIGHT;

        if(Util.isPortraitMode(context)) params.rightMargin = 50; else {
          if(isSmallScreen) params.rightMargin = 30;
            else params.rightMargin = 120;
        }
        if(Util.isPortraitMode(context)) params.topMargin = 45;else params.topMargin = 30;
        addView(actionButton, params);
        if(!Util.getIsFullScreen(context)) actionButton.setImageResource(R.mipmap.video_zoom_in);
        else actionButton.setImageResource(R.mipmap.video_zoom_out);
        actionButton.setTag("fullscreen");


        /** Full screen text in mediacontroller */
        TextView text2 = new TextView(super.getContext());
        LayoutParams params4 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params4.gravity = Gravity.RIGHT; params4.rightMargin = 36; params4.topMargin = 29;
        text2.setTextColor(context.getResources().getColor(R.color.white));
        text2.setTextSize(8);
        text2.setLineSpacing(1,1);

//        /** Measure the TextView to get its width **/
//        text2.measure(0, 0);
//        int textWidth = text2.getMeasuredWidth();
//
//        Toast.makeText(context, textWidth, Toast.LENGTH_SHORT).show();
//
//        /** Adjust the position of the ImageView based on text size **/
//        if (Util.isPortraitMode(context)) params.rightMargin = 50 + textWidth;
//        else params.rightMargin = 50 + textWidth;

//        addView(text2, params4);

        if(Util.getSelectedLanguage(context).equals("hindi")) if(!Util.getIsFullScreen(context)) text2.setText("पूर्ण\nस्क्रीन"); else text2.setText("छोटी\nस्क्रीन");
        else if(!Util.getIsFullScreen(context)) text2.setText("Full\nScreen"); else text2.setText("Small\nScreen");


        /** image button for settings to be added to media controller */
        settings_button = new ImageView(super.getContext());
        LayoutParams params2 = new LayoutParams(26, 26);

        if(isSmallScreen)  params2 = new LayoutParams(iconSize, iconSize);

        if(Util.isPortraitMode(context)) params2 = new LayoutParams(48, 48);

        params2.gravity = Gravity.LEFT;
        if(type==null) params2.leftMargin = 80;
        else params2.leftMargin = 20;
        if(Util.isPortraitMode(context)) params2.topMargin = 40; else params2.topMargin = 26;
        addView(settings_button, params2);
        settings_button.setImageResource(R.drawable.ic_video_setting);



        /** VideoSetting text in mediacontroller */
        TextView text = new TextView(super.getContext());
        LayoutParams params3 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.gravity = Gravity.LEFT;
        if(type==null) params3.leftMargin = 135;
        else params3.leftMargin = 85;
        params3.topMargin = 30;
        if(Util.getSelectedLanguage(context).equals("hindi")) text.setText("वीडियो\nसेटिंग"); else text.setText("Video\nSetting");
        text.setTextColor(context.getResources().getColor(R.color.white));
        text.setTextSize(8);
        text.setLineSpacing(1,1);
//        addView(text, params3);


        /** click listener's */
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonClick();
            }
        });
        text2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonClick();
            }
        });
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });
        settings_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });


    }

    private void actionButtonClick() {

        if(type==null) {

            if(Util.isPortraitMode(context)) PalTopicListingActivity.palTopicListingActivity.rotate_screen();
            else
            {
                if(Util.getIsFullScreen(context)){
                    actionButton.setImageResource(R.mipmap.video_zoom_in);
                    actionButton.setTag("fullscreen");
//                    goto_smallscreen=true;
                    try {
                        videoView_activity.onBackPressed();
                        PalContentListingActivity.backToNormalView();
                    } catch (Exception e) {
                        PalContentListingActivity.backToNormalView();
                    }
                }else {
                    actionButton.setTag("cross");
                    current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
                    actionButton.setImageResource(R.mipmap.video_zoom_out);

                    Util.setIsFullScreen(context,true);
                    PalContentListingActivity.openVimeoVideoFullScreenFragment();
                }
            }
        }
        else if(type.equals("ActivityVideos")) {
            if(Util.getIsFullScreen(context)){
                System.out.println("------- clicked 3");
                actionButton.setImageResource(R.mipmap.video_zoom_in);
                actionButton.setTag("fullscreen");
                goto_smallscreen=true;
                try {
                    try {
                        ((ActivityVideosListingActivity)context).backToNormalView();
                    } catch (Exception e) {
                        ((StemProjectsListingActivity)context).backToNormalView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                videoView_activity.onBackPressed();
            }else {
                System.out.println("------- clicked 4");
                actionButton.setTag("cross");
                actionButton.setImageResource(R.mipmap.video_zoom_out);
                Util.setIsFullScreen(context,true);
                try {
                    ((ActivityVideosListingActivity)context).openVimeoVideoFullScreenFragment();
                } catch (Exception e) {
                    ((StemProjectsListingActivity)context).openVimeoVideoFullScreenFragment();
                }

//                current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
//                PalContentListingActivity.openVimeoVideoFullScreenFragment();
            }

        }
        else if(type.equals("video_lessons")) {
            if(Util.getIsFullScreen(context)){
                actionButton.setImageResource(R.mipmap.video_zoom_in);
                actionButton.setTag("fullscreen");
//                    goto_smallscreen=true;
                try {
                    videoView_activity.onBackPressed();
                    PalContentListingActivity.backToNormalView();
                } catch (Exception e) {
                    if(Util.isPortraitMode(context)) {
                        palTopicListingActivity.rotate_screen();
                    }
                    else {
                        PalContentListingActivity.backToNormalView();
                    }
                }
            }else {
                actionButton.setTag("cross");
                current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
                actionButton.setImageResource(R.mipmap.video_zoom_out);

                Util.setIsFullScreen(context,true);
                if(Util.isPortraitMode(context)){
                    palTopicListingActivity.rotate_screen();
                }
                else {
                    PalContentListingActivity.openVimeoVideoFullScreenFragment();
                }

            }

        }
        else {


            try {
                if(Util.getIsFullScreen(context)){
                    actionButton.setImageResource(R.mipmap.video_zoom_in);
                    actionButton.setTag("fullscreen");
//                goto_smallscreen=true;
                    try {
                        Util.setIsFullScreen(context,false);
                        QuizActivity.gotoSmallScreen(context, VideoView_Activity.videoView.getCurrentPosition());
                        videoView_activity.onBackPressed();
                    } catch (Exception e) {
                        PalContentListingActivity.backToNormalView();
                    }
                }else {
                    actionButton.setTag("cross");
                    actionButton.setImageResource(R.mipmap.video_zoom_out);
                    QuizActivity.gotoFullScreen(context);
                    Util.setIsFullScreen(context,true);

//                current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
//                PalContentListingActivity.openVimeoVideoFullScreenFragment();
                }
            }
            catch (Exception r) {
                if(Util.getIsFullScreen(context)){
                    actionButton.setImageResource(R.mipmap.video_zoom_in);
                    actionButton.setTag("fullscreen");
//                    goto_smallscreen=true;
                    try {
                        videoView_activity.onBackPressed();
                        PalContentListingActivity.backToNormalView();
                    } catch (Exception e) {
                        PalContentListingActivity.backToNormalView();
                    }
                }else {
                    actionButton.setTag("cross");
                    current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
                    actionButton.setImageResource(R.mipmap.video_zoom_out);

                    Util.setIsFullScreen(context,true);
                    PalContentListingActivity.openVimeoVideoFullScreenFragment();
                }
            }



        }
    }

    private void getTextList() {

        if(Util.isOfflineMode(context))
        {
            if(Util.getSelectedLanguage(context).equals("hindi"))
            {
                textArraylist.add("वीडियो सेटिंग्स");
                textArraylist.add("वीडियो की गुणवत्ता");
                textArraylist.add("प्लेबैक स्पीड");
                textArraylist.add("कैंसिल करें");
                textArraylist.add("परिवर्तन लागू करें");
            }
            else
            {
                textArraylist.add("Video Settings");
                textArraylist.add("Video quality");
                textArraylist.add("Playback speed");
                textArraylist.add("Cancel");
                textArraylist.add("Apply Changes");
            }
        }
        else FirebaseDatabase.getInstance().getReference().child("screen_text/student/1/"+Util.getSelectedLanguage(context)+"/VideoDialog")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        System.out.println("--------- snapshot "+snapshot);

                        if(snapshot!=null)
                        {
                            textArraylist= (ArrayList<String>) snapshot.getValue();
                        }
                        else
                        {
                            if(Util.getSelectedLanguage(context).equals("hindi"))
                            {
                                textArraylist.add("वीडियो सेटिंग्स");
                                textArraylist.add("वीडियो की गुणवत्ता");
                                textArraylist.add("प्लेबैक स्पीड");
                                textArraylist.add("कैंसिल करें");
                                textArraylist.add("परिवर्तन लागू करें");
                            }
                            else
                            {
                                textArraylist.add("Video Settings");
                                textArraylist.add("Video quality");
                                textArraylist.add("Playback speed");
                                textArraylist.add("Cancel");
                                textArraylist.add("Apply Changes");
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showSettings() {

        switch (Util.getVideoSpeed(context)) {

            case "0.5x" : speed=0.5f; speedpos=0; break;
            case "0.75x" : speed=0.75f; speedpos=1; break;
            case "1x Normal Speed" : speed=1f; speedpos=2; break;
            case "1x सामान्य स्पीड" : speed=1f; speedpos=2; break;
            case "1.2x" : speed=1.25f; speedpos=3; break;
            case "2x" : speed=2f; speedpos=4; break;

            default:speed=1f;speedpos=1;

        }

        /** Pause Video when dialog is open save current position of player so if user change quality we resume video from current position */
        videoView.pause();
        c_pos=videoView.getCurrentPosition();

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.video_settings_dialog);
        dialog.setCancelable(true);

        /** Variables */
        ArrayAdapter<String> adapter,speed_spinneradapter;
        ArrayList<String> speed_list=new ArrayList<>();
        ArrayList<String> quality_list=new ArrayList<>();
        ArrayList<String> shortedquality_list=new ArrayList<>();
        ArrayList<String> videoLink_list=new ArrayList<>();

        /** init All Views */
        TextView video_settings_text=dialog.findViewById(R.id.video_settings_text);
        TextView video_quality_text=dialog.findViewById(R.id.video_quality_text);
        TextView video_speed_text=dialog.findViewById(R.id.video_speed_text);
        TextView cancel_button=dialog.findViewById(R.id.cancel_button);
        Button apply_btn=dialog.findViewById(R.id.apply_btn);
        LinearLayout videoQualityLayout=dialog.findViewById(R.id.videoQualityLayout);

        Spinner quality_spinner=dialog.findViewById(R.id.quality_spinner);
        Spinner speed_spinner=dialog.findViewById(R.id.speed_spinner);

        /** Hide video quality option in offline mode */
        if(Util.isOfflineMode(context)) videoQualityLayout.setVisibility(GONE);

        speed_list.add("0.5x");
        speed_list.add("0.75x");

        if(Util.getSelectedLanguage(context).equals("hindi")) speed_list.add("1x सामान्य स्पीड");
        else speed_list.add("1x Normal Speed");

        speed_list.add("1.2x");
        speed_list.add("2x");

        /** Setting Text in Views */
        video_settings_text.setText(textArraylist.get(0));
        video_quality_text.setText(textArraylist.get(1));
        video_speed_text.setText(textArraylist.get(2));
        cancel_button.setText(textArraylist.get(3));
        apply_btn.setText(textArraylist.get(4));


        /** Checking videoQuality is available or not */
        if(videoQuality_data!=null) {
            for(String quality:videoQuality_data.keySet())
            {
                quality_list.add(quality);
                shortedquality_list.add(quality);
                videoLink_list.add(videoQuality_data.get(quality));
            }

            shortedquality_list=shortlist(shortedquality_list);


        } else {
           if(Util.getSelectedLanguage(context).equals("hindi")) quality_list.add("गुणवत्ता उपलब्ध नहीं है।");
           else quality_list.add("Quality Not Available");
           videoQualityLayout.setVisibility(GONE);
        }


        /** set adapter in spinner */
        adapter = new ArrayAdapter<String>(context, R.layout.view_spinner_item, shortedquality_list);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        quality_spinner.setAdapter(adapter);

        speed_spinneradapter = new ArrayAdapter<String>(context, R.layout.view_spinner_item, speed_list);
        speed_spinneradapter.setDropDownViewResource(R.layout.spinner_list);
        speed_spinner.setAdapter(speed_spinneradapter);

        /** Set Previous Selection in spinners*/
        speed_spinner.setSelection(speedpos);
        if(shortedquality_list.contains(Util.getVideoQuality(context))) {
            for(int i=0;i<=shortedquality_list.size();i++)
            {
                if(shortedquality_list.get(i).equals(Util.getVideoQuality(context)))
                {
                    quality_spinner.setSelection(i);
                    break;
                }
            }
        }

        /** Click Listener's */
        ArrayList<String> finalShortedquality_list = shortedquality_list;
        quality_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!quality_list.get(i).equals("Quality Not Available") && !quality_list.get(i).equals("गुणवत्ता उपलब्ध नहीं है।"))
                {

                    String qualitySelected= finalShortedquality_list.get(i);

                    if(quality_list.contains(qualitySelected))
                    {
                        for(int ij=0;ij<=quality_list.size();ij++)
                        {
                            if(quality_list.get(ij).equals(qualitySelected))
                            {
                                selectedQuality = quality_list.get(ij);
                                selectedQuality_videolink =  videoLink_list.get(ij);
                                quality_change=true;
                                return;
                            }
                        }
                    }
                }
                else quality_change=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        speed_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (speed_list.get(i)) {
                    case "0.5x" : speed=0.5f; speedpos=0; break;
                    case "0.75x" : speed=0.75f; speedpos=1; break;
                    case "1x Normal Speed" : speed=1f; speedpos=2; break;
                    case "1x सामान्य स्पीड" : speed=1f; speedpos=2; break;
                    case "1.2x" : speed=1.25f; speedpos=3; break;
                    case "2x" : speed=2f; speedpos=4; break;

                    default:speed=1f;speedpos=1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        dialog.findViewById(R.id.close_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                videoView.start();
            }
        });
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                videoView.start();
            }
        });
        apply_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quality_change) {
                    Util.setVideoQuality(context,selectedQuality);
                    Uri video = Uri.parse(selectedQuality_videolink);
                    videoView.setVideoURI(video);
                    videoView.seekTo(c_pos);
                }
                else {
                    videoView.resume();
                    videoView.seekTo(c_pos);
                }

                switch (speedpos) {

                    case 0 : Util.setVideoSpeed(context,"0.5x"); break;
                    case 1 : Util.setVideoSpeed(context,"0.75x"); break;
                    case 2 : Util.setVideoSpeed(context,"1x Normal Speed"); break;
                    case 3 : Util.setVideoSpeed(context,"1.2x"); break;
                    case 4 : Util.setVideoSpeed(context,"2x"); break;
                    default: Util.setVideoSpeed(context,"1x Normal Speed");

                }

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    private ArrayList<String> shortlist(ArrayList<String> quality_list) {
        ArrayList<String> sortquality_list=quality_list;
        Collections.sort(sortquality_list);
        if(sortquality_list.contains("1080p")) {
            sortquality_list.remove("1080p");
            sortquality_list.add("1080p");
        }
        return sortquality_list;
    }
}

