package com.idreameducation.ipreppal.pal.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.idreameducation.ipreppal.util.Util.isPortraitMode;
import static com.idreameducation.ipreppal.util.Util.isTablet;
import static com.idreameducation.ipreppal.util.Util.setLandscapeView;
import static com.idreameducation.ipreppal.util.Util.setPageType;
import static com.idreameducation.ipreppal.util.Util.setPortraitView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.idreameducation.ipreppal.BuildConfig;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.deeplink.DeepLinkManager;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.Notification_model;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalAnonymousLoginActivity;
import com.idreameducation.ipreppal.pal.adapter.AssignedContentAdapter;
import com.idreameducation.ipreppal.pal.adapter.Notification_Adapter;
import com.idreameducation.ipreppal.pal.adapter.PalLanguageSelectionAdapter;
import com.idreameducation.ipreppal.pal.fragments.BatchesFragments;
import com.idreameducation.ipreppal.pal.fragments.HomeScreenIntroDialogFragment;
import com.idreameducation.ipreppal.pal.fragments.PalHomeFragment;
import com.idreameducation.ipreppal.roomdatabase.model.ActivationModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ActivationDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;
import com.idreameducation.ipreppal.videoPlayer.iPrepVideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class PracticeTopicActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private Global global;
    private Context context;
    private DrawerLayout drawer_layout;
    private RecyclerView NotificationRecyclerView;
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private String studentClass;
    private String board;
    private String language;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ImageView imageViewBack;
    private ImageView logoIprep;
    private ImageView imageViewMessage;
    private ImageView imageViewCrossNotification,imageViewCrossMenu;
    private LinearLayout AnalyticsLayout;
    private LinearLayout navlanguageLayout;
    private LinearLayout iPrepWorkLayout;
    private LinearLayout supportHistory;
    private LinearLayout writeToUsLayout;
    private LinearLayout shareLayout;
    private LinearLayout headerLayout;
    private LinearLayout linearLayoutEmpty;
    private RelativeLayout classLayout;
    private RelativeLayout reletiveDrawerLayoutNotification,reletiveDrawerLayout;
    private TextView classTextView;
    private RelativeLayout languageLayout;
    private TextView languageTextView;
    private TextView textViewUserName;
    private TextView textViewClass_Board;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout logoutLayout;
    private ArrayList<HashMap<String, Object>> assignedContentArrayList;
    private ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;
    private TextView textViewAnalytics,textViewLanguage, textViewShare, textViewiPrepWork, textViewWriteToUs, textViewLogout, textViewTitle, textView1, textView2;
    private FrameLayout topTransparentBg;
    private FrameLayout bottomTransparentBg;
    private FrameLayout fullScreenTransparentBg;
    public static String textToSend,heading1Text,heading2Text,heading3Text,heading4Text,button1Text,button2Text,button3Text;
    private String textOfChangeLanguageDialog, changeLanguageButton;
    String sclassText;
    private Boolean sFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;
    private LinearLayout linearSecond,aboutPAL,faqLayout,introlayout;
    CircleImageView profileImage;
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;
    ArrayList<HashMap<String, String>> languageArrayList;
    public static boolean autoplayLevelVideo=false;
    iPrepVideoPlayerActivity prepVideoPlayerActivity;

    private View mContentView;
    private final Handler mHideHandler = new Handler(Looper.myLooper());

    LottieAnimationView lotie_player;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Util.setWindowSettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pal_activity_topics);
        mContentView=findViewById(R.id.drawer_layout);


        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);

        getNotificationPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION), ContextCompat.RECEIVER_NOT_EXPORTED);
        }

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());

        /** ask to desable power saving mode */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent intent = new Intent();
//            String packageName = getPackageName();
//            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                startActivity(intent);
//            }
//        }
        assignIds();


        updateLastNetConnection();
        notNetConnection();
    }

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS}, 22);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 22) {
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!Util.isPortraitMode(context)) getWindow().getDecorView().setSystemUiVisibility(
                SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (textViewClass_Board != null) {

            if (Util.getSelectedClass(context).contains("11")) {
                textViewClass_Board.setText(sClass + " " + "11th");
            }
            else if(Util.getSelectedClass(context).contains("12")) {
                textViewClass_Board.setText(sClass + " " + "12th");
            }
            
        }
        if (classTextView != null) {
            classTextView.setText(sclassText +" "+ Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));
        }
        if (languageTextView != null) {
            if (Util.getSelectedLanguage(context).equals("english")) {
                languageTextView.setText("ENG");
            } else {
                languageTextView.setText("हिंदी");
            }
        }
    }

    @Override
    public void networkAvailable() {
        if(PracticeTopicActivity.dialog!=null) {
            try {
                PracticeTopicActivity.dialog.dismiss();
                PracticeTopicActivity.dialog=null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void networkUnavailable() {
        if(!Util.isOfflineMode(context)) {
            if(dialog==null) {
                openGifDialogue();
                recheckConnection();
            }
        }
        System.out.println("-------- not Available ");
    }

    private void recheckConnection() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try
                {
                    NetworkStateReceiver.notifyStateToAll();
                    networkStateReceiver.notify();
                    networkStateReceiver.notifyAll();
                }catch (Exception r){}
            }
        },5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
        if(Util.isOfflineMode(context)) try {

            String image=Util.getUserProfileUrl(context);

            if (image == null)  Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImage);
            else {
                String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ image+".png";
                File file = new File(filePath);

                Uri uri = null;

                if(file.exists()){
                    uri = Uri.fromFile(file);
                }

                if(uri != null){
                    Glide.with(context)
                            .load(uri).into(profileImage);
                }
            }

//            Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NetworkStateReceiver.notifyStateToAll();
            networkStateReceiver.notify();
            networkStateReceiver.notifyAll();
        }
        catch (Exception r){}

    }

    private void logoutPopup() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_exit_model);
        TextView textView = dialog.findViewById(R.id.textView);
        ImageView crossImage = dialog.findViewById(R.id.crossImage);
        TextView textView1 = dialog.findViewById(R.id.textView1);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Util.setContentonboadingMode(context,false);
                    Util.setHomeboadingMode(context,false);
                    Util.setSubjectboadingMode(context,false);

                    Util.setAge(context,"");
                    Util.setUserEmail(context,"");
                    Util.setCitySelection(context,"");
                    Util.setParentsContact(context,"");
                    Util.setUsername(context,null);
                    Util.setVideoSpeed(context,"1x Normal Speed");
                    Util.setDiagnosticboadingMode(context,false);
                    Util.setLogoutClicked(context,true);
                    Util.preventTwoClick(view);
                    Intent intent;

                    intent = new Intent(context, PalAnonymousLoginActivity.class);

                    Util.setUserId(context,null);
                    Util.setClassSelection(context,null);
                    Util.setClassSelectionDummy(context,null);

                    intent.putExtra("flow", "no");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView TxtExit = dialog.findViewById(R.id.TxtExit);

        String message,logout,cancel,text;

        if(Util.getSelectedLanguage(context).equals("hindi")){
            text="लॉगआउट पक्का करें";
            message="क्या आप सच में लॉगआउट करना चाहते हैं ?";
            logout="लॉगआउट करें";
            cancel="रद्द करें";
        }
        else {
            text="Confirm Log Out";
            message="Are you sure you want to logout";
            logout="Logout";
            cancel="Cancel";
        }

        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textView.setText(message);
        textView1.setText(text);
        textView1.setVisibility(View.GONE);
        textViewContinoue.setText(logout);
        TxtExit.setText(cancel);
        dialog.setCancelable(true);
        try {
            dialog.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }
    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

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

    @Override
    protected void onPause() {
        Util.preventPause(context,getTaskId());
        if(dialog!=null) {
            dialog.dismiss();
            dialog=null;
        }
        super.onPause();
    }

    private void showIntroVideo() {

        introlayout=findViewById(R.id.introlayout);
        introlayout.setVisibility(View.VISIBLE);
        introlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation=AnimationUtils.loadAnimation(context,R.anim.slide_out_left);
                introlayout.startAnimation(animation);
                introlayout.setVisibility(View.GONE);
                Util.setHomeboadingMode(context,true);
            }
        });

//        Bundle bundl=new Bundle();
//        bundl.putString("url", "https://ntpproductionall.blob.core.windows.net/ntp-content-production/content/assets/do_3130879784457666561154/l67-mathematics-xii-ch2-ep1.mp4");
//        bundl.putString("type", "video_lessons");
//        bundl.putString("offlineLink", "offlineLink");
//        bundl.putBoolean("isLocalFile", false);
//        bundl.putString("videoName", "videoName");
//        bundl.putBoolean("isFullScreen", Util.getIsFullScreen(context));
//
//        prepVideoPlayerActivity = new iPrepVideoPlayerActivity();
//        prepVideoPlayerActivity.setArguments(bundl);
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.videocontainer, prepVideoPlayerActivity, "tag");
//        transaction.addToBackStack(null);

//        transaction.commit();
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

    private void listners() {

        supportHistory.setVisibility(View.GONE);
        supportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PracticeTopicActivity.this, MyIssuesActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);            }
        });

        faqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPageType(context,AboutPalActivity.FAQ);
                startActivity(new Intent(PracticeTopicActivity.this, AboutPalActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);

            }
        });

        aboutPAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPageType(context,AboutPalActivity.ABOUTPAL);
                startActivity(new Intent(PracticeTopicActivity.this, AboutPalActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);            }
        });

    }

    TextView expiretext;
    public String type;

    private void showCustomPopupMenu() {
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.dialog_exit_mode,  null);

        PopupWindow popupWindow=new PopupWindow();
        popupWindow.setContentView(view);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);


        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("---------");
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < 0 || motionEvent.getX() > LinearLayout.LayoutParams.WRAP_CONTENT) return true;
                return motionEvent.getY() < 0 || motionEvent.getY() > LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void assignIds() {
        context = this;
        Util.setContext(context);
        Util.get_support_dialog_language();

        Util.getUserId(context);



//        if(!isTablet(context)) {
//            Util.setPortraitMode(context, true);
//            setPortraitView(this);
//        }
//        else {
//            Util.setPortraitMode(context, false);
//            setLandscapeView(this);
//        }

        global = (Global) getApplicationContext();
        language = Util.getSelectedLanguagePackage(context);
        studentClass = Util.getSelectedClass(context);
        board = Util.getSelectedBoard(context);

        if(!Util.isHomeboadingDone(context))
//            showIntroVideo();
            show_onboading();
        new UserActivities(this);


        checkAppId();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reportsDiagnosticTestCompleteRepository = new ReportsDiagnosticTestCompleteRepository(context);

        TextView aboutPALText,issueHistoryText,faqText;

        aboutPALText = findViewById(R.id.aboutPALText);
        issueHistoryText = findViewById(R.id.issueHistoryText);
        faqText = findViewById(R.id.faqText);
        textViewTitle = findViewById(R.id.textViewTitle);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textViewAnalytics = findViewById(R.id.textViewAnalytics);
        textViewLanguage = findViewById(R.id.textViewLanguage);
        textViewShare = findViewById(R.id.textViewShare);
        textViewiPrepWork = findViewById(R.id.textViewiPrepWork);
        textViewWriteToUs = findViewById(R.id.textViewWriteToUs);
        textViewLogout = findViewById(R.id.textViewLogout);
        logoutLayout = findViewById(R.id.logoutLayout);
        textViewUserName = findViewById(R.id.textViewUserName);
        profileImage = findViewById(R.id.profileImage);
        textViewClass_Board = findViewById(R.id.textViewClass_Board);
        textViewUserName.setText(Util.getUsernameShowable(context));

        if (Util.getSelectedLanguage(context).equals("hindi")) {
            aboutPALText.setText("iPrep PAL के बारे में");
            issueHistoryText.setText("समस्या की  रिपोर्ट");
            faqText.setText("सामान्य प्रश्न");
        }
        else {
            aboutPALText.setText("How iPrep Pal works   ");
            issueHistoryText.setText("Report an issue History");
            faqText.setText("FAQ");
        }

        if(Util.isOfflineMode(context)) try {

            String image=Util.getUserProfileUrl(context);

            if (image == null)  Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImage);
            else {
                String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ image+".png";
                File file = new File(filePath);

                Uri uri = null;

                if(file.exists()){
                    uri = Uri.fromFile(file);
                }

                if(uri != null){
                    Glide.with(context)
                            .load(uri).into(profileImage);
                }
            }

//            Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImage);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        else try {
            global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("profileImage").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null)
                    {
                        Util.setUserProfile(context,snapshot.getValue().toString());
                        try {
                            Glide.with(context).load(snapshot.getValue().toString()).into(profileImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        AnalyticsLayout = findViewById(R.id.AnalyticsLayout);
        navlanguageLayout = findViewById(R.id.navlanguageLayout);
        headerLayout = findViewById(R.id.headerLayout);
        shareLayout = findViewById(R.id.shareLayout);
        writeToUsLayout = findViewById(R.id.writeToUsLayout);
        iPrepWorkLayout = findViewById(R.id.iPrepWorkLayout);
        supportHistory = findViewById(R.id.supportHistory);
        aboutPAL = findViewById(R.id.aboutPAL);
        faqLayout = findViewById(R.id.faqLayout);
        drawer_layout = findViewById(R.id.drawer_layout);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmpty);
        classLayout = findViewById(R.id.classLayout);
        classTextView = findViewById(R.id.classTextView);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);
        profileImage = findViewById(R.id.profileImage);
        lotie_player = findViewById(R.id.lotie_player);

//        writeToUsLayout.setVisibility(View.GONE);

        sclassText="Class";

        if(Util.getSelectedLanguage(context).equals("hindi")) sclassText="कक्षा";
        else sclassText="Class";

        classTextView.setText(sclassText+" " +Util.getSelectedClass(context).replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities"));

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);
        linearSecond = findViewById(R.id.linearSecond);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

//        hideNavigationBar(getWindow());
        languageLayout = findViewById(R.id.languageLayout);
        languageTextView = findViewById(R.id.languageTextView);

        if (Util.getSelectedLanguage(context) != null) {
            if (Util.getSelectedLanguage(context).equals("english")) {
                languageTextView.setText("ENG");
            } else {
                languageTextView.setText("हिंदी");
            }
        }
        topTransparentBg = findViewById(R.id.topTransparentBg);
        bottomTransparentBg = findViewById(R.id.bottomTransparentBg);
        fullScreenTransparentBg = findViewById(R.id.fullScreenTransparentBg);
        imageViewCrossNotification = findViewById(R.id.imageViewCrossNotification);
        reletiveDrawerLayoutNotification = findViewById(R.id.reletiveDrawerLayoutNotification);
        reletiveDrawerLayout = findViewById(R.id.reletiveDrawerLayout);
        imageViewCrossMenu = findViewById(R.id.imageViewCrossMenu);
        imageViewCrossNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.closeDrawer(Gravity.RIGHT);
            }
        });
        reletiveDrawerLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.closeDrawer(Gravity.RIGHT);
            }
        });
        reletiveDrawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        imageViewCrossMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        imageViewMessage = findViewById(R.id.imageViewMessage);
        imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.openDrawer(Gravity.RIGHT);
            }
        });
        lotie_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.openDrawer(Gravity.RIGHT);
            }
        });
        imageViewBack = findViewById(R.id.imageViewDrawer);
        imageViewBack.clearFocus();
        logoIprep = findViewById(R.id.logoIprep);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                drawer_layout.openDrawer(Gravity.LEFT);
                if (textViewClass_Board != null) {
                    if (Util.getSelectedClass(context).contains("11") || Util.getSelectedClass(context).contains("12")) {
                        if (Util.getSelectedClass(context).contains("11"))
                        {
                            textViewClass_Board.setText(sclassText + "11th");
                        }else if(Util.getSelectedClass(context).contains("12"))
                        {
                            textViewClass_Board.setText(sclassText + "12th");
                        }
                    } else {
                        textViewClass_Board.setText(sclassText + Util.getSelectedClass(context) + "th ");
                    }

                }
                if (classTextView != null) {
                    classTextView.setText(sclassText+" " + Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities"));
                }
            }
        });

        try {
            logoIprep = findViewById(R.id.logoIprep);

            logoIprep.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.versiondetails_layout);
                    dialog.setCancelable(true);
                    TextView updated_date_text = dialog.findViewById(R.id.updated_date_text);
                    TextView app_version_text = dialog.findViewById(R.id.app_version_text);
                    Button open_launcher_btn = dialog.findViewById(R.id.open_launcher_btn);
                    ImageView close_version_ly_btn = dialog.findViewById(R.id.close_version_ly_btn);

                    TextView detailsText = dialog.findViewById(R.id.detailsText);
                    TextView versionText = dialog.findViewById(R.id.versionText);
                    TextView updateText = dialog.findViewById(R.id.updateText);

                    if(Util.getSelectedLanguage(context).equals("hindi")) {
                        detailsText.setText("विवरण");
                        versionText.setText("एप्लिकेशन वेरीज़न");
                        updateText.setText("ऐप अपडेट किया गया");
                        open_launcher_btn.setText("लॉन्चर खोलें");
                    }

                    Long buildDate = BuildConfig.TIMESTAMP;
                    String dateString = DateFormat.format("dd-MM-yyyy", new Date(buildDate)).toString();
                    int versionNo = BuildConfig.VERSION_CODE;

                    updated_date_text.setText(dateString);
                    try {
                        PackageManager pm = context.getPackageManager();
                        PackageInfo pInfo = pm.getPackageInfo("com.idreameducation.ipreppal", 0);
                        app_version_text.setText(pInfo.versionName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    close_version_ly_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    open_launcher_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_exit_mode);
                            dialog.setCancelable(true);
                            final EditText editTextPassword = dialog.findViewById(R.id.editTextPassword);
                            Button btn1 = dialog.findViewById(R.id.btn1);
                            btn1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!editTextPassword.getText().toString().equals(context.getResources().getString(R.string.Pass))) {
                                        String message;
                                        message = "Password is incorrect";
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(Intent.createChooser(intent, getString(R.string.please_set_launcher_setting)));
                                    Util.preventTwoClick(view);
                                }
                            });
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

                        }
                    });
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

                    return false;
                }
            });


        } catch (Exception r){}

        NotificationRecyclerView = findViewById(R.id.NotificationRecyclerView);
        NotificationRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager_ = new LinearLayoutManager(context);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        NotificationRecyclerView.setLayoutManager(manager_);

        /* check refer link */
        DeepLinkManager deepLinkManager = new DeepLinkManager((FragmentActivity) context);
        deepLinkManager.checkForInvites();

//        boolean keepAsLauncher = true;
        String packageName = context.getPackageName();
//        ComponentName componentName = new ComponentName(packageName, packageName + ".PracticeTopicActivity.alias");
//        PackageManager pm = getPackageManager();
//        if (keepAsLauncher) {
//            System.out.println("True");
//            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//        } else {
//            System.out.println("False");
//            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//        }
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }



        type = getIntent().getStringExtra("goto");

        if(type==null) type="HomePage";

        Fragment homeFragment = new PalHomeFragment();

        System.out.println("====== type a "+getIntent().getStringExtra("goto"));
        System.out.println("====== type h "+type);

        FullScreencall();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnApplyWindowInsetsListener(null);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setItemIconTintList(null);
        if(type.equals("HomePage")) {
            openFragment(homeFragment);
            bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_active_home);
            bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_inactive_batch);
        }
        else if(type.contains("notification")) {
            openFragment(new BatchesFragments());

            bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.inactive_home);
            bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_active_batch);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.home){
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_active_home);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_inactive_batch);
                    openFragment(homeFragment);
                }
                else if(menuItem.getItemId() == R.id.batches){
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.inactive_home);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_active_batch);
                    openFragment(new BatchesFragments());
                }

                return true;
            }
        });
        get_support_dialog_language();
        try {
            showIntroDialog();
            setStaticText();
            try {
                getNotifications();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            getAssignedContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if(!Util.getSelectedLanguage(context).equals("english")) {
//            findViewById(R.id.searchImageView).setVisibility(View.GONE);
//        }
//        else {
//            if(!Util.isPortraitMode(context)) findViewById(R.id.searchImageView).setVisibility(View.VISIBLE);
//        }

        findViewById(R.id.searchImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PracticeTopicActivity.this,PalGlobalSearchActivity.class));
            }
        });

        findViewById(R.id.AnalyticsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                startActivity(new Intent(context, PalReportsActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);
            }
        });

        findViewById(R.id.navlanguageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                openLanguageSwitchDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },50);

            }
        });

        findViewById(R.id.logoutLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                logoutPopup();
            }
        });

        findViewById(R.id.headerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                startActivity(new Intent(context, PalProfileActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);
            }
        });

        findViewById(R.id.iPrepWorkLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlineLink,fileName,screenName;
                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                {
                    onlineLink = "https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/how_iprep_works%2Fhow_the_app_works_hindi.pdf?alt=media&token=3cb54ed6-12f0-4b61-9423-e196985b576b";
                    fileName = "how_the_app_works_hindi";
                    screenName = "iPrep Pal ऐप कैसे काम करता है";
                }else {
                    onlineLink = "https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/how_iprep_works%2Fhow_the_app_works_english.pdf?alt=media&token=df98401f-a328-4f24-9002-420bb3fdea68";
                    fileName = "how_the_app_works_english";
                    screenName = "How the iPrep Pal app Works";
                }

                Util.preventTwoClick(v);
                Intent intent = new Intent(context, HowTheAppWorksPdf.class);
                intent.putExtra("onlineLink",onlineLink);
                intent.putExtra("topic","how_the_app_works");
                intent.putExtra("offlineLink",fileName+".pdf");
                intent.putExtra("topicName", screenName);
                intent.putExtra("name", screenName);
                intent.putExtra("subjectName", "how_the_app_works");
                intent.putExtra("bookId", fileName);
                intent.putExtra("categoryID", "how_the_app_works");
                intent.putExtra("topicId","how_the_app_works");
                intent.putExtra("topic_name_main","how_the_app_works");
                context.startActivity(intent);
            }
        });
        findViewById(R.id.writeToUsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                startActivity(new Intent(context, PalContactUsNew.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);
            }
        });
        findViewById(R.id.shareLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                startActivity(new Intent(context, PalShareEarnActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                },1000);
            }
        });findViewById(R.id.languageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                openLanguageSwitchDialog();
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                animateFAB();
            }
        });



        updateInfo();
        listners();
        getLanguages();
    }

    private void getLanguages() {
        global.getDatabaseReference().child(ApplicationConstants.LANGUAGE).child("cbse").child("language").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        languageArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                        for (int i = 0; i < languageArrayList.size(); i++) {
                            if (languageArrayList.get(i).get("id").equalsIgnoreCase(Util.getSelectedLanguage(context)))
                            {
                                languageArrayList.get(i).put("selected", "true");
                            }else {
                                languageArrayList.get(i).put("selected", "false");
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

    private void showIntroDialog() {


        if (Util.isIntroDialogToBeShown(context)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            HomeScreenIntroDialogFragment homeScreenIntroDialogFragment = HomeScreenIntroDialogFragment.newInstance();
            homeScreenIntroDialogFragment.show(fragmentManager, "homeScreenIntroDialogFragment");
            Util.setIntroDialog(context, false);
        }
    }

    public void showFullScreenTransparentBg() {
        if (fullScreenTransparentBg != null) {
            fullScreenTransparentBg.setVisibility(View.VISIBLE);
        }
    }

    public void hideFullScreenTransparentBg() {
        if (fullScreenTransparentBg != null) {
            fullScreenTransparentBg.setVisibility(View.GONE);
        }
    }

    public void showOnlySubjectsScreen() {
        if (Util.isOnlySubjectsToBeShown(context)) {
            showTransparentBackgroundLayout();
            Util.setShowOnlySubjects(context, false);
        }
    }

    public void setMarginTransparentBackground(int margin) {
        if (bottomTransparentBg != null && topTransparentBg != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomTransparentBg.getLayoutParams();
            params.setMargins(0, margin, 0, 0);
            bottomTransparentBg.setLayoutParams(params);
        }
    }

    public void showTransparentBackgroundLayout() {
        if (bottomTransparentBg != null && topTransparentBg != null) {
            topTransparentBg.setVisibility(View.VISIBLE);
            bottomTransparentBg.setVisibility(View.VISIBLE);
        }
    }

    public void hideTransparentBackgroundLayout() {
        if (bottomTransparentBg != null && topTransparentBg != null) {
            topTransparentBg.setVisibility(View.GONE);
            bottomTransparentBg.setVisibility(View.GONE);
        }
    }

    private void setLayoutText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject("english");
                JSONObject object_ = object.getJSONObject("Main_Tab_Selection_Screen");
                bottomNavigationView.getMenu().getItem(0).setTitle((String) object_.get("home_tab_title"));
                bottomNavigationView.getMenu().getItem(1).setTitle((String) object_.get("batches_tab_title"));
//                bottomNavigationView.getMenu().getItem(2).setTitle((String) object_.get("saved_tab_title"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.databaseReference.child("pal_test").child("labels").child("english").child("Main_Tab_Selection_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                            bottomNavigationView.getMenu().getItem(0).setTitle(dataHashMap.get("home_tab_title"));
                            bottomNavigationView.getMenu().getItem(1).setTitle(dataHashMap.get("batches_tab_title"));
//                            bottomNavigationView.getMenu().getItem(2).setTitle(dataHashMap.get("saved_tab_title"));
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

    public static String sClass,callNo;
    private String sBoard;

    public ArrayList<String> textArrayList;
    public ArrayList<String> onBoadingtextArrayList;
    public void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("homeScreen");
                textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }

                if (textArrayList.size() > 0) {

                    textOfChangeLanguageDialog = textArrayList.get(7);
                    if(Util.getSelectedLanguage(context).equals("hindi")) sClass = "कक्षा"; // change to 27  after offline folder update
                       else sClass = "Class"; // change to 27  after offline folder update
                    sBoard = textArrayList.get(13);
                    changeLanguageButton = textArrayList.get(10);
                    textViewLanguage.setText(textArrayList.get(10));
                    textViewAnalytics.setText(textArrayList.get(11));
                    textViewWriteToUs.setText(textArrayList.get(12));
                    textViewLogout.setText(textArrayList.get(13));
                    textViewiPrepWork.setText(textArrayList.get(14));
                    callNo = textArrayList.get(15);
                    textToSend = textArrayList.get(16);
                    heading1Text =textArrayList.get(17);
                    heading2Text =textArrayList.get(18);
                    heading3Text =textArrayList.get(19);
                    heading4Text =textArrayList.get(20);
                    button1Text =textArrayList.get(21);
                    button2Text =textArrayList.get(22);
                    button3Text =textArrayList.get(23);

                    textViewTitle.setText(textArrayList.get(24));
                    textView1.setText(textArrayList.get(25));
                    textView2.setText(textArrayList.get(26));


                    bottomNavigationView.getMenu().getItem(0).setTitle(textArrayList.get(4));
                    bottomNavigationView.getMenu().getItem(1).setTitle(textArrayList.get(5));


                    classTextView.setText(sClass + " " + Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities"));
//                    classTextView.setText(sclassText +Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities"));

                    if (Util.getSelectedClass(context).contains("11")) textViewClass_Board.setText(sClass + " " + "11th");
                    else if(Util.getSelectedClass(context).contains("12")) textViewClass_Board.setText(sClass + " " + "12th");


//

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("homeScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            if (textArrayList.size() > 0) {
                                textOfChangeLanguageDialog = textArrayList.get(7);
                                sClass = textArrayList.get(27);
                                sBoard = textArrayList.get(13);
                                changeLanguageButton = textArrayList.get(10);
                                textViewLanguage.setText(textArrayList.get(10));
                                textViewAnalytics.setText(textArrayList.get(11));
                                textViewWriteToUs.setText(textArrayList.get(12));
                                textViewLogout.setText(textArrayList.get(13));
                                textViewiPrepWork.setText(textArrayList.get(14));
                                callNo = textArrayList.get(15);
                                textToSend = textArrayList.get(16);
                                heading1Text =textArrayList.get(17);
                                heading2Text =textArrayList.get(18);
                                heading3Text =textArrayList.get(19);
                                heading4Text =textArrayList.get(20);
                                button1Text =textArrayList.get(21);
                                button2Text =textArrayList.get(22);
                                button3Text =textArrayList.get(23);

                                textViewTitle.setText(textArrayList.get(24));
                                textView1.setText(textArrayList.get(25));
                                textView2.setText(textArrayList.get(26));

                                classTextView.setText(sClass + " " + Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities"));


                                bottomNavigationView.getMenu().getItem(0).setTitle(textArrayList.get(4));

                                bottomNavigationView.getMenu().getItem(1).setTitle(textArrayList.get(5));


                            }
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
    }

    private final int count = 0;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public void openFragment(Fragment fragment) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment, "tag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private HashMap<String, Object> data;

    private ArrayList<String> unReadNotifications;
    TextView markAllRead;
    public static int notificationPos=1;
    private void getNotifications() throws Exception {

        markAllRead=findViewById(R.id.markAllRead);
        markAllRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unReadNotifications!=null) markNotificationRead();
            }
        });
        if(Util.checkInternetConnection(context)) global.getDatabaseReference().child("notifications").child("student").child(Util.getUserId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unReadNotifications=new ArrayList<>();
                ArrayList<Notification_model> notification_list=new ArrayList<>();
                try {
                    if (snapshot.getValue() != null) {
                        for(DataSnapshot ss:snapshot.getChildren()) {
                            Notification_model notification_model=ss.getValue(Notification_model.class);
                            notification_model.setKey(ss.getKey());
                            if(!notification_model.isRead()) unReadNotifications.add(ss.getKey());
                            notification_list.add(notification_model);
                        }

                        Notification_Adapter notification_adapter=new Notification_Adapter(notification_list);
                        NotificationRecyclerView.setAdapter(notification_adapter);
                        NotificationRecyclerView.setVisibility(View.VISIBLE);
                        linearLayoutEmpty.setVisibility(View.GONE);
                        NotificationRecyclerView.smoothScrollToPosition(notificationPos);
                        if(unReadNotifications.size()!=0) updateNotificationIcon(true);
                        else updateNotificationIcon(false);
                    } else {
                        NotificationRecyclerView.setVisibility(View.GONE);
                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        else updateNotificationIcon(false);
    }

    private void markNotificationRead() {
        for(int i=0;i<=unReadNotifications.size()-1;i++) FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(context))
                .child(unReadNotifications.get(i)).child("read").setValue(true);

    }

    private void getAssignedContent() throws Exception {

        global.getDatabaseReference().child("content_assignement_student_wise").child(Util.getUserId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, Object> assignedHashMap = (HashMap<String, Object>) snapshot.getValue();
                        assignedContentArrayList = new ArrayList<>();
                        for (String date : assignedHashMap.keySet()) {
                            HashMap<String, Object> dataIbject = (HashMap<String, Object>) assignedHashMap.get(date);
                            for (String key : dataIbject.keySet()) {
                                HashMap<String, Object> dataInnerbject = (HashMap<String, Object>) dataIbject.get(key);
                                dataInnerbject.put("isOpened", false);
                                dataInnerbject.put("date", date);
                                dataInnerbject.put("key", key);
                                assignedContentArrayList.add(dataInnerbject);
                            }
                        }
                        if (assignedContentArrayList.size() > 0) {
                            NotificationRecyclerView.setVisibility(View.VISIBLE);
                            linearLayoutEmpty.setVisibility(View.GONE);
                            AssignedContentAdapter assignedContentAdapter = new AssignedContentAdapter(context, assignedContentArrayList);
                            NotificationRecyclerView.setAdapter(assignedContentAdapter);
                            assignedContentAdapter.SetOnItemClickListener(new AssignedContentAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
//                                    String date = Util.getCurrentDateWithDifferentFormat().split(" ")[0];
//                                    long endDate = (long) assignedContentArrayList.get(position).get("endDate");
//                                    String dateString = DateFormat.format("dd MM yy", new Date(endDate)).toString();
//                                    String dateTocompare = dateString.split(" ")[0];
//                                    int diff = Integer.parseInt(dateTocompare) - Integer.parseInt(date);
//                                    //   if (diff >= 0) {
//                                    String batchId = (String) assignedContentArrayList.get(position).get("batchId");
//                                    String teacherID = (String) assignedContentArrayList.get(position).get("teacherID");
//                                    String dateToSend = (String) assignedContentArrayList.get(position).get("date");
//                                    String key = (String) assignedContentArrayList.get(position).get("key");
//                                    String subject = (String) assignedContentArrayList.get(position).get("subject");
//                                    String batchClass = (String) assignedContentArrayList.get(position).get("batchClass");
//                                    String type = (String) assignedContentArrayList.get(position).get("type");
//                                    switch (type) {
//                                        case "video_lessons":
//                                            HashMap<String, String> videoMap = (HashMap<String, String>) assignedContentArrayList.get(position).get("videoMap");
//                                            String onlineLink = videoMap.get("onlineLink");
//                                            String[] urlArray = onlineLink.split("/");
//                                            String url = urlArray[urlArray.length - 1];
//                                            String topicID = (String) assignedContentArrayList.get(position).get("topicID");
//                                            String videoName = videoMap.get("name");
//                                            Util.preventTwoClick(view);
//                                            Intent intent = new Intent(context, PalAssignedVideoPlayerActivity.class);
//                                            intent.putExtra("url", url);
//                                            intent.putExtra("videoName", videoName);
//                                            intent.putExtra("topicID", topicID);
//                                            intent.putExtra("subject", subject);
//                                            intent.putExtra("teacherID", teacherID);
//                                            intent.putExtra("date", dateToSend);
//                                            intent.putExtra("key", key);
//                                            intent.putExtra("batchId", batchId);
//                                            intent.putExtra("isAssigned", "true");
//                                            startActivity(intent);
//                                            break;
//                                        case "biMonthlyTest":
//                                            try {
//                                                String topicName = (String) assignedContentArrayList.get(position).get("topicName");
//                                                getBimonthlyTestTopics(subject, batchClass, teacherID, dateToSend, key, batchId, topicName);
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            break;
//                                        case "practice":
//                                            HashMap<String, String> practiceMap = (HashMap<String, String>) assignedContentArrayList.get(position).get("practiceMap");
//                                            String topicId = practiceMap.get("TopicID");
//                                            String streak = practiceMap.get("StreakCount");
//                                            String incorrect_streak = practiceMap.get("incorrectStreak");
//                                            String TNAME = practiceMap.get("TName");
//                                            Util.setTopicID(context, topicId);
//                                            Util.setTopicNameAlt(context, TNAME);
//                                            Util.setLevel(context, Integer.parseInt("1"));
//                                            String studentClass = (String) assignedContentArrayList.get(position).get("batchClass");
//                                            String streakProgress = "0";
//                                            global.setProgress(0);
//                                            Util.preventTwoClick(view);
//                                            if (Util.isNetworkAvailable(context)) {
//                                                startActivity(new Intent(context, QuizActivity.class).putExtra("teacherID", teacherID).putExtra("date", dateToSend).putExtra("key", key).putExtra("batchId", batchId).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", "null").putExtra("seniorTopicID", "null"));
//
//                                            }else {
//                                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                                {
//                                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
//                                                }else {
//                                                    Util.openGifDialogue(context,"Internet Connection is not working");
//                                                }
//                                            }
//                                            break;
//                                        case "diagnosticTest":
//                                            String topicName = (String) assignedContentArrayList.get(position).get("topicName");
//                                            String topicId1 = (String) assignedContentArrayList.get(position).get("topicID");
//                                            Util.setTopicNameAlt(context, topicName);
//                                            Util.setTopicID(context, topicId1);
//                                            Util.preventTwoClick(view);
//                                            Intent intent1 = new Intent(context, DStartActivity.class);
//                                            intent1.putExtra("teacherID", teacherID);
//                                            intent1.putExtra("date", dateToSend);
//                                            intent1.putExtra("key", key);
//                                            intent1.putExtra("batchId", batchId);
//                                            intent1.putExtra("isAssigned", "true");
//                                            intent1.putExtra("sClass", batchClass);
//                                            context.startActivity(intent1);
//                                            break;
//                                    }
//                                    } else {
//                                        Util.showToast(context, "Date has been expired");
//                                    }
                                }
                            });


                        } else {
                            NotificationRecyclerView.setVisibility(View.GONE);
                            linearLayoutEmpty.setVisibility(View.VISIBLE);
                        }

                    } else {
                        NotificationRecyclerView.setVisibility(View.GONE);
                        linearLayoutEmpty.setVisibility(View.VISIBLE);
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

    public void updateNotificationIcon(boolean show) {

        if (imageViewMessage != null && show) {
            lotie_player.setAnimation("bell1.json");
            lotie_player.loop(true);
            lotie_player.playAnimation();
            imageViewMessage.setImageResource(R.drawable.ic_active_bell);
            imageViewMessage.setTag("notification");
            if(markAllRead!=null) markAllRead.setVisibility(View.VISIBLE);
        } else {
            lotie_player.setAnimation("bell2.json");
            lotie_player.loop(false);
            lotie_player.playAnimation();
            imageViewMessage.setImageResource(R.drawable.ic_inactice_bell);
            imageViewMessage.setTag("no_notification");
            if(markAllRead!=null) markAllRead.setVisibility(View.GONE);
        }
    }

    private void getBimonthlyTestTopics(String subject, String sClass, String teacherID, String dateToSend, String key, String batchId, String topicName) throws Exception {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
            try {

                DiagnosticCompleteTask diagnosticCompleteTask = new DiagnosticCompleteTask(Util.getUserId(context), board, sClass, subject.toLowerCase(), null, teacherID, dateToSend, key, batchId, topicName);
                diagnosticCompleteTask.execute();

//                HashMap<String, Boolean> topicsHashMap = new HashMap<>();
//                List<ReportsDiagnosticCompleteModel> list = reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, subject.toLowerCase(), null);
//                if (list != null && list.size() > 0) {
//                    for(ReportsDiagnosticCompleteModel item: list){
//                        topicsHashMap.put(item.getTopicId(), item.isComplete());
//                    }
//                    ArrayList<String> topicsArrayList = new ArrayList<>();
//                    for (String topicID : topicsHashMap.keySet()) {
//                        topicsArrayList.add(topicID);
//                    }
//                    global.setTopicIdArrayList(topicsArrayList);
//                    Intent intent = new Intent(context, BiMonthlyTestActivity.class);
//                    intent.putExtra("sClass", sClass);
//                    intent.putExtra("teacherID", teacherID);
//                    intent.putExtra("date", dateToSend);
//                    intent.putExtra("key", key);
//                    intent.putExtra("batchId", batchId);
//                    intent.putExtra("isAssigned", "true");
////                    intent.putExtra("subject", "science");
//                    intent.putExtra("subject", subject.toLowerCase());
//                    intent.putExtra("topicName", topicName);
//                    startActivity(intent);
//                    
//                }else{
//                    Util.showToast(context, "You did not attempt any of the topic yet");
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(board).child(sClass).child(subject.toLowerCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            HashMap<String, String> topicsHashMap = (HashMap<String, String>) snapshot.getValue();
                            ArrayList<String> topicsArrayList = new ArrayList<>();
                            for (String topicID : topicsHashMap.keySet()) {
                                topicsArrayList.add(topicID);
                            }
                            global.setTopicIdArrayList(topicsArrayList);
                            Intent intent = new Intent(context, BiMonthlyTestActivity.class);
                            intent.putExtra("sClass", sClass);
                            intent.putExtra("teacherID", teacherID);
                            intent.putExtra("date", dateToSend);
                            intent.putExtra("key", key);
                            intent.putExtra("batchId", batchId);
                            intent.putExtra("isAssigned", "true");
//                            intent.putExtra("subject", "science");
                            intent.putExtra("subject", subject.toLowerCase());
                            intent.putExtra("topicName", topicName);
                            startActivity(intent);
                            
                        } else {
                            Util.showToast(context, "You did not attempt any of the topic yet");
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

    public static Dialog dialog;

    private void updateInfo() {

        try {
            global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(Util.getUserId(context) ).child("sClass").setValue(Util.getSelectedClass(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void openGifDialogue() {

        try {
            dialog = new Dialog(Util.getContext());
            dialog.setContentView(R.layout.dialogue_toast_messages);
            dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
            dialog.setCancelable(true);
            TextView text = dialog.findViewById(R.id.text);
            ImageView crossImage = dialog.findViewById(R.id.crossImage);
            LinearLayout connetion_lyt = dialog.findViewById(R.id.connetion_lyt);

            TextView text_ = dialog.findViewById(R.id.text_);
            TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
            textViewRetry.setVisibility(View.GONE);

            if(Util.getSelectedLanguage(Util.getContext()).equalsIgnoreCase("hindi"))
            {
                text.setText("इंटरनेट नहीं है");
                text_.setText("कृपया अपना इंटरनेट चेक करे ");
                textViewRetry.setText("इंटरनेट चालू करें");
            }
            else
            {
                text.setText("Oops! No Internet");
                text_.setText("Please check your network connection");
                textViewRetry.setText("Turn on Wifi");
            }


            ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
            Glide.with(Util.getContext()).load(R.raw.no_internet).into(imageViewGif);

            crossImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            textViewRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });

            connetion_lyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    view.postDelayed(()-> view.setEnabled(true), 1000);
                    System.out.println("-------- clicked ");
//                    NetworkStateReceiver.notifyStateToAll();

                }
            });

            try {
                dialog.show();
                /* Change the background color of the dialog */
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Util.getContext().getResources().getColor(android.R.color.transparent)));
                dialog.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                //Clear the not focusable flag from the window
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            }catch (Exception r){r.printStackTrace();}
        }
        catch (Exception e){e.printStackTrace();}

    }

    private class DiagnosticCompleteTask extends AsyncTask<Void, Void, ArrayList<ReportsDiagnosticCompleteModel>> {

        String userId;
        String board;
        String sClass;
        String subject;
        String topicId;
        String teacherID;
        String dateToSend;
        String key;
        String batchId;
        String topicName;

        private DiagnosticCompleteTask(String userId, String board, String sClass, String subject, String topicId, String teacherID, String dateToSend, String key, String batchId, String topicName) {
            this.userId = userId;
            this.board = board;
            this.sClass = sClass;
            this.subject = subject;
            this.topicId = topicId;
            this.teacherID = teacherID;
            this.dateToSend = dateToSend;
            this.key = key;
            this.batchId = batchId;
            this.topicName = topicName;
        }

        @Override
        protected ArrayList<ReportsDiagnosticCompleteModel> doInBackground(Void... voids) {
            return (ArrayList<ReportsDiagnosticCompleteModel>) reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(context), board, sClass, subject.toLowerCase(), null);
        }

        @Override
        protected void onPostExecute(ArrayList<ReportsDiagnosticCompleteModel> list) {
            super.onPostExecute(list);
            HashMap<String, Boolean> topicsHashMap = new HashMap<>();
            if (list != null && list.size() > 0) {
                for (ReportsDiagnosticCompleteModel item : list) {
                    topicsHashMap.put(item.getTopicId(), item.isComplete());
                }
                ArrayList<String> topicsArrayList = new ArrayList<>();
                for (String topicID : topicsHashMap.keySet()) {
                    topicsArrayList.add(topicID);
                }
                global.setTopicIdArrayList(topicsArrayList);
                Intent intent = new Intent(context, BiMonthlyTestActivity.class);
                intent.putExtra("sClass", sClass);
                intent.putExtra("teacherID", teacherID);
                intent.putExtra("date", dateToSend);
                intent.putExtra("key", key);
                intent.putExtra("batchId", batchId);
                intent.putExtra("isAssigned", "true");
//                    intent.putExtra("subject", "science");
                intent.putExtra("subject", subject.toLowerCase());
                intent.putExtra("topicName", topicName);
                startActivity(intent);
                
            } else {
                Util.showToast(context, "You did not attempt any of the topic yet");
            }
        }
    }

    private void openContactUsDialogue() {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(false);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from - " +Util.getUsernameShowable(context));
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                && checkSelfPermission(Manifest.permission.CALL_PHONE)

                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+callNo));
            startActivity(callIntent);

        }
    }

    @SuppressLint("RestrictedApi")
    public void animateFAB() {
        buttonScreenshot(null);
//        if (isFabOpen) {
//
//            fab.setImageResource(R.drawable.ic_inactive);
//            isFabOpen = false;
////            fab.startAnimation(rotate_backward);
////            fab1.startAnimation(fab_close);
////            fab2.startAnimation(fab_close);
////            screenshotText.startAnimation(fab_close);
////            callText.startAnimation(fab_close);
////            fab1.setClickable(false);
////            fab2.setClickable(false);
//
////            callText.setVisibility(View.GONE);
////            screenshotText.setVisibility(View.GONE);
////            fab1.setVisibility(View.GONE);
////            fab2.setVisibility(View.GONE);
//
//        } else {
//
//            fab.setImageResource(R.drawable.ic_active);
//            isFabOpen = true;
//
//            buttonScreenshot(null);
//
////            fab1.setImageResource(R.drawable.ic_screenshot_inactive);
////            fab2.setImageResource(R.drawable.ic_call_inactive);
////            fab1.performClick();
////            fab1.setVisibility(View.GONE);
////            fab2.setVisibility(View.GONE);
////            fab.startAnimation(rotate_forward);
////            fab1.startAnimation(fab_open);
////            callText.startAnimation(fab_open);
////            screenshotText.startAnimation(fab_open);
////            fab2.startAnimation(fab_open);
////            fab1.setClickable(true);
////            fab2.setClickable(true);
//
////            callText.setVisibility(View.VISIBLE);
////            screenshotText.setVisibility(View.VISIBLE);
//
//        }
    }

    private PalLanguageSelectionAdapter palLanguageSelectionAdapter;
    
    private void openLanguageSwitchDialog() {
        Dialog dialog = new Dialog(context,R.style.CustomDialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_langauge_switch);
        TextView textTitle = dialog.findViewById(R.id.textTitle);
        TextView nextButton = dialog.findViewById(R.id.nextButton);
        textTitle.setText(textOfChangeLanguageDialog);
        nextButton.setText(changeLanguageButton);

        ImageView imageViewCancel = dialog.findViewById(R.id.imageViewCancel);
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Util.preventTwoClick(view);
            }
        });
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.hasFixedSize();

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/LanguageDB.txt";
            JSONObject classObect = Util.readJsonFile(context, filePath);
            try {
                JSONObject boardObject = classObect.getJSONObject(board);
                JSONArray languageArray = boardObject.getJSONArray("language");
                ArrayList<HashMap<String, String>> languageArrayList = new ArrayList<>();
                for (int i = 0; i < languageArray.length(); i++) {
                    JSONObject jsonObject = languageArray.getJSONObject(i);
                    HashMap<String, String> languageHashMap = new HashMap<>();
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    languageHashMap.put("name", name);
                    languageHashMap.put("id", id);
                    languageHashMap.put("selected", String.valueOf(id.equalsIgnoreCase(Util.getSelectedLanguage(context))));

                    languageArrayList.add(languageHashMap);

                }
                palLanguageSelectionAdapter = new PalLanguageSelectionAdapter(context, languageArrayList);
                recyclerView.setAdapter(palLanguageSelectionAdapter);
                palLanguageSelectionAdapter.SetOnItemClickListener(new PalLanguageSelectionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        for (int i = 0; i < languageArrayList.size(); i++) languageArrayList.get(i).put("selected", "false");

                        languageArrayList.get(position).put("selected", "true");
                        palLanguageSelectionAdapter.notifyDataSetChanged();
                        language = languageArrayList.get(position).get("id");
                        Util.setLanguagePackageSelection(context, language);

                        if (language != null) {

                            if (Util.getSelectedLanguage(context) == language) dialog.dismiss();
                            else {
                                Util.setLanguagePackageSelection(context, language);
                                Util.setLanguageSelection(context, language);

                                dialog.dismiss();
                                Util.showAnimatedDialog(context,Util.LANGUAGE_UPDATED);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        assignIds();
                                    }
                                }, 3000);

                            }


                        } else {
                            Util.showToast(context, "कृपया अपनी भाषा चुनें");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if(languageArrayList==null) {
                getLanguages();
                return;
            }
            palLanguageSelectionAdapter = new PalLanguageSelectionAdapter(context, languageArrayList);
            recyclerView.setAdapter(palLanguageSelectionAdapter);
            palLanguageSelectionAdapter.SetOnItemClickListener(new PalLanguageSelectionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    for (int i = 0; i < languageArrayList.size(); i++) {
                        languageArrayList.get(i).put("selected", "false");
                    }
                    languageArrayList.get(position).put("selected", "true");
                    palLanguageSelectionAdapter.notifyDataSetChanged();
                    language = languageArrayList.get(position).get("id");

                    if (language != null) {

                        if (Util.getSelectedLanguage(context) == language) dialog.dismiss();
                        else {
                            Util.setLanguagePackageSelection(context, language);
                            Util.setLanguageSelection(context, language);

                            dialog.dismiss();
                            Util.showAnimatedDialog(context,Util.LANGUAGE_UPDATED);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    assignIds();
                                }
                            }, 3000);
                        }

                    }

                }
            });

        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (language != null) {

                    if (Util.getSelectedLanguage(context) == language)
                    {
                        dialog.dismiss();
                    }else {
                        Util.setLanguagePackageSelection(context, language);
                        Util.setLanguageSelection(context, language);

//                    Intent intent;
//                    intent = new Intent(context, PracticeTopicActivity.class);
//                    intent.putExtra("board", board);
//                    intent.putExtra("class", Util.getSelectedClass(context));
//                    startActivity(intent);
//                    recreate();
                        dialog.dismiss();
                        if (language.equalsIgnoreCase("hindi"))
                        {
                            openGifDialogueSuccess(context,"आपकी भाषा बदल दी गई है");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    assignIds();
                                }
                            }, 3000);


                        }else {
                            openGifDialogueSuccess(context,"Your Language has been updated");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    assignIds();
                                }
                            }, 3000);
                        }

                    }


                } else {
                    Util.showToast(context, "कृपया अपनी भाषा चुनें");
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

    public static void openGifDialogueSuccess(Context context, String textError) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_toast_messages);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
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
        }, 3000);

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setVisibility(View.GONE);
        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(context).load(R.raw.submit_gif).into(imageViewGif);
        textViewRetry.setText("Try Again");
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private String linkFromFirebaseStorage,textSendWithImageTextString;

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
//                isFabOpen = false;
            }
        });

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        if(isPortraitMode(context)) dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

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
        hashMapToSync.put("user_name",Util.getUsernameShowable(context));
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
//                                    Toast.makeText(PracticeTopicActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();



                                    Util.openGifDialogueSuccess(context,textToSend);
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

    public void refreshMyData() {

        Intent intent = new Intent(context, PracticeTopicActivity.class);
        
        startActivity(intent);

    }


    public void FullScreencall() {
        if(Util.isPortraitMode(context)) return;

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void hideNavigationBar(Window window) {

//        if(Util.isPortraitMode(Util.context)) return;

        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = SYSTEM_UI_FLAG_LAYOUT_STABLE
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        //...
        return true; //Must return a boolean
    }

    LottieAnimationView animationView;
    LinearLayout onboading_layout;
    TextView onboading_heading_text,onboading_body_text,onboading_skip_text;
    Button onboading_nextbtn,onboarding_previousBtn;
    TextView onboading_point1,onboading_point2,onboading_point3,onboading_point4,onboading_point5,onboading_point6;
    ArrayList<String> jsonList=new ArrayList<>();
    ArrayList<String> headingList=new ArrayList<>();
    ArrayList<String> bodyList=new ArrayList<>();
    int onboading_position=0;
    Animation animation,animation2;

    boolean isPrevious=false;

    private void show_onboading() {

        onboading_layout=findViewById(R.id.onboading_layout);
        animationView=findViewById(R.id.animationView);

        onboading_heading_text=findViewById(R.id.onboading_heading_text);
        onboading_body_text=findViewById(R.id.onboading_body_text);
        onboading_skip_text=findViewById(R.id.onboading_skip_text);
        onboarding_previousBtn=findViewById(R.id.onboarding_previousBtn);
        onboading_nextbtn=findViewById(R.id.onboading_nextbtn);

        onboading_point1=findViewById(R.id.onboading_point1);
        onboading_point2=findViewById(R.id.onboading_point2);
        onboading_point3=findViewById(R.id.onboading_point3);
        onboading_point4=findViewById(R.id.onboading_point4);
        onboading_point5=findViewById(R.id.onboading_point5);
        onboading_point6=findViewById(R.id.onboading_point6);

        onboading_layout.setVisibility(View.VISIBLE);
        onboading_nextbtn.requestFocus();
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_rever);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);

        onboading_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                view.postDelayed(()-> view.setEnabled(true), 150);
                if(!onboading_nextbtn.getText().equals(getstarted_textl)) {
                    onboading_position++;
                    isPrevious=false;
                    showOnBoadingScreen();
                }
                else {
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
                Util.preventTwoClick(view);
//                onboading_layout.startAnimation(animation2);
//                onboading_layout.setVisibility(View.GONE);
            }
        });
        onboading_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-");
            }
        });

        gettext_();
    }

    String continue_text,next_text,getstarted_textl,previous_textl;

    private void gettext_() {

        if(Util.isOfflineMode(context)) set_static_text();
        else global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context))
                    .child("Home onBoarding screen").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null) {
                                    onBoadingtextArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                    setdata();
                                    showOnBoadingScreen();
                                }
                                else set_static_text();
                            } catch (Exception e) {
                                Util.dismissdataDialog();
                                e.printStackTrace();
                                set_static_text();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { set_static_text();}});

    }

    private void set_static_text() {
        onBoadingtextArrayList=new ArrayList<>();
        if(Util.getSelectedLanguage(context).equals("hindi"))
        {
            onBoadingtextArrayList.add("iPrep PAL में आपका स्वागत है");
            onBoadingtextArrayList.add("विषयों");
            onBoadingtextArrayList.add("पुस्तकें");
            onBoadingtextArrayList.add("परियोजनाओं और व्यावहारिक");
            onBoadingtextArrayList.add("रिपोर्टों");
            onBoadingtextArrayList.add("भाषा बदलो");
            onBoadingtextArrayList.add("iPrep व्यक्तिगत अनुकूली शिक्षण के साथ विषयों का व्यापक कवरेज प्रदान करता है जिसका अर्थ है कि iPrep हर कदम पर आपका मार्गदर्शन करेगा।");
            onBoadingtextArrayList.add("अपनी पसंद के विषय का चयन करें और उन विषयों की समझ को गहरा करने के लिए नई अवधारणाएं सीखें जिन्हें आप पहले ही सीख चुके हैं।");
            onBoadingtextArrayList.add("कला, साहित्य, विज्ञान पर कहानियों, कविताओं, प्रेरक जीवनियों और किताबों का अद्भुत संग्रह और आपके समग्र सीखने के लिए बहुत कुछ। अपनी पसंद की किताब चुनें और पढ़ना शुरू करें।");
            onBoadingtextArrayList.add("अपने आस-पास आसानी से उपलब्ध सामग्री का उपयोग करके प्रयोगों को डिजाइन और बनाकर अपने सीखने को क्रियान्वित करें। एक चंचल व्यावहारिक सीखने के अनुभव के लिए डिज़ाइन किया गया।");
            onBoadingtextArrayList.add("अपनी प्रगति जानने के लिए रिपोर्ट अनुभाग में सभी सामग्री, वीडियो, पुस्तकों, अभ्यास के उपयोग को ट्रैक करें।");
            onBoadingtextArrayList.add("भाषा परिवर्तन विकल्प का चयन करके अपनी पसंदीदा भाषा में सीखें।");
            onBoadingtextArrayList.add("जारी रखें");
            onBoadingtextArrayList.add("अगला");
            onBoadingtextArrayList.add("आरंभ करें");

        }
        else
        {
            onBoadingtextArrayList.add("Welcome to iPrep PAL");
            onBoadingtextArrayList.add("Subjects");
            onBoadingtextArrayList.add("Books");
            onBoadingtextArrayList.add("Projects and Practicals");
            onBoadingtextArrayList.add("Reports");
            onBoadingtextArrayList.add("Change Language ");
            onBoadingtextArrayList.add("iPrep offers a comprehensive coverage of subjects with personalised adaptive learning which means that iPrep will guide you at every step. ");
            onBoadingtextArrayList.add("Select a subject of your choice and learn new concepts to deepen an understanding for topics you have already learnt. ");
            onBoadingtextArrayList.add("Amazing collection of stories, poems, inspirational biographies and books on Arts, Literature, Science and so much more for your holistic learning. Select a Book of your choice and start reading.");
            onBoadingtextArrayList.add("Put your learning in action by designing and creating experiments using material easily available around you. Designed for a playful practical learning experience.");
            onBoadingtextArrayList.add("Track usage of all content, videos, books, practice in reports section to know your progress. ");
            onBoadingtextArrayList.add("Learn in your preferred language by selecting the language change option. ");
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
            if(isPortraitMode(context)) {
                jsonList.add("M-ipreppal_logo.json");
                jsonList.add("M-PAL-Sub_Hindi.json");
                jsonList.add("M-PAL-Books_Hindi.json");
                jsonList.add("M-PAL-Proj_Hindi.json");
                jsonList.add("M-PAL-Report_Hindi.json");
                jsonList.add("M-PAL-Lang_Hindi.json");
            }
            else {
                jsonList.add("ipreppal_logo.json");
                jsonList.add("PAL-Sub_Hindi.json");
                jsonList.add("PAL-Books_Hindi.json");
                jsonList.add("PAL-Proj_Hindi.json");
                jsonList.add("PAL-Report_Hindi.json");
                jsonList.add("PAL-Lang_Hindi.json");
            }

        }
        else {
            if(isPortraitMode(context)) {
                jsonList.add("M-ipreppal_logo.json");
                jsonList.add("M-Subjects-v3.json");
                jsonList.add("M-Books-v3.json");
                jsonList.add("M-Projects-v3.json");
                jsonList.add("M-Reports-v3.json");
                jsonList.add("M-Class_Lang-v3.json");
            }
            else {
                jsonList.add("ipreppal_logo.json");
                jsonList.add("Subjects-v3.json");
                jsonList.add("Books-v3.json");
                jsonList.add("Projects-v3.json");
                jsonList.add("Reports-v3.json");
                jsonList.add("Class_Lang-v3.json");
            }
        }

        headingList.add(onBoadingtextArrayList.get(0));
        headingList.add(onBoadingtextArrayList.get(1));
        headingList.add(onBoadingtextArrayList.get(2));
        headingList.add(onBoadingtextArrayList.get(3));
        headingList.add(onBoadingtextArrayList.get(4));
        headingList.add(onBoadingtextArrayList.get(5));

        bodyList.add(onBoadingtextArrayList.get(6));
        bodyList.add(onBoadingtextArrayList.get(7));
        bodyList.add(onBoadingtextArrayList.get(8));
        bodyList.add(onBoadingtextArrayList.get(9));
        bodyList.add(onBoadingtextArrayList.get(10));
        bodyList.add(onBoadingtextArrayList.get(11));

        continue_text=onBoadingtextArrayList.get(12);
        //use index 13 for "next"
        next_text=onBoadingtextArrayList.get(12);
        getstarted_textl=onBoadingtextArrayList.get(14);

        if(Util.getSelectedLanguage(context).equals("hindi")) onBoadingtextArrayList.add("पीछे जाएँ");
        else onBoadingtextArrayList.add("Previous");

        previous_textl=onBoadingtextArrayList.get(15);

        onboading_nextbtn.setText(continue_text);
        onboarding_previousBtn.setText(previous_textl);
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
                Util.setHomeboadingMode(context,true);

//                FirebaseMessaging.getInstance().subscribeToTopic(Util.getUserId(context));
//                Util.sendNormalNotification(Util.getUserId(context),"Welcome","Hello "+Util.getUsernameShowable(context)+" Welcome to the iPrep PAL. iPrep pal app will take you on a learning path you need to you and guide you to achieve for 100% mastery on all topics.");

            }

        }

    }

    private void updateLastNetConnection() {
        if(Util.checkInternetConnection(context)){
            //Updating the last internet connected  to show the 30 or more Day without internet dialog
            Util.setLastNetConnected(context);
        }
    }
    /** Method to Check the user Connected to Internet in past 30 days or more **/
    private void notNetConnection() {
        long currentTime = System.currentTimeMillis();
        // Calculating Days since the last internet is connected
        long daysSinceLastLogin=(currentTime-Util.getLastNetConnected(context))/ (24 * 60 * 60 * 1000);
        // Checking whether current day without internet is more than 30 and less than 40
        if(daysSinceLastLogin>=30 && daysSinceLastLogin<=40){
            // Calculating last seen of popup
            long lastPopupSeen=(currentTime-Util.getLastSeenNetDialog(context))/ (24 * 60 * 60 * 1000);
            // Checking user seen a popup in same day or not... condition only works if user open app same day
            if(lastPopupSeen<=1){
                // Checking user seen a popup in a day or not... if value is 0 then user does not seen popup even 1 time
                if(Util.getNetConnectDialogStatus(context)==0){
                    showPopup();
                }
            }else{
                showPopup();
            }
            // Checking whether current day without internet exceed 40 days then popup show every time when open the App
        }else if(daysSinceLastLogin>40){
            showPopup();
        }else {

        }
    }
    /** Method to show popup for internet connected 30 days or more**/
    private void showPopup() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.no_file_available_dialog);
        dialog.setCancelable(false);
        /* Hook up the UI element in the Dialog */
        TextView textViewTitle=dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText("Kindly connect to internet");

        TextView textViewGoBack=dialog.findViewById(R.id.textViewGoBack);
        textViewGoBack.setText("Okay");

//        dialog.show();
        Util.setLastSeenNetDialog(context,System.currentTimeMillis());
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Update value to confirm that user seen the dialog
                Util.setNetConnectDialogStatus(context,1);
            }
        });
    }

    /** Check Freeze & Launcher values & */
    private void checkAppId() {
//        getNGODetails(Util.getNGOID(context),Util.getTABID(context),Util.getAPPID(context));
        try {
            global.getDatabaseReference().child("app_ngo_relation").child(Util.getNGOID(context))
                    .child(Util.getAPPID(context)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.getValue()==null) return;

                            /* Freeze check */
                            if(Boolean.parseBoolean(String.valueOf(snapshot.child("appLicenseFreeze").getValue()))) {
                                findViewById(R.id.expireLayout).setVisibility(View.VISIBLE);
                                findViewById(R.id.expireLayout).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("-");
                                    }
                                });
                                String expireText = "Hi, <B>" + Util.getUsername(context) + "</B> , your app subscription plan is now expired. Without getting your app subscription renewed, you will not be able to use your iPrep app for learning";

                                expiretext=findViewById(R.id.expiretext);

                                expiretext.setText(Html.fromHtml(expireText));

                                findViewById(R.id.textViewclose).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PracticeTopicActivity.super.onBackPressed();
                                        finish();
                                    }
                                });

                            }
                            else findViewById(R.id.expireLayout).setVisibility(View.GONE);

                            /* Launcher check */
                            Util.setLauncherMode(context,Boolean.parseBoolean(String.valueOf(
                                    snapshot.child("withLauncher").getValue())));

                            if (snapshot.getValue() != null) {
                                HashMap<String, Object> hash = (HashMap<String, Object>) snapshot.getValue();
                                String tID = (String) hash.get("tabID");

                                try {
                                    if (tID.equals(Util.getTABID(context))) {
                                        Util.setActivation(context, true);
                                        long serverTime = (long) hash.get("serverTime");
                                        long localTime = (long) hash.get("localTime");
                                        String days = String.valueOf(hash.get("licensePeriod"));
                                        String image1 = (String) hash.get("image1");
                                        String image2 = (String) hash.get("image2");
                                        String userID = (String) hash.get("userID");
                                        String schoolID = (String) hash.get("schoolId");
                                        String projectID = (String) hash.get("projectId");
                                        String schoolName = (String) hash.get("schoolName");

                                        Util.setSchoolId(context,schoolID);
//                                    Util.setStartDate(context,Long.parseLong(hash.get("startDate").toString()));
                                        Util.setEndDate(context,Long.parseLong(hash.get("endDate").toString()));


                                        Util.setProjectId(context,projectID);
                                        Util.setSchoolName(context,schoolName);
                                        String forPAl = "true";
                                        Util.setForPal(context, forPAl);
                                        ArrayList<String> ngoImages = new ArrayList<>();
                                        ngoImages.add(image1);
                                        ngoImages.add(image2);
                                        Util.setNGOIMAGES(context, ngoImages);

                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("appID", Util.getAPPID(context));
                                        map.put("ngoID", Util.getNGOID(context));
                                        map.put("userID","");


                                        ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(context);
                                        ActivationModel activationModel = new ActivationModel();
                                        activationModel.setAppID(Util.getAPPID(context));
                                        activationModel.setTabID(Util.getTABID(context));
                                        activationModel.setUserid("");
                                        activationModel.setServerTime(serverTime);
                                        activationModel.setLocalTime(localTime);
                                        activationModel.setDays(days);
                                        activationDetailsRepository.insertActivationDetails(activationModel);
                                        global.getDatabaseReference().child("app_tab_relation").child(Util.getTABID(context)).setValue(map);

                                        checkLicenceExpiry();
                                    }
                                }catch (Exception r) {
                                    r.printStackTrace();
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkLicenceExpiry() {
        ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(this);
        activationDetailsRepository.getActivationDetails().observe(this, new Observer<List<ActivationModel>>() {
            @Override
            public void onChanged(@Nullable List<ActivationModel> activationModels) {
                try {
                    if (activationModels.size() > 0) {

                        final Date currentDATE =  new Date();
                        Date endDate1=new Date(Util.getEndDate(context));

                        if(!currentDATE.before(endDate1)){
                            showPlanExpireDialog(context, R.drawable.daysexpired, "Hi <B>" + Util.getUsername(context) + "</B>, Your app subscription plan is now expired. To continue learning, click on the ReCheck button below. Without getting your app subscription renewed, you will not be able to use your iPrep PAL for learning", "ReCheck", "checkReactivationDateAndDuration");
                        }else {
                            if(planExpireDialog!=null) planExpireDialog.dismiss();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Dialog planExpireDialog;

    public void showPlanExpireDialog(Context context, int license_image, String license_text, String license_button, String call_method_name) {

        if(planExpireDialog==null) planExpireDialog= new Dialog(context);

        if(planExpireDialog.isShowing()) {
            TextView button_close = planExpireDialog.findViewById(R.id.textViewclose);
            button_close.setText(license_button);
            return;
        }

        planExpireDialog.setContentView(R.layout.dialog_plan_expire);
        planExpireDialog.setCancelable(false);
        ImageView imageViewClose = planExpireDialog.findViewById(R.id.imageViewClose);
        ImageView liscence_activation_image = planExpireDialog.findViewById(R.id.imageliscence);
        TextView button_close = planExpireDialog.findViewById(R.id.textViewclose);
        TextView license_activation_text = planExpireDialog.findViewById(R.id.text);
        button_close.setText(license_button);
        liscence_activation_image.setBackgroundResource(license_image);
        imageViewClose.setVisibility(View.VISIBLE);
//        Util.getFocusListenerOnButon(context, button_close);
        String expireText = license_text;
        license_activation_text.setText(Html.fromHtml(expireText));

        imageViewClose.setVisibility(View.GONE);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeTopicActivity.super.onBackPressed();
                finish();
            }
        });

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(context)) {
                    button_close.setText("Checking");
                    checkAppId();
//                    getNGODetails(Util.getNGOID(context),Util.getTABID(context),Util.getAPPID(context));

                }
                else {
                    Toast.makeText(context, "Please, Connect with internet to re-activate the License.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planExpireDialog.show();
        planExpireDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

}
