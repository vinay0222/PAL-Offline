package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button1Text;
import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.button3Text;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idreameducation.ipreppal.PalMobile.adapter.TestReview_Adapter_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.TestReview_Adapter;
import com.idreameducation.ipreppal.util.NetworkStateReceiver;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestReviewActivity extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    public static ArrayList<HashMap<String, String>> questionsArrayList;
    TextView textViewTopicName;

    TextView textViewSubmit,textViewPrevious;

    ImageView imageViewBackCross,imageViewBack;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView screenshotText, callText;

    private Uri filePath2;
    private String linkFromFirebaseStorage,textSendWithImageTextString;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private NetworkStateReceiver networkStateReceiver;

    TextView textViewTitleName;

    public TestReviewActivity(ArrayList<HashMap<String, String>> questionsArrayList) {
        this.questionsArrayList = questionsArrayList;
    }

    public TestReviewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.setWindowSettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_review);
        Util.handleNotch(this);

        if(Util.isPortraitMode(this)) initMobile();
        else init();

        PalHomeActivity.hideNavigationBar(getWindow());
    }

    private void init() {
        context=this;

        recyclerView=findViewById(R.id.recyclerView);

        textViewSubmit=findViewById(R.id.textViewSubmit);
        textViewPrevious=findViewById(R.id.textViewPrevious);
        imageViewBackCross=findViewById(R.id.imageViewBackCross);
        textViewTopicName=findViewById(R.id.textViewTopicName);

        textViewTitleName=findViewById(R.id.textViewTitleName);

        textViewTopicName.setText(Util.getTopicNameAlt(context));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));


        if(Util.isPortraitMode(context)) {
            TestReview_Adapter_Mobile testReview_adapter=new TestReview_Adapter_Mobile(questionsArrayList);
            recyclerView.setAdapter(testReview_adapter);
        }
        else
        {
            TestReview_Adapter testReview_adapter=new TestReview_Adapter(questionsArrayList);
            recyclerView.setAdapter(testReview_adapter);
        }



        recyclerView.setEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                questionPosition=targetPosition;
//                questionPosition++;
                System.out.println("======== questionPosition 1 "+questionPosition);
                updateBtn();
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(recyclerView);

        if(Util.getSelectedLanguage(context).equals("hindi"))
        {
            textViewTitleName.setText("रिव्यु");
            textViewSubmit.setText("अगला सवाल");
            textViewPrevious.setText("पिछला प्रश्न");
        }
        else
        {
            textViewTitleName.setText("Review Test");
            textViewSubmit.setText("Next Question");
            textViewPrevious.setText("Previous Question");
        }

        textViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                previousQuestion();
            }
        });

        clicklisteners();
    }

    private void initMobile() {
        context=this;
        recyclerView=findViewById(R.id.recyclerView);

        textViewSubmit=findViewById(R.id.textViewSubmit);

        imageViewBack=findViewById(R.id.imageViewBack);
        imageViewBackCross=findViewById(R.id.imageViewBackCross);
        textViewTopicName=findViewById(R.id.textViewTopicName);

        textViewTopicName.setText(Util.getTopicNameAlt(context));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));


        if(Util.isPortraitMode(context)) {
            TestReview_Adapter_Mobile testReview_adapterMobile =new TestReview_Adapter_Mobile(questionsArrayList);
            recyclerView.setAdapter(testReview_adapterMobile);
        }
        else
        {
            TestReview_Adapter testReview_adapter=new TestReview_Adapter(questionsArrayList);
            recyclerView.setAdapter(testReview_adapter);
        }

        recyclerView.setEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                questionPosition=targetPosition;
//                questionPosition++;
                System.out.println("======== questionPosition 1 "+questionPosition);
                updateBtn();
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(recyclerView);

        if(Util.getSelectedLanguage(context).equals("hindi"))
        {
//            textViewTitleName.setText("रिव्यु");
            textViewSubmit.setText("अगला सवाल");
//            textViewPrevious.setText("पिछला प्रश्न");
        }
        else
        {
//            textViewTitleName.setText("Review Test");
            textViewSubmit.setText("Next Question");
//            textViewPrevious.setText("Previous Question");
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                previousQuestion();
            }
        });


        clicklisteners();
    }


    int questionPosition=0;

    private void clicklisteners() {
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                nextQuestion();
            }
        });

        imageViewBackCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                onBackPressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });
    }

    public void nextQuestion() {
        if(questionPosition<questionsArrayList.size())
        {
            questionPosition++;
            recyclerView.smoothScrollToPosition(questionPosition);
        }
        updateBtn();
    }

    public void previousQuestion() {
        if(questionPosition!=0)
        {
            questionPosition--;
            recyclerView.smoothScrollToPosition(questionPosition);
        }
        updateBtn();
    }

    private void updateBtn() {
        if(questionPosition==questionsArrayList.size()-1)
        {
            textViewSubmit.setVisibility(View.GONE);
            if(Util.isPortraitMode(context)) imageViewBack.setVisibility(View.VISIBLE);
            else textViewPrevious.setVisibility(View.VISIBLE);
        }
        else if(questionPosition==0)
        {
            textViewSubmit.setVisibility(View.VISIBLE);
            if(Util.isPortraitMode(context)) imageViewBack.setVisibility(View.GONE);
            else textViewPrevious.setVisibility(View.GONE);
        }
        else
        {
            textViewSubmit.setVisibility(View.VISIBLE);
            if(Util.isPortraitMode(context)) imageViewBack.setVisibility(View.VISIBLE);
                else textViewPrevious.setVisibility(View.VISIBLE);

        }
    }

    /** Support Btn Functions */
    @SuppressLint("RestrictedApi")
    public void animateFAB() {

        fab.setImageResource(R.drawable.ic_inactive);
        isFabOpen = true;

        buttonScreenshot(null);

    }

    private void openCapturedScreenshot(File fileScreenshot) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogue_send_screenshot);

        Button textViewCancel = dialog.findViewById(R.id.textViewCancel);
        Button textViewSend = dialog.findViewById(R.id.textViewSend);
        ImageView imageViewScreenshot = dialog.findViewById(R.id.imageViewScreenshot);
        ImageView close_pop = dialog.findViewById(R.id.close_pop);
//        ImageView attachScreenshot = dialog.findViewById(R.id.attachScreenshot);
        EditText textSendWithImage__ = dialog.findViewById(R.id.textSendWithImage_);
//        attachScreenshot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SelectImage();
//                Util.preventTwoClick(view);
//            }
//        });


        TextView number_text=dialog.findViewById(R.id.number_text);
        TextView email_text=dialog.findViewById(R.id.email_text);

        number_text.setText(button1Text);
        email_text.setText(button3Text);

        Uri uri = Uri.fromFile(fileScreenshot);
        filePath2 = uri;
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

    // UploadImage method
    private void uploadImage(Dialog dialog) {
        if (filePath2 != null) {

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
            ref.putFile(filePath2)
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

    private void setlayout_forlandscape() {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Util.setIsFullScreen(context,true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void setlayout_forportrait() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        Util.setIsFullScreen(context,false);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}