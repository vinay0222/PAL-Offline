package com.idreameducation.ipreppal.PalMobile.activity;

import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.board;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.completeType;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.currentTopicid;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.five;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.four;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.fourteen;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.isPracticeCompleted;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.practiceTopicAdapterMobile;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.sClass;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.six;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.static_completeType;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.three;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.videoContentArrayList;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.videoKey;
import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.practiceScoreModelArrayList;
import static com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.topicsArrayList;
import static com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity.reletiveVideoView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.adapter.PalContentPagerAdaper;
import com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter;
import com.idreameducation.ipreppal.pal.fragments.PalPracticeFrament;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.TextViewBodyFont;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalTopicListingActivity extends AppCompatActivity {

    public ViewPager viewPager;
    private TabLayout tabLayout;
    private Global global;
    ImageView imageViewSubject;
    TextView textViewSubjectName;
    static Context context;
    public static String subject;
    public static String icon;
    public static String color;
    public static String subjectName,lastTopicId;
    public static PalTopicListingActivity palTopicListingActivity;
    static FrameLayout frameLayout;
    FrameLayout vimeo_video_container;
    private Fragment palVideoPlayerActivity;
    static Bundle bundl=new Bundle();
    static int duration=0;
    FragmentManager manager;
    FragmentTransaction transaction;
    boolean messageLayoutidvisible=false;
    private int videoLevel;
    private int videoPosition;
//    public static String videoKey="";

    private Configuration orientation;
    LinearLayout title_Bar;

    ArrayList<String> onboading_textList=new ArrayList<>();
    TextView loading_text;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler = new Handler();
    ProgressBar mProgressBar;
    private LinearLayout layout_connection;
    PalContentPagerAdaper contentPagerAdaper;
    public static int viewPagerPos=0;

    private final int[] tabIcons = {
            R.drawable.video,
            R.drawable.practice,
            R.drawable.test,
            R.drawable.ic_bookscat
    };

    private final int[] tabIcons_unselected = {
            R.drawable.video_1,
            R.drawable.practice_1,
            R.drawable.test_1,
            R.drawable.ic_bookscat_1
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.setWindowSettings(this);

        setContentView(R.layout.activity_pal_topic_listing);
        Util.handleNotch(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingWithQuotes();
        try {
            PalContentListingActivity_Mobile.palContentListingActivityMobile.getScore();
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoundationTopicDetails(Util.getTopicID(context));
                    }
                },1500);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (Exception f) {
            f.printStackTrace();
        }

    }

    public static int actvePosition=0;

    private void checkFoundation() {

        /** Check foundation details */
        try {

            if(PracticeTopicActivity.autoplayLevelVideo) viewPagerPos=0;

//            String path = topicsArrayList.get(1).get("path");
//            boolean first=true;

            int position=actvePosition;
            boolean foundActiveFoundationTopic=false;
            if(PalContentListingActivity_Mobile.palContentListingActivityMobile.lastTopicId.equals(topicsArrayList.get(position).get("TopicID"))) {

                if(foundationalTopicData != null)
                    for(int f = foundationalTopicData.size()-1; f>=0; f--) {
                        FoundationalTopicModel item = foundationalTopicData.get(f);

                        if(item.getSeniorTopicID().equals(topicsArrayList.get(position).get("TopicID"))) {

                            ArrayList<String> foundationRecord =  new ArrayList<>();
                            foundationRecord.add(item.getSClass());

                            /** Set Score of foundation practice  */
                            for(int o = PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                PracticeScoreModel data=PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.get(o);
                                if (data.getTopicId().equals(item.getTopicId())) {

                                    /** show foundation class */
                                    String sclass = item.getSClass().replace("_nonmedical_medical", "").replace("_commerce", "").replace("_arts", "");

                                    if (data.getScore().equals("100")) {
                                        /** completed foundation topic */

                                    }
                                    else {
                                        if (first) {
                                            /** Active foundation topic */
                                            PracticeTopicAdapter.activeFoundationTopic=item.getTopicId();
                                            PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideoss(item.getTopicId(),item.getSClass(),item.getSubjectId());
                                            completeType = "foundationalPractice";
                                            PalVideoListFragment.palVideoListFragment.showTestLayout(completeType, item, position);
                                            Messagetype=completeType;
                                            Messagemodel=item;
                                            Messageposition=position;
                                            foundActiveFoundationTopic=true;
                                            first = false;
                                        }
                                    }
                                }
                            }



                        }
                    }

            }

            if (!foundActiveFoundationTopic) {
                PracticeTopicAdapter.activeFoundationTopic=PalContentListingActivity_Mobile.palContentListingActivityMobile.lastTopicId;
                PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideoss(PalContentListingActivity_Mobile.palContentListingActivityMobile.lastTopicId,Util.getSelectedClass(context),PalContentListingActivity_Mobile.palContentListingActivityMobile.subject);
                completeType = "practice";
                PalVideoListFragment.palVideoListFragment.showTestLayout(completeType, null, position);
                PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagetype = null;
                PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagemodel = null;
                PalContentListingActivity_Mobile.palContentListingActivityMobile.Messageposition = 0;
                Messagetype=completeType;
                Messagemodel=null;
                Messageposition=position;
            }

            setPagerAdapter();
            setTabLayout();

            if(PracticeTopicActivity.autoplayLevelVideo) playLevelVideo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Disposable foundationalTopicDisposable;
    public List<FoundationalTopicModel> foundationalTopicData;

    public String clss,subjec;
    public static String currentTopicid;
    boolean first = true;

    public String Messagetype; public FoundationalTopicModel Messagemodel;public int Messageposition;
    public void getFoundationTopicDetails(String topicID) {

        foundationalTopicRepository = new FoundationalTopicRepository(context);
        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);

        first = true;
        try
        {
            getList(Util.getUserId(context), board, sClass, subject, topicID, null, "practiceScore").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            practiceScoreTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsLatestDataPracticeModel> list = (ArrayList<ReportsLatestDataPracticeModel>) o;
                            for (ReportsLatestDataPracticeModel item : list) {
                                if (practiceScoreModelArrayList.size() > 0) {
                                    for (PracticeScoreModel data : practiceScoreModelArrayList) {
                                        if (data.getTopicId().equals(item.getTopicId())) {
                                            practiceScoreModelArrayList.remove(data);
                                            break;
                                        }
                                    }
                                }
                                practiceScoreModelArrayList.add(new PracticeScoreModel(item.getTopicId(), item.getMastery(),item.getStreakProgress(),item.getCurrentLevel()));
                            }
                            try {
                                PalPracticeFrament.practiceAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            PalContentListingActivity_Mobile.palContentListingActivityMobile.updateUi();

                            getList(Util.getUserId(context), board, sClass, subject, topicID, null, "foundationalTopicData").subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.Observer<Object>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            foundationalTopicDisposable = d;
                                        }

                                        @Override
                                        public void onNext(Object o) {

                                            List<FoundationalTopicModel> list = (List<FoundationalTopicModel>) o;
                                            if (list != null && list.size() > 0) {
                                                foundationalTopicData = list;
                                            }

                                            checkFoundation();

                                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {

                                                try
                                                {
                                                    for(int f=foundationalTopicData.size()-1;f>=0;f--) {

                                                        FoundationalTopicModel item = foundationalTopicData.get(f);
                                                        if (item.getSeniorTopicID().equals(topicID)) {
                                                            for (int p = practiceScoreModelArrayList.size() - 1; p >= 0; p--) {
                                                                PracticeScoreModel data = practiceScoreModelArrayList.get(p);
                                                                if (data.getTopicId().equals(item.getTopicId())) {
                                                                    if (!data.getScore().equals("100")) {
                                                                        if (first) {
                                                                            clss=item.getSClass();
                                                                            subjec=item.getSubjectId();
                                                                            currentTopicid=item.getTopicId();
                                                                            first = false;
                                                                            completeType = "foundationalPractice";
                                                                            static_completeType = "foundationalPractice";
                                                                            try {
                                                                                PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(currentTopicid);
                                                                                System.out.println("============= from 2");
                                                                                return;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }

                                                                    }
                                                                    else {
                                                                        if(f == 0) {
                                                                            if(completeType.equals("practice")) PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
                                                                            else checkmessageChanges("practice");
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    if(p == 0) {
                                                                        if(completeType.equals("practice")) PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
                                                                        else checkmessageChanges("practice");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

//                                                    for (FoundationalTopicModel item : foundationalTopicData) {
//                                                        if (lastTopicId.equals(item.getSeniorTopicID())) {
//                                                            if (!isPracticeCompleted(item.getTopicId())) {
//                                                                int position = 0;
//
//                                                                completeType = "foundationalPractice";
//                                                                static_completeType = "foundationalPractice";
//                                                                for (int i = 0; i < topicsArrayList.size(); i++) {
//                                                                    if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
//                                                                        position = i;
//                                                                        break;
//                                                                    }
//                                                                }
//
//                                                                try {
//                                                                    PalVideoListFragment.palVideoListFragment.showTestLayout(completeType, item, position);
//                                                                } catch (Exception e) {
//                                                                    Messagetype=completeType;
//                                                                    Messagemodel=item;
//                                                                    Messageposition=position;
//                                                                    PalVideoListFragment.refreshMessage=true;
//                                                                    e.printStackTrace();
//                                                                }
////                                                    break;
//                                                            }
//                                                            else {
//                                                                try {
//                                                                    PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
//                                                                    System.out.println("============= from 3");
//                                                                    return;
//                                                                } catch (Exception e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                                try {
//                                                                    PalVideoListFragment.palVideoListFragment.showTestLayout(completeType,  null, 0);
//                                                                } catch (Exception e) {
//                                                                    Messagetype=completeType;
//                                                                    Messagemodel=null;
//                                                                    Messageposition=0;
//                                                                    PalVideoListFragment.refreshMessage=true;
//                                                                    e.printStackTrace();
//                                                                }
//                                                            }
//                                                        }
//                                                        else {
//                                                            if(completeType.equals("practice")) {
//                                                                try {
//                                                                    PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
//                                                                } catch (Exception e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                            }
//                                                            else checkmessageChanges("practice");
//                                                        }
//
//                                                    }

                                                    for (FoundationalTopicModel item : foundationalTopicData) {
                                                        if (lastTopicId.equals(item.getSeniorTopicID())) {
                                                            if (!isPracticeCompleted(item.getTopicId())) {
                                                                int position = 0;

                                                                completeType = "foundationalPractice";
                                                                static_completeType = "foundationalPractice";
                                                                for (int i = 0; i < topicsArrayList.size(); i++) {
                                                                    if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
                                                                        position = i;
                                                                        break;
                                                                    }
                                                                }

                                                                try {
//                                                                    PalVideoListFragment.palVideoListFragment.showTestLayout(completeType, item, position);
                                                                } catch (Exception e) {
                                                                    Messagetype=completeType;
                                                                    Messagemodel=item;
                                                                    Messageposition=position;
                                                                    PalVideoListFragment.refreshMessage=true;
                                                                    e.printStackTrace();
                                                                }
                //                                                    break;
                                                            }
                                                            else {
                                                                try {
//                                                                    getVideos(topicID);
                                                                    System.out.println("============= from 3");
                                                                    return;
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
//                                                                    PalVideoListFragment.palVideoListFragment.showTestLayout(completeType,  null, 0);
                                                                } catch (Exception e) {
                                                                    Messagetype=completeType;
                                                                    Messagemodel=null;
                                                                    Messageposition=0;
                                                                    PalVideoListFragment.refreshMessage=true;
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            if(completeType.equals("practice")) {
                                                                try {
//                                                                    getVideos(topicID);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            else checkmessageChanges("practice");
                                                        }

                                                    }

                                                }catch (Exception ee) {
                                                    if(completeType.equals("practice")) {
                                                        try {
                                                            PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    else checkmessageChanges("practice");
                                                }

                                            }
                                            else {
                                                try {
                                                    if(completeType.equals("practice")) PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(topicID);
                                                    else checkmessageChanges("practice");
                                                    return;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            foundationalTopicDisposable.dispose();
                                        }
                                    });
//                            updateUi();

//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            practiceScoreTopicDisposable.dispose();
                        }
                    });



        }catch (Exception f){
            f.printStackTrace();
        }
    }

    public void checkmessageChanges(String message) {
        if(completeType==null) completeType="";
        if(!message.equals(completeType)) {
            completeType=message;
            static_completeType=message;
            PalContentListingActivity_Mobile.palContentListingActivityMobile.refreshMessageLayout();
        }
    }


    public FoundationalTopicRepository foundationalTopicRepository;
    private Disposable practiceScoreTopicDisposable;

    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String topicId, String value, String type) {
        if (type.equals("foundationalTopicData")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(Util.getContext()));
            });
        }else if (type.equals("practiceScore")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, "practice", null);
            });
        }
        return null;
    }

//    public ArrayList<HashMap<String, Object>> contentArrayList;
//    public ArrayList<HashMap<String, Object>> contentArrayList_;
//    public static ArrayList<HashMap<String, Object>> videoContentArrayList;
//
//    private String videoTopicID="";
//
//    public void getVideos(String topicID) throws Exception {
//
//        if(!completeType.equals("foundationalPractice")) {
//            clss=Util.getSelectedClass(context);
//            subjec=Util.getSubject(context).toLowerCase();
//            currentTopicid=Util.getSeniorTopicID(context);
//        }
//
//        if(currentTopicid.contains("sci")) subjec="science";
//
//        System.out.println("========= video topic id "+topicID);
//
//        if(videoTopicID.equals(currentTopicid)) {
//            System.out.println("======== already loaded ");
//            return;
//        }
//
//
//        currentTopicid = topicID;
//        String finalTopicid = topicID;
//        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(clss).child(Util.getSelectedLanguage(context)).child("video_lessons").child("content").child(subjec).child("topics").child(currentTopicid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    if (snapshot.getValue() != null) {
//                        PracticeTopicAdapter.activeFoundationTopic=snapshot.getKey();
//                        contentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
//                        contentArrayList_ = new ArrayList<>();
//                        videoContentArrayList = new ArrayList<>();
//                        videoTopicID=currentTopicid;
//                        for (int i = 1; i < contentArrayList.size(); i++) {
//                            LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
//                            LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
//                            HashMap<String, Object> videoMap = contentArrayList.get(i);
//                            int j_ = 0;
//
//                            ArrayList<Integer> shortedKey=new ArrayList<>();
//
//                            for(String keys:videoMap.keySet())
//                            {
//                                shortedKey.add(Integer.parseInt(keys));
//                            }
//                            Collections.sort(shortedKey);
//
//                            for (Integer key : shortedKey) {
//                                HashMap<String, Object> newHashMap_ = new HashMap<>();
//                                HashMap<String, String> innerMap = (HashMap<String, String>) contentArrayList.get(i).get(key+"");
//                                String AssessmentTopicID = innerMap.get("AssessmentTopicID");
//                                String detail = innerMap.get("detail");
//                                String name = innerMap.get("name");
//                                String offlineLink = innerMap.get("offlineLink");
//                                String offlineThumbnail = innerMap.get("offlineThumbnail");
//                                String onlineLink = innerMap.get("onlineLink");
//                                String thumbnail = innerMap.get("thumbnail");
//                                String topicName = innerMap.get("topicName");
//                                newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
//                                newHashMap_.put("detail", detail);
//                                newHashMap_.put("name", name);
//                                newHashMap_.put("offlineLink", offlineLink);
//                                newHashMap_.put("offlineThumbnail", offlineThumbnail);
//                                newHashMap_.put("onlineLink", onlineLink);
//                                newHashMap_.put("thumbnail", thumbnail);
//                                newHashMap_.put("topicName", topicName);
//                                newHashMap_.put("topicID", finalTopicid);
//                                newHashMap_.put("isSelected", "false");
//                                newHashMap_.put("key", key+"");
//                                ((HashMap<String, String>) contentArrayList.get(i).get(key+"")).put("isSelected", "false");
//                                ((HashMap<String, String>) contentArrayList.get(i).get(key+"")).put("topicID", finalTopicid);
//                                newHashMap.put(key+"", newHashMap_);
//                                videoHashMap.put(i + "-" + j_, newHashMap_);
//                                j_++;
//                            }
//                            contentArrayList_.add(newHashMap);
//                            videoContentArrayList.add(videoHashMap);
//
//                        }
//                        try {
//                            PalVideoListFragment.palVideoListFragment.getVideos();
//                            PalPracticeFrament.practiceAdapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        removeFragment();
        Util.preventPause(context,getTaskId());
        viewPagerPos= tabLayout.getSelectedTabPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setlayout_forportrait();
    }

    private void init() {
        context=this;
        palTopicListingActivity=this;
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        imageViewSubject = findViewById(R.id.imageViewSubject);
        textViewSubjectName = findViewById(R.id.textViewSubjectName);
        title_Bar = findViewById(R.id.tab_layout);
        global = (Global) getApplicationContext();

        showLoadingWithQuotes();

        subjectName = getIntent().getStringExtra("subjectName");
        subject = getIntent().getStringExtra("subject");
        icon = getIntent().getStringExtra("icon");
        color = getIntent().getStringExtra("color");

        if(color==null) color=Util.getColorOffline(subject);

        global.setColor(color);

        if(getIntent().getStringExtra("lastTopicId")!=null) {
            lastTopicId = getIntent().getStringExtra("lastTopicId");
        }


        textViewSubjectName.setText(subjectName);

        vimeo_video_container = new FrameLayout(context);
        vimeo_video_container.setId(R.id.vimeo_video_container);
        frameLayout = findViewById(R.id.container);


        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.palContentListingActivity.getScore();
//                setPagerAdapter();
//                setTabLayout();
            }
        });

        setPagerAdapter();
        setTabLayout();


        frameLayout = findViewById(R.id.container);

        /** set subject name and colour  */
        textViewSubjectName.setText(subjectName.substring(0, 1).toUpperCase() + subjectName.substring(1));
        textViewSubjectName.setTextColor(Color.parseColor(color));

    }


    private void showLoadingWithQuotes() {

        mProgressBar = findViewById(R.id.progressBar);
        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);
        loading_text = findViewById(R.id.loading_text);
        layout_connection = findViewById(R.id.layout_connection);
        layout_connection.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);


        if(onboading_textList.size()!=0) {
            showLoadingQuotes();
            return;
        }
        if(!Util.isOfflineMode(context)) global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("OnBoardingText").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        /** getting text from firebase */
                        onboading_textList = (ArrayList<String>) dataSnapshot.getValue();

                        showLoadingQuotes();


                    }
                } catch (Exception e) {
                    onBoadingOfflineText();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onBoadingOfflineText();
            }
        });
        else onBoadingOfflineText();
    }

    private void showLoadingQuotes () {
        int max = onboading_textList.size()-1;
        int min =0;
        int randomNumber = (int) (Math.random()*(max-min)) + min;
        loading_text.setText(onboading_textList.get(randomNumber));
    }

    private void onBoadingOfflineText() {
        if(Util.getSelectedLanguage(context).equals("hindi")) {
            onboading_textList.add("क्या आप जानते हैं ? की डायग्नोस्टिक टेस्ट के जरिए आप किसी भी टॉपिक में अपनी पकड़ चेक कर सकते हैं।\uD83D\uDE32");
            onboading_textList.add("क्या आप जानते हैं ? PAL एप्लीकेशन के डिजिटल बुक्स में आपके पसंद की काफ़ी किताबे दी हुई है।\uD83D\uDE32");
            onboading_textList.add("क्या आप जाते हैं? प्रोजेक्ट वीडियो के जरीये आप खुद से साइंस या मैथ के प्रोजेक्ट्स बना सकते हैं । \uD83D\uDE32");
            onboading_textList.add("क्या आप जानते हैं ? यदि आप अंतिम परीक्षा में 80% से अधिक अंक प्राप्त करते हैं, तो आपको एक बैच मिलता है।\uD83D\uDE32");
            onboading_textList.add("क्या आप जानते हैं ? की आप अपने देखे हुए वीडियो की रिपोर्ट आप मेरी रिपोर्ट्स पे क्लिक करके देख सकते हैं।\uD83E\uDD29");
            onboading_textList.add("क्या आप जानते हैं ? की भाषा पे क्लिक करके आप App और कंटेंट की Language बदल सकते हैं ।\uD83E\uDD14");
            onboading_textList.add("क्या आप जानते हैं ? की PAL App का उद्देस्य आपकी किसी टॉपिक या सब्जेक्ट की पकड़ को मजबूत बनाना है । \uD83E\uDD14");
            onboading_textList.add("क्या आप जानते हैं ? की PAL App में हर एक सब्जेक्ट्स की डिजिटल किताबें भी मौजूद हैं । \uD83E\uDD14");
            onboading_textList.add("क्या आप जानते हैं ? किसी भी टॉपिक के फाइनल टेस्ट आप तभी दे सकते हो जब आपने उस टॉपिक में 100% महारत हासिल कर ली हो । \uD83E\uDD14");
            onboading_textList.add("क्या आप जानते हैं ? आप हमें PAL App की कोई भी समस्या स्क्रीनशॉट लेकर सपोर्ट बटन के मध्यम से शेयर कर सकते हैं। \uD83E\uDD14");

        }
        else {
            onboading_textList.add("Do you know ? Through the Diagnostic Test, you can check your grip in any topic.\uD83D\uDE32");
            onboading_textList.add("Do you know ? Many books of your choice are given in the Digital Books of Pal Application.\uD83D\uDE32\n");
            onboading_textList.add("Do you know ? You can create your own science and math projects with the help of Activity videos. \uD83D\uDE32");
            onboading_textList.add("Do you know ? If you get more than 80% marks in the final test, you get a batch. \uD83D\uDE32");
            onboading_textList.add("Do you know ? That you can see the report of your watched video by clicking on My Reports.\uD83E\uDD29\n");
            onboading_textList.add("Do you know ? You can change the language of the app and the content by clicking on the language \uD83E\uDD14");
            onboading_textList.add("Do you know ? The purpose of PAL App is to strengthen your grip of any topic or subject.\uD83E\uDD14");
            onboading_textList.add("Do you know ? Digital books of each subject are also present in the PAL App. \uD83E\uDD14");
            onboading_textList.add("Do you know ? You can give the final test of any topic only when you have achieved 100% mastery in the same topic.\uD83E\uDD14");
            onboading_textList.add("Do you know ? You can share any problem of PAL App with us through support button by taking screenshot.\uD83E\uDD14");
        }

        int max = onboading_textList.size()-1;
        int min =0;
        int randomNumber = (int) (Math.random()*(max-min)) + min;
        loading_text.setText(onboading_textList.get(randomNumber));
    }

    public void hide_connectionlayout() {
        new Handler().postDelayed(new Runnable() {@Override
        public void run() {
            mProgressBar.setVisibility(View.GONE);
            slow_internet_Text.setVisibility(View.GONE);
            imageViewCrossVideo2.setVisibility(View.GONE);
            layout_connection.setVisibility(View.GONE);
        }
        },1300);
    }

    private void setPagerAdapter() {
        contentPagerAdaper = new PalContentPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(contentPagerAdaper);
        viewPager.setOffscreenPageLimit(4);
    }

    private void setTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        // this needs to be dynamic
        tabLayout.getTabAt(0).setText(three);
        tabLayout.getTabAt(1).setText(four);
        tabLayout.getTabAt(2).setText(five);
        tabLayout.getTabAt(3).setText(six);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_unselected[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_unselected[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons_unselected[3]);

        try {
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(global.getColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabLayout.setTabTextColors(Color.parseColor("#C9C9C9"), Color.parseColor("#223322"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        LinearLayout tabLayoutt = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
        tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabLayout.getTabAt(position).setIcon(tabIcons[position]);
                if(!keys.equals("")) {
                    PalContentListingActivity.keys="";
                    try {
                        PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
                        removeFragment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LinearLayout tabLayoutt = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
                removeFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabLayout.getTabAt(position).setIcon(tabIcons_unselected[position]);
                LinearLayout tabLayoutt = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(viewPagerPos);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
//                    viewPager.setCurrentItem(viewPagerPos);
                    showMessageLayput(completeType);
                    Glide.with(getApplicationContext()).load(Uri.parse(icon)).transition(DrawableTransitionOptions.withCrossFade()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageViewSubject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    public void openFragment(String url, String offlineLink, String videoName, String topicID, boolean isLocalFile, String type, String key) {
//        tabposition=0;
//        viewPager.setCurrentItem(tabposition);
//        remove_fragment=false;
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
//        videoName_Text.setVisibility(View.VISIBLE);
        int[] location = new int[2];
        frameLayout.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        int newX = x - convertDpToPx(20);

        int deviceWidth = metrics.widthPixels;


        try {
            vimeo_video_container.removeAllViews();
//           vimeo_video_container.setLayoutParams(layoutParams);
            ViewGroup parentView = findViewById(R.id.rootLayout);
            parentView.addView(vimeo_video_container, 1);
        } catch (Exception r) {
            r.printStackTrace();
        }

        System.out.println( "------  type "+type);

        if(type.equals("video_lessons"))
        {
            System.out.println( "------  key "+key);
            if(PalVideoListFragment.dataMap != null){

                HashMap<String, String> reportsMap = (HashMap<String, String>) PalVideoListFragment.dataMap.get(key);
                if (reportsMap != null) {
                    try
                    {
                        int seektime = Integer.parseInt(reportsMap.get("seektime"));
                        System.out.println( "-------- seektime "+seektime);
                        PalContentListingActivity.duration=seektime;
                        PalContentListingActivity.current_duration=seektime;
                    }
                    catch (Exception r)
                    {
                        PalContentListingActivity.duration=0;
                        PalContentListingActivity.current_duration=0;
                        r.printStackTrace();
                    }
                }
                else {
                    PalContentListingActivity.duration=0;
                    PalContentListingActivity.current_duration=0;
                }

            }
        }
        else if(type.equals("diksha_content"))
        {
//            System.out.println( "------  key "+key);
//            if(PalDikshaContentFragment.dataMap != null){
//
//                HashMap<String, String> reportsMap = (HashMap<String, String>) PalDikshaContentFragment.dataMap.get(key);
//                if (reportsMap != null) {
//                    try
//                    {
//                        int seektime = Integer.parseInt(reportsMap.get("seektime"));
//                        System.out.println( "-------- seektime "+seektime);
//                        PalContentListingActivity.duration=seektime;
//                        PalContentListingActivity.current_duration=seektime;
//                    }
//                    catch (Exception r)
//                    {
//                        PalContentListingActivity.duration=0;
//                        PalContentListingActivity.current_duration=0;
//                        r.printStackTrace();
//                    }
//                }
//
//            }
        }

//        setlayout_forportrait();

        bundl.putString("url", url);
        bundl.putString("type", type);
        bundl.putString("offlineLink", offlineLink);
        bundl.putBoolean("isLocalFile", isLocalFile);
        bundl.putString("videoName", videoName);
        bundl.putString("topicID", topicID);
        bundl.putString("subjectName", subjectName);
        bundl.putString("videoid_for_reports", key);
        bundl.putBoolean("isFullScreen", false);
        bundl.putInt("duration", duration);
//        bundl.putBoolean("from_smallScreen", true);
//        Util.setIsFullScreen(context,false);
//        videoName_Text.setText(videoName);

//        currentvideo_url = url;
        PalContentListingActivity.keys = key;

//        if (!Util.getIsFullScreen(context)) {
//            try {
//
//                System.out.println( "-------- seektime final "+duration);
//
//                palVideoPlayerActivity = new iPrepVideoPlayerActivity();
//                palVideoPlayerActivity.setArguments(bundl);
//                manager = getSupportFragmentManager();
//                transaction = manager.beginTransaction();
//                transaction.add(R.id.container, palVideoPlayerActivity, "tag");
//                transaction.addToBackStack(null);
//
//                transaction.commit();
//                frameLayout.setVisibility(View.VISIBLE);
//
//                try {
//
//                    if (messageLayoutidvisible) {
////                        messageLayout.setVisibility(View.GONE);
//
//                    }
//
////                    messageLayout2.setVisibility(View.GONE);
//
//
//                } catch (Exception r) {}
//
//
//            } catch (Exception f) {
//
//
//                f.printStackTrace();
//            }
//        } else {
//            Intent intent = new Intent(context, VideoView_Activity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            intent.putExtras(bundl);
//
//            startActivity(intent);
//        }
        palVideoPlayerActivity = new iPrepVideoPlayerActivity();
        palVideoPlayerActivity.setArguments(bundl);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, palVideoPlayerActivity, "tag");
        transaction.addToBackStack(null);

        transaction.commit();
        frameLayout.setVisibility(View.VISIBLE);



        try {

            if (messageLayoutidvisible) {
//                        messageLayout.setVisibility(View.GONE);

            }

//                    messageLayout2.setVisibility(View.GONE);


        } catch (Exception r) {}

    }

    public void removeVideoActivity() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("tag");
//        if (fragment != null)
//            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void removeFragment() {
//        frameLayout.setVisibility(View.GONE);

        setlayout_forportrait();

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PalContentListingActivity.keys = "";
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        if (fragment != null) {
            try {
                transaction.remove(fragment);
                transaction.commit();
                manager.popBackStack();
            } catch (Exception e) {}
        }
        ViewGroup parent = null;
        try {
            parent = (ViewGroup) vimeo_video_container.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frameLayout.setVisibility(View.GONE);

        if (parent != null) {
            parent.removeView(vimeo_video_container);
        }


    }

    public void removeFragment(boolean isCompleted) {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        frameLayout.setVisibility(View.GONE);
        Fragment fragment = manager.findFragmentByTag("tag");
        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
            manager.popBackStack();
        }
        ViewGroup parent = (ViewGroup) vimeo_video_container.getParent();

        if (parent != null) {
            parent.removeView(vimeo_video_container);
        }
//        isFullScreen = false;
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        Fragment f = manager.findFragmentByTag("tag");
//        transaction.remove(f);
//        if (f != null) {
//            transaction.remove(f);
//            transaction.commit();
//            manager.popBackStack();
//        }
//
//        if(isFullScreen){
//            smallScreen(true);
//        }
//
//        if (isCompleted) {
//            frameLayout.setVisibility(View.GONE);
////            setMarginForView(60);
////            setMinHeightForLayout(0);
//        }
//
//        // transaction.commit();
    }

    public void openVideoView(String videoData) {
        if (videoContentArrayList != null && videoData != null) {
            String[] videoKeySeparated = videoData.split("-");
            videoLevel = Integer.parseInt(videoKeySeparated[0]);
            videoPosition = Integer.parseInt(videoKeySeparated[1]);
            for (int i = 0; i < videoContentArrayList.size(); i++) {
                if (videoContentArrayList.get(i).containsKey(videoLevel + "-" + videoPosition)) {
                    HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(i).get(videoLevel + "-" + videoPosition);
                    String offlineLink = (String) data.get("offlineLink");
                    String onlineLink = (String) data.get("onlineLink");
                    String key = (String) data.get("key");
                    data.put("isSelected", "true");

                    String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                    File file = new File(filePath);
                    String url;
                    boolean isLocalFile;
                    if (file.exists() && Util.isOfflineMode(context)) {
                        url = file.toString();
                        isLocalFile = true;
                    } else {
                        String[] urlArray = onlineLink.split("/");
                        url = urlArray[urlArray.length - 1];
                        isLocalFile = false;
                    }
                    videoKey = videoLevel + "-" + videoPosition;
                    if (Util.getIsFullScreen(context)) {
                        openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null, key);
                        openVimeoVideoFullScreenFragment(context);
                    } else {
                        openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null, key);
                        //openVimeoVideoFullScreenFragment();
                    }

                    break;
                } else {
                    if (i == videoContentArrayList.size() - 1) {
                        videoLevel = Integer.parseInt(videoKeySeparated[0]) + 1;
                        videoPosition = 0;
                        for (int j = 0; j < videoContentArrayList.size(); j++) {
                            if (videoContentArrayList.get(j).containsKey(videoLevel + "-" + videoPosition)) {
                                HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(j).get(videoLevel + "-" + videoPosition);
                                String offlineLink = (String) data.get("offlineLink");
                                String onlineLink = (String) data.get("onlineLink");
                                String key = (String) data.get("key");
                                String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                File file = new File(filePath);
                                String url;
                                boolean isLocalFile;
                                if (file.exists() && Util.isOfflineMode(context)) {
                                    url = file.toString();
                                    isLocalFile = true;
                                } else {
                                    String[] urlArray = onlineLink.split("/");
                                    url = urlArray[urlArray.length - 1];
                                    isLocalFile = false;
                                }
                                videoKey = videoLevel + "-" + videoPosition;

                                if (Util.getIsFullScreen(context)) {
                                    openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null, key);
                                    openVimeoVideoFullScreenFragment(context);
                                } else {
                                    openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null, key);
                                    //openVimeoVideoFullScreenFragment();
                                }


                                //openVimeoVideoFullScreenFragment();
                                //openFragment(url, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null);
                                break;
                            } else {
                                if (j == videoContentArrayList.size() - 1) {
                                    keys="";
                                    Util.openGifDialogue(context, fourteen);
                                    Toast.makeText(context, fourteen, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String keys="";

    public void openVideoView(String videoData,String type) {

//        current_duration=0;
//        PalContentListingActivity.playedDuration=0;
        if(type!=null) {
            if (videoContentArrayList != null && videoData != null) {
                String[] videoKeySeparated = videoData.split("-");
                videoLevel = Integer.parseInt(videoKeySeparated[0]);
                videoPosition = Integer.parseInt(videoKeySeparated[1]);

                for (int i = 0; i < videoContentArrayList.size(); i++) {
                    if (videoContentArrayList.get(i).containsKey(videoLevel + "-" + videoPosition)) {
                        HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(i).get(videoLevel + "-" + videoPosition);


                        String offlineLink = (String) data.get("offlineLink");
                        String onlineLink = (String) data.get("onlineLink");

                        String key = (String) data.get("key");
                        data.put("isSelected", "true");
                        keys=key;
                        String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                        File file = new File(filePath);
                        String url;
                        boolean isLocalFile;

                        if(onlineLink.contains("https://vimeo.com/")) type="video_lessons";
                        else type="diksha_content";

                        if(type.equals("video_lessons"))
                        {
                            if (file.exists() && Util.isOfflineMode(context)) {
                                url = file.toString();
                                isLocalFile = true;
                            } else {
                                String[] urlArray = onlineLink.split("/");
                                url = urlArray[urlArray.length - 1];
                                isLocalFile = false;
                            }

                        }
                        else
                        {
                            url = onlineLink;
                            isLocalFile = false;

                        }
                        videoKey = videoLevel + "-" + videoPosition;
                        Util.setVideoLevel(context,videoLevel-1);
                        if (Util.getIsFullScreen(context)) {
                            openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, type, key);
                            openVimeoVideoFullScreenFragment(context);
                        } else {
                            openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, type, key);
                            //openVimeoVideoFullScreenFragment();
                        }

                        break;
                    } else {

                        /* show dialog */

                        if (i == videoContentArrayList.size() - 1) {
                            videoLevel = Integer.parseInt(videoKeySeparated[0]) + 1;
                            videoPosition = 0;
                            for (int j = 0; j < videoContentArrayList.size(); j++) {
                                if (videoContentArrayList.get(j).containsKey(videoLevel + "-" + videoPosition)) {
                                    HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(j).get(videoLevel + "-" + videoPosition);
                                    String offlineLink = (String) data.get("offlineLink");
                                    String onlineLink = (String) data.get("onlineLink");
                                    String key = (String) data.get("key");
                                    keys=key;
                                    String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                    File file = new File(filePath);
                                    String url;
                                    boolean isLocalFile;
                                    if(onlineLink.contains("https://vimeo.com/")) type="video_lessons";
                                    else type="diksha_content";

                                    if(type.equals("video_lessons"))
                                    {
                                        if (file.exists() && Util.isOfflineMode(context)) {
                                            url = file.toString();
                                            isLocalFile = true;
                                        } else {
                                            String[] urlArray = onlineLink.split("/");
                                            url = urlArray[urlArray.length - 1];
                                            isLocalFile = false;
                                        }

                                    }
                                    else
                                    {
                                        url = onlineLink;
                                        isLocalFile = false;

                                    }
                                    videoKey = videoLevel + "-" + videoPosition;
                                    Util.setVideoLevel(context,videoLevel-1);
                                    if (Util.getIsFullScreen(context)) {
                                        openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, type, key);
                                        openVimeoVideoFullScreenFragment(context);
                                    } else {
                                        openFragment(url, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, type, key);
                                        //openVimeoVideoFullScreenFragment();
                                    }


                                    //openVimeoVideoFullScreenFragment();
                                    //openFragment(url, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null);
                                    break;
                                } else {
                                    if (j == videoContentArrayList.size() - 1) {
                                        keys="";
                                        Util.openGifDialogue(context, fourteen);
                                        Toast.makeText(context, fourteen, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
        else {
            if(keys==null)
            {
//                return;
            }

        }

    }

    public static int playedDuration=0;

    public static void backToNormalView() {
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.VISIBLE);
////        videoName_Text.setVisibility(View.VISIBLE);
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
////for 7 inch tablets
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(350));
//        layoutParams.setMargins(510, 115, 10, 5);
////        setMargins(videoName_Text,10,1050,10,5);
//        setMargins(tabLayout, 10, 10, 10, 5);
//        setMargins(viewPager, 20, 320, 10, 0);
//
//
//        //for 10 inch tablets
//
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(400));
////        layoutParams.setMargins(480, 100, 10, 5);
//////        setMargins(videoName_Text,10,1050,10,5);
////        setMargins(tabLayout, 10, 10, 10, 5);
////        setMargins(viewPager, 20, 320, 10, 0);
//
//        vimeo_video_container.setLayoutParams(layoutParams);
//        vimeo_video_container.setClickable(true);
//        vimeo_video_container.setFocusable(true);
////        ViewGroup parentView = (ViewGroup) findViewById(R.id.rootLayout);
////        parentView.addView(vimeo_video_container, 1);
//
//
//        frameLayout.setVisibility(View.GONE);
//        isFullScreen = false;

//        frameLayout.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                if (iPrepVideoPlayerActivity.videoView.isPlaying()) {
//                    System.out.println("--- playing");
//                } else {
//                    System.out.println("---- not playing");
//
//                    try {
//                        iPrepVideoPlayerActivity.videoView = VideoView_Activity.videoView;
//                        System.out.println("---- done woeking");
//                    } catch (Exception r) {
//                        System.out.println("---- error");
//                        r.printStackTrace();
//                    }
//
//                }
//
//            }
//        }, 2000);//time in milisecond

    }

    public static void openVimeoVideoFullScreenFragment(Context context) {
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.VISIBLE);
////        videoName_Text.setVisibility(View.VISIBLE);
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
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(1500, 800);
//        layoutParams.setMargins(0, 0, 0, 0);
////        setMargins(videoName_Text,10,1050,10,5);
//        setMargins(tabLayout, 10, 5, 10, 5);
//        setMargins(viewPager, 20, 300, 10, 0);
//
//        vimeo_video_container.setLayoutParams(layoutParams);
//        vimeo_video_container.setClickable(true);
//        vimeo_video_container.setFocusable(true);
//        ViewGroup parentView = (ViewGroup) findViewById(R.id.rootLayout);
//        parentView.addView(vimeo_video_container, 1);

//        Intent intent = new Intent(context, VideoView_Activity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        System.out.println("========= bundl "+bundl);
//        bundl.putInt("duration", duration);
//        bundl.putInt("playedDuration", playedDuration);
//        bundl.putBoolean("from_smallScreen", true);
//        intent.putExtras(bundl);
//
//        try {
//            iPrepVideoPlayerActivity.videoView.pause();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        context.startActivity(intent);
//        frameLayout.setVisibility(View.GONE);
//        isFullScreen = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation=newConfig;
    }


    @SuppressLint("SourceLockedOrientationActivity")
    public void rotate_screen() {
        System.out.println("----- orientation "+orientation);
        try {
            if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setlayout_forportrait();
            }
            else if (orientation.orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setlayout_forlandscape();
            }
        }catch (Exception ee)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setlayout_forlandscape();
        }
    }

    private static int width,height=630;

    private void setlayout_forlandscape() {
        try {
            setMargins(reletiveVideoView,150,0,150,0);
        } catch (Exception e) {

        }

        height=frameLayout.getHeight();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        title_Bar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        frameLayout.setLayoutParams(layoutParams);
        Util.setIsFullScreen(context,true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void setlayout_forportrait() {
        try {
            setMargins(reletiveVideoView,0,0,0,0);
        } catch (Exception e) {

        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        title_Bar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        frameLayout.setLayoutParams(layoutParams);
        Util.setIsFullScreen(context,false);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void showMessageLayput(String type) {

        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof PalVideoListFragment) {
                if (type.equals("foundationalPractice")) {
                    if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
                        for (FoundationalTopicModel item : foundationalTopicData) {
                            if (lastTopicId.equals(item.getSeniorTopicID())) {
                                if (!isPracticeCompleted(item.getTopicId())) {
                                    int position = 0;

                                    completeType = "foundationalPractice";
                                    PalContentListingActivity_Mobile.static_completeType = "foundationalPractice";
                                    for (int i = 0; i < PalContentListingActivity_Mobile.topicsArrayList.size(); i++) {
                                        if (PalContentListingActivity_Mobile.topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
                                            position = i;
                                            break;
                                        }
                                    }

                                    ((PalVideoListFragment) fragment).showTestLayout(type, item, position);
                                }
                            }
                        }
                    }
                } else {
                    ((PalVideoListFragment) fragment).showTestLayout(type, null, 0);
                }
            }
        }
    }

    public void showFoundationalTopicDialog(String userId, String subjectId, String seniorTopicId, String topicId, String topicName, String seniorClass, int type, int position, int mastrey, String streekprogress, int level) {

        if(PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime) {
            PalContentListingActivity_Mobile.palContentListingActivityMobile.foundationalTopicRepository.getFoundationalTopicsDetail(userId, subjectId, seniorTopicId, topicId, seniorClass).observe(PalTopicListingActivity.this, new Observer<List<FoundationalTopicModel>>() {
                @Override
                public void onChanged(List<FoundationalTopicModel> foundationalTopicModels) {
                    if (foundationalTopicModels.size() > 0) {
                        for (int i = 0; i < foundationalTopicModels.size(); i++) {

                            String currentTopicId = foundationalTopicModels.get(i).getSeniorTopicID();

                            Util.setTopicID(context, foundationalTopicModels.get(i).getTopicId());
                            Util.setTopicNameAlt(context, foundationalTopicModels.get(i).getTopicName());
                            Util.setLevel(context, level);

                            global.setProgress(mastrey);

                            if (PalContentListingActivity_Mobile.palContentListingActivityMobile.trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), foundationalTopicData.get(i).getTopicId())) {
                                PalContentListingActivity_Mobile.palContentListingActivityMobile.trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, foundationalTopicData.get(i).getSeniorTopicID(), foundationalTopicData.get(i).getSeniorClass(), foundationalTopicData.get(i).getSeniorTopicName(), foundationalTopicData.get(i).getTopicId(), Util.getSelectedLanguagePackage(context));
                            } else {
                                TrackTopicModel trackTopicModel = new TrackTopicModel();
                                trackTopicModel.setUserId(Util.getUserId(context));
                                trackTopicModel.setSubject(Util.getSubject(context));
                                trackTopicModel.setBoard(board);
                                trackTopicModel.setSeniorTopicId(foundationalTopicData.get(i).getSeniorTopicID());
                                trackTopicModel.setSeniorClass(foundationalTopicData.get(i).getSeniorClass());
                                trackTopicModel.setSeniorTopicName(foundationalTopicData.get(i).getSeniorTopicName());
                                trackTopicModel.setFoundationalTopicId(foundationalTopicData.get(i).getTopicId());
                                trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                                PalContentListingActivity_Mobile.palContentListingActivityMobile.trackTopicRepository.insertDetails(trackTopicModel);
                            }

                            HashMap<String, String> mapF = new HashMap<>();
                            mapF.put("foundational_class", foundationalTopicModels.get(i).getSClass());
                            mapF.put("foundational_topic", foundationalTopicModels.get(i).getTopicId());
                            global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(currentTopicId).setValue(mapF);
                            if (Util.isNetworkAvailable(context)) {
                                if(PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime)
                                {
                                    PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime=false;
                                    if(Util.isPortraitMode(context)) startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class).putExtra("sClass", foundationalTopicModels.get(i).getSClass()).putExtra("streakProgress", streekprogress).putExtra("streak", foundationalTopicModels.get(i).getStreakCount()).putExtra("incorrectStreak", foundationalTopicModels.get(i).getIncorrectStreak()).putExtra("practiceType", "junior").putExtra("seniorClass", foundationalTopicModels.get(i).getSeniorClass()).putExtra("seniorTopicID", foundationalTopicModels.get(i).getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicModels.get(i).getSeniorTopicName()).putExtra("testPercentageAchieved", mastrey).putExtra("type", "foundation"));
                                    else startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", foundationalTopicModels.get(i).getSClass()).putExtra("streakProgress", streekprogress).putExtra("streak", foundationalTopicModels.get(i).getStreakCount()).putExtra("incorrectStreak", foundationalTopicModels.get(i).getIncorrectStreak()).putExtra("practiceType", "junior").putExtra("seniorClass", foundationalTopicModels.get(i).getSeniorClass()).putExtra("seniorTopicID", foundationalTopicModels.get(i).getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicModels.get(i).getSeniorTopicName()).putExtra("testPercentageAchieved", mastrey).putExtra("type", "foundation"));
                                    
                                }

                            } else {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                    Util.openGifDialogue(context, "इंटरनेट कनेक्शन काम नहीं कर रहा");
                                } else {
                                    Util.openGifDialogue(context, "Internet Connection is not working");
                                }
                            }

                            break;

                        }
                    }
                    else {
                        if (type == 1) {
                            PalContentListingActivity_Mobile.palContentListingActivityMobile.onClickHandler(position);
                        } else {
//                        Toast.makeText(context, hundredPercentMastery + topicName + ".", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }
    }

    public void playLevelVideo() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    PalVideoListFragment.palVideoListFragment.getVideos();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(videoKey != null){
                    int videoLevel = Util.getVideoLevel(context)+1;
                    int videoPosition = 0;
                    videoKey = videoLevel + "-" + videoPosition;
                    openVideoView(videoKey,"video_lessons");
                    PracticeTopicActivity.autoplayLevelVideo=false;
//                    practiceTopicAdapter.notifyDataSetChanged();
                }
            }
        }, 2000);
    }

    public void checkAndStartPractice() {
        if(PalVideoListFragment.ttype.equals("foundationalPractice")) {
            PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime=true;
            int mastery = 0 ;
            int level = 1 ;
            String streekProgress = "0" ;
            for(int o = practiceScoreModelArrayList.size()-1; o>=0; o--) {

                PracticeScoreModel data = practiceScoreModelArrayList.get(o);
                if (data.getTopicId().equals(PalVideoListFragment.foundationalTopicModel.getTopicId())) {
                    mastery=  Integer.parseInt(practiceScoreModelArrayList.get(o).getScore());
                    level=  Integer.parseInt(practiceScoreModelArrayList.get(o).getCurrentLevel());
                    streekProgress= practiceScoreModelArrayList.get(o).getStreakProgress();
                    break;
                }
            }
            PalTopicListingActivity.palTopicListingActivity.showFoundationalTopicDialog(Util.getUserId(context), subject, PalVideoListFragment.foundationalTopicModel.getSeniorTopicID(), PalVideoListFragment.foundationalTopicModel.getTopicId(), PalVideoListFragment.foundationalTopicModel.getTopicName(), PalVideoListFragment.foundationalTopicModel.getSeniorClass(), 0, PalVideoListFragment.pposition,mastery,streekProgress,level);

        }
        else {
            PalContentListingActivity_Mobile.palContentListingActivityMobile.startPractice(PalContentListingActivity_Mobile.palContentListingActivityMobile.topicID);
        }
    }

    @Override
    public void onBackPressed() {
        PalContentListingActivity_Mobile.palContentListingActivityMobile.reloadLayout=true;

        if(frameLayout.getVisibility()==View.VISIBLE) removeFragment();
        else finish();
    }
}