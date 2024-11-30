package com.idreameducation.ipreppal.PalMobile.activity;

import static com.idreameducation.ipreppal.PalMobile.activity.ProjectVideos_topic_Activity.icon;
import static com.idreameducation.ipreppal.PalMobile.activity.ProjectVideos_topic_Activity.subjectName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.PalMobile.Models.ProjectSubTopicModel;
import com.idreameducation.ipreppal.PalMobile.adapter.Project_Subtopic_Adapter;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.fragments.IPrepYoutubeVideoPlayerFragment;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class Project_SubTopic_Activity extends AppCompatActivity {

    ArrayList<ProjectSubTopicModel> list;
    RecyclerView subtopic_recyclerview ;
    TextView subj_name;
    TextView selected_topic_name;
    ImageView sub_icon;
    public static Context context;
    private Global global;

    private static LinearLayout main_layout,title_Bar;
    private static RelativeLayout video_layout;
    ImageView imageViewCrossVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_project_sub_topic);

        assignIds();
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();

        manager = getSupportFragmentManager();


        subtopic_recyclerview=findViewById(R.id.subtopic_recyclerview);
        subj_name=findViewById(R.id.subj_name);
        sub_icon=findViewById(R.id.sub_icon);
        selected_topic_name=findViewById(R.id.selected_topic_name);
        frameLayout = findViewById(R.id.container);
        main_layout = findViewById(R.id.main_layout);
        title_Bar = findViewById(R.id.title_Bar);
        video_layout = findViewById(R.id.video_layout);
        imageViewCrossVideo = findViewById(R.id.imageViewCrossVideo);

        topBar=findViewById(R.id.topBar);

        frameLayout.setVisibility(View.GONE);
        video_layout.setVisibility(View.GONE);
        main_layout.setVisibility(View.VISIBLE);
        topBar.setVisibility(View.VISIBLE);

        subtopic_recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        subj_name.setTextColor(Color.parseColor(ProjectVideos_topic_Activity.color));
        subj_name.setText(subjectName+"");
        Glide.with(context).load(icon).into(sub_icon);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageViewCrossVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-------- removing layout");
                remove_videoLayout();
            }
        });

        findViewById(R.id.fullscreen_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate_screen();
            }
        });

        getDataFromBundle();
        setTopic();


    }

    private void setTopic() {
        subtopic_recyclerview.setAdapter(new Project_Subtopic_Adapter(ProjectVideos_topic_Activity.selected_Topic_list));
    }

    public static int selected_pos;
    String selected_topicname;

    private static FrameLayout frameLayout;
    private static Fragment iPrepYoutubeVideoPlayerFragment;
    private static FragmentManager manager;
    private static FragmentTransaction transaction;

    private Configuration orientation;
    public static boolean isActive=true;

    private void getDataFromBundle() {
        list=ProjectVideos_topic_Activity.selected_Topic_list;
        selected_pos=getIntent().getIntExtra("position",0);
        selected_topicname=getIntent().getStringExtra("topicname");

        if(selected_pos<=9)
        {
            selected_topic_name.setText("0"+selected_pos+"."+selected_topicname);
        }
        else
        {
            selected_topic_name.setText(""+selected_pos+"."+selected_topicname);
        }
        selected_topic_name.setTextColor(Color.parseColor(ProjectVideos_topic_Activity.color));

    }

    public static String clickedVideoID="";

    public static void openFragment(String url, String videoName, String offlineLink, int id, int adapterPos,int position,String topicName) {
        ((Project_SubTopic_Activity)context).remove_videoLayout();
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.setClickable(true);
        frameLayout.setFocusable(true);

        Bundle bundle = new Bundle();


        clickedVideoID=System.currentTimeMillis()+"";

        bundle.putString("url", url);
        bundle.putString("videoName", videoName);
        bundle.putString("offlineLink", offlineLink);
        bundle.putBoolean("isFullScreen", false);
        bundle.putFloat("duration", 0);
        bundle.putInt("id", id);
        bundle.putString("categoryName","categoryName");
        bundle.putInt("adapterPos", adapterPos);
        bundle.putInt("position", position);
        bundle.putString("topicName",topicName);
        bundle.putString("clickedVideoID",clickedVideoID);
        Util.setSubjectId(context,ProjectVideos_topic_Activity.subject);


        transaction = manager.beginTransaction();
        iPrepYoutubeVideoPlayerFragment = new IPrepYoutubeVideoPlayerFragment();
        iPrepYoutubeVideoPlayerFragment.setArguments(bundle);

        transaction.replace(R.id.container, iPrepYoutubeVideoPlayerFragment, "tag");
        isActive=true;
        transaction.addToBackStack(null).commit();

        frameLayout.setVisibility(View.VISIBLE);
        video_layout.setVisibility(View.VISIBLE);
//        setMargins(title_Bar,10,10,10,0);
//        setMargins(video_layout,5,140,5,0);
//        setMargins(main_layout,0,570,0,0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation=newConfig;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void rotate_screen() {
        try {
            if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setlayout_forportrait();
            } else if (orientation.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setlayout_forlandscape();
            }
        }catch (Exception ee)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setlayout_forlandscape();
        }
    }

    private static int width,height=600;

    private void setlayout_forlandscape() {

        main_layout.setVisibility(View.GONE);
        topBar.setVisibility(View.GONE);
//        height=frameLayout.getHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video_layout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        frameLayout.setLayoutParams(layoutParams);
//        setMargins(frameLayout,0,0,0,0);
//        setMargins(video_layout,0,0,0,0);
        Util.setIsFullScreen(context,true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    LinearLayout topBar;

    public void setlayout_forportrait() {

        main_layout.setVisibility(View.VISIBLE);
        video_layout.setVisibility(View.VISIBLE);
        topBar.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
//        setMargins(title_Bar,10,10,10,0);
//        setMargins(video_layout,5,140,5,0);
//        setMargins(main_layout,0,480,0,0);
        title_Bar.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.VISIBLE);
        frameLayout.setLayoutParams(layoutParams);
        Util.setIsFullScreen(context,false);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void remove_videoLayout() {

        clickedVideoID="";
        isActive=false;

        if(orientation!=null)
        {
            if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setlayout_forportrait();
            }
        }


        frameLayout.removeAllViews();
        try {
            getSupportFragmentManager().beginTransaction().remove(iPrepYoutubeVideoPlayerFragment).addToBackStack(null).commit();
//            transaction.remove(iPrepYoutubeVideoPlayerFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            manager.popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }


        frameLayout.setVisibility(View.GONE);
        video_layout.setVisibility(View.GONE);

        setMargins(title_Bar,10,10,10,0);
        setMargins(video_layout,0,0,0,0);
        setMargins(main_layout,0,40,0,0);

    }

    private static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Override
    public void onBackPressed() {

        if(frameLayout.getVisibility()==View.VISIBLE || video_layout.getVisibility()==View.VISIBLE)
        {
            remove_videoLayout();
        }
        else
        {
            finish();
        }

    }
}