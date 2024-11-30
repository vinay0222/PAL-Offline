package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.NotificationAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anurag on 26-Oct-17.
 */

public class NotificationActivity extends AppCompatActivity {
    public static NotificationActivity activity;
    /*Dec Widgets*/
    private RecyclerView mRecyclerView;
    private Context context;
    private String readMore;
    private TextView textView;
    private Global global;
    private String okay;
    private String assign;
    private String chat;
    private String message;
    private String notAvailable;
    private TextView textViewWelcome;
    private TextView textViewChat;
    private TextView textViewAssigned;
    //*****                   END                    *****//
    private boolean isDataLoaded = false;
    private LinearLayoutManager manager;
    private TextView textViewEmptyMessage;

    private String assignedMessage;
    private String chatMessage;
    private LinearLayout linearOptions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        /* Call hookUp function */
        assignIds();

    }

    /*Init values , widgets , strings etc...*/
    private void assignIds() {
        context = this;
        activity = this;
        global = (Global) getApplicationContext();
        textView = findViewById(R.id.textView);
        linearOptions = findViewById(R.id.linearOptions);
        textViewEmptyMessage = findViewById(R.id.textViewEmptyMessage);
        setStaticText();
        global.sendData("Messages", this.getClass().getName());
        try {
            Util.setNotificationCount(context, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Util.setNotificationCount(context, 0);
        mRecyclerView = findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        manager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(manager);
//

        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewChat = findViewById(R.id.textViewChat);
        textViewAssigned = findViewById(R.id.textViewAssigned);
        try {

            if(Util.getNotificationType(context)!=null){

                int type =0;
                String notificationType = Util.getNotificationType(context);
                if(notificationType.equalsIgnoreCase("AssignTopic")){
                    textViewWelcome.setBackgroundResource(R.drawable.one_side_right_white);
                    textViewAssigned.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    textViewChat.setBackgroundResource(R.drawable.one_side_radius_white);
                    textViewWelcome.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.white));
                    textViewChat.setTextColor(context.getResources().getColor(android.R.color.black));
                    type = 3;
                }else if(notificationType.equalsIgnoreCase("Message")) {
                    textViewWelcome.setBackgroundResource(R.drawable.one_side_right_white);
                    textViewAssigned.setBackgroundColor(context.getResources().getColor(R.color.white));
                    textViewChat.setBackgroundResource(R.drawable.one_side_right_blue);

                    textViewWelcome.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewChat.setTextColor(context.getResources().getColor(android.R.color.white));
                    type = 2;
                }else {
                    type  =1;
                }
                Util.setNotificationType(context , null);
                getNotifications(type);
            }else {
                textViewWelcome.setBackgroundResource(R.drawable.one_side_radius_blue);
                textViewAssigned.setBackgroundResource(R.drawable.one_side_radius_blue);
                textViewChat.setBackgroundResource(R.drawable.one_side_radius_white);
                textViewWelcome.setTextColor(context.getResources().getColor(R.color.white));
                textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.black));
                textViewChat.setTextColor(context.getResources().getColor(android.R.color.black));


                getNotifications(3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    textViewWelcome.setBackgroundResource(R.drawable.one_side_radius_blue);
                    textViewAssigned.setBackgroundColor(context.getResources().getColor(R.color.white));
                    textViewChat.setBackgroundResource(R.drawable.one_side_radius_white);

                    textViewWelcome.setTextColor(context.getResources().getColor(R.color.white));
                    textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewChat.setTextColor(context.getResources().getColor(android.R.color.black));

                    getNotifications(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        textViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    textViewWelcome.setBackgroundResource(R.drawable.one_side_right_white);
                    textViewAssigned.setBackgroundColor(context.getResources().getColor(R.color.white));
                    textViewChat.setBackgroundResource(R.drawable.one_side_right_blue);

                    textViewWelcome.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewChat.setTextColor(context.getResources().getColor(android.R.color.white));
                    getNotifications(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        textViewAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    textViewWelcome.setBackgroundResource(R.drawable.one_side_right_white);
                    textViewAssigned.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    textViewChat.setBackgroundResource(R.drawable.one_side_radius_white);
                    textViewWelcome.setTextColor(context.getResources().getColor(android.R.color.black));
                    textViewAssigned.setTextColor(context.getResources().getColor(android.R.color.white));
                    textViewChat.setTextColor(context.getResources().getColor(android.R.color.black));
                    getNotifications(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        try {
            if(global.getFacilitatorList().size()>0){
                linearOptions.setVisibility(View.VISIBLE);
            }else {
                linearOptions.setVisibility(View.GONE);
            }

            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    public void showNotificationInDetail(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_detail_notification);
        TextView textViewOkay = dialog.findViewById(R.id.textViewOkay);
        textViewOkay.setText(okay);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        textViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    public void getNotifications(int type) throws Exception {
        if (Util.isNetworkAvailable(context))
        {
            Util.showDialog(context);
        }

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<HashMap<String, String>> notificationArrayList = new ArrayList<>();
                final ArrayList<HashMap<String, String>> notificationAssignedArrayList = new ArrayList<>();
                final ArrayList<HashMap<String, String>> notificationChatArrayList = new ArrayList<>();
                final ArrayList<HashMap<String, String>> notificationChatArrayList_ = new ArrayList<>();

                try {
                    Util.dismissDialog();
                    isDataLoaded = true;
                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot single : dataSnapshot.getChildren()) {
                                String key = single.getKey();
                                HashMap<String, String> notificationHashmap = (HashMap<String, String>) single.getValue();
                                if (key.contains("welcome")) {
                                    notificationArrayList.add(notificationHashmap);
                                } else {
                                    if (!notificationHashmap.get("replyable").equalsIgnoreCase("Showable")) {
                                        notificationHashmap.put("isClicked","false");
                                        notificationAssignedArrayList.add(notificationHashmap);
                                    } else {
                                        try {
                                            notificationChatArrayList.add(notificationHashmap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            if (type == 1) {
                                onItemClick(notificationArrayList, type);
                            } else if (type == 2) {
                                Set<String> ids = new HashSet<>();
                                for (int i = notificationChatArrayList.size() - 1; i >= 0; i--) {
                                    if (!ids.contains(notificationChatArrayList.get(i).get("senderId"))) {
                                        ids.add(notificationChatArrayList.get(i).get("senderId"));
                                        notificationChatArrayList_.add(notificationChatArrayList.get(i));
                                    }
                                }
                                onItemClick(notificationChatArrayList_, type);
                            } else if (type == 3) {
                                manager.setReverseLayout(true);
                                onItemClick(notificationAssignedArrayList, type);
                            }
                        } else {
                            Util.showToast(context, notAvailable);
                        }
                    } else {
                        Util.showToast(context, notAvailable);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Util.dismissDialog();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("Student").child(Util.getUserId(context)).addValueEventListener(valueEventListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDataLoaded) {
                    Util.dismissDialog();
                    global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("Student").child(Util.getUserId(context)).removeEventListener(valueEventListener);
                    Util.showInternetConnectioError(context);
                }
            }
        }, 8000);
    }


    private void onItemClick(ArrayList<HashMap<String, String>> notificationArrayList, int type) {
        try {

            if (notificationArrayList.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                textViewEmptyMessage.setVisibility(View.GONE);
                NotificationAdapter notificationAdapter = new NotificationAdapter(context, notificationArrayList, readMore, assign, chat, message);
                mRecyclerView.setAdapter(notificationAdapter);
                try {
                    if (type == 3) {
                        mRecyclerView.scrollToPosition(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notificationAdapter.SetOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        global.setUserNameForChat(notificationArrayList.get(position).get("sender"));
                        if (notificationArrayList.get(position).get("replyable").equalsIgnoreCase("Not Showable")) {
                            ((NotificationActivity) context).showNotificationInDetail(notificationArrayList.get(position).get("message"));
                        } else if (notificationArrayList.get(position).get("replyable").equalsIgnoreCase("Showable")) {
                            context.startActivity(new Intent(context, ChatActivity.class).putExtra("FacilitatorId", notificationArrayList.get(position).get("senderId")));
                            
                        } else {
                            if (!notificationArrayList.get(position).get("board").equalsIgnoreCase(Util.getSelectedBoard(context))) {
                                Util.showToast(context, "Please change your board to " + notificationArrayList.get(position).get("board"));
                                return;
                            }
                            if (!Util.getSelectedLanguagePackage(context).equalsIgnoreCase(notificationArrayList.get(position).get("language"))) {
                                Util.showToast(context, "Please change your Language to "+notificationArrayList.get(position).get("language"));
                                return;
                            }
                            if (notificationArrayList.get(position).get("category") != null) {
                                notificationArrayList.get(position).put("isClicked","true");
                                if (notificationArrayList.get(position).get("category").contains("Book") || notificationArrayList.get(position).get("category").contains("पुस्तके आणि कथा")) {
                                    String subject = notificationArrayList.get(position).get("subject");
                                    Util.setSubject(context, subject);
                                   // context.startActivity(new Intent(context, BooksActivity.class).putExtra("positiona", position).putExtra("name", notificationArrayList.get(position).get("category")).putExtra("categoryID", "books"));
                                    
                                } else if (notificationArrayList.get(position).get("category").contains("STEM प्रकल्प") || notificationArrayList.get(position).get("category").contains("STEM Projects")) {
                                    global.setEncryptedContent(false );
//                                    startActivity(new Intent(context, YoutubeDialogActivity.class).putExtra("name", notificationArrayList.get(position).get("message").split(":")[1]).putExtra("url", notificationArrayList.get(position).get("link")).putExtra("subject", notificationArrayList.get(position).get("subject")).putExtra("category", notificationArrayList.get(position).get("category")).putExtra("sClass", notificationArrayList.get(position).get("class")));
                                    //context.startActivity(new Intent(context, VideoActivity.class).putExtra("positiona", position).putExtra("name", notificationArrayList.get(position).get("category")).putExtra("categoryID", "activityVideos"));

                                    
                                } else if (notificationArrayList.get(position).get("category").contains("Video Lessons") || notificationArrayList.get(position).get("category").contains("व्हिडिओ धडे")) {
                                    global.setEncryptedContent(true);
                                   // context.startActivity(new Intent(context, VideoActivity.class).putExtra("positiona", position).putExtra("name", notificationArrayList.get(position).get("category")).putExtra("categoryID", "videoLessons"));
//                                    ((NotificationActivity) context).animateActivity();
//                                    startActivity(new Intent(context, VideoviewActivity.class).putExtra("name", notificationArrayList.get(position).get("message").split(":")[1]).putExtra("code", notificationArrayList.get(position).get("link")).putExtra("subject", notificationArrayList.get(position).get("subject")).putExtra("category", notificationArrayList.get(position).get("category")).putExtra("sClass", notificationArrayList.get(position).get("class")));
                                    
                                }else if (notificationArrayList.get(position).get("category").contains("अभ्यासक्रम पुस्तके") || notificationArrayList.get(position).get("category").contains("Syllabus Books")|| notificationArrayList.get(position).get("category").contains("NCERT Books")) {
                                    String subject = notificationArrayList.get(position).get("subject");
                                    Util.setSubject(context, subject);
                                  //  context.startActivity(new Intent(context, BooksActivity.class).putExtra("positiona", position).putExtra("name", notificationArrayList.get(position).get("category")).putExtra("categoryID", "books_ncert"));
                                    
                                }
                            } else {
                                Util.setTopicNameAlt(context, notificationArrayList.get(position).get("topicName"));
                                Util.setTopicID(context, notificationArrayList.get(position).get("id"));
                                String subject = notificationArrayList.get(position).get("subject");
                                Util.setSubject(context, subject);
                                if (notificationArrayList.get(position).get("isTest").equalsIgnoreCase("true")) {
                               //     startActivity(new Intent(context, ModelPaperActivity.class).putExtra("Time", notificationArrayList.get(position).get("time")));
                                } else {
                                    global.setLevelNo(Integer.parseInt(notificationArrayList.get(position).get("level")));
                                    Util.setLevel(context, 1);
                                    global.setFoundationalTopicId(ApplicationConstants.NOT_AVAILABLE);
                                    global.setProgress(0);
                                   // startActivity(new Intent(context, QuestionActivity.class).putExtra("sClass", notificationArrayList.get(position).get("class")).putExtra("streakProgress", "0").putExtra("streak", notificationArrayList.get(position).get("streak")).putExtra("incorrectStreak", notificationArrayList.get(position).get("incorrectStreak")));
                                    
                                }
                            }
                        }
                    }
                });
            } else {
                mRecyclerView.setVisibility(View.GONE);
                textViewEmptyMessage.setVisibility(View.VISIBLE);
                if (type == 2) {
                    textViewEmptyMessage.setText(chatMessage);
                } else if (type == 3) {
                    textViewEmptyMessage.setText(assignedMessage);
                }

            }
        }catch(Exception e){
                e.printStackTrace();
            }

    }

    private void setStaticText() {

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("Notification screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if(textArrayList.size()>0){
                    textView.setText(textArrayList.get(0));
                    readMore = textArrayList.get(1);
                    okay = textArrayList.get(2);
                    assign = textArrayList.get(3);
                    chat = textArrayList.get(4);
                    message = textArrayList.get(5);
                    notAvailable = textArrayList.get(6);
                    assignedMessage = textArrayList.get(7);
                    chatMessage = textArrayList.get(8);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Notification screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textView.setText(textArrayList.get(0));
                            readMore = textArrayList.get(1);
                            okay = textArrayList.get(2);
                            assign = textArrayList.get(3);
                            chat = textArrayList.get(4);
                            message = textArrayList.get(5);
                            notAvailable = textArrayList.get(6);
                            assignedMessage = textArrayList.get(7);
                            chatMessage = textArrayList.get(8);
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
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }
}
