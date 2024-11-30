package com.idreameducation.ipreppal.pal.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.FacilitatorListAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.DummyModel;
import com.idreameducation.ipreppal.model.FacilitatorStudentModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 21/02/18.
 */

public class PalFacilitatorListActivity extends AppCompatActivity implements View.OnClickListener {
    private Global global;
    private Context context;
    private RecyclerView mRecyclerView;
    private TextView joinClassText;
    private String addedSuccess;
    private String sorryMessage;
    private String okay;
    private String alreadyAdded;
    private String deleteFromAnother;
    private String cancel;
    private String add;
    private String error;
    private TextView textView;
    private String textInfo;
    private String buttonText;
    private String join;
    private String invalidCode;
    private String enterCode;
    private String batchNotExist;
    private String boardDifferent;
    private String classDifferent;
    private String langaugeDiferenr;
    private boolean isDataloaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_facilitator_list);
        assignIds();
        listners();

    }

    private void listners() {
        joinClassText.setOnClickListener(this);
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        mRecyclerView = findViewById(R.id.recyclerView);
        joinClassText = findViewById(R.id.joinClassText);
        textView = findViewById(R.id.textView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(manager);
        try {
            Util.setBackButton(context);
            setStaticText();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFacilitatorForStudent() throws Exception {

        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        final ArrayList<HashMap<String, String>> facilitatorArrayList = new ArrayList<>();
                        ArrayList<String> keyArrayList = new ArrayList<>();
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot single : dataSnapshot.getChildren()) {
                                HashMap<String, String> facHashmap = (HashMap<String, String>) single.getValue();
                                facilitatorArrayList.add(facHashmap);
                                keyArrayList.add(single.getKey());
                            }
                            FacilitatorListAdapter facilitatorListAdapter = new FacilitatorListAdapter(context, facilitatorArrayList, keyArrayList);
                            mRecyclerView.setAdapter(facilitatorListAdapter);
                            facilitatorListAdapter.SetOnItemClickListener(new FacilitatorListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                }
                            });
                        } else {
                            Util.showToast(context, batchNotExist);
                        }
                    } else {
                        Util.showToast(context, batchNotExist);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.joinClassText:
//                try {
//                    addClassCode();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
    }

    private void addClassCode() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_ngo_code);
        dialog.setCancelable(true);
        TextView textViewokay = dialog.findViewById(R.id.textViewokay);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(textInfo);
        TextInputLayout input_fullName= dialog.findViewById(R.id.input_fullName);
        input_fullName.setHint(join);
        textViewokay.setText(buttonText);
        EditText ngoCodeEditText = dialog.findViewById(R.id.ngoCodeEditText);
        textViewokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classCode = ngoCodeEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(classCode)) {
                    try {
                        validateClassCode(classCode.toUpperCase(), dialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ngoCodeEditText.setError(enterCode);
                }

            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    private void validateClassCode(String code, Dialog dialog) {
        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child("ClassesByTeacherCode").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                        setStaticText(map, dialog);
                    } else {
                        Util.showToast(context, invalidCode);
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

    public void addStudents(HashMap<String, String> map, String key, Dialog dialog) throws Exception {
        Util.dismissDialog();
        Util.showDialog(context);
        //board-class-lanuage-batchName-subjectName-facilitatorID
        DummyModel dummyModel = new DummyModel();
        dummyModel.setsBoard(Util.getSelectedBoardName(context));
        dummyModel.setsClass(map.get("classID"));
        dummyModel.setLangauge(map.get("language"));
        dummyModel.setBatch(map.get("className"));
        dummyModel.setSubject(map.get("subject"));
        dummyModel.setUserId(Util.getUserId(context));
        dummyModel.setName(Util.getUsername(context));
        dummyModel.setKey(key);
        dummyModel.setUserMobile(Util.getUserMobile(context));
        dummyModel.setStudentImage(Util.getUserProfileUrl(context));
        dummyModel.setJoiningDate(Util.getCurrentDateWithDifferentFormat());
        dummyModel.setJoiningDate(Util.getCurrentDateWithDifferentFormat());
        dummyModel.setUserType("Anonymous");

        String facilitatorId = map.get("teacherID");
        String teacherName = map.get("teacherName");
        String boardID = map.get("boardID");
        String classID = map.get("classID");
        String language = map.get("language");
        String subject = map.get("subject");
        String className = map.get("className");
        String data = boardID + "-" + classID + "-" + language + "-" + className + "-" + subject + "-" + teacherName;
        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facilitatorId).child(data).child("List").child(Util.getUserId(context)).setValue(dummyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Util.dismissDialog();
                if (task.getException() != null) {
                    Util.showToast(context, error);
                } else {
                    dialog.dismiss();
                    Util.setFacilitatorNeedToSynced(context , true);
                    Util.showSuccessDialog(context, addedSuccess, okay);

                }
            }
        });
    }

    public void addFacilitatorToStudentData(final HashMap<String, String> map, Dialog dialog) throws Exception {
        FacilitatorStudentModel facilitatorStudentModel = new FacilitatorStudentModel();
        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className");
        facilitatorStudentModel.setName(name);
        facilitatorStudentModel.setFacilitatorId(map.get("teacherID"));
        facilitatorStudentModel.setSubject(map.get("subject"));
        facilitatorStudentModel.setTeacherName(map.get("teacherName"));
        facilitatorStudentModel.setUserType("Anonymous");
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(facilitatorStudentModel.getName()).setValue(facilitatorStudentModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                try {
                    addStudents(map, uniqueKey, dialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getFacilitatorForStudent(HashMap<String, String> map, Dialog dialog) throws Exception {
        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className");
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        HashMap<String, String> facHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                        String facilitatorId = facHashmap.get("facilitatorId");
                        String newFacId = map.get("teacherID"); //code.split("-")[4];
                        if (facilitatorId.equalsIgnoreCase(newFacId)) {
                            Util.showToast(context, alreadyAdded);
                        } else {
                            /* Take Confirmation from user to add same class of another teacher */
                            confirmDialog(map, facilitatorId, dialog);
                        }
                    } else {
                        addFacilitatorToStudentData(map, dialog);
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

    private void confirmDialog(final HashMap<String, String> map, final String facId, Dialog dialog_) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(true);
        TextView textViewAdd = dialog.findViewById(R.id.textViewAdd);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(deleteFromAnother);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewAdd.setText(add);
        textViewCancel.setText(cancel);
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className");
                    global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(map.get("classID")).child(map.get("language")).child(map.get("boardID")).child(ApplicationConstants.FACILITATOR).removeValue();
                    global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facId).child(name).child("List").child(Util.getUserId(context)).removeValue();
                    dialog.dismiss();
                    addFacilitatorToStudentData(map, dialog_);
                    //addStudents(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));


    }

    public void setStaticText() throws Exception {

//        if (!Util.isNetworkAvailable(context)) {
//            Util.showToast(context, Util.getCommonMessages(context).get(6));
//            finish();
//            return;
//        }

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("Facilitator classes Screen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 1) {
                    textView.setText(textArrayList.get(0));
                    joinClassText.setText(textArrayList.get(1));
                    boardDifferent = textArrayList.get(2);
                    classDifferent = textArrayList.get(3);
                    langaugeDiferenr = textArrayList.get(4);
                    batchNotExist = textArrayList.get(5);
                    enterCode = textArrayList.get(6);
                    invalidCode = textArrayList.get(7);
                    buttonText = textArrayList.get(8);
                    textInfo = textArrayList.get(9);
                    join = textArrayList.get(10);

                }
                getFacilitatorForStudent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        isDataloaded = true;
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            textView.setText(textArrayList.get(0));
                            joinClassText.setText(textArrayList.get(1));
                            boardDifferent = textArrayList.get(2);
                            classDifferent = textArrayList.get(3);
                            langaugeDiferenr = textArrayList.get(4);
                            batchNotExist = textArrayList.get(5);
                            enterCode = textArrayList.get(6);
                            invalidCode = textArrayList.get(7);
                            buttonText = textArrayList.get(8);
                            textInfo = textArrayList.get(9);
                            join = textArrayList.get(10);
                        }
                        getFacilitatorForStudent();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Facilitator classes Screen").addValueEventListener(valueEventListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isDataloaded) {
                        Util.dismissDialog();
                        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Facilitator classes Screen").removeEventListener(valueEventListener);
                        Util.showInternetConnectioError(context);
                    }
                }
            }, 8000);
        }

    }


    //    Facilitator classes Screen
    private void setStaticText(HashMap<String, String> map, Dialog dialog) {
        Util.showDialog(context);
        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child(ApplicationConstants.SELECT_SUBJECT_SCREEN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        sorryMessage = textArrayList.get(19);
                        okay = textArrayList.get(17);
                        alreadyAdded = textArrayList.get(18);
                        deleteFromAnother = textArrayList.get(20);
                        cancel = textArrayList.get(21);
                        add = textArrayList.get(22);
                        error = textArrayList.get(25);
                        addedSuccess = textArrayList.get(26);
                        try {
                            String className = Util.getSelectedClassName(context);
                            String boardID = Util.getSelectedBoard(context);
                            String language = Util.getSelectedLanguagePackage(context);
                            String board = map.get("boardID");
                            String sClass = map.get("classID");
                            String slanguage = map.get("language");
                            if (!board.equalsIgnoreCase(boardID)) {
                                Util.showToast(context, boardDifferent);
                                Util.dismissDialog();
                            } else if (!sClass.equalsIgnoreCase(className)) {
                                Util.showToast(context, classDifferent);
                                Util.dismissDialog();
                            } else if (!slanguage.equalsIgnoreCase(language)) {
                                Util.showToast(context, langaugeDiferenr);
                                Util.dismissDialog();
                            } else {
                                getFacilitatorForStudent(map, dialog);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            }
        });
    }
    @Override
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }

}
