package com.idreameducation.ipreppal.PalMobile.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idreameducation.ipreppal.PalMobile.adapter.TrackTestAdapter_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.activity.DataTypeConverter;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.GetTopicLevelsDetails;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalGlobalSearchActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.SelectTopicActivity;
import com.idreameducation.ipreppal.pal.activity.TestReviewActivity;
import com.idreameducation.ipreppal.PalMobile.adapter.DiagonosticTestAdapter_Mobile;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestScoreModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.TestModel;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDateWiseTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TestDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TrackTopicRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.ArcProgress;
import com.idreameducation.ipreppal.util.CountDownTimerWithPause;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;
import com.skydoves.balloon.ArrowOrientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;


public class DiagonosticTestActivity_Mobile extends AppCompatActivity{

    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private final long timeInMilliseconds = 0L;
    private final long timeSwapBuff = 0L;
    private final long updatedTime = 0L;
    /*Dec Widgets*/
    private RecyclerView mRecyclerView,recyclerViewTrack;
    private Context context;
    private TextView textViewTime,textViewToatal,textViewTopicName,textViewSubmit,textViewdUserName,btnChangeLanguageEnglish,btnChangeLanguage;
    private ImageView imageViewBack;
    private ImageView downBtn,colapseImage,imageViewBackCross;
    private Global global;
    private String selectedLanguge;
    private DiagonosticTestAdapter_Mobile diagonosticTestAdapterMobile;
    public TrackTestAdapter_Mobile trackTestAdapterMobile;
    private ArrayList<HashMap<String, String>> questionsArrayList;
    private ArrayList<HashMap<Integer, String[]>> arrayListForUser;
    private ArrayList<HashMap<String, HashMap<String, String>>> questionArrayList;
    private long duration = 0;
    private CountDownTimerWithPause countDownTimerWithPause;
    private ArcProgress arcProgress;
    private int progress = 0;
    private String date;
    private String attemptedQuestions = "0";
    private boolean isNeededToOpenBackDialog = true;
    private TextView resume_text;

    private String totalTime,welcomeBack,resume,warning,exitMessage,continueTest,
            unattemptedQLeft,sureSubmit,submitTest,lastMessage,imageInternet,submit
            ,board,completeTest,calculateScore,minutes;

    private String start;
    private String attempted;
    private String attempt;
    private String exitTest;
    private String greeting;
    private String yourScore;
    private String review;
    private String goBack;
    private String startMessage;
    private String questionAttempted;

    //    private SnapTopLinearLayoutManager manager;
    private boolean isDialogOpend = false;
    private long startTime = 0L;
    private long endTime = 0L;
    private RelativeLayout reletiveLayout,reletiveLayoutLowerLevel,reletiveTop,linearActionLayout;
    private LinearLayout linearLayoutTrack;
    private ImageView imageViewBack_;
    //        private TextView textViewSkip;
    private boolean toggleQuestionsList = false;
    private TextView attemptedQ, skippedQ, yetToAttemptQ;
    public TreeSet<Integer> attemptedQMap = new TreeSet<>();
    public HashMap<Integer, Boolean> skippedQHashMap = new HashMap<>();
    private int skipCount = 0;
    private int topicPosition;
    private int testPercentageAchieved;
    private Disposable timeTaskDisposable;
    private Disposable countTaskDisposable;

    private FoundationalTopicRepository foundationalTopicRepository;
    private TestDetailsRepository testDetailsRepository;
    public HashMap<String, Boolean> restoreQuestionHashMap = new HashMap<>();
    private ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;
    private TrackTopicRepository trackTopicRepository;
    private ReportsLatestDataTestRepository reportsLatestDataTestRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWiseTestRepository reportsDateWiseTestRepository;
    private ReportsTopicWiseTestRepository reportsTopicWiseTestRepository;

    private TextView textViewdMessaeg,textViewDetail,info,  buttonPractice,textViewTitleName;

    ImageView imageViewCross;

    private TextView buttonVideo;
    private TextView orText;

    private FrameLayout transparentBg;


    private String isAssigned;
    private String batchId;
    private String keyTo;
    private String datetostore;
    private String teacherID;
    private String subject;
    private TextView textViewAllQuestions;
    private String leaveQuestions,submitAnswer,parikshanSubmit,questionText,diagnosticText,previousQuestionText;
    public int pos = 0;
    private String sClass;
    private String topicId;
    private String current_topicId;
    String filePath;
    private String questionID;

    private long diagonosticCount;
    private long timeget;

    CustomLinearLayoutManager lm;
    private String skipOnlastQuestion;

    ArrayList<String> textArrayList;


    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private TextView screenshotText, callText;

    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler1=new Handler();
    ProgressBar mProgressBar;
    LinearLayout loading_layout;

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

    private Uri filePath2;
    private String linkFromFirebaseStorage,textSendWithImageTextString;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;
    int classs=11;

    String attempted_text="Attempted";
    String skipped_text="Skipped";
    String yet_to_attempt_text="yet to attempt";

    ProgressBar progressBar;
    CardView card;

    /** Activity Methods*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_model_paper);

        context = this;
        init();
        get_text_from_backend();
        checkConnection(false);

        /*Call hookUp function*/
        assignIds();
        listners();
    }

    @Override
    protected void onPause() {
        super.onPause();


        Util.preventPause(context,getTaskId());
        try {
            if (!global.isImageAdded()) {
                if (!isDialogOpend) {
                    if (countDownTimerWithPause != null) {
                        isDialogOpend = true;

                        countDownTimerWithPause.pause();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);

        try {
            if (!global.isImageAdded()) {
                if (isNeededToOpenBackDialog) {
                    if (countDownTimerWithPause != null) {
                        if (countDownTimerWithPause.isPaused()) {
                            if (isDialogOpend) {
                                isNeededToOpenBackDialog = false;
                                resumeModelTestpaper();
                            }
                        }
                    }
                }
            } else {
                global.setImageAdded(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearLayoutTrack.setVisibility(View.GONE);
        clearSkipData();
        restoreQuestionHashMap.clear();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
//        PalContentListingActivity_Mobile.backPressed = true;
//        super.onBackPressed();
        
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                diagonosticTestAdapterMobile.notifyDataSetChanged();
            } else {
                diagonosticTestAdapterMobile.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Override onActivityResult method
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
            filePath2 = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath2);
                // imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    /** Init Diagonostic Test Activity */
    @SuppressLint("WrongViewCast")
    private void init() {
        transparentBg = findViewById(R.id.transparentBg);

        textViewdMessaeg = findViewById(R.id.textViewdMessaeg);
        textViewDetail = findViewById(R.id.textViewDetail);
        textViewAllQuestions = findViewById(R.id.textViewAllQuestions);
        info = findViewById(R.id.info);
        buttonPractice = findViewById(R.id.buttonPractice);
        buttonVideo = findViewById(R.id.buttonVideo);
        orText = findViewById(R.id.orText);
        textViewTitleName = findViewById(R.id.textViewTitleName);
        linearLayoutTrack = findViewById(R.id.linearLayoutTrack);
        linearLayoutTrack.setVisibility(View.GONE);
        linearActionLayout = findViewById(R.id.linear);
        imageViewBack_ = findViewById(R.id.imageViewBack_);
        card = findViewById(R.id.cardv);

        attemptedQ = findViewById(R.id.attemptedQ);
        skippedQ = findViewById(R.id.skippedQ);
        yetToAttemptQ = findViewById(R.id.yetToAttemptQ);
        reletiveTop = findViewById(R.id.reletiveTop);
        reletiveLayoutLowerLevel = findViewById(R.id.reletiveLayoutLowerLevel);
        reletiveLayout = findViewById(R.id.reletiveLayout);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBackCross = findViewById(R.id.imageViewBackCross);
        mRecyclerView = findViewById(R.id.recyclerView);
        recyclerViewTrack = findViewById(R.id.recyclerViewTrack);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,5);
        recyclerViewTrack.hasFixedSize();
        recyclerViewTrack.setLayoutManager(linearLayoutManager);
        downBtn = findViewById(R.id.downBtn);
        colapseImage = findViewById(R.id.colapseImage);
        resume_text = findViewById(R.id.resume_text);

        imageViewCross = findViewById(R.id.imageViewCross);
        textViewToatal = findViewById(R.id.textViewToatal);
        textViewTopicName = findViewById(R.id.textViewTopicName);
        textViewTopicName.setText(Util.getTopicNameAlt(context));
        textViewTime = findViewById(R.id.textViewTime);
        arcProgress = findViewById(R.id.arcProgress);
        arcProgress.setVisibility(View.GONE);
        btnChangeLanguageEnglish = findViewById(R.id.btnChangeLanguageEnglish);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);

        textViewSubmit = findViewById(R.id.textViewSubmit);
        textViewdUserName = findViewById(R.id.textViewdUserName);
        styleType_image=findViewById(R.id.styleType_image);

        imageViewBackCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

        resume_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (!toggleQuestionsList) {
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
//                    layoutParams.leftMargin = 400;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;
                } else {
                    linearLayoutTrack.setVisibility(View.GONE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });

        styleType_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if(gridView) {
                    recyclerViewTrack.setLayoutManager(linearLayoutManager);
                    trackTestAdapterMobile.gridView=false;
                    gridView=false;
                    styleType_image.setImageResource(R.drawable.icon_open_grid_two_up);
                } else {
                    recyclerViewTrack.setLayoutManager(gridLayoutManager);
                    trackTestAdapterMobile.gridView=true;
                    gridView=true;
                    styleType_image.setImageResource(R.drawable.icon_awesome_list);
                }
                trackTestAdapterMobile.notifyDataSetChanged();
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "=");
            }
        });

    }

    ImageView styleType_image;
    boolean gridView=false;


    /*Init values , widgets , strings etc...*/
    @SuppressLint("RestrictedApi")
    private void assignIds() {
        context = this;
//        Util.setContext(context);
        global = (Global) getApplicationContext();
        global.setImageAdded(false);
        startTime = System.currentTimeMillis();
        global.sendData("TestPaper", this.getClass().getName());
        database = FirebaseDatabase.getInstance();
        startTime = System.currentTimeMillis();
        databaseReference = database.getReference();
        reportsDiagnosticTestCompleteRepository = new ReportsDiagnosticTestCompleteRepository(context);
        foundationalTopicRepository = new FoundationalTopicRepository(context);
        testDetailsRepository = new TestDetailsRepository(context);
        trackTopicRepository = new TrackTopicRepository(context);
        reportsLatestDataTestRepository = new ReportsLatestDataTestRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseTestRepository = new ReportsDateWiseTestRepository(context);
        reportsTopicWiseTestRepository = new ReportsTopicWiseTestRepository(context);
        String time = null;
        board = Util.getSelectedBoard(context);
        try {
            time = getIntent().getStringExtra("Time");
            if (time != null) {
                duration = Long.parseLong(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
            duration = 10;
        }
        try {
            sClass = getIntent().getStringExtra("sClass");

            if (sClass == null) {
                sClass = Util.getSelectedClassName(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sClass = Util.getSelectedClassName(context);
        }


        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);

        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);
        screenshotText.setVisibility(View.GONE);
        callText.setVisibility(View.GONE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        isAssigned = getIntent().getStringExtra("isAssigned");
        batchId = getIntent().getStringExtra("batchId");
        keyTo = getIntent().getStringExtra("key");
        datetostore = getIntent().getStringExtra("date");
        teacherID = getIntent().getStringExtra("teacherID");
        subject = getIntent().getStringExtra("subject");
        topicPosition = getIntent().getIntExtra("topicPosition", 0);

        topicId = Util.getTopicID(context);
        current_topicId = Util.getTopicID(context);

//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            textViewDetail.setText(Util.getUsername(context) + " आपके डायग्नोस्टिक परिक्षण के परिणाम के आधार पर, आगे बढ़ने के लिए आपको इस पाठ में \n" +
//                    "१००% मास्टरी प्राप्त करनी होगी इस पाठ के टेस्ट को क्लियर करना होगा \n" +
//                    "१००% मास्टरी के लिए नीचे दिए गए किसी भी बटन पे क्लिक करें और आगे बढ़ें");
//            textViewAllQuestions.setText("सभी प्रश्न");
//        }else {
//            textViewDetail.setText(Util.getUsername(context) + "Based on the result of your diagnostic test, you may need to \n in this lesson to proceed" +
//                    "Must have achieved 100% mastery to clear the test of this lesson\n" +
//                    "Click on any of the buttons below for 100% Mastery and proceed");
//            textViewAllQuestions.setText("All questions");
//        }
//

        imageViewBack_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                onBackPressed();
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (!toggleQuestionsList) {
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
//                    layoutParams.leftMargin = 400;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;


                    System.out.println( "======== pos "+pos);

                    try {
                        recyclerViewTrack.smoothScrollToPosition(pos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    linearLayoutTrack.setVisibility(View.GONE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });
        colapseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (!toggleQuestionsList) {
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
//                    layoutParams.leftMargin = 400;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;
                } else {
                    linearLayoutTrack.setVisibility(View.GONE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                onBackPressed();
            }
        });
        textViewToatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (!toggleQuestionsList) {
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
//                    layoutParams.leftMargin = 400;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;
                } else {
                    linearLayoutTrack.setVisibility(View.GONE);
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    linearLayoutTrack.startAnimation(animation);
                    linearLayoutTrack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTrack.clearAnimation();
                        }
                    },400);

                    downBtn.refreshDrawableState();
                    downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                    downBtn.setPadding(10,10,10,10);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
//                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });


        if (Util.getSelectedLanguage(context).equals("english")) {
//            btnChangeLanguage.setText("English");
//            leaveQuestions = "Skip Question";
//            submitAnswer = "Submit Answer";
//            parikshanSubmit = "Submit Test";
            questionText ="Question ";
//            diagnosticText ="Diagnostic Test";
//            skipOnlastQuestion = "Well, that's the last question. Are you sure you want to leave it? Click \"x\" to solve this question or click the button below to proceed (enter test)";
//            previousQuestionText ="Prev. Question";

        } else {
            btnChangeLanguage.setText("Hindi");
//            leaveQuestions = "प्रश्न छोड़ें";
//            submitAnswer = "उत्तर सबमिट करें";
//            parikshanSubmit = "परीक्षण जमा करें";
//            skipOnlastQuestion = ", यह आखिरी सवाल है। क्या आप वाकई इसे छोड़ना चाहते हैं? इस प्रश्न को हल करने के लिए \"x\" पर क्लिक करें या आगे बढ़ने के लिए नीचे दिए बटन पर क्लिक करें (परीक्षण दर्ज करें)";
            questionText ="प्रश्न ";
//            diagnosticText ="डायग्नोस्टिक परीक्षण";
//            previousQuestionText ="पिछला प्रश्न";
        }


        linearLayoutTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                linearLayoutTrack.setVisibility(View.GONE);
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                linearLayoutTrack.startAnimation(animation);
                linearLayoutTrack.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutTrack.clearAnimation();
                    }
                },400);

                downBtn.refreshDrawableState();
                downBtn.setBackgroundResource(R.drawable.ic_menu_que);
                downBtn.setPadding(10,10,10,10);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                layoutParams.leftMargin = 0;
//                    linearActionLayout.setLayoutParams(layoutParams);
                toggleQuestionsList = false;
            }
        });

//        imageViewBack.setText(previousQuestionText);
        imageViewBack.setVisibility(View.GONE);
        textViewTitleName.setText(diagnosticText);


        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                try {

                    if (pos == questionsArrayList.size()) {
                        if (questionsArrayList.get(11).get("IsAttampted").equalsIgnoreCase("TRUE")) submitTestPaper();
                        else showSkipOption();
                    } else if (pos == questionsArrayList.size() - 1) {
                        if (textViewSubmit.getText().equals("प्रश्न छोड़ें") || textViewSubmit.getText().equals(leaveQuestions))
                            showSkipOption();
                        else {
                            imageViewBack.setVisibility(View.VISIBLE);
                            textViewSubmit.setText(submitAnswer);
                            textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                            textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
                            checkSkippedQ(pos, 0);
                            pos++;
                            refreshQuestionsListingAdapter();
                            textViewToatal.setText(questionText+" " + pos + " / " + questionsArrayList.size());
                            if (pos == questionsArrayList.size()) {
//                            textViewSkip.setVisibility(View.GONE);
                                textViewSubmit.setText(parikshanSubmit);
                                textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                                textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
                            }
                        }
                    } else {
                        imageViewBack.setVisibility(View.VISIBLE);
                        checkSkippedQ(pos, 0);
                        pos++;
                        mRecyclerView.smoothScrollToPosition(pos);
                        refreshQuestionsListingAdapter();
                    }
                    
                    
//                    if (pos == questionsArrayList.size()) {
//                        submitTestPaper();
//                        linearLayoutTrack.setVisibility(View.GONE);
//                        clearSkipData();
//                    } else if (pos == questionsArrayList.size() - 1) {
//                        if (textViewSubmit.getText().equals("प्रश्न छोड़ें") ||textViewSubmit.getText().equals(leaveQuestions))
//                        {
//                            showSkipOption();
//                        } else {
//                            imageViewBack.setVisibility(View.VISIBLE);
//                            textViewSubmit.setText(submitAnswer);
//                            textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                            textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
//                            checkSkippedQ(pos, 0);
//                            pos++;
//                            refreshQuestionsListingAdapter();
//                            textViewToatal.setText(questionText+" " + pos + " / " + questionsArrayList.size());
//                            if (pos == questionsArrayList.size()) {
////                            textViewSkip.setVisibility(View.GONE);
//                                textViewSubmit.setText(parikshanSubmit);
//                                textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
//                            }
//                        }
//                    } else {
//                        imageViewBack.setVisibility(View.VISIBLE);
//                        checkSkippedQ(pos, 0);
//                        pos++;
//                        mRecyclerView.smoothScrollToPosition(pos);
//                        refreshQuestionsListingAdapter();
//                        textViewSubmit.setText(leaveQuestions);
//                        textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                        textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
//                        textViewToatal.setText(questionText+" " + pos + " / " + questionsArrayList.size());
//                        if (pos == questionsArrayList.size()) {
////                            textViewSkip.setVisibility(View.VISIBLE);
//                            textViewSubmit.setText(leaveQuestions);
//                            textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                            textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
//                        } else {
//                            textViewSubmit.setText(leaveQuestions);
//                            textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                            textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
////                            textViewSkip.setVisibility(View.GONE);
//                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
//        textViewSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSkipOption();
//            }
//        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                try {
                    if (pos == 0) {
                        imageViewBack.setVisibility(View.GONE);
                    } else {
                        if (pos == questionsArrayList.size()) {
                            if (questionsArrayList.get(pos - 1).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                            }
                            pos--;
                        } else {
                            pos--;
                            mRecyclerView.smoothScrollToPosition(pos);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        lm = new CustomLinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        //lm.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(lm);
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(mRecyclerView);

        selectedLanguge = Util.getSelectedLanguage(context);

        if (global.isFlagEnabled()) {
            btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguage.setBackgroundResource(R.drawable.signup);
            btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
        }
        try {
            setLayoutText();
            setStaticText();
            getTime();
            getBiMonthyTestCount();

        } catch (Exception e) {
            e.printStackTrace();
        }

        GetTopicLevelsDetails.task(context, sClass, topicId, global, board, reportsDiagnosticTestCompleteRepository);

        getResult_text();

        //Show tooltip
        if (Util.isToolTipDiagnosticToBeShown(context)) {
//            showFullScreenTransparentBg();
//            String message = "यहाँ पर आप सभी प्रश्नो को लिस्ट के रूप में देख सकते हैं";
//            String buttonText = "आगे बढ़ें";
//            String skipText = "Skip";
//            Util.showTooltip(context, DiagonosticTestActivity.this, ArrowOrientation.LEFT, "right", message, buttonText,
//                    skipText, "diagnosticTestDownArrow", downBtn);
        }
    }

    private void checkConnection(boolean first) {

        mProgressBar = findViewById(R.id.progressBar);
        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);
        loading_layout = findViewById(R.id.loading_layout);

        if(!Util.isOfflineMode(context)) {

            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);
                            imageViewCrossVideo2.setVisibility(View.VISIBLE);
                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        }

                    } else {
                        hideconnection_layout();
                    }
                }
            }, 10000);//time in milisecond

            imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            loading_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("-----");
                }
            });
        }
        else
        {
            hideconnection_layout();
        }
    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        loading_layout.setVisibility(View.GONE);
    }

    /** Support Btn Functions */
    @SuppressLint("RestrictedApi")
    public void animateFAB() {

        if (isFabOpen) {

            fab.setImageResource(R.drawable.ic_inactive);
            isFabOpen = false;
//            fab.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            screenshotText.startAnimation(fab_close);
//            callText.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);

//            callText.setVisibility(View.GONE);
//            screenshotText.setVisibility(View.GONE);
//            fab1.setVisibility(View.GONE);
//            fab2.setVisibility(View.GONE);

        } else {

            fab.setImageResource(R.drawable.ic_active);
            isFabOpen = true;

            buttonScreenshot(null);

//            fab1.setImageResource(R.drawable.ic_screenshot_inactive);
//            fab2.setImageResource(R.drawable.ic_call_inactive);
//            fab1.performClick();
//            fab1.setVisibility(View.GONE);
//            fab2.setVisibility(View.GONE);
//            fab.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            callText.startAnimation(fab_open);
//            screenshotText.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);

//            callText.setVisibility(View.VISIBLE);
//            screenshotText.setVisibility(View.VISIBLE);

        }
    }

    private void openContactUsDialogue() {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+91"+callNo+"&text=Hey%20there%")));
                Util.preventTwoClick(view);
            }
        });

        EmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","share@idreameducation.org", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from - " +Util.getUsername(context));
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                Util.preventTwoClick(view);
            }
        });

        imageViewCrossDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab2.setImageResource(R.drawable.ic_call_inactive);
                dialog.dismiss();
                Util.preventTwoClick(view);
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

    private void callAtRuntime() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+callNo));
        startActivity(callIntent);
    }

    private void openCapturedScreenshot(File fileScreenshot) {
        Dialog dialog = new Dialog(context);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogue_send_screenshot);

        Button textViewCancel = dialog.findViewById(R.id.textViewCancel);
        Button textViewSend = dialog.findViewById(R.id.textViewSend);
        ImageView imageViewScreenshot = dialog.findViewById(R.id.imageViewScreenshot);
        ImageView close_pop = dialog.findViewById(R.id.close_pop);
//        ImageView attachScreenshot = dialog.findViewById(R.id.attachScreenshot);
        EditText textSendWithImage__ = dialog.findViewById(R.id.textSendWithImage_);
//        attachScreenshot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SelectImage();
//                Util.preventTwoClick(view);
//            }
//        });


        TextView number_text=dialog.findViewById(R.id.number_text);
        TextView email_text=dialog.findViewById(R.id.email_text);

        number_text.setText(button1Text);
        email_text.setText(button3Text);

        Uri uri = Uri.fromFile(fileScreenshot);
        filePath2 = uri;
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

                fab.setImageResource(R.drawable.ic_inactive);
                isFabOpen = false;
            }
        });

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
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

    // Select Image method
    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    // UploadImage method
    private void uploadImage(Dialog dialog) {
        if (filePath2 != null) {

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
            ref.putFile(filePath2)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUri =  uri;
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
                            Util.openGifDialogue(context,"Some error occured !");
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonScreenshot(null);
                    }
                }, 100);


            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab2.setImageResource(R.drawable.ic_call_active);
                openContactUsDialogue();
            }
        });

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (global.isFlagEnabled()) {
                        btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
                        btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
                        btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
                        btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
                        if (Util.getSelectedLanguage(context).equals("Hindi")) {
                            DiagonosticTestAdapter_Mobile.selectedLanguge = "Hindi";
                        } else {
                            DiagonosticTestAdapter_Mobile.selectedLanguge = "English";
                        }
                        diagonosticTestAdapterMobile.updateAdapter();
                    } else {
                        Util.showToast(context, "Coming soon");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnChangeLanguageEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
                    btnChangeLanguage.setBackgroundResource(R.drawable.signup);
                    btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
                    btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
                    DiagonosticTestAdapter_Mobile.selectedLanguge = "English";
                    diagonosticTestAdapterMobile.updateAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /** Getting text from Backend */
    private void get_text_from_backend() {

        FirebaseDatabase.getInstance().getReference("screen_text/student/1")
                .child(Util.getSelectedLanguage(context)).child("DiagonosticTestActivity")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                textArrayList = (ArrayList<String>) dataSnapshot.getValue();

                                update_textLang();
                                hideconnection_layout();

//                        if (textArrayList.size() > 0) {
////                            textViewtitle.setText(textArrayList.get(0));
////                            nameStudent.setHint(textArrayList.get(1));
////                            textViewtitleName.setText(textArrayList.get(2));
////                            rollnoStudent.setHint(textArrayList.get(3));
////                            textViewtitlePassword.setText(textArrayList.get(4));
////                            buttonSignInStudent.setText(textArrayList.get(5));
////                            errorMessage = textArrayList.get(6);
////                            mobilenoStudent.setHint(textArrayList.get(7));
////                            textViewtitlemobileNo.setText(textArrayList.get(8));
////                            textViewtitleRollNo.setText(textArrayList.get(9));
////                            nameError = textArrayList.get(10);
////                            nameDialogueText = textArrayList.get(11);
////                            rollNoDialogueText = textArrayList.get(12);
////                            rollNoError = textArrayList.get(13);
////                            phoneNoerror = textArrayList.get(14);
////                            mobileDialogueText = textArrayList.get(15);
////
//                        }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void update_textLang() {

        imageViewBack=findViewById(R.id.imageViewBack);
        textViewTitleName=findViewById(R.id.textViewTitleName);

        textViewDetail.setText(Util.getUsername(context) + textArrayList.get(0));
        textViewAllQuestions.setText(textArrayList.get(1));

        leaveQuestions =textArrayList.get(22);
        submitAnswer =textArrayList.get(23);
        parikshanSubmit =textArrayList.get(24);


        leaveQuestions = textArrayList.get(22);
        submitAnswer = textArrayList.get(23);
        parikshanSubmit = textArrayList.get(24);
        skipOnlastQuestion = textArrayList.get(25);
        questionText = textArrayList.get(26);
        diagnosticText = textArrayList.get(27);
        previousQuestionText = textArrayList.get(28);

//        imageViewBack.setText(textArrayList.get(28));

        attempted_text=textArrayList.get(31);
        skipped_text=textArrayList.get(32);
        yet_to_attempt_text=textArrayList.get(33);

        textViewdUserName.setText(Util.getUsername(context));
//        imageViewBack.setText(previousQuestionText);
        imageViewBack.setVisibility(View.GONE);
        textViewTitleName.setText(diagnosticText);

    }

    /** get questions from firebase */
    private void getQuestions(final String topicId) throws Exception {
        final ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList = new ArrayList<>();

//        Toast.makeText(context, "Language :- "+ Util.getSelectedLanguage(context), Toast.LENGTH_SHORT).show();

        if (Util.getSelectedLanguage(context).equals("english")) {
            filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "questions.txt";
        } else {
            filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "Hindi_questions.txt";
        }

        File file = new File(Util.getSDCardPath(context) + "/" + filePath);
        if (file.exists()) {

            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONArray array = jsonObject.getJSONArray(topicId);

                HashMap<String, HashMap<String, String>> innerHashMap = null;
                HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap_ = null;

                HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;

                HashMap<String, String> finalMap = new HashMap<>();
                int length_ = array.length();
                for (int i = 0; i < array.length(); i++) {
                    coreOuterHashMap = new HashMap<>();
                    outerHashMap = new HashMap<>();
                    outerHashMap_ = new HashMap<>();
                    JSONArray jsonArray = array.getJSONArray(i);
                    int length = jsonArray.length();
                    ArrayList<Integer> trackArrayList = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {

                        int max = length;

                        int number = generateRandom(trackArrayList, max);
                        trackArrayList.add(number);
                        // HashMap<String, HashMap<String, String>> aa = outerHashMap.get("" + number);

                        innerHashMap = new HashMap<>();
                        finalMap = new HashMap<>();
                        JSONObject aa = jsonArray.getJSONObject(number);
                        Iterator<String> iterator = aa.keys();
                        String key = "";
                        while (iterator.hasNext()) {
                            key = iterator.next();
                        }
                        JSONObject jsonObject1 = aa.getJSONObject(key);
                        String q = jsonObject1.getString("q");

                        String questionImage = "";
                        if (jsonObject1.has("questionImage")) {
                            questionImage = jsonObject1.getString("questionImage");
                        }

                        String feedbackImage = null;
                        String correct_feedback = null;
                        String incorrect_feedback = null;
                        if (jsonObject1.has("feedbackImage")) {
                            feedbackImage = jsonObject1.getString("feedbackImage");
                            finalMap.put("feedbackImage", feedbackImage);
                        }
                        if (jsonObject1.has("correct_feedback")) {
                            correct_feedback = jsonObject1.getString("correct_feedback");
                            finalMap.put("correct_feedback", correct_feedback);
                        }
                        if (jsonObject1.has("incorrect_feedback")) {
                            incorrect_feedback = jsonObject1.getString("incorrect_feedback");

                            finalMap.put("incorrect_feedback", incorrect_feedback);
                        }
                        JSONObject aObject = jsonObject1.getJSONObject("A");
                        JSONObject bObject = jsonObject1.getJSONObject("B");
                        JSONObject cObject = jsonObject1.getJSONObject("C");
                        JSONObject dObject = jsonObject1.getJSONObject("D");
                        String aValue = aObject.getString("value");
                        String aType = aObject.getString("type");
                        String bValue = null;
                        try {
                            bValue = bObject.getString("value");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String bType = bObject.getString("type");
                        String cValue = cObject.getString("value");
                        String cType = cObject.getString("type");
                        String dValue = dObject.getString("value");
                        String dType = dObject.getString("type");

                        finalMap.put("q", q);

                        finalMap.put("questionImage", questionImage);
                        HashMap<String, String> aHAshMap = new HashMap<>();
                        HashMap<String, String> bHAshMap = new HashMap<>();
                        HashMap<String, String> cHAshMap = new HashMap<>();
                        HashMap<String, String> dHAshMap = new HashMap<>();

                        aHAshMap.put("value", aValue);
                        aHAshMap.put("type", aType);

                        bHAshMap.put("value", bValue);
                        bHAshMap.put("type", bType);

                        cHAshMap.put("value", cValue);
                        cHAshMap.put("type", cType);

                        dHAshMap.put("value", dValue);
                        dHAshMap.put("type", dType);

                        finalMap.put("A", aHAshMap.toString());
                        finalMap.put("B", bHAshMap.toString());
                        finalMap.put("C", cHAshMap.toString());
                        finalMap.put("D", dHAshMap.toString());

                        innerHashMap.put(key, finalMap);
                        outerHashMap.put(j + "", innerHashMap);

                    }
                    coreOuterHashMap.put(i + "", outerHashMap);
                    contentArrayList.add(coreOuterHashMap);


                }
                Log.i("data", contentArrayList.toString());
                try {
                    if (Util.getSelectedLanguage(context).equals("english")) {
                        dataManipulation(contentArrayList, "English");
                    } else {
                        dataManipulation(contentArrayList, "Hindi");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                String board = null;

                if (Util.getSelectedLanguage(context).equals("hindi")) {
                    board = ApplicationConstants.QUESTION_DB_HINDI;
                } else {
                    board = "QuDB_Classwise_english";
                }

                global.getDatabaseReference().child("QuDB").child(Util.getSelectedBoard(context)).child(board).child(sClass).child(topicId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            HashMap<String, HashMap<String, String>> innerHashMap = null;
                            HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                            HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap_ = null;
                            HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;
                            for (DataSnapshot single : dataSnapshot.getChildren()) {
                                coreOuterHashMap = new HashMap<>();
                                outerHashMap_ = new HashMap<>();
                                outerHashMap = new HashMap<>();
                                for (DataSnapshot innerData : single.getChildren()) {
                                    for (DataSnapshot innerCoreData : innerData.getChildren()) {
                                        innerHashMap = new HashMap<>();
                                        innerHashMap.put(innerCoreData.getKey(), (HashMap<String, String>) innerCoreData.getValue());
                                        outerHashMap.put(innerData.getKey(), innerHashMap);
                                    }
                                }
                                ArrayList<Integer> trackArrayList = new ArrayList<>();
                                // getting three random questions from each level
                                for (int i = 0; i < 3; i++) {
                                    int max = outerHashMap.size();
                                    int number = generateRandom(trackArrayList, max);
                                    trackArrayList.add(number);
                                    HashMap<String, HashMap<String, String>> aa = outerHashMap.get("" + number);
                                    outerHashMap_.put(i + "", aa);
                                    Log.i("QuestionArrayList : ", aa.toString());
                                    coreOuterHashMap.put(single.getKey(), outerHashMap_);
                                }
                                contentArrayList.add(coreOuterHashMap);

                            }
                            Util.dismissDialog();
                            if (Util.getSelectedLanguage(context).equals("Hindi")) {
                                dataManipulation(contentArrayList, "Hindi");
                            } else {
                                dataManipulation(contentArrayList, "English");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private int generateRandom(ArrayList<Integer> trackArrayList, int max) {
        Random r = new Random();
        int number = r.nextInt(max - 1) + 1;
        if (trackArrayList.contains(number)) {
            number = generateRandom(trackArrayList, max);
        }
        return number;
    }

    private void showFullScreenTransparentBg() {
        if (transparentBg != null) {
            transparentBg.setVisibility(View.VISIBLE);
        }
    }

    public void hideFullScreenTransparentBg() {
        if (transparentBg != null) {
            transparentBg.setVisibility(View.GONE);
            Util.setToolTipDiagnosticScreen(context, false);
        }
    }

    public void showToolTipForSkipButton() {
        if (textViewSubmit != null) {
            String message,buttonText,skipText;

            message = textArrayList.get(2);
            buttonText = textArrayList.get(3);
            skipText = textArrayList.get(4);

//
//            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//            {
//                message = "इस बटन से आप इस प्रश्न को अभी के लिए छोड़ सकते हैं और बाद में आकर दोबारा प्रयास कर सकते हैं";
//                buttonText = "आगे बढ़ें";
//                skipText = "स्किप";
//            }else {
//                message = "With this button you can leave this question for now and try again later";
//                buttonText = "Go ahead";
//                skipText = "Skip";
//            }

            Util.showTooltip(context, DiagonosticTestActivity_Mobile.this, ArrowOrientation.BOTTOM, "top", message, buttonText,
                    skipText, "diagnosticTestSkipButton", textViewSubmit);
        }
    }

    public void showToolTipForBackButton() {
        if (textViewSubmit != null) {
            String message,buttonText,skipText;


            message = textArrayList.get(5);
            buttonText = textArrayList.get(3);
            skipText = textArrayList.get(4);

//            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//            {
//                message = "इस बटन से आप पिछले प्रश्न पर जा सकते हैं";
//                buttonText = "आगे बढ़ें";
//                skipText = "स्किप";
//            }else {
//                message = "With this button you can go to the previous question";
//                buttonText = "Go ahead";
//                skipText = "Skip";
//            }

            Util.showTooltip(context, DiagonosticTestActivity_Mobile.this, ArrowOrientation.BOTTOM, "top", message, buttonText,
                    skipText, "diagnosticTestBackButton", imageViewBack);
        }
    }

    public void moveToNextQuestion(int position) {
        if (mRecyclerView != null) {
            pos = position;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(position);
                }
            }, 1000);
        }
    }

    public void updateNextQuestionButtonText(int type, int position) {
        if (type == 0) {
            if (textViewSubmit != null) {
                if (textViewSubmit.getText().equals("परीक्षण जमा करें") ||textViewSubmit.getText().equals(parikshanSubmit)) {
                    pos = pos - 1;
                }
                textViewSubmit.setText(submitAnswer);
                textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
            }
        } else if (type == 1) {
            if (textViewSubmit != null) {
                if (position == questionsArrayList.size() - 1) {
                    textViewSubmit.setText(leaveQuestions);
                    textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                    textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                } else {
                    textViewSubmit.setText(leaveQuestions);
                    textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                    textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                }
            }
        }
    }

    /** Dialogs */
    private void showSkipOption() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.skip_question_dialog_layout);
        dialog.setCancelable(false);
        ImageView imageViewCross = dialog.findViewById(R.id.imageViewCross);
        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                dialog.dismiss();
            }
        });
        TextView textView = dialog.findViewById(R.id.textView);
        TextView submit_bt = dialog.findViewById(R.id.submit_bt);
        submit_bt.setText(parikshanSubmit);
        textView.setText(Util.getUsername(context) + skipOnlastQuestion);
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
                submitTestPaper();
                linearLayoutTrack.setVisibility(View.GONE);
                clearSkipData();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
    }

    private void submitConfirmationCheck() {
        isNeededToOpenBackDialog = false;
        try {
            countDownTimerWithPause.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_submit_confirmation);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(sureSubmit);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(String.format(unattemptedQLeft, questionsArrayList.size() - Integer.parseInt(this.attemptedQuestions)));
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        textViewContinoue.setText(submit);
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                try {
                    submitTestPaper();
                    //openDialog();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        textViewCancel.setText(completeTest);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isNeededToOpenBackDialog = true;
            }
        });
    }

    private void showExitDialog() {

        String texViewMsgText,countinueText,exitTextButton;
        if(Util.isOfflineMode(context))
        {

        }
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {

            texViewMsgText = "आपकी प्रतिक्रिया सबमिट किए बिना, स्कोर कैप्चर नहीं किया जाएगा। आप या तो बाहर निकल सकते हैं, या कुछ प्रश्नों का प्रयास करना जारी रख सकते हैं और अपना परीक्षण सबमिट कर सकते हैं";
            countinueText = "जारी रखें";
            exitTextButton = "टेस्ट से बाहर निकलें";
        }else {

            texViewMsgText = warning + "\n" + exitMessage;
            countinueText = "Continue";
            exitTextButton = "Exit Test";
        }


        SelectTopicActivity.currentTestScoreChanged = "0";
        isNeededToOpenBackDialog = false;
        try {
            if (countDownTimerWithPause != null) {
                countDownTimerWithPause.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_exit_model);
        dialog.setCancelable(false);
        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
        TextView textView = dialog.findViewById(R.id.textView);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        TxtExit.setText(exitTextButton);
        textView.setText(texViewMsgText);
        textViewContinoue.setText(countinueText);

        ImageView crossImage=dialog.findViewById(R.id.crossImage);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    countDownTimerWithPause.resume();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                dialog.dismiss();
            }
        });
        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PalContentListingActivity_Mobile.backPressed = true;
                finish();
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

    private void openDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.dismissScoreDialog();

//                PalContentListingActivity.instance.setPath(board, sClass, Util.getSubject(context), topicId, "D");

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).child("DTest").setValue("D");

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).setValue(true);

                if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId,Util.getSelectedLanguage(context))) {
                    reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, true,Util.getSelectedLanguage(context));
                } else {
                    ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                    reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                    reportsDiagnosticCompleteModel.setBoard(board);
                    reportsDiagnosticCompleteModel.setSClass(sClass);
                    reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                    reportsDiagnosticCompleteModel.setTopicId(topicId);
                    reportsDiagnosticCompleteModel.setComplete(true);
                    reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                    reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
                }




                int percentage = (diagonosticTestAdapterMobile.scoreArrayList.size() * 100) / questionsArrayList.size();
                testPercentageAchieved = percentage;
                showResult(percentage);



//                else if(percentage == 100){
//                    openSuccessDialog();
//                }
            }
        }, 3000);
    }

    private void openMiddleDialog(int level) {


        String textViewPracticeText,textViewDiagnosticMessageText,textLayout1Text,textLayout2Text,subTextLayout1Text,
                subTextLayout2Text,pointsText1Text,pointsText2Text,pointsText3Text,textViewScoreDetailText
                ,testPercentagebeloweighty,testPercentageaboveeighty,videoLevelText,orTextText,testScoreTextText;

        if(textArrayList==null)
        {
            get_text_from_backend();
        }


        try {
            textViewPracticeText = textArrayList.get(7);
            textViewDiagnosticMessageText = textArrayList.get(8);
            textLayout1Text = textArrayList.get(9);
            textLayout2Text = textArrayList.get(10);
            subTextLayout1Text = textArrayList.get(11);
            subTextLayout2Text = textArrayList.get(12);
            pointsText1Text = textArrayList.get(13);
            pointsText2Text = textArrayList.get(14);
            pointsText3Text = textArrayList.get(15);
            textViewScoreDetailText = textArrayList.get(16);
            testPercentagebeloweighty = textArrayList.get(17);
            testPercentageaboveeighty = textArrayList.get(18);
            videoLevelText = textArrayList.get(19);
            orTextText = textArrayList.get(20);
            testScoreTextText = textArrayList.get(21);
        }
        catch (Exception r)
        {
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
            {
                textViewPracticeText ="अभ्यास करें";
                textViewDiagnosticMessageText =" के डायग्नोस्टिक परिक्षण में आपके अंक हैं";
                textLayout1Text ="100% महारत हासिल करें";
                textLayout2Text ="फाइनल टेस्ट में 100% स्कोर";
                subTextLayout1Text ="अनलॉक हो गया है";
                subTextLayout2Text ="बंद है";
                pointsText1Text ="इस अध्याय को पूरा करने के लिए आपको दो कार्य करने होंगे :";
                pointsText2Text ="1. अभ्यास में पूर्ण महारत";
                pointsText3Text ="2. अंतिम परिक्षण में 100% अंक को प्राप्त करना";
                textViewScoreDetailText =", आपका डायग्नोस्टिक परीक्षण स्कोर है";
                testPercentagebeloweighty ="अपने सफर की शुरुआत करने के लिए या तो आप वीडियो पाठ देखकर अपनी समझ को और बेहतर कर सकते हैं या फिर अगर आप तैयार हैं तो सीधे अभ्यास पर भी जा सकते हैं";
                testPercentageaboveeighty ="आगे बढ़ने के लिए अपना अभ्यास शुरू करें या उपचारात्मक वीडियो देखें";
                videoLevelText ="वीडियो पाठ - स्तर ";
                orTextText ="या";
                testScoreTextText =" अंक हैं";
            }else {
                textViewPracticeText ="Practice";
                textViewDiagnosticMessageText =", In the diagnostic test of ";
                textLayout1Text ="Achieve 100% mastery";
                textLayout2Text ="100% score in the final test";
                subTextLayout1Text ="Is unlocked";
                subTextLayout2Text ="Is closed";
                pointsText1Text ="To complete this chapter, you need to achieve two goals:";
                pointsText2Text ="1. Achieve 100% mastery in the Practice assessment";
                pointsText3Text ="2. Take the final test and and score 80% or more";
                textViewScoreDetailText =", your score is";
                testPercentagebeloweighty ="To begin your journey, you can either watch video lessons to build more understanding or straight away start attempting questions in the Practice. All the best.";
                testPercentageaboveeighty ="Start your own practice or watch remedial videos to get going";
                videoLevelText ="Video Lessons - Levels ";
                orTextText ="Or";
                testScoreTextText = " Marks";
            }

        }


        reletiveLayoutLowerLevel.setVisibility(View.VISIBLE);
        reletiveTop.setVisibility(View.GONE);
        TextView textViewPractice = findViewById(R.id.buttonPractice);
        textViewPractice.setText(textViewPracticeText);
        TextView orText = findViewById(R.id.orText);
        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
        TextView testScoreText = findViewById(R.id.testScoreText);
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            textViewdMessaeg.setText(Html.fromHtml(Util.getUsername(context) + " " + topicName + textViewDiagnosticMessageText));
            testScoreText.setVisibility(View.VISIBLE);
        }else {
            textViewdMessaeg.setText(Html.fromHtml(Util.getUsername(context) + " in the diagnostic test of " + topicName + " your score is "));
            testScoreText.setVisibility(View.GONE);
        }
        textViewdMessaeg.setTypeface(null, Typeface.BOLD);
        textViewDetail.setVisibility(View.GONE);
        TextView textLayout1 = findViewById(R.id.textLayout1);

        textLayout1.setText(textLayout1Text);
        TextView textLayout2 = findViewById(R.id.textLayout2);
        textLayout2.setText(textLayout2Text);
        TextView subTextLayout1 = findViewById(R.id.subTextLayout1);
        subTextLayout1.setText(subTextLayout1Text);
        TextView subTextLayout2 = findViewById(R.id.subTextLayout2);
        subTextLayout2.setText(subTextLayout2Text);
        TextView testScoreLowerLevel = findViewById(R.id.testScoreLowerLevel);
        testScoreLowerLevel.setText(testPercentageAchieved + "%");
        testScoreLowerLevel.setTypeface(null, Typeface.BOLD);
        if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            testScoreText.setVisibility(View.VISIBLE);
        }else {
            testScoreText.setVisibility(View.GONE);
        }

        testScoreText.setText(testScoreTextText);
        TextView pointsText1 = findViewById(R.id.pointsText1);
        pointsText1.setText(pointsText1Text);
        TextView pointsText2 = findViewById(R.id.pointsText2);
        pointsText2.setText(pointsText2Text);
        TextView pointsText3 = findViewById(R.id.pointsText3);
        pointsText3.setText(pointsText3Text);
        TextView textViewScoreDetail = findViewById(R.id.textViewScoreDetail);
        textViewScoreDetail.setText(Util.getUsername(context) + textViewScoreDetailText);
        orText.setText(orTextText);
//        info.setText("१००% मास्टरी के लिए नीचे दिए गए किसी भी बटन पे क्लिक करें और आगे बढ़ें");
        if (testPercentageAchieved > 80) {
            info.setText(testPercentagebeloweighty);
        } else {
            info.setText(testPercentageaboveeighty);
        }
        if (GetTopicLevelsDetails.isSameTopicAvailable()) {
            textViewPractice.setVisibility(View.VISIBLE);
            orText.setVisibility(View.VISIBLE);
        } else {
            textViewPractice.setVisibility(View.GONE);
            orText.setVisibility(View.GONE);
        }
        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                getSameLevelPractice();
            }
        });
        TextView textViewContinoue = findViewById(R.id.buttonVideo);
        orText.setVisibility(View.VISIBLE);
        textViewContinoue.setVisibility(View.VISIBLE);

        textViewContinoue.setText(videoLevelText + level + "");
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (Util.isNetworkAvailable(context))
                {
                    String subject = Util.getSubject(context);
                    String subjectName = Util.getSubjectName(context);
                    String icon = PalContentListingActivity_Mobile.icon;
                    Intent intent = new Intent(context, PalContentListingActivity_Mobile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Util.setVideoLevel(context, (level - 1));
                    Util.setLevel(context, (level));
                    PracticeTopicActivity.autoplayLevelVideo=true;
                    intent.putExtra("subject", subject);
                    intent.putExtra("sClass", sClass);
                    intent.putExtra("board", board);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("icon", icon);
                    intent.putExtra("getPath", "yes");
                    Util.setTopicID(context, topicId);
                    startActivity(intent);
                    
                    finish();
                } else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }
        });
    }



//    private boolean checkcontain(ArrayList<Integer> trackArrayList,int max,int number)
//    {
//        if(trackArrayList.contains(number))
//        {
//            generateRandom(trackArrayList,max);
//
//        }
//        else
//        {
//            return true;
//        }
//    }

//    private int generateRandom(ArrayList<Integer> trackArrayList, int max) {
//
//        Random r = new Random();
//        int number=r.nextIntr.nextInt(max - 1) + 1;
//
//        if(trackArrayList.contains(number))
//        {
//            checkcontain(trackArrayList,max,number
//            );
//        }
//
//
////        try
////        {
////            number = r.nextInt(max - 1) + 1;
////        }
////        catch (Exception rw)
////        {
////            rw.printStackTrace();
////            number = generateRandom(trackArrayList, max);
//////            number = max-1;
////        }
//
//        if (trackArrayList.contains(number)) {
//
//            try
//            {
//                new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    int number2;
//
//                    number= r.nextInt(max - 1) + 1;
////                    number=number2;
//
//
//
////                    try {
////
////                    }catch (Exception d)
////                    {
////                        try {
////                            number=max-1;
////                        }
////                        catch (Exception dd)
////                        {
////                            number=max;
////                        }
////                    }
//
////                    number = generateRandom(trackArrayList, max);
//                }
//            }, 2000 );//time in milisecond
//
//            }
//            catch (Exception ree)
//            {
//                ree.printStackTrace();
//            }
//
//
//        }
//        return number;
//    }

    /** Data manipulation of questions and answer */
    private void dataManipulation(ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList, String language) throws Exception {
        try {
            Util.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Save time for next question */
        Util.saveTime();
        /* Variables which we will be used to manipluate the data */
        String keyToGet = null;
        String keyToGetQuestion = null;
        /*Total number of level*/
        questionsArrayList = new ArrayList<>();
        for (int p = 0; p < contentArrayList.size(); p++) {
            HashMap<String, String> finalHashmap = new HashMap<>();

            HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> stringHashMapHashMap = contentArrayList.get(p);
            /*getting the key of the Hashmap*/
            for (String key : stringHashMapHashMap.keySet()) {
                keyToGet = key;
            }
            HashMap<String, HashMap<String, HashMap<String, String>>> stringHashMapHashMap1 = stringHashMapHashMap.get(keyToGet);
            /* Add data in the array list only once in a single level to avoid the repeatition */

            questionArrayList = new ArrayList<>();
            for (int i = 0; i < stringHashMapHashMap1.size(); i++) {
                HashMap<String, HashMap<String, String>> hashMapHashMap = stringHashMapHashMap1.get("" + i);
                questionArrayList.add(hashMapHashMap);
            }
            try {
                /*Question that will be displyed to the user*/
                HashMap<String, HashMap<String, String>> map = null;
                for (int k = 0; k < questionArrayList.size(); k++) {
                    map = questionArrayList.get(k);
                    finalHashmap = new HashMap<>();
                    /*Getting the key of the innerHashmap*/
                    for (String key : map.keySet()) {
                        keyToGetQuestion = key;
                        questionID = key;
                    }
                    /*Final question Hashmap*/
                    HashMap<String, String> mapToQuestion = map.get(keyToGetQuestion);

                    /* Collecting answers from the map  */
                    Object optionA = mapToQuestion.get("A");
                    Object optionB = mapToQuestion.get("B");
                    Object optionC = mapToQuestion.get("C");
                    Object optionD = mapToQuestion.get("D");
                    /* Adding the options objects in to Arraylist for further processing */
                    ArrayList<Object> optionsArrayList = new ArrayList<>();
                    optionsArrayList.add(optionA);
                    optionsArrayList.add(optionB);
                    optionsArrayList.add(optionC);
                    optionsArrayList.add(optionD);

                    /* Getting questioins in english language and in alternate language */
                    String questionQ = mapToQuestion.get("q");
                    String questionQAlt = mapToQuestion.get("q_alt");
                    /* Getting Correct Feedbck in english and alternate language */
                    String correct_feedback_alt = mapToQuestion.get("correct_feedback_alt");
                    String correct_feedback = mapToQuestion.get("correct_feedback");
                    /* Getting Incorrect Feedbck in english and alternate language */
                    String incorrect_feedback = mapToQuestion.get("incorrect_feedback");
                    String incorrect_feedback_alt = mapToQuestion.get("incorrect_feedback_alt");
                    String feedbackImage = null;
                    try {
                        feedbackImage = mapToQuestion.get("feedback_image");
                    } catch (Exception e) {
                        e.printStackTrace();
                        feedbackImage = null;
                    }

                    /* Set question image if exist */
                    String questionIcon = null;
                    try {
                        questionIcon = mapToQuestion.get("questionImage");
                    } catch (Exception e) {
                        e.printStackTrace();
                        questionIcon = "Not";
                    }
                    finalHashmap.put("feedback_image", feedbackImage);
                    finalHashmap.put("questionID", questionID);
                    finalHashmap.put("correct_feedback_alt", correct_feedback_alt);
                    finalHashmap.put("image", questionIcon);
                    finalHashmap.put("correct_feedback", correct_feedback);
                    finalHashmap.put("incorrect_feedback", incorrect_feedback);
                    finalHashmap.put("incorrect_feedback_alt", incorrect_feedback_alt);
                    finalHashmap.put("questionQAlt", questionQAlt);
                    finalHashmap.put("questionQ", questionQ);
                    finalHashmap.put("OptionSelected", "NOT");
                    finalHashmap.put("Status", "PENDING");
                    finalHashmap.put("IsAttampted", "FALSE");
                    /* Getting answers hashmaps from the arraylist */

                    HashMap<String, String> mapAnswer1 = new HashMap<>();
                    HashMap<String, String> mapAnswer2 = new HashMap<>();
                    HashMap<String, String> mapAnswer3 = new HashMap<>();
                    HashMap<String, String> mapAnswer4 = new HashMap<>();

                    int l = 1;
                    if (Util.isOfflineMode(context)) {
                        String a = (String) optionsArrayList.get(0);
                        String b = (String) optionsArrayList.get(1);
                        String c = (String) optionsArrayList.get(2);
                        String d = (String) optionsArrayList.get(3);


                        a = a.substring(1, a.length() - 1);
                        String[] keyValuePairs = new String[2];
                        String[] keyValuePairs1 = a.split(", ", 2);
                        if (keyValuePairs1.length > 2) {
                            keyValuePairs[0] = keyValuePairs1[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs1.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs1[i];
                                } else {
                                    keyy = keyValuePairs1[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs[1] = keyValuePairs1[1] + ", " + keyy;
                        } else {
                            keyValuePairs[0] = keyValuePairs1[0];
                            keyValuePairs[1] = keyValuePairs1[1];
                        }
                        Log.i("keyValuePairs", keyValuePairs.toString());

//        for (String pair : keyValuePairs) {
//            String[] entry = pair.split("=");
//
//            mapAnswer1.put(entry[0].trim(), entry[1].trim());
//        }

                        for (String pair : keyValuePairs) {
                            String[] entry;
                            if (pair.equals("value==")) {
                                entry = pair.split("=", 2);
                            } else {
                                entry = pair.split("=");
                            }
//            String[] entry = pair.split("=");
                            String key = entry[0].trim();
                            int length = entry.length;
                            String val = "";

                            if (length > 2) {
                                for (int i = 1; i < length; i++) {
//                            val = val + " = " + entry[i];
                                    if (i == 1) {
                                        val = entry[i];

                                    } else {
                                        val = val + " = " + entry[i];
                                    }

                                }

                            } else {
                                val = entry[1];
                            }


                            mapAnswer1.put(key, val);
                        }


                        b = b.substring(1, b.length() - 1);
                        String[] keyValuePairs_ = new String[2];
                        String[] keyValuePairs2 = b.split(", ", 2);
                        if (keyValuePairs2.length > 2) {
                            keyValuePairs_[0] = keyValuePairs2[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs2.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs2[i];
                                } else {
                                    keyy = keyValuePairs2[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs_[1] = keyValuePairs2[1] + ", " + keyy;
                        } else {
                            keyValuePairs_[0] = keyValuePairs2[0];
                            keyValuePairs_[1] = keyValuePairs2[1];
                        }

                        for (String pair : keyValuePairs_) {
                            String[] entry;
                            if (pair.equals("value==")) {
                                entry = pair.split("=", 2);
                            } else {
                                entry = pair.split("=");
                            }
//            String[] entry = pair.split("=");
//                    mapAnswer2.put(entry[0].trim(), entry[1].trim());
                            String key = entry[0].trim();
                            int length = entry.length;
                            String val = "";

                            if (length > 2) {
                                for (int i = 1; i < length; i++) {
//                            val = val + " = " + entry[i];
                                    if (i == 1) {
                                        val = entry[i];

                                    } else {
                                        val = val + " = " + entry[i];
                                    }

                                }
                            } else {
                                val = entry[1];
                            }
//
//                    for (int i = 1; i < length; i++) {
//                        val = val + " = " + entry[i];
//                    }

                            mapAnswer2.put(key, val);
                        }

                        c = c.substring(1, c.length() - 1);

                        String[] keyValuePairs__ = new String[2];
                        String[] keyValuePairs3 = c.split(", ", 2);
                        if (keyValuePairs3.length > 2) {
                            keyValuePairs__[0] = keyValuePairs3[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs3.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs3[i];
                                } else {
                                    keyy = keyValuePairs3[i] + ", " + keyy;
                                }
                            }

                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs__[1] = keyValuePairs3[1] + ", " + keyy;
                        } else {
                            keyValuePairs__[0] = keyValuePairs3[0];
                            keyValuePairs__[1] = keyValuePairs3[1];
                        }


                        for (String pair : keyValuePairs__) {
                            String[] entry;
                            if (pair.equals("value==")) {
                                entry = pair.split("=", 2);
                            } else {
                                entry = pair.split("=");
                            }
//            String[] entry = pair.split("=");
//                    mapAnswer3.put(entry[0].trim(), entry[1].trim());
                            String key = entry[0].trim();
                            int length = entry.length;
                            String val = "";
                            if (length > 2) {
                                for (int i = 1; i < length; i++) {
//                            val = val + " = " + entry[i];
                                    if (i == 1) {
                                        val = entry[i];

                                    } else {
                                        val = val + " = " + entry[i];
                                    }

                                }
                            } else {
                                val = entry[1];
                            }
//                    for (int i = 1; i < length; i++) {
//                        val = val + " = " + entry[i];
//                    }

                            mapAnswer3.put(key, val);
                        }


                        d = d.substring(1, d.length() - 1);
                        String[] keyValuePairs___ = new String[2];

                        String[] keyValuePairs4 = d.split(", ", 2);
                        if (keyValuePairs4.length > 2) {
                            keyValuePairs___[0] = keyValuePairs4[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs4.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs4[i];
                                } else {
                                    keyy = keyValuePairs4[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs___[1] = keyValuePairs4[1] + ", " + keyy;
                        } else {
                            keyValuePairs___[0] = keyValuePairs4[0];
                            keyValuePairs___[1] = keyValuePairs4[1];
                        }


                        for (String pair : keyValuePairs___) {
                            String[] entry;
                            if (pair.equals("value==")) {
                                entry = pair.split("=", 2);
                            } else {
                                entry = pair.split("=");
                            }
//            String[] entry = pair.split("=");
//                    mapAnswer4.put(entry[0].trim(), entry[1].trim());
                            String key = entry[0].trim();
                            int length = entry.length;
                            String val = "";
                            if (length > 2) {
                                for (int i = 1; i < length; i++) {
                                    if (i == 1) {
                                        val = entry[i];

                                    } else {
                                        val = val + " = " + entry[i];
                                    }

                                }
                            } else {
                                val = entry[1];
                            }

//                    for (int i = 1; i < length; i++) {
//                        val = val + " = " + entry[i];
//                    }

                            mapAnswer4.put(key, val);
                        }


                    } else {
                        mapAnswer1 = (HashMap<String, String>) optionsArrayList.get(0);
                        mapAnswer2 = (HashMap<String, String>) optionsArrayList.get(1);
                        mapAnswer3 = (HashMap<String, String>) optionsArrayList.get(2);
                        mapAnswer4 = (HashMap<String, String>) optionsArrayList.get(3);

                    }


                    String option1Type = mapAnswer1.get("type").trim();
                    String option2Type = mapAnswer2.get("type").trim();
                    String option3Type = mapAnswer3.get("type").trim();
                    String option4Type = mapAnswer4.get("type").trim();

                    if (TextUtils.isEmpty(option1Type)) {
                        option1Type = "Text";
                    }
                    if (TextUtils.isEmpty(option2Type)) {
                        option2Type = "Text";
                    }
                    if (TextUtils.isEmpty(option3Type)) {
                        option3Type = "Text";
                    }
                    if (TextUtils.isEmpty(option4Type)) {
                        option4Type = "Text";
                    }
                    if (option1Type.equalsIgnoreCase("पाठ")) {
                        option1Type = "Text";
                    }
                    if (option2Type.equalsIgnoreCase("पाठ")) {
                        option2Type = "Text";
                    }
                    if (option3Type.equalsIgnoreCase("पाठ")) {
                        option3Type = "Text";
                    }
                    if (option4Type.equalsIgnoreCase("पाठ")) {
                        option4Type = "Text";
                    }


                    if (option1Type.equalsIgnoreCase("छवि") || option1Type.equalsIgnoreCase("image")) {
                        option1Type = "Image";
                    }
                    if (option2Type.equalsIgnoreCase("छवि") || option2Type.equalsIgnoreCase("image")) {
                        option2Type = "Image";
                    }
                    if (option3Type.equalsIgnoreCase("छवि") || option3Type.equalsIgnoreCase("image")) {
                        option3Type = "Image";
                    }
                    if (option4Type.equalsIgnoreCase("छवि") || option4Type.equalsIgnoreCase("image")) {
                        option4Type = "Image";
                    }

                    /* getting option values in the english language from the above stated hashmaps */
                    String option1 = mapAnswer1.get("value");
                    String option2 = mapAnswer2.get("value");
                    String option3 = mapAnswer3.get("value");
                    String option4 = mapAnswer4.get("value");

                    finalHashmap.put("type1", option1Type);
                    finalHashmap.put("type2", option2Type);
                    finalHashmap.put("type3", option3Type);
                    finalHashmap.put("type4", option4Type);
                    Random r = new Random();
                    int corectAnswer ;//= r.nextInt(5 - 1) + 1;
//                    int corectAnswer = r.nextInt(5 - 1) + 1;

                    if(Util.isTestingAPP(context)) corectAnswer = 1;
                    else corectAnswer = r.nextInt(5 - 1) + 1;

                    finalHashmap.put("correctOption", "Option" + corectAnswer);

                    if (TextUtils.isEmpty(option1)) {
                        option1 = "-";
                    }
                    if (TextUtils.isEmpty(option2)) {
                        option2 = "-";
                    }
                    if (TextUtils.isEmpty(option3)) {
                        option3 = "-";
                    }
                    if (TextUtils.isEmpty(option4)) {
                        option4 = "-";
                    }

                    /* Implement swich statement to Update the UI with randomise answers */
                    switch (corectAnswer) {
                        case 1:
                            finalHashmap.put("option1", option1);
                            finalHashmap.put("option2", option2);
                            finalHashmap.put("option3", option3);
                            finalHashmap.put("option4", option4);


                            finalHashmap.put("type1", option1Type);
                            finalHashmap.put("type2", option2Type);
                            finalHashmap.put("type3", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 2:

                            finalHashmap.put("option2", option1);
                            finalHashmap.put("option1", option2);
                            finalHashmap.put("option3", option3);
                            finalHashmap.put("option4", option4);

                            finalHashmap.put("type2", option1Type);
                            finalHashmap.put("type1", option2Type);
                            finalHashmap.put("type3", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 3:
                            finalHashmap.put("option3", option1);
                            finalHashmap.put("option1", option2);
                            finalHashmap.put("option2", option3);
                            finalHashmap.put("option4", option4);

                            finalHashmap.put("type3", option1Type);
                            finalHashmap.put("type1", option2Type);
                            finalHashmap.put("type2", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 4:

                            finalHashmap.put("option4", option1);
                            finalHashmap.put("option3", option2);
                            finalHashmap.put("option2", option3);
                            finalHashmap.put("option1", option4);


                            finalHashmap.put("type4", option1Type);
                            finalHashmap.put("type3", option2Type);
                            finalHashmap.put("type2", option3Type);
                            finalHashmap.put("type1", option4Type);

                            break;
                    }
                    questionsArrayList.add(finalHashmap);


                }
                arrayListForUser = new ArrayList<>();
                for (int g = 0; g < questionsArrayList.size(); g++) {
                    HashMap<Integer, String[]> optionsMap = new HashMap<>();
                    String[] op = new String[4];
                    op[0] = "FALSE";
                    op[1] = "FALSE";
                    op[2] = "FALSE";
                    op[3] = "FALSE";
                    optionsMap.put(g, op);
                    arrayListForUser.add(optionsMap);
                }
                if (Util.getSelectedLanguage(context).equals("Hindi")) {
                    DiagonosticTestAdapter_Mobile.selectedLanguge = "Hindi";
                } else {
                    DiagonosticTestAdapter_Mobile.selectedLanguge = "English";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textViewToatal.setText(questionText + " 0 / " + questionsArrayList.size());
        //imageViewBack.setVisibility(View.GONE);
        setAttemptedQ(0);
        setSkippedQ(0);
        setYetToAttemptQ(questionsArrayList.size());
        diagonosticTestAdapterMobile = new DiagonosticTestAdapter_Mobile(context, arrayListForUser, questionsArrayList, mRecyclerView, selectedLanguge, getScreenHeight() / 2 + 350, submit, submitTest, imageInternet, lastMessage);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("Status", "unattempted");
            diagonosticTestAdapterMobile.trackArrayList.add(data);
        }
        mRecyclerView.setAdapter(diagonosticTestAdapterMobile);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = mRecyclerView.computeHorizontalScrollOffset();
                if (offset % mRecyclerView.getWidth() == 0) {
                    int position = offset / mRecyclerView.getWidth() + 1;
                    textViewToatal.setText(questionText+" " + position + " / " + questionsArrayList.size());
                    if (position==1)
                    {
                        imageViewBack.setVisibility(View.GONE);
                    }else {
                        imageViewBack.setVisibility(View.VISIBLE);
                    }
                    if (position == questionsArrayList.size()) {
                        if (questionsArrayList.get(position - 1).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                            textViewSubmit.setText(submitAnswer);
                            textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                            textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
                        } else {
                            textViewSubmit.setText(leaveQuestions);
                            textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                            textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
                        }
//                        textViewSkip.setVisibility(View.VISIBLE);
                    } else {
                        textViewSubmit.setText(leaveQuestions);
                        textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                        textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
//                        textViewSkip.setVisibility(View.GONE);
                    }

                    if (questionsArrayList.get(position - 1).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                        textViewSubmit.setText(submitAnswer);
                        textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
                        textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
                    }

                    pos = position - 1;
                    checkItsLastQuestion();
                }
            }
        });
        trackTestAdapterMobile = new TrackTestAdapter_Mobile(context, questionsArrayList);
        recyclerViewTrack.setAdapter(trackTestAdapterMobile);
        trackTestAdapterMobile.SetOnItemClickListener(new TrackTestAdapter_Mobile.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (position == questionsArrayList.size()) {
//                    textViewSubmit.setText(leaveQuestions);
//                    textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                    textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
////                    textViewSkip.setVisibility(View.VISIBLE);
//                } else {
//                    textViewSubmit.setText(leaveQuestions);
//                    textViewSubmit.setTextColor(getResources().getColor(R.color.white));
//                    textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
////                    textViewSkip.setVisibility(View.GONE);
//                }


                if (mRecyclerView != null) {
                    mRecyclerView.scrollToPosition(position);
                }
                pos = position;

            }
        });
        //mRecyclerView.stopScroll();
    }
    public void checkItsLastQuestion(){
        if (pos == questionsArrayList.size()-1) {
            pos=questionsArrayList.size();
            imageViewBack.setVisibility(View.VISIBLE);
            textViewSubmit.setText(parikshanSubmit);
            textViewSubmit.setTextColor(getResources().getColor(R.color.blue));
            textViewSubmit.setBackgroundResource(R.drawable.trans_blue_iprep_button);
        }
    }
    public void refreshQuestionsListingAdapter() {
        if (trackTestAdapterMobile != null) {
            trackTestAdapterMobile.questionsArrayList = questionsArrayList;
            trackTestAdapterMobile.notifyDataSetChanged();
        }
    }

    public void setAttemptedQ(int count) {
        if (attemptedQ != null) {
            attemptedQ.setText(count + "\n"+attempted_text);
        }
    }

    public void setSkippedQ(int count) {
        if (skippedQ != null) {
            skippedQ.setText(count + "\n"+skipped_text);
        }
    }

    public void setYetToAttemptQ(int count) {
        if (yetToAttemptQ != null) {
            yetToAttemptQ.setText(count + "\n"+yet_to_attempt_text);
        }
    }

    private boolean isSkippedQ(int pos) {
        boolean isSkipped = true;
        for (Map.Entry<Integer, Boolean> entry : skippedQHashMap.entrySet()) {
            if (entry.getKey() == pos) {
                isSkipped = false;
                break;
            }
        }
        return isSkipped;
    }

    public void updateSkippedQMap(int pos, int type) {
        if (skippedQHashMap != null) {
            if (type == 0) {
                if (!skippedQHashMap.containsKey(pos) && !attemptedQMap.contains(pos)) {
                    skippedQHashMap.put(pos, true);
                    skipCount++;
                    setSkippedQ(skipCount);
                }
            } else {
                if (skippedQHashMap.containsKey(pos) && skippedQHashMap.get(pos) && attemptedQMap.contains(pos)) {
                    skippedQHashMap.put(pos, false);
                    skipCount--;
                    skippedQHashMap.remove(pos);
                    setSkippedQ(skipCount);
                }
            }
        }
    }

    public void checkSkippedQ(int pos, int type) {
        if (isSkippedQ(pos)) {
            updateSkippedQMap(pos, type);
        } else {
            if (skipCount != 0) {
                updateSkippedQMap(pos, type);
            }
        }
    }

    private void clearSkipData() {
        skipCount = 0;
        setSkippedQ(0);
        if (attemptedQMap != null) {
            attemptedQMap.clear();
        }
        if (skippedQHashMap != null) {
            skippedQHashMap.clear();
        }
    }

    public void removeAttempt(int position) {
        if (attemptedQMap != null) {
            attemptedQMap.remove(position);
        }
    }

    private void startCountDownTimer() throws Exception {
        final long time = duration * 60 * 1000;
        countDownTimerWithPause = new CountDownTimerWithPause(time, 1000, true) {
            public void onTick(long millisUntilFinished) {
                long progressTime = millisUntilFinished / 100;
                progress++;
                arcProgress.setProgress(progress * 100 / (time / 1000));
                String time = Util.getTimeString_(millisUntilFinished / 1000);
                arcProgress.setBottomText(time);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                progress++;

                arcProgress.setProgress(100);
                try {
                    countDownTimerWithPause.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                submitTestPaper();
                try {
                    // syncScore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                openDialog();
            }
        };

        try {
            countDownTimerWithPause.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startModelTestpaper() {
        try {
            getQuestions(topicId);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.dialog_start_model);
//        dialog.setCancelable(false);
//        TextView textView = dialog.findViewById(R.id.textView);
//        String mess = "आप" + Util.getTopicNameAlt(context) + " का डायग्नोस्टिक टेस्ट शुरू करने जा रहे है। कृपया स्टार्ट बटन पे क्लिक करके शुरू करें। आपकी सफलता के लिए आपको शुभकामनाएं।";
//        textView.setText(mess);
//        TextView textViewGoBack = dialog.findViewById(R.id.textViewGoBack);
//        textViewGoBack.setText(goBack);
//        TextView textViewStart = dialog.findViewById(R.id.textViewStart);
//        textViewStart.setText(start);
//        textViewStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    dialog.dismiss();
//                    getQuestions(topicId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        textViewGoBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                finish();
//                
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                isNeededToOpenBackDialog = true;
//            }
//        });
    }

    private void resumeModelTestpaper() {
        isDialogOpend = true;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pause_model);
        dialog.setCancelable(false);
        TextView textViewStart = dialog.findViewById(R.id.textViewStart);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(welcomeBack);
        textViewStart.setText(resume);
        try {

            if (countDownTimerWithPause != null) {
                countDownTimerWithPause.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                try {
                    countDownTimerWithPause.resume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                isDialogOpend = false;
                isNeededToOpenBackDialog = true;
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

    public void submitTestPaper() {
        Util.showScoreDialog(context, calculateScore);
        isNeededToOpenBackDialog = false;
        date = Util.getCurrentDateWithDifferentFormat();

        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
        scoreModel.setTotalScores(questionsArrayList.size() + "");
        scoreModel.setQuestionsAttempted(attemptedQuestions);
        scoreModel.setTotalQuestions(questionsArrayList.size() + "");
        scoreModel.setTopicName(Util.getTopicNameAlt(context));
        scoreModel.setDate(date);

        HashMap<String, Object> scoreDetailHashmap = new HashMap<>();
        scoreDetailHashmap.put("scores", scoreModel);
        scoreDetailHashmap.put("detail_review", questionsArrayList);
        diagonosticCount = diagonosticCount + 1;
        endTime = System.currentTimeMillis();
        long timeTosync = endTime - startTime;
        long ttime=timeTosync;

        timeTosync = timeTosync + timeget;

        HashMap<String, String> row_usage = new HashMap<>();
        row_usage.put("category", "diagonostic_test");
        row_usage.put("language", Util.getSelectedLanguage(context));
        row_usage.put("class", Util.getSelectedClass(context));
        row_usage.put("content_name", Util.getTopicNameAlt(context).trim());
        row_usage.put("topic_name", Util.getTopicNameAlt(context).trim());
        row_usage.put("district", Util.getDistrict(context));
        row_usage.put("total_qsn", "" + questionsArrayList.size());
        row_usage.put("schoolID", Util.getSchoolId(context));
        row_usage.put("schoolName", Util.getSchoolName(context));
        row_usage.put("projectId", Util.getProjectId(context));
        row_usage.put("score", diagonosticTestAdapterMobile.scoreArrayList.size() + "");
        row_usage.put("state", Util.getSelectedState(context));
        row_usage.put("subject", Util.getSubject(context));
        row_usage.put("subject_name", Util.getSubjectName(context));
        row_usage.put("time_spent", "" + ttime);
        row_usage.put("topic", "" + topicId);
        row_usage.put("userId", Util.getUserId(context));
        row_usage.put("username", Util.getUsername(context));
        row_usage.put("username", Util.getUsername(context));
        row_usage.put("app_id", Util.getAPPID(context));
        // row_usage.put("userIdFirebase", Util.getLoginUserId(context));

        row_usage.put("board", Util.getSelectedBoard(context));
        row_usage.put("category_name", "Diagnostic Test");
        row_usage.put("userType", "students");
        System.out.println( "------- Util.getDistrict(context) "+Util.getDistrict(context));

        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(timeTosync);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("diagonostic_test").setValue(diagonosticCount);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("diagonostic_test").child(Util.getSubjectName(context)).child(date).child("" + System.currentTimeMillis()).setValue(scoreDetailHashmap);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("diagonostic_test").child(date).child(topicId).child("name").setValue(Util.getTopicNameAlt(context));
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("diagonostic_test").child(date).child(topicId).child("detail").child("" + System.currentTimeMillis()).setValue(scoreDetailHashmap);


        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("diagonostic_test").child(topicId).child("detail").setValue(scoreDetailHashmap);

        int percentage = (diagonosticTestAdapterMobile.scoreArrayList.size() * 100) / questionsArrayList.size();

        //For Time Spent
        if (reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, subject,Util.getSelectedLanguage(context))) {
            reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, subject, timeTosync,Util.getSelectedLanguage(context));
        } else {
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
        if (reportsCountRepository.isDataExist(Util.getUserId(context), board, sClass, date, "diagonostic_test")) {
            reportsCountRepository.updateField(Util.getUserId(context), board, sClass, date, "diagonostic_test", diagonosticCount);
        } else {
            ReportsCountModel reportsCountModel = new ReportsCountModel();
            reportsCountModel.setUserId(Util.getUserId(context));
            reportsCountModel.setBoard(board);
            reportsCountModel.setSClass(sClass);
            reportsCountModel.setDate(date);
            reportsCountModel.setType("diagonostic_test");
            reportsCountModel.setCount(diagonosticCount);
            reportsCountRepository.insertCountDetails(reportsCountModel);
        }

        //For Date Wise
        if (reportsDateWiseTestRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "diagonostic_test", date, System.currentTimeMillis())) {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsDateWiseTestRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "diagonostic_test", date, System.currentTimeMillis(), listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored());
        } else {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsDateWiseTestModel reportsDateWiseTestModel = new ReportsDateWiseTestModel();
            reportsDateWiseTestModel.setUserId(Util.getUserId(context));
            reportsDateWiseTestModel.setBoard(board);
            reportsDateWiseTestModel.setSClass(sClass);
            reportsDateWiseTestModel.setSubject(Util.getSubject(context));
            reportsDateWiseTestModel.setType("diagonostic_test");
            reportsDateWiseTestModel.setTestDate(date);
            reportsDateWiseTestModel.setTime(System.currentTimeMillis());
            reportsDateWiseTestModel.setList(list);
            reportsDateWiseTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsDateWiseTestRepository.insertTestDetails(reportsDateWiseTestModel);
        }

        //For Topic Wise
        if (reportsTopicWiseTestRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "diagonostic_test", topicId, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))) {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsTopicWiseTestRepository.updateField(Util.getUserId(context), board, sClass, subject, "diagonostic_test", topicId, date,
                    System.currentTimeMillis(), Util.getTopicNameAlt(context), listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored(),Util.getSelectedLanguage(context));
        } else {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsTopicWiseTestModel reportsTopicWiseTestModel = new ReportsTopicWiseTestModel();
            reportsTopicWiseTestModel.setUserId(Util.getUserId(context));
            reportsTopicWiseTestModel.setBoard(board);
            reportsTopicWiseTestModel.setSClass(sClass);
            reportsTopicWiseTestModel.setSubject(Util.getSubject(context));
            reportsTopicWiseTestModel.setType("diagonostic_test");
            reportsTopicWiseTestModel.setTestDate(date);
            reportsTopicWiseTestModel.setTime(System.currentTimeMillis());
            reportsTopicWiseTestModel.setTopicId(topicId);
            reportsTopicWiseTestModel.setName(Util.getTopicNameAlt(context));
            reportsTopicWiseTestModel.setList(list);
            reportsTopicWiseTestModel.setLang(Util.getSelectedLanguage(context));
            reportsTopicWiseTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsTopicWiseTestRepository.insertTestDetails(reportsTopicWiseTestModel);
        }

        //For Latest data
        if (reportsLatestDataTestRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "diagnostic_test", topicId)) {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsLatestDataTestRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "diagnostic_test", topicId, listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored());
        } else {
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(diagonosticTestAdapterMobile.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for (HashMap<String, String> item : questionsArrayList) {
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsLatestDataTestModel reportsLatestDataTestModel = new ReportsLatestDataTestModel();
            reportsLatestDataTestModel.setUserId(Util.getUserId(context));
            reportsLatestDataTestModel.setBoard(board);
            reportsLatestDataTestModel.setSClass(sClass);
            reportsLatestDataTestModel.setSubject(Util.getSubject(context));
            reportsLatestDataTestModel.setType("diagnostic_test");
            reportsLatestDataTestModel.setTopicId(topicId);
            reportsLatestDataTestModel.setList(list);
            reportsLatestDataTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsLatestDataTestRepository.insertTestDetails(reportsLatestDataTestModel);
        }

        if (isAssigned != null) {
            if (isAssigned.equalsIgnoreCase("true")) {
                String scores = diagonosticTestAdapterMobile.scoreArrayList.size() + "/" + questionsArrayList.size();
                global.getDatabaseReference().child("content_assignement_batch_wise").child(teacherID).child(batchId).child(datetostore).child(keyTo).child("student").child(Util.getUserId(context)).child("progress").setValue(scores);
            }
        }

        //save date topic wise to show it in the topic listing path for diagnostic test
        if (!testDetailsRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), topicId)) {
            TestModel testModel = new TestModel();
            testModel.setUserId(Util.getUserId(context));
            testModel.setSubjectId(Util.getSubject(context));
            testModel.setTopicId(topicId);
            testModel.setDate(date);
            testDetailsRepository.insertTestDetails(testModel);
        } else {
            testDetailsRepository.updateField(Util.getUserId(context), Util.getSubject(context), topicId, date);
        }

        //global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("diagonostic_test").child(date).child(topicId).child("name").setValue(Util.getTopicNameAlt(context));


        openDialog();
    }

    private void getTime() {
        date = Util.getCurrentDateWithDifferentFormat();
        if (!Util.isNetworkAvailable(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "diagnostic_test", "timeTask");
        } else {
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

    private void getBiMonthyTestCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if (!Util.isNetworkAvailable(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "diagnostic_test", "countTask");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("users").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("diagonostic_test").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            diagonosticCount = (long) snapshot.getValue();
                        } else {
                            diagonosticCount = 0;
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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String date, String testType, String type) {
        if (type.equals("timeTask")) {
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
                            if (list != null && list.size() > 0) {
                                for (ReportsTimeSpentModel item : list) {
                                    timeget = item.getTime();
                                }
                            } else {
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
        } else if (type.equals("countTask")) {
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            countTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsCountModel> list = (ArrayList<ReportsCountModel>) o;
                            if (list != null && list.size() > 0) {
                                for (ReportsCountModel item : list) {
                                    diagonosticCount = item.getCount();
                                }
                            } else {
                                diagonosticCount = 0;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            countTaskDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date, String testType, String type) {
        if (type.equals("timeTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, subject,Util.getSelectedLanguage(context));
            });
        } else if (type.equals("countTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsCountRepository.getDetail(userId, board, sClass, date, testType);
            });
        }
        return null;
    }

    public void scroll(final int pos) {
        mRecyclerView.smoothScrollToPosition(pos);


    }

    public void setTextOnQuestionAttampted(int size) {
        this.attemptedQuestions = String.valueOf(size);
//        textViewToatal.setText("Question " + size + " / " + questionsArrayList.size());
    }


//    private void showExitDialog() {
//        SelectTopicActivity.currentTestScoreChanged = "0";
//        isNeededToOpenBackDialog = false;
//        try {
//            if (countDownTimerWithPause != null) {
//                countDownTimerWithPause.pause();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.dialog_exit_model);
//        dialog.setCancelable(false);
//        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
//        TextView textView = dialog.findViewById(R.id.textView);
//        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
//        TxtExit.setText("Exit Test");
//        textView.setText(warning + "\n" + exitMessage);
//        textViewContinoue.setText(continueTest);
//
//        textViewContinoue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Util.preventTwoClick(view);
////                try {
////                    countDownTimerWithPause.resume();
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                dialog.dismiss();
//            }
//        });
//        TxtExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Util.preventTwoClick(view);
//                dialog.dismiss();
//                com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.backPressed = true;
//                finish();
//                
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
////        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////            @Override
////            public void onDismiss(DialogInterface dialogInterface) {
////                isNeededToOpenBackDialog = true;
////            }
////        });
//
//    }


    private void saveFoundationalDetails() {
        if (Util.isOfflineMode(context)) {
            try {

                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(topicId);
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String StreakCount = object_______.getString("StreakCount");
                String foundational_class = object_______.getString("foundational_class");
                String TName = object_______.getString("TName");
                String incorrectStreak = object_______.getString("incorrectStreak");
                if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
//                    Util.showToast(context, "Not Available");
//                    PalContentListingActivity.instance.setTopicIdList(topicId);
//                    PalContentListingActivity.instance.setNextTopicPosition(topicId);
                } else {
                    String seniorTopicID = topicId;
                    String seniorClass = sClass;
                    String seniorTopicName = TName;

                    String sClass = foundational_class;
//                    Util.setClassNameSelection(context, foundational_class);
//                    Util.setClassSelection(context, foundational_class);
//                    Util.setClassNameSelectionDummy(context, foundational_class);
//                    Util.setClassSelectionDummy(context, foundational_class);
                    String streakProgress = "0";

                    // getting lower topic Name
                    String filePath1 = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                    JSONObject jsonObject1 = Util.readJsonFile(context, filePath1);
                    JSONObject object__1 = jsonObject1.getJSONObject(Util.getSelectedLanguagePackage(context));
                    JSONObject object___1 = object__1.getJSONObject("practice");
                    JSONObject object____1 = object___1.getJSONObject("content");
                    JSONObject object_____1 = object____1.getJSONObject(Util.getSubject(context));
                    JSONObject object______1 = object_____1.getJSONObject("topics");
                    JSONObject object_______1 = object______1.getJSONObject(Foundational_Topic_ID);
                    String TName1 = object_______1.getString("TName");

                    if (!foundationalTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, Foundational_Topic_ID,Util.getSelectedLanguage(context))) {
                        FoundationalTopicModel foundationalTopicModel = new FoundationalTopicModel();
                        foundationalTopicModel.setUserId(Util.getUserId(context));
                        foundationalTopicModel.setSubjectId(Util.getSubject(context));
                        foundationalTopicModel.setTopicId(Foundational_Topic_ID);
                        foundationalTopicModel.setTopicName(TName1);
                        foundationalTopicModel.setSClass(sClass);
                        foundationalTopicModel.setStreakProgress(streakProgress);
                        foundationalTopicModel.setStreakCount(StreakCount);
                        foundationalTopicModel.setIncorrectStreak(incorrectStreak);
                        foundationalTopicModel.setPracticeType("junior");
                        foundationalTopicModel.setSeniorClass(seniorClass);
                        foundationalTopicModel.setSeniorTopicID(seniorTopicID);
                        foundationalTopicModel.setSeniorTopicName(seniorTopicName);
                        foundationalTopicModel.setShow(true);
                        foundationalTopicModel.setVideoLevel(0);
                        foundationalTopicModel.setTestPercentageAchieved(testPercentageAchieved);
                        foundationalTopicModel.setLang(Util.getSelectedLanguage(context));
                        foundationalTopicRepository.insertFoundationalTopicDetails(foundationalTopicModel);
                    } else {
                        foundationalTopicRepository.updateFields(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, TName1, sClass, streakProgress, StreakCount, incorrectStreak, "junior", true, 0, testPercentageAchieved,Util.getSelectedLanguage(context));
                    }
                    PalContentListingActivity_Mobile.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(seniorClass).child(Util.getSubject(context)).child(seniorTopicID).child("FoundationalPractice").setValue("F");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass)
                    .child(Util.getSelectedLanguagePackage(context)).child("practice")
                    .child("content").child(Util.getSubject(context)).child("topics")
                    .child(topicId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                                    String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                                    if (Foundational_Topic_ID.equalsIgnoreCase("Not Available")|| Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
                                        //Util.showToast(context, "Not Available");
                                        PalContentListingActivity_Mobile.instance.setTopicIdList(topicId);
                                        PalContentListingActivity_Mobile.instance.setNextTopicPosition(topicId);
                                    } else {
                                        String foundational_class = detailHashMap.get("foundational_class");
                                        String streak = detailHashMap.get("StreakCount");
                                        String incorrectStreak = detailHashMap.get("incorrectStreak");
                                        String TName = detailHashMap.get("TName");
                                        String seniorTopicID = topicId;
                                        String seniorClass = sClass;
                                        String seniorTopicName = TName;

                                        String sClass = foundational_class;
//                                Util.setClassNameSelection(context, foundational_class);
//                                Util.setClassSelection(context, foundational_class);
//                                Util.setClassNameSelectionDummy(context, foundational_class);
//                                Util.setClassSelectionDummy(context, foundational_class);
                                        String streakProgress = "0";
                                        String foundation_subject = Util.getSubject(context);
                                        if (Foundational_Topic_ID.contains("pol_sci")) {
                                                foundation_subject = "political_science";

                                        }
                                        else if (Foundational_Topic_ID.contains("sci")) {
                                            foundation_subject = "science";
                                        }
                                        else if (Foundational_Topic_ID.contains("gra")) {
                                            foundation_subject = "geography";
                                        }
                                        else if (Foundational_Topic_ID.contains("eco")) {
                                            foundation_subject ="economics";
                                        }
                                        else if (Foundational_Topic_ID.contains("eng_gr")) {
                                            foundation_subject = "english_grammar";
                                        }
                                        else if (Foundational_Topic_ID.contains("his")) {
                                            foundation_subject = "history";
                                        }
                                        else if (Foundational_Topic_ID.contains("evs")) {
                                            foundation_subject = "evs";
                                        }

                                        // getting lower topic Name
                                        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(foundation_subject).child("topics").child(Foundational_Topic_ID).child("TName").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                try {
                                                    if (snapshot.getValue() != null) {
                                                        String name = (String) snapshot.getValue();

                                                        clearMastery(Foundational_Topic_ID,"practice");

                                                        HashMap<String, String> map = new HashMap<>();
                                                        map.put("sClass", seniorClass);
                                                        map.put("seniorTopicID", seniorTopicID);
                                                        global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguage(context)).child(Foundational_Topic_ID).setValue(map);


                                                        if (!foundationalTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, Foundational_Topic_ID,Util.getSelectedLanguage(context))) {
                                                            FoundationalTopicModel foundationalTopicModel = new FoundationalTopicModel();
                                                            foundationalTopicModel.setUserId(Util.getUserId(context));
                                                            foundationalTopicModel.setSubjectId(Util.getSubject(context));
                                                            foundationalTopicModel.setTopicId(Foundational_Topic_ID);
                                                            foundationalTopicModel.setTopicName(name);
                                                            foundationalTopicModel.setSClass(sClass);
                                                            foundationalTopicModel.setStreakProgress(streakProgress);
                                                            foundationalTopicModel.setStreakCount(streak);
                                                            foundationalTopicModel.setIncorrectStreak(incorrectStreak);
                                                            foundationalTopicModel.setPracticeType("junior");
                                                            foundationalTopicModel.setSeniorClass(seniorClass);
                                                            foundationalTopicModel.setSeniorTopicID(seniorTopicID);
                                                            foundationalTopicModel.setSeniorTopicName(seniorTopicName);
                                                            foundationalTopicModel.setShow(true);
                                                            foundationalTopicModel.setVideoLevel(0);
                                                            foundationalTopicModel.setTestPercentageAchieved(testPercentageAchieved);
                                                            foundationalTopicModel.setLang(Util.getSelectedLanguage(context));
                                                            foundationalTopicRepository.insertFoundationalTopicDetails(foundationalTopicModel);
                                                        } else {
                                                            foundationalTopicRepository.updateFields(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, name, sClass, streakProgress, streak, incorrectStreak, "junior", true, 0, testPercentageAchieved,Util.getSelectedLanguage(context));
                                                        }
                                                        if(PalContentListingActivity_Mobile.instance!=null) PalContentListingActivity_Mobile.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                        else if(PalGlobalSearchActivity.palGlobalSearchActivity!=null) PalGlobalSearchActivity.palGlobalSearchActivity.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(seniorClass).child(Util.getSubject(context)).child(seniorTopicID).child("FoundationalPractice").setValue("F");
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
                                } else {
                                    String someIssue;

                                    someIssue = textArrayList.get(6);

//                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                    {
//                                        someIssue ="कुछ समस्या हुई है बाद में पुन: प्रयास करें";
//                                    }else {
//                                        someIssue ="Some issue occur Please try again!";
//                                    }
                                    openGifDialogue(someIssue);
//                            Util.showToast(context, "Some issue occur Please try again!");
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

    private void getJuniorLevelPractice() {

        if (Util.isOfflineMode(context)) {
            try {

                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(topicId);
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String StreakCount = object_______.getString("StreakCount");
                String foundational_class = object_______.getString("foundational_class");
                String incorrectStreak = object_______.getString("incorrectStreak");
                String TName = object_______.getString("TName");
                if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
                    // Util.showToast(context, "Not Available");
                } else {
                    String seniorTopicID = topicId;
                    String seniorClass = sClass;
                    String seniorTopicName = TName;
//                    topicId = Foundational_Topic_ID;
                    Util.setTopicID(context, Foundational_Topic_ID);
                    Util.setLevel(context, Integer.parseInt("1"));
                    String sClass = foundational_class;

                    String streakProgress = "0";
                    global.setProgress(0);
                    // getting lower topic Name
                    String filePath1 = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                    JSONObject jsonObject1 = Util.readJsonFile(context, filePath1);
                    JSONObject object__1 = jsonObject1.getJSONObject(Util.getSelectedLanguage(context));
                    JSONObject object___1 = object__1.getJSONObject("practice");
                    JSONObject object____1 = object___1.getJSONObject("content");
                    JSONObject object_____1 = object____1.getJSONObject(Util.getSubject(context));
                    JSONObject object______1 = object_____1.getJSONObject("topics");
                    JSONObject object_______1 = object______1.getJSONObject(Foundational_Topic_ID);
                    String TName1 = object_______1.getString("TName");
                    Util.setTopicNameAlt(context, TName1);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("sClass", seniorClass);
                    map.put("seniorTopicID", seniorTopicID);
                    global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Foundational_Topic_ID).setValue(map);

                    if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                        trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                    } else {
                        TrackTopicModel trackTopicModel = new TrackTopicModel();
                        trackTopicModel.setUserId(Util.getUserId(context));
                        trackTopicModel.setSubject(Util.getSubject(context));
                        trackTopicModel.setBoard(board);
                        trackTopicModel.setSeniorTopicId(seniorTopicID);
                        trackTopicModel.setSeniorClass(seniorClass);
                        trackTopicModel.setSeniorTopicName(seniorTopicName);
                        trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                        trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                        trackTopicRepository.insertDetails(trackTopicModel);
                    }

                    HashMap<String, String> mapF = new HashMap<>();
                    mapF.put("foundational_class", foundational_class);
                    mapF.put("foundational_topic", Foundational_Topic_ID);
                    global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);

                    if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                        startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", testPercentageAchieved));

                        finish();
                    }else {
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                        {
                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                        }else {
                            Util.openGifDialogue(context,"Internet Connection is not working");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if(textArrayList==null)
            {
                get_text_from_backend();
            }



//            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    try {
//                        if (snapshot.getValue() != null) {
//                            HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
//                            String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
//                            if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
//                                //Util.showToast(context, "Not Available");
//                            } else {
//                                String foundational_class = detailHashMap.get("foundational_class");
//                                String streak = detailHashMap.get("StreakCount");
//                                String incorrectStreak = detailHashMap.get("incorrectStreak");
//                                String TName = detailHashMap.get("TName");
//                                String seniorTopicID = topicId;
//                                String seniorClass = sClass;
//                                String seniorTopicName = TName;
//                                topicId = Foundational_Topic_ID;
//                                Util.setTopicID(context, Foundational_Topic_ID);
//                                Util.setLevel(context, Integer.parseInt("1"));
//                                String sClass = foundational_class;
////                                Util.setClassNameSelection(context, foundational_class);
////                                Util.setClassSelection(context, foundational_class);
////                                Util.setClassNameSelectionDummy(context, foundational_class);
////                                Util.setClassSelectionDummy(context, foundational_class);
//                                String streakProgress = "0";
//                                global.setProgress(0);
//
//                                HashMap<String, String> map = new HashMap<>();
//                                map.put("sClass", seniorClass);
//                                map.put("seniorTopicID", seniorTopicID);
//                                global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Foundational_Topic_ID).setValue(map);
//
//                                if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
//                                    trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
//                                } else {
//                                    TrackTopicModel trackTopicModel = new TrackTopicModel();
//                                    trackTopicModel.setUserId(Util.getUserId(context));
//                                    trackTopicModel.setSubject(Util.getSubject(context));
//                                    trackTopicModel.setBoard(board);
//                                    trackTopicModel.setSeniorTopicId(seniorTopicID);
//                                    trackTopicModel.setSeniorClass(seniorClass);
//                                    trackTopicModel.setSeniorTopicName(seniorTopicName);
//                                    trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
//                                    trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
//                                    trackTopicRepository.insertDetails(trackTopicModel);
//                                }
//
//                                HashMap<String, String> mapF = new HashMap<>();
//                                mapF.put("foundational_class", foundational_class);
//                                mapF.put("foundational_topic", Foundational_Topic_ID);
//                                global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);
//
//
//                                // getting lower topic Name
//                                global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(topicId).child("TName").addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        try {
//                                            if (snapshot.getValue() != null) {
//                                                String name = (String) snapshot.getValue();
//                                                Util.setTopicNameAlt(context, name);
//                                                if (Util.isNetworkAvailable(context)) {
//                                                    startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", testPercentageAchieved));
//
//                                                    finish();
//                                                }else {
//                                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                                    {
//                                                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
//                                                    }else {
//                                                        Util.openGifDialogue(context,"Internet Connection is not working");
//                                                    }
//                                                }
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//
//
//                        } else {
//
//                            if (topicId.contains("sci"))
//                            {
//
//                            }
//                            String someIssue;
//
//                            try
//                            {
//                                someIssue=textArrayList.get(6);
//                            }catch (Exception r)
//                            {
//                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                {
//                                    someIssue ="कुछ समस्या हुई है बाद में पुन: प्रयास करें";
//                                }else {
//                                    someIssue ="Some issue occur Please try again!";
//                                }
//                            }
//
//                            openGifDialogue(someIssue);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            set_topicc(sClass,Util.getSubject(context));

        }
    }

    private void set_topicc(String sClass,String subject) {
        System.out.println("-------- board "+board);
        System.out.println("-------- sClass "+sClass);
        System.out.println("-------- Util.getSubject(context) "+Util.getSubject(context));
        System.out.println("-------- topicId "+topicId);

        String foundation_subject = subject;
        if (topicId.contains("pol_sci"))
        {
            foundation_subject = "political_science";

        }else if (topicId.contains("sci")) {
            foundation_subject = "science";
        }
        else if (topicId.contains("gra")) {
            foundation_subject = "geography";
        }
        else if (topicId.contains("eco"))
        {
            foundation_subject ="economics";
        }
        else if (topicId.contains("eng_gr"))
        {
            foundation_subject = "english_grammar";
        }
        else if (topicId.contains("his"))
        {
            foundation_subject = "history";
        }
        else if (topicId.contains("evs"))
        {
            foundation_subject = "evs";
        }


        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(foundation_subject).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        System.out.println("------ getting data ");
                        HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                        String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                        if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
                            Util.showToast(context, "Not Available");
                        } else {
                            String foundational_class = detailHashMap.get("foundational_class");
                            String streak = detailHashMap.get("StreakCount");
                            String incorrectStreak = detailHashMap.get("incorrectStreak");
                            String TName = detailHashMap.get("TName");
                            String seniorTopicID = topicId;
                            String seniorClass = sClass;
                            String seniorTopicName = TName;
                            topicId = Foundational_Topic_ID;
                            Util.setTopicID(context, Foundational_Topic_ID);
                            Util.setLevel(context, Integer.parseInt("1"));
                            String sClass = foundational_class;
//                                Util.setClassNameSelection(context, foundational_class);
//                                Util.setClassSelection(context, foundational_class);
//                                Util.setClassNameSelectionDummy(context, foundational_class);
//                                Util.setClassSelectionDummy(context, foundational_class);
                            String streakProgress = "0";
                            global.setProgress(0);

                            String foundation_subject = subject;
                            if (Foundational_Topic_ID.contains("pol_sci"))
                            {
                                foundation_subject = "political_science";

                            }else if (Foundational_Topic_ID.contains("sci")) {
                                foundation_subject = "science";
                            }
                            else if (Foundational_Topic_ID.contains("gra")) {
                                foundation_subject = "geography";
                            }
                            else if (Foundational_Topic_ID.contains("eco"))
                            {
                                foundation_subject ="economics";
                            }
                            else if (Foundational_Topic_ID.contains("eng_gr"))
                            {
                                foundation_subject = "english_grammar";
                            }
                            else if (Foundational_Topic_ID.contains("his"))
                            {
                                foundation_subject = "history";
                            }
                            else if (Foundational_Topic_ID.contains("evs"))
                            {
                                foundation_subject = "evs";
                            }

                            HashMap<String, String> map = new HashMap<>();
                            map.put("sClass", seniorClass);
                            map.put("seniorTopicID", seniorTopicID);
                            global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Foundational_Topic_ID).setValue(map);

                            if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                                trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                            } else {
                                TrackTopicModel trackTopicModel = new TrackTopicModel();
                                trackTopicModel.setUserId(Util.getUserId(context));
                                trackTopicModel.setSubject(Util.getSubject(context));
                                trackTopicModel.setBoard(board);
                                trackTopicModel.setSeniorTopicId(seniorTopicID);
                                trackTopicModel.setSeniorClass(seniorClass);
                                trackTopicModel.setSeniorTopicName(seniorTopicName);
                                trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                                trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                                trackTopicRepository.insertDetails(trackTopicModel);
                            }

                            HashMap<String, String> mapF = new HashMap<>();
                            mapF.put("foundational_class", foundational_class);
                            mapF.put("foundational_topic", Foundational_Topic_ID);
                            global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);


                            // getting lower topic Name
                            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(foundation_subject).child("topics").child(topicId).child("TName").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        if (snapshot.getValue() != null) {
                                            String name = (String) snapshot.getValue();
                                            Util.setTopicNameAlt(context, name);
                                            if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", testPercentageAchieved));
                                                finish();
                                            }else {
                                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                                {
                                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                                }else {
                                                    Util.openGifDialogue(context,"Internet Connection is not working");
                                                }
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


                    } else {

                        if (topicId.contains("sci"))
                        {
                            classs--;
                            if(topicId.contains("pol")) set_topicc(String.valueOf(classs),"political_science");
                            else set_topicc(String.valueOf(classs),"science");
                        }
//                        String someIssue;
//
//                        try
//                        {
//                            someIssue=textArrayList.get(6);
//                        }catch (Exception r)
//                        {
//                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                            {
//                                someIssue ="कुछ समस्या हुई है बाद में पुन: प्रयास करें";
//                            }else {
//                                someIssue ="Some issue occur Please try again!";
//                            }
//                        }
//
//                        openGifDialogue(someIssue);
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

    private void openFailsDialog() {

        //saving details of foundational topic for maintaining path for a student and taking him to do
        //practice or watch video if he/she exits from the app
        saveFoundationalDetails();

        reletiveLayoutLowerLevel.setVisibility(View.VISIBLE);
        reletiveTop.setVisibility(View.GONE);


        String textViewPracticeText,textViewPracticeText_current_class,textViewDiagnosticMessageText,textLayout1Text,textLayout2Text,subTextLayout1Text,
                subTextLayout2Text,pointsText1Text,pointsText2Text,pointsText3Text,textViewScoreDetailText
                ,testPercentagebeloweighty,testPercentageaboveeighty,videoLevelText,textViewhundred,orTextText,testScoreTextText;


        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            textViewPracticeText ="मुलभुत बिषय का अभ्यास करें";
            textViewPracticeText_current_class ="अभ्यास करें";
        }else {
            textViewPracticeText ="Practice the Foundational Topics";
            textViewPracticeText_current_class ="Practice";
        }

        try
        {
            textViewDiagnosticMessageText = textArrayList.get(8);
            textLayout1Text = textArrayList.get(9);
            textLayout2Text = textArrayList.get(10);
            subTextLayout1Text = textArrayList.get(11);
            subTextLayout2Text = textArrayList.get(12);
            pointsText1Text = textArrayList.get(13);
            pointsText2Text = textArrayList.get(14);
            pointsText3Text = textArrayList.get(15);
            textViewScoreDetailText = textArrayList.get(16);
            testPercentagebeloweighty = textArrayList.get(17);
            testPercentageaboveeighty = textArrayList.get(18);
            videoLevelText = textArrayList.get(29);
            textViewhundred = textArrayList.get(30);
            orTextText = textArrayList.get(20);
            testScoreTextText = textArrayList.get(21);

        }
        catch (Exception r)
        {

            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
            {
                textViewPracticeText_current_class ="अभ्यास करें";
                textViewPracticeText ="मुलभुत बिषय का अभ्यास करें";
                textViewDiagnosticMessageText =" के डायग्नोस्टिक परिक्षण में आपके अंक हैं";
                textLayout1Text ="100% महारत हासिल करें";
                textLayout2Text ="फाइनल टेस्ट में 100% स्कोर";
                subTextLayout1Text ="अनलॉक हो गया है";
                subTextLayout2Text ="बंद है";
                pointsText1Text ="इस अध्याय को पूरा करने के लिए आपको दो कार्य करने होंगे :";
                pointsText2Text ="1. अभ्यास में १००% महारत प्राप्त करन";
                pointsText3Text ="2. अंतिम परिक्षण का प्रयास कर उसमे ८०% से अधिक अंक प्राप्त करना";
                textViewScoreDetailText =", आपका डायग्नोस्टिक परीक्षण स्कोर है";
                testPercentagebeloweighty ="आगे बढ़ने के लिए अपना अभ्यास शुरू करें";
                testPercentageaboveeighty ="अपने सफर की शुरुआत करने के लिए या तो आप वीडियो पाठ को देखकर अपनी समझ को और बेहतर कर सकते हैं या फिर अगर आप तैयार हैं तो सीधे अभ्यास पर भी जा सकते हैं";
                videoLevelText ="स्तर - 1 के वीडियो पाठ देखें ";
                textViewhundred ="इस अध्याय में आपको और मेहनत करनी होगी। हमें भरोसा है की आप इससे भी अच्छा कर सकते हैं।";
                orTextText ="या";
                testScoreTextText =" अंक हैं";
            }else {
                textViewPracticeText_current_class ="Go to Practice";
                textViewPracticeText ="Practice the Foundational Topics";
                textViewDiagnosticMessageText =" in the diagnostic test of your";
                textLayout1Text ="Achieve 100% mastery";
                textLayout2Text ="100% score in the final test";
                subTextLayout1Text ="Is unlocked";
                subTextLayout2Text ="Is closed";
                pointsText1Text ="You have two goals to complete this chapter:";
                pointsText2Text ="1. Complete Mastery in Practice";
                pointsText3Text ="2. Obtaining 100% marks in the final test";
                textViewScoreDetailText =", is your diagnostic test score";
                testPercentagebeloweighty ="Get started on your practice";
                testPercentageaboveeighty ="To begin your journey, you can either watch video lessons to build more understanding or straight away start attempting questions in the Practice. All the best.";
                videoLevelText =" Watch Video Lesson - Level 1 ";
                textViewhundred ="You would need to work harder and prepare better for this topic. We are sure you can do this.";
                orTextText = "Or";
                testScoreTextText = " Marks";
            }
        }


        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
        TextView testScoreText = findViewById(R.id.testScoreText);
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            textViewdMessaeg.setText(Html.fromHtml(Util.getUsername(context) + " " + topicName + textViewDiagnosticMessageText));

        }else {
            textViewdMessaeg.setText(Html.fromHtml(Util.getUsername(context) + ", In the diagnostic test of " + topicName + " your current learning level is"));
        }
//        textViewdMessaeg.setText(Html.fromHtml(Util.getUsername(context) + " " + topicName + textViewDiagnosticMessageText));
        textViewdMessaeg.setTypeface(null, Typeface.BOLD);
        TextView textViewPractice = findViewById(R.id.buttonPractice);
        TextView orText = findViewById(R.id.orText);
        orText.setText(orTextText);
        TextView textLayout1 = findViewById(R.id.textLayout1);
        textLayout1.setText(textLayout1Text);
        TextView textLayout2 = findViewById(R.id.textLayout2);
        textLayout2.setText(textLayout2Text);
        TextView subTextLayout1 = findViewById(R.id.subTextLayout1);
        subTextLayout1.setText(subTextLayout1Text);
        TextView subTextLayout2 = findViewById(R.id.subTextLayout2);
        subTextLayout2.setText(subTextLayout2Text);
        TextView testScoreLowerLevel = findViewById(R.id.testScoreLowerLevel);
        testScoreLowerLevel.setText(testPercentageAchieved + "%");
        testScoreText.setText(testScoreTextText);
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            testScoreText.setVisibility(View.VISIBLE);
        }else {
            testScoreText.setVisibility(View.GONE);
        }

        testScoreLowerLevel.setTypeface(null, Typeface.BOLD);
        TextView pointsText1 = findViewById(R.id.pointsText1);
        pointsText1.setText(pointsText1Text);
        TextView pointsText2 = findViewById(R.id.pointsText2);
        pointsText2.setText(pointsText2Text);
        TextView pointsText3 = findViewById(R.id.pointsText3);
        pointsText3.setText(pointsText3Text);
        TextView textViewScoreDetail = findViewById(R.id.textViewScoreDetail);
        textViewScoreDetail.setText(Util.getUsername(context) + textViewScoreDetailText);



        textViewDetail.setText(textViewhundred);
        info.setText(testPercentageaboveeighty);

        textViewPractice.setVisibility(View.VISIBLE);
        orText.setVisibility(View.VISIBLE);
//        info.setText("शुरुआत करने के लिए");
        if (GetTopicLevelsDetails.isJuniorTopicAvailable()) {
            textViewPractice.setText(textViewPracticeText);
            textViewDetail.setVisibility(View.GONE);
        } else {
            textViewPractice.setText(textViewPracticeText_current_class);
            textViewDetail.setVisibility(View.GONE);
        }
//        textViewPractice.setText(textViewPracticeText);
        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (GetTopicLevelsDetails.isJuniorTopicAvailable()) {
                    getJuniorLevelPractice();
                } else {
                    getSameLevelPractice();
                }

            }
        });
        TextView textViewContinoue = findViewById(R.id.buttonVideo);
        textViewContinoue.setText(videoLevelText);
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context))
                {
                    String subject = Util.getSubject(context);
                    String subjectName = Util.getSubjectName(context);
                    String icon = PalContentListingActivity_Mobile.icon;
                    Intent intent = new Intent(context, PalContentListingActivity_Mobile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Util.setVideoLevel(context, 0);
                    Util.setLevel(context, 0);
                    PracticeTopicActivity.autoplayLevelVideo=true;
                    intent.putExtra("subject", subject);
                    intent.putExtra("sClass", sClass);
                    intent.putExtra("board", board);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("icon", icon);
                    intent.putExtra("getPath", "yes");
                    Util.setTopicID(context, topicId);
                    startActivity(intent);
                    
                    finish();
                } else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }
        });
    }

    private void getSameLevelPractice() {
        if (Util.isOfflineMode(context)) {
            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(topicId);

                String foundational_class = object_______.getString("foundational_class");
                String streak = object_______.getString("StreakCount");
                String incorrectStreak = object_______.getString("incorrectStreak");
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String TName = object_______.getString("TName");

                Util.setTopicID(context, topicId);
                Util.setTopicNameAlt(context, TName);
                Util.setLevel(context, Integer.parseInt("1"));
                String studentClass = sClass;

                String streakProgress = "0";
                global.setProgress(0);

                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", TName).putExtra("testPercentageAchieved", testPercentageAchieved));
                
                finish();

//                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                {
//                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
//                }else {
//                    Util.openGifDialogue(context,"Internet Connection is not working");
//                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                            String foundational_class = detailHashMap.get("foundational_class");
                            String streak = detailHashMap.get("StreakCount");
                            String incorrectStreak = detailHashMap.get("incorrectStreak");
                            String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                            String TName = detailHashMap.get("TName");

                            Util.setTopicID(context, topicId);
                            Util.setTopicNameAlt(context, TName);
                            Util.setLevel(context, Integer.parseInt("1"));
                            String studentClass = sClass;

                            String streakProgress = "0";
                            global.setProgress(0);
                            if (Util.isNetworkAvailable(context)) {
                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", TName).putExtra("testPercentageAchieved", testPercentageAchieved));
                                
                                finish();
                            }else {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                }else {
                                    Util.openGifDialogue(context,"Internet Connection is not working");
                                }
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

    private void openSuccessDialog() {

        PalContentListingActivity_Mobile.instance.setTopicIdList(topicId);
        PalContentListingActivity_Mobile.instance.setNextTopicPosition(topicId);

        reletiveLayout.setVisibility(View.VISIBLE);
        reletiveTop.setVisibility(View.GONE);
        TextView textViewMessage = findViewById(R.id.textViewMessage);

        RelativeLayout successDescImageLayout = findViewById(R.id.successDescImageLayout);
        successDescImageLayout.setVisibility(View.GONE);

        LinearLayout successDescTextLayout = findViewById(R.id.successDescTextLayout);
        successDescTextLayout.setVisibility(View.GONE);

        LinearLayout successDescTextLayout1 = findViewById(R.id.successDescTextLayout);
        successDescTextLayout1.setVisibility(View.GONE);

        TextView textViewdTest = findViewById(R.id.textViewdTest);
        textViewdTest.setVisibility(View.GONE);

        if (GetTopicLevelsDetails.getNextTopicName() != null) {
            String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
            String nextTopicName = "<font color='#0077FF'>" + GetTopicLevelsDetails.getNextTopicName() + "</font>";
            textViewMessage.setText(Html.fromHtml("आपने " + topicName + " को पूरा अनलॉक कर लिया है और साथ ही अब आप अगले विषय " + nextTopicName + " के डायग्नोस्टिक परिक्षण को भी कर सकते हैं "));
        } else {
            String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
            textViewMessage.setText(Html.fromHtml("You have unlocked " + topicName + ", and unlocked Diagonostic test for next Topic"));
        }
        TextView buttonProceedSuccess = findViewById(R.id.buttonProceedSuccess);
//        buttonPreceed.setText("You have unlocked " + Util.getTopicNameAlt(context) + ", and unlocked Diagonostic test for next Topic");
        if (GetTopicLevelsDetails.isNextTopicAvailable()) {
            buttonProceedSuccess.setVisibility(View.VISIBLE);
            buttonProceedSuccess.setText("अगले विषय का डायग्नोस्टिक परिक्षण करें");
        } else {
            buttonProceedSuccess.setVisibility(View.GONE);
        }
        buttonProceedSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Data for next chapter
                restoreQuestionHashMap.clear();
                getNextChapterDetails();

            }
        });
        TextView otherButton = findViewById(R.id.otherButton);
        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
        otherButton.setText(Html.fromHtml(topicName + " के वीडियो पाठ देखें"));
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context))
                {
                    Intent intent = new Intent(context, PalContentListingActivity_Mobile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String icon = PalContentListingActivity_Mobile.icon;
                    String subject = PalContentListingActivity_Mobile.subject;
                    String subjectName = PalContentListingActivity_Mobile.subjectName;
                    String studentClass = PalContentListingActivity_Mobile.sClass;
                    intent.putExtra("sClass", studentClass);
                    intent.putExtra("board", board);
                    intent.putExtra("subject", subject);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("icon", icon);
                    intent.putExtra("getPath", "yes");
                    Util.setTopicID(context, topicId);
                    Util.setSubject(context, subject);
                    startActivity(intent);
                    
                    finish();
                } else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }
        });

    }

    private void getNextChapterDetails() {

        if (Util.isOfflineMode(context)) {
            Log.i("working : ", "Yes");
            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(topicId);

                String Display = object_______.getString("Display");
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String IsModelTestPaper = object_______.getString("IsModelTestPaper");
                String Levels = object_______.getString("Levels");
                String next_topic_id = object_______.getString("Next_topic_id");
                String next_chapter_name = object_______.getString("next_chapter_name");
                String next_class = object_______.getString("next_class");

                String StreakCount = object_______.getString("StreakCount");
                String TName = object_______.getString("TName");
                String TName_alt = object_______.getString("TName_alt");
                String TopicID = object_______.getString("TopicID");
                String foundational_class = object_______.getString("foundational_class");
                String incorrectStreak = object_______.getString("incorrectStreak");
                String isAlternateLanguageAvailable = object_______.getString("isAlternateLanguageAvailable");

                if (next_topic_id.equalsIgnoreCase("Not Available")) {
                    Util.showToast(context, "Not Available");
                } else {
                    Util.showDialog(context);
                    mRecyclerView.setAdapter(null);
                    topicId = next_topic_id;
                    sClass = next_class;
                    Util.setClassSelection(context, next_class);
                    textViewTopicName.setText(next_chapter_name);
                    reletiveLayout.setVisibility(View.GONE);
                    reletiveTop.setVisibility(View.VISIBLE);
                    pos = 0;
                    getQuestions(next_topic_id);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {

                            HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                            String next_topic_id = detailHashMap.get("Next_topic_id");
                            if (next_topic_id.equalsIgnoreCase("Not Available")) {
                                Util.showToast(context, "Not Available");
                            } else {
                                Util.showDialog(context);
                                mRecyclerView.setAdapter(null);
                                topicId = next_topic_id;
                                String next_class = detailHashMap.get("next_class");
                                sClass = next_class;
                                Util.setClassSelection(context, next_class);
                                textViewTopicName.setText(detailHashMap.get("next_chapter_name"));
                                reletiveLayout.setVisibility(View.GONE);
                                reletiveTop.setVisibility(View.VISIBLE);
                                pos = 0;
                                getQuestions(next_topic_id);
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

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }

    private void setLayoutText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("Diagnostic_Test_Screen");
                textViewSubmit.setText((String) object_.get("next_question_btn_text"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            checkConnection(false);
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("Diagnostic_Test_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            textViewSubmit.setText(dataHashMap.get("next_question_btn_text"));
                            hideconnection_layout();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject("english");
                JSONArray array = object.getJSONArray("Model Test Screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    questionAttempted = textArrayList.get(0);
                    totalTime = textArrayList.get(1);
                    welcomeBack = textArrayList.get(2);
                    start = textArrayList.get(3);
                    warning = textArrayList.get(4);
                    exitMessage = textArrayList.get(5);
                    continueTest = textArrayList.get(6);
                    exitTest = textArrayList.get(7);
                    unattemptedQLeft = textArrayList.get(8);
                    sureSubmit = textArrayList.get(9);
                    submit = textArrayList.get(10);
                    completeTest = textArrayList.get(11);
                    calculateScore = textArrayList.get(12);
                    greeting = textArrayList.get(13);
                    yourScore = textArrayList.get(14);
                    review = textArrayList.get(15);
                    goBack = textArrayList.get(16);
                    startMessage = textArrayList.get(17);
                    minutes = textArrayList.get(18);
                    attempted = textArrayList.get(19);
                    attempt = textArrayList.get(20);
                    resume = textArrayList.get(21);
                    submitTest = textArrayList.get(22);
                    imageInternet = textArrayList.get(23);
                    lastMessage = textArrayList.get(24);

                    textViewTime.setText(totalTime + " : " + duration + " " + minutes);

                }
                startModelTestpaper();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showDialog(context);
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Model Test Screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            questionAttempted = textArrayList.get(0);
                            totalTime = textArrayList.get(1);
                            welcomeBack = textArrayList.get(2);
                            start = textArrayList.get(3);
                            warning = textArrayList.get(4);
                            exitMessage = textArrayList.get(5);
                            continueTest = textArrayList.get(6);
                            exitTest = textArrayList.get(7);
                            unattemptedQLeft = textArrayList.get(8);
                            sureSubmit = textArrayList.get(9);
                            submit = textArrayList.get(10);
                            completeTest = textArrayList.get(11);
                            calculateScore = textArrayList.get(12);
                            greeting = textArrayList.get(13);
                            yourScore = textArrayList.get(14);
                            review = textArrayList.get(15);
                            goBack = textArrayList.get(16);
                            startMessage = textArrayList.get(17);
                            minutes = textArrayList.get(18);
                            attempted = textArrayList.get(19);
                            attempt = textArrayList.get(20);
                            resume = textArrayList.get(21);
                            submitTest = textArrayList.get(22);
                            imageInternet = textArrayList.get(23);
                            lastMessage = textArrayList.get(24);

                            textViewTime.setText(totalTime + " : " + duration + " " + minutes);
                        }

                        startModelTestpaper();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void openGifDialogue(String textError) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_toast_messages);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText("Oops! No Internet");
        text.setVisibility(View.GONE);

        TextView text_ = dialog.findViewById(R.id.text_);
        text_.setText(textError);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setVisibility(View.GONE);
        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(this)
                .load(R.raw.error)
                .into(imageViewGif);
        textViewRetry.setText("Try Again");
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

    public void finalSubmition() {
        if (Integer.parseInt(this.attemptedQuestions) == questionsArrayList.size()) {
            submitTestPaper();
            try {
                //syncScore();
            } catch (Exception e) {
                e.printStackTrace();
            }
            openDialog();
        } else {
            submitConfirmationCheck();
        }
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }


        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

//        @Override
//        public boolean canScrollHorizontally() {
//            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
//            return isScrollEnabled && super.canScrollHorizontally();
//        }
    }

    private class showPracticeOrTestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }

    /** scroll recycleview current time on the top*/
    public class SnapTopLinearLayoutManager extends GridLayoutManager {

        public SnapTopLinearLayoutManager(Context context, int count) {
            super(context, count);
        }

        @Override
        public void scrollToPositionWithOffset(int position, int offset) {
            super.scrollToPositionWithOffset(position, offset);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            SnapTopLinearSmoothScroller linearSmoothScroller =
                    new SnapTopLinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return SnapTopLinearLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    /** Smooth scroller for recycleview*/
    public abstract class SnapTopLinearSmoothScroller extends LinearSmoothScroller {
        public SnapTopLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }



    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;

    /** Clear foundation topic mastery | type = "foundation" or "practice" */
    private void clearMastery(String topicId,String type) {
        String date = Util.getCurrentDateWithDifferentFormat();
        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);
        if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), type, topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

            reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), type, topicId);

            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType(type);
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(1 + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery("0");
            reportsLatestDataPracticeModel.setStreakProgress(0 + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

        } else {
            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType(type);
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(1 + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery("0");
            reportsLatestDataPracticeModel.setStreakProgress(0 + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }


    }
    int progressStatus=0;

    ArrayList<String> result_text=new ArrayList<>();
    Drawable progressbar_drawable;

    private void getResult_text() {
        global.getDatabaseReference().child("screen_text/student/1/"+Util.getSelectedLanguage(context)+"/Diagnostic_result").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("-------- snapshot "+snapshot);
                if(snapshot!=null)
                {
                    result_text= (ArrayList<String>) snapshot.getValue();
                    System.out.println("===== result_text "+result_text);
                }
                else
                {
                    if(Util.getSelectedLanguage(context).equals("hindi"))
                    {
                        /** Hindi text need to be changed */

                        result_text.add(", In the Diagnostic test of ");
                        result_text.add(" your current learning level is ");
                        result_text.add("To master this chapter, you need to achieve two goals: ");
                        result_text.add("1.  Achieve 100% Mastery in the practice section");
                        result_text.add("2. Take the final test and score 80% or more for completion ");
                        result_text.add("To achieve this, iPrep PAL suggests you to either watch concept building videos as per the recommended learning level, or attempt practice questions as per the recommended learning level. iPrep shall recommend the next steps in your learning path as you continue to improve your understanding in this chapter.");
                        result_text.add("Happy Learning!");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("Score card");
                        result_text.add("Level");
                        result_text.add("out of");
                        result_text.add("correct answer");
                        result_text.add("Review your test");
                        result_text.add("Next Step");
                        result_text.add("Practice - Level");
                        result_text.add("or");
                        result_text.add("Watch video Lessons - Level");
                        result_text.add("Practice Foundation Topic");
                        result_text.add("Watch Foundation video Lessons");

                        global.getDatabaseReference().child("screen_text/student/1/"+Util.getSelectedLanguage(context)+"/Diagnostic_result").setValue(result_text);
                    }
                    else
                    {
                        result_text.add(", In the Diagnostic test of ");
                        result_text.add(" your current learning level is ");
                        result_text.add("To master this chapter, you need to achieve two goals: ");
                        result_text.add("1.  Achieve 100% Mastery in the practice section");
                        result_text.add("2. Take the final test and score 80% or more for completion ");
                        result_text.add("To achieve this, iPrep PAL suggests you to either watch concept building videos as per the recommended learning level, or attempt practice questions as per the recommended learning level. iPrep shall recommend the next steps in your learning path as you continue to improve your understanding in this chapter.");
                        result_text.add("Happy Learning!");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("");
                        result_text.add("Score card");
                        result_text.add("Level");
                        result_text.add("out of");
                        result_text.add("correct answer");
                        result_text.add("Review your test");
                        result_text.add("Next Step");
                        result_text.add("Practice - Level");
                        result_text.add("or");
                        result_text.add("Watch video Lessons - Level");
                        result_text.add("Practice Foundation Topic");
                        result_text.add("Watch Foundation video Lessons");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showResult(int percentage) {
        TextView score_percentage_text;
        LinearLayout result_layout;
        TextView mainText1,result_text2,point_text1,point_text2,result_text3,happy_learning_text;
        TextView score_card_Text,level_text,correct_questions_Text,review_text_test,nextStep_text;
        TextView practice_text,or_text,watch_video_text;
        ImageView direct_cross_res_img;
        Handler handler=new Handler();

        progressBar=findViewById(R.id.score_progressBar);
        result_layout=findViewById(R.id.result_layout);
        result_text2=findViewById(R.id.result_text2);
        point_text1=findViewById(R.id.point_text1);
        point_text2=findViewById(R.id.point_text2);
        result_text3=findViewById(R.id.result_text3);
        happy_learning_text=findViewById(R.id.happy_learning_text);
        mainText1=findViewById(R.id.mainText1);

        score_card_Text=findViewById(R.id.score_card_Text);
        correct_questions_Text=findViewById(R.id.correct_questions_Text);
        level_text=findViewById(R.id.level_text);
        review_text_test=findViewById(R.id.review_text_test);
        nextStep_text=findViewById(R.id.nextStep_text);

        practice_text=findViewById(R.id.practice_text);
        or_text=findViewById(R.id.or_text);
        watch_video_text=findViewById(R.id.watch_video_text);

        direct_cross_res_img=findViewById(R.id.direct_cross_res_img);

        progressStatus=0;

        result_layout.setVisibility(View.VISIBLE);
        mainText1.setText(Html.fromHtml(Util.getUsernameShowable(context)+result_text.get(0)+ "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>"+result_text.get(1).replace("→"," ")));
        result_text2.setText(result_text.get(2));
        point_text1.setText(result_text.get(3));
        point_text2.setText(result_text.get(4));
        result_text3.setText(result_text.get(5));
        happy_learning_text.setText(result_text.get(6));

        score_card_Text.setText(result_text.get(11));
        level_text.setText(result_text.get(12));

        if(Util.getSelectedLanguage(context).equals("hindi")) correct_questions_Text.setText(" 12 "+result_text.get(13)+""+ diagonosticTestAdapterMobile.scoreArrayList.size()+" "+result_text.get(14)); else
            correct_questions_Text.setText(diagonosticTestAdapterMobile.scoreArrayList.size()+" "+result_text.get(13)+" 12 "+result_text.get(14));


        review_text_test.setText(result_text.get(15));
        nextStep_text.setText(result_text.get(16));

        practice_text.setText(result_text.get(17));
        or_text.setText(result_text.get(18));
        watch_video_text.setText(result_text.get(19));

        score_percentage_text=findViewById(R.id.score_percentage_text);


        progressBar.setMax(100);
        progressBar.setProgress(progressStatus);


        int level = 1;

        /** Calculation......*/
        if (percentage >= 25 && percentage <= 100) {

            /** Score is more then 25%  So Show Current Topic Practice * Videos */
            int one = 0;
            int two = 0;
            int three = 0;
            int four = 0;
            for (int i = 0; i < diagonosticTestAdapterMobile.trackArrayList.size(); i++) {
                if (i >= 0 && i < 3) {
                    if (diagonosticTestAdapterMobile.trackArrayList.get(i).get("Status").equalsIgnoreCase("Correct")) {
                        one++;
                    }
                } else if (i >= 3 && i < 6) {
                    if (diagonosticTestAdapterMobile.trackArrayList.get(i).get("Status").equalsIgnoreCase("Correct")) {
                        two++;
                    }
                } else if (i >= 6 && i < 9) {
                    if (diagonosticTestAdapterMobile.trackArrayList.get(i).get("Status").equalsIgnoreCase("Correct")) {
                        three++;
                    }
                } else if (i >= 9 && i < 12) {
                    if (diagonosticTestAdapterMobile.trackArrayList.get(i).get("Status").equalsIgnoreCase("Correct")) {
                        four++;
                    }
                }
            }
            if (one <= 2) {
                level = 1;
            } else if (two <= 2) {
                level = 2;
            } else if (three <= 2) {
                level = 3;
            } else if (four <= 2) {
                level = 4;
            }
            Log.i("Level:", level + "");



            if (percentage==100) {
                level = 4;
                mainText1.setText(Html.fromHtml(result_text.get(7)+" "+Util.getUsernameShowable(context)+result_text.get(0)+ "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>"+result_text.get(1).replace("→"," ")));
            }

            Util.setUnlockVideoLevel(context,topicId,level);
            int finalLevel = level;
            level_text.setText(result_text.get(12)+" "+level);
            practice_text.setText(result_text.get(17));
            watch_video_text.setText(result_text.get(19)+" "+ level);

            /** Checking Current Topic Practice Available or not  */
            if (GetTopicLevelsDetails.isSameTopicAvailable()) {
                practice_text.setText(result_text.get(17));
                practice_text.setVisibility(View.VISIBLE);
            } else {
                practice_text.setVisibility(View.GONE);
            }

            /** Start Current Level Practice */
            practice_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    getSameLevelPractice();
                }
            });

            /** Watch Current Topic Videos */
            watch_video_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    if (Util.isNetworkAvailable(context))
                    {
                        String subject = Util.getSubject(context);
                        String subjectName = Util.getSubjectName(context);
                        String icon = PalContentListingActivity_Mobile.icon;
                        Intent intent = new Intent(context, PalTopicListingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Util.setVideoLevel(context, (finalLevel - 1));
                        Util.setLevel(context, (finalLevel));
                        Util.setUnlockVideoLevel(context,topicId,finalLevel);
                        PracticeTopicActivity.autoplayLevelVideo=true;
                        intent.putExtra("subject", subject);
                        intent.putExtra("sClass", sClass);
                        intent.putExtra("board", board);
                        intent.putExtra("subjectName", subjectName);
                        intent.putExtra("icon", icon);
                        intent.putExtra("getPath", "yes");
                        Util.setTopicID(context, topicId);
                        startActivity(intent);
                        
                        finish();
                    } else {
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                        {
                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                        }else {
                            Util.openGifDialogue(context,"Internet Connection is not working");
                        }
                    }
                }
            });

        }
        else if (percentage < 25) {
            level=1;
            Util.setUnlockVideoLevel(context,topicId,level);
            practice_text.setVisibility(View.VISIBLE);
            /** Score is less then 25%
             * First check Foundation topic is available or not */
            if (GetTopicLevelsDetails.isJuniorTopicAvailable()) {

                saveFoundationalDetails();

                int finalLevel = level;
                level_text.setText(result_text.get(12));
                practice_text.setText(result_text.get(20));
                watch_video_text.setText(result_text.get(21));

                /** Start Foundation Level Practice */
                practice_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        getJuniorLevelPractice();
                    }
                });

                /** Watch Current Topic Videos */
                watch_video_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        if (Util.isNetworkAvailable(context))
                        {
                            String subject = Util.getSubject(context);
                            String subjectName = Util.getSubjectName(context);
                            String icon = PalContentListingActivity_Mobile.icon;
                            Intent intent = new Intent(context, PalTopicListingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Util.setVideoLevel(context, 0);
                            Util.setLevel(context, 0);
                            PracticeTopicActivity.autoplayLevelVideo=true;
                            intent.putExtra("subject", subject);
                            intent.putExtra("sClass", sClass);
                            intent.putExtra("board", board);
                            intent.putExtra("subjectName", subjectName);
                            intent.putExtra("icon", icon);
                            intent.putExtra("getPath", "yes");
                            Util.setTopicID(context, topicId);
                            startActivity(intent);
                            
                            finish();
                        } else {
                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                            {
                                Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                            }else {
                                Util.openGifDialogue(context,"Internet Connection is not working");
                            }
                        }
                    }
                });


            } else {


                int finalLevel = level;
                level_text.setText(result_text.get(12)+" "+level);
                practice_text.setText(result_text.get(17));
                watch_video_text.setText(result_text.get(19)+" "+ level);

                /** Checking Current Topic Practice Available or not  */
                if (GetTopicLevelsDetails.isSameTopicAvailable()) {
                    practice_text.setText(result_text.get(17));
                    practice_text.setVisibility(View.VISIBLE);
                } else {
                    practice_text.setVisibility(View.GONE);
                }

                /** Start Current Level Practice */
                practice_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        getSameLevelPractice();
                    }
                });

                /** Watch Current Topic Videos */
                watch_video_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        if (Util.isNetworkAvailable(context))
                        {
                            String subject = Util.getSubject(context);
                            String subjectName = Util.getSubjectName(context);
                            String icon = PalContentListingActivity_Mobile.icon;
                            Intent intent = new Intent(context, PalTopicListingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Util.setVideoLevel(context, (finalLevel - 1));
                            Util.setLevel(context, (finalLevel));
                            Util.setUnlockVideoLevel(context,topicId,finalLevel);
                            PracticeTopicActivity.autoplayLevelVideo=true;
                            intent.putExtra("subject", subject);
                            intent.putExtra("sClass", sClass);
                            intent.putExtra("board", board);
                            intent.putExtra("subjectName", subjectName);
                            intent.putExtra("icon", icon);
                            intent.putExtra("getPath", "yes");
                            Util.setTopicID(context, topicId);
                            startActivity(intent);
                            
                            finish();
                        } else {
                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                            {
                                Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                            }else {
                                Util.openGifDialogue(context,"Internet Connection is not working");
                            }
                        }
                    }
                });

            }
//            openFailsDialog();
        }

        progressbar_drawable= context.getResources().getDrawable(R.drawable.circular_progress_bar_red);

        /** animate Progress bar & score text */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < percentage) {
                    progressStatus ++;
                    android.os.SystemClock.sleep(30);
                    handler.post(new Runnable() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void run() {
                            score_percentage_text.setText(progressStatus+"%");
                            if(progressStatus<=25)
                            {
                                progressbar_drawable=context.getResources().getDrawable(R.drawable.circular_progress_bar_red);

                            }
                            else if(progressStatus<=50)
                            {
                                progressbar_drawable=context.getResources().getDrawable(R.drawable.circular_progress_bar_yellow);
                            }
                            else if(progressStatus<=75)
                            {
                                progressbar_drawable=context.getResources().getDrawable(R.drawable.circular_progress_bar_blue);
                            }
                            else
                            {
                                progressbar_drawable=context.getResources().getDrawable(R.drawable.circular_progress_bar_green);
                            }
                            progressBar.setProgressDrawable(progressbar_drawable);
                            progressBar.setProgress(progressStatus);
                        }
                    });

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgressDrawable(progressbar_drawable);
                        progressBar.setProgress(progressStatus-1);
                        progressBar.setProgress(progressStatus);
//                        loading.setText("COMPLETE");
                    }
                });
            }
        }).start();


        review_text_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, TestReviewActivity.class);
                TestReviewActivity.questionsArrayList= diagonosticTestAdapterMobile.questionsArrayList;
                context.startActivity(intent);

//                ((TestReviewActivity)context).questionsArrayList=questionsArrayList;

            }
        });

        direct_cross_res_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PalContentListingActivity_Mobile.backPressed = true;
                finish();
            }
        });

        result_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----");
            }
        });

    }

}
