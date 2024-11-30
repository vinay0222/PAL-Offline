package com.idreameducation.ipreppal.pal.activity;


import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.pal.adapter.BiMonthlyTestAdapter;
import com.idreameducation.ipreppal.pal.adapter.TrackTestAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestScoreModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDateWiseTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseTestRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.ArcProgress;
import com.idreameducation.ipreppal.util.CountDownTimerWithPause;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by anurag on 26-Oct-17.
 */

public class BiMonthlyTestActivity extends AppCompatActivity {
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private final long timeInMilliseconds = 0L;
    private final long timeSwapBuff = 0L;
    private final long updatedTime = 0L;
    /*Dec Widgets*/
    private RecyclerView mRecyclerView;
    private RecyclerView recyclerViewTrack;
    private Context context;
    private TextView textViewTime;
    private TextView textViewToatal;
    private ImageView downBtn;
    private Global global;
    private TextView textViewSubmit;
    private TextView btnChangeLanguageEnglish;
    private TextView btnChangeLanguage;
    private String selectedLanguge;
    private BiMonthlyTestAdapter biMonthlyTestAdapter;
    private TrackTestAdapter trackTestAdapter;
    private ArrayList<HashMap<String, String>> questionsArrayList;
    private ArrayList<HashMap<Integer, String[]>> arrayListForUser;
    private ArrayList<HashMap<String, HashMap<String, String>>> questionArrayList;
    private long duration = 0;
    private CountDownTimerWithPause countDownTimerWithPause;
    private ArcProgress arcProgress;
    private int progress = 0;
    private String date;
    private String attemptedQuestions = "0";
    private boolean isNeededToOpenBackDialog = true;
    private String questionAttempted;
    private String totalTime;
    private String welcomeBack;
    private String start;
    private String attempted;
    private String attempt;
    private String resume;
    private String warning;
    private String exitMessage;
    private String continueTest;
    private String exitTest;
    private String unattemptedQLeft;
    private String sureSubmit;
    private String submitTest;
    private String lastMessage;
    private String imageInternet;
    private String submit;
    private String completeTest;
    private String calculateScore;
    private String greeting;
    private String yourScore;
    private String review;
    private String minutes;
    private String goBack;
    private String startMessage;
    private LinearLayoutManager manager;
    private boolean isDialogOpend = false;
    private long startTime = 0L;
    private long endTime = 0L;
    private ImageView imageViewBack;
    private long totalTimee;
    private long classTime;
    private int Seconds, Minutes, MilliSeconds;
    private Handler handler;
    //*****                   END                    *****//
    private final String time = "0";
    private String subject;
    private ArrayList<String> topicArrayList;

    private LinearLayout linearLayoutTrack;
    private RelativeLayout linearActionLayout;
    private TextView textViewTopicName;
//    private TextView textViewSkip;
    private boolean toggleQuestionsList = false;
    private TextView attemptedQ, skippedQ, yetToAttemptQ;
    public TreeSet<Integer> attemptedQMap = new TreeSet<>();
    public HashMap<Integer, Boolean> skippedQHashMap = new HashMap<>();
    private int skipCount = 0;
    private TextView textViewTitleName;

    private String isAssigned;
    private String batchId;
    private String keyTo;
    private String datetostore;
    private String teacherID;
    private TextView textViewDetail;
    private TextView textViewdUserName;
    private ImageView imageViewBackCross;

    private Disposable timeTaskDisposable;
    private Disposable countTaskDisposable;

    public HashMap<String, Boolean> restoreQuestionHashMap = new HashMap<>();
    private ReportsLatestDataTestRepository reportsLatestDataTestRepository;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private ReportsDateWiseTestRepository reportsDateWiseTestRepository;
    private ReportsTopicWiseTestRepository reportsTopicWiseTestRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_paper);
        /*Call hookUp function*/
        assignIds();
        listners();
    }

    private void listners() {
    }

    public int pos = 0;

    /*Init values , widgets , strings etc...*/
    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        global.setImageAdded(false);
        startTime = System.currentTimeMillis();
        handler = new Handler();
        global.sendData("TestPaper", this.getClass().getName());
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference();
        String time = null;
        try {
            time = getIntent().getStringExtra("Time");
            duration = Long.parseLong(time);
        } catch (Exception e) {
            e.printStackTrace();
            duration = 10;
        }
        try {
            sClass = getIntent().getStringExtra("sClass");
            if (sClass == null) {
                sClass = Util.getSelectedClassName(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sClass = Util.getSelectedClassName(context);
        }


        topicId = Util.getTopicID(context);
        current_topicId = Util.getTopicID(context);
        topicArrayList = global.getTopicIdArrayList();


        isAssigned = getIntent().getStringExtra("isAssigned");
        batchId = getIntent().getStringExtra("batchId");
        keyTo = getIntent().getStringExtra("key");
        datetostore = getIntent().getStringExtra("date");
        teacherID = getIntent().getStringExtra("teacherID");
        subject = getIntent().getStringExtra("subject");

        reportsLatestDataTestRepository = new ReportsLatestDataTestRepository(context);
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);
        reportsDateWiseTestRepository = new ReportsDateWiseTestRepository(context);
        reportsTopicWiseTestRepository = new ReportsTopicWiseTestRepository(context);

        textViewTitleName = findViewById(R.id.textViewTitleName);
        textViewTitleName.setVisibility(View.GONE);
        textViewTopicName = findViewById(R.id.textViewTopicName);
        textViewTopicName.setText(getIntent().getStringExtra("topicName"));
        linearLayoutTrack = findViewById(R.id.linearLayoutTrack);
        linearLayoutTrack.setVisibility(View.GONE);
        linearActionLayout = findViewById(R.id.linear);
//        textViewSkip = findViewById(R.id.textViewSkip);
        attemptedQ = findViewById(R.id.attemptedQ);
        skippedQ = findViewById(R.id.skippedQ);
        yetToAttemptQ = findViewById(R.id.yetToAttemptQ);

        textViewDetail = findViewById(R.id.textViewDetail);
        textViewDetail.setText(Util.getUsername(context) + " Based on our analysis, to move forward,\nyou will have to ");

        mRecyclerView = findViewById(R.id.recyclerView);
        recyclerViewTrack = findViewById(R.id.recyclerViewTrack);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerViewTrack.hasFixedSize();
        recyclerViewTrack.setLayoutManager(linearLayoutManager);
        imageViewBackCross = findViewById(R.id.imageViewBackCross);
        downBtn = findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toggleQuestionsList){
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 400;
                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;
                }else{
                    linearLayoutTrack.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });
        textViewToatal = findViewById(R.id.textViewToatal);
        textViewToatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toggleQuestionsList){
                    linearLayoutTrack.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 400;
                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = true;
                }else{
                    linearLayoutTrack.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearActionLayout.getLayoutParams();
                    layoutParams.leftMargin = 0;
                    linearActionLayout.setLayoutParams(layoutParams);
                    toggleQuestionsList = false;
                }
            }
        });
        textViewTime = findViewById(R.id.textViewTime);
        arcProgress = findViewById(R.id.arcProgress);
        arcProgress.setVisibility(View.GONE);
        btnChangeLanguageEnglish = findViewById(R.id.btnChangeLanguageEnglish);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        if (Util.getSelectedLanguage(context).equals("english"))
        {
            btnChangeLanguage.setText("English");
        }else {
            btnChangeLanguage.setText("Hindi");
        }
        textViewSubmit = findViewById(R.id.textViewSubmit);
        textViewdUserName = findViewById(R.id.textViewdUserName);
        textViewdUserName.setText(Util.getUsername(context));
        imageViewBack = findViewById(R.id.imageViewBack);
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (pos == questionsArrayList.size()) {
                        submitTestPaper();
                        linearLayoutTrack.setVisibility(View.GONE);
                        clearSkipData();
                    } else if (pos == questionsArrayList.size() - 1) {
                        if(textViewSubmit.getText().equals("Skip")){
                            showSkipOption();
                        }else{
                            imageViewBack.setVisibility(View.VISIBLE);
                            textViewSubmit.setText("Submit Answer");
                            textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                            textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                            checkSkippedQ(pos, 0);
                            pos++;
                            refreshQuestionsListingAdapter();
                            textViewToatal.setText("Question " + pos + " / " + questionsArrayList.size());
                            if(pos == questionsArrayList.size()){
//                                textViewSkip.setVisibility(View.GONE);
                                textViewSubmit.setText("Submit Test");
                                textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                            }
                        }
                    } else {
                        imageViewBack.setVisibility(View.VISIBLE);
                        checkSkippedQ(pos, 0);
                        pos++;
                        mRecyclerView.smoothScrollToPosition(pos);
                        refreshQuestionsListingAdapter();
                        textViewSubmit.setText("Next Question");
                        textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                        textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                        textViewToatal.setText("Question " + pos + " / " + questionsArrayList.size());
                        if(pos == questionsArrayList.size()){
//                            textViewSkip.setVisibility(View.VISIBLE);
                            textViewSubmit.setText("Skip");
                            textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                        }else{
                            textViewSubmit.setText("Next Question");
                            textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
//                            textViewSkip.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
//        textViewSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSkipOption();
//            }
//        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (pos == 0) {
                        imageViewBack.setVisibility(View.GONE);
                    } else {
                        if(pos == questionsArrayList.size()){
                            for (int i = 0; i < questionsArrayList.size(); i++) {
                                if (questionsArrayList.get(pos - 1).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                                    textViewSubmit.setText("Submit Answer");
                                    textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                                    textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                                    break;
                                }
                            }
                            pos--;
                        }else{
                            pos--;
                            mRecyclerView.smoothScrollToPosition(pos);
                            textViewSubmit.setText("Next Question");
                            textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                            textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
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
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(mRecyclerView);

        selectedLanguge = Util.getSelectedLanguage(context);

        if (global.isFlagEnabled()) {
            btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguage.setBackgroundResource(R.drawable.signup);
            btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
        }
        try {
            getBiMonthyTestCount();
            getTime();
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }


        findViewById(R.id.btnChangeLanguageEnglish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
                    btnChangeLanguage.setBackgroundResource(R.drawable.signup);
                    btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
                    btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
                    BiMonthlyTestAdapter.selectedLanguge = "English";
                    biMonthlyTestAdapter.updateAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnChangeLanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (global.isFlagEnabled()) {
                        btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
                        btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
                        btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
                        btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));
                        if (Util.getSelectedLanguage(context).equals("Hindi"))
                        {
                            BiMonthlyTestAdapter.selectedLanguge = "Hindi";
                        }else {
                            BiMonthlyTestAdapter.selectedLanguge = "English";
                        }
//                        biMonthlyTestAdapter.selectedLanguge = Util.getSelectedLanguage(context);
                        biMonthlyTestAdapter.updateAdapter();
                    } else {
                        Util.showToast(context, "Coming soon");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.imageViewBackCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void updateNextQuestionButtonText(int type, int position){
        if(type == 0){
            if(textViewSubmit != null){
                if(textViewSubmit.getText().equals("Submit Test")){
                    pos = pos - 1;
                }
                textViewSubmit.setText("Submit Answer");
                textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
            }
        }else if(type == 1){
            if(textViewSubmit != null){
                if(position == questionsArrayList.size() - 1){
                    textViewSubmit.setText("Skip");
                    textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                    textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                }else{
                    textViewSubmit.setText("Next Question");
                    textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                    textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                }
            }
        }
    }

    private void showSkipOption(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.skip_question_dialog_layout);
        dialog.setCancelable(false);
        ImageView imageViewCross = dialog.findViewById(R.id.imageViewCross);
        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView textView = dialog.findViewById(R.id.textView);
        TextView submit_bt = dialog.findViewById(R.id.submit_bt);
        submit_bt.setText("परीक्षण जमा करें");
        textView.setText(Util.getUsername(context) + ", यह आखिरी सवाल है। क्या आप वाकई इसे छोड़ना चाहते हैं? इस प्रश्न को हल करने के लिए \"x\" पर क्लिक करें या आगे बढ़ने के लिए नीचे दिए बटन पर क्लिक करें (परीक्षण दर्ज करें)");
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                submitTestPaper();
                linearLayoutTrack.setVisibility(View.GONE);
                clearSkipData();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
        try {
            if (!global.isImageAdded()) {
                if (!isDialogOpend) {
                    if (countDownTimerWithPause != null) {
                        isDialogOpend = true;

                        countDownTimerWithPause.pause();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearLayoutTrack.setVisibility(View.GONE);
        clearSkipData();
        restoreQuestionHashMap.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!global.isImageAdded()) {
                if (isNeededToOpenBackDialog) {
                    if (countDownTimerWithPause != null) {
                        if (countDownTimerWithPause.isPaused()) {
                            if (isDialogOpend) {
                                isNeededToOpenBackDialog = false;
                                resumeModelTestpaper();
                            }
                        }
                    }
                }
            } else {
                global.setImageAdded(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finalSubmition() {
        if (Integer.parseInt(this.attemptedQuestions) == questionsArrayList.size()) {
            submitTestPaper();

            // openDialog();
        } else {
            submitConfirmationCheck();
        }
    }


    @Override
    public void onBackPressed() {
//        showExitDialog();
        super.onBackPressed();
        
    }

    private String sClass;
    private String topicId;
    private String current_topicId;

    int count = 0;
    final ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList = new ArrayList<>();

    /* get questions from firebase */
    private void getQuestions() throws Exception {
        if (count == topicArrayList.size()) {
            Util.dismissDialog();
            dataManipulation(contentArrayList);
        } else {
            String filePath;
            topicId = topicArrayList.get(count);
            if (Util.getSelectedLanguage(context).equals("english"))
            {
                 filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "questions.txt";
            }else {
                 filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "Hindi_questions.txt";
            }
//            String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "Hindi_questions.txt";
            File file = new File(Util.getSDCardPath(context) + "/" + filePath);
            if (file.exists()) {
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                try {
                    JSONArray array = jsonObject.getJSONArray(topicId);
                    HashMap<String, HashMap<String, String>> innerHashMap = null;
                    HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                    HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap_ = null;

                    HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;

                    HashMap<String, String> finalMap = new HashMap<>();
                    int length_ = array.length();
                    int k = 0;
                    for (int i = 0; i < array.length(); i++) {
                        k++;
                        coreOuterHashMap = new HashMap<>();
                        outerHashMap = new HashMap<>();
                        outerHashMap_ = new HashMap<>();
                        JSONArray jsonArray = array.getJSONArray(i);
                        int length = jsonArray.length();
                        ArrayList<Integer> trackArrayList = new ArrayList<>();
                        int l = 3;
                        if (k == 3) {
                            l = 1;
                        }
                        for (int j = 0; j < l; j++) {
                            int max = length;
                            int number;//= generateRandom(trackArrayList, max);
                            switch (j) {
                                case 0:
                                    number = 2;
                                    break;
                                case 1:
                                    number = 4;
                                    break;
                                case 2:
                                    number = 6;
                                    break;
                                default:
                                    number = 3;
                                    break;
                            }

                            trackArrayList.add(number);
                            // HashMap<String, HashMap<String, String>> aa = outerHashMap.get("" + number);

                            innerHashMap = new HashMap<>();
                            finalMap = new HashMap<>();
                            JSONObject aa = jsonArray.getJSONObject(number);
                            Iterator<String> iterator = aa.keys();
                            String key = "";
                            while (iterator.hasNext()) {
                                key = iterator.next();
                            }
                            JSONObject jsonObject1 = aa.getJSONObject(key);
                            String q = jsonObject1.getString("q");

                            String questionImage = "";
                            if (jsonObject1.has("questionImage")) {
                                questionImage = jsonObject1.getString("questionImage");
                            }

                            String feedbackImage = null;
                            String correct_feedback = null;
                            String incorrect_feedback = null;
                            if (jsonObject1.has("feedbackImage")) {
                                feedbackImage = jsonObject1.getString("feedbackImage");
                                finalMap.put("feedbackImage", feedbackImage);
                            }
                            if (jsonObject1.has("correct_feedback")) {
                                correct_feedback = jsonObject1.getString("correct_feedback");
                                finalMap.put("correct_feedback", correct_feedback);
                            }
                            if (jsonObject1.has("incorrect_feedback")) {
                                incorrect_feedback = jsonObject1.getString("incorrect_feedback");

                                finalMap.put("incorrect_feedback", incorrect_feedback);
                            }
                            JSONObject aObject = jsonObject1.getJSONObject("A");
                            JSONObject bObject = jsonObject1.getJSONObject("B");
                            JSONObject cObject = jsonObject1.getJSONObject("C");
                            JSONObject dObject = jsonObject1.getJSONObject("D");
                            String aValue = aObject.getString("value");
                            String aType = aObject.getString("type");
                            String bValue = null;
                            try {
                                bValue = bObject.getString("value");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String bType = bObject.getString("type");
                            String cValue = cObject.getString("value");
                            String cType = cObject.getString("type");
                            String dValue = dObject.getString("value");
                            String dType = dObject.getString("type");

                            finalMap.put("q", q);

                            finalMap.put("questionImage", questionImage);
                            HashMap<String, String> aHAshMap = new HashMap<>();
                            HashMap<String, String> bHAshMap = new HashMap<>();
                            HashMap<String, String> cHAshMap = new HashMap<>();
                            HashMap<String, String> dHAshMap = new HashMap<>();

                            aHAshMap.put("value", aValue);
                            aHAshMap.put("type", aType);

                            bHAshMap.put("value", bValue);
                            bHAshMap.put("type", bType);

                            cHAshMap.put("value", cValue);
                            cHAshMap.put("type", cType);

                            dHAshMap.put("value", dValue);
                            dHAshMap.put("type", dType);

                            finalMap.put("A", aHAshMap.toString());
                            finalMap.put("B", bHAshMap.toString());
                            finalMap.put("C", cHAshMap.toString());
                            finalMap.put("D", dHAshMap.toString());

                            innerHashMap.put(key, finalMap);
                            outerHashMap.put(j + "", innerHashMap);

                        }
                        coreOuterHashMap.put(i + "", outerHashMap);
                        contentArrayList.add(coreOuterHashMap);


                    }
                    Log.i("data", contentArrayList.toString());

                    count++;
                    getQuestions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {


                try {
                    String board = null;

                    board = ApplicationConstants.QUESTION_DB_HINDI;
                    databaseReference.child(board).child(sClass).child(topicId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {


                                HashMap<String, HashMap<String, String>> innerHashMap = null;
                                HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap = null;
                                HashMap<String, HashMap<String, HashMap<String, String>>> outerHashMap_ = null;
                                HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> coreOuterHashMap = null;
                                int k = 0;
                                for (DataSnapshot single : dataSnapshot.getChildren()) {
                                    k++;
                                    coreOuterHashMap = new HashMap<>();
                                    outerHashMap_ = new HashMap<>();
                                    outerHashMap = new HashMap<>();
                                    for (DataSnapshot innerData : single.getChildren()) {
                                        for (DataSnapshot innerCoreData : innerData.getChildren()) {
                                            innerHashMap = new HashMap<>();
                                            innerHashMap.put(innerCoreData.getKey(), (HashMap<String, String>) innerCoreData.getValue());
                                            outerHashMap.put(innerData.getKey(), innerHashMap);
                                        }
                                    }
                                    ArrayList<Integer> trackArrayList = new ArrayList<>();
                                    // getting three random questions from each level

                                    int j = 3;
                                    if (k == 3) {
                                        j = 1;
                                    }
                                    for (int i = 0; i < j; i++) {
                                        int max = outerHashMap.size();

                                        int number = generateRandom(trackArrayList, max);
                                        trackArrayList.add(number);
                                        HashMap<String, HashMap<String, String>> aa = outerHashMap.get("" + number);
                                        outerHashMap_.put(i + "", aa);
                                        coreOuterHashMap.put(single.getKey(), outerHashMap_);
                                    }
                                    contentArrayList.add(coreOuterHashMap);

                                }
                                Util.dismissDialog();
                                dataManipulation(contentArrayList);

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


    private int generateRandom(ArrayList<Integer> trackArrayList, int max) {
        Random r = new Random();
        int number = r.nextInt(max - 1) + 1;
        if (trackArrayList.contains(number)) {
            number = generateRandom(trackArrayList, max);
        }
        return number;
    }


    private String questionID;

    /* Data manipulation of questions and answer */
    private void dataManipulation(ArrayList<HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> contentArrayList) throws Exception {
        try {
            Util.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Save time for next question */
        Util.saveTime();
        /* Variables which we will be used to manipluate the data */
        String keyToGet = null;
        String keyToGetQuestion = null;
        /*Total number of level*/
        questionsArrayList = new ArrayList<>();
        for (int p = 0; p < contentArrayList.size(); p++) {
            HashMap<String, String> finalHashmap = new HashMap<>();

            HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> stringHashMapHashMap = contentArrayList.get(p);
            /*getting the key of the Hashmap*/
            for (String key : stringHashMapHashMap.keySet()) {
                System.out.println(key);
                keyToGet = key;
            }
            HashMap<String, HashMap<String, HashMap<String, String>>> stringHashMapHashMap1 = stringHashMapHashMap.get(keyToGet);
            /* Add data in the array list only once in a single level to avoid the repeatition */

            questionArrayList = new ArrayList<>();
            for (int i = 0; i < stringHashMapHashMap1.size(); i++) {
                HashMap<String, HashMap<String, String>> hashMapHashMap = stringHashMapHashMap1.get("" + i);
                questionArrayList.add(hashMapHashMap);
            }
            try {
                /*Question that will be displyed to the user*/
                HashMap<String, HashMap<String, String>> map = null;
                for (int k = 0; k < questionArrayList.size(); k++) {
                    map = questionArrayList.get(k);
                    finalHashmap = new HashMap<>();
                    /*Getting the key of the innerHashmap*/
                    for (String key : map.keySet()) {
                        System.out.println(key);
                        keyToGetQuestion = key;
                        questionID = key;
                    }
                    /*Final question Hashmap*/
                    HashMap<String, String> mapToQuestion = map.get(keyToGetQuestion);

                    /* Collecting answers from the map  */
                    Object optionA = mapToQuestion.get("A");
                    Object optionB = mapToQuestion.get("B");
                    Object optionC = mapToQuestion.get("C");
                    Object optionD = mapToQuestion.get("D");
                    /* Adding the options objects in to Arraylist for further processing */
                    ArrayList<Object> optionsArrayList = new ArrayList<>();
                    optionsArrayList.add(optionA);
                    optionsArrayList.add(optionB);
                    optionsArrayList.add(optionC);
                    optionsArrayList.add(optionD);

                    /* Getting questioins in english language and in alternate language */
                    String questionQ = mapToQuestion.get("q");
                    String questionQAlt = mapToQuestion.get("q_alt");
                    /* Getting Correct Feedbck in english and alternate language */
                    String correct_feedback_alt = mapToQuestion.get("correct_feedback_alt");
                    String correct_feedback = mapToQuestion.get("correct_feedback");
                    /* Getting Incorrect Feedbck in english and alternate language */
                    String incorrect_feedback = mapToQuestion.get("incorrect_feedback");
                    String incorrect_feedback_alt = mapToQuestion.get("incorrect_feedback_alt");
                    String feedbackImage = null;
                    try {
                        feedbackImage = mapToQuestion.get("feedback_image");
                    } catch (Exception e) {
                        e.printStackTrace();
                        feedbackImage = null;
                    }

                    /* Set question image if exist */
                    String questionIcon = null;
                    try {
                        questionIcon = mapToQuestion.get("questionImage");
                    } catch (Exception e) {
                        e.printStackTrace();
                        questionIcon = "Not";
                    }
                    finalHashmap.put("feedback_image", feedbackImage);
                    finalHashmap.put("questionID", questionID);
                    finalHashmap.put("correct_feedback_alt", correct_feedback_alt);
                    finalHashmap.put("image", questionIcon);
                    finalHashmap.put("correct_feedback", correct_feedback);
                    finalHashmap.put("incorrect_feedback", incorrect_feedback);
                    finalHashmap.put("incorrect_feedback_alt", incorrect_feedback_alt);
                    finalHashmap.put("questionQAlt", questionQAlt);
                    finalHashmap.put("questionQ", questionQ);
                    finalHashmap.put("OptionSelected", "NOT");
                    finalHashmap.put("Status", "PENDING");
                    finalHashmap.put("IsAttampted", "FALSE");
                    /* Getting answers hashmaps from the arraylist */

                    HashMap<String, String> mapAnswer1 = new HashMap<>();
                    HashMap<String, String> mapAnswer2 = new HashMap<>();
                    HashMap<String, String> mapAnswer3 = new HashMap<>();
                    HashMap<String, String> mapAnswer4 = new HashMap<>();

                    int l = 1;
                    if (l == 1) {
                        String a = (String) optionsArrayList.get(0);
                        String b = (String) optionsArrayList.get(1);
                        String c = (String) optionsArrayList.get(2);
                        String d = (String) optionsArrayList.get(3);

                        a = a.substring(1, a.length() - 1);
                        String[] keyValuePairs = new String[2];
                        String[] keyValuePairs1 = a.split(", ");
                        if (keyValuePairs1.length > 2) {
                            keyValuePairs[0] = keyValuePairs1[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs1.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs1[i];
                                } else {
                                    keyy = keyValuePairs1[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs[1] = keyValuePairs1[1] + ", " + keyy;
                        } else {
                            keyValuePairs[0] = keyValuePairs1[0];
                            keyValuePairs[1] = keyValuePairs1[1];
                        }
                        Log.i("keyValuePairs", keyValuePairs.toString());
                        for (String pair : keyValuePairs) {
                            String[] entry = pair.split("=");

                            mapAnswer1.put(entry[0].trim(), entry[1].trim());
                        }


                        b = b.substring(1, b.length() - 1);
                        String[] keyValuePairs_ = new String[2];
                        String[] keyValuePairs2 = b.split(", ");
                        if (keyValuePairs2.length > 2) {
                            keyValuePairs_[0] = keyValuePairs2[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs2.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs2[i];
                                } else {
                                    keyy = keyValuePairs2[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs_[1] = keyValuePairs2[1] + ", " + keyy;
                        } else {
                            keyValuePairs_[0] = keyValuePairs2[0];
                            keyValuePairs_[1] = keyValuePairs2[1];
                        }

                        for (String pair : keyValuePairs_) {
                            String[] entry = pair.split("=");
                            mapAnswer2.put(entry[0].trim(), entry[1].trim());
                        }

                        c = c.substring(1, c.length() - 1);

                        String[] keyValuePairs__ = new String[2];
                        String[] keyValuePairs3 = c.split(", ");
                        if (keyValuePairs3.length > 2) {
                            keyValuePairs__[0] = keyValuePairs3[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs3.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs3[i];
                                } else {
                                    keyy = keyValuePairs3[i] + ", " + keyy;
                                }
                            }

                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs__[1] = keyValuePairs3[1] + ", " + keyy;
                        } else {
                            keyValuePairs__[0] = keyValuePairs3[0];
                            keyValuePairs__[1] = keyValuePairs3[1];
                        }


                        for (String pair : keyValuePairs__) {
                            String[] entry = pair.split("=");
                            mapAnswer3.put(entry[0].trim(), entry[1].trim());
                        }


                        d = d.substring(1, d.length() - 1);
                        String[] keyValuePairs___ = new String[2];

                        String[] keyValuePairs4 = d.split(", ");
                        if (keyValuePairs4.length > 2) {
                            keyValuePairs___[0] = keyValuePairs4[0];
                            String keyy = "";
                            for (int i = 1; i < keyValuePairs4.length; i++) {
                                if (i == 1) {
                                    keyy = keyValuePairs4[i];
                                } else {
                                    keyy = keyValuePairs4[i] + ", " + keyy;
                                }
                            }
                            if (keyy.contains(", value")) {
                                keyy = keyy.replace(", value", "");
                            }
                            keyValuePairs___[1] = keyValuePairs4[1] + ", " + keyy;
                        } else {
                            keyValuePairs___[0] = keyValuePairs4[0];
                            keyValuePairs___[1] = keyValuePairs4[1];
                        }


                        for (String pair : keyValuePairs___) {
                            String[] entry = pair.split("=");
                            mapAnswer4.put(entry[0].trim(), entry[1].trim());
                        }


                    } else {
                        mapAnswer1 = (HashMap<String, String>) optionsArrayList.get(0);
                        mapAnswer2 = (HashMap<String, String>) optionsArrayList.get(1);
                        mapAnswer3 = (HashMap<String, String>) optionsArrayList.get(2);
                        mapAnswer4 = (HashMap<String, String>) optionsArrayList.get(3);

                    }


                    String option1Type = mapAnswer1.get("type").trim();
                    String option2Type = mapAnswer2.get("type").trim();
                    String option3Type = mapAnswer3.get("type").trim();
                    String option4Type = mapAnswer4.get("type").trim();

                    if (TextUtils.isEmpty(option1Type)) {
                        option1Type = "Text";
                    }
                    if (TextUtils.isEmpty(option2Type)) {
                        option2Type = "Text";
                    }
                    if (TextUtils.isEmpty(option3Type)) {
                        option3Type = "Text";
                    }
                    if (TextUtils.isEmpty(option4Type)) {
                        option4Type = "Text";
                    }
                    if (option1Type.equalsIgnoreCase("पाठ")) {
                        option1Type = "Text";
                    }
                    if (option2Type.equalsIgnoreCase("पाठ")) {
                        option2Type = "Text";
                    }
                    if (option3Type.equalsIgnoreCase("पाठ")) {
                        option3Type = "Text";
                    }
                    if (option4Type.equalsIgnoreCase("पाठ")) {
                        option4Type = "Text";
                    }


                    if (option1Type.equalsIgnoreCase("छवि")) {
                        option1Type = "Image";
                    }
                    if (option2Type.equalsIgnoreCase("छवि")) {
                        option2Type = "Image";
                    }
                    if (option3Type.equalsIgnoreCase("छवि")) {
                        option3Type = "Image";
                    }
                    if (option4Type.equalsIgnoreCase("छवि")) {
                        option4Type = "Image";
                    }

                    /* getting option values in the english language from the above stated hashmaps */
                    String option1 = mapAnswer1.get("value");
                    String option2 = mapAnswer2.get("value");
                    String option3 = mapAnswer3.get("value");
                    String option4 = mapAnswer4.get("value");

                    finalHashmap.put("type1", option1Type);
                    finalHashmap.put("type2", option2Type);
                    finalHashmap.put("type3", option3Type);
                    finalHashmap.put("type4", option4Type);
                    Random r = new Random();
                    int corectAnswer =  r.nextInt(5 - 1) + 1;
//                    int corectAnswer = 1;//= r.nextInt(5 - 1) + 1;
                    finalHashmap.put("correctOption", "Option" + corectAnswer);

                    if (TextUtils.isEmpty(option1)) {
                        option1 = "-";
                    }
                    if (TextUtils.isEmpty(option2)) {
                        option2 = "-";
                    }
                    if (TextUtils.isEmpty(option3)) {
                        option3 = "-";
                    }
                    if (TextUtils.isEmpty(option4)) {
                        option4 = "-";
                    }

                    /* Implement swich statement to Update the UI with randomise answers */
                    switch (corectAnswer) {
                        case 1:
                            finalHashmap.put("option1", option1);
                            finalHashmap.put("option2", option2);
                            finalHashmap.put("option3", option3);
                            finalHashmap.put("option4", option4);


                            finalHashmap.put("type1", option1Type);
                            finalHashmap.put("type2", option2Type);
                            finalHashmap.put("type3", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 2:

                            finalHashmap.put("option2", option1);
                            finalHashmap.put("option1", option2);
                            finalHashmap.put("option3", option3);
                            finalHashmap.put("option4", option4);

                            finalHashmap.put("type2", option1Type);
                            finalHashmap.put("type1", option2Type);
                            finalHashmap.put("type3", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 3:
                            finalHashmap.put("option3", option1);
                            finalHashmap.put("option1", option2);
                            finalHashmap.put("option2", option3);
                            finalHashmap.put("option4", option4);

                            finalHashmap.put("type3", option1Type);
                            finalHashmap.put("type1", option2Type);
                            finalHashmap.put("type2", option3Type);
                            finalHashmap.put("type4", option4Type);

                            break;
                        case 4:

                            finalHashmap.put("option4", option1);
                            finalHashmap.put("option3", option2);
                            finalHashmap.put("option2", option3);
                            finalHashmap.put("option1", option4);


                            finalHashmap.put("type4", option1Type);
                            finalHashmap.put("type3", option2Type);
                            finalHashmap.put("type2", option3Type);
                            finalHashmap.put("type1", option4Type);

                            break;
                    }
                    questionsArrayList.add(finalHashmap);


                }
                arrayListForUser = new ArrayList<>();
                for (int g = 0; g < questionsArrayList.size(); g++) {
                    HashMap<Integer, String[]> optionsMap = new HashMap<>();
                    String[] op = new String[4];
                    op[0] = "FALSE";
                    op[1] = "FALSE";
                    op[2] = "FALSE";
                    op[3] = "FALSE";
                    optionsMap.put(g, op);
                    arrayListForUser.add(optionsMap);
                }
                if (Util.getSelectedLanguage(context).equals("Hindi"))
                {
                    BiMonthlyTestAdapter.selectedLanguge = "Hindi";
                }else {
                    BiMonthlyTestAdapter.selectedLanguge = "English";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textViewToatal.setText("Question " + "0 / " + questionsArrayList.size());
        setAttemptedQ(0);
        setSkippedQ(0);
        setYetToAttemptQ(questionsArrayList.size());
        biMonthlyTestAdapter = new BiMonthlyTestAdapter(context, arrayListForUser, questionsArrayList, mRecyclerView, selectedLanguge, getScreenHeight() / 2 + 350, submit, submitTest, imageInternet, lastMessage);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("Status", "unattempted");
            biMonthlyTestAdapter.trackArrayList.add(data);
        }
        mRecyclerView.setAdapter(biMonthlyTestAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = mRecyclerView.computeHorizontalScrollOffset();
                if(offset % mRecyclerView.getWidth() == 0){
                    int position = offset / mRecyclerView.getWidth() + 1;
                    textViewToatal.setText("Question " + position + " / " + questionsArrayList.size());
                    if(position == questionsArrayList.size()){
                        for (int i = 0; i < questionsArrayList.size(); i++) {
                            if (questionsArrayList.get(position - 1).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                                textViewSubmit.setText("Submit Answer");
                                textViewSubmit.setTextColor(getResources().getColor(R.color.white));
                                textViewSubmit.setBackgroundResource(R.drawable.blue_iprep_button);
                                break;
                            }else{
                                textViewSubmit.setText("Skip");
                                textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                                textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                                break;
                            }
                        }
//                        textViewSkip.setVisibility(View.VISIBLE);
                    }else{
                        textViewSubmit.setText("Next Question");
                        textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                        textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
//                        textViewSkip.setVisibility(View.GONE);
                    }
                    pos = position - 1;
                }
            }
        });
        trackTestAdapter = new TrackTestAdapter(context , questionsArrayList);
        recyclerViewTrack.setAdapter(trackTestAdapter);
        trackTestAdapter.SetOnItemClickListener(new TrackTestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mRecyclerView != null){
                    mRecyclerView.scrollToPosition(position);
                }
                if(position == questionsArrayList.size()){
//                    textViewSkip.setVisibility(View.VISIBLE);
                    textViewSubmit.setText("Skip");
                    textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                    textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
                }else{
                    textViewSubmit.setText("Next Question");
                    textViewSubmit.setTextColor(getResources().getColor(R.color.gray));
                    textViewSubmit.setBackgroundResource(R.drawable.gray_iprep);
//                    textViewSkip.setVisibility(View.GONE);
                }
                pos = position;
            }
        });
    }

    public void refreshQuestionsListingAdapter(){
        if(trackTestAdapter != null){
            trackTestAdapter.questionsArrayList = questionsArrayList;
            trackTestAdapter.notifyDataSetChanged();
        }
    }

    public void setAttemptedQ(int count){
        if(attemptedQ != null){
            attemptedQ.setText(count + "\nAttempted");
        }
    }

    public void setSkippedQ(int count){
        if(skippedQ != null){
            skippedQ.setText(count + "\nSkipped");
        }
    }

    public void setYetToAttemptQ(int count){
        if(yetToAttemptQ != null){
            yetToAttemptQ.setText(count + "\nyet to attempt");
        }
    }

    private boolean isSkippedQ(int pos){
        boolean isSkipped = true;
        for (Map.Entry<Integer, Boolean> entry : skippedQHashMap.entrySet()) {
            if(entry.getKey() == pos){
                isSkipped = false;
                break;
            }
        }
        return isSkipped;
    }

    public void updateSkippedQMap(int pos, int type){
        if(skippedQHashMap != null){
            if(type == 0){
                if(!skippedQHashMap.containsKey(pos) && !attemptedQMap.contains(pos)){
                    skippedQHashMap.put(pos, true);
                    skipCount++;
                    setSkippedQ(skipCount);
                }
            }else{
                if (skippedQHashMap.containsKey(pos) && skippedQHashMap.get(pos) && attemptedQMap.contains(pos)) {
                    skippedQHashMap.put(pos, false);
                    skipCount--;
                    setSkippedQ(skipCount);
                }
            }
        }
    }

    public void checkSkippedQ(int pos, int type){
        if(isSkippedQ(pos)){
            updateSkippedQMap(pos, type);
        }else{
            if(skipCount != 0){
                updateSkippedQMap(pos, type);
            }
        }
    }

    private void clearSkipData(){
        skipCount = 0;
        setSkippedQ(0);
        if(attemptedQMap != null){
            attemptedQMap.clear();
        }
        if(skippedQHashMap != null){
            skippedQHashMap.clear();
        }
    }

    public void removeAttempt(int position){
        if(attemptedQMap != null){
            attemptedQMap.remove(position);
        }
    }

    private void startCountDownTimer() throws Exception {
        final long time = duration * 60 * 1000;
        countDownTimerWithPause = new CountDownTimerWithPause(time, 1000, true) {
            public void onTick(long millisUntilFinished) {
                long progressTime = millisUntilFinished / 100;
                progress++;
                arcProgress.setProgress(progress * 100 / (time / 1000));
                String time = Util.getTimeString_(millisUntilFinished / 1000);
                arcProgress.setBottomText(time);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                progress++;

                arcProgress.setProgress(100);
                try {
                    countDownTimerWithPause.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                submitTestPaper();
                try {
                    // syncScore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                openDialog();
            }
        };

        try {
            countDownTimerWithPause.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startModelTestpaper() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_start_model);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(startMessage);
        TextView textViewGoBack = dialog.findViewById(R.id.textViewGoBack);
        textViewGoBack.setText(goBack);
        TextView textViewStart = dialog.findViewById(R.id.textViewStart);
        textViewStart.setText(start);
        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startCountDownTimer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isNeededToOpenBackDialog = true;
            }
        });
    }

    private void resumeModelTestpaper() {
        isDialogOpend = true;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pause_model);
        dialog.setCancelable(false);
        TextView textViewStart = dialog.findViewById(R.id.textViewStart);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(welcomeBack);
        textViewStart.setText(resume);
        try {

            if (countDownTimerWithPause != null) {
                countDownTimerWithPause.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    countDownTimerWithPause.resume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                isDialogOpend = false;
                isNeededToOpenBackDialog = true;
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private final String board = "cbse";

    public void submitTestPaper() {
        Util.showScoreDialog(context, calculateScore);
        isNeededToOpenBackDialog = false;
        date = Util.getCurrentDateWithDifferentFormat();

        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
        scoreModel.setTotalScores(questionsArrayList.size() + "");
        scoreModel.setQuestionsAttempted(attemptedQuestions);
        scoreModel.setTotalQuestions(questionsArrayList.size() + "");
        scoreModel.setTopicName(Util.getTopicNameAlt(context));

        HashMap<String, Object> scoreDetailHashmap = new HashMap<>();
        scoreDetailHashmap.put("scores", scoreModel);
        scoreDetailHashmap.put("detail_review", questionsArrayList);

        long timeTosync = endTime - startTime;

        biMontlyNumber = biMontlyNumber + 1;

        endTime = System.currentTimeMillis();
        timeTosync = timeTosync + timeget;

        int percentage = (biMonthlyTestAdapter.scoreArrayList.size() * 100) / questionsArrayList.size();

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(subject).setValue(timeTosync);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("bi_monthly_test").setValue(biMontlyNumber);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child("bi_monthly_test").child(subject).child(date).child("" + System.currentTimeMillis()).setValue(scoreDetailHashmap);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child("bi_monthly_test").child(date).child(topicId).child("name").setValue(Util.getTopicNameAlt(context));
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child("bi_monthly_test").child(date).child(topicId).child("detail").child("" + System.currentTimeMillis()).setValue(scoreDetailHashmap);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child("bi_monthly_test").child(topicId).child("detail").setValue(scoreDetailHashmap);

        //For Time Spent
        if(reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, subject,Util.getSelectedLanguage(context))){
            reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, subject, timeTosync,Util.getSelectedLanguage(context));
        }else{
            ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
            reportsTimeSpentModel.setUserId(Util.getUserId(context));
            reportsTimeSpentModel.setBoard(board);
            reportsTimeSpentModel.setSClass(sClass);
            reportsTimeSpentModel.setSubject(subject);
            reportsTimeSpentModel.setDate(date);
            reportsTimeSpentModel.setTime(timeTosync);
            reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
            reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
        }

        //For Count
        if(reportsCountRepository.isDataExist(Util.getUserId(context), board, sClass, date, "bi_monthly_test")){
            reportsCountRepository.updateField(Util.getUserId(context), board, sClass, date, "bi_monthly_test", biMontlyNumber);
        }else{
            ReportsCountModel reportsCountModel = new ReportsCountModel();
            reportsCountModel.setUserId(Util.getUserId(context));
            reportsCountModel.setBoard(board);
            reportsCountModel.setSClass(sClass);
            reportsCountModel.setDate(date);
            reportsCountModel.setType("bi_monthly_test");
            reportsCountModel.setCount(biMontlyNumber);
            reportsCountRepository.insertCountDetails(reportsCountModel);
        }

        //For Date Wise
        if(reportsDateWiseTestRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "bi_monthly_test", date, System.currentTimeMillis())){
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsDateWiseTestRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "bi_monthly_test", date, System.currentTimeMillis(), listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored());
        }else{
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsDateWiseTestModel reportsDateWiseTestModel = new ReportsDateWiseTestModel();
            reportsDateWiseTestModel.setUserId(Util.getUserId(context));
            reportsDateWiseTestModel.setBoard(board);
            reportsDateWiseTestModel.setSClass(sClass);
            reportsDateWiseTestModel.setSubject(Util.getSubject(context));
            reportsDateWiseTestModel.setType("bi_monthly_test");
            reportsDateWiseTestModel.setTestDate(date);
            reportsDateWiseTestModel.setTime(System.currentTimeMillis());
            reportsDateWiseTestModel.setList(list);
            reportsDateWiseTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsDateWiseTestRepository.insertTestDetails(reportsDateWiseTestModel);
        }

        //For Topic Wise
        if(reportsTopicWiseTestRepository.isDataExist(Util.getUserId(context), board, sClass, subject, "bi_monthly_test", topicId, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsTopicWiseTestRepository.updateField(Util.getUserId(context), board, sClass, subject, "bi_monthly_test", topicId, date,
                    System.currentTimeMillis(), Util.getTopicNameAlt(context), listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored(),Util.getSelectedLanguage(context));
        }else{
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsTopicWiseTestModel reportsTopicWiseTestModel = new ReportsTopicWiseTestModel();
            reportsTopicWiseTestModel.setUserId(Util.getUserId(context));
            reportsTopicWiseTestModel.setBoard(board);
            reportsTopicWiseTestModel.setSClass(sClass);
            reportsTopicWiseTestModel.setSubject(Util.getSubject(context));
            reportsTopicWiseTestModel.setType("bi_monthly_test");
            reportsTopicWiseTestModel.setTestDate(date);
            reportsTopicWiseTestModel.setTime(System.currentTimeMillis());
            reportsTopicWiseTestModel.setTopicId(topicId);
            reportsTopicWiseTestModel.setName(Util.getTopicNameAlt(context));
            reportsTopicWiseTestModel.setList(list);
            reportsTopicWiseTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsTopicWiseTestModel.setSubjectName(Util.getSubjectName(context));
            reportsTopicWiseTestRepository.insertTestDetails(reportsTopicWiseTestModel);
        }

        //For Latest data
        if(reportsLatestDataTestRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), "bi_monthly_test", topicId)){
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }
            String listData = DataTypeConverter.ListToString(list);
            reportsLatestDataTestRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), "bi_monthly_test", topicId, listData, reportsTestScoreModel.getDate(),
                    reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored());
        }else{
            ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
            reportsTestScoreModel.setDate(date);
            reportsTestScoreModel.setQuestionsAttempted(attemptedQuestions);
            reportsTestScoreModel.setScores(biMonthlyTestAdapter.scoreArrayList.size() + "");
            reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
            reportsTestScoreModel.setTotalQuestions(questionsArrayList.size() + "");
            reportsTestScoreModel.setTotalScores(questionsArrayList.size() + "");
            reportsTestScoreModel.setPercentageScored(percentage + "");

            ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();
            for(HashMap<String, String> item: questionsArrayList){
                ReportsTestDetailReviewModel reportsTestDetailReviewModel = new ReportsTestDetailReviewModel();
                reportsTestDetailReviewModel.setCorrectFeedback(item.get("correct_feedback"));
                reportsTestDetailReviewModel.setCorrectOption(item.get("correctOption"));
                reportsTestDetailReviewModel.setImage(item.get("image"));
                reportsTestDetailReviewModel.setIncorrectFeedback(item.get("incorrect_feedback"));
                reportsTestDetailReviewModel.setIsAttempted(item.get("IsAttampted"));
                reportsTestDetailReviewModel.setOption1(item.get("option1"));
                reportsTestDetailReviewModel.setOption2(item.get("option2"));
                reportsTestDetailReviewModel.setOption3(item.get("option3"));
                reportsTestDetailReviewModel.setOption4(item.get("option4"));
                reportsTestDetailReviewModel.setOptionSelected(item.get("OptionSelected"));
                reportsTestDetailReviewModel.setQuestionId(item.get("questionID"));
                reportsTestDetailReviewModel.setQuestionQ(item.get("questionQ"));
                reportsTestDetailReviewModel.setStatus(item.get("Status"));
                reportsTestDetailReviewModel.setType1(item.get("type1"));
                reportsTestDetailReviewModel.setType2(item.get("type2"));
                reportsTestDetailReviewModel.setType3(item.get("type3"));
                reportsTestDetailReviewModel.setType4(item.get("type4"));
                list.add(reportsTestDetailReviewModel);
            }

            ReportsLatestDataTestModel reportsLatestDataTestModel = new ReportsLatestDataTestModel();
            reportsLatestDataTestModel.setUserId(Util.getUserId(context));
            reportsLatestDataTestModel.setBoard(board);
            reportsLatestDataTestModel.setSClass(sClass);
            reportsLatestDataTestModel.setSubject(Util.getSubject(context));
            reportsLatestDataTestModel.setType("bi_monthly_test");
            reportsLatestDataTestModel.setTopicId(topicId);
            reportsLatestDataTestModel.setList(list);
            reportsLatestDataTestModel.setReportsTestScoreModel(reportsTestScoreModel);
            reportsLatestDataTestRepository.insertTestDetails(reportsLatestDataTestModel);
        }

        if (isAssigned.equalsIgnoreCase("true")) {
            String scores = biMonthlyTestAdapter.scoreArrayList.size() + "/" + questionsArrayList.size();
            global.getDatabaseReference().child("content_assignement_batch_wise").child(teacherID).child(batchId).child(datetostore).child(keyTo).child("student").child(Util.getUserId(context)).child("progress").setValue(scores);
        }

        openDialog();
    }


    private long timeget;

    private void getTime() {
        date = Util.getCurrentDateWithDifferentFormat();
        if(Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "bi_monthly_test", "timeTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").child(date).child(subject).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue() != null) {
                                timeget = (long) snapshot.getValue();
                            } else {
                                timeget = 0;
                            }
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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String date, String testType, String type){
        if(type.equals("timeTask")){
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    timeTaskDisposable = d;
                                }

                                @Override
                                public void onNext(Object o) {
                                    ArrayList<ReportsTimeSpentModel> list = (ArrayList<ReportsTimeSpentModel>) o;
                                    if(list != null && list.size() > 0){
                                        for(ReportsTimeSpentModel item: list){
                                            timeget = item.getTime();
                                        }
                                    }else{
                                        timeget = 0;
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                    timeTaskDisposable.dispose();
                                }
                            });
        }else if(type.equals("countTask")){
            getList(userId, board, sClass, subject, date, testType, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            countTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsCountModel> list = (ArrayList<ReportsCountModel>) o;
                            if(list != null && list.size() > 0){
                                for(ReportsCountModel item: list){
                                    biMontlyNumber = item.getCount();
                                }
                            }else{
                                biMontlyNumber = 0;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            countTaskDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date, String testType, String type){
        if(type.equals("timeTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, subject,Util.getSelectedLanguage(context));
            });
        }else if(type.equals("countTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsCountRepository.getDetail(userId, board, sClass, date, testType);
            });
        }
        return null;
    }

    public void scroll(final int pos) {
        //mRecyclerView.smoothScrollToPosition(pos);
    }

    public void setTextOnQuestionAttampted(int size) {
        this.attemptedQuestions = String.valueOf(size);
//        textViewToatal.setText("Question " + size + " / " + questionsArrayList.size());

    }


    private long biMontlyNumber;

    private void getBiMonthyTestCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        if(!Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), date, "bi_monthly_test", "countTask");
        }else{
             global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("count").child(date).child("bi_monthly_test").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            biMontlyNumber = (long) snapshot.getValue();
                        } else {
                            biMontlyNumber = 0;
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


    private void showExitDialog() {
        SelectTopicActivity.currentTestScoreChanged = "0";
        isNeededToOpenBackDialog = false;
        try {
            countDownTimerWithPause.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_exit_model);
        dialog.setCancelable(false);
        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
        TextView textView = dialog.findViewById(R.id.textView);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
//        TxtExit.setText(exitTest);
        TxtExit.setText("Exit Test");
        textView.setText(warning + "\n" + exitMessage);
        textViewContinoue.setText(continueTest);

        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    countDownTimerWithPause.resume();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isNeededToOpenBackDialog = true;
            }
        });

    }

    private void submitConfirmationCheck() {
        isNeededToOpenBackDialog = false;
        try {
            countDownTimerWithPause.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_submit_confirmation);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(sureSubmit);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(String.format(unattemptedQLeft, questionsArrayList.size() - Integer.parseInt(this.attemptedQuestions)));
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        textViewContinoue.setText(submit);
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    submitTestPaper();

                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        textViewCancel.setText(completeTest);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isNeededToOpenBackDialog = true;
            }
        });
    }

    private void openDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.dismissScoreDialog();
                try {

                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).child("BTest").setValue("BT");

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_submit_model);
                    dialog.setCancelable(false);
                    TextView textViewReview = dialog.findViewById(R.id.textViewReview);
                    TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
                    textViewMessage.setText(greeting + " " + Util.getUsername(context) + "\n" + yourScore + " " + biMonthlyTestAdapter.scoreArrayList.size() + "/" + questionsArrayList.size());
                    TextView textViewGoBack = dialog.findViewById(R.id.textViewGoBack);
                    textViewGoBack.setText(goBack);
                    textViewReview.setText(review);
                    textViewReview.setVisibility(View.GONE);
                    textViewReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            int percentage = (biMonthlyTestAdapter.scoreArrayList.size() * 100) / questionsArrayList.size();
                            startActivity(new Intent(context, ReviewModelTestPaperActivity.class));
                            finish();
                            
                        }
                    });
                    textViewGoBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                            

                        }
                    });
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            isNeededToOpenBackDialog = true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }













    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }


    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject("english");
                JSONArray array = object.getJSONArray("Model Test Screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    questionAttempted = textArrayList.get(0);
                    totalTime = textArrayList.get(1);
                    welcomeBack = textArrayList.get(2);
                    start = textArrayList.get(3);
                    warning = textArrayList.get(4);
                    exitMessage = textArrayList.get(5);
                    continueTest = textArrayList.get(6);
                    exitTest = textArrayList.get(7);
                    unattemptedQLeft = textArrayList.get(8);
                    sureSubmit = textArrayList.get(9);
                    submit = textArrayList.get(10);
                    completeTest = textArrayList.get(11);
                    calculateScore = textArrayList.get(12);
                    greeting = textArrayList.get(13);
                    yourScore = textArrayList.get(14);
                    review = textArrayList.get(15);
                    goBack = textArrayList.get(16);
                    startMessage = textArrayList.get(17);
                    minutes = textArrayList.get(18);
                    attempted = textArrayList.get(19);
                    attempt = textArrayList.get(20);
                    resume = textArrayList.get(21);
                    submitTest = textArrayList.get(22);
                    imageInternet = textArrayList.get(23);
                    lastMessage = textArrayList.get(24);

                    textViewTime.setText(totalTime + " : " + duration + " " + minutes);

                }

                getQuestions();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showDialog(context);
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Model Test Screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            questionAttempted = textArrayList.get(0);
                            totalTime = textArrayList.get(1);
                            welcomeBack = textArrayList.get(2);
                            start = textArrayList.get(3);
                            warning = textArrayList.get(4);
                            exitMessage = textArrayList.get(5);
                            continueTest = textArrayList.get(6);
                            exitTest = textArrayList.get(7);
                            unattemptedQLeft = textArrayList.get(8);
                            sureSubmit = textArrayList.get(9);
                            submit = textArrayList.get(10);
                            completeTest = textArrayList.get(11);
                            calculateScore = textArrayList.get(12);
                            greeting = textArrayList.get(13);
                            yourScore = textArrayList.get(14);
                            review = textArrayList.get(15);
                            goBack = textArrayList.get(16);
                            startMessage = textArrayList.get(17);
                            minutes = textArrayList.get(18);
                            attempted = textArrayList.get(19);
                            attempt = textArrayList.get(20);
                            resume = textArrayList.get(21);
                            submitTest = textArrayList.get(22);
                            imageInternet = textArrayList.get(23);
                            lastMessage = textArrayList.get(24);

                            textViewTime.setText(totalTime + " : " + duration + " " + minutes);
                        }
                        getQuestions();
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
    protected void onStop() {
        super.onStop();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                biMonthlyTestAdapter.notifyDataSetChanged();
            } else {
                biMonthlyTestAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* scroll recycleview current time on the top*/
    public class SnapTopLinearLayoutManager extends GridLayoutManager {

        public SnapTopLinearLayoutManager(Context context, int count) {
            super(context, count);
        }

        @Override
        public void scrollToPositionWithOffset(int position, int offset) {
            super.scrollToPositionWithOffset(position, offset);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            SnapTopLinearSmoothScroller linearSmoothScroller =
                    new SnapTopLinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return SnapTopLinearLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    /* Smooth scroller for recycleview*/
    public abstract class SnapTopLinearSmoothScroller extends LinearSmoothScroller {
        public SnapTopLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}

