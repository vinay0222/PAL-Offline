package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.messageLayout1;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.messageLayout2;
import static com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment.messageLayoutidvisible;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.adapter.PalContentPagerAdaper;
import com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter;
import com.idreameducation.ipreppal.pal.fragments.PalDikshaContentFragment;
import com.idreameducation.ipreppal.pal.fragments.PalPracticeFrament;
import com.idreameducation.ipreppal.pal.fragments.PalTestFrament;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;
import com.idreameducation.ipreppal.roomdatabase.model.FinalTestCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.LastTopicDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicPathModel;
import com.idreameducation.ipreppal.roomdatabase.model.TestModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicPositionWiseModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicSubjectWiseModel;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FinalTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.LastTopicDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicPathRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TestDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicPositionRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicSubjectRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TrackTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoDetailsRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity;
import com.skydoves.balloon.ArrowOrientation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class PalContentListingActivity extends AppCompatActivity  {

    private Global global;
    public static Context context;
    public static PalContentListingActivity palContentListingActivity;
    public static PracticeTopicAdapter practiceTopicAdapter;
    public static PalContentListingActivity instance;
    private RequestOptions requestOptions;
    public String clss,subjec,topicName,topic_id,language,lastTopicId,completeType,chaptersText,linkFromFirebaseStorage, textSendWithImageTextString,mastery = "0",valueSend, topicID, userIDSend;
    public static String board,subjectName,sClass,icon,color,subject,currentvideo_url,static_completeType,currentTopicid;
    public static String videoKey="";
    public static String one, two, three, four, five, six, seven, eight, nine, ten, eleven,
            tweleve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, ninteen, twenty,twentyOne, twentyTwo, twentyThree;
    public static boolean backPressed,refreshAdapter,isFullScreen=false;
    public static FrameLayout vimeo_video_container, frameLayout;
    public static ArrayList<HashMap<String, Object>> videoContentArrayList;
    public static ArrayList<HashMap<String, Object>> dikshaContentArrayList_;
    public static ArrayList<PracticeScoreModel> practiceScoreModelArrayList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> topicsArrayList;
    public static Bundle bundl = new Bundle();
    public static int duration = 0,current_duration = 0;
    private TextView textViewSubjectName, selectTopicTitle,textViewChapterCount;
    private TextView textViewResumePractice,textViewPractice,loading_text,screenshotText, callText;
    private ImageView imageViewBack,imageViewSubject,imageViewToggle;
    private VideoDetailsRepository videoDetailsRepository;
    private FoundationalTopicRepository foundationalTopicRepository;
    private TestDetailsRepository testDetailsRepository;
    private TopicSubjectRepository topicSubjectRepository;
    private TopicRepository topicRepository;
    private TopicPositionRepository topicPositionRepository;
    private ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;
    private ReportsTopicPathRepository reportsTopicPathRepository;
    private TrackTopicRepository trackTopicRepository;
    private ReportsLatestDataTestRepository reportsLatestDataTestRepository;
    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;
    private LastTopicDetailsRepository lastTopicDetailsRepository;
    private FinalTestCompleteRepository finalTestCompleteRepository;

    private Disposable pathDisposable;
    private Disposable lastTopicIdDisposable;
    private Disposable videoListingTopicDisposable;
    private Disposable foundationalTopicDisposable;
    private Disposable diagnosticTestCompleteDisposable;
    private Disposable testScoreTopicDisposable;
    private Disposable practiceScoreTopicDisposable;
    private Disposable finalTestCompleteDisposable;

    public  ArrayList<String> textArrayList;
    public  ArrayList<HashMap<String, Object>> contentArrayList;
    public  ArrayList<HashMap<String, Object>> contentArrayList_;
    public  ArrayList<HashMap<String, Object>> dikshaContentArrayList;
    public  ArrayList<TestScoreModel> testScoreModelArrayList = new ArrayList<>();
    public  ArrayList<String> finalTestTopicIdArrayList=new ArrayList<>();
    public  ArrayList<VideoModel> topicVideoArrayList = new ArrayList<>();
    public  ArrayList<String> onboading_textList=new ArrayList<>();
    public  List<FoundationalTopicModel> foundationalTopicData;
    private ArrayList<TopicSubjectWiseModel> lastTopicIdList;
    private final TreeSet<String> topicPathData = new TreeSet<>();

    private int videoLevel,videoPosition,tabposition=0;
    public  int nextTopicPosition = 0;

    private RecyclerView recyclerView;
    public  ViewPager viewPager;
    private TabLayout tabLayout;

    public  FrameLayout leftTransparentBg,rightTransparentBg,fullTransparentBg;
    private Fragment palVideoPlayerActivity;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private LinearLayout linearLayoutWeight,layout_connection,linearSecond;
    private RelativeLayout practiceLayout,togglePracticeLayout,linearLayout;
    private  RelativeLayout reletiveLayout,videoViewLayout;

    private final Handler handler = new Handler();
    private ImageView imageViewCrossVideo2;
    private ProgressDialog progressDialog;
    private HashMap<String, Object> data;
    private TextView slow_internet_Text;
    private ProgressBar mProgressBar;

    private Uri filePath;
    private ViewGroup progressView;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Boolean isFabOpen = false;
    private FloatingActionButton supportBtn;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private final int PICK_IMAGE_REQUEST = 22;
    private int count = 0,countTopic = 0;
    public  boolean isCompleted = false,done = false;
    protected boolean isProgressShowing = false;
    private boolean isloading = true,first = true;
    public static boolean remove_fragment=false;

    private final int[] tabIcons = { R.drawable.video
            , R.drawable.practice
            , R.drawable.test
            , R.drawable.ic_bookscat};

    private final int[] tabIcons_unselected = { R.drawable.video_1
            , R.drawable.practice_1
            , R.drawable.test_1
            , R.drawable.ic_bookscat_1};

    public PalContentListingActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_activity_content);
        assignIds();
        listners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.preventPause(context,getTaskId());
        try {
            duration = iPrepVideoPlayerActivity.videoView.getDuration();
        } catch (Exception R) {}
        tabposition= tabLayout.getSelectedTabPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Util.setLastTopicID(context, null);
//        Util.setDefaultTopicComplete(context, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Util.setWindowSettings(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {

        }
        Util.setContext(context);
        hideStatusBar();
        try {
            duration = iPrepVideoPlayerActivity.videoView.getDuration();
        } catch (Exception F) {
        }
        if (backPressed) {

            try {
                refreshListing();
                backPressed = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if(remove_fragment) removeFragment();
        }

        if (Util.isToolTipContentToBeShown(context)) {
            if (Util.toolTip != null) {
                Util.toolTip.dismiss();
                onlyhideTransparentScreenForTopicSelection();
            }
            showToolTipForDiagnosticTest();

        }



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFullScreen) {
            smallScreen(true);
        } else {
            if (frameLayout.getVisibility() == View.VISIBLE) {
                PalContentListingActivity.keys = "";
                try {
                    PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removeFragment(true);
                //setMargins(tabLayout, 20, 15, 20, 0);
            } else {
//                super.onBackPressed();
                finish();

            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("getPath")) {
            if (intent.getStringExtra("getPath").equals("yes")) {
                try {
                    refreshListing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Intent aa = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
        startActivityForResult(aa, 123);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                // imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void assignIds() {
        context = this;
        instance = this;
        palContentListingActivity = this;

        board = Util.getSelectedBoard(context);
        sClass = Util.getSelectedClass(context);
        language = Util.getSelectedLanguagePackage(context);
        if(color==null) color=Util.getColour(context);
        global = (Global) getApplicationContext();

        Util.setContext(context);
        Util.setToolTipContentScreen(context,false);



        /** show OnBoarding */
        if(!Util.isContentonboadingDone(context))
            showOnBoarding();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        vimeo_video_container = new FrameLayout(context);
        vimeo_video_container.setId(R.id.vimeo_video_container);
        setOnBoardingTextList();

        /** getting data from intent */
        subjectName = getIntent().getStringExtra("subjectName");
        subject = getIntent().getStringExtra("subject");
        icon = getIntent().getStringExtra("icon");
        color = getIntent().getStringExtra("color");
        lastTopicId = getIntent().getStringExtra("lastTopicId");

        /** init views */
        frameLayout = findViewById(R.id.container);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewSubject = findViewById(R.id.imageViewSubject);
        textViewSubjectName = findViewById(R.id.textViewSubjectName);
        selectTopicTitle = findViewById(R.id.selectTopicTitle);
        textViewChapterCount = findViewById(R.id.textViewChapterCount);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        frameLayout = findViewById(R.id.container);
        reletiveLayout = findViewById(R.id.reletiveLayout);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayoutWeight = findViewById(R.id.linearLayoutWeight);
        practiceLayout = findViewById(R.id.practiceLayout);
        textViewResumePractice = findViewById(R.id.textViewResumePractice);
        textViewPractice = findViewById(R.id.textViewPractice);
        togglePracticeLayout = findViewById(R.id.togglePracticeLayout);
        imageViewToggle = findViewById(R.id.imageViewToggle);
        recyclerView = findViewById(R.id.recyclerView);
        leftTransparentBg = findViewById(R.id.leftTransparentBg);
        rightTransparentBg = findViewById(R.id.rightTransparentBg);
        fullTransparentBg = findViewById(R.id.fullTransparentBg);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);
        linearSecond = findViewById(R.id.linearSecond);
        layout_connection = findViewById(R.id.layout_connection);
        textViewChapterCount.setVisibility(View.GONE);

        reletiveLayout=findViewById(R.id.reletiveLayout);
        videoViewLayout=findViewById(R.id.videoViewLayout);

        videoDetailsRepository = new VideoDetailsRepository(context);
        foundationalTopicRepository = new FoundationalTopicRepository(context);
        testDetailsRepository = new TestDetailsRepository(context);
        topicSubjectRepository = new TopicSubjectRepository(context);
        topicRepository = new TopicRepository(context);
        topicPositionRepository = new TopicPositionRepository(context);
        reportsDiagnosticTestCompleteRepository = new ReportsDiagnosticTestCompleteRepository(context);
        reportsTopicPathRepository = new ReportsTopicPathRepository(context);
        trackTopicRepository = new TrackTopicRepository(context);
        reportsLatestDataTestRepository = new ReportsLatestDataTestRepository(context);
        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);
        lastTopicDetailsRepository = new LastTopicDetailsRepository(context);
        finalTestCompleteRepository = new FinalTestCompleteRepository(context);

        supportBtn = findViewById(R.id.supportBtn);
        supportBtn.setVisibility(View.VISIBLE);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                System.out.println("----- clicked ");
                animateFAB();
            }
        });

        try {
            setStaticText(); } catch (Exception e) {
            e.printStackTrace();
        }

        // not in use
        togglePracticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (imageViewToggle.getRotation() == 270) {
                    hidePracticeLayout();
                    imageViewToggle.setRotation(90);
                } else if (imageViewToggle.getRotation() == 90) {
                    showPracticeLayout();
                    imageViewToggle.setRotation(270);
                }
            }
        });

        hideNavigationBar(getWindow());
        smallScreen(false);

        /** setup recyclerview for showing topic listing */
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(PalContentListingActivity.this);
        progressDialog.setMessage(one);
        progressDialog.setCancelable(false);
        selectTopicTitle.setText(two);

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            progressDialog.setMessage("कृपया प्रतीक्षा करें...");
            selectTopicTitle.setText("सबसे पहले उस अध्याय का चयन करें जिसे आप पढ़ना चाहते हैं।");
        } else {
            progressDialog.setMessage("Please wait...");
            selectTopicTitle.setText("First select the chapter you want to read.");
        }

        /** set subject name and colour  */
        textViewSubjectName.setText(subjectName.substring(0, 1).toUpperCase() + subjectName.substring(1).replace("_"," "));
        if(color==null) color=Util.getColorOffline(Util.getSubjectId(context));
        global.setColor(color);
        textViewSubjectName.setTextColor(Color.parseColor(color));
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        get_support_dialog_language();
    }

    private void setOnBoardingTextList() {
        if(!Util.isOfflineMode(context)) global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("OnBoardingText").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        /** getting text from firebase */
                        onboading_textList = (ArrayList<String>) dataSnapshot.getValue();
                        System.out.println("------ onboading_textList "+onboading_textList);

                        int max = onboading_textList.size()-1;
                        int min =0;
                        int randomNumber = (int) (Math.random()*(max-min)) + min;
                        loading_text.setText(onboading_textList.get(randomNumber));

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
        loading_text = findViewById(R.id.loading_text);
        int max = onboading_textList.size()-1;
        int min =0;
        int randomNumber = (int) (Math.random()*(max-min)) + min;
        loading_text.setText(onboading_textList.get(randomNumber));
    }

    @SuppressLint("RestrictedApi")
    public void animateFAB() {
        buttonScreenshot(null);
    }

    private void openContactUsDialogue() {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogue_contact_us);
        TextView textViewMessge1 = dialog.findViewById(R.id.textViewMessge1);
        TextView textViewMessge2 = dialog.findViewById(R.id.textViewMessge2);
        TextView textViewMessge3 = dialog.findViewById(R.id.textViewMessge3);
        TextView textViewMessge4 = dialog.findViewById(R.id.textViewMessge4);
        TextView call1TextView = dialog.findViewById(R.id.call1TextView);
        TextView WhatsappTextView = dialog.findViewById(R.id.WhatsappTextView);
        TextView EmailTextView = dialog.findViewById(R.id.EmailTextView);
        textViewMessge1.setText(Html.fromHtml(heading1Text));
        textViewMessge2.setText(Html.fromHtml(PracticeTopicActivity.heading2Text));
        textViewMessge3.setText(Html.fromHtml(heading3Text));
        textViewMessge4.setText(Html.fromHtml(heading4Text));
        call1TextView.setText(Html.fromHtml(button1Text));
        WhatsappTextView.setText(Html.fromHtml(button2Text));
        EmailTextView.setText(Html.fromHtml(button3Text));
        ImageView imageViewCrossDialogue = dialog.findViewById(R.id.imageViewCrossDialogue);

        call1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    callAtRuntime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Util.preventTwoClick(view);
            }
        });

        WhatsappTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+91" + callNo + "&text=Hey%20there%")));
                Util.preventTwoClick(view);
            }
        });

        EmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "share@idreameducation.org", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from - " + Util.getUsername(context));
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                Util.preventTwoClick(view);
            }
        });

        imageViewCrossDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Util.preventTwoClick(view);
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void callAtRuntime() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + callNo));
        startActivity(callIntent);
    }

    public void buttonScreenshot(View view) {
        View view1 = getWindow().getDecorView().getRootView();
        view1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String filePath = Environment.getExternalStorageDirectory() + "/Download/" + timeStamp + ".jpg";
        File fileScreenshot = new File(filePath);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Util.openCapturedScreenshot(context,fileScreenshot);


    }

    public static ArrayList<String> support_Language;

    private void get_support_dialog_language() {
        support_Language=new ArrayList<>();

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("SupportDialog");
                support_Language = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    support_Language.add(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else FirebaseDatabase.getInstance().getReference().child("screen_text/student/1/").child(Util.getSelectedLanguage(context))
                .child("SupportDialog").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot!=null)
                        {
                            support_Language= (ArrayList<String>) snapshot.getValue();
                            System.out.println( "------------ support_Language "+support_Language);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void openCapturedScreenshot(File fileScreenshot) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogue_send_screenshot);


        // SETTING TEXT --------

        TextView heading = dialog.findViewById(R.id.heading);
        TextView send_feed_text = dialog.findViewById(R.id.send_feed_text);
        TextView inc_screentext = dialog.findViewById(R.id.inc_screentext);
        TextView contect_us_text = dialog.findViewById(R.id.contect_us_text);
        TextView contect_us_text2 = dialog.findViewById(R.id.contect_us_text2);
        EditText textSendWithImage__ = dialog.findViewById(R.id.textSendWithImage_);
        TextView number_text=dialog.findViewById(R.id.number_text);
        TextView email_text=dialog.findViewById(R.id.email_text);

        Button textViewCancel = dialog.findViewById(R.id.textViewCancel);
        Button textViewSend = dialog.findViewById(R.id.textViewSend);

        heading.setText(support_Language.get(0));
        send_feed_text.setText(support_Language.get(1));
        textSendWithImage__.setHint(support_Language.get(2));
        inc_screentext.setText(support_Language.get(3));
        contect_us_text.setText(support_Language.get(4));
        contect_us_text2.setText(support_Language.get(5));
        number_text.setText(support_Language.get(6));
        email_text.setText(support_Language.get(7));
        textViewCancel.setText(support_Language.get(8));
        textViewSend.setText(support_Language.get(9));




        ImageView imageViewScreenshot = dialog.findViewById(R.id.imageViewScreenshot);
        ImageView close_pop = dialog.findViewById(R.id.close_pop);
//        ImageView attachScreenshot = dialog.findViewById(R.id.attachScreenshot);

//        attachScreenshot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SelectImage();
//                Util.preventTwoClick(view);
//            }
//        });




        Uri uri = Uri.fromFile(fileScreenshot);
        filePath = uri;
        Glide.with(context).load(uri).into(imageViewScreenshot);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Util.preventTwoClick(view);
            }
        });

        close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Util.preventTwoClick(view);
            }
        });




        textViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                textSendWithImageTextString = textSendWithImage__.getText().toString();
                uploadImage(dialog);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                supportBtn.setImageResource(R.drawable.ic_inactive);
                isFabOpen = false;
            }
        });

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialog.show();
        /* Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


    }


    private void sendDataToFirebaseOfIssueReported() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        long millis = new Date().getTime();



        HashMap<String, String> hashMapToSync = new HashMap<>();
        hashMapToSync.put("user_id",Util.getUserId(context));
        hashMapToSync.put("userToken",Util.getToken(context));
        hashMapToSync.put("device_id",Util.getAndroidId(context));
        hashMapToSync.put("user_class",Util.getSelectedClass(context));
        hashMapToSync.put("user_board",Util.getSelectedBoard(context));
        hashMapToSync.put("user_language",Util.getSelectedLanguage(context));
        hashMapToSync.put("date_of_reported_issue",formattedDate);
        hashMapToSync.put("timestamp",""+millis);
        hashMapToSync.put("text_by_users",textSendWithImageTextString);
        hashMapToSync.put("image_by_users",linkFromFirebaseStorage);
        hashMapToSync.put("status","");
        hashMapToSync.put("user_name",Util.getUsername(context));
        hashMapToSync.put("district",Util.getDistrict(context));
        hashMapToSync.put("school_name",Util.getSchoolName(context));
        hashMapToSync.put("state",Util.getSelectedState(context));
        hashMapToSync.put("issue_type","Screenshot");
        hashMapToSync.put("project_name",Util.getProjectName(context)); // project name*

        hashMapToSync.put("schoolID", Util.getSchoolId(context));
        hashMapToSync.put("projectId", Util.getProjectId(context));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("support")
                .document()
                .set(hashMapToSync);

//         global.getDatabaseReference().child("issue_raised_in_pal_application").child(Util.getSchoolId(context)).child(Util.getUserId(context)).child(""+millis).setValue(hashMapToSync);


    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(Dialog dialog) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "prabhakar_images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUri = uri;
                                            linkFromFirebaseStorage = String.valueOf(downloadUri);
                                            sendDataToFirebaseOfIssueReported();
                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
//                                    Toast.makeText(PracticeTopicActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();


                                    Util.openGifDialogueSuccess(context, PracticeTopicActivity.textToSend);
                                    dialog.dismiss();


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Util.openGifDialogue(context, "Some error occured !");
//                            Toast
//                                    .makeText(PracticeTopicActivity.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    private void listners() {

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        videoImageViewBack.setOnClickListener(this);
    }

    private void checkConnection(boolean first) {
        setOnBoardingTextList();

        System.out.println("-------- checking connection ");

        mProgressBar = findViewById(R.id.progressBar);
        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);
        loading_text = findViewById(R.id.loading_text);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isloading) {
                    if (first) {
                        mProgressBar.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);
                        slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
                        slow_internet_Text.setVisibility(View.VISIBLE);
                        imageViewCrossVideo2.setVisibility(View.VISIBLE);
                    } else {
                        checkConnection(true);
                        slow_internet_Text.setVisibility(View.VISIBLE);
                        loading_text.setVisibility(View.GONE);
                    }

                } else {
                    mProgressBar.setVisibility(View.GONE);
                    slow_internet_Text.setVisibility(View.GONE);
                    imageViewCrossVideo2.setVisibility(View.GONE);
                    layout_connection.setVisibility(View.GONE);
                }
            }
        }, 10000);//time in milisecond

        imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layout_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-");
            }
        });

//        hide_connectionlayout();

    }

    private void hide_connectionlayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                slow_internet_Text.setVisibility(View.GONE);
                imageViewCrossVideo2.setVisibility(View.GONE);
                layout_connection.setVisibility(View.GONE);
            }
        },100);
    }

    public void setStaticText() {
        isloading = true;
        checkConnection(false);

        if (Util.isOfflineMode(context)) try {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";                   // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);                      // read file
            JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));   // filter by language
            JSONArray array = object.getJSONArray("content_listing_screen");             // filter by content_listing_screen

            /** textArrayList => contain text */
            textArrayList = new ArrayList<>();

            /** adding text in list */
            for (int i = 0; i < array.length(); i++) textArrayList.add(array.getString(i));

            /** set text in views from list */
            if (textArrayList.size() > 0) {
                one = textArrayList.get(0);
                two = textArrayList.get(1);
                three = textArrayList.get(2);
                four = textArrayList.get(3);
                five = textArrayList.get(4);
                six = textArrayList.get(5);
                seven = textArrayList.get(6);
                eight = textArrayList.get(7);
                nine = textArrayList.get(8);
                ten = textArrayList.get(9);
                eleven = textArrayList.get(10);
                tweleve = textArrayList.get(11);
                thirteen = textArrayList.get(12);
                fourteen = textArrayList.get(13);
                fifteen = textArrayList.get(14);
                sixteen = textArrayList.get(15);
                seventeen = textArrayList.get(16);
                eighteen = textArrayList.get(17);
                ninteen = textArrayList.get(18);
                twenty = textArrayList.get(19);
                twentyOne = textArrayList.get(23);
                twentyTwo = textArrayList.get(24);
                twentyThree = textArrayList.get(25);
//                                adapterfour = textArrayList.get(23);
//                                adapterfive = textArrayList.get(24);
//                                adaptersix = textArrayList.get(25);
//                                adapterseven = textArrayList.get(26);
//                                adaptereight = textArrayList.get(27);
//                                adapternine = textArrayList.get(28);

            }

            /** get topics details */
            getTopics(subject);
            getTopicSeenVideoListing(false);


        } catch (Exception e) {
            e.printStackTrace();
        }
        else global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("content_listing_screen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        /** getting text from firebase */
                        textArrayList = (ArrayList<String>) dataSnapshot.getValue();

                        /** set text in views from list */
                        if (textArrayList.size() > 0) {
                            one = textArrayList.get(0);
                            two = textArrayList.get(1);
                            three = textArrayList.get(2);
                            four = textArrayList.get(3);
                            five = textArrayList.get(4);
                            six = textArrayList.get(5);
                            seven = textArrayList.get(6);
                            eight = textArrayList.get(7);
                            nine = textArrayList.get(8);
                            ten = textArrayList.get(9);
                            eleven = textArrayList.get(10);
                            tweleve = textArrayList.get(11);
                            thirteen = textArrayList.get(12);
                            fourteen = textArrayList.get(13);
                            fifteen = textArrayList.get(14);
                            sixteen = textArrayList.get(15);
                            seventeen = textArrayList.get(16);
                            eighteen = textArrayList.get(17);
                            ninteen = textArrayList.get(18);
                            twenty = textArrayList.get(19);
                            twentyOne = textArrayList.get(23);
                            twentyTwo = textArrayList.get(24);
                            twentyThree = textArrayList.get(25);
                        }

                        /** getting path */
                        getPath(subject);

                    }
                } catch (Exception e) {
                    Util.dismissdataDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
    }

    public void getFoundationTopicDetails(String topicID) {
        first = true;
        clss = sClass;
        subjec = subject;
        currentTopicid = topicID;

        try {

            getList(userIDSend, board, sClass, subject, topicID, valueSend, "foundationalTopicData").subscribeOn(Schedulers.io())
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
                                                            try {
                                                                getVideos(currentTopicid);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
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

                                                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                                                for (Fragment fragment : allFragments) {
                                                    if (fragment instanceof PalVideoListFragment) {
                                                        ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
                                                    } else if (fragment instanceof PalDikshaContentFragment) {
                                                        ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
                                                    }
                                                }
//                                                    break;
                                            }
                                        }
                                    }
                                }catch (Exception ee){
                                    System.out.println("++++++ handled");
                                }

                            }

                            try { if(first)
                                getVideos(currentTopicid);
                            } catch (Exception e) {
                                e.printStackTrace();
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


        }
        catch (Exception f){}
    }

    private void getVideos(String topicID) throws Exception {

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/Class" + clss + "_core_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            JSONObject object__ = jsonObject.getJSONObject(language);
            JSONObject object___ = object__.getJSONObject("video_lessons");
            JSONObject object____ = object___.getJSONObject("content");
            JSONObject object_____ = object____.getJSONObject(subjec);
            JSONObject object______ = object_____.getJSONObject("topics");
            contentArrayList = new ArrayList<>();
            contentArrayList_ = new ArrayList<>();
            videoContentArrayList = new ArrayList<>();


            if (object______.has(currentTopicid)) {
                JSONArray array = object______.getJSONArray(currentTopicid);

                hide_connectionlayout();
                setPagerAdapter();
                setTabLayout();

                for (int i = 1; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                    LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                    for (int j = 0; j < object1.names().length(); j++) {
                        HashMap<String, Object> newHashMap_ = new HashMap<>();
                        String key = object1.names().getString(j);
                        JSONObject object2 = object1.getJSONObject(key);
                        String AssessmentTopicID = object2.getString("AssessmentTopicID");
                        String detail = object2.getString("detail");
                        String name = object2.getString("name");
                        String offlineLink = object2.getString("offlineLink");
                        String offlineThumbnail = object2.getString("offlineThumbnail");
                        String onlineLink = object2.getString("onlineLink");
                        String thumbnail = object2.getString("thumbnail");
                        String topicName = object2.getString("topicName");
                        newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                        newHashMap_.put("detail", detail);
                        newHashMap_.put("name", name);
                        newHashMap_.put("offlineLink", offlineLink);
                        newHashMap_.put("offlineThumbnail", offlineThumbnail);
                        newHashMap_.put("onlineLink", onlineLink);
                        newHashMap_.put("thumbnail", thumbnail);
                        newHashMap_.put("topicName", topicName);
                        newHashMap_.put("topicID", currentTopicid);
                        newHashMap_.put("isSelected", "false");
                        newHashMap_.put("key", key+"");
                        if(onlineLink.contains("vimeo")) {
                            videoHashMap.put(i + "-" + j, newHashMap_);
                            newHashMap.put(key, newHashMap_);
                        }

                    }
                    contentArrayList_.add(newHashMap);
                    videoContentArrayList.add(videoHashMap);

                    System.out.println("---- videos addddeeddddd");

                }

            }

            System.out.println("-------- videos loaded");
//            getDikshaContent(topicID);
        } else {
            String finalTopicid = currentTopicid;
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(clss).child(language).child("video_lessons").child("content").child(subjec).child("topics").child(currentTopicid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            contentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
                            contentArrayList_ = new ArrayList<>();
                            videoContentArrayList = new ArrayList<>();

                            for (int i = 1; i < contentArrayList.size(); i++) {
                                LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                                LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                                HashMap<String, Object> videoMap = contentArrayList.get(i);
                                int j_ = 0;

                                ArrayList<Integer> shortedKey=new ArrayList<>();

                                for(String keys:videoMap.keySet()) {
                                    shortedKey.add(Integer.parseInt(keys));
                                }
                                Collections.sort(shortedKey);

                                for (Integer key : shortedKey) {
                                    HashMap<String, Object> newHashMap_ = new HashMap<>();
                                    HashMap<String, String> innerMap = (HashMap<String, String>) contentArrayList.get(i).get(key+"");
                                    String AssessmentTopicID = innerMap.get("AssessmentTopicID");
                                    String detail = innerMap.get("detail");
                                    String name = innerMap.get("name");
                                    String offlineLink = innerMap.get("offlineLink");
                                    String offlineThumbnail = innerMap.get("offlineThumbnail");
                                    String onlineLink = innerMap.get("onlineLink");
                                    String thumbnail = innerMap.get("thumbnail");
                                    String topicName = innerMap.get("topicName");
                                    newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                                    newHashMap_.put("detail", detail);
                                    newHashMap_.put("name", name);
                                    newHashMap_.put("offlineLink", offlineLink);
                                    newHashMap_.put("offlineThumbnail", offlineThumbnail);
                                    newHashMap_.put("onlineLink", onlineLink);
                                    newHashMap_.put("thumbnail", thumbnail);
                                    newHashMap_.put("topicName", topicName);
                                    newHashMap_.put("topicID", finalTopicid);
                                    newHashMap_.put("isSelected", "false");
                                    newHashMap_.put("key", key+"");
                                    ((HashMap<String, String>) contentArrayList.get(i).get(key+"")).put("isSelected", "false");
                                    ((HashMap<String, String>) contentArrayList.get(i).get(key+"")).put("topicID", finalTopicid);
                                    newHashMap.put(key+"", newHashMap_);
                                    videoHashMap.put(i + "-" + j_, newHashMap_);
                                    j_++;
                                }
                                contentArrayList_.add(newHashMap);
                                videoContentArrayList.add(videoHashMap);
                            }
                            hide_connectionlayout();
                            setPagerAdapter();
                            setTabLayout();
//                            getDikshaContent(topicID);
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
        if(PracticeTopicActivity.autoplayLevelVideo) playLevelVideo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(showingposition);
//                recyclerView.requestFocus();
            }
        }, 4000);//time in milisecond

    }

    private void getDikshaContent(String topicId) throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            JSONObject object__ = jsonObject.getJSONObject(language);
            if (object__.has("diksha_videos")) {
                JSONObject object___ = object__.getJSONObject("diksha_videos");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(subject);
                JSONObject object______ = object_____.getJSONObject("topics");
                dikshaContentArrayList = new ArrayList<>();
                if (object______.has(topicId)) {
                    JSONArray array = object______.getJSONArray(topicId);
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                        LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                        for (int j = 0; j < object1.names().length(); j++) {
                            HashMap<String, Object> newHashMap_ = new HashMap<>();
                            String key = object1.names().getString(j);
                            JSONObject object2 = object1.getJSONObject(key);
                            String AssessmentTopicID = object2.getString("AssessmentTopicID");
                            String detail = object2.getString("detail");
                            String name = object2.getString("name");
                            String offlineLink = object2.getString("offlineLink");
                            String offlineThumbnail = object2.getString("offlineThumbnail");
                            String onlineLink = object2.getString("onlineLink");
                            String thumbnail = object2.getString("thumbnail");
                            String topicName = object2.getString("topicName");
                            newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                            newHashMap_.put("detail", detail);
                            newHashMap_.put("name", name);
                            newHashMap_.put("offlineLink", offlineLink);
                            newHashMap_.put("offlineThumbnail", offlineThumbnail);
                            newHashMap_.put("onlineLink", onlineLink);
                            newHashMap_.put("thumbnail", thumbnail);
                            newHashMap_.put("topicName", topicName);
                            newHashMap_.put("topicID", topicId);
                            newHashMap_.put("isSelected", "false");
                            newHashMap.put(key, newHashMap_);
                            videoHashMap.put(i + "-" + j, newHashMap_);
                        }
                        dikshaContentArrayList.add(newHashMap);
                    }
                }
            }
            setPagerAdapter();
            setTabLayout();
        } else {
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(language).child("diksha_videos").child("content").child(subject).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            dikshaContentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
                            dikshaContentArrayList_ = new ArrayList<>();
                            for (int i = 1; i < dikshaContentArrayList.size(); i++) {
                                LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                                LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                                HashMap<String, Object> videoMap = dikshaContentArrayList.get(i);

                                for (String key : videoMap.keySet()) {
                                    HashMap<String, Object> newHashMap_ = new HashMap<>();
                                    HashMap<String, String> innerMap = (HashMap<String, String>) dikshaContentArrayList.get(i).get(key);
                                    String AssessmentTopicID = innerMap.get("AssessmentTopicID");
                                    String detail = innerMap.get("detail");
                                    String name = innerMap.get("name");
                                    String offlineLink = innerMap.get("offlineLink");
                                    String offlineThumbnail = innerMap.get("offlineThumbnail");
                                    String onlineLink = innerMap.get("onlineLink");
                                    String thumbnail = innerMap.get("thumbnail");
                                    String topicName = innerMap.get("topicName");
                                    newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                                    newHashMap_.put("detail", detail);
                                    newHashMap_.put("name", name);
                                    newHashMap_.put("offlineLink", offlineLink);
                                    newHashMap_.put("offlineThumbnail", offlineThumbnail);
                                    newHashMap_.put("onlineLink", onlineLink);
                                    newHashMap_.put("thumbnail", thumbnail);
                                    newHashMap_.put("topicName", topicName);
                                    newHashMap_.put("topicID", AssessmentTopicID);
                                    newHashMap_.put("isSelected", "false");
                                    ((HashMap<String, String>) dikshaContentArrayList.get(i).get(key)).put("isSelected", "false");
                                    ((HashMap<String, String>) dikshaContentArrayList.get(i).get(key)).put("topicID", AssessmentTopicID);
                                    newHashMap.put(key, newHashMap_);
                                    videoHashMap.put(i + "-" + key, newHashMap_);
                                }
                                dikshaContentArrayList_.add(newHashMap);
                            }
                            setPagerAdapter();
                            setTabLayout();
                        } else {
                            setPagerAdapter();
                            setTabLayout();
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


        System.out.println( "======== play 1");

    }

    private void showDialog() {
        if (progressDialog != null) {
            try {
                progressDialog.show();
                showProgressingView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideDialog() {
        isloading = false;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (progressDialog != null) {
                progressDialog.dismiss();
                hideProgressingView();
            }
        }, 500);


    }

    public void hide_diaglognow() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            hideProgressingView();
        }
    }

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    private void setPagerAdapter() {
        PalContentPagerAdaper contentPagerAdaper = new PalContentPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(contentPagerAdaper);
        viewPager.setOffscreenPageLimit(1);
        showTestLayout(completeType);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setOffscreenPageLimit(4);
            }
        },4000);

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

        System.out.println("-------------  tab refresh");

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(global.getColor()));
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                removeFragment();

                LinearLayout tabLayoutt = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
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


        viewPager.setCurrentItem(tabposition);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.with(getApplicationContext()).load(Uri.parse(icon)).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
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

    private boolean videoFragmentExists() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        return fragment != null;
    }

    boolean done_diagnostic = false;

    public void showToolTipForDiagnosticTest() {
        if (practiceTopicAdapter != null) {
            String message, buttonText, skipText;
            message = seven;
            buttonText = eight;
            skipText = nine;
//            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//            {
//                 message = "हर अध्याय की शुरुआत में एक डायग्नोस्टिक परिक्षण है जो आपको करना है";
//                 buttonText = "आगे बढ़ें";
//                 skipText = "Skip";
//            }else {
//                 message = "At the beginning of every chapter there is a diagnostic test that you have to do";
//                 buttonText = "Go ahead";
//                 skipText = "Skip";
//            }
            Util.showTooltip(context, PalContentListingActivity.this, ArrowOrientation.LEFT, "right", message, buttonText,
                    skipText, "diagnosticTestTextView", practiceTopicAdapter.getViewForDiagnosticTestTextView());


        }
    }

    public void showToolTipForPractice() {
        if (practiceTopicAdapter != null) {
            done_diagnostic = true;
            String message, buttonText, skipText;

            message = ten;
            buttonText = eight;
            skipText = nine;
//            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//            {
//                message = "डायग्नोस्टिक परिक्षण के बाद आपकी समझ के स्तर के अनुसार यहाँ आपको अभ्यास के साथ उपचारात्मक वीडियो दिए गए हैं";
//                buttonText = "आगे बढ़ें";
//                skipText = "Skip";
//            }else {
//                message = "Get practical with diagnostics as per your level of understanding after the diagnostic test";
//                buttonText = "Go ahead";
//                skipText = "Skip";
//            }
            Util.showTooltip(context, PalContentListingActivity.this, ArrowOrientation.LEFT, "right", message, buttonText,
                    skipText, "practiceTextView", practiceTopicAdapter.getViewForPracticeTextView());


        }
    }


    public void showToolTipForFinalTest() {
        if (practiceTopicAdapter != null) {
            String message, buttonText, skipText;

            message = eleven;
            buttonText = eight;
            skipText = nine;
//            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//            {
//                message = "महारत प्राप्त करने के बाद यहाँ आपको अपने अध्याय को पूर्ण करने के लिए अंतिम परीक्षण देना है";
//                buttonText = "आगे बढ़ें";
//                skipText = "Skip";
//            }else {
//                message = "Once mastered here is the final test for you to complete your chapter";
//                buttonText = "Go ahead";
//                skipText = "Skip";
//            }
            Util.showTooltip(context, PalContentListingActivity.this, ArrowOrientation.LEFT, "right", message, buttonText,
                    skipText, "finalTestTextView", practiceTopicAdapter.getViewForFinalTestTextView());
        }
    }

    public void showTransparentScreenForTopicSelection() {
        if (leftTransparentBg != null && rightTransparentBg != null) {
            leftTransparentBg.setVisibility(View.VISIBLE);
            rightTransparentBg.setVisibility(View.VISIBLE);
        }
    }

    public void onlyhideTransparentScreenForTopicSelection() {
        if (leftTransparentBg != null && rightTransparentBg != null) {
            leftTransparentBg.setVisibility(View.GONE);
            rightTransparentBg.setVisibility(View.GONE);
        }
    }

    public void hideTransparentScreenForTopicSelection() {
        if (leftTransparentBg != null && rightTransparentBg != null) {
            leftTransparentBg.setVisibility(View.GONE);
            rightTransparentBg.setVisibility(View.GONE);
            Util.setToolTipContentScreen(context, false);
        }
    }

    public void showFullTransparentScreen() {
        if (fullTransparentBg != null) {
            fullTransparentBg.setVisibility(View.VISIBLE);
        }
    }

    public void hideFullTransparentScreen() {
        if (fullTransparentBg != null) {
            fullTransparentBg.setVisibility(View.GONE);
        }
    }

    public static int showingposition=0;

    private void getTopics(String subject) {
        chaptersText = tweleve;
        if (Util.isOfflineMode(context)) try {
            sClass = Util.getSelectedClass(context);
            language = Util.getSelectedLanguage(context);
            subject = Util.getSubjectId(context);

            String filePath = ".iDream_content/offlinetab_PAL/" + "Class" + sClass + "_topics.txt"; // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);                           // read file
            JSONObject object__ = jsonObject.getJSONObject(language);                               // filter by language
            JSONObject object___ = object__.getJSONObject(subject.toLowerCase());                   // filter by subject
            JSONArray array = object___.getJSONArray("topics");                         // getting topics

            /** topicsArrayList => contain all topics info */
            topicsArrayList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                /** topicsMap => contain current topic data */
                HashMap<String, String> topicsMap = new HashMap<>();

                JSONObject innerObject = array.getJSONObject(i);
                topicsMap.put("TName", innerObject.getString("TName"));
                topicsMap.put("TopicID", innerObject.getString("TopicID"));
                topicsMap.put("needToShow", "false");

                /** adding (topicsMap) data into list (topicsArrayList) */
                topicsArrayList.add(topicsMap);
            }

            textViewChapterCount.setText(topicsArrayList.size() + chaptersText);

            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), null, null, "pathTask");
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), null, null, "testScore");

        } catch (Exception e) {
            e.printStackTrace();
            Util.showToast(context, thirteen);
            finish();
        }
        else global.getDatabaseReference().child("topics").child(board).child(sClass).child(Util.getSelectedLanguagePackage(context).toLowerCase()).child(subject).child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        /** topicsArrayList => contain all topics info */

                        /** getting data from firebase */
                        topicsArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                        for (int i = 0; i < topicsArrayList.size(); i++) {
                            String nameSend = topicsArrayList.get(i).get("TName");
                            String topicID = topicsArrayList.get(i).get("TopicID");
                            topicsArrayList.get(i).put("isenable", "true");
                            topicsArrayList.get(i).put("TName", nameSend);
                            topicsArrayList.get(i).put("TopicID", topicID);
                            topicsArrayList.get(i).put("needToShow", "false");
                        }

                        /** set data in adapter */
                        if (topicsArrayList.size() > 0) {
                            PracticeTopicAdapter practiceTopicAdapter = new PracticeTopicAdapter(context, topicsArrayList, sClass, PalContentListingActivity.this);
//                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PalContentListingActivity.this, getResources().getIdentifier("layout_animation_fall_down", "anim", getPackageName()));
//                                recyclerView.setLayoutAnimation(animation);
//                                practiceTopicAdapter.notifyDataSetChanged();
//                                recyclerView.scheduleLayoutAnimation();

                            /** checking previous path data */
                            checkData(topicsArrayList, practiceTopicAdapter);
                            recyclerView.setAdapter(practiceTopicAdapter);
                        }

                        textViewChapterCount.setText(topicsArrayList.size() + chaptersText);
                        runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), null, null, "testScore");


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

    public void updateUi() {
//        lastTopicIdList = list;
        String topicID_ = null;
        boolean dataExist = false;
        boolean notCompleted = false;
        if (lastTopicId == null || lastTopicId.equals("")) {
            for (HashMap<String, String> data : topicsArrayList) {
                if (isDiagnosticTestAttempted(data.get("TopicID"))) {
                    if (isPracticeCompleted(data.get("TopicID"))) {
                        if (!isFinalTestAttempted(data.get("TopicID"))) {
                            lastTopicId = data.get("TopicID");
                            topicID_ = lastTopicId;
                            dataExist = true;
                            notCompleted = true;
                            completeType = "final";
                            static_completeType = "final";
                            break;
                        } else {
                            completeType = "all";
                            static_completeType = "all";
                        }
                    } else {
                        lastTopicId = data.get("TopicID");
                        topicID_ = lastTopicId;
                        dataExist = true;
                        notCompleted = true;
                        completeType = "practice";
                        static_completeType = "practice";
                        break;
                    }
                }
            }
            if (!notCompleted) {
                lastTopicId = topicsArrayList.get(0).get("TopicID");
                topicID_ = topicsArrayList.get(0).get("TopicID");
                dataExist = false;
                if (completeType == null || !completeType.equals("all")) {
                    completeType = "diagnostic";
                    static_completeType = "diagnostic";
                }
            }
        } else {
            topicID_ = lastTopicId;
            dataExist = true;
            if (isDiagnosticTestAttempted(topicID_)) {
                if (isPracticeCompleted(topicID_)) {
                    if (!isFinalTestAttempted(topicID_)) {
                        completeType = "final";
                        static_completeType = "final";
                    } else {
                        completeType = "all";
                        static_completeType = "all";
                    }
                } else {
                    completeType = "practice";
                    static_completeType = "practice";
                }
            } else {
                completeType = "diagnostic";
                static_completeType = "diagnostic";
            }
        }

        try {
            String topicID;
            String name = null;
            int position = 0;
            boolean backToScreen;

            if (dataExist) {
                topicID = topicID_;
                for (HashMap<String, String> item : topicsArrayList) {
                    if (item.get("TopicID").equals(topicID)) {
                        name = item.get("TName");
                        PracticeTopicAdapter.showingPosition=position;
                        break;
                    }
                    position++;
                }
                backToScreen = position != 0;
            } else {
                topicID = topicsArrayList.get(0).get("TopicID");
                name = topicsArrayList.get(0).get("TName");
                PracticeTopicAdapter.showingPosition=0;
                position = 0;
                backToScreen = false;
            }

            try {
                if (topicsArrayList.get(position).containsKey("needToShow")) {
                    if (topicsArrayList.get(position).get("needToShow").equals("true")) {
                        topicsArrayList.get(position).put("needToShow", "false");
                    } else {
                        topicsArrayList.get(position).put("needToShow", "true");
                        PracticeTopicAdapter.showingPosition=position;
                    }
                } else {
                    topicsArrayList.get(position).put("needToShow", "true");
                    PracticeTopicAdapter.showingPosition=position;
                }
            } catch (IndexOutOfBoundsException f) {
                topicsArrayList.get(0).put("needToShow", "true");
                PracticeTopicAdapter.showingPosition=0;
            }

            practiceTopicAdapter = new PracticeTopicAdapter(context, topicsArrayList, sClass, PalContentListingActivity.this);

            if (!dataExist) {
                practiceTopicAdapter.showDefaultTopic(true);
                PracticeTopicAdapter.showingPosition=0;
            } else {

                try {
                    practiceTopicAdapter.showDefaultTopic(topicID_.equals(topicsArrayList.get(position).get("TopicID")));
                } catch (Exception e) {
                    e.printStackTrace();
                    practiceTopicAdapter.showDefaultTopic(true);
                    PracticeTopicAdapter.showingPosition=0;
                }
//                PracticeTopicAdapter.showingPosition=position;
            }

            recyclerView.setAdapter(practiceTopicAdapter);

            Util.setTopicID(context, topicID);
            Util.setTopicNameAlt(context, name);
            topic_id = topicID;

            try {
                getFoundationTopicDetails(topicID);
                getAllFoundationalTopicData(practiceTopicAdapter, false);
                setListForFinalTestCompletion();
                checkData(topicsArrayList, practiceTopicAdapter);
//                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, topicID_);
                getCompleteStatusForTopic(practiceTopicAdapter, topicsArrayList, topicID_);
                practiceTopicAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

//                    if(position == 0){
//                        getCompleted(topicID, backToScreen, practiceTopicAdapter, topicsArrayList, position);
//                    }else{
//                        String topicId = topicsArrayList.get(position - 1).get("TopicID");
//                        getCompleted(topicId, backToScreen, practiceTopicAdapter, topicsArrayList, position);
//                    }
            saveLastTopicDetails();
//                    checkTopicCompleted(Util.getTopicID(context), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshTabLayout(String topicId, boolean hideDialog) {
        boolean hide = true;
        if (topicsArrayList != null && topicsArrayList.size() > 0) {
            for (HashMap<String, String> item : topicsArrayList) {
                if (item.containsKey("isCompleted") && item.get("TopicID").equals(topicId)) {
                    hide = !item.get("isCompleted").equals("true");
                    break;
                }
            }
            final Handler handler = new Handler();
            boolean finalHide = hide;
            handler.postDelayed(() -> refreshFragments(topicId, finalHide, 0, hideDialog), 1000);
        }
    }

    public boolean isDiagnosticTestAttempted(String topicId) {
        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
                return true;
            }
        }
        return false;
    }

    public boolean isPracticeCompleted(String topicId) {
        for (PracticeScoreModel item : practiceScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getScore().equals("100")) {
                return true;
            }
        }
        return false;
    }

    public boolean isFinalTestAttempted(String topicId) {
        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("simple_test") &&
                    item.getPercentage().equals("100")) {
                return true;
            }
        }
        return false;
    }

    private void setListForFinalTestCompletion() {
        runBackgroundTask(Util.getUserId(context), board, sClass, subject, null, null, "finalTestCompletionTask");
    }

    public boolean getLastTopicId(String topicID) {
        boolean exist = false;
//        List<TopicSubjectWiseModel> list = topicSubjectRepository.getDetail(Util.getUserId(context), Util.getSubject(context));
        if (lastTopicIdList != null && lastTopicIdList.size() > 0) {
            String topicId = lastTopicIdList.get(0).getTopicId();
            if (topicId.equals(topicID)) {
                exist = true;
            }
        } else {
            exist = true;
        }
        return exist;
    }

    public void onClickHandler(int position) {
        try {
            tabposition=0;
            viewPager.setCurrentItem(tabposition);

            hideTransparentScreenForTopicSelection();
            showDialog();

            viewPager.setCurrentItem(tabposition);
            String topicID = topicsArrayList.get(position).get("TopicID");
            String name = topicsArrayList.get(position).get("TName");
            topic_id = topicID;
            lastTopicId = topicID;
            Util.setTopicNameAlt(context, name);
            Util.setTopicID(context, topicID);
//            Util.setVideoLevel(context, 0);

            if (isDiagnosticTestAttempted(topicID)) {
                if (isPracticeCompleted(topicID)) {
                    if (!isFinalTestAttempted(topicID)) {
                        completeType = "final";
                        static_completeType = "final";
                    } else {
                        completeType = "all";
                        static_completeType = "all";
                    }
                } else {
                    completeType = "practice";
                    static_completeType = "practice";
                }
            } else {
                completeType = "diagnostic";
                static_completeType = "diagnostic";
            }

            for (int i = 0; i < topicsArrayList.size(); i++) {
                if (i != position) {
                    topicsArrayList.get(i).put("needToShow", "false");
                }
            }

            if (position == 0) {
                if (practiceTopicAdapter.getShowDefaultTopic()) {
                    topicsArrayList.get(position).put("needToShow", "false");
                    practiceTopicAdapter.showDefaultTopic(false);
                } else {
                    topicsArrayList.get(position).put("needToShow", "true");
                    practiceTopicAdapter.showDefaultTopic(true);
                    PracticeTopicAdapter.showingPosition=position;
                }
            } else {
                if (topicsArrayList.get(position).containsKey("needToShow")) {
                    if (topicsArrayList.get(position).get("needToShow").equals("true")) {
                        topicsArrayList.get(position).put("needToShow", "false");
                    } else {
                        topicsArrayList.get(position).put("needToShow", "true");
                        PracticeTopicAdapter.showingPosition=position;
                    }
                } else {
                    topicsArrayList.get(position).put("needToShow", "true");
                    PracticeTopicAdapter.showingPosition=position;
                }
                practiceTopicAdapter.showDefaultTopic(false);
            }

            practiceTopicAdapter.topicsArrayList = topicsArrayList;
            getFoundationTopicDetails(topicID);
            refreshMessageLayout();
            getCompleteStatusForTopic(practiceTopicAdapter, topicsArrayList, topicID);
            refreshTabLayout(topicID, true);
            saveLastTopicDetails();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showingposition=position;
                    recyclerView.smoothScrollToPosition(position);
                    getVideos();
                }
            }, 1000);//time in milisecond

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshMessageLayout() {

        if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
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

                        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                        for (Fragment fragment : allFragments) {
                            if (fragment instanceof PalVideoListFragment) {
                                ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);

                            } else if (fragment instanceof PalDikshaContentFragment) {
                                ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
                            }
                        }
//                        break;
                    }
                }
            }
        }
    }

    public void getVideos() {
        if (viewPager != null) {
            viewPager.setCurrentItem(0);
        }
    }

    /** check topic have foundation topic ??  */
    private void   checkData(ArrayList<HashMap<String, String>> topicsArrayList, PracticeTopicAdapter practiceTopicAdapter) {
        if (count != topicsArrayList.size()) {
            String topicID = topicsArrayList.get(count).get("TopicID");

            if (data != null) {
                /** if foundation topic available we add path info in topic (topicsArrayList) */
                String pathMap = (String) data.get(topicID);
                if (pathMap != null) topicsArrayList.get(count).put("path", pathMap);
            }
            count++;
            checkData(topicsArrayList, practiceTopicAdapter);
        }

    }

    private void getPath(String subject) {
        if (Util.isOfflineMode(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, subject, null, null, "pathTask");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            /** set path info in (data) and update in RoomDB for offline usage */
                            data = (HashMap<String, Object>) snapshot.getValue();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, entry.getKey())) {
                                    reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, entry.getKey(), (String) entry.getValue());
                                } else {
                                    ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                    reportsTopicPathModel.setUserId(Util.getUserId(context));
                                    reportsTopicPathModel.setBoard(board);
                                    reportsTopicPathModel.setSClass(sClass);
                                    reportsTopicPathModel.setSubject(subject);
                                    reportsTopicPathModel.setTopicId(entry.getKey());
                                    reportsTopicPathModel.setTypeV((String) entry.getValue());
                                    reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                                }
                            }
                        }
                        isCompleted = false;
                        countTopic = 0;
                        count = 0;

                        /** get topics of subject */
                        getTopics(subject);
                        getTopicSeenVideoListing(false);
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

    public void setPath(String board, String sClass, String subject, String topicId, String value) {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, subject, topicId, value, "pathTask");
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, value, "testScore");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String path = (String) snapshot.getValue();
                        StringBuilder strBuilder = new StringBuilder();
                        if (!path.contains(value)) {
                            strBuilder.append(path).append("-").append(value);
                            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).setValue(strBuilder.toString());
                            if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
                                reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
                            } else {
                                ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                reportsTopicPathModel.setUserId(Util.getUserId(context));
                                reportsTopicPathModel.setBoard(board);
                                reportsTopicPathModel.setSClass(sClass);
                                reportsTopicPathModel.setSubject(subject);
                                reportsTopicPathModel.setTopicId(topicId);
                                reportsTopicPathModel.setTypeV(strBuilder.toString());
                                reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                            }
                        }
                    } else {
                        topicPathData.add(topicId + "-" + value);
                        StringBuilder strBuilder = new StringBuilder();
                        int count = 0;
                        for (String item : topicPathData) {
                            String[] separated = item.split("-");
                            if (separated[0].equals(topicId)) {
                                if (count == 0) {
                                    strBuilder.append(separated[1]);
                                } else {
                                    strBuilder.append("-").append(separated[1]);
                                }
                            }
                            count++;
                        }
                        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).setValue(strBuilder.toString());
                        if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
                            reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
                        } else {
                            ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                            reportsTopicPathModel.setUserId(Util.getUserId(context));
                            reportsTopicPathModel.setBoard(board);
                            reportsTopicPathModel.setSClass(sClass);
                            reportsTopicPathModel.setSubject(subject);
                            reportsTopicPathModel.setTopicId(topicId);
                            reportsTopicPathModel.setTypeV(strBuilder.toString());
                            reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void openFragment(String url, String offlineLink, String videoName, String topicID, boolean isLocalFile, String type, String key) {

        tabposition=0;
        viewPager.setCurrentItem(tabposition);
        remove_fragment=false;
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
//        videoName_Text.setVisibility(View.VISIBLE);
        int[] location = new int[2];
        frameLayout.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int newX = x - convertDpToPx(20);

        int deviceWidth = metrics.widthPixels;
//        int deviceWidth = findViewById(R.id.line2).getWidth();


//        int videoLayoutWidth = deviceWidth;
//
        //for 7 inch tablets
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(350));
//        layoutParams.setMargins(370, 90, 5, 5);
//        setMargins(viewPager, 20, 376, 10, 0);

        //for 8 inch tab
//        layoutParams.setMargins(460, 130,10, 5);
//        setMargins(tabLayout, 10, 10, 10, 0);
//        setMargins(viewPager, 20, 420, 10, 0);


        //for 10 inch tablets
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(400));
//        layoutParams.setMargins(480, 100, 10, 5);
////        setMargins(videoName_Text,10,1050,10,5);
//        setMargins(tabLayout, 10, 10, 10, 5);
//        setMargins(viewPager, 20, 320, 10, 0);

//
//        //for 8 inch tablets
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(350));
//        layoutParams.setMargins(510, 115, 10, 5);
////        setMargins(videoName_Text,10,1050,10,5);
//        setMargins(tabLayout, 10, 10, 10, 5);
//        setMargins(viewPager, 20, 320, 10, 0);
//

        //for 10 inch tablets
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(400));
//        layoutParams.setMargins(480, 100, 10, 5);
////        setMargins(videoName_Text,10,1050,10,5);
//        setMargins(tabLayout, 10, 10, 10, 5);
//        setMargins(viewPager, 20, 320, 10, 0);

        try {
            vimeo_video_container.removeAllViews();
//           vimeo_video_container.setLayoutParams(layoutParams);
            ViewGroup parentView = findViewById(R.id.rootLayout);
            parentView.addView(vimeo_video_container, 1);
        } catch (Exception r) {
            r.printStackTrace();
        }

        System.out.println( "------  type "+type);

        if(type.equals("video_lessons")) {
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

            }
        }
        else if(type.equals("diksha_content")) {
            System.out.println( "------  key "+key);
            if(PalDikshaContentFragment.dataMap != null){

                HashMap<String, String> reportsMap = (HashMap<String, String>) PalDikshaContentFragment.dataMap.get(key);
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

            }
        }



        bundl.putString("url", url);
        bundl.putString("type", type);
        bundl.putString("offlineLink", offlineLink);
        bundl.putBoolean("isLocalFile", isLocalFile);
        bundl.putString("videoName", videoName);
        bundl.putString("topicID", topicID);
        bundl.putString("subjectName", subjectName);
        bundl.putString("videoid_for_reports", key);
        bundl.putBoolean("isFullScreen", Util.getIsFullScreen(context));
        bundl.putInt("duration", duration);

        currentvideo_url = url;
        PalContentListingActivity.keys = key;


        if (!Util.getIsFullScreen(context)) {
            try {
                setupSmallscreen();

            } catch (Exception f) {
                f.printStackTrace();
            }
        } else {
            setupFullscreen();
//            Intent intent = new Intent(context, VideoView_Activity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            intent.putExtras(bundl);
//
//            startActivity(intent);
        }
        palVideoPlayerActivity = new iPrepVideoPlayerActivity();
        palVideoPlayerActivity.setArguments(bundl);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, palVideoPlayerActivity, "tag");
        transaction.addToBackStack(null);

        try {
            transaction.commit();
            frameLayout.setVisibility(View.VISIBLE);
        }
        catch (Exception r) {

            currentvideo_url = "";
            PalContentListingActivity.keys = "";
            r.printStackTrace();
        }

        try {

            if (messageLayoutidvisible) {
                messageLayout1.setVisibility(View.GONE);

            }

            if(!Util.isPortraitMode(context)) messageLayout2.setVisibility(View.GONE);

            if (PalDikshaContentFragment.messageLayoutidvisible) {
                PalDikshaContentFragment.messageLayout.setVisibility(View.GONE);
            }

        } catch (Exception r) {}


    }

    private void setupFullscreen() {
        tabLayout.setVisibility(View.GONE);
        reletiveLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        supportBtn.setVisibility(View.GONE);
        videoViewLayout.setVisibility(View.VISIBLE);
        Util.setIsFullScreen(context,true);
    }

    private void setupSmallscreen() {
        try {
            tabLayout.setVisibility(View.VISIBLE);
            reletiveLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            videoViewLayout.setVisibility(View.VISIBLE);
            supportBtn.setVisibility(View.VISIBLE);
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.setIsFullScreen(context,false);
    }

    boolean updatingInProgress=false;

    public void removeFragment() {
        setupSmallscreen();
        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------  removedd");
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        if (fragment != null) {
            try {
                transaction.remove(fragment);
                transaction.commit();
                manager.popBackStack();
            } catch (Exception e) {
            }
        }
//        setMargins(tabLayout, 10, 10, 10, 0);
//        setMargins(viewPager, 0, 0, 0, 0);
        ViewGroup parent = (ViewGroup) vimeo_video_container.getParent();
        frameLayout.setVisibility(View.GONE);

        try {
            if (messageLayoutidvisible) {
                if(Util.isSubjectboadingDone(context)) messageLayout1.setVisibility(View.VISIBLE);
            }

            if (PalDikshaContentFragment.messageLayoutidvisible) {
                PalDikshaContentFragment.messageLayout.setVisibility(View.GONE);
            }

        } catch (Exception r) {}

        if (parent != null) {
            parent.removeView(vimeo_video_container);
        }
        isFullScreen = false;
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
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

        PalContentListingActivity.palContentListingActivity.setupSmallscreen();

        frameLayout.setVisibility(View.VISIBLE);
//        reletiveLayout.setVisibility(View.GONE);
        ;
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


    public static void openVimeoVideoFullScreenFragment() {

//        reletiveLayout.setVisibility(View.GONE);

        PalContentListingActivity.palContentListingActivity.setupFullscreen();

//        Intent intent = new Intent(context, VideoView_Activity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
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
                        openVimeoVideoFullScreenFragment();
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
                                    openVimeoVideoFullScreenFragment();
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
                        if(key==null) key="";
                        String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                        File file = new File(filePath);
                        String url;
                        boolean isLocalFile;

                        if(onlineLink.contains("https://vimeo.com/")) type="video_lessons";
                        else type="diksha_content";

                        if(type.equals("video_lessons")) {
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
//                            openVimeoVideoFullScreenFragment();
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
                                        openVimeoVideoFullScreenFragment();
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
            else
            {
                ArrayList<String> all_keys=new ArrayList<>();

                for (int i = 0; i < dikshaContentArrayList_.size(); i++) {

                    Set<String> keys =  dikshaContentArrayList_.get(i).keySet();

                    for(String key : keys)
                    {
                        all_keys.add(key);
                    }

                }

                if (dikshaContentArrayList_ != null && videoData != null) {
                    String[] videoKeySeparated = videoData.split("-");
                    videoLevel = Integer.parseInt(videoKeySeparated[0]);
                    videoPosition = Integer.parseInt(videoKeySeparated[1]);

//                String video_key="931024";
                    String video_key;

                    if(all_keys.contains(keys))
                    {
                        for(int f=0;f<all_keys.size();f++)
                        {
                            if(all_keys.get(f).equals(keys))
                            {
                                try {
                                    if(!all_keys.get(f+1).isEmpty())
                                    {
                                        video_key=all_keys.get(f+1);
                                        for (int i = 0; i < dikshaContentArrayList_.size(); i++) {

                                            if (dikshaContentArrayList_.get(i).containsKey(video_key)) {

                                                HashMap<String, Object> data = (HashMap<String, Object>) dikshaContentArrayList_.get(i).get(video_key);

                                                String offlineLink = (String) data.get("offlineLink");
                                                String onlineLink = (String) data.get("onlineLink");
                                                String key = video_key;
                                                data.put("isSelected", "true");
                                                keys=key;
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
                                                    openFragment(onlineLink, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, "diksha_content", key);
                                                    openVimeoVideoFullScreenFragment();
                                                } else {
                                                    openFragment(onlineLink, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, "diksha_content", key);
                                                    //openVimeoVideoFullScreenFragment();
                                                }

                                                break;
                                            }
                                            else {
                                                if (i == dikshaContentArrayList_.size() - 1) {
                                                    videoLevel = Integer.parseInt(videoKeySeparated[0]) + 1;
                                                    videoPosition = 0;
                                                    for (int j = 0; j < dikshaContentArrayList_.size(); j++) {
                                                        if (dikshaContentArrayList_.get(j).containsKey(video_key))
                                                        {
                                                            keys=video_key;
                                                            HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(j).get(video_key);
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
                                                                openFragment(onlineLink, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, "diksha_content", key);
                                                                openVimeoVideoFullScreenFragment();
                                                            } else {
                                                                openFragment(onlineLink, offlineLink, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, "diksha_content", key);
                                                                //openVimeoVideoFullScreenFragment();
                                                            }


                                                            //openVimeoVideoFullScreenFragment();
                                                            //openFragment(url, (String) data.get("name"), (String) data.get("topicID"), isLocalFile, null);
                                                            break;
                                                        }
                                                        else {
                                                            if (j == dikshaContentArrayList_.size() - 1) {
                                                                keys="";
                                                                Util.openGifDialogue(context, fourteen);
                                                                Toast.makeText(context, fourteen, Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        break;

                                    }
                                    else
                                    {
                                        keys="";
                                        if (Util.getIsFullScreen(context)) Util.openGifDialogue(Util.getContext(), fourteen);
                                        else Util.openGifDialogue(context, fourteen);
                                        System.out.println(  "-------- no video available");
                                        removeFragment();
                                        return;
                                    }
                                }catch (Exception e)
                                {
                                    keys="";
                                    if (Util.getIsFullScreen(context)) Util.openGifDialogue(Util.getContext(), fourteen);
                                    else Util.openGifDialogue(context, fourteen);
                                    removeFragment();
                                    System.out.println(  "-------- no video available");
                                }
                                f=all_keys.size();
                                break;
                            }

                        }
                    }

                }
            }

        }

    }

    public void removeFragment(boolean isCompleted) {

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PalContentListingActivity.palContentListingActivity.setupSmallscreen();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        frameLayout.setVisibility(View.GONE);
        Fragment fragment = manager.findFragmentByTag("tag");
        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
            manager.popBackStack();
        }
        System.out.println(fragment);
//        setMargins(tabLayout, 10, 10, 10, 0);
//        setMargins(viewPager, 00, 10, 00, 0);
//        ViewGroup parent = (ViewGroup) vimeo_video_container.getParent();
//
//        if (parent != null) {
//            parent.removeView(vimeo_video_container);
//        }
        isFullScreen = false;

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

    public void setPracticeData(String topicName, String mastery) {
        this.topicName = topicName;
        this.mastery = mastery;
    }

    public void checkAndStartPractice() {

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(PalVideoListFragment.ttype.equals("foundationalPractice"))
        {
            ((PalContentListingActivity)context).firsttime=true;
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
            ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), subject, PalVideoListFragment.foundationalTopicModel.getSeniorTopicID(), PalVideoListFragment.foundationalTopicModel.getTopicId(), PalVideoListFragment.foundationalTopicModel.getTopicName(), PalVideoListFragment.foundationalTopicModel.getSeniorClass(), 0, PalVideoListFragment.pposition,mastery,streekProgress,level);

        }
        else
        {
            startPractice();
        }
    }

    public void startPractice() {

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof PalPracticeFrament) {
                ((PalPracticeFrament) fragment).startPractice();
            }
        }
    }

    public void displayPracticeLayout(boolean isFullScreen) {
        boolean exists = false;
        if (practiceLayout != null) {
            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
                for (int i = 0; i < foundationalTopicData.size(); i++) {
                    if (foundationalTopicData.get(i).getSeniorTopicID().equals(Util.getTopicID(context))) {
                        if (foundationalTopicData.get(i).getShow()) {
                            handleVisibilityPracticeLayout("foundational", isFullScreen, foundationalTopicData.get(i).getTopicName());
                            int finalI = i;
                            textViewResumePractice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Util.preventTwoClick(v);
                                    String currentTopicId = foundationalTopicData.get(finalI).getSeniorTopicID();

                                    Util.setTopicID(context, foundationalTopicData.get(finalI).getTopicId());
                                    Util.setTopicNameAlt(context, foundationalTopicData.get(finalI).getTopicName());
                                    Util.setLevel(context, Integer.parseInt("1"));

                                    global.setProgress(0);

//                                    HashMap<String, String> map = new HashMap<>();
//                                    map.put("sClass", foundationalTopicData.get(finalI).getSeniorClass());
//                                    map.put("seniorTopicID", foundationalTopicData.get(finalI).getSeniorTopicID());
//                                    global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(foundationalTopicData.get(finalI).getTopicId()).setValue(map);

                                    if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), foundationalTopicData.get(finalI).getTopicId())) {
                                        trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, foundationalTopicData.get(finalI).getSeniorTopicID(), foundationalTopicData.get(finalI).getSeniorClass(), foundationalTopicData.get(finalI).getSeniorTopicName(), foundationalTopicData.get(finalI).getTopicId(), Util.getSelectedLanguagePackage(context));
                                    } else {
                                        TrackTopicModel trackTopicModel = new TrackTopicModel();
                                        trackTopicModel.setUserId(Util.getUserId(context));
                                        trackTopicModel.setSubject(Util.getSubject(context));
                                        trackTopicModel.setBoard(board);
                                        trackTopicModel.setSeniorTopicId(foundationalTopicData.get(finalI).getSeniorTopicID());
                                        trackTopicModel.setSeniorClass(foundationalTopicData.get(finalI).getSeniorClass());
                                        trackTopicModel.setSeniorTopicName(foundationalTopicData.get(finalI).getSeniorTopicName());
                                        trackTopicModel.setFoundationalTopicId(foundationalTopicData.get(finalI).getTopicId());
                                        trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                                        trackTopicRepository.insertDetails(trackTopicModel);
                                    }

                                    HashMap<String, String> mapF = new HashMap<>();
                                    mapF.put("foundational_class", foundationalTopicData.get(finalI).getSClass());
                                    mapF.put("foundational_topic", foundationalTopicData.get(finalI).getTopicId());
                                    global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(currentTopicId).setValue(mapF);
                                    if (Util.isNetworkAvailable(context)) {
                                        System.out.println("======== ppppppppppp ");
                                        startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", foundationalTopicData.get(finalI).getSClass()).putExtra("streakProgress", foundationalTopicData.get(finalI).getStreakProgress()).putExtra("streak", foundationalTopicData.get(finalI).getStreakCount()).putExtra("incorrectStreak", foundationalTopicData.get(finalI).getIncorrectStreak()).putExtra("practiceType", "junior").putExtra("seniorClass", foundationalTopicData.get(finalI).getSeniorClass()).putExtra("seniorTopicID", foundationalTopicData.get(finalI).getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicData.get(finalI).getSeniorTopicName()).putExtra("testPercentageAchieved", foundationalTopicData.get(finalI).getTestPercentageAchieved()));

                                    } else {
                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                            Util.openGifDialogue(context, "इंटरनेट कनेक्शन काम नहीं कर रहा");
                                        } else {
                                            Util.openGifDialogue(context, "Internet Connection is not working");
                                        }
                                    }
                                }
                            });
                            exists = true;
                            break;
                        }
                    }
                }
                if (!exists) {
                    handleVisibilityPracticeLayout("current", isFullScreen, null);
                    textViewResumePractice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.preventTwoClick(v);
                            startPractice();
                        }
                    });
                }
            } else {
                handleVisibilityPracticeLayout("current", isFullScreen, null);
                textViewResumePractice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        startPractice();
                    }
                });
            }
        }
    }

    private void handleVisibilityPracticeLayout(String type, boolean isFullScreen, String topicName) {

        String goToabhyas, goAhead;

        goToabhyas = fifteen;
        goAhead = sixteen + topicName + seventeen;
//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            goToabhyas = "अभ्यास पे जाएँ";
//            goAhead = "आगे बढ़ने के लिए"+ topicName +"में १००% मास्टरी करें";
//        }else {
//            goToabhyas = "go to practice";
//            goAhead = "Do 100% mastery in "+ topicName +" to proceed";
//        }

        if (type.equals("foundational")) {
            if (isFullScreen) {
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
                setMarginToVideoList(0);
            } else {
                if (textViewPractice != null) {
                    textViewResumePractice.setText(goToabhyas);
                    textViewPractice.setText(goAhead);
                }
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
//                setMarginToVideoList(60);
                setMarginToVideoList(0);
            }
        } else {
            if (isFullScreen) {
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
                setMarginToVideoList(0);
            } else if (this.mastery.equals("100")) {
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
                setMarginToVideoList(0);
            } else if (Integer.parseInt(this.mastery) > 0 && Integer.parseInt(this.mastery) < 100) {
                if (textViewPractice != null) {
                    textViewResumePractice.setText(goToabhyas);
                    textViewPractice.setText(goAhead);
                }
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
//                setMarginToVideoList(60);
                setMarginToVideoList(0);
            } else if (Integer.parseInt(this.mastery) == 0) {
                if (textViewPractice != null) {
//                    textViewResumePractice.setText("अभ्यास शुरू करें");
                    textViewResumePractice.setText(goToabhyas);
                    textViewPractice.setText(goAhead);
                }
                practiceLayout.setVisibility(View.GONE);
                togglePracticeLayout.setVisibility(View.GONE);
//                setMarginToVideoList(60);
                setMarginToVideoList(0);
            }
        }
    }

    private void showPracticeLayout() {
        if (practiceLayout != null) {
            practiceLayout.setVisibility(View.VISIBLE);
            practiceLayout.startAnimation(AnimationUtils.loadAnimation(
                    context, R.anim.right_to_left_anim));
        }
    }

    private void hidePracticeLayout() {
        if (practiceLayout != null) {
            practiceLayout.startAnimation(AnimationUtils.loadAnimation(
                    context, R.anim.left_to_right_anim));
            practiceLayout.setVisibility(View.GONE);
        }
    }

    private void setMarginToVideoList(int margin) {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof PalVideoListFragment) {
                ((PalVideoListFragment) fragment).setMarginToRecyclerView(margin);
            }
        }
    }

    public void showTestLayout(String type) {

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
                                    static_completeType = "foundationalPractice";
                                    for (int i = 0; i < topicsArrayList.size(); i++) {
                                        if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
                                            position = i;
                                            break;
                                        }
                                    }

                                    List<Fragment> allFragments2 = getSupportFragmentManager().getFragments();
                                    for (Fragment fragment2 : allFragments2) {
                                        if (fragment2 instanceof PalVideoListFragment) {
                                            ((PalVideoListFragment) fragment2).showTestLayout(completeType, item, position);
                                        }
                                    }
//                                                    break;
                                }
                            }
                        }
                    }

                } else {
                    ((PalVideoListFragment) fragment).showTestLayout(type, null, 0);
                }

            } else if (fragment instanceof PalDikshaContentFragment) {
                ((PalDikshaContentFragment) fragment).showTestLayout(type, null, 0);
            }
        }
    }

    public void fullScreen() {
//        isFullScreen = true;
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        frameLayout.setLayoutParams(layoutParams);
//        LinearLayout.LayoutParams layoutParams_ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2f);
//        linearLayoutWeight.setLayoutParams(layoutParams_);
//        displayPracticeLayout(false);
////        LinearLayout.LayoutParams layoutParams_ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f);
////        linearLayoutWeight.setLayoutParams(layoutParams_);
//        linearLayout.setVisibility(View.GONE);
//        reletiveLayout.setVisibility(View.GONE);
//        hidePracticeLayout();
        // displayPracticeLayout(true);
//        videoImageViewBack.setVisibility(View.GONE);
    }

    public void smallScreen(boolean showBackButton) {
//        isFullScreen = false;
//        linearLayout.setVisibility(View.VISIBLE);
//        reletiveLayout.setVisibility(View.VISIBLE);
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
////        layoutParams.setMargins(0, 0, 20, 0);
//        //frameLayout.setLayoutParams(layoutParams);
//
//        LinearLayout.LayoutParams layoutParams_ = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.3f);
//        linearLayoutWeight.setLayoutParams(layoutParams_);
//        displayPracticeLayout(false);
//        if (showBackButton) {
////            videoImageViewBack.setVisibility(View.VISIBLE);
//        }
    }

    private void getCompleted(String topicId, boolean isPrevious, PracticeTopicAdapter practiceTopicAdapter, ArrayList<HashMap<String, String>> topicsArrayList, int position) {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {

            DiagnosticCompleteTask diagnosticCompleteTask = new DiagnosticCompleteTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, "getCompleted", isPrevious, position, false);
            diagnosticCompleteTask.execute();

        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            isCompleted = (boolean) snapshot.getValue();
                            if (isPrevious) {
                                if (isPreviousTopicIdCompleted(position, topicsArrayList)) {
//                                    viewPager.setCurrentItem(0);
                                    String topicID = topicsArrayList.get(position).get("TopicID");
                                    String name = topicsArrayList.get(position).get("TName");
                                    topic_id = topicID;
                                    Util.setTopicNameAlt(context, name);
                                    Util.setTopicID(context, topicID);
//                                    setLastTopicId(topicID);
//                                    Util.setVideoLevel(context, 0);
                                    for (int i = 0; i < topicsArrayList.size(); i++) {
                                        if (i != position) {
                                            topicsArrayList.get(i).put("needToShow", "false");
                                        }
                                    }
                                    if (topicsArrayList.get(position).containsKey("needToShow")) {
                                        if (topicsArrayList.get(position).get("needToShow").equals("true")) {
                                            topicsArrayList.get(position).put("needToShow", "false");
                                        } else {
                                            topicsArrayList.get(position).put("needToShow", "true");
                                            PracticeTopicAdapter.showingPosition=position;
                                        }
                                    } else {
                                        topicsArrayList.get(position).put("needToShow", "true");
                                        PracticeTopicAdapter.showingPosition=position;
                                    }
                                    practiceTopicAdapter.showDefaultTopic(false);
                                    practiceTopicAdapter.topicsArrayList = topicsArrayList;
                                    getAllFoundationalTopicData(practiceTopicAdapter, false);
                                    //                            practiceTopicAdapter.notifyDataSetChanged();
                                    getCompleted(topicID, false, practiceTopicAdapter, topicsArrayList, position);
                                    getFoundationTopicDetails(topicID);
                                    //Set spinner in Test fragment to default option
//                                    refreshTestFragment();
                                    checkTopicCompleted(topicID, true);
                                } else {
                                    Util.showToast(context, twenty);
                                    getAllFoundationalTopicData(practiceTopicAdapter, true);
                                }
                            }
                            if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                                reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, isCompleted,Util.getSelectedLanguage(context));
                            } else {
                                ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                                reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                                reportsDiagnosticCompleteModel.setBoard(board);
                                reportsDiagnosticCompleteModel.setSClass(sClass);
                                reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                                reportsDiagnosticCompleteModel.setTopicId(topicId);
                                reportsDiagnosticCompleteModel.setComplete(isCompleted);
                                reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                                reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
                            }
                        } else {
                            isCompleted = false;
                            if (isPrevious) {
                                //Util.showToast(context, eighteen);
                                Util.openGifDialogue(context, eighteen);

//                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                {
//                                    Util.showToast(context, "कृपया पहले पिछले विषय का प्रयास करें");
//                                }else {
//                                    Util.showToast(context, "Please try previous topic first");
//                                }

                            }
                            getAllFoundationalTopicData(practiceTopicAdapter, true);
                            if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                                reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, isCompleted,Util.getSelectedLanguage(context));
                            } else {
                                ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                                reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                                reportsDiagnosticCompleteModel.setBoard(board);
                                reportsDiagnosticCompleteModel.setSClass(sClass);
                                reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                                reportsDiagnosticCompleteModel.setTopicId(topicId);
                                reportsDiagnosticCompleteModel.setComplete(isCompleted);
                                reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                                reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
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

    private class DiagnosticCompleteTask extends AsyncTask<Void, Void, ArrayList<ReportsDiagnosticCompleteModel>> {

        String userId;
        String board;
        String sClass;
        String subject;
        String topicId;
        String methodCall;
        boolean isPrevious;
        int position;
        boolean hideDialog;

        private DiagnosticCompleteTask(String userId, String board, String sClass, String subject, String topicId, String methodCall, boolean isPrevious, int position, boolean hideDialog) {

            this.userId = userId;
            this.board = board;
            this.sClass = sClass;
            this.subject = subject;
            this.topicId = topicId;
            this.methodCall = methodCall;
            this.isPrevious = isPrevious;
            this.position = position;
            this.hideDialog = hideDialog;
        }

        @Override
        protected ArrayList<ReportsDiagnosticCompleteModel> doInBackground(Void... voids) {
            if (methodCall.equals("getCompleted")) {
                return (ArrayList<ReportsDiagnosticCompleteModel>) reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
            } else if (methodCall.equals("getCompletedForAllTopics")) {
                return (ArrayList<ReportsDiagnosticCompleteModel>) reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
            } else if (methodCall.equals("checkTopicCompleted")) {
                return (ArrayList<ReportsDiagnosticCompleteModel>) reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ReportsDiagnosticCompleteModel> list) {
            super.onPostExecute(list);
            if (methodCall.equals("getCompleted")) {
                if (list != null && list.size() > 0) {
                    try {
                        isCompleted = list.get(0).isComplete();
                        if (isPrevious) {
                            if (isPreviousTopicIdCompleted(position, topicsArrayList)) {
//                                viewPager.setCurrentItem(0);
                                String topicID = topicsArrayList.get(position).get("TopicID");
                                String name = topicsArrayList.get(position).get("TName");
                                topic_id = topicID;
                                Util.setTopicNameAlt(context, name);
                                Util.setTopicID(context, topicID);
//                                setLastTopicId(topicID);
//                                Util.setVideoLevel(context, 0);
                                for (int i = 0; i < topicsArrayList.size(); i++) {
                                    if (i != position) {
                                        topicsArrayList.get(i).put("needToShow", "false");
                                    }
                                }
                                if (topicsArrayList.get(position).containsKey("needToShow")) {
                                    if (topicsArrayList.get(position).get("needToShow").equals("true")) {
                                        topicsArrayList.get(position).put("needToShow", "false");
                                    } else {
                                        topicsArrayList.get(position).put("needToShow", "true");
                                        PracticeTopicAdapter.showingPosition=position;
                                    }
                                } else {
                                    topicsArrayList.get(position).put("needToShow", "true");
                                    PracticeTopicAdapter.showingPosition=position;
                                }
                                practiceTopicAdapter.showDefaultTopic(false);
                                practiceTopicAdapter.topicsArrayList = topicsArrayList;
                                getAllFoundationalTopicData(practiceTopicAdapter, false);
                                //                            practiceTopicAdapter.notifyDataSetChanged();
                                getCompleted(topicID, false, practiceTopicAdapter, topicsArrayList, position);
                                getFoundationTopicDetails(topicID);
                                //Set spinner in Test fragment to default option
//                                refreshTestFragment();
                                checkTopicCompleted(topicID, true);
                            } else {
                                Util.showToast(context, "Please complete previous topic to attempt this topic.");
                                getAllFoundationalTopicData(practiceTopicAdapter, true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    isCompleted = false;
                    if (isPrevious) {
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                            Util.openGifDialogue(context, "कृपया पहले पिछले विषय का प्रयास करें");
//                            Util.showToast(context, "कृपया पहले पिछले विषय का प्रयास करें");
                        } else {
//                            Util.showToast(context, "Please try previous topic first");
                            Util.openGifDialogue(context, "Please try previous topic first");
                        }
                    }
                    getAllFoundationalTopicData(practiceTopicAdapter, true);
                }
            } else if (methodCall.equals("getCompletedForAllTopics")) {
                try {
                    String comp = "false";
                    if (list != null && list.size() > 0) {
                        if (list.get(0).isComplete()) {
                            comp = "true";
                        } else {
                            comp = "false";
                        }
                    }
                    if (countTopic <= topicsArrayList.size() - 1) {
                        if (countTopic == 0) {
                            if (topicsArrayList.get(0).get("TopicID").equals(topicId)) {
                                topicsArrayList.get(countTopic).put("isCompleted", comp);
                            } else {
                                String tID = topicsArrayList.get(countTopic).get("TopicID");
                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
                                return;
                            }
                        } else {
                            topicsArrayList.get(countTopic).put("isCompleted", comp);
                        }
                    }
                    countTopic++;
                    if (countTopic <= topicsArrayList.size() - 1) {
                        String tID = topicsArrayList.get(countTopic).get("TopicID");
                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (methodCall.equals("checkTopicCompleted")) {
                try {
                    boolean hide = true;
                    if (list != null && list.size() > 0) {
                        hide = !list.get(0).isComplete();
                    }
                    final Handler handler = new Handler();
                    boolean finalHide = hide;
                    handler.postDelayed(() -> refreshFragments(topicId, finalHide, 0, hideDialog), 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean getTopicCompleteStatus(String topicId) {
        boolean isComplete = false;
        for (HashMap<String, String> item : topicsArrayList) {
            if (item.containsKey("isCompleted") && item.get("TopicID").equals(topicId)) {
                isComplete = item.get("isCompleted").equals("true");
                break;
            }
        }
        return isComplete;
    }

    private boolean isPreviousTopicIdCompleted(int position, ArrayList<HashMap<String, String>> topicsArrayList) {
        boolean isComplete = false;
        List<TopicModel> list = topicRepository.getDetail(Util.getUserId(context));
        if (list != null && list.size() > 0) {
            if (position != 0) {
                String topicId = topicsArrayList.get(position - 1).get("TopicID");
                for (TopicModel item : list) {
                    if (item.getTopicId().equals(topicId)) {
                        isComplete = true;
                        break;
                    }
                }
            }
        }

        return isComplete;
    }

    public void setTopicIdList(String topicId) {

        if (!topicRepository.isDataExist(Util.getUserId(context), topicId)) {
            TopicModel topicModel = new TopicModel();
            topicModel.setUserId(Util.getUserId(context));
            topicModel.setTopicId(topicId);
            topicRepository.insertTopicDetails(topicModel);
        }
    }

    public int getNextTopicPosition(String topicId) {
        int position = 0;
        List<TopicPositionWiseModel> list = topicPositionRepository.getDetail(Util.getUserId(context), Util.getSubject(context));
        if (list != null && list.size() > 0) {
            if (list.get(0).getTopicId().equals(topicId)) {
                position = list.get(0).getTopicPosition();
            }
        }
        return position;
    }

    public void setNextTopicPosition(String topicId) {
        for (int i = 0; i < topicsArrayList.size(); i++) {
            if (topicsArrayList.get(i).get("TopicID").equals(topicId)) {
                nextTopicPosition = i + 1;
                if (nextTopicPosition > topicsArrayList.size() - 1) {
                    nextTopicPosition = 0;
                }

                if (topicPositionRepository.isDataExist(Util.getUserId(context), Util.getSubject(context))) {
                    topicPositionRepository.updateField(Util.getUserId(context), Util.getSubject(context), nextTopicPosition, topicsArrayList.get(nextTopicPosition).get("TopicID"));
                } else {
                    TopicPositionWiseModel topicPositionWiseModel = new TopicPositionWiseModel();
                    topicPositionWiseModel.setUserId(Util.getUserId(context));
                    topicPositionWiseModel.setSubjectId(Util.getSubject(context));
                    topicPositionWiseModel.setTopicPosition(nextTopicPosition);
                    topicPositionWiseModel.setTopicId(topicsArrayList.get(nextTopicPosition).get("TopicID"));
                    topicPositionRepository.insertTopicPositionWiseDetails(topicPositionWiseModel);
                }
                break;
            }
        }
    }

    public void setLastTopicId(String topicId) {
        if (topicSubjectRepository.isDataExist(Util.getUserId(context), Util.getSubject(context))) {
            topicSubjectRepository.updateField(Util.getUserId(context), Util.getSubject(context), topicId);
            for (TopicSubjectWiseModel item : lastTopicIdList) {
                item.setTopicId(topicId);
            }
        } else {
            TopicSubjectWiseModel topicSubjectWiseModel = new TopicSubjectWiseModel();
            topicSubjectWiseModel.setUserId(Util.getUserId(context));
            topicSubjectWiseModel.setSubjectId(Util.getSubject(context));
            topicSubjectWiseModel.setTopicId(topicId);
            topicSubjectRepository.insertTopicSubjectWiseDetails(topicSubjectWiseModel);
        }
    }

    private void getCompletedForAllTopics(PracticeTopicAdapter practiceTopicAdapter, ArrayList<HashMap<String, String>> topicsArrayList, String topicId) throws Exception {
        if (countTopic == topicsArrayList.size() - 1 && done) {
            if (topicsArrayList.size() > 0) {
                practiceTopicAdapter.topicsArrayList = topicsArrayList;
                practiceTopicAdapter.setTooltipVisibility(true);
                practiceTopicAdapter.notifyDataSetChanged();
                refreshTabLayout(Util.getTopicID(context), true);
                if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                    diagnosticTestCompleteDisposable.dispose();
                }
//                getAllFoundationalTopicData(practiceTopicAdapter, true);
//                practiceTopicAdapter.notifyDataSetChanged();
//                hideDialog();
//                String topicID = topicsArrayList.get(0).get("TopicID");
//                if(Util.getLastTopicID(context).equals(topicID)){
//                    showFoundationalTopicDialog(topicID, 0, 0);
//                }
            }
        } else {
            if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
//                try {

//                    DiagnosticCompleteTask diagnosticCompleteTask = new DiagnosticCompleteTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, "getCompletedForAllTopics", false, 0,false);
//                    diagnosticCompleteTask.execute();

                runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, null, "diagnosticTestComplete");

//                    String comp = "false";
//                    List<ReportsDiagnosticCompleteModel> list = reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
//                    if (list != null && list.size() > 0) {
//                        if(list.get(0).isComplete()){
//                            comp = "true";
//                        }else{
//                            comp = "false";
//                        }
//                    }
//                    if(countTopic <= topicsArrayList.size() - 1){
//                        if(countTopic == 0){
//                            if(topicsArrayList.get(0).get("TopicID").equals(topicId)){
//                                topicsArrayList.get(countTopic).put("isCompleted", comp);
//                            }else{
//                                String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                                return;
//                            }
//                        }else{
//                            topicsArrayList.get(countTopic).put("isCompleted", comp);
//                        }
//                    }
//                    countTopic++;
//                    if(countTopic <= topicsArrayList.size() - 1){
//                        String tID = topicsArrayList.get(countTopic).get("TopicID");
//                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                    }
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else {
                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            String comp;
                            if (snapshot.getValue() != null) {
                                comp = "true";
                            } else {
                                comp = "false";
                            }
                            if (countTopic <= topicsArrayList.size() - 1) {
                                if (countTopic == 0) {
                                    if (topicsArrayList.get(0).get("TopicID").equals(topicId)) {
                                        topicsArrayList.get(countTopic).put("isCompleted", comp);
                                    } else {
                                        String tID = topicsArrayList.get(countTopic).get("TopicID");
                                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
                                        return;
                                    }
                                } else {
                                    topicsArrayList.get(countTopic).put("isCompleted", comp);
                                }
                            }
                            if (countTopic == topicsArrayList.size() - 1) {
                                done = true;
                            } else {
                                countTopic++;
                                done = false;
                            }
                            if (countTopic <= topicsArrayList.size() - 1) {
                                String tID = topicsArrayList.get(countTopic).get("TopicID");
                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
                            }
                            boolean isCompleted = false;
                            isCompleted = comp.equals("true");
                            if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                                reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, isCompleted,Util.getSelectedLanguage(context));
                            } else {
                                ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                                reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                                reportsDiagnosticCompleteModel.setBoard(board);
                                reportsDiagnosticCompleteModel.setSClass(sClass);
                                reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                                reportsDiagnosticCompleteModel.setTopicId(topicId);
                                reportsDiagnosticCompleteModel.setComplete(isCompleted);
                                reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                                reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
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
    }

    private void getCompleteStatusForTopic(PracticeTopicAdapter practiceTopicAdapter, ArrayList<HashMap<String, String>> topicsArrayList, String topicId) {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, null, "diagnosticTestComplete");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String comp;
                        if (snapshot.getValue() != null) {
                            comp = "true";
                        } else {
                            comp = "false";
                        }
                        for (HashMap<String, String> item : topicsArrayList) {
                            if (item.get("TopicID").equals(topicId)) {
                                item.put("isCompleted", comp);
                                break;
                            }
                        }
                        practiceTopicAdapter.topicsArrayList = topicsArrayList;
                        practiceTopicAdapter.setTooltipVisibility(true);
                        practiceTopicAdapter.notifyDataSetChanged();
                        refreshTabLayout(Util.getTopicID(context), true);
//                    if(countTopic <= topicsArrayList.size() - 1){
//                        if(countTopic == 0){
//                            if(topicsArrayList.get(0).get("TopicID").equals(topicId)){
//                                topicsArrayList.get(countTopic).put("isCompleted", comp);
//                            }else{
//                                String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                                return;
//                            }
//                        }else{
//                            topicsArrayList.get(countTopic).put("isCompleted", comp);
//                        }
//                    }
//                    if(countTopic == topicsArrayList.size() - 1){
//                        done = true;
//                    }else{
//                        countTopic++;
//                        done = false;
//                    }
//                    if(countTopic <= topicsArrayList.size() - 1){
//                        String tID = topicsArrayList.get(countTopic).get("TopicID");
//                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                    }
                        boolean isCompleted = false;
                        isCompleted = comp.equals("true");
                        if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                            reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, isCompleted,Util.getSelectedLanguage(context));
                        } else {
                            ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                            reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                            reportsDiagnosticCompleteModel.setBoard(board);
                            reportsDiagnosticCompleteModel.setSClass(sClass);
                            reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                            reportsDiagnosticCompleteModel.setTopicId(topicId);
                            reportsDiagnosticCompleteModel.setComplete(isCompleted);
                            reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                            reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
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

    public void refreshFragments(String topicId, boolean hide, int position, boolean hideDialog) {
        if (hideDialog) {
            hideDialog();
        }
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof PalVideoListFragment) {
                ((PalVideoListFragment) fragment).hideLayout(topicId, hide, position);
            }
            if (fragment instanceof PalDikshaContentFragment) {
                ((PalDikshaContentFragment) fragment).hideLayout(topicId, hide, position);
            }
            if (fragment instanceof PalPracticeFrament) {
                ((PalPracticeFrament) fragment).hideLayout(topicId, hide, position);
            }
            if (fragment instanceof PalTestFrament) {
                ((PalTestFrament) fragment).hideLayout(topicId, hide, position);
            }


        }
    }

    private void checkTopicCompleted(String topicId, boolean hideDialog) {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {

            DiagnosticCompleteTask diagnosticCompleteTask = new DiagnosticCompleteTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, "checkTopicCompleted", false, 0, hideDialog);
            diagnosticCompleteTask.execute();

//            try {
//                boolean hide = true;
//                List<ReportsDiagnosticCompleteModel> list = reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId);
//                if (list != null && list.size() > 0) {
//                    if(list.get(0).isComplete()){
//                        hide = false;
//                    }else{
//                        hide = true;
//                    }
//                }
//                final Handler handler = new Handler();
//                boolean finalHide = hide;
//                handler.postDelayed(() -> refreshFragments(topicId, finalHide, 0, hideDialog), 1000);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        boolean hide;
                        boolean isCompleted = false;
                        if (snapshot.getValue() != null) {
                            hide = false;
                            isCompleted = true;
                        } else {
                            hide = true;
                            isCompleted = false;
                        }
                        refreshFragments(topicId, hide, 0, hideDialog);
                        if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                            reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, isCompleted,Util.getSelectedLanguage(context));
                        } else {
                            ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                            reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                            reportsDiagnosticCompleteModel.setBoard(board);
                            reportsDiagnosticCompleteModel.setSClass(sClass);
                            reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                            reportsDiagnosticCompleteModel.setTopicId(topicId);
                            reportsDiagnosticCompleteModel.setComplete(isCompleted);
                            reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                            reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
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

    public void refreshTestFragment() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof PalTestFrament) {
                ((PalTestFrament) fragment).refreshSpinner();
            }
        }
    }

    public boolean firsttime=false;

    public void showFoundationalTopicDialog(String userId, String subjectId, String seniorTopicId, String topicId, String topicName, String seniorClass, int type, int position,int mastrey,String streekprogress,int level) {

        String hundredPercentMastery;
        hundredPercentMastery = ninteen;

        if(firsttime)
        {
            foundationalTopicRepository.getFoundationalTopicsDetail(userId, subjectId, seniorTopicId, topicId, seniorClass).observe(PalContentListingActivity.this, new Observer<List<FoundationalTopicModel>>() {
                @Override
                public void onChanged(List<FoundationalTopicModel> foundationalTopicModels) {
                    if (foundationalTopicModels.size() > 0) {
                        for (int i = 0; i < foundationalTopicModels.size(); i++) {

                            String currentTopicId = foundationalTopicModels.get(i).getSeniorTopicID();

                            Util.setTopicID(context, foundationalTopicModels.get(i).getTopicId());
                            Util.setTopicNameAlt(context, foundationalTopicModels.get(i).getTopicName());
                            Util.setLevel(context, level);

                            global.setProgress(mastrey);

                            if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), foundationalTopicData.get(i).getTopicId())) {
                                trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, foundationalTopicData.get(i).getSeniorTopicID(), foundationalTopicData.get(i).getSeniorClass(), foundationalTopicData.get(i).getSeniorTopicName(), foundationalTopicData.get(i).getTopicId(), Util.getSelectedLanguagePackage(context));
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
                                trackTopicRepository.insertDetails(trackTopicModel);
                            }

                            HashMap<String, String> mapF = new HashMap<>();
                            mapF.put("foundational_class", foundationalTopicModels.get(i).getSClass());
                            mapF.put("foundational_topic", foundationalTopicModels.get(i).getTopicId());
                            global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(currentTopicId).setValue(mapF);
                            if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                                if(firsttime) {
                                    firsttime=false;
                                    startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", foundationalTopicModels.get(i).getSClass()).putExtra("streakProgress", streekprogress).putExtra("streak", foundationalTopicModels.get(i).getStreakCount()).putExtra("incorrectStreak", foundationalTopicModels.get(i).getIncorrectStreak()).putExtra("practiceType", "junior").putExtra("seniorClass", foundationalTopicModels.get(i).getSeniorClass()).putExtra("seniorTopicID", foundationalTopicModels.get(i).getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicModels.get(i).getSeniorTopicName()).putExtra("testPercentageAchieved", mastrey).putExtra("type", "foundation"));

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
                            onClickHandler(position);
                        } else {
//                        Toast.makeText(context, hundredPercentMastery + topicName + ".", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshListing() {
//        showDialog();
        mProgressBar.setVisibility(View.VISIBLE);
        loading_text.setVisibility(View.VISIBLE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        layout_connection.setVisibility(View.VISIBLE);
        isloading=false;
        PalContentListingActivity.keys="";
        try {
            PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ((PalContentListingActivity) context).removeFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkConnection(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPath(subject);
                try {
                    PalVideoListFragment.videoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1500 );//time in milisecond


    }

    public LiveData<List<TestModel>> getSubmissionDateForDiagnosticTest(String userId, String subjectId, String topicId) {
        return testDetailsRepository.getDetail(userId, subjectId, topicId);
    }

    private void getAllFoundationalTopicData(PracticeTopicAdapter practiceTopicAdapter, boolean hideDialog) {
        runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), null, null, "foundationalTopicData");
//        foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(context), Util.getSubject(context)).observe(PalContentListingActivity.this, new Observer<List<FoundationalTopicModel>>() {
//            @Override
//            public void onChanged(List<FoundationalTopicModel> foundationalTopicModels) {
//                if(foundationalTopicModels.size() > 0){
//                    foundationalTopicData = foundationalTopicModels;
//                }
//                practiceTopicAdapter.notifyDataSetChanged();
//                if(hideDialog){
//                    hideDialog();
//                }
//            }
//        });
    }

    public void getTopicSeenVideoListing(boolean refreshAdapter) {
        PalContentListingActivity.refreshAdapter = refreshAdapter;
//        List<VideoModel> list = videoDetailsRepository.getVideoDetails(Util.getUserId(context), board, sClass, subject);
//        if(list != null && list.size() > 0){
//            topicVideoArrayList = (ArrayList<VideoModel>) list;
//        }
//        VideoListingTask videoListingTask = new VideoListingTask(Util.getUserId(context), subject, board, sClass);
//        videoListingTask.execute();
        runBackgroundTask(Util.getUserId(Util.getContext()), board, sClass, subject, null, null, "videoListingTask");

//        videoDetailsRepository.getVideoDetails(Util.getUserId(context), board, sClass, subject).observe(PalContentListingActivity.this, new Observer<List<VideoModel>>() {
//            @Override
//            public void onChanged(List<VideoModel> videoModels) {
//                if(videoModels.size() > 0){
//                    topicVideoArrayList = (ArrayList<VideoModel>) videoModels;
//                }
//            }
//        });
    }

    private void saveLastTopicDetails() {
        if (lastTopicDetailsRepository.isDataExist(Util.getUserId(context), board, sClass, subject, Util.getSelectedLanguage(context))) {
            lastTopicDetailsRepository.updateField(Util.getUserId(context), board, sClass, subject, Util.getTopicID(context), Util.getTopicNameAlt(context), subjectName, icon, color, Util.getSelectedLanguage(context));
        } else {
            LastTopicDetailsModel lastTopicDetailsModel = new LastTopicDetailsModel();
            lastTopicDetailsModel.setUserId(Util.getUserId(context));
            lastTopicDetailsModel.setBoard(board);
            lastTopicDetailsModel.setSClass(sClass);
            lastTopicDetailsModel.setSubject(subject);
            lastTopicDetailsModel.setTopicId(Util.getTopicID(context));
            lastTopicDetailsModel.setTopicName(Util.getTopicNameAlt(context));
            lastTopicDetailsModel.setSubjectName(subjectName);
            lastTopicDetailsModel.setIconUrl(icon);
            lastTopicDetailsModel.setColor(color);
            lastTopicDetailsModel.setLanguage(Util.getSelectedLanguage(context));
            lastTopicDetailsRepository.insertDetails(lastTopicDetailsModel);
        }
    }

    public boolean isFragmentAdded() {
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("tag");
        return fragment != null;
    }

    public static void hideNavigationBar(Window window) {
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(flags);
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

    public void runBackgroundTask(String userId, String board, String sClass, String subject, String topicId, String value, String type) {
        valueSend = value;
        userIDSend = userId;
        topicID = topicId;
        if(board==null)board=Util.getSelectedBoard(context);
        if (type.equals("lastTopicIdTask")) {
            getList(userId, board, sClass, subject, topicId, value, "lastTopicIdTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            lastTopicIdDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
//                        updateUi((ArrayList<TopicSubjectWiseModel>) o);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            lastTopicIdDisposable.dispose();
                        }
                    });
        } else if (type.equals("pathTask")) {
            String finalBoard = board;
            getList(userId, board, sClass, subject, topicId, value, "pathTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            pathDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsTopicPathModel> list = (ArrayList<ReportsTopicPathModel>) o;
                            if (topicId == null) {
                                if (list != null && list.size() > 0) {
                                    data = new HashMap<>();
                                    for (ReportsTopicPathModel item : list) {
                                        data.put(item.getTopicId(), item.getTypeV());
                                    }
                                }
                                try {
                                    isCompleted = false;
                                    countTopic = 0;
                                    count = 0;
                                    getTopicSeenVideoListing(false);
                                    getTopics(subject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (list != null && list.size() > 0) {
                                    String path = list.get(0).getTypeV();
                                    StringBuilder strBuilder = new StringBuilder();
                                    if (!path.contains(value)) {
                                        strBuilder.append(path).append("-").append(value);
                                        if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), finalBoard, sClass, subject, topicId)) {
                                            reportsTopicPathRepository.updateField(Util.getUserId(context), finalBoard, sClass, subject, topicId, strBuilder.toString());
                                        } else {
                                            ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                            reportsTopicPathModel.setUserId(Util.getUserId(context));
                                            reportsTopicPathModel.setBoard(finalBoard);
                                            reportsTopicPathModel.setSClass(sClass);
                                            reportsTopicPathModel.setSubject(subject);
                                            reportsTopicPathModel.setTopicId(topicId);
                                            reportsTopicPathModel.setTypeV(strBuilder.toString());
                                            reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                                        }
                                    }
                                } else {
                                    topicPathData.add(topicId + "-" + value);
                                    StringBuilder strBuilder = new StringBuilder();
                                    int count = 0;
                                    for (String item : topicPathData) {
                                        String[] separated = item.split("-");
                                        if (separated[0].equals(topicId)) {
                                            if (count == 0) {
                                                strBuilder.append(separated[1]);
                                            } else {
                                                strBuilder.append("-").append(separated[1]);
                                            }
                                        }
                                        count++;
                                    }
                                    if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), finalBoard, sClass, subject, topicId)) {
                                        reportsTopicPathRepository.updateField(Util.getUserId(context), finalBoard, sClass, subject, topicId, strBuilder.toString());
                                    } else {
                                        ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                        reportsTopicPathModel.setUserId(Util.getUserId(context));
                                        reportsTopicPathModel.setBoard(finalBoard);
                                        reportsTopicPathModel.setSClass(sClass);
                                        reportsTopicPathModel.setSubject(subject);
                                        reportsTopicPathModel.setTopicId(topicId);
                                        reportsTopicPathModel.setTypeV(strBuilder.toString());
                                        reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            pathDisposable.dispose();
                        }
                    });
        } else if (type.equals("videoListingTask")) {
            getList(userId, board, sClass, subject, topicId, value, "videoListingTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            videoListingTopicDisposable = d;
                        }
                        @Override
                        public void onNext(Object o) {
                            topicVideoArrayList = (ArrayList<VideoModel>) o;
                            if (refreshAdapter && practiceTopicAdapter != null) {
                                practiceTopicAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                        }
                        @Override
                        public void onComplete() {
                            videoListingTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("diagnosticTestComplete")) {
            String finalBoard1 = board;
            getList(userId, board, sClass, subject, topicId, value, "diagnosticTestComplete").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            diagnosticTestCompleteDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsDiagnosticCompleteModel> list = (ArrayList<ReportsDiagnosticCompleteModel>) o;
                            try {
                                String comp = "false";
                                if (list != null && list.size() > 0) {
                                    if (list.get(0).isComplete()) {
                                        comp = "true";
                                    } else {
                                        comp = "false";
                                    }
                                }
                                for (HashMap<String, String> item : topicsArrayList) {
                                    if (item.get("TopicID").equals(topicId)) {
                                        item.put("isCompleted", comp);
                                        break;
                                    }
                                }
                                practiceTopicAdapter.topicsArrayList = topicsArrayList;
                                practiceTopicAdapter.setTooltipVisibility(true);
                                practiceTopicAdapter.notifyDataSetChanged();
                                refreshTabLayout(Util.getTopicID(Util.getContext()), true);

                                boolean isCompleted = false;
                                isCompleted = comp.equals("true");
                                if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(Util.getContext()), finalBoard1, sClass, Util.getSubject(Util.getContext()), topicId,Util.getSelectedLanguage(context))) {
                                    reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(Util.getContext()), finalBoard1, sClass, Util.getSubject(Util.getContext()), topicId, isCompleted,Util.getSelectedLanguage(context));
                                } else {
                                    ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                                    reportsDiagnosticCompleteModel.setUserId(Util.getUserId(Util.getContext()));
                                    reportsDiagnosticCompleteModel.setBoard(finalBoard1);
                                    reportsDiagnosticCompleteModel.setSClass(sClass);
                                    reportsDiagnosticCompleteModel.setSubject(Util.getSubject(Util.getContext()));
                                    reportsDiagnosticCompleteModel.setTopicId(topicId);
                                    reportsDiagnosticCompleteModel.setComplete(isCompleted);
                                    reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                                    reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                        try {
//                            String comp = "false";
//                            if (list != null && list.size() > 0) {
//                                if(list.get(0).isComplete()){
//                                    comp = "true";
//                                }else{
//                                    comp = "false";
//                                }
//                            }
//                            if(countTopic <= topicsArrayList.size() - 1){
//                                if(countTopic == 0){
//                                    if(topicsArrayList.get(0).get("TopicID").equals(topicId)){
//                                        topicsArrayList.get(countTopic).put("isCompleted", comp);
//                                    }else{
//                                        String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                                        return;
//                                    }
//                                }else{
//                                    topicsArrayList.get(countTopic).put("isCompleted", comp);
//                                }
//                            }
//                            countTopic++;
//                            if(countTopic <= topicsArrayList.size() - 1){
//                                String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                            }
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
//                        disposable.dispose();
                        }
                    });
        } else if (type.equals("foundationalTopicData")) {
            getList(userId, board, sClass, subject, topicId, value, "foundationalTopicData").subscribeOn(Schedulers.io())
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
                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {

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

                                            List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                                            for (Fragment fragment : allFragments) {
                                                if (fragment instanceof PalVideoListFragment) {
                                                    ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
                                                } else if (fragment instanceof PalDikshaContentFragment) {
                                                    ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
                                                }
                                            }
//                                            break;
                                        }
                                    }
                                }
                            }
                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            foundationalTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("testScore")) {

            String finalBoard2 = board;
            getList(userId, board, sClass, subject, topicId, value, "testScore").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            testScoreTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            List<ReportsLatestDataTestModel> list = (ArrayList<ReportsLatestDataTestModel>) o;
                            for (ReportsLatestDataTestModel item : list) {
                                if (testScoreModelArrayList.size() > 0) {
                                    for (TestScoreModel data : testScoreModelArrayList) {
                                        if (data.getTopicId().equals(item.getTopicId()) && data.getType().equals(item.getType())) {
                                            testScoreModelArrayList.remove(data);
                                            break;
                                        }
                                    }
                                }
                                testScoreModelArrayList.add(new TestScoreModel(item.getTopicId(), item.getType(), item.getReportsTestScoreModel().getScores() + "/" + item.getReportsTestScoreModel().getTotalScores(), item.getReportsTestScoreModel().getPercentageScored()));
                            }
                            runBackgroundTask(userId, finalBoard2, sClass, subject, topicId, value, "practiceScore");
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            testScoreTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("practiceScore")) {
            getList(userId, board, sClass, subject, topicId, value, "practiceScore").subscribeOn(Schedulers.io())
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
                            updateUi();
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
        } else if (type.equals("finalTestCompletionTask")) {
            getList(userId, board, sClass, subject, topicId, value, "finalTestCompletionTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            finalTestCompleteDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            finalTestTopicIdArrayList = new ArrayList<>();
                            List<FinalTestCompleteModel> list = (List<FinalTestCompleteModel>) o;
                            if (list != null && list.size() > 0) {
                                for (FinalTestCompleteModel item : list) {
                                    finalTestTopicIdArrayList.add(item.getTopicId());
                                }
                            }
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            finalTestCompleteDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String topicId, String value, String type) {
        if (type.equals("lastTopicIdTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return topicSubjectRepository.getDetail(userId, subject);
            });
        } else if (type.equals("pathTask")) {
            if (topicId == null) {
                return Observable.fromCallable(() -> {
                    //do something, get your Data object
                    return reportsTopicPathRepository.getDetail(userId, board, sClass, subject, null);
                });
            } else {
                return Observable.fromCallable(() -> {
                    //do something, get your Data object
                    return reportsTopicPathRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, subject, topicId);
                });
            }
        } else if (type.equals("videoListingTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return videoDetailsRepository.getVideoDetails(userId, board, sClass, subject.toLowerCase());
            });
        } else if (type.equals("diagnosticTestComplete")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId);
            });
        } else if (type.equals("foundationalTopicData")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return foundationalTopicRepository.getAllFoundationalTopicDetails(userId);
            });
        } else if (type.equals("testScore")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataTestRepository.getDetail(userId, board, sClass, subject, topicId);
            });
        } else if (type.equals("practiceScore")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, "practice", null);
            });
        } else if (type.equals("finalTestCompletionTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return finalTestCompleteRepository.getDetail(userId, board, sClass, subject);
            });
        }
        return null;
    }

    LottieAnimationView animationView;
    LinearLayout onboading_layout;
    TextView onboading_heading_text, onboading_body_text, onboading_skip_text;
    Button onboading_nextbtn,onboarding_previousBtn;
    TextView onboading_point1, onboading_point2, onboading_point3, onboading_point4, onboading_point5, onboading_point6;
    ArrayList<String> jsonList = new ArrayList<>();
    ArrayList<String> headingList = new ArrayList<>();
    ArrayList<String> bodyList = new ArrayList<>();
    int onboading_position = 0;
    Animation animation,animation2;
    String continue_text,next_text,getstarted_textl,previous_textl;

    boolean isPrevious=false;
    ArrayList<String> onBoadingtextArrayList;

    private void showOnBoarding() {
        onboading_layout = findViewById(R.id.onboading_layout);

        animationView = findViewById(R.id.animationView);
        onboading_heading_text = findViewById(R.id.onboading_heading_text);
        onboading_body_text = findViewById(R.id.onboading_body_text);
        onboarding_previousBtn = findViewById(R.id.onboarding_previousBtn);
        onboading_skip_text = findViewById(R.id.onboading_skip_text);

        onboading_nextbtn = findViewById(R.id.onboading_nextbtn);

        onboading_point1 = findViewById(R.id.onboading_point1);
        onboading_point2 = findViewById(R.id.onboading_point2);
        onboading_point3 = findViewById(R.id.onboading_point3);
        onboading_point4 = findViewById(R.id.onboading_point4);
        onboading_point5 = findViewById(R.id.onboading_point5);
        onboading_point6 = findViewById(R.id.onboading_point6);

        animationView.setAnimation("PAL-Topic.json");
        onboading_nextbtn.requestFocus();

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_rever);

        onboading_layout.setVisibility(View.VISIBLE);
        onboading_layout.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);

        gettext_();

        onboading_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                if (!onboading_nextbtn.getText().equals(getstarted_textl)) {
                    onboading_position++;
                    showOnBoadingScreen();
                } else {
                    view.setEnabled(false);
                    onboading_nextbtn.setVisibility(View.GONE);
                    onboading_layout.startAnimation(animation2);
                    onboading_layout.setVisibility(View.GONE);
                    onboading_nextbtn.clearFocus();
                }
            }
        });

        onboarding_previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                view.postDelayed(()-> view.setEnabled(true), 150);
                onboading_position--;
                isPrevious=true;
                showOnBoadingScreen();
            }
        });

        onboading_skip_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onboading_layout.startAnimation(animation2);
//                onboading_layout.setVisibility(View.INVISIBLE);
//                onboading_nextbtn.clearFocus();
            }
        });

        onboading_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-");
            }
        });

    }

    private void gettext_() {

        if(Util.isOfflineMode(context)) set_static_text();
        else global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context))
                .child("Contentpage onBoarding screen").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                onBoadingtextArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                setdata();
                                showOnBoadingScreen();
                            } else set_static_text();
                        } catch (Exception e) {
                            Util.dismissdataDialog();
                            e.printStackTrace();
                            set_static_text();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        set_static_text();
                    }
                });

    }

    private void set_static_text() {
        onBoadingtextArrayList = new ArrayList<>();
        if (Util.getSelectedLanguage(context).equals("hindi")) {
            onBoadingtextArrayList.add("अपने अध्याय का चयन करें");
            onBoadingtextArrayList.add("डायग्नोस्टिक परिक्षण");
            onBoadingtextArrayList.add("उपचारात्मक वीडियो के साथ अभ्यास करें ");
            onBoadingtextArrayList.add("अंतिम परिक्षण दें और अध्याय पर महारत बनाएं");
            onBoadingtextArrayList.add("यहाँ उपलब्ध सभी अध्याय आपके पाठ्यक्रम के अनुसार हैं। जो अध्याय आप पढ़ना चाहते हैं, उसपे क्लिक करके आगे बढ़ें");
            onBoadingtextArrayList.add("आप जिस भी अध्याय का चयन करेंगे, आपको सबसे पहले इस अध्याय के डायग्नोस्टिक परिक्षण को देना होगा। कृपया ध्यान रखें की आप डायग्नोस्टिक परिक्षण केवल एक बार ही दे सकते हैं। आपकी इस अध्याय पर कितनी पकड़ है, इसके अनुसार iPrep PAL ऍप आपको अगले कदम के लिए मार्गदर्शित करेगा।");
            onBoadingtextArrayList.add("हर अध्याय पर आपकी अभी की समझ के अनुसार, iPrep PAL ऍप आपको अभ्यास प्रश्न और उपचारात्मक वीडियो के प्रयोग से आपकी सभी अध्यायों पर पूरी तरह से समझ बनाने में सक्षम करेगा।");
            onBoadingtextArrayList.add("जब आप अभ्यास मैं १००% मास्टरी पर पहुंच जाएंगे, तब आपको हर अध्याय का अंतिम परिक्षण देना होगा। यदि आप इस परिक्षण में ८०% से अधिक अंक लाते हैं, तो आप उस अध्याय को पूर्ण कर लेंगे। अगर आप ८०% अंक नहीं लाते, तो आपको मायूस नहीं होना है। iPrep PAL ऍप आपको अगले कदम के लिए मार्गदर्शित करेगा ताकि आप और बेहतर तरीके से अपनी तैयारी कर पाएं।");
            onBoadingtextArrayList.add("शुरू करो");
            onBoadingtextArrayList.add("अगला");
            onBoadingtextArrayList.add("आरंभ करें");

        } else {
            onBoadingtextArrayList.add("Select a topic you want to learn");
            onBoadingtextArrayList.add("Start with a Diagnostic Test");
            onBoadingtextArrayList.add("Practice Questions with Videos");
            onBoadingtextArrayList.add("Take Final Test and Earn your badge");
            onBoadingtextArrayList.add("All topics provided here are as per your syllabus. To start learning, click on any topic of your choice");
            onBoadingtextArrayList.add("To begin learning any topic, you will have to take a Diagnostic Test. Please note that you can take a Diagnostic Test only once. Based on your understanding, iPrep PAL app will guide you with appropriate next steps personalised for you");
            onBoadingtextArrayList.add("Based on your current level of understanding, iPrep PAL app will guide you to do Practice questions where you will get feedback and watch enjoyable animated videos to build complete understanding on a topic");
            onBoadingtextArrayList.add("Once you have achieved 100% mastery in Practice, you will have to take a Final Test in every topic. To earn your badge, you must get 80% or more. Just in case you do not, there is nothing to worry. iPrep PAL app will guide you with next steps on how you can prepare better and get the badge");
            onBoadingtextArrayList.add("Continue");
            onBoadingtextArrayList.add("Next");
            onBoadingtextArrayList.add("Get started");
        }

        setdata();
        showOnBoadingScreen();
    }

    private void setdata() {
        jsonList.clear();
        headingList.clear();
        bodyList.clear();

        if(Util.getSelectedLanguage(context).equals("hindi")) {
            jsonList.add("PAL-Topic_Hindi.json");
            jsonList.add("PAL-Diagnos_Hindi.json");
            jsonList.add("PAL-Prac_Hindi.json");
            jsonList.add("PAL-FinalTest_Hindi.json");
        }
        else {
            jsonList.add("PAL-Topic.json");
            jsonList.add("PAL-Diagnos.json");
            jsonList.add("PAL-Prac.json");
            jsonList.add("PAL-FinalTest.json");
        }

        headingList.add(onBoadingtextArrayList.get(0));
        headingList.add(onBoadingtextArrayList.get(1));
        headingList.add(onBoadingtextArrayList.get(2));
        headingList.add(onBoadingtextArrayList.get(3));

        bodyList.add(onBoadingtextArrayList.get(4));
        bodyList.add(onBoadingtextArrayList.get(5));
        bodyList.add(onBoadingtextArrayList.get(6));
        bodyList.add(onBoadingtextArrayList.get(7));

        continue_text=onBoadingtextArrayList.get(8);

        //change index to 9 you will get "Next" text
        next_text=onBoadingtextArrayList.get(8);
        getstarted_textl=onBoadingtextArrayList.get(10);

        if(Util.getSelectedLanguage(context).equals("hindi")) onBoadingtextArrayList.add("पीछे जाएँ");
        else onBoadingtextArrayList.add("Previous");

        previous_textl=onBoadingtextArrayList.get(11);


        onboarding_previousBtn.setText(previous_textl);
        onboading_nextbtn.setText(continue_text);

        onboading_point5.setVisibility(View.GONE);
        onboading_point6.setVisibility(View.GONE);
    }

    private void showOnBoadingScreen() {

        if(onboading_position<jsonList.size()) {
            if(onboading_position!=0) {
                onboading_nextbtn.setText(next_text);
                onboarding_previousBtn.setVisibility(View.VISIBLE);
            }

            if(onboading_position==0) onboarding_previousBtn.setVisibility(View.INVISIBLE);

            animationView.setAnimation(jsonList.get(onboading_position));
            animationView.loop(true);
            animationView.playAnimation();

            onboading_heading_text.setText(headingList.get(onboading_position));
            onboading_body_text.setText(bodyList.get(onboading_position));

            if(isPrevious) {
                animationView.startAnimation(animation2);
                onboading_heading_text.startAnimation(animation2);
                onboading_body_text.startAnimation(animation2);
            }
            else {
                animationView.startAnimation(animation);
                onboading_heading_text.startAnimation(animation);
                onboading_body_text.startAnimation(animation);
            }
            switch (onboading_position) {

                case 0 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.selected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));

                    break;

                case 1 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.selected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));

                    break;

                case 2 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.selected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));

                    break;

                case 3 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.selected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));

                    break;

                case 4 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.selected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));

                    break;

                case 5 :
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.selected_pointer));

                    break;

                default:
                    onboading_point1.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point2.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point3.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point4.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point5.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
                    onboading_point6.setTextColor(context.getResources().getColor(R.color.unselected_pointer));
            }

            if(onboading_position==jsonList.size()-1) {
                onboading_nextbtn.setText(getstarted_textl);
                onboading_skip_text.setVisibility(View.INVISIBLE);
                Util.setContentonboadingMode(context,true);
            }

        }

    }

    public static int autoPlayDelay=5000;
    public static int connectionLayoutDelay=500;
    private void playLevelVideo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(videoKey != null){
                    int videoLevel = Util.getVideoLevel(context)+1;
                    int videoPosition = 0;
                    videoKey = videoLevel + "-" + videoPosition;
                    openVideoView(videoKey,"video_lessons");
                    PracticeTopicActivity.autoplayLevelVideo=false;
//                    practiceTopicAdapter.notifyDataSetChanged();
                }
            }
        }, autoPlayDelay);
    }

}