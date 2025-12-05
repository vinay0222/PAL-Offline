//package com.idream.android.iprep;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.ViewPager;
//
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.material.tabs.TabLayout;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//import com.idream.android.R;
//import com.idream.android.educationApplication.Global;
//import com.idream.android.iprep.adapters.ContentScreenSideAdapter;
//import com.idream.android.iprep.adapters.StemListingSideAdapter;
//import com.idream.android.iprep.adapters.StemVideoListingAdapter;
//import com.idream.android.iprep.fragments.IPrepYoutubeVideoPlayerFragment;
//import com.idream.android.iprep.fragments.VideoListFragment;
//import com.idream.android.util.ApplicationConstants;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class StemProjectsListingActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private Context context;
//    private Global global;
//    public String board;
//    public String sClass;
//    public String subject;
//    public String subjectName;
//    public String language = "english";
//    private RecyclerView recyclerView;
//    private RecyclerView videoRecyclerView;
//    private TextView textViewSubjectName;
//    private TextView textViewVideoTitle;
//    private ImageView imageViewBack;
//    private ImageView imageViewSubject;
//    private RequestOptions requestOptions;
//    private ProgressBar mProgressBar;
//    private VideoView videoView;
//    private RelativeLayout relativeLayout;
//    private boolean isFullScreenModeEnabled = true;
//    private FrameLayout frameLayout;
//    private Fragment iPrepYoutubeVideoPlayerFragment;
//    private FragmentManager manager;
//    private FragmentTransaction transaction;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_stem_listing);
//        assignIds();
//        listners();
//    }
//
//    private void listners() {
//        imageViewBack.setOnClickListener(this);
//    }
//
//    private void assignIds() {
//        context = this;
//        global = (Global) getApplicationContext();
//        imageViewBack = findViewById(R.id.imageViewBack);
//        imageViewSubject = findViewById(R.id.imageViewSubject);
//        textViewSubjectName = findViewById(R.id.textViewSubjectName);
//        textViewVideoTitle = findViewById(R.id.textViewVideoTitle);
//        board = getIntent().getStringExtra("board");
//        sClass = getIntent().getStringExtra("sClass");
//        subject = getIntent().getStringExtra("subject");
//        subjectName = getIntent().getStringExtra("subjectName");
//        textViewSubjectName.setText(subjectName);
//
//
//        requestOptions = new RequestOptions();
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        GridLayoutManager manager = new GridLayoutManager(context, 1);
//        recyclerView.setLayoutManager(manager);
//
//        videoRecyclerView = findViewById(R.id.videoRecyclerView);
//        videoRecyclerView.setHasFixedSize(true);
//        GridLayoutManager videoRecyclerViewManager = new GridLayoutManager(context, 2);
//        videoRecyclerView.setLayoutManager(videoRecyclerViewManager);
//
//        requestOptions.dontTransform();
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//
//
//        try {
//            getListings();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public ArrayList<HashMap<String, Object>> contentArrayList;
//    private int adapterPos = 0;
//
//    private void getListings() throws Exception {
//        global.getDatabaseReference().child(ApplicationConstants.TOPICS).child(board).child(language).child(sClass).child(ApplicationConstants.SUBJECTS).child(subject).child("activity_videos").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    if (snapshot.getValue() != null) {
//                        contentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
////                        for(Object list : contentArrayList){s
////                            Log.v("VideoListFrag TTTT", "list = "+list);
////                        }
////                        Log.v("StemListing TTTT", "value = "+contentArrayList);
//                        stemListingSideAdapter = new StemListingSideAdapter(context, contentArrayList, sClass);
//                        recyclerView.setAdapter(stemListingSideAdapter);
//                        stemListingSideAdapter.SetOnItemClickListener(new StemListingSideAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                adapterPos = position;
//                                ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(position).get(ApplicationConstants.TOPICS);
//                                textViewVideoTitle.setText((String) contentArrayList.get(position).get("name"));
//                                StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
//                                videoRecyclerView.setAdapter(stemVideoListingAdapter);
//                                stemVideoListingAdapter.notifyDataSetChanged();
//                                stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View view, int position) {
//                                        String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
//                                        String url = getYouTubeId(completeUrl);
//                                        String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
//                                        openFragment(url, name);
//                                        //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS);
//                        textViewVideoTitle.setText((String) contentArrayList.get(adapterPos).get("name"));
//                        stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
//                        videoRecyclerView.setAdapter(stemVideoListingAdapter);
//                        stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
//                                String url = getYouTubeId(completeUrl);
//                                String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
//                                openFragment(url, name);
//                                Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public void openFragment(String url, String videoName) {
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.VISIBLE);
//
//        Bundle bundl = new Bundle();
//        bundl.putString("url", url);
//        bundl.putString("videoName", videoName);
//        iPrepYoutubeVideoPlayerFragment = new IPrepYoutubeVideoPlayerFragment();
//        iPrepYoutubeVideoPlayerFragment.setArguments(bundl);
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.container, iPrepYoutubeVideoPlayerFragment, "IPrepYoutubeVideoPlayerTag");
//        transaction.addToBackStack(null);
//
//        transaction.commit();
//    }
//
//    public void removeFragment() {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction trans = manager.beginTransaction();
//        Fragment fragment = manager.findFragmentByTag("IPrepYoutubeVideoPlayerTag");
//        trans.remove(fragment);
//        trans.commit();
//        manager.popBackStack();
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.GONE);
//    }
//
//    private String getYouTubeId(String youTubeUrl) {
//        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
//        Pattern compiledPattern = Pattern.compile(pattern);
//        Matcher matcher = compiledPattern.matcher(youTubeUrl);
//        if (matcher.find()) {
//            return matcher.group();
//        } else {
//            return "error";
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imageViewBack:
//                onBackPressed();
//                break;
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.GONE);
//        
//    }
//}
package com.idreameducation.ipreppal.PalMobile.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.StemListingSideAdapter;
import com.idreameducation.ipreppal.pal.adapter.StemVideoListingAdapter;
import com.idreameducation.ipreppal.pal.fragments.IPrepYoutubeVideoPlayerFragment;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StemProjectsListingActivity extends AppCompatActivity  {

    private Context context;
    private Global global;
    public String board;
    public String sClass;
    public String subject;
    public String subjectName;
    public RelativeLayout hideLayout;
    public String categoryName;
    public RelativeLayout showLayout;
    public String language = "english";
    private RecyclerView recyclerView;
    private RecyclerView videoRecyclerView;
    private TextView textViewSubjectName;
    private TextView textViewVideoTitle;
    private ImageView imageViewBack;
    public int width, height;
    private ImageView imageViewSubject;
    private RequestOptions requestOptions;
    private View viiew2;
    private ProgressBar mProgressBar;
    private LinearLayout containerLinearLayout;
    private VideoView videoView;
    private RelativeLayout relativeLayout;
    private final boolean isFullScreenModeEnabled = true;
    private FrameLayout frameLayout;
    private Fragment iPrepYoutubeVideoPlayerFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FrameLayout yt_video_container;
    private TextView videoName_Text;
    private TextView textViewChapterCount;
    private boolean isVideoFullScreen = false;
    private String color;
    private StemListingSideAdapter stemListingSideAdapter;
    private StemVideoListingAdapter stemVideoListingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_stem_listing);
        Util.handleNotch(this);
        assignIds();
        listners();
        hideNavigationBar(getWindow());
    }

    private void listners() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewSubject = findViewById(R.id.imageViewSubject);
        textViewSubjectName = findViewById(R.id.textViewSubjectName);
        textViewVideoTitle = findViewById(R.id.textViewVideoTitle);
        textViewChapterCount = findViewById(R.id.textViewChapterCount);
        containerLinearLayout = findViewById(R.id.containerLinearLayout);
        board = getIntent().getStringExtra("board");
        sClass = getIntent().getStringExtra("sClass");
//        sClass = "6";
        subject = getIntent().getStringExtra("subject");
//        subject = "math";
        subjectName = getIntent().getStringExtra("subjectName");
        categoryName = getIntent().getStringExtra("categoryName");
        color = getIntent().getStringExtra("color");
        language = Util.getSelectedLanguage(context);
        viiew2 = findViewById(R.id.viiew2);
        textViewSubjectName.setText(subjectName);
        textViewSubjectName.setTextColor(Color.parseColor(color));
        videoName_Text = findViewById(R.id.videoName);
        yt_video_container = new FrameLayout(this);
        yt_video_container.setId(R.id.yt_video_container);
        yt_video_container.setBackgroundColor(Color.BLACK);
        requestOptions = new RequestOptions();
        recyclerView = findViewById(R.id.recyclerView);
        hideLayout = findViewById(R.id.hideLayout);
        showLayout = findViewById(R.id.showLayout);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setHasFixedSize(true);
        GridLayoutManager videoRecyclerViewManager = new GridLayoutManager(context, 2);
        videoRecyclerView.setLayoutManager(videoRecyclerViewManager);
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

//        imageViewBack.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    imageViewBack.setBackgroundResource(R.drawable.foregroundborderonly);
//                }
//                else {
//                    imageViewBack.setBackgroundResource(R.drawable.rounded_homeground_backscreen_normal);
//                }
//            }
//        });


        try {
            showLoader();
          //  getListings();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Util.isOfflineMode(context))
        {
            String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + subjectName+".png";

            Glide.with(context).load(completePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageViewSubject);

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/subjects%2FMath.png?alt=media&token=ee3f6cb1-afa0-413a-9dfb-4a5dc70a83a2").transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageViewSubject);
                }
            }, 1000);
        }

    }

    public void showLoader() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLayout.setVisibility(View.GONE);
                showLayout.setVisibility(View.VISIBLE);
                try {
                    getListings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 300);
    }

    public ArrayList<HashMap<String, Object>> contentArrayList;
    private int adapterPos = 0;

    private void getListings() throws Exception {
        if (Util.isOfflineMode(context))
        {
            try {

                String filePath = ".iDream_content/offlinetab_PAL/Class"+sClass+language+".txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object = jsonObject.getJSONObject(board);
                JSONObject object_ = object.getJSONObject(language.toLowerCase());
                JSONObject object__ = object_.getJSONObject(sClass);

                //


                JSONObject object___ = object__.getJSONObject("extra_content");
                JSONObject object____ = object___.getJSONObject(categoryName);
                JSONArray array = object____.getJSONArray(subject);

                //


//                JSONObject object___ = object__.getJSONObject("subjects");
//                JSONObject object____ = object___.getJSONObject(subject);
//                JSONArray array = object____.getJSONArray(categoryName);

                contentArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    String objName = object1.getString("name");
                    JSONArray objArray = object1.getJSONArray("topics");
                    ArrayList<HashMap<String, String>> topicsArray = new ArrayList<>();
                    for (int j = 0; j < objArray.length(); j++) {
                        JSONObject object2 = objArray.getJSONObject(j);
                        HashMap<String, String> newHashMap = new HashMap<>();
                        String detail = object2.getString("detail");
                        String id = object2.getString("id");
                        String contentName = object2.getString("name");
                        String offlineLink = object2.getString("offlineLink");
                        String offlineThumbnail = object2.getString("offlineThumbnail");
                        String onlineLink = object2.getString("onlineLink");
                        String thumbnail = object2.getString("thumbnail");
                        String topicName = object2.getString("topicName");
                        newHashMap.put("detail", detail);
                        newHashMap.put("id", id);
                        newHashMap.put("name", contentName);
                        newHashMap.put("offlineLink", offlineLink);
                        newHashMap.put("offlineThumbnail", offlineThumbnail);
                        newHashMap.put("onlineLink", onlineLink);
                        newHashMap.put("thumbnail", thumbnail);
                        newHashMap.put("topicName", topicName);
                        hashMap.put("name", objName);
                        topicsArray.add(newHashMap);
                        hashMap.put("topics", topicsArray);
                    }
                    contentArrayList.add(hashMap);

                    for (int j = 0; j < contentArrayList.size(); j++) {
                        if (j == 0)
                        {
                            contentArrayList.get(j).put("selected", "true");
                        }
                        else
                        {
                            contentArrayList.get(j).put("selected", "false");
                        }
                    }
                    textViewChapterCount.setText(contentArrayList.size()+" Chapters");
                    stemListingSideAdapter = new StemListingSideAdapter(context, contentArrayList, sClass,color);
                    recyclerView.setAdapter(stemListingSideAdapter);
                    stemListingSideAdapter.SetOnItemClickListener(new StemListingSideAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Util.preventTwoClick(view);
                            adapterPos = position;
                            for (int i = 0; i < contentArrayList.size(); i++) {
                                contentArrayList.get(i).put("selected", "false");
                            }
                            contentArrayList.get(position).put("selected", "true");
                            stemListingSideAdapter.notifyDataSetChanged();
                            ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(position).get(ApplicationConstants.TOPICS);
                            textViewVideoTitle.setText((String) contentArrayList.get(position).get("name"));
                            textViewVideoTitle.setTextColor(Color.parseColor(color));
                            StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                            videoRecyclerView.setAdapter(stemVideoListingAdapter);
                            stemVideoListingAdapter.notifyDataSetChanged();
                            videoRecyclerView.requestFocus();
                            stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Util.preventTwoClick(view);
                                    onBackPressedpracti();
                                    stemListingSideAdapter.notifyDataSetChanged();
                                    String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                    String url = getYouTubeId(completeUrl);
                                    String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                    String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                 //   String topicName = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("topicName");
                                    int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                                    String topicName = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("topicName");
                                    Util.preventTwoClick(view);
                                    try {
                                        System.out.println( "===== pausing");
                                        videoView.pause();
                                    }catch (Exception f){}
                                    openFragment(url, name,offlineLink,id,position,adapterPos,topicName);
                                    videoRecyclerView.requestFocus();
                                }
                            });
                        }
                    });

                    ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS);
                    textViewVideoTitle.setText((String) contentArrayList.get(adapterPos).get("name"));
                    textViewVideoTitle.setTextColor(Color.parseColor(color));
                    StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                    videoRecyclerView.setAdapter(stemVideoListingAdapter);
                    stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            onBackPressedpracti();
                            String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                            String url = getYouTubeId(completeUrl);
                            String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                            String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                            int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                            String topicName = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("topicName");
                            Util.preventTwoClick(view);
                            try {
                                System.out.println( "===== pausing");
                                videoView.pause();
                            }catch (Exception f){}
                            openFragment(url, name,offlineLink,id,position,adapterPos,topicName);
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        global.getDatabaseReference().child(ApplicationConstants.TOPICS).child(board).child(language).child(sClass).child(ApplicationConstants.SUBJECTS).child(subject).child(categoryName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        contentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();

                        StemListingSideAdapter stemListingSideAdapter = new StemListingSideAdapter(context, contentArrayList, sClass,color);
                        recyclerView.setAdapter(stemListingSideAdapter);
                        stemListingSideAdapter.SetOnItemClickListener(new StemListingSideAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Util.preventTwoClick(view);
                                adapterPos = position;
                                ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(position).get(ApplicationConstants.TOPICS);
                                textViewVideoTitle.setText((String) contentArrayList.get(position).get("name"));
                                textViewVideoTitle.setTextColor(Color.parseColor(color));
                                StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                                videoRecyclerView.setAdapter(stemVideoListingAdapter);
                                stemVideoListingAdapter.notifyDataSetChanged();
                                stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        onBackPressedpracti();
                                        Util.preventTwoClick(view);
                                        String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                        String url = getYouTubeId(completeUrl);
                                        String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                        String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                        int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                                        String topicName = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("topicName");
                                        try {
                                            System.out.println( "===== pausing");
                                            videoView.pause();
                                        }catch (Exception f){}
                                        openFragment(url, name,offlineLink,id,position,adapterPos,topicName);
                                        //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
                            }
                        });

                        ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS);
                        textViewVideoTitle.setText((String) contentArrayList.get(adapterPos).get("name"));
                        textViewVideoTitle.setTextColor(Color.parseColor(color));
                        StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                        videoRecyclerView.setAdapter(stemVideoListingAdapter);
                        stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Util.preventTwoClick(view);
                                onBackPressedpracti();
                                String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                String url = getYouTubeId(completeUrl);
                                String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                                String topicName = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("topicName");
                                try {
                                    System.out.println( "===== pausing");
                                    videoView.pause();
                                }catch (Exception f){}
                                openFragment(url, name,offlineLink,id,position,adapterPos,topicName);
                            }
                        });

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

    public void openFragment(String url, String videoName, String offlineLink, int id, int adapterPos,int position,String topicName) {
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
        videoName_Text.setVisibility(View.GONE);

        ViewTreeObserver vto = frameLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    frameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width  = frameLayout.getMeasuredWidth();
                height = frameLayout.getMeasuredHeight();

            }
        });

//        int[] location = new int[2];
//        frameLayout.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int newX = x - convertDpToPx(20);
//        int newY = y- convertDpToPx(20);
//        int deviceWidth = metrics.widthPixels;
//        int deviceHeight = metrics.heightPixels;
//        int videoLayoutWidth = deviceWidth - newX;
//        int videoLayoutHeight = deviceHeight - newY;
          videoName_Text.setText(videoName);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, videoLayoutHeight);
////        layoutParams.setMargins(1150, 5, 0, 750);
////        setMargins(videoName_Text,10,1270,20,0);
//
//
////        For 55" tv
////        layoutParams.setMargins(600,5,5,350);
////        setMargins(videoName_Text,10,720,20,0);
//
//// For 75" Benq Panel
////        layoutParams.setMargins(1150, 5, 0, 750);
////        setMargins(videoName_Text,10,1350,20,0);
//
//        setMargins(textViewVideoTitle, 20, 2, 20, 0);
//
////        layoutParams.setMargins(600, 5, 5, 420);
////        setMargins(videoName_Text,10,630,20,0);
//
////        For 10" lenovo tab
//
//        layoutParams.setMargins(400, 120, 2, 220);
//        setMargins(videoName_Text,20,450,20,0);
//
////        For 8" lenovo tab
////        layoutParams.setMargins(400, 2, 2, 270);
////        setMargins(videoName_Text,10,440,20,0);
//
////        for 7"lenovo tab
////        layoutParams.setMargins(310, 50, 2, 200);
////        setMargins(videoName_Text,12,350,20,0);
//
//        yt_video_container.setLayoutParams(layoutParams);
        frameLayout.setClickable(true);
        frameLayout.setFocusable(true);

        //removeFragment();
   //     onBackPressedpracti();
//        parentView.removeView(yt_video_container);
//        parentView.addView(yt_video_container, 1);

    //    frameLayout.setVisibility(View.GONE);

        Bundle bundle = new Bundle();


        bundle.putString("url", url);
        bundle.putString("videoName", videoName);
        bundle.putString("offlineLink", offlineLink);
        bundle.putBoolean("isFullScreen", false);
        bundle.putFloat("duration", 0);
        bundle.putInt("id", id);
        bundle.putString("categoryName",categoryName);
        bundle.putInt("adapterPos", adapterPos);
        bundle.putInt("position", position);
        bundle.putString("topicName",topicName);
        Util.setSubjectId(context,subject);
        System.out.println("========= id 2 "+id);

        iPrepYoutubeVideoPlayerFragment = new IPrepYoutubeVideoPlayerFragment();
        iPrepYoutubeVideoPlayerFragment.setArguments(bundle);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, iPrepYoutubeVideoPlayerFragment, "tag");

        //setMargins(textViewVideoTitle, 20, 420, 20, 0);
//        transaction.addToBackStack(null);

        transaction.commit();
        isVideoFullScreen = false;
    }


    public void openYoutubeVideoFragment(Fragment fragment, String url, String videoName, float duration) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        transaction.remove(fragment);
        manager.popBackStack();
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.GONE);

        yt_video_container = findViewById(R.id.yt_video_container);
        yt_video_container.setVisibility(View.VISIBLE);

        Fragment newFragment = recreateFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("videoName", videoName);
        bundle.putBoolean("isFullScreen", true);
        bundle.putFloat("duration", duration);
        newFragment.setArguments(bundle);
        transaction.add(R.id.yt_video_container, newFragment, "IPrepYoutubeVideoPlayerTag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Fragment recreateFragment(Fragment f) {
        try {
            manager = getSupportFragmentManager();
            Fragment.SavedState savedState = manager.saveFragmentInstanceState(f);

            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        }
        catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
        }
    }

    public void openVimeoVideoFullScreenFragment() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        frameLayout.setLayoutParams(params);
        isVideoFullScreen = true;
    }



    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    public void backToNormalView(){
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);

//        int[] location = new int[2];
//        frameLayout.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int newX = x - convertDpToPx(20);
//
//        int deviceWidth = metrics.widthPixels;
//        int videoLayoutWidth = deviceWidth - newX;
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                videoLayoutWidth, convertDpToPx(400));
//
//
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        params.width=width;
        params.height=height;
        frameLayout.setLayoutParams(params);
        shortscreencontroller();

////      For TV Kodak"tab
//        layoutParams.setMargins(newX, y, 0, 0);
////        setMargins(yt_video_container,1150, 5, 0, 750);
//
////      For TV "
//  //      setMargins(yt_video_container,600,5,5,350);
//
////      For 10"tab
//        setMargins(yt_video_container,400, 120, 2, 220);
//
////      For 8"tab
//     //   setMargins(yt_video_container,400, 2, 2, 270);
//
////      For 7"tab
//   //     setMargins(yt_video_container,310, 50, 2, 200);
//
//
//  //      yt_video_container.setLayoutParams(layoutParams);
//   //     frameLayout.setVisibility(View.GONE);
        isVideoFullScreen = false;
    }

    public void removeFragment() {
        frameLayout.setVisibility(View.GONE);
        shortscreencontroller();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        if(fragment != null){
            transaction.remove(fragment);
            transaction.commit();
            manager.popBackStack();
        }

//        ViewGroup parent = (ViewGroup) yt_video_container.getParent();
//        if(parent != null){
//            parent.removeView(yt_video_container);
//        }
        videoName_Text.setVisibility(View.GONE);
        isVideoFullScreen = false;
    }


    public void fullscreencontroller(){

        textViewVideoTitle.setVisibility(View.GONE);
        videoRecyclerView.setVisibility(View.GONE);
        viiew2.setVisibility(View.GONE);
        videoName_Text.setVisibility(View.GONE);
        isVideoFullScreen = true;
       // viewid.setVisibility(View.GONE);
       // tabLayout.setVisibility(View.GONE);
        containerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, 2f));
     //   hideSystemUI();
    }

    public void shortscreencontroller(){
        showSystemUI();
        textViewVideoTitle.setVisibility(View.VISIBLE);
        videoRecyclerView.setVisibility(View.VISIBLE);
        //viewPager.setVisibility(View.VISIBLE);
        videoName_Text.setVisibility(View.GONE);
        isVideoFullScreen=false;
        viiew2.setVisibility(View.VISIBLE);
       // viewid.setVisibility(View.VISIBLE);
       // tabLayout.setVisibility(View.VISIBLE);
        containerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, WindowManager.LayoutParams.MATCH_PARENT, 1.4f));





    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.GONE);
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showSystemUI() {
        View mDecorView = StemProjectsListingActivity.this.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean videoFragmentExists(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        return fragment != null;
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
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


    public void onBackPressedpracti() {
        //      super.onBackPressed();

        if(isVideoFullScreen){
            backToNormalView();
        }else{
            if(videoFragmentExists()){
                removeFragment();
            }
        }
    }

    @Override
    public void onBackPressed() {
  //      super.onBackPressed();

        if(isVideoFullScreen){
            backToNormalView();
        }else{
            if(videoFragmentExists()){
               removeFragment();
            }else{
                super.onBackPressed();
                
            }
        }
    }

    public void refreshContentAdapter(int i, ArrayList<HashMap<String, String>> videoList) {
        stemListingSideAdapter.notifyDataSetChanged();
//        stemVideoListingAdapter.notifyDataSetChanged();
        textViewVideoTitle.setText((String) contentArrayList.get(i).get("name"));
        textViewVideoTitle.setTextColor(Color.parseColor(color));
        stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
        videoRecyclerView.setAdapter(stemVideoListingAdapter);
        stemVideoListingAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        Util.preventPause(context,getTaskId());
        super.onPause();
    }

    public static void hideNavigationBar(Window window) {
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = window.getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }



}
