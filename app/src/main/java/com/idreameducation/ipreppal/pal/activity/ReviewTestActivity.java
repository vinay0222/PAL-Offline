package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.ReviewModelPaperAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 30/12/17.
 */

public class ReviewTestActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Context context;
    private Global global;
    private TextView textViewNext;
    private TextView btnChangeLanguageEnglish;
    private TextView btnChangeLanguage;
    private ReviewModelPaperAdapter reviewModelPaperAdapter;
    private int position = 1;
    private TextView textViewToatal;
    private TextView textViewMarkes;
    private TextView textViewTime;
    private LinearLayoutManager manager;
    private String correct;
    private String next;
    private String goToTop;
    private String incorrect;
    private String correctAnswer;
    private String incorrectAnswer;
    private String unattempted;
    private String yourAnswer;
    private String exit;
    private String continue_;

    private String[] textString;
    private String message;
    private String type;
    private String date;
    private String topicID;

    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private void confirmExit() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_exit_model);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(message);
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        textViewContinoue.setText(continue_);
        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
        TxtExit.setText(exit);
        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        textViewContinoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                position = 0;
                mRecyclerView.smoothScrollToPosition(position);
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_review_modeltestpaper);
        Util.handleNotch(this);
        assignIds();
        listners();
    }

    private void listners() {
        findViewById(R.id.btnChangeLanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (global.isFlagEnabled()) {
                        btnChangeLanguage.setBackgroundResource(R.drawable.correct_answer);
                        btnChangeLanguageEnglish.setBackgroundResource(R.drawable.signup);
                        btnChangeLanguageEnglish.setTextColor(Color.parseColor("#000000"));
                        btnChangeLanguage.setTextColor(Color.parseColor("#FFFFFF"));

                        ReviewModelPaperAdapter.selectedLanguge = Util.getSelectedLanguagePackage(context);
                        reviewModelPaperAdapter.updateAdapter();
                    } else {
                        Util.showToast(context, "Coming Soon");
                        btnChangeLanguage.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnChangeLanguage.setEnabled(true);
                            }
                        }, 2000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        findViewById(R.id.btnChangeLanguageEnglish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
                    btnChangeLanguage.setBackgroundResource(R.drawable.signup);
                    btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
                    btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
                    ReviewModelPaperAdapter.selectedLanguge = "English";
                    reviewModelPaperAdapter.updateAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.textViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    position = manager.findFirstVisibleItemPosition();
                    if (position == reviewModelPaperAdapter.getItemCount() - 1) {
                        confirmExit();
                    } else {
                        position++;
                        mRecyclerView.smoothScrollToPosition(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();

                }
            }
        });

    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        global.sendData("ReviewModelTest", this.getClass().getName());
        mRecyclerView = findViewById(R.id.recyclerView);
        textViewTime = findViewById(R.id.textViewTime);
        textViewNext = findViewById(R.id.textViewNext);
        textViewMarkes = findViewById(R.id.textViewMarkes);
        btnChangeLanguageEnglish = findViewById(R.id.btnChangeLanguageEnglish);
        textViewToatal = findViewById(R.id.textViewToatal);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnChangeLanguage.setText(Util.getSelectedLanguagePackage(context));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        topicID = getIntent().getStringExtra("topicID");

        // use a linear layout manager
        manager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(manager);
        if (!global.isFlagEnabled()) {
            btnChangeLanguageEnglish.setBackgroundResource(R.drawable.correct_answer);
            btnChangeLanguage.setBackgroundResource(R.drawable.signup);

            btnChangeLanguage.setTextColor(Color.parseColor("#000000"));
            btnChangeLanguageEnglish.setTextColor(Color.parseColor("#FFFFFF"));
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        date = getIntent().getStringExtra("date");
        type = getIntent().getStringExtra("type");
        String key = getIntent().getStringExtra("key");
        try {
            setStaticText(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            getLastSubmitedTestPaper();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Util.dismissDialog();
//        }
    }



    private final String sClass = "9";

    private void getDetailsLastSubmitedTestPaper(String key) throws Exception {
        DatabaseReference aa;

        if (topicID == null) {
            aa = databaseReference.child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child(date).child(type).child(key).child("detail_review");
        } else {
            aa = databaseReference.child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child(date).child(type).child(topicID).child(key).child("detail_review");
        }
        aa.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Util.dismissDialog();
                    ArrayList<HashMap<String, String>> questionsArrayList = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                    if (questionsArrayList.size() > 0) {
                        ReviewModelPaperAdapter.selectedLanguge = Util.getSelectedLanguagePackage(context);
                        reviewModelPaperAdapter = new ReviewModelPaperAdapter(context, questionsArrayList, textString);
                        mRecyclerView.setAdapter(reviewModelPaperAdapter);
                        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                int totalItemCount = layoutManager.getItemCount();
                                int lastVisible = layoutManager.findLastVisibleItemPosition();
                                boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
                                if (totalItemCount > 0 && endHasBeenReached) {
                                    position = 0;
                                    textViewNext.setText(exit);
                                } else {
                                    position++;
                                    textViewNext.setText(next);
                                }
                            }
                        });
                    } else {
                        //TODO Implement oops screen
                    }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    private void setStaticText(String key) {
        if (Util.isOfflineMode(context)) {

            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("Review Test Screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textString = new String[7];
                  //  textViewToatal.setText(textArrayList.get(0) + " : " + scoresHashmap.get("questionsAttempted") + "/" + scoresHashmap.get("totalQuestions"));
                   // textViewMarkes.setText(textArrayList.get(1) + " : " + scoresHashmap.get("scores") + "/" + scoresHashmap.get("totalScores"));
                    next = textArrayList.get(2);
                    textViewNext.setText(textArrayList.get(2));
                    correct = textArrayList.get(3);
                    incorrect = textArrayList.get(4);
                    unattempted = textArrayList.get(5);
                    goToTop = textArrayList.get(6);
                    yourAnswer = textArrayList.get(7);
                    correct = textArrayList.get(8);
                    incorrectAnswer = textArrayList.get(9);
                    correctAnswer = textArrayList.get(10);
                    exit = textArrayList.get(11);
                    continue_ = textArrayList.get(13);
                    message = textArrayList.get(15);
                    textString[0] = correct;
                    textString[1] = unattempted;
                    textString[2] = yourAnswer;
                    textString[3] = incorrectAnswer;
                    textString[4] = correctAnswer;
                    textString[5] = textArrayList.get(16);
                    textString[6] = textArrayList.get(17);
                    getDetailsLastSubmitedTestPaper(key);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Review Test Screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textString = new String[7];
                           // textViewToatal.setText(textArrayList.get(0) + " : " + scoresHashmap.get("questionsAttempted") + "/" + scoresHashmap.get("totalQuestions"));
                           // textViewMarkes.setText(textArrayList.get(1) + " : " + scoresHashmap.get("scores") + "/" + scoresHashmap.get("totalScores"));
                            next = textArrayList.get(2);
                            textViewNext.setText(textArrayList.get(2));
                            correct = textArrayList.get(3);
                            incorrect = textArrayList.get(4);
                            unattempted = textArrayList.get(5);
                            goToTop = textArrayList.get(6);
                            yourAnswer = textArrayList.get(7);
                            correct = textArrayList.get(8);
                            incorrectAnswer = textArrayList.get(9);
                            correctAnswer = textArrayList.get(10);
                            exit = textArrayList.get(11);
                            continue_ = textArrayList.get(13);
                            message = textArrayList.get(15);
                            textString[0] = correct;
                            textString[1] = unattempted;
                            textString[2] = yourAnswer;
                            textString[3] = incorrectAnswer;
                            textString[4] = correctAnswer;
                            textString[5] = textArrayList.get(16);
                            textString[6] = textArrayList.get(17);
                            getDetailsLastSubmitedTestPaper(key);
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
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
    }
}
