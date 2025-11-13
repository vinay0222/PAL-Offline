package com.idreameducation.ipreppal.pal.activity.loginPages;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.UserInfoModel;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.adapter.PalLanguageSelectionAdapter;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;




public class PalLanguageSelectionActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private TextView nextButton;
    private String board;
    private String language;
    private Global global;
    private boolean isHomeScreen;
    private TextView textView;
    private PalLanguageSelectionAdapter palLanguageSelectionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_language_selection_view);
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
                if (language != null) {
                    Util.setLanguagePackageSelection(context, language);
                    Util.setLanguageSelection(context, language);
                    Intent intent;
                    if(isHomeScreen){
                        intent = new Intent(context, PracticeTopicActivity.class);
                        intent.putExtra("board", board);
                        intent.putExtra("class", Util.getSelectedClass(context));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }else{
                        intent = new Intent(context, PalClassesActivity.class);
                        intent.putExtra("board", board);
                        intent.putExtra("language", language);
                        Util.setIntroDialog(context, true);
                    }

                    UserInfoModel userInfoModel=new UserInfoModel(Util.getUserId(context));
                    userInfoModel.setLanguage(Util.getSelectedLanguage(context));
                    userInfoModel.setPackageLanguage(Util.getSelectedLanguage(context));

                    FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(Util.getUserId(context)).set(userInfoModel);

                    startActivity(intent);
                    finish();
                } else {
                    Util.showToast(context, errorMessage);
                }
            }
        });

    }

    private void assignIds() {


        context = this;
        global = (Global) getApplicationContext();
        isHomeScreen = getIntent().getBooleanExtra("isHomeScreen", false);
//        isHomeScreen = Util.isSRNUser(context);
        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        nextButton = findViewById(R.id.nextButton);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

       // hideNavigationBar(getWindow());
        try {
            setStaticText();
            getBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkOfflineMode();
    }

    private void getBoard() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/BoardsDB.txt";
            JSONObject classObect = Util.readJsonFile(context, filePath);
            try {
                JSONArray jsonArray = classObect.getJSONArray("Boards");
                JSONObject boardsObject = jsonArray.getJSONObject(0);
                String boardID = boardsObject.getString("id");
                String name = boardsObject.getString("name");
                String icon = boardsObject.getString("icon");
                board = name;
                Util.setBoardSelection(context, name);
                Util.setBoardNameSelection(context, name);
                Util.setBoardNameSelectionDummy(context, name);
                Util.setBoardSelectionDummy(context, name);
                getLanguage(board);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.getDatabaseReference().child("boards").child("Boards").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    try{
                   if(snapshot.getValue()!=null){
                       ArrayList<HashMap<String, String>> boardsArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                      String name =  boardsArrayList.get(0).get("name");
                      String id =  boardsArrayList.get(0).get("id");
                      String icon =  boardsArrayList.get(0).get("icon");
                       board = name;
                       Util.setBoardSelection(context, name);
                       Util.setBoardNameSelection(context, name);
                       Util.setBoardNameSelectionDummy(context, name);
                       Util.setBoardSelectionDummy(context, name);
                       getLanguage(board);

                   }
               }catch (Exception e){
                   e.printStackTrace();
               }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    private void getLanguage(String board) {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/LanguageDB.txt";
            JSONObject classObect = Util.readJsonFile(context, filePath);
            try {
                JSONObject boardObject = classObect.getJSONObject(board);
                JSONArray languageArray = boardObject.getJSONArray("language");
                ArrayList<HashMap<String, String>> languageArrayList = new ArrayList<>();
                for (int i = 0; i < languageArray.length(); i++) {
                    JSONObject jsonObject = languageArray.getJSONObject(i);
                    HashMap<String, String> languageHashMap = new HashMap<>();
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    languageHashMap.put("name", name);
                    languageHashMap.put("id", id);
                    languageHashMap.put("selected", "false");
                    languageArrayList.add(languageHashMap);
                }
                palLanguageSelectionAdapter = new PalLanguageSelectionAdapter(context, languageArrayList);
                recyclerView.setAdapter(palLanguageSelectionAdapter);
                palLanguageSelectionAdapter.SetOnItemClickListener(new PalLanguageSelectionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        for (int i = 0; i < languageArrayList.size(); i++) {
                            languageArrayList.get(i).put("selected", "false");
                        }
                        languageArrayList.get(position).put("selected", "true");
                        palLanguageSelectionAdapter.notifyDataSetChanged();
                        language = languageArrayList.get(position).get("id");
                        Util.setLanguagePackageSelection(context,language);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.getDatabaseReference().child(ApplicationConstants.LANGUAGE).child("cbse").child("language").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            ArrayList<HashMap<String, String>> languageArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                            for (int i = 0; i < languageArrayList.size(); i++) {
                                languageArrayList.get(i).put("selected", "false");
                            }

                            palLanguageSelectionAdapter = new PalLanguageSelectionAdapter(context, languageArrayList);
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PalLanguageSelectionActivity.this,getResources().getIdentifier("layout_animation_from_right","anim",getPackageName()));
                            recyclerView.setLayoutAnimation(animation);
                            palLanguageSelectionAdapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setAdapter(palLanguageSelectionAdapter);
                            palLanguageSelectionAdapter.SetOnItemClickListener(new PalLanguageSelectionAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < languageArrayList.size(); i++) {
                                        languageArrayList.get(i).put("selected", "false");
                                    }
                                    languageArrayList.get(position).put("selected", "true");
                                    palLanguageSelectionAdapter.notifyDataSetChanged();
                                    language = languageArrayList.get(position).get("id");
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


    private String errorMessage;

    public void setStaticText() {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {

                JSONObject object;

                if(Util.getSelectedLanguage(context)!=null) object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                else object = jsonObject.getJSONObject("english");

                JSONArray array = object.getJSONArray("languageSelectionScreen");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    textView.setText(textArrayList.get(0));
                    nextButton.setText(textArrayList.get(1));
                    errorMessage = textArrayList.get(2);
                    nextButton.requestFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Util.getSelectedLanguage(context)!=null)
            {
                global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("languageSelectionScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                if (textArrayList.size() > 0) {
                                    textView.setText(textArrayList.get(0));
                                    nextButton.setText(textArrayList.get(1));
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

            }else {
                global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child("english").child("languageSelectionScreen").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                if (textArrayList.size() > 0) {
                                    textView.setText(textArrayList.get(0));
                                    nextButton.setText(textArrayList.get(1));
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


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) { // Same request code used in requestPermissions
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
//                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                assignIds();
                checkOfflineMode();
                // You can now access storage safely
            } else {
                // Permission denied
//                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        try {
            assignIds();
        }catch (Exception e) {

        }
    }
}
