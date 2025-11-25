package com.idreameducation.ipreppal.pal.activity.loginPages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.EnvironmentCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.ServiceCloseBar;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.PalSRNLogin;
import com.idreameducation.ipreppal.roomdatabase.model.ActivationModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ActivationDetailsRepository;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class PalActivationDetailActivity extends AppCompatActivity {
    private static final String TAG = "ActivationDetailActivity";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private EditText tabIDEditText;
    private EditText appIDEditText;
    private TextView btnProceed;
    private Global global;
    private Context context;
    private boolean isDataLoaded = false;
    private boolean isDetailLoaded;
    private TextView textViewDetails;
    private TextInputLayout input_TabID;
    private TextInputLayout input_AppID;
    private String enterAppID;
    private String invalidAPPID;
    private String enterTabID;
    private String wrongCombination;
    private FirebaseAuth mAuth;
    private String userType = "Anonymous";
    private String userIdCreated;
    private TextView textViewtitle,loginwithSRNText;
    private TextView textViewtitleName;
    private TextView textViewtitlePassword,requestAPPID;

    private ImageView setupPathText;
    private String appIDBackup,tabIDBackup,ngoIDBackup;

    // To keep track of activity's window focus
    boolean currentFocus;

    // To keep track of activity's foreground/background status
    boolean isPaused;

    Handler collapseNotificationHandler;

    private Switch languageSwitch;
    private String language="english";

    static int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        System.out.println("======= count " + count);
//
//        if (count >= 2) {
//            Util.setPortraitMode(this, false);
//            Util.setLandscapeView(this);
//
//            Util.setWindowSettings(this);
//            Util.setKeyboardWindowSettings(this);
//
//            setContentView(R.layout.activity_activation_detail);
//
//            assignIds();
//            listners();
//        } else {
//            count++;
//            if (!isTablet(this)) {
//                Util.setPortraitMode(this, true);
//                Util.setPortraitView(this);
//
//            } else {
//                Util.setPortraitMode(this, false);
//                Util.setLandscapeView(this);
//            }
//            Util.setWindowSettings(this);
//            Util.setKeyboardWindowSettings(this);
//            if (Util.isPortraitMode(this)) Util.setPortraitView(this);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    setContentView(R.layout.activity_activation_detail);
//
//                    assignIds();
//                    listners();
//                }
//            }, 1000);
//        }

        Util.setPortraitMode(this, true);
        Util.setPortraitView(this);

        Util.setWindowSettings(this);
        Util.setKeyboardWindowSettings(this);
        if (Util.isPortraitMode(this)) Util.setPortraitView(this);

        setContentView(R.layout.activity_activation_detail);

        assignIds();
        listners();
    }


    @Override
    protected void onStop() {
        try {
            Util.setLogoutSelection(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void listners() {
        findViewById(R.id.btnProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkInternetConnection(context)) {
                    String appID = appIDEditText.getText().toString().trim();
                    String tabID = tabIDEditText.getText().toString().trim();

                    if(appID.contains(".") || appID.contains("#") || appID.contains("$") || appID.contains("[") ||appID.contains("]") ) {
                        Toast.makeText(context, "Invalid AppID", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(tabID)) {
                        tabIDEditText.setError(enterTabID);
                    } else if (TextUtils.isEmpty(appID)) {
                        appIDEditText.setError(enterAppID);
                    } else {
                        getNGOID(appID, tabID);
                    }
                } else {
                    openGifDialogue();
//                    Util.showToast(context, "Internet is not available");
                }
            }
        });
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        textViewtitle = findViewById(R.id.textViewtitle);
        textViewtitleName = findViewById(R.id.textViewtitleName);
        textViewtitlePassword = findViewById(R.id.textViewtitlePassword);
        tabIDEditText = findViewById(R.id.tabIDEditText);
        textViewDetails = findViewById(R.id.textViewDetails);
        input_AppID = findViewById(R.id.input_AppID);
        input_TabID = findViewById(R.id.input_TabID);
        appIDEditText = findViewById(R.id.appIDEditText);
        btnProceed = findViewById(R.id.btnProceed);
        setupPathText = findViewById(R.id.setupPathText);
        loginwithSRNText = findViewById(R.id.loginwithSRNText);
        requestAPPID = findViewById(R.id.requestAPPID);
        languageSwitch = findViewById(R.id.languageSwitch);
        mAuth = FirebaseAuth.getInstance();

        String androidID = Util.getAndroidId(context);

//        checkOfflineMode();

        Intent serviceIntent = new Intent(this, ServiceCloseBar.class);
//        startService(serviceIntent);

        if (TextUtils.isEmpty(androidID)) {
            tabIDEditText.setFocusable(true);
            tabIDEditText.setText("");
        } else {
            tabIDEditText.setFocusable(false);
            tabIDEditText.setText(androidID);
        }
        setStaticText();

//        if(Util.isActivationDone(context)) startActivity(new Intent(this,PalAnonymousLoginActivity.class));
//        finish();


//        findViewById(R.id.logo).setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.versiondetails_layout);
//                dialog.setCancelable(true);
//                TextView updated_date_text = dialog.findViewById(R.id.updated_date_text);
//                TextView app_version_text = dialog.findViewById(R.id.app_version_text);
//                Button open_launcher_btn = dialog.findViewById(R.id.open_launcher_btn);
//                ImageView close_version_ly_btn = dialog.findViewById(R.id.close_version_ly_btn);
//
//                Long buildDate = BuildConfig.TIMESTAMP;
//                String dateString = DateFormat.format("dd-MM-yyyy", new Date(buildDate)).toString();
//                int versionNo = BuildConfig.VERSION_CODE;
//
//                updated_date_text.setText(dateString);
//                try {
//                    PackageManager pm = context.getPackageManager();
//                    PackageInfo pInfo = pm.getPackageInfo("com.idreameducation.ipreppal", 0);
//                    app_version_text.setText(pInfo.versionName);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                close_version_ly_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                open_launcher_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        final Dialog dialog = new Dialog(context);
//                        dialog.setContentView(R.layout.dialog_exit_mode);
//                        dialog.setCancelable(true);
//                        final EditText editTextPassword = dialog.findViewById(R.id.editTextPassword);
//                        Button btn1 = dialog.findViewById(R.id.btn1);
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                if (!editTextPassword.getText().toString().equals(getResources().getString(R.string.Pass))) {
//                                    String message;
//                                    message = "Password is incorrect";
//                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                dialog.dismiss();
//                                Intent intent = new Intent(Intent.ACTION_MAIN);
//                                intent.addCategory(Intent.CATEGORY_HOME);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(Intent.createChooser(intent, getString(R.string.please_set_launcher_setting)));
//                                Util.preventTwoClick(view);
//                            }
//                        });
//                        dialog.show();
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
//
//                    }
//                });
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
//
//                return false;
//            }
//        });

        try {

            if (Util.getSDCardPath(context)==null) detectiDreamSDCardNeww(context);
            else {
                setupPathText.setVisibility(View.GONE);
                File file = new File(Util.getSDCardPath(context) + ".iDream_content/offlinetab_PAL/PALiDream.txt");
                File filenew = new File(Util.getSDCardPath(context) + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
                if (file.exists()) {
//                    Toast.makeText(context, "path"+Util.getSDCardPath(context), Toast.LENGTH_SHORT).show();
                    Util.setOfflineMode(context, file.exists());
                    Util.setSDCardPath(context, Util.getSDCardPath(context));
                } else if (filenew.exists()) {
//                    Toast.makeText(context, "path"+Util.getSDCardPath(context), Toast.LENGTH_SHORT).show();
                    Util.setOfflineMode(context, filenew.exists());
                    Util.setSDCardPath(context, Util.getSDCardPath(context) + "/");

                }
            }

            if (Util.getSDCardPath(context)== null) setupPathText.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
            if (Util.getSDCardPath(context)== null) setupPathText.setVisibility(View.VISIBLE);
        }

        if(Util.isPortraitMode(context))setupPathText.setVisibility(View.GONE);

        setupPathText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getManualPath();
            }
        });
        loginwithSRNText.setVisibility(View.GONE);
        loginwithSRNText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PalActivationDetailActivity.this, PalSRNLogin.class));
                finish();
            }
        });

        requestAPPID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Request AppID for PAL");
                i.putExtra(Intent.EXTRA_TEXT   , "Hi i need a APP id");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PalActivationDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        hideNavigationBar(getWindow());

        language=Util.getSelectedLanguage(context);
        if (Util.getSelectedLanguage(context).equals("english")) {
            languageSwitch.setChecked(false);
        } else {
            languageSwitch.setChecked(true);
        }

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) language = "hindi";
                else language = "english";
                if (language != null) {

                    if (Util.getSelectedLanguage(context) != language) {
                        Util.setLanguagePackageSelection(context, language);
                        Util.setLanguageSelection(context, language);
                        Util.showAnimatedDialog(context,Util.LANGUAGE_UPDATED);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
//
                                startActivity(getIntent());
//
                            }
                        }, 3000);
                    }

                }
            }
        });
        setLayoutText();
    }

    private void startAnonymousSignIn() {

        if (Util.isNetworkAvailable(context)) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Task<GetTokenResult> token = mAuth.getAccessToken(true);
                                //Toast.makeText(context, "Token :- " + token, Toast.LENGTH_SHORT).show();
                                userType = "Anonymous";
                                Util.setUserType(context, userType);
                                updateUI(user);
                                Util.setIsFirstTime(context, "true");
                            } else {
                                //Util.showToast(context, "" + task.getException());
                                Util.openGifDialogue(context,"" + task.getException());
                                // If sign in fails, display a message to the user.
                            }

                            // ...
                        }
                    });
        } else {
//            String internetConnection;
//            Util.showToast(context, "Internet Connection is not working");
            if (Util.getSelectedLanguage(context)!=null)
            {
                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                {
                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                }else {
                    Util.openGifDialogue(context,"Internet Connection is not working");
                }
            }else {
                Util.openGifDialogue(context,"Internet Connection is not working");
            }
        }


    }

    private void updateUI(FirebaseUser user) {
        //Util.dismissDialog();
        if (user != null) {
            String userName;
            if (!userType.equalsIgnoreCase("App")) {
                userName = user.getDisplayName();
                if (userName == null) {
                    userName = "User";
                }
                Util.setUsername(context, userName);
            } else {
                userName = user.getDisplayName();
                if (userName == null) {
                    userName = "User";
                }
                Util.setUsername(context, userName);
            }


            Util.setIsFirstTime(context, "true");
            Util.setLoginUserId(context, user.getUid());
            Util.setDummyUserId(context, user.getUid());
            Util.setUserId(context, user.getUid());
            userIdCreated = user.getUid();

            try {
                getNGODetails(ngoIDBackup,tabIDBackup,appIDBackup);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {

        }
    }

    private long getTimeDifference(long time, long now, String tabID) {
        final long diff = time - now;
        return diff;
    }

    private void getNGOID(String appID, final String tabID) {
        Util.showDialog(context);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    isDataLoaded = true;
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, String> ngoHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                        String ngoID = ngoHashMap.get("NgoId");

                        try {
                            Util.setNGOID(context, ngoID);
                            Util.setAPPID(context, appID);
                            Util.setTABID(context, tabID);
                            getNGODetails(ngoID, tabID, appID);
                            appIDBackup = appID;
                            tabIDBackup = tabID;
                            ngoIDBackup = ngoID;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.dismissDialog();
                        }
                    } else {
                        Util.dismissDialog();
//                        Util.showToast(context, invalidAPPID);
                        Util.openGifDialogue(context,invalidAPPID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                try {
                    Log.i("ugughjbj", databaseError.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

//        String path__ = String.valueOf(global.getDatabaseReference().child("ngo_app_relation").child(appID).addValueEventListener(valueEventListener));

        global.getDatabaseReference().child("ngo_app_relation").child(appID).addValueEventListener(valueEventListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDataLoaded) {
                    Util.dismissDialog();
                    global.getDatabaseReference().child("ngo_app_relation").child(appID).removeEventListener(valueEventListener);
                }

            }
        }, 4000);


    }

    private void getNGODetails(String ngoID, String tabID, String appID) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    isDataLoaded = true;
                                    Util.dismissDialog();
                                    if (dataSnapshot.getValue() != null) {
                                        HashMap<String, Object> hash = (HashMap<String, Object>) dataSnapshot.getValue();
                                        String tID = (String) hash.get("tabID");
                                        String appLicenseFreeze ="false";

                                        try{
                                            appLicenseFreeze = (String) hash.get("appLicenseFreeze");
                                        }
                                        catch (Exception r) {

                                        }


                                        if(appLicenseFreeze.equals("true")) {

                                            Util.openGifDialogue(context,"Hi! The app subscription has expired. Please contact the iDream Support team for extending your license.");
                                            return;
                                        }
                                        if (tID.equals(tabID)) {
                                            Util.setActivation(context, true);
                                            long serverTime = (long) hash.get("serverTime");
                                            long localTime = (long) hash.get("localTime");
                                            String days = (String) hash.get("licencePeriod");
                                            String image1 = (String) hash.get("image1");
                                            String image2 = (String) hash.get("image2");
                                            String userID = (String) hash.get("userID");
                                            String schoolID = (String) hash.get("schoolId");
                                            String projectID = (String) hash.get("projectId");
                                            String schoolName = (String) hash.get("schoolName");
                                            String district = (String) hash.get("district");
                                            String state = (String) hash.get("state");
                                            String projectname = (String) hash.get("projectName");


                                            if(hash.get("testingEnv")==null) {

                                                Util.setTestingAPP(context, false);
                                            }
                                            else {

                                                boolean testingEnv = (boolean) hash.get("testingEnv");
                                                Util.setTestingAPP(context,testingEnv);
                                            }



                                            boolean withLauncher = (boolean) hash.get("withLauncher");


                                            Util.setLauncherMode(context,withLauncher);


                                            Util.setSchoolId(context,schoolID);
                                            Util.setProjectId(context,projectID);
                                            Util.setSchoolName(context,schoolName);
                                            Util.setDistrict(context,district);
                                            Util.setStateSelection(context,state);
                                            Util.setProjectName(context,projectname);
                                            String forPAl = "true";
                                            Util.setForPal(context, forPAl);
                                            ArrayList<String> ngoImages = new ArrayList<>();
                                            ngoImages.add(image1);
                                            ngoImages.add(image2);
                                            Util.setNGOIMAGES(context, ngoImages);

                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("appID", appID);
                                            map.put("ngoID", ngoID);
                                            map.put("userID","");


                                            ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(getApplicationContext());
                                            ActivationModel activationModel = new ActivationModel();
                                            activationModel.setAppID(appID);
                                            activationModel.setTabID(tabID);
                                            activationModel.setUserid("");
                                            activationModel.setServerTime(serverTime);
                                            activationModel.setLocalTime(localTime);
                                            activationModel.setDays(days);
                                            activationDetailsRepository.insertActivationDetails(activationModel);


                                            try
                                            {
                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if(task.isComplete()){

                                                            try {
                                                                String tkn = task.getResult();
                                                                global.setUniqueId(tkn);

                                                                Util.setToken(context, tkn);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });
                                            }catch (Exception r){}

                                            global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);
                                            startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "yes"));
                                            
                                            finish();
                                            Util.setIsFirstTime(context, "true");

//                                            if (userID == null) {
//                                                if (Util.getUserId(context)==null)
//                                                {
//
//                                                    HashMap<String, String> map = new HashMap<>();
//                                                    map.put("appID", appID);
//                                                    map.put("ngoID", ngoID);
////                                                    map.put("userID",Util.getUserId(context));
//                                                    map.put("userID","");
//
//
//                                                    ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(getApplicationContext());
//                                                    ActivationModel activationModel = new ActivationModel();
//                                                    activationModel.setAppID(appID);
//                                                    activationModel.setTabID(tabID);
//                                                    activationModel.setUserID("");
////                                                    activationModel.setUserID(Util.getUserId(context));
//                                                    activationModel.setServerTime(serverTime);
//                                                    activationModel.setLocalTime(localTime);
//                                                    activationModel.setDays(days);
//                                                    activationDetailsRepository.insertActivationDetails(activationModel);
//                                                    global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);
//                                                    startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "yes"));
//                                                    
//                                                    finish();
//                                                    Util.setIsFirstTime(context, "true");
////                                                    startAnonymousSignIn();
//                                                }else {
//                                                    HashMap<String, String> map = new HashMap<>();
//                                                    map.put("appID", appID);
//                                                    map.put("ngoID", ngoID);
//                                                    map.put("userID",Util.getUserId(context));
//
//
//                                                    ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(getApplicationContext());
//                                                    ActivationModel activationModel = new ActivationModel();
//                                                    activationModel.setAppID(appID);
//                                                    activationModel.setTabID(tabID);
//                                                    activationModel.setUserID(Util.getUserId(context));
//                                                    activationModel.setServerTime(serverTime);
//                                                    activationModel.setLocalTime(localTime);
//                                                    activationModel.setDays(days);
//                                                    activationDetailsRepository.insertActivationDetails(activationModel);
//                                                    global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);
//                                                    startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "yes"));
//                                                    
//                                                    Util.setIsFirstTime(context, "true");
//                                                    finish();
//                                                }
//
//
//                                            }else {
//                                                HashMap<String, String> map = new HashMap<>();
//                                                map.put("appID", appID);
//                                                map.put("ngoID", ngoID);
//                                                map.put("userID",userID);
//                                                Util.setUserId(context,userID);
//                                                Util.setLoginUserId(context,userID);
//                                                Util.setDummyUserId(context,userID);
//
//
//                                                ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(getApplicationContext());
//                                                ActivationModel activationModel = new ActivationModel();
//                                                activationModel.setAppID(appID);
//                                                activationModel.setTabID(tabID);
//                                                activationModel.setUserID("");
//                                                activationModel.setServerTime(serverTime);
//                                                activationModel.setLocalTime(localTime);
//                                                activationModel.setDays(days);
//                                                activationDetailsRepository.insertActivationDetails(activationModel);
//                                                global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);
//                                                startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "yes"));
//                                                
//                                                Util.setIsFirstTime(context, "true");
//                                                finish();
//                                            }
                                        } else {
                                            //Util.showToast(context, wrongCombination);
                                            Util.openGifDialogue(context,wrongCombination);
                                            //TODO implement TabID should be same check here
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {


                        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.getValue() != null) {
                                        HashMap<String, Object> ngoHashmap = (HashMap<String, Object>) dataSnapshot.getValue();
                                        String image1 = (String) ngoHashmap.get("image1");
                                        String image2 = (String) ngoHashmap.get("image2");
                                        ArrayList<String> ngoImages = new ArrayList<>();
                                        ngoImages.add(image1);
                                        ngoImages.add(image2);
                                        Util.setNGOIMAGES(context, ngoImages);

                                        if (Util.getUserId(context)==null)
                                        {

                                            if(ngoHashmap.get("testingEnv")==null) {

                                                Util.setTestingAPP(context, false);
                                            }
                                            else {

                                                boolean testingEnv = (boolean) ngoHashmap.get("testingEnv");
                                                Util.setTestingAPP(context, testingEnv);
                                            }


                                            sendDataToSerevr(ngoID, tabID, appID, ngoHashmap);
//                                            startAnonymousSignIn();
                                        }else {
                                            sendDataToSerevr(ngoID, tabID, appID, ngoHashmap);
                                        }


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Util.dismissDialog();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).child("isRegistered").addListenerForSingleValueEvent(valueEventListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDetailLoaded) {
                    Util.dismissDialog();
                    global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).child("isRegistered").removeEventListener(valueEventListener);
                }
            }
        }, 4000);
    }

    private void getManualPath(){
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialoguepath);
        TextView submitbtn = dialog.findViewById(R.id.submitbtn);
        EditText enterpath = dialog.findViewById(R.id.enterpath);
        ImageView cross_btn = dialog.findViewById(R.id.cross_btn);


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = enterpath.getText().toString().trim();
                String pathnew = enterpath.getText().toString().trim();
                File file = new File(path + ".iDream_content/offlinetab_PAL/PALiDream.txt");
                File filenew = new File(pathnew + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
                if (file.exists()){

                    Util.setOfflineMode(context, file.exists());
                    Util.setSDCardPath(context,path);
                    try {
                        dialog.dismiss();
                        assignIds();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (filenew.exists()){

                    Util.setOfflineMode(context, filenew.exists());
                    Util.setSDCardPath(context,pathnew+"/");
                    try {
                        dialog.dismiss();
                        assignIds();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
//                    Toast.makeText(context, "No path found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));


    }




    private void detectiDreamSDCardNeww(Context context) throws Exception {
        List<String> results = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = context.getExternalFilesDirs(null);

            for (File file : externalDirs) {
                String path = "";

                if (file != null) {

                    path = file.getPath().split("/Android")[0];

                    boolean addPath = false;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        addPath = Environment.isExternalStorageRemovable(file);
                    } else {
                        addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                    }
                    //  if (addPath) {
                        results.add(path);
                    //}
                }
            }
        }

        if (results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                output = output + new String(buffer);
            }
            is.close();
            if (!output.trim().isEmpty()) {
                String[] devicePoints = output.split("\n");
                for (String voldPoint : devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

//        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            for (int i = 0; i < results.size(); i++) {
//                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
//                    Log.d("Tag", results.get(i) + " might not be extSDcard");
//                    results.remove(i--);
//                }
//            }
//        } else {
//            for (int i = 0; i < results.size(); i++) {
//                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
//                    Log.d("Tag", results.get(i) + " might not be extSDcard");
//                    results.remove(i--);
//                }
//            }
//        }

        String[] storageDirectories = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            storageDirectories[i] = results.get(i);
        }

//        String path;
        String path = storageDirectories[0];
        for (int i=0;i<storageDirectories.length;i++){
            path = storageDirectories[i];
            File file = new File(path + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
            if (file.exists()){
                Util.setSDCardPath(context, path+"/");
                Util.setOfflineMode(context, file.exists());
                break;
            }
        }



    }

    private void sendDataToSerevr(String ngoID, String tabID, String appID, HashMap<String, Object> ngoMap) {
        ngoMap.put("tabID", tabID);
        ngoMap.put("appID", appID);
        ngoMap.put("serverTime", ServerValue.TIMESTAMP);
        ngoMap.put("localTime", System.currentTimeMillis());
        ngoMap.put("isRegistered", true);
        ngoMap.put("userID", userIdCreated);

        HashMap<String, String> map = new HashMap<>();
        map.put("appID", appID);
        map.put("ngoID", ngoID);
        map.put("userID", userIdCreated);
        global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);
        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).setValue(ngoMap);
        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Util.dismissDialog();
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, Object> activationDetails = (HashMap<String, Object>) dataSnapshot.getValue();
                        long serverTime = (long) activationDetails.get("serverTime");
                        long localTime = (long) activationDetails.get("localTime");
//                        String forPal = (String) activationDetails.get("forPal");

                        String forPal = "true";
                        Util.setForPal(context, forPal);
                        String days = (String) activationDetails.get("licencePeriod");
                        try {

                            long diff = getTimeDifference(serverTime, localTime, tabID);
                            ngoMap.put("TimeDiffernce", diff);
                            global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).setValue(ngoMap);
                            ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(getApplicationContext());
                            ActivationModel activationModel = new ActivationModel();
                            activationModel.setUserid(userIdCreated);
                            activationModel.setAppID(appID);
                            activationModel.setTabID(tabID);
                            activationModel.setServerTime(serverTime);
                            activationModel.setLocalTime(localTime);
                            activationModel.setDays(days);
                            activationDetailsRepository.insertActivationDetails(activationModel);


                            String schoolID = (String) activationDetails.get("schoolId");
                            String projectID = (String) activationDetails.get("projectId");
                            String schoolName = (String) activationDetails.get("schoolName");
                            String district = (String) activationDetails.get("district");
                            String state = (String) activationDetails.get("state");
                            String projectname = (String) activationDetails.get("projectName");


                            Util.setSchoolId(context,schoolID);
                            Util.setProjectId(context,projectID);
                            Util.setSchoolName(context,schoolName);
                            Util.setDistrict(context,district);
                            Util.setStateSelection(context,state);
                            Util.setProjectName(context,projectname);

                            Util.setActivation(context, true);

                            try
                            {
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if(task.isComplete()){

                                            try {
                                                String tkn = task.getResult();
                                                global.setUniqueId(tkn);

                                                Util.setToken(context, tkn);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }catch (Exception r){}

                            startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "yes"));
                            
                            Util.setIsFirstTime(context, "true");
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void setLayoutText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("Tab_Activation_Screen");
                textViewtitle.setText((String) object_.get("title_text"));
                textViewtitleName.setText((String) object_.get("tab_edittext"));
                textViewtitlePassword.setText((String) object_.get("appid_edittext"));
                btnProceed.setText((String) object_.get("btn_text"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("Tab_Activation_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            textViewtitle.setText(dataHashMap.get("title_text"));
                            textViewtitleName.setText(dataHashMap.get("tab_edittext"));
                            textViewtitlePassword.setText(dataHashMap.get("appid_edittext"));
                            btnProceed.setText(dataHashMap.get("btn_text"));
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

    public void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("ActivationDetailActivity");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textViewtitle.setText(textArrayList.get(0));
                    input_TabID.setHint(textArrayList.get(1));
                    input_AppID.setHint(textArrayList.get(2));
                    btnProceed.setText(textArrayList.get(3));
                    enterAppID = textArrayList.get(5);
                    enterTabID = textArrayList.get(4);
                    invalidAPPID = textArrayList.get(6);
                    wrongCombination = textArrayList.get(7);

                    if(Util.getSelectedLanguage(context).equals("hindi")) {
                        requestAPPID.setText("लाइसेंस की बनवाने के लिए आप हमें 18008899710 पर कॉल कर सकते हैं");
                        textViewtitle.setText("आप गलत डिवाइस आईडी या लाइसेंस की इस्तेमाल कर रहे हैं");
                    }
                    else {
                        requestAPPID.setText("To generate an Licnece key you can reach out to us at 18008899710");
                        textViewtitle.setText("Please Enter TabID and AppID");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("ActivationDetailActivity").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textViewtitle.setText(textArrayList.get(0));
                            input_TabID.setHint(textArrayList.get(1));
                            input_AppID.setHint(textArrayList.get(2));
                            btnProceed.setText(textArrayList.get(3));
                            enterAppID = textArrayList.get(5);
                            enterTabID = textArrayList.get(4);
                            invalidAPPID = textArrayList.get(6);
                            wrongCombination = textArrayList.get(7);

                            if(Util.getSelectedLanguage(context).equals("hindi")) {
                                requestAPPID.setText("लाइसेंस की बनवाने के लिए आप हमें 18008899710 पर कॉल कर सकते हैं");
                                textViewtitle.setText("आप गलत लाइसेंस की इस्तेमाल कर रहे हैं");
                            }
                            else {
                                requestAPPID.setText("To generate an License Key you can reach out to us at 18008899710");
                                textViewtitle.setText("Please Enter TabID and License Key");
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

    private void openGifDialogue() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_toast_messages);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText("Oops! No Internet");

        TextView text_ = dialog.findViewById(R.id.text_);
        text_.setText("Please check your network connection");

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(this)
                .load(R.raw.no_internet)
                .into(imageViewGif);
        textViewRetry.setText("Try Again");
        textViewRetry.setVisibility(View.GONE);
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

//    public void collapseNow() {
//
//        // Initialize 'collapseNotificationHandler'
//        if (collapseNotificationHandler == null) {
//            collapseNotificationHandler = new Handler();
//        }
//
//        // If window focus has been lost && activity is not in a paused state
//        // Its a valid check because showing of notification panel
//        // steals the focus from current activity's window, but does not
//        // 'pause' the activity
//        if (!currentFocus && !isPaused) {
//
//            // Post a Runnable with some delay - currently set to 300 ms
//            collapseNotificationHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    // Use reflection to trigger a method from 'StatusBarManager'
//
//                    Object statusBarService = getSystemService("statusbar");
//                    Class<?> statusBarManager = null;
//
//                    try {
//                        statusBarManager = Class.forName("android.app.StatusBarManager");
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    Method collapseStatusBar = null;
//
//                    try {
//
//                        // Prior to API 17, the method to call is 'collapse()'
//                        // API 17 onwards, the method to call is `collapsePanels()`
//
//                        if (Build.VERSION.SDK_INT > 16) {
//                            collapseStatusBar = statusBarManager .getMethod("collapsePanels");
//                        } else {
//                            collapseStatusBar = statusBarManager .getMethod("collapse");
//                        }
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    }
//
//                    collapseStatusBar.setAccessible(true);
//
//                    try {
//                        collapseStatusBar.invoke(statusBarService);
//                    } catch (IllegalArgumentException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//
//                    // Check if the window focus has been returned
//                    // If it hasn't been returned, post this Runnable again
//                    // Currently, the delay is 100 ms. You can change this
//                    // value to suit your needs.
//                    if (!currentFocus && !isPaused) {
//                        collapseNotificationHandler.postDelayed(this, 0);
//                    }
//
//                }
//            }, 0);
//        }
//    }

    private void checkOfflineMode() {

//        if (Build.VERSION.SDK_INT >= 30){
//            if (!Environment.isExternalStorageManager()) {
//                Intent getpermission = new Intent();
//                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivity(getpermission);
//            }
//        }
//
//
//        try {
//            detectiDreamSDCardNeww(context);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}
