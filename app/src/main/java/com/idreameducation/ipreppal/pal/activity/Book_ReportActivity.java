package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button2Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.callNo;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading3Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.heading4Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.hideNavigationBar;

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
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.BookRep_Model;
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

public class Book_ReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back_btn;
    TextView type_text;
    Context context;
    private RelativeLayout reletiveNoReports,relativeReports;
    String from="books_stories";
    String name;
    private Global global;
    ArrayList<String> dateList;
    private String no_reports_to_show,your_usage_reports,start_learning;
    private TextView title,noReportsTextView,noReportsTextViewDetail,startLearningTextView;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_book_report);
        global = (Global) getApplicationContext();
        init();
//        hideNavigationBar(getWindow());
    }

    private void init() {

        context=this;

        recyclerView=findViewById(R.id.recyclerView);
        type_text=findViewById(R.id.type_text);
        back_btn=findViewById(R.id.back_btn);

        reletiveNoReports = findViewById(R.id.reletiveNoReports);
        relativeReports = findViewById(R.id.reletiveReports);

        Bundle bundle = getIntent().getExtras();

        from = bundle.getString("from", "Default");
        name = bundle.getString("name", "Default");

        type_text.setText(name);

        dateList=new ArrayList<>();


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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getRecords();
    }

    private void getRecords() {
        if (from.equalsIgnoreCase("project_videos")) {
            FirebaseDatabase.getInstance().getReference("pal").child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context))
                    .child(Util.getSelectedLanguage(context)).child(from)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null)
                            {
                                for(DataSnapshot ss:snapshot.getChildren())
                                {
                                    dateList.add(ss.getKey());
                                }

                                recyclerView.setAdapter(new Book_report_Adapter(context,dateList,snapshot));
                                reletiveNoReports.setVisibility(View.GONE);
                                relativeReports.setVisibility(View.VISIBLE);
                            }else {
                                reletiveNoReports.setVisibility(View.VISIBLE);
                                relativeReports.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else {
            FirebaseDatabase.getInstance().getReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context))
                    .child(Util.getSelectedLanguage(context)).child(from).child(from)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue()!=null)
                            {
                                for(DataSnapshot ss:snapshot.getChildren())
                                {
                                    dateList.add(ss.getKey());
                                }

                                recyclerView.setAdapter(new Book_report_Adapter(context,dateList,snapshot));
                                reletiveNoReports.setVisibility(View.GONE);
                                relativeReports.setVisibility(View.VISIBLE);
                            }else {
                                reletiveNoReports.setVisibility(View.VISIBLE);
                                relativeReports.setVisibility(View.GONE);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }

    private void setStaticText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject("english");
                JSONObject object_ = object.getJSONObject("My_Reports_Screen");
                //title.setText((String) object_.get("reports_title"));

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


    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
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


}

class Book_report_Adapter extends RecyclerView.Adapter<Book_report_Adapter.holder> {

    static Context context;
    static ArrayList<String> datelist=new ArrayList<>();
    DataSnapshot dataSnapshot;
    static holder holder;
    int rowindex=0;

    public Book_report_Adapter(Context context, ArrayList<String> datelist, DataSnapshot dataSnapshot) {
        Book_report_Adapter.context = context;
        Book_report_Adapter.datelist = datelist;
        this.dataSnapshot = dataSnapshot;
    }

    public Book_report_Adapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_report_dateview, viewGroup, false);
        return new holder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Book_report_Adapter.holder =holder;
        holder.text.setText(datelist.get(position).replace("_"," "));


        ArrayList<String> bookname_list=new ArrayList<>();

        for(DataSnapshot dd:dataSnapshot.child(datelist.get(position)).getChildren())
        {
            bookname_list.add(dd.getKey());

            dataSnapshot.child(datelist.get(position)).child(dd.getKey()).child("detail").getChildren();
            set_data(dataSnapshot,position,0,dd.getKey());

//                    for(DataSnapshot aa:snapshot.child(ss.getKey()).child(dd.getKey()).child("detail").getChildren())
//                    {
//
//                        System.out.println("---- aa "+aa.getKey());
//
//                        for(DataSnapshot bb:snapshot.child(ss.getKey()).child(dd.getKey()).child("detail").child(aa.getKey()).getChildren())
//                        {
//                            System.out.println("------- bb "+bb.getKey());
//
//                            list.add(bb.getValue(BookRep_Model.class));
//
//
//                        }
//
//                    }
        }

        holder.book_name_recyclerview.setAdapter(new Book_topic_Adapter(context,dataSnapshot,bookname_list,position));


    }

    public static void set_data(DataSnapshot snapshot,int card_position,int selected_position,String topic_name) {
        ArrayList<String> list=new ArrayList<>();

        DataSnapshot f;

        for(DataSnapshot bb:snapshot.child(datelist.get(card_position)).child(topic_name).child("detail").getChildren())
        {
            list.add(bb.getKey());
            f=bb;

            holder.book_details_recyclerview.setAdapter(new Book_datails_Adapter(context,list,snapshot,datelist.get(card_position),topic_name));

        }
    }

    @Override
    public int getItemCount() {
        return datelist.size();
    }


    public class holder extends RecyclerView.ViewHolder{

        RecyclerView book_name_recyclerview,book_details_recyclerview;
        TextView text;

        public holder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
            book_details_recyclerview=itemView.findViewById(R.id.book_details_recyclerview);
            book_name_recyclerview=itemView.findViewById(R.id.book_name_recyclerview);

            book_name_recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            if(Util.isPortraitMode(context)) book_details_recyclerview.setLayoutManager(new LinearLayoutManager(context));
            else book_details_recyclerview.setLayoutManager(new GridLayoutManager(context,2));


        }
    }
}

class Book_topic_Adapter extends RecyclerView.Adapter<Book_topic_Adapter.holder> {

    Context context;
    DataSnapshot dataSnapshot;
    ArrayList<String> topic_name;
    int rowindex=0;
    int cardpos=0;

    boolean firstt=true;

    public Book_topic_Adapter(Context context, DataSnapshot dataSnapshot, ArrayList<String> topic_name, int cardpos) {
        this.context = context;
        this.dataSnapshot = dataSnapshot;
        this.topic_name = topic_name;
        this.cardpos = cardpos;
    }

    public Book_topic_Adapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_topic_view, viewGroup, false);
        return new holder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.text.setText(topic_name.get(position).replace("_", " "));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book_report_Adapter.set_data(dataSnapshot, cardpos, position, topic_name.get(position));

                rowindex = position;
                changeTextColour(holder, position);
                notifyDataSetChanged();

            }
        });

        if (firstt)
        {
            rowindex=topic_name.size()-1;
            firstt=false;
        }

        changeTextColour(holder,position);

    }

    private void changeTextColour(holder holder, int position) {

        if (rowindex == position) {
            holder.text.setTextColor(context.getResources().getColor(R.color.blue));}
        else {
            holder.text.setTextColor(context.getResources().getColor(R.color.black_overlay));
        }

    }


    @Override
    public int getItemCount() {
        return topic_name.size();
    }

    class holder extends RecyclerView.ViewHolder {
        TextView text;
        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);

            text=itemView.findViewById(R.id.text);
            card=itemView.findViewById(R.id.card);

        }
    }
}

class Book_datails_Adapter extends RecyclerView.Adapter<Book_datails_Adapter.holder> {

    Context context;
    ArrayList<String> details_list;
    DataSnapshot dataSnapshot;

    String ch1,topic_name;
    int pos=0;
    public Book_datails_Adapter(Context context, ArrayList<String> details_list,DataSnapshot dataSnapshot,String ch1,String topic_name) {
        this.context = context;
        this.details_list = details_list;
        this.dataSnapshot = dataSnapshot;
        this.ch1 = ch1;
        this.topic_name = topic_name;
    }

    public Book_datails_Adapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_inner_layout, viewGroup, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        ArrayList<BookRep_Model> list=new ArrayList<>();

        int attempts=0;
        int totatime=0;


        for(DataSnapshot bb:dataSnapshot.child(ch1).child(topic_name).child("detail").child(details_list.get(position)).getChildren())
        {

            list.add(bb.getValue(BookRep_Model.class));

            holder.textViewTopicName.setText(bb.child("name").getValue().toString());
            totatime= totatime+Integer.valueOf(bb.child("time").getValue().toString());
            holder.textViewDetail.setText(" Time : " + millisecondsToTime(Long.parseLong(String.valueOf(totatime))) + " Sec");
            attempts++;
            holder.textViewAttempts.setText(attempts+" Read");



        }

        holder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 0) {
                    pos = 1;
                    holder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                    holder.linearAttempts1.setVisibility(View.VISIBLE);
                    holder.linearAttempts.removeAllViews();

                    View child = null;
                    for (int i = 0; i < list.size(); i++) {
                        child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                        TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                        TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);

                        String videoName = list.get(i).getName();
                        String time = list.get(i).getTime();
                        String date = list.get(i).getDate();
                        String[] splitStr = date.split("\\s+");
                        String firstFourChars = "";     //substring containing first 4 characters

                        try {
                            textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                        }
                        catch (Exception w)
                        {
                            w.printStackTrace();
                            textViewAttempt.setText(date.substring(0,6).replace("-"," "));
                        }

                        String timeforSave= millisecondsToTime(Long.parseLong(time));
//                        textViewAttemptCount.setText(timeforSave +" Sec");
                        textViewAttemptCount.setText(list.get(i).getPageRead());

                        holder.linearAttempts.addView(child);

                    }


                } else {
                    pos = 0;
                    notifyDataSetChanged();
                    holder.linearAttempts1.setVisibility(View.GONE);

                    holder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                }

            }
        });

    }

    private String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return minutes + ":" + secs;
    }


    @Override
    public int getItemCount() {
        return details_list.size();
    }

    class holder extends RecyclerView.ViewHolder {

        protected TextView textViewDate;
        protected TextView textViewTopicName;
        protected TextView textViewDetail;
        protected TextView textViewAttempts;
        protected ImageView image;
        protected LinearLayout linearAttempts;
        protected LinearLayout linearAttempts1;
        protected RelativeLayout RelativeClickListener;

        public holder(View holderView) {
            super(holderView);
            textViewAttempts = holderView.findViewById(R.id.textViewAttempts);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewDetail = holderView.findViewById(R.id.textViewDetail);
            textViewDate = holderView.findViewById(R.id.textViewDate);
            image = holderView.findViewById(R.id.image);
            linearAttempts = holderView.findViewById(R.id.linearAttempts);
            linearAttempts1 = holderView.findViewById(R.id.linearAttempts1);
            RelativeClickListener = holderView.findViewById(R.id.RelativeClickListener);
        }

    }
}


