package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
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
import com.idreameducation.ipreppal.pal.adapter.PalReportPagerDetailedAdapter;
import com.idreameducation.ipreppal.pal.adapter.ReportsListAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWisePracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWisePracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseVideoRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;


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
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class ReportListActivity extends AppCompatActivity {
    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private TextView textViewHead;
    private TextView title,noReportsTextView,noReportsTextViewDetail,startLearningTextView;
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private String type;
    private String topicID;
    private String subject;
    private String sClass;
    private String board;
    private DatePickerDialog datePickerDialog;
    private Button buttonDate;

    private ViewPager pager;
    private TabLayout tabs;
    private String section;
    private String no_reports_to_show,your_usage_reports,start_learning;
    private ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;
    private ReportsTopicWisePracticeRepository reportsTopicWisePracticeRepository;
    private ReportsTopicWiseTestRepository reportsTopicWiseTestRepository;

    private Disposable topicWisePracticeDisposable;
    private Disposable topicWiseVideoDisposable;
    private Disposable topicWiseTestDisposable;

    private RelativeLayout reletiveNoReports;
    private LinearLayout linearVisible;


    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_activity_reports_list);

        assignIds();
        listners();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.setImageResource(R.drawable.ic_inactive);
            isFabOpen = false;

        } else {

            fab.setImageResource(R.drawable.ic_active);
            isFabOpen = true;

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
                            Util.openGifDialogue(context,"Some error occur !");
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
                animateFAB();
            }
        });

        findViewById(R.id.buttonDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void assignIds() {
        context = this;
        Util.setContext(context);
        global = (Global) getApplicationContext();
        title = findViewById(R.id.title);
        buttonDate = findViewById(R.id.buttonDate);
        textViewHead = findViewById(R.id.textViewHead);
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        recyclerView.setLayoutManager(manager);
        type = getIntent().getStringExtra("type");
        section = getIntent().getStringExtra("section");
        try {
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        noReportsTextView = findViewById(R.id.noReportsTextView);

        noReportsTextView.setText(no_reports_to_show);
        noReportsTextViewDetail = findViewById(R.id.noReportsTextViewDetail);
        noReportsTextViewDetail.setText(your_usage_reports);
        startLearningTextView = findViewById(R.id.startLearningTextView);
        noReportsTextViewDetail.setText(start_learning);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        startLearningTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.preventTwoClick(v);
                Intent intent = new Intent(context, PracticeTopicActivity.class);
                intent.putExtra("board", Util.getSelectedBoard(context));
                intent.putExtra("class", Util.getSelectedClass(context));
                startActivity(intent);
            }
        });


        //((PalReportsActivity)context).video_watched_text)

        reletiveNoReports = findViewById(R.id.reletiveNoReports);
        linearVisible = findViewById(R.id.linearVisible);
        linearVisible.setVisibility(View.VISIBLE);

        reportsTopicWisePracticeRepository = new ReportsTopicWisePracticeRepository(context);
        reportsTopicWiseVideoRepository = new ReportsTopicWiseVideoRepository(context);
        reportsTopicWiseTestRepository = new ReportsTopicWiseTestRepository(context);

        // hideNavigationBar(getWindow());

        title.setText(section);
        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        subject = getIntent().getStringExtra("subject");
        topicID = getIntent().getStringExtra("topicID");

        sClass = Util.getSelectedClass(context);
        board = Util.getSelectedBoard(context);
        pager = findViewById(R.id.pagers);
        tabs = findViewById(R.id.tab_view);

        textViewHead.setText(type);
        String date = Util.getCurrentDateWithDifferentFormat();
        buttonDate.setText(date);
        getReportsListing(date);
        get_support_dialog_language();

    }

    private void getReportsListing(String date) {

        if(Util.isOfflineMode(context)){
            if(type.equals("practice")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "practiceTopicWiseTask", date);
            }else if(type.equals("video_lessons")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "videoTopicWiseTask", date);
            }
            else if(type.equals("books_stories")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "books_stories", date);
            }else if(type.equals("books_ncert")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "books_ncert", date);
            }
            else if(type.equals("diksha_content")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "videoTopicWiseTask", date);
            }else if(type.equals("project_video")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "videoTopicWiseTask", date);
            }else if(type.equals("simulation_content")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "videoTopicWiseTask", date);
            }else if(type.equals("diagonostic_test") || type.equals("simple_test") || type.equals("bi_monthly_test")){
                runBackgroundTask(Util.getUserId(context), board, sClass, type, "testTopicWiseTask", date);}
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise")
                    .child(Util.getUserId(context)).child(board).child(sClass)
                    .child(Util.getSelectedLanguage(context))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public int hashCode() {
                            return super.hashCode();
                        }

                        @Override
                        public boolean equals(@Nullable Object obj) {
                            return super.equals(obj);
                        }

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    buttonDate.setText(date);
                                    HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                                    ArrayList<HashMap<String, Object>> array = new ArrayList<>();
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    for (String subject : map.keySet()) {
                                        HashMap<String, Object> categoryMap = (HashMap<String, Object>) map.get(subject);
                                        HashMap<String, Object> subjectMap = (HashMap<String, Object>) categoryMap.get(type);
                                        subject=subject.replace("_"," ");
                                        if(subjectMap != null) {
                                            subjectArrayList.add(subject);
                                            for (String date : subjectMap.keySet()) {
                                                dateArrayList.add(subject + "_" + date);
                                                HashMap<String, Object> innerMap = (HashMap<String, Object>) subjectMap.get(date);
                                                HashMap<String, Object> aa = new HashMap<>();
                                                HashMap<String, Object> pp = new HashMap<>();
                                                String tName;
                                                for (String topicID : innerMap.keySet()) {
                                                    HashMap<String, Object> topicMap = (HashMap<String, Object>) innerMap.get(topicID);
                                                    HashMap<String, Object> dataMap = (HashMap<String, Object>) topicMap.get("detail");
                                                    tName = (String) topicMap.get("name");
                                                    nameArrayList.add(subject + "_" + tName);
                                                    // HashMap<String, Object> map_ = (HashMap<String, Object>) dataMap.get(date);
                                                    pp.put(topicID, sortHashMapByKey(dataMap));
                                                    aa.put(subject + "_" + date, pp);
                                                }
                                                mainArrayList.add(aa);
                                            }
                                        }
                                    }
                                    pager.setOffscreenPageLimit(subjectArrayList.size());
                                    //Adding the tabs using addTab() method

                                    if (subjectArrayList.size()==0) {
                                        reletiveNoReports.setVisibility(View.VISIBLE);
                                        linearVisible.setVisibility(View.GONE);
                                    }
                                    else {
                                        for (int i = 0; i < subjectArrayList.size(); i++) {
                                            tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                        }

                                        Collections.sort(dateArrayList);
                                        Collections.reverse(dateArrayList);
                                        PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
                                        pager.setAdapter(adapter);
                                        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                            @Override
                                            public void onTabSelected(TabLayout.Tab tab) {
                                                pager.setCurrentItem(tab.getPosition());
                                            }

                                            @Override
                                            public void onTabUnselected(TabLayout.Tab tab) {

                                            }

                                            @Override
                                            public void onTabReselected(TabLayout.Tab tab) {

                                            }
                                        });
                                    }
                                    Log.i("Data :  ", mainArrayList.toString());
                                }
                                else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
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

    public static HashMap<String, Object> sortHashMapByKey(HashMap<String, Object> hashMap) {
        Set<String> keys = hashMap.keySet();
        List<String> timestampStrings = new ArrayList<>(keys);
        Collections.sort(timestampStrings);

        HashMap<String, Object> sortedTimestampMap = new HashMap<>();
        for (String timestamp : timestampStrings) {
            sortedTimestampMap.put(timestamp, hashMap.get(timestamp));
        }

        // not working----> HashMap not supported
        return sortedTimestampMap;
    }

    private void runBackgroundTask(String userId, String board, String sClass, String type, String taskType, String date){
        if(taskType.equals("practiceTopicWiseTask")){
            getList(userId, board, sClass, type, taskType).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            topicWisePracticeDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTopicWisePracticeModel> list = (ArrayList<ReportsTopicWisePracticeModel>) o;
                                if(list != null && list.size() > 0){
                                    buttonDate.setText(date);
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    for(ReportsTopicWisePracticeModel item: list){

                                        if(!subjectArrayList.contains(item.getSubjectName())) subjectArrayList.add(item.getSubjectName());
                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate())) dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName())) nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());

                                        HashMap<String, Object> mainMap = new HashMap<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        HashMap<String, String> dataMapValue = new HashMap<>();
                                        dataMapValue.put("currentLevel", item.getCurrentLevel());
                                        dataMapValue.put("date", item.getDate());
                                        dataMapValue.put("topicName", item.getTopicName());
                                        dataMapValue.put("mastery", item.getMastery());
                                        dataMapValue.put("streakProgress", item.getStreakProgress());
                                        dataMap.put(String.valueOf(item.getTime()), dataMapValue);

//                                        ArrayList<HashMap<String, Object>> innerMap;

                                        boolean added=false;

                                        for(int i=mainArrayList.size()-1;i>=0;i--)
                                        {
                                            if(mainArrayList.get(i).containsKey(item.getSubjectName() + "_" + item.getDate()))
                                            {
                                                mainMap=mainArrayList.get(i);
                                                HashMap<String,Object> m = (HashMap<String, Object>) mainMap.get(item.getSubjectName() + "_" + item.getDate());
                                                if(m.containsKey(item.getTopicId()))
                                                {
                                                    HashMap<String,Object> map2= (HashMap<String, Object>) m.get(item.getTopicId());
                                                    map2.put(String.valueOf(item.getTime()), dataMapValue);
                                                    map.put(item.getTopicId(), map2);
                                                    mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }
                                                else
                                                {
                                                    m.put(item.getTopicId(), dataMap);
//                                                    mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                    mainArrayList.add(m);
                                                    added=true;
                                                }



                                            }
                                            else if(i==0 && !added)
                                            {
                                                map.put(item.getTopicId(), dataMap);
                                                mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                mainArrayList.add(mainMap);
                                            }
                                        }

                                        if(mainArrayList.size()==0)
                                        {
                                            map.put(item.getTopicId(), dataMap);
                                            mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                            mainArrayList.add(mainMap);
                                        }


                                    }
                                    pager.setOffscreenPageLimit(subjectArrayList.size());
                                    //Adding the tabs using addTab() method
                                    for (int i = 0; i < subjectArrayList.size(); i++) {
                                        tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                    }

                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
                                    pager.setAdapter(adapter);
                                    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                    tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                    tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                        @Override
                                        public void onTabSelected(TabLayout.Tab tab) {
                                            pager.setCurrentItem(tab.getPosition());
                                        }

                                        @Override
                                        public void onTabUnselected(TabLayout.Tab tab) {

                                        }

                                        @Override
                                        public void onTabReselected(TabLayout.Tab tab) {

                                        }
                                    });
                                }else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            topicWisePracticeDisposable.dispose();
                        }
                    });
        }
        else if(taskType.equals("videoTopicWiseTask")){
            getList(userId, board, sClass, type, taskType).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            topicWiseVideoDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTopicWiseVideoModel> list = (ArrayList<ReportsTopicWiseVideoModel>) o;
                                if(list != null && list.size() > 0){
                                    buttonDate.setText(date);
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList1 = new ArrayList<>();

                                    ArrayList<HashMap<String,Object>> mainlist=new ArrayList<>();


                                    HashMap<String, Object> datewisereport=new HashMap<>();
                                    HashMap<String, Object> topicwisereport=new HashMap<>();

                                    for(int p=list.size()-1;p>=0;p--) {
                                        ReportsTopicWiseVideoModel item = list.get(p);

                                        if(!subjectArrayList.contains(item.getSubjectName())) subjectArrayList.add(item.getSubjectName());
                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate())) dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName())) nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());


                                        HashMap<String, Object> mainMap = new HashMap<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        HashMap<String, String> dataMapValue = new HashMap<>();
                                        dataMapValue.put("totalTime", item.getTotalTime());
                                        dataMapValue.put("videoName", item.getVideoName());
                                        dataMapValue.put("topicName", item.getTopicName());
                                        dataMapValue.put("subjectName", item.getSubjectName());
                                        dataMapValue.put("date", item.getDate());
                                        dataMapValue.put("time", item.getVTime());
                                        dataMapValue.put("videoId", item.getVideoId());

                                        Random r = new Random();
                                        int i1 = r.nextInt(50000 - 1) + 1;


                                        boolean added=false;

                                        for(int i=mainArrayList.size()-1;i>=0;i--)
                                        {
                                            if(mainArrayList.get(i).containsKey(item.getSubjectName() + "_" + item.getDate()))
                                            {
                                                mainMap=mainArrayList.get(i);
                                                HashMap<String,Object> m = (HashMap<String, Object>) mainMap.get(item.getSubjectName() + "_" + item.getDate());
                                                if(m.containsKey(item.getTopicId()))
                                                {
                                                    HashMap<String,Object> map2= (HashMap<String, Object>) m.get(item.getTopicId());
//                                                    map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                    if(map2.containsKey(item.getVideoId()))
                                                    {
                                                        HashMap<String,Object> map3= (HashMap<String, Object>) map2.get(item.getVideoId());
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }
                                                    else
                                                    {
//
//                                                        HashMap<String,Object> map3= new HashMap<>();
//                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);
//                                                        map2.put(item.getVideoId(), map3);
//                                                        m.put(item.getTopicId(), map2);
//                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), m);
//                                                        mainArrayList.remove(i);
//                                                        mainArrayList.add(mainMap);
//                                                        added=true;

                                                        HashMap<String,Object> map3= new HashMap<>();
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }


                                                }
                                                else
                                                {
                                                    HashMap<String,Object> map11=new HashMap<>();
                                                    HashMap<String,Object> map22=new HashMap<>();

                                                    map22.put(String.valueOf(item.getTime()),dataMapValue);
                                                    map11.put(item.getVideoId(),map22);
                                                    m.put(item.getTopicId(), map11);
                                                    mainMap.put(item.getSubjectName() + "_" + item.getDate(),m );
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }



                                            }
                                            else if(i==0 && !added)
                                            {
                                                HashMap<String,Object> map2= new HashMap<>();
                                                map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                HashMap<String,Object> map3= new HashMap<>();
                                                map3.put(item.getVideoId(), map2);
                                                map.put(item.getTopicId(), map3);
                                                mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                mainArrayList.add(mainMap);
                                            }
                                        }

                                        if(mainArrayList.size()==0)
                                        {
                                            HashMap<String,Object> map2= new HashMap<>();
                                            map2.put(String.valueOf(item.getTime()), dataMapValue);

                                            HashMap<String,Object> map3= new HashMap<>();
                                            map3.put(item.getVideoId(), map2);
                                            map.put(item.getTopicId(), map3);
                                            mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                            mainArrayList.add(mainMap);
                                        }


                                        if(p==0)
                                        {

                                            pager.setOffscreenPageLimit(subjectArrayList.size());
                                            //Adding the tabs using addTab() method
                                            for (int i = 0; i < subjectArrayList.size(); i++) {
                                                tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                            }
                                            if(type.equals("project_video")) Collections.reverse(nameArrayList);
                                            if(type.equals("simulation_content")) Collections.reverse(nameArrayList);
                                            PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
//                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList,mainArrayList1, nameArrayList, type);
                                            pager.setAdapter(adapter);
                                            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                            tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                            tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                @Override
                                                public void onTabSelected(TabLayout.Tab tab) {
                                                    pager.setCurrentItem(tab.getPosition());
                                                }

                                                @Override
                                                public void onTabUnselected(TabLayout.Tab tab) {

                                                }

                                                @Override
                                                public void onTabReselected(TabLayout.Tab tab) {

                                                }
                                            });


                                        }



                                    }

                                }else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            topicWiseVideoDisposable.dispose();
                        }
                    });
        }
        else if(taskType.equals("books_stories")){
            getList(userId, board, sClass, "books_stories", "videoTopicWiseTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            topicWiseVideoDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTopicWiseVideoModel> list = (ArrayList<ReportsTopicWiseVideoModel>) o;
                                if(list != null && list.size() > 0){
                                    buttonDate.setText(date);
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList1 = new ArrayList<>();

                                    ArrayList<HashMap<String,Object>> mainlist=new ArrayList<>();

//                                    for(int p=list.size()-1;p>=0;p--)
//                                    {
//                                        ReportsTopicWiseVideoModel item = list.get(p);
//
//                                        if(!subjectArrayList.contains(item.getSubjectName())) subjectArrayList.add(item.getSubjectName());
//                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate())) dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
//                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName())) nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());
//
//
//                                        HashMap<String, Object> mainMap = new HashMap<>();
//                                        HashMap<String, Object> map = new HashMap<>();
//                                        HashMap<String, Object> dataMap = new HashMap<>();
//                                        HashMap<String, String> dataMapValue = new HashMap<>();
//                                        dataMapValue.put("totalTime", item.getTotalTime());
//                                        dataMapValue.put("videoName", item.getVideoName());
//                                        dataMapValue.put("topicName", item.getTopicName());
//                                        dataMapValue.put("subjectName", item.getSubjectName());
//                                        dataMapValue.put("date", item.getDate());
//                                        dataMapValue.put("time", item.getVTime());
//                                        dataMapValue.put("topicid", item.getTopicId());
//
//                                        map.put(item.getTopicId(), dataMap);
//                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
//
//                                        mainArrayList.add(mainMap);
//                                        mainArrayList1.add(dataMap);
//
//                                        if(p==0)
//                                        {
//
//                                            pager.setOffscreenPageLimit(subjectArrayList.size());
//                                            //Adding the tabs using addTab() method
//                                            for (int i = 0; i < subjectArrayList.size(); i++) {
//                                                tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
//                                            }
//
//
//                                            PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
////                                          PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList,mainArrayList1, nameArrayList, type);
//                                            pager.setAdapter(adapter);
//                                            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
//
//                                            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
//
//                                            tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                                                @Override
//                                                public void onTabSelected(TabLayout.Tab tab) {
//                                                    pager.setCurrentItem(tab.getPosition());
//                                                }
//
//                                                @Override
//                                                public void onTabUnselected(TabLayout.Tab tab) {
//
//                                                }
//
//                                                @Override
//                                                public void onTabReselected(TabLayout.Tab tab) {
//
//                                                }
//                                            });
//
//
//                                        }
//
//                                    }

//                                     new code


//                                    HashMap<String, Object> datewisereport=new HashMap<>();
//                                    HashMap<String,Object> topicwisereport=new HashMap<>();
//                                    ArrayList<HashMap<String,Object>> m=new ArrayList<>();
//
//                                    for(int p=list.size()-1;p>=0;p--)
//                                    {
//                                        ReportsTopicWiseVideoModel item = list.get(p);
//
//                                        if(!subjectArrayList.contains(item.getSubjectName()))
//                                        {
//                                            subjectArrayList.add(item.getSubjectName());
//                                        }
//
//                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate()))
//                                        {
//                                            dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
//                                        }
//
//                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName()))
//                                        {
//                                            nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());
//                                        }
//
//                                        HashMap<String, Object> mainMap = new HashMap<>();
//                                        HashMap<String, Object> map = new HashMap<>();
//                                        HashMap<String, Object> dataMap = new HashMap<>();
//                                        HashMap<String, String> dataMapValue = new HashMap<>();
//                                        dataMapValue.put("totalTime", item.getTotalTime());
//                                        dataMapValue.put("videoName", item.getVideoName());
//                                        dataMapValue.put("topicName", item.getTopicName());
//                                        dataMapValue.put("subjectName", item.getSubjectName());
//                                        dataMapValue.put("date", item.getDate());
//                                        dataMapValue.put("time", item.getVTime());
//
//                                        HashMap<String, Object> datewise=new HashMap<>();
//                                        HashMap<String,Object> topicwise=new HashMap<>();
//
//                                        Random r = new Random();
//                                        int i1 = r.nextInt(50000 - 1) + 1;
//
//
//                                        if(datewisereport.containsKey(item.getSubjectName() + "_" + item.getDate()))
//                                        {
//                                            datewise = (HashMap<String, Object>) datewisereport.get(item.getSubjectName() + "_" + item.getDate());
//
//                                            if(topicwise.containsKey(item.getTopicName()))
//                                            {
//                                                topicwise = (HashMap<String, Object>) topicwise.get(item.getTopicName());
//
//
//                                                // added in topicwise
//                                                topicwise.put(item.getTopicName(),dataMapValue);
////                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//                                            else
//                                            {
//                                                // added in topicwise
//                                                topicwise.put(item.getTopicName(),dataMapValue);
////                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//
//                                            // added in datewise
//                                            datewise.put(item.getTopicId(),topicwise);
//                                            datewisereport.put(item.getSubjectName() + "_" + item.getDate(),datewise);
//
//
//                                        }
//                                        else
//                                        {
//
//                                            if(topicwise.containsKey(item.getTopicName()))
//                                            {
//                                                topicwise = (HashMap<String, Object>) topicwise.get(item.getTopicName());
//
//                                                // added in topicwise
////                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                                topicwise.put(item.getTopicName(),dataMapValue);
//                                            }
//                                            else
//                                            {
//                                                // added in topicwise
//                                                topicwise.put(item.getTopicName(),dataMapValue);
////                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//
//                                            // added in datewise
//                                            datewise.put(item.getTopicId(),topicwise);
//                                            datewisereport.put(item.getSubjectName() + "_" + item.getDate(),datewise);
//                                            m.add(datewisereport);
//                                        }
//
//
//
//
//                                        HashMap<String, Object> dataMapValue11 = new HashMap<>();
//                                        dataMapValue11.put(String.valueOf(i1),dataMapValue);
//                                        dataMap.put(String.valueOf(item.getTopicId()), dataMapValue11);
//
//
//                                        map.put(item.getTopicId(), dataMap);
//                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
//
//                                        mainArrayList.add(mainMap);
//                                        mainArrayList1.add(dataMap);
//
//                                        if(p==0)
//                                        {
//
//                                            pager.setOffscreenPageLimit(subjectArrayList.size());
//                                            //Adding the tabs using addTab() method
//                                            for (int i = 0; i < subjectArrayList.size(); i++) {
//                                                tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
//                                            }
//
//
//                                            System.out.println("!!!!!! o  dateArrayList    "+dateArrayList);
//                                            System.out.println("!!!!!! o  subjectArrayList "+subjectArrayList);
//                                            System.out.println("!!!!!! o  mainArrayList    "+mainArrayList);
//                                            System.out.println("!!!!!! o  nameArrayList    "+nameArrayList);
//                                            System.out.println("!!!!!! o  type             "+type);
//
////                                            m.add(datewisereport);
//
//                                            System.out.println("!!!!!! o  datewise         "+m);
//
//
//                                            PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
////                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList,mainArrayList1, nameArrayList, type);
//                                            pager.setAdapter(adapter);
//                                            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
//
//                                            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
//
//                                            tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                                                @Override
//                                                public void onTabSelected(TabLayout.Tab tab) {
//                                                    pager.setCurrentItem(tab.getPosition());
//                                                }
//
//                                                @Override
//                                                public void onTabUnselected(TabLayout.Tab tab) {
//
//                                                }
//
//                                                @Override
//                                                public void onTabReselected(TabLayout.Tab tab) {
//
//                                                }
//                                            });
//
//
//                                        }
//
//                                    }



//                                     backup
//
//
//                                       subject
//                                             date
//                                                  chapter
//                                                      topic & its details
//                                             date
//                                                  chapter
//                                                      topic & its details

                                    HashMap<String, Object> datewisereport=new HashMap<>();
                                    HashMap<String, Object> topicwisereport=new HashMap<>();

                                    for(int p=list.size()-1;p>=0;p--) {
                                        ReportsTopicWiseVideoModel item = list.get(p);

                                        if(!subjectArrayList.contains(item.getSubjectName())) subjectArrayList.add(item.getSubjectName());
                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate())) dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName())) nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());


                                        HashMap<String, Object> mainMap = new HashMap<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        HashMap<String, String> dataMapValue = new HashMap<>();
                                        dataMapValue.put("totalTime", item.getTotalTime());
                                        dataMapValue.put("videoName", item.getVideoName());
                                        dataMapValue.put("topicName", item.getTopicName());
                                        dataMapValue.put("subjectName", item.getSubjectName());
                                        dataMapValue.put("date", item.getDate());
                                        dataMapValue.put("time", item.getVTime());
                                        dataMapValue.put("videoId", item.getVideoId());

//                                        ArrayList<HashMap<String, Object>> datewise=new ArrayList<>();
//                                        ArrayList<HashMap<String, String>> topicwise=new ArrayList<>();
//
//
//                                        if(datewisereport.containsKey(item.getDate()))
//                                        {
//                                            datewise = (ArrayList<HashMap<String, Object>>) datewisereport.get(item.getDate());
//
//                                            if(topicwisereport.containsKey(item.getTopicId()))
//                                            {
//                                                topicwise = (ArrayList<HashMap<String, String>>) topicwisereport.get(item.getTopicId());
//
//
//                                                // added in topicwise
//                                                topicwise.add(dataMapValue);
//                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//                                            else
//                                            {
//                                                // added in topicwise
//                                                topicwise.add(dataMapValue);
//                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//
//                                            // added in datewise
//                                            datewise.add(topicwisereport);
//                                            datewisereport.put(item.getDate(),datewise);
//
//                                        }
//                                        else
//                                        {
//
//                                            if(topicwisereport.containsKey(item.getTopicId()))
//                                            {
//                                                topicwise = (ArrayList<HashMap<String, String>>) topicwisereport.get(item.getTopicId());
//
//
//                                                // added in topicwise
//                                                topicwise.add(dataMapValue);
//                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//                                            else
//                                            {
//                                                // added in topicwise
//                                                topicwise.add(dataMapValue);
//                                                topicwisereport.put(item.getTopicId(),topicwise);
//                                            }
//
//
//                                            // added in datewise
//                                            datewise.add(topicwisereport);
//                                            datewisereport.put(item.getDate(),datewise);
//
//                                        }

                                        Random r = new Random();
                                        int i1 = r.nextInt(50000 - 1) + 1;

//                                        HashMap<String, Object> dataMapValue11 = new HashMap<>();
//                                        dataMapValue11.put(String.valueOf(i1),dataMapValue);
//                                        dataMap.put(String.valueOf(item.getTopicId()), dataMapValue11);
//
//
//                                        map.put(item.getTopicId(), dataMap);
//                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), dataMap);
//
//                                        mainArrayList.add(mainMap);
//                                        mainArrayList1.add(dataMap);
                                        boolean added=false;

                                        for(int i=mainArrayList.size()-1;i>=0;i--)
                                        {
                                            if(mainArrayList.get(i).containsKey(item.getSubjectName() + "_" + item.getDate()))
                                            {
                                                mainMap=mainArrayList.get(i);
                                                HashMap<String,Object> m = (HashMap<String, Object>) mainMap.get(item.getSubjectName() + "_" + item.getDate());
                                                if(m.containsKey(item.getTopicId()))
                                                {
                                                    HashMap<String,Object> map2= (HashMap<String, Object>) m.get(item.getTopicId());
//                                                    map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                    if(map2.containsKey(item.getVideoId()))
                                                    {
                                                        HashMap<String,Object> map3= (HashMap<String, Object>) map2.get(item.getVideoId());
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }
                                                    else
                                                    {
//
//                                                        HashMap<String,Object> map3= new HashMap<>();
//                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);
//                                                        map2.put(item.getVideoId(), map3);
//                                                        m.put(item.getTopicId(), map2);
//                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), m);
//                                                        mainArrayList.remove(i);
//                                                        mainArrayList.add(mainMap);
//                                                        added=true;

                                                        HashMap<String,Object> map3= new HashMap<>();
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }


                                                }
                                                else
                                                {
                                                    HashMap<String,Object> map11=new HashMap<>();
                                                    HashMap<String,Object> map22=new HashMap<>();

                                                    map22.put(String.valueOf(item.getTime()),dataMapValue);
                                                    map11.put(item.getVideoId(),map22);
                                                    m.put(item.getTopicId(), map11);
                                                    mainMap.put(item.getSubjectName() + "_" + item.getDate(),m );
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }



                                            }
                                            else if(i==0 && !added)
                                            {
                                                HashMap<String,Object> map2= new HashMap<>();
                                                map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                HashMap<String,Object> map3= new HashMap<>();
                                                map3.put(item.getVideoId(), map2);
                                                map.put(item.getTopicId(), map3);
                                                mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                mainArrayList.add(mainMap);
                                            }
                                        }

                                        if(mainArrayList.size()==0)
                                        {
                                            HashMap<String,Object> map2= new HashMap<>();
                                            map2.put(String.valueOf(item.getTime()), dataMapValue);

                                            HashMap<String,Object> map3= new HashMap<>();
                                            map3.put(item.getVideoId(), map2);
                                            map.put(item.getTopicId(), map3);
                                            mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                            mainArrayList.add(mainMap);
                                        }


                                        if(p==0)
                                        {

                                            pager.setOffscreenPageLimit(subjectArrayList.size());
                                            //Adding the tabs using addTab() method
                                            for (int i = 0; i < subjectArrayList.size(); i++) {
                                                tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                            }
                                            if(type.equals("project_video")) Collections.reverse(nameArrayList);
                                            if(type.equals("simulation_content")) Collections.reverse(nameArrayList);
                                            PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
//                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList,mainArrayList1, nameArrayList, type);
                                            pager.setAdapter(adapter);
                                            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                            tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                            tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                @Override
                                                public void onTabSelected(TabLayout.Tab tab) {
                                                    pager.setCurrentItem(tab.getPosition());
                                                }

                                                @Override
                                                public void onTabUnselected(TabLayout.Tab tab) {

                                                }

                                                @Override
                                                public void onTabReselected(TabLayout.Tab tab) {

                                                }
                                            });


                                        }



                                    }

                                }else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            topicWiseVideoDisposable.dispose();
                        }
                    });
        }
        else if(taskType.equals("books_ncert")){
            getList(userId, board, sClass, "books_ncert", "videoTopicWiseTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            topicWiseVideoDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTopicWiseVideoModel> list = (ArrayList<ReportsTopicWiseVideoModel>) o;
                                if(list != null && list.size() > 0){
                                    buttonDate.setText(date);
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList1 = new ArrayList<>();

                                    ArrayList<HashMap<String,Object>> mainlist=new ArrayList<>();


                                    HashMap<String, Object> datewisereport=new HashMap<>();
                                    HashMap<String, Object> topicwisereport=new HashMap<>();

                                    for(int p=list.size()-1;p>=0;p--) {
                                        ReportsTopicWiseVideoModel item = list.get(p);

                                        if(!subjectArrayList.contains(item.getSubjectName())) subjectArrayList.add(item.getSubjectName());
                                        if(!dateArrayList.contains(item.getSubjectName() + "_" + item.getDate())) dateArrayList.add(item.getSubjectName() + "_" + item.getDate());
                                        if(!nameArrayList.contains(item.getSubjectName() + "_" + item.getTopicName())) nameArrayList.add(item.getSubjectName() + "_" + item.getTopicName());


                                        HashMap<String, Object> mainMap = new HashMap<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        HashMap<String, String> dataMapValue = new HashMap<>();
                                        dataMapValue.put("totalTime", item.getTotalTime());
                                        dataMapValue.put("videoName", item.getVideoName());
                                        dataMapValue.put("topicName", item.getTopicName());
                                        dataMapValue.put("subjectName", item.getSubjectName());
                                        dataMapValue.put("date", item.getDate());
                                        dataMapValue.put("time", item.getVTime());
                                        dataMapValue.put("videoId", item.getVideoId());


                                        Random r = new Random();
                                        int i1 = r.nextInt(50000 - 1) + 1;


                                        boolean added=false;

                                        for(int i=mainArrayList.size()-1;i>=0;i--)
                                        {
                                            if(mainArrayList.get(i).containsKey(item.getSubjectName() + "_" + item.getDate()))
                                            {
                                                mainMap=mainArrayList.get(i);
                                                HashMap<String,Object> m = (HashMap<String, Object>) mainMap.get(item.getSubjectName() + "_" + item.getDate());
                                                if(m.containsKey(item.getTopicId()))
                                                {
                                                    HashMap<String,Object> map2= (HashMap<String, Object>) m.get(item.getTopicId());
//                                                    map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                    if(map2.containsKey(item.getVideoId()))
                                                    {
                                                        HashMap<String,Object> map3= (HashMap<String, Object>) map2.get(item.getVideoId());
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }
                                                    else
                                                    {
//
//                                                        HashMap<String,Object> map3= new HashMap<>();
//                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);
//                                                        map2.put(item.getVideoId(), map3);
//                                                        m.put(item.getTopicId(), map2);
//                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(), m);
//                                                        mainArrayList.remove(i);
//                                                        mainArrayList.add(mainMap);
//                                                        added=true;

                                                        HashMap<String,Object> map3= new HashMap<>();
                                                        map3.put(String.valueOf(item.getTime()), dataMapValue);

//                                                        map2.clear();
                                                        map2.put(item.getVideoId(),map3);
                                                        m.put(item.getTopicId(),map2);
                                                        mainMap.put(item.getSubjectName() + "_" + item.getDate(),m);
                                                        mainArrayList.remove(i);
                                                        mainArrayList.add(mainMap);
                                                        added=true;

                                                    }


                                                }
                                                else
                                                {
                                                    HashMap<String,Object> map11=new HashMap<>();
                                                    HashMap<String,Object> map22=new HashMap<>();

                                                    map22.put(String.valueOf(item.getTime()),dataMapValue);
                                                    map11.put(item.getVideoId(),map22);
                                                    m.put(item.getTopicId(), map11);
                                                    mainMap.put(item.getSubjectName() + "_" + item.getDate(),m );
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }



                                            }
                                            else if(i==0 && !added)
                                            {
                                                HashMap<String,Object> map2= new HashMap<>();
                                                map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                HashMap<String,Object> map3= new HashMap<>();
                                                map3.put(item.getVideoId(), map2);
                                                map.put(item.getTopicId(), map3);
                                                mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                                mainArrayList.add(mainMap);
                                            }
                                        }

                                        if(mainArrayList.size()==0)
                                        {
                                            HashMap<String,Object> map2= new HashMap<>();
                                            map2.put(String.valueOf(item.getTime()), dataMapValue);

                                            HashMap<String,Object> map3= new HashMap<>();
                                            map3.put(item.getVideoId(), map2);
                                            map.put(item.getTopicId(), map3);
                                            mainMap.put(item.getSubjectName() + "_" + item.getDate(), map);
                                            mainArrayList.add(mainMap);
                                        }


                                        if(p==0)
                                        {

                                            pager.setOffscreenPageLimit(subjectArrayList.size());
                                            //Adding the tabs using addTab() method
                                            for (int i = 0; i < subjectArrayList.size(); i++) {
                                                tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                            }
                                            if(type.equals("project_video")) Collections.reverse(nameArrayList);
                                            if(type.equals("simulation_content")) Collections.reverse(nameArrayList);
                                            PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
//                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList,mainArrayList1, nameArrayList, type);
                                            pager.setAdapter(adapter);
                                            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                            tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                            tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                @Override
                                                public void onTabSelected(TabLayout.Tab tab) {
                                                    pager.setCurrentItem(tab.getPosition());
                                                }

                                                @Override
                                                public void onTabUnselected(TabLayout.Tab tab) {

                                                }

                                                @Override
                                                public void onTabReselected(TabLayout.Tab tab) {

                                                }
                                            });


                                        }



                                    }

                                }else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            topicWiseVideoDisposable.dispose();
                        }
                    });
        }
        else if(taskType.equals("testTopicWiseTask")){
            getList(userId, board, sClass, type, taskType).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            topicWiseTestDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTopicWiseTestModel> list = (ArrayList<ReportsTopicWiseTestModel>) o;
                                if(list != null && list.size() > 0){
                                    buttonDate.setText(date);
                                    ArrayList<String> subjectArrayList = new ArrayList<>();
                                    ArrayList<String> dateArrayList = new ArrayList<>();
                                    ArrayList<String> nameArrayList = new ArrayList<>();
                                    ArrayList<HashMap<String, Object>> mainArrayList = new ArrayList<>();
                                    for(ReportsTopicWiseTestModel item: list){

                                        if(!subjectArrayList.contains(item.getSubjectName().replace("_"," ").toLowerCase())) subjectArrayList.add(item.getSubjectName().replace("_"," ").toLowerCase());
                                        if(!dateArrayList.contains(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate())) dateArrayList.add(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate());
                                        if(!nameArrayList.contains(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getName())) nameArrayList.add(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getName());

                                        HashMap<String, Object> mainMap = new HashMap<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        HashMap<String, Object> dataMapValue = new HashMap<>();
                                        HashMap<String, String> scoresDataMap = new HashMap<>();
                                        ArrayList<HashMap<String, String>> detailReviewList = new ArrayList<>();
                                        HashMap<String, String> detailReviewDataMap = new HashMap<>();
                                        scoresDataMap.put("date", item.getReportsTestScoreModel().getDate());
                                        scoresDataMap.put("totalQuestions", item.getReportsTestScoreModel().getTotalQuestions());
                                        scoresDataMap.put("totalScores", item.getReportsTestScoreModel().getTotalScores());
                                        scoresDataMap.put("scores", item.getReportsTestScoreModel().getScores());
                                        scoresDataMap.put("topicName", item.getReportsTestScoreModel().getTopicName());
                                        scoresDataMap.put("questionsAttempted", item.getReportsTestScoreModel().getQuestionsAttempted());
                                        dataMapValue.put("scores", scoresDataMap);
                                        for(ReportsTestDetailReviewModel value: item.getList()){
                                            detailReviewDataMap.put("OptionSelected", value.getOptionSelected());
                                            detailReviewDataMap.put("Status", value.getStatus());
                                            detailReviewDataMap.put("image", value.getImage());
                                            detailReviewDataMap.put("type4", value.getType4());
                                            detailReviewDataMap.put("type3", value.getType3());
                                            detailReviewDataMap.put("questionID", value.getQuestionId());
                                            detailReviewDataMap.put("type2", value.getType2());
                                            detailReviewDataMap.put("correct_feedback", value.getCorrectFeedback());
                                            detailReviewDataMap.put("correctOption", value.getCorrectOption());
                                            detailReviewDataMap.put("IsAttampted", value.getIsAttempted());
                                            detailReviewDataMap.put("type1", value.getType1());
                                            detailReviewDataMap.put("questionQ", value.getQuestionQ());
                                            detailReviewDataMap.put("option3", value.getOption3());
                                            detailReviewDataMap.put("option4", value.getOption4());
                                            detailReviewDataMap.put("incorrect_feedback", value.getIncorrectFeedback());
                                            detailReviewDataMap.put("option1", value.getOption1());
                                            detailReviewDataMap.put("option2", value.getOption2());
                                            detailReviewList.add(detailReviewDataMap);
                                        }
                                        dataMapValue.put("detail_review", detailReviewList);


                                        boolean added=false;

                                        for(int i=mainArrayList.size()-1;i>=0;i--)
                                        {
                                            if(mainArrayList.get(i).containsKey(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate()))
                                            {
                                                mainMap=mainArrayList.get(i);
                                                HashMap<String,Object> m = (HashMap<String, Object>) mainMap.get(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate());

                                                if(m.containsKey(item.getTopicId()))
                                                {
                                                    HashMap<String,Object> map22= (HashMap<String, Object>) m.get(item.getTopicId());
                                                    map22.put(String.valueOf(item.getTime()),dataMapValue);
                                                    m.put(item.getTopicId(), map22);
                                                    mainMap.put(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate(),m );
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }
                                                else
                                                {
                                                    HashMap<String,Object> map22=new HashMap<>();

                                                    map22.put(String.valueOf(item.getTime()),dataMapValue);
                                                    m.put(item.getTopicId(), map22);
                                                    mainMap.put(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate(),m );
                                                    mainArrayList.remove(i);
                                                    mainArrayList.add(mainMap);
                                                    added=true;
                                                }




                                            }
                                            else if(i==0 && !added)
                                            {
                                                HashMap<String,Object> map2= new HashMap<>();
                                                map2.put(String.valueOf(item.getTime()), dataMapValue);

                                                map.put(item.getTopicId(), map2);
                                                mainMap.put(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate(), map);
                                                mainArrayList.add(mainMap);
                                            }
                                        }

                                        if(mainArrayList.size()==0)
                                        {
                                            dataMap.put(String.valueOf(item.getTime()), dataMapValue);
                                            map.put(item.getTopicId(), dataMap);
                                            mainMap.put(item.getSubjectName().replace("_"," ").toLowerCase() + "_" + item.getTestDate(), map);
                                            mainArrayList.add(mainMap);

                                        }


                                    }
                                    pager.setOffscreenPageLimit(subjectArrayList.size());
                                    //Adding the tabs using addTab() method
                                    for (int i = 0; i < subjectArrayList.size(); i++) {
                                        tabs.addTab(tabs.newTab().setText(subjectArrayList.get(i)));
                                    }
                                    PalReportPagerDetailedAdapter adapter = new PalReportPagerDetailedAdapter(getSupportFragmentManager(), dateArrayList, subjectArrayList, mainArrayList, nameArrayList, type);
                                    pager.setAdapter(adapter);
                                    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                                    tabs.setTabGravity(TabLayout.GRAVITY_FILL);

                                    tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                        @Override
                                        public void onTabSelected(TabLayout.Tab tab) {
                                            pager.setCurrentItem(tab.getPosition());
                                        }

                                        @Override
                                        public void onTabUnselected(TabLayout.Tab tab) {

                                        }

                                        @Override
                                        public void onTabReselected(TabLayout.Tab tab) {

                                        }
                                    });
                                }else{
                                    reletiveNoReports.setVisibility(View.VISIBLE);
                                    linearVisible.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            topicWiseTestDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String type, String taskType){
        String lang=Util.getLanguageJson(context);
        if(taskType.equals("practiceTopicWiseTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTopicWisePracticeRepository.getDetail(userId, board, sClass, null, type, null,Util.getSelectedLanguage(context));
            });
        }else if(taskType.equals("videoTopicWiseTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTopicWiseVideoRepository.getDetail(userId, board, sClass, null, type, null,Util.getSelectedLanguage(context));
            });
        }else if(taskType.equals("testTopicWiseTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTopicWiseTestRepository.getDetail(userId, board, sClass, null, type, null,Util.getSelectedLanguage(context));
            });
        }
        return null;
    }

    private void getPracticeReports(String date) {
        DatabaseReference aa;
        if (topicID == null) {
            aa = global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child(type).child(date);
        } else {
            aa = global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child(type).child(date).child(topicID);
        }
        aa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        buttonDate.setText(date);
                        HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                        textViewHead.setText(type + "\n\n" + "Total Attempts: " + map.size());
                        ArrayList<HashMap<String, Object>> array = new ArrayList<>();
                        for (String key : map.keySet()) {
                            HashMap<String, Object> innerMap = (HashMap<String, Object>) map.get(key);

                            innerMap.put("key", key);
                            array.add(innerMap);
                        }
                        ReportsListAdapter reportsListAdapter = new ReportsListAdapter(context, array, date, type, topicID);
                        recyclerView.setAdapter(reportsListAdapter);
                        reportsListAdapter.SetOnItemClickListener(new ReportsListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }
                        });

                    } else {
                        Util.showToast(context, "Not available");
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

    private void setStaticText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("My_Reports_Screen");
                //title.setText((String) object_.get("reports_title"));

                no_reports_to_show = object_.get("no_reports_show").toString();
                noReportsTextView.setText(no_reports_to_show);
                your_usage_reports = object_.get("your_usage_reports_show").toString();
                noReportsTextViewDetail.setText(your_usage_reports);
                start_learning = object_.get("start_learning").toString();
                startLearningTextView.setText(start_learning);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("My_Reports_Screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            // title.setText(dataHashMap.get("reports_title"));
                            no_reports_to_show = dataHashMap.get("no_reports_show");
                            noReportsTextView.setText(no_reports_to_show);
                            your_usage_reports = dataHashMap.get("your_usage_reports_show");
                            noReportsTextViewDetail.setText(your_usage_reports);
                            start_learning = dataHashMap.get("start_learning");
                            startLearningTextView.setText(start_learning);

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

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(ReportListActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String date = dayOfMonth + " " + (monthOfYear + 1) + " " + year;
                        String month = "";
                        String mm = "" + (monthOfYear + 1);
                        switch (mm) {
                            case "1":
                                month = "Jan";
                                break;
                            case "2":
                                month = "Feb";
                                break;
                            case "3":
                                month = "Mar";
                                break;
                            case "4":
                                month = "Apr";
                                break;
                            case "5":
                                month = "May";
                                break;
                            case "6":
                                month = "Jun";
                                break;
                            case "7":
                                month = "Jul";
                                break;
                            case "8":
                                month = "Aug";
                                break;
                            case "9":
                                month = "Sept";
                                break;
                            case "10":
                                month = "Oct";
                                break;
                            case "11":
                                month = "Nov";
                                break;
                            case "12":
                                month = "Dec";
                                break;

                        }
                        String days = String.valueOf(year);
                        date = dayOfMonth + " " + month + " " + days.substring(2);
                        if (type.equalsIgnoreCase("practice")) {
                            getPracticeReports(date);
                        } else {
                            getReportsListing(date);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
    }



}
