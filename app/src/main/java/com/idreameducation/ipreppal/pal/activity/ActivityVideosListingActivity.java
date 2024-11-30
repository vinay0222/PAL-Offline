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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.StemListingSideAdapter;
import com.idreameducation.ipreppal.pal.adapter.StemVideoListingAdapter;
import com.idreameducation.ipreppal.pal.fragments.IPrepYoutubeVideoPlayerFragment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class ActivityVideosListingActivity extends AppCompatActivity {

    private Context context;
    private Global global;
    public String board;
    public String sClass;
    public String subject;
    public String subjectName;
    public String categoryNAme;
    public String language;
    private RecyclerView recyclerView;
    private RecyclerView videoRecyclerView;
    private TextView textViewSubjectName;
    private TextView textViewVideoTitle;
    private ImageView imageViewBack;
    private ImageView imageViewSubject;
    private RequestOptions requestOptions;
    private ProgressBar mProgressBar;
    private VideoView videoView;
    private RelativeLayout relativeLayout;
    private final boolean isFullScreenModeEnabled = true;
    private FrameLayout frameLayout;
    private Fragment iPrepYoutubeVideoPlayerFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FrameLayout yt_video_container;
    private TextView videoName_Text;
    public boolean isVideoFullScreen = false;
    private String color;
    private Boolean isFabOpen = false;
    public FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;
    public ArrayList<HashMap<String, Object>> contentArrayList;
    private int adapterPos = 0;
    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    ProgressBar ProgressBar;
    LinearLayout layout_connection;
    private Uri filePath;
    private String linkFromFirebaseStorage,textSendWithImageTextString;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;

    public static ArrayList<String> support_Language;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_videos_listing);
        assignIds();
        listners();
        hideStatusBar();
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

    private void checkConnection(boolean first) {
        if(!Util.isOfflineMode(context)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            ProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);
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

    }

    private void hideconnection_layout() {
        isloading=false;
        ProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        layout_connection.setVisibility(View.GONE);
    }

    private void listners() {


        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
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
        findViewById(R.id.fab2).setOnClickListener(new View.OnClickListener() {
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
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewSubject = findViewById(R.id.imageViewSubject);
        textViewSubjectName = findViewById(R.id.textViewSubjectName);
        textViewVideoTitle = findViewById(R.id.textViewVideoTitle);
        board = getIntent().getStringExtra("board");
        sClass = getIntent().getStringExtra("sClass");
        subject = getIntent().getStringExtra("subject");
        subjectName = getIntent().getStringExtra("subjectName");
        categoryNAme = getIntent().getStringExtra("categoryName");
        color = getIntent().getStringExtra("color");
        ProgressBar=findViewById(R.id.progressBar);
        slow_internet_Text=findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=findViewById(R.id.imageViewCrossVideo2);
        layout_connection=findViewById(R.id.layout_connection);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        screenshotText = findViewById(R.id.screenshotText);
        callText = findViewById(R.id.callText);

        topicListingLayout=findViewById(R.id.topicListingLayout);
        videosListLayout=findViewById(R.id.videosListLayout);
        frameLayout = findViewById(R.id.container);
        linearSupport = findViewById(R.id.linearSupport);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        language = Util.getSelectedLanguage(context);
        textViewSubjectName.setText(subjectName);
        textViewSubjectName.setTextColor(Color.parseColor(color));

        //hideNavigationBar(getWindow());
        videoName_Text = findViewById(R.id.videoName);
        yt_video_container = new FrameLayout(this);
        yt_video_container.setId(R.id.yt_video_container);
        yt_video_container.setBackgroundColor(Color.BLACK);


        requestOptions = new RequestOptions();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);

        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setHasFixedSize(true);
        GridLayoutManager videoRecyclerViewManager = new GridLayoutManager(context, 2);
        videoRecyclerView.setLayoutManager(videoRecyclerViewManager);

        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);


        try {
            getListings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/" +subjectName+".png";

        if (Util.isOfflineMode(context))
        {


          //  String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + Util.getSubjectName(context)+".png";

            Glide.with(context).load(completePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageViewSubject);

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Glide.with(context).load(completePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageViewSubject);
                }
            }, 1000);
        }

        get_support_dialog_language();
        showClosedVideoView();
    }

    public void hideStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {


            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );


        }
    }

    private void getListings() throws Exception {

        isloading=true;
        checkConnection(false);
        if (Util.isOfflineMode(context))
        {

            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class"+sClass+language+".txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object = jsonObject.getJSONObject(board);
                JSONObject object_ = object.getJSONObject(language.toLowerCase());
                JSONObject object__ = object_.getJSONObject(sClass);
                JSONObject object___ = object__.getJSONObject(ApplicationConstants.SUBJECTS);
                JSONObject object____ = object___.getJSONObject(subject);
                JSONArray array = object____.getJSONArray(categoryNAme);
                contentArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    String objName = object1.getString("name");
                    JSONArray objArray = object1.getJSONArray("topics");
                    ArrayList<HashMap<String, String>> topicsArray = new ArrayList<>();
                    for (int j = 0; j < objArray.length(); j++) {
                        JSONObject object2 = objArray.getJSONObject(j);
                        HashMap<String, String> newHashMap = new HashMap<>();
                        String detail = object2.getString("detail");
                        String id = object2.getString("id");
                        String contentName = object2.getString("name");
                        String offlineLink = object2.getString("offlineLink");
                        String offlineThumbnail = object2.getString("offlineThumbnail");
                        String onlineLink = object2.getString("onlineLink");
                        String thumbnail = object2.getString("thumbnail");
                        String topicName = object2.getString("topicName");
                        newHashMap.put("detail", detail);
                        newHashMap.put("id", id);
                        newHashMap.put("name", contentName);
                        newHashMap.put("offlineLink", offlineLink);
                        newHashMap.put("offlineThumbnail", offlineThumbnail);
                        newHashMap.put("onlineLink", onlineLink);
                        newHashMap.put("thumbnail", thumbnail);
                        newHashMap.put("topicName", topicName);
                        hashMap.put("name", objName);
                        topicsArray.add(newHashMap);
                        hashMap.put("topics", topicsArray);
                    }
                    contentArrayList.add(hashMap);

                    StemListingSideAdapter stemListingSideAdapter = new StemListingSideAdapter(context, contentArrayList, sClass,color);
                    recyclerView.setAdapter(stemListingSideAdapter);
                    stemListingSideAdapter.SetOnItemClickListener(new StemListingSideAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            adapterPos = position;
                            ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(position).get(ApplicationConstants.TOPICS);
                            textViewVideoTitle.setText((String) contentArrayList.get(position).get("name"));
                            textViewVideoTitle.setTextColor(Color.parseColor(color));
                            StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                            videoRecyclerView.setAdapter(stemVideoListingAdapter);
                            stemVideoListingAdapter.notifyDataSetChanged();
                            stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                    String url = getYouTubeId(completeUrl);
                                    String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                    String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                    int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));

                                    openFragment(url, name,offlineLink,id,adapterPos,position, String.valueOf(contentArrayList.get(adapterPos).get("name")));

                                }
                            });
                        }
                    });

                    ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS);
                    textViewVideoTitle.setText((String) contentArrayList.get(adapterPos).get("name"));
                    textViewVideoTitle.setTextColor(Color.parseColor(color));
                    StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                    videoRecyclerView.setAdapter(stemVideoListingAdapter);
                    stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                            String url = getYouTubeId(completeUrl);
                            String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                            String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                            int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                            openFragment(url, name,offlineLink,id,adapterPos,position, String.valueOf(contentArrayList.get(adapterPos).get("name")));

                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(sClass).child(ApplicationConstants.EXTRA_CONTENT).child(categoryNAme).child(subject).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            contentArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();

                            for (int i = 0; i < contentArrayList.size(); i++) {
                                contentArrayList.get(i).put("selected", "false");
                            }

                            contentArrayList.get(0).put("selected", "true");
                            contentArrayList.get(0).put("color", color);

                            StemListingSideAdapter stemListingSideAdapter = new StemListingSideAdapter(context, contentArrayList, sClass,color);
                            recyclerView.setAdapter(stemListingSideAdapter);
                            stemListingSideAdapter.SetOnItemClickListener(new StemListingSideAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    adapterPos = position;
                                    for (int i = 0; i < contentArrayList.size(); i++) {
                                        contentArrayList.get(i).put("selected", "false");
                                    }
                                    contentArrayList.get(position).put("selected", "true");
                                    stemListingSideAdapter.notifyDataSetChanged();
                                    ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(position).get(ApplicationConstants.TOPICS);
                                    textViewVideoTitle.setText((String) contentArrayList.get(position).get("name"));
                                    textViewVideoTitle.setTextColor(Color.parseColor(color));
                                    StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                                    videoRecyclerView.setAdapter(stemVideoListingAdapter);
                                    stemVideoListingAdapter.notifyDataSetChanged();
                                    stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                            String url = getYouTubeId(completeUrl);
                                            String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                            String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                            int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));

                                            openFragment(url, name,offlineLink,id,adapterPos,position, String.valueOf(contentArrayList.get(adapterPos).get("name")));

                                            //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
                                }
                            });

                            ArrayList<HashMap<String, String>> videoList = (ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS);
                            textViewVideoTitle.setText((String) contentArrayList.get(adapterPos).get("name"));
                            textViewVideoTitle.setTextColor(Color.parseColor(color));
                            StemVideoListingAdapter stemVideoListingAdapter = new StemVideoListingAdapter(context, videoList);
                            videoRecyclerView.setAdapter(stemVideoListingAdapter);
                            stemVideoListingAdapter.SetOnItemClickListener(new StemVideoListingAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String completeUrl = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("onlineLink");
                                    String url = getYouTubeId(completeUrl);
                                    String name = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("name");
                                    String offlineLink = ((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("offlineLink");
                                    int id = Integer.parseInt(((ArrayList<HashMap<String, String>>) contentArrayList.get(adapterPos).get(ApplicationConstants.TOPICS)).get(position).get("id"));
                                    openFragment(url, name,offlineLink,id,adapterPos,position, String.valueOf(contentArrayList.get(adapterPos).get("name")));
                                    //Toast.makeText(context, "Clicked on " + position , Toast.LENGTH_SHORT).show();
                                }
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

    public static String currentVideoID;
    public static String clickedVideoID;

    public void openFragment(String url, String videoName, String offlineLink, int id, int adapterPos,int position,String topicName) {
        removeFragment();

        frameLayout.setVisibility(View.VISIBLE);
        videoName_Text.setVisibility(View.VISIBLE);

        videoName_Text.setText(videoName);

        /* for 10' tab*/

        /* for 8' tab */

        frameLayout.setVisibility(View.GONE);

        String timestamp = System.currentTimeMillis()+"";

        Bundle bundle = new Bundle();

        clickedVideoID=System.currentTimeMillis()+"";

        bundle.putString("url", url);
        bundle.putString("videoName", videoName);
        bundle.putString("offlineLink", offlineLink);
        bundle.putBoolean("isFullScreen", false);
        bundle.putFloat("duration", 0);
        bundle.putInt("id", id);
        bundle.putInt("adapterPos", adapterPos);
        bundle.putInt("position", position);
        bundle.putString("topicName",topicName);
        bundle.putString("subject",subjectName);
        bundle.putString("timestamp",timestamp);
        bundle.putString("clickedVideoID",clickedVideoID);

        Project_SubTopic_Activity.clickedVideoID = clickedVideoID;

        currentVideoID = timestamp;
        System.out.println( "====== id "+id);
        Util.setSubjectId(context,subject);
        subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);

        Util.setSubject(context,subjectName);

        iPrepYoutubeVideoPlayerFragment = new IPrepYoutubeVideoPlayerFragment();
        iPrepYoutubeVideoPlayerFragment.setArguments(bundle);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, iPrepYoutubeVideoPlayerFragment, "tag");
        transaction.commit();
        isVideoFullScreen = false;

        showSmallScreenVideo();

    }

    RelativeLayout topicListingLayout;
    LinearLayout videosListLayout;

    public void showSmallScreenVideo () {
        isVideoFullScreen=false;
        Util.setIsFullScreen(context,false);
        topicListingLayout.setVisibility(View.VISIBLE);
        videosListLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        linearSupport.setVisibility(View.VISIBLE);
    }

    public void showFullScreenVideo () {
        isVideoFullScreen=true;
        Util.setIsFullScreen(context,true);
        topicListingLayout.setVisibility(View.GONE);
        videosListLayout.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        linearSupport.setVisibility(View.GONE);
    }

    private void showClosedVideoView () {
        topicListingLayout.setVisibility(View.VISIBLE);
        videosListLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        linearSupport.setVisibility(View.VISIBLE);
    }

    LinearLayout linearSupport;

    public void openYoutubeVideoFragment(Fragment fragment, String url, String videoName, float duration) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
//        Toast.makeText(context, "Coming into this", Toast.LENGTH_LONG).show();
        //transaction.remove(fragment);
        manager.popBackStack();
        frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.GONE);

        yt_video_container = findViewById(R.id.yt_video_container);
        yt_video_container.setVisibility(View.VISIBLE);
        isVideoFullScreen = false;
//        Fragment newFragment = recreateFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("videoName", videoName);
        bundle.putBoolean("isFullScreen", false);
        bundle.putFloat("duration", duration);
//        newFragment.setArguments(bundle);
//        transaction.add(R.id.yt_video_container, newFragment, "IPrepYoutubeVideoPlayerTag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Fragment recreateFragment(Fragment f) {
        try {
            manager = getSupportFragmentManager();
            Fragment.SavedState savedState = manager.saveFragmentInstanceState(f);

            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        }
        catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
        }
    }

    @SuppressLint("RestrictedApi")
    public void openVimeoVideoFullScreenFragment() {

        showFullScreenVideo();
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) yt_video_container.getLayoutParams();
//        params.width =  metrics.widthPixels;
//        params.height = metrics.heightPixels;
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        params.leftMargin = 0;
//        params.topMargin = 0;
//        params.rightMargin = 0;
//        params.bottomMargin = 0;
//        yt_video_container.setLayoutParams(layoutParams);
//        isVideoFullScreen = true;
//        try {
//            fab.setVisibility(View.GONE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @SuppressLint("RestrictedApi")
    public void backToNormalView(){
        showSmallScreenVideo();
//        frameLayout = findViewById(R.id.container);
//        frameLayout.setVisibility(View.VISIBLE);
//
//        int[] location = new int[2];
//        frameLayout.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int newX = x - convertDpToPx(20);
//
//        int deviceWidth = metrics.widthPixels;
//        int videoLayoutWidth = deviceWidth - newX;
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoLayoutWidth, convertDpToPx(400));
////        layoutParams.setMargins(525, 10, 10, 200);
//        layoutParams.setMargins(570, 10, 10, 400);
//        setMargins(yt_video_container,545,10,10,200);
//        yt_video_container.setLayoutParams(layoutParams);
//        yt_video_container.setClickable(true);
//        yt_video_container.setFocusable(true);
//
//        try {
//            fab.setVisibility(View.VISIBLE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
////        switch(screenSize) {
////            case Configuration.SCREENLAYOUT_SIZE_LARGE:
////                setMargins(yt_video_container,350,10,15,260);
////                break;
////            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
////                setMargins(yt_video_container,460,50,15,260);
////                break;
////            case Configuration.SCREENLAYOUT_SIZE_SMALL:
////                setMargins(yt_video_container,465,0,20,345);
////                break;
////
////            default:
////                setMargins(yt_video_container,465,0,20,345);
////        }
//        frameLayout.setVisibility(View.GONE);
//        isVideoFullScreen = false;
    }

    public void removeFragment() {

        try {
            IPrepYoutubeVideoPlayerFragment.requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    Log.d("DEBUG", "request running: " + request.getTag().toString());
                    return true;
                }
            });

        } catch (Exception r){
            r.printStackTrace();
        }

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        if (fragment != null) {
            try {
                transaction.remove(fragment);
                transaction.commit();
                manager.popBackStack();
            } catch (Exception e) {
            }
        }
        Fragment fragment2 = manager.findFragmentByTag("tag");

        clearBackStack();

        ViewGroup parent = (ViewGroup) yt_video_container.getParent();
        if(parent != null){
            parent.removeView(yt_video_container);
        }
        videoName_Text.setVisibility(View.GONE);
        isVideoFullScreen = false;

        showClosedVideoView();
    }
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    private boolean videoFragmentExists() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag("tag");
        return fragment != null;
    }
    private int convertDpToPx(int dp) {
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }
    private String getYouTubeId(String youTubeUrl) {
//        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
//        Pattern compiledPattern = Pattern.compile(pattern);
//        Matcher matcher = compiledPattern.matcher(youTubeUrl);
//        if (matcher.find()) {
//            return matcher.group();
//        } else {
//            return "error";
//        }
        return youTubeUrl;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(isVideoFullScreen){
            backToNormalView();
        }else{
            if(videoFragmentExists()){
                removeFragment();
                setMargins(textViewVideoTitle, 20, 15, 20, 0);
            }else{
                super.onBackPressed();
                
            }
        }

    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }


}