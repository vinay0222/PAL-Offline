package com.idreameducation.ipreppal.util;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.textToSend;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.SubjectInfoModel;
import com.idreameducation.ipreppal.pal.activity.AboutPalActivity;
import com.idreameducation.ipreppal.pal.activity.NotificationActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.SelectTopicActivity;
import com.idreameducation.ipreppal.roomdatabase.model.GsonModel;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by nice on 30-Oct-17.
 */

public class Util {

    public static String rawUsageNode=ApplicationConstants.REPORTS+"/raw_usage";
    public static String segmentedRawUsageNode=ApplicationConstants.REPORTS+"/segmented_raw_usage/raw_usage_daily";


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WERITE_EXTERNAL_STORAGE = 124;

    public static Dialog dataDialog;
    public static Context context;
    public static int count = 0;
    static boolean isavalable = false;
    static Dialog scoreDialog;
    static boolean success = false;
    static Dialog dialog;
    private static Dialog dialogLoader;
    private static Long starttime;
    private static Long startFeedbacktime;
    private static final String USER_ID = "USER_ID";
    private static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    private static final String REFER_CODE = "REFER_CODE";
    private static final String REFER_DETAILS = "REFER_DETAILS";
    private static final String ANDROID_ID = "ANDROID_ID";
    private static final String USER_DUMMY_ID = "USER_DUMMY_ID";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_TYPE = "USER_TYPE";
    private static final String REPEAT = "REPEAT";
    private static final String NAME_AGE = "NAME_AGE";
    private static final String NAME_AGE2 = "NAME_AGE2";
    private static final String NAME_Gender = "NAME_Gender";
    private static final String NAME_BIRTHDAY = "NAME_BIRTHDAY";
    private static final String DATA_STRING = "DATA_STRING";
    private static final String REFERAL_TYPE = "REFERAL_TYPE";
    private static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    private static final String SELECT_TYPE = "SELECT_TYPE";
    private static final String SELECT_LANGUAGE = "SELECT_LANGUAGE";
    private static final String SELECT_QUESTION_LANGUAGE = "SELECT_QUESTION_LANGUAGE";
    private static final String SELECT_LANGUAGE_PACKAGE = "SELECT_LANGUAGE_PACKAGE";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String SELECT_LOGOUT = "SELECT_LOGOUT";
    private static final String SUBJECT_VISIBILITY = "SUBJECT_VISIBILITY";
    private static final String DIAGNOSTIC_DONE = "DIAGNOSTIC_DONE";
    private static final String SELECT_FULL_SCREEN = "SELECT_FULL_SCREEN";
    private static final String SELECT_BOARD_DUMMY = "SELECT_BOARD_DUMMY";
    private static final String SELECT_BOARD = "SELECT_BOARD";
    private static final String SELECT_BOARD_NAME = "SELECT_BOARD_NAME";
    private static final String SELECT_BOARD_NAME_DUMMY = "SELECT_BOARD_NAME_DUMMY";
    private static final String SELECT_CLASS = "SELECT_CLASS";
    private static final String SELECT_STREAM_CLASS = "SELECT_STREAM_CLASS";
    private static final String SELECT_CLASS_DUMMY = "SELECT_CLASS_DUMMY";
    private static String LAST_NET_CONNECTED = "LAST_NET_CONNECTED";
    private static String LAST_DIALOG_SEEN_NET_CONNECTED = "LAST_DIALOG_SEEN_NET_CONNECTED";
    private static String NET_CONNECTED_DIALOG = "NET_CONNECTED_DIALOG";
    private static final String LANGUAGE_JSON = "LANGUAGE_JSON";
    private static final String DATA_DOWNLOADED = "DATA_DOWNLOADED";
    private static final String CATEGORY_DOWNLOADED = "CATEGORY_DOWNLOADED";
    private static final String OFFLINE_MODE = "OFFLINE_MODE";
    private static final String LAUNCHER_MODE = "LAUNCHER_MODE";
    private static final String FACILITATOR_SYNCED = "FACILITATOR_SYNCED";
    private static final String ACTIVATION = "ACTIVATION";
    private static final String CATEGORIES_DATA_DOWNLOADED = "CATEGORIES_DATA_DOWNLOADED";
    private static final String VERIFICATION_COMPLETED = "VERIFICATION_COMPLETED";
    private static final String ALL_VALUE_SET = "ALL_VALUE_SET";
    private static final String LOGOUT_CLICKED = "LOGOUT_CLICKED";
    private static final String NGO_IMAGE = "NGO_IMAGE";
    private static final String FAC = "FAC";
    private static final String SELECT_CLASS_NAME = "SELECT_CLASS_NAME";
    private static final String SELECT_CLASS_NAME_DUMMY = "SELECT_CLASS_NAME_DUMMY";
    private static final String LEVEL_ACHIEVED = "LEVEL_ACHIEVED";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_PROFILE = "USER_Profile";
    private static final String INTERNET = "INTERNET";
    private static final String TAG = "UTILS";
    private static final String USER_PROFILE_URL = "USER_PROFILE_URL";
    private static final String TEACHER_FIRST_TIME = "TEACHER_FIRST_TIME";
    private static final String WELCOME_SCREEN = "WELCOME_SCREEN";
    private static final String TOKEN = "TOKEN";
    private static final String OPEN_SUBJECT = "OPEN_SUBJECT";
    private static Toast toast;
    private static final String USER_MOBILE = "USER_MOBILE";
    private static final String USER_ROLL = "USER_ROLL";
    private static final String WELCOME_MESSAGE = "WELCOME_MESSAGE";
    private static final String OPEN_SUBJECT_NAVIGATION = "OPEN_SUBJECT_NAVIGATION";
    private static final String OPEN_TOPICS = "OPEN_TOPICS";
    private static final String OPEN_QUESTIONS = "OPEN_QUESTIONS";
    private static final String OPEN_PROFILE = "OPEN_PROFILE";
    private static final String OPEN_SETTINGS = "OPEN_SETTINGS";
    private static final String OPEN_REPORTS = "OPEN_REPORTS";
    private static final String COUNT = "COUNT";
    private static final String VIDEO_COUNT = "VIDEO_COUNT";
    private static final String EXIT_COUNT = "EXIT_COUNT";
    private static final String RATE = "RATE";
    private static final String PERMISSION_GRANTED = "PERMISSION_GRANTED";
    private static final String TOPIC_ID = "TOPIC_ID";
    private static final String LAST_TOPIC_ID = "LAST_TOPIC_ID";
    private static final String NEXT_TOPIC_ID_POSITION = "NEXT_TOPIC_ID_POSITION";
    private static final String DEFAULT_TOPIC_COMPLETE = "DEFAULT_TOPIC_COMPLETE";
    private static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";
    private static final String TOPIC_IDS = "TOPIC_IDS";
    private static final String SUBJECT = "SUBJECT";
    private static final String SUBJECTID = "SUBJECTID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String NGO_ID = "NGO_ID";
    private static final String FOR_PAL = "FOR_PAL";
    private static final String APP_ID = "APP_ID";
    private static final String TAB_ID = "TAB_ID";
    private static final String SERVER_TIME = "SERVER_TIME";
    private static final String SD_CARD_PATH = "SD_CARD_PATH";
    private static final String TOPIC_NAME_ALT = "TOPIC_NAME_ALT";
    private static final String LEVEL = "LEVEL";
    private static final String VIDEO_LEVEL = "VIDEO_LEVEL";
    private static final String UNLOCK_VIDEO_LEVEL = "unLock_VIDEO_LEVEL";
    private static final String CATEGORY_TYPE = "CATEGORY_TYPE";
    private static final String ARRAYLIST_COUNT = "ARRAYLIST_COUNT";
    private static final String SELECT_STATE = "SELECT_STATE";
    private static final String PARENTS_CONTACT = "PARENTS_CONTACT";
    private static final String SELECT_CITY = "SELECT_CITY";
    private static int pos = 0;
    private static final String COMMON_MESSAGE = "COMMON_MESSAGE";
    private static final String TOPICS = "TOPICS";
    private static final String NGO_IMAGES = "NGO_IMAGES";
    private static final String FACILITATOR_LIST = "FACILITATOR_LIST";
    private static final String IS_EXISTING_USER = "IS_EXISTING_USER";
    private static final String SHOW_ONLY_SUBJECTS = "SHOW_ONLY_SUBJECTS";
    private static final String SHOW_INTRO_DIALOG = "SHOW_INTRO_DIALOG";
    private static final String SRN_USER = "SRN_USER";
    private static final String SHOW_TOOLTIP_HOME = "SHOW_TOOLTIP_HOME";
    private static final String SHOW_TOOLTIP_CONTENT = "SHOW_TOOLTIP_CONTENT";
    private static final String SHOW_TOOLTIP_DIAGNOSTIC = "SHOW_TOOLTIP_DIAGNOSTIC";
    private static final String Senior_TOPIC_ID = "Senior_ID";


    private static final String SCHOOL_ID = "SCHOOL_ID";
    private static final String DISTRICT = "DISTRICT";
    private static final String PROJECT_ID = "PROJECT_ID";
    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String SCHOOL_NAME = "SCHOOL_NAME";
    private static final String CONTENT_ONBOADING = "Content_onboading";
    private static final String SUBJECT_ONBOADING = "SUBJECT_onboadnng";
    private static final String HOME_ONBOADING = "HOME_onboading";
    private static final String VIDEO_Quality = "VIDEO_Quality";
    private static final String VIDEO_Speed = "VIDEO_Speed";
    private static final String DefColour = "Def Color";

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Util.context = context;
    }

    public static int getRandomNumber(int max) {
        Random r = new Random();
        int randomNumber = r.nextInt(max - 0) + 0;
        return randomNumber;
    }

    public static void showDialog(Context context) {
        try {
//            dialogLoader = new Dialog(context);
//            dialogLoader.setCancelable(false);
//            dialogLoader.setContentView(R.layout.progress);
//            dialogLoader.show();
//            dialogLoader.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showScoreDialog(Context context, String message) {
        try {
            scoreDialog = new Dialog(context);
            scoreDialog.setCancelable(false);
            scoreDialog.setContentView(R.layout.progress_score);
            TextView textViewScore = scoreDialog.findViewById(R.id.textViewScore);
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) message = "कृपया प्रतीक्षा करें, iPrep PAL ऐप आपके स्कोर की गणना कर रहा है";
            else message = "Please wait, iPrep PAL App is calculating your score";

            textViewScore.setText(message);
            scoreDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            if(Util.isPortraitMode(context)) scoreDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            scoreDialog.show();
            /* Change the background color of the dialog */
            scoreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            scoreDialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //Clear the not focusable flag from the window
            scoreDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);} catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismissScoreDialog() {
        try {
            if (scoreDialog != null) scoreDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showDataDialog(Context context, final String message) {
        final TextView textViewMessage;
        try {
            if (dataDialog == null) {
                dataDialog = new Dialog(context);
                dataDialog.setCancelable(false);
                dataDialog.setContentView(R.layout.progress_download_data);
                textViewMessage = dataDialog.findViewById(R.id.textViewMessage);
                textViewMessage.setText(message);
                dataDialog.show();
                dataDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

            } else {
                textViewMessage = dataDialog.findViewById(R.id.textViewMessage);
                textViewMessage.setText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void dismissdataDialog() {
        try {
            if (dataDialog != null) dataDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog() {
        try {
            if (dialogLoader != null) dialogLoader.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, String yes, String permissionN, String permissionNMessage) {
        try {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_permission);
                        TextView okayButton = dialog.findViewById(R.id.okayButton);
                        TextView textTitle = dialog.findViewById(R.id.textTitle);
                        TextView textMessage = dialog.findViewById(R.id.textMessage);
                        okayButton.setText(yes);
                        textTitle.setText(permissionN);
                        textMessage.setText(permissionNMessage);
                        okayButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                try {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getColor(android.R.color.transparent)));
                    } else {
                        try {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Long saveTime() {
        starttime = System.currentTimeMillis();
        return starttime;
    }

    public static Long saveFeedbackTime() {
        startFeedbacktime = System.currentTimeMillis();
        return startFeedbacktime;
    }

    public static Long getDifference() {
        Long endtime = System.currentTimeMillis();
        Long differenz = ((endtime - starttime) / 1000);
        return differenz;
    }

    public static void setDistrict(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DISTRICT, MODE_PRIVATE).edit();
        editor.putString(DISTRICT, userId);
        editor.commit();

    }

    public static String getDistrict(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DISTRICT, MODE_PRIVATE);
        String language = prefs.getString(DISTRICT, null);
        return language;
    }

    public static void setSchoolId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SCHOOL_ID, MODE_PRIVATE).edit();
        editor.putString(SCHOOL_ID, userId);
        editor.commit();

    }

    public static String getSchoolId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SCHOOL_ID, MODE_PRIVATE);
        String language = prefs.getString(SCHOOL_ID, null);
        return language;
    }

    public static void setProjectId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE).edit();
        editor.putString(PROJECT_ID, userId);
        editor.commit();

    }

    public static String getProjectId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE);
        String language = prefs.getString(PROJECT_ID, null);
        return language;
    }

    public static void setProjectName(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE).edit();
        editor.putString(PROJECT_NAME, userId);
        editor.commit();

    }

    public static String getProjectName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE);
        String language = prefs.getString(PROJECT_NAME, null);
        return language;
    }

    public static void setSchoolName(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SCHOOL_NAME, MODE_PRIVATE).edit();
        editor.putString(SCHOOL_NAME, userId);
        editor.commit();

    }

    public static String getSchoolName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SCHOOL_NAME, MODE_PRIVATE);
        String language = prefs.getString(SCHOOL_NAME, null);
        return language;
    }

    public static Long getFeedbackDifference() {
        Long endtime = System.currentTimeMillis();
        Long differenz = ((endtime - startFeedbacktime) / 1000);
        return differenz;
    }

    public static void setLanguageSelection(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_LANGUAGE, MODE_PRIVATE).edit();
        editor.putString(SELECT_LANGUAGE, language);
        editor.commit();

    }

    public static String getSelectedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_LANGUAGE, MODE_PRIVATE);
        String userType = prefs.getString(SELECT_LANGUAGE, "english");
        return userType;

    }

    public static void setUserType(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_TYPE, MODE_PRIVATE).edit();
        editor.putString(USER_TYPE, language);
        editor.commit();

    }

    public static String getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_TYPE, MODE_PRIVATE);
        String userType = prefs.getString(USER_TYPE, null);
        return userType;

    }

    public static void setQuestionLanguageSelection(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_QUESTION_LANGUAGE, MODE_PRIVATE).edit();
        editor.putString(SELECT_QUESTION_LANGUAGE, language);
        editor.commit();

    }

    public static String getSelectedLanguageQuestion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_QUESTION_LANGUAGE, MODE_PRIVATE);
        String language = prefs.getString(SELECT_QUESTION_LANGUAGE, null);
        return language;

    }

    public static String getTimeString(long time) {
        long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;

//        return (String.format("%02d:%02d:%02d", hour, minute, second));
        return (String.format("%02d:%02d", minute, second));
    }

    public static String getTimeString_(long time) {
        int hour, minute, second;
        second = (int) time % 60;
        minute = (int) (time / 60) % 60;
        hour = (int) time / (60 * 60);

        return (String.format(Locale.US, "%02d:%02d:%02d", hour, minute, second));
    }


    public static void setLoginUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LOGIN_USER_ID, MODE_PRIVATE).edit();
        editor.putString(LOGIN_USER_ID, userId);
        editor.commit();

    }

    public static String getLoginUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_USER_ID, MODE_PRIVATE);
        String language = prefs.getString(LOGIN_USER_ID, getUserId(context));
        return language;
    }





    public static void setUserId(Context context, String userId) {

        SharedPreferences.Editor editor = context.getSharedPreferences(USER_ID, MODE_PRIVATE).edit();
        if(userId==null ) editor.putString(USER_ID, userId);
        else editor.putString(USER_ID, userId.replace(" ","_").toLowerCase());
        editor.commit();

    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_ID, MODE_PRIVATE);
        String language = prefs.getString(USER_ID, null);
        if (language==null) return language;
        else return language.toLowerCase();
    }

    public static void setUserMobile(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_MOBILE, MODE_PRIVATE).edit();
        editor.putString(USER_MOBILE, userId);
        editor.commit();

    }

    public static String getUserMobile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_MOBILE, MODE_PRIVATE);
        String language = prefs.getString(USER_MOBILE, "");
        return language;

    }

    public static void setUserRoll(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_ROLL, MODE_PRIVATE).edit();
        editor.putString(USER_ROLL, userId);
        editor.commit();

    }

    public static String getUserRoll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_ROLL, MODE_PRIVATE);
        String language = prefs.getString(USER_ROLL, "");
        return language;

    }

    public static void setWelcomeMessage(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(WELCOME_MESSAGE, MODE_PRIVATE).edit();
        editor.putString(WELCOME_MESSAGE, userId);
        editor.commit();

    }

    public static String getWelcomeMessage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(WELCOME_MESSAGE, MODE_PRIVATE);
        String language = prefs.getString(WELCOME_MESSAGE, null);
        return language;

    }

    public static void setAndroidId(Context context, String androidId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ANDROID_ID, MODE_PRIVATE).edit();
        editor.putString(ANDROID_ID, androidId);
        editor.commit();

    }

    public static String getAndroidId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ANDROID_ID, MODE_PRIVATE);
        String androidId = prefs.getString(ANDROID_ID, "");
        return androidId;

    }

    public static void setDummyUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_DUMMY_ID, MODE_PRIVATE).edit();
        editor.putString(USER_DUMMY_ID, userId);
        editor.commit();

    }

    public static String getDummyUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_DUMMY_ID, MODE_PRIVATE);
        String language = prefs.getString(USER_DUMMY_ID, null);
        return language;

    }

    public static void setUserProfileUrl(Context context, String userPrfileUrl) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PROFILE_URL, MODE_PRIVATE).edit();
        editor.putString(USER_PROFILE_URL, userPrfileUrl);
        editor.commit();

    }

    public static String getUserProfileUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PROFILE_URL, MODE_PRIVATE);
        String language = prefs.getString(USER_PROFILE_URL, null);
        return language;

    }

    public static void setBoardSelectionDummy(Context context, String board) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_BOARD_DUMMY, MODE_PRIVATE).edit();
        editor.putString(SELECT_BOARD_DUMMY, board);
        editor.commit();

    }

    public static void setBoardSelection(Context context, String board) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_BOARD, MODE_PRIVATE).edit();
        editor.putString(SELECT_BOARD, board);
        editor.commit();

    }

    public static String getSelectedBoard(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_BOARD, MODE_PRIVATE);
        String language = prefs.getString(SELECT_BOARD, "cbse");
        return language;

    }

    public static String getSelectedBoardDummy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_BOARD_DUMMY, MODE_PRIVATE);
        String language = prefs.getString(SELECT_BOARD_DUMMY, null);
        return language;

    }

    public static void setLanguagePackageSelection(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_LANGUAGE_PACKAGE, MODE_PRIVATE).edit();
        editor.putString(SELECT_LANGUAGE_PACKAGE, language);
        editor.commit();

    }

    public static String getSelectedLanguagePackage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_LANGUAGE_PACKAGE, MODE_PRIVATE);
        String language = prefs.getString(SELECT_LANGUAGE_PACKAGE, null);
        return language;

    }


    public static void setIsFirstTime(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(IS_FIRST_TIME, MODE_PRIVATE).edit();
        editor.putString(IS_FIRST_TIME, language);
        editor.commit();

    }

    public static String getIsFirstTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(IS_FIRST_TIME, MODE_PRIVATE);
        String language = prefs.getString(IS_FIRST_TIME, "false");
        return language;

    }


    public static void setLogoutSelection(Context context, Boolean logout) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_LOGOUT, MODE_PRIVATE).edit();
        editor.putBoolean(SELECT_LOGOUT, logout);
        editor.commit();

    }

    public static boolean getLogoutSelection(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_LOGOUT, MODE_PRIVATE);
        boolean language = prefs.getBoolean(SELECT_LOGOUT, false);
        return language;

    }


    public static void setSubjectVisibility(Context context, Boolean subject_visibility) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SUBJECT_VISIBILITY, MODE_PRIVATE).edit();
        editor.putBoolean(SUBJECT_VISIBILITY, subject_visibility);
        editor.commit();

    }

    public static boolean getSubjectVisibility(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SUBJECT_VISIBILITY, MODE_PRIVATE);
        boolean subject_visibility = prefs.getBoolean(SUBJECT_VISIBILITY, true);
        return subject_visibility;
    }


    public static void setDiagnosticDone(Context context, Boolean diagnosticDone) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DIAGNOSTIC_DONE, MODE_PRIVATE).edit();
        editor.putBoolean(DIAGNOSTIC_DONE, diagnosticDone);
        editor.commit();
    }

    public static boolean isDiagnosticDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DIAGNOSTIC_DONE, MODE_PRIVATE);
        boolean language = prefs.getBoolean(DIAGNOSTIC_DONE, false);
        return language;

    }


    public static void setEndDate(Context context, long StartDate) {
        SharedPreferences.Editor editor = context.getSharedPreferences("END_DATE", MODE_PRIVATE).edit();
        editor.putLong("END_DATE", StartDate);
        editor.commit();

    }

    public static Long getEndDate(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("END_DATE", MODE_PRIVATE);
        Long START_DATE_ = prefs.getLong("END_DATE", 0);
        return START_DATE_;

    }

    public static void setIsFullScreen(Context context, Boolean fullscreen) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_FULL_SCREEN, MODE_PRIVATE).edit();
        editor.putBoolean(SELECT_FULL_SCREEN, fullscreen);
        editor.commit();

    }

    public static boolean getIsFullScreen(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_FULL_SCREEN, MODE_PRIVATE);
        boolean fullscreen = prefs.getBoolean(SELECT_FULL_SCREEN, false);
        return fullscreen;

    }


    public static void setBoardNameSelection(Context context, String board) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_BOARD_NAME, MODE_PRIVATE).edit();
        editor.putString(SELECT_BOARD_NAME, board);
        editor.commit();

    }

    public static String getSelectedBoardName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_BOARD_NAME, MODE_PRIVATE);
        String language = prefs.getString(SELECT_BOARD_NAME, null);
        return language;

    }

    public static void setBoardNameSelectionDummy(Context context, String board) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_BOARD_NAME_DUMMY, MODE_PRIVATE).edit();
        editor.putString(SELECT_BOARD_NAME_DUMMY, board);
        editor.commit();

    }

    public static String getSelectedBoardNameDummy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_BOARD_NAME_DUMMY, MODE_PRIVATE);
        String language = prefs.getString(SELECT_BOARD_NAME_DUMMY, null);
        return language;

    }

    public static void setClassSelection(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_CLASS, MODE_PRIVATE).edit();
        editor.putString(SELECT_CLASS, sClass);
        editor.commit();

    }

    public static String getSelectedClass(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_CLASS, MODE_PRIVATE);
        String language = prefs.getString(SELECT_CLASS, null);
        return language;

    }


    public static void setStreamClassSelection(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_STREAM_CLASS, MODE_PRIVATE).edit();
        editor.putString(SELECT_STREAM_CLASS, sClass);
        editor.commit();

    }

    public static String getStreamSelectedClass(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_STREAM_CLASS, MODE_PRIVATE);
        String language = prefs.getString(SELECT_STREAM_CLASS, null);
        return language;

    }

    public static void setClassSelectionDummy(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_CLASS_DUMMY, MODE_PRIVATE).edit();
        editor.putString(SELECT_CLASS_DUMMY, sClass);
        editor.commit();

    }

    public static String getSelectedClassDummy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_CLASS_DUMMY, MODE_PRIVATE);
        String language = prefs.getString(SELECT_CLASS_DUMMY, null);
        return language;

    }

    public static void setClassNameSelection(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_CLASS_NAME, MODE_PRIVATE).edit();
        editor.putString(SELECT_CLASS_NAME, sClass);
        editor.commit();

    }

    public static void setClassNameSelectionDummy(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_CLASS_NAME_DUMMY, MODE_PRIVATE).edit();
        editor.putString(SELECT_CLASS_NAME_DUMMY, sClass);
        editor.commit();

    }

    public static String getLanguageJson(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_JSON, MODE_PRIVATE);
        String language = prefs.getString(LANGUAGE_JSON, null);
        return language;

    }

    public static void setLanguageJson(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LANGUAGE_JSON, MODE_PRIVATE).edit();
        editor.putString(LANGUAGE_JSON, sClass);
        editor.commit();

    }

    public static String getTeacherFirstTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TEACHER_FIRST_TIME, MODE_PRIVATE);
        String language = prefs.getString(TEACHER_FIRST_TIME, "false");
        return language;

    }

    public static void setTeacherFirstTime(Context context, String sClass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TEACHER_FIRST_TIME, MODE_PRIVATE).edit();
        editor.putString(TEACHER_FIRST_TIME, sClass);
        editor.commit();

    }

    public static boolean isDataDownloaded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_DOWNLOADED, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(DATA_DOWNLOADED, false);
        return isDataDownloaded;

    }

    /** Method returns the Timestamp in Long for User Last Connected to Internet**/
    public static long getLastNetConnected(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LAST_NET_CONNECTED, MODE_PRIVATE);
        return sharedPreferences.getLong("lastNetConnected", 0);
    }

    /** Method sets the Timestamp in Long for User Last Connected to Internet**/
    public static void setLastNetConnected(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LAST_NET_CONNECTED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long lastLoginTimestamp = System.currentTimeMillis();
        editor.putLong("lastNetConnected", lastLoginTimestamp);
        editor.apply();
    }

    /** Method returns the Timestamp in Long for User Seen last Last Ineternet Connected Dialog**/
    public static long getLastSeenNetDialog(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LAST_DIALOG_SEEN_NET_CONNECTED, MODE_PRIVATE);
        return sharedPreferences.getLong("lastPopupTimestamp", 0);
    }

    /** Method sets the Timestamp in Long for User Last Connected to Internet**/
    public static void setLastSeenNetDialog(Context context, long currentTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LAST_DIALOG_SEEN_NET_CONNECTED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastPopupTimestamp", currentTime);
        editor.apply();;
    }

    /** Method to set if user seen dialog same day of Last Connected to Internet **/
    public static void setNetConnectDialogStatus(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NET_CONNECTED_DIALOG, MODE_PRIVATE).edit();
        editor.putInt(NET_CONNECTED_DIALOG, value);
        editor.commit();
    }

    /** Method to check if user seen dialog same day of Last Connected to Internet... where 0 means no and 1 means yes**/
    public static Integer getNetConnectDialogStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NET_CONNECTED_DIALOG, MODE_PRIVATE);
        Integer userType = prefs.getInt(NET_CONNECTED_DIALOG, 0);
        return userType;

    }

    public static void setDataDownloaded(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DATA_DOWNLOADED, MODE_PRIVATE).edit();
        editor.putBoolean(DATA_DOWNLOADED, data);
        editor.commit();

    }

    public static String getSelectedClassName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_CLASS_NAME, MODE_PRIVATE);
        String language = prefs.getString(SELECT_CLASS_NAME, null);
        return language;

    }

    public static String getSelectedClassNameDummy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_CLASS_NAME_DUMMY, MODE_PRIVATE);
        String language = prefs.getString(SELECT_CLASS_NAME_DUMMY, null);
        return language;

    }

    public static void setRepeatedValue(Context context, boolean isReapted) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REPEAT, MODE_PRIVATE).edit();
        editor.putBoolean(REPEAT, isReapted);
        editor.commit();

    }

    public static boolean getRepeatedValue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(REPEAT, MODE_PRIVATE);
        boolean isRepeated = prefs.getBoolean(REPEAT, true);
        return isRepeated;

    }

    public static void setUsername(Context context, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_NAME, MODE_PRIVATE);
        String name = prefs.getString(USER_NAME, null);
        String output;
        if (name!=null) output = name.substring(0, 1).toUpperCase() + name.substring(1);
        else output=name;
        return output;

    }

    public static String getUsernameShowable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_NAME, MODE_PRIVATE);
        String name = prefs.getString(USER_NAME, null);
        String output;
        if(name!=null) {
            output = name.substring(0, 1).toUpperCase() + name.substring(1);
            output=output.replace("_"," ");
        } else output=name;
        return output;

    }


    public static void setUserProfile(Context context, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PROFILE, MODE_PRIVATE).edit();
        editor.putString(USER_PROFILE, name);
        editor.commit();
    }

    public static String getUserProfile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PROFILE, MODE_PRIVATE);
        String name = prefs.getString(USER_PROFILE,  null);
        return name;

    }


    public static void setInternetMessage(Context context, String internet) {
        SharedPreferences.Editor editor = context.getSharedPreferences(INTERNET, MODE_PRIVATE).edit();
        editor.putString(INTERNET, internet);
        editor.commit();
    }

    public static String getInternetMessage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(INTERNET, MODE_PRIVATE);
        String internet = prefs.getString(INTERNET, null);
        return internet;

    }

    private static String removeUnwantedSpecialCharacter(String date) {
        String result = date.replaceAll("[^a-zA-Z0-9: ]", "").toUpperCase();
        return result;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss a");
        String date = df.format(c.getTime());
        String resultant = removeUnwantedSpecialCharacter(date);
        return resultant;
    }

    public static String  getCurrentDate(Context context) {

        if(isTimeAutomatic(context)) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss a");
            String date = df.format(c.getTime());
            String resultant = removeUnwantedSpecialCharacter(date);
            return resultant;

        }else {
            Timestamp timestamp = new Timestamp(getCurrentTimestamp(context));
            Date date = new Date(timestamp.getTime());

            // S is the millisecond
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss a");

            System.out.println(simpleDateFormat.format(timestamp));
            System.out.println(simpleDateFormat.format(date));

            String date2 =simpleDateFormat.format(date);
            String resultant = removeUnwantedSpecialCharacter(date2);
            return resultant;
        }
    }

    public static boolean isTimeAutomatic(Context c) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
            else return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }catch (Exception r) {
            return false;
        }
    }

    public static void enableScroll(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setMovementMethod(new ScrollingMovementMethod());
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    public static String getCurrentDateWithDifferentFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MM yy");
        String date = df.format(c.getTime());
        String resultant;
        String manipulatedString = null;

        switch (date.split(" ")[1]) {
            case "01":
                manipulatedString = "Jan";
                break;
            case "02":
                manipulatedString = "Feb";
                break;
            case "03":
                manipulatedString = "Mar";
                break;
            case "04":
                manipulatedString = "Apr";
                break;
            case "05":
                manipulatedString = "May";
                break;
            case "06":
                manipulatedString = "Jun";
                break;
            case "07":
                manipulatedString = "Jul";
                break;
            case "08":
                manipulatedString = "Aug";
                break;
            case "09":
                manipulatedString = "Sept";
                break;
            case "10":
                manipulatedString = "Oct";
                break;
            case "11":
                manipulatedString = "Nov";
                break;
            case "12":
                manipulatedString = "Dec";
                break;

        }
        resultant = date.split(" ")[0] + " " + manipulatedString + " " + date.split(" ")[2];
        return resultant;
    }

    public static void setNgoImage(Context context, String image) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NGO_IMAGE, MODE_PRIVATE).edit();
        editor.putString(NGO_IMAGE, image);
        editor.commit();
    }

    public static String getNgoImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NGO_IMAGE, MODE_PRIVATE);
        String image = prefs.getString(NGO_IMAGE, null);
        return image;
    }

    public static boolean isAllvalueSetted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ALL_VALUE_SET, MODE_PRIVATE);
        boolean language = prefs.getBoolean(ALL_VALUE_SET, false);
        return language;

    }

    public static void setLogoutClicked(Context context, boolean isClickedLogout) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LOGOUT_CLICKED, MODE_PRIVATE).edit();
        editor.putBoolean(LOGOUT_CLICKED, isClickedLogout);
        editor.commit();

    }


    public static boolean isClickedOnLogout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGOUT_CLICKED, MODE_PRIVATE);
        boolean language = prefs.getBoolean(LOGOUT_CLICKED, false);
        return language;

    }


    public static void setAllsetValue(Context context, boolean isAllSet) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ALL_VALUE_SET, MODE_PRIVATE).edit();
        editor.putBoolean(ALL_VALUE_SET, isAllSet);
        editor.commit();

    }


    public static boolean isFac(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FAC, MODE_PRIVATE);
        boolean language = prefs.getBoolean(FAC, false);
        return language;
    }

    public static void setFac(Context context, boolean fac) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FAC, MODE_PRIVATE).edit();
        editor.putBoolean(FAC, fac);
        editor.commit();
    }

    public static void shareApp(Context context, String link) {
        String message = "\nDownload the iDream Learning App to enjoy learning Math, EVS, Science, Social Sciences, English Grammar, Commerce & Computers for 1st to 12th\n" +
                " \n" +
                "Watch animated video lessons, make projects, read digital books and master topics with questions & feedback.\n" +
                "\n" +
                "Available in English, Hindi  & other local languages.\n" +
                "\n" +
                "Try it now. \n" +
                "Keep Learning.  Keep Sharing.";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_SUBJECT, "iDream");
        i.putExtra(Intent.EXTRA_TEXT, link + message);
        i.setType("text/plain");
        context.startActivity(Intent.createChooser(i, "Share via"));
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static boolean checkInternetConnection(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    URL url = new URL("https://google.com");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(10000);
//                    connection.connect();
//                    success = connection.getResponseCode() == 200;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Util.dismissDialog();
//            }
//        }.execute();
//        return success;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showInternetConnectioError(final Context context) {
        try {
            if (dialog != null) {
                //   dialog.dismiss();
                dialog = null;
            }
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_internetconnection_error);
            dialog.setCancelable(false);
            TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
            TextView text = dialog.findViewById(R.id.text);
            if (count == 2) {
                try {
                    text.setText(Util.getCommonMessages(context).get(2));
                    textViewRetry.setText(Util.getCommonMessages(context).get(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    textViewRetry.setText(getInternetMessage(context).split(":")[1]);
                    text.setText(getInternetMessage(context).split(":")[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            textViewRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (count == 2) {
                        count = 0;

                        if (context instanceof NotificationActivity) {
                            ((NotificationActivity) context).finish();
                        }

                        if (context instanceof SelectTopicActivity) {
                            ((SelectTopicActivity) context).finish();
                        }


                        return;
                    }
                    count++;
                    if (context instanceof NotificationActivity) {
                        try {
                            //    NotificationActivity.activity.getNotifications();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (context instanceof SelectTopicActivity) {
                        try {
                            SelectTopicActivity.activity.setStaticText();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unsubscribeAllTopic(final Context context) {

    }

    public static void subscribeAllTopic(final Context context) {

    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showSuccessDialog(Context context, String meesage, String okay) {
        try {





            final Dialog dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_update);
            TextView text = dialog.findViewById(R.id.text);
            TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);
            textViewUpdate.setText(okay);
            text.setText(meesage);
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showToast(final Context context, final String message) {
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//        if (!ApplicationConstants.IS_SNACK_BAR_RUNNING) {
//            TopSnackBarMessage topSnackBarMessage = new TopSnackBarMessage((Activity) context);
//            topSnackBarMessage.showSuccessMessage(message);
        //        }
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static String getTimeAmPm() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }

    public static void setWelcomeScreen(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(WELCOME_SCREEN, MODE_PRIVATE).edit();
        editor.putString(WELCOME_SCREEN, language);
        editor.commit();
    }

    public static String getWelcomeScreen(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(WELCOME_SCREEN, MODE_PRIVATE);
        String userType = prefs.getString(WELCOME_SCREEN, "NO");
        return userType;
    }

    public static void setTypeSelectio(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_TYPE, MODE_PRIVATE).edit();
        editor.putString(SELECT_TYPE, language);
        editor.commit();
    }

    public static String getSelectedType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_TYPE, MODE_PRIVATE);
        String userType = prefs.getString(SELECT_TYPE, null);
        return userType;

    }

    public static void setNameAge(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_AGE, MODE_PRIVATE).edit();
        editor.putString(NAME_AGE, language);
        editor.commit();

    }

    public static String getNameAge(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_AGE, MODE_PRIVATE);
        String userType = prefs.getString(NAME_AGE, null);
        return userType;
    }

    public static void setAge(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_AGE2, MODE_PRIVATE).edit();
        editor.putString(NAME_AGE2, language);
        editor.commit();

    }

    public static String getAge(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_AGE2, MODE_PRIVATE);
        String userType = prefs.getString(NAME_AGE2, null);
        return userType;
    }

    public static void setGender(Context context,String userID, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_Gender+userID, MODE_PRIVATE).edit();
        editor.putString(NAME_Gender+userID, language);
        editor.commit();

    }

    public static String getGender(Context context,String userID) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_Gender+userID, MODE_PRIVATE);
        String userType = prefs.getString(NAME_Gender+userID, null);
        return userType;
    }

    public static void setBirthDate(Context context,String userID, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_BIRTHDAY+userID, MODE_PRIVATE).edit();
        editor.putString(NAME_BIRTHDAY+userID, language);
        editor.commit();

    }

    public static String getBirthDate(Context context,String userID) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_BIRTHDAY+userID, MODE_PRIVATE);
        String userType = prefs.getString(NAME_BIRTHDAY+userID, null);
        return userType;
    }

    public static void setDataString(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DATA_STRING, MODE_PRIVATE).edit();
        editor.putString(DATA_STRING, language);
        editor.commit();

    }

    public static String getDataString(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DATA_STRING, MODE_PRIVATE);
        String userType = prefs.getString(DATA_STRING, null);
        return userType;
    }


    public static void setNotificationType(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NOTIFICATION_TYPE, MODE_PRIVATE).edit();
        editor.putString(NOTIFICATION_TYPE, language);
        editor.commit();

    }

    public static String getNotificationType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NOTIFICATION_TYPE, MODE_PRIVATE);
        String userType = prefs.getString(NOTIFICATION_TYPE, null);
        return userType;
    }

    public static void setReferalType(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REFERAL_TYPE, MODE_PRIVATE).edit();
        editor.putString(REFERAL_TYPE, language);
        editor.commit();

    }

    public static String getReferalType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(REFERAL_TYPE, MODE_PRIVATE);
        String userType = prefs.getString(REFERAL_TYPE, null);
        return userType;
    }

    public static String capitalize(String capString) {
        String upperString = capString.substring(0, 1).toUpperCase() + capString.substring(1);

//        StringBuffer capBuffer = new StringBuffer();
//        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
//        while (capMatcher.find()) {
//            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
//        }
//        return capMatcher.appendTail(capBuffer).toString();
        return upperString;
    }

    public static void setToken(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TOKEN, MODE_PRIVATE).edit();
        editor.putString(TOKEN, language);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TOKEN, MODE_PRIVATE);
        String userType = prefs.getString(TOKEN, "NO");
        return userType;
    }

    public static void setUserEmail(Context context, String language) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_EMAIL, MODE_PRIVATE).edit();
        editor.putString(USER_EMAIL, language);
        editor.commit();

    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_EMAIL, MODE_PRIVATE);
        String userType = prefs.getString(USER_EMAIL, null);
        return userType;

    }

    public static void setSubjectTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_SUBJECT, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_SUBJECT, isOpened);
        editor.commit();

    }

    public static boolean isSubjectOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_SUBJECT, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_SUBJECT, false);
        return isOpened;

    }

    public static void setSubjectNavigationTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_SUBJECT_NAVIGATION, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_SUBJECT_NAVIGATION, isOpened);
        editor.commit();

    }

    public static boolean isSubjectNavigationOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_SUBJECT_NAVIGATION, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_SUBJECT_NAVIGATION, false);
        return isOpened;

    }

    public static void setTopicsTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_TOPICS, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_TOPICS, isOpened);
        editor.commit();

    }

    public static boolean isTopicsOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_TOPICS, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_TOPICS, false);
        return isOpened;

    }

    public static void setQuestionsTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_QUESTIONS, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_QUESTIONS, isOpened);
        editor.commit();
    }

    public static boolean isQuestionsOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_QUESTIONS, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_QUESTIONS, false);
        return isOpened;
    }

    public static void setSettingsTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_SETTINGS, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_SETTINGS, isOpened);
        editor.commit();
    }

    public static boolean isSettingsOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_SETTINGS, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_SETTINGS, false);
        return isOpened;
    }

    public static void setReportTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_REPORTS, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_REPORTS, isOpened);
        editor.commit();
    }

    public static boolean isReportOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_REPORTS, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_REPORTS, false);
        return isOpened;
    }

    public static void setPofileTautorial(Context context, boolean isOpened) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OPEN_PROFILE, MODE_PRIVATE).edit();
        editor.putBoolean(OPEN_PROFILE, isOpened);
        editor.commit();
    }

    public static boolean isProfileOpened(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OPEN_PROFILE, MODE_PRIVATE);
        boolean isOpened = prefs.getBoolean(OPEN_PROFILE, false);
        return isOpened;
    }

    public static void setBackButton(final Context context) throws Exception {
        final Activity activity = (Activity) context;
        ImageView imageViewBack = activity.findViewById(R.id.imageViewBack);
        imageViewBack.clearFocus();
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

    }

    public static int getNotificationCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(COUNT, MODE_PRIVATE);
        int count = prefs.getInt(COUNT, 0);
        return count;

    }

    public static void setNotificationCount(Context context, int count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(COUNT, MODE_PRIVATE).edit();
        editor.putInt(COUNT, count);
        editor.commit();

    }

    public static void timeOut(Context context, DatabaseReference reference, ValueEventListener valueEventListener) {
        dialog.dismiss();
        reference.removeEventListener(valueEventListener);
        Util.showInternetConnectioError(context);

    }

    public static void setReferCode(Context context, String referCode) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REFER_CODE, MODE_PRIVATE).edit();
        editor.putString(REFER_CODE, referCode);
        editor.commit();

    }

    public static String getReferCode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(REFER_CODE, MODE_PRIVATE);
        String language = prefs.getString(REFER_CODE, null);
        return language;

    }

    public static void setReferalDetails(Context context, String referCode) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REFER_DETAILS, MODE_PRIVATE).edit();
        editor.putString(REFER_DETAILS, referCode);
        editor.commit();

    }

    public static String getReferalDetails(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(REFER_DETAILS, MODE_PRIVATE);
        String language = prefs.getString(REFER_DETAILS, null);
        return language;

    }

    public static void setExitCount(Context context, int count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(EXIT_COUNT, MODE_PRIVATE).edit();
        editor.putInt(EXIT_COUNT, count);
        editor.commit();

    }

    public static int getExitCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(EXIT_COUNT, MODE_PRIVATE);
        int count = prefs.getInt(EXIT_COUNT, 0);
        return count;

    }

    public static void setRateDone(Context context, boolean rate) {
        SharedPreferences.Editor editor = context.getSharedPreferences(RATE, MODE_PRIVATE).edit();
        editor.putBoolean(RATE, rate);
        editor.commit();

    }

    public static boolean getRateDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(RATE, MODE_PRIVATE);
        boolean rate = prefs.getBoolean(RATE, false);
        return rate;

    }

    public static void setPermissonGranted(Context context, boolean rate) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PERMISSION_GRANTED, MODE_PRIVATE).edit();
        editor.putBoolean(PERMISSION_GRANTED, rate);
        editor.commit();

    }

    public static boolean isPermissionGranted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PERMISSION_GRANTED, MODE_PRIVATE);
        boolean rate = prefs.getBoolean(PERMISSION_GRANTED, false);
        return rate;

    }

    public static void setTopicID(Context context, String topicID) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TOPIC_ID, MODE_PRIVATE).edit();
        editor.putString(TOPIC_ID, topicID);
        editor.commit();

    }

    public static String getTopicID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TOPIC_ID, MODE_PRIVATE);
        String topicID = prefs.getString(TOPIC_ID, null);
        return topicID;

    }

    public static void setNotifCount(Context context, int count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NOTIFICATION_COUNT, MODE_PRIVATE).edit();
        editor.putInt(NOTIFICATION_COUNT, count);
        editor.commit();
    }

    public static int getNotifCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NOTIFICATION_COUNT, MODE_PRIVATE);
        int count = prefs.getInt(NOTIFICATION_COUNT, 0);
        return count;
    }

    public static void setSubject(Context context, String subject) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SUBJECT, MODE_PRIVATE).edit();
        editor.putString(SUBJECT, subject);
        editor.commit();

    }

    public static String getSubject(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SUBJECT, MODE_PRIVATE);
        String topicID = prefs.getString(SUBJECT, null);
        return topicID;

    }

    public static void setSubjectId(Context context, String subject) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SUBJECTID, MODE_PRIVATE).edit();
        editor.putString(SUBJECTID, subject);
        editor.commit();

    }

    public static String getSubjectId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SUBJECTID, MODE_PRIVATE);
        String topicID = prefs.getString(SUBJECTID, null);
        return topicID;

    }

    public static void setSubjectName(Context context, String subjectName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SUBJECT_NAME, MODE_PRIVATE).edit();
        editor.putString(SUBJECT_NAME, subjectName);
        editor.commit();

    }

    public static String getSubjectName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SUBJECT_NAME, MODE_PRIVATE);
        String subjectName = prefs.getString(SUBJECT_NAME, null);
        return subjectName;

    }

    public static void setNGOID(Context context, String NGO) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NGO_ID, MODE_PRIVATE).edit();
        editor.putString(NGO_ID, NGO);
        editor.commit();

    }

    public static String getNGOID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NGO_ID, MODE_PRIVATE);
        String NGO_ID_ = prefs.getString(NGO_ID, getProjectID());
        return NGO_ID_;

    }

    public static void setAPPID(Context context, String NGO) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, MODE_PRIVATE).edit();
        editor.putString(APP_ID, NGO);
        editor.commit();

    }

    public static String getAPPID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
        String APP_ID_ = prefs.getString(APP_ID, null);
        return APP_ID_;

    }

    public static void setTABID(Context context, String TAB) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, MODE_PRIVATE).edit();
        editor.putString(TAB_ID, TAB);
        editor.commit();

    }

    public static String getTABID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
        String APP_ID_ = prefs.getString(TAB_ID, null);
        return APP_ID_;

    }

    public static void setSDCardPath(Context context, String path) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SD_CARD_PATH, MODE_PRIVATE).edit();
        editor.putString(SD_CARD_PATH, path);
        editor.commit();

    }

    public static String getSDCardPath(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SD_CARD_PATH, MODE_PRIVATE);
        String path = prefs.getString(SD_CARD_PATH, null);
        return path;

    }

    public static void setTopicNameAlt(Context context, String topicName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TOPIC_NAME_ALT, MODE_PRIVATE).edit();
        editor.putString(TOPIC_NAME_ALT, topicName);
        editor.commit();

    }

    public static String getTopicNameAlt(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TOPIC_NAME_ALT, MODE_PRIVATE);
        String topicNameAlt = prefs.getString(TOPIC_NAME_ALT, null);
        return topicNameAlt;

    }


    public static void setLevel(Context context, int level) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LEVEL, MODE_PRIVATE).edit();
        editor.putInt(LEVEL, level);
        editor.commit();

    }

    public static int getLevel(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LEVEL, MODE_PRIVATE);
        int level = prefs.getInt(LEVEL, 1);
        return level;

    }

    public static boolean isCategoriesDataDownloaded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CATEGORIES_DATA_DOWNLOADED, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(CATEGORIES_DATA_DOWNLOADED, false);
        return isDataDownloaded;

    }

    public static void setCategoriesDataDownloaded(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CATEGORIES_DATA_DOWNLOADED, MODE_PRIVATE).edit();
        editor.putBoolean(CATEGORIES_DATA_DOWNLOADED, data);
        editor.commit();
    }

    public static boolean isVerificationCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(VERIFICATION_COMPLETED, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(VERIFICATION_COMPLETED, false);
        return isDataDownloaded;

    }

    public static void setVerificationCompleted(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VERIFICATION_COMPLETED, MODE_PRIVATE).edit();
        editor.putBoolean(VERIFICATION_COMPLETED, data);
        editor.commit();
    }

    public static int getArrayListCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ARRAYLIST_COUNT, MODE_PRIVATE);
        int count = prefs.getInt(ARRAYLIST_COUNT, 0);
        return count;

    }

    public static void setArrayListCount(Context context, int count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ARRAYLIST_COUNT, MODE_PRIVATE).edit();
        editor.putInt(ARRAYLIST_COUNT, count);
        editor.commit();

    }

    public static void setExistingUser(Context context, boolean exist) {
        SharedPreferences.Editor editor = context.getSharedPreferences(IS_EXISTING_USER, MODE_PRIVATE).edit();
        editor.putBoolean(IS_EXISTING_USER, exist);
        editor.commit();
    }

    public static boolean isExistingUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(IS_EXISTING_USER, MODE_PRIVATE);
        boolean exist = prefs.getBoolean(IS_EXISTING_USER, false);
        return exist;
    }

    public static void setShowOnlySubjects(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHOW_ONLY_SUBJECTS, MODE_PRIVATE).edit();
        editor.putBoolean(SHOW_ONLY_SUBJECTS, show);
        editor.commit();
    }

    public static boolean isOnlySubjectsToBeShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHOW_ONLY_SUBJECTS, MODE_PRIVATE);
        boolean show = prefs.getBoolean(SHOW_ONLY_SUBJECTS, true);
        return show;
    }

    public static void setIntroDialog(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHOW_INTRO_DIALOG, MODE_PRIVATE).edit();
        editor.putBoolean(SHOW_INTRO_DIALOG, show);
        editor.commit();
    }

    public static boolean isIntroDialogToBeShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHOW_INTRO_DIALOG, MODE_PRIVATE);
//        boolean show = prefs.getBoolean(SHOW_INTRO_DIALOG, true);
        return false;
    }

    public static void setSRNUser(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SRN_USER, MODE_PRIVATE).edit();
        editor.putBoolean(SRN_USER, show);
        editor.commit();
    }

    public static boolean isSRNUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SRN_USER, MODE_PRIVATE);
        boolean show = prefs.getBoolean(SRN_USER, true);
        return show;
    }



    public static void setToolTipHomeScreen(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHOW_TOOLTIP_HOME, MODE_PRIVATE).edit();
        editor.putBoolean(SHOW_TOOLTIP_HOME, show);
        editor.commit();
    }

    public static boolean isToolTipHomeToBeShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHOW_TOOLTIP_HOME, MODE_PRIVATE);
        boolean show = prefs.getBoolean(SHOW_TOOLTIP_HOME, true);
        return show;
    }

    public static void setToolTipContentScreen(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHOW_TOOLTIP_CONTENT, MODE_PRIVATE).edit();
        editor.putBoolean(SHOW_TOOLTIP_CONTENT, show);
        editor.commit();
    }

    public static boolean isToolTipContentToBeShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHOW_TOOLTIP_CONTENT, MODE_PRIVATE);
        boolean show = prefs.getBoolean(SHOW_TOOLTIP_CONTENT, true);
        return show;
    }

    public static void setToolTipDiagnosticScreen(Context context, boolean show) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHOW_TOOLTIP_DIAGNOSTIC, MODE_PRIVATE).edit();
        editor.putBoolean(SHOW_TOOLTIP_DIAGNOSTIC, show);
        editor.commit();
    }

    public static boolean isToolTipDiagnosticToBeShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHOW_TOOLTIP_DIAGNOSTIC, MODE_PRIVATE);
        boolean show = prefs.getBoolean(SHOW_TOOLTIP_DIAGNOSTIC, true);
        return show;
    }

    public static void setStateSelection(Context context, String state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_STATE, MODE_PRIVATE).edit();
        editor.putString(SELECT_STATE, state);
        editor.commit();

    }

    public static String getSelectedState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_STATE, MODE_PRIVATE);
        String language = prefs.getString(SELECT_STATE, null);
        return language;

    }

    public static void setParentsContact(Context context, String state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PARENTS_CONTACT, MODE_PRIVATE).edit();
        editor.putString(PARENTS_CONTACT, state);
        editor.commit();

    }

    public static String getParentsContact(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PARENTS_CONTACT, MODE_PRIVATE);
        String language = prefs.getString(PARENTS_CONTACT, null);
        return language;

    }

    public static void setCitySelection(Context context, String city) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SELECT_CITY, MODE_PRIVATE).edit();
        editor.putString(SELECT_CITY, city);
        editor.commit();

    }

    public static String getSelectedCity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SELECT_CITY, MODE_PRIVATE);
        String language = prefs.getString(SELECT_CITY, null);
        return language;

    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() throws Exception {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize_(model);
        }
        return capitalize_(manufacturer) + " " + model;
    }

    private static String capitalize_(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static void getCategories(Context context, Global global) throws Exception {
        global.getDatabaseReference().child(ApplicationConstants.NEW_TOPIC_DB).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child(Util.getSelectedLanguagePackage(context)).child(ApplicationConstants.CATEGORIES).child("" + pos).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        pos++;
                        getCategories(context, global);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    setCategoryDownloaded(context, true);
                    Log.i("Data : ", "Test");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean isCategoryDownloaded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CATEGORY_DOWNLOADED, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(CATEGORY_DOWNLOADED, false);
        return isDataDownloaded;

    }

    public static void setCategoryDownloaded(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CATEGORY_DOWNLOADED, MODE_PRIVATE).edit();
        editor.putBoolean(CATEGORY_DOWNLOADED, data);
        editor.commit();

    }

    public static ArrayList<String> getCommonMessages(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_MESSAGE, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(COMMON_MESSAGE, null);
            ArrayList<String> messagesArrayList = new ArrayList<>();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            messagesArrayList = gson.fromJson(json, type);
            return messagesArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getScreenOrientation(Context context) {
        int configration = context.getResources().getConfiguration().orientation;
        return configration;
    }

    public static double getScreenSizeInInches(Activity context) throws Exception {
        int mWidthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int mHeightPixels = context.getResources().getDisplayMetrics().heightPixels;
        context.getWindowManager().getDefaultDisplay().getMetrics(context.getResources().getDisplayMetrics());
        double x = Math.pow(mWidthPixels / context.getResources().getDisplayMetrics().xdpi, 2);
        double y = Math.pow(mHeightPixels / context.getResources().getDisplayMetrics().ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }


    public static boolean isActivationDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ACTIVATION, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(ACTIVATION, false);
        return isDataDownloaded;
    }

    public static void setActivation(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ACTIVATION, MODE_PRIVATE).edit();
        editor.putBoolean(ACTIVATION, data);
        editor.commit();
    }

    public static boolean isOfflineMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(OFFLINE_MODE, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(OFFLINE_MODE, false);
//        boolean isDataDownloaded = false;
        return isDataDownloaded;
    }

    public static void setOfflineMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(OFFLINE_MODE, MODE_PRIVATE).edit();
        editor.putBoolean(OFFLINE_MODE, data);
        editor.commit();
    }

    public static boolean isFacilitatorNeedToSynced(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FACILITATOR_SYNCED, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(FACILITATOR_SYNCED, false);
        return isDataDownloaded;
    }

    public static void setFacilitatorNeedToSynced(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FACILITATOR_SYNCED, MODE_PRIVATE).edit();
        editor.putBoolean(FACILITATOR_SYNCED, data);
        editor.commit();
    }

//    public static JSONObject readFile(Context context,String filePath) {
//        File fileEvents = new File(Util.getSDCardPath(context), "/" + filePath);
//        StringBuilder text = new StringBuilder();
//        JSONObject jsonObj = null;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
//            String line;
//            while ((line = br.readLine()) != null) {
//                text.append(line);
//                text.append('\n');
//            }
//            br.close();
//        } catch (IOException e) { }
//        try {
//            jsonObj = new JSONObject(text.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return jsonObj;
//
//
//    }
//    public static JSONObject readJsonFile(Context context, String filePath) {
//        JSONObject jsonObj = null;
//        try {
//            File yourFile = new File(Util.getSDCardPath(context)+"/"+ filePath);
//
//            InputStream inputStream= context.openFileInput(Util.getSDCardPath(context)+"/"+ yourFile);
//
//            FileInputStream  stream = new FileInputStream(yourFile1);
//
//            String jsonStr = null;
//            try {
//                FileChannel fc = stream.getChannel();
//                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//
//                jsonStr = Charset.defaultCharset().decode(bb).toString();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                stream.close();
//            }
//            try {
//                jsonObj = new JSONObject(jsonStr);
//                if (jsonObj == null)
//                {
//                    jsonObj = readFile(context,filePath);
//                }else {
//                    jsonObj = new JSONObject(jsonStr);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                if (jsonObj == null)
//                {
//                    jsonObj = readFile(context,filePath);
//                }else {
//                    jsonObj = new JSONObject(jsonStr);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonObj = null;
//            //                jsonObj = readFile3(context,filePath);
//            jsonObj = getDecryptedContent(context,filePath);
//
//        }
//        return jsonObj;
//
//    }


    public static JSONObject readJsonFile(Context context, String filePath) {
        JSONObject jsonObj = null;
        try {
            File yourFile = new File(Util.getSDCardPath(context),filePath);

            FileInputStream stream = new FileInputStream(yourFile);

            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            try {
                jsonObj = new JSONObject(jsonStr);
                if (jsonObj == null)
                {
                    jsonObj = readFile(context,filePath);
                }else {
                    jsonObj = new JSONObject(jsonStr);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                if (jsonObj == null)
                {
                    jsonObj = readFile(context,filePath);
                }else {
                    jsonObj = new JSONObject(jsonStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObj = null;

        }
        return jsonObj;

    }

    public static JSONObject readFile(Context context,String filePath) {
        File fileEvents = new File(Util.getSDCardPath(context),filePath);
        StringBuilder text = new StringBuilder();
        JSONObject jsonObj = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) { }
        try {
            jsonObj = new JSONObject(text.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;


    }

    public static JSONObject getDecryptedContent(Context context, String path) {
        String decryptedOutput= "";
        String encryptedOutput = "";

        try {
//            CryptLib _crypt = new CryptLib();
            String yourFile4 = Util.getSDCardPath(context)+path;
            File yourFile1 = new File(Util.getSDCardPath(context),path);
            FileInputStream inputStream = new FileInputStream(yourFile1);
            String content = null;

            byte[] readByte = new byte[inputStream.available()];
            while(inputStream.read(readByte) != -1){
                content = new String(readByte);
            }
//            Util.setsavecontent(context,content);
            inputStream.close();
            String plainText = content;
//            String key = CryptLib.SHA256("my secret key", 32); //32 bytes = 256 bit
//            String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit
////            encryptedOutput = _crypt.encrypt(plainText, key, "SLEBijJ3sN69otbw"); //encrypt
//            decryptedOutput = _crypt.decrypt(plainText, key,"SLEBijJ3sN69otbw"); //decrypt
            return new JSONObject(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static JSONObject readJsonFile(Context context, String filePath) {
//        JSONObject jsonObj = null;
//        try {
//            File yourFile = new File(Util.getSDCardPath(context), "/" + filePath);
////            File yourFile = new File("/storage/824D-1606/.iDream_content/offlinetab_PAL/StaticTextDB.txt");
////            File yourFile = new File(filePath);
//
//            //  /storage/824D-1606/.iDream_content/offlinetab_PAL/StaticTextDB.txt
//            System.out.println("------- yourFile "+yourFile);
//            System.out.println("------- Util.getSDCardPath(context) "+Util.getSDCardPath(context));
//            FileInputStream stream = new FileInputStream(yourFile);
//            String jsonStr = null;
//            try {
//                FileChannel fc = stream.getChannel();
//                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//
//                jsonStr = Charset.defaultCharset().decode(bb).toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                stream.close();
//            }
//            try {
//                jsonObj = new JSONObject(jsonStr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonObj = null;
//
//        }
//        return jsonObj;
//
//    }

    public static void parse(Context context, String jsonFilePath) {
        try {
            //BufferedReader reader = new BufferedReader(new FileReader(Util.getSDCardPath(context)+"/" + jsonFilePath));
            JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(Util.getSDCardPath(context) + "/" + jsonFilePath), StandardCharsets.UTF_8));

            Gson gson = new GsonBuilder().create();
            jsonReader.beginObject();
//            GsonModel classesModel = gson.fromJson(jsonReader, GsonModel.class);
            ArrayList<GsonModel> aa = new ArrayList<>();
            while (jsonReader.hasNext()) {
                //jsonReader.beginObject();
                GsonModel classesModel = gson.fromJson(jsonReader, GsonModel.class);
                HashMap<String, Object> a = classesModel.getTestMap();
                GsonModel gsonModel = new GsonModel();
                gsonModel.setTestMap(a);
                aa.add(gsonModel);
                //do something real
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void setNGOIMAGES(Context context, ArrayList<String> messages) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NGO_IMAGES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        editor.putString(NGO_IMAGES, json);
        editor.apply();


    }

    public static ArrayList<String> getNGOIMAGES(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NGO_IMAGES, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(NGO_IMAGES, null);
            ArrayList<String> messagesArrayList = new ArrayList<>();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            messagesArrayList = gson.fromJson(json, type);
            return messagesArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean checkZero(int input){
        if(input==0){
            return true;
        }else{
            return false;
        }
    }

    public static void setLastBatch(Context context, String VIDEO_Qualit) {
        SharedPreferences.Editor editor = context.getSharedPreferences("LastBatch", MODE_PRIVATE).edit();
        editor.putString("LastBatch", VIDEO_Qualit);
        editor.commit();

    }

    public static String getLastBatch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LastBatch", MODE_PRIVATE);
        String language = prefs.getString("LastBatch", null);
        return language;
    }

    /** add/update data of subject*/
    public static void saveSubjectInfo(HashMap<String, SubjectInfoModel> subjectMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveSubjectInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = new Gson().toJson(subjectMap);
        editor.putString("saveSubjectInfo", jsonString);
        editor.apply();
    }

    /** get subjects info by subjectID */
    public static SubjectInfoModel getSubjectInfo(String subjectID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveSubjectInfo", MODE_PRIVATE);

        String defValue = new Gson().toJson(new HashMap<String, Object>());
        String json=sharedPreferences.getString("saveSubjectInfo",defValue);
        com.google.common.reflect.TypeToken<HashMap<String,SubjectInfoModel>> token = new com.google.common.reflect.TypeToken<HashMap<String,SubjectInfoModel>>() {};
        HashMap<String,SubjectInfoModel> retrievedMap=new Gson().fromJson(json,token.getType());

        SubjectInfoModel subjectInfoModel = retrievedMap.get(subjectID);

        return subjectInfoModel;
    }

    /** add all subject*/
    public static void saveAllSubjectInfo(ArrayList<SubjectInfoModel> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveAllSubjectInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = new Gson().toJson(list);
        editor.putString("saveAllSubjectInfo", jsonString);
        editor.apply();
    }

    /** get all subjects info */
    public static ArrayList<SubjectInfoModel> getAllSubject() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveAllSubjectInfo", MODE_PRIVATE);

        String defValue = new Gson().toJson(new HashMap<String, Object>());
        String json=sharedPreferences.getString("saveAllSubjectInfo",defValue);

        if(Objects.equals(json, defValue)) return null;

        com.google.common.reflect.TypeToken<ArrayList<SubjectInfoModel>> token = new com.google.common.reflect.TypeToken<ArrayList<SubjectInfoModel>>() {};
        ArrayList<SubjectInfoModel> retrievedMap=new Gson().fromJson(json,token.getType());

        return retrievedMap;
    }


    private void parseLargeJson(String jsonFilePath) {
//        try {
//        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(jsonFilePath), StandardCharsets.UTF_8);
//        JsonReader jsonReader1 = new JsonReader(streamReader);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static void setTopics(Context context, ArrayList<String> messages) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOPICS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        editor.putString(TOPICS, json);
        editor.apply();
    }

    public static ArrayList<String> getTopics(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(TOPICS, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(TOPICS, null);
            ArrayList<String> messagesArrayList = new ArrayList<>();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            messagesArrayList = gson.fromJson(json, type);
            return messagesArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJson(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("image.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }


    public static void setForPal(Context context, String NGO) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FOR_PAL, MODE_PRIVATE).edit();
        editor.putString(FOR_PAL, NGO);
        editor.commit();

    }

    public static String getForPal(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FOR_PAL, MODE_PRIVATE);
        String NGO_ID_ = prefs.getString(FOR_PAL, null);
        return NGO_ID_;

    }

    public static void setPageType(Context context, String PageType) {
        SharedPreferences.Editor editor = context.getSharedPreferences("PageType", MODE_PRIVATE).edit();
        editor.putString("PageType", PageType);
        editor.commit();

    }

    public static String getPageType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PageType", MODE_PRIVATE);
        String PageType = prefs.getString("PageType", AboutPalActivity.ABOUTPAL);
        return PageType;

    }

    public static void setVideoLevel(Context context, int level) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VIDEO_LEVEL, MODE_PRIVATE).edit();
        editor.putInt(VIDEO_LEVEL, level);
        editor.commit();

    }

    public static int getVideoLevel(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(VIDEO_LEVEL, MODE_PRIVATE);
        int level = prefs.getInt(VIDEO_LEVEL,1);
        return level;

    }

    public static void setUnlockVideoLevel(Context context,String topicid, int level) {
        SharedPreferences.Editor editor = context.getSharedPreferences(UNLOCK_VIDEO_LEVEL+topicid, MODE_PRIVATE).edit();
        editor.putInt(UNLOCK_VIDEO_LEVEL+topicid, level);
        editor.commit();

    }

    public static int getUnlockVideoLevel(Context context,String topicid) {
        SharedPreferences prefs = context.getSharedPreferences(UNLOCK_VIDEO_LEVEL+topicid, MODE_PRIVATE);
        int level = prefs.getInt(UNLOCK_VIDEO_LEVEL+topicid, 1);
        return level;

    }

    public static String timestampToDate100(long timestamp) {

        Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return sdf.format(date);
    }

    public static String timestampToDate(long timestamp) {

        Date date = new Date(timestamp); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return sdf.format(date);


    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) throws Exception {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static Balloon toolTip;
    public static void showTooltip(Context context, LifecycleOwner lifecycleOwner, ArrowOrientation arrowOrientation, String direction, String message,
                                   String buttonText, String skipText, String type, View anchor){
//        final Handler handler = new Handler();
//        handler.postDelayed(() -> {
//            //Write whatever to want to do after delay specified (1 sec)
//            try
//            {
//                toolTip = PalTooltipBuilder.getTooltip(context, lifecycleOwner, arrowOrientation);
//                if(direction.equals("right")){
//                    toolTip.showAlignRight(anchor, 2, 50);
//                }else if(direction.equals("top")){
//                    toolTip.showAlignTop(anchor, 150);
//                }
//                TextView text = toolTip.getContentView().findViewById(R.id.textViewTooltipMsg);
//                text.setText(message);
//                TextView skipBt = toolTip.getContentView().findViewById(R.id.textViewTooltipSkipBt);
//                if(type.equals("homeScreen") || type.equals("contentScreen") || type.equals("diagnosticTestTextView") || type.equals("practiceTextView") ||
//                        type.equals("finalTestTextView") || type.equals("diagnosticTestDownArrow") || type.equals("diagnosticTestSkipButton") ||
//                        type.equals("diagnosticTestBackButton")){
//                    skipBt.setVisibility(View.GONE);
//                }else{
//                    skipBt.setVisibility(View.VISIBLE);
//                }
//                skipBt.setText(skipText);
//                TextView bt = toolTip.getContentView().findViewById(R.id.textViewTooltipBt);
//                bt.setVisibility(View.VISIBLE);
//                bt.setText(buttonText);
//                skipBt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toolTip.dismiss();
//                    }
//                });
//                bt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toolTip.dismiss();
//                        try {
//                            if(type.equals("homeScreen")){
//                                ((PracticeTopicActivity)context).hideFullScreenTransparentBg();
//                                ((PracticeTopicActivity)context).showOnlySubjectsScreen();
//                            }else if(type.equals("contentScreen")){
//                                ((PalContentListingActivity)context).showToolTipForDiagnosticTest();
//                            }else if(type.equals("diagnosticTestTextView")){
//                                ((PalContentListingActivity)context).showToolTipForPractice();
//                            }else if(type.equals("practiceTextView")){
//                                ((PalContentListingActivity)context).showToolTipForFinalTest();
//                            }else if(type.equals("finalTestTextView")){
//                                ((PalContentListingActivity)context).hideFullTransparentScreen();
//                                ((PalContentListingActivity)context).showTransparentScreenForTopicSelection();
//                            }else if(type.equals("diagnosticTestDownArrow")){
//                                ((DiagonosticTestActivity)context).showToolTipForSkipButton();
//                            }else if(type.equals("diagnosticTestSkipButton")){
//                                ((DiagonosticTestActivity)context).showToolTipForBackButton();
//                            }else if(type.equals("diagnosticTestBackButton")){
//                                ((DiagonosticTestActivity)context).hideFullScreenTransparentBg();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//            catch (Exception ee)
//            {
//                ee.printStackTrace();
//            }
//        }, 1000);
    }

    private static boolean isdialogvisible=false;

    public static void openGifDialogue(Context context, String textError) {

        if(!isdialogvisible)
        {
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
                    try
                    {
                        isdialogvisible=false;
                        dialog.dismiss();
                    }
                    catch (Exception e){}
                }
            }, 5000);

            TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
            textViewRetry.setVisibility(View.GONE);
            ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
            Glide.with(context)
                    .load(R.raw.error)
                    .into(imageViewGif);
            textViewRetry.setText("Try Again");
            textViewRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isdialogvisible=false;
                    dialog.dismiss();
                }
            });
//            dialog.show();
            isdialogvisible=true;
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


            dialog.show();
            /* Change the background color of the dialog */
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            dialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //Clear the not focusable flag from the window
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isdialogvisible=false;
                }
            },1000);

        }

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
                try
                {
                    dialog.dismiss();
                }
                catch (Exception r){r.printStackTrace();}
            }
        }, 5000);

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setVisibility(View.GONE);
        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(context)
                .load(R.raw.submit_gif)
                .into(imageViewGif);
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

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(()-> view.setEnabled(true), 100);
    }

    public static boolean isContentonboadingDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CONTENT_ONBOADING, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(CONTENT_ONBOADING, false);
        return isDataDownloaded;
    }

    public static void setContentonboadingMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CONTENT_ONBOADING, MODE_PRIVATE).edit();
        editor.putBoolean(CONTENT_ONBOADING, data);
        editor.commit();
    }

    public static boolean isQuizOnboardingDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("QuizOnBoading", MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean("QuizOnBoading", false);
        return isDataDownloaded;
    }

    public static void setQuizOnboardingMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("QuizOnBoading", MODE_PRIVATE).edit();
        editor.putBoolean("QuizOnBoading", data);
        editor.commit();
    }

    public static boolean isSubjectboadingDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SUBJECT_ONBOADING, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(SUBJECT_ONBOADING, false);
        return isDataDownloaded;
    }

    public static void setSubjectboadingMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SUBJECT_ONBOADING, MODE_PRIVATE).edit();
        editor.putBoolean(SUBJECT_ONBOADING, data);
        editor.commit();
    }

    public static boolean isDiagnosticboadingDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Diagnostic_test", MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean("Diagnostic_test", false);
        return isDataDownloaded;
    }

    public static void setDiagnosticboadingMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Diagnostic_test", MODE_PRIVATE).edit();
        editor.putBoolean("Diagnostic_test", data);
        editor.commit();
    }

    public static boolean isHomeboadingDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(HOME_ONBOADING, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(HOME_ONBOADING, false);
        return isDataDownloaded;
    }

    public static void setHomeboadingMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(HOME_ONBOADING, MODE_PRIVATE).edit();
        editor.putBoolean(HOME_ONBOADING, data);
        editor.commit();
    }

    public static void setVideoQuality(Context context, String VIDEO_Qualit) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VIDEO_Quality, MODE_PRIVATE).edit();
        editor.putString(VIDEO_Quality, VIDEO_Qualit);
        editor.commit();

    }

    public static String getVideoQuality(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(VIDEO_Quality, MODE_PRIVATE);
        String language = prefs.getString(VIDEO_Quality, "360p");
        return language;
    }

    public static void setVideoSpeed(Context context, String VIDEO_speedd) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VIDEO_Speed, MODE_PRIVATE).edit();
        editor.putString(VIDEO_Speed, VIDEO_speedd);
        editor.commit();

    }

    public static String getVideoSpeed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(VIDEO_Speed, MODE_PRIVATE);
        String language = prefs.getString(VIDEO_Speed, "1x Normal Speed");
        return language;
    }


    public static void setclassChange(Context context, boolean classchange) {
        SharedPreferences.Editor editor = context.getSharedPreferences("classchange", MODE_PRIVATE).edit();
        editor.putBoolean("classchange", classchange);
        editor.commit();

    }

    public static boolean getclasschange(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("classchange", MODE_PRIVATE);
        boolean language = prefs.getBoolean("classchange", true);
        return language;
    }

    public static void setColour(Context context, String VIDEO_speedd) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DefColour, MODE_PRIVATE).edit();
        editor.putString(DefColour, VIDEO_speedd);
        editor.commit();

    }

    public static String getColour(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DefColour, MODE_PRIVATE);
        String language = prefs.getString(DefColour, "#FCAC52");
        return language;
    }

    public static boolean isLauncherMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LAUNCHER_MODE, MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean(LAUNCHER_MODE, false);
        isDataDownloaded = false;
        return isDataDownloaded;
    }

    public static void setLauncherMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LAUNCHER_MODE, MODE_PRIVATE).edit();
        editor.putBoolean(LAUNCHER_MODE, data);
        editor.commit();
    }

    public static boolean isPortraitMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Portrait_MODE", MODE_PRIVATE);
        boolean isDataDownloaded = prefs.getBoolean("Portrait_MODE", false);
        return isDataDownloaded;
    }

    public static void setPortraitMode(Context context, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Portrait_MODE", MODE_PRIVATE).edit();
        editor.putBoolean("Portrait_MODE", false);
        editor.commit();
    }

    public static void setSeniorTopicID(Context context, String topicID) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Senior_TOPIC_ID, MODE_PRIVATE).edit();
        editor.putString(Senior_TOPIC_ID, topicID);
        editor.commit();

    }

    public static String getSeniorTopicID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Senior_TOPIC_ID, MODE_PRIVATE);
        String topicID = prefs.getString(Senior_TOPIC_ID, null);
        return topicID;

    }

    private static ArrayList<String> support_Language;
    private static Uri filePath;
    private static String linkFromFirebaseStorage,textSendWithImageTextString;

    public static void get_support_dialog_language() {
        support_Language=new ArrayList<>();
        if (isOfflineMode(context)) {
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
                        if(snapshot!=null) {
                            support_Language= (ArrayList<String>) snapshot.getValue();
                            System.out.println( "------------ support_Language "+support_Language);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void openCapturedScreenshot(Context context,File fileScreenshot) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogue_send_screenshot);

        if(support_Language==null) {
            get_support_dialog_language();
//            Util.openCapturedScreenshot(context,fileScreenshot);
            return;
        }

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
                if(Util.checkInternetConnection(context)) uploadImage(context,dialog);
                else PracticeTopicActivity.openGifDialogue();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        if(Util.isPortraitMode(context)) dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.show();
        /* Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }

    private static void openLiveChat(Context context) {
//        Webview.URL="https://iprep.in/index21a2.html";
//        context.startActivity(new Intent(context,Webview.class));

//        Kommunicate.loginAsVisitor(context, new KMLoginHandler() {
//            @Override
//            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
//                // You can perform operations such as opening the conversation, creating a new conversation or update user details on success
//
//                Kommunicate.launchConversationWithPreChat(context, progressDialog, new KmCallback() {
//                    @Override
//                    public void onSuccess(Object message) {
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Object error) {
//                        progressDialog.dismiss();
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
//                // You can perform actions such as repeating the login call or throw an error message on failure
//            }
//        });
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Starting chat");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();



    }

    private static void uploadImage(Context context,Dialog dialog) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = FirebaseStorage.getInstance().getReference()
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

    private static void sendDataToFirebaseOfIssueReported() {

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

    private static final String notificationUrl="https://fcm.googleapis.com/fcm/send";
    private static RequestQueue mrequestQue;

    public static void sendNormalNotification(String sendTO,String title,String body) {
        mrequestQue = Volley.newRequestQueue(context);
        try {
            JSONObject mainobj= new JSONObject();
            mainobj.put("to","/topics/"+sendTO);
            JSONObject notificationobj=new JSONObject();
            notificationobj.put("title",title);
            notificationobj.put("body",body);

            JSONObject data=new JSONObject();
            data.put("type","notification");
            data.put("studentId",sendTO);
            data.put("title",title);
            data.put("body",body);

            mainobj.put("notification",notificationobj);
            mainobj.put("data",data);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, notificationUrl, mainobj, null, null){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header =new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","Key=AAAAJnoj22E:APA91bFhtFassESy7oOey1LR6YmAVOzR6Ah2JacJAk2-9CXAJCh3dLevilgH58wa8wNlzl6WnpXgRe8g6WzEbrS2PuUhzQZ12iKNCN9yJR0edKfrNofROKBeTHEVwXEshO5mMPuAgCVI");
                    return header;
                }
            };

            mrequestQue.add(request);
        }
        catch (JSONException j)
        {
            j.printStackTrace();
        }
    }

    public static String setColourVisibility(String colour,int visibility) {

        String finalColour=colour;

        try {
            String[] items=colour.split("#");
            finalColour="#"+visibility+items[1];
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ColourVisibility:error","colour code Mismatch");
        }

        return finalColour;

    }

    public static void preventPause(Context context,int TaskId) {

        if(Util.isLauncherMode(context))
        {
            ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(TaskId, 0);
        }


    }

    public static void setWindowSettings(Activity activity) {
//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if(isPortraitMode(activity.getApplicationContext())) {
            setPortraitView(activity);

            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        else {
            setLandscapeView(activity);
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            View mDecorView = activity.getWindow().getDecorView();

            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


            hideNavigationBar(activity.getWindow());
        }

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

    public static void setKeyboardWindowSettings(Activity activity) {

        if(isPortraitMode(activity.getApplicationContext())) {
            setPortraitView(activity);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        else {
            setLandscapeView(activity);

            ////
//            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

            ////
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        hideNavigationBar(activity.getWindow());
//        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        activity.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        activity.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static void setPortraitView(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public static void setLandscapeView(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void makePrefered(Context c) {
//        PackageManager p = c.getPackageManager();
//        ComponentName cN = new ComponentName(c, PalSplashActivity.class);
//        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//        Intent selector = new Intent(Intent.ACTION_MAIN);
//        selector.addCategory(Intent.CATEGORY_HOME);
//        c.startActivity(selector);
//
//        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static void setImageURL(Context context, String userPrfileUrl) {
        SharedPreferences.Editor editor = context.getSharedPreferences("IMAGEURL", MODE_PRIVATE).edit();
        editor.putString("IMAGEURL", userPrfileUrl);
        editor.commit();

    }

    public static String getImageURL(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("IMAGEURL", MODE_PRIVATE);
        String language = prefs.getString("IMAGEURL", null);
        return language;

    }

    private static MediaPlayer mediaPlayer;


    public static MediaPlayer initMediaPlayer() {

        releaseMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        return mediaPlayer;
    }

    public static MediaPlayer setSourceMediaPlayer(Uri uri) throws IOException {
        mediaPlayer.setDataSource(getApplicationContext(), uri);
        mediaPlayer.prepare();
        return mediaPlayer;
    }

    public static void releaseMediaPlayer() {
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static final String SELECT_AN_OPTION="Please select an option to submit";
    public static final String COMPLETE_LEVEL="Complete level :";
    public static final String LANGUAGE_UPDATED="Your language is being updated";
    public static final String COMPLETE_DIAGNOSTIC_FIRST="Please do Diagnostic test first";
    public static final String COMPLETE_PRACTICE_FIRST="To take the final exam, you must master 100% of the practice test";
    public static final String COMPLETE_FOUNDATION_FIRST="Please do foundational Practice first";
    public static final String DIAGNOSTIC_IS_COMPLETED="You have already tried it. A diagnostic test can only be taken once per topic";
    private static final String SELECT_AN_OPTION_HINDI="सबमिट करने के लिए कृपया एक विकल्प चुनें";
    private static final String LANGUAGE_UPDATED_HINDI="आपकी भाषा बदली जा रही है";
    private static final String COMPLETE_DIAGNOSTIC_FIRST_HINDI="कृपया पहले डायग्नोस्टिक परिक्षण करें";
    private static final String COMPLETE_PRACTICE_FIRST_HINDI="अंतिम परीक्षण का प्रयास करने के लिए आपको अभ्यास में 100% महारत हासिल करनी होगी";
    private static final String COMPLETE_FOUNDATION_FIRST_HINDI="कृपया पहले मुलभुत अध्याय का परिक्षण करें";
    private static final String DIAGNOSTIC_IS_COMPLETED_HINDI="आप पहले ही डायग्नोस्टिक परीक्षण का प्रयास कर चुके हैं";
    private static final String SELECT_AN_OPTION_ANIM="click_dialog.json";
    private static final String COMPLETE_LEVEL_ANIM="level_dialog.json";
    private static final String COMPLETE_DIAGNOSTIC_FIRST_ANIM="test_dialog.json";
    private static final String LANGUAGE_UPDATED_ANIM="tick_dialog.json";
    private static final String COMPLETE_PRACTICE_FIRST_ANIM="practice_dialog.json";
    private static final String COMPLETE_FOUNDATION_FIRST_ANIM="practice_dialog.json";
    private static final String DIAGNOSTIC_IS_COMPLETED_ANIM="no_dialog.json";
    private static final String OOPS_ANIM="oops_dialog.json";
    private static final String[] ss= {LANGUAGE_UPDATED,COMPLETE_DIAGNOSTIC_FIRST,COMPLETE_PRACTICE_FIRST};

    /** Animated Dialog  */
    @SuppressLint("SetTextI18n")
    public static void showAnimatedDialog(Context context,String message) {

        /**
         * SELECT_AN_OPTION
         *
         * COMPLETE_LEVEL_1
         * COMPLETE_LEVEL_2
         * COMPLETE_LEVEL_3
         * COMPLETE_DIAGNOSTIC_FIRST
         * LANGUAGE_UPDATED
         * COMPLETE_PRACTICE_FIRST
         * COMPLETE_FOUNDATION_FIRST
         * DIAGNOSTIC_IS_COMPLETED   */
        if(!isdialogvisible) {
            /** create Dialog & init by custom layout */
            final Dialog dialog = new Dialog(context, R.style.CustomDialog);
            dialog.setContentView(R.layout.animated_dialogue);
            dialog.setCancelable(true);

            /** init dialog views */
            LottieAnimationView lotie_player=dialog.findViewById(R.id.lotie_player);
            LinearLayout animDialog=dialog.findViewById(R.id.animDialog);
            TextView text=dialog.findViewById(R.id.text);
            lotie_player.loop(true);

            /** dialog delay
             * if we don't want delay set its value to 0 */
            int delay=0;

            /** set Default Message & Anim.. */
            text.setText(message);
            lotie_player.setAnimation(OOPS_ANIM);

            /** Check if Message have Anim.. */
            if(message.equals(SELECT_AN_OPTION)) {
                lotie_player.setAnimation(SELECT_AN_OPTION_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(SELECT_AN_OPTION_HINDI);
            }
            else if(message.equals(LANGUAGE_UPDATED)) {
                dialog.setCancelable(false);
                animDialog.setEnabled(false);
                delay=3000;
                lotie_player.setAnimation(LANGUAGE_UPDATED_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(LANGUAGE_UPDATED_HINDI);
            }
            else if(message.equals(COMPLETE_DIAGNOSTIC_FIRST)) {
                lotie_player.setAnimation(COMPLETE_DIAGNOSTIC_FIRST_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(COMPLETE_DIAGNOSTIC_FIRST_HINDI);
            }
            else if(message.equals(COMPLETE_PRACTICE_FIRST)) {
                lotie_player.setAnimation(COMPLETE_PRACTICE_FIRST_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(COMPLETE_PRACTICE_FIRST_HINDI);
            }
            else if(message.equals(COMPLETE_FOUNDATION_FIRST)) {
                lotie_player.setAnimation(COMPLETE_FOUNDATION_FIRST_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(COMPLETE_FOUNDATION_FIRST_HINDI);
            }
            else if(message.equals(DIAGNOSTIC_IS_COMPLETED)) {
                lotie_player.setAnimation(DIAGNOSTIC_IS_COMPLETED_ANIM);
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText(DIAGNOSTIC_IS_COMPLETED_HINDI);
            }
            else if(message.contains(":")) {
                /** This is specially for show level dialog,add level by adding COMPLETE_LEVEL + {level number}*/
                lotie_player.setAnimation(COMPLETE_LEVEL_ANIM);
                String[] a= message.split(":");
                if(Util.getSelectedLanguage(context).equals("hindi")) text.setText( "पहले स्तर "+a[1]+" पूरा करे");
                else text.setText("First complete level "+a[1]);
            }

            /** Add background & flags for dialog */
            Objects.requireNonNull(dialog.getWindow()).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            animDialog.setOnClickListener(v -> {
                isdialogvisible=false;
                dialog.dismiss();
            });

            /** Automatically hide dialog after delay */
            if(delay>0) {
                new Handler().postDelayed(() -> {
                    try {
                        isdialogvisible=false;
                        dialog.dismiss();
                    } catch (Exception e) {}
                },delay);
            }


            /** play anim.. & show dialog */
            lotie_player.playAnimation();
//            isdialogvisible=true;
            try {
                dialog.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            dialog.setOnDismissListener(dialog1 -> isdialogvisible=false);
        }
    }

    /**
     * @param context => context
     * @param title  => title to show
     * @param message => details to show
     * @param intent => What should happen on clicking the notification
     * @param reqCode => unique code for the notification  */
    public static void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }

    public static String getColorOffline(String subjectID) {

        if(subjectID==null) return "#FCAC52";
        else if(subjectID.equals("math")) return "#1c6b41";
        else if(subjectID.equals("biology")) return "#139889";
        else if(subjectID.equals("chemistry")) return "#524E9B";
        else if(subjectID.equals("physics")) return "#008ECE";
        else if(subjectID.equals("english_grammar")) return "#E57638";
        else {
            try {
                SubjectInfoModel subjectsModel = Util.getSubjectInfo(subjectID);
                return subjectsModel.getColor();
            }catch (Exception e ){
                return "#FCAC52";
            }
        }
    }

    public static String getSubjectName(String subjectID) {
        if(subjectID.equals("math")) return "Math";
        else if(subjectID.equals("biology")) return "Biology";
        else if(subjectID.equals("chemistry")) return "Chemistry";
        else if(subjectID.equals("physics")) return "Physics";
        else if(subjectID.equals("english_grammar")) return "English Grammar";
        else {
            if(Util.getSubjectInfo(subjectID).getName()!=null) return Util.getSubjectInfo(subjectID).getName();
        }

        return "Math";
    }

    public static String capitalStringFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String getProjectID() {
        return "p_1685947953096";
    }

    public static void setUpdatedTimestamp(Context context, long userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences("UpdatedTimestamp", MODE_PRIVATE).edit();
        editor.putLong("UpdatedTimestamp", userId);
        editor.commit();
    }

    public static void setUpdatedOfflineTabPath(Context context, String path) {
        SharedPreferences.Editor editor = context.getSharedPreferences("UPDATED_FILE_PATH", MODE_PRIVATE).edit();
        editor.putString("UPDATED_FILE_PATH", path);
        editor.commit();
    }

    public static long getUpdatedTimestamp(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UpdatedTimestamp", MODE_PRIVATE);
        long language = prefs.getLong("UpdatedTimestamp",0);
        return language;
    }

    public static String generateRandomColor() {
        Random random = new Random();
        int red, green, blue;

        do {
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
        } while (red == 255 && green == 255 && blue == 255);

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private static boolean isTestingApp=false;

    public static boolean isTestApp() {
        return isTestingApp;
    }

    public static void setTestingAPP(Context context, boolean isTest) {
        SharedPreferences.Editor editor = context.getSharedPreferences("TESTING_ENV", MODE_PRIVATE).edit();
        editor.putBoolean("TESTING_ENV", isTest);
        editor.commit();
    }

    public static boolean isTestingAPP(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("TESTING_ENV", MODE_PRIVATE);
        boolean language = prefs.getBoolean("TESTING_ENV", false);
        return language;
    }

    private static Long timestamp=null;

    public static long getCurrentTimestamp(Context c) {

        timestamp=null;

//        if(isTimeAutomatic(c)) {
//            System.out.println("========--- LOCAL TIME");
        return System.currentTimeMillis();
//        }
//        else {
        // yhi to nehi hoo reha ;)
//            System.out.println("========--- SERVER TIME ");
//            getttime(c);
//            return fetchTimestamp();
//
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if(countt==10) {
//
//                    }
//
//                    if(timestamp==null) {
//                        countt++;
//                        handler.postDelayed(this, 1000);
//                    }
//
//
//                }
//            }, 1000);
//        }


    }

    public static int dpToPixel(Context context,int dp){
        float totalScale;
        int dpAsPixels;
        totalScale = context.getResources().getDisplayMetrics().density;
        dpAsPixels = (int) (dp * totalScale + 0.5f);
        return dpAsPixels;
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}