package com.idreameducation.ipreppal.pal.adapter;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalFullScreenVideoActivity;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.fragments.BatchesFragments;
import com.idreameducation.ipreppal.pal.fragments.PalAssignedFragment;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by sony on 4/4/2017.
 */

public class PalAssignedInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final Global global;
    private final Context context;
    private final HashMap<String, Object> hashMap;
    private final ArrayList<String> keyArrayList;
    String batchID,date;
    public List<FoundationalTopicModel> foundationalTopicData;
    FoundationalTopicModel foundationalTopicModel;
    private FoundationalTopicRepository foundationalTopicRepository;
    private Disposable foundationalTopicDisposable;
    Dialog dialog;
    String streek="3";
    String stopicID;
    String sstudentClass,sstreak,sincorrectStreak,sseniorClass,sseniorTopicID,sseniorTopicName,sstartDate;
    int sposition;

    ArrayList<Long> assignTime=new ArrayList<>();
    public PalAssignedInnerAdapter(Context context, HashMap<String, Object> hashMap, ArrayList<String> keyArrayList,String batchID,String date) {
        this.context = context;
        this.hashMap = hashMap;
        this.keyArrayList = keyArrayList;
        this.batchID = batchID;
        this.date = date;
        global = (Global) context.getApplicationContext();

        /** creating array to short list by time */
        for (String datee : keyArrayList)
        /** adding date in assignTime list */
            assignTime.add(Long.parseLong(datee));

        Collections.sort(assignTime);
        Collections.reverse(assignTime);

    }

    /** @param showingItem add 0 if you want to show all items  */
    public PalAssignedInnerAdapter(Context context, HashMap<String, Object> hashMap, ArrayList<String> keyArrayList,int showingItem) {
        this.context = context;
        this.hashMap = hashMap;
        this.keyArrayList = keyArrayList;
//        if(showingItem==0) this.showingItem = hashMap.size();
//        else if(hashMap.size()>=showingItem) this.showingItem = showingItem;
//        else this.showingItem = hashMap.size();
        global = (Global) context.getApplicationContext();

        /** creating array to short list by time */
        for (String date : keyArrayList)
        /** adding date in assignTime list */
            assignTime.add(Long.parseLong(date));

        Collections.sort(assignTime);
        Collections.reverse(assignTime);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_assigned_inner, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, @SuppressLint("RecyclerView") final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            HashMap<String, Object> data2 = (HashMap<String, Object>) hashMap.get(assignTime.get(position).toString());
            HashMap<String, Object> data;

            System.out.println("----- hashMap "+hashMap);
            System.out.println("----- data2 "+data2);

            try {

                if(data2.containsKey(assignTime.get(position).toString())) data = (HashMap<String, Object>) data2.get(assignTime.get(position).toString());
                else data=data2;

//                System.out.println("----- data2 "+data2);

                HashMap<String, Object> maindata = (HashMap<String, Object>) data.get("info");

//                System.out.println("---- maindata "+maindata);
//            System.out.println("---- data2 "+data2);
                String name = null;
                String title = (String) maindata.get("name");
                String topicID = (String) maindata.get("topicID");
                String topicName = (String) maindata.get("topicName");

                Long endDate = (Long) maindata.get("endDate");
                String startDate = (String) maindata.get("startDate");
                String subject = (String) maindata.get("subjectName");


                String assignmentName=null;
                String assignmentKey= null;
                try {
                    assignmentName= (String) maindata.get("assignmentName");
                    assignmentKey = (String) maindata.get("assignmentKey");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String subjectID = "null";

                try {

                    try {
                        subjectID = (String) maindata.get("subjectID");
                    } catch (Exception e) {
                        subjectID = (String) maindata.get("subjectName");
                    }

                    if(subjectID==null) subjectID=subject;

                    System.out.println("--- maindata.get() "+maindata.get("type"));
                    System.out.println("--- name "+maindata.get("name"));

                    String type = (String) maindata.get("type");
                    String videoLink = (String) maindata.get("videoLink");
                    String status = (String) maindata.get("status");
                    String teacherID = (String) maindata.get("teacherID");

                    String key = assignTime.get(position).toString();

                    if(date==null) date=startDate;

                    switch (type) {
                        case "practice":
                            name = BatchesFragments.textArrayList.get(14)+" - "+maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.practice_icon);
                            break;
                        case "biMonthlyTest":
                            name = (String) maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.practice_icon);
                            break;

                        case "book":
                            name = BatchesFragments.textArrayList.get(15)+" - "+maindata.get("topicName");
                            viewHolder.imageViewType.setImageResource(R.drawable.practice_icon);
                            break;
                        case "video":
                            name = BatchesFragments.textArrayList.get(16)+" - "+ maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.video_icon);
                            break;
                        case "diagnostic":
                            name = BatchesFragments.textArrayList.get(17)+" - "+ maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.test_icon);
                            break;
                        case "diagnosticTest":
                            name = (String) maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.test_icon);
                            break;
                        case "finalTest":
                            name = BatchesFragments.textArrayList.get(18)+" - "+maindata.get("name");
                            viewHolder.imageViewType.setImageResource(R.drawable.test_icon);
                            break;
                    }

                    viewHolder.textViewVideoTitle.setText(name);

                    SimpleDateFormat dayTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                    String endDa = dayTimeFormatter.format(new Date(endDate));

                    viewHolder.textViewInfo.setText(subjectID.replace("_"," ") + " | "+BatchesFragments.textArrayList.get(19)+": " + startDate + " | "+BatchesFragments.textArrayList.get(20)+": " + endDa);


                    if(status!=null) {
//                viewHolder.textViewInfo.setText(status+" "+subject + " | Start: " + startDate + " | Due: " + endDate);
                    }


                    String finalSubjectID = subjectID;
                    HashMap<String, Object> finalData = maindata;
                    String finalAssignmentName = assignmentName;
                    String finalAssignmentKey = assignmentKey;
                    String finalAssignmentName1 = assignmentName;
                    String finalAssignmentKey1 = assignmentKey;
                    String finalAssignmentName2 = assignmentName;
                    String finalAssignmentKey2 = assignmentKey;
                    String finalAssignmentName3 = assignmentName;
                    String finalAssignmentKey3 = assignmentKey;
                    viewHolder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Util.preventTwoClick(view);
                            final Date currentDATE =  new Date();
                            final long endtime = Long.parseLong(finalData.get("endDate").toString());
                            final Date endDATE =  new Date(Long.parseLong(finalData.get("endDate").toString()));

                            Util.setSubjectId(context, finalSubjectID);

//                            PalContentListingActivity.updateAgain=true;

                            if(type.equals("video")) {

                                String completeType="";

                                /** Check complete Type */
                                if ((!PalAssignedFragment.palAssignedFragment.isDiagnosticTestAttempted(topicID)))
                                    completeType = "diagnostic";


                                if(completeType.equals("diagnostic")) {
                                    Util.setTopicID(context,topicID);
                                    Util.setTopicNameAlt(context,topicName);
                                    Util.setSubject(context, finalData.get("subjectName").toString());
                                    Util.setSubjectName(context,finalSubjectID);
                                    showDialogue(context,"diagnosticVideo");
                                }
                                else {
                                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                                    intent.putExtra("vidId", videoLink);
                                    intent.putExtra("videoName", title);
                                    intent.putExtra("videoId", videoLink);
                                    intent.putExtra("assignedDate",date);
                                    intent.putExtra("assignedKey",key);
                                    intent.putExtra("assignmentName", finalAssignmentName);
                                    intent.putExtra("assignmentKey", finalAssignmentKey);
                                    intent.putExtra("teacherID", teacherID);
                                    intent.putExtra("onlineLink", videoLink);
                                    intent.putExtra("category_name", finalData.get("topicName").toString());
                                    intent.putExtra("subject", finalData.get("subjectName").toString());
                                    intent.putExtra("topicID", finalData.get("topicID").toString());
                                    intent.putExtra("videoID_ForReports", finalData.get("key").toString());
                                    Util.setTopicNameAlt(context, finalData.get("topicName").toString());
                                    context.startActivity(intent);
                                }

                            }
                            else if(type.equals("practice")) {

                                String completeType;

                                String studentClass = (String) finalData.get("studentClass");
                                String streakProgress = (String) finalData.get("streakProgress");
                                String streak = (String) finalData.get("streak");
                                String incorrectStreak = (String) finalData.get("incorrectStreak");
                                String seniorClass = (String) finalData.get("seniorClass");
                                String seniorTopicID = (String) finalData.get("seniorTopicID");
                                String seniorTopicName = (String) finalData.get("seniorTopicName");
                                Util.setTopicID(context,topicID);
                                Util.setTopicNameAlt(context,title);

                                Util.setSubject(context,subject.replace(" ","").toLowerCase());
                                Util.setSubjectName(context,finalSubjectID);

                                Util.setLevel(context,1);
                                String smastery="0",stopicID=topicID;

                                /** Check complete Type */
                                if ((PalAssignedFragment.palAssignedFragment.isDiagnosticTestAttempted(topicID))) {
                                    if (PalAssignedFragment.palAssignedFragment.isPracticeCompleted(topicID)) {
                                        if (!PalAssignedFragment.palAssignedFragment.isFinalTestAttempted(topicID)) completeType = "final";
                                        else completeType = "all";
                                    } else completeType = checkfoundation(topicID);
                                } else completeType = "diagnostic";


                                if(completeType.equals("diagnostic")) {
                                    showDialogue(context,"diagnostic");
                                }
                                else if(completeType.equals("foundationalPractice")) {
                                    Util.setTopicNameAlt(context,foundationalTopicModel.getTopicName());
                                    Util.setTopicID(context,foundationalTopicModel.getTopicId());
                                    streek=streak;
                                    showDialogue(context,"foundationalPractice");
                                }
                                else {

                                    PracticeScoreModel practiceScoreModel=PalAssignedFragment.palAssignedFragment.getPracticeScore(topicID);
                                    Util.setLevel(context, 1);
                                    if(practiceScoreModel!=null) {
                                        smastery = practiceScoreModel.getScore();
                                        stopicID = practiceScoreModel.getTopicId();
                                        streakProgress = practiceScoreModel.getStreakProgress();
                                        Util.setLevel(context, Integer.parseInt(practiceScoreModel.getCurrentLevel()));
                                    }

                                    Util.setTopicID(context, stopicID);
                                    global.setProgress(Integer.parseInt(smastery));

                                    Intent intent=new Intent(context, QuizActivity.class);;

                                    if(Util.isPortraitMode(context)) intent=new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class);

                                    intent.putExtra("assignedDate",date)
                                            .putExtra("assignedKey",key)
                                            .putExtra("assignmentName", finalAssignmentName1)
                                            .putExtra("assignmentKey", finalAssignmentKey1)
                                            .putExtra("teacherID", teacherID)
                                            .putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress)
                                            .putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak)
                                            .putExtra("practiceType", "same").putExtra("seniorClass", seniorClass)
                                            .putExtra("seniorTopicID", seniorTopicID).putExtra("seniorTopicName", seniorTopicName)
                                            .putExtra("testPercentageAchieved", "0");

                                    context.startActivity(intent);
                                    global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID).child(startDate).child(keyArrayList.get(position)).child("status").setValue("completed");

                                }


                            }
                            else if(type.equals("finalTest")) {

                                String studentClass = (String) finalData.get("studentClass");
                                Util.setTopicID(context,topicID);
                                Util.setTopicNameAlt(context,title);
                                Util.setLevel(context,1);

                                String topicName = Util.getTopicNameAlt(context);
                                Util.setTopicID(context, topicID);
                                Util.setTopicNameAlt(context, topicName);
                                Util.setSubject(context,subject.replace(" ","").toLowerCase());
                                Util.setSubjectName(context,finalSubjectID);
                                String completeType;

//                        String studentClass = (String) maindata.get("studentClass");
                                String streakProgress = "0";
                                String streak = "3";
                                String incorrectStreak = "3";
                                String seniorClass = (String) finalData.get("seniorClass");
                                String seniorTopicID = (String) finalData.get("seniorTopicID");
                                String seniorTopicName = (String) finalData.get("seniorTopicName");
                                Util.setTopicID(context,topicID);
                                Util.setTopicNameAlt(context,title);
                                Util.setLevel(context,1);

                                if ((PalAssignedFragment.palAssignedFragment.isDiagnosticTestAttempted(topicID))) {
                                    if (PalAssignedFragment.palAssignedFragment.isPracticeCompleted(topicID)) {
                                        if (!PalAssignedFragment.palAssignedFragment.isFinalTestAttempted(topicID)) {
                                            completeType = "final";
                                        } else {
                                            completeType = "all";
                                        }
                                    } else {
                                        completeType = checkfoundation(topicID);
                                    }
                                } else {
                                    completeType = "diagnostic";
                                }

                                if(completeType.equals("diagnostic")) {
                                    showDialogue(context,"diagnostic");
                                }
                                else if(completeType.equals("foundationalPractice")) {

                                    Util.setTopicNameAlt(context,foundationalTopicModel.getTopicName());
                                    Util.setTopicID(context,foundationalTopicModel.getTopicId());
                                    streek=streak;
                                    showDialogue(context,"foundationalPractice");

                                }
                                else if(completeType.equals("practice")){

                                    stopicID=topicID;
                                    sstudentClass=studentClass;
                                    sstreak=streak;
                                    sincorrectStreak=incorrectStreak;
                                    sseniorClass=seniorClass;
                                    sseniorTopicID=seniorTopicID;
                                    sseniorTopicName=seniorTopicName;
                                    sstartDate=startDate;
                                    sposition=position;
                                    showDialogue(context,"practice");
                                }
                                else {
                                    if (Util.isNetworkAvailable(context))
                                    {
                                        if ((PalAssignedFragment.palAssignedFragment.isDiagnosticTestAttempted(topicID))) {

                                            Intent intent=new Intent(context, NormalTestActivity.class);

                                            if(Util.isPortraitMode(context)) intent=new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity.class);

                                            intent.putExtra("assignedDate",date)
                                                    .putExtra("assignedKey",key)
                                                    .putExtra("assignmentName", finalAssignmentName2)
                                                    .putExtra("assignmentKey", finalAssignmentKey2)
                                                    .putExtra("teacherID", teacherID)
                                                    .putExtra("sClass", studentClass);

                                            context.startActivity(intent);
                                            global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID).child(startDate).child(keyArrayList.get(position)).child("status").setValue("completed");

                                        }
                                        else Util.openGifDialogue(context, "Complete diagnostic first");

                                    } else {
                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                        {
                                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                        } else {
                                            Util.openGifDialogue(context,"Internet Connection is not working");
                                        }
                                    }
                                }


                            }
                            else if(type.equals("diagnostic")) {

                                String studentClass = (String) finalData.get("studentClass");
                                Util.setTopicID(context,topicID);
                                Util.setTopicNameAlt(context,topicName);
                                Util.setSubjectName(context,finalSubjectID);
                                Util.setSubject(context,subject);
                                Util.setSubjectName(context, finalData.get("subjectName").toString());

                                /** Check complete Type */
                                if ((!PalAssignedFragment.palAssignedFragment.isDiagnosticTestAttempted(topicID))) {
                                    Util.setSubject(context,subject);

                                    Intent intent = new Intent(context, DStartActivity.class);
                                    intent.putExtra("sClass", Util.getSelectedClass(context));
                                    intent.putExtra("assignedDate",date);
                                    intent.putExtra("assignedKey",key);
                                    intent.putExtra("assignmentName", finalAssignmentName);
                                    intent.putExtra("assignmentKey", finalAssignmentKey);
                                    intent.putExtra("teacherID", teacherID);
                                    context.startActivity(intent);
                                }
                                else {
                                    TestScoreModel testScoreModel=PalAssignedFragment.palAssignedFragment.getDiagnosticTestScore(topicID);

                                    if(testScoreModel==null) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    batchID=global.getBatchID();

                                    HashMap<String,String> report=new HashMap<>();
                                    report.put("mastery",testScoreModel.getScore());
                                    report.put("time",testScoreModel.getTime());
                                    report.put("userName",Util.getUsername(context));

                                    global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                                            .child(date).child(key).child("report").setValue(report);

                                    /** add reports in assigned content info in batch node */

                                    if(finalAssignmentKey ==null) global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(key)
                                            .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);

                                    else global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(finalAssignmentKey).child(finalAssignmentName).child(key)
                                            .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);


                                    showDialogue(context,"Completeddiagnostic");

                                }
                            }
                            else if(type.equals("book")) {

                                String studentClass = (String) finalData.get("studentClass");
                                Util.setTopicID(context,topicID);
                                Util.setTopicNameAlt(context,title);
                                Util.setSubjectName(context,finalSubjectID);
                                /** Check complete Type */
                                if (true) {
                                    Util.setSubject(context,subject);
                                    Util.setSubjectName(context, finalData.get("subjectName").toString());
                                    Intent intent = new Intent(context, PalPdfViewerActivity.class);

                                    intent.putExtra("sClass", Util.getSelectedClass(context));
                                    intent.putExtra("assignedDate",date);
                                    intent.putExtra("assignedKey",key);
                                    intent.putExtra("assignmentName", finalAssignmentName3);
                                    intent.putExtra("assignmentKey", finalAssignmentKey3);
                                    intent.putExtra("teacherID", teacherID);

                                    intent.putExtra("onlineLink", (String) finalData.get("onlineLink"));
                                    intent.putExtra("topic",(String) finalData.get("topicName"));
                                    intent.putExtra("offlineLink",(String) finalData.get("offlineLink"));
                                    intent.putExtra("topicName", (String) finalData.get("topicName"));
                                    intent.putExtra("name", (String) finalData.get("name"));
                                    intent.putExtra("subjectName", (String) finalData.get("subjectName"));
                                    intent.putExtra("bookId", (String) finalData.get("id"));
                                    intent.putExtra("categoryID", "books_ncert");
                                    intent.putExtra("topicId", ((String) finalData.get("subjectName")).replace(" ","_").toLowerCase());
                                    intent.putExtra("topic_name_main",(String)  finalData.get("topicName"));

                                    context.startActivity(intent);

                                }
                                else {
                                    TestScoreModel testScoreModel=PalAssignedFragment.palAssignedFragment.getDiagnosticTestScore(topicID);

                                    if(testScoreModel==null){
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    batchID=global.getBatchID();

                                    HashMap<String,String> report=new HashMap<>();
                                    report.put("mastery",testScoreModel.getScore());
                                    report.put("time",testScoreModel.getTime());

                                    global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                                            .child(date).child(key).child("report").setValue(report);

                                    showDialogue(context,"Completeddiagnostic");


                                }


                            }

//                    if(currentDATE.before(endDATE)){
//
//                    }else {
//                        Toast.makeText(global, "Time out", Toast.LENGTH_SHORT).show();
//                    }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {

                try {

//                    System.out.println("--- data2 "+data2);
//
//                    String assignContentName="";
//                    String assignContentName2="";
//                    String assignContentName3="";
////
//                    ArrayList<String> f=new ArrayList<>();
//
//                    for(String name:data2.keySet()) f.add(name);
//
//                    HashMap<String, Object> data1= (HashMap<String, Object>) data2.get(f.get(position));
//
//                    for(String name:data1.keySet()) assignContentName2=name;
//
//                    HashMap<String, Object> data3= (HashMap<String, Object>) data1.get(assignContentName2);
//
//                    for(String name:data3.keySet()) assignContentName3=name;
//
//                    HashMap<String, Object> data44= (HashMap<String, Object>) data3.get(assignContentName3);
//                    HashMap<String, Object> data4= (HashMap<String, Object>) data44.get("info");
//
//
//                    System.out.println("---- data1 "+data1);
//                    System.out.println("---- data3 "+data3);
//                    System.out.println("---- data44 "+data44);
//                    System.out.println("---- data4 "+data4);
//
//                    viewHolder.textViewVideoTitle.setText(assignContentName2);
//
//                    String subject = (String) data4.get("subjectName");
//                    String endDate = (String) data4.get("endDate").toString();
//                    String subjectName=subject.replace("_"," ");
//
//                    SimpleDateFormat dayTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//                    endDate = dayTimeFormatter.format(new Date(Long.parseLong(endDate)));
//
//                    if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textViewInfo.setText(Util.capitalStringFirstLetter(subjectName) + " | देय-दिनांक " + endDate);
//                    else viewHolder.textViewInfo.setText(Util.capitalStringFirstLetter(subjectName) + " | due " + endDate);
//
//                    ArrayList<String> keyArrayList = new ArrayList<>();
//                    for (String key : data3.keySet()) keyArrayList.add(key);


                    data = (HashMap<String, Object>) data2.get(assignTime.get(position).toString());

                    String assignContentName="";
                    String assignContentName2="";

                    for(String name:data.keySet()) assignContentName=name;

                    HashMap<String, Object> data1= (HashMap<String, Object>) data.get(assignContentName);

                    for(String name:data1.keySet()) assignContentName2=name;

                    HashMap<String, Object> data3= (HashMap<String, Object>) data1.get(assignContentName2);
                    HashMap<String, Object> data4= (HashMap<String, Object>) data3.get("info");

                    viewHolder.textViewVideoTitle.setText(assignContentName);

                    String subject = (String) data4.get("subjectName");
                    String subjectID = (String) data4.get("subjectID");
                    String endDate = (String) data4.get("endDate").toString();
                    String subjectName=subject.replace("_"," ");

                    SimpleDateFormat dayTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                    endDate = dayTimeFormatter.format(new Date(Long.parseLong(endDate)));

                    if(subjectID==null) subjectID=subject;

                    if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textViewInfo.setText(Util.capitalStringFirstLetter(subjectID) + " | देय-दिनांक " + endDate);
                    else viewHolder.textViewInfo.setText(Util.capitalStringFirstLetter(subjectID).replace("_"," ") + " | due " + endDate);

                    ArrayList<String> keyArrayList = new ArrayList<>();
                    for (String key : data1.keySet()) keyArrayList.add(key);


                    viewHolder.imageViewForward.setImageResource(R.drawable.downword_arrow);

                    viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.imageViewForward.setImageResource(R.drawable.downword_arrow);
                            if(viewHolder.contentRecyclerview.getVisibility()==View.VISIBLE) viewHolder.contentRecyclerview.setVisibility(GONE);
                            else {
                                viewHolder.imageViewForward.setImageResource(R.drawable.upword_arrow);
                                viewHolder.contentRecyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                                viewHolder.contentRecyclerview.setAdapter(new PalAssignedInnerAdapter(context, data1, keyArrayList, global.getBatchID(),date));
                                viewHolder.contentRecyclerview.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                catch (Exception r){
                    r.printStackTrace();
                }

            }

        }
    }

    public String checkfoundation(String lastTopicId) {

        String completeType="practice";
        List<FoundationalTopicModel> foundationalTopicData = PalAssignedFragment.palAssignedFragment.foundationalTopicData;

        if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
            for (FoundationalTopicModel item : foundationalTopicData) {
                if (lastTopicId.equals(item.getSeniorTopicID())) {
                    if (!PalAssignedFragment.palAssignedFragment.isPracticeCompleted(item.getTopicId())) {
                        int position = 0;

                        foundationalTopicModel=item;
                        completeType = "foundationalPractice";


//                        return completeType;

//                                                    break;
                    }
                }
            }
        }

        return completeType;
    }

    private void showDialogue(Context context,String type) {

        if(dialog!=null) if(dialog.isShowing()) dialog.dismiss();

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView messageText = dialog.findViewById(R.id.messageText);
        Button button = dialog.findViewById(R.id.btn);


        if(type.equals("diagnostic")) {

            messageText.setText("First, take the diagnostic test of this chapter.");
            button.setText(BatchesFragments.textArrayList.get(13));


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", Util.getSelectedClass(context));
                    context.startActivity(intent);

                }
            });

        }
        if(type.equals("diagnosticVideo")) {

            messageText.setText(BatchesFragments.textArrayList.get(12));
            button.setText(BatchesFragments.textArrayList.get(13));


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", Util.getSelectedClass(context));
                    context.startActivity(intent);

                }
            });

        }
        else if(type.equals("foundationalPractice")) {

            messageText.setText("To start the practice, you have to first complete the foundation practice. ");
            button.setText("Start Foundation practice");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    PracticeScoreModel practiceScoreModel=PalAssignedFragment.palAssignedFragment.getPracticeScore(foundationalTopicModel.getTopicId());
                    String mastery="0";
                    String topicId=foundationalTopicModel.getTopicId();
                    Util.setLevel(context, 1);
                    String streakProgress1="0";

                    if(practiceScoreModel!=null)
                    {
                        mastery = practiceScoreModel.getScore();
                        topicId = practiceScoreModel.getTopicId();
                        Util.setLevel(context, Integer.parseInt(practiceScoreModel.getCurrentLevel()));
                        streakProgress1 = practiceScoreModel.getStreakProgress();
                    }

                    Util.setTopicID(context, topicId);
                    global.setProgress(Integer.parseInt(mastery));

                    Intent intent=new Intent(context, QuizActivity.class);;

                    if(Util.isPortraitMode(context)) intent=new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class);

                    intent.putExtra("sClass", foundationalTopicModel.getSClass()).putExtra("streakProgress", streakProgress1).putExtra("streak", streek).putExtra("incorrectStreak", foundationalTopicModel.getIncorrectStreak()).putExtra("practiceType", foundationalTopicModel.getPracticeType()).putExtra("seniorClass", foundationalTopicModel.getSeniorClass()).putExtra("seniorTopicID", foundationalTopicModel.getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicModel.getSeniorTopicName()).putExtra("testPercentageAchieved", "0");

                    context.startActivity(intent);


                }
            });

        }
        else if(type.equals("practice")) {

            messageText.setText("To start the final test, you have to first complete the practice.");
            button.setText("Start Practice");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    PracticeScoreModel practiceScoreModel=PalAssignedFragment.palAssignedFragment.getPracticeScore(stopicID);
                    String mastery="0",topicId=stopicID,level="1";
                    String streakProgress = "0";

                    if(practiceScoreModel!=null) {
                        mastery = practiceScoreModel.getScore();
                        topicId = practiceScoreModel.getTopicId();
                        level = practiceScoreModel.getCurrentLevel();
                        streakProgress = practiceScoreModel.getStreakProgress();
                    }

                    Util.setTopicID(context, topicId);
                    Util.setLevel(context, Integer.parseInt(level));

                    global.setProgress(Integer.parseInt(mastery));

                    Intent intent=new Intent(context, QuizActivity.class);;

                    if(Util.isPortraitMode(context)) intent=new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.QuizActivity.class);

                    intent.putExtra("sClass", sstudentClass).putExtra("streakProgress", streakProgress).putExtra("streak", sstreak).putExtra("incorrectStreak", sincorrectStreak).putExtra("practiceType", "same").putExtra("seniorClass", sseniorClass).putExtra("seniorTopicID", sseniorTopicID).putExtra("seniorTopicName", sseniorTopicName).putExtra("testPercentageAchieved", "0");

                    context.startActivity(intent);
                    global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID).child(sstartDate).child(keyArrayList.get(sposition)).child("status").setValue("completed");


                }
            });

        }
        else if(type.equals("Completeddiagnostic")) {

            messageText.setText("You already attempted Diagnostic test of this topic.\nYour last score is sent to assigner");
            button.setText("Okay");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialog.show();
        /* Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void getFoundationTopicDetails(String topicID) {

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
                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {

                                try
                                {
                                    for(int f=foundationalTopicData.size()-1;f>=0;f--) {

                                        FoundationalTopicModel item = foundationalTopicData.get(f);
                                        if (item.getSeniorTopicID().equals(topicID)) {
                                            for (int p = PalContentListingActivity.practiceScoreModelArrayList.size() - 1; p >= 0; p--) {
                                                PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(p);
                                                if (data.getTopicId().equals(item.getTopicId())) {
                                                    if (!data.getScore().equals("100")) {

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    for (FoundationalTopicModel item : foundationalTopicData) {
                                        if (topicID.equals(item.getSeniorTopicID())) {
                                            if (!PalAssignedFragment.palAssignedFragment.isPracticeCompleted(item.getTopicId())) {
                                                int position = 0;

//                                                completeType = "foundationalPractice";
//                                                static_completeType = "foundationalPractice";
//                                                for (int i = 0; i < topicsArrayList.size(); i++) {
//                                                    if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
//                                                        position = i;
//                                                        break;
//                                                    }
//                                                }

//                                                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
//                                                for (Fragment fragment : allFragments) {
//                                                    if (fragment instanceof PalVideoListFragment) {
//                                                        ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
//                                                    } else if (fragment instanceof PalDikshaContentFragment) {
//                                                        ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
//                                                    }
//                                                }
//                                                    break;
                                            }
                                        }
                                    }
                                }catch (Exception ee){
                                    System.out.println("++++++ handled");
                                }

                            }


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

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return hashMap.size();

    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private String getDate(String dateString) {
        String manipulatedString = "";

        switch (dateString) {
            case "01":
                manipulatedString = "Jan";
                break;
            case "02":
                manipulatedString = "Feb";
                break;
            case "03":
                manipulatedString = "Mar";
                break;
            case "04":
                manipulatedString = "Apr";
                break;
            case "05":
                manipulatedString = "May";
                break;
            case "06":
                manipulatedString = "Jun";
                break;
            case "07":
                manipulatedString = "Jul";
                break;
            case "08":
                manipulatedString = "Aug";
                break;
            case "09":
                manipulatedString = "Sept";
                break;
            case "10":
                manipulatedString = "Oct";
                break;
            case "11":
                manipulatedString = "Nov";
                break;
            case "12":
                manipulatedString = "Dec";
                break;

        }
        return manipulatedString;
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoTitle;
        protected TextView textViewInfo;
        protected TextView textViewSmallDate;
        protected ImageView imageViewType;
        protected ImageView imageViewForward;
        protected LinearLayout mainLayout;
        protected LinearLayout card;

        protected RecyclerView contentRecyclerview;

        public ViewItem(View holderView) {
            super(holderView);
            // holderView.setOnClickListener(this);
            textViewVideoTitle = holderView.findViewById(R.id.textViewVideoTitle);
            textViewInfo = holderView.findViewById(R.id.textViewInfo);
            imageViewType = holderView.findViewById(R.id.imageViewType);
            textViewSmallDate = holderView.findViewById(R.id.textViewSmallDate);
            imageViewForward = holderView.findViewById(R.id.imageViewForward);
            contentRecyclerview = holderView.findViewById(R.id.contentRecyclerview);
            mainLayout = holderView.findViewById(R.id.mainLayout);
            card = holderView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }


}