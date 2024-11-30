package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.StudentInfo;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalStreamsActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.adapter.ClassesAdapter_;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.StudentDetailsRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PalChangeClassDialogFragment extends DialogFragment  {

    private Global global;
    private Context context;
    private String language;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private TextView nextButton;
    private TextView textView;
    private TextView textViewtitle;
    private StudentDetailsRepository studentDetailsRepository;

    public PalChangeClassDialogFragment() {
    }

    public static PalChangeClassDialogFragment newInstance(String board, String language) {
        PalChangeClassDialogFragment frag = new PalChangeClassDialogFragment();
        Bundle args = new Bundle();
        args.putString("board", board);
        args.putString("language", language);
        frag.setArguments(args);
        return frag;
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.50), (int) (size.y * 0.80));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_iprep_class_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds(view);
        listners();
    }

    private void listners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sClass == null) {
                    Util.showToast(context, "कृपया अपनी कक्षा का चयन करें");
                } else {
                    if (sClass.equalsIgnoreCase("11") || sClass.equalsIgnoreCase("12")) {
                        startActivity(new Intent(context, PalStreamsActivity.class).putExtra("board", board).putExtra("class", sClass));

                        getDialog().dismiss();
                    } else {


                        String ngoID = Util.getNGOID(context);
                        String userName = Util.getUsername(context);
                        String key = global.getDatabaseReference().push().getKey();
//                        String userID = userName + "-" + board + "-" + sClass + "-" + ngoID + "-" + key;
//                        Util.setUserId(context, userID);

                        saveNewStudentIntoFirebaseDatabaseAndLogin();

                        Util.setClassSelection(context, sClass);
                        Util.setClassNameSelection(context, sClass);
                        saveProfile();

                        Intent intent = new Intent(context, PracticeTopicActivity.class);
                        intent.putExtra("board", board);
                        intent.putExtra("class", sClass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        getDialog().dismiss();
                    }

                }
            }
        });
    }

    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        board = getArguments().getString("board", null);
        language = Util.getSelectedLanguagePackage(context);
        studentDetailsRepository = new StudentDetailsRepository(context);
        nextButton = view.findViewById(R.id.nextButton);
        textView = view.findViewById(R.id.textView);
        textViewtitle = view.findViewById(R.id.textViewtitle);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);
        try {
            setStaticText();
//            getBoard();
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
                    textView.setText(textArrayList.get(0));
                    nextButton.setText(textArrayList.get(1));
                    //errorMessage = textArrayList.get(2);
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
                                textView.setText(textArrayList.get(0));
                                nextButton.setText(textArrayList.get(1));
                               // errorMessage = textArrayList.get(2);
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
                JSONObject innerObject = classObect.getJSONObject("classes");
                JSONObject classesObject = innerObject.getJSONObject(language);
                JSONArray classesArray = classesObject.getJSONArray(board);
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
                            recyclerView.setAdapter(classesAdapter);
                            classesAdapter.SetOnItemClickListener(new ClassesAdapter_.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < classesArrayList.size(); i++) {
                                        classesArrayList.get(i).put("selected", "false");
                                    }
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


    public void getClasses_() throws Exception {
        if (Util.isOfflineMode(context)) {

            String filePath = ".iDream_content/offlinetab_PAL/ClassesDB.txt";
            JSONObject classObect = Util.readJsonFile(context, filePath);
            try {
                JSONObject innerObject = classObect.getJSONObject("classes");
                JSONObject classesObject = innerObject.getJSONObject(language);
                JSONArray classesArray = classesObject.getJSONArray(board);
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
                            recyclerView.setAdapter(classesAdapter);
                            classesAdapter.SetOnItemClickListener(new ClassesAdapter_.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < classesArrayList.size(); i++) {
                                        classesArrayList.get(i).put("selected", "false");
                                    }
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
            global.getDatabaseReference().child(ApplicationConstants.CLASSES).child(board).addValueEventListener(valueEventListener);
        }

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

        global.getDatabaseReference().child(usersDetailsPath).child("students").child(userID).setValue(studentInfo);
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

}
