package com.idreameducation.ipreppal.pal.activity.loginPages;

import static com.idreameducation.ipreppal.util.Util.openGifDialogueSuccess;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.idreameducation.ipreppal.BuildConfig;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AndroidIdModel;
import com.idreameducation.ipreppal.model.CurrentPlanModel;
import com.idreameducation.ipreppal.model.RegisterModel;
import com.idreameducation.ipreppal.model.StudentInfoModel;
import com.idreameducation.ipreppal.model.UserInfoModel;
import com.idreameducation.ipreppal.pal.activity.PalSRNLogin;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.adapter.PalLanguageSelectionAdapter;
import com.idreameducation.ipreppal.pal.adapter.loginPage.StudentProfileAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.StudentDetailsRepository;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalAnonymousLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SigninActivity";
    private String message1;
    private String message2;
    private String message3;
    private EditText otpEditText;
    private EditText nameStudent;
    private EditText rollnoStudent, mobilenoStudent;
    private final ArrayList<String> referalArrayList = new ArrayList<>();
    private Context context;
    private EditText mPhoneNumberField;
    private TextView textViewAlreadtAccount;
    private TextView textViewAlreadtAccount_;
    private ImageView buttonGoogle;
    private ImageView buttonFacebook;
    private TextView buttonSignip;
    private TextView buttonSignInStudent;
    private TextView buttonAnnonymous;
    private TextView textInfo;
    private TextView textInfo1;
    private TextView textInfoOtp;
    private TextView textViewConfirm;
    private TextView textView;
    private TextView textViewChangeMobileNumber;
    private TextView resendButton;
    private TextView textViewOr;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private Global global;
    private final int count = 0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private TextInputLayout inputMobile;
    private TextInputLayout inputOtp;
    private LinearLayout linearLayout;
    private boolean isRegisterTraversed = false;
    private String mVerificationId;
    private final String userType = "Pal_Users";
    private String enterOtp;
    private String helloOtp;
    private String resend;
    private String mobileEnter;
    private String info1;
    private String okay;
    private String emptyMessage;
    private String emptyOtp;
    private String flow;
    private String invalidMobile;
    private String emptyMobile;
    private String emptyName;
    private String changeMobile;
    private String verifyMobile;
    private String enterName;
    private String enterAge;
    private String googlePlayError;
    private String exitMessage;
    private String yes;
    private String otpInfoText;
    private String otpText;
    private String no;
    private String error;
    private String incorrectOtp;
    private String manyTimeUsage;
    private String timeLeft;
    private RelativeLayout reletiveOtp;
    private RelativeLayout reletiveSignUp;
    //private Toolbar toolbar;
    private CountDownTimer countDownTimer;
    private final String counrtyCode = "+91";
    private boolean isVerified = true;
    private String enterMobile;
    private String message;
    private String message4;
    private String message5;
    private String message6;
    private String countryMessage;
    private String languageChangeMessage;
    private String enterPartnerCode;
    private String proceed;
    private String partnerCode;
    private String codeValidated;
    private String invalidCode;
    // [END auth_with_google]
    private String confirm;
    // [END signin]
    private ImageView country_flag;
    private TextView textViewCountry;
    private TextView textViewParterCode;
    private LinearLayout linearMobile;
    String userId;
    private TextView textViewtitle;
    private TextView textViewtitleName;
    private TextView textViewtitlePassword, textViewtitleRollNo, textViewtitlemobileNo;
    private StudentDetailsRepository studentDetailsRepository;
    private String rollNo_student;
    private String nameError, rollNoError, phoneNoerror, nameDialogueText, rollNoDialogueText, mobileDialogueText;
    private ArrayList<String> textArrayList2;
    private TextView languageTextView;
    private Switch languageSwitch;
    boolean checkLangUpdate = true;
    String rollZeroError;
    String mobZeroError;
    TextView loginwithSRNText,selectProfileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Util.setWindowSettings(this);
        new UserActivities(this, false);
        setContentView(R.layout.activity_anonymous_login);
//        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);

        assignIds();
        setStaticText();
        setStaticTextlanguage();
        listners();

    }
    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        global.sendData("Signup", this.getClass().getName());
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        Util.setToolTipContentScreen(context, true);
        Util.setToolTipDiagnosticScreen(context, true);
        Util.setToolTipHomeScreen(context, true);

        studentDetailsRepository = new StudentDetailsRepository(context);

        textViewtitle = findViewById(R.id.textViewtitle);
        textViewtitleName = findViewById(R.id.textViewtitleName);
        textViewtitlePassword = findViewById(R.id.textViewtitlePassword);
        textViewtitleRollNo = findViewById(R.id.textViewtitleRollNo);
        textViewtitlemobileNo = findViewById(R.id.textViewtitlemobileNo);
        textInfoOtp = findViewById(R.id.textInfoOtp);
        textViewParterCode = findViewById(R.id.textViewParterCode);
        if (Util.isOfflineMode(context)) {
            textViewParterCode.setVisibility(View.GONE);
        }
        country_flag = findViewById(R.id.country_flag);
        linearMobile = findViewById(R.id.linearMobile);
        textViewCountry = findViewById(R.id.textViewCountry);
        textViewAlreadtAccount_ = findViewById(R.id.textViewAlreadtAccount_);
        textViewAlreadtAccount = findViewById(R.id.textViewAlreadtAccount);
        buttonSignip = findViewById(R.id.buttonSignip);
        buttonAnnonymous = findViewById(R.id.buttonAnnonymous);
        buttonSignInStudent = findViewById(R.id.buttonSignInStudent);
        textInfo = findViewById(R.id.textInfo);
        textInfo1 = findViewById(R.id.textInfo1);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        textView = findViewById(R.id.textView);
        textViewOr = findViewById(R.id.textViewOr);
        buttonFacebook = findViewById(R.id.buttonFacebook);
        mPhoneNumberField = findViewById(R.id.mobileEditText);
        linearLayout = findViewById(R.id.linearLayout);
        resendButton = findViewById(R.id.resendButton);
        inputMobile = findViewById(R.id.inputMobile);
        inputOtp = findViewById(R.id.inputOtp);
        reletiveSignUp = findViewById(R.id.reletiveSignUp);
        reletiveOtp = findViewById(R.id.reletiveOtp);
        textViewConfirm = findViewById(R.id.textViewConfirm);
        textViewChangeMobileNumber = findViewById(R.id.textViewChangeMobileNumber);
        nameStudent = findViewById(R.id.nameStudent);
        rollnoStudent = findViewById(R.id.rollnoStudent);
        mobilenoStudent = findViewById(R.id.mobilenoStudent);
        languageTextView = findViewById(R.id.languageTextView);
        languageSwitch = findViewById(R.id.languageSwitch);
        loginwithSRNText = findViewById(R.id.loginwithSRNText);
        selectProfileText = findViewById(R.id.selectProfileText);


        flow = getIntent().getStringExtra("flow");
        try {
            //   userId = Util.getUserId(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();


        Util.setclassChange(context,true);

        double screenSize = 0;

        try {
            screenSize = Util.getScreenSizeInInches((Activity) context);
        } catch (Exception e) {
            e.printStackTrace();
            screenSize = 6.0;
        }
        if (screenSize == 10.0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2 + 50, ViewGroup.LayoutParams.WRAP_CONTENT);
            reletiveSignUp.setLayoutParams(params);
        } else if (screenSize > 7.0) {
        }

        language = Util.getSelectedLanguage(context);
        if (Util.getSelectedLanguage(context).equals("english")) {
            languageSwitch.setChecked(false);
        } else {
            languageSwitch.setChecked(true);
        }

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) language = "hindi";
                else language = "english";
                if (language != null) {

                    if (Util.getSelectedLanguage(context) != language) {
                        Util.setLanguagePackageSelection(context, language);
                        Util.setLanguageSelection(context, language);
                        Util.showAnimatedDialog(context, Util.LANGUAGE_UPDATED);
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
        findViewById(R.id.logo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.versiondetails_layout);
                dialog.setCancelable(true);
                TextView updated_date_text = dialog.findViewById(R.id.updated_date_text);
                TextView app_version_text = dialog.findViewById(R.id.app_version_text);
                Button open_launcher_btn = dialog.findViewById(R.id.open_launcher_btn);
                ImageView close_version_ly_btn = dialog.findViewById(R.id.close_version_ly_btn);

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

                                if (!editTextPassword.getText().toString().equals(getResources().getString(R.string.Pass))) {
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

        loginwithSRNText = findViewById(R.id.loginwithAPPID);

        if (Util.getSelectedLanguage(context).equals("hindi")) {
            loginwithSRNText.setText("SRN के साथ लॉग इन करें");
            selectProfileText.setText("पहले से ही अकाउंट है?");
        }
        else {
            loginwithSRNText.setText("Login with SRN");
            selectProfileText.setText("Already have account ?");
        }

        loginwithSRNText.setVisibility(View.GONE);
        loginwithSRNText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PalSRNLogin.class));
                finish();
            }
        });

        selectProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudentProfiles();
            }
        });
    }

    private void showStudentProfiles() {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.student_profiles);
        dialog.setCancelable(false);

        RecyclerView recyclerview=dialog.findViewById(R.id.recyclerview);
        ImageView crossBtn=dialog.findViewById(R.id.crossBtn);
        TextView noStudentText=dialog.findViewById(R.id.noStudentText);

        ArrayList<StudentInfoModel> studentInfoModels=UserActivities.getUserInfo();

        if(studentInfoModels.size()==0) noStudentText.setVisibility(View.VISIBLE);
        else {
            noStudentText.setVisibility(View.GONE);

            Collections.reverse(studentInfoModels);

            StudentProfileAdapter studentProfileAdapter = new StudentProfileAdapter(studentInfoModels);

            studentProfileAdapter.SetOnItemClickListener(new StudentProfileAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    nameStudent.setText(studentInfoModels.get(position).getUserName());
                    rollnoStudent.setText(studentInfoModels.get(position).getUserRollNo());
                    mobilenoStudent.setText(studentInfoModels.get(position).getUserMobile());

                    buttonSignInStudent.performClick();
                }
            });

            recyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
            recyclerview.setAdapter(studentProfileAdapter);
        }

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    private void listners() {
        try {
            findViewById(R.id.buttonSignInStudent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLastNetConnection();
                    try {
                        String studentName = nameStudent.getText().toString().trim();
                        String studentPassword = rollnoStudent.getText().toString();
                        String studentMobile = mobilenoStudent.getText().toString();

                        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");

                        boolean isOnlyNumber;

                        try {
                            int a = Integer.parseInt(studentName);
                            isOnlyNumber=true;
                        }catch (Exception e ){
                            isOnlyNumber=false;
                        }

                        if (regex.matcher(studentName).find() || isOnlyNumber) {

                            String nameError="Invalid Name";

                            if(Util.getSelectedLanguage(context).equals("hindi")) nameError="अमान्य नाम";


                            nameStudent.requestFocus();
                            nameStudent.setError(nameError);
                            return;
                        }

                        if (studentName.contains("/") || studentName.contains("<") || studentName.contains(">")) {
                            nameStudent.requestFocus();
                            nameStudent.setError(nameError);
                            openGifDialogue(nameDialogueText);
                            return;
                        }

                        if(studentName.contains(".") || studentName.contains("#") || studentName.contains("$") || studentName.contains("[") ||studentName.contains("]") ) {
                            nameStudent.requestFocus();
                            nameStudent.setError(nameError);
                            openGifDialogue(nameDialogueText);
                            return;
                        }

                        if(studentPassword.contains(".") || studentPassword.contains("#") || studentPassword.contains("$") || studentPassword.contains("[") ||studentPassword.contains("]") ) {
                            rollnoStudent.requestFocus();
                            openGifDialogue(rollNoDialogueText);
                            rollnoStudent.setError(rollNoError);
                            return;
                        }

                        if(studentMobile.contains(".") || studentMobile.contains("#") || studentMobile.contains("$") || studentMobile.contains("[") ||studentMobile.contains("]") ) {
                            mobilenoStudent.requestFocus();
                            mobilenoStudent.setError(phoneNoerror);
                            openGifDialogue(mobileDialogueText);
                            return;
                        }

                        if (Util.getSelectedLanguage(context).equals("hindi")) {
                            rollZeroError = "रोल नंबर शून्य नहीं हो सकता";
                            mobZeroError = "मोबाइल नंबर शून्य नहीं हो सकता";
                        } else {
                            rollZeroError = "Roll number can't be zero";
                            mobZeroError = "Mobile number can't be zero";
                        }

                        try {
                            if (Util.checkZero(Integer.parseInt(studentPassword))) {
                                Toast.makeText(context, rollZeroError, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (Util.checkZero(Integer.parseInt(studentMobile))) {
                                Toast.makeText(context, mobZeroError, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                        Util.setUserMobile(context, studentMobile);
                        studentName = (studentName.replace(" ", "_")).toLowerCase();
                        Util.setUsername(context, studentName);
                        if (studentName.length() < 3) {
                            nameStudent.requestFocus();
                            nameStudent.setError(nameError);
                            openGifDialogue(nameDialogueText);
                            return;

                        } else if (studentPassword.length() == 0) {
                            rollnoStudent.requestFocus();
                            openGifDialogue(rollNoDialogueText);
                            rollnoStudent.setError(rollNoError);
                            return;
                        } else if (studentMobile.length() != 10) {
                            mobilenoStudent.requestFocus();
                            mobilenoStudent.setError(phoneNoerror);
                            openGifDialogue(mobileDialogueText);
                            return;
                        }

                        rollNo_student = studentPassword;
                        Util.setUserRoll(context, studentPassword);
                        if (!Util.isOfflineMode(context)) {
                            if (!Util.isNetworkAvailable(context)) {
                                String ngoID = Util.getNGOID(context);
                                String userID = ngoID + "_" + studentName + "_" + studentMobile;
                                Util.setUserId(context, userID);
                                List<StudentDetailsModel> list = studentDetailsRepository.getDetail(userID, ngoID);
                                if (list != null && list.size() > 0) {
                                    Util.setUsername(context, studentName);
                                    Util.setUserMobile(context, studentMobile);
                                    Util.setUserRoll(context, studentPassword);
                                    Util.setBoardSelectionDummy(context, list.get(0).getBoard());
                                    Util.setBoardSelection(context, list.get(0).getBoard());
                                    Util.setBoardNameSelectionDummy(context, list.get(0).getBoard());
                                    Util.setBoardNameSelection(context, list.get(0).getBoard());
                                    Util.setClassSelection(context, list.get(0).getSClass());
                                    Util.setClassSelectionDummy(context, list.get(0).getSClass());
                                    Util.setClassNameSelectionDummy(context, list.get(0).getSClass());
                                    Util.setClassNameSelection(context, list.get(0).getSClass());
                                    Util.setLanguageSelection(context, list.get(0).getLanguage());
                                    Util.setLanguagePackageSelection(context, list.get(0).getLanguage());

                                    Util.setContentonboadingMode(context, true);
                                    Util.setHomeboadingMode(context, true);

                                    Util.setDiagnosticboadingMode(context, true);
                                    Util.setQuizOnboardingMode(context, true);

                                    UserActivities.syncDataFromFirebase();


                                    startActivity(new Intent(context, PracticeTopicActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    System.out.println("============ hiding 1");
                                } else {
                                    if (Util.getIsFirstTime(context).equalsIgnoreCase("true")) {
                                        afterRegisterProcess(userID, studentName, studentMobile);

                                        System.out.println("============ hiding 2");
                                    } else {
                                        UserActivities.resetPref();
                                        Util.setSRNUser(context, false);
                                        startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        clearFields();
                                        finish();
                                    }
                                }
                            } else {
                                if (Util.getIsFirstTime(context).equals("true"))
                                    getUserDetails(studentName, studentMobile);
                                else getUserDetails(studentName, studentMobile);
                                System.out.println("============ hiding 4");
                            }
                        } else {
                            String ngoID = Util.getNGOID(context);
//                        String userID = ngoID + "_" + studentName + "_" + studentMobile;
                            String userID = ngoID + "_" + (Util.getUsername(context).replace(" ", "_")).toLowerCase() + "_" + studentMobile;

                            Util.setUserId(context, userID);

                            if (studentDetailsRepository.isDataExist(userID, ngoID)) {
                                String finalStudentName = studentName;
//                            getUserInfo(userID,ngoID)
                                Observable.fromCallable(() -> {
                                            //do something, get your Data object

//                                List<StudentDetailsModel> list =  (List<StudentDetailsModel>)  studentDetailsRepository.getDetail(userID, ngoID);;

                                            return (Object) studentDetailsRepository.getDetail(userID, ngoID);
                                        }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new io.reactivex.Observer<Object>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                studentDetailsDisposable = d;
                                            }

                                            @Override
                                            public void onNext(Object o) {

                                                List<StudentDetailsModel> list = (List<StudentDetailsModel>) o;

                                                if (list != null && list.size() > 0) {
                                                    Util.setUsername(context, finalStudentName.replace(" ", "_"));
                                                    Util.setUserMobile(context, studentMobile);
                                                    Util.setUserRoll(context, studentPassword);
                                                    Util.setBoardSelectionDummy(context, list.get(0).getBoard());
                                                    Util.setBoardSelection(context, list.get(0).getBoard());
                                                    Util.setBoardNameSelectionDummy(context, list.get(0).getBoard());
                                                    Util.setBoardNameSelection(context, list.get(0).getBoard());
                                                    Util.setClassSelection(context, list.get(0).getSClass());
                                                    Util.setClassSelectionDummy(context, list.get(0).getSClass());
                                                    Util.setClassNameSelectionDummy(context, list.get(0).getSClass());
                                                    Util.setClassNameSelection(context, list.get(0).getSClass());
                                                    Util.setLanguageSelection(context, list.get(0).getLanguage());
                                                    Util.setLanguagePackageSelection(context, list.get(0).getLanguage());

                                                    Util.setContentonboadingMode(context, true);
                                                    Util.setHomeboadingMode(context, true);

                                                    Util.setDiagnosticboadingMode(context, true);
                                                    Util.setQuizOnboardingMode(context, true);

                                                    UserActivities.syncDataFromFirebase();
                                                    startActivity(new Intent(context, PracticeTopicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                                    System.out.println("============ hiding 5");
                                                } else {
                                                    if (Util.getIsFirstTime(context).equalsIgnoreCase("true")) {
                                                        afterRegisterProcess(userID, finalStudentName, studentMobile);
                                                    } else {
                                                        UserActivities.resetPref();
                                                        Util.setSRNUser(context, false);
                                                        startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        clearFields();
                                                        finish();
                                                    }

                                                    System.out.println("============ hiding 6");
                                                }

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                studentDetailsDisposable.dispose();
                                            }
                                        });
                            } else {
                                if (Util.getIsFirstTime(context).equalsIgnoreCase("true")) {
                                    afterRegisterProcess(userID, studentName, studentMobile);
                                } else {
                                    UserActivities.resetPref();
                                    Util.setSRNUser(context, false);
                                    startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    clearFields();
                                    finish();
                                }
                            }


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Please Try again", Toast.LENGTH_SHORT).show();

                        System.out.println("============ hiding 7");
                    }
                }
            });

            findViewById(R.id.textViewParterCode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNGOCodeDialog();
                }
            });
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            String modal = Util.getDeviceName().toLowerCase();
            if (!modal.contains("huawei") && mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.

        Util.showToast(context, googlePlayError);
    }

    private Disposable studentDetailsDisposable;

    private void clearFields() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    nameStudent.setText("");
                    rollnoStudent.setText("");
                    mobilenoStudent.setText("");
                } catch (Exception r) {
                }
            }
        }, 500);
    }

    private Observable<Object> getUserInfo(String userId, String ngoID) {
        Observable.fromCallable(() -> {
            //do something, get your Data object
            return (Object) studentDetailsRepository.getDetail(userId, ngoID);
        });
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (reletiveOtp.getVisibility() == View.VISIBLE) {
                try {
                    countDownTimer.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isVerified = false;
                reletiveSignUp.setVisibility(View.VISIBLE);
                reletiveOtp.setVisibility(View.GONE);
            } else if (reletiveSignUp.getVisibility() == View.VISIBLE) {
                exitDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 
    }

    private void exitDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_main_exit);
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(exitMessage);
        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setText(yes);
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                try {
                    dialog.dismiss();
                    mGoogleApiClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();

            }
        });
        TextView textViewNo = dialog.findViewById(R.id.textViewNo);
        textViewNo.setText(no);
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private String errorMessage;

    private void afterRegisterProcess(String UId, String name, String mobile) {
        isRegisterTraversed = true;
        global.setUserType("Student");
        RegisterModel registerModel = new RegisterModel();
        registerModel.setPackageLanguage(Util.getSelectedLanguage(context));
        registerModel.setBoardID(Util.getSelectedBoard(context));
        registerModel.setNgoID(Util.getNGOID(context));
        registerModel.setAppID(Util.getAPPID(context));
        registerModel.setClassID(Util.getSelectedClass(context));
        registerModel.setStudentClass(Util.getSelectedClass(context));
        registerModel.setLanguage(Util.getSelectedLanguage(context));
        registerModel.setEducationBoard(Util.getSelectedBoard(context));
        registerModel.setMobile(mobile);
        registerModel.setRollNo(rollNo_student);
        registerModel.setAge("");
        registerModel.setFullName(name.replace(" ", "_"));

        String token = Util.getToken(context);
        registerModel.setToken(token);
        if (ApplicationConstants.isSDCardPresent) {
            registerModel.setIsSDCARDAvailable("True");
            registerModel.setIsLicenseVerificationCompleted("False");
        } else registerModel.setIsSDCARDAvailable("False");

        registerModel.setUserType(userType);
        registerModel.setDateStarted(Calendar.getInstance().getTimeInMillis() + "");
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(UId).setValue(registerModel);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).setValue(registerModel);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolId").setValue(Util.getSchoolId(context));
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolName").setValue(Util.getSchoolName(context));

        Util.setUserId(context, UId);
        Util.setUserMobile(context, mobile);
        Util.setUsername(context, name);
        Util.setBoardNameSelection(context, "cbse");
        Util.setBoardNameSelectionDummy(context, "cbse");
        Util.setBoardSelection(context, "cbse");
        Util.setBoardSelectionDummy(context, "cbse");

        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setPackageLanguage(Util.getSelectedLanguage(context));
        userInfoModel.setBoardID(Util.getSelectedBoard(context));
        userInfoModel.setNgoID(Util.getNGOID(context));
        userInfoModel.setAppID(Util.getAPPID(context));
        userInfoModel.setClassID(Util.getSelectedClass(context));
        userInfoModel.setStudentClass(Util.getSelectedClass(context));
        userInfoModel.setLanguage(Util.getSelectedLanguage(context));
        userInfoModel.setEducationBoard(Util.getSelectedBoard(context));
        userInfoModel.setMobile(mobile);
        userInfoModel.setRollNo(rollNo_student);
        userInfoModel.setFullName(Util.getUsernameShowable(context));
        userInfoModel.setAge("");
        userInfoModel.setFullName(name.replace(" ", "_"));

        FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(UId).set(userInfoModel);

//        StudentModel studentModel=new StudentModel();
//        studentModel.setIsActive("true");
//        studentModel.setAcademicYear("not Available");
//        studentModel.setAllocatedSubjects("not Available");
//        studentModel.setGender("not Available");
//        studentModel.setDateofAdmission("not Available");
//        studentModel.setDateOfBirth("not Available");
//        studentModel.setIdreamEmail("not Available");
//        studentModel.setLastLoginTime("true");
//        studentModel.setsClass("not Available");
//        studentModel.setSRN("not Available");
//        studentModel.setSchoolBlockName("");
//        studentModel.setSchoolCode("true");
//        studentModel.setSchoolDistrictName("true");
//        studentModel.setSchoolName("true");
//        studentModel.setSection("true");
//        studentModel.setStream("true");
//        studentModel.setStudentName("true");
//        studentModel.setUDISECode("true");
//        studentModel.setAuthTime("true");
//        studentModel.setStudentName("true");

//        addUserInfoInFirestore(studentModel);
        Util.setIntroDialog(context, true);
        UserActivities.resetPref();
        Util.setSRNUser(context, false);
        startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        clearFields();
        finish();
    }

    private String userID;

    private void moveToHomePage(String UId) {
        try {
            userID = UId;
            String token = Util.getToken(context);
            global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(UId).child("token").setValue(token);
            global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(UId).child("token").setValue(token);
            global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(UId).child("schoolId").setValue(Util.getSchoolId(context));
            global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(UId).child("schoolName").setValue(Util.getSchoolName(context));
            global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("token").setValue(token);
            global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolId").setValue(Util.getSchoolId(context));
            global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolName").setValue(Util.getSchoolName(context));
            Util.setNameAge(context, yes);

            startTrialPlan();
            syncAndroidId();

            Util.setIntroDialog(context, true);
            UserActivities.resetPref();
            Util.setSRNUser(context, false);
            startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            clearFields();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        global.setPreferenceChange(false);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalAnonymousLoginActivity that = (PalAnonymousLoginActivity) o;

        return referalArrayList.equals(that.referalArrayList);
    }

    @Override
    public int hashCode() {
        return referalArrayList.hashCode();
    }

    /* This call is only  to save the plan for topic screen*/
    private void startTrialPlan() {
        Date currentTime = Calendar.getInstance().getTime();
        CurrentPlanModel currentPlanModel = new CurrentPlanModel();
        currentPlanModel.setDateStarted(currentTime.toString());
        currentPlanModel.setPlanDuration("180");
        currentPlanModel.setStatus("180 days plan");
        HashMap<String, Object> currentPlanHashmap = new HashMap<>();
        currentPlanHashmap.put("dateStarted", currentTime.toString());
        currentPlanHashmap.put("status", "180 days plan");
        currentPlanHashmap.put("planDuration", "180");
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(userID).child("UsersPlans").updateChildren(currentPlanHashmap);
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(userID).child("UsersPlans").updateChildren(currentPlanHashmap);
        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child("UsersPlans").child(userID).setValue(currentPlanModel);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(userID).child("UsersPlans").updateChildren(currentPlanHashmap);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(userID).child("UsersPlans").updateChildren(currentPlanHashmap);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(userID).setValue(currentPlanModel);
        ApplicationConstants.STATUS = "PLAN";
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {

                JSONObject object;

                if (Util.getSelectedLanguage(context) != null)
                    object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                else object = jsonObject.getJSONObject("english");


                JSONArray array = object.getJSONArray("loginScreen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textViewtitle.setText(textArrayList.get(0));
                    nameStudent.setHint(textArrayList.get(1));
                    textViewtitleName.setText(textArrayList.get(2)+"*");
                    rollnoStudent.setHint(textArrayList.get(3));
                    textViewtitlePassword.setText(textArrayList.get(4));
                    buttonSignInStudent.setText(textArrayList.get(5));
                    errorMessage = textArrayList.get(6);
                    mobilenoStudent.setHint(textArrayList.get(7));
                    textViewtitlemobileNo.setText(textArrayList.get(8)+"*");
                    textViewtitleRollNo.setText(textArrayList.get(9)+"*");
                    nameError = textArrayList.get(10);
                    nameDialogueText = textArrayList.get(11);
                    rollNoDialogueText = textArrayList.get(12);
                    rollNoError = textArrayList.get(13);
                    phoneNoerror = textArrayList.get(14);
                    mobileDialogueText = textArrayList.get(15);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Util.getSelectedLanguage(context) != null) {
                try {
                    global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("loginScreen").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null) {
                                    ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                    if (textArrayList.size() > 0) {
                                        textViewtitle.setText(textArrayList.get(0));
                                        nameStudent.setHint(textArrayList.get(1));
                                        textViewtitleName.setText(textArrayList.get(2)+"*");
                                        rollnoStudent.setHint(textArrayList.get(3));
                                        textViewtitlePassword.setText(textArrayList.get(4));
                                        buttonSignInStudent.setText(textArrayList.get(5));
                                        errorMessage = textArrayList.get(6);
                                        mobilenoStudent.setHint(textArrayList.get(7));
                                        textViewtitlemobileNo.setText(textArrayList.get(8)+"*");
                                        textViewtitleRollNo.setText(textArrayList.get(9)+"*");
                                        nameError = textArrayList.get(10);
                                        nameDialogueText = textArrayList.get(11);
                                        rollNoDialogueText = textArrayList.get(12);
                                        rollNoError = textArrayList.get(13);
                                        phoneNoerror = textArrayList.get(14);
                                        mobileDialogueText = textArrayList.get(15);


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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child("english").child("loginScreen").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null) {
                                    ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                    if (textArrayList.size() > 0) {
                                        textViewtitle.setText(textArrayList.get(0));
                                        nameStudent.setHint(textArrayList.get(1));
                                        textViewtitleName.setText(textArrayList.get(2)+"*");
                                        rollnoStudent.setHint(textArrayList.get(3));
                                        textViewtitlePassword.setText(textArrayList.get(4));
                                        buttonSignInStudent.setText(textArrayList.get(5));
                                        errorMessage = textArrayList.get(6);
                                        mobilenoStudent.setHint(textArrayList.get(7));
                                        textViewtitlemobileNo.setText(textArrayList.get(8)+"*");
                                        textViewtitleRollNo.setText(textArrayList.get(9)+"*");
                                        nameError = textArrayList.get(10);
                                        nameDialogueText = textArrayList.get(11);
                                        rollNoDialogueText = textArrayList.get(12);
                                        rollNoError = textArrayList.get(13);
                                        phoneNoerror = textArrayList.get(14);
                                        mobileDialogueText = textArrayList.get(15);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setStaticTextlanguage() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("homeScreen");
                textArrayList2 = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList2.add(message);
                }

                if (textArrayList2.size() > 0) {

                    textOfChangeLanguageDialog = textArrayList2.get(7);
                    changeLanguageButton = textArrayList2.get(10);

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
                            textArrayList2 = (ArrayList<String>) dataSnapshot.getValue();
                            if (textArrayList2.size() > 0) {
                                textOfChangeLanguageDialog = textArrayList2.get(7);
                                changeLanguageButton = textArrayList2.get(10);
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

    private void syncAndroidId() {
        AndroidIdModel androidIdModel = new AndroidIdModel();
        androidIdModel.setAndroidId(Util.getAndroidId(context));
        if (Util.getUserId(context) != null) {
            androidIdModel.setUserId(userID);
        } else {
            androidIdModel.setUserId("");
        }
        androidIdModel.setLogin(false);
        DatabaseReference databaseReference = global.getDatabaseReference();//FirebaseDatabase.getInstance().getReference();
        databaseReference.child(ApplicationConstants.USERS).child("DeviceIds").child(Util.getAndroidId(context)).push().setValue(androidIdModel);
    }

    private void openNGOCodeDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_ngo_code);
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(enterPartnerCode);
        TextInputLayout input_fullName = dialog.findViewById(R.id.input_fullName);
        input_fullName.setHint(partnerCode);
        TextView textViewokay = dialog.findViewById(R.id.textViewokay);
        textViewokay.setText(proceed);
        EditText ngoCodeEditText = dialog.findViewById(R.id.ngoCodeEditText);
        textViewokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                String partnerCode = ngoCodeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(partnerCode)) {
                    ngoCodeEditText.setError(enterPartnerCode);
                } else {
                    //Validation is pending from
                    try {
                        validateCode(partnerCode, dialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.dismissDialog();
                        Util.showToast(context, invalidCode);
                    }
                }

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void validateCode(String referCode, Dialog dialog) throws Exception {
        Util.showDialog(context);
        global.getDatabaseReference().child("Referal_Ngo_Code").child(referCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Util.dismissDialog();
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                        String NgoId = map.get("ngoID");
                        String image1 = map.get("image1");
                        String image2 = map.get("image2");
                        ArrayList<String> ngoImages = new ArrayList<>();
                        ngoImages.add(image1);
                        ngoImages.add(image2);
                        dialog.dismiss();
                        Util.setNGOIMAGES(context, ngoImages);
                        Util.showToast(context, codeValidated);
                        Util.setNGOID(context, NgoId);
                    } else {
                        Util.showToast(context, invalidCode);
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

    //    private void saveNewStudentIntoFirebaseDatabaseAndLogin(final String studentName, String userPassword) {
//        // Create user
//        StudentInfo studentInfo = new StudentInfo(studentName, userPassword, studentName);
//        String ngoID = Util.getNGOID(context);
//        String usersDetailsPath = "offline_login_users/" + ngoID;
//        String userID = ngoID + "_" + studentName+"_"+Util.getUserMobile(context);
//
//        global.getDatabaseReference().child(usersDetailsPath).child(userID).setValue(studentInfo);
//    }
    private void getUserDetails(String studentName, String studentMobile) {

        String ngoID = Util.getNGOID(context);
        String userID = ngoID + "_" + studentName.replace(" ", "_") + "_" + studentMobile;

        // global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(Util.getAPPID(context)).child("userID").setValue(userID);

        global.getDatabaseReference().child("offline_login_users").child(ngoID).child("students").child(userID.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        String studentName = map.get("fullName");
                        String sClass = map.get("sClass");
                        String sBoard = map.get("educationBoard");
                        String sLanguage = map.get("sLanguage");
                        String phoneNo = map.get("mobile");
                        String appID = map.get("appID");
                        String boardID = map.get("boardID");
                        String dateStarted = map.get("dateStarted");
                        String schoolId = map.get("schoolId");
                        String rollNo = map.get("rollNo");

                        Util.setUsername(context, studentName.replace(" ", "_"));
                        Util.setBoardSelectionDummy(context, sBoard);
                        Util.setBoardSelection(context, sBoard);
                        Util.setBoardNameSelectionDummy(context, sBoard);
                        Util.setBoardNameSelection(context, sBoard);
                        Util.setClassSelection(context, sClass);
                        Util.setClassSelectionDummy(context, sClass);
                        Util.setClassNameSelectionDummy(context, sClass);
                        Util.setClassNameSelection(context, sClass);
                        Util.setLanguageSelection(context, sLanguage);
                        Util.setLanguagePackageSelection(context, sLanguage);
                        Util.setUserMobile(context, phoneNo);
                        Util.setExistingUser(context, true);
                        Util.setUserRoll(context, rollNo_student);

                        if (sClass == null) {
                            UserActivities.resetPref();
                            Util.setSRNUser(context, false);
                            startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            clearFields();
                            finish();
                        } else if (sClass.contains("11") || sClass.contains("12")) {
                            Util.setStreamClassSelection(context, sClass);
                        }

                        if (studentDetailsRepository.isDataExist(userID, ngoID)) {
                            studentDetailsRepository.updateField(userID, ngoID, studentName, studentName, studentName, sBoard, sClass, sLanguage);
                        } else {
                            StudentDetailsModel studentDetailsModel = new StudentDetailsModel();
                            studentDetailsModel.setNgoId(ngoID);
                            studentDetailsModel.setUserId(userID);
                            studentDetailsModel.setStudentName(studentName);
                            studentDetailsModel.setStudentPassword(studentName);
                            studentDetailsModel.setUserName(studentName);
                            studentDetailsModel.setBoard(Util.getSelectedBoard(context));
                            studentDetailsModel.setSClass(Util.getSelectedClass(context));
                            studentDetailsModel.setLanguage(Util.getSelectedLanguage(context));
                            studentDetailsRepository.insertStudentDetails(studentDetailsModel);

                        }
                        if (Util.isExistingUser(context)) {
                            //Show On Boarding Pop Up's
                            Util.setUserId(context, userID);
                            Util.setShowOnlySubjects(context, true);
                            Util.setIntroDialog(context, false);
                            Util.setToolTipHomeScreen(context, true);
                            Util.setToolTipContentScreen(context, true);
                            Util.setToolTipDiagnosticScreen(context, true);
                            Util.setExistingUser(context, false);

                            Util.setContentonboadingMode(context, true);
                            Util.setHomeboadingMode(context, true);

                            Util.setDiagnosticboadingMode(context, true);
                            Util.setQuizOnboardingMode(context, true);

                            Util.setBoardNameSelection(context, "cbse");
                            Util.setBoardNameSelectionDummy(context, "cbse");
                            Util.setBoardSelection(context, "cbse");
                            Util.setBoardSelectionDummy(context, "cbse");

                            if (sClass != null) {
                                UserActivities.syncDataFromFirebase();
                                Intent intent = new Intent(PalAnonymousLoginActivity.this, PracticeTopicActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                startActivity(new Intent(PalAnonymousLoginActivity.this, PracticeTopicActivity.class));


                            }
                        } else {
                            Util.setIntroDialog(context, true);
                            afterRegisterProcess(userID, studentName, studentMobile);
                        }

                    } else {

                        if (Util.isNetworkAvailable(context)) {
                            Util.setIntroDialog(context, true);
                            afterRegisterProcess(userID, studentName, studentMobile);
                        } else {
                            if (Util.getSelectedLanguage(context) != null) {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                    Util.openGifDialogue(context, "इंटरनेट कनेक्शन काम नहीं कर रहा");
                                } else {
                                    Util.openGifDialogue(context, "Internet Connection is not working");
                                }
                            } else {
                                Util.openGifDialogue(context, "Internet Connection is not working");
                            }
                        }

//                        if (Util.getIsFirstTime(context).equalsIgnoreCase("true")) {
//                            Util.setIntroDialog(context, true);
//                            afterRegisterProcess(userID, studentName, studentMobile);
//                        } else {
//                            Util.setIntroDialog(context, true);
//                            afterRegisterProcess(userID, studentName, studentMobile);
//                        }
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

    private void openGifDialogue(String textError) {

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
                Util.preventTwoClick(view);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    private String textOfChangeLanguageDialog, changeLanguageButton;
    private PalLanguageSelectionAdapter palLanguageSelectionAdapter;
    private String language;

    private void openLanguageSwitchDialog() {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
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
                JSONObject boardObject = classObect.getJSONObject("cbse");
                JSONArray languageArray = boardObject.getJSONArray("language");
                ArrayList<HashMap<String, String>> languageArrayList = new ArrayList<>();
                for (int i = 0; i < languageArray.length(); i++) {
                    JSONObject jsonObject = languageArray.getJSONObject(i);
                    HashMap<String, String> languageHashMap = new HashMap<>();
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    languageHashMap.put("name", name);
                    languageHashMap.put("id", id);
                    languageHashMap.put("selected", "false");
                    languageArrayList.add(languageHashMap);
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
                        Util.setLanguagePackageSelection(context, language);

                        if (language != null) {

                            if (Util.getSelectedLanguage(context) == language) {
                                dialog.dismiss();
                            } else {
                                Util.setLanguagePackageSelection(context, language);
                                Util.setLanguageSelection(context, language);

//                    Intent intent;
//                    intent = new Intent(context, PracticeTopicActivity.class);
//                    intent.putExtra("board", board);
//                    intent.putExtra("class", Util.getSelectedClass(context));
//                    startActivity(intent);
//                    recreate();
                                dialog.dismiss();
                                if (language.equalsIgnoreCase("hindi")) {
                                    openGifDialogueSuccess(context, "आपकी भाषा बदल दी गई है");

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

                                            startActivity(getIntent());

                                        }
                                    }, 3000);


                                } else {
                                    openGifDialogueSuccess(context, "Your Language has been updated");

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

                                            startActivity(getIntent());

                                        }
                                    }, 3000);
                                }

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

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    global.getDatabaseReference().child(ApplicationConstants.LANGUAGE).child("cbse").child("language").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    ArrayList<HashMap<String, String>> languageArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                                    for (int i = 0; i < languageArrayList.size(); i++) {
                                        if (languageArrayList.get(i).get("id").equalsIgnoreCase(Util.getSelectedLanguage(context))) {
                                            languageArrayList.get(i).put("selected", "true");
                                        } else {
                                            languageArrayList.get(i).put("selected", "false");
                                        }


                                    }

                                    palLanguageSelectionAdapter = new PalLanguageSelectionAdapter(context, languageArrayList);
                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PalAnonymousLoginActivity.this, getResources().getIdentifier("layout_animation_from_right", "anim", getPackageName()));
                                    recyclerView.setLayoutAnimation(animation);
                                    recyclerView.scheduleLayoutAnimation();
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

                                                if (Util.getSelectedLanguage(context) == language) {
                                                    dialog.dismiss();
                                                } else {
                                                    Util.setLanguagePackageSelection(context, language);
                                                    Util.setLanguageSelection(context, language);

//                    Intent intent;
//                    intent = new Intent(context, PracticeTopicActivity.class);
//                    intent.putExtra("board", board);
//                    intent.putExtra("class", Util.getSelectedClass(context));
//                    startActivity(intent);
//                    recreate();
                                                    dialog.dismiss();
//                                                    openGifDialogueSuccess(context,"आपकी भाषा बदल दी गई है");
                                                    Util.showAnimatedDialog(context, Util.LANGUAGE_UPDATED);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();

                                                            startActivity(getIntent());

                                                        }
                                                    }, 3000);
                                                }

                                            }

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
            }, 350);//time in milisecond

        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (language != null) {

                    if (Util.getSelectedLanguage(context) == language) {
                        dialog.dismiss();
                    } else {
                        Util.setLanguagePackageSelection(context, language);
                        Util.setLanguageSelection(context, language);

//                    Intent intent;
//                    intent = new Intent(context, PracticeTopicActivity.class);
//                    intent.putExtra("board", board);
//                    intent.putExtra("class", Util.getSelectedClass(context));
//                    startActivity(intent);
//                    recreate();
                        dialog.dismiss();
                        if (language.equalsIgnoreCase("hindi")) {
                            openGifDialogueSuccess(context, "आपकी भाषा बदल दी गई है");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();

                                    startActivity(getIntent());

                                }
                            }, 3000);


                        } else {
                            openGifDialogueSuccess(context, "Your Language has been updated");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();

                                    startActivity(getIntent());

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

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context, getTaskId());
    }

    private void updateLastNetConnection() {
        if(Util.checkInternetConnection(context)){
            //Updating the last internet connected  to show the 30 or more Day without internet dialog
            Util.setLastNetConnected(context);
        }
    }

//    /** add User info in Firestore
//     * @param StudentModel employe model to set/update in firestore
//     * @param updateDate set true if you want to update data else its set the data in firestore*/
//    private void addUserInfoInFirestore(StudentModel StudentModel) {
//        DocumentReference db=FirebaseFirestore.getInstance().collection(Util.getProjectID()+"_students").document(StudentModel.getUid());
//
//        OnSuccessListener onSuccessListener=new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object o) {
////                goToNextActivity(StudentModel);
//            }
//        };
//        OnFailureListener onFailureListener=new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//                /** if data is not available then add data in firestore */
////                if(e.getMessage().contains("No document to update")) addUserInfoInFirestore(StudentModel);
////                else dismissDialog();
//            }
//        };
//
//        db.set(StudentModel).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
//    }


}
