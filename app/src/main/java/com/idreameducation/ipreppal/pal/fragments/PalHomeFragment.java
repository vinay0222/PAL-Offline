package com.idreameducation.ipreppal.pal.fragments;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.sClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.ProjectVideos_topic_Activity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.SubjectInfoModel;
import com.idreameducation.ipreppal.model.SubjectsModel;
import com.idreameducation.ipreppal.model.simulationModel.SimulationChaptersModel;
import com.idreameducation.ipreppal.model.simulationModel.SimulationSubjectModel;
import com.idreameducation.ipreppal.model.simulationModel.SimulationTopicsModel;
import com.idreameducation.ipreppal.pal.activity.ActivityVideosListingActivity;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.SimulationTopicsListingActivity;
import com.idreameducation.ipreppal.pal.activity.StemProjectsListingActivity;
import com.idreameducation.ipreppal.pal.adapter.ExtraContentAdapter;
import com.idreameducation.ipreppal.pal.adapter.HomeBooksAdapter;
import com.idreameducation.ipreppal.pal.adapter.PalSubjectsAdapter;
import com.idreameducation.ipreppal.pal.adapter.StemSubjectsAdapter;
import com.idreameducation.ipreppal.pal.adapter.simulationAdapters.SimulationSubjectsAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.ActivationModel;
import com.idreameducation.ipreppal.roomdatabase.model.LastTopicDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ActivationDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.LastTopicDetailsRepository;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PalHomeFragment extends Fragment {

    private Context context;
    private Global global;

    private RelativeLayout subjectsLayout;
    private LinearLayout lastTopicLayout;

    private RecyclerView mRecyclerView,recyclerViewBooks,recyclerViewStem,recyclerViewSimulation;
    private String studentClass,board,language,lastTopicId,textViewBooksText,you_were_learning, wish_to_continue;

    private ImageView subjectImageView,closeImg,imageViewCrossVideo2;

    private TextView textViewTitle,textViewBooks,textViewStem,textViewContinue,textViewMsg,slow_internet_Text
            ,textViewTestSeeAll,textViewStemSeeAll,textViewBooksSeeAll,textViewsimulation,audioBooksTitle;

    private boolean isloading=true,isDataLoaded = false;
    private byte[] private_key;

    public static ArrayList<Integer> topicLengthArrayList = new ArrayList<>();
    public static ArrayList<String> topicArrayList = new ArrayList<>();
    public static ArrayList<String> topicArrayListid = new ArrayList<>();
    public static ArrayList<String> subjectArrayList = new ArrayList<>();
    private ArrayList<String> textArrayList;
    ArrayList<String> simulationSubjects;
    ArrayList<String> audioBookSubjects;

    private LastTopicDetailsRepository lastTopicDetailsRepository;
    private PalSubjectsAdapter palSubjectsAdapter;
    private RequestOptions requestOptions;
    private Disposable disposable;
    Handler handler=new Handler();
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_home, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                assignIds(view);
                listners();
            }
        },350);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getLastTopicDetails();
    }
    @SuppressLint("CheckResult")
    private void assignIds(View view) {

        /** Subscribe user to there user id & all for notification */
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic(Util.getUserId(context));
        FirebaseMessaging.getInstance().subscribeToTopic("vinay");


        Util.getSelectedState(context);

        /** init views */
        imageViewCrossVideo2=view.findViewById(R.id.imageViewCrossVideo2);
        slow_internet_Text=view.findViewById(R.id.slow_internet_Text);
        mProgressBar=view.findViewById(R.id.progressBar);

        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewStem = view.findViewById(R.id.recyclerViewStem);
        recyclerViewSimulation = view.findViewById(R.id.recyclerViewSimulation);
        subjectsLayout = view.findViewById(R.id.subjectsLayout);
        audioBooksRecyclerView = view.findViewById(R.id.audioBooksRecyclerView);

        audioBooksRecyclerView.setVisibility(View.GONE);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewBooks = view.findViewById(R.id.textViewBooks);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        textViewStem = view.findViewById(R.id.textViewStem);

        lastTopicLayout = view.findViewById(R.id.lastTopicLayout);
        subjectImageView = view.findViewById(R.id.subjectImageView);
        textViewContinue = view.findViewById(R.id.textViewContinue);
        textViewMsg = view.findViewById(R.id.textViewMsg);
        closeImg = view.findViewById(R.id.closeImg);

        textViewBooksSeeAll = view.findViewById(R.id.textViewBooksSeeAll);
        textViewTestSeeAll = view.findViewById(R.id.textViewTestSeeAll);
        textViewStemSeeAll = view.findViewById(R.id.textViewStemSeeAll);
        textViewsimulation = view.findViewById(R.id.textViewSimulation);
        audioBooksTitle = view.findViewById(R.id.audioBooksTitle);

        audioBooksTitle.setVisibility(View.GONE);

        lastTopicDetailsRepository = new LastTopicDetailsRepository(context);

        language = Util.getSelectedLanguagePackage(context);
        studentClass = Util.getSelectedClass(context);
        board = Util.getSelectedBoard(context);
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        lastTopicLayout.setVisibility(View.GONE);
        subjectsLayout.setVisibility(View.VISIBLE);


        if (studentClass.equalsIgnoreCase("11")||studentClass.equalsIgnoreCase("12")) {
            studentClass = Util.getStreamSelectedClass(context);
            Util.setClassSelection(context,Util.getStreamSelectedClass(context));
            Util.setClassNameSelection(context,Util.getStreamSelectedClass(context));
        }

        if(Util.getSchoolId(context)==null) getNGODetails(Util.getNGOID(context),Util.getTABID(context),Util.getAPPID(context));

        /** print userID in logs */
        try {
            Log.e("user id", Util.getUserId(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecyclerView.setHasFixedSize(true);
        recyclerViewBooks.setHasFixedSize(true);
        recyclerViewStem.setHasFixedSize(true);

        /** set layout manager to subjects,Books & project videos recyclerview
         * according to portrait mode/Landscapee mode  */
        LinearLayoutManager managerHorizontally1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerHorizontally2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerHorizontally3 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerHorizontally4 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerHorizontally5 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        /** Subjects Layout */
        if(Util.isPortraitMode(context)) mRecyclerView.setLayoutManager(new GridLayoutManager(context,4));
        else mRecyclerView.setLayoutManager(managerHorizontally1);

        /** Books Layout */
        if(Util.isPortraitMode(context)) recyclerViewBooks.setLayoutManager(new GridLayoutManager(context,2));
        else recyclerViewBooks.setLayoutManager(managerHorizontally2);

        /** Project videos Layout */
        if(Util.isPortraitMode(context)) recyclerViewStem.setLayoutManager(new GridLayoutManager(context,2));
        else recyclerViewStem.setLayoutManager(managerHorizontally3);

        /** Simulation Layout */
        if(Util.isPortraitMode(context)) recyclerViewSimulation.setLayoutManager(new GridLayoutManager(context,2));
        else recyclerViewSimulation.setLayoutManager(managerHorizontally4);

        /** AudioBook Layout */
        if(Util.isPortraitMode(context)) audioBooksRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        else audioBooksRecyclerView.setLayoutManager(managerHorizontally5);

        try {
            setStaticText();
            getSubjects();
            getSubjectsLayoutHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** get text of screen */
    public void setStaticText() {
        if (Util.isOfflineMode(context)) try {
            textArrayList = new ArrayList<>();

            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";                    // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);                       // read file
            JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));    // filter by language
            JSONArray array = object.getJSONArray("homeScreen");                          // get homeScreen text

            /** adding text in textArrayList one by one */
            for (int i = 0; i < array.length(); i++) textArrayList.add(array.getString(i));

            /** set text in views */
            if (textArrayList.size() > 0) {
                textViewBooksSeeAll.setText(textArrayList.get(3));
                textViewTitle.setText(textArrayList.get(0));
                textViewBooks.setText(textArrayList.get(1));
                textViewStem.setText(textArrayList.get(2));
                you_were_learning = textArrayList.get(8);
                textViewBooksText= textArrayList.get(1);
                wish_to_continue = textArrayList.get(9);
            }

            if(Util.getSelectedLanguage(context).equals("hindi")) {
                textViewsimulation.setText("सिमुलेशन परियोजनाएँ");
            } else
                textViewsimulation.setText("Simulation Projects");


        } catch (Exception e) {
            e.printStackTrace();
        }
        else global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("homeScreen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        /** getting data from firebase */
                        textArrayList = (ArrayList<String>) dataSnapshot.getValue();

                        /** set data in views */
                        if (textArrayList.size() > 0) {
                            textViewBooksSeeAll.setText(textArrayList.get(3));
                            textViewTitle.setText(textArrayList.get(0));
                            textViewBooks.setText(textArrayList.get(1));
                            textViewStem.setText(textArrayList.get(2));
                            you_were_learning = textArrayList.get(8);
                            textViewBooksText= textArrayList.get(1);
                            wish_to_continue = textArrayList.get(9);
                        }
                        if(Util.getSelectedLanguage(context).equals("hindi")) {
                            textViewsimulation.setText("सिमुलेशन परियोजनाएँ");
                        } else
                            textViewsimulation.setText("Simulation Projects");

                    }
                } catch (Exception e){
                    Util.dismissdataDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /** get Subjects */
    private void getSubjects() throws Exception {
        isloading=true;
        checkConnection(false);

        if (Util.isOfflineMode(context)) try {
            String filePath = ".iDream_content/offlinetab_PAL/subjects.txt";            // file path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);           // read file
            JSONObject object = jsonObject.getJSONObject(board);                    // filter by board
            JSONObject object_ = object.getJSONObject(language);                    // filter by language
            JSONArray array = object_.getJSONArray(studentClass);                   // filter by class

            ArrayList<HashMap<String, String>> subjectArrayList = new ArrayList<>();
            HashMap<String, SubjectInfoModel> subjectMap=new HashMap<>();

            /** add subject info in subjectArrayList*/
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> subjectHashMap = new HashMap<>();
                JSONObject innerObject = array.getJSONObject(i);                    // point all subjects by position
                String icon = innerObject.getString("icon");
                String name = innerObject.getString("name");
                String id = innerObject.getString("id");
                String color = innerObject.getString("color");
                subjectHashMap.put("icon", icon);
                subjectHashMap.put("color", color);
                subjectHashMap.put("name", name);
                subjectHashMap.put("id", id);

                SubjectInfoModel subjectInfoModel=new SubjectInfoModel();
                subjectInfoModel.setColor(color);
                subjectInfoModel.setIcon(icon);
                subjectInfoModel.setId(id);
                subjectInfoModel.setName(name);
                subjectInfoModel.setShort_name(name);

                subjectMap.put(id,subjectInfoModel);

                /** we add subject info in hashMap(subjectHashMap)
                 * and we add all subject info in arraylist(subjectArrayList)*/
                subjectArrayList.add(subjectHashMap);
            }

            /** set data to adapter */
            PalSubjectsAdapter palSubjectsAdapter = new PalSubjectsAdapter(context, subjectArrayList, getViewLifecycleOwner());
            mRecyclerView.setAdapter(palSubjectsAdapter);
            palSubjectsAdapter.SetOnItemClickListener(new PalSubjectsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Util.preventTwoClick(view);
                    Intent intent;
                    /** check is portrait mode or not & start next activity accordingly */
                    if(Util.isPortraitMode(context)) intent = new Intent(context, PalContentListingActivity_Mobile.class);
                    else intent = new Intent(context, PalContentListingActivity.class);

                    /** getting subject icon */
                    Uri uri = null;
                    String icon = null;

                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/Subject_Icons/"+ subjectArrayList.get(position).get("name")+".png";
                    File file = new File(filePath);
                    if(file.exists()) uri = Uri.fromFile(file);
                    if(uri != null) icon = uri.toString();

                    /** getting data of subject & send to next activity */
                    String subject = subjectArrayList.get(position).get("id");
                    String subjectName = subjectArrayList.get(position).get("name");
                    String color = subjectArrayList.get(position).get("color");

                    intent.putExtra("sClass", studentClass);
                    intent.putExtra("board", board);
                    intent.putExtra("subject", subject);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("icon", icon);
                    intent.putExtra("color", color);
                    intent.putExtra("backToScreen", false);
                    Util.setSubject(context, subject);
                    Util.setSubjectId(context, subject);
                    Util.setVideoLevel(context, 0);
                    Util.setSubjectName(context, subjectName);
                    PalContentListingActivity.autoPlayDelay=2000;
                    PalContentListingActivity.connectionLayoutDelay=2000;
                    global.setColor(color);
                    startActivity(intent);


                    ((PracticeTopicActivity)context).hideTransparentBackgroundLayout();
                }
            });


            syncSubjects(subjectArrayList);

            Util.saveSubjectInfo(subjectMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        else global.getDatabaseReference().child(ApplicationConstants.SUBJECTS).child(board).child(language).child(studentClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        /** getting subject from firebase */
                        ArrayList<HashMap<String, String>> subjectArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                        /** filter subjects & show only visible subjects */
                        ArrayList<HashMap<String, String>> filteredSubjectArrayList =new ArrayList<>();
                        HashMap<String, SubjectInfoModel> subjectMap=new HashMap<>();

                        for(int i=0;i<=subjectArrayList.size()-1;i++)  {
                            if(Boolean.parseBoolean(String.valueOf(subjectArrayList.get(i).get("visibility")))) filteredSubjectArrayList.add(subjectArrayList.get(i));


                            String icon = subjectArrayList.get(i).get("icon");
                            String name = subjectArrayList.get(i).get("name");
                            String id = subjectArrayList.get(i).get("id");
                            String color = subjectArrayList.get(i).get("color");
                            SubjectInfoModel subjectInfoModel=new SubjectInfoModel();
                            subjectInfoModel.setColor(color);
                            subjectInfoModel.setIcon(icon);
                            subjectInfoModel.setId(id);
                            subjectInfoModel.setName(name);
                            subjectInfoModel.setShort_name(name);

                            subjectMap.put(id,subjectInfoModel);


                        }

                        /** set filtered Subject list in adapter */
                        PalSubjectsAdapter palSubjectsAdapter = new PalSubjectsAdapter(context, filteredSubjectArrayList, getViewLifecycleOwner());

                        Util.saveSubjectInfo(subjectMap);
                        palSubjectsAdapter.SetOnItemClickListener(new PalSubjectsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Util.preventTwoClick(view);
                                Intent intent;

                                /** check is portrait mode or not & start next activity accordingly */
                                if(Util.isPortraitMode(context)) intent = new Intent(context, PalContentListingActivity_Mobile.class);
                                else intent = new Intent(context, PalContentListingActivity.class);

                                /** getting data of subject & send to next activity */
                                String icon = subjectArrayList.get(position).get("icon");
                                String subject = subjectArrayList.get(position).get("id");
                                String subjectName = subjectArrayList.get(position).get("name");
                                String color = subjectArrayList.get(position).get("color");

                                intent.putExtra("sClass", studentClass);
                                intent.putExtra("board", board);
                                intent.putExtra("subject", subject);
                                intent.putExtra("subjectName", subjectName);
                                intent.putExtra("icon", icon);
                                intent.putExtra("color", color);
                                intent.putExtra("backToScreen", false);
                                Util.setSubjectName(context, subjectName);
                                Util.setVideoLevel(context, 0);
                                Util.setSubject(context, subject);
                                PalContentListingActivity.autoPlayDelay=2000;
                                PalContentListingActivity.connectionLayoutDelay=2000;
                                global.setColor(color);
                                startActivity(intent);


                                ((PracticeTopicActivity)context).hideTransparentBackgroundLayout();
                            }
                        });

                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_right","anim",getActivity().getPackageName()));
                        mRecyclerView.setLayoutAnimation(animation);
                        palSubjectsAdapter.notifyDataSetChanged();
                        mRecyclerView.scheduleLayoutAnimation();
                        mRecyclerView.setAdapter(palSubjectsAdapter);
                        isloading=false;
                        syncSubjects(subjectArrayList);
                        hideconnection_layout();

                    } else {
                        Util.showToast(context, "Subjects not available");
                        isloading=false;
                        hideconnection_layout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getStemProjects();
        getSimulationBookInfo();
//        getAudioBookInfo();
    }

    private RecyclerView audioBooksRecyclerView;


    private void getAudioBookInfo() {
        global.getDatabaseReference().child("/audioBookIds/cbse")
                .child("english")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null) {

                            HashMap<String,SubjectInfoModel> infoModelHashMap=new HashMap<>();

                            ArrayList<SubjectsModel> subjectsModels=new ArrayList<>();

                            for(DataSnapshot ss:snapshot.getChildren()) {
                                SubjectInfoModel subjectInfoModel=ss.getValue(SubjectInfoModel.class);
//                                SubjectsModel subjectsModel=new SubjectsModel(subjectInfoModel.getId(),subjectInfoModel);
//                                subjectsModels.add(subjectsModel);
                                infoModelHashMap.put(subjectInfoModel.getId(),subjectInfoModel);
                            }

                            getAudioBooksNew(infoModelHashMap);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getSimulationBookInfo() {

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/simulationSubjectsIds.txt";            // file path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {

                JSONObject object=jsonObject.getJSONObject("cbse");
                JSONObject object1=object.getJSONObject(Util.getSelectedLanguage(context));

                HashMap<String,SubjectInfoModel> infoModelHashMap=new HashMap<>();

                ArrayList<SubjectsModel> subjectsModels=new ArrayList<>();

                Iterator<String> subjects = object1.keys();

                while (subjects.hasNext()) {

                    String key = subjects.next();

                    JSONObject jsonObject1 = object1.getJSONObject(key);

                    SubjectInfoModel subjectInfoModel=new SubjectInfoModel();
                    subjectInfoModel.setColor(jsonObject1.getString("color"));
                    subjectInfoModel.setIcon(jsonObject1.getString("icon"));
                    subjectInfoModel.setId(jsonObject1.getString("id"));
                    subjectInfoModel.setName(jsonObject1.getString("name"));
                    subjectInfoModel.setShort_name(jsonObject1.getString("short_name"));

//                                SubjectsModel subjectsModel=new SubjectsModel(subjectInfoModel.getId(),subjectInfoModel);
//                                subjectsModels.add(subjectsModel);
                    infoModelHashMap.put(subjectInfoModel.getId(),subjectInfoModel);
                }

                getSimulationProjects(infoModelHashMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else global.getDatabaseReference().child("/simulationSubjectsIds/cbse")
                .child(Util.getSelectedLanguage(context))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null) {

                            HashMap<String,SubjectInfoModel> infoModelHashMap=new HashMap<>();

                            ArrayList<SubjectsModel> subjectsModels=new ArrayList<>();

                            for(DataSnapshot ss:snapshot.getChildren()) {
                                SubjectInfoModel subjectInfoModel=ss.getValue(SubjectInfoModel.class);
//                                SubjectsModel subjectsModel=new SubjectsModel(subjectInfoModel.getId(),subjectInfoModel);
//                                subjectsModels.add(subjectsModel);
                                infoModelHashMap.put(subjectInfoModel.getId(),subjectInfoModel);
                            }

                            getSimulationProjects(infoModelHashMap);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /** fetch Simulation Projects from firebase */
    private void getSimulationProjects(HashMap<String,SubjectInfoModel> infoModelHashMap) {
        simulationSubjects=new ArrayList<>();
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/simulation.txt";            // file path
            JSONObject jsonObject11 = Util.readJsonFile(context, filePath);
            try {

                ArrayList<SimulationSubjectModel> subjectModelArrayList =new ArrayList<>();

                JSONObject jsonObject_=jsonObject11.getJSONObject(Util.getSelectedClass(context));
                JSONObject jsonObject=jsonObject_.getJSONObject(Util.getSelectedLanguage(context));

                /** getting subjects */
                Iterator<String> iterator=jsonObject.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();   // subject

                    JSONObject jsonObject1= jsonObject.getJSONObject(key);

                    /** adding chapters of current subject */
                    ArrayList<SimulationChaptersModel> chapters=new ArrayList<>();

                    JSONArray array=jsonObject1.getJSONArray("chapters");

                    for (int i=0;i<=array.length()-1;i++) {

                        JSONObject object= (JSONObject) array.get(i);

                        /** adding topics of current chapter */
                        ArrayList<SimulationTopicsModel> topics=new ArrayList<>();

                        JSONArray array1 = object.getJSONArray("topics");

                        for (int j=0;j<=array1.length()-1;j++) {

                            JSONObject object1= (JSONObject) array1.get(j);

                            SimulationTopicsModel simulationTopicsModel=new SimulationTopicsModel();
                            simulationTopicsModel.setDetail(object1.getString("detail"));
                            simulationTopicsModel.setId(object1.getString("id"));
                            simulationTopicsModel.setName(object1.getString("name"));
                            simulationTopicsModel.setOfflineLink(object1.getString("offlineLink"));
                            simulationTopicsModel.setOfflineThumbnail(object1.getString("offlineThumbnail"));
                            simulationTopicsModel.setOnlineLink(object1.getString("onlineLink"));
                            simulationTopicsModel.setSubjectID(object1.getString("subjectID"));
                            simulationTopicsModel.setThumbnail(object1.getString("thumbnail"));
                            simulationTopicsModel.setTopicName(object1.getString("topicName"));

                            topics.add(simulationTopicsModel);

                        }

                        /** creating chapter model */
                        SimulationChaptersModel simulationChaptersModel=new SimulationChaptersModel();
                        simulationChaptersModel.setName(object.getString("name"));
                        simulationChaptersModel.setTopics(topics);

                        chapters.add(simulationChaptersModel);
                    }



                    /** subject details */
                    SimulationSubjectModel subjectModel =new SimulationSubjectModel();
                    subjectModel.setChapters(chapters);
                    subjectModel.setSubjectName(infoModelHashMap.get(key).getName());     // adding subject name
                    subjectModel.setId(key);     // adding subject key
                    subjectModel.setName(key);     // adding subject name
                    subjectModel.setColor(infoModelHashMap.get(key).getColor());
                    subjectModel.setIcon(infoModelHashMap.get(key).getIcon());
                    subjectModel.setSubjectName(infoModelHashMap.get(key).getName());


                    /** adding subject in final list */
                    subjectModelArrayList.add(subjectModel);



                }

                /** show data in recyclerview */
                SimulationSubjectsAdapter simulationSubjectsAdapter=new SimulationSubjectsAdapter(subjectModelArrayList);
                recyclerViewSimulation.setAdapter(simulationSubjectsAdapter);

                recyclerViewSimulation.setVisibility(View.VISIBLE);
                textViewsimulation.setVisibility(View.VISIBLE);

                /** handle clicks */
                simulationSubjectsAdapter.SetOnItemClickListener((view, position) -> {
                    SimulationTopicsListingActivity.subjectModel=subjectModelArrayList.get(position);
                    SimulationTopicsListingActivity.contentType=SimulationTopicsListingActivity.CONTENT_TYPE_SIMULATION;
                    startActivity(new Intent(context, SimulationTopicsListingActivity.class));
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else global.getDatabaseReference().child("extra_content")
                .child(Util.getSelectedBoard(context))
                .child(Util.getSelectedLanguage(context))
                .child("10")
                .child("extra_content/simulation")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.getValue()!=null) {

                            /** Creating Arraylist which contain subjects & there all data */
                            ArrayList<SimulationSubjectModel> subjectModelArrayList=new ArrayList<>();

                            /** point to each subject and save there chapters & subject name data to subjectModelArrayList  */
                            for(DataSnapshot ss:snapshot.getChildren()) {
                                SimulationSubjectModel simulationSubjectModel=ss.getValue(SimulationSubjectModel.class);
                                simulationSubjectModel.setSubjectName(ss.getKey());     // adding subject name
                                simulationSubjectModel.setName(ss.getKey());     // adding subject name
                                simulationSubjectModel.setColor(infoModelHashMap.get(ss.getKey()).getColor());
                                simulationSubjectModel.setIcon(infoModelHashMap.get(ss.getKey()).getIcon());
                                simulationSubjectModel.setSubjectName(infoModelHashMap.get(ss.getKey()).getName());
                                subjectModelArrayList.add(simulationSubjectModel);    // adding subject data to main Arraylist
                                simulationSubjects.add(ss.getKey());                // add subject name in string list
                            }

                            /** show data in recyclerview */
                            SimulationSubjectsAdapter simulationSubjectsAdapter=new SimulationSubjectsAdapter(subjectModelArrayList);
                            recyclerViewSimulation.setAdapter(simulationSubjectsAdapter);

                            recyclerViewSimulation.setVisibility(View.VISIBLE);
                            textViewsimulation.setVisibility(View.VISIBLE);

                            /** handle clicks */
                            simulationSubjectsAdapter.SetOnItemClickListener((view, position) -> {
                                SimulationTopicsListingActivity.subjectModel=subjectModelArrayList.get(position);
                                SimulationTopicsListingActivity.contentType=SimulationTopicsListingActivity.CONTENT_TYPE_SIMULATION;
                                startActivity(new Intent(context, SimulationTopicsListingActivity.class));
                            });

                        }
                        else {
                            recyclerViewSimulation.setVisibility(View.GONE);
                            textViewsimulation.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getAudioBooksNew(HashMap<String,SubjectInfoModel> infoModelHashMap)  {
        audioBookSubjects=new ArrayList<>();
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/audioBookSubjects.txt";            // file path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
//            try {
//                JSONObject object = jsonObject.getJSONObject(board);
//                JSONObject object_ = object.getJSONObject(Util.getSelectedLanguage(context));
//                JSONArray array = object_.getJSONArray(Util.getSelectedClass(context));
//                ArrayList<HashMap<String, String>> audiobookContentArrayList = new ArrayList<>();
//                for (int i = 0; i < array.length(); i++) {
//                    HashMap<String, String> subjectHashMap = new HashMap<>();
//                    JSONObject innerObject = array.getJSONObject(i);
//                    String icon = innerObject.getString("icon");
//                    String name = innerObject.getString("name");
//                    String id = innerObject.getString("id");
//                    String color = innerObject.getString("color");
//                    subjectHashMap.put("icon", icon);
//                    subjectHashMap.put("name", name);
//                    subjectHashMap.put("id", id);
//                    subjectHashMap.put("color", color);
//                    subjectHashMap.put("categoryId", "audio_books");
//                    audiobookContentArrayList.add(subjectHashMap);
//                }
//                RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//                audioBooksRecyclerView.setLayoutManager(manager);
//                AudioBooksSubjectsAdapter audioBooksSubjectsAdapter = new AudioBooksSubjectsAdapter(context, audiobookContentArrayList, "audio_books");
//                audioBooksRecyclerView.setAdapter(audioBooksSubjectsAdapter);
//                audioBooksRecyclerView.setVisibility(View.VISIBLE);
//
//                audioBooksSubjectsAdapter.SetOnItemClickListener(new AudioBooksSubjectsAdapter.OnItemClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (!Settings.canDrawOverlays(context)) {
//                            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                            myIntent.setData(Uri.parse("package:" + context.getPackageName()));
//                            startActivityForResult(myIntent, 100);
//                        } else {
//                            Util.preventTwoClick(view);
//                            Intent intent = new Intent(context, StemProjectsListingActivity.class);
//                            String subject = audiobookContentArrayList.get(position).get("id");
//                            String subjectName = audiobookContentArrayList.get(position).get("name");
//                            Util.setSubject(context, subjectName);
//                            Util.setSubjectId(context, subject);
//                            String icon = audiobookContentArrayList.get(position).get("icon");
//                            String color = audiobookContentArrayList.get(position).get("color");
//                            intent.putExtra("sClass", sClass);
//                            intent.putExtra("board", board);
//                            intent.putExtra("subject", subject);
//                            intent.putExtra("subjectName", subjectName);
//                            intent.putExtra("icon", icon);
//                            intent.putExtra("categoryName", "audio_books");
//                            intent.putExtra("color", color);
//
//                            startActivity(intent);
//                            getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                        }
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        else
            global.getDatabaseReference().child("extra_content")
                    .child(Util.getSelectedBoard(context))
                    .child(Util.getSelectedLanguage(context))
                    .child("11_commerce")
                    .child("extra_content/audio_books")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.getValue()!=null) {

                                /** Creating Arraylist which contain subjects & there all data */
                                ArrayList<SimulationSubjectModel> subjectModelArrayList=new ArrayList<>();

                                /** point to each subject and save there chapters & subject name data to subjectModelArrayList  */
                                for(DataSnapshot ss:snapshot.getChildren()) {
                                    SimulationSubjectModel simulationSubjectModel=ss.getValue(SimulationSubjectModel.class);
                                    simulationSubjectModel.setName(ss.getKey());     // adding subject name
                                    simulationSubjectModel.setColor(infoModelHashMap.get(ss.getKey()).getColor());
                                    simulationSubjectModel.setIcon(infoModelHashMap.get(ss.getKey()).getIcon());
                                    simulationSubjectModel.setSubjectName(infoModelHashMap.get(ss.getKey()).getName());
                                    subjectModelArrayList.add(simulationSubjectModel);    // adding subject data to main Arraylist
                                    audioBookSubjects.add(ss.getKey());                // add subject name in string list
                                }

                                /** show data in recyclerview */
                                SimulationSubjectsAdapter simulationSubjectsAdapter=new SimulationSubjectsAdapter(subjectModelArrayList);
                                audioBooksRecyclerView.setAdapter(simulationSubjectsAdapter);

                                audioBooksRecyclerView.setVisibility(View.VISIBLE);
                                audioBooksTitle.setVisibility(View.VISIBLE);

                                /** handle clicks */
                                simulationSubjectsAdapter.SetOnItemClickListener((view, position) -> {
                                    SimulationTopicsListingActivity.subjectModel=subjectModelArrayList.get(position);
                                    SimulationTopicsListingActivity.contentType=SimulationTopicsListingActivity.CONTENT_TYPE_AUDIOBOOK;
                                    startActivity(new Intent(context, SimulationTopicsListingActivity.class));
                                });

                            }
                            else {
                                audioBooksRecyclerView.setVisibility(View.GONE);
                                audioBooksTitle.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

    private void syncSubjects(ArrayList<HashMap<String, String>> subjectArrayList) {
        for(int i=0;i<=subjectArrayList.size()-1;i++)
            UserActivities.addSubject(subjectArrayList.get(i).get("id"));
    }

    /** get Project videos */
    private void getStemProjects() {
        if (Util.isOfflineMode(context)) try {

            sClass=Util.getSelectedClass(context);
            String filePath = ".iDream_content/offlinetab_PAL/stemsubjects.txt";                // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);                   // read file
            JSONObject object = jsonObject.getJSONObject(Util.getSelectedBoard(context));   // filter by board
            JSONObject object_ = object.getJSONObject(Util.getSelectedLanguage(context));   // filter by language
            JSONArray object__ = object_.getJSONArray(Util.getSelectedClass(context));      // filter by class

            ArrayList<HashMap<String, String>> stemSubjectArrayList = new ArrayList<>();

            /** add subject info in stemSubjectArrayList */
            for (int i = 0; i <= object__.length()-1; i++) {
                HashMap<String, String> subjectHashMap = new HashMap<>();
                JSONObject jsonObject1=object__.getJSONObject(i);                           // point all subjects by position
                subjectHashMap.put("icon", jsonObject1.get("icon").toString());
                subjectHashMap.put("name", jsonObject1.get("name").toString());
                subjectHashMap.put("id", jsonObject1.get("id").toString());
                subjectHashMap.put("color", jsonObject1.get("color").toString());

                /** we add subject info in hashMap(subjectHashMap)
                 * and we add all subject info in arraylist(stemSubjectArrayList)*/
                stemSubjectArrayList.add(subjectHashMap);
            }


            if (stemSubjectArrayList.size() > 0) {
                recyclerViewStem.setVisibility(View.VISIBLE);
                textViewStem.setVisibility(View.VISIBLE);

                /** set data in adapter */
                StemSubjectsAdapter stemSubjectsAdapter = new StemSubjectsAdapter(context, stemSubjectArrayList);
                stemSubjectsAdapter.SetOnItemClickListener(new StemSubjectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Util.preventTwoClick(view);
                        Intent intent ;

                        /** check is portrait mode or not & start next activity accordingly */
                        if(Util.isPortraitMode(context)) intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.StemProjectsListingActivity.class);
                        else intent = new Intent(context, StemProjectsListingActivity.class);

                        /** getting data of subject & send to next activity */
                        String subject = stemSubjectArrayList.get(position).get("id");
                        String subjectName = stemSubjectArrayList.get(position).get("name");
                        String icon = stemSubjectArrayList.get(position).get("icon");
                        String color = stemSubjectArrayList.get(position).get("color");

                        intent.putExtra("sClass", studentClass);
                        intent.putExtra("board", board);
                        intent.putExtra("subject", subject);
                        intent.putExtra("subjectName", subjectName);
                        intent.putExtra("icon", icon);
                        intent.putExtra("categoryName", "video_practicals");
                        intent.putExtra("color", color);
                        Util.setSubject(context,subjectName);
                        Util.setSubjectId(context,subject);

                        startActivity(intent);

                    }
                });

                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_right","anim",getActivity().getPackageName()));
                recyclerViewStem.setAdapter(stemSubjectsAdapter);
                recyclerViewStem.setLayoutAnimation(animation);
                recyclerViewStem.setVisibility(View.VISIBLE);
            }
            else {
                recyclerViewStem.setVisibility(View.GONE);
                textViewStem.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            recyclerViewStem.setVisibility(View.GONE);
            textViewStem.setVisibility(View.GONE);
        }
        else global.getDatabaseReference().child(ApplicationConstants.SUBJECTS_STEM).child(board).child(language).child(studentClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        /** getting subject from firebase */
                        ArrayList<HashMap<String, String>>  List = (ArrayList<HashMap<String, String>>) snapshot.getValue();

                        /** filter subjects & show only visible subjects */
                        ArrayList<HashMap<String, String>> stemArrayList=new ArrayList<>();
                        for(int i=0;i<=List.size()-1;i++)  if(Boolean.parseBoolean(String.valueOf(List.get(i).get("visibility")))) stemArrayList.add(List.get(i));

                        if (stemArrayList.size() > 0) {
                            /** set filtered Subject list in adapter */
                            ExtraContentAdapter extraContentAdapter = new ExtraContentAdapter(context, stemArrayList, null, null, "Videos");
                            extraContentAdapter.SetOnItemClickListener(new ExtraContentAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Util.preventTwoClick(view);

                                    /** getting data of subject & send to next activity */
                                    Util.setSubject(context,stemArrayList.get(position).get("id"));
                                    Util.setSubjectName(context,stemArrayList.get(position).get("subjectName"));
                                    Util.setSubjectId(context,stemArrayList.get(position).get("id"));

                                    /** check is portrait mode or not & start next activity accordingly */
                                    if(Util.isPortraitMode(context)) startActivity(new Intent(context, ProjectVideos_topic_Activity.class).putExtra("color", stemArrayList.get(position).get("color")).putExtra("board", board).putExtra("sClass", studentClass).putExtra("categoryName", "activity_videos").putExtra("position", position).putExtra("subject", stemArrayList.get(position).get("id")).putExtra("subjectName", stemArrayList.get(position).get("name")).putExtra("subjects", subjectArrayList).putExtra("icon",stemArrayList.get(position).get("icon")));
                                    else startActivity(new Intent(context, ActivityVideosListingActivity.class).putExtra("color", stemArrayList.get(position).get("color")).putExtra("board", board).putExtra("sClass", studentClass).putExtra("categoryName", "activity_videos").putExtra("position", position).putExtra("subject", stemArrayList.get(position).get("id")).putExtra("subjectName", stemArrayList.get(position).get("name")).putExtra("subjects", subjectArrayList));


                                }
                            });
                            recyclerViewStem.setAdapter(extraContentAdapter);
                        }
                        else {
                            recyclerViewStem.setVisibility(View.GONE);
                            textViewBooks.setVisibility(View.GONE);
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
        getBooks();
    }

    /** get Books */
    private void getBooks() {
        if (Util.isOfflineMode(context)) try {
            String filePath = ".iDream_content/offlinetab_PAL/Class"+sClass+"_core_content.txt";    // path
            JSONObject jsonObject = Util.readJsonFile(context, filePath);                       // read file
            JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));    // filter by language

            String subject;
            Iterator<String> subjectIterator = object.keys();

            /** topicArrayList => contain books subject name */
            /** topicArrayListid => contain books subject key */
            /** subjectArrayList => contain books type ( books_ncert or subject_books) */
            /** topicLengthArrayList => contain books count in subject */

            topicArrayList = new ArrayList<>();
            topicArrayListid = new ArrayList<>();
            subjectArrayList = new ArrayList<>();
            topicLengthArrayList = new ArrayList<>();

            subject = subjectIterator.next();

            /** check books available or not */
            if(object.has("books")){
                JSONObject object______ = object.getJSONObject("books");
                Iterator<String> iterator = object______.keys();
                String key = "";

                /** point every book subject & set book subject data/info in arraylist */
                while (iterator.hasNext()) {
                    key = iterator.next();
                    JSONArray object1e2 = object______.getJSONArray(key);
                    JSONObject object12 = object1e2.getJSONObject(0);
                    topicArrayList.add(object12.getString("name"));
                    topicArrayListid.add(key);
                    topicLengthArrayList.add(object1e2.length());
                    subjectArrayList.add(subject);
                }
            }

            if (topicArrayList.size() > 0) {
                recyclerViewBooks.setVisibility(View.VISIBLE);
                textViewBooks.setVisibility(View.VISIBLE);

                /** set data in adapter */
                ExtraContentAdapter extraContentAdapter = new ExtraContentAdapter(context, null, topicArrayList, topicLengthArrayList, "Books");
                recyclerViewBooks.setAdapter(extraContentAdapter);
                extraContentAdapter.SetOnItemClickListener(new ExtraContentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Util.preventTwoClick(view);

                        Intent intent = new Intent(context, ExtraContentListingActivity.class);

                        /** send data to another screen */
                        intent.putExtra("subjectName", topicArrayList.get(position));
                        intent.putExtra("subject", subjectArrayList.get(position));
                        intent.putExtra("sClass", studentClass);
                        intent.putExtra("board", board);
                        intent.putExtra("type", "books");
                        intent.putExtra("position", position);
                        intent.putExtra("subjects", subjectArrayList);
                        intent.putExtra("books_heading", textViewBooksText);

                        startActivity(intent);
                    }
                });
            }
            else {
                recyclerViewBooks.setVisibility(View.GONE);
                textViewBooks.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        else global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT).child(board).child(language).child(studentClass).child("extra_content").child("books").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        /** getting books from firebase */
                        HashMap<String, Object> booksHashMap = (HashMap<String, Object>) snapshot.getValue();

                        /** booksArrayList => contain books subject & books list */
                        /** topicArrayList => contain books list */
                        /** subjectArrayList => contain books name */

                        ArrayList<HashMap<String, Object>> booksArrayList = new ArrayList<>();
                        subjectArrayList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : booksHashMap.entrySet()) {

                            /** (booksData) contain books subjects & there books data */
                            ArrayList<HashMap<String, Object>> booksData = (ArrayList<HashMap<String, Object>>) entry.getValue();

                            for (HashMap<String, Object> item : booksData) {
                                /** adding book subject data in list */
                                booksArrayList.add(item);
                                /** adding book subject name in list */
                                subjectArrayList.add(item.get("name").toString());
                            }
                        }

                        /** set data in adapter */
                        HomeBooksAdapter booksAdapter = new HomeBooksAdapter(context, booksArrayList,0);
                        booksAdapter.SetOnItemClickListener(new HomeBooksAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Util.preventTwoClick(view);

                                /** add books list in (topicArrayList) of selected book subject */
                                ArrayList<HashMap<String, String>> topicArrayList = (ArrayList<HashMap<String, String>>) booksArrayList.get(position).get(ApplicationConstants.TOPICS);

                                Intent intent ;

                                /** set activity according to portrait mode or landscape mode */
                                if(Util.isPortraitMode(context)) intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.ExtraContentListingActivity.class);
                                else intent = new Intent(context, ExtraContentListingActivity.class);

                                /** send data to another activity */
                                intent.putExtra("subjectName", topicArrayList.get(position).get("topicName"));
                                intent.putExtra("subject", subjectArrayList.get(position));
                                intent.putExtra("sClass", studentClass);
                                intent.putExtra("board", board);
                                intent.putExtra("type", "books");
                                intent.putExtra("position", position);
                                intent.putExtra("subjects", subjectArrayList);
                                intent.putExtra("books_heading", textViewBooksText);

                                startActivity(intent);
                            }
                        });

                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_right","anim",getActivity().getPackageName()));
                        recyclerViewBooks.setLayoutAnimation(animation);
                        booksAdapter.notifyDataSetChanged();
                        recyclerViewBooks.scheduleLayoutAnimation();
                        recyclerViewBooks.setAdapter(booksAdapter);

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

    /** Update Message layout */
    private void updateUi(ArrayList<LastTopicDetailsModel> list) {
        if(list != null && list.size() > 0) {
            for(LastTopicDetailsModel item: list) {

                System.out.println("======= level 2 "+ item);
                lastTopicId = item.getTopicId();
                lastTopicLayout.setVisibility(View.VISIBLE);
                String topicName = "<b>"+ item.getTopicName() + "</b>";                /** static text added because if method calls before getting text from firebase */
                if(you_were_learning==null) if(Util.getSelectedLanguage(context).equals("hindi")) {
                        you_were_learning = ", आप सीख रहे थे ";
                        wish_to_continue = " . क्या आप जारी रखना चाहते हैं?";
                } else {
                        you_were_learning = ", you were learning ";
                        wish_to_continue = ".Do you wish to continue?";
                    }

                /** message text */
                textViewMsg.setText(Html.fromHtml(Util.getUsernameShowable(context).replace("_"," ")+ you_were_learning + topicName.trim() + wish_to_continue));


                /** get icon of subject */
                String icon = null;
                Uri uri = null;

                String filePath = Util.getSDCardPath(context)+"/.iDream_content/Subject_Icons/"+ item.getSubjectName() + ".png";
                File file = new File(filePath);

                if(file.exists()) uri = Uri.fromFile(file);

                if(uri != null) icon = uri.toString();
                else icon = item.getIconUrl();

                String finalIcon = icon;

                /** load icon */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Glide.with(getContext()).load(Uri.parse(finalIcon)).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(subjectImageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);

                lastTopicLayout.requestFocus();
                lastTopicLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        Intent intent;

                        if(Util.isPortraitMode(context)) intent = new Intent(context, PalContentListingActivity_Mobile.class);
                        else intent = new Intent(context, PalContentListingActivity.class);

                        Util.setVideoLevel(context, 0);
                        String subject = item.getSubject();
                        String subjectName = item.getSubjectName();
                        String color = item.getColor();
                        global.setColor(color);
                        intent.putExtra("sClass", item.getSClass());
                        intent.putExtra("board", item.getBoard());
                        intent.putExtra("subject", item.getSubject());
                        intent.putExtra("subjectName", item.getSubjectName());
                        intent.putExtra("icon", finalIcon);
                        intent.putExtra("color", item.getColor());
                        intent.putExtra("backToScreen", false);
                        intent.putExtra("lastTopicId", lastTopicId);
                        PalContentListingActivity.autoPlayDelay=2000;
                        PalContentListingActivity.connectionLayoutDelay=2000;
                        Util.setSubject(context, subject);
                        Util.setSubjectName(context, subjectName);
                        startActivity(intent);

                    }
                });
                closeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.preventTwoClick(v);
                        lastTopicLayout.setVisibility(View.GONE);
                    }
                });

            }
        } else {
            lastTopicLayout.setVisibility(View.GONE);
        }
    }

    private void checkConnection(boolean first) {
        if(!Util.isOfflineMode(context)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
                        imageViewCrossVideo2.setVisibility(View.GONE);
                    }
                }
            }, 10000);//time in milisecond
        }
        else hideconnection_layout();
    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
    }

    private void listners() {
        textViewBooksSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                startActivity(new Intent(context, ExtraContentListingActivity.class).putExtra("subjectName", topicArrayList.get(0)).putExtra("subject", subjectArrayList.get(0)).putExtra("board", board).putExtra("sClass", studentClass).putExtra("type", "books").putExtra("position", 0).putExtra("subjects", subjectArrayList));
            }
        });
    }

    private void getNGODetails(String ngoID, String tabID, String appID) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    isDataLoaded = true;
                                    Util.dismissDialog();
                                    if (dataSnapshot.getValue() != null) {
                                        HashMap<String, Object> hash = (HashMap<String, Object>) dataSnapshot.getValue();
                                        String tID = (String) hash.get("tabID");
                                        if (tID.equals(tabID)) {
                                            Util.setActivation(context, true);
                                            long serverTime = (long) hash.get("serverTime");
                                            long localTime = (long) hash.get("localTime");
                                            String days = (String) hash.get("licencePeriod");
                                            String image1 = (String) hash.get("image1");
                                            String image2 = (String) hash.get("image2");
                                            String userID = (String) hash.get("userID");
                                            String schoolID = (String) hash.get("schoolId");
                                            String projectID = (String) hash.get("projectId");
                                            String schoolName = (String) hash.get("schoolName");

                                            Util.setSchoolId(context,schoolID);
                                            Util.setProjectId(context,projectID);
                                            Util.setSchoolName(context,schoolName);
                                            String forPAl = "true";
                                            Util.setForPal(context, forPAl);
                                            ArrayList<String> ngoImages = new ArrayList<>();
                                            ngoImages.add(image1);
                                            ngoImages.add(image2);
                                            Util.setNGOIMAGES(context, ngoImages);

                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("appID", appID);
                                            map.put("ngoID", ngoID);
                                            map.put("userID","");


                                            ActivationDetailsRepository activationDetailsRepository = new ActivationDetailsRepository(context);
                                            ActivationModel activationModel = new ActivationModel();
                                            activationModel.setAppID(appID);
                                            activationModel.setTabID(tabID);
                                            activationModel.setUserid("");
                                            activationModel.setServerTime(serverTime);
                                            activationModel.setLocalTime(localTime);
                                            activationModel.setDays(days);
                                            activationDetailsRepository.insertActivationDetails(activationModel);
                                            global.getDatabaseReference().child("app_tab_relation").child(tabID).setValue(map);

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).child("isRegistered").addListenerForSingleValueEvent(valueEventListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDataLoaded) {
                    Util.dismissDialog();
                    global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(appID).child("isRegistered").removeEventListener(valueEventListener);
                }

            }
        }, 4000);


    }

    private void getSubjectsLayoutHeight(){
        ViewTreeObserver vto = subjectsLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    subjectsLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    subjectsLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = subjectsLayout.getMeasuredWidth();
                int height = subjectsLayout.getMeasuredHeight();
                getSubjectsRecyclerViewHeight(width, height);
            }
        });
    }

    private void getSubjectsRecyclerViewHeight(int width, int height) {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new
             ViewTreeObserver.OnGlobalLayoutListener() {
                 @Override
                 public void onGlobalLayout() {
                     //don't forget remove this listener
                     int itemWidth = mRecyclerView.getWidth();
                     int itemHeight = mRecyclerView.getHeight();
                     if (itemWidth > 0 && itemHeight > 0) {
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                             mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                         } else {
                             mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                         }
                     }
                     int totalHeight = height + itemHeight + 5;
                     ((PracticeTopicActivity)context).setMarginTransparentBackground(height);
                 }
             });
    }

    private void getLastTopicDetails() {
        runBackgroundTask(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), Util.getSubject(context));
    }

    public void startTestPrep() {
        if (TextUtils.isEmpty(Util.getUserEmail(context))) {
            Util.showToast(context, "Please Update your Email address from your profile");
        } else if (!TextUtils.isEmpty(Util.getUserMobile(context))) {
            if (Util.getUserMobile(context).equalsIgnoreCase("+91-")) {
                Util.showToast(context, "Please Update your Mobile number from your profile");
            } else {
                try {
                    try {
                        private_key = getPrivateKeyData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject obj = new JSONObject();
                    obj.put("name", Util.getUsernameShowable(context));
                    obj.put("email", Util.getUserEmail(context));
                    try {
                        obj.put("mobile", Util.getUserMobile(context).split("-")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        obj.put("mobile", Util.getUserMobile(context));
                    }
                    SecureRandom r = new SecureRandom();
                    byte[] ivBytes = new byte[16];
                    r.nextBytes(ivBytes);
                    byte[] cipher_text = encrypt(stringToByte(obj.toString()), private_key, ivBytes);
                    String base64_cipher_text = base64Encode(cipher_text);
                    String base64_IV = base64Encode(ivBytes);

                    // startActivity(new Intent(context, WebViewActivity.class).putExtra("url", testUrl).putExtra("iv", base64_IV).putExtra("cipherText", base64_cipher_text));
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Util.showToast(context, "Please Update your Mobile number from your profile");
        }
    }

    public byte[] base64Decode(String text) {
        return Base64.decode(text, Base64.NO_WRAP);
    }

    public String base64Encode(byte[] byte_text) {
        return Base64.encodeToString(byte_text, Base64.NO_WRAP);
    }

    public byte[] stringToByte(String text) {
        try {
            return text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public byte[] encrypt(byte[] plaintext, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key_spec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv_spec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key_spec, iv_spec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }

    public byte[] getPrivateKeyData() throws IOException {
        InputStream ins = getResources().openRawResource(/*R.raw.aes_secret_key_*/0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 0;
        byte[] buffer = new byte[1024];
        while ((size = ins.read(buffer, 0, 1024)) >= 0) {
            outputStream.write(buffer, 0, size);
        }
        ins.close();
        buffer = outputStream.toByteArray();
        return buffer;
    }

    /** getting data from RoomDb */
    private void runBackgroundTask(String userId, String board, String sClass, String subject) {

        System.out.println("======= level 0 "+ userId +" "+ board +" "+ sClass +" "+ subject +" ");

        /** Getting last topic & subject details  */
        getList(userId, board, sClass, subject).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("======= level 1 "+ o);
                        updateUi((ArrayList<LastTopicDetailsModel>) o);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject) {
        return Observable.fromCallable(() -> {
            return lastTopicDetailsRepository.getDetail(userId, board, sClass, subject,Util.getSelectedLanguage(context));
        });
    }

}
