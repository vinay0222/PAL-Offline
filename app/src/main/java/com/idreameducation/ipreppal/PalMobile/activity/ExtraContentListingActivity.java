package com.idreameducation.ipreppal.PalMobile.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.idreameducation.ipreppal.pal.activity.LinearLayoutManagerWithSmoothScroller;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.activity.VideoView_Activity;
import com.idreameducation.ipreppal.pal.adapter.BooksListingActivityAdapter;
import com.idreameducation.ipreppal.pal.adapter.ExtraContentTopicAdapter;
import com.idreameducation.ipreppal.pal.adapter.PalExtraContentPagerAdaper;
import com.idreameducation.ipreppal.pal.adapter.VideoListAdapter;
import com.idreameducation.ipreppal.pal.fragments.PalBooksListingFragment;
import com.idreameducation.ipreppal.pal.fragments.PalHomeFragment;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataVideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataVideoRepository;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class ExtraContentListingActivity extends AppCompatActivity {
    public static Context context;
    private Global global;
    public String board;
    public static String subjectName;
    public static String sClass;
    public static String subject;
    public static String type;
    public static String icon;
    public static ArrayList<String> subjects;
    public String topicId;
    public String topicName,books_heading;
    public String language;
    private RecyclerView recyclerView;
    public ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView textViewSubjectName;
    private TextView textViewChapterCount;
    private ImageView imageViewBack;
    private ImageView imageViewBackFoundational;
    private ImageView imageViewSubject;
    private RequestOptions requestOptions;
    private FrameLayout frameLayout;
    //    private ImageView videoImageViewBack;
    private int position;
    private TextView textViewTitle;
    private TextView textViewSubTitle;
    private RelativeLayout videoLayout;
    private RecyclerView videoRecyclerView;
    private VideoListAdapter videoListAdapter;
    private HashMap<String, Object> dataMap;
    public static String videoKey;
    private static int videoLevel;
    private static int videoPosition;
    public String name;
    private int testPercentageAchieved = 0;
    private Disposable disposable;
    private ReportsLatestDataVideoRepository reportsLatestDataVideoRepository;

    private RelativeLayout practiceLayout;
    private boolean refreshAdapter;


    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        Util.setContext(context);
        setContentView(R.layout.pal_activity_extra_content);
        Util.handleNotch(this);
        assignIds();
        listners();

    }

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
        imageViewBackFoundational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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


    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        board = Util.getSelectedBoard(context);
        checkConnection(false);
        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        type = getIntent().getStringExtra("type");
        subject = getIntent().getStringExtra("subject");
        subjectName = getIntent().getStringExtra("subjectName");
        subjects = getIntent().getStringArrayListExtra("subjects");
        position = getIntent().getIntExtra("position", 0);
        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");
        books_heading = getIntent().getStringExtra("books_heading");
        testPercentageAchieved = getIntent().getIntExtra("testPercentageAchieved", 0);

        if(type != null && type.equals("foundationalTopicVideos")){
            sClass = getIntent().getStringExtra("sClass");
        }else{
            sClass = Util.getSelectedClass(context);
        }
        practiceLayout = findViewById(R.id.practiceLayout);
        practiceLayout.setVisibility(View.GONE);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBackFoundational = findViewById(R.id.imageViewBackFoundational);
        imageViewSubject = findViewById(R.id.imageViewSubject);
        textViewSubjectName = findViewById(R.id.textViewSubjectName);
        textViewChapterCount = findViewById(R.id.textViewChapterCount);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        language = Util.getSelectedLanguage(context);
        reletiveLayout = findViewById(R.id.reletiveLayout);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayoutWeight = findViewById(R.id.linearLayoutWeight);
        if(type != null && type.equals("foundationalTopicVideos")){
            frameLayout = findViewById(R.id.videoContainer);
//            fullScreen();
            smallScreen(false);
        }else{
            frameLayout = findViewById(R.id.container);
            smallScreen(false);
        }
//        videoImageViewBack = findViewById(R.id.videoImageViewBack);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 1);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSubTitle = findViewById(R.id.textViewSubTitle);

        //hideNavigationBar(getWindow());

        recyclerView.setLayoutManager(manager);

        videoLayout = findViewById(R.id.videoLayout);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setHasFixedSize(true);
        LinearLayoutManager videoRecyclerViewManager = new LinearLayoutManager(context);
        videoRecyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));

        if(type != null && type.equals("foundationalTopicVideos")){
            reletiveLayout.setVisibility(View.GONE);
            linearLayoutWeight.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);
            textViewTitle.setVisibility(View.VISIBLE);
            textViewSubTitle.setVisibility(View.VISIBLE);
            textViewSubTitle.setTextColor(Color.parseColor(global.getColor()));
            if(topicName != null){
                textViewSubTitle.setText(topicName);
            }
        }else{
            videoLayout.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            textViewSubTitle.setVisibility(View.GONE);
            reletiveLayout.setVisibility(View.VISIBLE);
            linearLayoutWeight.setVisibility(View.VISIBLE);
        }

        if(type.equals("books")){
            textViewSubjectName.setText(books_heading);
            textViewChapterCount.setVisibility(View.GONE);
        }else{
            textViewSubjectName.setText(subjectName);
        }
        textViewSubjectName.setTextColor(Color.parseColor("#000000"));


        if (type != null && type.equalsIgnoreCase("books")) {
            getBooks();
        } else if(type != null && type.equalsIgnoreCase("foundationalTopicVideos")){
            getFoundationalTopicVideos();
        } else {
            getContent();
        }
        get_support_dialog_language();
    }


//    public ArrayList<HashMap<String, Object>> contentArrayList;


    private void setPagerAdapter(ArrayList<HashMap<String, String>> contentArrayList, String name) {
        PalExtraContentPagerAdaper contentPagerAdaper = new PalExtraContentPagerAdaper(getSupportFragmentManager(), type, contentArrayList, name,position);
        viewPager.setAdapter(contentPagerAdaper);
    }

    private void setTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        // this needs to be dynamic
        switch (type) {
            case "activityVideos":
                tabLayout.getTabAt(0).setText("Stem");
                break;
            case "books":
                tabLayout.getTabAt(0).setText("Books");
                break;
        }
//        if(type.equalsIgnoreCase("activityVideos")){
//
//        }else {
//            tabLayout.getTabAt(0).setText("");
//        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setStaticText(){

    }



    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            if(type != null && type.equals("foundationalTopicVideos")){
                if (videoFragmentExists()) {
                    removeFragment(true);
                    //setMargins(tabLayout, 20, 15, 20, 0);
                } else {
                    if(type != null && type.equals("foundationalTopicVideos")){
                        PalContentListingActivity.backPressed = true;
                    }
                    super.onBackPressed();
                }
            }else{
                smallScreen(true);
            }
        } else {
            if (videoFragmentExists()) {
                removeFragment(true);
                //setMargins(tabLayout, 20, 15, 20, 0);
            } else {
                if(type != null && type.equals("foundationalTopicVideos")){
                    PalContentListingActivity.backPressed = true;
                }
                super.onBackPressed();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
    }

    private boolean videoFragmentExists() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag1");
        return fragment != null;
    }


    private Fragment fragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    public static void openFragment(String url, String videoName, String topicId, String openFragment, boolean isLocalFile) {
//        videoImageViewBack.setVisibility(View.VISIBLE);

//        if(type != null && type.equals("foundationalTopicVideos")){
//            fullScreen();
//        }
//        frameLayout.setVisibility(View.VISIBLE);
//        Bundle bundl = new Bundle();
//        bundl.putString("url", url);
//        bundl.putString("topicID", topicId);
//        bundl.putString("videoName", videoName);
//        if(type != null && type.equals("foundationalTopicVideos")){
//            bundl.putBoolean("isLocalFile", isLocalFile);
//            bundl.putString("type", type);
//            fragment = new PalVideoPlayerActivity();
//        }else{
//            bundl.putString("vType", openFragment);
//            fragment = new ExtraContentVideoPlayerFragment();
//        }
//        fragment.setArguments(bundl);
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        if(type != null && type.equals("foundationalTopicVideos")){
//            transaction.add(R.id.videoContainer, fragment, "tag1");
//            frameLayout.setVisibility(View.VISIBLE);
//        }else{
//            transaction.add(R.id.container, fragment, "tag1");
//        }
//        transaction.addToBackStack(null);
//        transaction.commit();

//        PalContentListingActivity.videoKey=videoKey;

        Bundle bundls =new Bundle();
        bundls.putString("url", url);
        bundls.putString("type", type);
        bundls.putString("offlineLink", "offlineLink");
        bundls.putBoolean("isLocalFile", isLocalFile);
        bundls.putString("videoName", videoName);
        bundls.putString("topicID", topicId);
        bundls.putString("subjectName", subjectName);
        bundls.putString("videoid_for_reports", videoKey);
        bundls.putBoolean("isFullScreen", false);
        bundls.putInt("duration", 0);

        Intent intent = new Intent(context, VideoView_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundls);

        context.startActivity(intent);
    }


    public void removeFragment(boolean isCompleted) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("tag1");
        transaction.remove(f);
        if (f != null) {
            transaction.remove(f);
            transaction.commit();
            manager.popBackStack();
        }

        if(isFullScreen){
            smallScreen(true);
        }

        if (isCompleted) {
            frameLayout.setVisibility(View.GONE);
//            videoImageViewBack.setVisibility(View.GONE);
        }
    }


    public boolean isFullScreen = false;

    public void fullScreen() {
        isFullScreen = true;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams_ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
        linearLayoutWeight.setLayoutParams(layoutParams_);
        linearLayout.setVisibility(View.GONE);
        reletiveLayout.setVisibility(View.GONE);
//        videoImageViewBack.setVisibility(View.GONE);
    }

    private RelativeLayout linearLayout;
    private LinearLayout linearLayoutWeight;
    private RelativeLayout reletiveLayout;

    public void smallScreen(boolean showBackButton) {
        isFullScreen = false;
        linearLayout.setVisibility(View.VISIBLE);
        if(type != null && type.equals("foundationalTopicVideos")){
            reletiveLayout.setVisibility(View.GONE);
        }else{
            reletiveLayout.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
        layoutParams.setMargins(0, 0, 20, 0);
        frameLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams_ = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.3f);
        linearLayoutWeight.setLayoutParams(layoutParams_);
        if(showBackButton){
//            videoImageViewBack.setVisibility(View.VISIBLE);
        }
    }


    public boolean isFragmentAdded() {
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("tag1");
        return fragment != null;
    }
    public ArrayList<HashMap<String, Object>> ActivityVideosArrayList;
    private void getContent() {
        isloading=true;

        if (Util.isOfflineMode(context)) {
            hideconnection_layout();
            String filePath = ".iDream_content/offlinetab_PAL/extra_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject aa = jsonObject.getJSONObject(board);
                JSONObject object_ = aa.getJSONObject(sClass);
                JSONObject object__ = object_.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object___ = object__.getJSONObject(subject);
                JSONObject object_____ = object___.getJSONObject(type);
                JSONObject object______ = object_____.getJSONObject("topics");
                Iterator<String> iterator = object______.keys();
                String key = "";
                ArrayList<String> topicArrayList = new ArrayList<>();
                ArrayList<Integer> topicLengthArrayList = new ArrayList<>();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    topicArrayList.add(key);
                    int lenght = object______.getJSONObject(key).length();
                    topicLengthArrayList.add(lenght);
                }

                String topicName = topicArrayList.get(position);
                JSONObject object12 = object______.getJSONObject(topicName);
                Iterator<String> iterator_ = object12.keys();
                String key_ = "";
                contentArrayList = new ArrayList<>();
                while (iterator_.hasNext()) {
                    HashMap<String, String> contentHashMap = new HashMap<>();
                    key_ = iterator_.next();
                    JSONObject inFIne = object12.getJSONObject(key_);
                    String vName = inFIne.getString("name");
                    String onlineLink = inFIne.getString("onlineLink");
                    String offlineLink = inFIne.getString("offlineLink");
                    contentHashMap.put("name", vName);
                    contentHashMap.put("onlineLink", onlineLink);
                    contentHashMap.put("offlineLink", offlineLink);
                    contentArrayList.add(contentHashMap);
                }


                if (topicArrayList.size() > 0) {
                    textViewChapterCount.setText(topicArrayList.size() + " Chapters");
                    ExtraContentTopicAdapter extraContentAdapter = new ExtraContentTopicAdapter(context, topicArrayList, topicLengthArrayList,type);
                    recyclerView.setAdapter(extraContentAdapter);
                    extraContentAdapter.SetOnItemClickListener(new ExtraContentTopicAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            try {
                                String topicName = topicArrayList.get(position);
                                JSONObject object12 = object______.getJSONObject(topicName);
                                Iterator<String> iterator_ = object12.keys();
                                String key_ = "";
                                contentArrayList = new ArrayList<>();
                                while (iterator_.hasNext()) {
                                    HashMap<String, String> contentHashMap = new HashMap<>();
                                    key_ = iterator_.next();
                                    JSONObject inFIne = object12.getJSONObject(key_);
                                    String vName = inFIne.getString("name");
                                    String onlineLink = inFIne.getString("onlineLink");
                                    String offlineLink = inFIne.getString("offlineLink");
                                    contentHashMap.put("name", vName);
                                    contentHashMap.put("onlineLink", onlineLink);
                                    contentHashMap.put("offlineLink", offlineLink);
                                    contentArrayList.add(contentHashMap);
                                }
                                setPagerAdapter(contentArrayList, null);
                                setTabLayout();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Util.showToast(context, "Not available");
                }
                setPagerAdapter(contentArrayList, null);
                setTabLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(sClass).child("extra_content").child("activity_videos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        if (snapshot.getValue() != null) {


                            HashMap<String, Object> booksHashMap = (HashMap<String, Object>) snapshot.getValue();
                            ActivityVideosArrayList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : booksHashMap.entrySet()) {
                                ArrayList<HashMap<String, Object>> booksData = (ArrayList<HashMap<String, Object>>) entry.getValue();
                                for (HashMap<String, Object> item : booksData) {
                                    ActivityVideosArrayList.add(item);
                                }
                            }



                            BooksListingActivityAdapter booksAdapter = new BooksListingActivityAdapter(context, ActivityVideosArrayList,position);
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_left","anim",getPackageName()));
                            recyclerView.setLayoutAnimation(animation);
                            booksAdapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setAdapter(booksAdapter);
                            isloading=false;
                            hideconnection_layout();
                            setPagerAdapter(contentArrayList, topicName);
                            setTabLayout();

                            booksAdapter.SetOnItemClickListener(new BooksListingActivityAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                                    for (Fragment fragment : allFragments) {
                                        if(fragment instanceof PalBooksListingFragment){
                                            ((PalBooksListingFragment)fragment).refreshAdapter(position);
                                        }
                                    }
                                }
                            });
                        }
                        else
                        {
                            isloading=false;
                            hideconnection_layout();
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


    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    ProgressBar mProgressBar;
    LinearLayout connection_layout;

    private void checkConnection(boolean first) {

        mProgressBar = findViewById(R.id.progressBar);
        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);
        connection_layout = findViewById(R.id.connection_layout);


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
                        hideconnection_layout();
                    }
                }
            }, 10000);//time in milisecond

            imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            connection_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("-");
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
        connection_layout.setVisibility(View.GONE);
    }

    public ArrayList<HashMap<String, String>> contentArrayList;
    public ArrayList<HashMap<String, Object>> contentArrayList1;
    public static ArrayList<HashMap<String, Object>> contentArrayList_;
    public ArrayList<HashMap<String, Object>> videosArrayList;
    public static ArrayList<HashMap<String, Object>> videoContentArrayList;
    public ArrayList<HashMap<String, Object>> booksArrayList;

    private void gedtBooks() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/extra_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject aa = jsonObject.getJSONObject(board);
                JSONObject object_ = aa.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONObject object_D_ = object_.getJSONObject(sClass);
                JSONObject object__ = object_D_.getJSONObject("extra_content");
                JSONObject object_______ = null;
                Iterator<String> subjectIterator = object__.keys();
                Iterator<String> subjectIterator_ = object__.keys();
                ArrayList<String> topicArrayList = new ArrayList<>();
                ArrayList<Integer> topicLengthArrayList = new ArrayList<>();
                while (subjectIterator.hasNext()) {
                    String subject_ = subjectIterator.next();

                    if (subject_.equalsIgnoreCase("books")) {

                        if(object__.has("books")){
                            JSONObject object______ = object__.getJSONObject("books");
                            object_______ = object______;
                            Iterator<String> iterator = object______.keys();
                            String key = "";
                            while (iterator.hasNext()) {
                                key = iterator.next();
                                JSONArray object1e2 = object______.getJSONArray(key);
                                JSONObject object12 = object1e2.getJSONObject(0);

                                topicArrayList.add(object12.getString("name"));
                                topicLengthArrayList.add(object1e2.length());
                                PalHomeFragment.subjectArrayList.add(subject);
                            }
                        }

                    }


                }

                String topicName = PalHomeFragment.topicArrayListid.get(position);

                JSONArray object1e2 = object_______.getJSONArray(topicName);
                JSONObject object12 = object1e2.getJSONObject(0);

//                for(int i=0;i<object1e2.length()-1;i++)
//                {
//                    JSONObject object122 = object1e2.getJSONObject(i);
//                    topicArrayList.add(object122.getString("name"));
//                }
//                String name=object12.getString("name");

                Iterator<String> iterator_ = object12.keys();
                String key_ = "";
                contentArrayList = new ArrayList<>();
                HashMap<String,Object> maap = new HashMap<>();

                while (iterator_.hasNext()) {
                    HashMap<String, String> contentHashMap = new HashMap<>();
                    key_ = iterator_.next();
                    booksArrayList=new ArrayList<>();
                    try {
                        if(key_.equalsIgnoreCase("topics"))
                        {
                            JSONArray array = object12.getJSONArray(key_);

                            ArrayList<HashMap<String,Object>> f=new ArrayList<>();

                            for(int i=0;i<=array.length()-1;i++)
                            {
                                JSONObject inFIne = array.getJSONObject(i);

                                String vName = inFIne.getString("name");
                                String onlineLink = inFIne.getString("onlineLink");
                                String offlineLink = inFIne.getString("offlineLink");
                                contentHashMap.put("name", vName);
                                contentHashMap.put("onlineLink", onlineLink);
                                contentHashMap.put("offlineLink", offlineLink);
                                HashMap<String,Object> maap2 = new HashMap<>();

                                maap2.put("onlineLink",inFIne.getString("onlineLink"));
                                maap2.put("offlineLink",inFIne.getString("offlineLink"));
                                maap2.put("name",inFIne.getString("name"));
                                maap2.put("subject",inFIne.getString("subjectID"));
                                maap2.put("id",inFIne.getString("id"));
                                maap2.put("subjectID",inFIne.getString("subjectID"));
                                maap2.put("topicName",inFIne.getString("topicName"));

                                f.add(maap2);
                                maap.put(key_,f);
                                booksArrayList.add(maap);
                                contentArrayList.add(contentHashMap);
                            }

                        }

                    }
                    catch (Exception rr)
                    {
                        rr.printStackTrace();
                    }
                }

                if (topicArrayList.size() > 0) {
                    textViewChapterCount.setText(topicArrayList.size() + " Chapters");
                    ExtraContentTopicAdapter extraContentAdapter = new ExtraContentTopicAdapter(context, PalHomeFragment.topicArrayList, topicLengthArrayList, type);
                    recyclerView.setAdapter(extraContentAdapter);
                    JSONObject finalObject_______ = object_______;
                    extraContentAdapter.SetOnItemClickListener(new ExtraContentTopicAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try {

//                                if (object.has("books")) {
//                                    JSONObject object______ = object.getJSONObject("books");
//                                    object_ = object______.getJSONObject("topics");
//                                }

//                                if(object__.has("books")){
//                                    JSONObject object______ = object__.getJSONObject("books");
//                                    Iterator<String> iterator = object______.keys();
//                                    String key = "";
//                                    while (iterator.hasNext()) {
//                                        key = iterator.next();
//                                        topicArrayList.add(key);
//                                        topicLengthArrayList.add(0);
//                                        PalHomeFragment.subjectArrayList.add(subject);
//                                    }
//                                }
//
//                                while (subjectIterator_.hasNext()) {
//                                    subject = subjectIterator_.next();
//                                    JSONObject object___ = object__.getJSONObject(subject);
//                                    if (object___.has("books")) {
//                                        JSONObject object_____ = object___.getJSONObject("books");
//                                        JSONObject object______ = object_____.getJSONObject("topics");
//                                        Iterator<String> iterator = object______.keys();
//                                        String key = "";
//                                        while (iterator.hasNext()) {
//                                            key = iterator.next();
//                                            topicArrayList.add(key);
//                                            int lenght = object______.getJSONObject(key).length();
//                                            topicLengthArrayList.add(lenght);
//                                        }
//                                    }
//                                }
                                String topicName = PalHomeFragment.topicArrayList.get(position);

                                JSONArray object1e2 = finalObject_______.getJSONArray(topicName);
                                JSONObject object12 = object1e2.getJSONObject(0);

                                Iterator<String> iterator_ = object12.keys();
                                String key_ = "";
                                contentArrayList = new ArrayList<>();
                                HashMap<String,Object> maap = new HashMap<>();

                                while (iterator_.hasNext()) {
                                    HashMap<String, String> contentHashMap = new HashMap<>();
                                    key_ = iterator_.next();
                                    booksArrayList=new ArrayList<>();
                                    try {
                                        if(key_.equalsIgnoreCase("topics"))
                                        {
                                            JSONArray array = object12.getJSONArray(key_);

                                            ArrayList<HashMap<String,Object>> f=new ArrayList<>();

                                            for(int i=0;i<=array.length()-1;i++)
                                            {
                                                JSONObject inFIne = array.getJSONObject(i);

                                                String vName = inFIne.getString("name");
                                                String onlineLink = inFIne.getString("onlineLink");
                                                String offlineLink = inFIne.getString("offlineLink");
                                                contentHashMap.put("name", vName);
                                                contentHashMap.put("onlineLink", onlineLink);
                                                contentHashMap.put("offlineLink", offlineLink);
                                                HashMap<String,Object> maap2 = new HashMap<>();

                                                maap2.put("onlineLink",inFIne.getString("onlineLink"));
                                                maap2.put("offlineLink",inFIne.getString("offlineLink"));
                                                maap2.put("name",inFIne.getString("name"));
                                                maap2.put("subject",inFIne.getString("subjectID"));
                                                maap2.put("id",inFIne.getString("id"));
                                                maap2.put("subjectID",inFIne.getString("subjectID"));
                                                maap2.put("topicName",inFIne.getString("topicName"));

                                                f.add(maap2);
                                                maap.put(key_,f);
                                                booksArrayList.add(maap);
                                                contentArrayList.add(contentHashMap);
                                            }

                                        }

                                    }
                                    catch (Exception rr)
                                    {
                                        rr.printStackTrace();
                                    }
                                }

//                                String topicName = PalHomeFragment.topicArrayList.get(position);
//                                JSONObject object1 = object_.getJSONObject(topicName);
//                                Iterator<String> iterator_ = object1.keys();
//                                String key_ = "";
//                                contentArrayList = new ArrayList<>();
//                                while (iterator_.hasNext()) {
//                                    HashMap<String, String> contentHashMap = new HashMap<>();
//                                    key_ = iterator_.next();
//                                    JSONObject inFIne = object1.getJSONObject(key_);
//                                    String vName = inFIne.getString("name");
//                                    String onlineLink = inFIne.getString("onlineLink");
//                                    String offlineLink = inFIne.getString("offlineLink");
//                                    contentHashMap.put("name", vName);
//                                    contentHashMap.put("onlineLink", onlineLink);
//                                    contentHashMap.put("offlineLink", offlineLink);
//                                    contentArrayList.add(contentHashMap);
//                                }
                                name = topicName;
                                setPagerAdapter(contentArrayList, topicName);
                                setTabLayout();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    name = topicName;
                    setPagerAdapter(contentArrayList, topicName);
                    setTabLayout();

                } else {
                    Util.showToast(context, "Not available");
                }


                hideconnection_layout();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(sClass).child("extra_content").child("books").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        if (snapshot.getValue() != null) {


                            HashMap<String, Object> booksHashMap = (HashMap<String, Object>) snapshot.getValue();
                            booksArrayList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : booksHashMap.entrySet()) {
                                ArrayList<HashMap<String, Object>> booksData = (ArrayList<HashMap<String, Object>>) entry.getValue();
                                for (HashMap<String, Object> item : booksData) {
                                    booksArrayList.add(item);
                                }
                            }
                            textViewChapterCount.setText(booksArrayList.size() + " Chapters");
                            for (int i = 0; i < booksArrayList.size(); i++) {
                                booksArrayList.get(i).put("selected", "false");
                            }

                            booksArrayList.get(position).put("selected", "true");


                            BooksListingActivityAdapter booksAdapter = new BooksListingActivityAdapter(context, booksArrayList,position);
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_left","anim",getPackageName()));
                            recyclerView.setLayoutAnimation(animation);
                            booksAdapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setAdapter(booksAdapter);

                            setPagerAdapter(contentArrayList, topicName);
                            setTabLayout();

                            booksAdapter.SetOnItemClickListener(new BooksListingActivityAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    for (int i = 0; i < booksArrayList.size(); i++) {
                                        booksArrayList.get(i).put("selected", "false");
                                    }
                                    booksArrayList.get(position).put("selected", "true");


                                    List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                                    for (Fragment fragment : allFragments) {
                                        if(fragment instanceof PalBooksListingFragment){
                                            ((PalBooksListingFragment)fragment).refreshAdapter(position);
                                            booksAdapter.notifyDataSetChanged();
                                        }
                                    }}
                            });


                            hideconnection_layout();
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

    private void online() {
        global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(sClass).child("extra_content").child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    if (snapshot.getValue() != null) {


                        HashMap<String, Object> booksHashMap = (HashMap<String, Object>) snapshot.getValue();
                        booksArrayList = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : booksHashMap.entrySet()) {
                            ArrayList<HashMap<String, Object>> booksData = (ArrayList<HashMap<String, Object>>) entry.getValue();
                            for (HashMap<String, Object> item : booksData) {
                                booksArrayList.add(item);
                            }
                        }
                        textViewChapterCount.setText(booksArrayList.size() + " Chapters");
                        for (int i = 0; i < booksArrayList.size(); i++) {
                            booksArrayList.get(i).put("selected", "false");
                        }

                        booksArrayList.get(position).put("selected", "true");


                        BooksListingActivityAdapter booksAdapter = new BooksListingActivityAdapter(context, booksArrayList,position);
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_left","anim",getPackageName()));
                        recyclerView.setLayoutAnimation(animation);
                        booksAdapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                        recyclerView.setAdapter(booksAdapter);

                        setPagerAdapter(contentArrayList, topicName);
                        setTabLayout();

                        booksAdapter.SetOnItemClickListener(new BooksListingActivityAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                for (int i = 0; i < booksArrayList.size(); i++) {
                                    booksArrayList.get(i).put("selected", "false");
                                }
                                booksArrayList.get(position).put("selected", "true");


                                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                                for (Fragment fragment : allFragments) {
                                    if(fragment instanceof PalBooksListingFragment){
                                        ((PalBooksListingFragment)fragment).refreshAdapter(position);
                                        booksAdapter.notifyDataSetChanged();
                                    }
                                }}
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

    private void getBooks() {

        gedtBooks();
//        online();


//        isloading=true;
//        checkConnection(false);
//        if (Util.isOfflineMode(context)) {
//            String filePath = ".iDream_content/offlinetab_PAL/extra_content.txt";
//            JSONObject jsonObject = Util.readJsonFile(context, filePath);
//            try {
//                JSONObject aa = jsonObject.getJSONObject(board);
//                JSONObject object_ = aa.getJSONObject(Util.getSelectedLanguagePackage(context));
//                System.out.println("------ object_ "+object_);
//                JSONObject object_d_ = object_.getJSONObject(sClass);
//                System.out.println("------ object_d_ "+object_d_);
//                JSONObject object__ = object_d_.getJSONObject("extra_content");
//                System.out.println("------ object__ "+object__);
//                JSONObject object_______ = null;
//                Iterator<String> subjectIterator = object__.keys();
//                Iterator<String> subjectIterator_ = object__.keys();
//                ArrayList<String> topicArrayList = new ArrayList<>();
//                ArrayList<Integer> topicLengthArrayList = new ArrayList<>();
//                while (subjectIterator.hasNext()) {
//                    String subject_ = subjectIterator.next();
//                    System.out.println("----- subject " + subject_);
//                    if (object__.has("books")) {
//                        JSONObject object______ = object__.getJSONObject("books");
//
//                        System.out.println("------ object______ 1 "+object______);
//                        object_______=object______;
//                        Iterator<String> iterator = object______.keys();
//                        System.out.println("--------- iterator "+iterator);
//                        String key = "";
//                        while (iterator.hasNext()) {
//                            key = iterator.next();
//                            topicArrayList.add(key);
//
//                            System.out.println("======= key "+key);
//                            System.out.println("======= topicArrayList "+topicArrayList);
//
////                            int lenght = object_______.getJSONObject(key).length();
////                                topicLengthArrayList.add(lenght);
////                                topicArrayList.add(key);
////                                int lenght = object______.getJSONObject(key).length();
//                            //topicLengthArrayList.add(lenght);
//                            //subjectArrayList.add(subject);
//                        }
//                    }
//
//                    if(subject_.equalsIgnoreCase(subject)){
//                        JSONObject object____ = object__.getJSONObject(subject);
//                    }
//                }
//                String subject;
//                while (subjectIterator_.hasNext()) {
//                    subject = subjectIterator_.next();
//                    JSONObject object___ = object__.getJSONObject(subject);
//                    if(object___.has("books")){
//                        JSONObject object_____ = object___.getJSONObject("books");
////                        JSONObject object______ = object_____.getJSONObject("topics");
//                        Iterator<String> iterator = object_____.keys();
//                        String key = "";
//                        while (iterator.hasNext()) {
//                            key = iterator.next();
//                            topicArrayList.add(key);
////                            int lenght = object_____.getJSONObject(key).length();
//                            int lenght = 0;
//                            topicLengthArrayList.add(lenght);
//                        }
//                    }
//                }
//                String topicName = topicArrayList.get(position);
//
//                System.out.println("------- position "+position);
//                System.out.println("------- topicName "+topicName);
//                System.out.println("------- object_______ "+object_______);
//
//                JSONObject object12 = object_______.getJSONObject(topicName);
//                Iterator<String> iterator_ = object12.keys();
//                String key_ = "";
//                contentArrayList = new ArrayList<>();
//                while (iterator_.hasNext()) {
//                    HashMap<String, String> contentHashMap = new HashMap<>();
//                    key_ = iterator_.next();
//                    JSONObject inFIne = object12.getJSONObject(key_);
//                    String vName = inFIne.getString("name");
//                    String onlineLink = inFIne.getString("onlineLink");
//                    String offlineLink = inFIne.getString("offlineLink");
//                    contentHashMap.put("name", vName);
//                    contentHashMap.put("onlineLink", onlineLink);
//                    contentHashMap.put("offlineLink", offlineLink);
//                    contentArrayList.add(contentHashMap);
//                }
//
//                if (topicArrayList.size() > 0) {
//                    textViewChapterCount.setText(topicArrayList.size() + " Chapters");
//                    ExtraContentTopicAdapter extraContentAdapter = new ExtraContentTopicAdapter(context, PalHomeFragment.topicArrayList, topicLengthArrayList, type);
//                    recyclerView.setAdapter(extraContentAdapter);
//                    extraContentAdapter.SetOnItemClickListener(new ExtraContentTopicAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            try {
//                                String subject = subjects.get(position);
//                                JSONObject object = object__.getJSONObject(subject);
//                                JSONObject object_ = null;
//                                if (object.has("books")) {
//                                    JSONObject object______ = object.getJSONObject("books");
//                                    object_ = object______.getJSONObject("topics");
//                                }
//                                String topicName = PalHomeFragment.topicArrayList.get(position);
//                                JSONObject object1 = object_.getJSONObject(topicName);
//                                Iterator<String> iterator_ = object1.keys();
//                                String key_ = "";
//                                contentArrayList = new ArrayList<>();
//                                while (iterator_.hasNext()) {
//                                    HashMap<String, String> contentHashMap = new HashMap<>();
//                                    key_ = iterator_.next();
//                                    JSONObject inFIne = object1.getJSONObject(key_);
//                                    String vName = inFIne.getString("name");
//                                    String onlineLink = inFIne.getString("onlineLink");
//                                    String offlineLink = inFIne.getString("offlineLink");
//                                    contentHashMap.put("name", vName);
//                                    contentHashMap.put("onlineLink", onlineLink);
//                                    contentHashMap.put("offlineLink", offlineLink);
//                                    contentArrayList.add(contentHashMap);
//                                }
//                                name = topicName;
//                                setPagerAdapter(contentArrayList, topicName);
//                                setTabLayout();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    name = topicName;
//                    setPagerAdapter(contentArrayList, topicName);
//                    setTabLayout();
//
//                } else {
//                    Util.showToast(context, "Not available");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }else {
//            global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(sClass).child("extra_content").child("books").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    try {
//                        if (snapshot.getValue() != null) {
//
//
//                            HashMap<String, Object> booksHashMap = (HashMap<String, Object>) snapshot.getValue();
//                            booksArrayList = new ArrayList<>();
//                            for (Map.Entry<String, Object> entry : booksHashMap.entrySet()) {
//                                ArrayList<HashMap<String, Object>> booksData = (ArrayList<HashMap<String, Object>>) entry.getValue();
//                                for (HashMap<String, Object> item : booksData) {
//                                    booksArrayList.add(item);
//                                }
//                            }
//                            textViewChapterCount.setText(booksArrayList.size() + " Chapters");
//                            for (int i = 0; i < booksArrayList.size(); i++) {
//                                booksArrayList.get(i).put("selected", "false");
//                            }
//
//                            booksArrayList.get(position).put("selected", "true");
//
//
//                            BooksListingActivityAdapter booksAdapter = new BooksListingActivityAdapter(context, booksArrayList,position);
//                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_left","anim",getPackageName()));
//                            recyclerView.setLayoutAnimation(animation);
//                            booksAdapter.notifyDataSetChanged();
//                            recyclerView.scheduleLayoutAnimation();
//                            recyclerView.setAdapter(booksAdapter);
//                            isloading=false;
//                            hideconnection_layout();
//                            setPagerAdapter(contentArrayList, topicName);
//                            setTabLayout();
//
//                            booksAdapter.SetOnItemClickListener(new BooksListingActivityAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//
//                                    for (int i = 0; i < booksArrayList.size(); i++) {
//                                        booksArrayList.get(i).put("selected", "false");
//                                    }
//                                    booksArrayList.get(position).put("selected", "true");
//
//
//                                    List<Fragment> allFragments = getSupportFragmentManager().getFragments();
//                                    for (Fragment fragment : allFragments) {
//                                        if(fragment instanceof PalBooksListingFragment){
//                                            ((PalBooksListingFragment)fragment).refreshAdapter(position);
//                                            booksAdapter.notifyDataSetChanged();
//                                        }
//                                    }}
//                            });
//                        }
//                        else
//                        {
//                            isloading=false;
//                            hideconnection_layout();
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
//
//        }
    }

    public void getPracticeContent(){
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
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getFoundationalTopicVideos(){
        if (Util.isOfflineMode(context)) {
            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__ = jsonObject.getJSONObject(language);
                JSONObject object___ = object__.getJSONObject("video_lessons");
                JSONObject object____ = object___.getJSONObject("content");
                JSONObject object_____ = object____.getJSONObject(subject);
                JSONObject object______ = object_____.getJSONObject("topics");
                JSONArray array = object______.getJSONArray(topicId);
                videosArrayList = new ArrayList<>();
                videoContentArrayList = new ArrayList<>();
                for (int i = 1; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                    LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                    for (int j = 0; j < object1.names().length(); j++) {
                        HashMap<String, Object> newHashMap_ = new HashMap<>();
                        String key = object1.names().getString(j);
                        JSONObject object2 = object1.getJSONObject(key);
                        String AssessmentTopicID = object2.getString("AssessmentTopicID");
                        String detail = object2.getString("detail");
                        String name = object2.getString("name");
                        String offlineLink = object2.getString("offlineLink");
                        String offlineThumbnail = object2.getString("offlineThumbnail");
                        String onlineLink = object2.getString("onlineLink");
                        String thumbnail = object2.getString("thumbnail");
                        String topicName = object2.getString("topicName");
                        newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                        newHashMap_.put("detail", detail);
                        newHashMap_.put("name", name);
                        newHashMap_.put("offlineLink", offlineLink);
                        newHashMap_.put("offlineThumbnail", offlineThumbnail);
                        newHashMap_.put("onlineLink", onlineLink);
                        newHashMap_.put("thumbnail", thumbnail);
                        newHashMap_.put("topicName", topicName);
                        newHashMap_.put("topicID", topicId);
                        newHashMap_.put("isSelected", "false");
                        newHashMap.put(key, newHashMap_);
                        videoHashMap.put(i + "-" + j, newHashMap_);
                    }
                    videosArrayList.add(newHashMap);
                    videoContentArrayList.add(videoHashMap);
                }
                videoListAdapter = new VideoListAdapter(context, videosArrayList, sClass, subject, language, board, icon, null, dataMap, type);
                videoRecyclerView.setAdapter(videoListAdapter);

                if (Util.getVideoLevel(context) == 0) {
                    videoRecyclerView.smoothScrollToPosition(0);
                } else {
                    videoRecyclerView.smoothScrollToPosition(Util.getVideoLevel(context));
                }
                videoListAdapter.SetOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                getVideoReports();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass).child(language).child("video_lessons").child("content").child(subject).child("topics").child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            contentArrayList1 = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
                            contentArrayList_ = new ArrayList<>();
                            videoContentArrayList = new ArrayList<>();

                            for (int i = 1; i < contentArrayList1.size(); i++) {
                                LinkedHashMap<String, Object> newHashMap = new LinkedHashMap<>();
                                LinkedHashMap<String, Object> videoHashMap = new LinkedHashMap<>();
                                HashMap<String, Object> videoMap = contentArrayList1.get(i);
                                int j_ = 0;

                                ArrayList<Integer> shortedKey=new ArrayList<>();

                                for(String keys:videoMap.keySet())
                                {
                                    shortedKey.add(Integer.parseInt(keys));
                                }
                                Collections.sort(shortedKey);

                                for (Integer key : shortedKey) {
                                    HashMap<String, Object> newHashMap_ = new HashMap<>();
                                    HashMap<String, String> innerMap = (HashMap<String, String>) contentArrayList1.get(i).get(key+"");
                                    String AssessmentTopicID = innerMap.get("AssessmentTopicID");
                                    String detail = innerMap.get("detail");
                                    String name = innerMap.get("name");
                                    String offlineLink = innerMap.get("offlineLink");
                                    String offlineThumbnail = innerMap.get("offlineThumbnail");
                                    String onlineLink = innerMap.get("onlineLink");
                                    String thumbnail = innerMap.get("thumbnail");
                                    String topicName = innerMap.get("topicName");
                                    newHashMap_.put("AssessmentTopicID", AssessmentTopicID);
                                    newHashMap_.put("detail", detail);
                                    newHashMap_.put("name", name);
                                    newHashMap_.put("offlineLink", offlineLink);
                                    newHashMap_.put("offlineThumbnail", offlineThumbnail);
                                    newHashMap_.put("onlineLink", onlineLink);
                                    newHashMap_.put("thumbnail", thumbnail);
                                    newHashMap_.put("topicName", topicName);
                                    newHashMap_.put("topicID", topicId);
                                    newHashMap_.put("isSelected", "false");
                                    newHashMap_.put("key", key+"");
                                    ((HashMap<String, String>) contentArrayList1.get(i).get(key+"")).put("isSelected", "false");
                                    ((HashMap<String, String>) contentArrayList1.get(i).get(key+"")).put("topicID", topicId);
                                    newHashMap.put(key+"", newHashMap_);
                                    videoHashMap.put(i + "-" + j_, newHashMap_);
                                    j_++;
                                }
                                contentArrayList_.add(newHashMap);
                                videoContentArrayList.add(videoHashMap);

                                videoListAdapter = new VideoListAdapter(context, contentArrayList_, sClass, subject, language, board, icon, null, dataMap, type);
                                videoRecyclerView.setAdapter(videoListAdapter);

                                if (Util.getVideoLevel(context) == 0) {
                                    videoRecyclerView.smoothScrollToPosition(0);
                                } else {
                                    videoRecyclerView.smoothScrollToPosition(Util.getVideoLevel(context));
                                }
                                videoListAdapter.SetOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                    }
                                });
                                getVideoReports();
                            }

                            hideconnection_layout();
//                            hide_connectionlayout();
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

    private void getVideoReports() {
        if(!Util.isNetworkAvailable(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicId);
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video_lessons").child(topicId).child("detail").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {

                        if (snapshot.getValue() != null) {
                            dataMap = (HashMap<String, Object>) snapshot.getValue();
                        } else {
                            dataMap = null;
                        }
//                        videoListAdapter.dataMap = dataMap;
                        videoListAdapter.notifyDataSetChanged();

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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String type, String topicId){
        getList(userId, board, sClass, subject, type, topicId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {
                        ArrayList<ReportsLatestDataVideoModel> list = (ArrayList<ReportsLatestDataVideoModel>) o;
                        HashMap<String, Object> data = new HashMap<>();
                        HashMap<String, String> dataItem = new HashMap<>();
                        for(ReportsLatestDataVideoModel item: list){
                            dataItem.put("time", item.getVTime());
                            dataItem.put("topicName", item.getTopicName());
                            dataItem.put("totalTime", item.getTotalTime());
                            dataItem.put("videoName", item.getVideoName());
                            if(item.getUrl() != null){
                                data.put(item.getUrl(), dataItem);
                            }else{
                                data.put(item.getVId(), dataItem);
                            }
                        }
                        dataMap = data;
//                        videoListAdapter.dataMap = dataMap;
                        videoListAdapter.notifyDataSetChanged();
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

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String type, String topicId){
        return Observable.fromCallable(() -> {
            //do something, get your Data object
            return reportsLatestDataVideoRepository.getDetail(userId, board, sClass, subject, type, topicId);
        });
    }

    public void getTopicSeenVideoListing(){
        runBackgroundTask(Util.getUserId(context), board, sClass, subject, null, null);
    }

    public static void openVideoView(String videoData){
        if(videoContentArrayList != null && videoData != null){
            String[] videoKeySeparated = videoData.split("-");
            videoLevel = Integer.parseInt(videoKeySeparated[0]);
            videoPosition = Integer.parseInt(videoKeySeparated[1]);
            for(int i = 0; i < videoContentArrayList.size(); i++){
                if(videoContentArrayList.get(i).containsKey(videoLevel + "-" +videoPosition)){
                    HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(i).get(videoLevel + "-" +videoPosition);
                    String offlineLink = (String) data.get("offlineLink");
                    String onlineLink = (String) data.get("onlineLink");
                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+offlineLink;
                    File file = new File(filePath);
                    String url;
                    boolean isLocalFile;

                    if (!file.exists()) {
                        filePath = Util.getSDCardPath(context)+"/.iDream_content/multimediaEE/"+offlineLink;
                        file = new File(filePath);
                    }

                    if(file.exists() && Util.isOfflineMode(context)){
                        url = file.toString();
                        isLocalFile = true;
                    }else{
                        String[] urlArray = onlineLink.split("/");
                        url = urlArray[urlArray.length - 1];
                        isLocalFile = false;
                    }
                    videoKey = videoLevel + "-" +videoPosition;
                    openFragment(url, (String) data.get("name"), (String) data.get("topicID"), type, isLocalFile);
                    break;
                }else{
                    if(i == videoContentArrayList.size() - 1){
                        videoLevel = Integer.parseInt(videoKeySeparated[0]) + 1;
                        videoPosition = 0;
                        for(int j = 0; j < videoContentArrayList.size(); j++){
                            if(videoContentArrayList.get(j).containsKey(videoLevel + "-" +videoPosition)){
                                HashMap<String, Object> data = (HashMap<String, Object>) videoContentArrayList.get(j).get(videoLevel + "-" +videoPosition);
                                String offlineLink = (String) data.get("offlineLink");
                                String onlineLink = (String) data.get("onlineLink");
                                String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+offlineLink;
                                File file = new File(filePath);
                                String url;
                                boolean isLocalFile;
                                String type;
                                if(file.exists() && Util.isOfflineMode(context)){
                                    url = file.toString();
                                    isLocalFile = true;
                                    type = "local";
                                }else{
                                    String[] urlArray = onlineLink.split("/");
                                    url = urlArray[urlArray.length - 1];
                                    isLocalFile = false;
                                    type = "vimeo";
                                }
                                videoKey = videoLevel + "-" +videoPosition;
                                openFragment(url, (String) data.get("name"), (String) data.get("topicID"), type, isLocalFile);
                                break;
                            }else{
                                if(j==videoContentArrayList.size()-1){

                                    String noMoreVideo;
                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                    {
                                        noMoreVideo = "दिखाने के लिए और कोई वीडियो नहीं.";
                                    }else {
                                        noMoreVideo ="No more Videos to show.";
                                    }
                                    Util.openGifDialogue(context,"No more Videos to show.");

                                    Toast.makeText(context, noMoreVideo, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {


        Util.preventPause(context,getTaskId());

        super.onPause();
    }




}