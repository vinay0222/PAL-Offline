package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ScoreModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoListActivity extends AppCompatActivity {
    private Global global;
    private Context context;
    private RecyclerView mRecyclerView;
    public DatabaseReference databaseReference;
    private FirebaseDatabase database = null;
    private String topicID;
    private String subject;
    private String sClass;
    private String level;
    private Button buttonPractice;
    private int videoCount = 0;
    private int videoPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_video_list);
        assignIds();

    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        startTime = System.currentTimeMillis();
        mRecyclerView = findViewById(R.id.mRecyclerView);
        buttonPractice = findViewById(R.id.buttonPractice);
        buttonPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSameLevelPractice();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference();
        mRecyclerView.setLayoutManager(manager);
        topicID = getIntent().getStringExtra("topicID");
        subject = getIntent().getStringExtra("subject");
        sClass = getIntent().getStringExtra("sClass");
        level = getIntent().getStringExtra("level");

        try {
            getVideoCount();
            getTime();
            if (level.equalsIgnoreCase("all")) {
                getCurrentLevelVideos();
            } else {
                getSelectedLevelVideos();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getSameLevelPractice() {
        databaseReference.child("NewTopicDB").child("H_E_B").child(sClass).child("Hindi").child("Categories/practice/content").child(Util.getSubject(context)).child("Topics").child(topicID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, String> detailHashMap = (HashMap<String, String>) snapshot.getValue();
                        String foundational_class = detailHashMap.get("foundational_class");
                        String streak = detailHashMap.get("StreakCount");
                        String incorrectStreak = detailHashMap.get("incorrectStreak");
                        String Foundational_Topic_ID = detailHashMap.get("Foundational_Topic_ID");

                        Util.setTopicID(context, topicID);
                        Util.setLevel(context, Integer.parseInt("1"));
                        String studentClass = sClass;
                        String streakProgress = "0";
                        global.setProgress(0);
                        startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "same").putExtra("seniorClass", "null").putExtra("seniorTopicID", "null"));
                        
                        finish();
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

    private void getSelectedLevelVideos() {

        databaseReference.child("NewTopicDB").child("H_E_B").child(sClass).child("Hindi").child("Categories/videoLessons/content").child(subject).child("Topics").child(topicID).child(level).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, HashMap<String, String>> innerHashmap = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        videoArrayList = new ArrayList<>();
                        int p = 0;
                        for (String key : innerHashmap.keySet()) {
                            HashMap<String, String> hasMap = innerHashmap.get(key);
                            if (p == 0) {
                                hasMap.put("visible", "yes");
                            } else {
                                hasMap.put("visible", "no");
                            }
                            p++;
                            hasMap.put("level", "" + level);
                            videoArrayList.add(hasMap);
                        }


//                        VideoListAdapter videoListAdapter = new VideoListAdapter(context, videoArrayList);
//                        mRecyclerView.setAdapter(videoListAdapter);
//                        videoListAdapter.SetOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                try {
//                                    videoPosition = position;
//                                    videoCount++;
//                                    String completeUrl = videoArrayList.get(position).get("onlineLink");
//                                    String urlArray[] = completeUrl.split("/");
//                                    String url = urlArray[urlArray.length - 1];
//                                    startActivityForResult(new Intent(context, PalVideoPlayerActivity.class).putExtra("type", "digonostic").putExtra("url", url), 123);
//                                    
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
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

    private ArrayList<HashMap<String, String>> videoArrayList;

    private void getCurrentLevelVideos() {

        databaseReference.child("NewTopicDB").child("H_E_B").child(sClass).child("Hindi").child("Categories/videoLessons/content").child(subject).child("Topics").child(topicID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        ArrayList<HashMap<String, HashMap<String, String>>> topicsArrayList = (ArrayList<HashMap<String, HashMap<String, String>>>) snapshot.getValue();
                        videoArrayList = new ArrayList<>();
                        for (int i = 1; i < topicsArrayList.size(); i++) {
                            HashMap<String, HashMap<String, String>> innerHashmap = topicsArrayList.get(i);
                            int p = 0;
                            for (String key : innerHashmap.keySet()) {
                                HashMap<String, String> hasMap = innerHashmap.get(key);
                                if (p == 0) {
                                    hasMap.put("visible", "yes");
                                } else {
                                    hasMap.put("visible", "no");
                                }
                                p++;
                                hasMap.put("level", "" + i);

                                videoArrayList.add(hasMap);
                            }
                        }

//                        VideoListAdapter videoListAdapter = new VideoListAdapter(context, videoArrayList);
//                        mRecyclerView.setAdapter(videoListAdapter);
//                        videoListAdapter.SetOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                try {
//                                    videoCount++;
//                                    videoPosition = position;
//                                    videoName = videoArrayList.get(position).get("name");
//                                    String completeUrl = videoArrayList.get(position).get("onlineLink");
//                                    String urlArray[] = completeUrl.split("/");
//                                    String url = urlArray[urlArray.length - 1];
//                                    startActivityForResult(new Intent(context, PalVideoPlayerActivity.class).putExtra("type", "digonostic").putExtra("url", url), 123);
//                                    
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
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

    private String videoName;
    private long startTime = 0L;
    private long endTime = 0L;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123) {
            String date = Util.getCurrentDateWithDifferentFormat();
            ScoreModel scoreModel = new ScoreModel();
            String time = data.getStringExtra("time");
            String totalTime = data.getStringExtra("totalTime");
            scoreModel.setTopicName(Util.getTopicNameAlt(context));
            scoreModel.setTime(time + "");
            scoreModel.setTotalTime(totalTime + "");
            scoreModel.setVideoName(videoName);
            endTime = System.currentTimeMillis();
            long timeTosync = endTime - startTime;
            videoNumber = videoNumber + 1;
            timeTosync = timeTosync + timeget;
//            databaseReference.child("pal").child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSubject(context)).child("time").setValue(timeTosync);
//            databaseReference.child("pal").child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(date).child("count").child("video").setValue(videoNumber);


            databaseReference.child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child("H_E_B").child(sClass).child("time_spent").child(date).child(Util.getSubject(context)).setValue(timeTosync);
            databaseReference.child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child("H_E_B").child(sClass).child("count").child(date).child("video_lessons").setValue(videoNumber);



            databaseReference.child(ApplicationConstants.REPORTS).child("date_wise").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child(date).child("video_lessons").child("" + System.currentTimeMillis()).setValue(scoreModel);
            databaseReference.child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child(date).child("video_lessons").child(topicID).child("" + System.currentTimeMillis()).setValue(scoreModel);

            openSuccessDialog();
        }
    }


    private long timeget;

    private void getTime() {
        databaseReference.child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSubject(context)).child("time").addValueEventListener(new ValueEventListener() {
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


    private long videoNumber;

    private void getVideoCount() throws Exception {
        String date = Util.getCurrentDateWithDifferentFormat();
        databaseReference.child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child("H_E_B").child(sClass).child(Util.getSubject(context)).child("count").child(date).child("video_lessons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        videoNumber = (long) snapshot.getValue();
                    } else {
                        videoNumber = 0;
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


    private void openSuccessDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pal_sucess_dialog);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.textView);
        String mess;
        Util.showToast(context, "videoCount : " + videoCount);
        if (videoCount == 1) {
            mess = "बहुत बढ़िया। आप अच्छा कर रहे हैं। आगे बढ़ने के लिए या तो आप इसी टॉपिक पर और वीडियो देख सकते हैं या फिर आप अभ्यास के प्रश्नो को हल कर सकते हैं।  उचित बटन दबा कर आगे बढ़ें।";
        } else if (videoPosition == videoArrayList.size() - 1) {
            mess = "शाबाश।  आपने इस टॉपिक पर उपलब्ध सभी वीडियो देख लिए है।  अब आप " + Util.getTopicNameAlt(context) + " पर अभ्यास के प्रश्न करने के लिए तैयार है।  नीचे दिए बटन पर क्लिक करके इस टॉपिक पर अभ्यास कर अपनी महारत बनाएं।";
        } else {
            mess = "क्या अब आप " + Util.getTopicNameAlt(context) + " के अभ्यास के प्रश्नों को हल करना चाहेंगे ।  यदि हाँ तो नीचे उचित बटन पे क्लिक करके अपना अभ्यास शुरू करे।  अगर आप आगे के वीडियो देखना चाहते हैं तो \"अगला वीडियो\" बटन पे क्लिक करके उसे देखें और " + Util.getTopicNameAlt(context) + " पर अपनी समझ को बेहतर करने का प्रयास ज़ारी रखें। ";
        }
        textView.setText(mess);
        TextView textViewPractice = dialog.findViewById(R.id.textViewPractice);
        textViewPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getSameLevelPractice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        TextView textViewContinoue = dialog.findViewById(R.id.textViewContinoue);
        if (videoPosition == videoArrayList.size() - 1) {
            textViewContinoue.setVisibility(View.GONE);
        } else {
            textViewContinoue.setText("अगला वीडियो");
            textViewContinoue.setVisibility(View.VISIBLE);
            textViewContinoue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (videoPosition == videoArrayList.size() - 1) {

                    } else {
                        videoPosition++;
                        try {
                            videoCount++;
                            String completeUrl = videoArrayList.get(videoPosition).get("onlineLink");
                            String[] urlArray = completeUrl.split("/");
                            String url = urlArray[urlArray.length - 1];
                            startActivityForResult(new Intent(context, PalVideoPlayerActivity.class).putExtra("type", "digonostic").putExtra("url", url), 123);
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }


        TextView TxtExit = dialog.findViewById(R.id.TxtExit);
        TxtExit.setText("Exit");

        TxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }


}
