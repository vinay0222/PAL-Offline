package com.idreameducation.ipreppal.pal.activity;


import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.idreameducation.ipreppal.model.AndroidIdModel;
import com.idreameducation.ipreppal.model.AvatarModel;
import com.idreameducation.ipreppal.model.ProfileImageModel;
import com.idreameducation.ipreppal.model.ProfileModel;
import com.idreameducation.ipreppal.model.RateModel;
import com.idreameducation.ipreppal.pal.adapter.AvatarAdapter;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Country;
import com.idreameducation.ipreppal.util.ImagePickerActivity;
import com.idreameducation.ipreppal.util.MyUploadService;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class PalProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    public static final int REQUEST_PERMISSION_SETTING = 1;
    public static final int REQUEST_IMAGE = 100;
    public static PalProfileActivity activity;
    private static Context context;
    private static String dob;
    private static String dob_;
    private static EditText textViewDateOfBirth;
    private static int day;
    private static int month;
    private static int year;
    private static String yes;
    private static String permissionN;
    private static String permissionNMessage;
    private static String futureDate;
    public Uri URI;
    public String HINT = "NONE";
    private int p;
    private FirebaseAuth firebaseAuth;
    private final ArrayList<String> keyArrayList = new ArrayList<>();
    private final ArrayList<Boolean> isDatadownaloadArratList = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> androidIdsArrayList = new ArrayList<>();
    //private TextView btnReset;
    private TextView btnContinoue;
    private TextView textViewUserName;
    private TextView textViewClass;
    private TextView textViewBoard;
    private TextView shareBtn;
    private TextView rateTextView;
    private TextView textViewLikeThisApp;
    private TextView textViewProfile;
    //private TextView textViewLanguage;
    private CircleImageView profileImageView;
    private ImageView imageViewEdit;
    private String userChoosenTask;
    private BroadcastReceiver mBroadcastReceiver;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private EditText nameEditText;
    private EditText boardEditText;
    private EditText languageEditText;
    private TextView ageEditText;
    private AppCompatSpinner genderEditText;
    private EditText mobileEditText;
    private EditText schoolEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private Global global;
    private String educationBoard;
    private String language;
    private String studentClass;
    private String userType;
    private String token;
    private String ratingThanks;
    private String profileImage;
    private String alreadyRunningMessage;
    private String addPhoto;
    private String takePhoto;
    private String gallery;
    private String cancel;
    private String enterName;
    private String enterMobile;
    private String enterEmail;
    private String invalidEmail;
    private String enterAddress;
    private String enterSchool;
    private String age,gender;
    private String ageText;
    private String okay;
    private String rateThisApp;
    private String rateErrorMessage;
    private String enterMessage;
    private String updatedMessage;
    private String Message;
    private String updatedImageMessage;
    private String languageString;
    private String sclass;
    private String board;
    private String city;
    private String state;
    private String changeMessage2;
    private String changeMessage1;
    private String packageLanguage;
    private String boardID;
    private String classID;
    private String NGOID;
    private String APPID;
    private String done;
    private String rate;
    private String message1;
    private String message2;
    private String message3;
    private String message4;
    private File destination;
    private float rating = 0;
    private TextView inputMObile;
    private TextView input_fullName;
    private TextView inputEmail;
    private TextView inputAddress;
    private TextView inputSchool;
    private TextView inputCity;
    private TextView inputDateOfBirth;
    private String notUpdated;
    //    private ProgressBar imageProgressBar;
    private boolean isUploadRunning = false;
    private ProfileModel profileModel;
    private TextView textViewAge;
    private Spinner spinnerStates;
    private ArrayList<String> listStatesCities;
    private EditText cityStateAutoComplete;
    private EditText parentsPhoneEditText;
    private EditText stateAtioComplete;
    private DatabaseReference reference;
    private final boolean isUpdateRunning = false;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ArrayList<String> cityStateList = new ArrayList<>();
    private final String counrtyCode = "91";
    private String date;
    private ImageView country_flag;
    private TextView textViewCountry;
    private String uploadError;
    private String title;
    private String titleMessage;
    private String deny;
    private String progressMessage;
    private String errorOccured;
    private String parentsMobile;
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            dob_ = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            reference.child("dob").setValue(dob_);
            textViewDateOfBirth.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }
    };
    private String givePermissionMessage;

    private String givePermission;
    private String permissionBeforMessage;
    private String permissionBeforeTitle;
    private String grant;
    private String message;
    private String isLicenseVerificationCompleted;
    private List<Country> countries;
    private TextView textViewUserInfo;
    private TextView input_Board;
    private TextView input_Class;
    private TextView input_Language;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;
    TextView changeAvatar_Text;
    TextView updateBtn;
    ImageView avtarPopupCrossBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        Util.setContext(context);
        setContentView(R.layout.activity_user_profile_screen);
        assignIds();
        listners();
        input_Class.setClickable(false);
        ageEditText.setClickable(false);
    }

    @SuppressLint("RestrictedApi")
    public void animateFAB() {

        if (isFabOpen) {
            fab.setImageResource(R.drawable.ic_inactive);
            isFabOpen = false;
        } else {

            fab.setImageResource(R.drawable.ic_inactive);
            isFabOpen = false;

            buttonScreenshot(null);

        }
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


        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
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
    private Uri filePath;
    private String linkFromFirebaseStorage,textSendWithImageTextString;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;

    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    ProgressBar mProgressBar;
    LinearLayout loading_layout;

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

//        global.getDatabaseReference().child("issue_raised_in_pal_application").child(Util.getSchoolId(context)).child(Util.getUserId(context)).child(""+millis).setValue(hashMapToSync);


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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionWrite(final Context context) {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_permission);
                TextView okayButton = dialog.findViewById(R.id.okayButton);
                TextView textTitle = dialog.findViewById(R.id.textTitle);
                TextView textMessage = dialog.findViewById(R.id.textMessage);
                textTitle.setText(permissionN);
                textMessage.setText(permissionNMessage);
                okayButton.setText(yes);

                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    public static void showdDateToast() {
        Util.showToast(context, futureDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalProfileActivity that = (PalProfileActivity) o;

        return cityStateList.equals(that.cityStateList);
    }

    @Override
    public int hashCode() {
        return cityStateList.hashCode();
    }

    private void checkConnection(boolean first) {
        mProgressBar=findViewById(R.id.progressBar);
        slow_internet_Text=findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=findViewById(R.id.imageViewCrossVideo2);
        loading_layout=findViewById(R.id.connection_layout);
        if(!Util.isOfflineMode(context)) {
            handler.postDelayed(new Runnable() {
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
                        mProgressBar.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
                        imageViewCrossVideo2.setVisibility(View.GONE);
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

    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        loading_layout.setVisibility(View.GONE);
    }

    private void listners() {
        findViewById(R.id.fab2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab2.setImageResource(R.drawable.ic_call_active);
                openContactUsDialogue();
            }
        });

        findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab1.setImageResource(R.drawable.ic_screenshot_active);
                animateFAB();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonScreenshot(null);                    }
                }, 600);
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                animateFAB();
            }
        });

        findViewById(R.id.btnContinoue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                if (name!=null) {
                    updateProfile(name);
                } else {
                    Toast.makeText(activity, "Kindly enter correct name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.profileImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                showAvatar();
            }
        });

        findViewById(R.id.avatar_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----");
            }
        });

        findViewById(R.id.imageViewEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatar();
            }
        });

        findViewById(R.id.textViewDateOfBirth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openDatePickerDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    ArrayAdapter adapter;
    ArrayList<String> genderArray;

    private void assignIds() {
        context = this;
        activity = this;
        global = (Global) getApplicationContext();
        global.sendData("Profile", this.getClass().getName());
        try {
            reference = global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);
        changeAvatar_Text = findViewById(R.id.changeAvatar_Text);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileImageView = findViewById(R.id.profileImageView);
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.linearLayout);
        cityStateAutoComplete = findViewById(R.id.cityStateAutoComplete);
        parentsPhoneEditText = findViewById(R.id.parentsPhoneEditText);
        stateAtioComplete = findViewById(R.id.stateAutoComplete);
        avtarPopupCrossBtn = findViewById(R.id.avtarPopupCrossBtn);
//        cityStateAutoComplete.setThreshold(0);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        textViewUserName = findViewById(R.id.textViewUserName);

        nameEditText = findViewById(R.id.nameEditText);

        avtarPopupCrossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_layout.setVisibility(View.GONE);
            }
        });

//        nameEditText.setText(Util.getUsername(context));
        if (Util.getUsername(context)!=null)
        {
            String name=Util.getUsernameShowable(context);
            name=name.replace('_',' ');
            nameEditText.setText(name);
        }

        // textViewLanguage = findViewById(R.id.textViewLanguage);
//        textViewLikeThisApp = findViewById(R.id.textViewLikeThisApp);
//        rateTextView = findViewById(R.id.rateTextView);
//        textViewCountry = findViewById(R.id.textViewCountry);
//        country_flag = findViewById(R.id.country_flag);
//        shareBtn = findViewById(R.id.shareBtn);
        textViewClass = findViewById(R.id.textViewClass);
//        textViewClass.setText(Util.getSelectedClass(context) + "th");
        textViewClass.setText(Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));

//        imageProgressBar = findViewById(R.id.imageProgressBar);

        textViewBoard = findViewById(R.id.textViewBoard);
        btnContinoue = findViewById(R.id.btnContinoue);
        //btnReset = findViewById(R.id.btnReset);
        textViewProfile = findViewById(R.id.textViewProfile);
        updateBtn = findViewById(R.id.updateBtn);

        if(Util.isOfflineMode(context)) imageViewEdit.setVisibility(View.GONE);


//        boardEditText.setText("CBSE");

        mobileEditText = findViewById(R.id.mobileEditText);
        mobileEditText.setText(Util.getUserMobile(context));


        genderArray = new ArrayList<String>();
        genderArray.add("Select your Gender");
        genderArray.add("Male");
        genderArray.add("Female");


        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);

        int layout=android.R.layout.simple_spinner_item;
        if(Util.isPortraitMode(context)) layout=R.layout.gender_spinner_view;

        adapter = new ArrayAdapter(this, layout, genderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderEditText.setAdapter(adapter);
        genderEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                gender = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        PalHomeActivity.hideNavigationBar(getWindow());

        schoolEditText = findViewById(R.id.schoolEditText);
        emailEditText = findViewById(R.id.emailEditText);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewDateOfBirth.setFocusable(false);

        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    if (!Util.isValidEmail(editable.toString())) {
                        emailEditText.setError(invalidEmail);
                    } else {
                        emailEditText.setError(null);
                    }
                }


            }
        });
        addressEditText = findViewById(R.id.addressEditText);
        inputMObile = findViewById(R.id.inputMObile);
        input_fullName = findViewById(R.id.input_fullName);
        inputEmail = findViewById(R.id.inputEmail);
        //input_Language = findViewById(R.id.input_Language);
        input_Class = findViewById(R.id.input_Class);
        inputAddress = findViewById(R.id.inputAddress);
        inputSchool = findViewById(R.id.inputSchool);
        inputCity = findViewById(R.id.inputCity);
        inputDateOfBirth = findViewById(R.id.inputDateOfBirth);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Util.dismissDialog();
                switch (intent.getAction()) {
                    case MyUploadService.UPLOAD_COMPLETED:
                        Bundle bundle = intent.getExtras();
                        Uri url = (Uri) bundle.get(MyUploadService.EXTRA_DOWNLOAD_URL);
                        try {
                            Util.setUserProfileUrl(context, url.toString());
                            Message = updatedImageMessage;
                            setProfileImageView(url.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MyUploadService.UPLOAD_ERROR:
                        Util.showToast(context, uploadError);
                        break;

                }
            }
        };

        try {
            setStaticText();
            getAppIDDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cityStateList = obj_list();
        get_support_dialog_language();
        ImagePickerActivity.clearCache(this);
        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        changeAvatar_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatar();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                if (name!=null) {
                    updateProfile(name);
                } else {
                    Toast.makeText(activity, "Kindly enter correct name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // This add all JSON object's data to the respective lists
    public ArrayList<String> obj_list() {
        // Exceptions are returned by JSONObject when the object cannot be created
        ArrayList<String> listSelect = new ArrayList<>();
        try {
            // Convert the string returned to a JSON object
            JSONObject jsonObject = new JSONObject(getJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            listStatesCities = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                // select the particular JSON data
                JSONObject object = array.getJSONObject(i);
                String city = object.getString("name");
                String state = object.getString("state");
                // add to the lists in the specified format
                if (i == 0) {
                    listSelect.add(city);
                } else {
                    listStatesCities.add(city + " , " + state);
                }
            }
            Collections.sort(listStatesCities, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            listSelect.addAll(listStatesCities);
//            cityStateAutoComplete.setAdapter(new ArrayAdapter<>(context, R.layout.row_spinner, listSelect));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listSelect;
    }

    // Get the content of cities.json from assets directory and store it as string
    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }
    //*****                   END                    *****//

    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        Util.setLogoutSelection(context, false);
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            // Register receiver for uploads and downloads
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Util.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int currentAPIVersion = Build.VERSION.SDK_INT;
                    if (currentAPIVersion >= Build.VERSION_CODES.O) {
                        checkPermissionWrite(context);
                    } else {
//                        selectImage();
                        showImagePickerOptions();
                    }
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRationale) {
                        userCheckNeverAskPermission(givePermissionMessage);
                        return;
                    }
                    Util.checkPermission(PalProfileActivity.this, yes, permissionN, permissionNMessage);

                }
                break;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                try {
//                    selectImage();
                    showImagePickerOptions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if (userChoosenTask.equals(takePhoto))
//                    cameraIntent();
//                else if (userChoosenTask.equals(gallery))
//                    galleryIntent();

                break;


        }
    }

    private void userCheckNeverAskPermission(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);
        textViewUpdate.setText(givePermission);
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void selectImage() {
        final CharSequence[] items = {takePhoto, gallery,
                cancel};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PalProfileActivity.this);
        builder.setTitle(addPhoto);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;//Util.checkPermission(UserProfileScreen.this);
                if (items[item].equals(takePhoto)) {
                    userChoosenTask = takePhoto;
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(gallery)) {
                    userChoosenTask = gallery;
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(cancel)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
    }


    LinearLayout avatar_layout;

    String profileLink="";

    private void showAvatar() {

        avatar_layout=findViewById(R.id.avatar_layout);
        avatar_layout.setVisibility(View.VISIBLE);

        RecyclerView recyclerView=findViewById(R.id.avatar_recyclerview);
        if(Util.isPortraitMode(context)) recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else recyclerView.setLayoutManager(new GridLayoutManager(context,4));

        ArrayList<AvatarModel> avatarClassArrayList=new ArrayList<>();
        AvatarAdapter avatarAdapter=new AvatarAdapter(avatarClassArrayList);

        avatarAdapter.SetOnItemClickListener(new AvatarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(!Util.isOfflineMode(context)) {
                    profileLink = avatarClassArrayList.get(position).getLink();
                    avatar_layout.setVisibility(View.GONE);

                    Glide.with(context).load(profileLink).into(profileImageView);
                } else {
                    profileLink = avatarClassArrayList.get(position).getId();
                    avatar_layout.setVisibility(View.GONE);
                    Util.setUserProfileUrl(context,profileLink);
                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ avatarClassArrayList.get(position).getId()+".png";
                    File file = new File(filePath);

                    Uri uri = null;

                    if(file.exists()) uri = Uri.fromFile(file);

                    if(uri != null){
                        Uri finalUri = uri;
                        Glide.with(context)
                                .load(uri).into(profileImageView);

                    }
                }
            }
        });

        if(!Util.isOfflineMode(context)) global.getDatabaseReference().child("avatar").child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                avatarClassArrayList.clear();

                if(snapshot.getValue()!=null)
                {
                    for(DataSnapshot ss:snapshot.getChildren())
                    {
                        AvatarModel avatarClass=ss.getValue(AvatarModel.class);
                        avatarClassArrayList.add(avatarClass);

                        recyclerView.setAdapter(avatarAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        else {
            String filePath = ".iDream_content/offlinetab_PAL/avatar.txt";    // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);    // read file
            try {
                JSONArray object = jsonObject.getJSONArray("student");
                avatarClassArrayList.clear();

                for(int i=0;i<=object.length()-1;i++) {

                    JSONObject object1= (JSONObject) object.get(i);

                    AvatarModel avatarClass=new AvatarModel((String) object1.get("id"), (String) object1.get("link"), (Boolean) object1.get("visable"));
                    avatarClassArrayList.add(avatarClass);

                }

                recyclerView.setAdapter(avatarAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void changePrefrenceDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confim_change);
        dialog.setCancelable(true);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView titleTextView = dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(message);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewOkay.setText(yes);
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                global.setPreferenceChange(true);
                Util.setAllsetValue(context, false);
                Util.dataDialog = null;
                Util.setDataDownloaded(context, false);
                Util.setCategoryDownloaded(context, false);
                Util.setCategoriesDataDownloaded(context, false);
                Util.setClassNameSelection(context, null);
                Util.setBoardNameSelection(context, null);
                Util.setClassSelection(context, null);
                Util.setBoardSelection(context, null);
//                Util.setLanguagePackageSelection(context, null);
                Util.setFac(context, false);
                try {

                    global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("classID").removeValue();
                    global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("boardID").removeValue();
//                global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("packageLanguage").removeValue();
                    global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("studentClass").removeValue();
                    global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).child("educationBoard").removeValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getAndroidId();
                //  startActivity(new Intent(context, PreferenceSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                //
//                if (city != null && !TextUtils.isEmpty(city)) {
//                    startActivity(new Intent(context, SelectBoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                } else {
//                    startActivity(new Intent(context, SelectLocationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                }

            }
        });
        textViewCancel.setText(cancel);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void getAndroidId() {
        global.getDatabaseReference().child(ApplicationConstants.USERS).child("DeviceIds").child(Util.getAndroidId(context)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                            HashMap<String, Object> androidIdHashMap = (HashMap<String, Object>) singleSnapShot.getValue();
                            String key = singleSnapShot.getKey();
                            keyArrayList.add(key);
                            isDatadownaloadArratList.add((boolean) androidIdHashMap.get("login"));
                            androidIdHashMap.remove("login");
                            androidIdsArrayList.add(androidIdHashMap);
                        }
                        HashMap<String, Object> hashMapToCompare = new HashMap<>();
                        hashMapToCompare.put("userId", Util.getUserId(context));
                        hashMapToCompare.put("androidId", Util.getAndroidId(context));
                        if (androidIdsArrayList.contains(hashMapToCompare)) {
                            for (int i = 0; i < androidIdsArrayList.size(); i++) {
                                if (Util.getUserId(context).equalsIgnoreCase((String) androidIdsArrayList.get(i).get("userId"))) {
                                    String key = keyArrayList.get(i);
                                    syncAndroidId(false, key);
                                    return;
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
        });
    }

    private void syncAndroidId(boolean isDataDownloaded, String key) {
        AndroidIdModel androidIdModel = new AndroidIdModel();
        androidIdModel.setAndroidId(Util.getAndroidId(context));
        androidIdModel.setUserId(Util.getUserId(context));
        androidIdModel.setLogin(isDataDownloaded);
        global.getDatabaseReference().child(ApplicationConstants.USERS).child("DeviceIds").child(Util.getAndroidId(context)).child(key).setValue(androidIdModel);
    }

    private void updateProfile(String name) {
        String cityState = cityStateAutoComplete.getText().toString();
        String state = stateAtioComplete.getText().toString();
        String age = ageEditText.getText().toString();
//        String gender = genderEditText.;
        String pContact = parentsPhoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String dob = textViewDateOfBirth.getText().toString();
        name = nameEditText.getText().toString().trim();
//        if (!email.equalsIgnoreCase(""))
//        {
//            if (!Util.isValidEmail(email))
//            {
//                Util.showToast(context,"Kindly enter correct email");
//                return;
//            }
//        }

        if (!pContact.equalsIgnoreCase(""))
        {
            if (!(pContact.length() == 10))
            {
                Util.showToast(context,"Kindly enter correct Parents Number");
                return;
            }
        }




        //    if (cityStateList.contains(cityState) || TextUtils.isEmpty(cityState)) {
        if (!isUpdateRunning) {
            Util.showDialog(context);
            btnContinoue.setEnabled(false);
            String mobile = counrtyCode + "-" + mobileEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(mobileEditText.getText().toString().trim())) Util.setUserMobile(context, mobile);

            String address = addressEditText.getText().toString().trim();
            final String school = schoolEditText.getText().toString().trim();
            try {
                reference.child("token").setValue(token);
                reference.child("language").setValue(Util.getSelectedLanguage(context));
                reference.child("studentClass").setValue(Util.getSelectedClassName(context));
                reference.child("fullName").setValue(name);
                reference.child("dateStarted").setValue(date);
                reference.child("mobile").setValue(mobile);
                reference.child("educationBoard").setValue(Util.getSelectedBoardName(context));
                reference.child("userType").setValue(userType);
                reference.child("address").setValue(address);
                reference.child("age").setValue(age);
                reference.child("gender").setValue(gender);
                reference.child("dob").setValue(dob);
                reference.child("school").setValue(school);
                reference.child("packageLanguage").setValue(Util.getSelectedLanguagePackage(context));
                reference.child("isLicenseVerificationCompleted").setValue(isLicenseVerificationCompleted);
                reference.child("classID").setValue(Util.getSelectedClass(context));
                reference.child("ngoID").setValue(NGOID);
                reference.child("appID").setValue(APPID);
                reference.child("boardID").setValue(Util.getSelectedBoard(context));
                reference.child("email").setValue(email);
                reference.child("city").setValue(cityState);
                reference.child("state").setValue(state);
                reference.child("parentsMobile").setValue(pContact);
                reference.child("profileImage").setValue(profileLink);

//                global.getDatabaseReference().child("users").child("students").child(Util.getUserId(context)).child("profileImage").setValue(profileLink);

                Util.setUserProfile(context,profileLink);

                Util.setAge(context,age);
                Util.setGender(context,Util.getUserId(context),gender);
                Util.setBirthDate(context,Util.getUserId(context),dob);
                Util.setUserEmail(context,email);
                Util.setCitySelection(context,cityState);
                Util.setStateSelection(context,state);
                Util.setParentsContact(context,pContact);

//                if(Util.getSelectedLanguage(context).equals("hindi")) Toast.makeText(context, "प्रोफ़ाइल अपडेट हो चुकी हे", Toast.LENGTH_SHORT).show();
//                else Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                Message = updatedMessage;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnContinoue.setEnabled(true);
                        linearLayout.clearFocus();
                        nameEditText.setCursorVisible(false);
                        Util.showToast(context, Message);
                        Util.dismissDialog();
                    }
                }, 1000);
                global.getDatabaseReference().child("users").child("students").child(Util.getUserId(context)).setValue(reference);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showToast(context, progressMessage);
        }
    }


    /* Upload Image on Firebase storage */
    public void uploadImage(Uri uri) throws FileNotFoundException {
        try {
            HINT = "IMAGE";
            if (!Util.checkInternetConnection(context)) {
                Util.showInternetConnectioError(context);
                return;
            }
            isUploadRunning = true;
//            imageProgressBar.setVisibility(View.VISIBLE);
            startService(new Intent(this, MyUploadService.class)
//                    .putExtra(MyUploadService.EXTRA_FILE_URI, Uri.fromFile(destination))
                    .putExtra(MyUploadService.EXTRA_FILE_URI, uri)
                    .setAction(MyUploadService.ACTION_UPLOAD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (isUploadRunning) {
            Util.showToast(context, progressMessage);
        } else {
            super.onBackPressed();

        }
    }

    private void getUserProfile() throws Exception {
//        if (!Util.isNetworkAvailable(context)) {
//            try {
//                Util.showToast(context, Util.getCommonMessages(context).get(6));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            finish();
//            Util.dismissDialog();
//            return;
//        }

        isloading=true;
        checkConnection(false);
        firebaseAuth = FirebaseAuth.getInstance();

        textViewBoard.setVisibility(View.GONE);
        findViewById(R.id.textViewBoardHint).setVisibility(View.GONE);
        if(Util.isOfflineMode(context))
        {
            Util.getAge(context);
            Util.getGender(context,Util.getUserId(context));
            Util.getBirthDate(context,Util.getUserId(context));
            Util.getUserEmail(context);
            Util.getSelectedCity(context);
            Util.getSelectedState(context);
            Util.getParentsContact(context);

            try {
                hideconnection_layout();
                Util.dismissDialog();
                HashMap<String, String> languagePackageHashMap;

                educationBoard = Util.getSelectedBoard(context);
                String fullName = Util.getUsername(context);
//                isLicenseVerificationCompleted = languagePackageHashMap.get("isLicenseVerificationCompleted");
                String email = Util.getUserEmail(context);
                age = Util.getAge(context);
                gender = Util.getGender(context,Util.getUserId(context));
                String school = Util.getSchoolName(context);
//                String address = Util.getGender(context);
                String mobile = Util.getUserMobile(context);
//                profileLink = languagePackageHashMap.get("profileImage");
                packageLanguage = Util.getSelectedLanguage(context);
                profileLink=Util.getUserProfileUrl(context);
                if(Util.isOfflineMode(context)) {
                    if (profileLink == null)
                        Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImageView);
                    else {
                        String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ profileLink+".png";
                        File file = new File(filePath);

                        Uri uri = null;

                        if(file.exists()){
                            uri = Uri.fromFile(file);
                        }

                        if(uri != null){
                            profileImageView.setVisibility(View.VISIBLE);
                            Glide.with(context).load(uri).into(profileImageView);

                        }
                    }

                }
                else {
                    if (profileLink != null)
                        Glide.with(context).load(profileLink).into(profileImageView);
                    else
                        Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImageView);
                }
                if(genderArray.contains(gender))
                {
                    int pos = genderArray.indexOf(gender);
                    genderEditText.setSelection(pos);
                }


                NGOID = Util.getNGOID(context);
                APPID = Util.getAPPID(context);
                try {
                    boardID = Util.getSelectedBoard(context);
                    classID = Util.getSelectedClass(context);
                    studentClass = Util.getSelectedClass(context);
                    if (classID.contains("_")) {
                        classID = classID.replace("_", " ");
                    }
                    if (classID.contains("Medical")) {
                        classID = classID.split(" ")[0] + " non medical/medical";
                    }
                    textViewBoard.setText(educationBoard.toUpperCase(Locale.ROOT));
//                    textViewClass.setText(classID.replace(" commerce","") + "th");
                    textViewClass.setText(Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));

                } catch (Exception e) {
                    e.printStackTrace();
                    textViewBoard.setText(Util.getSelectedBoardName(context).toUpperCase(Locale.ROOT));
//                    textViewClass.setText(Util.getSelectedClassName(context).replace("_commerce",""));
                    textViewClass.setText(Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));
                }

                ageEditText.setText(age);

                language = Util.getSelectedLanguage(context);
                // languageEditText.setText(packageLanguage);
                // languageEditText.setFocusable(false);


                dob_ = Util.getBirthDate(context,Util.getUserId(context));
                textViewDateOfBirth.setText(dob);
                userType = Util.getUserType(context);
                token = Util.getToken(context);
                city = Util.getSelectedCity(context);
                parentsMobile = Util.getParentsContact(context);
                parentsPhoneEditText.setText(parentsMobile);
                state = Util.getSelectedState(context);

                /*Update UI */
//                addressEditText.setText(address);
                if (city != null) {
                    if (!TextUtils.isEmpty(city)) {
//                        cityStateAutoComplete.setText(city);
                    }
                }

                if (state != null) {
                    if (!TextUtils.isEmpty(state)) {
//                        stateAtioComplete.setText(state);
                    }
                }
                // textViewLanguage.setText(languageString + " : " + Util.getSelectedLanguage(context));
                //  textViewAge.setText(ageText + " : " + age);
//                schoolEditText.setText(school);
                emailEditText.setText(email);
                //Util.setUsername(context, fullName);
                if (dob_ != null) {
                    textViewDateOfBirth.setText(dob_ + "   ");
                } else {
//                        textViewDateOfBirth.setText(dob + "       ");
                }

                nameEditText.setText(Util.getUsernameShowable(context));


                try {
                    if (!TextUtils.isEmpty(mobile)) {
                        if (mobile.contains("-")) {
                            //   countries = new ArrayList<>(Arrays.asList(Util.COUNTRIES));
//                                Integer.parseInt(mobile.split("-")[0])
//                                String phone;
//                                try {
//                                    if (mobile.split("-")[0].contains("+")) {
//                                        phone = mobile.split("-")[0];
//                                    } else {
//                                        phone = "+" + mobile.split("-")[0];
//                                    }
//                                    Country country = getCountryByISO(phone);
//                                    textViewCountry.setText(country.getDialCode());
//                                    country_flag.setImageResource(country.getFlag());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                            if (!TextUtils.isEmpty(mobile.split("-")[1])) {
                                mobileEditText.setText(mobile.split("-")[1]);
                                mobileEditText.setFocusable(false);
                            } else {
                                mobileEditText.setFocusable(true);
                            }

                        } else {
                            mobileEditText.setText(mobile);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                /*Get userImage from the FireBase*/
                try {
                    getUserProfileImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    hideconnection_layout();
                    Util.dismissDialog();
                    HashMap<String, String> languagePackageHashMap;
                    languagePackageHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                    educationBoard = languagePackageHashMap.get("educationBoard");
                    String fullName = languagePackageHashMap.get("fullName");
                    isLicenseVerificationCompleted = languagePackageHashMap.get("isLicenseVerificationCompleted");
                    String email = languagePackageHashMap.get("email");
                    age = languagePackageHashMap.get("age");
                    gender = languagePackageHashMap.get("gender");
                    String school = languagePackageHashMap.get("school");
                    String address = languagePackageHashMap.get("address");
                    String mobile = languagePackageHashMap.get("mobile");
                    profileLink = languagePackageHashMap.get("profileImage");
                    packageLanguage = languagePackageHashMap.get("packageLanguage");

                    if(profileLink!=null)
                        Glide.with(context).load(profileLink).into(profileImageView);
                    else
                        Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImageView);

                    if(genderArray.contains(gender))
                    {
                        int pos = genderArray.indexOf(gender);
                        genderEditText.setSelection(pos);
                    }


                    NGOID = languagePackageHashMap.get("ngoID");
                    APPID = languagePackageHashMap.get("appID");
                    try {
                        boardID = languagePackageHashMap.get("boardID");
                        classID = languagePackageHashMap.get("classID");
                        studentClass = languagePackageHashMap.get("studentClass");
                        if (classID.contains("_")) {
                            classID = classID.replace("_", " ");
                        }
                        if (classID.contains("Medical")) {
                            classID = classID.split(" ")[0] + " non medical/medical";
                        }
                        textViewBoard.setText(educationBoard.toUpperCase(Locale.ROOT));
//                        textViewClass.setText(classID.replace("_commerce","") + "th");
                        textViewClass.setText(Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        textViewBoard.setText(Util.getSelectedBoardName(context).toUpperCase(Locale.ROOT));
//                        textViewClass.setText(Util.getSelectedClassName(context).replace("_commerce",""));
                        textViewClass.setText(Util.getSelectedClass(context).replace("_"," ").replace("nonmedical medical","Non-med").replace("arts","Arts and Humanities").replace("arts","Arts and Humanities"));
                    }

                    ageEditText.setText(age);

                    language = languagePackageHashMap.get("language");
                    // languageEditText.setText(packageLanguage);
                    // languageEditText.setFocusable(false);


                    dob_ = languagePackageHashMap.get("dob");
                    textViewDateOfBirth.setText(dob);
                    userType = languagePackageHashMap.get("userType");
                    token = languagePackageHashMap.get("token");
                    city = languagePackageHashMap.get("city");
                    parentsMobile = languagePackageHashMap.get("parentsMobile");
                    parentsPhoneEditText.setText(parentsMobile);
                    state = languagePackageHashMap.get("state");
                    try {
                        date = languagePackageHashMap.get("dateStarted");
                        if (date == null) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            date = firebaseUser.getMetadata().getCreationTimestamp() + "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*Update UI */
                    addressEditText.setText(address);
                    if (city != null) {
                        if (!TextUtils.isEmpty(city)) {
//                            cityStateAutoComplete.setText(city);
                        }
                    }

                    if (state != null) {
                        if (!TextUtils.isEmpty(state)) {
//                            stateAtioComplete.setText(state);
                        }
                    }
                    // textViewLanguage.setText(languageString + " : " + Util.getSelectedLanguage(context));
                    //  textViewAge.setText(ageText + " : " + age);
//                    schoolEditText.setText(school);
                    emailEditText.setText(email);
                    //Util.setUsername(context, fullName);
                    if (dob_ != null) {
                        textViewDateOfBirth.setText(dob_ + "   ");
                    } else {
//                        textViewDateOfBirth.setText(dob + "       ");
                    }

                    nameEditText.setText(Util.getUsernameShowable(context));


                    try {
                        if (!TextUtils.isEmpty(mobile)) {
                            if (mobile.contains("-")) {
                                //   countries = new ArrayList<>(Arrays.asList(Util.COUNTRIES));
//                                Integer.parseInt(mobile.split("-")[0])
//                                String phone;
//                                try {
//                                    if (mobile.split("-")[0].contains("+")) {
//                                        phone = mobile.split("-")[0];
//                                    } else {
//                                        phone = "+" + mobile.split("-")[0];
//                                    }
//                                    Country country = getCountryByISO(phone);
//                                    textViewCountry.setText(country.getDialCode());
//                                    country_flag.setImageResource(country.getFlag());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                                if (!TextUtils.isEmpty(mobile.split("-")[1])) {
                                    mobileEditText.setText(mobile.split("-")[1]);
                                    mobileEditText.setFocusable(false);
                                } else {
                                    mobileEditText.setFocusable(true);
                                }

                            } else {
                                mobileEditText.setText(mobile);
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    /*Get userImage from the FireBase*/
                    try {
                        getUserProfileImage();
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public Country getCountryByISO(@NonNull String countryIsoCode) {
        Country country = new Country();
        for (int i = 0; i < countries.size(); i++) {
            if (countryIsoCode.equalsIgnoreCase(countries.get(i).getDialCode())) {
                country = countries.get(i);
                break;
            }
        }

        return country;
    }

    private void getUserProfileImage() throws Exception {
//        if (Util.getUserProfileUrl(context) != null) {
//
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.placeholder(R.mipmap.place_holder);
//            requestOptions.centerCrop();
//            Glide.with(context)
//                    .load(Util.getUserProfileUrl(context))
//                    .apply(requestOptions)
//                    .into(profileImageView);
//            return;
//        }
        reference.child(ApplicationConstants.USER_IMAGE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, String> languagePackageHashMap;
                    languagePackageHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                    try {
                        profileImage = languagePackageHashMap.get("profileImage");
                        Util.setUserProfileUrl(context, profileImage);
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.mipmap.place_holder);
                        requestOptions.centerCrop();
//                                requestOptions.error(R.drawable.ic_error);
                        Glide.with(context)
                                .load(profileImage)
                                .apply(requestOptions)
                                .into(profileImageView);


                    } catch (Exception e) {
                        profileImage = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    profileImage = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(Util.isOfflineMode(context)) {

            if (profileLink == null)  Glide.with(context).load(getResources().getDrawable(R.drawable.ic_avatar)).into(profileImageView);
            else {
                String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ profileLink+".png";
                File file = new File(filePath);

                Uri uri = null;

                if(file.exists()){
                    uri = Uri.fromFile(file);
                }

                if(uri != null){
                    Glide.with(context)
                            .load(uri).into(profileImageView);
                }
            }
        }
        else  reference.child(ApplicationConstants.USER_IMAGE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, String> languagePackageHashMap;
                    languagePackageHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                    try {
                        profileImage = languagePackageHashMap.get("profileImage");
                        Util.setUserProfileUrl(context, profileImage);
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.mipmap.place_holder);
                        requestOptions.centerCrop();
//                                requestOptions.error(R.drawable.ic_error);
                        Glide.with(context)
                                .load(profileImage)
                                .apply(requestOptions)
                                .into(profileImageView);


                    } catch (Exception e) {
                        profileImage = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    profileImage = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setProfileImageView(String url) throws Exception {
        ProfileImageModel profileImageModel = new ProfileImageModel();
        profileImageModel.setProfileImage(url);
        reference.child(ApplicationConstants.USER_IMAGE).setValue(profileImageModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                imageProgressBar.setVisibility(View.GONE);
                isUploadRunning = false;
                if (databaseError != null) {
                    Util.showToast(context, databaseError.getMessage());
                } else {
                    Util.showToast(context, Message);


                }
            }
        });
    }

    public void updateProfile() {
        HINT = "PROFILE";
        if (!Util.checkInternetConnection(context)) {
            Util.showInternetConnectioError(context);
            return;
        }
        reference.setValue(profileModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Util.dismissDialog();
                if (databaseError != null) {
                    Util.showToast(context, databaseError.getMessage());
                } else {
                    try {
                        Message = updatedMessage;
                        if (Util.getUserProfileUrl(context) != null) {

                            setProfileImageView(Util.getUserProfileUrl(context));
                        } else {
                            Util.showToast(context, Message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void showRateDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_share_rate);
        dialog.setCancelable(true);
        final TextView inputMObile = dialog.findViewById(R.id.inputMObile);
        inputMObile.setHint(enterMessage);

        final EditText rateEditText = dialog.findViewById(R.id.rateEditText);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView textViewRate = dialog.findViewById(R.id.textViewRate);
        TextView textTitle = dialog.findViewById(R.id.textTitle);
        inputMObile.setVisibility(View.VISIBLE);
        textViewRate.setText(done);
        textTitle.setText(rateThisApp);

        textViewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rating == 0) {
                    Util.showToast(context, rateErrorMessage);
                } else {
                    String message = rateEditText.getText().toString().trim();
                    try {
                        sendRatingToServer(message, rating + "", dialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v <= 4.0) {
                    inputMObile.setVisibility(View.VISIBLE);
                } else {
                    inputMObile.setVisibility(View.GONE);
                }
                rating = v;
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void moveToPlaystoreForRating() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void sendRatingToServer(String message, final String rate, final Dialog dialog) throws Exception {
        try {
            Util.showDialog(context);
            RateModel rateModel = new RateModel();
            rateModel.setMessage(message);
            rateModel.setRating(rate);
            global.getDatabaseReference().child(ApplicationConstants.RATING).child(Util.getUserId(context)).setValue(rateModel, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {
                        Util.showToast(context, databaseError.getMessage());


                    } else {

                    }
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Util.dismissDialog();
                    if (rating > 4.0) {
                        moveToPlaystoreForRating();
                    } else {
                        rateusSuccessDialog();
                    }
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
            Util.dismissDialog();
            dialog.dismiss();
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(this,
                R.style.AppThemeProfile, datePickerListener, year, month, day);
    }

    private Intent openFacebookIntent() {
        try {
            context.getPackageManager().getPackageInfo(ApplicationConstants.FACEBOOK_PACKAGE_NAME, 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(ApplicationConstants.FACEBOOK_PAGE_WEB_LINK));
        } catch (Exception e) {
            e.printStackTrace();
            return new Intent(Intent.ACTION_VIEW, Uri.parse(ApplicationConstants.FACEBOOK_PAGE_WEB_LINK));
        }
    }

    private void rateusSuccessDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_uploadsuccess);
        dialog.setCancelable(true);
        TextView titleTextView = dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(ratingThanks);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewOkay.setText(okay);
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    public void openDatePickerDialog() throws Exception {

        com.idreameducation.ipreppal.pal.datepicker.DatePicker mDatePickerDialogFragment;
        mDatePickerDialogFragment = new com.idreameducation.ipreppal.pal.datepicker.DatePicker();
        mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");

//        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, new DatePickerPopWin.OnDatePickedListener() {
//            @Override
//            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                dob_ = day + "/" + month + "/" + year;
//                textViewDateOfBirth.setText(dob_);
//                try {
//                    reference.child("dob").setValue(dob_);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).textConfirm(okay) //text of confirm button
//                .textCancel(cancel) //text of cancel button
//                .btnTextSize(16) // button text size
//                .viewTextSize(25) // pick view text size
//                .colorCancel(Color.parseColor("#999999")) //color of cancel button
//                .colorConfirm(Color.parseColor("#10bfe9"))//color of confirm button
//                .minYear(1900) //min year in loop
//                .maxYear(2550) // max year in loop
//                .dateChose("2013-11-11") // date chose when init popwindow
//                .build();
//
//        pickerPopWin.showPopWin(this);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar currentTime = Calendar.getInstance();
        int currentDate = currentTime.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentTime.get(Calendar.MONTH);
        int currentYear = currentTime.get(Calendar.YEAR);

        if (year > currentYear || (month == currentMonth && dayOfMonth >= currentDate)) {
            Toast.makeText(PalProfileActivity.this, "Selected time must be greater than or equal to the current time.", Toast.LENGTH_SHORT).show();

        } else {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
            Date d = new Date();
            CharSequence s  = DateFormat.getDateInstance(DateFormat.FULL).format(d.getTime());
            String age= s.toString();
            ageEditText.setText(String.valueOf((Integer.parseInt(age.substring(age.length()-4))-Integer.parseInt(selectedDate.substring(selectedDate.length()-4)))));
            textViewDateOfBirth.setText(selectedDate);
        }



    }

    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("Profile Screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }

                if (textArrayList.size() > 0) {
                    textViewProfile.setText(textArrayList.get(0));
                    inputMObile.setHint(textArrayList.get(1));
                    input_fullName.setHint(textArrayList.get(2));
                    inputEmail.setHint(textArrayList.get(3));
                    inputAddress.setHint(textArrayList.get(4));
                    inputSchool.setHint(textArrayList.get(5));
                    board = textArrayList.get(7);
                    sclass = textArrayList.get(6);
                    //  input_Board.setHint(board);
                    //  input_Class.setHint(sclass);
                    btnContinoue.setText(textArrayList.get(8));
                    //  textViewLikeThisApp.setText(textArrayList.get(9));
//                    rateTextView.setText(textArrayList.get(10));
//                    shareBtn.setText(textArrayList.get(11));
                    addPhoto = textArrayList.get(12);
                    takePhoto = textArrayList.get(13);
                    gallery = textArrayList.get(14);
                    cancel = textArrayList.get(15);
                    rateThisApp = textArrayList.get(16);
                    enterMessage = textArrayList.get(17);
                    //   languageString = textArrayList.get(18);
//                    input_Language.setHint(languageString);
                    updatedMessage = textArrayList.get(19);
                    ratingThanks = textArrayList.get(20);
                    changeMessage1 = textArrayList.get(21);
                    changeMessage2 = textArrayList.get(22);
                    alreadyRunningMessage = textArrayList.get(23);
                    enterName = textArrayList.get(24);
                    enterMobile = textArrayList.get(25);
                    enterEmail = textArrayList.get(26);
                    invalidEmail = textArrayList.get(27);
                    enterAddress = textArrayList.get(28);
                    enterSchool = textArrayList.get(29);
                    updatedImageMessage = textArrayList.get(30);
                    ageText = textArrayList.get(31);
                    dob = textArrayList.get(32);
                    okay = textArrayList.get(33);
                    inputCity.setHint(textArrayList.get(34));
                    notUpdated = textArrayList.get(35);
                    rateErrorMessage = textArrayList.get(36);
                    done = textArrayList.get(37);
                    rate = textArrayList.get(38);
                    message1 = textArrayList.get(39);
                    message2 = textArrayList.get(40);
                    message3 = textArrayList.get(41);
                    message4 = textArrayList.get(42);

                    permissionN = textArrayList.get(43);
                    permissionNMessage = textArrayList.get(44);
                    givePermissionMessage = textArrayList.get(45);
                    givePermission = textArrayList.get(46);
                    yes = textArrayList.get(47);
                    permissionBeforeTitle = textArrayList.get(48);
                    permissionBeforMessage = textArrayList.get(49);
                    grant = textArrayList.get(50);
                    message = textArrayList.get(51);
//                    btnReset.setText(textArrayList.get(52));
                    //  textViewUserInfo.setText(textArrayList.get(53));
                    uploadError = textArrayList.get(55);
                    title = textArrayList.get(56);
                    titleMessage = textArrayList.get(57);
                    deny = textArrayList.get(58);
                    progressMessage = textArrayList.get(59);
                    errorOccured = textArrayList.get(60);
                    futureDate = textArrayList.get(61);

                    inputDateOfBirth.setHint(dob);
                    textViewDateOfBirth.setText(notUpdated);
                    //textViewBoard.setText( Util.getSelectedBoardName(context));
                    //textViewClass.setText( Util.getSelectedClassName(context));
                    // textViewLanguage.setText(languageString + " : " + Util.getSelectedLanguage(context));
                    educationBoard = Util.getSelectedBoardName(context);
                    studentClass = Util.getSelectedClassName(context);
                    language = Util.getSelectedLanguage(context);
                    try {
                        getUserProfile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.dismissDialog();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showDialog(context);

            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Profile Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textViewProfile.setText(textArrayList.get(0));
                            inputMObile.setHint(textArrayList.get(1));
                            input_fullName.setHint(textArrayList.get(2));
                            inputEmail.setHint(textArrayList.get(3));
                            inputAddress.setHint(textArrayList.get(4));
                            inputSchool.setHint(textArrayList.get(5));
                            board = textArrayList.get(7);
                            sclass = textArrayList.get(6);
                            //  input_Board.setHint(board);
                            //  input_Class.setHint(sclass);
                            btnContinoue.setText(textArrayList.get(8));
                            //  textViewLikeThisApp.setText(textArrayList.get(9));
//                    rateTextView.setText(textArrayList.get(10));
//                    shareBtn.setText(textArrayList.get(11));
                            addPhoto = textArrayList.get(12);
                            takePhoto = textArrayList.get(13);
                            gallery = textArrayList.get(14);
                            cancel = textArrayList.get(15);
                            rateThisApp = textArrayList.get(16);
                            enterMessage = textArrayList.get(17);
                            //   languageString = textArrayList.get(18);
//                    input_Language.setHint(languageString);
                            updatedMessage = textArrayList.get(19);
                            ratingThanks = textArrayList.get(20);
                            changeMessage1 = textArrayList.get(21);
                            changeMessage2 = textArrayList.get(22);
                            alreadyRunningMessage = textArrayList.get(23);
                            enterName = textArrayList.get(24);
                            enterMobile = textArrayList.get(25);
                            enterEmail = textArrayList.get(26);
                            invalidEmail = textArrayList.get(27);
                            enterAddress = textArrayList.get(28);
                            enterSchool = textArrayList.get(29);
                            updatedImageMessage = textArrayList.get(30);
                            ageText = textArrayList.get(31);
                            dob = textArrayList.get(32);
                            okay = textArrayList.get(33);
                            inputCity.setHint(textArrayList.get(34));
                            notUpdated = textArrayList.get(35);
                            rateErrorMessage = textArrayList.get(36);
                            done = textArrayList.get(37);
                            rate = textArrayList.get(38);
                            message1 = textArrayList.get(39);
                            message2 = textArrayList.get(40);
                            message3 = textArrayList.get(41);
                            message4 = textArrayList.get(42);

                            permissionN = textArrayList.get(43);
                            permissionNMessage = textArrayList.get(44);
                            givePermissionMessage = textArrayList.get(45);
                            givePermission = textArrayList.get(46);
                            yes = textArrayList.get(47);
                            permissionBeforeTitle = textArrayList.get(48);
                            permissionBeforMessage = textArrayList.get(49);
                            grant = textArrayList.get(50);
                            message = textArrayList.get(51);
//                    btnReset.setText(textArrayList.get(52));
                            //  textViewUserInfo.setText(textArrayList.get(53));
                            uploadError = textArrayList.get(55);
                            title = textArrayList.get(56);
                            titleMessage = textArrayList.get(57);
                            deny = textArrayList.get(58);
                            progressMessage = textArrayList.get(59);
                            errorOccured = textArrayList.get(60);
                            futureDate = textArrayList.get(61);

                            inputDateOfBirth.setHint(dob);
                            textViewDateOfBirth.setText(notUpdated);
                            //textViewBoard.setText( Util.getSelectedBoardName(context));
                            //textViewClass.setText( Util.getSelectedClassName(context));
                            // textViewLanguage.setText(languageString + " : " + Util.getSelectedLanguage(context));
                            educationBoard = Util.getSelectedBoardName(context);
                            studentClass = Util.getSelectedClassName(context);
                            language = Util.getSelectedLanguage(context);
                            try {
                                getUserProfile();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Util.dismissDialog();
                            }
                        } else {
                            Util.dismissDialog();
                        }

                    } catch (Exception e) {
                        Util.dismissDialog();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


    }

    private void dialogBeforePermission() throws Exception {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_permission_information);
        dialog.setCancelable(true);
        TextView textViewContinue = dialog.findViewById(R.id.okayButton);
        textViewContinue.setText(okay);
        TextView denyButton = dialog.findViewById(R.id.denyButton);
        denyButton.setText(deny);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView textTitle = dialog.findViewById(R.id.textTitle);
        textTitle.setText(title);
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        textMessage.setText(titleMessage);
        textViewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Util.setPermissonGranted(context, true);
                if (!isUploadRunning) {
                    boolean aa = Util.checkPermission(PalProfileActivity.this, yes, permissionN, permissionNMessage);
                    if (aa) {
                        showImagePickerOptions();
                    }
                } else {
                    Util.showToast(context, alreadyRunningMessage);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(PalProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(PalProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    //    loadProfile(uri.toString());
                    profileImageView.setImageBitmap(bitmap);
                    URI = uri;
                    uploadImage(uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Util.showToast(context, errorOccured);
        }

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

    private void getAppIDDetails() throws Exception {
        isloading=true;
        checkConnection(false);
        String tabID = Util.getAndroidId(context);
        global.getDatabaseReference().child("app_tab_relation").child(tabID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        String appID = map.get("appID");
                        textViewUserName.setText("App id: " + appID);
                        Util.setAPPID(context, appID);
                        hideconnection_layout();
                    } else {

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

    // region Comparators
    public static class ISOCodeComparator implements Comparator<Country> {
        @Override
        public int compare(Country country, Country nextCountry) {
            return country.getDialCode().compareToIgnoreCase(nextCountry.getDialCode());
        }
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }


}