package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.R.drawable.ic_rectangle_roound_corner_background;
import static com.idreameducation.ipreppal.userActivities.UserActivities.VIDEO_CONTENT_KEY;
import static com.idreameducation.ipreppal.userActivities.UserActivities.searchFromName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.CoreContent.PracticeDataModel;
import com.idreameducation.ipreppal.model.CoreContent.VideoDataModel;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.adapter.GlobalSearch.PracticeAdapter;
import com.idreameducation.ipreppal.pal.adapter.GlobalSearch.VideosAdapter;
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
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalGlobalSearchActivity extends AppCompatActivity {
    Context context;
    Global global;
    EditText searchEdittext;
    TextView catTotalResultsTextView,totalResultsTextView,videoTextView,practiceTextView,categoryTypeTextView;
    ImageView searchBtn,videoImageView,practiceImageView;
    RecyclerView recyclerView;
    LinearLayout selectVideoLayout,selectPracticeLayout,searchImageLayout;
    LinearLayout selectablePracticeLayout,selectableVideoLayout,loadingLayout;
    VideosAdapter videosAdapter;
    PracticeAdapter practiceAdapter;
    int VIDEO_FILTER=1,PRACTICE_FILTER=2;
    int selectedFilter=VIDEO_FILTER;
    int videoCount=0,practiceCount=0;
    public static PalGlobalSearchActivity palGlobalSearchActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.setWindowSettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_global_search);
        palGlobalSearchActivity=this;

        init();

    }
    private void init() {

        /** init views */
        context = this;
        global= (Global) getApplicationContext();

        UserActivities userActivities=new UserActivities(context);
        userActivities.fetchCoreContent();

        recyclerView = findViewById(R.id.recyclerView);

        searchEdittext = findViewById(R.id.searchEdittext);
        searchBtn = findViewById(R.id.searchBtn);

        selectPracticeLayout = findViewById(R.id.selectPracticeLayout);
        selectVideoLayout = findViewById(R.id.selectVideoLayout);
        loadingLayout = findViewById(R.id.loadingLayout);


        selectablePracticeLayout = findViewById(R.id.selectablePracticeLayout);
        selectableVideoLayout = findViewById(R.id.selectableVideoLayout);

        catTotalResultsTextView = findViewById(R.id.catTotalResultsTextView);
        totalResultsTextView = findViewById(R.id.totalResultsTextView);

        videoTextView = findViewById(R.id.videoTextView);
        practiceTextView = findViewById(R.id.practiceTextView);

        videoImageView = findViewById(R.id.videoImageView);
        practiceImageView = findViewById(R.id.practiceImageView);

        categoryTypeTextView = findViewById(R.id.categoryTypeTextView);
        searchImageLayout = findViewById(R.id.searchImageLayout);


        showLoadingLayout();

        /** setup layout manager in recyclerview */
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));


        /** check search text changes */
        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                /** show search image */
                if (charSequence.length() < 3) {
                    searchImageLayout.setVisibility(View.VISIBLE);
                    return;
                }
                else searchImageLayout.setVisibility(View.GONE);

                /** check videos is selected to show or not */
                getSearchVideos(charSequence.toString());

                /** check practice is selected to show or not  */
                getSearchPractice(charSequence.toString());

                if(selectedFilter==PRACTICE_FILTER && practiceAdapter!=null) recyclerView.setAdapter(practiceAdapter);
                else if(videosAdapter!=null) recyclerView.setAdapter(videosAdapter);
                showResultCount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getDataFromRoomDatabase();
        setupSelectedFilter();
        clickEvents();

    }
    private void showLoadingLayout() {

        if(checkContentAvailable()) loadingLayout.setVisibility(View.GONE);
        else new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoadingLayout();
                }
            },500);

    }
    private boolean checkContentAvailable() {
        ArrayList<VideoDataModel> list = searchFromName(VIDEO_CONTENT_KEY,"");

        if(list.size()!=0) return true;

        return false;
    }
    private void clickEvents() {

        selectVideoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter=VIDEO_FILTER;
                setupSelectedFilter();
                if(videosAdapter!=null) recyclerView.setAdapter(videosAdapter);
                else getSearchVideos(searchEdittext.getText().toString().toLowerCase().trim());
            }
        });

        selectPracticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter=PRACTICE_FILTER;
                setupSelectedFilter();
                if(practiceAdapter!=null) recyclerView.setAdapter(practiceAdapter);
                else getSearchVideos(searchEdittext.getText().toString().toLowerCase().trim());

            }
        });

        findViewById(R.id.backImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




    }
    private void getSearchVideos(String searchText) {

        videoCount=0;
        if (searchText.length() == 0) videosAdapter = new VideosAdapter(new ArrayList<VideoDataModel>());
        else if (searchText.length() < 3) return;
        else {
            /** getting data from Pref */
            ArrayList<VideoDataModel> list = searchFromName(VIDEO_CONTENT_KEY, searchText);
            videosAdapter = new VideosAdapter(list);
            videoCount=list.size();
        }



    }
    private void getSearchPractice(String searchText) {
        practiceCount=0;
        if (searchText.length() == 0) practiceAdapter = new PracticeAdapter(new ArrayList<PracticeDataModel>());
        else if (searchText.length() < 3) return;
        else {
            /** getting data from Pref */
            ArrayList<PracticeDataModel> list2 = UserActivities.searchFromName2(UserActivities.PRACTICE_CONTENT_KEY, searchText);
            practiceAdapter = new PracticeAdapter(list2);
            practiceCount=list2.size();
        }

    }
    private void setupSelectedFilter() {

        if(selectedFilter==VIDEO_FILTER) {
            /** set category name */
            categoryTypeTextView.setText("Videos");

            /** set selectable background  */
            selectableVideoLayout.setBackground(context.getResources().getDrawable(ic_rectangle_roound_corner_background));
            selectablePracticeLayout.setBackground(null);

            /** change selectable text colour */
            videoTextView.setTextColor(context.getResources().getColor(R.color.blue));
            practiceTextView.setTextColor(context.getResources().getColor(R.color.textcolour));

            /** change selectable icon */
            videoImageView.setColorFilter(context.getResources().getColor(R.color.blue));
            practiceImageView.setColorFilter(context.getResources().getColor(R.color.grey));
        }

        else if(selectedFilter==PRACTICE_FILTER) {
            /** set category name */
            categoryTypeTextView.setText("Practice");

            /** set selectable background  */
            selectablePracticeLayout.setBackground(context.getResources().getDrawable(ic_rectangle_roound_corner_background));
            selectableVideoLayout.setBackground(null);

            /** change selectable text colour */
            videoTextView.setTextColor(context.getResources().getColor(R.color.textcolour));
            practiceTextView.setTextColor(context.getResources().getColor(R.color.blue));

            /** change selectable icon */
            videoImageView.setColorFilter(context.getResources().getColor(R.color.grey));
            practiceImageView.setColorFilter(context.getResources().getColor(R.color.blue));
        }

        showResultCount();
    }

    private void showResultCount() {

        if(selectedFilter==VIDEO_FILTER) catTotalResultsTextView.setText(videoCount+" Results");
        else if(selectedFilter==PRACTICE_FILTER) catTotalResultsTextView.setText(practiceCount+" Results");

        if(selectedFilter==VIDEO_FILTER) totalResultsTextView.setText(videoCount+" Results");
        else if(selectedFilter==PRACTICE_FILTER) totalResultsTextView.setText(practiceCount+" Results");
//        totalResultsTextView.setText("Total "+(videoCount+practiceCount)+" results");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromRoomDatabase();
    }


    // need to remove this code from hear to a new class

    private void getDataFromRoomDatabase() {
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


        runBackgroundTask(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), Util.getSubject(context), null,null, null, "testScore");
        runBackgroundTask(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), Util.getSubject(context), null,null, null, "foundationalTopicData");
    }

    public void setPath(String board, String sClass, String subject, String topicId, String value) {
        if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
            runBackgroundTask(Util.getUserId(context), board, sClass, subject, topicId, value, "pathTask");
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), topicId, value, "testScore");
        } else {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String path = (String) snapshot.getValue();
                        StringBuilder strBuilder = new StringBuilder();
                        if (!path.contains(value)) {
                            strBuilder.append(path).append("-").append(value);
                            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).setValue(strBuilder.toString());
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
                        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_path").child(Util.getUserId(context)).child(board).child(sClass).child(subject).child(topicId).setValue(strBuilder.toString());
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    String topicID;

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

    public void runBackgroundTask(String userId, String board, String sClass, String subject, String topicId, String value, String type) {

        topicID = topicId;
        if(board==null)board=Util.getSelectedBoard(context);
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
            String finalBoard = board;
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
                                        if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), finalBoard, sClass, subject, topicId)) {
                                            reportsTopicPathRepository.updateField(Util.getUserId(context), finalBoard, sClass, subject, topicId, strBuilder.toString());
                                        } else {
                                            ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                            reportsTopicPathModel.setUserId(Util.getUserId(context));
                                            reportsTopicPathModel.setBoard(finalBoard);
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
                                    if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), finalBoard, sClass, subject, topicId)) {
                                        reportsTopicPathRepository.updateField(Util.getUserId(context), finalBoard, sClass, subject, topicId, strBuilder.toString());
                                    } else {
                                        ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
                                        reportsTopicPathModel.setUserId(Util.getUserId(context));
                                        reportsTopicPathModel.setBoard(finalBoard);
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
        for (TestScoreModel item : testScoreModelArrayList) {
            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
                return true;
            }
        }
        return false;
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


}