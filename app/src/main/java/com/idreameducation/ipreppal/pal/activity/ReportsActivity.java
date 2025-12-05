package com.idreameducation.ipreppal.pal.activity;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.PalReportsSubjectsAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsCountRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportsActivity extends Fragment implements View.OnClickListener {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private Context context;
    private Global global;
    private Button buttonDiagonostic;

    private TextView textDiagonostic;
    private TextView textNormalTest;
    private TextView textPractice;
    private TextView textVideos;
    private TextView textBimonthly;
    private TextView textViewDays,book_report,ncert_book_report,projectvideo_text,simulation_text;
    private TextView textViewTotalTime;
    private TextView tab_title;
    private String sClass;
    private String board;
    private LinearLayout linearReports;
    private TextView timeSpent;
    private TextView daysSpent;
    private TextView relative_category;
    private TextView dikshavideo_text;
    private RecyclerView mRecyclerView;
    private ReportsTimeSpentRepository reportsTimeSpentRepository;
    private ReportsCountRepository reportsCountRepository;
    private Disposable timeTaskDisposable;
    private Disposable countTaskDisposable;

    String booksReportsText,projectVideosText,simulation_textText,DikshaVideo_text,book_report_text;
    public String bi_monthly_text, categories_text, days_spent_text, diagnostic_text, month_text,
            practice_attempted_text, tab_month_text, tab_week_text, tab_year_text, tests_completed_text,
            time_spent_text, video_watched_text, week_text, year_text,no_reports_to_show,your_usage_reports,start_learning;

    public int count = 0;
    public ArrayList<HashMap<String, Object>> countArrayList = new ArrayList<>();
    public long diagonostic_test;
    public long simple_test;
    public long video;
    public long practice;
    public long bi_monthly_test;

    private final ArrayList<Object> timesavedArrayList = new ArrayList<>();
    private final ArrayList<String> subjectsArrayList = new ArrayList<>();
    public HashMap<String, Object> datamap = new HashMap<>();

    public ArrayList<HashMap<String, String>> subjectArrayList_ = new ArrayList<>();
    public ArrayList<HashMap<String, String>> categoryArrayList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> categoryArrayList_ = new ArrayList<>();
    private long _days = 0;
    private long _hours = 0;
    private long _mins = 0;
    private long _sec = 0;
    int s = 0 ;
    public static int max_progress=0;
    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    ProgressBar mProgressBar;
    private int back_days = 0;
    ArrayList<String> dateList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_activity_reports, container, false);
        Util.setContext(context);
        assignIds(view);
        listners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(Util.isDevelopmentSettingsEnabled(context)) Util.showDeveloperOptionPopup(getActivity());
        Util.setContext(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {}
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

                        hideconnection_layout();
                    }
                }
            }, 10000);//time in milisecond
        }
        else
        {
            hideconnection_layout();
        }
    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
    }

    private void listners() {

    }

    public ReportsActivity(int days, Context context) {
        back_days = days;
        this.context = context;
    }

    public ReportsActivity() {
        if(getApplicationContext()==null) return;
        this.context = getApplicationContext();
    }

    private void assignIds(View view) {
//        context = getActivity().getApplicationContext();
        try {
            global = (Global) context.getApplicationContext();
        } catch (Exception e) {
            onBackPressed();
        }

        linearReports = view.findViewById(R.id.linearReports);
        mRecyclerView = view.findViewById(R.id.recyclerView);
//        crecyclerView = view.findViewById(R.id.crecyclerView);
//        subjectWiseText = view.findViewById(R.id.subjectWiseText);
        mRecyclerView.setHasFixedSize(true);
//        crecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager manager2 = new GridLayoutManager(context, 2);

        if(Util.isPortraitMode(context)) mRecyclerView.setLayoutManager(manager);
        else mRecyclerView.setLayoutManager(manager2);

//        if(Util.isPortraitMode(context)) crecyclerView.setLayoutManager(manager2);
//        else crecyclerView.setLayoutManager(manager2);

        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
        reportsCountRepository = new ReportsCountRepository(context);

        mProgressBar=view.findViewById(R.id.progressBar);
        slow_internet_Text=view.findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=view.findViewById(R.id.imageViewCrossVideo2);

        timeSpent = view.findViewById(R.id.timeSpent);
        daysSpent = view.findViewById(R.id.daysSpent);


        try {
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        relative_category = view.findViewById(R.id.relative_category);
//        try {
//            relative_category.setText(((PalReportsActivity)context).categories_text);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        textDiagonostic = view.findViewById(R.id.textDiagonostic);
        tab_title = view.findViewById(R.id.tab_title);
        textViewTotalTime2 = view.findViewById(R.id.textViewTotalTime2);
        textViewDays2 = view.findViewById(R.id.textViewDays2);
        timeSpent2 = view.findViewById(R.id.timeSpent2);
        daysSpent2 = view.findViewById(R.id.daysSpent2);
        textNormalTest = view.findViewById(R.id.textNormalTest);
        textPractice = view.findViewById(R.id.textPractice);
        textVideos = view.findViewById(R.id.textVideos);
        book_report = view.findViewById(R.id.book_report);
        ncert_book_report = view.findViewById(R.id.ncert_book_report);
        projectvideo_text = view.findViewById(R.id.projectvideo_text);
        simulation_text = view.findViewById(R.id.simulation_text);
        textBimonthly = view.findViewById(R.id.textBimonthly);
        textViewDays = view.findViewById(R.id.textViewDays);
        textViewTotalTime = view.findViewById(R.id.textViewTotalTime);
        dikshavideo_text = view.findViewById(R.id.dikshavideo_text);

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            booksReportsText ="पुस्तकालय";
            projectVideosText="गतिविधि वीडियो";
            DikshaVideo_text="दीक्षा वीडियो";
            book_report_text="विषय पुस्तकें";
//            subjectWiseText.setText("सीखने में बिताया गया समय");
            relative_category.setText("सीखने में बिताये समय का सार");
            timeSpent2.setText("वर्चुअल लैब पर बिताया समय");
            daysSpent2.setText("वर्चुअल लैब के सैशन");
            simulation_textText="सिमुलेशन परियोजनाएँ";

        }
        else {
            booksReportsText ="Book Library";
            projectVideosText="Activity Videos";
            DikshaVideo_text="Diksha videos";
            book_report_text="Subject Books";
            simulation_textText="Simulation Projects";
//            subjectWiseText.setText("Time spent overview");
            relative_category.setText("Category wise usage");
        }


        book_report.setText(booksReportsText);
        projectvideo_text.setText(projectVideosText);
        simulation_text.setText(simulation_textText);
        dikshavideo_text.setText(DikshaVideo_text);
        ncert_book_report.setText(book_report_text);

        if(Util.isPortraitMode(context)) {
            dikshavideo_text.setVisibility(View.VISIBLE);
            simulation_text.setVisibility(View.VISIBLE);
        }
        else {
            dikshavideo_text.setVisibility(View.VISIBLE);
            simulation_text.setVisibility(View.INVISIBLE);
        }
        simulation_text.setVisibility(View.VISIBLE);
        book_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Util.isOfflineMode(context))
                {
                    startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "books_stories").
                            putExtra("section", book_report.getText().toString()));
                }
                else
                {
                    Intent intent=new Intent(context,Book_ReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "books_stories");
                    bundle.putString("name", book_report.getText().toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        ncert_book_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isOfflineMode(context))
                {
                    startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "books_ncert").
                            putExtra("section", ncert_book_report.getText().toString()));
                }
                else {
                    Intent intent = new Intent(context, Book_ReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "books_ncert");
                    bundle.putString("name", ncert_book_report.getText().toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        projectvideo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "project_video").
                        putExtra("section", projectVideosText));
            }
        });


        simulation_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "simulation_content").
                        putExtra("section", simulation_textText));
            }
        });

        dikshavideo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "diksha_content").
                        putExtra("section", DikshaVideo_text));

            }
        });

        board = Util.getSelectedBoard(context);
        sClass = Util.getSelectedClass(context);
        switch (back_days) {
            case 7:
                tab_title.setText(((PalReportsActivity)context).week_text);
                break;
            case 30:
                tab_title.setText(((PalReportsActivity)context).month_text);
                break;
            case 365:
                tab_title.setText(((PalReportsActivity)context).year_text);
                break;
        }

        try {
            getUniqueDateUsage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String userID = Util.getUserId(context);
//            settingAdapter();
//            getSubjects(userID , back_days);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            getVirtualLibraryTimestamp();
//            getVirtualLibraryTimestamp2();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    private void setStaticText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("My_Reports_Screen");
                bi_monthly_text = object_.get("bi_monthly_text").toString();
                categories_text = object_.get("categories_text").toString();
                days_spent_text = object_.get("days_spent_text").toString();
                diagnostic_text = object_.get("diagnostic_text").toString();
                no_reports_to_show = object_.get("no_reports_show").toString();
                your_usage_reports = object_.get("your_usage_reports_show").toString();
                start_learning = object_.get("start_learning").toString();
                month_text = object_.get("month_text").toString();
                practice_attempted_text = object_.get("practice_attempted_text").toString();
                tab_month_text = object_.get("tab_month_text").toString();
                tab_week_text = object_.get("tab_week_text").toString();
                tab_year_text = object_.get("tab_year_text").toString();
                tests_completed_text = object_.get("tests_completed_text").toString();
                time_spent_text = object_.get("time_spent_text").toString();
                video_watched_text = object_.get("video_watched_text").toString();
                week_text = object_.get("week_text").toString();
                year_text = object_.get("year_text").toString();
                daysSpent.setText(days_spent_text);
                timeSpent.setText(time_spent_text);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("My_Reports_Screen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                            bi_monthly_text = dataHashMap.get("bi_monthly_text");
                            categories_text = dataHashMap.get("categories_text");
                            days_spent_text = dataHashMap.get("days_spent_text");
                            diagnostic_text = dataHashMap.get("diagnostic_text");
                            no_reports_to_show = dataHashMap.get("no_reports_show");

                            your_usage_reports = dataHashMap.get("your_usage_reports_show");

                            start_learning = dataHashMap.get("start_learning");

                            month_text = dataHashMap.get("month_text");
                            practice_attempted_text = dataHashMap.get("practice_attempted_text");
                            tab_month_text = dataHashMap.get("tab_month_text");
                            tab_week_text = dataHashMap.get("tab_week_text");
                            tab_year_text = dataHashMap.get("tab_year_text");
                            tests_completed_text = dataHashMap.get("tests_completed_text");
                            time_spent_text = dataHashMap.get("time_spent_text");
                            video_watched_text = dataHashMap.get("video_watched_text");
                            week_text = dataHashMap.get("week_text");
                            year_text = dataHashMap.get("year_text");
                            daysSpent.setText(days_spent_text);
                            timeSpent.setText(time_spent_text);

                            textDiagonostic.setText(((PalReportsActivity)context).diagnostic_text);
                            textNormalTest.setText(((PalReportsActivity)context).tests_completed_text);
                            textPractice.setText(((PalReportsActivity)context).practice_attempted_text);
                            textVideos.setText(((PalReportsActivity)context).video_watched_text);
                            textBimonthly.setText(((PalReportsActivity)context).bi_monthly_text);
                            textBimonthly.setVisibility(View.GONE);
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

    public ArrayList<String> getCalculatedDate(int days, ArrayList<HashMap<String, String>> subjectArrayList, String userID) {
        ArrayList<String> dateArrayList = new ArrayList<>();

        for (int i = days; i < 1; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yy");
            cal.add(Calendar.DAY_OF_YEAR, i);
            String date = s.format(new Date(cal.getTimeInMillis()));
            String[] splittedDate = date.split("-");
            String month;
            switch (splittedDate[1]) {
                case "01":
                    month = "Jan";
                    break;
                case "02":
                    month = "Feb";
                    break;
                case "03":
                    month = "Mar";
                    break;
                case "04":
                    month = "Apr";
                    break;
                case "05":
                    month = "May";
                    break;
                case "06":
                    month = "Jun";
                    break;
                case "07":
                    month = "Jul";
                    break;
                case "08":
                    month = "Aug";
                    break;
                case "09":
                    month = "Sept";
                    break;
                case "10":
                    month = "Oct";
                    break;
                case "11":
                    month = "Nov";
                    break;
                case "12":
                    month = "Dec";
                    break;
                default:
                    month = "";
                    break;

            }
            String date_ = splittedDate[0] + " " + month + " " + splittedDate[2];
//            dateArrayList.add(days+"");
            dateArrayList.add(date_);
//            dateArrayList.add("22 Aug 22");
//            dateArrayList.add("21 Aug 22");

        }
        try {
            getWeeklyReports(dateArrayList, subjectArrayList, userID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateArrayList;
    }

    private void getTimeSpent(ArrayList<String> dateArrayList, ArrayList<HashMap<String, String>> subjectArrayList , String userID) throws Exception {
        String date = dateArrayList.get(count);
        if(Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, date, "timeTask", dateArrayList, subjectArrayList, userID, timesavedArrayList, datamap);
        }else{
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(userID).child(board).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    try {
                        if (snapshot.getValue() != null) {

                            HashMap<String, Object> classes_map = (HashMap<String, Object>) snapshot.getValue();
                            for (String key1 : classes_map.keySet()) {
                                HashMap<String,Object> classs = (HashMap<String, Object>) classes_map.get(key1);
                                HashMap<String,Object> timestemp = (HashMap<String, Object>) classs.get("time_spent");

                                for(String da : timestemp.keySet()) {
                                    HashMap<String,Object> timeHashMap = (HashMap<String, Object>) timestemp.get(da);

//                                        HashMap<String, Object> timeHashMap = (HashMap<String, Object>) snapshot.getValue();
                                    for (String key : timeHashMap.keySet()) {
                                        System.out.println("----- timeHashMap.get(key) "+timeHashMap.get(key));
                                        int e=Integer.parseInt(timeHashMap.get(key).toString());
                                        long time = (long) e;
                                        if (!subjectsArrayList.contains(key)) {
                                            timesavedArrayList.add(time);
                                            datamap.put(key, time);
                                            subjectsArrayList.add(key);
                                        } else {
                                            if(datamap.size() > 0 && datamap.containsKey(key)){
                                                long tim = (long) datamap.get(key);
                                                long t = tim + time;
                                                datamap.put(key, t);
                                            }
                                        }
                                    }

                                }

                                setTimeSpent(dateArrayList, subjectArrayList, userID);

                            }

                        } else {
                            count++;
                            setTimeSpent(dateArrayList, subjectArrayList, userID);
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

    private void setTimeSpent(ArrayList<String> dateArrayList, ArrayList<HashMap<String, String>> subjectArrayList , String userID) throws Exception {
        int ss = 0;
        subjectArrayList_.clear();
        categoryArrayList_.clear();
        max_progress=0;


        for (String key : datamap.keySet()) {
            long seconds = ((long) datamap.get(key) / 1000);
            ss= (int) (ss+seconds);

            int sec = 0 ;
            int min = 0 ;
            int hours = 0 ;
            int day = 0 ;

            sec = (int) seconds % 60;
            min = (int) (seconds / 60) % 60;
            hours = (int) (seconds / (60 * 60)) % 24;
            day = (int) (seconds / (60 * 60 * 24)) % 7;

            _days = day + _days;
            _hours = hours + _hours;
            _mins = min + _mins;
            _sec = sec + _sec;

            String subject = key;

            /** calculate subject usage */
            for (int i = 0; i < subjectArrayList.size(); i++) {
                String subject_ = subjectArrayList.get(i).get("name").toLowerCase().replace(" ","_");

                if (subject.equalsIgnoreCase(subject_)) {

                    if (min == 0) subjectArrayList.get(i).put("time", sec + " s");
                    else subjectArrayList.get(i).put("time",hours+" h "+ min + " m " + sec + " s");

                    subjectArrayList.get(i).put("progress",seconds+"");
                    max_progress=max_progress+Integer.parseInt(String.valueOf(seconds));
                    subjectArrayList_.add(subjectArrayList.get(i));
                }
            }

            /** calculate category usage */
            for (int i = 0; i < categoryArrayList.size(); i++) {
                String subject_ = categoryArrayList.get(i).get("id").toLowerCase().replace(" ","_");
                String subject__ = categoryArrayList.get(i).get("id");

                if (subject.equalsIgnoreCase(subject_) ||  subject.equalsIgnoreCase(subject__)) {

                    if (min == 0) categoryArrayList.get(i).put("time", sec + " s");
                    else categoryArrayList.get(i).put("time",hours+" h "+ min + " m " + sec + " s");

                    categoryArrayList.get(i).put("progress",seconds+"");
                    max_progress=max_progress+Integer.parseInt(String.valueOf(seconds));
                    categoryArrayList_.add(categoryArrayList.get(i));
                }
            }




//            System.out.println("-------- categoryArrayList_ "+categoryArrayList_);
//
//            PalReportsSubjectsAdapter palSubjectsAdapter2 = new PalReportsSubjectsAdapter(context, categoryArrayList_);
//            crecyclerView.setAdapter(palSubjectsAdapter2);

        }

        for(int i=0;i<=categoryArrayList_.size()-1;i++) subjectArrayList_.add(categoryArrayList_.get(i));


        PalReportsSubjectsAdapter palSubjectsAdapter = new PalReportsSubjectsAdapter(context, subjectArrayList_);
        mRecyclerView.setAdapter(palSubjectsAdapter);

//        crecyclerView.setVisibility(View.GONE);

        int _sec = ss % 60;
        int _mins = (ss / 60) % 60;
        int _hours = (ss / (60 * 60)) % 24;
        int _days = (ss / (60 * 60 * 24)) % 7;

        textViewTotalTime.setText(_hours + " h " + _mins + " m "+ _sec + " s");
    }

    private void getUniqueDateUsage() {

        if(Util.isOfflineMode(context)) {
            textViewDays.setText(""+dateList.size());
            try {
                getSubjects(Util.getUserId(context) , 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(board).child(sClass).child("time_spent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    dateArrayList=new ArrayList<>();
                    if (snapshot.getValue() != null) {
                        HashMap<String, Object> timeHashMap = (HashMap<String, Object>) snapshot.getValue();
                        try {
                            for(String date:timeHashMap.keySet()) dateArrayList.add(date);
                            getSubjects(Util.getUserId(context) , timeHashMap.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        textViewDays.setText(""+timeHashMap.size());
                    } else {
                        textViewDays.setText("0");
                        getSubjects(Util.getUserId(context) , 0);
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
    ArrayList<String> dateArrayList;
    public void getSubjects(String userID , int days) throws Exception {
        if (Util.isOfflineMode(context)) {

            hideconnection_layout();
            String filePath = ".iDream_content/offlinetab_PAL/subjects.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);

            try {
                JSONObject object = jsonObject.getJSONObject(board);
                JSONObject object_ = object.getJSONObject(Util.getSelectedLanguagePackage(context));
                JSONArray array = object_.getJSONArray(sClass);
                ArrayList<HashMap<String, String>> subjectArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, String> subjectHashMap = new HashMap<>();
                    JSONObject innerObject = array.getJSONObject(i);
                    String icon = innerObject.getString("icon");
                    String name = innerObject.getString("name");
                    String id = innerObject.getString("id");
                    subjectHashMap.put("icon", icon);
                    subjectHashMap.put("name", name);
                    subjectHashMap.put("id", id);
                    subjectArrayList.add(subjectHashMap);

                }

                HashMap<String,String> Books=new HashMap<>();
                Books.put("color","#FBD364");
                Books.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/icons%2FBooks.png?alt=media&token=f3214417-7e8b-41d5-88ca-64c9d92e0867");
                Books.put("id","Books");
                Books.put("name","Books");
                Books.put("short_name","Books");
                if(!categoryArrayList.contains(Books)) categoryArrayList.add(Books);

                HashMap<String,String> m=new HashMap<>();
                m.put("color","#A2A3DF");
                m.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/icons%2FProjects.png?alt=media&token=68487c73-67c7-4fc3-b962-016da270900b");
                m.put("id","Project Videos");
                m.put("name","Activity videos");
                m.put("short_name","Activity videos");
                if(!categoryArrayList.contains(m)) categoryArrayList.add(m);

                HashMap<String,String> s=new HashMap<>();
                s.put("color","#A2A3DF");
                s.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/Boardsicon%2Fscience.png?alt=media&token=46d6460a-7428-49fa-8ef4-09d96ec908b5");
                s.put("id","Simulation_project");
                s.put("name","Simulation");
                s.put("short_name","Simulation");
                subjectArrayList.add(s);
                if(!categoryArrayList.contains(s)) categoryArrayList.add(s);

                getCalculatedDate(-days, subjectArrayList, userID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isloading=true;
            checkConnection(false);
            global.getDatabaseReference().child(ApplicationConstants.SUBJECTS).child(board).child(Util.getSelectedLanguagePackage(context)).child(sClass).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            ArrayList<HashMap<String, String>> subjectArrayList = (ArrayList<HashMap<String, String>>) snapshot.getValue();
                            HashMap<String,String> Books=new HashMap<>();
                            Books.put("color","#FBD364");
                            Books.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/icons%2FBooks.png?alt=media&token=f3214417-7e8b-41d5-88ca-64c9d92e0867");
                            Books.put("id","Books");
                            Books.put("name","Books");
                            if(Util.getSelectedLanguage(context).equals("english")) Books.put("name","Books"); else Books.put("name","पुस्तकें");
                            Books.put("short_name","Books");
                            subjectArrayList.add(Books);

                            HashMap<String,String> m=new HashMap<>();
                            m.put("color","#A2A3DF");
                            m.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/icons%2FProjects.png?alt=media&token=68487c73-67c7-4fc3-b962-016da270900b");
                            m.put("id","Project Videos");
                            if(Util.getSelectedLanguage(context).equals("english")) m.put("name","Activity videos"); else m.put("name","Activity videos");
                            m.put("short_name","Activity videos");
                            subjectArrayList.add(m);

                            HashMap<String,String> s=new HashMap<>();
                            s.put("color","#A2A3DF");
                            s.put("icon","https://firebasestorage.googleapis.com/v0/b/iprep-7f10a.appspot.com/o/Boardsicon%2Fscience.png?alt=media&token=46d6460a-7428-49fa-8ef4-09d96ec908b5");
                            s.put("id","Simulation_project");
                            s.put("name","Simulation");
                            if(Util.getSelectedLanguage(context).equals("english")) s.put("name","Simulation"); else s.put("name","सिमुलेशन");
                            s.put("short_name","Simulation");
                            subjectArrayList.add(s);

//                            getCalculatedDate(-days, subjectArrayList, userID);

                            getWeeklyReports(dateArrayList, subjectArrayList, userID);
                            hideconnection_layout();
                        } else {
                            Util.showToast(context, "Subjects not available");
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
        }
    }

    private void getWeeklyReports(ArrayList<String> dateArrayList, ArrayList<HashMap<String, String>> subjectArrayList, String userID) throws Exception {
        if (count == dateArrayList.size()) {
            if (countArrayList.size() > 0) {
                ((PalReportsActivity) context).showReportsLayout();
                ((PalReportsActivity) context).hideDialog();
                for (int i = 0; i < countArrayList.size(); i++) {
                    long diagonostic = 0;
                    try {
                        diagonostic = (long) countArrayList.get(i).get("diagonostic_test");
                    } catch (Exception e) {
                    }
                    long simple = 0;
                    try {
                        simple = (long) countArrayList.get(i).get("simple_test");
                    } catch (Exception e) {
                    }

                    long video_count = 0;
                    try {
                        video_count = (long) countArrayList.get(i).get("video_lessons");
                    } catch (Exception e) {
                    }

                    long practice_count = 0;
                    try {
                        practice_count = (long) countArrayList.get(i).get("practice");
                    } catch (Exception e) {

                    }


                    long bi_monthly = 0;
                    try {
                        bi_monthly = (long) countArrayList.get(i).get("bi_monthly_test");
                    } catch (Exception e) {

                    }
                    diagonostic_test = diagonostic_test + diagonostic;
                    simple_test = simple_test + simple;
                    video = video + video_count;
                    practice = practice + practice_count;

                    bi_monthly_test = bi_monthly_test + bi_monthly;

                    textDiagonostic.setText(((PalReportsActivity)context).diagnostic_text);
                    textNormalTest.setText(((PalReportsActivity)context).tests_completed_text);
                    textPractice.setText(((PalReportsActivity)context).practice_attempted_text);
                    textVideos.setText(((PalReportsActivity)context).video_watched_text);
                    textBimonthly.setText(((PalReportsActivity)context).bi_monthly_text);
                    textBimonthly.setVisibility(View.GONE);

                    textVideos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "video_lessons").
                                    putExtra("section", ((PalReportsActivity)context).video_watched_text));
                        }
                    });
                    textPractice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "practice").
                                    putExtra("section", ((PalReportsActivity)context).practice_attempted_text));
                        }
                    });
                    textNormalTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "simple_test").
                                    putExtra("section", ((PalReportsActivity)context).tests_completed_text));
                        }
                    });
                    textDiagonostic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "diagonostic_test").
                                    putExtra("section", ((PalReportsActivity)context).diagnostic_text));
                        }
                    });
//                    textBimonthly.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "bi_monthly_test").
//                                    putExtra("section", "Bi Monthly Test"));
//                        }
//                    });
                }
                count = 0;
                getTimeSpent(dateArrayList, subjectArrayList , userID);
            } else {
                ((PalReportsActivity) context).hideReportsLayout();
                ((PalReportsActivity) context).hideDialog();
            }
        } else {
            if(count <= dateArrayList.size()) {
                String date = dateArrayList.get(count);
                if(Util.isOfflineMode(context)) {
                    runBackgroundTask(Util.getUserId(context), board, sClass, date, "countTask", dateArrayList, subjectArrayList, userID, null, null);
                }else{
                    global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(userID).child(board).child(sClass).child("count").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    HashMap<String, Object> countHashMap = (HashMap<String, Object>) snapshot.getValue();
                                    countArrayList.add(countHashMap);
                                    count++;
                                    getWeeklyReports(dateArrayList, subjectArrayList, userID);
                                } else {
                                    count++;
                                    getWeeklyReports(dateArrayList, subjectArrayList, userID);
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

    }

    public void onBackPressed() {
//        ((PalReportsActivity)context).onBackPressed();

        ((ReportListActivity)context).finish();

    }

    private void runBackgroundTask(String userId, String board, String sClass, String date, String type, ArrayList<String> dateArrayList, ArrayList<HashMap<String, String>> subjectArrayList, String userID, ArrayList<Object> timesavedArrayList, HashMap<String, Object> datamap){
        if(type.equals("timeTask")){
            getList(userId, board, sClass, date, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            timeTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsTimeSpentModel> list = (ArrayList<ReportsTimeSpentModel>) o;
                                if(list != null && list.size() > 0){
                                    HashMap<String, Object> timeHashMap = new HashMap<>();
                                    for(ReportsTimeSpentModel item: list){
                                        timeHashMap.put(item.getSubject(), item.getTime());
                                    }
                                    for (String key : timeHashMap.keySet()) {
                                        long time = (long) timeHashMap.get(key);
                                        if (!subjectsArrayList.contains(key)) {
                                            timesavedArrayList.add(time);
                                            datamap.put(key, time);
                                            subjectsArrayList.add(key);
                                        } else {
                                            if(datamap.size() > 0 && datamap.containsKey(key)){
                                                long tim = (long) datamap.get(key);
                                                long t = tim + time;
                                                datamap.put(key, t);
                                            }
                                        }
                                    }
                                    count++;
                                    setTimeSpent(dateArrayList, subjectArrayList, userID);

                                }else{
                                    count++;
                                    setTimeSpent(dateArrayList, subjectArrayList, userID);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            timeTaskDisposable.dispose();
                        }
                    });
        }else if(type.equals("countTask")){
            getList(userId, board, sClass, date, type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            countTaskDisposable = d;
                        }

                        @Override
                        public void onNext(Object o) {
                            try {
                                ArrayList<ReportsCountModel> list = (ArrayList<ReportsCountModel>) o;
                                if(list != null && list.size() > 0){
                                    HashMap<String, Object> countHashMap = new HashMap<>();
                                    for(ReportsCountModel item: list){
                                        countHashMap.put(item.getType(), item.getCount());
                                        if(!dateList.contains(item.getDate()))dateList.add(item.getDate());
                                    }
                                    countArrayList.add(countHashMap);
                                    count++;
                                    getUniqueDateUsage();
                                    getWeeklyReports(dateArrayList, subjectArrayList, userID);
                                }else{
                                    count++;
                                    getWeeklyReports(dateArrayList, subjectArrayList, userID);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            countTaskDisposable.dispose();
                        }
                    });
        }
    }

    private Observable<Object> getList(String userId, String board, String sClass, String date, String type) {
        if (type.equals("timeTask")) {
            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, null,Util.getSelectedLanguage(context));
            });
        } else if (type.equals("countTask")) {

            return Observable.fromCallable(() -> {
                //do something, get your Data object
                return reportsCountRepository.getDetail(userId, board, sClass, date, null);
            });
        }
        return null;
    }

    String URL="https://api.scholarlab.in/Analytics/GetUsersTotalActiveTime?userName=";
    String URL2="https://api.scholarlab.in/Analytics/GetUsersLoginInfos?userName=";

    TextView textViewTotalTime2,textViewDays2;
    TextView timeSpent2,daysSpent2;
    private void getVirtualLibraryTimestamp() throws JSONException {

        //admin@SCHT2T.com

        textViewTotalTime2.setText("0");
        StringRequest request = new StringRequest(Request.Method.GET, URL+Util.getUserId(context), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String  minS = jsonObject.getString("AvgSessionDuration");

                    String split =minS.replace(".00","");

                    int ss = Integer.parseInt(split);

                    int _sec = ss % 60;
                    int _mins = (ss / 60) % 60;
                    int _hours = (ss / (60 * 60)) % 24;
                    int _days = (ss / (60 * 60 * 24)) % 7;

                    textViewTotalTime2.setText(""+_hours + " h " + _mins + " m "+ _sec + " s");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                    Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                    Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));

//                String dd= String.valueOf(error.networkResponse.data);
                    String dd= "" + new String(error.networkResponse.data);

                    System.out.println("----" +dd);


                    JSONObject jsonObject1=new JSONObject(dd);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }) {

        };
        RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(context);
            request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        } catch (NullPointerException o) {
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
            } catch (Exception e) {}
        }


    }

    private void getVirtualLibraryTimestamp2() throws JSONException {
        textViewDays2.setText("0");
        StringRequest request = new StringRequest(Request.Method.GET, URL2+Util.getUserId(context), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    array.length();

                    textViewDays2.setText(""+array.length());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                try {
                    Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                    Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                    Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));

//                String dd= String.valueOf(error.networkResponse.data);
                    String dd= "" + new String(error.networkResponse.data);

                    System.out.println("----" +dd);

                    JSONObject jsonObject1=new JSONObject(dd);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }) {

        };
        RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(context);
            request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        } catch (NullPointerException o) {
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
            } catch (Exception e) {}
        }
    }

}
