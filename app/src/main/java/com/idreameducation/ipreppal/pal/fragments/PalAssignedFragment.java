package com.idreameducation.ipreppal.pal.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.adapter.PalAssignedAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FinalTestCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicPathModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FinalTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.LastTopicDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsDiagnosticTestCompleteRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataPracticeRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataTestRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicPathRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TestDetailsRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicPositionRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TopicSubjectRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.TrackTopicRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.VideoDetailsRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalAssignedFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private String subject;
    private String batchName;
    private String batchID;
    private String topicID;
    private ImageView imageViewOops;
    public ArrayList<HashMap<String, String>> booksArrayList;
    public List<FoundationalTopicModel> foundationalTopicData;
    FoundationalTopicModel foundationalTopicModel;
    private FoundationalTopicRepository foundationalTopicRepository;
    private Disposable foundationalTopicDisposable;
    private Disposable pathDisposable;
    private Disposable lastTopicIdDisposable;
    private Disposable videoListingTopicDisposable;
    private Disposable diagnosticTestCompleteDisposable;
    private Disposable testScoreTopicDisposable;
    private Disposable practiceScoreTopicDisposable;
    private Disposable finalTestCompleteDisposable;
    public ArrayList<VideoModel> topicVideoArrayList = new ArrayList<>();
    private VideoDetailsRepository videoDetailsRepository;
    private TestDetailsRepository testDetailsRepository;
    private TopicSubjectRepository topicSubjectRepository;
    private TopicRepository topicRepository;
    private TopicPositionRepository topicPositionRepository;
    private ReportsDiagnosticTestCompleteRepository reportsDiagnosticTestCompleteRepository;
    private ReportsTopicPathRepository reportsTopicPathRepository;
    private TrackTopicRepository trackTopicRepository;
    private ReportsLatestDataTestRepository reportsLatestDataTestRepository;
    private ReportsLatestDataPracticeRepository reportsLatestDataPracticeRepository;
    private LastTopicDetailsRepository lastTopicDetailsRepository;
    private FinalTestCompleteRepository finalTestCompleteRepository;
    public ArrayList<TestScoreModel> testScoreModelArrayList = new ArrayList<>();
    public static ArrayList<PracticeScoreModel> practiceScoreModelArrayList = new ArrayList<>();
    private final TreeSet<String> topicPathData = new TreeSet<>();
    public ArrayList<String> finalTestTopicIdArrayList=new ArrayList<>();
    TextView noAssignmentText;
    public static PalAssignedFragment palAssignedFragment;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iprep_batches_assigned_fragment, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        imageViewOops = view.findViewById(R.id.imageViewOops);
        noAssignmentText = view.findViewById(R.id.noAssignmentText);
        palAssignedFragment=this;

        videoDetailsRepository = new VideoDetailsRepository(context);
        foundationalTopicRepository = new FoundationalTopicRepository(context);
        testDetailsRepository = new TestDetailsRepository(context);
        topicSubjectRepository = new TopicSubjectRepository(context);
        topicRepository = new TopicRepository(context);
        topicPositionRepository = new TopicPositionRepository(context);
        reportsDiagnosticTestCompleteRepository = new ReportsDiagnosticTestCompleteRepository(context);
        reportsTopicPathRepository = new ReportsTopicPathRepository(context);
        trackTopicRepository = new TrackTopicRepository(context);
        reportsLatestDataTestRepository = new ReportsLatestDataTestRepository(context);
        reportsLatestDataPracticeRepository = new ReportsLatestDataPracticeRepository(context);
        lastTopicDetailsRepository = new LastTopicDetailsRepository(context);
        finalTestCompleteRepository = new FinalTestCompleteRepository(context);

        if (BatchesFragments.textArrayList!=null) noAssignmentText.setText(BatchesFragments.textArrayList.get(8));

        try {
            batchID = global.getBatchID();
            getAssignedContent();
            getDiagnosticReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public PalAssignedFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(Util.isDevelopmentSettingsEnabled(context)) Util.showDeveloperOptionPopup(getActivity());
        updateTask(topicID);
    }

    public void updateTask(String topicID) {
        runBackgroundTask(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), Util.getSubject(context), topicID,topicID, null, "testScore");
        getFoundationTopicDetails();
    }
    private void getAssignedContent() throws Exception {

        runBackgroundTask(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), Util.getSubject(context), null,null, null, "testScore");

        getFoundationTopicDetails();
//        global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    if (dataSnapshot.getValue() != null) {
//                        HashMap<String, Object> assignedContentHashMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                        ArrayList<String> dateArrayList = new ArrayList<>();
//                        for (String date : assignedContentHashMap.keySet()) {
//                            dateArrayList.add(date);
//                        }
//                        if (dateArrayList.size() > 0) {
//                            imageViewOops.setVisibility(View.GONE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        } else {
//                            recyclerView.setVisibility(View.GONE);
//                            imageViewOops.setVisibility(View.VISIBLE);
//                        }
//                        PalAssignedAdapter palAssignedAdapter = new PalAssignedAdapter(context, assignedContentHashMap, dateArrayList);
//
//                        recyclerView.setAdapter(palAssignedAdapter);
//                        palAssignedAdapter.SetOnItemClickListener(new PalAssignedAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                            }
//                        });
//                        noAssignmentText.setVisibility(View.GONE);
//                        // Adapter to be added here
//                    } else {
//                        recyclerView.setVisibility(View.GONE);
//                        imageViewOops.setVisibility(View.VISIBLE);
//                        noAssignmentText.setVisibility(View.VISIBLE);
//                    }
//                } catch (Exception e) {
//                    noAssignmentText.setVisibility(View.VISIBLE);
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void runBackgroundTask(String userId, String board, String sClass, String subject, String lastTopicId, String topicId, String value, String type) {

        topicID = topicId;
        if (type.equals("lastTopicIdTask")) {
            getList(userId, board, sClass, subject, topicId, value, "lastTopicIdTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            lastTopicIdDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
//                        updateUi((ArrayList<TopicSubjectWiseModel>) o);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            lastTopicIdDisposable.dispose();
                        }
                    });
        } else if (type.equals("pathTask")) {
            getList(userId, board, sClass, subject, topicId, value, "pathTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            pathDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsTopicPathModel> list = (ArrayList<ReportsTopicPathModel>) o;
                            if (topicId == null) {
//                                if (list != null && list.size() > 0) {
//                                    data = new HashMap<>();
//                                    for (ReportsTopicPathModel item : list) {
//                                        data.put(item.getTopicId(), item.getTypeV());
//                                    }
//                                }
//                                try {
//                                    isCompleted = false;
//                                    countTopic = 0;
//                                    count = 0;
//                                    getTopicSeenVideoListing(false);
//                                    getTopics(subject);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            } else {
                                if (list != null && list.size() > 0) {
                                    String path = list.get(0).getTypeV();
                                    StringBuilder strBuilder = new StringBuilder();
                                    if (!path.contains(value)) {
                                        strBuilder.append(path).append("-").append(value);
                                        if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
                                            reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
                                        } else {
                                            ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                            reportsTopicPathModel.setUserId(Util.getUserId(context));
                                            reportsTopicPathModel.setBoard(board);
                                            reportsTopicPathModel.setSClass(sClass);
                                            reportsTopicPathModel.setSubject(subject);
                                            reportsTopicPathModel.setTopicId(topicId);
                                            reportsTopicPathModel.setTypeV(strBuilder.toString());
                                            reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                                        }
                                    }
                                } else {
                                    topicPathData.add(topicId + "-" + value);
                                    StringBuilder strBuilder = new StringBuilder();
                                    int count = 0;
                                    for (String item : topicPathData) {
                                        String[] separated = item.split("-");
                                        if (separated[0].equals(topicId)) {
                                            if (count == 0) {
                                                strBuilder.append(separated[1]);
                                            } else {
                                                strBuilder.append("-").append(separated[1]);
                                            }
                                        }
                                        count++;
                                    }
                                    if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
                                        reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
                                    } else {
                                        ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                        reportsTopicPathModel.setUserId(Util.getUserId(context));
                                        reportsTopicPathModel.setBoard(board);
                                        reportsTopicPathModel.setSClass(sClass);
                                        reportsTopicPathModel.setSubject(subject);
                                        reportsTopicPathModel.setTopicId(topicId);
                                        reportsTopicPathModel.setTypeV(strBuilder.toString());
                                        reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            pathDisposable.dispose();
                        }
                    });
        } else if (type.equals("videoListingTask")) {
            getList(userId, board, sClass, subject, topicId, value, "videoListingTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            videoListingTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            topicVideoArrayList = (ArrayList<VideoModel>) o;
//                            if (refreshAdapter && practiceTopicAdapter != null) {
//                                practiceTopicAdapter.notifyDataSetChanged();
//                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            videoListingTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("diagnosticTestComplete")) {
            getList(userId, board, sClass, subject, topicId, value, "diagnosticTestComplete").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            diagnosticTestCompleteDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsDiagnosticCompleteModel> list = (ArrayList<ReportsDiagnosticCompleteModel>) o;
//                            try {
//                                String comp = "false";
//                                if (list != null && list.size() > 0) {
//                                    if (list.get(0).isComplete()) {
//                                        comp = "true";
//                                    } else {
//                                        comp = "false";
//                                    }
//                                }
//                                for (HashMap<String, String> item : topicsArrayList) {
//                                    if (item.get("TopicID").equals(topicId)) {
//                                        item.put("isCompleted", comp);
//                                        break;
//                                    }
//                                }
//                                practiceTopicAdapter.topicsArrayList = topicsArrayList;
//                                practiceTopicAdapter.setTooltipVisibility(true);
//                                practiceTopicAdapter.notifyDataSetChanged();
//                                refreshTabLayout(Util.getTopicID(Util.getContext()), true);
//
//                                boolean isCompleted = false;
//                                isCompleted = comp.equals("true");
//                                if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId)) {
//                                    reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId, isCompleted);
//                                } else {
//                                    ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
//                                    reportsDiagnosticCompleteModel.setUserId(Util.getUserId(Util.getContext()));
//                                    reportsDiagnosticCompleteModel.setBoard(board);
//                                    reportsDiagnosticCompleteModel.setSClass(sClass);
//                                    reportsDiagnosticCompleteModel.setSubject(Util.getSubject(Util.getContext()));
//                                    reportsDiagnosticCompleteModel.setTopicId(topicId);
//                                    reportsDiagnosticCompleteModel.setComplete(isCompleted);
//                                    reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

//                        try {
//                            String comp = "false";
//                            if (list != null && list.size() > 0) {
//                                if(list.get(0).isComplete()){
//                                    comp = "true";
//                                }else{
//                                    comp = "false";
//                                }
//                            }
//                            if(countTopic <= topicsArrayList.size() - 1){
//                                if(countTopic == 0){
//                                    if(topicsArrayList.get(0).get("TopicID").equals(topicId)){
//                                        topicsArrayList.get(countTopic).put("isCompleted", comp);
//                                    }else{
//                                        String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                                        return;
//                                    }
//                                }else{
//                                    topicsArrayList.get(countTopic).put("isCompleted", comp);
//                                }
//                            }
//                            countTopic++;
//                            if(countTopic <= topicsArrayList.size() - 1){
//                                String tID = topicsArrayList.get(countTopic).get("TopicID");
//                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
//                            }
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
//                        disposable.dispose();
                        }
                    });
        } else if (type.equals("foundationalTopicData")) {
            getList(userId, board, sClass, subject, topicId, value, "foundationalTopicData").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            foundationalTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {

                            List<FoundationalTopicModel> list = (List<FoundationalTopicModel>) o;
                            if (list != null && list.size() > 0) {
                                foundationalTopicData = list;
                            }
                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {

                                for (FoundationalTopicModel item : foundationalTopicData) {
                                    if(lastTopicId!=null)
                                    {
                                        if (lastTopicId.equals(item.getSeniorTopicID())) {
                                            if (!isPracticeCompleted(item.getTopicId())) {
                                                int position = 0;

//                                            completeType = "foundationalPractice";
//                                            static_completeType = "foundationalPractice";
//                                            for (int i = 0; i < topicsArrayList.size(); i++) {
//                                                if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
//                                                    position = i;
//                                                    break;
//                                                }
//                                            }
//
//                                            List<Fragment> allFragments = getSupportFragmentManager().getFragments();
//                                            for (Fragment fragment : allFragments) {
//                                                if (fragment instanceof PalVideoListFragment) {
//                                                    ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
//                                                } else if (fragment instanceof PalDikshaContentFragment) {
//                                                    ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
//                                                }
//                                            }
//                                            break;
                                            }
                                        }
                                    }

                                }
                            }
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            foundationalTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("testScore")) {

            getList(userId, board, sClass, subject, topicId, value, "testScore").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            testScoreTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            List<ReportsLatestDataTestModel> list = (ArrayList<ReportsLatestDataTestModel>) o;
                            for (ReportsLatestDataTestModel item : list) {
                                if (testScoreModelArrayList.size() > 0) {
                                    for (TestScoreModel data : testScoreModelArrayList) {
                                        if (data.getTopicId().equals(item.getTopicId()) && data.getType().equals(item.getType())) {
                                            testScoreModelArrayList.remove(data);
                                            break;
                                        }
                                    }
                                }
                                testScoreModelArrayList.add(new TestScoreModel(item.getTopicId(), item.getType(), item.getReportsTestScoreModel().getScores() + "/" + item.getReportsTestScoreModel().getTotalScores(), item.getReportsTestScoreModel().getPercentageScored(), item.getTimeTaken()));
                            }
                            runBackgroundTask(userId, board, sClass, subject, lastTopicId,topicId, value, "practiceScore");
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            testScoreTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("practiceScore")) {
            getList(userId, board, sClass, subject, topicId, value, "practiceScore").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            practiceScoreTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            ArrayList<ReportsLatestDataPracticeModel> list = (ArrayList<ReportsLatestDataPracticeModel>) o;
                            for (ReportsLatestDataPracticeModel item : list) {
                                if (practiceScoreModelArrayList.size() > 0) {
                                    for (PracticeScoreModel data : practiceScoreModelArrayList) {
                                        if (data.getTopicId().equals(item.getTopicId())) {
                                            practiceScoreModelArrayList.remove(data);
                                            break;
                                        }
                                    }
                                }
                                practiceScoreModelArrayList.add(new PracticeScoreModel(item.getTopicId(), item.getMastery(),item.getStreakProgress(),item.getCurrentLevel()));
                            }

                            global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        if (dataSnapshot.getValue() != null) {
                                            HashMap<String, Object> assignedContentHashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                                            HashMap<String, Object> assignedContentHashMap2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                            ArrayList<String> dateArrayList = new ArrayList<>();
                                            ArrayList<String> converedDateArrayList = new ArrayList<>();

                                            /** final HashMap  */
                                            HashMap<String,Object> map1=new HashMap<>();

                                            for (String date : assignedContentHashMap.keySet()) {
                                                HashMap<String,Object> mape= (HashMap<String, Object>) assignedContentHashMap.get(date);

                                                // check if it assined to me or not
                                                for (String name : mape.keySet()) {


                                                    HashMap<String,Object> mapwe= (HashMap<String, Object>) mape.get(name);

                                                    for (String namde : mapwe.keySet()) {


                                                        HashMap<String,Object> mawwpwe= (HashMap<String, Object>) mapwe.get(namde);
                                                        HashMap<String,Object> m= (HashMap<String, Object>) mawwpwe.get("info");

                                                        HashMap<String,Object> w= (HashMap<String, Object>) m.get("st_list");


                                                        if(w.containsKey(Util.getUserId(context))) {



                                                            /** this hashmap handling content details */
                                                            HashMap<String,Object> map21;

                                                            /** check Date first */

                                                            String convertedDate = Util.timestampToDate(Long.valueOf(date));

                                                            /** check date is already available in map1 */
                                                            if(map1.containsKey(convertedDate)) {
                                                                /** fetching old values */
                                                                map21 = (HashMap<String, Object>) map1.get(convertedDate);
                                                                map21.put(date,assignedContentHashMap);
                                                            }
                                                            else {
                                                                /** created new hashmap  */
                                                                map21= new HashMap<>();
                                                                map21.put(date,assignedContentHashMap);
                                                                dateArrayList.add(date);
                                                                converedDateArrayList.add(convertedDate);
                                                            }

                                                            /** added content details in converted date */
                                                            map1.put(convertedDate,map21);


                                                        }



                                                    }


                                                }





                                            }
                                            if (dateArrayList.size() > 0) {
                                                imageViewOops.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                            } else {
                                                recyclerView.setVisibility(View.GONE);
                                                imageViewOops.setVisibility(View.VISIBLE);
                                            }
                                            PalAssignedAdapter palAssignedAdapter = new PalAssignedAdapter(context, map1, dateArrayList,converedDateArrayList);

                                            recyclerView.setAdapter(palAssignedAdapter);
                                            noAssignmentText.setVisibility(View.GONE);
                                            // Adapter to be added here
                                        } else {
                                            recyclerView.setVisibility(View.GONE);
                                            imageViewOops.setVisibility(View.VISIBLE);
                                            noAssignmentText.setVisibility(View.VISIBLE);
                                        }
                                    } catch (Exception e) {
                                        global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").removeValue();
                                        noAssignmentText.setVisibility(View.VISIBLE);
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

//                            updateUi();
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            practiceScoreTopicDisposable.dispose();
                        }
                    });
        } else if (type.equals("finalTestCompletionTask")) {
            getList(userId, board, sClass, subject, topicId, value, "finalTestCompletionTask").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            finalTestCompleteDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            finalTestTopicIdArrayList = new ArrayList<>();
                            List<FinalTestCompleteModel> list = (List<FinalTestCompleteModel>) o;
                            if (list != null && list.size() > 0) {
                                for (FinalTestCompleteModel item : list) {
                                    finalTestTopicIdArrayList.add(item.getTopicId());
                                }
                            }
//                        practiceTopicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            finalTestCompleteDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String subject, String topicId, String value, String type) {
        if (type.equals("lastTopicIdTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return topicSubjectRepository.getDetail(userId, subject);
            });
        } else if (type.equals("pathTask")) {
            if (topicId == null) {
                return Observable.fromCallable(() -> {
                    //do something, get your Data object
                    return reportsTopicPathRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, subject, null);
                });
            } else {
                return Observable.fromCallable(() -> {
                    //do something, get your Data object
                    return reportsTopicPathRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, subject, topicId);
                });
            }
        } else if (type.equals("videoListingTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return videoDetailsRepository.getVideoDetails(userId, board, sClass, subject.toLowerCase());
            });
        } else if (type.equals("diagnosticTestComplete")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId);
            });
        } else if (type.equals("foundationalTopicData")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(Util.getContext()));
            });
        } else if (type.equals("testScore")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataTestRepository.getDetail(userId, board, sClass, subject, topicId);
            });
        } else if (type.equals("practiceScore")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, "practice", null);
            });
        } else if (type.equals("finalTestCompletionTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return finalTestCompleteRepository.getDetail(userId, board, sClass, subject);
            });
        }
        return null;
    }

    public boolean isDiagnosticTestAttempted(String topicId) {

        if(diagnosticCompletedList.contains(topicId)) return true;

        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
                return true;
            }
        }
        return false;
    }

    ArrayList<String> diagnosticCompletedList=new ArrayList<>();

    private void getDiagnosticReport() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("d_completed").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child(Util.getSubject(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    diagnosticCompletedList=new ArrayList<>();
                    HashMap<String, String> topicIDs = (HashMap<String, String>) snapshot.getValue();

                    for (String topicID : topicIDs.keySet()) {

                        diagnosticCompletedList.add(topicID);

                        boolean isCompleted = true;
                        if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(context), board, sClass, Util.getSubject(context), topicID, Util.getSelectedLanguage(context)))
                            reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(context), board, sClass, Util.getSubject(context), topicID, isCompleted, Util.getSelectedLanguage(context));
                        else {
                            ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
                            reportsDiagnosticCompleteModel.setUserId(Util.getUserId(context));
                            reportsDiagnosticCompleteModel.setBoard(board);
                            reportsDiagnosticCompleteModel.setSClass(sClass);
                            reportsDiagnosticCompleteModel.setSubject(Util.getSubject(context));
                            reportsDiagnosticCompleteModel.setTopicId(topicID);
                            reportsDiagnosticCompleteModel.setComplete(isCompleted);
                            reportsDiagnosticCompleteModel.setLang(Util.getSelectedLanguage(context));
                            reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
                        }
                    }

                } catch (Exception r) {
                    r.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public TestScoreModel getDiagnosticTestScore(String topicId) {
        TestScoreModel score;
        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
                score = item;
                return score;
            }
        }
        return null;
    }

    public boolean isPracticeCompleted(String topicId) {
        for (PracticeScoreModel item : practiceScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getScore().equals("100")) {
                return true;
            }
        }
        return false;
    }

    public PracticeScoreModel getPracticeScore(String topicId) {
        for (PracticeScoreModel item : practiceScoreModelArrayList) {
            if (topicId.equals(item.getTopicId())) {
                return item;
            }
        }
        return null;
    }

    public boolean isFinalTestAttempted(String topicId) {
        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("simple_test") &&
                    item.getPercentage().equals("100")) {
                return true;
            }
        }
        return false;
    }

    private void getFoundationTopicDetails() {

        try
        {
            foundationalTopicRepository=new FoundationalTopicRepository(context);

            getList().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            foundationalTopicDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {

                            List<FoundationalTopicModel> list = (List<FoundationalTopicModel>) o;
                            if (list != null && list.size() > 0) {
                                foundationalTopicData = list;
                            }



//                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
//
//                                try
//                                {
//                                    for(int f=foundationalTopicData.size()-1;f>=0;f--) {
//
//                                        FoundationalTopicModel item = foundationalTopicData.get(f);
//                                        if (item.getSeniorTopicID().equals(topicID)) {
//                                            for (int p = ((PalContentListingActivity) context).practiceScoreModelArrayList.size() - 1; p >= 0; p--) {
//                                                PracticeScoreModel data = ((PalContentListingActivity) context).practiceScoreModelArrayList.get(p);
//                                                if (data.getTopicId().equals(item.getTopicId())) {
//                                                    if (!data.getScore().equals("100")) {
//
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                    for (FoundationalTopicModel item : foundationalTopicData) {
//                                        if (topicID.equals(item.getSeniorTopicID())) {
//                                            if (!PalAssignedFragment.palAssignedFragment.isPracticeCompleted(item.getTopicId())) {
//                                                int position = 0;
//
////                                                completeType = "foundationalPractice";
////                                                static_completeType = "foundationalPractice";
////                                                for (int i = 0; i < topicsArrayList.size(); i++) {
////                                                    if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
////                                                        position = i;
////                                                        break;
////                                                    }
////                                                }
//
////                                                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
////                                                for (Fragment fragment : allFragments) {
////                                                    if (fragment instanceof PalVideoListFragment) {
////                                                        ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
////                                                    } else if (fragment instanceof PalDikshaContentFragment) {
////                                                        ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
////                                                    }
////                                                }
////                                                    break;
//                                            }
//                                        }
//                                    }
//                                }catch (Exception ee){
//                                    System.out.println("++++++ handled");
//                                }
//
//                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            foundationalTopicDisposable.dispose();
                        }
                    });


        }catch (Exception f){}
    }

    private Observable<Object> getList() {
        return Observable.fromCallable(() -> {
            //do something, get your Data object
            return foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(Util.getContext()));
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = Resources.getSystem();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
