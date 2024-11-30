package com.idreameducation.ipreppal.pal.fragments;

import static com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter.activeFoundationTopic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.adapter.PracticeAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
public class PalPracticeFrament extends Fragment {
    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private RelativeLayout practiceLayout;
    private String board;
    private String sClass;
    private String subject;
    private String topicID;
    private String language ;
    private String categoryID;
    private HashMap<String, String> practiceMap;
    private HashMap<String, String> practiceHashMap;
    private Disposable disposable;
    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;

    public static PracticeAdapter practiceAdapter;

    private ArrayList<HashMap<String,String>> practiceArray;

    public static int practice_mastry=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();

        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);

        practiceLayout = view.findViewById(R.id.practiceLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        if (Util.isPortraitMode(context)) {
            board = PalContentListingActivity_Mobile.board;
            sClass = PalContentListingActivity_Mobile.sClass;
            subject = PalContentListingActivity_Mobile.subject;
            language = Util.getSelectedLanguage(context);
            topicID = PalContentListingActivity_Mobile.palContentListingActivityMobile.topic_id;

        } else
        {
            board = PalContentListingActivity.board;
            sClass = PalContentListingActivity.sClass;
            subject = PalContentListingActivity.subject;
            language = ((PalContentListingActivity) context).language;
            topicID = ((PalContentListingActivity) context).topic_id;
        }

        try {
            getPractice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

    public void hideLayout(String topicId, boolean hide, int position) {
        if(practiceLayout != null){
            if(topicID != null && topicId.equals(topicID) && !hide){
                practiceLayout.setAlpha(1f);
            }else{
                practiceLayout.setAlpha(.5f);
            }
        }
    }

    public PalPracticeFrament(String contentType) {
        this.categoryID = contentType;
    }

    public PalPracticeFrament() {

    }

    public void getPractice() throws Exception {
        if (Util.isOfflineMode(context)) {

            String filePath = ".iDream_content/offlinetab_PAL/Class"+sClass+"_core_content.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            JSONObject object__ = jsonObject.getJSONObject(language);
            JSONObject object___ = object__.getJSONObject("practice");
            JSONObject object____ = object___.getJSONObject("content");
            JSONObject object_____ = object____.getJSONObject(subject);
            JSONObject object______ = object_____.getJSONObject("topics");
            JSONObject object_______ = object______.getJSONObject(topicID);

            String Display = object_______.getString("Display");
            String Foundational_Topic_ID = object_______.getString("Foundational_Topic_ID");
            String IsModelTestPaper = object_______.getString("IsModelTestPaper");
            String Levels = object_______.getString("Levels");
            String Next_topic_id = object_______.getString("Next_topic_id");
            String StreakCount = object_______.getString("StreakCount");
            String TName = object_______.getString("TName");
            String TName_alt = object_______.getString("TName_alt");
            String TopicID = object_______.getString("TopicID");
            String foundational_class = object_______.getString("foundational_class");
            String incorrectStreak = object_______.getString("incorrectStreak");
            String isAlternateLanguageAvailable = object_______.getString("isAlternateLanguageAvailable");

            practiceArray=new ArrayList<>();
            practiceMap = new HashMap<>();
            practiceMap.put("Display", Display);
            practiceMap.put("Foundational_Topic_ID", Foundational_Topic_ID);
            practiceMap.put("IsModelTestPaper", IsModelTestPaper);
            practiceMap.put("Levels", Levels);
            practiceMap.put("Next_topic_id", Next_topic_id);
            practiceMap.put("StreakCount", StreakCount);
            practiceMap.put("TName", TName);
            practiceMap.put("TName_alt", TName_alt);
            practiceMap.put("TopicID", TopicID);
            practiceMap.put("foundational_class", foundational_class);
            practiceMap.put("incorrectStreak", incorrectStreak);
            practiceMap.put("isAlternateLanguageAvailable", isAlternateLanguageAvailable);
            practiceArray.add(practiceMap);
            PracticeAdapter practiceAdapter = new PracticeAdapter(context, practiceArray);
            getMastery(practiceMap, practiceAdapter);
            recyclerView.setAdapter(practiceAdapter);
            practiceAdapter.SetOnItemClickListener(new PracticeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    boolean isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(topicID);
                    if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                    {
                        Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);
//                        if(Util.getSelectedLanguage(context).equals("hindi"))
//                        {
//                            Util.openGifDialogue(context, "कृपया पहले मुलभुत अध्याय का परिक्षण करें");
//                        }
//                        else
//                        {
//                            Util.openGifDialogue(context, "Please do foundational practice first");
//                        }
                    }
                    else
                    {
                        if(isCompleted)
                        {
                            String topicId = practiceMap.get("TopicID");
                            String topicName = practiceMap.get("TName");
                            String streak = practiceMap.get("StreakCount");
                            String incorrect_streak = practiceMap.get("incorrectStreak");
                            Util.setTopicID(context, topicId);
                            Util.setTopicNameAlt(context, topicName);
                            Util.setLevel(context, Integer.parseInt("1"));
                            String studentClass = sClass;
                            String streakProgress = "0";
//                                            String streakProgress = ""+practice_mastry;
//                                            global.setProgress(practice_mastry);
                            global.setProgress(0);
                            System.out.println("====== practice_mastry "+practice_mastry);

                            String mastery = "0";
                            for(PracticeScoreModel item: PalContentListingActivity.practiceScoreModelArrayList)
                            {
                                if(item.getTopicId().equals(topicID))
                                {
                                    mastery = item.getScore();
                                    streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                                    Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                                    global.setProgress(Integer.parseInt(mastery));
                                }

                            }

                            if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                                startActivity(new Intent(context, QuizActivity.class).putExtra("mastery", ""+practice_mastry).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", mastery));

                            }else {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                }else {
                                    Util.openGifDialogue(context,"Internet Connection is not working");
                                }
                            }
                        }
                        else
                        {
                            //9b124a035eeb0481
                            Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
//                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                            {
//                                // Util.showToast(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
//                                Util.openGifDialogue(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
//                            }else {
//                                //  Util.showToast(context, "Please do diagnostic test first");
//                                Util.openGifDialogue(context, "Please do diagnostic test first");
//
//                            }
                        }
                    }

                }
            });
        }
        else global.getDatabaseReference().child(ApplicationConstants.CORE_CONTENT).child(board).child(sClass)
                .child(language).child("practice").child("content").child(subject).child("topics")
                .child(topicID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        practiceArray=new ArrayList<>();
                        if (snapshot.getValue() != null) {
                            HashMap<String,String> practiceHashMap = (HashMap<String, String>) snapshot.getValue();
                            practiceHashMap.put("mastery",""+0);
                            practiceHashMap.put("isFoundation","false");
                            practiceArray.add(practiceHashMap);
                            ArrayList<HashMap<String,String>> FoundationPracticeArray=new ArrayList<>();
                            if(Util.isPortraitMode(context)) {
                                if(PalTopicListingActivity.palTopicListingActivity.foundationalTopicData != null)
                                    for(int f=PalTopicListingActivity.palTopicListingActivity.foundationalTopicData.size()-1;f>=0;f--) {
                                        FoundationalTopicModel item = PalTopicListingActivity.palTopicListingActivity.foundationalTopicData.get(f);
                                        if(item.getSeniorTopicID().equals(practiceHashMap.get("TopicID"))) {

                                            HashMap<String,String> practiceHashMap11 = new HashMap<>();
                                            practiceHashMap11.put("id",item.getId()+"");
                                            practiceHashMap11.put("TopicID",item.getTopicId()+"");
                                            practiceHashMap11.put("TName",item.getTopicName()+"");
                                            practiceHashMap11.put("sClass",item.getSClass()+"");
                                            practiceHashMap11.put("streakProgress",item.getStreakProgress()+"");
                                            practiceHashMap11.put("StreakCount",item.getStreakCount()+"");
                                            practiceHashMap11.put("incorrectStreak",item.getIncorrectStreak()+"");
                                            practiceHashMap11.put("practiceType",item.getPracticeType()+"");
                                            practiceHashMap11.put("seniorClass",item.getSeniorClass()+"");
                                            practiceHashMap11.put("seniorTopicID",item.getSeniorTopicID()+"");
                                            practiceHashMap11.put("seniorTopicName",item.getSeniorTopicName()+"");
                                            practiceHashMap11.put("Display",item.getShow()+"");
                                            practiceHashMap11.put("videoLevel",item.getVideoLevel()+"");
                                            practiceHashMap11.put("testPercentageAchieved",item.getTestPercentageAchieved()+"");
                                            practiceHashMap11.put("userId",item.getUserId()+"");
                                            practiceHashMap11.put("subjectId",item.getSubjectId()+"");
                                            practiceHashMap11.put("lang",item.getLang()+"");
                                            practiceHashMap11.put("isFoundation","true");
                                            practiceHashMap11.put("mastery",""+0);

                                            FoundationPracticeArray.add(practiceHashMap11);
                                        }
                                    }
                            }
                            else {
                                if(((PalContentListingActivity)context).foundationalTopicData != null)
                                    for(int f=((PalContentListingActivity)context).foundationalTopicData.size()-1;f>=0;f--) {
                                        FoundationalTopicModel item = ((PalContentListingActivity)context).foundationalTopicData.get(f);
                                        if(item.getSeniorTopicID().equals(practiceHashMap.get("TopicID"))) {

                                            HashMap<String,String> practiceHashMap11 = new HashMap<>();
                                            practiceHashMap11.put("id",item.getId()+"");
                                            practiceHashMap11.put("TopicID",item.getTopicId()+"");
                                            practiceHashMap11.put("TName",item.getTopicName()+"");
                                            practiceHashMap11.put("sClass",item.getSClass()+"");
                                            practiceHashMap11.put("streakProgress",item.getStreakProgress()+"");
                                            practiceHashMap11.put("StreakCount",item.getStreakCount()+"");
                                            practiceHashMap11.put("incorrectStreak",item.getIncorrectStreak()+"");
                                            practiceHashMap11.put("practiceType",item.getPracticeType()+"");
                                            practiceHashMap11.put("seniorClass",item.getSeniorClass()+"");
                                            practiceHashMap11.put("seniorTopicID",item.getSeniorTopicID()+"");
                                            practiceHashMap11.put("seniorTopicName",item.getSeniorTopicName()+"");
                                            practiceHashMap11.put("Display",item.getShow()+"");
                                            practiceHashMap11.put("videoLevel",item.getVideoLevel()+"");
                                            practiceHashMap11.put("testPercentageAchieved",item.getTestPercentageAchieved()+"");
                                            practiceHashMap11.put("userId",item.getUserId()+"");
                                            practiceHashMap11.put("subjectId",item.getSubjectId()+"");
                                            practiceHashMap11.put("lang",item.getLang()+"");
                                            practiceHashMap11.put("isFoundation","true");
                                            practiceHashMap11.put("mastery",""+0);

                                            FoundationPracticeArray.add(practiceHashMap11);
                                        }
                                    }
                            }

                            for(int i=FoundationPracticeArray.size()-1;i>=0;i--) {
                                practiceArray.add(FoundationPracticeArray.get(i));
                            }

                            practiceAdapter = new PracticeAdapter(context, practiceArray);
                            recyclerView.setAdapter(practiceAdapter);
                            practiceAdapter.SetOnItemClickListener(new PracticeAdapter.OnItemClickListener() {
                                @SuppressLint("SuspiciousIndentation")
                                @Override
                                public void onItemClick(View view, int position) {

                                    if(practiceArray.get(position).get("isFoundation").equals("true")) {

                                        if(activeFoundationTopic.equals(practiceArray.get(position).get("TopicID"))) {
                                            if(Util.isPortraitMode(context)) {

                                                PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime=true;
//                                                PalTopicListingActivity.palTopicListingActivity.firsttime=true;

                                                int mastery = 0 ;
                                                int level = 1 ;
                                                String streekProgress = "0" ;
                                                for(int o = PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                    PracticeScoreModel data = PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList.get(o);
                                                    if (data.getTopicId().equals(practiceArray.get(position).get("TopicID"))) {
                                                        mastery=  Integer.parseInt(PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList.get(o).getScore());
                                                        level=  Integer.parseInt(PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                        streekProgress=  PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList.get(o).getStreakProgress();
                                                        break;
                                                    }
                                                }
                                                PalTopicListingActivity.palTopicListingActivity.showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), practiceArray.get(position).get("seniorTopicID"), practiceArray.get(position).get("TopicID"), practiceArray.get(position).get("TName"), practiceArray.get(position).get("seniorClass"), 0, position,mastery,streekProgress,level);


                                            }
                                            else {
                                                ((PalContentListingActivity)context).firsttime=true;

                                                int mastery = 0 ;
                                                int level = 1 ;
                                                String streekProgress = "0" ;
                                                for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                    PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                                                    if (data.getTopicId().equals(practiceArray.get(position).get("TopicID"))) {
                                                        mastery=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getScore());
                                                        level=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                        streekProgress=  PalContentListingActivity.practiceScoreModelArrayList.get(o).getStreakProgress();
                                                        break;
                                                    }
                                                }
                                                ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), practiceArray.get(position).get("seniorTopicID"), practiceArray.get(position).get("TopicID"), practiceArray.get(position).get("TName"), practiceArray.get(position).get("seniorClass"), 0, position,mastery,streekProgress,level);

                                            }
                                        }
                                        else {
//                                            Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);


                                            ArrayList<PracticeScoreModel> scoreList= PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList;

                                            for(int i=0;i<=scoreList.size()-1;i++) {

                                                if(scoreList.get(i).getTopicId().equals(practiceArray.get(position).get("TopicID"))){

                                                    if( scoreList.get(i).getScore().equals("100")) Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity_Mobile.twentyOne);
                                                    else Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);
                                                }


                                            }






                                        }

                                    }
                                    else {
                                        boolean isCompleted ,isFoundationCompleted;
                                        if(Util.isPortraitMode(context)) isCompleted = PalContentListingActivity_Mobile.instance.getTopicCompleteStatus(topicID);
                                        else isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(topicID);


                                        if(Util.isPortraitMode(context)) isFoundationCompleted = PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice");
                                        else isFoundationCompleted = ((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice");


                                        if (isFoundationCompleted)
                                        {
                                            Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);
                                        }
                                        else
                                        {
                                            if(isCompleted) {

                                                if (Util.isPortraitMode(context))
                                                {
                                                    String topicId = practiceArray.get(position).get("TopicID");
                                                    String topicName = practiceArray.get(position).get("TName");
                                                    String streak = practiceArray.get(position).get("StreakCount");
                                                    String incorrect_streak = practiceArray.get(position).get("incorrectStreak");
                                                    Util.setTopicID(context, topicId);
                                                    Util.setTopicNameAlt(context, topicName);
                                                    Util.setLevel(context, Integer.parseInt("1"));
                                                    String studentClass = sClass;
                                                    String streakProgress = "0";
//                                            String streakProgress = ""+practice_mastry;
//                                            global.setProgress(practice_mastry);
                                                    global.setProgress(0);
                                                    System.out.println("====== practice_mastry "+practice_mastry);

                                                    String mastery = "0";
                                                    for(PracticeScoreModel item: PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList)
                                                    {
                                                        if(item.getTopicId().equals(topicID))
                                                        {
                                                            mastery = item.getScore();
                                                            streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                                                            Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                                                            global.setProgress(Integer.parseInt(mastery));
                                                        }

                                                    }

                                                    if (Util.isNetworkAvailable(context)) {
                                                        startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class).putExtra("mastery", ""+practice_mastry).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", mastery));

                                                    }else {
                                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                                        {
                                                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                                        }else {
                                                            Util.openGifDialogue(context,"Internet Connection is not working");
                                                        }
                                                    }
                                                }
                                                else {
                                                    String topicId = practiceArray.get(position).get("TopicID");
                                                    String topicName = practiceArray.get(position).get("TName");
                                                    String streak = practiceArray.get(position).get("StreakCount");
                                                    String incorrect_streak = practiceArray.get(position).get("incorrectStreak");
                                                    Util.setTopicID(context, topicId);
                                                    Util.setTopicNameAlt(context, topicName);
                                                    Util.setLevel(context, Integer.parseInt("1"));
                                                    String studentClass = sClass;
                                                    String streakProgress = "0";
//                                            String streakProgress = ""+practice_mastry;
//                                            global.setProgress(practice_mastry);
                                                    global.setProgress(0);
                                                    System.out.println("====== practice_mastry " + practice_mastry);

                                                    String mastery = "0";
                                                    for (PracticeScoreModel item : PalContentListingActivity.practiceScoreModelArrayList) {
                                                        if (item.getTopicId().equals(topicID)) {
                                                            mastery = item.getScore();
                                                            streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                                                            Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                                                            global.setProgress(Integer.parseInt(mastery));
                                                        }

                                                    }

                                                    if (Util.isNetworkAvailable(context)) {
                                                        startActivity(new Intent(context, QuizActivity.class).putExtra("mastery", "" + practice_mastry).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", mastery));

                                                    } else {
                                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                                            Util.openGifDialogue(context, "इंटरनेट कनेक्शन काम नहीं कर रहा");
                                                        } else {
                                                            Util.openGifDialogue(context, "Internet Connection is not working");
                                                        }
                                                    }

                                                }
                                            }
                                            else
                                            {
                                                Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
                                            }
                                        }

                                    }

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

    public void startPractice() {

        if(Util.isPortraitMode(context))
        {
            startPracticeMobile();
            return;
        }

        if (Util.isOfflineMode(context)) {
            boolean isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(topicID);
//            boolean isCompleted = ((PalContentListingActivity) context).isCompleted;
            if (isCompleted) {
                String topicId = practiceMap.get("TopicID");
                String topicName = practiceMap.get("TName");
                String streak = practiceMap.get("StreakCount");
                String incorrect_streak = practiceMap.get("incorrectStreak");
                String mastery = practiceMap.get("mastery");
                Util.setTopicID(context, topicId);
                Util.setTopicNameAlt(context, topicName);
                Util.setLevel(context, Integer.parseInt("1"));
                String studentClass = sClass;
                String streakProgress = "0";
//                String streakProgress = mastery;
                global.setProgress(0);

                for(PracticeScoreModel item: PalContentListingActivity.practiceScoreModelArrayList)
                {
                    if(item.getTopicId().equals(topicID))
                    {
                        mastery = item.getScore();
                        streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                        Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                        global.setProgress(Integer.parseInt(mastery));
                    }

                }

                System.out.println("===== mastery 1 "+mastery);
                Intent intent = new Intent(context, QuizActivity.class).putExtra("mastery", mastery).putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", 0);
                startActivity(intent);



            } else {
                Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
//                Util.showToast(context, "Please take Diagnostic test");
                /*if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                {
                    // Util.showToast(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                    Util.openGifDialogue(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                }else {
                    //  Util.showToast(context, "Please do diagnostic test first");
                    Util.openGifDialogue(context, "Please do diagnostic test first");

                }*/

            }
        }
        else{

            String mastery = practiceArray.get(0).get("mastery");
            String topicId = practiceArray.get(0).get("TopicID");
            String topicName = practiceArray.get(0).get("TName");
            String streak = practiceArray.get(0).get("StreakCount");
            String incorrect_streak = practiceArray.get(0).get("incorrectStreak");
            Util.setTopicID(context, topicId);
            Util.setTopicNameAlt(context, topicName);
            Util.setLevel(context, Integer.parseInt("1"));
            String studentClass = sClass;
            String streakProgress = "0";
//                String streakProgress = ""+practice_mastry;
//                global.setProgress(practice_mastry);
            global.setProgress(0);

            System.out.println("====== practice_mastry 2 "+practice_mastry);
            System.out.println("====== mastery 2 "+mastery);


            boolean isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(topicID);

            if (isCompleted) {

                for(PracticeScoreModel item: PalContentListingActivity.practiceScoreModelArrayList)
                {
                    if(item.getTopicId().equals(topicID))
                    {
                        mastery = item.getScore();
                        streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                        Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                        global.setProgress(Integer.parseInt(mastery));
                    }

                }
                if (Util.isNetworkAvailable(context)) {

                    startActivity(new Intent(context, QuizActivity.class).putExtra("mastery", ""+mastery).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", mastery));



                }else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }else {
                Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
//                Util.showToast(context, "Please take Diagnostic test");
//                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                {
//                    // Util.showToast(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
//                    Util.openGifDialogue(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
//                }else {
//                    //  Util.showToast(context, "Please do diagnostic test first");
//                    Util.openGifDialogue(context, "Please do diagnostic test first");
//
//                }
            }



        }
    }

    public void startPracticeMobile(){
        if (Util.isOfflineMode(context)) {
            boolean isCompleted = PalContentListingActivity_Mobile.palContentListingActivityMobile.getTopicCompleteStatus(topicID);
//            boolean isCompleted = com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.palContentListingActivity.isCompleted;
            if (isCompleted) {
                String topicId = practiceMap.get("TopicID");
                String topicName = practiceMap.get("TName");
                String streak = practiceMap.get("StreakCount");
                String incorrect_streak = practiceMap.get("incorrectStreak");
                String mastery = practiceMap.get("mastery");
                Util.setTopicID(context, topicId);
                Util.setTopicNameAlt(context, topicName);
                Util.setLevel(context, Integer.parseInt("1"));
                String studentClass = sClass;
                String streakProgress = "0";
//                String streakProgress = mastery;
                global.setProgress(0);


                System.out.println("===== mastery 1 "+mastery);
                Intent intent = new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class).putExtra("mastery", mastery).putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", 0);
                startActivity(intent);



            } else {
//                Util.showToast(context, "Please take Diagnostic test");
                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                {
                    // Util.showToast(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                    Util.openGifDialogue(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                }else {
                    //  Util.showToast(context, "Please do diagnostic test first");
                    Util.openGifDialogue(context, "Please do diagnostic test first");

                }
            }
        }
        else{

            String mastery = practiceArray.get(0).get("mastery");
            String topicId = practiceArray.get(0).get("TopicID");
            String topicName = practiceArray.get(0).get("TName");
            String streak = practiceArray.get(0).get("StreakCount");
            String incorrect_streak = practiceArray.get(0).get("incorrectStreak");
            Util.setTopicID(context, topicId);
            Util.setTopicNameAlt(context, topicName);
            Util.setLevel(context, Integer.parseInt("1"));
            String studentClass = sClass;
            String streakProgress = "0";

            global.setProgress(0);


            boolean isCompleted = PalContentListingActivity_Mobile.palContentListingActivityMobile.getTopicCompleteStatus(topicID);

            if (isCompleted) {

                for(PracticeScoreModel item: PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList)
                {
                    if(item.getTopicId().equals(topicID))
                    {
                        mastery = item.getScore();
                        streakProgress = item.getStreakProgress();
//                        streak = item.getStreakProgress();
                        Util.setLevel(context, Integer.parseInt(item.getCurrentLevel()));
                        global.setProgress(Integer.parseInt(mastery));
                    }

                }
                if (Util.isNetworkAvailable(context)) {

                    startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class).putExtra("mastery", ""+mastery).putExtra("isAssigned", "true").putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrect_streak).putExtra("practiceType", "same").putExtra("seniorClass", sClass).putExtra("seniorTopicID", topicId).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", mastery));



                }else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }else {
//                Util.showToast(context, "Please take Diagnostic test");
                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                {
                    // Util.showToast(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                    Util.openGifDialogue(context, "कृपया पहले डायग्नोस्टिक परिक्षण करें");
                }else {
                    //  Util.showToast(context, "Please do diagnostic test first");
                    Util.openGifDialogue(context, "Please do diagnostic test first");

                }
            }



        }
    }

    private void getMastery(HashMap<String, String> practiceMap, PracticeAdapter practiceAdapter) {
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicID, practiceAdapter);
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("practice").child(topicID).child("detail").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            String mastery = "0";
                            HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                            if (dataMap != null) {
                                mastery = (String) dataMap.get("mastery");
                                practiceMap.put("mastery", mastery);
                                ((PalContentListingActivity)context).setPracticeData(Util.getTopicNameAlt(context), mastery);
                                ((PalContentListingActivity)context).displayPracticeLayout(false);
                            } else {
                                practiceMap.put("mastery", "0");
                                ((PalContentListingActivity)context).setPracticeData(Util.getTopicNameAlt(context), mastery);
                                ((PalContentListingActivity)context).displayPracticeLayout(false);
                            }
                        } else {
                            practiceMap.put("mastery", "0");
                            ((PalContentListingActivity)context).setPracticeData(Util.getTopicNameAlt(context), "0");
                            ((PalContentListingActivity)context).displayPracticeLayout(false);
                        }
                        practiceAdapter.notifyDataSetChanged();

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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String type, String topicId, PracticeAdapter practiceAdapter){
        getList(userId, board, sClass, subject, type, topicId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {
                        ArrayList<ReportsLatestDataPracticeModel> list = (ArrayList<ReportsLatestDataPracticeModel>) o;
                        String mastery = "0";
                        for(ReportsLatestDataPracticeModel item: list){
                            if(item.getTopicId().equals(topicID)){
                                mastery = item.getMastery();
                                break;
                            }
                        }
                        practiceMap.put("mastery", mastery);
                        ((PalContentListingActivity)context).setPracticeData(Util.getTopicNameAlt(context), mastery);
                        ((PalContentListingActivity)context).displayPracticeLayout(false);
                        practiceAdapter.notifyDataSetChanged();
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

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String type, String topicId){
        return Observable.fromCallable(() -> {
            //do something, get your Data object
            return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, type, topicId);
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}