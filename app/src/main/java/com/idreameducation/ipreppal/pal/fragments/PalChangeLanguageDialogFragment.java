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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalClassesActivity;
import com.idreameducation.ipreppal.pal.adapter.PalLanguageSelectionAdapter;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PalChangeLanguageDialogFragment extends DialogFragment{

    private Context context;
    private RecyclerView recyclerView;
    private TextView nextButton;
    private String board;
    private String language;
    private Global global;
    private boolean isHomeScreen;
    private PalLanguageSelectionAdapter palLanguageSelectionAdapter;

    public PalChangeLanguageDialogFragment() {
    }

    public static PalChangeLanguageDialogFragment newInstance(boolean isHomeScreen) {
        PalChangeLanguageDialogFragment frag = new PalChangeLanguageDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isHomeScreen", isHomeScreen);
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
        return inflater.inflate(R.layout.pal_language_selection_view, container);
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
                    }
                    startActivity(intent);

                    getDialog().dismiss();
                } else {
                    Util.showToast(context, "कृपया अपनी भाषा चुनें");
                }
            }
        });
    }

    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        isHomeScreen = getArguments().getBoolean("isHomeScreen", false);
        recyclerView = view.findViewById(R.id.recyclerView);
        nextButton = view.findViewById(R.id.nextButton);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        try {
            getBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            global.getDatabaseReference().child("boards").child("Boards").addValueEventListener(new ValueEventListener() {
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
                        Util.preventTwoClick(view);
                        ((PracticeTopicActivity)getActivity()).refreshMyData();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.getDatabaseReference().child(ApplicationConstants.LANGUAGE).child("cbse").child("language").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            ArrayList<HashMap<String, String>> languageArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                            for (int i = 0; i < languageArrayList.size(); i++) {
                                languageArrayList.get(i).put("selected", "false");
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

}
