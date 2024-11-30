package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.TestModel;
import com.idreameducation.ipreppal.pal.activity.BiMonthlyTestActivity;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.DataTypeConverter;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.RecyclerTouchListener;
import com.idreameducation.ipreppal.pal.adapter.PracticeAdapter;
import com.idreameducation.ipreppal.pal.adapter.TestsListAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestScoreModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataTestRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalTestFrament extends Fragment {
    private Context context;
    private Global global;
    private RelativeLayout testLayout;
    private String board;
    private String sClass;
    private String subject;
    private String topicID;
    private String language = "english";
    private String categoryID;
    private Spinner testsSpinner;
    private TextView textViewResult;
    private TextView textViewStartTest;
    private RecyclerView recyclerView;
    private TestsListAdapter testsListAdapter;
    private final List<TestModel> testsList = new ArrayList<>();
    private final long now = System.currentTimeMillis();
    private Disposable practiceDisposable;
    private Disposable testDisposable;
    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;
    private ReportsLatestDataTestRepository reportsLatestDataTestRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        assignIds(view);
        return view;
    }

    public PalTestFrament() {
    }

    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();

        if(Util.isPortraitMode(context))
        {
            board = PalContentListingActivity_Mobile.board;
            sClass = PalContentListingActivity_Mobile.sClass;
            subject = PalContentListingActivity_Mobile.subject;
            language = Util.getSelectedLanguage(context);
            topicID = PalContentListingActivity_Mobile.palContentListingActivityMobile.topic_id;

        }
        else
        {
            board = PalContentListingActivity.board;
            sClass = PalContentListingActivity.sClass;
            subject = PalContentListingActivity.subject;
            language = ((PalContentListingActivity) context).language;
            topicID = ((PalContentListingActivity) context).topic_id;
        }

        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);
        reportsLatestDataTestRepository = new ReportsLatestDataTestRepository(context);

        testLayout = view.findViewById(R.id.testLayout);
        testsSpinner = view.findViewById(R.id.testsSpinner);
        textViewResult = view.findViewById(R.id.textViewResult);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        testsListAdapter = new TestsListAdapter(testsList,language);
        recyclerView.setAdapter(testsListAdapter);
        ArrayList<String> testsTypeList = new ArrayList<>();
        testsTypeList.add("diagonostic_test");
        testsTypeList.add("simple_test");
        setTestList(testsTypeList);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(testsList.get(position).getName().equals("डायग्नोस्टिक परीक्षण")){
                    if(testsList.get(position).getScore().equals("0")){

                        boolean isCompleted;

                        if(Util.isPortraitMode(context)) isCompleted = PalContentListingActivity_Mobile.instance.getTopicCompleteStatus(topicID);
                        else isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(topicID);

                        if (isCompleted)
                        {
                            Util.showAnimatedDialog(context,Util.DIAGNOSTIC_IS_COMPLETED);
//                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                            {
//                                Util.openGifDialogue(context, "आप पहले ही "+Util.getTopicNameAlt(context)+ " के डायग्नोस्टिक परीक्षण का प्रयास कर चुके हैं");
//
//                            }else {
//                                //  Util.showToast(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                                Util.openGifDialogue(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                            }
                        }else {
                            Intent intent = new Intent(context, DStartActivity.class);
                            intent.putExtra("sClass", sClass);
                            context.startActivity(intent);
                        }

                    }else{
                        Util.showAnimatedDialog(context,Util.DIAGNOSTIC_IS_COMPLETED);
//                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                        {
//                            Util.openGifDialogue(context, "आप पहले ही "+Util.getTopicNameAlt(context)+ " के डायग्नोस्टिक ​​परीक्षण का प्रयास कर चुके हैं");
//
//                        }else {
//                            Util.openGifDialogue(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                        }

                    }
                }
                else if(testsList.get(position).getName().equals("परीक्षा")){
                    getMastery();
//                    if(testsList.get(position).getScore().equals("0")){
//                        getMastery();
//                    }
//                    else{
//
//                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                        {
//                            Util.openGifDialogue(context, "आप पहले ही "+Util.getTopicNameAlt(context)+ " के अंतिम ​​परीक्षण का प्रयास कर चुके हैं");
//
//                        }else {
//                            //Util.showToast(context, "You have already attempted the Final Test of "+Util.getTopicNameAlt(context));
//                            Util.openGifDialogue(context, "You have already attempted the Final Test of "+Util.getTopicNameAlt(context));
//                        }
//
//                    }
                }
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        textViewStartTest = view.findViewById(R.id.textViewStartTest);
        textViewStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> topicArrayList = new ArrayList<>();
                topicArrayList.add("NCERT_Hin_Maths_09_01_01");
                topicArrayList.add("NCERT_Hin_Maths_09_01_02");
                global.setTopicIdArrayList(topicArrayList);
                startActivity(new Intent(context, BiMonthlyTestActivity.class).putExtra("sClass", "9"));

            }
        });
        ArrayList<String> testsArraylist = new ArrayList<>();
        testsArraylist.add("Select Test Type");
        testsArraylist.add("Final Test");
        testsArraylist.add("Diagonostic test");
//        testsArraylist.add("Bi Monthly Test");

        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(context, R.layout.row_spinner, testsArraylist);
        testsSpinner.setAdapter(classAdapter);
        testsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
//                        Util.showToast(context, "please select test");
                        textViewResult.setVisibility(View.GONE);
                        textViewStartTest.setVisibility(View.GONE);
                        break;
                    case 1:
                        try {
//                            textViewResult.setVisibility(View.VISIBLE);
                            textViewStartTest.setVisibility(View.GONE);
//                            getNormalTestDetails();
                            getTestDetails("simple_test");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
//                            textViewResult.setVisibility(View.VISIBLE);
                            textViewStartTest.setVisibility(View.GONE);
                            getTestDetails("diagonostic_test");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
//                    case 3:
//                        try {
////                            textViewResult.setVisibility(View.VISIBLE);
//                            textViewStartTest.setVisibility(View.GONE);
////                            getBiMonthlyTestDetails();
//                            getTestDetails("bi_monthly_test");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void refreshSpinner() {
        if(testsSpinner != null){
            testsSpinner.setSelection(0);
        }
    }

    public void hideLayout(String topicId, boolean hide, int position) {
        if(testLayout != null){
            if(topicID != null && topicId.equals(topicID) && !hide){
//                testLayout.setAlpha(1f);
            }else{
//                testLayout.setAlpha(.5f);
            }
//            if(topicID != null && topicId.equals(topicID) && hide){
//                testLayout.setAlpha(.5f);
//            }else if(!hide && position == 0 && ((PalContentListingActivity) context).firstTimeTestHide){
//                testLayout.setAlpha(1f);
//                ((PalContentListingActivity) context).firstTimeTestHide = false;
//            }
        }
    }

    public PalTestFrament(String contentType) {
        this.categoryID = contentType;
    }

    private void setTestList(ArrayList<String> testsTypeList) {
        for(String item: testsTypeList){
            getTestDetails(item);
        }
    }

    private void getTestDetails(String type) {
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, subject, type, topicID, "testTask");
        }else{
            try {
                global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(subject).child(type).child(topicID).child("detail").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                HashMap<String, Object> dataHashMap = (HashMap<String, Object>) snapshot.getValue();
                                HashMap<String, String> scoreMap = (HashMap<String, String>) dataHashMap.get("scores");
                                assert scoreMap != null;
                                String score = scoreMap.get("scores");
                                String totalScore = scoreMap.get("totalScores");
                                String scoresToShow = score + "/" + totalScore;
                                if(type.equalsIgnoreCase("simple_test")){
    //                            textViewResult.setText("Final Test Score is : " + scoresToShow);
                                    TestModel testModel = new TestModel("परीक्षा", scoresToShow);
                                    if(testsList.size() > 0){
                                        for(TestModel item: testsList){
                                            if(item.getName().equals("परीक्षा")){
                                                testsList.remove(item);
                                                break;
                                            }
                                        }
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                }else if(type.equalsIgnoreCase("diagonostic_test")){
    //                            textViewResult.setText("Diagonostic Test Score is : " + scoresToShow);
                                    TestModel testModel = new TestModel("डायग्नोस्टिक परीक्षण", scoresToShow);
                                    if(testsList.size() > 0){
                                        for(TestModel item: testsList){
                                            if(item.getName().equals("डायग्नोस्टिक परीक्षण")){
                                                testsList.remove(item);
                                                break;
                                            }
                                        }
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                }else if(type.equalsIgnoreCase("bi_monthly_test")){
    //                            textViewResult.setText("Bi Monthly Test Score is : " + scoresToShow);
                                }
    //                        textViewResult.setVisibility(View.VISIBLE);

                                //For Latest data
                                try {
                                    if (reportsLatestDataTestRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), type, topicID)) {
                                        ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
                                        reportsTestScoreModel.setDate(scoreMap.get("date"));
                                        reportsTestScoreModel.setQuestionsAttempted(scoreMap.get("questionsAttempted"));
                                        reportsTestScoreModel.setScores(scoreMap.get("scores") + "");
                                        reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
                                        reportsTestScoreModel.setTotalQuestions(scoreMap.get("totalQuestions") + "");
                                        reportsTestScoreModel.setTotalScores(scoreMap.get("totalScores") + "");
                                        reportsTestScoreModel.setPercentageScored(scoreMap.get("scores") + "");

                                        ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();

                                        String listData = DataTypeConverter.ListToString(list);
                                        reportsLatestDataTestRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), type, topicID, listData, reportsTestScoreModel.getDate(),
                                                reportsTestScoreModel.getQuestionsAttempted(), reportsTestScoreModel.getScores(), reportsTestScoreModel.getTopicName(), reportsTestScoreModel.getTotalQuestions(), reportsTestScoreModel.getTotalScores(), reportsTestScoreModel.getPercentageScored());
                                    } else {
                                        ReportsTestScoreModel reportsTestScoreModel = new ReportsTestScoreModel();
                                        reportsTestScoreModel.setDate(scoreMap.get("date"));
                                        reportsTestScoreModel.setQuestionsAttempted(scoreMap.get("questionsAttempted"));
                                        reportsTestScoreModel.setScores(scoreMap.get("scores") + "");
                                        reportsTestScoreModel.setTopicName(Util.getTopicNameAlt(context));
                                        reportsTestScoreModel.setTotalQuestions(scoreMap.get("totalQuestions") + "");
                                        reportsTestScoreModel.setTotalScores(scoreMap.get("totalScores") + "");
                                        reportsTestScoreModel.setPercentageScored(scoreMap.get("scores") + "");

                                        ArrayList<ReportsTestDetailReviewModel> list = new ArrayList<>();

                                        ReportsLatestDataTestModel reportsLatestDataTestModel = new ReportsLatestDataTestModel();
                                        reportsLatestDataTestModel.setUserId(Util.getUserId(context));
                                        reportsLatestDataTestModel.setBoard(board);
                                        reportsLatestDataTestModel.setSClass(sClass);
                                        reportsLatestDataTestModel.setSubject(Util.getSubject(context));
                                        reportsLatestDataTestModel.setType("diagnostic_test");
                                        reportsLatestDataTestModel.setTopicId(topicID);
                                        reportsLatestDataTestModel.setList(list);
                                        reportsLatestDataTestModel.setTimeTaken(String.valueOf(scoreMap.get("date")));
                                        reportsLatestDataTestModel.setReportsTestScoreModel(reportsTestScoreModel);
                                        reportsLatestDataTestRepository.insertTestDetails(reportsLatestDataTestModel);
                                    }
                                }catch (Exception r){
                                    r.printStackTrace();
                                }
                            } else {
                                if(type.equalsIgnoreCase("simple_test")){
                                    TestModel testModel = new TestModel("परीक्षा", "0");
                                    if(testsList.size() > 0){
                                        for(TestModel item: testsList){
                                            if(item.getName().equals("परीक्षा")){
                                                testsList.remove(item);
                                                break;
                                            }
                                        }
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                } else if(type.equalsIgnoreCase("diagonostic_test")){
                                    TestModel testModel = new TestModel("डायग्नोस्टिक परीक्षण", "0");
                                    if(testsList.size() > 0){
                                        for(TestModel item: testsList){
                                            if(item.getName().equals("डायग्नोस्टिक परीक्षण")){
                                                testsList.remove(item);
                                                break;
                                            }
                                        }
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                }
    //                        textViewResult.setVisibility(View.GONE);
    //                        Util.showToast(context, "Not yet attempted");
                            }
                            testsListAdapter.testsList = testsList;
                            testsListAdapter.notifyDataSetChanged();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getMastery() {
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), "practice", topicID, "practiceTask");
        }else{
            global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(ApplicationConstants.ANALYTICS).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(sClass).
                    child(Util.getSubject(context))
                    .child(topicID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            System.out.println("-----------  snapshot  "+ snapshot);
                            String mastery = "0";
                            if (snapshot.getValue() != null) {
                                HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                                if (dataMap != null) {
                                    mastery = (String) dataMap.get("mastery");
                                } else {
                                    mastery = "0";
                                }
                            } else {
                                mastery = "0";
                            }
                            if(mastery.equals("100")){
                                if(Util.isNetworkAvailable(context) || Util.isOfflineMode(context))
                                {
                                    if(Util.isPortraitMode(context)) startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity.class).putExtra("sClass", sClass));
                                    else startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

                                } else {
                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                    {
                                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                    }else {
                                        Util.openGifDialogue(context,"Internet Connection is not working");
                                    }
                                }
                            }else{
                                Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);
//                                Util.showAnimatedDialog(context,Util.DIAGNOSTIC_IS_COMPLETED);
//                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                {
//                                    Util.openGifDialogue(context, Util.getTopicNameAlt(context)+" के अंतिम परीक्षण का प्रयास करने के लिए आपको अभ्यास में 100% महारत हासिल करनी होगी");
//
//                                }else {
//                                    //  Util.showToast(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                                    Util.openGifDialogue(context, "You have to achieve 100% mastery in practice to attempt the Final Test of "+Util.getTopicNameAlt(context));
//                                }
                                // Util.showToast(context, "You have to achieve 100% mastery in practice to attempt the Final Test of "+Util.getTopicNameAlt(context));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }
    }

    private void getDiagnosticMastery() {

        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data_diagnostic").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSelectedLanguage(context)).child(Util.getSubject(context)).child("diagonostic_test").child(topicID).child("detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String questionsAttempted = "0";
                    if (snapshot.getValue() != null) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                        if (dataMap != null) {
                            questionsAttempted = (String) dataMap.get("questionsAttempted");
                        } else {
                            questionsAttempted = "0";
                        }
                    } else {
                        questionsAttempted = "0";
                    }
                    if(questionsAttempted.equals("12")){
                        Util.showAnimatedDialog(context,Util.DIAGNOSTIC_IS_COMPLETED);
//                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                        {
//                            Util.openGifDialogue(context, "आप पहले ही "+Util.getTopicNameAlt(context)+ " के डायग्नोस्टिक ​​परीक्षण का प्रयास कर चुके हैं");
//
//                        }else {
//                            //  Util.showToast(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                            Util.openGifDialogue(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                        }
                    }else {
                        Intent intent = new Intent(context, DStartActivity.class);
                        intent.putExtra("sClass", sClass);
                        context.startActivity(intent);

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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String type, String topicId, String taskType) {
        if(taskType.equals("practiceTask")){
            getList(userId, board, sClass, subject, type, topicId, "practiceTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            practiceDisposable = d;
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
                            if(mastery.equals("100")){
                                if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context))
                                {
                                    if(Util.isPortraitMode(context)) startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity.class).putExtra("sClass", sClass));
                                    else startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

                                } else {
                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                    {
                                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                    }else {
                                        Util.openGifDialogue(context,"Internet Connection is not working");
                                    }
                                }
                            }else{
//                                Util.showToast(context, "You have to achieve 100% mastery in practice to attempt the Final Test of "+Util.getTopicNameAlt(context));
//                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                {
//                                    Util.openGifDialogue(context, Util.getTopicNameAlt(context)+" के अंतिम परीक्षण का प्रयास करने के लिए आपको अभ्यास में 100% महारत हासिल करनी होगी");
//
//                                }else {
//                                    //  Util.showToast(context, "You have already attempted the Diagnostic Test of "+Util.getTopicNameAlt(context));
//                                    Util.openGifDialogue(context, "You have to achieve 100% mastery in practice to attempt the Final Test of "+Util.getTopicNameAlt(context));
//                                }
                                Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            practiceDisposable.dispose();
                        }
                    });
        }else if(taskType.equals("testTask")){
            getList(userId, board, sClass, subject, type, topicId, "testTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            testDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            testsListAdapter.testsList.clear();
                            ArrayList<ReportsLatestDataTestModel> list = (ArrayList<ReportsLatestDataTestModel>) o;
                            ArrayList<String> testNameList=new ArrayList<>();
                            for(ReportsLatestDataTestModel item: list){
                                String score = item.getReportsTestScoreModel().getScores();
                                String totalScore = item.getReportsTestScoreModel().getTotalScores();
                                String scoresToShow = score + "/" + totalScore;
                                if(item.getType().equals("diagnostic_test")){
                                    testNameList.add("diagnostic_test");
                                    TestModel testModel = new TestModel("डायग्नोस्टिक परीक्षण", scoresToShow);
                                    if(testsList.size() > 0){
                                        try
                                        {
                                            for(TestModel data: testsList){
                                                if(data.getName().equals("डायग्नोस्टिक परीक्षण")){
                                                    testsList.remove(data);
//                                                break;
                                                }
                                            }
                                        }catch (Exception r){}
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                }
                                else if(item.getType().equals("simple_test")){
                                    testNameList.add("simple_test");
                                    TestModel testModel = new TestModel("परीक्षा", scoresToShow);
                                    if(testsList.size() > 0){
                                        try
                                        {
                                            for(TestModel data: testsList){
                                                if(data.getName().equals("परीक्षा")){
                                                    testsList.remove(data);
//                                                break;
                                                }
                                            }
                                        }catch (Exception e){}
                                        testsList.add(testModel);
                                    }else{
                                        testsList.add(testModel);
                                    }
                                }
//                                break;
                            }


                            if(!testNameList.contains("diagnostic_test")) {
                                TestModel item=new TestModel();
                                item.setScore("0");
                                item.setName("डायग्नोस्टिक परीक्षण");
                                testsList.add(item);
                            }

                            if(!testNameList.contains("simple_test")) {
                                TestModel item=new TestModel();
                                item.setScore("0");
                                item.setName("परीक्षा");
                                testsList.add(item);
                            }

                            testsListAdapter.testsList = testsList;
                            testsListAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            testDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String type, String topicId, String taskType) {
        if(taskType.equals("practiceTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, type, topicId);
            });
        }else if(taskType.equals("testTask")){
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataTestRepository.getDetail(userId, board, sClass, subject, topicId);
            });
        }
        return null;
    }

    private void getTestReports(HashMap<String, String> practiceMap, PracticeAdapter practiceAdapter) {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(topicID).child("detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        String mastery = "0";
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();

                        HashMap<String, Object> innerDataMap__ = (HashMap<String, Object>) dataMap.get(topicID);
                        if (innerDataMap__ != null) {
                            for (String main : innerDataMap__.keySet()) {
                                HashMap<String, Object> masteryMap = (HashMap<String, Object>) innerDataMap__.get(main);
                                mastery = (String) masteryMap.get("mastery");
                            }
                            practiceMap.put("mastery", mastery);
                        } else {
                            practiceMap.put("mastery", "0");
                        }
                    } else {
                        practiceMap.put("mastery", "0");
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
