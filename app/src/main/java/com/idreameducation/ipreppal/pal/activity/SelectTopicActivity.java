package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.SelectTopicAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AssignTopicModel;
import com.idreameducation.ipreppal.model.ComulativeMasteryModel;
import com.idreameducation.ipreppal.roomdatabase.model.PracticeModel;
import com.idreameducation.ipreppal.roomdatabase.repository.PrcticeRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by anurag on 26-Oct-17.
 */

public class SelectTopicActivity extends AppCompatActivity  {
    public static String currentTopicMasteryChanged;
    public static String currentTestScoreChanged;
    public static SelectTopicActivity activity;
    int position = 0;
    /*Dec Widgets*/
    private RecyclerView mRecyclerView;
    private Context context;
    private TextView textViewGrade;
    private TextView textView;
    private Global global;
    private ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList;
    private ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList_;
    private String subject;
    private String latestScore;
    private String mastery;
    private String takeTest;
    private String start;
    private String currentTopicMastery;
    private String currentTestScore;
    private String score;
    private String notyet;
    private String message1;
    private String message2;
    private String assigned;
    private RelativeLayout reletiveParent;
    private int pos;
    private SelectTopicAdapter selectTopicAdapter;
    private ImageView imageViewHome;
    private ImageView searchImageView;
    private AutoCompleteTextView topicSearchAutocompletetextView;
    private ImageView askforhelpImageView;
    private ArrayList<HashMap<String, HashMap<String, String>>> contentSearchedArrayList;
    private int posCate;
    private String currentMastery;
    private boolean isSearching = false;
    private String iDreamWork;
    private String categoryName;
    private String categoryID;
    private Spinner subjectSpinner;
    private boolean isTopicLoaded = false;
    private PrcticeRepository practiceRepository;
    private boolean isClicked = false;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout LinearShimmer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_select_topic);
        /*Call hookUp function*/
        assignIds();
        listners();
    }

    private void listners() {
        findViewById(R.id.askforhelpImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWelcomeMessages();
            }
        });

        findViewById(R.id.topicSearchAutocompletetextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicSearchAutocompletetextView.setFocusable(true);
                topicSearchAutocompletetextView.requestFocus();
            }
        });

        findViewById(R.id.searchImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearching) {
                    isSearching = false;
                    //searchImageView.setImageResource(R.mipmap.search);
                    try {
                        Glide.with(context)
                                .load(R.mipmap.search)
                                .into(searchImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    subjectSpinner.setVisibility(View.VISIBLE);
                    topicSearchAutocompletetextView.setVisibility(View.GONE);
                    topicSearchAutocompletetextView.setText("");
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    //Find the currently focused view, so we can grab the correct window token from it.
//                    //If no view currently has focus, create a new one, just so we can grab a window token from it
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } else {
                    isSearching = true;
//                    searchImageView.setImageResource(R.mipmap.cross);
                    try {
                        Glide.with(context)
                                .load(R.mipmap.cross)
                                .into(searchImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    subjectSpinner.setVisibility(View.GONE);
                    topicSearchAutocompletetextView.setVisibility(View.VISIBLE);
                    topicSearchAutocompletetextView.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

    }

    /*Init values , widgets , strings etc...*/
    private void assignIds() {
        context = this;
        activity = this;
        currentTopicMasteryChanged = null;
        currentTestScoreChanged = null;
        global = (Global) getApplicationContext();
        practiceRepository = new PrcticeRepository(getApplicationContext());
        global.sendData("Practice Topic", this.getClass().getName());
        //subject = Util.getSubject(context);
        posCate = getIntent().getIntExtra("position", 0);
        try {
            categoryName = getIntent().getStringExtra("name");
            categoryID = getIntent().getStringExtra("categoryID");
            posCate = getIntent().getIntExtra("position", 0);
            //posCate = 4;
            global.setCateGoryName(categoryName);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setStaticText();
                }
            }, 300);

        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IllegalStateException) {
                Util.showInternetConnectioError(context);
                Util.dismissdataDialog();
            }
        }
        subjectSpinner = findViewById(R.id.subjectSpinner);
        imageViewHome = findViewById(R.id.imageViewHome);
        imageViewHome.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.recyclerView);
        askforhelpImageView = findViewById(R.id.askforhelpImageView);
        textViewGrade = findViewById(R.id.textViewGrade);
        textView = findViewById(R.id.textView);
        reletiveParent = findViewById(R.id.reletiveParent);
        searchImageView = findViewById(R.id.searchImageView);
        searchImageView.setVisibility(View.VISIBLE);
        topicSearchAutocompletetextView = findViewById(R.id.topicSearchAutocompletetextView);
        topicSearchAutocompletetextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!TextUtils.isEmpty(editable.toString())) {
                        position = 0;
                        contentSearchedArrayList = new ArrayList<>();
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("English")) {
                            HashMap<String, HashMap<String, String>> hashMapHashMap;
                            HashMap<String, String> hashMap;
                            for (int i = 0; i < contentArrayList.size(); i++) {
                                hashMapHashMap = new HashMap<>();
                                String name = contentArrayList.get(i).get(i + "").get("TName");
                                if (name.toLowerCase().contains(editable.toString().toLowerCase())) {
                                    hashMap = contentArrayList.get(i).get("" + i);
                                    hashMapHashMap.put("" + position, hashMap);
                                    position++;
                                    contentSearchedArrayList.add(hashMapHashMap);
                                }
                            }
                            try {
                                selectTopicAdapter = new SelectTopicAdapter(context, contentSearchedArrayList, currentMastery, takeTest, start, score, assigned, latestScore);
                                mRecyclerView.setAdapter(selectTopicAdapter);
                                itemClickListner(contentSearchedArrayList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            for (int i = 0; i < contentArrayList.size(); i++) {
                                String nameAlt = contentArrayList.get(i).get(i + "").get("TName_alt");
                            }
                        }
                    } else {
                        if (contentArrayList.size() > 0) {
                            selectTopicAdapter = new SelectTopicAdapter(context, contentArrayList, currentMastery, takeTest, start, score, assigned, latestScore);
                            mRecyclerView.setAdapter(selectTopicAdapter);
                            try {
                                itemClickListner(contentArrayList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        LinearShimmer = findViewById(R.id.LinearShimmer);
        shimmerFrameLayout.startShimmerAnimation();

        ImageView imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                if (isSearching) {
                    isSearching = false;
                    //searchImageView.setImageResource(R.mipmap.search);
                    try {
                        Glide.with(context)
                                .load(R.mipmap.search)
                                .into(searchImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    subjectSpinner.setVisibility(View.VISIBLE);
                    topicSearchAutocompletetextView.setVisibility(View.GONE);
                    topicSearchAutocompletetextView.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //Find the currently focused view, so we can grab the correct window token from it.
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } else {
                    onBackPressed();
                }
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(manager);

    }

    private void itemClickListner(final ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList) throws Exception {
        selectTopicAdapter.SetOnItemClickListener(new SelectTopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                try {
                    if (!isClicked) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                        if (contentArrayList.get(position).get(position + "").get("isAssigned").equalsIgnoreCase("True")) {
                            // delete the assigned Topic
                            String FID = contentArrayList.get(position).get(position + "").get("FID");
                            String key = contentArrayList.get(position).get(position + "").get("keytoDelete");
                            global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child("AssignedContent").child(FID).child(Util.getUserId(context)).child(categoryID).child(key).removeValue();
                            contentArrayList.get(position).get(position + "").put("isAssigned", "False");
                            selectTopicAdapter.notifyDataSetChanged();
                        }

                        //     if (contentArrayList.get(position).get(position + "").get("status").equalsIgnoreCase("Demo")) {
                        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.TOPICS).child(contentArrayList.get(position).get("" + position).get("TopicID")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot != null) {
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            HashMap<String, String> facMap = (HashMap<String, String>) dataSnapshot.getValue();
                                            AssignTopicModel assignTopicModel = new AssignTopicModel();
                                            assignTopicModel.setFlag("Done");
                                            assignTopicModel.setStatus("Started");
                                            assignTopicModel.setTopicId(contentArrayList.get(position).get("" + position).get("TopicID"));
                                            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).
                                                    child(Util.getSubject(context))
                                                    .child(contentArrayList.get(position).get("" + position).get("TopicID")).child("flag").removeValue();
                                            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).
                                                    child(Util.getSubject(context))
                                                    .child(contentArrayList.get(position).get("" + position).get("TopicID")).child("status").removeValue();

                                            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.TOPICS).child(assignTopicModel.getTopicId()).removeValue();
                                            global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.TOPICS).child(facMap.get("facilitatorId")).child(ApplicationConstants.DATA).child(Util.getUserId(context)).child(contentArrayList.get(position).get("" + position).get("TopicID")).removeValue();
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
                        if (contentArrayList.get(position).get("" + position).get("mastery") == null) {
                            currentTopicMastery = "0";
                        } else {
                            currentTopicMastery = contentArrayList.get(position).get("" + position).get("mastery");
                        }

                        if (contentArrayList.get(position).get("" + position).get("score") == null) {
                            currentTestScore = "0";
                        } else {
                            currentTestScore = contentArrayList.get(position).get("" + position).get("score");
                        }


                        pos = position;
                        Util.setTopicNameAlt(context, contentArrayList.get(position).get("" + position).get("TName"));
                        if (contentArrayList.get(position).get("" + position).get("isAlternateLanguageAvailable").equalsIgnoreCase("False")) {
                            global.setFlagEnabled(false);
                            Util.setTopicNameAlt(context, contentArrayList.get(position).get("" + position).get("TName"));
                        } else {
                            global.setFlagEnabled(true);
                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("English")) {
                                Util.setTopicNameAlt(context, contentArrayList.get(position).get("" + position).get("TName"));
                            } else {
                                Util.setTopicNameAlt(context, contentArrayList.get(position).get("" + position).get("TName_alt"));
                            }
                        }
                        Util.setTopicID(context, contentArrayList.get(position).get("" + position).get("TopicID"));


                        if (contentArrayList.get(position).get("" + position).get("IsModelTestPaper").equalsIgnoreCase("True")) {
                            isClicked = false;
                            //startActivity(new Intent(context, ModelPaperActivity.class).putExtra("Time", contentArrayList.get(position).get("" + position).get("Time")));
                        } else {
                            global.setLevelNo(Integer.parseInt(contentArrayList.get(position).get("" + position).get("Levels")));
                            try {
                                try {
                                    if (contentArrayList.get(position).get("" + position).get("Foundational_Topic_ID") != null) {
                                        global.setFoundationalTopicId(contentArrayList.get(position).get("" + position).get("Foundational_Topic_ID"));
                                    } else {
                                        global.setFoundationalTopicId(ApplicationConstants.NOT_AVAILABLE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    global.setFoundationalTopicId(ApplicationConstants.NOT_AVAILABLE);
                                }
                                int progress = Integer.parseInt(contentArrayList.get(position).get("" + position).get("mastery"));
                                global.setProgress(progress);
                                Util.setLevel(context, Integer.parseInt(contentArrayList.get(position).get("" + position).get("currentLevel")));

                            } catch (Exception e) {
                                global.setProgress(0);
                                Util.setLevel(context, 1);
                            }
                            String sProgress = contentArrayList.get(position).get("" + position).get("streakProgress");
                            isClicked = false;
                           // startActivity(new Intent(context, QuestionActivity.class).putExtra("streakProgress", sProgress).putExtra("streak", contentArrayList.get(position).get("" + position).get("StreakCount")).putExtra("incorrectStreak", contentArrayList.get(position).get("" + position).get("incorrectStreak")));
                        }
                        
//                        } else {
//                            showDemoPlanDialog();
//                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* Reflect mastery changes in recycleView*/
        try {
            if (currentTopicMastery != null && currentTopicMasteryChanged != null) {
                if (!currentTopicMastery.equalsIgnoreCase(currentTopicMasteryChanged)) {
                    try {
                        getUserAnalyticsAfterChange(Util.getTopicID(context), pos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    if (currentTestScore != null && currentTestScoreChanged != null) {
                        getDetailsLastSubmitedTestPaperScore(contentArrayList.get(pos).get("" + pos).get("TopicID"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void calculateCumulativeMastery() {
        float comulative_mastery = 0;
        int numberOfHiddenTopic = 0;
        int totalTopics = 0;
        if (contentArrayList != null) {
            if (contentArrayList.size() > 0) {
                for (int i = 0; i < contentArrayList.size(); i++) {
                    if (contentArrayList.get(i).get("" + i).get("IsModelTestPaper").equalsIgnoreCase("False")) {
                        totalTopics++;
                        try {
                            comulative_mastery += Float.parseFloat(contentArrayList.get(i).get("" + i).get("mastery"));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (contentArrayList.get(i).get("" + i).get("enabled").equalsIgnoreCase("False")) {
                            numberOfHiddenTopic++;
                        }
                    }
                }
                int calculations = (int) (comulative_mastery / (totalTopics - numberOfHiddenTopic));
                global.setCumulativeMastery(calculations);
                sendAnalyticsToFireBase(calculations, Util.getSubject(context));
            }
        }
    }

    //*****                                          *****//
    //*****     Send User comulative to firebase     *****//
    //*****                                          *****//
    private void sendAnalyticsToFireBase(int comulativeMastery, String subject) {
        ComulativeMasteryModel comulativeMasteryModel = new ComulativeMasteryModel();
        comulativeMasteryModel.setComulativeMastery(String.valueOf(comulativeMastery));
        comulativeMasteryModel.setLastAttamptedDate(global.getLastAttemptedDate());
        /* Call for Test optimise the code */
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.COMULUTIVE).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).
                child(Util.getSubject(context)).setValue(comulativeMasteryModel);
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).child(subject).child(ApplicationConstants.COMULUTIVE).setValue(comulativeMasteryModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    private void getTopics(String subject) throws Exception {

        global.getDatabaseReference().child(ApplicationConstants.NEW_TOPIC_DB).child(Util.getSelectedBoard(context).toUpperCase()).child(Util.getSelectedClass(context)).child(Util.getSelectedLanguagePackage(context)).child(ApplicationConstants.CATEGORIES).child("" + posCate).child("content").child(subject).child(ApplicationConstants.TOPICS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    try {
                        Util.dismissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentArrayList = new ArrayList<>();
                    HashMap<String, HashMap<String, String>> modelHashMap = null;
                    for (DataSnapshot single_ : dataSnapshot.getChildren()) {
                        HashMap<String, String> innerTopicNode = (HashMap<String, String>) single_.getValue();
                        innerTopicNode.put("mastery", "0");
                        innerTopicNode.put("enabled", "False");
                        innerTopicNode.put("currentLevel", "1");
                        innerTopicNode.put("flag", "Done");
                        innerTopicNode.put("score", notyet);
                        innerTopicNode.put("isAssigned", "False");
                        innerTopicNode.put("FID", "not");
                        innerTopicNode.put("keytoDelete", "not");

                        modelHashMap = new HashMap<>();
                        modelHashMap.put(single_.getKey(), innerTopicNode);
                        contentArrayList.add(modelHashMap);
                    }
                    askforhelpImageView.setVisibility(View.GONE);


//                    try {
//                        selectTopicAdapter = new SelectTopicAdapter(context, contentArrayList, currentMastery, takeTest, start, score, assigned, latestScore);
//                        ArrayList<HashMap<String, String>> batchArrayList = global.getFacilitatorList();
//                        if (batchArrayList.size() > 0) {
//                            String sClass = Util.getSelectedClass(context);
//                            String sBoard = Util.getSelectedBoard(context);
//                            String sLangauge = Util.getSelectedLanguagePackage(context);
//                            for (int k = 0; k < batchArrayList.size(); k++) {
//                                String name = batchArrayList.get(k).get("name");
//                                String subject_ = batchArrayList.get(k).get("subject");
//                                if (name.contains(sBoard)) {
//                                    if (name.contains(sClass)) {
//                                        if (name.contains(sLangauge)) {
////                                                                if (subject_.contains(subject)) {
//                                            String facilitatorId = batchArrayList.get(k).get("facilitatorId");
//                                            getAssignedTopics(selectTopicAdapter, facilitatorId);
////                                                                }
//
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    shimmerFrameLayout.stopShimmerAnimation();
                    LinearShimmer.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(selectTopicAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    itemClickListner(contentArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    getPlanDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    posi = 0;
                    contentArrayList_ = new ArrayList<>();
//                    for (int i = 0; i < contentArrayList.size(); i++) {
//                        String topicID = contentArrayList.get(i).get("" + i).get("TopicID");
//                        ArrayList<String> storedArrayList = Util.getTopics(context);
//
//
//                        if (storedArrayList.contains(topicID)) {
//                            HashMap<String, HashMap<String, String>> data = contentArrayList.get(i);
//                            HashMap<String, String> dataInner = new HashMap<>();
//                            HashMap<String, HashMap<String, String>> data_ = new HashMap<>();
//
//                            for (String key : data.keySet()) {
//                                dataInner = data.get(key);
//                                data_.put("" + i, dataInner);
//                                contentArrayList_.add(data_);
//                            }
//
//                        }
//                    }


                    int position=0;
                    for (int i = 0; i < contentArrayList.size(); i++) {
                        String topicID = contentArrayList.get(i).get("" + i).get("TopicID");
                        ArrayList<String> storedArrayList = Util.getTopics(context);
                        if (storedArrayList.contains(topicID)) {
                            HashMap<String, HashMap<String, String>> modelHashMap_ = new HashMap<>();
                            HashMap<String, String> innerTopicNode = contentArrayList.get(i).get(""+i);
                            modelHashMap_.put(""+position, innerTopicNode);
                            contentArrayList_.add(modelHashMap_);
                            position++;
                        }
                    }

                    if (contentArrayList_.size() > 0) {
                        String tID = contentArrayList_.get(0).get("" + 0).get("TopicID");
                        getData(contentArrayList_, tID);
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

    /* Get user analytics */
    private void getUserAnalyticsAfterChange(String TopicId, final int pos) {
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).child(Util.getSubject(context)).child(TopicId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    HashMap<String, String> masteryMap = (HashMap<String, String>) dataSnapshot.getValue();
                    String mastery = masteryMap.get("mastery");
                    String currentLevel = masteryMap.get("currentLevel");
                    contentArrayList.get(pos).get("" + pos).put("mastery", mastery);
                    contentArrayList.get(pos).get("" + pos).put("currentLevel", currentLevel);
                    selectTopicAdapter.notifyDataSetChanged();
                    //calculateCumulativeMastery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDetailsLastSubmitedTestPaperScore(String topicID) throws Exception {
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).
                child(Util.getSubject(context))
                .child(topicID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Util.dismissDialog();
                    HashMap<String, String> scoresHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                    score = scoresHashmap.get("Score");
                    mastery = scoresHashmap.get("mastery");
                    contentArrayList.get(pos).get("" + pos).put("score", score);
                    contentArrayList.get(pos).get("" + pos).put("mastery", mastery);
                    selectTopicAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Util.dismissDialog();
            }
        });
    }

    private int posi = 0;

    private void getData(final ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList_, String topicID) throws Exception {
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).
                child(Util.getSubject(context)).child(topicID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {


                        if (posi == contentArrayList_.size() - 1) {
                            ArrayList<HashMap<String, String>> topicIdsArrayList = new ArrayList<>();
                            HashMap<String, String> dataHashMap = new HashMap<>();
                            HashMap<String, String> masteryMap = (HashMap<String, String>) dataSnapshot.getValue();
                            String mastery = masteryMap.get("mastery");
                            String streakProgress = masteryMap.get("streakProgress");
                            if (streakProgress != null) {
                                dataHashMap.put("streakProgress", streakProgress);
                            } else {
                                dataHashMap.put("streakProgress", "0");
                            }
                            if (mastery != null) {
                                dataHashMap.put("mastery", mastery);
                            } else {
                                dataHashMap.put("mastery", "0");
                            }
                            String currentLevel = masteryMap.get("currentLevel");
                            String score = masteryMap.get("Score");
                            if (masteryMap.get("flag") != null) {
                                dataHashMap.put("flag", masteryMap.get("flag"));
                            } else {
                                dataHashMap.put("flag", "Done");
                            }
                            if (score != null) {
                                dataHashMap.put("score", score);
                            } else {
                                dataHashMap.put("score", notyet);
                            }
                            dataHashMap.put("topicId", dataSnapshot.getKey());
                            dataHashMap.put("currentLevel", currentLevel);
                            topicIdsArrayList.add(dataHashMap);
                            try {
                                for (int i = 0; i < contentArrayList_.size(); i++) {
                                    String topicId = contentArrayList_.get(i).get("" + i).get("TopicID");
                                    for (int j = 0; j < topicIdsArrayList.size(); j++) {
                                        if (topicId.equalsIgnoreCase(topicIdsArrayList.get(j).get("topicId"))) {
                                            contentArrayList_.get(i).get("" + i).put("score", topicIdsArrayList.get(j).get("score"));
                                            contentArrayList_.get(i).get("" + i).put("streakProgress", topicIdsArrayList.get(j).get("streakProgress"));
                                            contentArrayList_.get(i).get("" + i).put("flag", topicIdsArrayList.get(j).get("flag"));
                                            contentArrayList_.get(i).get("" + i).put("mastery", topicIdsArrayList.get(j).get("mastery"));
                                            contentArrayList_.get(i).get("" + i).put("currentLevel", topicIdsArrayList.get(j).get("currentLevel"));
                                        } else {
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                selectTopicAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            ArrayList<HashMap<String, String>> topicIdsArrayList = new ArrayList<>();
                            HashMap<String, String> dataHashMap = new HashMap<>();
                            HashMap<String, String> masteryMap = (HashMap<String, String>) dataSnapshot.getValue();
                            String mastery = masteryMap.get("mastery");
                            String streakProgress = masteryMap.get("streakProgress");
                            if (streakProgress != null) {
                                dataHashMap.put("streakProgress", streakProgress);
                            } else {
                                dataHashMap.put("streakProgress", "0");
                            }
                            if (mastery != null) {
                                dataHashMap.put("mastery", mastery);
                            } else {
                                dataHashMap.put("mastery", "0");
                            }
                            String currentLevel = masteryMap.get("currentLevel");
                            String score = masteryMap.get("Score");
                            if (masteryMap.get("flag") != null) {
                                dataHashMap.put("flag", masteryMap.get("flag"));
                            } else {
                                dataHashMap.put("flag", "Done");
                            }
                            if (score != null) {
                                dataHashMap.put("score", score);
                            } else {
                                dataHashMap.put("score", notyet);
                            }
                            dataHashMap.put("topicId", dataSnapshot.getKey());
                            dataHashMap.put("currentLevel", currentLevel);
                            topicIdsArrayList.add(dataHashMap);
                            try {
                                for (int i = 0; i < contentArrayList_.size(); i++) {
                                    String topicId = contentArrayList_.get(i).get("" + i).get("TopicID");
                                    for (int j = 0; j < topicIdsArrayList.size(); j++) {
                                        if (topicId.equalsIgnoreCase(topicIdsArrayList.get(j).get("topicId"))) {
                                            contentArrayList_.get(i).get("" + i).put("score", topicIdsArrayList.get(j).get("score"));
                                            contentArrayList_.get(i).get("" + i).put("streakProgress", topicIdsArrayList.get(j).get("streakProgress"));
                                            contentArrayList_.get(i).get("" + i).put("flag", topicIdsArrayList.get(j).get("flag"));
                                            contentArrayList_.get(i).get("" + i).put("mastery", topicIdsArrayList.get(j).get("mastery"));
                                            contentArrayList_.get(i).get("" + i).put("currentLevel", topicIdsArrayList.get(j).get("currentLevel"));
                                        } else {
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                selectTopicAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            posi++;
                            String tID = contentArrayList_.get(posi).get("" + posi).get("TopicID");
                            getData(contentArrayList_, tID);

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        selectTopicAdapter.notifyDataSetChanged();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPlanDetail() throws Exception {
        global.getDatabaseReference().child(ApplicationConstants.REFERALS_SCHOLAR_PLANS).child("UsersPlans").child(Util.getUserId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < contentArrayList.size(); i++) {
                    try {
                        contentArrayList.get(i).get("" + i).put("status", "Demo");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (dataSnapshot != null) {
                        HashMap<String, String> planHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                        String status = planHashMap.get("status");
                        if (status.equalsIgnoreCase("Trial")) {
                            ApplicationConstants.STATUS = "TRIAL";
                        } else if (status.equalsIgnoreCase("Demo")) {
                            ApplicationConstants.STATUS = "DEMO";
                            for (int i = 0; i < contentArrayList.size(); i++) {
                                if (i < 4) {
                                    contentArrayList.get(i).get("" + i).put("status", "Demo");
                                } else {
                                    contentArrayList.get(i).get("" + i).put("status", "Trial");
                                }
                            }
                            selectTopicAdapter.notifyDataSetChanged();
                        } else {
                            ApplicationConstants.STATUS = "PLAN";
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

    public void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray(ApplicationConstants.SELECT_TOPIC_SCREEN);
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textViewGrade.setText(textArrayList.get(1) + " " + Util.getSelectedClassName(context));
                    textView.setText(textArrayList.get(0));
                    currentMastery = textArrayList.get(2);
                    takeTest = textArrayList.get(3);
                    start = textArrayList.get(4);
                    score = textArrayList.get(5);
                    assigned = textArrayList.get(6);
                    message1 = textArrayList.get(7);
                    message2 = textArrayList.get(8);
                    latestScore = textArrayList.get(9);
                    iDreamWork = textArrayList.get(12);
                    notyet = textArrayList.get(16);
                    try {
                        // if(Util.isOfflineMode(context)){
                        getTopicsFromDatabase();
                        //}else {

                        //}

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showDialog(context);
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child(ApplicationConstants.SELECT_TOPIC_SCREEN).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {

                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textViewGrade.setText(textArrayList.get(1) + " " + Util.getSelectedClassName(context));
                            textView.setText(textArrayList.get(0));
                            currentMastery = textArrayList.get(2);
                            takeTest = textArrayList.get(3);
                            start = textArrayList.get(4);
                            score = textArrayList.get(5);
                            assigned = textArrayList.get(6);
                            message1 = textArrayList.get(7);
                            message2 = textArrayList.get(8);
                            latestScore = textArrayList.get(9);
                            iDreamWork = textArrayList.get(12);
                            notyet = textArrayList.get(16);
                            try {
                                // getSubjects();
                                getTopicsFromDatabase();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.dismissDialog();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.dismissDialog();
                }
            });
        }

    }

    private void showDemoPlanDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_demo_plan);
        dialog.setCancelable(true);
        TextView textViewAdd = dialog.findViewById(R.id.textViewAdd);
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context, SubscriptionPlanActivity.class));
                //
                //dialog.dismiss();
            }
        });
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    private void showWelcomeDialog(String message1, String message2) throws Exception {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_welcome);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        TextView textViewHeading = dialog.findViewById(R.id.textViewHeading);
        textViewHeading.setText(iDreamWork);
        textViewMessage.setText(message1);
        TextView textViewMessage2 = dialog.findViewById(R.id.textViewMessage2);
        textViewMessage2.setVisibility(View.GONE);
        textViewMessage2.setText(message2);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewOkay.setText(message2);
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    private void getWelcomeMessages() {
        global.getDatabaseReference().child("StaticTextDB/StudentApp/3").child("Messages").child(Util.getSelectedLanguage(context)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        HashMap<String, String> welcomeHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                        String message1 = welcomeHashmap.get("message1");
                        String message2 = welcomeHashmap.get("message2");
                        showWelcomeDialog(message1, message2);
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

    private void getSubjects() throws Exception {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    isTopicLoaded = true;
                    ArrayList<String> subjectArrayList = new ArrayList<>();
                    if (dataSnapshot.getValue() != null && !TextUtils.isEmpty(dataSnapshot.getValue().toString())) {
                        for (DataSnapshot single : dataSnapshot.getChildren()) {
                            String subject = single.getKey();
                            subjectArrayList.add(subject);

                        }
                        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(context, R.layout.row_spinner, subjectArrayList);
                        subjectSpinner.setAdapter(classAdapter);
                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    String subject = subjectArrayList.get(i);
                                    try {
                                        if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
//                                            if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")||subject.equalsIgnoreCase("ಮಠ")|| subject.equalsIgnoreCase("गणित")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_math_land);
//                                            } else if (subject.equalsIgnoreCase("English")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.english_back_land);
//                                            }else if (subject.equalsIgnoreCase("Geography") ||subject.equalsIgnoreCase("भूगोल") ||subject.equalsIgnoreCase("ಭೌಗೋಳಿಕತೆ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.geography_land);
//                                            }else if (subject.equalsIgnoreCase("Economics") ||subject.equalsIgnoreCase("अर्थशास्त्र")||subject.equalsIgnoreCase("ಅರ್ಥಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.economics_land);
//                                            }else if (subject.equalsIgnoreCase("History") || subject.equalsIgnoreCase("इतिहास")||subject.equalsIgnoreCase("ಇತಿಹಾಸ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.history_land);
//                                            }else if (subject.equalsIgnoreCase("Civics") || subject.equalsIgnoreCase("नागरिकशास्र")||subject.equalsIgnoreCase("ಸಿವಿಕ್ಸ್")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.civics_land);
//                                            }else if (subject.equalsIgnoreCase("Sociology") || subject.equalsIgnoreCase("ಸಮಾಜಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.sociology_land);
//                                            }else if (subject.equalsIgnoreCase("Business Studies")||subject.equalsIgnoreCase("ವ್ಯಾಪಾರ ಅಧ್ಯಯನಗಳು")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.business_land);
//                                            } else {
//                                                reletiveParent.setBackgroundResource(R.mipmap.science_back_land);
//                                            }
                                        } else {
//                                            if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")||subject.equalsIgnoreCase("ಮಠ")|| subject.equalsIgnoreCase("गणित")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_math);
//                                            } else if (subject.equalsIgnoreCase("English")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_english);
//                                            }else if (subject.equalsIgnoreCase("Geography") ||subject.equalsIgnoreCase("भूगोल")||subject.equalsIgnoreCase("ಭೌಗೋಳಿಕತೆ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.geography_port);
//                                            }else if (subject.equalsIgnoreCase("Economics") ||subject.equalsIgnoreCase("अर्थशास्त्र")||subject.equalsIgnoreCase("ಅರ್ಥಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.economics_port);
//                                            }else if (subject.equalsIgnoreCase("History") || subject.equalsIgnoreCase("इतिहास")||subject.equalsIgnoreCase("ಇತಿಹಾಸ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.history_port);
//                                            }else if (subject.equalsIgnoreCase("Civics") || subject.equalsIgnoreCase("नागरिकशास्र")||subject.equalsIgnoreCase("ಸಿವಿಕ್ಸ್")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.civics_port);
//                                            }else if (subject.equalsIgnoreCase("Sociology") || subject.equalsIgnoreCase("ಸಮಾಜಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.sociology_port);
//                                            }else if (subject.equalsIgnoreCase("Business Studies")||subject.equalsIgnoreCase("ವ್ಯಾಪಾರ ಅಧ್ಯಯನಗಳು")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.business_port);
//                                            } else {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_science);
//                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Util.setSubject(context, subject);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {


                                                getTopics(subject);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 300);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {
                        Util.dismissDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Util.dismissDialog();
            }
        };
        global.getDatabaseReference().child(ApplicationConstants.NEW_TOPIC_DB).child(Util.getSelectedBoard(context).toUpperCase()).child(Util.getSelectedClass(context)).child(Util.getSelectedLanguagePackage(context)).child("Categories/" + posCate + "/content").addValueEventListener(valueEventListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTopicLoaded) {
                    Util.dismissDialog();
                    global.getDatabaseReference().child(ApplicationConstants.NEW_TOPIC_DB).child(Util.getSelectedBoard(context).toUpperCase()).child(Util.getSelectedClass(context)).child(Util.getSelectedLanguagePackage(context)).child("Categories/" + posCate + "/content").removeEventListener(valueEventListener);
                    Util.showInternetConnectioError(context);
                }
            }
        }, 8000);
    }

    private void getAssignedTopics(SelectTopicAdapter videoListAdapter, String faciitatorID) throws Exception {
        global.getDatabaseReference().child("Facilitator/AssignedContent").child(faciitatorID).child(Util.getUserId(context)).child(categoryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, HashMap<String, String>> assignedArraylist = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                        ArrayList<String> keysArrayList = new ArrayList<>();
                        for (String key : assignedArraylist.keySet()) {
                            keysArrayList.add(key);
                        }
                        for (int i = 0; i < keysArrayList.size(); i++) {
                            String key = keysArrayList.get(i);
                            HashMap<String, String> contentID = assignedArraylist.get(key);
                            for (int j = 0; j < contentArrayList.size(); j++) {
                                if (contentArrayList.get(j).get("" + j).get("TopicID").contains(contentID.get("id"))) {
                                    if (contentID.get("isOpened").equalsIgnoreCase("false")) {
                                        contentArrayList.get(j).get("" + j).put("isAssigned", "true");
                                        contentArrayList.get(j).get("" + j).put("FID", faciitatorID);
                                        contentArrayList.get(j).get("" + j).put("keytoDelete", key);
                                        videoListAdapter.notifyDataSetChanged();
                                    }
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

    private void getTopicsFromDatabase() {
        Util.dismissDialog();
        practiceRepository.getPracticeReports(Util.getSelectedClass(context), Util.getSelectedLanguagePackage(context), Util.getSelectedBoard(context), categoryID).observe(SelectTopicActivity.this, new Observer<List<PracticeModel>>() {
            @Override
            public void onChanged(@Nullable List<PracticeModel> topics) {
                if (topics.size() > 0) {
                    ArrayList<String> subjectArrayList = new ArrayList<>();

                    if (Util.isOfflineMode(context))
                    {

                        String filePath = ".iDream_content/offlinetab_PAL/SubjectOrder.txt";
                        JSONObject obect = Util.readJsonFile(context, filePath);
                        try {
                            JSONObject orderObject = obect.getJSONObject(Util.getSelectedBoard(context));
                            JSONObject classObject = orderObject.getJSONObject(Util.getSelectedClass(context));
                            JSONObject languageObject = classObject.getJSONObject(Util.getSelectedLanguagePackage(context));
                            JSONArray categoryOrderObject = languageObject.getJSONArray("Categories");
                            JSONObject positionOrderArray = categoryOrderObject.getJSONObject(posCate);
                            JSONArray contentOrderArray = positionOrderArray.getJSONArray("content");
                            for (int i = 0; i < contentOrderArray.length(); i++) {
                                subjectArrayList.add(String.valueOf(contentOrderArray.get(i)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.dismissDialog();
                        }


                    }else {
                        HashSet<String> set = new HashSet<>();
                        for (int i = 0; i < topics.size(); i++) {
                            String subject = topics.get(i).getSubject();
                            set.add(subject);
                        }
                        subjectArrayList.addAll(set);
                        isTopicLoaded = true;
                    }




                    if (subjectArrayList.size() > 0) {

                        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(context, R.layout.row_spinner, subjectArrayList);
                        subjectSpinner.setAdapter(classAdapter);
                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    String subject = subjectArrayList.get(i);
                                    try {
                                        if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
//                                            if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")||subject.equalsIgnoreCase("ಮಠ")|| subject.equalsIgnoreCase("गणित")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_math_land);
//                                            } else if (subject.equalsIgnoreCase("English")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.english_back_land);
//                                            }else if (subject.equalsIgnoreCase("Geography") ||subject.equalsIgnoreCase("भूगोल") ||subject.equalsIgnoreCase("ಭೌಗೋಳಿಕತೆ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.geography_land);
//                                            }else if (subject.equalsIgnoreCase("Economics") ||subject.equalsIgnoreCase("अर्थशास्त्र")||subject.equalsIgnoreCase("ಅರ್ಥಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.economics_land);
//                                            }else if (subject.equalsIgnoreCase("History") || subject.equalsIgnoreCase("इतिहास")||subject.equalsIgnoreCase("ಇತಿಹಾಸ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.history_land);
//                                            }else if (subject.equalsIgnoreCase("Civics") || subject.equalsIgnoreCase("नागरिकशास्र")||subject.equalsIgnoreCase("ಸಿವಿಕ್ಸ್")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.civics_land);
//                                            }else if (subject.equalsIgnoreCase("Sociology") || subject.equalsIgnoreCase("ಸಮಾಜಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.sociology_land);
//                                            }else if (subject.equalsIgnoreCase("Business Studies")||subject.equalsIgnoreCase("ವ್ಯಾಪಾರ ಅಧ್ಯಯನಗಳು")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.business_land);
//                                            } else {
//                                                reletiveParent.setBackgroundResource(R.mipmap.science_back_land);
//                                            }
                                        } else {
//                                            if (subject.equalsIgnoreCase("Math") || subject.equalsIgnoreCase("Maths")||subject.equalsIgnoreCase("ಮಠ")|| subject.equalsIgnoreCase("गणित")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_math);
//                                            } else if (subject.equalsIgnoreCase("English")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_english);
//                                            }else if (subject.equalsIgnoreCase("Geography") ||subject.equalsIgnoreCase("भूगोल")||subject.equalsIgnoreCase("ಭೌಗೋಳಿಕತೆ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.geography_port);
//                                            }else if (subject.equalsIgnoreCase("Economics") ||subject.equalsIgnoreCase("अर्थशास्त्र")||subject.equalsIgnoreCase("ಅರ್ಥಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.economics_port);
//                                            }else if (subject.equalsIgnoreCase("History") || subject.equalsIgnoreCase("इतिहास")||subject.equalsIgnoreCase("ಇತಿಹಾಸ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.history_port);
//                                            }else if (subject.equalsIgnoreCase("Civics") || subject.equalsIgnoreCase("नागरिकशास्र")||subject.equalsIgnoreCase("ಸಿವಿಕ್ಸ್")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.civics_port);
//                                            }else if (subject.equalsIgnoreCase("Sociology") || subject.equalsIgnoreCase("ಸಮಾಜಶಾಸ್ತ್ರ")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.sociology_port);
//                                            }else if (subject.equalsIgnoreCase("Business Studies")||subject.equalsIgnoreCase("ವ್ಯಾಪಾರ ಅಧ್ಯಯನಗಳು")) {
//                                                reletiveParent.setBackgroundResource(R.mipmap.business_port);
//                                            } else {
//                                                reletiveParent.setBackgroundResource(R.mipmap.bg_science);
//                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Util.setSubject(context, subject);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //getTopics(subject);
                                                contentArrayList = new ArrayList<>();
                                                HashMap<String, HashMap<String, String>> modelHashMap = null;
                                                String selectedSubject = (String) subjectSpinner.getItemAtPosition(i);
                                                int pos = 0;
                                                for (int k = 0; k < topics.size(); k++) {

                                                    String subject = topics.get(k).getSubject();
                                                    if (selectedSubject.equalsIgnoreCase(subject)) {

                                                        HashMap<String, String> innerTopicNode = new HashMap<>();
                                                        innerTopicNode.put("Display", topics.get(k).getDisplay());
                                                        innerTopicNode.put("Foundational_Topic_ID", topics.get(k).getFoundational_Topic_ID());
                                                        innerTopicNode.put("IsModelTestPaper", topics.get(k).getIsModelTestPaper());
                                                        innerTopicNode.put("Levels", topics.get(k).getLevels());
                                                        innerTopicNode.put("StreakCount", topics.get(k).getStreakCount());
                                                        innerTopicNode.put("TName", topics.get(k).getTName());
                                                        innerTopicNode.put("TName_alt", topics.get(k).getTName_alt());
                                                        innerTopicNode.put("Time", topics.get(k).getTime());
                                                        innerTopicNode.put("TopicID", topics.get(k).getTopicID());
                                                        innerTopicNode.put("incorrectStreak", topics.get(k).getIncorrectStreak());
                                                        innerTopicNode.put("isAlternateLanguageAvailable", topics.get(k).getIsAlternateLanguageAvailable());
                                                        innerTopicNode.put("mastery", "0");
                                                        innerTopicNode.put("enabled", innerTopicNode.get("Display"));
                                                        innerTopicNode.put("currentLevel", "1");
                                                        innerTopicNode.put("flag", "Done");
                                                        innerTopicNode.put("score", notyet);
                                                        innerTopicNode.put("isAssigned", "False");
                                                        innerTopicNode.put("FID", "not");
                                                        innerTopicNode.put("keytoDelete", "not");
                                                        modelHashMap = new HashMap<>();
                                                        modelHashMap.put(pos + "", innerTopicNode);
                                                        pos++;
                                                        contentArrayList.add(modelHashMap);
                                                    }
                                                }
                                                askforhelpImageView.setVisibility(View.GONE);
//                                                try {
                                                selectTopicAdapter = new SelectTopicAdapter(context, contentArrayList, currentMastery, takeTest, start, score, assigned, latestScore);
//                                                    ArrayList<HashMap<String, String>> batchArrayList = global.getFacilitatorList();
//                                                    if (batchArrayList != null) {
//                                                        if (batchArrayList.size() > 0) {
//                                                            String sClass = Util.getSelectedClass(context);
//                                                            String sBoard = Util.getSelectedBoard(context);
//                                                            String sLangauge = Util.getSelectedLanguagePackage(context);
//                                                            for (int k = 0; k < batchArrayList.size(); k++) {
//                                                                String name = batchArrayList.get(k).get("name");
//                                                                String subject_ = batchArrayList.get(k).get("subject");
//                                                                if (name.contains(sBoard)) {
//                                                                    if (name.contains(sClass)) {
//                                                                        if (name.contains(sLangauge)) {
////                                                                if (subject_.contains(subject)) {
//                                                                            String facilitatorId = batchArrayList.get(k).get("facilitatorId");
//                                                                            getAssignedTopics(selectTopicAdapter, facilitatorId);
////                                                                }
//
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//
//                                                        }
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }

                                                shimmerFrameLayout.stopShimmerAnimation();
                                                LinearShimmer.setVisibility(View.GONE);
                                                mRecyclerView.setAdapter(selectTopicAdapter);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                itemClickListner(contentArrayList);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                getPlanDetail();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                posi = 0;
                                                contentArrayList_ = new ArrayList<>();
//                                                for (int i = 0; i < contentArrayList.size(); i++) {
//                                                    String topicID = contentArrayList.get(i).get("" + i).get("TopicID");
//                                                    ArrayList<String> storedArrayList = Util.getTopics(context);
//
//                                                    HashMap<String, HashMap<String, String>> data_ = new HashMap<>();
//
//                                                    if (storedArrayList.contains(topicID)) {
//                                                        HashMap<String, HashMap<String, String>> data = contentArrayList.get(i);
//                                                        HashMap<String, String> dataInner = new HashMap<>();
//                                                        for (String key : data.keySet()) {
//                                                            dataInner = data.get(key);
//                                                            data_.put("" + i, dataInner);
//                                                            contentArrayList_.add(data_);
//
//                                                        }
//
////                                                        for (int k = 0; k < storedArrayList.size(); k++) {
////
////                                                        }
//                                                    }
//                                                }

                                                int position=0;
                                                for (int i = 0; i < contentArrayList.size(); i++) {
                                                    String topicID = contentArrayList.get(i).get("" + i).get("TopicID");
                                                    ArrayList<String> storedArrayList = Util.getTopics(context);
                                                    if (storedArrayList.contains(topicID)) {
                                                        HashMap<String, HashMap<String, String>> modelHashMap_ = new HashMap<>();
                                                        HashMap<String, String> innerTopicNode = contentArrayList.get(i).get(""+i);
                                                        modelHashMap_.put(""+position, innerTopicNode);
                                                        contentArrayList_.add(modelHashMap_);
                                                        position++;
                                                    }
                                                }

                                                if (contentArrayList_.size() > 0) {
                                                    String tID = contentArrayList_.get(0).get("" + 0).get("TopicID");
                                                    getData(contentArrayList_, tID);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        }

                                    }, 300);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {
                        Util.dismissDialog();
                    }


                } else {
                    Util.dismissdataDialog();

                }


            }
        });

    }
    @Override
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }
}
