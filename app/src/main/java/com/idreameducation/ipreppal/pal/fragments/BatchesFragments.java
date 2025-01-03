package com.idreameducation.ipreppal.pal.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.idreameducation.ipreppal.Interfaces.OnReceiveBatchInfo;
import com.idreameducation.ipreppal.PalMobile.activity.BatchActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.BatchModel;
import com.idreameducation.ipreppal.model.DummyModel;
import com.idreameducation.ipreppal.model.FacilitatorClassModel;
import com.idreameducation.ipreppal.model.FacilitatorStudentModel;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.adapter.PalCoachPagerAdaper;
import com.idreameducation.ipreppal.pal.adapter.PalFacilitatorClassesAdapter;
import com.idreameducation.ipreppal.pal.adapter.PalRecentChatAdapter;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BatchesFragments extends Fragment {

    private Context context;
    private Global global;
    private RecyclerView classesRecyclerView;
    private RecyclerView message_recycler_view;
    private RecyclerView assignment_recycler_view;
    private ArrayList<HashMap<String, String>> contentArrayList;
    private ArrayList<String> keyArrayList;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout linearLayoutBatches;
    private LinearLayout linearLaoutNoBtches;
    private TextView textViewJoinBatch;
    private TextView classesTextView;
    private TextView recentMessagesTextView;
    private TextView recentAssignmentTextView;
    private TextView textViewUserName, joinNewBatch;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static ArrayList<String> textArrayList;

    private String selectedBatchCode = "";
    public int selectedBatchPosition = 0;
    PalFacilitatorClassesAdapter facilitatorClassesAdapter;

    private LinearLayout newMessageLayout, assignmentLayout;
    LinearLayout batchLayout;
    TextView langChangeLayout,noBatchText;
    TextView noAssignmentText, noChatText,notConnectedWithInternet;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_batches, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        batchLayout = view.findViewById(R.id.batchLayout);
        joinNewBatch = view.findViewById(R.id.joinNewBatch);
        classesTextView = view.findViewById(R.id.classesTextView);
        textViewUserName = view.findViewById(R.id.textViewUserName);
        newMessageLayout = view.findViewById(R.id.newMessageLayout);
        noAssignmentText = view.findViewById(R.id.noAssignmentText);
        noChatText = view.findViewById(R.id.noChatText);
        noBatchText = view.findViewById(R.id.noBatchText);
        assignmentLayout = view.findViewById(R.id.assignmentLayout);
        langChangeLayout = view.findViewById(R.id.langChangeLayout);
        classesRecyclerView = view.findViewById(R.id.class_recycler_view);
        message_recycler_view = view.findViewById(R.id.message_recycler_view);
        recentMessagesTextView = view.findViewById(R.id.recentMessagesTextView);
        recentAssignmentTextView = view.findViewById(R.id.recentAssignmentTextView);
        assignment_recycler_view = view.findViewById(R.id.assignment_recycler_view);
        notConnectedWithInternet = view.findViewById(R.id.notConnectedWithInternet);

        assignment_recycler_view.setHasFixedSize(true);
        message_recycler_view.setHasFixedSize(true);
        classesRecyclerView.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(context, 1);
        GridLayoutManager manager1 = new GridLayoutManager(context, 1);

        assignment_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        message_recycler_view.setLayoutManager(manager1);
        classesRecyclerView.setLayoutManager(manager);

        linearLayoutBatches = view.findViewById(R.id.linearLayoutBatches);
        linearLaoutNoBtches = view.findViewById(R.id.linearLaoutNoBtches);
        textViewJoinBatch = view.findViewById(R.id.textViewJoinBatch);

        textViewJoinBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCodeEnterDialog();
            }
        });

        joinNewBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCodeEnterDialog();
            }
        });


        //setStaticTextToFireBase();
        setStaticTextFromFireBase();
        getBatches();


        if(Util.getSelectedLanguage(context).equals("hindi")) {
            noBatchText.setText("आपने अभी कोई बैच ज्वाइन नहीं किया है");
            textViewJoinBatch.setText("ज्वाइन बैच");
        }


        try {
            //setLayoutText();
//            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(!Util.checkInternetConnection(context)) notConnectedWithInternet.setVisibility(View.VISIBLE);
            else notConnectedWithInternet.setVisibility(View.GONE);
        }catch (Exception e) {e.printStackTrace();}

    }

    private void setPagerAdapter() {
        try {
            PalCoachPagerAdaper contentPagerAdaper = new PalCoachPagerAdaper(requireActivity().getSupportFragmentManager());
            viewPager.setAdapter(contentPagerAdaper);
            isBatchActive(selectedBatchCode);
        }catch (Exception f) {
            f.printStackTrace();
        }
        //viewPager.setOffscreenPageLimit(3);
    }

    private void setTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        // this needs to be dynamic
        tabLayout.getTabAt(1).setText(textArrayList.get(4));
        tabLayout.getTabAt(0).setText(textArrayList.get(5));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        tabLayout.setSelectedTabIndicatorColor(context.getResources().getColor(R.color.blue));
        tabLayout.setTabTextColors(Color.parseColor("#C9C9C9"), Color.parseColor("#223322"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        LinearLayout tabLayoutt = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
        tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
        tabTextView.setTextSize(14);

//        if(((PracticeTopicActivity) context).type.equals("chat_notification")){
//            viewPager.setCurrentItem(1);
//        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabLayoutt = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tabLayoutt = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoutt.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getBatches() {

        getBachesNew();

//        if(context==null) return;
//        else if(Util.getUserId(context)==null) return;
//        else global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    contentArrayList = new ArrayList<>();
//                    keyArrayList = new ArrayList<>();
//                    if (dataSnapshot.getValue() != null) {
//                        for (DataSnapshot single : dataSnapshot.getChildren()) {
//                            HashMap<String, String> boardHashmap = (HashMap<String, String>) single.getValue();
//                            boardHashmap.put("selected", "false");
//                            boardHashmap.put("deleted", isBatchActive(boardHashmap.get("batchID")));
//                            if (boardHashmap.get("deleted") == null) {
//                                contentArrayList.add(boardHashmap);
//                                keyArrayList.add(single.getKey());
//                            }
//                        }
//                        if (contentArrayList.size() > 0) {
//                            hideChangeLanguageOption();
//                            linearLaoutNoBtches.setVisibility(View.GONE);
//                            linearLayoutBatches.setVisibility(View.VISIBLE);
//
//                            String teacherID = contentArrayList.get(selectedBatchPosition).get("facilitatorId");
//                            String teacherName = contentArrayList.get(selectedBatchPosition).get("teacherName");
//                            String batchID = contentArrayList.get(selectedBatchPosition).get("batchID");
//                            String batchName = contentArrayList.get(selectedBatchPosition).get("name").split("-")[3];
//
//                            String[] sp = contentArrayList.get(selectedBatchPosition).get("name").split("-");
//                            String id = sp[0] + "-" + sp[1] + "-" + sp[2] + "-" + sp[3] + "-" + sp[4] + "-" + sp[6] + "-" + sp[7];
//
//                            if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
//                                global.setBatchCode(id);
//                                selectedBatchCode = batchID;
//                                global.setBatchName(batchName);
//                                global.setBatchID(batchID);
//                                global.setChatID(teacherID);
//                                global.setChatName(teacherName);
//                                textViewUserName.setText(teacherName);
//
//                                FirebaseMessaging.getInstance().subscribeToTopic(batchID);
//
//
//                                if (Util.isPortraitMode(context)) {
//                                    Intent intent = new Intent(context, BatchActivity.class);
//                                    intent.putExtra("classID", sp[1]);
//                                    intent.putExtra("batchName", batchName);
////                                    startActivity(intent);
//                                } else {
//                                    //openFragment();
//                                    setPagerAdapter();
//                                    setTabLayout();
//                                    PalChatFragment.getMessages();
//                                    viewPager.setCurrentItem(0);
//
//                                    try {
//                                        facilitatorClassesAdapter.setPosition(0);
//                                        facilitatorClassesAdapter.notifyDataSetChanged();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            } else if (contentArrayList.get(1).get("batchID") != null) {
//
//                                linearLaoutNoBtches.setVisibility(View.GONE);
//                                linearLayoutBatches.setVisibility(View.VISIBLE);
//                                teacherID = contentArrayList.get(1).get("facilitatorId");
//                                teacherName = contentArrayList.get(1).get("teacherName");
//                                batchID = contentArrayList.get(1).get("batchID");
//                                batchName = contentArrayList.get(1).get("name").split("-")[3];
//
//                                sp = contentArrayList.get(1).get("name").split("-");
//                                id = sp[0] + "-" + sp[1] + "-" + sp[2] + "-" + sp[3] + "-" + sp[4] + "-" + sp[6] + "-" + sp[7];
//
//
//                                if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
//                                    global.setBatchCode(id);
//                                    global.setBatchName(batchName);
//                                    global.setBatchID(batchID);
//                                    global.setChatID(teacherID);
//                                    global.setChatName(teacherName);
//                                    textViewUserName.setText(teacherName);
//
//                                    FirebaseMessaging.getInstance().subscribeToTopic(batchID);
//
//                                    setPagerAdapter();
//                                    setTabLayout();
//                                    viewPager.setCurrentItem(0);
//                                    PalChatFragment.getMessages();
//
//                                    classesRecyclerView.setVisibility(View.VISIBLE);
//                                    contentArrayList.remove(0);
//
//                                    try {
//                                        facilitatorClassesAdapter.setPosition(0);
//                                        classesRecyclerView.setAdapter(facilitatorClassesAdapter);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                } else showChangeLanguageOption();
//
//                            } else showChangeLanguageOption();
//
//                            classesRecyclerView.setVisibility(View.VISIBLE);
//                            facilitatorClassesAdapter = new PalFacilitatorClassesAdapter(context, contentArrayList, keyArrayList);
//                            if (!Util.isPortraitMode(context)) facilitatorClassesAdapter.setPosition(0);
//                            classesRecyclerView.setAdapter(facilitatorClassesAdapter);
//                            facilitatorClassesAdapter.SetOnItemClickListener(new PalFacilitatorClassesAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    String teacherID = contentArrayList.get(position).get("facilitatorId");
//                                    String teacherName = contentArrayList.get(position).get("teacherName");
//                                    String batchID = contentArrayList.get(position).get("batchID");
//                                    String batchName = contentArrayList.get(position).get("name").split("-")[3];
//                                    String[] sp = contentArrayList.get(position).get("name").split("-");
//                                    String id = sp[0] + "-" + sp[1] + "-" + sp[2] + "-" + sp[3] + "-" + sp[4] + "-" + sp[6] + "-" + sp[7];
//
//                                    hideChangeLanguageOption();
//                                    selectedBatchCode = batchID;
//                                    global.setBatchName(batchName);
//                                    global.setBatchID(batchID);
//                                    global.setChatID(teacherID);
//                                    global.setChatName(teacherName);
//                                    global.setBatchCode(id);
//                                    FirebaseMessaging.getInstance().subscribeToTopic(batchID);
//
//                                    if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
//
//                                        if (Util.isPortraitMode(context)) {
//                                            Intent intent = new Intent(context, BatchActivity.class);
//                                            intent.putExtra("classID", sp[1]);
//                                            intent.putExtra("batchName", batchName);
//                                            startActivity(intent);
//                                        } else {
//                                            setPagerAdapter();
//                                            setTabLayout();
////                                            PalChatFragment.getMessages();
//                                            viewPager.setCurrentItem(0);
//                                            facilitatorClassesAdapter.setPosition(position);
//                                            facilitatorClassesAdapter.notifyDataSetChanged();
//                                        }
//                                    } else {
//                                        viewPager.setCurrentItem(0);
//                                        facilitatorClassesAdapter.setPosition(position);
//                                        facilitatorClassesAdapter.notifyDataSetChanged();
//                                        showChangeLanguageOption();
//                                    }
//
//
//                                }
//
//                            });
//
//                        } else {
//                            linearLayoutBatches.setVisibility(View.GONE);
//                            linearLaoutNoBtches.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        linearLayoutBatches.setVisibility(View.GONE);
//                        linearLaoutNoBtches.setVisibility(View.VISIBLE);
//                    }
//                    getRecentMessages();
//                    getRecentAssignments();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Util.showInternetConnectioError(context);
//            }
//        });
    }

    ArrayList<String> batchesList=new ArrayList<>();

    private void getBachesNew() {

        if(context==null) return;
        else if(Util.getUserId(context)==null) return;
        else global.getDatabaseReference().child("students").child(Util.getUserId(context)).child("batches").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        batchesList.clear();
                        contentArrayList = new ArrayList<>();
                        keyArrayList = new ArrayList<>();
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot single : dataSnapshot.getChildren()) {

                                try {
                                    HashMap<String, String> boardHashmap = (HashMap<String, String>) single.child("info").getValue();
                                    boardHashmap.put("selected", String.valueOf(boardHashmap.get("shareCode").equals(Util.getLastBatch(context))));
                                    boardHashmap.put("key", single.getKey());

                                    if(!batchesList.contains(single.getKey())) batchesList.add(single.getKey());

                                    /** show only selected language batches */
                                    if(Util.getSelectedLanguage(context).equals(boardHashmap.get("language").toLowerCase())) {
                                        contentArrayList.add(boardHashmap);
                                        keyArrayList.add(single.getKey());
                                    }
                                }catch (Exception r) {
                                    r.printStackTrace();
                                }


                                /** batch is deleted checker */
//                            boardHashmap.put("deleted", isBatchActive(boardHashmap.get("batchID")));
//                            if (boardHashmap.get("deleted") == null) {
//                                contentArrayList.add(boardHashmap);
//                                keyArrayList.add(single.getKey());
//                            }
                            }
                            showBatches();
                        } else {
                            linearLayoutBatches.setVisibility(View.GONE);
                            linearLaoutNoBtches.setVisibility(View.VISIBLE);
                        }
//                    getRecentMessages();
//                    getRecentAssignments();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.showInternetConnectioError(context);
                }
            });
    }
    String status = null;

    private void showBatches() {

        if (contentArrayList.size() > 0) {
            hideChangeLanguageOption();
            linearLaoutNoBtches.setVisibility(View.GONE);
            linearLayoutBatches.setVisibility(View.VISIBLE);

            classesRecyclerView.setVisibility(View.VISIBLE);
            facilitatorClassesAdapter = new PalFacilitatorClassesAdapter(context, contentArrayList, keyArrayList);
            openBatch(0);
            if (!Util.isPortraitMode(context)) try {
                if(Util.getLastBatch(context)==null) openBatch(0);
                else {
                    for(int i=0;i<=contentArrayList.size()-1;i++) {
                        if(contentArrayList.get(i).get("key").equals(Util.getLastBatch(context))) {
                            openBatch(i);
                            break;
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            classesRecyclerView.setAdapter(facilitatorClassesAdapter);
            facilitatorClassesAdapter.SetOnItemClickListener(new PalFacilitatorClassesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    openBatch(position);
                }

            });

        } else {
            linearLayoutBatches.setVisibility(View.GONE);
            linearLaoutNoBtches.setVisibility(View.VISIBLE);
        }
    }

    private void openBatch(int position) {
        getBatchInfo(contentArrayList.get(position).get("key"), batchModel -> {
            String teacherID = batchModel.getTeacherID();
            String teacherName = batchModel.getTeacherName();
            String batchID = batchModel.getShareCode();
            String batchName = batchModel.getClassName();

            hideChangeLanguageOption();
            selectedBatchCode = batchID;
            global.setBatchName(batchName);
            global.setBatchID(batchID);
            global.setChatID(teacherID);
            global.setChatName(teacherName);
            global.setBatchCode(batchModel.getShareCode());
            FirebaseMessaging.getInstance().subscribeToTopic(batchID);

            if (Util.isPortraitMode(context)) {
                Intent intent = new Intent(context, BatchActivity.class);
                intent.putExtra("classID", batchModel.getClassID());
                intent.putExtra("batchName", batchName);
                intent.putExtra("batchID", batchModel.getShareCode());
                startActivity(intent);
            } else {
                try {
                    setPagerAdapter();
                    setTabLayout();
                    PalChatFragment.getMessages();
                    viewPager.setCurrentItem(0);
                    Util.setLastBatch(context,batchModel.getShareCode());
                    facilitatorClassesAdapter.setPosition(position);
                    facilitatorClassesAdapter.notifyDataSetChanged();
                }catch (Exception f) {
                    f.printStackTrace();
                }
            }


            /** language not same code */
//                            if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
//
//
//                            } else {
//                                viewPager.setCurrentItem(0);
//                                facilitatorClassesAdapter.setPosition(position);
//                                facilitatorClassesAdapter.notifyDataSetChanged();
//                                showChangeLanguageOption();
//                            }
//


        });
    }

    private void getBatchInfo(String batchCode, OnReceiveBatchInfo onReceiveBatchInfo) {
        global.getDatabaseReference().child("batches").child(batchCode).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!=null)
                    onReceiveBatchInfo.onReceive(snapshot.getValue(BatchModel.class));

                else {
                    if(Util.getSelectedLanguage(context).equals("hindi")) Toast.makeText(context, "बैच हटा दिया गया है", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(context, "Batch is Deleted", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String isBatchActive(String batchID) {
        status = null;
        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child("ClassesByTeacherCode")
                .child(batchID).child("deleted").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            status = "true";

                            viewPager.setAlpha(.5f);

                        } else viewPager.setAlpha(1f);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return status;
    }

    public void openFragment() {
        Fragment BatchFragment = new PalChatFragment();
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, BatchFragment, "tag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showChangeLanguageOption() {
        if (Util.isPortraitMode(context)) return;
        langChangeLayout.setVisibility(View.VISIBLE);
        batchLayout.setVisibility(View.GONE);
    }

    private void hideChangeLanguageOption() {
        if (Util.isPortraitMode(context)) return;
        langChangeLayout.setVisibility(View.GONE);
        batchLayout.setVisibility(View.VISIBLE);
    }

    private void getRecentMessages() {
        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child(ApplicationConstants.STUDENT).child(Util.getUserId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, Object> recentMessageHashMap = (HashMap<String, Object>) snapshot.getValue();
                        ArrayList<HashMap<String, String>> recentMessagesArrayList = new ArrayList<>();
                        for (String key : recentMessageHashMap.keySet()) {
                            HashMap<String, String> data = (HashMap<String, String>) recentMessageHashMap.get(key);
                            recentMessagesArrayList.add(data);
                        }

                        PalRecentChatAdapter palRecentChatAdapter = new PalRecentChatAdapter(context, recentMessagesArrayList);
                        message_recycler_view.setAdapter(palRecentChatAdapter);
                        palRecentChatAdapter.SetOnItemClickListener(new PalRecentChatAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                                viewPager.setCurrentItem(0);
                                if (contentArrayList != null) {
                                    for (int i = 0; i <= contentArrayList.size() - 1; i++) {
                                        HashMap<String, String> batchInfo = contentArrayList.get(i);
                                        String batchTeacherID = batchInfo.get("facilitatorId");
                                        String chatTeacherID = recentMessagesArrayList.get(position).get("userId");
                                        if (batchTeacherID.equals(chatTeacherID)) {
                                            String teacherID = contentArrayList.get(i).get("facilitatorId");
                                            String teacherName = contentArrayList.get(i).get("teacherName");
                                            String batchID = contentArrayList.get(i).get("batchID");
                                            String batchName = contentArrayList.get(i).get("name").split("-")[3];
                                            String[] sp = contentArrayList.get(i).get("name").split("-");
                                            String id = sp[0] + "-" + sp[1] + "-" + sp[2] + "-" + sp[3] + "-" + sp[4] + "-" + sp[6] + "-" + sp[7];
                                            selectedBatchCode = batchID;
                                            global.setBatchName(batchName);
                                            global.setBatchID(batchID);
                                            global.setChatID(teacherID);
                                            global.setChatName(teacherName);
                                            global.setBatchCode(id);
                                            FirebaseMessaging.getInstance().subscribeToTopic(batchID);

                                            if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
                                                hideChangeLanguageOption();
                                                if (Util.isPortraitMode(context)) {
                                                    Intent intent = new Intent(context, BatchActivity.class);
                                                    intent.putExtra("classID", sp[1]);
                                                    intent.putExtra("batchName", batchName);
                                                    intent.putExtra("batchID",sp[1]);
                                                    startActivity(intent);
                                                } else {
                                                    //openFragment();
                                                    PalChatFragment.getMessages();
                                                    setPagerAdapter();
                                                    setTabLayout();
                                                    viewPager.setCurrentItem(0);
                                                }
                                            } else showChangeLanguageOption();

                                            facilitatorClassesAdapter.setPosition(position);
                                            facilitatorClassesAdapter.notifyDataSetChanged();
                                            return;

                                        }

                                    }
                                }

                            }
                        });
                        noChatText.setVisibility(View.GONE);
                    } else noChatText.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRecentAssignments() {

        ArrayList<HashMap<String, Object>> recentAssignment = UserActivities.getLocalAssignment();

        if (recentAssignment.size() != 0) {
            PalRecentChatAdapter palRecentChatAdapter = new PalRecentChatAdapter(context, recentAssignment, "Assignment");
            assignment_recycler_view.setAdapter(palRecentChatAdapter);
            palRecentChatAdapter.SetOnItemClickListener(new PalRecentChatAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                                viewPager.setCurrentItem(0);
                    if (contentArrayList != null) {
                        for (int i = 0; i <= contentArrayList.size() - 1; i++) {
                            HashMap<String, String> batchInfo = contentArrayList.get(i);
                            String batchTeacherID = batchInfo.get("batchID");
                            String batchid = recentAssignment.get(position).get("batchID").toString();
                            if (batchTeacherID.equals(batchid)) {
                                String teacherID = contentArrayList.get(i).get("facilitatorId");
                                String teacherName = contentArrayList.get(i).get("teacherName");
                                String batchID = contentArrayList.get(i).get("batchID");
                                String batchName = contentArrayList.get(i).get("name").split("-")[3];
                                String[] sp = contentArrayList.get(i).get("name").split("-");
                                String id = sp[0] + "-" + sp[1] + "-" + sp[2] + "-" + sp[3] + "-" + sp[4] + "-" + sp[6] + "-" + sp[7];
                                selectedBatchCode = batchID;
                                global.setBatchName(batchName);
                                global.setBatchID(batchID);
                                global.setChatID(teacherID);
                                global.setChatName(teacherName);
                                global.setBatchCode(id);
                                FirebaseMessaging.getInstance().subscribeToTopic(batchID);

                                if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
                                    hideChangeLanguageOption();
                                    if (Util.isPortraitMode(context)) {
                                        Intent intent = new Intent(context, BatchActivity.class);
                                        intent.putExtra("classID", sp[1]);
                                        intent.putExtra("batchName", batchName);
                                        intent.putExtra("batchID",sp[1]);
                                        startActivity(intent);
                                    } else {
                                        //openFragment();
                                        PalChatFragment.getMessages();
                                        setPagerAdapter();
                                        setTabLayout();
                                        viewPager.setCurrentItem(1);
                                    }
                                } else showChangeLanguageOption();

                                facilitatorClassesAdapter.setPosition(position);
                                facilitatorClassesAdapter.notifyDataSetChanged();
                                return;

                            }

                        }
                    }

                }
            });
            noAssignmentText.setVisibility(View.GONE);

        } else noAssignmentText.setVisibility(View.VISIBLE);

//        global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child("assignments").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.getValue() != null) {
//                    HashMap<String, Object> recentMessageHashMap = (HashMap<String, Object>) snapshot.getValue();
//                    ArrayList<HashMap<String, String>> recentMessagesArrayList = new ArrayList<>();
//                    for (String key : recentMessageHashMap.keySet()) {
//                        HashMap<String, String> data = (HashMap<String, String>) recentMessageHashMap.get(key);
//                        recentMessagesArrayList.add(data);
//                    }
//
//                    PalRecentChatAdapter palRecentChatAdapter = new PalRecentChatAdapter(context, UserActivities.getLocalAssignment(),"Assignment");
//                    assignment_recycler_view.setAdapter(palRecentChatAdapter);
//                    palRecentChatAdapter.SetOnItemClickListener(new PalRecentChatAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
////                                viewPager.setCurrentItem(0);
//                            if(contentArrayList!=null) {
//                                for(int i=0;i<=contentArrayList.size()-1;i++) {
//                                    HashMap<String, String> batchInfo=contentArrayList.get(i);
//                                    String batchTeacherID=batchInfo.get("batchID");
//                                    String batchid =recentMessagesArrayList.get(position).get("batchID");
//                                    if(batchTeacherID.equals(batchid)) {
//                                        String teacherID = contentArrayList.get(i).get("facilitatorId");
//                                        String teacherName = contentArrayList.get(i).get("teacherName");
//                                        String batchID = contentArrayList.get(i).get("batchID");
//                                        String batchName = contentArrayList.get(i).get("name").split("-")[3];
//                                        String[] sp=contentArrayList.get(i).get("name").split("-");
//                                        String id = sp[0]+"-"+sp[1]+"-"+sp[2]+"-"+sp[3]+"-"+sp[4]+"-"+sp[6]+"-"+sp[7];
//                                        selectedBatchCode=batchID;
//                                        global.setBatchName(batchName);
//                                        global.setBatchID(batchID);
//                                        global.setChatID(teacherID);
//                                        global.setChatName(teacherName);
//                                        global.setBatchCode(id);
//                                        FirebaseMessaging.getInstance().subscribeToTopic(batchID);
//
//                                        if (sp[2].equalsIgnoreCase(Util.getSelectedLanguage(context))) {
//                                            hideChangeLanguageOption();
//                                            if(Util.isPortraitMode(context)){
//                                                Intent intent=new Intent(context, BatchActivity.class);
//                                                intent.putExtra("classID",sp[1]);
//                                                intent.putExtra("batchName",batchName);
//                                                startActivity(intent);
//                                            }
//                                            else {
//                                                //openFragment();
//                                                PalChatFragment.getMessages();
//                                                setPagerAdapter();
//                                                setTabLayout();
//                                                viewPager.setCurrentItem(1);
//                                            }
//                                        }
//                                        else showChangeLanguageOption();
//
//                                        facilitatorClassesAdapter.setPosition(position);
//                                        facilitatorClassesAdapter.notifyDataSetChanged();
//                                        return;
//
//                                    }
//
//                                }
//                            }
//
//                        }
//                    });
//                    noAssignmentText.setVisibility(View.GONE);
//                }
//                else noAssignmentText.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void openCodeEnterDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_code_enter);
        dialog.setCancelable(true);

        try {
            TextView joinBatchTitle=dialog.findViewById(R.id.joinBatchTitle);
            TextView joinBatchDescription=dialog.findViewById(R.id.joinBatchDescription);
            joinBatchTitle.setText(textArrayList.get(1));
            joinBatchDescription.setText(textArrayList.get(21));
            TextView textViewJoinBatch = dialog.findViewById(R.id.textViewJoinBatch);
            TextView codeEditText = dialog.findViewById(R.id.codeEditText);
            textViewJoinBatch.setText(textArrayList.get(1));
            textViewJoinBatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = codeEditText.getText().toString().replace(".", "").trim();
                    if (TextUtils.isEmpty(code)) if(Util.getSelectedLanguage(context).equals("hindi")) codeEditText.setError("कृपया कोड दर्ज करें");
                    else codeEditText.setError("Please enter code");
                    else if (code.length() != 4) if(Util.getSelectedLanguage(context).equals("hindi")) codeEditText.setError("कोड की लंबाई 4 होनी चाहिए");
                    else codeEditText.setError("Code Length should be 4");
                    else if (!Util.isNetworkAvailable(context)) if(Util.getSelectedLanguage(context).equals("hindi")) Util.showToast(context, "इंटरनेट उपलब्ध नहीं है");
                    else Util.showToast(context, "Internet is not available");
                    if(code.contains(".") || code.contains("#") || code.contains("$") || code.contains("[") ||code.contains("]") ) Toast.makeText(context, "Wrong batch code", Toast.LENGTH_SHORT).show();
                    else if(batchesList.contains(code)) {
                        Toast.makeText(context, "Already Joined", Toast.LENGTH_SHORT).show();
                    }
                    else validateClassCode(code, dialog);




                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.show();
    }

    /** Validate Batch code */
    private void validateClassCode(String code, Dialog dialog) {

        global.getDatabaseReference().child("batches").child(code).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                    map.put("code",code);
                    setStaticText(map, dialog);
                } else {
                    Util.showToast(context, invalidCode);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child("ClassesByTeacherCode").child(code).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    if (dataSnapshot.getValue() != null) {
//                        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
//                        setStaticText(map, dialog);
//                    } else {
//                        Util.showToast(context, invalidCode);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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
//        dummyModel.setJoiningDate(Util.getCurrentDateWithDifferentFormat());
        dummyModel.setUserType("Anonymous");

        String facilitatorId = map.get("teacherID");
        String teacherName = map.get("teacherName");
        String boardID = map.get("boardID");
        String classID = map.get("classID");
        String language = map.get("language");
        String subject = map.get("subject");
        String className = map.get("className");
        String referCode = map.get("referCode");
        String data = boardID + "-" + classID + "-" + language + "-" + className + "-" + subject + "-" + teacherName + "-" + referCode;
        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facilitatorId).child(data).child("List").child(Util.getUserId(context)).setValue(dummyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Util.dismissDialog();
                if (task.getException() != null) Util.showToast(context, error);
                else {
                    dialog.dismiss();
                    Util.setFacilitatorNeedToSynced(context, true);
                    getBatches();
                    Util.showSuccessDialog(context, addedSuccess, okay);
                }
            }
        });
    }

    private void setDataToFirebase(FacilitatorStudentModel teacherInfo) {

//        HashMap<String,String> batchInfo =new HashMap<>();
//        batchInfo.put("class",Util.getSelectedClass(context));
//        batchInfo.put("board",Util.getSelectedBoard(context));
//        batchInfo.put("batchName",teacherInfo.getClassName());
//        batchInfo.put("createdAt",System.currentTimeMillis()+"");

        global.getDatabaseReference().child("batches").child(teacherInfo.getBatchID()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!=null) {
                    FacilitatorClassModel facilitatorClassModel = snapshot.getValue(FacilitatorClassModel.class);

                    /** adding batch info in student section node */
                    global.getDatabaseReference().child("students").child(Util.getUserId(context))
                            .child("batches").child(teacherInfo.getBatchID()).child("userName").setValue(Util.getUsernameShowable(context));;

                    /** adding batch info in student section node */
                    global.getDatabaseReference().child("students").child(Util.getUserId(context))
                            .child("batches").child(teacherInfo.getBatchID()).child("info").setValue(facilitatorClassModel);

                    /** adding batch info in teacher section node */
                    global.getDatabaseReference().child("teachers").child(teacherInfo.getFacilitatorId()).child("batches")
                            .child(teacherInfo.getBatchID()).child("info").setValue(facilitatorClassModel);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /** adding student info in batches section node */
        global.getDatabaseReference().child("batches").child(teacherInfo.getBatchID()).child("students")
                .child(Util.getUserId(context)).child("userName").setValue(Util.getUsernameShowable(context));

        /** adding student info in teacher section node */
        global.getDatabaseReference().child("teachers").child(teacherInfo.getFacilitatorId()).child("students")
                .child(Util.getUserId(context)).child(teacherInfo.getBatchID()).child("userName").setValue(Util.getUsernameShowable(context));;

        /** adding student info in teacher section node */
        global.getDatabaseReference().child("teachers").child(teacherInfo.getFacilitatorId()).child("batches")
                .child(teacherInfo.getBatchID()).child("students").child(Util.getUserId(context)).child("userName").setValue(Util.getUsernameShowable(context));;

        Util.dismissDialog();
    }

    public void addFacilitatorToStudentData(final HashMap<String, String> map, Dialog dialog) throws Exception {
        FacilitatorStudentModel facilitatorStudentModel = new FacilitatorStudentModel();
        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className") + "-" + map.get("subject") + "-" + map.get("teacherID") + "-" + map.get("teacherName") + "-" + map.get("referCode");
//        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className") + "-" + "gs" + "-" + "g" + "-" + "f" + "-" + map.get("referCode");
//        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className");
        facilitatorStudentModel.setName(name);
        facilitatorStudentModel.setClassName(map.get("className"));
        facilitatorStudentModel.setFacilitatorId(map.get("teacherID"));
        facilitatorStudentModel.setSubject(map.get("subject"));
        facilitatorStudentModel.setTeacherName(map.get("teacherName"));
        facilitatorStudentModel.setBatchID(map.get("shareCode"));
        facilitatorStudentModel.setUserType("Anonymous");


        global.getDatabaseReference().child(ApplicationConstants.USER_DATA+"/"+Util.getUserId(context)+"/"+ApplicationConstants.FACILITATOR)
                .child(facilitatorStudentModel.getName())
                .setValue(facilitatorStudentModel, new DatabaseReference.CompletionListener() {
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

        setDataToFirebase(facilitatorStudentModel);
    }

    /** get teacher details for student */
    public void getFacilitatorForStudent(HashMap<String, String> map, Dialog dialog) throws Exception {
        //C_E_B-2-English-test-Math_-kSAaqdN8oGezoOha9SKpZGocRX63-Anurag-TO17XZ
        String name = map.get("boardID") + "-" + map.get("classID") + "-" + map.get("language") + "-" + map.get("className") + "-" + map.get("subject") + "-" + map.get("teacherID") + "-" + map.get("teacherName") + "-" + map.get("referCode");
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        HashMap<String, String> facHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                        String facilitatorId = facHashmap.get("facilitatorId");
                        String newFacId = map.get("teacherID"); //code.split("-")[4];
                        if (facilitatorId.equalsIgnoreCase(newFacId)) {
                            Util.dismissDialog();
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

    private void setLayoutText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject("english");
                JSONObject object_ = object.getJSONObject("Batches_Screen");
                classesTextView.setText((String) object_.get("classes_title"));
                recentMessagesTextView.setText((String) object_.get("recent_messages_text"));
                recentAssignmentTextView.setText((String) object_.get("recent_assignment_text"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            global.databaseReference.child("pal_test").child("labels").child("english").child("Batches_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            classesTextView.setText(dataHashMap.get("classes_title"));
                            recentMessagesTextView.setText(dataHashMap.get("recent_messages_text"));
                            recentAssignmentTextView.setText(dataHashMap.get("recent_assignment_text"));
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

    public void setStaticText() throws Exception {


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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();

                            boardDifferent = textArrayList.get(2);
                            classDifferent = textArrayList.get(3);
                            langaugeDiferenr = textArrayList.get(4);
                            batchNotExist = textArrayList.get(5);
                            enterCode = textArrayList.get(6);
                            invalidCode = textArrayList.get(7);
                            buttonText = textArrayList.get(8);
                            textInfo = textArrayList.get(9);
                            join = textArrayList.get(10);
                            textView.setText(textArrayList.get(0));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Facilitator classes Screen").addValueEventListener(valueEventListener);

        }

    }

    private void setStaticTextToFireBase() {
        ArrayList<String> al = new ArrayList<>();
        al.clear();

        al.add("Classes");
        al.add("Join Batch");
        al.add("Recent Messages");
        al.add("Recent Assignments");
        al.add("Assigned");
        al.add("Messages");
        al.add("Notifications");
        al.add("Mark all as read");
        al.add("No Assignment Assigned");
        al.add("No Chat found");
        al.add("Type a message");
        al.add("To start the practice, you have to first complete the diagnostic test.");
        al.add("You have to complete the diagnostic test for play this video.");
        al.add("Start Diagnostic Test");
        al.add("Practice");
        al.add("Book");
        al.add("Video");
        al.add("Diagnostic Test");
        al.add("Final Test");
        al.add("Start");
        al.add("Due");
        al.add("Enter Invitation Code");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("screen_text").child("coach").child("1").child("english").child("Batches Fragments").setValue(al);

        al.clear();

        al.add("कक्षाएँ");
        al.add("ज्वाइन बैच");
        al.add("रीसेंट बैच");
        al.add("रीसेंट असाइनमेंट");
        al.add("एसाइन्ड");
        al.add("संदेश");
        al.add("सूचनाएं");
        al.add("पढ़ा हुआ मार्क करें");
        al.add("कोई कार्यभार नहीं सौंपा गया");
        al.add("कोई चैट नहीं मिली");
        al.add("संदेश लिखें");
        al.add("अभ्यास शुरू करने के लिए, आपको पहले डायग्नोस्टिक टेस्ट पूरा करना होगा।");
        al.add("इस वीडियो को चलाने के लिए आपको डायग्नोस्टिक टेस्ट पूरा करना होगा।");
        al.add("डायग्नोस्टिक टेस्ट शुरू करें");
        al.add("प्रैक्टिस");
        al.add("पुस्तक");
        al.add("वीडियो");
        al.add("डायग्नोस्टिक टेस्ट");
        al.add("अंतिम टेस्ट");
        al.add("शुरू");
        al.add("ड्यू-डेट");
        al.add("बैच कोड दर्ज करें");

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("screen_text").child("coach").child("1").child("hindi").child("Batches Fragments").setValue(al);
    }

    private void setStaticTextFromFireBase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("screen_text").child("coach").child("1")
                .child(Util.getSelectedLanguage(context))
                .child("Batches Fragments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                                classesTextView.setText(textArrayList.get(0));
                                joinNewBatch.setText(textArrayList.get(1));
                                recentMessagesTextView.setText(textArrayList.get(2));
                                recentAssignmentTextView.setText(textArrayList.get(3));
                                invalidCode=textArrayList.get(24);
                                langChangeLayout.setText(textArrayList.get(22)+"\n"+textArrayList.get(23));
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
                            String className = Util.getSelectedClass(context);
                            String boardID = Util.getSelectedBoard(context);
                            String language = Util.getSelectedLanguagePackage(context);
                            String board = map.get("boardID");
                            String sClass = map.get("classID");
                            String slanguage = map.get("language");
                            if (!className.equalsIgnoreCase(sClass)) Toast.makeText(context, "Class not same", Toast.LENGTH_SHORT).show();
                            else if (!boardID.equalsIgnoreCase(board)) Toast.makeText(context, "board not same", Toast.LENGTH_SHORT).show();
                            else if (!language.equalsIgnoreCase(slanguage)) Toast.makeText(context, "language not same", Toast.LENGTH_SHORT).show();
//                            else getFacilitatorForStudent(map, dialog);

                            else addBatchInfo(map, dialog);

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

    private void addBatchInfo(HashMap<String, String> map, Dialog dialog)  {

        String batchCode=map.get("code");

        /** student batch info node referance */
        DatabaseReference studentDf=global.getDatabaseReference().child("students").child(Util.getUserId(context)).child(batchCode);

        /** check student already added in batch or not */
        studentDf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!=null) {

                    Util.dismissDialog();
                    Util.showToast(context, alreadyAdded);

                } else {
                    FacilitatorStudentModel facilitatorStudentModel = new FacilitatorStudentModel();

                    facilitatorStudentModel.setClassName(map.get("className"));
                    facilitatorStudentModel.setFacilitatorId(map.get("teacherID"));
                    facilitatorStudentModel.setSubject(map.get("subject"));
                    facilitatorStudentModel.setTeacherName(map.get("teacherName"));
                    facilitatorStudentModel.setBatchID(map.get("shareCode"));
                    facilitatorStudentModel.setUserType("Anonymous");

                    setDataToFirebase(facilitatorStudentModel);

                }
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
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
    private String joinClassText;
    private String classDifferent;
    private String langaugeDiferenr;


}
