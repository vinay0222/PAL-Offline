package com.idreameducation.ipreppal.PalMobile.activity;

import static com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.viewPagerPos;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.autoplayLevelVideo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.textToSend;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AnalyticModel;
import com.idreameducation.ipreppal.model.ChatModel;
import com.idreameducation.ipreppal.model.ContentErrorModel;
import com.idreameducation.ipreppal.model.LevelVideoModel;
import com.idreameducation.ipreppal.model.MasteryNodeModel;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.pal.activity.GetTopicLevelsDetails;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalFullScreenVideoActivity;
import com.idreameducation.ipreppal.pal.activity.PalGlobalSearchActivity;
import com.idreameducation.ipreppal.pal.activity.SelectTopicActivity;
import com.idreameducation.ipreppal.pal.activity.VideoView_Activity;
import com.idreameducation.ipreppal.pal.adapter.VideoLevelAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWisePracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicPathModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWisePracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDateWisePracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicPathRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWisePracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TrackTopicRepository;
import com.idreameducation.ipreppal.services.SocketService;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.FullScreenMediaController;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity;

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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class QuizActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public static TextView textViewSubmit;
    public static TextView textViewSkip;
    public static QuizActivity instance;
    ArrayList<HashMap<String, Object>> masterArrayListToSync = new ArrayList<>();
    HashMap<String, Object> objectHashMap = new HashMap<>();
    private long MillisecondTime;
    private long TimeBuff;
    private final long UpdateTime = 0L;
    private final long timeInMilliseconds = 0L;
    private final long timeSwapBuff = 0L;
    private final long updatedTime = 0L;
    /* Activity context Declaration*/
    private Context context;
    /* Application class Declaration*/
    private Global global;
    /* UI Declaration TextView*/
    private LinearLayout linearFullscreen1;
    private LinearLayout linearFullscreen2;
    private LinearLayout linearFullscreen3;
    private LinearLayout linearFullscreen4;
    private TextView textViewQuestion;
    private TextView textViewOption1;
    private TextView textViewOption2;
    private TextView downloadingTextView;
    private TextView textViewOption3;
    private TextView textViewOption4;
    private TextView NoInternetConnectionTextView;
    private TextView option4NoInternet;
    private TextView option3NoInternet;
    private TextView option2NoInternet;
    private TextView option1NoInternet;
    private TextView textViewReadMore;
    private TextView btnChangeLanguageEnglish;
    private TextView btnChangeLanguage;
    private TextView topicNameTextView;

    private EditText messageEditText;
    /* UI Declaration ImageView*/
    private ImageView questionImageView;
    private LinearLayout linearFullImage1;
    private LinearLayout linearFullImage2;
    private LinearLayout linearFullImage3;
    private LinearLayout linearFullImage4;
    private ImageView imageViewOption1FullImage;
    private ImageView imageViewOption2FullImage;
    private ImageView imageViewOption3FullImage;
    private ImageView imageViewOption4FullImage;
    private ImageView imageViewOption1;
    private ImageView imageViewOption2;
    private ImageView imageViewOption3;
    private ImageView imageViewOption4;
    private ImageView imageViewClose;
    /* UI Declaration RelativeLayout*/
    private RelativeLayout reletiveLayoutAnimate;
    private RelativeLayout languageLayout;
    /* UI Declaration Progress Bar*/
//    private RoundedHorizontalProgressBar progress;
    private AppCompatSeekBar progress;
    /* Boolean Variables */
    private boolean isQuestionLoaded = false;
    private final boolean isFirstTopicActive = false;
    private boolean isAnswerCorrect = false;
    private boolean isQuestionAttempted = false;
    private final boolean isShareDialogOpen = false;
    private boolean newQuestionLoad = true;
    private boolean isIncorrectStreakClicked = true;
    private boolean isCorrectStreakClicked = true;
    private boolean isClosePoped = false;
    private final boolean isKillEnable = false;
    private final boolean isFoundationStarted = false;
    private final boolean isFoundationArrayRun = false;
    /* String Variables */
    private String selectedLanguage;
    private String correct_feedback_alt;
    private String correct_feedback;
    private String questionIcon;
    private String feedbackImage;
    private String incorrect_feedback;
    private String incorrect_feedback_alt;
    private String questionID;
    private String choosenOption;
    private String sessionDateTime;
    private String topicId;
    private String topicName;
    private String facilitatorId;

    private String current_topicId;
    private String closeMidway;
    private String facebookInstallMessage;
    private String message;
    private String completeQuiz;
    private String closeAnyways;
    private String incorrectMessage;
    private String okay;
    private String correctMessage;
    private String endMessage;
    private String waitString;
    private String goBack;
    private String weldone;
    private String emptyMessage;
    private String greatAttempt;
    private String tenMessage;
    private String share;
    private String comingSoon;
    private String time = "0";
    private String switchTopic;
    private String exit;
    private String enterMessage;
    private String chatBox;
    private String topicMaster;
    private String message1;
    private String message2;
    private String message3;
    private String chooseAnotherTopic;
    private String seniorTopicID;
    private String seniorTopicName;
    private String seniorClass;
    /* int4 Variables */
    private int streak;
    private String isAssigned;
    private String batchId;
    private String keyTo;
    private String datetostore;
    private String teacherID;
    private int tenQuestionCheck = 0;
    private int incorrectStreak;
    private int questionNo;
    private int QuestionNumberAfterConfigChange;
    private int level;
    private int corectAnswer = 0;
    private final int answerCorrect = 0;
    /* Float Variables */
    private float streakProgress = 0;
    private float streakDeProgress = 0;
    private float totalNoOfLevel = 0;
    /* ArrayList variables to manipulate data */
    private ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList;
    private ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayListHindi;
    private ArrayList<HashMap<String, HashMap<String, String>>> questionArrayList = new ArrayList<>();
    private final ArrayList<String> stackArrayList = new ArrayList<>();
    private ArrayList<Integer> stackPositionArrayList;
    /* Facebook share dialog to share image on facebook*/
    private ShareDialog shareDialog;
    private RecyclerView mRecyclerView;
    private RelativeLayout chatLayout;
    private TextView textViewChatBox;
    private TextView masteryTextView;
    private Handler handler;
    private Dialog dialogToDismiss;
    private int testPercentageAchieved;
    private TextView textViewdMessaeg;
    private int Seconds, Minutes, MilliSeconds;
    private ImageView hintImageView;
    private LinearLayout hint_btn;
    private int hintCount = 0;
    private boolean hintRefresh = false;
    private boolean optionSelected = false;
    private int skipCount = 0;
    private Handler hintHandler;
    private TextView textViewNext;
    //    public Runnable runnable = new Runnable() {
//        public void run() {
//            MillisecondTime = (SystemClock.uptimeMillis() - StartTime);
//            UpdateTime = TimeBuff + MillisecondTime + Long.parseLong(time);
////            time = String.valueOf(UpdateTime);
//            Seconds = (int) (UpdateTime / 1000);
//            Minutes = Seconds / 60;
//            Seconds = Seconds % 60;
//            MilliSeconds = (int) (UpdateTime % 1000);
//            handler.postDelayed(this, 1000);
//        }
//
//    };
    private ImageView sendButton;
    private ImageView imageViewBack;
    private ImageView chatImageView;
    private ProgressBar progressBar3;
    private ImageView askforhelpImageView;
    private String readMore;
    private String restartMessage;
    private String restartMessage1;
    private String restartMessage2;
    private String howItWork;
    private String error;
    private String download;
    private String checkMessage1;
    private String checkMessage2;
    private String checkMessage3;
    private String checkMessage4;
    private String report;
    private String imageInternetError;
    private boolean isChecked;
    private String currentMastery;
    private String shortLink;
    private String tourMessage;
    /* Content Error Reporting Dialog*/
    private String selectOptionMessage;
    private String feedbackSuccess;
    private String reason1 = "";
    private String reason2 = "";
    private String reason3 = "";
    private String reason4 = "";
    //    private long startTime = 0L;
    //  private android.os.Handler customHandler = new android.os.Handler();
//    private long totalTime;
//    private long classTime;
//    private Runnable updateTimerThread = new Runnable() {
//        public void run() {
//            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
//            updatedTime = timeSwapBuff + timeInMilliseconds;
//            int secs = (int) (updatedTime / 1000);
//            int mins = secs / 60;
//            secs = secs % 60;
//            customHandler.postDelayed(this, 1);
//        }
//    };
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private final String board = "cbse";
    //*****                   END                    *****//
    //*****                   END                    *****//
    private boolean isConfigChanged = false;
    //*****                   END                    *****//
    //*****                                          *****//
    //*****     Dilaog code for the Activity         *****//
    //*****                                          *****//
    //*****                   END                    *****//
    private String practiceType;
    private String sClass;
    //*****                                          *****//
    //*****     Calculate Mastery algorithim         *****//
    //*****                                          *****//
    private int masteryTobeSynced = 0;
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private RelativeLayout reletiveTop;
    private RelativeLayout reletiveMasteryfailed;
    private RelativeLayout reletiveImageView;
    private RelativeLayout reletiveMasteryCompleted;
    private ImageView imageViewCross;
    private ImageView imageViewCrossfailed;

    private TextView info;
    private Intent serviceIntent;
    private FoundationalTopicRepository foundationalTopicRepository;
    private ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;
    private TrackTopicRepository trackTopicRepository;
    private ReportsTopicPathRepository reportsTopicPathRepository;
    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWisePracticeRepository reportsDateWisePracticeRepository;
    private ReportsTopicWisePracticeRepository reportsTopicWisePracticeRepository;
    private Disposable practiceCountTaskDisposable;

    private String videoLink;
    private String videoName;
    private String videoLink_Diksha;

    private int practiceCount = 0;
    private TextView textViewPractice;

    String trackTopic = null;
    String trackClass = null;
    private long startTime = 0L;
    private long endTime = 0L;
    private long practiceNumber;
    private long timeget;
    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler1=new Handler();
    ProgressBar mProgressBar;
    LinearLayout loading_layout;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;
    private Uri filePath;
    private String linkFromFirebaseStorage,textSendWithImageTextString;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;
    boolean attemptingtest=false;

    ImageView level1_icon,level2_icon,level3_icon,level4_icon;

    View video_layout_background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setContext(context);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_question);
        Util.handleNotch(this);
        /* Hookup ids with UI elements */
        assignIds(savedInstanceState);
        /* Add click listners on UI elements */
        listners();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizActivity that = (QuizActivity) o;
        return stackArrayList.equals(that.stackArrayList);
    }

    @Override
    public int hashCode() {
        return stackArrayList.hashCode();
    }

    private void checkConnection(boolean first) {

        mProgressBar=findViewById(R.id.progressBar);
        slow_internet_Text=findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=findViewById(R.id.imageViewCrossVideo2);
        loading_layout=findViewById(R.id.loading_layout);
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
        }
        else
        {
            hideconnection_layout();
        }

        imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loading_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println( "-----");
            }
        });

    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        loading_layout.setVisibility(View.GONE);
    }

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
        textViewMessge2.setText(Html.fromHtml(heading2Text));
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

        FirebaseDatabase.getInstance().getReference().child("/screen_text/student/1/").child(Util.getSelectedLanguage(context))
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
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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

        //




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
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
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
                                            Uri downloadUri =  uri;
                                            linkFromFirebaseStorage = String.valueOf(downloadUri);
                                            sendDataToFirebaseOfIssueReported();
                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
//                                    Toast.makeText(com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();



                                    Util.openGifDialogueSuccess(context, textToSend);
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
//                                    .makeText(com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.this,
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

    /*Listners on the required elements*/
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


        imageViewOption2FullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                } else {
                    global.setFullImage(image2);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });

        imageViewOption1FullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                } else {
                    global.setFullImage(image1);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });

        linearFullImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                } else {
                    global.setFullImage(image1);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });
        linearFullImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                } else {
                    global.setFullImage(image2);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });
        linearFullImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                } else {
                    global.setFullImage(image3);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });
        linearFullImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {


                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                } else {
                    global.setFullImage(image4);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });

        imageViewOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 3) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 3";

                /*Set background to the answers*/
                textViewOption3.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                /*Set text color of the answers*/
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));


                /*Set background to the answers*/
                linearFullscreen3.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);
            }
        });

        imageViewOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (corectAnswer == 2) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 2";

                /*Set background to the answers*/
                textViewOption2.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                /*Set text color of the answers*/
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));

                /*Set background to the answers*/
                linearFullscreen2.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);
            }
        });

        questionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {
                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" +questionIcon);
                } else {
                    global.setFullImage(questionIcon);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));
            }
        });

        imageViewOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 1) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 1";

                /*Set background to the answers*/
                textViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                /*Set text color of the answers*/
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));

                /*Set background to the answers*/
                linearFullscreen1.setBackgroundResource(R.drawable.correct_answer_new);
//                imageViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);

            }
        });

        findViewById(R.id.option4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 4) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 4";
                /*Set background to the answers*/
                textViewOption4.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);

                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_selected, 0, 0, 0);


                /*Set text color of the answers*/
//                textViewOption4.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));

                linearFullscreen4.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);

                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                textViewSubmit.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        findViewById(R.id.option3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 3) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 3";
                /*Set background to the answers*/
                textViewOption3.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_selected, 0, 0, 0);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);


                /*Set text color of the answers*/
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));
                linearFullscreen3.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);

                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                textViewSubmit.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        findViewById(R.id.option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 2) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 2";
                /*Set background to the answers*/
                textViewOption2.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_selected, 0, 0, 0);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);


                /*Set text color of the answers*/
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));

                linearFullscreen2.setBackgroundResource(R.drawable.correct_answer_new);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);

                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                textViewSubmit.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        findViewById(R.id.option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 1) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 1";
                /*Set background to the answers*/
                textViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_selected, 0, 0, 0);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);

                /*Set text color of the answers*/
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption4.setTextColor(Color.parseColor("#000000"));

                linearFullscreen1.setBackgroundResource(R.drawable.correct_answer_new);
//                imageViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);

                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                textViewSubmit.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        imageViewOption4FullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {
                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                } else {
                    global.setFullImage(image4);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));

            }
        });

        imageViewOption3FullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOfflineMode(context)) {
                    global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                } else {
                    global.setFullImage(image3);
                }
                context.startActivity(new Intent(context, FullProfileImageActivity.class));

            }
        });


        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQuestionLoaded = true;
                selectedLanguage = "English";//"Hindi";
                Util.setQuestionLanguageSelection(context, selectedLanguage);
                btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
                btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
                btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
                btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
                newQuestionLoad = true;
                try {
                    dataManipulation(contentArrayListHindi, selectedLanguage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnChangeLanguageEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQuestionLoaded = true;
                selectedLanguage = "English";
                newQuestionLoad = true;
                Util.setQuestionLanguageSelection(context, selectedLanguage);
                btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
                btnChangeLanguage.setBackgroundResource(R.drawable.signup);

                btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
                btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));

                try {
                    dataManipulation(contentArrayList, selectedLanguage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        hint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHintDialog(correct_feedback, 1);
            }
        });

        hintImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHintDialog(correct_feedback, 1);
            }
        });

        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSkipPopUp();
            }
        });

        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Answer submission Checks */
                if (isQuestionAttempted) {

                    attemptingtest=true;

                    if (selectedLanguage.equalsIgnoreCase("English")) {
                        if (isAnswerCorrect) {
                            showCorrectFeedbackDialog(correct_feedback, 0);
                        } else {
                            showIncorrectFeedbackDialog(incorrect_feedback);
                        }
                    } else {
                        if (global.isFlagEnabled()) {
                            if (isAnswerCorrect) {
                                showCorrectFeedbackDialog(correct_feedback_alt, 0);
                            } else {
                                showIncorrectFeedbackDialog(incorrect_feedback_alt);
                            }
                        } else {
                            if (isAnswerCorrect) {
                                showCorrectFeedbackDialog(correct_feedback, 0);
                            } else {
                                showIncorrectFeedbackDialog(incorrect_feedback);
                            }
                        }
                    }
                    skipCount = 0;
                } else {
//                    showSnackBar(emptyMessage);
                    textViewSubmit.setEnabled(false);
//                    Util.showToast(context, emptyMessage);

                    if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"सबमिट करने के लिए कृपया एक विकल्प चुनें");
//                        Util.showToast(context, "सबमिट करने के लिए कृपया एक विकल्प चुनें");
                    }else {
//                        Util.showToast(context, "Please select an option to submit");
                        Util.openGifDialogue(context,"Please select an option to submit");
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textViewSubmit.setEnabled(true);
                        }
                    }, 2000);

                }
                textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
            }
        });

        imageViewOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corectAnswer == 4) {
                    correctAnswer();
                } else {
                    incorrectAnswer();
                }
                choosenOption = "Option 4";
                /*Set background to the answers*/
                textViewOption4.setBackgroundResource(R.drawable.correct_answer_new);
                textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                /*Set text color of the answers*/
//                textViewOption4.setTextColor(Color.parseColor("#000000"));
//                textViewOption2.setTextColor(Color.parseColor("#000000"));
//                textViewOption3.setTextColor(Color.parseColor("#000000"));
//                textViewOption1.setTextColor(Color.parseColor("#000000"));
                /*Set background to the answers*/
                linearFullscreen4.setBackgroundResource(R.drawable.correct_answer_new);
                linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
                linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
//                imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
            }
        });

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatLayout.setVisibility(View.GONE);
            }
        });

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseMidwayDialog();
            }
        });

        imageViewCrossfailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseMidwayDialog();
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Util.showToast(context, enterMessage);
                } else {
                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(message);
                    chatModel.setUserType("Student");
                    chatModel.setMessageType("Showable");
                    chatModel.setTo(facilitatorId);
                    chatModel.setFrom(Util.getUserId(context));
                    chatModel.setTime(Util.getCurrentDateWithDifferentFormat() + "-" + Util.getTimeAmPm());

                }
            }
        });

        askforhelpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWelcomeMessages();
            }
        });

        chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatLayout.setVisibility(View.VISIBLE);
            }
        });


    }

    /*Hook up ids and Intialization os variables*/
    @SuppressLint("RestrictedApi")
    private void assignIds(Bundle savedInstanceState) {
        /* Initalise the current activity context */
        context = this;
        instance = this;
        /* initialise the handler.*/
        handler = new Handler();
        /* Initalise the current Application context */
        global = (Global) getApplicationContext();
        global.sendData("Quiz", this.getClass().getName());
        database = FirebaseDatabase.getInstance();

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);
        levelVideosRecyclerview = findViewById(R.id.levelVideosRecyclerview);
        video_layout = findViewById(R.id.video_layout);
        cross_video_ly_btn = findViewById(R.id.cross_video_ly_btn);
        videos_btn = findViewById(R.id.videos_btn);
        video_layout_background = findViewById(R.id.video_layout_background);

        level1_icon=findViewById(R.id.level1_icon);
        level2_icon=findViewById(R.id.level2_icon);
        level3_icon=findViewById(R.id.level3_icon);
        level4_icon=findViewById(R.id.level4_icon);

        practice_video_container=findViewById(R.id.practice_video_container);

        callText.setVisibility(View.GONE);
        screenshotText.setVisibility(View.GONE);
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reportsDiagnosticTestCompleteRepository = new ReportsDiagnosticTestCompleteRepository(context);
        foundationalTopicRepository = new FoundationalTopicRepository(context);
        reportsTopicPathRepository = new ReportsTopicPathRepository(context);
        trackTopicRepository = new TrackTopicRepository(context);
        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsDateWisePracticeRepository = new ReportsDateWisePracticeRepository(context);
        reportsTopicWisePracticeRepository = new ReportsTopicWisePracticeRepository(context);

        databaseReference = database.getReference();
        startTime = System.currentTimeMillis();
        /* get Current session date and time to send in analytics */
        sessionDateTime = "Session : " + Util.getCurrentDate();
        /* getting Topic ID from Application level class*/
        topicId = Util.getTopicID(context);
        topicName = Util.getTopicNameAlt(context);
        current_topicId = Util.getTopicID(context);
        streak = Integer.parseInt(getIntent().getStringExtra("streak"));
        isAssigned = getIntent().getStringExtra("isAssigned");
        batchId = getIntent().getStringExtra("batchId");
        keyTo = getIntent().getStringExtra("key");
        datetostore = getIntent().getStringExtra("date");
        teacherID = getIntent().getStringExtra("teacherID");
        type = getIntent().getStringExtra("type");

        if(type==null)
        {
            type="practice";
        }

        incorrectStreak = Integer.parseInt(getIntent().getStringExtra("incorrectStreak"));

        try {
            if (getIntent().getStringExtra("streakProgress") != null) {
                streakProgress = Float.parseFloat(getIntent().getStringExtra("streakProgress"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            practiceType = getIntent().getStringExtra("practiceType");
            seniorTopicID = getIntent().getStringExtra("seniorTopicID");
            seniorTopicName = getIntent().getStringExtra("seniorTopicName");
            seniorClass = getIntent().getStringExtra("seniorClass");
            sClass = getIntent().getStringExtra("sClass");
            try {
                testPercentageAchieved = getIntent().getIntExtra("testPercentageAchieved", 0);
                System.out.println("------ testPercentageAchieved "+testPercentageAchieved);

            }catch (Exception r)
            {
                testPercentageAchieved=0;
            }
            if (sClass == null) {
                sClass = Util.getSelectedClassName(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sClass = Util.getSelectedClassName(context);
        }

        /* Get current level from the Application class which we saved in previous class Adapter */
        level = Util.getLevel(context);
        tempLevel=level;
        getLevelVideos();
        selectedLanguage = "English";
        // hideNavigationBar(getWindow());
        Util.setQuestionLanguageSelection(context, selectedLanguage);
        /*Hookup Ids*/
        progress = findViewById(R.id.progress);
        progress.setOnTouchListener((view, motionEvent) -> true);
        mRecyclerView = findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageViewBack = findViewById(R.id.imageViewBack);
        askforhelpImageView = findViewById(R.id.askforhelpImageView);
        masteryTextView = findViewById(R.id.masteryTextView);
        progressBar3 = findViewById(R.id.progressBar3);
        downloadingTextView = findViewById(R.id.downloadingTextView);
        reletiveImageView = findViewById(R.id.reletiveImageView);
        progressBar3.setVisibility(View.GONE);
        downloadingTextView.setVisibility(View.GONE);
        reletiveImageView.setVisibility(View.GONE);
        messageEditText = findViewById(R.id.messageEditText);
        textViewChatBox = findViewById(R.id.textViewChatBox);
        textViewSubmit = findViewById(R.id.textViewSubmit);
        textViewSkip = findViewById(R.id.textViewSkip);
        hintImageView = findViewById(R.id.hintImageView);
        hint_btn = findViewById(R.id.hint_btn);
        hint_btn.setVisibility(View.GONE);
        showHintOption();
        sendButton = findViewById(R.id.sendButton);
        imageViewClose = findViewById(R.id.imageViewClose);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        linearFullscreen1 = findViewById(R.id.linearFullscreen1);
        linearFullscreen2 = findViewById(R.id.linearFullscreen2);
        linearFullscreen3 = findViewById(R.id.linearFullscreen3);
        linearFullscreen4 = findViewById(R.id.linearFullscreen4);
        textViewReadMore = findViewById(R.id.textViewReadMore);

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");
        textViewQuestion.setTypeface(myTypeface);
        textViewOption1 = findViewById(R.id.option1);
        textViewOption2 = findViewById(R.id.option2);
        reletiveTop = findViewById(R.id.reletiveTop);
        info = findViewById(R.id.info);
        textViewdMessaeg = findViewById(R.id.textViewdMessaeg);
        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
//        info.setText(Html.fromHtml("You have Mastered " + topicName + ", and Unlocked the Test"));
//        info.setText("You have Mastered " + Util.getTopicNameAlt(context) + ", and Unlocked the Test");
        info.setText(
                "१००% मास्टरी प्राप्त करनी होगी इस पाठ के टेस्ट को क्लियर करना होगा \n" +
                        "इस विषय में १००% मास्टरी करके आपने इसकी परीक्षा को अनलॉक कर लिया हैं ");
        textViewdMessaeg.setText("बहुत बढ़िया\n" + Util.getUsername(context));
        reletiveMasteryCompleted = findViewById(R.id.reletiveMasteryCompleted);
        reletiveMasteryfailed = findViewById(R.id.reletiveMasteryfailed);
        reletiveLayoutAnimate = findViewById(R.id.reletiveLayoutAnimate);

        imageViewCross = findViewById(R.id.imageViewCross);
        imageViewCrossfailed = findViewById(R.id.imageViewCrossfailed);
        languageLayout = findViewById(R.id.languageLayout);
        chatImageView = findViewById(R.id.chatImageView);
        chatLayout = findViewById(R.id.chatLayout);
        textViewOption3 = findViewById(R.id.option3);
        textViewOption4 = findViewById(R.id.option4);
        topicNameTextView = findViewById(R.id.topicName);
        btnChangeLanguageEnglish = findViewById(R.id.btnChangeLanguageEnglish);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        //  btnChangeLanguage.setText("Hindi");
        questionImageView = findViewById(R.id.questionImageView);
        NoInternetConnectionTextView = findViewById(R.id.NoInternetConnectionTextView);
        option4NoInternet = findViewById(R.id.option4NoInternet);
        option3NoInternet = findViewById(R.id.option3NoInternet);
        option2NoInternet = findViewById(R.id.option2NoInternet);
        option1NoInternet = findViewById(R.id.option1NoInternet);

        linearFullImage1 = findViewById(R.id.linearFullImage1);
        linearFullImage2 = findViewById(R.id.linearFullImage2);
        linearFullImage3 = findViewById(R.id.linearFullImage3);
        linearFullImage4 = findViewById(R.id.linearFullImage4);
        imageViewOption1FullImage = findViewById(R.id.imageViewOption1FullImage);
        imageViewOption2FullImage = findViewById(R.id.imageViewOption2FullImage);
        imageViewOption3FullImage = findViewById(R.id.imageViewOption3FullImage);
        imageViewOption4FullImage = findViewById(R.id.imageViewOption4FullImage);
        imageViewOption1 = findViewById(R.id.imageViewOption1);
        imageViewOption2 = findViewById(R.id.imageViewOption2);
        imageViewOption3 = findViewById(R.id.imageViewOption3);
        imageViewOption4 = findViewById(R.id.imageViewOption4);
        /* set progress mastery */
        progress.setProgress(global.getProgress());
        updateLevel(global.getProgress());
        if (Util.getSubject(context).equalsIgnoreCase("English")) {
            languageLayout.setVisibility(View.GONE);
        } else {
            languageLayout.setVisibility(View.GONE);
        }

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("english"))
        {
            textViewSkip.setText("Skip question");
            textViewSubmit.setText("Submit answer");
        }else {
            textViewSkip.setText("प्रश्न छोड़ें");
            textViewSubmit.setText("उत्तर सबमिट करें");
        }

        textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
        textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));

        getResult_text();
        setStaticText();
        get_support_dialog_language();

        try {
            getQuestions(topicId);
            getTrackTopic(topicId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!global.isFlagEnabled()) {
            selectedLanguage = "English";
            Util.setQuestionLanguageSelection(context, selectedLanguage);
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguage.setBackgroundResource(R.drawable.signup);

            btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
            languageLayout.setVisibility(View.GONE);
        } else {
            languageLayout.setVisibility(View.GONE);
        }

        if ("Hindi".equalsIgnoreCase("Hindi")) {
            languageLayout.setVisibility(View.GONE);
        }
        if (Util.getSubject(context).equalsIgnoreCase("English")) {
            languageLayout.setVisibility(View.GONE);
        }

        if (selectedLanguage.equalsIgnoreCase("English")) {
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguage.setBackgroundResource(R.drawable.signup);

            btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
        } else {

            btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);

            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
        }

        // getClassTime();
        // getTotalTime();
        // getTime();

//        com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.instance.setPath(board, sClass, Util.getSubject(context), topicId, "P");

        videos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                video_layout.setVisibility(View.VISIBLE);
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
                Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                video_layout.startAnimation(animation);
                video_layout.setVisibility(View.VISIBLE);

                video_layout_background.setVisibility(View.VISIBLE);
//                video_layout_background.startAnimation(animation);
//                video_layout_background.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        video_layout.clearAnimation();
                        video_layout_background.clearAnimation();
                    }
                },400);

            }
        });

        cross_video_ly_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Util.preventTwoClick(v);
//                onSlideViewButtonClick();
                removeVideoLayout();
                video_layout.setVisibility(View.GONE);
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);
                video_layout.startAnimation(animation);
                video_layout.setVisibility(View.GONE);
                video_layout_background.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        video_layout.clearAnimation();
                    }
                },400);

//                video_layout.clearAnimation();
            }
        });

        video_layout_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cross_video_ly_btn.performClick();
            }
        });

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child(topicId).child("practice").setValue("P");

        video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----");
            }
        });

        topicNameTextView.setText(Util.getTopicNameAlt(context));

        GetTopicLevelsDetails.task(context, sClass, topicId, global, board, reportsDiagnosticTestCompleteRepository);

        getTime();

    }

    private void masteryCompleteDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_mastery_complete);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(restartMessage);
        TextView textViewokay = dialog.findViewById(R.id.textViewokay);
        textViewokay.setText(restartMessage1);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewCancel.setText(restartMessage2);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                finish();
                PalContentListingActivity_Mobile.backPressed = true;

                dialog.dismiss();
            }
        });
        textViewokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                level = 1;
                tempLevel=1;
                streakProgress=0;
                SelectTopicActivity.currentTopicMasteryChanged = "0";
                calculateMastery();
                getLevelVideos();
                sendAnalyticsMasteryNodeToFireBase("0");
                dialog.dismiss();
                try {
                    getQuestions(topicId);
//                    getQuestionsHindi(topicId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    boolean isResultPage = false;

    @Override
    public void onBackPressed() {

        if(isResultPage) {
            PalContentListingActivity.backPressed = true;
            /* Calling super to finish the activity */
            super.onBackPressed();
        }
        else {
            if (!ApplicationConstants.IS_SNACK_BAR_RUNNING) {
                if (!isClosePoped) {
                    isClosePoped = true;
                    /* Showing confirmation dialog to confirm to the user that user really want to exit or want to continoue with the quiz*/
                    showExitDialog();
                } else {
                    PalContentListingActivity.backPressed = true;
                    /* Calling super to finish the activity */
                    super.onBackPressed();
                    /* Animate the activity transition */

                }
            }
        }


    }

    /* get questions from firebase */
    private void getQuestions(final String topicId) throws Exception {

        isloading=true;
        checkConnection(false);
        contentArrayList = new ArrayList<>();
        String filePath;
        if (Util.getSelectedLanguage(context).equals("english"))
        {
            filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "questions.txt";
        }else {
            filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "Hindi_questions.txt";
        }

        File file = new File(Util.getSDCardPath(context) + "/" + filePath);
        if (file.exists()) {
            JSONObject jsonObject = Util.readJsonFile(context, filePath);

            isloading=false;
            hideconnection_layout();
            try {
                JSONArray array = jsonObject.getJSONArray(topicId);
                HashMap<String, HashMap<String, String>> innerHashMap = null;
                HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;
                HashMap<String, String> finalMap = new HashMap<>();
                int length_ = array.length();
                for (int i = 0; i < array.length(); i++) {
                    coreOuterHashMap = new HashMap<>();
                    outerHashMap = new HashMap<>();
                    JSONArray jsonArray = array.getJSONArray(i);
                    int length = jsonArray.length();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        innerHashMap = new HashMap<>();
                        finalMap = new HashMap<>();
                        JSONObject aa = jsonArray.getJSONObject(j);
                        Iterator<String> iterator = aa.keys();
                        String key = "";
                        while (iterator.hasNext()) {
                            key = iterator.next();
                        }
                        JSONObject jsonObject1 = aa.getJSONObject(key);
                        String q = jsonObject1.getString("q");
                        String videoLink = jsonObject1.getString("videoLink");
                        String videoName = jsonObject1.getString("videoName");
                        String videoLink_Diksha = null;
                        if (jsonObject1.has("videoLink_Diksha")) {
                            videoLink_Diksha = jsonObject1.getString("videoLink_Diksha");
                        }

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
                        finalMap.put("videoLink", videoLink);
                        finalMap.put("videoLink_Diksha", videoLink_Diksha);
                        finalMap.put("videoName", videoName);

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
                    dataManipulation(contentArrayList, selectedLanguage);
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

                System.out.println("======= topicId "+topicId);

                global.getDatabaseReference().child("QuDB").child(Util.getSelectedBoard(context)).child(board).child(sClass).child(topicId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            HashMap<String, HashMap<String, String>> innerHashMap = null;
                            HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                            HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;
                            for (DataSnapshot single : dataSnapshot.getChildren()) {
                                coreOuterHashMap = new HashMap<>();
                                outerHashMap = new HashMap<>();
                                for (DataSnapshot innerData : single.getChildren()) {
                                    for (DataSnapshot innerCoreData : innerData.getChildren()) {
                                        innerHashMap = new HashMap<>();
                                        innerHashMap.put(innerCoreData.getKey(), (HashMap<String, String>) innerCoreData.getValue());
                                        outerHashMap.put(innerData.getKey(), innerHashMap);
                                    }
                                }
                                coreOuterHashMap.put(single.getKey(), outerHashMap);
                                contentArrayList.add(coreOuterHashMap);

                            }
                            try {
                                dataManipulation(contentArrayList, selectedLanguage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            isloading=false;
                            hideconnection_layout();

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

    private void showHintOption() {
//        if (hintHandler != null) {
//            hintHandler.removeCallbacksAndMessages(null);
//        }
//        hint_btn.setVisibility(View.GONE);
//        hintImageView.setVisibility(View.GONE);
//        hintHandler = new Handler();
//
//        hintHandler.postDelayed(() -> {
////            hintImageView.setVisibility(View.VISIBLE);
//
//            hint_btn.setVisibility(View.VISIBLE);
//            Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
//            hint_btn.startAnimation(animation);
//            hint_btn.setVisibility(View.VISIBLE);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hint_btn.clearAnimation();
//                }
//            },400);
//
//
//        }, 15000);
    }

    private void showExitDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.close_practice_dialog);
        dialog.setCancelable(false);


        String textViewCloseText,textViewTitleText,about_to_complete_text,close_midway_text,textViewOkayText;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            about_to_complete_text ="कुछ और सवालों को हल करने पर आप १००% महारत प्राप्त करने के और करीब पहुंच सकते हैं।";
            close_midway_text ="क्या आप अपने अभ्यास को बीच में ही रोकना चाहते हैं ?";
            textViewOkayText ="अभ्यास में वापिस जाएँ";
            textViewCloseText ="बहार जाएँ";
        }else {
            about_to_complete_text ="A few more questions can take you closer to achieving 100% mastery in this chapter.";
            close_midway_text ="Are you sure you want to exit?";
            textViewOkayText ="Keep Practicing";
            textViewCloseText ="Exit";
        }

        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
        TxtExit.setText(textViewCloseText);
        TextView textView = dialog.findViewById(R.id.textView);
        TextView textViewSubtitle = dialog.findViewById(R.id.textViewSubTitle);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);

        textViewContinoue.setText(textViewOkayText);
        textView.setText(close_midway_text);
        textViewSubtitle.setText(about_to_complete_text);
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
            }
        });
        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
                if(optionSelected) {streakProgress--;}
                PalContentListingActivity_Mobile.backPressed = true;
                finish();

            }
        });

        dialog.findViewById(R.id.closeImage).setOnClickListener(new View.OnClickListener() {
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
    }
    boolean isloaded=false;

    /* Data manipulation of questions and answer */
    private void dataManipulation(ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList, String language) throws Exception {
        /* Save time for next question */
        Util.saveTime();
        /* Variables which we will be used to manipluate the data */
        String keyToGet = null;
        String keyToGetQuestion = null;
        /*Total number of level*/
        totalNoOfLevel = contentArrayList.size();

        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> stringHashMapHashMap = contentArrayList.get(level - 1);
        /*getting the key of the Hashmap*/
        for (String key : stringHashMapHashMap.keySet()) {
            System.out.println(key);
            keyToGet = key;
        }
        HashMap<String, HashMap<String, HashMap<String, String>>> stringHashMapHashMap1 = stringHashMapHashMap.get(keyToGet);
        /* Add data in the array list only once in a single level to avoid the repeatition */
        if (newQuestionLoad) {
            newQuestionLoad = false;
            questionArrayList = new ArrayList<>();
            for (int i = 0; i < stringHashMapHashMap1.size(); i++) {
                HashMap<String, HashMap<String, String>> hashMapHashMap = stringHashMapHashMap1.get("" + i);
                questionArrayList.add(hashMapHashMap);
            }
        } else {
            if (questionArrayList.size() == 0) {
                newQuestionLoad = true;
                for (int i = 0; i < stringHashMapHashMap1.size(); i++) {
                    HashMap<String, HashMap<String, String>> hashMapHashMap = stringHashMapHashMap1.get("" + i);
                    questionArrayList.add(hashMapHashMap);
                }
            }
        }
        try {
            /*Total number of questions in the current level*/
            int questionCount = questionArrayList.size();
            /*Question that will be displyed to the user*/
            HashMap<String, HashMap<String, String>> map = null;
            if (!isQuestionLoaded) {
                isQuestionLoaded = true;
                /* getting random number from the method coded in the Util class to get the  random number for the question */
                if (isConfigChanged) {
                    questionNo = QuestionNumberAfterConfigChange;
                    isConfigChanged = false;
                    map = questionArrayList.get(questionNo);
                    corectAnswer = answerCorrect;
                } else {
                    questionNo = Util.getRandomNumber(questionCount);
                    map = questionArrayList.get(questionNo);
                    /* getting which option will be correct */
                    Random r = new Random();
//                     corectAnswer = r.nextInt(5 - 1) + 1;
//                    corectAnswer = 1;
                    if(Util.isTestingAPP(context)) corectAnswer = 1;
                    else corectAnswer = r.nextInt(5 - 1) + 1;
                }
            } else {
                /* Reusing the previous question count for language switching */
                map = questionArrayList.get(questionNo);
            }
            /*Getting the key of the innerHashmap*/
            for (String key : map.keySet()) {
                System.out.println(key);
                keyToGetQuestion = key;
                questionID = key;
            }
            /*Final question Hashmap*/
            HashMap<String, String> mapToQuestion = map.get(keyToGetQuestion);
            mapToQuestion.put("feedbackImage","https://download.iprep.in/super_app_content/assessment_images/ncert_eng_sci_06_01_01_023_q.png");
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
            /* Randomise the Answers and Update the Options UI with random Answers */
            randomizeAnswers(language, optionsArrayList);
            /*  Getting questioins in english language and in alternate language */
            String questionQ = mapToQuestion.get("q");
            videoLink = mapToQuestion.get("videoLink");
            videoName = mapToQuestion.get("videoName");
            if (mapToQuestion.containsKey("videoLink_Diksha")||mapToQuestion.containsKey("videoLink_diksha")) {
                videoLink_Diksha = mapToQuestion.get("videoLink_diksha");
            }
            String questionQAlt = null;
            if (global.isFlagEnabled()) {
                correct_feedback_alt = mapToQuestion.get("correct_feedback_alt");
                incorrect_feedback_alt = mapToQuestion.get("incorrect_feedback_alt");
                questionQAlt = mapToQuestion.get("q_alt");
            }
            /* Check which language user has selected and then update the UI accordingly*/
            if (language.equalsIgnoreCase("English")) {
                textViewQuestion.setText(Html.fromHtml(questionQ));
            } else {
                if (global.isFlagEnabled()) {
                    textViewQuestion.setText(Html.fromHtml(questionQAlt));
                } else {
                    textViewQuestion.setText(Html.fromHtml(questionQ));
                }
            }
//            if (textViewQuestion.getLineCount() >= 4) {
//                textViewReadMore.setVisibility(View.VISIBLE);
//                textViewReadMore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Util.preventTwoClick(view);
//                        showDetailFeedbackDialog(textViewQuestion.getText().toString());
//                    }
//                });
//                textViewQuestion.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Util.preventTwoClick(view);
//                        showDetailFeedbackDialog(textViewQuestion.getText().toString());
//                    }
//                });
//            } else {
//                textViewReadMore.setVisibility(View.GONE);
//            }

            /* Getting Correct Feedbck in english and alternate language */
            correct_feedback = mapToQuestion.get("correct_feedback");
            /* Getting Incorrect Feedbck in english and alternate language */
            incorrect_feedback = mapToQuestion.get("incorrect_feedback");
            /* Set question image if exist */
            try {

                questionIcon = mapToQuestion.get("questionImage");
                if (questionIcon != null) {
                    if (!TextUtils.isEmpty(questionIcon)) {
                        questionImageView.setEnabled(true);

                        if(!Util.isOfflineMode(context))
                        {
                            if (Util.checkInternetConnection(context)) {

                                progressBar3.setVisibility(View.VISIBLE);
                                downloadingTextView.setVisibility(View.VISIBLE);
                                reletiveImageView.setVisibility(View.VISIBLE);


                                NoInternetConnectionTextView.setVisibility(View.GONE);
                                questionImageView.setVisibility(View.VISIBLE);
                                // NoInternetConnectionTextView.setText(imageInternetError);

                                String link="https://download.iprep.in/super_app_content/assessment_images/"+questionIcon.replace(" ","");



                                Glide.with(this)
                                        .load(link)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressBar3.setVisibility(View.GONE);
                                                downloadingTextView.setVisibility(View.GONE);
                                                reletiveImageView.setVisibility(View.VISIBLE);
                                                isloaded=false;
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar3.setVisibility(View.GONE);
                                                downloadingTextView.setVisibility(View.GONE);
                                                reletiveImageView.setVisibility(View.VISIBLE);
                                                isloaded=true;
                                                return false;
                                            }
                                        })
                                        .into(questionImageView);

                                reletiveImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(!isloaded)
                                        {
                                            Glide.with(QuizActivity.this)
                                                    .load(link)
                                                    .listener(new RequestListener<Drawable>() {
                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                            progressBar3.setVisibility(View.GONE);
                                                            downloadingTextView.setVisibility(View.GONE);
                                                            reletiveImageView.setVisibility(View.VISIBLE);
                                                            isloaded=false;
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                            progressBar3.setVisibility(View.GONE);
                                                            downloadingTextView.setVisibility(View.GONE);
                                                            reletiveImageView.setVisibility(View.VISIBLE);
                                                            isloaded=true;
                                                            return false;
                                                        }
                                                    })
                                                    .into(questionImageView);
                                        }
                                    }
                                });



//                            if(Util.getSDCardPath(context)!=null)
//                            {
//                                try {
//                                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionIcon);
//                                    Uri uri = Uri.fromFile(file);
//                                    String url="https://download.iprep.in/super_app_content/assessment_images/"+questionIcon;
//                                    Glide.with(this)
//                                            .load(uri)
//                                            .listener(new RequestListener<Drawable>() {
//                                                @Override
//                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                                    progressBar3.setVisibility(View.GONE);
//                                                    downloadingTextView.setVisibility(View.GONE);
//                                                    reletiveImageView.setVisibility(View.GONE);
//                                                    return false;
//                                                }
//
//                                                @Override
//                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                                    progressBar3.setVisibility(View.GONE);
//                                                    downloadingTextView.setVisibility(View.GONE);
//                                                    reletiveImageView.setVisibility(View.VISIBLE);
//                                                    return false;
//                                                }
//                                            })
//                                            .into(questionImageView);
//
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//
//                                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionIcon);
//                                    Uri uri = Uri.fromFile(file);
//
//
//                                    String link="https://download.iprep.in/super_app_content/assessment_images/"+questionIcon;
//
//                                    Glide.with(this)
//                                            .load(link)
//                                            .listener(new RequestListener<Drawable>() {
//                                                @Override
//                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                                    progressBar3.setVisibility(View.GONE);
//                                                    downloadingTextView.setVisibility(View.GONE);
//                                                    reletiveImageView.setVisibility(View.GONE);
//                                                    return false;
//                                                }
//
//                                                @Override
//                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                                    progressBar3.setVisibility(View.GONE);
//                                                    downloadingTextView.setVisibility(View.GONE);
//                                                    reletiveImageView.setVisibility(View.VISIBLE);
//                                                    return false;
//                                                }
//                                            })
//                                            .into(questionImageView);
//
//
//                                }
//
//                            }
//                            else
//                            {
//                                String link="https://download.iprep.in/super_app_content/assessment_images/"+questionIcon;
//
//                                Glide.with(this)
//                                        .load(Uri.parse(link))
//                                        .listener(new RequestListener<Drawable>() {
//                                            @Override
//                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                                progressBar3.setVisibility(View.GONE);
//                                                downloadingTextView.setVisibility(View.GONE);
//                                                reletiveImageView.setVisibility(View.GONE);
//                                                return false;
//                                            }
//
//                                            @Override
//                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                                progressBar3.setVisibility(View.GONE);
//                                                downloadingTextView.setVisibility(View.GONE);
//                                                reletiveImageView.setVisibility(View.VISIBLE);
//                                                return false;
//                                            }
//                                        })
//                                        .into(questionImageView);
//
//                            }


//                            progressBar3.setVisibility(View.VISIBLE);
//                            downloadingTextView.setVisibility(View.VISIBLE);
//                            reletiveImageView.setVisibility(View.VISIBLE);
//                            NoInternetConnectionTextView.setVisibility(View.GONE);
//                            questionImageView.setVisibility(View.VISIBLE);
//
//                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionIcon);
//                            Uri uri = Uri.fromFile(file);
//
//                            Glide.with(context).load(uri).into(questionImageView);
//
//                            Glide.with(this)
//                                    .load(uri)
//                                    .listener(new RequestListener<Drawable>() {
//                                        @Override
//                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                            progressBar3.setVisibility(View.GONE);
//                                            downloadingTextView.setVisibility(View.GONE);
//                                            reletiveImageView.setVisibility(View.GONE);
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                            progressBar3.setVisibility(View.GONE);
//                                            downloadingTextView.setVisibility(View.GONE);
//                                            reletiveImageView.setVisibility(View.GONE);
//                                            return false;
//                                        }
//                                    })
//                                    .into(questionImageView);


                            }
                            else {
                                progressBar3.setVisibility(View.VISIBLE);
                                downloadingTextView.setVisibility(View.VISIBLE);
                                reletiveImageView.setVisibility(View.VISIBLE);


                                NoInternetConnectionTextView.setVisibility(View.GONE);
                                questionImageView.setVisibility(View.VISIBLE);
                                // NoInternetConnectionTextView.setText(imageInternetError);
                                try {
                                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionIcon);
                                    Uri uri = Uri.fromFile(file);
                                    Glide.with(this)
                                            .load(uri)
                                            .load(uri)
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    progressBar3.setVisibility(View.GONE);
                                                    downloadingTextView.setVisibility(View.GONE);
                                                    reletiveImageView.setVisibility(View.GONE);
                                                    System.out.println("@@@@ uri "+uri);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progressBar3.setVisibility(View.GONE);
                                                    downloadingTextView.setVisibility(View.GONE);
                                                    reletiveImageView.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            })
                                            .into(questionImageView);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        else {
                            System.out.println( "-------- Util.isOfflineMode(context) "+Util.isOfflineMode(context));
                            progressBar3.setVisibility(View.VISIBLE);
                            downloadingTextView.setVisibility(View.VISIBLE);
                            reletiveImageView.setVisibility(View.VISIBLE);


                            NoInternetConnectionTextView.setVisibility(View.GONE);
                            questionImageView.setVisibility(View.VISIBLE);
                            // NoInternetConnectionTextView.setText(imageInternetError);
                            try {
                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionIcon);
                                Uri uri = Uri.fromFile(file);
                                Glide.with(this)
                                        .load(uri)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressBar3.setVisibility(View.GONE);
                                                downloadingTextView.setVisibility(View.GONE);
                                                reletiveImageView.setVisibility(View.GONE);
                                                System.out.println("@@@@ offline  uri "+uri);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar3.setVisibility(View.GONE);
                                                downloadingTextView.setVisibility(View.GONE);
                                                reletiveImageView.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(questionImageView);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }





                    } else {
                        progressBar3.setVisibility(View.GONE);
                        downloadingTextView.setVisibility(View.GONE);
                        reletiveImageView.setVisibility(View.GONE);
                        questionImageView.setEnabled(false);
//                        questionImageView.setImageResource(android.R.color.transparent);
                        try {
                            questionImageView.setImageResource(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    progressBar3.setVisibility(View.GONE);
                    downloadingTextView.setVisibility(View.GONE);
                    reletiveImageView.setVisibility(View.GONE);


                }
                feedbackImage = mapToQuestion.get("feedback_image");
//                feedbackImage = "https://download.iprep.in/super_app_content/assessment_images/ncert_eng_sci_06_01_01_023_q.png";
            } catch (Exception e) {
                e.printStackTrace();
//                feedbackImage = null;
//                questionImageView.setImageResource(android.R.color.transparent);
                try {
                    questionImageView.setImageResource(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //*****                                          *****//
    //*****         Randomise the Answers            *****//
    //*****                                          *****//
    private void randomizeAnswers(String language, ArrayList<Object> optionsArrayList) {
        try {
            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.centerCrop();
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

//                a = a.substring(1, a.length() - 1);
//                String[] keyValuePairs = new String[2];
//                String[] keyValuePairs1 = a.split(", ");
//                if (keyValuePairs1.length > 2) {
//                    keyValuePairs[0] = keyValuePairs1[0];
//                    String keyy = "";
//                    for (int i = 1; i < keyValuePairs1.length; i++) {
//                        if (i == 1) {
//                            keyy = keyValuePairs1[i];
//                        } else {
//                            keyy = keyValuePairs1[i] + ", " + keyy;
//                        }
//                    }
//                    if (keyy.contains(", value")) {
//                        keyy = keyy.replace(", value", "");
//                    }
//                    keyValuePairs[1] = keyValuePairs1[1] + ", " + keyy;
//                } else {
//                    keyValuePairs[0] = keyValuePairs1[0];
//                    keyValuePairs[1] = keyValuePairs1[1];
//                }
//                Log.i("keyValuePairs", keyValuePairs.toString());
//                for (String pair : keyValuePairs) {
//                    String[] entry = pair.split("=");
//
//                    mapAnswer1.put(entry[0].trim(), entry[1].trim());
//                }
//
//
//                b = b.substring(1, b.length() - 1);
//                String[] keyValuePairs_ = new String[2];
//                String[] keyValuePairs2 = b.split(", ");
//                if (keyValuePairs2.length > 2) {
//                    keyValuePairs_[0] = keyValuePairs2[0];
//                    String keyy = "";
//                    for (int i = 1; i < keyValuePairs2.length; i++) {
//                        if (i == 1) {
//                            keyy = keyValuePairs2[i];
//                        } else {
//                            keyy = keyValuePairs2[i] + ", " + keyy;
//                        }
//                    }
//                    if (keyy.contains(", value")) {
//                        keyy = keyy.replace(", value", "");
//                    }
//                    keyValuePairs_[1] = keyValuePairs2[1] + ", " + keyy;
//                } else {
//                    keyValuePairs_[0] = keyValuePairs2[0];
//                    keyValuePairs_[1] = keyValuePairs2[1];
//                }
//
//                for (String pair : keyValuePairs_) {
//                    String[] entry = pair.split("=");
//                    mapAnswer2.put(entry[0].trim(), entry[1].trim());
//                }
//
//                c = c.substring(1, c.length() - 1);
//
//                String[] keyValuePairs__ = new String[2];
//                String[] keyValuePairs3 = c.split(", ");
//                if (keyValuePairs3.length > 2) {
//                    keyValuePairs__[0] = keyValuePairs3[0];
//                    String keyy = "";
//                    for (int i = 1; i < keyValuePairs3.length; i++) {
//                        if (i == 1) {
//                            keyy = keyValuePairs3[i];
//                        } else {
//                            keyy = keyValuePairs3[i] + ", " + keyy;
//                        }
//                    }
//
//                    if (keyy.contains(", value")) {
//                        keyy = keyy.replace(", value", "");
//                    }
//                    keyValuePairs__[1] = keyValuePairs3[1] + ", " + keyy;
//                } else {
//                    keyValuePairs__[0] = keyValuePairs3[0];
//                    keyValuePairs__[1] = keyValuePairs3[1];
//                }
//
//
//                for (String pair : keyValuePairs__) {
//                    String[] entry = pair.split("=");
//                    mapAnswer3.put(entry[0].trim(), entry[1].trim());
//                }
//
//
//                d = d.substring(1, d.length() - 1);
//                String[] keyValuePairs___ = new String[2];
//
//                String[] keyValuePairs4 = d.split(", ");
//                if (keyValuePairs4.length > 2) {
//                    keyValuePairs___[0] = keyValuePairs4[0];
//                    String keyy = "";
//                    for (int i = 1; i < keyValuePairs4.length; i++) {
//                        if (i == 1) {
//                            keyy = keyValuePairs4[i];
//                        } else {
//                            keyy = keyValuePairs4[i] + ", " + keyy;
//                        }
//                    }
//                    if (keyy.contains(", value")) {
//                        keyy = keyy.replace(", value", "");
//                    }
//                    keyValuePairs___[1] = keyValuePairs4[1] + ", " + keyy;
//                } else {
//                    keyValuePairs___[0] = keyValuePairs4[0];
//                    keyValuePairs___[1] = keyValuePairs4[1];
//                }
//
//
//                for (String pair : keyValuePairs___) {
//                    String[] entry = pair.split("=");
//                    mapAnswer4.put(entry[0].trim(), entry[1].trim());
//                }

                a = a.substring(1, a.length() - 1);
                String[] keyValuePairs = new String[2];
                String[] keyValuePairs1 = a.split(", ",2);
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
                    if(pair.equals("value==")){
                        entry = pair.split("=", 2);
                    }else{
                        entry = pair.split("=");
                    }
//            String[] entry = pair.split("=");
                    String key = entry[0].trim();
                    int length = entry.length;
                    String val = "";

                    if (length > 2) {
                        for (int i = 1; i < length; i++) {
//                            val = val + " = " + entry[i];
                            if(i==1){
                                val = entry[i];

                            }else{
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
                String[] keyValuePairs2 = b.split(", ",2);
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
                    if(pair.equals("value==")){
                        entry = pair.split("=", 2);
                    }else{
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
                            if(i==1){
                                val = entry[i];

                            }else{
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
                String[] keyValuePairs3 = c.split(", ",2);
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
                    if(pair.equals("value==")){
                        entry = pair.split("=", 2);
                    }else{
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
                            if(i==1){
                                val = entry[i];

                            }else{
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

                String[] keyValuePairs4 = d.split(", ",2);
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
                    if(pair.equals("value==")){
                        entry = pair.split("=", 2);
                    }else{
                        entry = pair.split("=");
                    }
//            String[] entry = pair.split("=");
//                    mapAnswer4.put(entry[0].trim(), entry[1].trim());
                    String key = entry[0].trim();
                    int length = entry.length;
                    String val = "";
                    if (length > 2) {
                        for (int i = 1; i < length; i++) {
                            if(i==1){
                                val = entry[i];

                            }else{
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


            if (option1Type.equalsIgnoreCase("छवि")||option1Type.equalsIgnoreCase("image")) {
                option1Type = "Image";
            }
            if (option2Type.equalsIgnoreCase("छवि")||option2Type.equalsIgnoreCase("image")) {
                option2Type = "Image";
            }
            if (option3Type.equalsIgnoreCase("छवि")||option3Type.equalsIgnoreCase("image")) {
                option3Type = "Image";
            }
            if (option4Type.equalsIgnoreCase("छवि")||option4Type.equalsIgnoreCase("image")) {
                option4Type = "Image";
            }


            String option1;
            String option2;
            String option3;
            String option4;
            if (option1Type.equalsIgnoreCase("Text")) {
                /* getting option values in the english language from the above stated hashmaps */
                if (topicId.equalsIgnoreCase(ApplicationConstants.ROMAN_NUMERAL_TOPIC_ID)) {
                    option1 = mapAnswer1.get("value");
                    option2 = mapAnswer2.get("value");
                    option3 = mapAnswer3.get("value");
                    option4 = mapAnswer4.get("value");
                } else {
//                    option1 = Util.capitalize(mapAnswer1.get("value"));
//                    option2 = Util.capitalize(mapAnswer2.get("value"));
//                    option3 = Util.capitalize(mapAnswer3.get("value"));
//                    option4 = Util.capitalize(mapAnswer4.get("value"));

                    option1 = mapAnswer1.get("value");
                    option2 = mapAnswer2.get("value");
                    option3 = mapAnswer3.get("value");
                    option4 = mapAnswer4.get("value");

                }
            } else {
                /* getting option values in the english language from the above stated hashmaps */
                option1 = mapAnswer1.get("value");
                option2 = mapAnswer2.get("value");
                option3 = mapAnswer3.get("value");
                option4 = mapAnswer4.get("value");
            }

            String option1_alt = null;
            String option2_alt = null;
            String option3_alt = null;
            String option4_alt = null;
            if (global.isFlagEnabled()) {
                /* getting option values in the Alternate language from the above stated hashmaps */
                option1_alt = mapAnswer1.get("value_alt");
                option2_alt = mapAnswer2.get("value_alt");
                option3_alt = mapAnswer3.get("value_alt");
                option4_alt = mapAnswer4.get("value_alt");
            }


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
                    if (option1Type.equalsIgnoreCase("Text")) {
                        linearFullscreen1.setVisibility(View.GONE);
                        textViewOption1.setVisibility(View.VISIBLE);
                        if (language.equalsIgnoreCase("English")) {
                            textViewOption1.setText(option1);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption1.setText(option1_alt);
                            } else {
                                textViewOption1.setText(option1);
                            }
                        }
                    } else {
                        image1 = option1;
                        linearFullscreen1.setVisibility(View.VISIBLE);
                        textViewOption1.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image1.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption1);
//                            Glide.with(context).load(option1).apply(requestOptions).into(imageViewOption1);
                        } else {
//                            textViewOption1.setVisibility(View.VISIBLE);
//                            linearFullscreen1.setVisibility(View.GONE);
//                            textViewOption1.setText(imageInternetError);

                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);

                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption1);


                        }
                    }
                    if (option2Type.equalsIgnoreCase("Text")) {
                        linearFullscreen2.setVisibility(View.GONE);
                        textViewOption2.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption2.setText(option2);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption2.setText(option2_alt);
                            } else {
                                textViewOption2.setText(option2);
                            }
                        }
                    } else {
                        image2 = option2;
                        linearFullscreen2.setVisibility(View.VISIBLE);
                        textViewOption2.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);

                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image2.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption2);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);

                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption2);


                        }
                    }
                    if (option3Type.equalsIgnoreCase("Text")) {
                        linearFullscreen3.setVisibility(View.GONE);
                        textViewOption3.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption3.setText(option3);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption3.setText(option3_alt);
                            } else {
                                textViewOption3.setText(option3);
                            }
                        }
                    } else {
                        image3 = option3;
                        linearFullscreen3.setVisibility(View.VISIBLE);
                        textViewOption3.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image3.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption3);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);

                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);


                        }

//                        if (Util.checkInternetConnection(context)) {
//                            textViewOption3.setVisibility(View.GONE);
//                            linearFullscreen3.setVisibility(View.VISIBLE);
//
//                            if(Util.getSDCardPath(context)!=null)
//                            {
//                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
//                                Uri uri = Uri.fromFile(file);
//                                Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);
//                            }
//                            else
//                            {
//                                String link = "https://download.iprep.in/super_app_content/assessment_images/"+image3;
//                                Glide.with(context).load(link).apply(requestOptions).into(imageViewOption3);
//                            }
//
//                        } else {
////                            textViewOption3.setVisibility(View.VISIBLE);
////                            linearFullscreen3.setVisibility(View.GONE);
////                            textViewOption3.setText(imageInternetError);
//
//                            textViewOption3.setVisibility(View.GONE);
//                            linearFullscreen3.setVisibility(View.VISIBLE);
//
//                            if(Util.getSDCardPath(context)!=null)
//                            {
//                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
//                                Uri uri = Uri.fromFile(file);
//                                Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);
//
//                            }
//                            else
//                            {
//                                String link = "https://download.iprep.in/super_app_content/assessment_images/"+image3;
//                                Glide.with(context).load(link).apply(requestOptions).into(imageViewOption3);
//                            }
//
//                        }


                    }
                    if (option4Type.equalsIgnoreCase("Text")) {
                        linearFullscreen4.setVisibility(View.GONE);
                        textViewOption4.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption4.setText(option4);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption4.setText(option4_alt);
                            } else {
                                textViewOption4.setText(option4);
                            }
                        }
                    } else {
                        image4 = option4;
                        linearFullscreen4.setVisibility(View.VISIBLE);
                        textViewOption4.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image4.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption4);
//                            Glide.with(context).load(option4).apply(requestOptions).into(imageViewOption4);
                        } else {
//                            textViewOption4.setVisibility(View.VISIBLE);
//                            linearFullscreen4.setVisibility(View.GONE);
//                            textViewOption4.setText(imageInternetError);

                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);

                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);

                            System.out.println("====== file "+file);
                            System.out.println("====== image2 "+image2);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption4);
                        }


                    }

                    break;
                case 2:

                    if (option2Type.equalsIgnoreCase("Text")) {
                        linearFullscreen1.setVisibility(View.GONE);

                        textViewOption1.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption1.setText(option2);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption1.setText(option2_alt);
                            } else {
                                textViewOption1.setText(option2);
                            }
                        }
                    } else {
                        image1 = option2;
                        linearFullscreen1.setVisibility(View.VISIBLE);
                        textViewOption1.setVisibility(View.GONE);


                        if (Util.checkInternetConnection(context)) {
                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image1.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption1);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption1);
                        } else {
//                            textViewOption1.setVisibility(View.VISIBLE);
//                            linearFullscreen1.setVisibility(View.GONE);
//                            textViewOption1.setText(imageInternetError);


                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption1);
                        }

                    }

                    if (option1Type.equalsIgnoreCase("Text")) {
                        linearFullscreen2.setVisibility(View.GONE);
                        textViewOption2.setVisibility(View.VISIBLE);
                        if (language.equalsIgnoreCase("English")) {
                            textViewOption2.setText(option1);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption2.setText(option1_alt);
                            } else {
                                textViewOption2.setText(option1);
                            }
                        }
                    } else {
                        image2 = option1;
                        linearFullscreen2.setVisibility(View.VISIBLE);
                        textViewOption2.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image2.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption2);
//                            Glide.with(context).load(option1).apply(requestOptions).into(imageViewOption2);
                        } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);

                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption2);

                        }


                    }

                    if (option3Type.equalsIgnoreCase("Text")) {
                        linearFullscreen3.setVisibility(View.GONE);

                        textViewOption3.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption3.setText(option3);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption3.setText(option3_alt);
                            } else {
                                textViewOption3.setText(option3);
                            }
                        }
                    } else {
                        image3 = option3;
                        linearFullscreen3.setVisibility(View.VISIBLE);
                        textViewOption3.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption3.setVisibility(View.GONE);
                            linearFullscreen3.setVisibility(View.VISIBLE);

                            if (Util.checkInternetConnection(context)) {
                                textViewOption2.setVisibility(View.GONE);
                                linearFullscreen2.setVisibility(View.VISIBLE);
                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                                Uri uri = Uri.fromFile(file);
                                String url="https://download.iprep.in/super_app_content/assessment_images/"+image3.replace(" ","");
                                Glide.with(context).load(url).apply(requestOptions).into(imageViewOption3);
//                            Glide.with(context).load(option1).apply(requestOptions).into(imageViewOption2);
                            } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);

                                textViewOption2.setVisibility(View.GONE);
                                linearFullscreen2.setVisibility(View.VISIBLE);
                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                                Uri uri = Uri.fromFile(file);
                                Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);

                            }

//                            if(Util.getSDCardPath(context)!=null)
//                            {
//                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
//                                Uri uri = Uri.fromFile(file);
//                                Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);
//
//                            }
//                            else
//                            {
//                                String link = "https://download.iprep.in/super_app_content/assessment_images/"+image3;
//                                Glide.with(context).load(link).apply(requestOptions).into(imageViewOption3);
//                            }

                        } else {
//                            textViewOption3.setVisibility(View.VISIBLE);
//                            linearFullscreen3.setVisibility(View.GONE);
//                            textViewOption3.setText(imageInternetError);

                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);

                            if(Util.getSDCardPath(context)!=null)
                            {
                                File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                                Uri uri = Uri.fromFile(file);
                                Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);

                            }
                            else
                            {
                                String link = "https://download.iprep.in/super_app_content/assessment_images/"+image3.replace(" ","");
                                Glide.with(context).load(link).apply(requestOptions).into(imageViewOption3);
                            }

                        }


                    }

                    if (option4Type.equalsIgnoreCase("Text")) {
                        linearFullscreen4.setVisibility(View.GONE);

                        textViewOption4.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption4.setText(option4);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption4.setText(option4_alt);
                            } else {
                                textViewOption4.setText(option4);
                            }
                        }

                    } else {
                        image4 = option4;
                        linearFullscreen4.setVisibility(View.VISIBLE);
                        textViewOption4.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image4.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption4);
//                            Glide.with(context).load(option4).apply(requestOptions).into(imageViewOption4);
                        } else {
//                            textViewOption4.setVisibility(View.VISIBLE);
//                            linearFullscreen4.setVisibility(View.GONE);
//                            textViewOption4.setText(imageInternetError);

                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption4);

                        }

                    }
                    break;
                case 3:


                    if (option3Type.equalsIgnoreCase("Text")) {
                        linearFullscreen1.setVisibility(View.GONE);

                        textViewOption1.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption1.setText(option3);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption1.setText(option3_alt);
                            } else {
                                textViewOption1.setText(option3);
                            }
                        }
                    } else {
                        image1 = option3;
                        linearFullscreen1.setVisibility(View.VISIBLE);
                        textViewOption1.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image1.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption1);
//                            Glide.with(context).load(option3).apply(requestOptions).into(imageViewOption1);
                        } else {
//                            textViewOption1.setVisibility(View.VISIBLE);
//                            linearFullscreen1.setVisibility(View.GONE);
//                            textViewOption1.setText(imageInternetError);

                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption1);

                        }


                    }

                    if (option2Type.equalsIgnoreCase("Text")) {
                        linearFullscreen2.setVisibility(View.GONE);

                        textViewOption2.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption2.setText(option2);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption2.setText(option2_alt);
                            } else {
                                textViewOption2.setText(option2);
                            }
                        }
                    } else {
                        image2 = option2;
                        linearFullscreen2.setVisibility(View.VISIBLE);
                        textViewOption2.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image2.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption2);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);

                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption2);

                        }


                    }

                    if (option1Type.equalsIgnoreCase("Text")) {
                        linearFullscreen3.setVisibility(View.GONE);

                        textViewOption3.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption3.setText(option1);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption3.setText(option1_alt);
                            } else {
                                textViewOption3.setText(option1);
                            }
                        }
                    } else {
                        image3 = option1;

                        linearFullscreen3.setVisibility(View.VISIBLE);
                        textViewOption3.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption3.setVisibility(View.GONE);
                            linearFullscreen3.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image3.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption3);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        } else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);

                            textViewOption3.setVisibility(View.GONE);
                            linearFullscreen3.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);

                        }

                    }

                    if (option4Type.equalsIgnoreCase("Text")) {
                        linearFullscreen4.setVisibility(View.GONE);

                        textViewOption4.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption4.setText(option4);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption4.setText(option4_alt);
                            } else {
                                textViewOption4.setText(option4);
                            }
                        }
                    } else {
                        image4 = option4;
                        linearFullscreen4.setVisibility(View.VISIBLE);
                        textViewOption4.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            String url="https://download.iprep.in/super_app_content/assessment_images/"+image4.replace(" ","");
                            Glide.with(context).load(url).apply(requestOptions).into(imageViewOption4);
//                            Glide.with(context).load(option4).apply(requestOptions).into(imageViewOption4);
                        } else {
//                            textViewOption4.setVisibility(View.VISIBLE);
//                            linearFullscreen4.setVisibility(View.GONE);
//                            textViewOption4.setText(imageInternetError);

                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption4);

                        }


                    }

                    break;
                case 4:

                    if (option4Type.equalsIgnoreCase("Text")) {
                        linearFullscreen1.setVisibility(View.GONE);

                        textViewOption1.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption1.setText(option4);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption1.setText(option4_alt);
                            } else {
                                textViewOption1.setText(option4);
                            }
                        }
                    } else {
                        image1 = option4;
                        linearFullscreen1.setVisibility(View.VISIBLE);
                        textViewOption1.setVisibility(View.GONE);

                        if (Util.checkInternetConnection(context)) {
                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            String link = "https://download.iprep.in/super_app_content/assessment_images/"+image1.replace(" ","");
                            Glide.with(context).load(link).apply(requestOptions).into(imageViewOption1);
//                            Glide.with(context).load(option4).apply(requestOptions).into(imageViewOption1);
                        } else {
//                            textViewOption1.setVisibility(View.VISIBLE);
//                            linearFullscreen1.setVisibility(View.GONE);
//                            textViewOption1.setText(imageInternetError);

                            textViewOption1.setVisibility(View.GONE);
                            linearFullscreen1.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image1 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption1);

                        }

                    }

                    if (option2Type.equalsIgnoreCase("Text")) {
                        linearFullscreen2.setVisibility(View.GONE);
                        textViewOption2.setVisibility(View.VISIBLE);
                        if (language.equalsIgnoreCase("English")) {
                            textViewOption2.setText(option2);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption2.setText(option2_alt);
                            } else {
                                textViewOption2.setText(option2);
                            }
                        }
                    } else {
                        image2 = option2;
                        linearFullscreen2.setVisibility(View.VISIBLE);
                        textViewOption2.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );

                            Uri uri = Uri.fromFile(file);
                            String link = "https://download.iprep.in/super_app_content/assessment_images/"+image2.replace(" ","");
                            Glide.with(context).load(link).apply(requestOptions).into(imageViewOption2);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        }
                        else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);


                            textViewOption2.setVisibility(View.GONE);
                            linearFullscreen2.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image2 );
                            Uri uri = Uri.fromFile(file);

                            System.out.println("====== 2  file "+file);
                            System.out.println("====== 2 image2 "+image2);

                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption2);

                        }


                    }

                    if (option3Type.equalsIgnoreCase("Text")) {
                        linearFullscreen3.setVisibility(View.GONE);

                        textViewOption3.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption3.setText(option3);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption3.setText(option3_alt);
                            } else {
                                textViewOption3.setText(option3);
                            }
                        }
                    } else {
                        image3 = option3;
                        linearFullscreen3.setVisibility(View.VISIBLE);
                        textViewOption3.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption3.setVisibility(View.GONE);
                            linearFullscreen3.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );

                            Uri uri = Uri.fromFile(file);
                            String link = "https://download.iprep.in/super_app_content/assessment_images/"+image3.replace(" ","");
                            Glide.with(context).load(link).apply(requestOptions).into(imageViewOption3);
//                            Glide.with(context).load(option2).apply(requestOptions).into(imageViewOption2);
                        }
                        else {
//                            textViewOption2.setVisibility(View.VISIBLE);
//                            linearFullscreen2.setVisibility(View.GONE);
//                            textViewOption2.setText(imageInternetError);
                            textViewOption3.setVisibility(View.GONE);
                            linearFullscreen3.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image3 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption3);

                        }



                    }

                    if (option1Type.equalsIgnoreCase("Text")) {
                        linearFullscreen4.setVisibility(View.GONE);

                        textViewOption4.setVisibility(View.VISIBLE);

                        if (language.equalsIgnoreCase("English")) {
                            textViewOption4.setText(option1);
                        } else {
                            if (global.isFlagEnabled()) {
                                textViewOption4.setText(option1_alt);
                            } else {
                                textViewOption4.setText(option1);
                            }
                        }
                    } else {
                        image4 = option1;
                        linearFullscreen4.setVisibility(View.VISIBLE);
                        textViewOption4.setVisibility(View.GONE);
                        if (Util.checkInternetConnection(context)) {
                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            String link = "https://download.iprep.in/super_app_content/assessment_images/"+image4.replace(" ","");
                            Glide.with(context).load(link).apply(requestOptions).into(imageViewOption4);
//                            Glide.with(context).load(option1).apply(requestOptions).into(imageViewOption4);
                        } else {
//                            textViewOption4.setVisibility(View.VISIBLE);
//                            linearFullscreen4.setVisibility(View.GONE);
//                            textViewOption4.setText(imageInternetError);

                            textViewOption4.setVisibility(View.GONE);
                            linearFullscreen4.setVisibility(View.VISIBLE);
                            File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + image4 );
                            Uri uri = Uri.fromFile(file);
                            Glide.with(context).load(uri).apply(requestOptions).into(imageViewOption4);

                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO DIALOG
        }


    }

    //*****                                          *****//
    //*****       Correct answers clicked code       *****//
    //*****                                          *****//
    private void correctAnswer() {
        if (isCorrectStreakClicked) {
            isCorrectStreakClicked = false;
            // isIncorrectStreakClicked = true;
            streakProgress++;
            optionSelected = true;
        }
        isAnswerCorrect = true;
        isQuestionAttempted = true;
    }

    //*****                                          *****//
    //*****     Incorrect answers clicked code       *****//
    //*****                                          *****//
    private void incorrectAnswer() {
        if (isIncorrectStreakClicked) {
            isIncorrectStreakClicked = false;
        }
        isAnswerCorrect = false;
        isQuestionAttempted = true;
    }

    /* update level */
    private void updateLevel(int masteryProgress) {
        if(masteryProgress<25) {
            level1_icon.setImageResource(R.drawable.ic_unlocked);
            level2_icon.setImageResource(R.drawable.ic_locked);
            level3_icon.setImageResource(R.drawable.ic_locked);
            level4_icon.setImageResource(R.drawable.ic_locked);
        }
        else if(masteryProgress<50) {
            level1_icon.setImageResource(R.drawable.ic_tick);
            level2_icon.setImageResource(R.drawable.ic_unlocked);
            level3_icon.setImageResource(R.drawable.ic_locked);
            level4_icon.setImageResource(R.drawable.ic_locked);



        }
        else if(masteryProgress<75) {
            level1_icon.setImageResource(R.drawable.ic_tick);
            level2_icon.setImageResource(R.drawable.ic_tick);
            level3_icon.setImageResource(R.drawable.ic_unlocked);
            level4_icon.setImageResource(R.drawable.ic_locked);
        }
        else if(masteryProgress<100) {
            level1_icon.setImageResource(R.drawable.ic_tick);
            level2_icon.setImageResource(R.drawable.ic_tick);
            level3_icon.setImageResource(R.drawable.ic_tick);
            level4_icon.setImageResource(R.drawable.ic_unlocked);
        }
    }


    private void showSkipPopUp() {

        String textSkipDialogue,buttonVideoDekhein,buttonPracticeKarein,orTextText;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            textSkipDialogue = "क्या आप आगे बढ़ने से पहले " + topicName + " को बेहतर ढंग से समझना चाहते हैं?";
            buttonVideoDekhein = "वीडियो देखें";
            buttonPracticeKarein = "अभ्यास जारी रखें";
            orTextText = "या";
        }else {
            textSkipDialogue = "Would you like to better understand " + topicName + " before proceeding?";
            buttonVideoDekhein = "Watch video";
            buttonPracticeKarein = "Keep Practicing";
            orTextText = "Or";
        }

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.skip_question_layout);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
        textView.setText(Html.fromHtml(textSkipDialogue));
        TextView videoBt = dialog.findViewById(R.id.videoBt);
        TextView orText = dialog.findViewById(R.id.orText);
        orText.setText(orTextText);
        videoBt.setText(buttonVideoDekhein);
        TextView continueBt = dialog.findViewById(R.id.continueBt);
        continueBt.setText(buttonPracticeKarein);

        ImageView crossImage=dialog.findViewById(R.id.closeImage);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        videoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                dialog.dismiss();
                String[] videoCode = videoLink.split("/");
                String videoc = videoCode[videoCode.length - 1];
                Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                intent.putExtra("videoId", videoLink);
                intent.putExtra("vidId", videoLink);
                intent.putExtra("offlineLink", getOffLineLink(videoc));
                intent.putExtra("videoName", videoName);
                intent.putExtra("from", "practice");
                startActivity(intent);
            }
        });
        continueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                dialog.dismiss();
                skipCount++;
                textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                showNextQuestionForSameLevel();
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
                dialogInterface.dismiss();
            }
        });
    }

    private void showNextQuestionForSameLevel() {
        try {
            if (skipCount >= streak) {
                animateLayout();
                /* Restore to fresh state */
                restoreOptionsInUnselectedState();
                /*Restore the Incorrect Streak Progress*/
                streakDeProgress = 0;
                streakProgress = 0;
                if (level != 1) {
                    newQuestionLoad = true;
                    /* Degrade the level as user match the count with incorrect streak */
                    level = level - 1;
                } else {
                    showLastLevelVideos("last");
                }
                hintCount = 0;
                /* Send user analytics to the firebase for further graphs processing in coming version*/
                sendAnalyticsToFireBase("Incorrect");
                sendAnalyticsMasteryNodeToFireBase(String.valueOf(calculateMastery()));
                try {
                    /* showing new  question to the user*/
                    dataManipulation(contentArrayList, selectedLanguage);
                    hintRefresh = true;
//                    int streakCount = streak - 1;
//                    if(streakProgress == streakCount){
//                        if(hintHandler != null){
//                            hintHandler.removeCallbacksAndMessages(null);
//                        }
//                        hintImageView.setVisibility(View.GONE);
//                    }else{
                    showHintOption();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                skipCount = 0;
            } else {
                restoreOptionsInUnselectedState();
                newQuestionLoad = true;
                animateLayout();
                dataManipulation(contentArrayList, selectedLanguage);
                if (optionSelected) {
                    if (streakProgress > 0) {
                        streakProgress--;
                    }
                    optionSelected = false;
                    textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                    textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                }
//                int streakCount = streak - 1;
//                if(streakProgress == streakCount){
//                    if(hintHandler != null){
//                        hintHandler.removeCallbacksAndMessages(null);
//                    }
//                    hintImageView.setVisibility(View.GONE);
//                }else{
                showHintOption();
                hintRefresh = true;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWelcomeMessages() {
        DatabaseReference databaseReference;
        String subject = Util.getSubject(context);
        if (subject.equalsIgnoreCase("science")) {
            subject = "Science";
        } else if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")) {
            subject = "Maths";
        } else if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")) {
            subject = "English";
        } else {
            subject = "Maths";
        }
        databaseReference = global.getDatabaseReference().child("StaticTextDB/StudentApp/3").child("Messages").child(Util.getSelectedLanguage(context)).child(subject);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        HashMap<String, String> welcomeHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                        String message1 = welcomeHashmap.get("message1");
                        String message2 = welcomeHashmap.get("message2");
                        showWelcomeDialog(message1, message2);
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

    private void showWelcomeDialog(String message1, String message2) throws Exception {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_welcome);
        CheckBox CheckBoxNever = dialog.findViewById(R.id.CheckBoxNever);

        CheckBoxNever.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
            }
        });
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        TextView textViewHeading = dialog.findViewById(R.id.textViewHeading);
        textViewHeading.setText(howItWork);
        textViewMessage.setText((Html.fromHtml(message1)));
        TextView textViewMessage2 = dialog.findViewById(R.id.textViewMessage2);
        textViewMessage2.setVisibility(View.GONE);
        textViewMessage2.setText(message2);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewOkay.setText(message2);
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    Util.setWelcomeMessage(context, "Done");
                }
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

    public int calculateMastery() {

        try {
            float masteryOfCurrentLevel = streakProgress / (totalNoOfLevel * streak);
            float mastery = ((level - 1) / totalNoOfLevel) + masteryOfCurrentLevel;
            double per = (mastery / 100.0f) * 10000;
            int masteryProgress = (int) per;
            masteryTobeSynced = masteryProgress;
            progress.setProgress(masteryProgress);
            MasteryNodeModel masteryNodeModel = new MasteryNodeModel();
            masteryNodeModel.setMastery(String.valueOf(masteryProgress));

            SelectTopicActivity.currentTopicMasteryChanged = masteryProgress + "";
            masteryTextView.setText(currentMastery + " " + masteryProgress + "%");
            updateLevel(masteryProgress);

            return masteryProgress;
        } catch (Exception e) {
            e.printStackTrace();
            SelectTopicActivity.currentTopicMasteryChanged = "0";
            masteryTextView.setText(currentMastery + " 0" + "%");
            return 0;
        }


    }

    //*****                                          *****//
    //*****         Close (Finish) Activity          *****//
    //*****                                          *****//
    private void closeActivity() {

        System.out.println( "===== close activity");
        PalContentListingActivity_Mobile.backPressed = true;
        finish();
        //
    }

    //*****                                          *****//
    //***** Send User (Mastery)analytics to firebase *****//
    //*****                                          *****//
    private void sendAnalyticsMasteryNodeToFireBase(String mastery) {
        try {
            MasteryNodeModel masteryNodeModel = new MasteryNodeModel();
            masteryNodeModel.setMastery(mastery);
            masteryNodeModel.setTime(time);
            masteryNodeModel.setStreakProgress(streakProgress + "");
            masteryNodeModel.setCurrentLevel(level + "");
            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).
                    child(Util.getSubject(context))
                    .child(topicId).setValue(masteryNodeModel);


            objectHashMap.put(ApplicationConstants.ANALYTICS, masteryNodeModel);
            //masterArrayListToSync.add(objectHashMap);

//            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).
//                    child(Util.getSubject(context))
//                    .child(topicId).child(ApplicationConstants.ANALYTICS).setValue(masteryNodeModel, new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    if (databaseError != null) {
//                        Util.showToast(context, databaseError.getMessage());
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            //TODO DIALOG
        }
    }

    //*****                                          *****//
    //*****     Send User analytics to firebase     *****//
    //*****                                          *****//
    private void sendAnalyticsToFireBase(String status) {
        try {
            AnalyticModel analyticModel = new AnalyticModel();
            analyticModel.setLanguage(selectedLanguage);
            analyticModel.setStatus(status);
            analyticModel.setChoosenOption(choosenOption);
            analyticModel.setCorrectOption("Option " + corectAnswer);
            analyticModel.setTotalDurationOfQuestions(Util.getTimeString_(Util.getDifference()));
            analyticModel.setTimeOfFeedBack(Util.getTimeString_(Util.getFeedbackDifference()));
            HashMap<String, Object> object_ = new HashMap<>();
            HashMap<String, Object> object__ = new HashMap<>();
            object__.put("Attempt : " + Util.getCurrentDate(), analyticModel);
            object_.put(questionID, object__);

            MasteryNodeModel masteryNodeModel = new MasteryNodeModel();
            masteryNodeModel.setMastery(String.valueOf(masteryTobeSynced));
            object_.put("Mastery", masteryNodeModel);
            objectHashMap.put(sessionDateTime, object_);


//            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).
//                    child(Util.getSubject(context))
//                    .child(topicId).child(sessionDateTime).child(questionID).child("Attempt : " + Util.getCurrentDate()).setValue(analyticModel, new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    try {
//                        if (databaseError != null) {
//                            Util.showToast(context, databaseError.getMessage());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            //TODO DIALOG
        }

    }

    //*****  Restore To Fresh state Only UI  *****//
    private void restoreOptionsInUnselectedState() {
        try {
            /* Remove question from arraylist to avoid repeatition of questions */
            try {
                questionArrayList.remove(questionNo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /* Restore to original state for new question */
            isIncorrectStreakClicked = true;
            isCorrectStreakClicked = true;
            isQuestionLoaded = false;
            isQuestionAttempted = false;
            /* Restore to original state for UI */
            textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
            textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
            textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
            textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

            linearFullscreen1.setBackgroundResource(R.drawable.gray_iprep);
//            imageViewOption1.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
            linearFullscreen2.setBackgroundResource(R.drawable.gray_iprep);
            linearFullscreen3.setBackgroundResource(R.drawable.gray_iprep);
            linearFullscreen4.setBackgroundResource(R.drawable.gray_iprep);

            textViewOption4.setTextColor(Color.parseColor("#000000"));
            textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);
            textViewOption2.setTextColor(Color.parseColor("#000000"));
            textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
            textViewOption3.setTextColor(Color.parseColor("#000000"));
            textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
            textViewOption1.setTextColor(Color.parseColor("#000000"));
            textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO DIALOG
        }


    }

    private void initializePlayer(String vid_id, VideoView videoView) throws Exception {
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(vid_id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                Map<String, String> aa = video.getStreams();
                String hdStream = aa.get("360p");
                if (hdStream != null) {
                    playVideoFromVimeo(hdStream, videoView, false);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("Error: ", throwable.getLocalizedMessage());
            }
        });
    }

    private void playVideoFromVimeo(final String stream, VideoView videoView, boolean isLocalFile) {
        runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {

                try {
                    final Uri video = Uri.parse(stream);
                    FullScreenMediaController mediaController = new FullScreenMediaController(context, "feedback");
                    videoView.setMediaController(mediaController);
                    mediaController.setVisibility(View.GONE);
                    if (isLocalFile) {
                        serviceIntent = new Intent(context, SocketService.class);
                        context.startService(serviceIntent);
                        String path = "http://localhost:7453" + stream;
                        videoView.setVideoPath(path);
                    } else {
                        videoView.setVideoURI(video);
                    }
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            videoView.start();
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            destroyService();
                            //
//                            Intent intent = new Intent();
//                            getActivity().setResult(123, intent);
//                            getActivity().finish();
//                            getActivity().

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private String getOffLineLink(String id) {
        try {
            String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
            JSONObject object___ = object__.getJSONObject("video_lessons");
            JSONObject object____ = object___.getJSONObject("content");
            JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
            JSONObject object______ = object_____.getJSONObject("topics");
            JSONArray array = object______.getJSONArray(topicId);
            for (int i = 1; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                for (int j = 0; j < object1.names().length(); j++) {
                    String key = object1.names().getString(j);
                    if (key.equals(id)) {
                        JSONObject object2 = object1.getJSONObject(key);
                        return object2.getString("offlineLink");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Correct Feedback dialog */
    private void showCorrectFeedbackDialog(String feedback, int type) {
//        reletiveLayoutAnimate.setVisibility(View.GONE);
        /* Increament the ten question check*/

        if(FeedBackDialog!=null) {
            if(FeedBackDialog.isShowing()) return;
        }

        if (type == 0) {
            if (choosenOption.equals("Option 1")) {
                textViewOption1.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption1.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 2")) {
                textViewOption2.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption2.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 3")) {
                textViewOption3.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption3.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 4")) {
                textViewOption4.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption4.setTextColor(Color.parseColor("#000000"));
            }

            tenQuestionCheck++;
            streakDeProgress = 0;
            /*Track time spend on feedback*/
            Util.saveFeedbackTime();
        }

        FeedBackDialog = new Dialog(context);
        FeedBackDialog.setContentView(R.layout.pal_row_feedback_dialog);
        FeedBackDialog.setCancelable(false);
        /* hookup the Ui element in the FeedBackDialog */
        View videoDikshaLayout = FeedBackDialog.findViewById(R.id.videoDikshaLayout);
        TextView videoTextView = FeedBackDialog.findViewById(R.id.videoTextView);
        TextView videoTextViewDiksha = FeedBackDialog.findViewById(R.id.videoTextViewDiksha);
        videoTextView.setTypeface(null, Typeface.BOLD);


        videoTextViewDiksha.setTypeface(null, Typeface.BOLD);
        TextView textViewReportError = FeedBackDialog.findViewById(R.id.textViewReportError);
        ImageView imageViewCross = FeedBackDialog.findViewById(R.id.imageViewCross);
        ImageView imageViewFeedback = FeedBackDialog.findViewById(R.id.imageViewFeedback);
//        feedbackImage="https://download.iprep.in/super_app_content/assessment_images/ncert_eng_sci_06_01_01_023_q.png";
        if (feedbackImage != null) {
            imageViewFeedback.setVisibility(View.VISIBLE);
            if (Util.checkInternetConnection(context)) {
                NoInternetConnectionTextView.setVisibility(View.GONE);
                questionImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(feedbackImage).into(imageViewFeedback);
            } else {
                NoInternetConnectionTextView.setVisibility(View.VISIBLE);
                questionImageView.setVisibility(View.GONE);
                NoInternetConnectionTextView.setText(imageInternetError);
            }

        } else {
            imageViewFeedback.setVisibility(View.GONE);
        }
        TextView textViewTitle = FeedBackDialog.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        ImageView playbutton = FeedBackDialog.findViewById(R.id.playbutton);
        ImageView playbuttonDiksha = FeedBackDialog.findViewById(R.id.playbuttonDiksha);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(20));
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbutton);
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbuttonDiksha);



        String sahiUttar,Sanket,textViewSubTitle1Text,nextQuestionText,videoTextViewText,videoTextViewTextDiksha;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            sahiUttar ="सही जवाब";
            Sanket ="संकेत";
            textViewSubTitle1Text ="आपने इस प्रश्न का उत्तर सही दिया है। फिर भी अगर आप चाहें तो अपनी समझ को और बेहतर बनाने के लिए आप निम्न वीडियो देख सकते हैं";
            nextQuestionText ="अगल प्रश्न";
            videoTextViewText ="वीडियो (iPrep)";
            videoTextViewTextDiksha ="वीडियो (Diksha)";
        }else {
            sahiUttar ="Correct Answer";
            Sanket ="Hint";
            textViewSubTitle1Text ="Your answer is correct. If you wish to further improve your clarity on this, you can watch the videos below, or else attempt the next question.";
            nextQuestionText ="Next Question";
            videoTextViewText ="Video (iPrep)";
            videoTextViewTextDiksha ="Video (Diksha)";
        }

        if (type == 0) {
            textViewTitle.setText(sahiUttar);
        } else {
            textViewTitle.setText(Sanket);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 50, 20, 0);
            textViewTitle.setLayoutParams(params);
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }

        videoTextView.setText(videoTextViewText);
        videoTextViewDiksha.setText(videoTextViewTextDiksha);
        TextView textViewSubTitle = FeedBackDialog.findViewById(R.id.textViewSubTitle);
        textViewSubTitle.setText(feedback);
        TextView textViewSubTitle1 = FeedBackDialog.findViewById(R.id.textViewSubTitle1);
        if (type == 0) {
            textViewSubTitle1.setVisibility(View.VISIBLE);
            textViewSubTitle1.setText(textViewSubTitle1Text);
            textViewSubTitle1.setTypeface(null, Typeface.BOLD);
        } else {
            textViewSubTitle1.setVisibility(View.GONE);
        }
        if (type == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 50, 20, 0);
            textViewSubTitle.setLayoutParams(params);
            textViewSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        TextView textViewOkay = FeedBackDialog.findViewById(R.id.textViewOkay);
        textViewNext = FeedBackDialog.findViewById(R.id.textViewNext);

        textViewNext.setText(nextQuestionText);
//        if(type == 0){
//            textViewNext.setVisibility(View.GONE);
//        }
        View reletiveparent = FeedBackDialog.findViewById(R.id.reletiveparent);
        View videoLayout = FeedBackDialog.findViewById(R.id.videoLayout);
        FrameLayout videoThumnailLayout = FeedBackDialog.findViewById(R.id.videoThumnailLayout);
        FrameLayout videoThumnailLayoutDiksha = FeedBackDialog.findViewById(R.id.videoThumnailLayoutDiksha);
        TextView textViewMessage = FeedBackDialog.findViewById(R.id.textViewMessage);
        TextView textViewMessageDiskha = FeedBackDialog.findViewById(R.id.textViewMessageDiskha);
        /* Set Feedback on the FeedBackDialog text*/
//        textViewMessage.setText(feedback);
        textViewMessage.setText(videoName);
        textViewMessageDiskha.setText(videoName);
        VideoView videoView = FeedBackDialog.findViewById(R.id.videoView);

        if (type == 0) {
            videoTextView.setVisibility(View.VISIBLE);
            videoTextViewDiksha.setVisibility(View.VISIBLE);
            reletiveparent.setVisibility(View.VISIBLE);
        } else {
            videoTextView.setVisibility(View.GONE);
            videoTextViewDiksha.setVisibility(View.GONE);
            reletiveparent.setVisibility(View.GONE);
        }

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                String[] videoCode = videoLink.split("/");
                String videoc = videoCode[videoCode.length - 1];
                Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                intent.putExtra("videoId", videoLink);
                intent.putExtra("vidId", videoLink);
                intent.putExtra("offlineLink", getOffLineLink(videoc));
                intent.putExtra("videoName", videoName);
                intent.putExtra("from", "practice");
                startActivity(intent);
//                videoView.setVisibility(View.VISIBLE);
//                String videoCode[] = videoLink.split("/");
//                String videoc = videoCode[videoCode.length - 1];
//                try {
//                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+getOffLineLink(videoc);
//                    File file = new File(filePath);
//                    if(file.exists() && Util.isOfflineMode(context)){
//                        playVideoFromVimeo(file.toString(), videoView, true);
//                    }else{
//                        initializePlayer(videoc, videoView);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                String[] videoCode = videoLink.split("/");
                String videoc = videoCode[videoCode.length - 1];
                Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                intent.putExtra("videoId", videoLink);
                intent.putExtra("vidId", videoLink);
                intent.putExtra("offlineLink", getOffLineLink(videoc));
                intent.putExtra("videoName", videoName);
                intent.putExtra("from", "practice");
                startActivity(intent);
//                videoView.setVisibility(View.VISIBLE);
//                String videoCode[] = videoLink.split("/");
//                String videoc = videoCode[videoCode.length - 1];
//                try {
//                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+getOffLineLink(videoc);
//                    File file = new File(filePath);
//                    if(file.exists() && Util.isOfflineMode(context)){
//                        playVideoFromVimeo(file.toString(), videoView, true);
//                    }else{
//                        initializePlayer(videoc, videoView);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {

            videoDikshaLayout.setVisibility(View.VISIBLE);
            videoTextViewDiksha.setVisibility(View.VISIBLE);
        } else {
            videoDikshaLayout.setVisibility(View.GONE);
            videoTextViewDiksha.setVisibility(View.GONE);
        }
        videoDikshaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                System.out.println("=== "+ videoLink_Diksha);
                if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {
                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                    intent.putExtra("onlineLink", videoLink_Diksha);
                    intent.putExtra("videoName", videoName);
                    intent.putExtra("from", "practice");
                    startActivity(intent);
                } else {
                    if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"वीडियो उपलब्ध नहीं है");
//                        Util.showToast(context, "वीडियो उपलब्ध नहीं है");
                    }else {
//                        Util.showToast(context, "Video not available");
                        Util.openGifDialogue(context,"Video not available");
                    }

                }
            }
        });


        textViewOkay.setText(okay);
        /* Set Feedback on the FeedBackDialog text*/
        //textViewMessage.setText(feedback);


        textViewReportError.setText(error);
        textViewReportError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContentErrorDialog();
            }
        });
        if (type == 1) {
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
            {
                textViewNext.setText("जारी रखें");
            }else {
                textViewNext.setText("Continue");
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
            params.gravity = Gravity.CENTER;
            params.setMargins(0, 50, 0, 0);
            textViewNext.setLayoutParams(params);
        }

        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!hintOpen) hintCount=0;


                if (type == 0) {
                    int streakCount = streak - 1;
                    if (hintCount >= streakCount) {

                        if (optionSelected) {
                            if (streakProgress > 0) {
                                streakProgress--;
                            }
                            optionSelected = false;
                            textViewSubmit.setVisibility(View.GONE);
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                            textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                            restoreOptionsInUnselectedState();
                            newQuestionLoad = true;
                            animateLayout();
                            try {
                                /* showing new  question to the user*/
                                dataManipulation(contentArrayList, selectedLanguage);
                                hintRefresh = true;
                                //                        int streakCount = streak - 1;
                                //                        if(streakProgress == streakCount){
                                //                            if(hintHandler != null){
                                //                                hintHandler.removeCallbacksAndMessages(null);
                                //                            }
                                //                            hintImageView.setVisibility(View.GONE);
                                //                        }else{
                                showHintOption();
                                //                        }
                                hintCount++;
                                optionSelected = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FeedBackDialog.dismiss();
                        }
                    } else {

                        animateLayout();
                        int mastery = calculateMastery();
                        /* Dismis FeedBackDialog */
                        FeedBackDialog.dismiss();
                        /* Restore to fresh state */
                        restoreOptionsInUnselectedState();
                        /* Check when user match the count with Correct streak*/
                        if (streakProgress == streak) {
                            /*Restore the isStreakCompleted Streak Progress*/
                            streakDeProgress = 0;
                            streakProgress = 0;




                            if (totalNoOfLevel == level) {
                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }

                            if(mastery>=100)
                            {
                                masteryTobeSynced=100;
                                SelectTopicActivity.currentTopicMasteryChanged = 100 + "";
                                masteryTextView.setText(currentMastery + " " + 100 + "%");

                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }

                            level = level + 1;
                            /* Upgrade the level as user match the count with Correct streak */
                            newQuestionLoad = true;
                            hintCount = 0;
                            hintRefresh = false;
                            optionSelected = false;
                        }

                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Correct");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));

                        try {
                            /* showing new  question to the user*/
                            dataManipulation(contentArrayList, selectedLanguage);
                            hintRefresh = true;
                            //                        int streakCount = streak - 1;
                            //                        if(streakProgress == streakCount){
                            //                            if(hintHandler != null){
                            //                                hintHandler.removeCallbacksAndMessages(null);
                            //                            }
                            //                            hintImageView.setVisibility(View.GONE);
                            //                        }else{
                            showHintOption();
                            //                        }
                            optionSelected = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {


                    if (hintCount == 0) {
                        hintCount++;
                        hintRefresh = false;
                    }
                    else {
                        if (hintCount == 8) {
                            //show Video Dialog
                            Util.showToast(context, "Video Dialog will come here");
                        } else {
                            if (hintRefresh) {
                                hintCount++;
                                int streakCount = streak - 1;
                                if (hintCount > streakCount) {
                                    //showNextQuestionForSameLevel();
                                }
                                hintRefresh = false;
                            }
                        }

                    }
                    FeedBackDialog.dismiss();
                }

                getLevelVideos();
                show_levelUP_toast("Level Completed");
            }
        });

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    int streakCount = streak - 1;
                    if (hintCount == streakCount) {
                        if (optionSelected) {
                            if (streakProgress > 0) {
                                streakProgress--;
                            }
                            optionSelected = false;
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                            textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                            restoreOptionsInUnselectedState();
                            newQuestionLoad = true;
                            animateLayout();
                            try {
                                /* showing new  question to the user*/
                                dataManipulation(contentArrayList, selectedLanguage);
                                hintRefresh = true;
                                //                        int streakCount = streak - 1;
                                //                        if(streakProgress == streakCount){
                                //                            if(hintHandler != null){
                                //                                hintHandler.removeCallbacksAndMessages(null);
                                //                            }
                                //                            hintImageView.setVisibility(View.GONE);
                                //                        }else{
                                showHintOption();
                                //                        }
                                hintCount++;
                                optionSelected = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FeedBackDialog.dismiss();
                        }
                    } else {
                        animateLayout();
                        int mastery = calculateMastery();
                        /* Dismis FeedBackDialog */
                        FeedBackDialog.dismiss();
                        /* Restore to fresh state */
                        restoreOptionsInUnselectedState();
                        /* Check when user match the count with Correct streak*/
                        if (streakProgress == streak) {
                            /*Restore the isStreakCompleted Streak Progress*/
                            streakDeProgress = 0;
                            streakProgress = 0;

                            if (totalNoOfLevel == level) {
                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }
                            if(mastery>=100)
                            {
                                masteryTobeSynced=100;
                                SelectTopicActivity.currentTopicMasteryChanged = 100 + "";
                                masteryTextView.setText(currentMastery + " " + 100 + "%");

                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }
                            level = level + 1;
                            /* Upgrade the level as user match the count with Correct streak */
                            newQuestionLoad = true;
                            hintCount = 0;
                            hintRefresh = false;
                            optionSelected = false;
                        }

                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Correct");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));

                        try {
                            /* showing new  question to the user*/
                            dataManipulation(contentArrayList, selectedLanguage);
                            hintRefresh = true;
                            //                        int streakCount = streak - 1;
                            //                        if(streakProgress == streakCount){
                            //                            if(hintHandler != null){
                            //                                hintHandler.removeCallbacksAndMessages(null);
                            //                            }
                            //                            hintImageView.setVisibility(View.GONE);
                            //                        }else{
                            showHintOption();
                            //                        }
                            optionSelected = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {


                    if (hintCount == 0) {
                        hintCount++;
                        hintRefresh = false;
                    } else {
                        if (hintCount == 8) {
                            //show Video FeedBackDialog
                            Util.showToast(context, "Video FeedBackDialog will come here");
                        } else {
                            if (hintRefresh) {
                                hintCount++;
                                int streakCount = streak - 1;
                                if (hintCount > streakCount) {
                                    //showNextQuestionForSameLevel();
                                }
                                hintRefresh = false;
                            }
                        }

                    }
                    FeedBackDialog.dismiss();
                }

                getLevelVideos();
            }
        });
        FeedBackDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        FeedBackDialog.show();
        /* Change the background color of the FeedBackDialog */
        FeedBackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        FeedBackDialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        FeedBackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        FeedBackDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private boolean hintOpen=false;

    /** Show Hint Dialog */
    private void showHintDialog(String feedback, int type) {
//        reletiveLayoutAnimate.setVisibility(View.GONE);
        /* Increament the ten question check*/
        if (type == 0) {
            if (choosenOption.equals("Option 1")) {
                textViewOption1.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption1.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 2")) {
                textViewOption2.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption2.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 3")) {
                textViewOption3.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption3.setTextColor(Color.parseColor("#000000"));
            } else if (choosenOption.equals("Option 4")) {
                textViewOption4.setBackgroundResource(R.drawable.right_answer_new);
                textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.right_icon, 0, 0, 0);
                /*Set text color of the answers*/
                textViewOption4.setTextColor(Color.parseColor("#000000"));
            }

            tenQuestionCheck++;
            streakDeProgress = 0;
            /*Track time spend on feedback*/
            Util.saveFeedbackTime();
        }

        hintOpen=true;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pal_row_hint_dialog);
        dialog.setCancelable(false);
        /* hookup the Ui element in the dialog */
        RelativeLayout videoDikshaLayout = dialog.findViewById(R.id.videoDikshaLayout);
        TextView videoTextView = dialog.findViewById(R.id.videoTextView);
        TextView videoTextViewDiksha = dialog.findViewById(R.id.videoTextViewDiksha);
        videoTextView.setTypeface(null, Typeface.BOLD);
        videoTextViewDiksha.setTypeface(null, Typeface.BOLD);
        TextView textViewReportError = dialog.findViewById(R.id.textViewReportError);
        ImageView imageViewCross = dialog.findViewById(R.id.imageViewCross);
        ImageView imageViewFeedback = dialog.findViewById(R.id.imageViewFeedback);
//        feedbackImage="https://download.iprep.in/super_app_content/assessment_images/ncert_eng_sci_06_01_01_023_q.png";
        if (feedbackImage != null) {
            imageViewFeedback.setVisibility(View.VISIBLE);
            if (!Util.isOfflineMode(context)) {
                NoInternetConnectionTextView.setVisibility(View.GONE);
                questionImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(feedbackImage).into(imageViewFeedback);
            } else {
                NoInternetConnectionTextView.setVisibility(View.VISIBLE);
                questionImageView.setVisibility(View.GONE);
                NoInternetConnectionTextView.setText(imageInternetError);
            }

        } else {
            imageViewFeedback.setVisibility(View.GONE);
        }
        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        ImageView playbutton = dialog.findViewById(R.id.playbutton);
        ImageView playbuttonDiksha = dialog.findViewById(R.id.playbuttonDiksha);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(20));
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbutton);
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbuttonDiksha);

        String sahiUtarText,sanketText,subtitleText;
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            sahiUtarText = "सही उत्तर";
            sanketText= "संकेत";
            subtitleText = "आपका उत्तर सही है लेकिन यदि आप इस टॉपिक को और बेहतर समझना चाहते हैं तो यह उपचारात्मक वीडियो देखें";
        }else {
            sahiUtarText = "Right answer";
            sanketText= "Hint";
            subtitleText = "Your answer is correct but if you want to understand this topic better then watch this remedial video";
        }

        if (type == 0) {
            textViewTitle.setText(sahiUtarText);
        } else {
            textViewTitle.setText(sanketText);
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        TextView textViewSubTitle = dialog.findViewById(R.id.textViewSubTitle);
        textViewSubTitle.setText(feedback);
        TextView textViewSubTitle1 = dialog.findViewById(R.id.textViewSubTitle1);
        if (type == 0) {
            textViewSubTitle1.setVisibility(View.VISIBLE);
            textViewSubTitle1.setText(subtitleText);
            textViewSubTitle1.setTypeface(null, Typeface.BOLD);
        } else {
            textViewSubTitle1.setVisibility(View.GONE);
        }
        if (type == 1) {
            textViewSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewNext = dialog.findViewById(R.id.textViewNext);
        RelativeLayout reletiveparent = dialog.findViewById(R.id.reletiveparent);
        FrameLayout videoThumnailLayout = dialog.findViewById(R.id.videoThumnailLayout);
        FrameLayout videoThumnailLayoutDiksha = dialog.findViewById(R.id.videoThumnailLayoutDiksha);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        TextView textViewMessageDiskha = dialog.findViewById(R.id.textViewMessageDiskha);
        textViewMessage.setText(videoName);
        textViewMessageDiskha.setText(videoName);
        VideoView videoView = dialog.findViewById(R.id.videoView);

        if (type == 0) {
            videoTextView.setVisibility(View.VISIBLE);
            videoTextViewDiksha.setVisibility(View.VISIBLE);
            reletiveparent.setVisibility(View.VISIBLE);
        } else {
            videoTextView.setVisibility(View.GONE);
            videoTextViewDiksha.setVisibility(View.GONE);
            reletiveparent.setVisibility(View.GONE);
        }

        videoThumnailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                String[] videoCode = videoLink.split("/");
                String videoc = videoCode[videoCode.length - 1];
                Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                intent.putExtra("videoId", videoLink);
                intent.putExtra("vidId", videoLink);
                intent.putExtra("offlineLink", getOffLineLink(videoc));
                intent.putExtra("videoName", videoName);
                intent.putExtra("from", "practice");
                startActivity(intent);
            }
        });
        if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {

            videoDikshaLayout.setVisibility(View.VISIBLE);
            videoTextViewDiksha.setVisibility(View.VISIBLE);
        } else {
            videoDikshaLayout.setVisibility(View.GONE);
            videoTextViewDiksha.setVisibility(View.GONE);
        }
        videoThumnailLayoutDiksha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {
                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                    intent.putExtra("onlineLink", videoLink_Diksha);
                    intent.putExtra("videoName", videoName);
                    intent.putExtra("from", "practice");
                    startActivity(intent);
                } else {
                    if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"वीडियो उपलब्ध नहीं है");
//                        Util.showToast(context, "वीडियो उपलब्ध नहीं है");
                    }else {
//                        Util.showToast(context, "Video not available");
                        Util.openGifDialogue(context,"Video not available");
                    }
                }
            }
        });


        textViewOkay.setText(okay);
        /* Set Feedback on the Dialog text*/
        //textViewMessage.setText(feedback);


        textViewReportError.setText(error);
        textViewReportError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContentErrorDialog();
            }
        });
        if (type == 1) {
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) textViewNext.setText("जारी रखें");
            else textViewNext.setText("Continue");


        }

        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    int streakCount = streak - 1;
                    if (hintCount == streakCount) {
                        if (optionSelected) {
                            if (streakProgress > 0) {
                                streakProgress--;
                            }
                            optionSelected = false;
                            textViewSubmit.setVisibility(View.GONE);
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                            textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                            restoreOptionsInUnselectedState();
                            newQuestionLoad = true;
                            animateLayout();
                            try {
                                /* showing new  question to the user*/
                                dataManipulation(contentArrayList, selectedLanguage);
                                hintRefresh = true;
                                showHintOption();
                                hintCount++;
                                optionSelected = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    } else {
                        animateLayout();
                        int mastery = calculateMastery();
                        /* Dismis dialog */
                        dialog.dismiss();
                        /* Restore to fresh state */
                        restoreOptionsInUnselectedState();
                        /* Check when user match the count with Correct streak*/
                        if (streakProgress == streak) {
                            /*Restore the isStreakCompleted Streak Progress*/
                            streakDeProgress = 0;
                            streakProgress = 0;

                            if (totalNoOfLevel == level) {
                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }
                            if(mastery>=100)
                            {
                                masteryTobeSynced=100;
                                SelectTopicActivity.currentTopicMasteryChanged = 100 + "";
                                masteryTextView.setText(currentMastery + " " + 100 + "%");

                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }

                            level = level + 1;
                            /* Upgrade the level as user match the count with Correct streak */
                            newQuestionLoad = true;
                            hintCount = 0;
                            hintRefresh = false;
                            optionSelected = false;
                        }

                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Correct");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));

                        try {
                            /* showing new  question to the user*/
                            dataManipulation(contentArrayList, selectedLanguage);
                            hintRefresh = true;
                            showHintOption();
                            optionSelected = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (hintCount == 0) {
                        hintCount++;
                        hintRefresh = false;
                    } else {
                        if (hintCount == 8) {
                            //show Video Dialog
                            Util.showToast(context, "Video Dialog will come here");
                        } else {
                            if (hintRefresh) {
                                hintCount++;
                                int streakCount = streak - 1;
                                if (hintCount > streakCount) {
//                                    showNextQuestionForSameLevel();
                                }
                                hintRefresh = false;
                            }
                        }
                    }
                    dialog.dismiss();
                }
                getLevelVideos();
            }
        });

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    int streakCount = streak - 1;
                    if (hintCount == streakCount) {
                        if (optionSelected) {
                            if (streakProgress > 0) {
                                streakProgress--;
                            }
                            optionSelected = false;
                            textViewSubmit.setVisibility(View.GONE);
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                            textViewSubmit.setTextColor(Color.parseColor("#9e9b9b"));
                            restoreOptionsInUnselectedState();
                            newQuestionLoad = true;
                            animateLayout();
                            try {
                                /* showing new  question to the user*/
                                dataManipulation(contentArrayList, selectedLanguage);
                                hintRefresh = true;
                                //                        int streakCount = streak - 1;
                                //                        if(streakProgress == streakCount){
                                //                            if(hintHandler != null){
                                //                                hintHandler.removeCallbacksAndMessages(null);
                                //                            }
                                //                            hintImageView.setVisibility(View.GONE);
                                //                        }else{
                                showHintOption();
                                //                        }
                                hintCount++;
                                optionSelected = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    } else {
                        animateLayout();
                        int mastery = calculateMastery();
                        /* Dismis dialog */
                        dialog.dismiss();
                        /* Restore to fresh state */
                        restoreOptionsInUnselectedState();
                        /* Check when user match the count with Correct streak*/
                        if (streakProgress == streak) {
                            /*Restore the isStreakCompleted Streak Progress*/
                            streakDeProgress = 0;
                            streakProgress = 0;

                            if (totalNoOfLevel == level) {
                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }
                            if(mastery>=100)
                            {
                                masteryTobeSynced=100;
                                SelectTopicActivity.currentTopicMasteryChanged = 100 + "";
                                masteryTextView.setText(currentMastery + " " + 100 + "%");

                                showWelDoneDialog();
                                /* Send user analytics to the firebase for further graphs processing in coming version*/
                                sendAnalyticsToFireBase("Correct");
                                sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));
                                return;
                            }

                            level = level + 1;
                            /* Upgrade the level as user match the count with Correct streak */
                            newQuestionLoad = true;
                            hintCount = 0;
                            hintRefresh = false;
                            optionSelected = false;
                        }

                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Correct");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(mastery));

                        try {
                            /* showing new  question to the user*/
                            dataManipulation(contentArrayList, selectedLanguage);
                            hintRefresh = true;
                            //                        int streakCount = streak - 1;
                            //                        if(streakProgress == streakCount){
                            //                            if(hintHandler != null){
                            //                                hintHandler.removeCallbacksAndMessages(null);
                            //                            }
                            //                            hintImageView.setVisibility(View.GONE);
                            //                        }else{
                            showHintOption();
                            //                        }
                            optionSelected = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {


                    if (hintCount == 0) {
                        hintCount++;
                        hintRefresh = false;
                    } else {
                        if (hintCount == 8) {
                            //show Video Dialog
                            Util.showToast(context, "Video Dialog will come here");
                        } else {
                            if (hintRefresh) {
                                hintCount++;
                                int streakCount = streak - 1;
                                if (hintCount > streakCount) {
                                    //showNextQuestionForSameLevel();
                                }
                                hintRefresh = false;
                            }
                        }

                    }
                    dialog.dismiss();
                }
                getLevelVideos();
            }
        });
        /* show the dialog */

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

    Dialog FeedBackDialog;

    /* Incorrect Feedback dialog */
    private void showIncorrectFeedbackDialog(String feedback) {
//        reletiveLayoutAnimate.setVisibility(View.GONE);
        /* Increament the ten question check*/

        if(FeedBackDialog!=null) {
            if(FeedBackDialog.isShowing()) return;
        }

        if (choosenOption.equals("Option 1")) {
            textViewOption1.setBackgroundResource(R.drawable.wrong_answer_new);
            textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wrong_icon, 0, 0, 0);
            /*Set text color of the answers*/
            textViewOption1.setTextColor(Color.parseColor("#000000"));
        } else if (choosenOption.equals("Option 2")) {
            textViewOption2.setBackgroundResource(R.drawable.wrong_answer_new);
            textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wrong_icon, 0, 0, 0);
            /*Set text color of the answers*/
            textViewOption2.setTextColor(Color.parseColor("#000000"));
        } else if (choosenOption.equals("Option 3")) {
            textViewOption3.setBackgroundResource(R.drawable.wrong_answer_new);
            textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wrong_icon, 0, 0, 0);
            /*Set text color of the answers*/
            textViewOption3.setTextColor(Color.parseColor("#000000"));
        } else if (choosenOption.equals("Option 4")) {
            textViewOption4.setBackgroundResource(R.drawable.wrong_answer_new);
            textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wrong_icon, 0, 0, 0);
            /*Set text color of the answers*/
            textViewOption4.setTextColor(Color.parseColor("#000000"));
        }

        tenQuestionCheck++;
        /*Restore the Correct Streak Progress*/
        streakProgress = 0;
        /* Increase Incorrect streak progress by 1 */
        streakDeProgress++;
        /*Track time spend on feedback*/
        Util.saveFeedbackTime();
        FeedBackDialog = new Dialog(context);
        FeedBackDialog.setContentView(R.layout.pal_row_feedback_dialog);
        FeedBackDialog.setCancelable(false);
        TextView videoTextView = FeedBackDialog.findViewById(R.id.videoTextView);

        TextView videoTextViewDiksha = FeedBackDialog.findViewById(R.id.videoTextViewDiksha);
        videoTextView.setTypeface(null, Typeface.BOLD);
        videoTextViewDiksha.setTypeface(null, Typeface.BOLD);
        ImageView playbutton = FeedBackDialog.findViewById(R.id.playbutton);
        ImageView imageViewCross = FeedBackDialog.findViewById(R.id.imageViewCross);
        ImageView playbuttonDiksha = FeedBackDialog.findViewById(R.id.playbuttonDiksha);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(20));
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbutton);
        Glide.with(context).load(R.mipmap.video_icon).apply(requestOptions).into(playbuttonDiksha);

        String galatUttar,Sanket,textViewSubTitle1Text,videoNotAvailable,nextQuestionText,videoTextViewText,videoTextViewTextDiksha;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            galatUttar ="गलत जवाब";
            Sanket ="संकेत";
            videoNotAvailable ="वीडियो उपलब्ध नहीं है";
            textViewSubTitle1Text ="आपका उत्तर सही नहीं है लेकिन आपको केवल आगे बढ़ने के बारे में ही सोचना है। आप निम्न वीडियो को देखकर अपनी समझ को और पक्का कर सकते हैं ।";
            nextQuestionText ="अगला प्रश्न";
            videoTextViewText ="वीडियो (iPrep)";
            videoTextViewTextDiksha ="वीडियो (Diksha)";
        }else {
            galatUttar ="Wrong Answer";
            Sanket ="Hint";
            videoNotAvailable ="Video not available";
            textViewSubTitle1Text ="Your answer is incorrect. To improve your clarity on this question, you can watch the video lessons below and then move onto the next question.";
            nextQuestionText ="Next Question";
            videoTextViewText ="Video (iPrep)";
            videoTextViewTextDiksha ="Video (Diksha)";
        }

        TextView textViewTitle = FeedBackDialog.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextColor(Color.parseColor("#FF7575"));
        textViewTitle.setText(galatUttar);
        TextView textViewSubTitle = FeedBackDialog.findViewById(R.id.textViewSubTitle);
        textViewSubTitle.setText(feedback);
        TextView textViewSubTitle1 = FeedBackDialog.findViewById(R.id.textViewSubTitle1);
        textViewSubTitle1.setText(textViewSubTitle1Text);
        textViewSubTitle1.setTypeface(null, Typeface.BOLD);
        textViewSubTitle1.setTextColor(Color.parseColor("#E84C4A"));

        videoTextView.setText(videoTextViewText);
        videoTextViewDiksha.setText(videoTextViewTextDiksha);

        /** set incorrect image */
        ImageView correctImageView=FeedBackDialog.findViewById(R.id.correctImageView);
        correctImageView.setImageResource(R.drawable.ic_incorrect_sign);

        /* hookup the Ui element in the FeedBackDialog */
        final TextView textViewReportError = FeedBackDialog.findViewById(R.id.textViewReportError);
        textViewReportError.setText(error);
        ImageView imageViewFeedback = FeedBackDialog.findViewById(R.id.imageViewFeedback);
        View reletiveparent = FeedBackDialog.findViewById(R.id.reletiveparent);
        View videoLayout = FeedBackDialog.findViewById(R.id.videoLayout);
        View videoDikshaLayout = FeedBackDialog.findViewById(R.id.videoDikshaLayout);
        FrameLayout videoThumnailLayout = FeedBackDialog.findViewById(R.id.videoThumnailLayout);
        FrameLayout videoThumnailLayoutDiksha = FeedBackDialog.findViewById(R.id.videoThumnailLayoutDiksha);
        TextView textViewMessage = FeedBackDialog.findViewById(R.id.textViewMessage);
        TextView textViewMessageDiskha = FeedBackDialog.findViewById(R.id.textViewMessageDiskha);
        /* Set Feedback on the FeedBackDialog text*/
//        textViewMessage.setText(feedback);
        textViewMessage.setText(videoName);
        textViewMessageDiskha.setText(videoName);
        VideoView videoView = FeedBackDialog.findViewById(R.id.videoView);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                String[] videoCode = videoLink.split("/");
                String videoc = videoCode[videoCode.length - 1];
                Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                intent.putExtra("videoId", videoLink);
                intent.putExtra("vidId", videoLink);
//                intent.putExtra("onlineLink",videoc);
                intent.putExtra("offlineLink", getOffLineLink(videoc));
                intent.putExtra("videoName", videoName);
                intent.putExtra("from", "practice");
                startActivity(intent);

//                reletiveparent.setVisibility(View.GONE);
//                videoView.setVisibility(View.VISIBLE);
//                String videoCode[] = videoLink.split("/");
//                String videoc = videoCode[videoCode.length - 1];
//                try {
//                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+getOffLineLink(videoc);
//                    File file = new File(filePath);
//                    if(file.exists() && Util.isOfflineMode(context)){
//                        playVideoFromVimeo(file.toString(), videoView, true);
//                    }else{
//                        initializePlayer(videoc, videoView);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {

            videoDikshaLayout.setVisibility(View.VISIBLE);
            videoTextViewDiksha.setVisibility(View.VISIBLE);
        } else {
            videoDikshaLayout.setVisibility(View.GONE);
            videoTextViewDiksha.setVisibility(View.GONE);
        }

        videoDikshaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (videoLink_Diksha != null && !videoLink_Diksha.equals("undefined")) {
                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                    intent.putExtra("onlineLink", videoLink_Diksha);
                    intent.putExtra("videoName", videoName);
                    intent.putExtra("from", "practice");
                    startActivity(intent);
                } else {
                    // Util.showToast(context, videoNotAvailable);
                    Util.openGifDialogue(context,videoNotAvailable);
//                    if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                    {
//                        Util.openGifDialogue(context,"वीडियो उपलब्ध नहीं है");
////                        Util.showToast(context, "वीडियो उपलब्ध नहीं है");
//                    }else {
////                        Util.showToast(context, "Video not available");
//                        Util.openGifDialogue(context,"Video not available");
//                    }
                }
            }
        });
//        feedbackImage="https://download.iprep.in/super_app_content/assessment_images/ncert_eng_sci_06_01_01_023_q.png";

        if (feedbackImage != null) {
            imageViewFeedback.setVisibility(View.VISIBLE);
            if (Util.checkInternetConnection(context)) {
                NoInternetConnectionTextView.setVisibility(View.GONE);
                questionImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(feedbackImage).into(imageViewFeedback);
            } else {
                NoInternetConnectionTextView.setVisibility(View.VISIBLE);
                questionImageView.setVisibility(View.GONE);
                NoInternetConnectionTextView.setText(imageInternetError);
            }

        } else {
            imageViewFeedback.setVisibility(View.GONE);
        }
        TextView textViewOkay = FeedBackDialog.findViewById(R.id.textViewOkay);
        textViewNext = FeedBackDialog.findViewById(R.id.textViewNext);
        textViewNext.setText(nextQuestionText);
//        textViewNext.setVisibility(View.GONE);

        textViewOkay.setText(okay);
        /* Click listners on UI element of the FeedBackDialog */
        textViewReportError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContentErrorDialog();
            }
        });
        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLayout();
                /* Dismis FeedBackDialog */
                FeedBackDialog.dismiss();
                /* Show this FeedBackDialog when 10 question attempted*/
//                if (tenQuestionCheck == 10) {
//                    tenQuestionCheck = 0;
//                    showTenQuestionCompleteDialog();
//                }
                /* Restore to fresh state */
                restoreOptionsInUnselectedState();
                /* Check when user match the count with incorrect streak*/
                if (streakDeProgress == incorrectStreak) {
                    /*Restore the Incorrect Streak Progress*/
                    streakDeProgress = 0;
                    streakProgress = 0;
                    hintCount = 0;
                    hintRefresh = false;
                    if (level == 1) {
                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Incorrect");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(calculateMastery()));
                        /* get questions from the Firebase (mapped according to TopicID) */
                        try {

                            showFoundationDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        if (practiceCount == 1) {
                            //  showWelDoneDialog();
                            showLastLevelVideos("not");
                            return;
                        } else {
                            newQuestionLoad = true;
                            /* Degrade the level as user match the count with incorrect streak */
                            level = level - 1;
                            // here list of videos will come for popup
                            practiceCount++;
                        }
                    }
                }
                /* Send user analytics to the firebase for further graphs processing in coming version*/
                sendAnalyticsToFireBase("Incorrect");
                sendAnalyticsMasteryNodeToFireBase(String.valueOf(calculateMastery()));
                try {
                    /* showing new  question to the user*/
                    dataManipulation(contentArrayList, selectedLanguage);
                    hintRefresh = true;
//                    int streakCount = streak - 1;
//                    if(streakProgress == streakCount){
//                        if(hintHandler != null){
//                            hintHandler.removeCallbacksAndMessages(null);
//                        }
//                        hintImageView.setVisibility(View.GONE);
//                    }else{
                    showHintOption();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getLevelVideos();
            }
        });

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLayout();
                /* Dismis FeedBackDialog */
                FeedBackDialog.dismiss();
                /* Show this FeedBackDialog when 10 question attempted*/
//                if (tenQuestionCheck == 10) {
//                    tenQuestionCheck = 0;
//                    showTenQuestionCompleteDialog();
//                }
                /* Restore to fresh state */
                restoreOptionsInUnselectedState();
                /* Check when user match the count with incorrect streak*/
                if (streakDeProgress == incorrectStreak) {
                    /*Restore the Incorrect Streak Progress*/
                    streakDeProgress = 0;
                    streakProgress = 0;
                    hintCount = 0;
                    hintRefresh = false;
                    if (level == 1) {
                        /* Send user analytics to the firebase for further graphs processing in coming version*/
                        sendAnalyticsToFireBase("Incorrect");
                        sendAnalyticsMasteryNodeToFireBase(String.valueOf(calculateMastery()));
                        /* get questions from the Firebase (mapped according to TopicID) */

                        try {

                            showFoundationDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {

                        if (practiceCount == 1) {
                            //  showWelDoneDialog();

                            showLastLevelVideos("not");
                            return;
                        } else {
                            newQuestionLoad = true;
                            /* Degrade the level as user match the count with incorrect streak */
                            level = level - 1;
                            // here list of videos will come for popup
                            practiceCount++;
                        }
                    }
                }
                /* Send user analytics to the firebase for further graphs processing in coming version*/
                sendAnalyticsToFireBase("Incorrect");
                sendAnalyticsMasteryNodeToFireBase(String.valueOf(calculateMastery()));
                try {
                    /* showing new  question to the user*/
                    dataManipulation(contentArrayList, selectedLanguage);
                    hintRefresh = true;
//                    int streakCount = streak - 1;
//                    if(streakProgress == streakCount){
//                        if(hintHandler != null){
//                            hintHandler.removeCallbacksAndMessages(null);
//                        }
//                        hintImageView.setVisibility(View.GONE);
//                    }else{
                    showHintOption();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getLevelVideos();
            }
        });

        FeedBackDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        FeedBackDialog.show();
        /* Change the background color of the FeedBackDialog */
        FeedBackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        FeedBackDialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        FeedBackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        FeedBackDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    private void showLastLevelVideos(String types) {

        //saving details of foundational topic for maintaining path for a student and taking him to do
        //practice or watch video if he/she exits from the app

        showResult(true);

//        reletiveTop.setVisibility(View.GONE);
//        reletiveMasteryfailed.setVisibility(View.VISIBLE);
//        TextView textViewDetailfailed = findViewById(R.id.textViewDetailfailed);
//        textViewDetailfailed.setVisibility(View.VISIBLE);
//
//        String threewrongans,textLayout1Text,textLayout2Text,subTextLayout1Text,
//                subTextLayout2Text,textViewScoreDetailText,infofailedText,foundationText,textViewDetailfailed1,
//                textViewDetailfailed2,textViewContinoueText,orText_;
//
//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            threewrongans =" आपने लगातार तीन प्रश्नों के गलत उत्तर दिए हैं";
//            textLayout1Text="100% महारत हासिल करें";
//            textLayout2Text ="फाइनल टेस्ट में 100% स्कोर";
//            subTextLayout1Text ="अनलॉक हो गया है";
//            subTextLayout2Text ="बंद है";
//            textViewScoreDetailText =Util.getUsername(context) + ", आपका प्रश्नोत्तरी स्कोर है";
//            infofailedText ="और फिर वापिस " + topicName + " पर आते हैं";
//            foundationText ="मूलभूत विषय का अभ्यास करें";
//            textViewDetailfailed1 ="इसलिए आओ " + topicName + " से पहले इसके मूलभूत टॉपिक मूलभूत अध्याय को पूरी तरह समझते हैं";
//            textViewDetailfailed2 ="इसलिए आओ " + topicName + " को पूरी तरह उपचारात्मक वीडियो के माध्यम से समझते हैं";
//            textViewContinoueText ="स्तर " + level + " के वीडियो पाठ को देखें";
//            orText_ ="या";
//
//        }else {
//            threewrongans =" you have given wrong answers to three questions of the same learning level.";
//            textLayout1Text="Achieve 100% mastery";
//            textLayout2Text ="100% score in the final test";
//            subTextLayout1Text ="Unlocked";
//            subTextLayout2Text ="Closed";
//            textViewScoreDetailText =Util.getUsername(context) + "is your quiz score";
//            infofailedText ="And then back to " + topicName + "";
//            infofailedText ="";
//            foundationText ="Practice Foundational topics";
//            textViewDetailfailed2 ="So come before " + topicName + " let's fully understand its basic topic basic chapter";
//            textViewDetailfailed1 ="Therefore, iPrep PAL suggests that you understand " + topicName + " through the conceptual video lesson of the recommended level below, and then come back to practice and master "+topicName+" again.";
//            textViewContinoueText ="Watch the video lesson of Level " + level + "";
//            orText_ ="Or";
//        }
//
//
//        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
//
//        TextView textViewdMessaegfailed = findViewById(R.id.textViewdMessaegfailed);
//        textViewdMessaegfailed.setText(Util.getUsername(context) + threewrongans);
//        textViewdMessaegfailed.setTypeface(null, Typeface.BOLD);
//
//        TextView textLayout1 = findViewById(R.id.textLayout1);
//        textLayout1.setText(textLayout1Text);
//        TextView textLayout2 = findViewById(R.id.textLayout2);
//        textLayout2.setText(textLayout2Text);
//        TextView subTextLayout1 = findViewById(R.id.subTextLayout1);
//        subTextLayout1.setText(subTextLayout1Text);
//        TextView subTextLayout2 = findViewById(R.id.subTextLayout2);
//        subTextLayout2.setText(subTextLayout2Text);
//        TextView testScoreLowerLevel = findViewById(R.id.testScoreLowerLevel);
//        testScoreLowerLevel.setText(masteryTobeSynced + "%");
//        TextView textViewScoreDetail = findViewById(R.id.textViewScoreDetail);
//        textViewScoreDetail.setText(textViewScoreDetailText);
//
//        TextView infofailed = findViewById(R.id.infofailed);
//        infofailed.setText(Html.fromHtml(infofailedText));
//        TextView textViewContinoue = findViewById(R.id.textViewWatchVideosfailed);
//        textViewPractice = findViewById(R.id.buttonTestfailed);
//        textViewPractice.setText(foundationText);
//        TextView orText = findViewById(R.id.orText);
//        orText.setText(orText_);
//
//        if (GetTopicLevelsDetails.isJuniorTopicAvailable()) {
//            textViewPractice.setVisibility(View.VISIBLE);
//            orText.setVisibility(View.VISIBLE);
//            textViewDetailfailed.setText(Html.fromHtml(textViewDetailfailed1));
//        } else {
//            textViewPractice.setVisibility(View.GONE);
//            orText.setVisibility(View.GONE);
//            textViewDetailfailed.setText(Html.fromHtml(textViewDetailfailed2));
//        }
//
//        textViewPractice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.preventTwoClick(v);
//                getJuniorLevelPractice();
//            }
//        });
//        textViewContinoue.setText(textViewContinoueText);
////        textViewContinoue.setText("Watch Level " + level + " Videos");
//        textViewContinoue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.preventTwoClick(v);
//                Spanned topicN = Html.fromHtml(topicName);
//                Intent intent;
//
//                if(type.equals("foundation"))
//                {
////                    Util.showToast(context, "Separate section of videos will be made");
//                    intent = new Intent(context, ExtraContentListingActivity.class);
//                    Util.setVideoLevel(context, (level - 1));
//                    Util.setLevel(context, (level - 1));
//                    intent.putExtra("board", board);
//                    intent.putExtra("sClass", sClass);
//                    intent.putExtra("type", "foundationalTopicVideos");
//                    intent.putExtra("subject", Util.getSubject(context));
//                    intent.putExtra("subjectName", Util.getSubjectName(context));
//                    intent.putExtra("topicId", topicId);
//                    intent.putExtra("topicName", topicN.toString());
//                    intent.putExtra("testPercentageAchieved", testPercentageAchieved);
//
//                }
//                else
//                {
//                    com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.backPressed = true;
//                    String subject = Util.getSubject(context);
//                    intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.autoplayLevelVideo=true;
//                    Util.setVideoLevel(context, (level - 1));
//                    Util.setLevel(context, (level - 1));
//                    intent.putExtra("subject", subject);
//                    intent.putExtra("sClass", sClass);
//                    intent.putExtra("board", board);
//                    intent.putExtra("subjectName", subject);
//                    intent.putExtra("icon", "");
//
//                }
//
//                startActivity(intent);
//
//                finish();
//            }
//        });
//        // TextView TxtExit = FeedBackDialog.findViewById(R.id.TxtExit);
////        if (type.equalsIgnoreCase("last")) {
////            TxtExit.setVisibility(View.GONE);
////        } else {
////            TxtExit.setVisibility(View.GONE);
////            TxtExit.setText("Continue Practice");
////        }


    }

    /** Clear foundation topic mastery | type = "foundation" or "practice" */
    private void clearMastery(String topicId,String type) {
        String date = Util.getCurrentDateWithDifferentFormat();
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
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
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
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }
        Util.setUnlockVideoLevel(context,topicId, 1);
        HashMap<String, String> map = new HashMap<>();
    }

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
                String incorrectStreak = object_______.getString("incorrectStreak");
                String TName = object_______.getString("TName");
                if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") ||Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इस विषय में अन्य मूलभूत विषय उपलब्ध नहीं हैं।");
                    }else {
                        Util.openGifDialogue(context,"Other foundational topics are not available in this topic.");
                    }
//                    PalContentListingActivity.instance.setTopicIdList(topicId);
//                    PalContentListingActivity.instance.setNextTopicPosition(topicId);
                } else {
//                    String seniorTopicID = topicId;
//                    String seniorClass = sClass;
//                    String seniorTopicName = TName;

                    String sClass = foundational_class;
//                    Util.setClassNameSelection(context, foundational_class);
//                    Util.setClassSelection(context, foundational_class);
//                    Util.setClassNameSelectionDummy(context, foundational_class);
//                    Util.setClassSelectionDummy(context, foundational_class);
                    String streakProgress = "0";

                    // getting lower topic Name
                    String filePath1 = ".iDream_content/offlinetab_PAL/Class" + sClass +"_core_content.txt";
                    JSONObject jsonObject1 = Util.readJsonFile(context, filePath1);
                    JSONObject object__1 = jsonObject1.getJSONObject(Util.getSelectedLanguage(context));
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
                        foundationalTopicModel.setVideoLevel(level);
                        foundationalTopicModel.setTestPercentageAchieved(testPercentageAchieved);
                        foundationalTopicModel.setLang(Util.getSelectedLanguage(context));
                        foundationalTopicRepository.insertFoundationalTopicDetails(foundationalTopicModel);
                    } else {
                        foundationalTopicRepository.updateFields(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, TName1, sClass, streakProgress, StreakCount, incorrectStreak, "junior", true, level, testPercentageAchieved,Util.getSelectedLanguage(context));
                    }
//                    String date = Util.getCurrentDateWithDifferentFormat();
//
//                    ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
//                    reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
//                    reportsLatestDataPracticeModel.setBoard(board);
//                    reportsLatestDataPracticeModel.setSClass(sClass);
//                    reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
//                    reportsLatestDataPracticeModel.setType("practice");
//                    reportsLatestDataPracticeModel.setTopicId(Foundational_Topic_ID);
//                    reportsLatestDataPracticeModel.setCurrentLevel(level + "");
//                    reportsLatestDataPracticeModel.setPDate(date);
//                    reportsLatestDataPracticeModel.setMastery(String.valueOf("0"));
//                    reportsLatestDataPracticeModel.setStreakProgress(streakProgress+ "");
//                    reportsLatestDataPracticeModel.setTopicName(TName1);
//                    reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
//                    reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

//                    updateFoundationTopicData();

                    if(PalContentListingActivity.instance!=null) PalContentListingActivity.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                    else if(PalGlobalSearchActivity.palGlobalSearchActivity!=null) PalGlobalSearchActivity.palGlobalSearchActivity.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(seniorClass).child(Util.getSubject(context)).child(seniorTopicID).child("FoundationalPractice").setValue("F");

                    String date = Util.getCurrentDateWithDifferentFormat();

                    ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
                    reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
                    reportsLatestDataPracticeModel.setBoard(board);
                    reportsLatestDataPracticeModel.setSClass(sClass);
                    reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
                    reportsLatestDataPracticeModel.setType("practice");
                    reportsLatestDataPracticeModel.setTopicId(Foundational_Topic_ID);
                    reportsLatestDataPracticeModel.setCurrentLevel(level + "");
                    reportsLatestDataPracticeModel.setPDate(date);
                    reportsLatestDataPracticeModel.setMastery(String.valueOf("0"));
                    reportsLatestDataPracticeModel.setStreakProgress(streakProgress+ "");
                    reportsLatestDataPracticeModel.setTopicName(TName1);
                    reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
                    reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

                    if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                        trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                    } else {
                        TrackTopicModel trackTopicModel = new TrackTopicModel();
                        trackTopicModel.setUserId(Util.getUserId(context));
                        trackTopicModel.setSubject(Util.getSubject(context));
                        trackTopicModel.setBoard(board);
                        trackTopicModel.setSeniorTopicId(topicId);
                        trackTopicModel.setSeniorClass(this.sClass);
                        trackTopicModel.setSeniorTopicName(topicName);
                        trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                        trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                        trackTopicRepository.insertDetails(trackTopicModel);
                    }

                    if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId)) {
                        reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass,  Util.getSubject(context), topicId, "F");
                    } else {
                        ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                        reportsTopicPathModel.setUserId(Util.getUserId(context));
                        reportsTopicPathModel.setBoard(board);
                        reportsTopicPathModel.setSClass(seniorClass);
                        reportsTopicPathModel.setSubject( Util.getSubject(context));
                        reportsTopicPathModel.setTopicId(topicId);
                        reportsTopicPathModel.setTypeV("F");
                        reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                    }

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
                            String foundational_class = detailHashMap.get("foundational_class");
                            String streak = detailHashMap.get("StreakCount");
                            String incorrectStreak = detailHashMap.get("incorrectStreak");
                            String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                            String TName = detailHashMap.get("TName");
                            if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") ||Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इस विषय में अन्य मूलभूत विषय उपलब्ध नहीं हैं।");
                                }else {
                                    Util.openGifDialogue(context,"Other foundational topics are not available in this topic.");
                                }
//                                PalContentListingActivity.instance.setTopicIdList(topicId);
//                                PalContentListingActivity.instance.setNextTopicPosition(topicId);
                            } else {
//                                String seniorTopicID = topicId;
//                                String seniorClass = sClass;
//                                String seniorTopicName = TName;

                                String sClass = foundational_class;
                                String streakProgress = "0";

                                String subject = Util.getSubject(context);

                                if(Foundational_Topic_ID.contains("sci"))
                                {
                                    if(!Foundational_Topic_ID.contains("pol")) subject="science";
                                }
                                else if (Foundational_Topic_ID.contains("evs")) {
                                    subject="evs";
                                }

                                // getting lower topic Name
                                String finalSubject = subject;
                                global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(subject).child("topics").child(Foundational_Topic_ID).child("TName").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            if (snapshot.getValue() != null) {
                                                String name = (String) snapshot.getValue();

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
                                                    foundationalTopicModel.setVideoLevel(level);
                                                    foundationalTopicModel.setTestPercentageAchieved(testPercentageAchieved);
                                                    foundationalTopicModel.setLang(Util.getSelectedLanguage(context));
                                                    foundationalTopicRepository.insertFoundationalTopicDetails(foundationalTopicModel);
                                                } else {
                                                    foundationalTopicRepository.updateFields(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, name, sClass, streakProgress, streak, incorrectStreak, "junior", true, level, testPercentageAchieved,Util.getSelectedLanguage(context));
                                                }
                                                System.out.println("--========== seniorTopicID "+seniorTopicID);
                                                getJuniorLevelPractice(false);
//                                                PalContentListingActivity.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                if(PalContentListingActivity.instance!=null) PalContentListingActivity.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                else if(PalGlobalSearchActivity.palGlobalSearchActivity!=null) PalGlobalSearchActivity.palGlobalSearchActivity.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                if(PalContentListingActivity_Mobile.instance!=null) PalContentListingActivity_Mobile.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                                                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(seniorClass).child(Util.getSubject(context)).child(seniorTopicID).child("FoundationalPractice").setValue("F");

                                                String date = Util.getCurrentDateWithDifferentFormat();

                                                ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
                                                reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
                                                reportsLatestDataPracticeModel.setBoard(board);
                                                reportsLatestDataPracticeModel.setSClass(sClass);
                                                reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
                                                reportsLatestDataPracticeModel.setType("practice");
                                                reportsLatestDataPracticeModel.setTopicId(Foundational_Topic_ID);
                                                reportsLatestDataPracticeModel.setCurrentLevel(level + "");
                                                reportsLatestDataPracticeModel.setPDate(date);
                                                reportsLatestDataPracticeModel.setMastery(String.valueOf("0"));
                                                reportsLatestDataPracticeModel.setStreakProgress(streakProgress+ "");
                                                reportsLatestDataPracticeModel.setTopicName(name);
                                                reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
                                                reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

                                                if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                                                    trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicID, seniorClass, seniorTopicName, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                                                } else {
                                                    TrackTopicModel trackTopicModel = new TrackTopicModel();
                                                    trackTopicModel.setUserId(Util.getUserId(context));
                                                    trackTopicModel.setSubject(Util.getSubject(context));
                                                    trackTopicModel.setBoard(board);
                                                    trackTopicModel.setSeniorTopicId(topicId);
                                                    trackTopicModel.setSeniorClass(seniorClass);
                                                    trackTopicModel.setSeniorTopicName(topicName);
                                                    trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                                                    trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                                                    trackTopicRepository.insertDetails(trackTopicModel);
                                                }

                                                if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId)) {
                                                    reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass,  Util.getSubject(context), topicId, "F");
                                                } else {
                                                    ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                                    reportsTopicPathModel.setUserId(Util.getUserId(context));
                                                    reportsTopicPathModel.setBoard(board);
                                                    reportsTopicPathModel.setSClass(seniorClass);
                                                    reportsTopicPathModel.setSubject( Util.getSubject(context));
                                                    reportsTopicPathModel.setTopicId(topicId);
                                                    reportsTopicPathModel.setTypeV("F");
                                                    reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
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

    private void saveFoundationalDetail() {
        if (trackTopic != null) {
            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + trackClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject("practice");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONObject object_______ = object______.getJSONObject(trackTopic);

                String Display = object_______.getString("Display");
                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                String IsModelTestPaper = object_______.getString("IsModelTestPaper");
                String Levels = object_______.getString("Levels");
                String Next_topic_id = object_______.getString("Next_topic_id");
//                        String next_chapter_name = object_______.getString("next_chapter_name");
                String StreakCount = object_______.getString("StreakCount");
                String TName = object_______.getString("TName");
                String TName_alt = object_______.getString("TName_alt");
                String TopicID = object_______.getString("TopicID");
                String foundational_class = object_______.getString("foundational_class");
                String incorrectStreak = object_______.getString("incorrectStreak");
                String isAlternateLanguageAvailable = object_______.getString("isAlternateLanguageAvailable");


                HashMap<String, String> practiceMap = new HashMap<>();
                practiceMap.put("Display", Display);
                practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
                practiceMap.put("IsModelTestPaper", IsModelTestPaper);
                practiceMap.put("Levels", Levels);
                practiceMap.put("Next_topic_id", Next_topic_id);
                practiceMap.put("StreakCount", StreakCount);
                practiceMap.put("TName", TName);
                practiceMap.put("TName_alt", TName_alt);
                practiceMap.put("TopicID", TopicID);
                practiceMap.put("foundational_class", foundational_class);
                practiceMap.put("incorrectStreak", incorrectStreak);
                practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);

                if (!trackClass.equals(Util.getSelectedClass(context))) {
                    if (!foundationalTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, trackTopic,Util.getSelectedLanguage(context))) {
                        FoundationalTopicModel foundationalTopicModel = new FoundationalTopicModel();
                        foundationalTopicModel.setUserId(Util.getUserId(context));
                        foundationalTopicModel.setSubjectId(Util.getSubject(context));
                        foundationalTopicModel.setTopicId(trackTopic);
                        foundationalTopicModel.setTopicName(TName);
                        foundationalTopicModel.setSClass(trackClass);
                        foundationalTopicModel.setStreakProgress("0");
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
                        foundationalTopicRepository.updateFields(Util.getUserId(context), Util.getSubject(context), seniorTopicID, seniorClass, seniorTopicName, trackTopic, TName, trackClass, "0", StreakCount, incorrectStreak, "junior", true, 0, testPercentageAchieved,Util.getSelectedLanguage(context));
                    }
                    if(PalContentListingActivity.instance!=null) PalContentListingActivity.instance.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                    else if(PalGlobalSearchActivity.palGlobalSearchActivity!=null) PalGlobalSearchActivity.palGlobalSearchActivity.setPath(board, seniorClass, Util.getSubject(context), seniorTopicID, "F");
                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(seniorClass).child(Util.getSubject(context)).child(seniorTopicID).child("FoundationalPractice").setValue("F");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openSuccessDialog() {

        //update the saved foundational topic data for completion
        updateFoundationTopicData();

        String date = Util.getCurrentDateWithDifferentFormat();

        //For Latest Data
        if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

            reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId);

            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

        } else {
            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }

        showResult(false);

//        reletiveMasteryCompleted.setVisibility(View.VISIBLE);
//        reletiveTop.setVisibility(View.GONE);
//        TextView textViewContinoue = findViewById(R.id.textViewWatchVideos);
//        TextView textViewdUserName = findViewById(R.id.textViewdUserName);
////        textViewdUserName.setText(Util.getUsername(context));
//        TextView textViewdMessaeg = findViewById(R.id.textViewdMessaeg);
////        textViewdMessaeg.setText(Util.getUsername(context) + ", इस विषय में महारत हासिल करने के लिए लक्ष्य है");
//        textViewdMessaeg.setTypeface(null, Typeface.BOLD);
////        textViewdMessaeg.setText("बहुत बढ़िया");
//        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
//        String threewrongans,textLayout1Text,textLayout2Text,subTextLayout1Text,
//                subTextLayout2Text,textViewScoreDetailText,infofailedText,foundationText,textViewDetailfailed1,
//                textViewDetailfailed2,textViewContinoueText,orText_,bahutBadhiyaText,orTextMain;
//
//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            threewrongans ="आपने " + topicName + " के अभ्यास में महारत पायी है।";
//            textLayout1Text="100% महारत हासिल करें";
//            textLayout2Text ="फाइनल टेस्ट में 100% स्कोर";
//            subTextLayout1Text ="अनलॉक हो गया है";
//            subTextLayout2Text ="बंद है";
//            textViewScoreDetailText ="आगे बढ़ने के लिए वापिस वरिष्ठ विषय का अभ्यास करें ";
//            infofailedText ="इस अध्याय को पूरा करने के लिए आओ अंतिम परिक्षण दें";
//            foundationText ="पूरा हुआ!";
//            textViewDetailfailed1 = Util.getUsername(context) + ", आपने अभ्यास में 100% महारत हासिल कर ली है और इसके लिए अंतिम परीक्षा को अनलॉक कर दिया है:\n";
//            textViewDetailfailed2 ="आपने " + topicName + " के अभ्यास में महारत हासिल कर ली है।";
//            textViewContinoueText ="अब आप वरिष्ठ विषय को समझने में सक्षम होंगे।";
//            orText_ ="अन्य विषय जानें";
//            bahutBadhiyaText ="बहुत बढ़िया";
//            orTextMain ="या";
//
//        }else {
//            threewrongans ="You have mastered the practice of " + topicName + ".";
//            textLayout1Text="Achieve 100% mastery";
//            textLayout2Text ="100% score in the final test";
//            subTextLayout1Text ="Unlocked";
//            subTextLayout2Text ="Closed";
//            textViewScoreDetailText ="Go back to the senior subject to move forward";
//            infofailedText ="Let's take the final test to complete this chapter";
//            foundationText ="It's finished!";
//            textViewDetailfailed1 =Util.getUsername(context) + ", you have mastered the practice 100% and unlocked the final exam for:\n";
//            textViewDetailfailed2 ="You have mastered the practice of " + topicName + ".";
//            textViewContinoueText ="Now you will be able to understand the senior subject.";
//            orText_ ="Learn other topics";
//            bahutBadhiyaText ="Very Good";
//            orTextMain = "Or";
//        }
//        textViewdMessaeg.setText(bahutBadhiyaText);
//        TextView textLayout1Success = findViewById(R.id.textLayout1Success);
//        textLayout1Success.setText(textLayout1Text);
//        TextView textLayout2Success = findViewById(R.id.textLayout2Success);
//        textLayout2Success.setText(textLayout2Text);
//        TextView subTextLayout1Success = findViewById(R.id.subTextLayout1Success);
//        subTextLayout1Success.setText(foundationText);
//        TextView subTextLayout2Success = findViewById(R.id.subTextLayout2Success);
//        subTextLayout2Success.setText(subTextLayout1Text);
//        TextView orText = findViewById(R.id.orTextSuccess);
//        orText.setText(orTextMain);
//        TextView successTextViewScoreDetail = findViewById(R.id.successTextViewScoreDetail);
//        successTextViewScoreDetail.setText(textViewDetailfailed1 + Util.getTopicNameAlt(context));
//
//        TextView textViewDetail = findViewById(R.id.textViewDetail);
//
//        if (trackTopic != null) {
//
//            textViewdUserName.setVisibility(View.VISIBLE);
//            textViewdUserName.setText(Html.fromHtml(textViewDetailfailed2));
//            textViewDetail.setText(textViewContinoueText);
//            info.setText(Html.fromHtml(textViewScoreDetailText));
//        } else {
//            topicName = Util.getTopicNameAlt(context);
//            textViewdUserName.setVisibility(View.GONE);
//            textViewDetail.setVisibility(View.VISIBLE);
////            textViewDetail.setText("इस विषय में १००% मास्टरी प्राप्त करके आपने इसकी परीक्षा को अनलॉक कर लिया हैं");
//            textViewDetail.setText(Html.fromHtml(threewrongans));
//            info.setText(infofailedText);
////            info.setText("इस विषय में १००% मास्टरी प्राप्त करके आपने इसकी परीक्षा को अनलॉक कर लिया हैं\n\nयदि आप परीक्षा में ८० प्रतिशत से ज़्यादा अंक लाते हैं तो आप इस विषय में मास्टरी प्राप्त करके अगले विषय को अनलॉक कर इसकी डायग्नोस्टिक परिक्षण को कर पाएंगे\n८०% से काम अंक आने पर आपको इस विषय में मास्टरी प्राप्त करने के लिए मार्गदर्शित किया जाएगा");
////            textViewDetail.setText(Util.getUsername(context) +
////                    "१००% मास्टरी प्राप्त करनी होगी इस विषय के टेस्ट को क्लियर करना होगा" +
////                    "आपने " + Util.getTopicNameAlt(context) + " पर १००% मास्टरी करके इस विषय की आखरी परीक्षा को अनलॉक कर लिया है");
//
//        }
//
//        //  TextView textViewPractice = findViewById(R.id.textViewPractice);
//        textViewContinoue.setText(orText_);
//        textViewContinoue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.preventTwoClick(v);
//                com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.backPressed = true;
//                String subject = Util.getSubject(context);
//                String subjectName = Util.getSubjectName(context);
//                String icon = com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.icon;
//                Intent intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("subject", subject);
//                intent.putExtra("sClass", sClass);
//                intent.putExtra("board", board);
//                intent.putExtra("subjectName", subjectName);
//                intent.putExtra("icon", icon);
//                intent.putExtra("getPath", "yes");
//                Util.setTopicID(context, topicId);
//                com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.autoplayLevelVideo=true;
//                Util.setVideoLevel(context, (level - 1));
//                Util.setLevel(context, (level - 1));
//                startActivity(intent);
//
//                finish();
//
//            }
//        });
//        TextView TxtExit = findViewById(R.id.buttonTest);

//        String nextTopicMainText, startExamText;
//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            nextTopicMainText = "अभ्यास करें";
//            startExamText = "परीक्षा शुरू करें";
//        }else {
//            nextTopicMainText = "Start Practice next Topic";
//            startExamText = "Start the exam";
//        }


//        TxtExit.setVisibility(View.VISIBLE);
//        if (trackTopic != null) {
//            TxtExit.setText(nextTopicMainText);
////            TxtExit.setText("Practice Senior Topic");
//            saveFoundationalDetail();
////            textViewContinoue.setVisibility(View.GONE);
//        } else {
//            TxtExit.setText(startExamText);
////            TxtExit.setText("Start Test");
//        }
//
//        String foundationalText;
//
//        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//        {
//            foundationalText = "कृपया पहले मुलभुत अध्याय का परिक्षण करें";
//        }else {
//            foundationalText = "Please do foundational practice first";
//        }
//
//
//        TxtExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.preventTwoClick(v);
//                if (trackTopic != null) {
//
//                    if (Util.isOfflineMode(context))
//                    {
//                        try {
//                            String filePath = ".iDream_content/offlinetab_PAL/Class" + trackClass + "_core_content.txt";
//                            JSONObject jsonObject = Util.readJsonFile(context, filePath);
//                            JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
//                            JSONObject object___ = object__.getJSONObject("practice");
//                            JSONObject object____ = object___.getJSONObject("content");
//                            JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
//                            JSONObject object______ = object_____.getJSONObject("topics");
//                            JSONObject object_______ = object______.getJSONObject(trackTopic);
//
//                            String Display = object_______.getString("Display");
//                            String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
//                            String IsModelTestPaper = object_______.getString("IsModelTestPaper");
//                            String Levels = object_______.getString("Levels");
//                            String Next_topic_id = object_______.getString("Next_topic_id");
//                            String StreakCount = object_______.getString("StreakCount");
//                            String TName = object_______.getString("TName");
//                            String TName_alt = object_______.getString("TName_alt");
//                            String TopicID = object_______.getString("TopicID");
//                            String foundational_class = object_______.getString("foundational_class");
//                            String incorrectStreak = object_______.getString("incorrectStreak");
//                            String isAlternateLanguageAvailable = object_______.getString("isAlternateLanguageAvailable");
//
//
//                            HashMap<String, String> practiceMap = new HashMap<>();
//                            practiceMap.put("Display", Display);
//                            practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
//                            practiceMap.put("IsModelTestPaper", IsModelTestPaper);
//                            practiceMap.put("Levels", Levels);
//                            practiceMap.put("Next_topic_id", Next_topic_id);
//                            practiceMap.put("StreakCount", StreakCount);
//                            practiceMap.put("TName", TName);
//                            practiceMap.put("TName_alt", TName_alt);
//                            practiceMap.put("TopicID", TopicID);
//                            practiceMap.put("foundational_class", foundational_class);
//                            practiceMap.put("incorrectStreak", incorrectStreak);
//                            practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);
//                            Util.setTopicID(context, trackTopic);
////                        Util.setClassNameSelection(context, trackClass);
////                        Util.setClassNameSelectionDummy(context, trackClass);
////                        Util.setClassSelection(context, trackClass);
////                        Util.setClassSelectionDummy(context, trackClass);
////                        Util.setTopicNameAlt(context, next_chapter_name);
//                            Util.setTopicNameAlt(context, TName);
//                            global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(topicId).removeValue();
//
//                            trackTopicRepository.delete(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), topicId);
//
//                            HashMap<String, String> mapF = new HashMap<>();
//                            mapF.put("foundational_class", foundational_class);
//                            mapF.put("foundational_topic", Foundational_Topic_ID);
//                            global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);
//
//                            global.setProgress(0);
//                            if (!trackClass.equals(Util.getSelectedClass(context))) {
//                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));
//                            } else {
//                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));
//                            }
//
//                            finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//
//                        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(trackClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(trackTopic).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                try {
//                                    if (snapshot.getValue() != null) {
//                                        HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
//
//
//
//                                        String Display = detailHashMap.get("Display");
//                                        String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
//                                        String IsModelTestPaper = detailHashMap.get("IsModelTestPaper");
//                                        String Levels = detailHashMap.get("Levels");
//                                        String Next_topic_id = detailHashMap.get("Next_topic_id");
//                                        String StreakCount = detailHashMap.get("StreakCount");
//                                        String TName = detailHashMap.get("TName");
//                                        String TName_alt = detailHashMap.get("TName_alt");
//                                        String TopicID = detailHashMap.get("TopicID");
//                                        String foundational_class = detailHashMap.get("foundational_class");
//                                        String incorrectStreak = detailHashMap.get("incorrectStreak");
//                                        String isAlternateLanguageAvailable = detailHashMap.get("isAlternateLanguageAvailable");
//
//
//                                        HashMap<String, String> practiceMap = new HashMap<>();
//                                        practiceMap.put("Display", Display);
//                                        practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
//                                        practiceMap.put("IsModelTestPaper", IsModelTestPaper);
//                                        practiceMap.put("Levels", Levels);
//                                        practiceMap.put("Next_topic_id", Next_topic_id);
//                                        practiceMap.put("StreakCount", StreakCount);
//                                        practiceMap.put("TName", TName);
//                                        practiceMap.put("TName_alt", TName_alt);
//                                        practiceMap.put("TopicID", TopicID);
//                                        practiceMap.put("foundational_class", foundational_class);
//                                        practiceMap.put("incorrectStreak", incorrectStreak);
//                                        practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);
//                                        Util.setTopicID(context, trackTopic);
////                        Util.setClassNameSelection(context, trackClass);
////                        Util.setClassNameSelectionDummy(context, trackClass);
////                        Util.setClassSelection(context, trackClass);
////                        Util.setClassSelectionDummy(context, trackClass);
////                        Util.setTopicNameAlt(context, next_chapter_name);
//                                        Util.setTopicNameAlt(context, TName);
//                                        global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(topicId).removeValue();
//
//                                        trackTopicRepository.delete(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), topicId);
//
//                                        HashMap<String, String> mapF = new HashMap<>();
//                                        mapF.put("foundational_class", foundational_class);
//                                        mapF.put("foundational_topic", Foundational_Topic_ID);
//                                        global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);
//
//                                        Util.setLevel(context,1);
//                                        global.setProgress(0);
//                                        if (!trackClass.equals(Util.getSelectedClass(context))) {
//                                            startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0));
//                                        } else {
//                                            startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0));
//                                        }
//
//                                        finish();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//
//
//                } else {
//
//                    if (com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
//                    {
//                        // Util.showToast(context, foundationalText);
//                        Util.openGifDialogue(context, foundationalText);
//                    }else {
//                        String topicName = Util.getTopicNameAlt(context);
//                        Util.setTopicID(context, topicId);
//                        Util.setTopicNameAlt(context, topicName);
//                        if (Util.isNetworkAvailable(context))
//                        {
//                            startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));
//
//                            finish();
//                        } else {
//                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                            {
//                                Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
//                            }else {
//                                Util.openGifDialogue(context,"Internet Connection is not working");
//                            }
//                        }
//                    }
//                }
//
//
//            }
//        });

    }

    private void playAudio() {
        AssetFileDescriptor afd = null;
        try {
            afd = getApplicationContext().getAssets().openFd("yaymp3-6326.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer player = new MediaPlayer();
        try {
            assert afd != null;
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }

    public void updateFoundationTopicData() {
        String date = Util.getCurrentDateWithDifferentFormat();

        foundationalTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), topicId, seniorTopicID, false);

        //For Latest Data
        if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

            reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId);

            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

        } else {
            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }

    }

    //*****  Show Weldon Dialog when user complete the mastery of Topic  *****//
    private void showWelDoneDialog() {
        openSuccessDialog();
    }

    //*****                   END                    *****//
    /* Foundation Level  dialog */
    private void showFoundationDialog() {
        showLastLevelVideos("last");
    }
    //*****                   END                    *****//

    //*****  Show for confirmation when user click on back button  *****//
    private void showCloseMidwayDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_close_midway);
        dialog.setCancelable(false);
        /* Hook up the UI element in the Dialog */
        TextView textViewClose = dialog.findViewById(R.id.textViewClose);
        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);


        String textViewCloseText,textViewTitleText,about_to_complete_text,close_midway_text,textViewOkayText;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            about_to_complete_text ="क्या आप अभ्यास के मध्य में बाहर जाना चाहते हैं? इस विषय में १००% महारत से आप कुछ ही प्रश्न दूर हैं";
            close_midway_text ="क्या आप अभ्यास के मध्य में बाहर जाना चाहते हैं?";
            textViewOkayText ="अभ्यास को जारी रखें";
            textViewCloseText ="बाहर जाएँ";
        }else {
            about_to_complete_text ="Do you want to go out in the middle of practice? With 100% mastery in this subject, you are just a few questions away.";
            close_midway_text ="Do you wish to exit before achieving 100% mastery?";
            textViewOkayText ="No, Keep Practicing";
            textViewCloseText ="Yes, Exit";
        }

        textViewTitle.setText("");
        if (masteryTobeSynced > 20) {
            textViewMessage.setText(about_to_complete_text);

        } else {

            textViewMessage.setText(close_midway_text);
        }
        textViewOkay.setText(textViewOkayText);
        textViewClose.setText(textViewCloseText);

//        textViewTitle.setText(closeMidway);
//        textViewMessage.setText(message);
//        textViewOkay.setText(completeQuiz);
//        textViewClose.setText(closeAnyways);
        /* Click listners on the dialog elements*/
        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Dismis MidwayDialog */
                dialog.dismiss();
                /* Finish activity */
                closeActivity();
            }
        });
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Dismis MidwayDialog */
                dialog.dismiss();
                /* User wants to complete the quiz*/
                isClosePoped = false;
            }
        });
        /* show the dialog */

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

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);

        if(iPrepVideoPlayerActivity.videoView!=null)
        {
            iPrepVideoPlayerActivity.videoView.seekTo(videocurrentPosition);
        }

        if (isShareDialogOpen) {
            closeActivity();
        }
    }

    private void getUserAnalytics(String TopicId) {
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).child(Util.getSubject(context)).child(TopicId).child(ApplicationConstants.ANALYTICS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    /* Crash may accur here */
                    Util.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    HashMap<String, String> masteryMap = (HashMap<String, String>) dataSnapshot.getValue();
                    String mastery = masteryMap.get("mastery");
                    String currentLevel = masteryMap.get("currentLevel");
                    progress.setProgress(Integer.parseInt(mastery));
                    level = Integer.parseInt(currentLevel);

                    /* get questions from firebase */
                    getQuestions(topicId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getTimeOnFirebase() {

        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).
                child(Util.getSubject(context)).child(topicId).
                child("time")
                .child(ApplicationConstants.TOTAL_TIME).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            time = (String) dataSnapshot.getValue();
                        } else {
                            time = "0";
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return time;
    }


    private void animateLayout() {
        Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.slide_in);
        reletiveLayoutAnimate.startAnimation(bottomUp);
        reletiveLayoutAnimate.setVisibility(View.VISIBLE);
    }

    boolean reportSaved = false;

    void saveReport() {

        if(reportSaved) {
            return;
        }
        reportSaved = true;
        try {
            if(attemptingtest)
            {
                String date = Util.getCurrentDateWithDifferentFormat();

                MasteryNodeModel masteryNodeModel = new MasteryNodeModel();
                masteryNodeModel.setMastery(String.valueOf(masteryTobeSynced));
                masteryNodeModel.setCurrentLevel(level + "");
                masteryNodeModel.setStreakProgress(streakProgress + "");
                masteryNodeModel.setTopicName(topicNameTextView.getText().toString());
                masteryNodeModel.setDate(date);

                if(Util.getUnlockVideoLevel(context,topicId)<level)
                    Util.setUnlockVideoLevel(context,topicId,level);

                endTime = System.currentTimeMillis();
                long timeTosync = endTime - startTime;
                long ttime=timeTosync;
                practiceNumber = practiceNumber + 1;
                timeTosync = timeTosync + timeget;

                String name=Util.getTopicNameAlt(context);

                if(name==null) {
                    name="";
                }

                HashMap<String, String> row_usage = new HashMap<>();
                row_usage.put("category","practice");
                row_usage.put("language",Util.getSelectedLanguage(context));
                row_usage.put("class",Util.getSelectedClass(context));
                row_usage.put("content_name",name.trim());
                row_usage.put("topic_name",name.trim());
                row_usage.put("district",Util.getDistrict(context));
                row_usage.put("mastery",String.valueOf(masteryTobeSynced));
                row_usage.put("schoolID",Util.getSchoolId(context));
                row_usage.put("schoolName",Util.getSchoolName(context));
                row_usage.put("projectId",Util.getProjectId(context));
//                row_usage.put("score",streakProgress + "");
                row_usage.put("level",level + "");
                row_usage.put("state",Util.getSelectedState(context));
                row_usage.put("subject",Util.getSubject(context));
                row_usage.put("subject_name",Util.getSubjectName(context));
                row_usage.put("time_spent",""+ttime);
                row_usage.put("topic",""+topicId);
                row_usage.put("userId",Util.getUserId(context));
                row_usage.put("username",Util.getUsername(context));
                row_usage.put("userIdFirebase",Util.getLoginUserId(context));

                row_usage.put("board", Util.getSelectedBoard(context));
                row_usage.put("category_name", "Practice");
                row_usage.put("userType", "students");
                saveAssignedReport();

                global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
                global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_")).setValue(timeTosync);

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("practice").setValue(practiceNumber);

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("practice").child(Util.getSubjectName(context)).child(date).child("" + System.currentTimeMillis()).setValue(masteryNodeModel);

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("practice").child(date).child(current_topicId).child("name").setValue(topicNameTextView.getText().toString());

                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("practice").child(date).child(current_topicId).child("detail").child("" + System.currentTimeMillis()).setValue(masteryNodeModel);


                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubjectName(context)).child("practice").child(topicId).child("detail").setValue(masteryNodeModel);

                //For Time Spent
                if (reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, Util.getSubject(context),Util.getSelectedLanguage(context))) {
                    reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, Util.getSubject(context), timeTosync,Util.getSelectedLanguage(context));
                } else {
                    ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
                    reportsTimeSpentModel.setUserId(Util.getUserId(context));
                    reportsTimeSpentModel.setBoard(board);
                    reportsTimeSpentModel.setSClass(sClass);
                    reportsTimeSpentModel.setSubject(Util.getSubject(context));
                    reportsTimeSpentModel.setDate(date);
                    reportsTimeSpentModel.setTime(timeTosync);
                    reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
                    reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
                }

                //For Count
                if (reportsCountRepository.isDataExist(Util.getUserId(context), board, sClass, date, "practice")) {
                    reportsCountRepository.updateField(Util.getUserId(context), board, sClass, date, "practice", practiceNumber);
                } else {
                    ReportsCountModel reportsCountModel = new ReportsCountModel();
                    reportsCountModel.setUserId(Util.getUserId(context));
                    reportsCountModel.setBoard(board);
                    reportsCountModel.setSClass(sClass);
                    reportsCountModel.setDate(date);
                    reportsCountModel.setType("practice");
                    reportsCountModel.setCount(practiceNumber);
                    reportsCountRepository.insertCountDetails(reportsCountModel);
                }

                //For Date Wise
                if (reportsDateWisePracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", date, System.currentTimeMillis())) {
                    reportsDateWisePracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", date, System.currentTimeMillis(),
                            level + "", date, masteryTobeSynced + "", streakProgress + "", Util.getTopicNameAlt(context));
                } else {
                    ReportsDateWisePracticeModel reportsDateWisePracticeModel = new ReportsDateWisePracticeModel();
                    reportsDateWisePracticeModel.setUserId(Util.getUserId(context));
                    reportsDateWisePracticeModel.setBoard(board);
                    reportsDateWisePracticeModel.setSClass(sClass);
                    reportsDateWisePracticeModel.setSubject(Util.getSubject(context));
                    reportsDateWisePracticeModel.setDate(date);
                    reportsDateWisePracticeModel.setTime(System.currentTimeMillis());
                    reportsDateWisePracticeModel.setType("practice");
                    reportsDateWisePracticeModel.setCurrentLevel(level + "");
                    reportsDateWisePracticeModel.setMastery(masteryTobeSynced + "");
                    reportsDateWisePracticeModel.setPDate(date);
                    reportsDateWisePracticeModel.setStreakProgress(streakProgress + "");
                    reportsDateWisePracticeModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsDateWisePracticeRepository.insertPracticeDetails(reportsDateWisePracticeModel);
                }

                //For Topic Wise
                if (reportsTopicWisePracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))) {
                    reportsTopicWisePracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,
                            date, System.currentTimeMillis(), Util.getTopicNameAlt(context), level + "", date, masteryTobeSynced + "", streakProgress + "", Util.getTopicNameAlt(context),Util.getSelectedLanguage(context));
                } else {
                    ReportsTopicWisePracticeModel reportsTopicWisePracticeModel = new ReportsTopicWisePracticeModel();
                    reportsTopicWisePracticeModel.setUserId(Util.getUserId(context));
                    reportsTopicWisePracticeModel.setBoard(board);
                    reportsTopicWisePracticeModel.setSClass(sClass);
                    reportsTopicWisePracticeModel.setSubject(Util.getSubject(context));
                    reportsTopicWisePracticeModel.setDate(date);
                    reportsTopicWisePracticeModel.setType("practice");
                    reportsTopicWisePracticeModel.setTime(System.currentTimeMillis());
                    reportsTopicWisePracticeModel.setTopicId(topicId);
                    reportsTopicWisePracticeModel.setCurrentLevel(level + "");
                    reportsTopicWisePracticeModel.setMastery(masteryTobeSynced + "");
                    reportsTopicWisePracticeModel.setPDate(date);
                    reportsTopicWisePracticeModel.setName(Util.getTopicNameAlt(context));
                    reportsTopicWisePracticeModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsTopicWisePracticeModel.setStreakProgress(streakProgress + "");
                    reportsTopicWisePracticeModel.setLang(Util.getSelectedLanguage(context));
                    reportsTopicWisePracticeRepository.insertPracticeDetails(reportsTopicWisePracticeModel);
                }

                //For Latest Data
                if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

                    reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId);

                    ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
                    reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
                    reportsLatestDataPracticeModel.setBoard(board);
                    reportsLatestDataPracticeModel.setSClass(sClass);
                    reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
                    reportsLatestDataPracticeModel.setType("practice");
                    reportsLatestDataPracticeModel.setTopicId(topicId);
                    reportsLatestDataPracticeModel.setCurrentLevel(level + "");
                    reportsLatestDataPracticeModel.setPDate(date);
                    reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
                    reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
                    reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

                } else {
                    ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
                    reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
                    reportsLatestDataPracticeModel.setBoard(board);
                    reportsLatestDataPracticeModel.setSClass(sClass);
                    reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
                    reportsLatestDataPracticeModel.setType("practice");
                    reportsLatestDataPracticeModel.setTopicId(topicId);
                    reportsLatestDataPracticeModel.setCurrentLevel(level + "");
                    reportsLatestDataPracticeModel.setPDate(date);
                    reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
                    reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
                    reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
                    reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
                }

                // will only work if the content is assigned
                if (isAssigned != null) {
                    global.getDatabaseReference().child("content_assignement_batch_wise").child(teacherID).child(batchId).child(datetostore).child(keyTo).child("student").child(Util.getUserId(context)).child("progress").setValue(String.valueOf(masteryTobeSynced));
                }

                reportSaved = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        saveReport();
        super.onDestroy();
//        destroyService();
    }

    private void destroyService() {
        if (serviceIntent != null) {
            context.stopService(serviceIntent);
        }
    }

    private void openContentErrorDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_content_error);
        dialog.setCancelable(false);
        TextInputLayout inputEmail = dialog.findViewById(R.id.inputEmail);
        inputEmail.setHint(enterMessage);
        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText(error);
        TextView textViewSubmitReport = dialog.findViewById(R.id.textViewSubmitReport);
        textViewSubmitReport.setText(report);
        TextView textViewBack = dialog.findViewById(R.id.textViewBack);
        textViewBack.setText(restartMessage2);
        final EditText errorMessageEditText = dialog.findViewById(R.id.errorMessageEditText);
        CheckBox checkbox1 = dialog.findViewById(R.id.checkbox1);
        CheckBox checkbox2 = dialog.findViewById(R.id.checkbox2);
        CheckBox checkbox3 = dialog.findViewById(R.id.checkbox3);
        CheckBox checkbox4 = dialog.findViewById(R.id.checkbox4);
        checkbox1.setText(checkMessage1);
        checkbox2.setText(checkMessage2);
        checkbox3.setText(checkMessage3);
        checkbox4.setText(checkMessage4);
        /* Check change listners*/
        checkbox1.setOnCheckedChangeListener(this);
        checkbox2.setOnCheckedChangeListener(this);
        checkbox3.setOnCheckedChangeListener(this);
        checkbox4.setOnCheckedChangeListener(this);
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textViewSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = errorMessageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(reason1) && TextUtils.isEmpty(reason2) && TextUtils.isEmpty(reason3) && TextUtils.isEmpty(reason4)) {
                    Util.showToast(context, selectOptionMessage);
//                    Toast.makeText(context, selectOptionMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                long timeinMillies = Calendar.getInstance().getTimeInMillis();
                /* Collecting data in the model*/
                ContentErrorModel contentErrorModel = new ContentErrorModel();
                contentErrorModel.setMessage(message);
                contentErrorModel.setTopicId(topicId);
                contentErrorModel.setQuestionId(questionID);
                contentErrorModel.setReason1(reason1);
                contentErrorModel.setReason2(reason2);
                contentErrorModel.setReason3(reason3);
                contentErrorModel.setReason4(reason4);
                contentErrorModel.setUserName(Util.getUsername(context));
                contentErrorModel.setUserId(Util.getUserId(context));
                contentErrorModel.setTimeOfReport(timeinMillies);
                contentErrorModel.setLevel(level + "-" + questionNo);
                Log.i("Level : Question", level + "-" + questionNo + " : " + questionID);
                contentErrorModel.setsClass(sClass);
                contentErrorModel.setBoard(Util.getSelectedBoardName(context).toUpperCase());

                contentErrorModel.setLanguage("Hindi");
                /* Sending data to firebase*/
                global.getDatabaseReference().child(ApplicationConstants.CONTENT_ISSUES).child(timeinMillies + "").setValue(contentErrorModel);
                /* Dismis dialog*/
                dialog.dismiss();
                Util.showToast(context, feedbackSuccess);

//                Toast.makeText(context, feedbackSuccess, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(compoundButton.getId() == R.id.checkbox1) {
            if (b) {
                reason1 = compoundButton.getText().toString();
            } else {
                reason1 = "";
            }
        }
        else if(compoundButton.getId() == R.id.checkbox2) {
            if (b) {
                reason2 = compoundButton.getText().toString();
            } else {
                reason2 = "";
            }
        }
        else if(compoundButton.getId() == R.id.checkbox3) {
            if (b) {
                reason3 = compoundButton.getText().toString();
            } else {
                reason3 = "";
            }
        }
        else if(compoundButton.getId() == R.id.checkbox4) {
            if (b) {
                reason4 = compoundButton.getText().toString();
            } else {
                reason4 = "";
            }
        }


    }

    private void getJuniorLevelPractice(boolean startPractice) {
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
                String incorrectStreak = object_______.getString("incorrectStreak");
                String TName = object_______.getString("TName");
                if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
//                    Util.showToast(context, "Not Available");
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इस विषय में अन्य मूलभूत विषय उपलब्ध नहीं हैं।");
                    }else {
                        Util.openGifDialogue(context,"This is the base level for this concept. Please learn this again and then go back to practicing and mastering this topic to learn further. In case you cannot, please get in touch with your teacher or a guide for guidance and learning support. ");
                    }
                } else {
                    String seniorTopicIDForJuniorTopic = topicId;
                    String seniorClassForJuniorTopic = sClass;
                    String seniorTopicNameForJuniorTopic = TName;
                    topicId = Foundational_Topic_ID;
                    Util.setTopicID(context, Foundational_Topic_ID);
                    Util.setLevel(context, Integer.parseInt("1"));
                    String sClass = foundational_class;
//                    Util.setClassNameSelection(context, foundational_class);
//                    Util.setClassSelection(context, foundational_class);
//                    Util.setClassNameSelectionDummy(context, foundational_class);
//                    Util.setClassSelectionDummy(context, foundational_class);
                    String streakProgress = "0";
                    global.setProgress(0);
                    // getting lower topic Name
                    String filePath1 = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                    JSONObject jsonObject1 = Util.readJsonFile(context, filePath1);
                    JSONObject object__1 = jsonObject1.getJSONObject(Util.getSelectedLanguagePackage(context));
                    JSONObject object___1 = object__1.getJSONObject("practice");
                    JSONObject object____1 = object___1.getJSONObject("content");
                    JSONObject object_____1 = object____1.getJSONObject(Util.getSubject(context));
                    JSONObject object______1 = object_____1.getJSONObject("topics");
                    JSONObject object_______1 = object______1.getJSONObject(topicId);
                    String TName1 = object_______1.getString("TName");
                    Util.setTopicNameAlt(context, TName1);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("sClass", seniorClassForJuniorTopic);
                    map.put("seniorTopicID", seniorTopicIDForJuniorTopic);


                    global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Foundational_Topic_ID).setValue(map);

                    if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                        trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicIDForJuniorTopic, seniorClassForJuniorTopic, seniorTopicNameForJuniorTopic, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                    } else {
                        TrackTopicModel trackTopicModel = new TrackTopicModel();
                        trackTopicModel.setUserId(Util.getUserId(context));
                        trackTopicModel.setSubject(Util.getSubject(context));
                        trackTopicModel.setBoard(board);
                        trackTopicModel.setSeniorTopicId(seniorTopicIDForJuniorTopic);
                        trackTopicModel.setSeniorClass(seniorClassForJuniorTopic);
                        trackTopicModel.setSeniorTopicName(seniorTopicNameForJuniorTopic);
                        trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                        trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                        trackTopicRepository.insertDetails(trackTopicModel);
                    }

                    HashMap<String, String> mapF = new HashMap<>();
                    mapF.put("foundational_class", sClass);
                    mapF.put("foundational_topic", Foundational_Topic_ID);
                    global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);

                    System.out.println("============ sClass 2 "+sClass);
                    global.setProgress(0);
                    startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));

                    finish();
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
                            String foundational_class = detailHashMap.get("foundational_class");
                            String streak = detailHashMap.get("StreakCount");
                            String incorrectStreak = detailHashMap.get("incorrectStreak");
                            String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                            String TName = detailHashMap.get("TName");
                            if (Foundational_Topic_ID.equalsIgnoreCase("Not Available") || Foundational_Topic_ID.equalsIgnoreCase("Not Available ")) {
//                                Util.showToast(context, "Not Available");
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इस विषय में अन्य मूलभूत विषय उपलब्ध नहीं हैं।");
                                }else {
                                    Util.openGifDialogue(context,"This is the base level for this concept. Please learn this again and then go back to practicing and mastering this topic to learn further. In case you cannot, please get in touch with your teacher or a guide for guidance and learning support.");
                                }
//                                textViewPractice.setVisibility(View.GONE);
                            } else {
//                                String seniorTopicID = topicId;
//                                String seniorClass = sClass;
//                                String seniorTopicName = TName;

                                if(!startPractice)
                                {
                                    clearMastery(Foundational_Topic_ID,"practice");

                                    String seniorTopicIDForJuniorTopic = topicId;
                                    String seniorClassForJuniorTopic = sClass;
                                    String seniorTopicNameForJuniorTopic = TName;
//                                    topicId = Foundational_Topic_ID;
//                                    Util.setTopicID(context, Foundational_Topic_ID);
                                    Util.setLevel(context, Integer.parseInt("1"));
                                    String sClass = foundational_class;
                                    String streakProgress = "0";
                                    global.setProgress(0);

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("sClass", seniorClassForJuniorTopic);
                                    map.put("seniorTopicID", seniorTopicIDForJuniorTopic);
                                    global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguage(context)).child(Foundational_Topic_ID).setValue(map);


                                    return;
                                }
                                else
                                {

                                    String seniorTopicIDForJuniorTopic = topicId;
                                    String seniorClassForJuniorTopic = sClass;
                                    String seniorTopicNameForJuniorTopic = TName;
                                    topicId = Foundational_Topic_ID;
                                    Util.setTopicID(context, Foundational_Topic_ID);
                                    Util.setLevel(context, Integer.parseInt("1"));
                                    String sClass = foundational_class;
                                    String streakProgress = "0";
                                    global.setProgress(0);

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("sClass", seniorClassForJuniorTopic);
                                    map.put("seniorTopicID", seniorTopicIDForJuniorTopic);
                                    global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguage(context)).child(Foundational_Topic_ID).setValue(map);

                                    if (trackTopicRepository.isDataExist(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), Foundational_Topic_ID)) {
                                        trackTopicRepository.updateField(Util.getUserId(context), Util.getSubject(context), board, seniorTopicIDForJuniorTopic, seniorClassForJuniorTopic, seniorTopicNameForJuniorTopic, Foundational_Topic_ID, Util.getSelectedLanguagePackage(context));
                                    } else {
                                        TrackTopicModel trackTopicModel = new TrackTopicModel();
                                        trackTopicModel.setUserId(Util.getUserId(context));
                                        trackTopicModel.setSubject(Util.getSubject(context));
                                        trackTopicModel.setBoard(board);
                                        trackTopicModel.setSeniorTopicId(seniorTopicIDForJuniorTopic);
                                        trackTopicModel.setSeniorClass(seniorClassForJuniorTopic);
                                        trackTopicModel.setSeniorTopicName(seniorTopicNameForJuniorTopic);
                                        trackTopicModel.setFoundationalTopicId(Foundational_Topic_ID);
                                        trackTopicModel.setLanguage(Util.getSelectedLanguagePackage(context));
                                        trackTopicRepository.insertDetails(trackTopicModel);
                                    }

                                    HashMap<String, String> mapF = new HashMap<>();
                                    mapF.put("foundational_class", sClass);
                                    mapF.put("foundational_topic", Foundational_Topic_ID);
                                    global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);



                                    // getting lower topic Name
                                    getlower_foundational_topic(Foundational_Topic_ID,sClass,Util.getSubject(context)
                                            ,streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName
                                            ,testPercentageAchieved);

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

    int classs=11;

    private void getlower_foundational_topic(String Foundational_Topic_ID,String sClass,String foundation_subject,String streakProgress,String streak,String incorrectStreak,String seniorClass,String seniorTopicID,String seniorTopicName,int testPercentageAchieved) {
        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(foundation_subject).child("topics").child(topicId).child("TName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        String name = (String) snapshot.getValue();
                        Util.setTopicNameAlt(context, name);
                        global.setProgress(0);
                        startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));

                        finish();
                    }else {

                        if(classs>0)
                        {
                            System.out.println("------ Foundational_Topic_ID "+Foundational_Topic_ID);
                            System.out.println("------ sClass  "+sClass);
                            System.out.println("------ foundation_subject  "+foundation_subject);
                            System.out.println("-------------------------------\n\n  ");

                            if (Foundational_Topic_ID.contains("pol_sci"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"political_science",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (Foundational_Topic_ID.contains("sci"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"science",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Foundational_Topic_ID.contains("gra"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"geography",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Foundational_Topic_ID.contains("eco"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"economics",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Foundational_Topic_ID.contains("eng_gr"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"english_grammar",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Foundational_Topic_ID.contains("his"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"history",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Foundational_Topic_ID.contains("evs"))
                            {
                                try {
                                    classs--;
                                    getlower_foundational_topic(Foundational_Topic_ID,String.valueOf(classs),"evs",streakProgress,streak,incorrectStreak,seniorClass,seniorTopicID,seniorTopicName,testPercentageAchieved);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

    private void showDetailFeedbackDialog(String question) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_feedback_detail);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText("");
        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView textViewFeedback = dialog.findViewById(R.id.textViewFeedback);
        textViewFeedback.setText(question);

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

    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject("english");
                JSONArray array = object.getJSONArray("Question Answer screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    btnChangeLanguageEnglish.setText(textArrayList.get(0));
//                    textViewSubmit.setText(textArrayList.get(1));
//                    textViewSubmit.setText("Submit Answer");
                    closeMidway = textArrayList.get(2);
                    message = textArrayList.get(3);
                    completeQuiz = textArrayList.get(23);
                    closeAnyways = textArrayList.get(5);
                    incorrectMessage = textArrayList.get(6);
                    okay = textArrayList.get(7);
                    correctMessage = textArrayList.get(8);
                    goBack = textArrayList.get(9);
                    share = textArrayList.get(10);
                    endMessage = textArrayList.get(11);
                    waitString = textArrayList.get(12);
                    weldone = textArrayList.get(13);
                    emptyMessage = textArrayList.get(14);
                    greatAttempt = textArrayList.get(15);
                    tenMessage = textArrayList.get(16);
                    facebookInstallMessage = textArrayList.get(17);
                    switchTopic = textArrayList.get(18);
                    exit = textArrayList.get(19);
                    topicMaster = textArrayList.get(22);
                    enterMessage = textArrayList.get(24);
                    chatBox = textArrayList.get(25);
                    comingSoon = textArrayList.get(26);
                    message1 = textArrayList.get(27);
                    message2 = textArrayList.get(28);
                    message3 = textArrayList.get(29);
                    chooseAnotherTopic = textArrayList.get(30);
                    selectOptionMessage = textArrayList.get(31);
                    feedbackSuccess = textArrayList.get(32);
                    imageInternetError = textArrayList.get(33);
                    tourMessage = textArrayList.get(34);
                    currentMastery = "Mastery";//textArrayList.get(35);
                    readMore = textArrayList.get(36);
                    restartMessage = textArrayList.get(37);
                    restartMessage1 = textArrayList.get(38);
                    restartMessage2 = textArrayList.get(39);
                    howItWork = textArrayList.get(40);
                    download = textArrayList.get(41);
                    error = textArrayList.get(42);
                    checkMessage1 = textArrayList.get(43);
                    checkMessage2 = textArrayList.get(44);
                    checkMessage3 = textArrayList.get(45);
                    checkMessage4 = textArrayList.get(46);
                    report = textArrayList.get(47);
                    textViewChatBox.setText(chatBox);
                    masteryTobeSynced=global.getProgress();
                    masteryTextView.setText(Html.fromHtml( currentMastery +" " + global.getProgress() + "%"));
                    if (global.getProgress() == 100) {
                        masteryCompleteDialog();
                        return;
                    }

                    textViewReadMore.setText(readMore);
                }
                getPracticeCount();
            } catch (Exception e) {
                e.printStackTrace();
                Util.dismissdataDialog();
            }
        } else
        {


            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Question Answer screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            btnChangeLanguageEnglish.setText(textArrayList.get(0));
//                            textViewSubmit.setText(textArrayList.get(1));
//                            textViewSubmit.setText("Submit Answer");
                            closeMidway = textArrayList.get(2);
                            message = textArrayList.get(3);
                            completeQuiz = textArrayList.get(23);
                            closeAnyways = textArrayList.get(5);
                            incorrectMessage = textArrayList.get(6);
                            okay = textArrayList.get(7);
                            correctMessage = textArrayList.get(8);


                            goBack = textArrayList.get(9);
                            share = textArrayList.get(10);
                            endMessage = textArrayList.get(11);
                            waitString = textArrayList.get(12);
                            weldone = textArrayList.get(13);
                            emptyMessage = textArrayList.get(14);
                            greatAttempt = textArrayList.get(15);
                            tenMessage = textArrayList.get(16);
                            facebookInstallMessage = textArrayList.get(17);
                            switchTopic = textArrayList.get(18);
                            exit = textArrayList.get(19);
                            // = textArrayList.get(21);
                            topicMaster = textArrayList.get(22);
                            enterMessage = textArrayList.get(24);
                            chatBox = textArrayList.get(25);
                            comingSoon = textArrayList.get(26);
                            message1 = textArrayList.get(27);
                            message2 = textArrayList.get(28);
                            message3 = textArrayList.get(29);
                            chooseAnotherTopic = textArrayList.get(30);
                            selectOptionMessage = textArrayList.get(31);
                            feedbackSuccess = textArrayList.get(32);
                            imageInternetError = textArrayList.get(33);
                            tourMessage = textArrayList.get(34);
                            currentMastery = "Mastery";//textArrayList.get(35);
                            readMore = textArrayList.get(36);
                            restartMessage = textArrayList.get(37);
                            restartMessage1 = textArrayList.get(38);
                            restartMessage2 = textArrayList.get(39);
                            howItWork = textArrayList.get(40);
                            download = textArrayList.get(41);
                            error = textArrayList.get(42);
                            checkMessage1 = textArrayList.get(43);
                            checkMessage2 = textArrayList.get(44);
                            checkMessage3 = textArrayList.get(45);
                            checkMessage4 = textArrayList.get(46);
                            report = textArrayList.get(47);
                            textViewChatBox.setText(chatBox);
                            masteryTobeSynced=global.getProgress();
                            masteryTextView.setText(Html.fromHtml(currentMastery +" " + global.getProgress() + "%"));
                            if (global.getProgress() == 100) {
                                masteryCompleteDialog();
                                return;

                            }

                            textViewReadMore.setText(readMore);
                        }
                        getPracticeCount();
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.preventPause(context,getTaskId());
        String date = Util.getCurrentDateWithDifferentFormat();
        //For Latest Data
        if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

            reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId);

            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);

        } else {
            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }

    }

    private void getTime() {
        System.out.println("------ getting time");
        String date = Util.getCurrentDateWithDifferentFormat();
        global.getDatabaseReference().child(ApplicationConstants.REPORTS)
                .child("user_time_spent")
                .child(Util.getUserId(context))
                .child(board).child(sClass).child("time_spent")
                .child(date).child(Util.getSubjectName(context).toLowerCase().replace(" ","_"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                if (snapshot.getValue() != null) {
                                    timeget = (long) snapshot.getValue();
                                    System.out.println( "------ time b "+timeget);
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


    private void getPracticeCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if (!Util.isNetworkAvailable(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "practice", "practiceCountTask");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("users").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("practice").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            practiceNumber = (long) snapshot.getValue();
                        } else {
                            practiceNumber = 0;
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
        if (type.equals("practiceCountTask")) {
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            practiceCountTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsCountModel> list = (ArrayList<ReportsCountModel>) o;
                            if (list != null && list.size() > 0) {
                                for (ReportsCountModel item : list) {
                                    practiceNumber = item.getCount();
                                }
                            } else {
                                practiceNumber = 0;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            practiceCountTaskDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date, String testType, String type) {
        if (type.equals("practiceCountTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsCountRepository.getDetail(userId, board, sClass, date, testType);
            });
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (image1 != null && !TextUtils.isEmpty(image1)) {
                    linearFullscreen1.setVisibility(View.VISIBLE);
                    linearFullImage1.setVisibility(View.VISIBLE);
                    imageViewOption1FullImage.setVisibility(View.VISIBLE);
                } else {
                    linearFullscreen1.setVisibility(View.GONE);
                    linearFullImage1.setVisibility(View.GONE);
                    imageViewOption1FullImage.setVisibility(View.GONE);
                }
                if (image2 != null && !TextUtils.isEmpty(image2)) {
                    linearFullImage2.setVisibility(View.VISIBLE);
                    imageViewOption2FullImage.setVisibility(View.VISIBLE);
                } else {
                    linearFullImage2.setVisibility(View.GONE);
                    imageViewOption2FullImage.setVisibility(View.GONE);
                }
                if (image3 != null && !TextUtils.isEmpty(image3)) {
                    linearFullImage3.setVisibility(View.VISIBLE);
                    imageViewOption3FullImage.setVisibility(View.VISIBLE);
                } else {
                    linearFullImage3.setVisibility(View.GONE);
                    imageViewOption3FullImage.setVisibility(View.GONE);
                }
                if (image4 != null && !TextUtils.isEmpty(image4)) {
                    linearFullImage4.setVisibility(View.VISIBLE);
                    imageViewOption4FullImage.setVisibility(View.VISIBLE);
                } else {
                    linearFullImage4.setVisibility(View.GONE);
                    imageViewOption4FullImage.setVisibility(View.GONE);
                }
            } else {
                linearFullImage1.setVisibility(View.GONE);
                linearFullImage2.setVisibility(View.GONE);
                linearFullImage3.setVisibility(View.GONE);
                linearFullImage4.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Async task to shorten url
    private class ShortenUrlTask extends AsyncTask<String, Void, String> {
        private final String GOOGLE_URL = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyC5UiikQU2_-_omC-qKYcXA0X4Ta8whfuI";
        private String mLongUrl = null;


        @Override
        protected String doInBackground(String... arg) {

            mLongUrl = "https://iprepteacher.page.link?link=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dorg.idreameducation.iprepapp%26hl%3Den%26ReferCode%3DS43ZGZ%253AZNszCULETMh44NMpejf49niLEg32%253AAndroid&apn=org.idreameducation.iprepapp";
//            mLongUrl = arg[0];

//            try {
//                // Set connection timeout to 5 secs and socket timeout to 10 secs
//                HttpParams httpParameters = new BasicHttpParams();
//                int timeoutConnection = 5000;
//                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//                int timeoutSocket = 10000;
//                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//                HttpClient hc = new DefaultHttpClient(httpParameters);
//                HttpPost request = new HttpPost(GOOGLE_URL);
//                request.setHeader("Content-type", "application/json");
//                request.setHeader("Accept", "application/json");
//                JSONObject obj = new JSONObject();
//                JSONObject objqq = new JSONObject();
//                obj.put("longDynamicLink", mLongUrl);
//                objqq.put("option", "SHORT");
//                obj.put("suffix", objqq);
//                request.setEntity(new StringEntity(obj.toString(), "UTF-8"));
//                HttpResponse response = hc.execute(request);
//                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    response.getEntity().writeTo(out);
//                    out.close();
//                    return out.toString();
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Util.dismissDialog();
//                Util.showToast(context, e.getLocalizedMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Util.showInternetConnectioError(context);
//                    }
//                });
//            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                dialogToDismiss.dismiss();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "iPrep " + mLongUrl);
                i.putExtra(Intent.EXTRA_TEXT, mLongUrl);
                startActivity(Intent.createChooser(i, "choose one"));
            } else {
                try {
                    final JSONObject json = new JSONObject(result);
                    final String id = json.getString("shortLink");
                    if (json.has("shortLink")) {
                        dialogToDismiss.dismiss();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "iPrep " + id);
                        i.putExtra(Intent.EXTRA_TEXT, id);
                        startActivity(Intent.createChooser(i, "choose one"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void getTrackTopic(String topicID) {
        if (!Util.isNetworkAvailable(context)) {
            TrackTopicTask trackTopicTask = new TrackTopicTask(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), topicID);
            trackTopicTask.execute();
        } else {
            global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguage(context)).child(topicID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            HashMap<String, String> dataMap = (HashMap<String, String>) snapshot.getValue();
                            trackTopic = dataMap.get("seniorTopicID");
                            trackClass = dataMap.get("sClass");
                        } else {
//                            trackTopic = null;
//                            trackClass = null;
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

    private class TrackTopicTask extends AsyncTask<Void, Void, ArrayList<TrackTopicModel>> {

        String userId;
        String subject;
        String board;
        String language;
        String foundationalTopicId;

        private TrackTopicTask(String userId, String subject, String board, String language, String foundationalTopicId) {
            this.userId = userId;
            this.subject = subject;
            this.board = board;
            this.language = language;
            this.foundationalTopicId = foundationalTopicId;
        }

        @Override
        protected ArrayList<TrackTopicModel> doInBackground(Void... voids) {
            return (ArrayList<TrackTopicModel>) trackTopicRepository.getDetail(userId, subject, board, language, foundationalTopicId);
        }

        @Override
        protected void onPostExecute(ArrayList<TrackTopicModel> list) {
            super.onPostExecute(list);
            if (list != null && list.size() > 0) {
                trackTopic = list.get(0).getSeniorTopicId();
                trackClass = list.get(0).getSeniorClass();
            } else {
                trackTopic = null;
                trackClass = null;
            }
        }
    }



    ArrayList<String> result_text=new ArrayList<>();
    int progressStatus=0,percentage=0;
    Drawable progressbar_drawable;

    private void getResult_text() {

        global.getDatabaseReference().child("screen_text/student/1/"+Util.getSelectedLanguage(context)+"/Practice_result").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null)
                {
                    result_text= (ArrayList<String>) snapshot.getValue();
                }
                else
                {
                    if(Util.getSelectedLanguage(context).equals("hindi"))
                    {
                        /** Hindi text need to be changed */
                        result_text.add(", you have given wrong answers to three questions of the same learning level");
                        result_text.add("Therefore, iPrep PAL suggests that you understand ");
                        result_text.add(" through the conceptual video lesson of the recommended level below, and then come back to practice and master ");
                        result_text.add(" again.");
                        result_text.add("Next Step");
                        result_text.add("Practice - Level ");
                        result_text.add("or");
                        result_text.add("Watch Foundation video Lessons");
                        result_text.add("Watch Video Lessons of Level ");
                        result_text.add("Start Final Test");
                        result_text.add("Practice Next Senior Topic");
                        result_text.add("Watch Video Lessons of level ");
                        result_text.add("Let’s take the final test to complete this chapter");
                        result_text.add("Very Good ");
                        result_text.add(", You have mastered the practice of ");
                        result_text.add("Learn Other Chapter");
                        result_text.add("Let’s practice next senior topic to complete this chapter");
                    }
                    else
                    {
                        result_text.add(", you have given wrong answers to three questions of the same learning level");
                        result_text.add("Therefore, iPrep PAL suggests that you understand ");
                        result_text.add(" through the conceptual video lesson of the recommended level below, and then come back to practice and master ");
                        result_text.add(" again.");
                        result_text.add("Next Step");
                        result_text.add("Practice - Level ");
                        result_text.add("or");
                        result_text.add("Watch Foundation video Lessons");
                        result_text.add("Watch Video Lessons of Level ");
                        result_text.add("Start Final Test");
                        result_text.add("Practice Next Senior Topic");
                        result_text.add("Watch Video Lessons of level ");
                        result_text.add("Let’s take the final test to complete this chapter");
                        result_text.add("Very Good ");
                        result_text.add(", You have mastered the practice of ");
                        result_text.add("Learn Other Chapter");
                        result_text.add("Let’s practice next senior topic to complete this chapter");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showResult(boolean isfailed) {

        saveReport();

        LinearLayout result_layout;
        TextView mainText1,result_text2,nextStep_text;
        TextView practice_text,or_text,watch_video_text;
        ImageView direct_cross_btn;
        TextView score_percentage_text,level_text;

        isResultPage = true;

        ProgressBar progressBar;
        progressStatus=0;

        String topicName = "<font color='#0077FF'>" + Util.getTopicNameAlt(context) + "</font>";
        result_layout=findViewById(R.id.result_layout);

        level_text=findViewById(R.id.level_text);
        progressBar=findViewById(R.id.score_progressBar);
        score_percentage_text=findViewById(R.id.score_percentage_text);

        mainText1=findViewById(R.id.mainText1);
        result_text2=findViewById(R.id.result_text2);
        nextStep_text=findViewById(R.id.nextStep_text);

        practice_text=findViewById(R.id.practice_text);
        or_text=findViewById(R.id.or_text);
        watch_video_text=findViewById(R.id.watch_video_text);

        direct_cross_btn=findViewById(R.id.direct_cross_btn);

        result_layout.setVisibility(View.VISIBLE);
        mainText1.setText(Util.getUsernameShowable(context)+result_text.get(0));
        result_text2.setText(Html.fromHtml(result_text.get(1)+topicName+result_text.get(2)+topicName+result_text.get(3)));
        nextStep_text.setText(result_text.get(4));

        practice_text.setText(result_text.get(5));
        or_text.setText(result_text.get(6));
        watch_video_text.setText(result_text.get(7));

        String date = Util.getCurrentDateWithDifferentFormat();
        String foundationalText;
        String levString="Level ";

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            foundationalText = "कृपया पहले मुलभुत अध्याय का परिक्षण करें";
            levString="स्तर ";
//            result_text2.setText(Html.fromHtml(result_text.get(1)+topicName+result_text.get(2)));
        } else {
            foundationalText = "Please do foundational practice first";
//            result_text2.setText(Html.fromHtml(result_text.get(1)+topicName+result_text.get(2)+topicName+result_text.get(3)));
        }

        /** Update Mastery of current test */
        if (reportsLatestDataPracticeRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId,Util.getSelectedLanguage(context))) {
//                    reportsLatestDataPracticeRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId, level + "", date, String.valueOf(masteryTobeSynced), streakProgress + "",
//                            Util.getTopicNameAlt(context));

            reportsLatestDataPracticeRepository.DeleteFields(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicId);

            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        } else {
            ReportsLatestDataPracticeModel reportsLatestDataPracticeModel = new ReportsLatestDataPracticeModel();
            reportsLatestDataPracticeModel.setUserId(Util.getUserId(context));
            reportsLatestDataPracticeModel.setBoard(board);
            reportsLatestDataPracticeModel.setSClass(sClass);
            reportsLatestDataPracticeModel.setSubject(Util.getSubject(context));
            reportsLatestDataPracticeModel.setType("practice");
            reportsLatestDataPracticeModel.setTopicId(topicId);
            reportsLatestDataPracticeModel.setCurrentLevel(level + "");
            reportsLatestDataPracticeModel.setPDate(date);
            reportsLatestDataPracticeModel.setMastery(String.valueOf(masteryTobeSynced));
            reportsLatestDataPracticeModel.setStreakProgress(streakProgress + "");
            reportsLatestDataPracticeModel.setTopicName(Util.getTopicNameAlt(context));
            reportsLatestDataPracticeModel.setLang(Util.getSelectedLanguage(context));
            reportsLatestDataPracticeRepository.insertPracticeDetails(reportsLatestDataPracticeModel);
        }


        /** Checking user failed a test ??  */
        if(isfailed) {

            /** Checking Current Test of a Foundational Test or not */
            if(type.equals("foundation")) watch_video_text.setText(result_text.get(7));
            else watch_video_text.setText(result_text.get(8)+level);

            /** Checking FoundationTopic */
            if (GetTopicLevelsDetails.isJuniorTopicAvailable()) {
                practice_text.setText(result_text.get(5));
                or_text.setText(result_text.get(6));

                /** if student is failed level 1 then clear foundation topic mastery he/she need to clear foundation topic first */
                if(level==1) saveFoundationalDetails();

                /**Start Foundation Topic Practice */
                practice_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        saveFoundationalDetails();
                        getJuniorLevelPractice(true);
                    }
                });

                /**Show Topic Videos */
                watch_video_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);

                        autoplayLevelVideo=true;
                        Util.setVideoLevel(context, (level - 1));
                        Util.setLevel(context, (level - 1));
                        if(Util.getUnlockVideoLevel(context,topicId)<level)
                            Util.setUnlockVideoLevel(context,topicId,level);

                        Intent intent = new Intent(context, PalTopicListingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Util.setVideoLevel(context, 0);
                        String subject = PalContentListingActivity_Mobile.subject;
                        String subjectName = PalContentListingActivity_Mobile.subjectName;
                        String color = PalContentListingActivity_Mobile.color;
                        global.setColor(color);
                        viewPagerPos=0;
                        intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                        intent.putExtra("board", Util.getSelectedBoard(context));
                        intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                        intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                        intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                        intent.putExtra("color", PalContentListingActivity_Mobile.color);
//                        intent.putExtra("lastTopicId", PalContentListingActivity_Mobile.currentTopicid);
                        intent.putExtra("backToScreen", false);
                        Util.setSubject(context, subject);
                        Util.setSubjectName(context, subjectName);
                        context.startActivity(intent);
                        finish();
                        /** Old Code which open new activity for foundation videos */
//                        if(type.equals("foundation"))
//                        {
////                    Util.showToast(context, "Separate section of videos will be made");
//                            intent = new Intent(context, ExtraContentListingActivity.class);
//                            Util.setVideoLevel(context, (level - 1));
//                            Util.setLevel(context, (level - 1));
//                            intent.putExtra("board", board);
//                            intent.putExtra("sClass", sClass);
//                            intent.putExtra("type", "foundationalTopicVideos");
//                            intent.putExtra("subject", Util.getSubject(context));
//                            intent.putExtra("subjectName", Util.getSubjectName(context));
//                            intent.putExtra("topicId", topicId);
//                            intent.putExtra("topicName", topicN.toString());
//                            intent.putExtra("testPercentageAchieved", testPercentageAchieved);
//
//                        }
//                        else
//                        {
//                            com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.backPressed = true;
//                            String subject = Util.getSubject(context);
//                            intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.autoplayLevelVideo=true;
//                            Util.setVideoLevel(context, (level - 1));
//                            Util.setLevel(context, (level - 1));
//                            intent.putExtra("subject", subject);
//                            intent.putExtra("sClass", sClass);
//                            intent.putExtra("board", board);
//                            intent.putExtra("subjectName", subject);
//                            intent.putExtra("icon", "");
//
//                        }
                    }
                });

            }
            else {

                /** Hide Practice or OR text */
                practice_text.setVisibility(View.GONE);
                or_text.setVisibility(View.GONE);

                /**Show Current Topic Videos */
                watch_video_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);

                        autoplayLevelVideo=true;
                        Util.setVideoLevel(context, (level - 1));
                        Util.setLevel(context, (level - 1));
                        if(Util.getUnlockVideoLevel(context,topicId)<level)
                            Util.setUnlockVideoLevel(context,topicId,level);

                        Intent intent = new Intent(context, PalTopicListingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Util.setVideoLevel(context, 0);
                        String subject = PalContentListingActivity_Mobile.subject;
                        String subjectName = PalContentListingActivity_Mobile.subjectName;
                        String color = PalContentListingActivity_Mobile.color;
                        global.setColor(color);
                        viewPagerPos=0;
                        intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                        intent.putExtra("board", Util.getSelectedBoard(context));
                        intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                        intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                        intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                        intent.putExtra("color", PalContentListingActivity_Mobile.color);
//                        intent.putExtra("lastTopicId", PalContentListingActivity_Mobile.currentTopicid);
                        intent.putExtra("backToScreen", false);
                        Util.setSubject(context, subject);
                        Util.setSubjectName(context, subjectName);
                        context.startActivity(intent);
                        finish();
                        /** Old Code which open new activity for foundation videos */
//                        if(type.equals("foundation"))
//                        {
////                    Util.showToast(context, "Separate section of videos will be made");
//                            intent = new Intent(context, ExtraContentListingActivity.class);
//                            Util.setVideoLevel(context, (level - 1));
//                            Util.setLevel(context, (level - 1));
//                            intent.putExtra("board", board);
//                            intent.putExtra("sClass", sClass);
//                            intent.putExtra("type", "foundationalTopicVideos");
//                            intent.putExtra("subject", Util.getSubject(context));
//                            intent.putExtra("subjectName", Util.getSubjectName(context));
//                            intent.putExtra("topicId", topicId);
//                            intent.putExtra("topicName", topicN.toString());
//                            intent.putExtra("testPercentageAchieved", testPercentageAchieved);
//
//                        }
//                        else
//                        {
//                            com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.backPressed = true;
//                            String subject = Util.getSubject(context);
//                            intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.autoplayLevelVideo=true;
//                            Util.setVideoLevel(context, (level - 1));
//                            Util.setLevel(context, (level - 1));
//                            intent.putExtra("subject", subject);
//                            intent.putExtra("sClass", sClass);
//                            intent.putExtra("board", board);
//                            intent.putExtra("subjectName", subject);
//                            intent.putExtra("icon", "");
//
//                        }

                    }
                });

            }

        }
        else {

            /** Checking Current Test of a Foundational Test or not */
            if(type.equals("foundation")) result_text2.setText(result_text.get(16));
            else result_text2.setText(result_text.get(12));

            practice_text.setText(result_text.get(9));
            mainText1.setText(Html.fromHtml(result_text.get(13)+Util.getUsernameShowable(context)+result_text.get(14)+topicName));


            /**Checking Next Practice Available or not*/
            if (trackTopic != null) {
                saveFoundationalDetail();
//                com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.showTestLayout("foundationalPractice");
                practice_text.setText(result_text.get(10));

                /**Start Next Topic Practice*/
                practice_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);

//                        PalVideoListFragment.messageLayout.performClick();

                        if (Util.isOfflineMode(context))
                        {
                            try {
                                String filePath = ".iDream_content/offlinetab_PAL/Class" + trackClass + "_core_content.txt";
                                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                                JSONObject object__ = jsonObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                                JSONObject object___ = object__.getJSONObject("practice");
                                JSONObject object____ = object___.getJSONObject("content");
                                JSONObject object_____ = object____.getJSONObject(Util.getSubject(context));
                                JSONObject object______ = object_____.getJSONObject("topics");
                                JSONObject object_______ = object______.getJSONObject(trackTopic);

                                String Display = object_______.getString("Display");
                                String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
                                String IsModelTestPaper = object_______.getString("IsModelTestPaper");
                                String Levels = object_______.getString("Levels");
                                String Next_topic_id = object_______.getString("Next_topic_id");
                                String StreakCount = object_______.getString("StreakCount");
                                String TName = object_______.getString("TName");
                                String TName_alt = object_______.getString("TName_alt");
                                String TopicID = object_______.getString("TopicID");
                                String foundational_class = object_______.getString("foundational_class");
                                String incorrectStreak = object_______.getString("incorrectStreak");
                                String isAlternateLanguageAvailable = object_______.getString("isAlternateLanguageAvailable");


                                HashMap<String, String> practiceMap = new HashMap<>();
                                practiceMap.put("Display", Display);
                                practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
                                practiceMap.put("IsModelTestPaper", IsModelTestPaper);
                                practiceMap.put("Levels", Levels);
                                practiceMap.put("Next_topic_id", Next_topic_id);
                                practiceMap.put("StreakCount", StreakCount);
                                practiceMap.put("TName", TName);
                                practiceMap.put("TName_alt", TName_alt);
                                practiceMap.put("TopicID", TopicID);
                                practiceMap.put("foundational_class", foundational_class);
                                practiceMap.put("incorrectStreak", incorrectStreak);
                                practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);
                                Util.setTopicID(context, trackTopic);
//                        Util.setClassNameSelection(context, trackClass);
//                        Util.setClassNameSelectionDummy(context, trackClass);
//                        Util.setClassSelection(context, trackClass);
//                        Util.setClassSelectionDummy(context, trackClass);
//                        Util.setTopicNameAlt(context, next_chapter_name);
                                Util.setTopicNameAlt(context, TName);
//                                global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(topicId).removeValue();

                                trackTopicRepository.delete(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), topicId);

                                HashMap<String, String> mapF = new HashMap<>();
                                mapF.put("foundational_class", foundational_class);
                                mapF.put("foundational_topic", Foundational_Topic_ID);
                                global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);

                                global.setProgress(0);
                                if (!trackClass.equals(Util.getSelectedClass(context))) {
                                    startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));
                                } else {
                                    startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0).putExtra("type", "foundation"));
                                }

                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {

                            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(trackClass).child(Util.getSelectedLanguagePackage(context)).child("practice").child("content").child(Util.getSubject(context)).child("topics").child(trackTopic).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        if (snapshot.getValue() != null) {
                                            HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();

                                            String Display = detailHashMap.get("Display");
                                            String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");
                                            String IsModelTestPaper = detailHashMap.get("IsModelTestPaper");
                                            String Levels = detailHashMap.get("Levels");
                                            String Next_topic_id = detailHashMap.get("Next_topic_id");
                                            String StreakCount = detailHashMap.get("StreakCount");
                                            String TName = detailHashMap.get("TName");
                                            String TName_alt = detailHashMap.get("TName_alt");
                                            String TopicID = detailHashMap.get("TopicID");
                                            String foundational_class = detailHashMap.get("foundational_class");
                                            String incorrectStreak = detailHashMap.get("incorrectStreak");
                                            String isAlternateLanguageAvailable = detailHashMap.get("isAlternateLanguageAvailable");


                                            HashMap<String, String> practiceMap = new HashMap<>();
                                            practiceMap.put("Display", Display);
                                            practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
                                            practiceMap.put("IsModelTestPaper", IsModelTestPaper);
                                            practiceMap.put("Levels", Levels);
                                            practiceMap.put("Next_topic_id", Next_topic_id);
                                            practiceMap.put("StreakCount", StreakCount);
                                            practiceMap.put("TName", TName);
                                            practiceMap.put("TName_alt", TName_alt);
                                            practiceMap.put("TopicID", TopicID);
                                            practiceMap.put("foundational_class", foundational_class);
                                            practiceMap.put("incorrectStreak", incorrectStreak);
                                            practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);
                                            Util.setTopicID(context, trackTopic);
//                        Util.setClassNameSelection(context, trackClass);
//                        Util.setClassNameSelectionDummy(context, trackClass);
//                        Util.setClassSelection(context, trackClass);
//                        Util.setClassSelectionDummy(context, trackClass);
//                        Util.setTopicNameAlt(context, next_chapter_name);
                                            Util.setTopicNameAlt(context, TName);
//                                            global.getDatabaseReference().child("track_lower_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(topicId).removeValue();

                                            trackTopicRepository.delete(Util.getUserId(context), Util.getSubject(context), board, Util.getSelectedLanguagePackage(context), topicId);

                                            HashMap<String, String> mapF = new HashMap<>();
                                            mapF.put("foundational_class", foundational_class);
                                            mapF.put("foundational_topic", Foundational_Topic_ID);
                                            global.getDatabaseReference().child("backward_topics").child(Util.getUserId(context)).child(board).child(Util.getSelectedLanguagePackage(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).child(current_topicId).setValue(mapF);

                                            Util.setLevel(context,1);
                                            global.setProgress(0);
                                            if (!trackClass.equals(Util.getSelectedClass(context))) {
                                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0));
                                            } else {
                                                startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", trackClass).putExtra("streakProgress", streakProgress).putExtra("streak", StreakCount).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "junior").putExtra("seniorClass", seniorClass).putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName).putExtra("testPercentageAchieved", 0));
                                            }

                                            finish();
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
                });
                watch_video_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        autoplayLevelVideo=true;
                        Util.setVideoLevel(context, (level - 1));
                        Util.setLevel(context, (level - 1));
                        if(Util.getUnlockVideoLevel(context,topicId)<level)
                            Util.setUnlockVideoLevel(context,topicId,level);

                        Intent intent = new Intent(context, PalTopicListingActivity.class);
                        Util.setVideoLevel(context, 0);
                        String subject = PalContentListingActivity_Mobile.subject;
                        String subjectName = PalContentListingActivity_Mobile.subjectName;
                        String color = PalContentListingActivity_Mobile.color;
                        global.setColor(color);
                        viewPagerPos=0;
                        intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                        intent.putExtra("board", Util.getSelectedBoard(context));
                        intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                        intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                        intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                        intent.putExtra("color", PalContentListingActivity_Mobile.color);
//                        intent.putExtra("lastTopicId", topicsArrayList.get(position).get("TopicID"));
                        intent.putExtra("backToScreen", false);
                        Util.setSubject(context, subject);
                        Util.setSubjectName(context, subjectName);
                        context.startActivity(intent);
                        finish();
                    }
                });
            } else {
                /** Checking Current test is not a Foundational test */
                if(!type.equals("foundation"))
                {
                    /**Start Final Test*/
                    practice_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.preventTwoClick(v);

                            String topicName = Util.getTopicNameAlt(context);
                            Util.setTopicID(context, topicId);
                            Util.setTopicNameAlt(context, topicName);
                            if (Util.isNetworkAvailable(context))
                            {
                                startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

                                finish();
                            } else {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                }else {
                                    Util.openGifDialogue(context,"Internet Connection is not working");
                                }
                            }


//                            if (com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
//                            {
//                                // Util.showToast(context, foundationalText);
//                                Util.openGifDialogue(context, foundationalText);
//                            }
//                            else
//                            {
//                                String topicName = Util.getTopicNameAlt(context);
//                                Util.setTopicID(context, topicId);
//                                Util.setTopicNameAlt(context, topicName);
//                                if (Util.isNetworkAvailable(context))
//                                {
//                                    startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));
//
//                                    finish();
//                                } else {
//                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                    {
//                                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
//                                    }else {
//                                        Util.openGifDialogue(context,"Internet Connection is not working");
//                                    }
//                                }
//                            }


                        }
                    });

                    /** Learn another topic */
                    watch_video_text.setText(result_text.get(15));
                    watch_video_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.preventTwoClick(v);
                            PalContentListingActivity_Mobile.backPressed = true;
                            String subject = Util.getSubject(context);
                            String subjectName = Util.getSubjectName(context);
                            String icon = PalContentListingActivity_Mobile.icon;
                            Intent intent = new Intent(context, PalContentListingActivity_Mobile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("subject", subject);
                            intent.putExtra("sClass", sClass);
                            intent.putExtra("board", board);
                            intent.putExtra("subjectName", subjectName);
                            intent.putExtra("icon", icon);
                            intent.putExtra("getPath", "yes");
                            Util.setTopicID(context, topicId);
                            autoplayLevelVideo=false;
                            Util.setVideoLevel(context, (level - 1));
                            Util.setLevel(context, (level - 1));
                            startActivity(intent);

                            finish();

                        }
                    });
                }
                else
                {
                    watch_video_text.setVisibility(View.GONE);
                    or_text.setVisibility(View.GONE);
                    practice_text.setText(result_text.get(10));
                    /** Start Practice Test*/
                    practice_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.preventTwoClick(v);
                            if (PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
                            {
                                // Util.showToast(context, foundationalText);
                                Util.openGifDialogue(context, foundationalText);
                            }
                            else
                            {
                                String topicName = Util.getTopicNameAlt(context);
                                Util.setTopicID(context, topicId);
                                Util.setTopicNameAlt(context, topicName);
                                if (Util.isNetworkAvailable(context))
                                {
                                    startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

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


                        }
                    });
                }

            }

//            /** Show Current Topic Videos*/
//            watch_video_text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.preventTwoClick(v);
//                    com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.backPressed = true;
//                    String subject = Util.getSubject(context);
//                    String subjectName = Util.getSubjectName(context);
//                    String icon = com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.icon;
//                    Intent intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("subject", subject);
//                    intent.putExtra("sClass", sClass);
//                    intent.putExtra("board", board);
//                    intent.putExtra("subjectName", subjectName);
//                    intent.putExtra("icon", icon);
//                    intent.putExtra("getPath", "yes");
//                    Util.setTopicID(context, topicId);
//                    com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile.autoplayLevelVideo=true;
//                    Util.setVideoLevel(context, (level - 1));
//                    Util.setLevel(context, (level - 1));
//                    startActivity(intent);
//
//                    finish();
//
//                }
//            });
        }

        percentage=masteryTobeSynced;
        progressBar.setMax(100);
        progressBar.setProgress(percentage);
        level_text.setText(levString+level);

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

        direct_cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                if(optionSelected) {streakProgress--;}
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



    /** Practice Level Videos functions */

    private static ArrayList<LevelVideoModel> videoList;
    RecyclerView levelVideosRecyclerview;
    LinearLayout video_layout,videos_btn;
    ImageView cross_video_ly_btn;

    String type="practice";
    public static FrameLayout practice_video_container;
    private static FragmentManager manager;
    private static FragmentTransaction transaction;
    private static iPrepVideoPlayerActivity palVideoPlayerActivity;
    private static String currentvideoID="",currentvideoName="";
    private static int videoPos=0;
    public static int videocurrentPosition=0;
    public static String TOPICID="";
    private static VideoLevelAdapter videoLevelAdapter;
    private int tempLevel=1;
    String assignedData,assignedKey,batchID;

    private void getLevelVideos() {

        levelVideosRecyclerview=findViewById(R.id.levelVideosRecyclerview);
        levelVideosRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        videoList=new ArrayList<>();
        videoLevelAdapter = new VideoLevelAdapter(videoList);
        TOPICID=topicId;
        global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("video_lessons").child("content").child(Util.getSubject(context)).child("topics").child(topicId).child(level+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        levelVideosRecyclerview.setAdapter(videoLevelAdapter);
                        for(DataSnapshot ss:snapshot.getChildren())
                        {
                            LevelVideoModel levelVideoModel=ss.getValue(LevelVideoModel.class);
                            levelVideoModel.setKey(ss.getKey());
                            videoList.add(levelVideoModel);
                        }
                        videoLevelAdapter.notifyDataSetChanged();
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

    public static void playVideo(Context context,int position) {

        removeVideoLayout();

        currentvideoID=videoList.get(position).getOnlineLink();
        currentvideoName=videoList.get(position).getName();
        videoPos=position;

        Bundle bundls =new Bundle();
        bundls.putString("url", currentvideoID);
        bundls.putString("type", "level_practice_videos");
        bundls.putString("offlineLink", "offlineLink");
        bundls.putBoolean("isLocalFile", false);
        bundls.putString("videoName", videoList.get(position).getName());
        bundls.putString("topicID", TOPICID);
        bundls.putString("subjectName", Util.getSubjectName(context));
        bundls.putString("videoid_for_reports", videoList.get(position).getKey());
        bundls.putBoolean("isFullScreen", false);
        bundls.putInt("duration", videocurrentPosition);
        PalContentListingActivity_Mobile.currentvideo_url = currentvideoID;


        if(Util.getIsFullScreen(context))
        {
            PalContentListingActivity_Mobile.current_duration=videocurrentPosition;
            Intent intent = new Intent(context, VideoView_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundls);

            context.startActivity(intent);

        }
        else
        {
            PalContentListingActivity_Mobile.duration=videocurrentPosition;
            palVideoPlayerActivity = new iPrepVideoPlayerActivity();
            palVideoPlayerActivity.setArguments(bundls);
            manager = instance.getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.add(R.id.practice_video_container, palVideoPlayerActivity, "tag");
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            practice_video_container.setVisibility(View.VISIBLE);

        }



    }

    public static void removeVideoLayout() {
        practice_video_container.setVisibility(View.GONE);
        PalContentListingActivity_Mobile.keys = "";
        notifyVideoList();

        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        manager = instance.getSupportFragmentManager();
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
        practice_video_container.setVisibility(View.GONE);
        try {
            iPrepVideoPlayerActivity.mediaController.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void gotoFullScreen(Context context) {
        Bundle bundls =new Bundle();
        bundls.putString("url", currentvideoID);
        bundls.putString("type", "level_practice_videos");
        bundls.putString("offlineLink", "offlineLink");
        bundls.putBoolean("isLocalFile", false);
        bundls.putString("videoName", currentvideoName);
        bundls.putString("topicID", TOPICID);
        bundls.putString("subjectName", Util.getSubjectName(context));
        bundls.putString("videoid_for_reports", videoList.get(videoPos).getKey());
        bundls.putBoolean("isFullScreen", false);
        bundls.putInt("duration", iPrepVideoPlayerActivity.videoView.getCurrentPosition());
        PalContentListingActivity_Mobile.currentvideo_url = currentvideoID;
        PalContentListingActivity_Mobile.current_duration=iPrepVideoPlayerActivity.videoView.getCurrentPosition();
        iPrepVideoPlayerActivity.videoView.pause();


//        palVideoPlayerActivity = new iPrepVideoPlayerActivity();
//        palVideoPlayerActivity.setArguments(bundls);
//        manager = instance.getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.practice_video_container, palVideoPlayerActivity, "tag");
//        transaction.addToBackStack(null);
//
//        transaction.commit();
//        practice_video_container.setVisibility(View.VISIBLE);

        Intent intent = new Intent(context, VideoView_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundls);

        context.startActivity(intent);

    }

    public static void gotoSmallScreen(Context context,int videocurrentPosition) {
        iPrepVideoPlayerActivity.mediaController.hide();
        QuizActivity.videocurrentPosition=videocurrentPosition;
        playVideo(context,videoPos);
    }

    public static boolean playNextVideo(Context context) {
        videocurrentPosition=0;
        iPrepVideoPlayerActivity.mediaController.hide();
        int nextVideoPositio=videoPos+1;
        if(videoList.size()>nextVideoPositio)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playVideo(context,nextVideoPositio);
                }
            },1500);
            return true;
        }
        else
        {
            iPrepVideoPlayerActivity.mediaController.hide();
            Toast.makeText(context, "No More Videos", Toast.LENGTH_SHORT).show();
            removeVideoLayout();
            return false;
        }

    }

    public static void notifyVideoList()
    {
        videoLevelAdapter.notifyDataSetChanged();
    }

    private void show_levelUP_toast(String message) {

        System.out.println("_______ tempLevel "+tempLevel);
        System.out.println("_______ level "+level);

        if(tempLevel==level) return;

        tempLevel=level;

        View toastLayout = getLayoutInflater().inflate(R.layout.customt_toast,
                findViewById(R.id.rootId));

        TextView textView = toastLayout.findViewById(R.id.textView);
        textView.setText("Level "+(level-1)+" Complete");

        LottieAnimationView lotie_player=toastLayout.findViewById(R.id.lotie_player);

        lotie_player.setAnimation("thumbsup1.json");
        lotie_player.playAnimation();

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 200);
//        toast.setMargin(0.2f, 0f);
        toast.setView(toastLayout);
        toast.show();



    }

    private void saveAssignedReport() {
        assignedData = getIntent().getStringExtra("assignedDate");
        assignedKey = getIntent().getStringExtra("assignedKey");
        batchID=global.getBatchID();
        if(assignedKey==null) return;


        HashMap<String,String> report=new HashMap<>();
        report.put("mastery",String.valueOf(masteryTobeSynced));
        report.put("time",String.valueOf(endTime - startTime));
        report.put("timestamp", Util.getCurrentDateWithDifferentFormat());

        global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                .child(assignedData).child(assignedKey).child("report").setValue(report);

    }

}
