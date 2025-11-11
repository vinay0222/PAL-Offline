package com.idreameducation.ipreppal.pal.activity.loginPages;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.StudentInfo;
import com.idreameducation.ipreppal.model.StudentInfoModel;
import com.idreameducation.ipreppal.model.UserInfoModel;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.adapter.StreamsAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.StudentDetailsRepository;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class PalStreamsActivity extends AppCompatActivity {


    private Global global;
    private Context context;
    private String language,toastText;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private String stream;
    private TextView nextButton;
    private StudentDetailsRepository studentDetailsRepository;

    private TextView textViewTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_iprep_streams);
        assignIds();
        setStaticText();
        listners();
    }

    private void listners() {
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stream != null) {
                    sClass=stream;
                    saveNewStudentIntoFirebaseDatabaseAndLogin();

                    String studentClass = stream;

                    Util.setClassSelection(context, studentClass);
                    Util.setClassNameSelection(context, studentClass);
                    Util.setStreamClassSelection(context, studentClass);
                    sClass = studentClass;
                    saveProfile();
                    Util.preventTwoClick(v);

                    UserInfoModel userInfoModel=new UserInfoModel(Util.getUserId(context));
                    userInfoModel.setClassID(Util.getSelectedClass(context));
                    userInfoModel.setStudentClass(Util.getSelectedClass(context));

                    FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(Util.getUserId(context)).set(userInfoModel);

                    StudentInfoModel studentInfoModel=new StudentInfoModel();
                    studentInfoModel.setUserClass(Util.getSelectedClass(context));
                    studentInfoModel.setUserName(Util.getUsername(context));
                    studentInfoModel.setUserLanguage(Util.getSelectedLanguage(context));
                    studentInfoModel.setUserMobile(Util.getUserMobile(context));
                    studentInfoModel.setUserRollNo(Util.getUserRoll(context));

                    UserActivities.setUserInfo(studentInfoModel);

                    Intent intent = new Intent(context, PracticeTopicActivity.class);
                    intent.putExtra("board", board);
                    intent.putExtra("class", studentClass);
                    Util.setClassSelection(context, studentClass);
                    Util.setClassNameSelection(context, studentClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //startActivity(new Intent(context, HomeActivity.class).putExtra("board", board).putExtra("class", studentClass));
                    //
                } else {
                    openGifDialogue(toastText);
                    // Util.showToast(context, "Please select your stream");
                }
            }
        });
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        studentDetailsRepository = new StudentDetailsRepository(context);
        recyclerView = findViewById(R.id.recyclerView);
        nextButton = findViewById(R.id.nextButton);
        textViewTitle = findViewById(R.id.textView);
        recyclerView.setHasFixedSize(true);

        //hideNavigationBar(getWindow());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        board = getIntent().getStringExtra("board");
        sClass = getIntent().getStringExtra("class");
        language = Util.getSelectedLanguage(context);
        try {
            getStreams();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkOfflineMode();
    }

    private void getStreams() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/streams.txt";
            JSONObject streamObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject boardObject = streamObject.getJSONObject(board);
                JSONObject languageObject = boardObject.getJSONObject(language);
                JSONArray streamsArray = languageObject.getJSONArray(sClass);
                ArrayList<HashMap<String, String>> streamsArrayList = new ArrayList<>();
                for (int i = 0; i < streamsArray.length(); i++) {
                    JSONObject jsonObject = streamsArray.getJSONObject(i);
                    HashMap<String, String> streamsHashMap = new HashMap<>();
                    String name = jsonObject.getString("name");
                    String id = jsonObject.getString("id");
                    String icon = jsonObject.getString("icon");
                    streamsHashMap.put("name", name);
                    streamsHashMap.put("id", id);
                    streamsHashMap.put("icon", icon);
                    streamsHashMap.put("selected", "false");
                    streamsArrayList.add(streamsHashMap);
                }
                StreamsAdapter streamsAdapter = new StreamsAdapter(context, streamsArrayList);
                recyclerView.setAdapter(streamsAdapter);
                streamsAdapter.SetOnItemClickListener(new StreamsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        for (int i = 0; i < streamsArrayList.size(); i++) {
                            streamsArrayList.get(i).put("selected", "false");
                        }
                        streamsArrayList.get(position).put("selected", "true");
                        streamsAdapter.notifyDataSetChanged();
                        stream = streamsArrayList.get(position).get("id");
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child(ApplicationConstants.STREAMS).child(board).child(language).child(sClass).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            ArrayList<HashMap<String, String>> streamsArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();
                            for (int i = 0; i < streamsArrayList.size(); i++) streamsArrayList.get(i).put("selected", "false");

                            StreamsAdapter streamsAdapter = new StreamsAdapter(context, streamsArrayList);
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PalStreamsActivity.this,getResources().getIdentifier("layout_animation_from_right","anim",getPackageName()));
                            recyclerView.setLayoutAnimation(animation);
                            streamsAdapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setAdapter(streamsAdapter);
                            streamsAdapter.SetOnItemClickListener(new StreamsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < streamsArrayList.size(); i++) streamsArrayList.get(i).put("selected", "false");
                                    streamsArrayList.get(position).put("selected", "true");
                                    streamsAdapter.notifyDataSetChanged();
                                    stream = streamsArrayList.get(position).get("id");
                                }
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
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    private void saveProfile() {
        Map<String, Object> profileMap = new HashMap<>();
        profileMap.put("educationBoard", board);
        profileMap.put("boardID", board);
        profileMap.put("packageLanguage", language);
        profileMap.put("language", language);
        profileMap.put("studentClass", sClass);
        profileMap.put("classID", sClass);
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).updateChildren(profileMap);
    }

    private void saveNewStudentIntoFirebaseDatabaseAndLogin() {
        // Create user
        StudentInfo studentInfo = new StudentInfo(Util.getUsername(context), Util.getUsername(context), Util.getUsername(context));
        studentInfo.setsBoard(board);
        studentInfo.setsClass(sClass);
        studentInfo.setsLanguage(language);
        String ngoID = Util.getNGOID(context);
        String usersDetailsPath = "offline_login_users/" + ngoID;
        String userID = ngoID + "_" + (Util.getUsername(context).replace(" ","_")).toLowerCase()+"_"+Util.getUserMobile(context);

        if(studentDetailsRepository.isDataExist(userID, ngoID)){
            studentDetailsRepository.updateField(userID, ngoID, Util.getUsername(context), Util.getUsername(context), Util.getUsername(context), board, sClass, language);
        }else{
            StudentDetailsModel studentDetailsModel = new StudentDetailsModel();
            studentDetailsModel.setNgoId(ngoID);
            studentDetailsModel.setUserId(userID);
            studentDetailsModel.setStudentName(Util.getUsername(context));
            studentDetailsModel.setStudentPassword(Util.getUsername(context));
            studentDetailsModel.setUserName(Util.getUsername(context));
            studentDetailsModel.setBoard(board);
            studentDetailsModel.setSClass(sClass);
            studentDetailsModel.setLanguage(language);
            studentDetailsRepository.insertStudentDetails(studentDetailsModel);
        }

        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("sClass").setValue(stream);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("studentClass").setValue(stream);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("classID").setValue(stream);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("sLanguage").setValue(language);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("language").setValue(language);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("packageLanguage").setValue(language);
    }

    private void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("streamScreen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    System.out.println("====== textArrayList "+textArrayList);
                    textViewTitle.setText(textArrayList.get(0));
                    nextButton.setText(textArrayList.get(1));
                    toastText =textArrayList.get(2);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Util.getSelectedLanguage(context)!=null)
            {
                global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("streamScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                if (textArrayList.size() > 0) {
                                    textViewTitle.setText(textArrayList.get(0));
                                    nextButton.setText(textArrayList.get(1));
                                    toastText =textArrayList.get(2);



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

            }else {
                global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child("english").child("streamScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                if (textArrayList.size() > 0) {
                                    textViewTitle.setText(textArrayList.get(0));
                                    nextButton.setText(textArrayList.get(1));
                                    toastText =textArrayList.get(2);
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
        }

    }

    private void openGifDialogue(String textError) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_toast_messages);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);
        text.setText("Oops! No Internet");
        text.setVisibility(View.GONE);

        TextView text_ = dialog.findViewById(R.id.text_);
        text_.setText(textError);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);

        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);
        textViewRetry.setVisibility(View.GONE);
        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(this)
                .load(R.raw.error)
                .into(imageViewGif);
        textViewRetry.setText("Try Again");
        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) { // Same request code used in requestPermissions
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                assignIds();
                checkOfflineMode();
                // You can now access storage safely
            } else {
                // Permission denied
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkOfflineMode() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 and above
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                    startActivityForResult(intent,100);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent,100);
                }
            }
        } else {
            // For Android 10 and below, request READ/WRITE permission normally
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    100
            );
        }

    }


}
