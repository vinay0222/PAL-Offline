package com.idreameducation.ipreppal.pal.activity;

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
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
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
import com.idreameducation.ipreppal.pal.adapter.PalReportPagerAdapter;
import com.idreameducation.ipreppal.pal.adapter.PalReportsStudentsAdapter;
import com.idreameducation.ipreppal.roomdatabase.repository.StudentDetailsRepository;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;


public class PalReportsActivity extends AppCompatActivity  {

    public ViewPager pager;
    private TabLayout tabs;
    private Context context;
    private RelativeLayout reletiveNoReports;
    private RelativeLayout reletiveReports;
    private TextView studentstextView;
    private Global global;
    private TextView title,noReportsTextView,noReportsTextViewDetail,startLearningTextView;
    public String bi_monthly_text, categories_text, days_spent_text, diagnostic_text, month_text,
            practice_attempted_text, tab_month_text, tab_week_text, tab_year_text, tests_completed_text,
            time_spent_text, video_watched_text, week_text, year_text,no_reports_to_show,your_usage_reports,start_learning;
    private StudentDetailsRepository studentDetailsRepository;
    private Disposable disposable;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private ProgressDialog progressDialog;

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

    public static ArrayList<String> support_Language;

    public PalReportsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_report_activity);
        context = this;
        global = (Global) getApplicationContext();
        title = findViewById(R.id.title);
        noReportsTextView = findViewById(R.id.noReportsTextView);
        noReportsTextViewDetail = findViewById(R.id.noReportsTextViewDetail);
        startLearningTextView = findViewById(R.id.startLearningTextView);
        pager = findViewById(R.id.pagers);
        tabs = findViewById(R.id.tab_view);
        reletiveNoReports = findViewById(R.id.reletiveNoReports);
        reletiveReports = findViewById(R.id.reletiveReports);
        studentstextView = findViewById(R.id.studentstextView);
        reletiveNoReports.setVisibility(View.GONE);
        //hideNavigationBar(getWindow());

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

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


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        try {
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }


        progressDialog = new ProgressDialog(PalReportsActivity.this);

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            progressDialog.setMessage("कृपया प्रतीक्षा करें जब तक हम आपकी रिपोर्ट लोड करते हैं");
        }else {
            progressDialog.setMessage("Please wait while we load your reports");
        }

        try {
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getusersforReports();
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentDetailsRepository = new StudentDetailsRepository(context);
        studentstextView.setVisibility(View.GONE);
        studentstextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                try {
                    openStudentListDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.report_container, new ReportsActivity(0,context));
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @SuppressLint("RestrictedApi")
    public void animateFAB() {

            fab.setImageResource(R.drawable.ic_inactive);
            isFabOpen = true;

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

            try {
                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                StringBuilder sb = new StringBuilder(20);
                Random random = new Random();
                for (int i = 0; i < 20; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                String output = sb.toString();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "prabhakar_images/"
                                        + output);

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
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private void setTabPager() {
        //Adding the tabs using addTab() method
        tabs.addTab(tabs.newTab().setText(tab_week_text));
        tabs.addTab(tabs.newTab().setText(tab_month_text));
        tabs.addTab(tabs.newTab().setText(tab_year_text));
        pager.beginFakeDrag();


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                try {
                    switch (position) {
                        case 0:
                            days = 7;
                            break;
                        case 1:
                            days = 30;
                            break;
                        case 2:
                            days = 365;
                            break;
                    }

                    ReportsActivity fragment = (ReportsActivity) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pagers + ":" + position);
                    fragment.countArrayList = new ArrayList<>();
                    fragment.subjectArrayList_ = new ArrayList<>();
                    fragment.datamap = new HashMap<>();
                    fragment.count = 0;
                    fragment.diagonostic_test = 0;
                    fragment.simple_test = 0;
                    fragment.video = 0;
                    fragment.practice = 0;
                    fragment.bi_monthly_test = 0;
                    fragment.getSubjects(studentId, days);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setStaticText() throws Exception {
        get_support_dialog_language();
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("My_Reports_Screen");
                title.setText(object_.get("reports_title").toString());
                bi_monthly_text = object_.get("bi_monthly_text").toString();
                categories_text = object_.get("categories_text").toString();
                days_spent_text = object_.get("days_spent_text").toString();
                diagnostic_text = object_.get("diagnostic_text").toString();
                no_reports_to_show = object_.get("no_reports_show").toString();
                noReportsTextView.setText(no_reports_to_show);
                your_usage_reports = object_.get("your_usage_reports_show").toString();
                noReportsTextViewDetail.setText(your_usage_reports);
                start_learning = object_.get("start_learning").toString();
                startLearningTextView.setText(start_learning);
                month_text = object_.get("month_text").toString();
                practice_attempted_text = object_.get("practice_attempted_text").toString();
                tab_month_text = object_.get("tab_month_text").toString();
                tab_week_text = object_.get("tab_week_text").toString();
                tab_year_text = object_.get("tab_year_text").toString();
                tests_completed_text = object_.get("tests_completed_text").toString();
                time_spent_text = object_.get("time_spent_text").toString();
                video_watched_text = object_.get("video_watched_text").toString();
                week_text = object_.get("week_text").toString();
                year_text = object_.get("year_text").toString();

                setTabPager();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("My_Reports_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            title.setText(dataHashMap.get("reports_title"));
                            bi_monthly_text = dataHashMap.get("bi_monthly_text");
                            categories_text = dataHashMap.get("categories_text");
                            days_spent_text = dataHashMap.get("days_spent_text");
                            System.out.println( "======= dataHashMap "+ dataHashMap);
                            System.out.println( "======= days_spent_text "+ days_spent_text);
                            diagnostic_text = dataHashMap.get("diagnostic_text");
                            no_reports_to_show = dataHashMap.get("no_reports_show");
                            noReportsTextView.setText(no_reports_to_show);
                            your_usage_reports = dataHashMap.get("your_usage_reports_show");
                            noReportsTextViewDetail.setText(your_usage_reports);
                            start_learning = dataHashMap.get("start_learning");
                            startLearningTextView.setText(start_learning);
                            month_text = dataHashMap.get("month_text");
                            practice_attempted_text = dataHashMap.get("practice_attempted_text");
                            tab_month_text = dataHashMap.get("tab_month_text");
                            tab_week_text = dataHashMap.get("tab_week_text");
                            tab_year_text = dataHashMap.get("tab_year_text");
                            tests_completed_text = dataHashMap.get("tests_completed_text");
                            time_spent_text = dataHashMap.get("time_spent_text");
                            video_watched_text = dataHashMap.get("video_watched_text");
                            week_text = dataHashMap.get("week_text");
                            year_text = dataHashMap.get("year_text");

                            setTabPager();
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

    private String studentId;

    private void showDialog() {
        if(progressDialog != null){
            progressDialog.show();
            progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

//            if(Util.isPortraitMode(context)) progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            progressDialog.show();
            /* Change the background color of the dialog */
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            progressDialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //Clear the not focusable flag from the window
            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            showProgressingView();
        }
    }

    public void hideDialog() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if(progressDialog != null){
                progressDialog.dismiss();
                hideProgressingView();
            }
        }, 500);
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

    public void showReportsLayout() {
        reletiveNoReports.setVisibility(View.GONE);
        reletiveReports.setVisibility(View.VISIBLE);

    }

    public void hideReportsLayout() {
        LottieAnimationView lotie_player=findViewById(R.id.lotie_player);

        startLearningTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                onBackPressed();
            }
        });

        lotie_player.setAnimation("noreporttoshow.json");
        lotie_player.playAnimation();
        reletiveNoReports.setVisibility(View.VISIBLE);
        reletiveReports.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public ArrayList<HashMap<String, String>> studentArrayList = new ArrayList<>();
    public ArrayList<String> studentSpinnerArrayList = new ArrayList<>();

    private void getusersforReports() {
        String ngoID = Util.getNGOID(context);


        if(Util.isOfflineMode(context))
        {

            runBackgroundTask(ngoID);

            PalReportPagerAdapter adapter = new PalReportPagerAdapter(getSupportFragmentManager(), context);
            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(3);
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

            settingAdapter();

            hideDialog();
        }
        else
        {

            if(Util.isOfflineMode(context)){
                runBackgroundTask(ngoID);
            }else{
                global.getDatabaseReference().child("offline_login_users").child(ngoID).child("students").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    HashMap<String, String> studentMap = (HashMap<String, String>) snapshot1.getValue();
                                    String key = snapshot1.getKey();
                                    studentMap.put("userID", key);

                                    String sClass = studentMap.get("sClass");
                                    String sName = studentMap.get("studentName");
                                    studentMap.put("isSelected", "no");
                                    studentArrayList.add(studentMap);
                                    studentSpinnerArrayList.add(sName + "(" + sClass + ")");
                                }
                                for (int i = 0; i < studentSpinnerArrayList.size(); i++) {
                                    String userID = studentArrayList.get(i).get("userID");
                                    if (Util.getUserId(context).equalsIgnoreCase(userID)) {

                                        studentSpinnerArrayList.add(0, studentSpinnerArrayList.get(i));
                                        studentArrayList.add(0, studentArrayList.get(i));
                                        studentArrayList.get(0).put("isSelected", "yes");

                                        studentSpinnerArrayList.remove(i + 1);
                                        studentArrayList.remove(i + 1);
                                    }
                                }
                                studentId = studentArrayList.get(0).get("userID");
                                studentstextView.setText(studentArrayList.get(0).get("studentName"));
                                settingAdapter();
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

    private void runBackgroundTask(String ngoID){
        getList(ngoID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    private Observable<Object> getList(String ngoID){
        return Observable.fromCallable(() -> {
            //do something, get your Data object

            return studentDetailsRepository.getDetail(null, ngoID);
        });
    }

    private void openStudentListDialog() throws Exception {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.setCancelable(true);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                dialog.dismiss();
            }
        });
        TextView textViewDone = dialog.findViewById(R.id.textViewDone);
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                dialog.dismiss();
            }
        });
        RecyclerView students_recycler_view = dialog.findViewById(R.id.students_recycler_view);
        students_recycler_view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        students_recycler_view.setLayoutManager(manager);
        PalReportsStudentsAdapter palReportsStudentsAdapter = new PalReportsStudentsAdapter(context, studentArrayList);
        students_recycler_view.setAdapter(palReportsStudentsAdapter);


        palReportsStudentsAdapter.SetOnItemClickListener(new PalReportsStudentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    for (int i = 0; i < studentArrayList.size(); i++) {
                        studentArrayList.get(i).put("isSelected", "no");
                    }
                    studentArrayList.get(position).put("isSelected", "yes");
                    String userId = ((PalReportsActivity) context).studentArrayList.get(position).get("userID");
                    int page = ((PalReportsActivity) context).pager.getCurrentItem();
                    switch (page) {
                        case 0:
                            days = 7;
                            break;
                        case 1:
                            days = 30;
                            break;
                        case 2:
                            days = 365;
                            break;
                    }
                    studentstextView.setText(studentArrayList.get(position).get("studentName")+ "(" + studentArrayList.get(position).get("sClass") + ")");
                    ReportsActivity fragment = (ReportsActivity) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pagers + ":" + pager.getCurrentItem());
                    fragment.countArrayList = new ArrayList<>();
                    fragment.subjectArrayList_ = new ArrayList<>();
                    fragment.datamap = new HashMap<>();
                    fragment.diagonostic_test = 0;
                    fragment.simple_test = 0;
                    fragment.video = 0;
                    fragment.practice = 0;
                    fragment.count = 0;
                    fragment.bi_monthly_test = 0;
                    studentId = userId;
                    fragment.getSubjects(userId, days);

                    palReportsStudentsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.show();
    }

    private int days = 1;

    private void settingAdapter() {
//        ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.row_spinner, ((PalReportsActivity) context).studentSpinnerArrayList);
//        ((PalReportsActivity) context).studentsSpinner.setAdapter(adapter);
//        ((PalReportsActivity) context).studentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Util.preventPause(context,getTaskId());
    }



}

