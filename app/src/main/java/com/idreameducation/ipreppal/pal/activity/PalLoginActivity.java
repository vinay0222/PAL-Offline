package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AndroidIdModel;
import com.idreameducation.ipreppal.model.CurrentPlanModel;
import com.idreameducation.ipreppal.model.ReferReversModel;
import com.idreameducation.ipreppal.model.RegisterModel;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalAnonymousLoginActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalLanguageSelectionActivity;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Country;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PalLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SigninActivity";
    private String message1;
    private String message2;
    private String message3;
    private EditText otpEditText;
    private final ArrayList<String> referalArrayList = new ArrayList<>();
    private Context context;
    private EditText mPhoneNumberField;
    private TextView textViewAlreadtAccount;
    private TextView textViewAlreadtAccount_;
    private ImageView buttonGoogle;
    private TextView buttonFacebook;
    private TextView buttonSignip;
    private TextView textInfo;
    private TextView textInfo1;
    private TextView textInfo2;
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
    private int count = 0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private TextInputLayout inputMobile;
    private TextInputLayout inputOtp;
    private LinearLayout linearLayout;
    private boolean isRegisterTraversed = false;
    private String mVerificationId;
    private String userType = "App";
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
    private String otpInfoText2;
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
    private String counrtyCode = "+91";
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
    private ShimmerFrameLayout shimmer_view_container;
    private LinearLayout linearToGONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_login);
        assignIds();
        listners();
    }

    // [END on_start_check_user]

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        global.sendData("Signup", this.getClass().getName());
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        // [START config_signin]
        // Configure Google Sign In
//        try {
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        textInfoOtp = findViewById(R.id.textInfoOtp);
//        textViewParterCode = findViewById(R.id.textViewParterCode);
//        if (Util.isOfflineMode(context)) {
//            textViewParterCode.setVisibility(View.GONE);
//        }
//        textViewParterCode.setVisibility(View.GONE);
        country_flag = findViewById(R.id.country_flag);
        linearMobile = findViewById(R.id.linearMobile);
//        if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2 + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.gravity = Gravity.CENTER;
//            linearMobile.setLayoutParams(params);
//        }
//        country_flag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Util.showCountryDialog(context, countryMessage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        textViewCountry = findViewById(R.id.textViewCountry);
//        textViewCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Util.showCountryDialog(context, countryMessage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        textViewAlreadtAccount_ = findViewById(R.id.textViewAlreadtAccount_);
        textViewAlreadtAccount = findViewById(R.id.textViewAlreadtAccount);
        buttonSignip = findViewById(R.id.buttonSignip);
        textInfo = findViewById(R.id.textInfo);
        textInfo1 = findViewById(R.id.textInfo1);
//        textInfo2 = findViewById(R.id.textInfo2);
        buttonGoogle = findViewById(R.id.buttonGoogle);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("");
        textView = findViewById(R.id.textView);
        textViewOr = findViewById(R.id.textViewOr);
        buttonFacebook = findViewById(R.id.buttonFacebook);
//        linearToGONE = findViewById(R.id.linearToGONE);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        mPhoneNumberField = findViewById(R.id.mobileEditText);
        linearLayout = findViewById(R.id.linearLayout);
        resendButton = findViewById(R.id.resendButton);
        inputMobile = findViewById(R.id.inputMobile);
        inputOtp = findViewById(R.id.inputOtp);
        reletiveSignUp = findViewById(R.id.reletiveSignUp);
        reletiveOtp = findViewById(R.id.reletiveOtp);
        textViewConfirm = findViewById(R.id.textViewConfirm);
        textViewChangeMobileNumber = findViewById(R.id.textViewChangeMobileNumber);
        flow = getIntent().getStringExtra("flow");
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                    Util.openGifDialogue(context,googlePlayError);
//                    Util.showToast(context, googlePlayError);
                }
            }

            @Override
            public void onCancel() {
                // [START_EXCLUDE]
                Log.i("Error: ", "Errpr");
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                // [START_EXCLUDE]
                Log.i("Error: ", error.toString());

                // [END_EXCLUDE]
            }
        });


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                // [END_EXCLUDE]
                Util.dismissDialog();

                try {
                    otpEditText.setText(credential.getSmsCode());
                    textViewConfirm.setEnabled(false);
                    if (isVerified) {
                        countDownTimer.cancel();
                        resendButton.setText("");
                        textViewChangeMobileNumber.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                }
                try {
                    if (isVerified) {
                        Util.showDialog(context);
                        signInWithPhoneAuthCredential(credential);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Util.dismissDialog();
                e.printStackTrace();
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]
                if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Util.showToast(context, manyTimeUsage);
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseNetworkException) {
                    try {
                        Util.showToast(context, Util.getCommonMessages(context).get(6));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try{
                        Util.showToast(context, e.getLocalizedMessage());
                        mPhoneNumberField.setError(e.getLocalizedMessage());
                    }catch (Exception e1){
                        Util.showToast(context, "play service issue");
                    }

                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                try {

                    showOtpDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // [END phone_auth_callbacks]
        setStaticText();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            try {
                userType = Util.getUserType(context);
                getUserProfile(currentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int mHeight = this.getResources().getDisplayMetrics().heightPixels;
        //linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (mHeight / 2) - 50));

    }

    private void listners() {
        findViewById(R.id.resendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Util.showDialog(context);
                    resendVerificationCode(mPhoneNumberField.getText().toString().trim(), mResendToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.buttonSignip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Util.isNetworkAvailable(context)) {
//                        Util.showDialog(context);
//                        startAnonymousSignIn();
                        if (!validatePhoneNumber()) {
                            return;
                        }
                        userType = "App";
                        Util.setUserType(context, userType);
                        try {
                            isVerified = true;
                            Util.showDialog(context);
                            Util.setUserMobile(context, mPhoneNumberField.getText().toString());
                            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.dismissDialog();
                            Util.showToast(context, googlePlayError);
                        }
                    } else {
                        try {
                            Util.showToast(context, Util.getCommonMessages(context).get(6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.buttonFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Util.checkInternetConnection(context)) {
                        facebookLogin();
                    } else {
                        try {
                            Util.showToast(context, Util.getCommonMessages(context).get(6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.buttonGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Util.checkInternetConnection(context)) {
                        try {
                            signInWithGoogle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Util.showToast(context, Util.getCommonMessages(context).get(6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            String modal = Util.getDeviceName().toLowerCase();
            if (!modal.contains("huawei")) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                try {
                    firebaseAuthWithGoogle(account);
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                    Util.showToast(context, googlePlayError);
                }
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }

    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) throws Exception {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        Util.showDialog(context);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            userType = "Facebook";
                            Util.setUserType(context, userType);
                            updateUI(user);
                        } else {


                            if (task.getException() instanceof FirebaseNetworkException) {
                                try {
                                    Util.showToast(context, Util.getCommonMessages(context).get(6));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Util.showToast(context, task.getException().getMessage());
                            }

                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) throws Exception {
        global.setGoogleAccount(acct);
        Util.setUserEmail(context, acct.getEmail());
        Util.showDialog(context);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            userType = "Google";
                            Util.setUserType(context, userType);
                            updateUI(user);
                        } else {

                            if (task.getException() instanceof FirebaseNetworkException) {
                                try {
                                    Util.showToast(context, Util.getCommonMessages(context).get(6));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Util.showToast(context, task.getException().getMessage());
                            }
                            updateUI(null);
                        }

                    }
                });
    }

    // [START signin]
    private void signInWithGoogle() throws Exception {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (mGoogleApiClient != null)
//            mGoogleApiClient.connect();
    }

    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(PalLoginActivity.this, Arrays.asList("email"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateUI(FirebaseUser user) {
        // Util.dismissDialog();
        if (user != null) {
            String userName;
            if (!userType.equalsIgnoreCase("App")) {
                userName = user.getDisplayName();
                if (userName == null) {
                    userName = "User";
                }
                Util.setUsername(context, userName);
            }
            Util.setUserId(context, user.getUid());
            Util.setDummyUserId(context, user.getUid());

            try {
                getUserProfile(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.

        Util.showToast(context, googlePlayError);
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
                dialog.dismiss();
                try {
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
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }


    private void startPhoneNumberVerification(String phoneNumber) throws Exception {

        phoneNumber = counrtyCode + phoneNumber;
        /* show loader progress */
        //  Util.showDialog(context);
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
    }

    private void afterRegisterProcess(String UId, String name, String age, String mobile, String userType_) {
//        try {
//           // Util.setVideoWatched(context, false);
//            StudentReferalManager studentReferalManager = new StudentReferalManager((FragmentActivity) context);
//            studentReferalManager.checkForInvites();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        isRegisterTraversed = true;
        global.setUserType("Student");
        RegisterModel registerModel = new RegisterModel();
        registerModel.setPackageLanguage(Util.getSelectedLanguagePackage(context));
        registerModel.setBoardID(Util.getSelectedBoard(context));
        registerModel.setNgoID(Util.getNGOID(context));
        registerModel.setClassID(Util.getSelectedClass(context));
        registerModel.setStudentClass(Util.getSelectedClassName(context));
        registerModel.setLanguage(Util.getSelectedLanguage(context));
        registerModel.setEducationBoard(Util.getSelectedBoardName(context));
        registerModel.setMobile(counrtyCode + "-" + mobile);
        registerModel.setAge(age);
        registerModel.setFullName(name);
        Util.setUsername(context,name);
        if (ApplicationConstants.isSDCardPresent) {
            registerModel.setIsSDCARDAvailable("True");
            registerModel.setIsLicenseVerificationCompleted("False");
        } else {
            registerModel.setIsSDCARDAvailable("False");
        }
        registerModel.setUserType(userType);
        registerModel.setDateStarted(Calendar.getInstance().getTimeInMillis() + "");

        Util.setAPPID(context,"APP1");
        Util.setNGOID(context,"1638268176fcd7dc64212b");
        Util.setUserId(context,UId);

        global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(UId).setValue(registerModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Util.dismissDialog();
                if (databaseError != null) {
                    try {
                        Util.showToast(context, databaseError.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(UId).child("City").setValue(Util.getSelectedCity(context));
                    global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(UId).child("State").setValue(Util.getSelectedState(context));
                    Util.dismissDialog();
                    moveToHomePage(userType_);
                }
            }
        });
    }

    private void moveToHomePage(String userType_) {
        try {
            String token = Util.getToken(context);
            global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(Util.getUserId(getApplicationContext())).child("token").setValue(token);
            Util.setNameAge(context, yes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        global.setPreferenceChange(false);
        if (userType_.equalsIgnoreCase("new")) {
            startTrialPlan();
            // syncAndroidId();
            sendWelcomMessage(message1, 1, "1");
            startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            //startActivity(new Intent(context, PreferenceSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
//            startActivity(new Intent(context, PracticeTopicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            startActivity(new Intent(context, PalLanguageSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
        finish();
        
    }

    private void sendWelcomMessage(String message, int id, String welcome) {
//        String CHANNEL_ID = "iPrep_channel_01";
//        CharSequence name = "iPrep";
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        Notification notification;
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mNotificationManager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(PalLoginActivity.this)
//                    .setContentTitle("Welcome to iPrep Pal Application")
//                    .setContentText(message)
//                    .setSmallIcon(R.mipmap.notification)
//                    .setChannelId(CHANNEL_ID)
//                    .build();
//        } else {
//            notification = new Notification.Builder(PalLoginActivity.this)
//                    .setContentTitle("iPrep")
//                    .setContentText(message)
//                    .setSmallIcon(R.mipmap.notification)
//                    .build();
//        }
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        // Issue the notification.
//        Intent intent = new Intent(this, NotificationActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        notification.contentIntent = pendingIntent;
//        Util.setNotificationType(context, "App");
//        mNotificationManager.notify(id, notification);
//        Util.setNotificationCount(context, Util.getNotificationCount(context) + 1);
//        Map<String, String> notificationMap = new HashMap<>();
//        notificationMap.put("message", "Hello");
//        notificationMap.put("notificationMessage", "Message from iPrep");
//        notificationMap.put("replyable", "Not Showable");
//        notificationMap.put("sender", "iPrep");
//        notificationMap.put("messageTime", Util.getCurrentDateWithDifferentFormat());
//        notificationMap.put("senderId", "iPrep");
//        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("Student").child(Util.getUserId(context)).child("welcome" + welcome).setValue(notificationMap);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        phoneNumber = counrtyCode + phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    // [END resend_verification]
    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Util.dismissDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                            updateUI(user);
                        } else {

                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseNetworkException) {
                                try {
                                    Util.showToast(context, Util.getCommonMessages(context).get(6));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Util.showToast(context, incorrectOtp);
                            }
                        }
                    }
                });
    }

    // [END sign_in_with_phone]
    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError(emptyMobile);
            return false;
        }
        if (phoneNumber.length() != 10) {
            mPhoneNumberField.setError(invalidMobile);
            return false;
        }
        return true;
    }

    /* verify Otp Dialog */
    private void showOtpDialog() throws Exception {
        /* dismiss loader progress*/
//        Util.dismissDialog();
        reletiveOtp.setVisibility(View.VISIBLE);
        textInfoOtp.setText("Enter Otp");

        TextView text = findViewById(R.id.text);
        text.setText("Verification OTP sent to "+mPhoneNumberField.getText().toString());

        //mPhoneNumberField.getText().toString()

        reletiveSignUp.setVisibility(View.GONE);
        

        resendButton.setPaintFlags(resendButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        resendButton.setText(resend.trim());
        try {
            Util.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startTimer(resendButton);
        otpEditText = findViewById(R.id.otpEditText);
        otpEditText.setText("");

        textViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(otp)) {
                    Util.showDialog(context);
                    verifyPhoneNumberWithCode(mVerificationId, otp);
                } else {
                    Util.showToast(context, emptyOtp);
                }
            }
        });


        textViewChangeMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otpEditText.setText("");
                isVerified = false;
                countDownTimer.cancel();
                reletiveOtp.setVisibility(View.GONE);
                reletiveSignUp.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getUserProfile(final FirebaseUser user) throws Exception {
        String userId = user.getUid();
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Util.dismissDialog();
                    if (dataSnapshot.getValue() == null) {

                        generateReferalCode();
                        if (flow.equalsIgnoreCase("yes")) {
                            if (userType.equalsIgnoreCase("App")) {
                                openNameAgeDialog(user, "new");
                            } else {
                                openMobileNumberDialog(user, "new");
                            }
                        } else {
                            openNameAgeDialog(user, "new");
                        }
                    } else {
//                        Util.setVideoWatched(context, false);
                        if (flow.equalsIgnoreCase("yes")) {
                            if (userType.equalsIgnoreCase("App")) {
                                openNameAgeDialog(user, "old");
                            } else {
                                openMobileNumberDialog(user, "old");

                            }
                        } else {
                            HashMap<String, String> languagePackageHashMap;
                            languagePackageHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            if (!isRegisterTraversed) {
                                String language = languagePackageHashMap.get("packageLanguage");
                                String educationBoardID = languagePackageHashMap.get("boardID");
                                String studentClassID = languagePackageHashMap.get("classID");
                                String studentClass = languagePackageHashMap.get("studentClass");
                                String educationBoard = languagePackageHashMap.get("educationBoard");
                                String token = languagePackageHashMap.get("token");
                                String city = languagePackageHashMap.get("City");
                                String state = languagePackageHashMap.get("State");
                                String fullName = languagePackageHashMap.get("fullName");
                                String profileLink = languagePackageHashMap.get("profileImage");
                                Util.setUserProfile(context,profileLink);
                                Util.setUsername(context, fullName);
                                Util.setNameAge(context, "YES");
                                try {
                                    Util.setStateSelection(context, state);
                                    Util.setCitySelection(context, city);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (language != null) {
                                    if (!language.equalsIgnoreCase(Util.getSelectedLanguagePackage(context))) {
                                        showLanguageChangeDialog(languagePackageHashMap);
                                    } else {
                                        Util.setLanguagePackageSelection(context, language);
                                        Util.setLanguageSelection(context, language);
                                        Util.setClassNameSelection(context, studentClass);
                                        Util.setClassSelection(context, studentClassID);
                                        Util.setBoardSelection(context, educationBoardID);
                                        Util.setBoardNameSelection(context, educationBoard);
                                        Util.setAllsetValue(context, studentClass != null);
                                        checkDeviceChange(language);
                                    }
                                } else {
                                    action(language);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(userId).addListenerForSingleValueEvent(valueEventListener);

    }

    private void showLanguageChangeDialog(HashMap<String, String> languagePackageHashMap) throws Exception {
        String language = languagePackageHashMap.get("packageLanguage");
        String educationBoardID = languagePackageHashMap.get("boardID");
        String studentClassID = languagePackageHashMap.get("classID");
        String studentClass = languagePackageHashMap.get("studentClass");
        String educationBoard = languagePackageHashMap.get("educationBoard");
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_main_exit);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(languageChangeMessage);

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setText(yes);
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getCommonMessages(language);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        TextView textViewNo = dialog.findViewById(R.id.textViewNo);
        textViewNo.setText(no);
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Util.setLanguagePackageSelection(context, language);
                    Util.setLanguageSelection(context, language);
                    Util.setClassNameSelection(context, studentClass);
                    Util.setClassSelection(context, studentClassID);
                    Util.setBoardSelection(context, educationBoardID);
                    Util.setBoardNameSelection(context, educationBoard);
                    Util.setAllsetValue(context, studentClass != null);
                    checkDeviceChange(language);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }


    private void getCommonMessages(String language) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        String internt = textArrayList.get(0) + ":" + textArrayList.get(1);
                        Util.setInternetMessage(context, internt);
                        try {
//                            Util.setCommonMessages(context, textArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Util.setAllsetValue(context, false);
                    action(language);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        global.getDatabaseReference().child("StaticTextDB").child("StudentApp/2").child("CommonMessages").child(Util.getSelectedLanguage(context)).addValueEventListener(valueEventListener);

    }


    private void startTimer(final TextView resendTextView) {

        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendTextView.setEnabled(false);
                resendTextView.setText(timeLeft + Util.getTimeString_(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (count == 2) {
                    count = 0;
                    try {
                        openErrorOtpDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    count++;
                    resendTextView.setEnabled(true);
                    resendTextView.setText(resend);
                }
            }
        }.start();
    }

    private void openErrorOtpDialog() throws Exception {
        reletiveSignUp.setVisibility(View.VISIBLE);
        reletiveOtp.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCancelable(false);
        TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(error);
        textViewUpdate.setText(okay);
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void openNameAgeDialog(final FirebaseUser user, String userType_) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_name_age);
        dialog.setCancelable(false);
        TextView textViewokay = dialog.findViewById(R.id.textViewokay);
        TextView text = dialog.findViewById(R.id.text);
        TextInputLayout input_fullName = dialog.findViewById(R.id.input_fullName);
        final TextView nameEditText = dialog.findViewById(R.id.nameEditText);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        text.setText(verifyMobile);
        input_fullName.setHint(enterName);
        textViewokay.setText(okay);
        if (!userType.equalsIgnoreCase("App")) {
            nameEditText.setText(Util.getUsername(context));
        }
        textViewokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (userType.equalsIgnoreCase("App")) {
//                    name = nameEditText.getText().toString().trim();
//
//                } else {
////                    name = Util.getUsername(context);
//                }
                String name = nameEditText.getText().toString().trim();
                Util.setUsername(context, name);
                if (TextUtils.isEmpty(name)) {
                    nameEditText.setError(emptyName);
                } else {
                    if (Util.checkInternetConnection(context)) {
                        Util.showDialog(context);
//                        if (global.getReferModel() != null) {
//                            if (Util.getReferalType(context).equalsIgnoreCase("Teacher")) {
//                                ReferModel referModel = new ReferModel();
//                                referModel.setReferUserName(Util.getUsername(context));
//                                referModel.setReferUserId(Util.getUserId(context));
//                                referModel.setDateStarted(Util.getCurrentDateWithDifferentFormat());
//                                global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child(ApplicationConstants.REFERALS_DATA).child(global.getReferModel().getReferUserId()).child("iPrepStudent").push().setValue(referModel);
//                            }
//                            global.setReferModel(null);
//                        }
                        afterRegisterProcess(user.getUid(), name, ""/*age*/, Util.getUserMobile(context), userType_);
                    } else {
                        try {
                            Util.showToast(context, Util.getCommonMessages(context).get(6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                textViewChangeMobileNumber.setEnabled(true);
            }
        });
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalLoginActivity that = (PalLoginActivity) o;

        return referalArrayList.equals(that.referalArrayList);
    }

    @Override
    public int hashCode() {
        return referalArrayList.hashCode();
    }

    private String generateReferalCode() {
        String userId = Util.getUserId(context);
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        salt.append("S");
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * userId.length());
            salt.append(userId.charAt(index));
        }
        final String referCode = salt.toString().toUpperCase();

        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child(ApplicationConstants.REFERAL_CODES).push().setValue(referCode);
        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child(ApplicationConstants.REFERALS).child(Util.getUserId(context)).child(ApplicationConstants.REFERAL_CODE).setValue(referCode);
        ReferReversModel referReversModel = new ReferReversModel();
        referReversModel.setUserId(Util.getUserId(context));
        referReversModel.setUserName(Util.getUsername(context));
        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child(ApplicationConstants.REFERALS_REVERSE).child(referCode).setValue(referReversModel);
        return referCode;
    }

    /* This call is only  to save the plan for topic screen*/
    private void startTrialPlan() {
        Date currentTime = Calendar.getInstance().getTime();
        CurrentPlanModel currentPlanModel = new CurrentPlanModel();
        currentPlanModel.setDateStarted(currentTime.toString());
        currentPlanModel.setPlanDuration("7");
        currentPlanModel.setStatus("Trial");
        HashMap<String, Object> currentPlanHashmap = new HashMap<>();
        currentPlanHashmap.put("dateStarted", currentTime.toString());
        currentPlanHashmap.put("status", "Trial");
        currentPlanHashmap.put("planDuration", "7");
        global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(Util.getUserId(context)).child("UsersPlans").updateChildren(currentPlanHashmap);
        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child("UsersPlans").child(Util.getUserId(context)).setValue(currentPlanModel);
        ApplicationConstants.STATUS = "TRIAL";
//        ApplicationConstants.STATUS = "PLAN";
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void setStaticText() {
//        linearToGONE.setVisibility(View.GONE);
  //      shimmer_view_container.setVisibility(View.VISIBLE);
  //      shimmer_view_container.startShimmerAnimation();

        if (Util.getSelectedLanguage(context)==null)
        {
            Util.setLanguageSelection(context,"english");
        }

        global.getDatabaseReference().child("StaticTextDB").child("StudentApp").child("1").child(Util.getSelectedLanguage(context)).child("Sign up screen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                  //  linearToGONE.setVisibility(View.VISIBLE);
                   // shimmer_view_container.setVisibility(View.GONE);
                    if (dataSnapshot != null) {
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        mobileEnter = textArrayList.get(0);
                        textView.setText(textArrayList.get(0));
                        buttonSignip.setText(textArrayList.get(10));
                        textViewOr.setText(textArrayList.get(5));
                        buttonFacebook.setText(textArrayList.get(6));
                       // buttonGoogle.setText(textArrayList.get(7));
                        textViewAlreadtAccount_.setText(textArrayList.get(5));
                        inputMobile.setHint(textArrayList.get(2));
                        enterMobile = textArrayList.get(2);
                        inputOtp.setHint(textArrayList.get(9));
//                      input_fullName.setHint(textArrayList.get(7));
                        enterOtp = textArrayList.get(8);
                        helloOtp = textArrayList.get(9);
                        resend = textArrayList.get(11);

                        emptyMessage = textArrayList.get(12);
                        textViewAlreadtAccount.setText(textArrayList.get(13));

                        emptyMobile = textArrayList.get(3);
                        invalidMobile = textArrayList.get(4);
                        emptyName = textArrayList.get(14);
                        changeMobile = textArrayList.get(15);


                        verifyMobile = textArrayList.get(18);

                        enterName = textArrayList.get(19);
                        enterAge = textArrayList.get(20);
                        enterAge = textArrayList.get(14);
                        enterAge = textArrayList.get(15);


                        okay = textArrayList.get(21);
                        exitMessage = textArrayList.get(22);
                        yes = textArrayList.get(23);
                        no = textArrayList.get(24);


                        resendButton.setText(textArrayList.get(11));
                        textViewConfirm.setText(textArrayList.get(10));
                        textViewChangeMobileNumber.setText(textArrayList.get(12));
                        textInfo.setText(textArrayList.get(1));
                        otpInfoText = textArrayList.get(8);
                        otpInfoText2 = textArrayList.get(48);
                        textInfo1.setText(otpInfoText);
                        textInfo2.setText(otpInfoText2);
                        error = textArrayList.get(17);
                        otpText = textArrayList.get(26);
                        textInfoOtp.setText(otpText);
                        incorrectOtp = textArrayList.get(27);
                        manyTimeUsage = textArrayList.get(28);
                        timeLeft = textArrayList.get(29);
                        emptyOtp = textArrayList.get(30);
                        googlePlayError = textArrayList.get(31);
                        message1 = textArrayList.get(32);
                        message2 = textArrayList.get(33);
                        message3 = textArrayList.get(34);
                        message = textArrayList.get(35);
                        confirm = textArrayList.get(36);
                        message4 = textArrayList.get(37);
                        message5 = textArrayList.get(38);
                        message6 = textArrayList.get(39);
                        countryMessage = textArrayList.get(40);
                        languageChangeMessage = textArrayList.get(41);
                        enterPartnerCode = textArrayList.get(42);
                        proceed = textArrayList.get(43);
                        partnerCode = textArrayList.get(44);
                        codeValidated = textArrayList.get(45);
                        invalidCode = textArrayList.get(46);

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

    private void syncAndroidId() {
        AndroidIdModel androidIdModel = new AndroidIdModel();
        androidIdModel.setAndroidId(Util.getAndroidId(context));
        if (Util.getUserId(context) != null) {
            androidIdModel.setUserId(Util.getUserId(context));
        } else {
            androidIdModel.setUserId("");
        }
        androidIdModel.setLogin(false);
        DatabaseReference databaseReference = global.getDatabaseReference();//FirebaseDatabase.getInstance().getReference();
        databaseReference.child(ApplicationConstants.USERS).child("DeviceIds").child(Util.getAndroidId(context)).push().setValue(androidIdModel);
    }


    private void openMobileNumberDialog(final FirebaseUser user, String userType_) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_mobile_number);
        dialog.setCancelable(false);
        final EditText mobileEditText = dialog.findViewById(R.id.mobileEditText);
        TextInputLayout input_mobile = dialog.findViewById(R.id.input_mobile);
        TextView textViewokay = dialog.findViewById(R.id.textViewokay);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(mobileEnter);
        textViewokay.setText(okay);
        input_mobile.setHint(enterMobile);
        textViewokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = mobileEditText.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    mobileEditText.setError(emptyMobile);
                } else if (mobile.length() != 10) {
                    mobileEditText.setError(invalidMobile);
                } else {
                    dialog.dismiss();
                    Util.showDialog(context);
                    afterRegisterProcess(user.getUid(), Util.getUsername(context), ""/*age*/, mobile, userType_);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }


    private void checkDeviceChange(final String language) {
        global.getDatabaseReference().child("LicenseKeys").child("UsedLicenseKeys").child(Util.getUserId(context)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        String androidID = (String) map.get("androidID");
                        if (androidID.equalsIgnoreCase(Util.getAndroidId(context))) {
                            Util.dismissDialog();
                            action(language);
                        } else {
                            Util.dismissDialog();
                            deviceChangeLicenseKeyRequest();
                        }

                    } else {
                        action(language);
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

    private void action(String language) {
        try {
            String tok = Util.getToken(context);
            /* Change Token for notification if user login in to new  mobile */
            global.getDatabaseReference().child(ApplicationConstants.USERS).child("Students").child(Util.getUserId(getApplicationContext())).child("token").setValue(tok);
//            global.setUserIDForAnalytics();
            if (Util.isAllvalueSetted(context)) {
                Util.dataDialog = null;
                startActivity(new Intent(context, PalAnonymousLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                startActivity(new Intent(context, PracticeTopicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
//                startActivity(new Intent(context, PreferenceSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                startActivity(new Intent(context, PalAnonymousLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
            finish();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deviceChangeLicenseKeyRequest() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);
        textViewUpdate.setText(confirm);
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(Util.getUserId(context)).child("isLicenseVerificationCompleted").setValue("False");
                //  global.getDatabaseReference().child("LicenseKeys").child("UsedLicenseKeys").child(Util.getUserId(context)).child("androidID").setValue(Util.getAndroidId(context));
                // startActivity(new Intent(context, LicenseVerificationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                //finish();
                


            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    public void setImageAndText(Country country) throws Exception {
        textViewCountry.setText("(" + country.getCode() + ") " + country.getDialCode());
        country_flag.setImageResource(country.getFlag());
        counrtyCode = country.getDialCode();
    }


    private void startAnonymousSignIn() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userType = "App";
                            Util.setUserType(context, userType);
                            updateUI(user);
                        } else {
                            Util.showToast(context, "" + task.getException());
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            // updateUI(null);
                        }

                        // ...
                    }
                });
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
                String partnerCode = ngoCodeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(partnerCode)) {
                    ngoCodeEditText.setError(enterPartnerCode);
                } else {
                    //Validation is pending from
                    validateCode(partnerCode, dialog);
                }

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }


    private void validateCode(String referCode, Dialog dialog) {
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


}