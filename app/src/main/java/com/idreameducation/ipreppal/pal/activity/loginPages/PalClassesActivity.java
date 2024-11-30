package com.idreameducation.ipreppal.pal.activity.loginPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.idreameducation.ipreppal.pal.adapter.ClassesAdapter_;
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

public class PalClassesActivity extends AppCompatActivity  {

    private Global global;
    private Context context;
    private String language;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private TextView nextButton;
    private TextView textView;
    private StudentDetailsRepository studentDetailsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_iprep_class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                assignIds();
                listners();
            }
        },500);
    }

    private void listners() {
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if (sClass == null) {
                    Util.openGifDialogue(context,errorMessage);
//                    Util.showToast(context, errorMessage);
                }
                else {


                    if (sClass.equalsIgnoreCase("11") || sClass.equalsIgnoreCase("12")) {
                        startActivity(new Intent(context, PalStreamsActivity.class).putExtra("board", board).putExtra("class", sClass));

                    } else {

                        saveNewStudentIntoFirebaseDatabaseAndLogin();

                        Util.setClassSelection(context, sClass);
                        Util.setClassNameSelection(context, sClass);

                        UserInfoModel userInfoModel=new UserInfoModel(Util.getUserId(context));
                        userInfoModel.setClassID(Util.getSelectedClass(context));
                        userInfoModel.setStudentClass(Util.getSelectedClass(context));

                        StudentInfoModel studentInfoModel=new StudentInfoModel();
                        studentInfoModel.setUserClass(Util.getSelectedClass(context));
                        studentInfoModel.setUserName(Util.getUsername(context));
                        studentInfoModel.setUserLanguage(Util.getSelectedLanguage(context));
                        studentInfoModel.setUserMobile(Util.getUserMobile(context));
                        studentInfoModel.setUserRollNo(Util.getUserRoll(context));

                        UserActivities.setUserInfo(studentInfoModel);


                        FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(Util.getUserId(context)).set(userInfoModel);

                        Intent intent = new Intent(context, PracticeTopicActivity.class);
                        intent.putExtra("board", board);
                        intent.putExtra("class", sClass);
                        Util.setClassSelection(context,sClass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        Util.setIsFirstTime(context,"false");
                    }

                }
            }
        });



    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        board = getIntent().getStringExtra("board");
        language = getIntent().getStringExtra("language");
        studentDetailsRepository = new StudentDetailsRepository(context);
        nextButton = findViewById(R.id.nextButton);
//        nextButton.setBackgroundResource(R.drawable.skip_button_color);
        nextButton.setEnabled(false);
        textView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);
        try {
            setStaticText();
            getClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("classSelectionScreen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textView.setText(textArrayList.get(1));
                    nextButton.setText(textArrayList.get(0));
                    errorMessage = textArrayList.get(2);
                    nextButton.requestFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("classSelectionScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            if (textArrayList.size() > 0) {
                                textView.setText(textArrayList.get(1));
                                nextButton.setText(textArrayList.get(0));
                                errorMessage = textArrayList.get(2);
                                nextButton.requestFocus();
                            }
                        }
                    } catch (Exception e) {
                        Util.dismissdataDialog();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }




    public void getClasses() throws Exception {
        if (Util.isOfflineMode(context)) {

            String filePath = ".iDream_content/offlinetab_PAL/ClassesDB.txt";
            JSONObject classObect = Util.readJsonFile(context, filePath);
            try {
//                JSONObject innerObject = classObect.getJSONObject("classes");
                JSONObject classesObject = classObect.getJSONObject(board);
                JSONArray classesArray = classesObject.getJSONArray(Util.getSelectedLanguage(context));
                ArrayList<HashMap<String, String>> classesArrayList = new ArrayList<>();

                for (int i = 0; i < classesArray.length(); i++) {
                    JSONObject jsonObject = classesArray.getJSONObject(i);
                    HashMap<String, String> classesHashMap = new HashMap<>();
                    String name = jsonObject.getString("name");
                    String id = jsonObject.getString("id");
                    classesHashMap.put("name", name);
                    classesHashMap.put("id", id);
                    classesHashMap.put("selected", "false");
                    classesArrayList.add(classesHashMap);
                }
                ClassesAdapter_ classesAdapter = new ClassesAdapter_(context, classesArrayList);
                recyclerView.setAdapter(classesAdapter);
                classesAdapter.SetOnItemClickListener(new ClassesAdapter_.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        for (int i = 0; i < classesArrayList.size(); i++) {
                            classesArrayList.get(i).put("selected", "false");
                        }

                        nextButton.setBackgroundResource(R.drawable.blue_iprep_button);
                        nextButton.setEnabled(true);
                        classesArrayList.get(position).put("selected", "true");
                        classesAdapter.notifyDataSetChanged();
                        sClass = classesArrayList.get(position).get("id");

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!Util.checkInternetConnection(context)) {
                Util.showInternetConnectioError(context);
                return;
            }
            final ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {

                            ArrayList<HashMap<String, String>> classesArrayList = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                            for (int i = 0; i < classesArrayList.size(); i++) {
                                classesArrayList.get(i).put("selected", "false");
                            }

                            ClassesAdapter_ classesAdapter = new ClassesAdapter_(context, classesArrayList);
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PalClassesActivity.this,getResources().getIdentifier("layout_animation_from_right","anim",getPackageName()));
                            recyclerView.setLayoutAnimation(animation);
                            classesAdapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setAdapter(classesAdapter);
                            classesAdapter.SetOnItemClickListener(new ClassesAdapter_.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < classesArrayList.size(); i++) {
                                        classesArrayList.get(i).put("selected", "false");
                                    }
                                    nextButton.setBackgroundResource(R.drawable.blue_iprep_button);
                                    nextButton.setEnabled(true);
                                    nextButton.setFocusable(true);
                                    nextButton.requestFocus();
                                    classesArrayList.get(position).put("selected", "true");
                                    classesAdapter.notifyDataSetChanged();
                                    sClass = classesArrayList.get(position).get("id");

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.showInternetConnectioError(context);
                }
            };
            global.getDatabaseReference().child(ApplicationConstants.CLASSES).child(board).child(language).addValueEventListener(valueEventListener);
        }

    }

    private String errorMessage;

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

        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("sClass").setValue(sClass);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("studentClass").setValue(sClass);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("classID").setValue(sClass);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("sLanguage").setValue(language);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("language").setValue(language);
        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).child("packageLanguage").setValue(language);
//        global.getDatabaseReference().child(usersDetailsPath).child(userID).child("schoolId").setValue(Util.getSchoolId(context));
//        global.getDatabaseReference().child(usersDetailsPath).child(userID).child("schoolName").setValue(Util.getSchoolName(context));
//        global.getDatabaseReference().child(usersDetailsPath).child(userID).child("phoneNo").setValue(Util.getUserMobile(context));
//        global.getDatabaseReference().child(usersDetailsPath).child(userID).child("rollNo").setValue(Util.getUserRoll(context));
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
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(Util.getNGOID(context)).child(Util.getUserId(context)).updateChildren(profileMap);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Util.preventPause(context,getTaskId());
    }


}
