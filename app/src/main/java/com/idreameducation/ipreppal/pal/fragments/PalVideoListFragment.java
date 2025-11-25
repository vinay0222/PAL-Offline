package com.idreameducation.ipreppal.pal.fragments;

import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.currentTopicid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.LinearLayoutManagerWithSmoothScroller;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter;
import com.idreameducation.ipreppal.pal.adapter.VideoListAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataVideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsLatestDataVideoRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PalVideoListFragment extends Fragment {
    private Context context;
    private Global global;
    private static RecyclerView recyclerView;
    private TextView topicName;
    private String board;
    private String sClass;
    private String subject;
    private String icon;
    private String language;
    private String categoryID;
    private String topicID;
    public static CardView messageLayout1;
    public static LinearLayout messageLayout11;
    public static CardView messageLayout2;
    public static LinearLayout messageLayout22;
    public static boolean messageLayoutidvisible =false;
    private TextView textViewText;
    private TextView textViewText2;
    private Disposable disposable;
    private ReportsLatestDataVideoRepository reportsLatestDataVideoRepository;

    public static String ttype="practice";
    public static FoundationalTopicModel foundationalTopicModel;
    public static int pposition;
    public static boolean refreshMessage=false;

    public static VideoListAdapter videoListAdapter;
    public static HashMap<String, Object> dataMap;

    private String foundational_topics,diagnostic_test,final_test;

    public static String current_videoid="";
    TextView currentTopic;
    ImageView textViewContinue;

    public static PalVideoListFragment palVideoListFragment;


    public PalVideoListFragment() {}

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_video, container, false);
        context = getActivity();
        palVideoListFragment=this;
        global = (Global) getActivity().getApplicationContext();

        reportsLatestDataVideoRepository = new ReportsLatestDataVideoRepository(context);

        topicName = view.findViewById(R.id.textViewTopicName);
        topicName.setTextColor(Color.parseColor(global.getColor()));
        topicName.setText(Util.getTopicNameAlt(context));
        messageLayout1 = view.findViewById(R.id.messageLayout1);
        messageLayout11 = view.findViewById(R.id.messageLayout11);
        textViewContinue = view.findViewById(R.id.textViewContinue);

        if(!Util.isPortraitMode(context)) textViewText2 = view.findViewById(R.id.textViewText2);
        if(!Util.isPortraitMode(context)) messageLayout2 = view.findViewById(R.id.messageLayout2);
        if(!Util.isPortraitMode(context)) messageLayout22 = view.findViewById(R.id.messageLayout22);

        messageLayout11.setAlpha(1f);
        textViewText = view.findViewById(R.id.textViewText);
        currentTopic = view.findViewById(R.id.currentTopic);
        textViewText.setTextColor(Color.parseColor(global.getColor()));
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(Util.isPortraitMode(context))
        {
            board = PalContentListingActivity_Mobile.board;
            sClass = PalContentListingActivity_Mobile.sClass;
            subject = PalContentListingActivity_Mobile.subject;
            language = Util.getSelectedLanguage(context);
            icon = PalContentListingActivity_Mobile.icon;
            topicID = PracticeTopicAdapter.activeFoundationTopic;
            if(board==null) board = Util.getSelectedBoard(context);

        }
        else{
            board = PalContentListingActivity.board;
            sClass = PalContentListingActivity.sClass;
            subject = PalContentListingActivity.subject;
            language = ((PalContentListingActivity) context).language;
            icon = PalContentListingActivity.icon;
            topicID = ((PalContentListingActivity) context).topic_id;
        }



        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            diagnostic_test = Util.getTopicNameAlt(context)+ " का डायग्नोस्टिक परीक्षण शुरू करें";
            foundational_topics = "मूलभूत अध्याय का अभ्यास करें";
            final_test = "अंतिम परीक्षा";

        }
        else {
            diagnostic_test = "Start Diagnostic test of " +Util.getTopicNameAlt(context);
            foundational_topics = "To learn, watch videos below or Start Foundational Practice";
            final_test = "Take the final test and Earn you badge";
        }

        if(!Util.isSubjectboadingDone(context)) {
            if(!Util.isPortraitMode(context)) messageLayout1.setVisibility(View.GONE);
            if(!Util.isPortraitMode(context)) messageLayout2.setVisibility(View.VISIBLE);

            if(!Util.isPortraitMode(context)) if(Util.getSelectedLanguage(context).equals("hindi")) textViewText2.setText("शुरू करने के लिए किसी भी अध्याय का चयन करें");
            else textViewText2.setText("Select a Chapter and Start Learning");


            try {
                if(((PalContentListingActivity)context).completeType.equals("foundationalPractice")) {
                    if(Util.getSelectedLanguage(context).equals("hindi")) currentTopic.setText("फाउंडेशन विषय के वीडियो");
                    else currentTopic.setText("Foundation Topic Videos");

                    currentTopic.setVisibility(View.VISIBLE);
                }
                else {
                    currentTopic.setVisibility(View.GONE);
                }
            }catch (Exception r) {
                r.printStackTrace();
            }

        }
        else {
            if(!Util.isPortraitMode(context)) messageLayout2.setVisibility(View.GONE);
            messageLayout1.setVisibility(View.VISIBLE);
        }

        if(Util.isOfflineMode(context)) getVideos();
        else getVideoReports();

        if(refreshMessage && messageLayout1.getVisibility()==View.GONE) showTestLayout(PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagetype,PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagemodel,PalContentListingActivity_Mobile.palContentListingActivityMobile.Messageposition);


        try {
            /** message layout 1 */
            messageLayout1.getBackground().setTint(Color.parseColor(global.getColor()));
            messageLayout11.getBackground().setTint(Color.parseColor(Util.setColourVisibility(global.getColor(),20)));
            textViewContinue.getBackground().setTint(Color.parseColor(global.getColor()));
            /** message layout 2 */
            messageLayout2.getBackground().setTint(Color.parseColor(global.getColor()));
            messageLayout22.getBackground().setTint(Color.parseColor(Util.setColourVisibility(global.getColor(),20)));
        }catch (Exception er){
            er.printStackTrace();
        }
        return view;
    }

    public void hideLayout(String topicId, boolean hide, int position) {
        if(recyclerView != null) if(topicID != null && topicId.equals(topicID) && !hide) recyclerView.setAlpha(1f);
            else recyclerView.setAlpha(.5f);
    }

    public void showTestLayout(String type, FoundationalTopicModel model, int position) {

        if(Util.isPortraitMode(context)) {
            if(PalContentListingActivity_Mobile.palContentListingActivityMobile.isFinalTestAttempted(Util.getTopicID(context))) {
                type="all";ttype="all";foundationalTopicModel=null;pposition=0;
            }
            else if(PalContentListingActivity_Mobile.palContentListingActivityMobile.isPracticeCompleted(Util.getTopicID(context))) {
                type="final";ttype="final";foundationalTopicModel=null;pposition=0;
            }
            else {
                if(type!=null) {
                    ttype=type;foundationalTopicModel=model;pposition=position;
                    try {
                        PalContentListingActivity_Mobile.palContentListingActivityMobile.getVideos(foundationalTopicModel.getTopicId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        else {
            ttype = type;
            foundationalTopicModel = model;
            pposition = position;
            if(Util.isSubjectboadingDone(context)) messageLayout1.setVisibility(View.VISIBLE);
            else { messageLayout1.setVisibility(View.GONE); return;}
        }

        if(type.equals("diagnostic")){
            messageLayout1.setVisibility(View.VISIBLE);
            messageLayoutidvisible =true;
            textViewText.setText(diagnostic_test);
            currentTopic.setVisibility(View.GONE);
        }else if(type.equals("final")){
            messageLayout1.setVisibility(View.VISIBLE);
            messageLayoutidvisible =true;
            textViewText.setText(final_test);
            currentTopic.setVisibility(View.GONE);
        }else if(type.equals("practice")){
            messageLayout1.setVisibility(View.VISIBLE);
            messageLayoutidvisible =true;
            String name = Util.getTopicNameAlt(context);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString redSpannable= new SpannableString(name);
            redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor(global.getColor())), 0, name.length(), 0);
            builder.append(redSpannable);
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) textViewText.setText(Html.fromHtml(name +" का अभ्यास जारी रखें "));
            else textViewText.setText(Html.fromHtml("To learn, watch videos below or to do Practice the "+ name));

            currentTopic.setText("");
            currentTopic.setVisibility(View.GONE);
        }else if(type.equals("foundationalPractice")){
            messageLayout1.setVisibility(View.VISIBLE);
            messageLayoutidvisible =true;
            textViewText.setText(foundational_topics);

            if(Util.getSelectedLanguage(context).equals("hindi")) currentTopic.setText("फाउंडेशन विषय के वीडियो");
            else currentTopic.setText("Foundation Topic Videos");
            
            currentTopic.setVisibility(View.VISIBLE);
        }else if(type.equals("all")){
            messageLayout1.setVisibility(View.GONE);
            messageLayoutidvisible =false;
            currentTopic.setVisibility(View.GONE);
        }
        
        String finalType = type;
        messageLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                if(finalType.equals("diagnostic")){
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", sClass);
                    context.startActivity(intent);

                }
                else if(finalType.equals("final")){
                    if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                        if(Util.isPortraitMode(context)) startActivity(new Intent(context, com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity.class).putExtra("sClass", sClass));
                        else startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

                    }
                    else if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                        else Util.openGifDialogue(context,"Internet Connection is not working");
                }
                else if(finalType.equals("practice")){
                    if(Util.isPortraitMode(context)) PalContentListingActivity_Mobile.palContentListingActivityMobile.startPractice(topicID);
                    else ((PalContentListingActivity)context).startPractice();
                }
                else if(finalType.equals("foundationalPractice")){
                    int mastery = 0 ;
                    int level = 1 ;
                    String streekProgress = "0" ;
                    if(Util.isPortraitMode(context)) {
                        PalContentListingActivity_Mobile.palContentListingActivityMobile.firsttime=true;
                        for(int o=PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.size()-1;o>=0;o--) {

                            PracticeScoreModel data = PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.get(o);
                            if (data.getTopicId().equals(model.getTopicId())) {
                                mastery=  Integer.parseInt(PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.get(o).getScore());
                                level=  Integer.parseInt(PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                streekProgress= PalContentListingActivity_Mobile.palContentListingActivityMobile.practiceScoreModelArrayList.get(o).getStreakProgress();
                                break;
                            }
                        }
                        if(Util.isPortraitMode(context)) PalContentListingActivity_Mobile.palContentListingActivityMobile.showFoundationalTopicDialog(((PalTopicListingActivity)context),Util.getUserId(context), subject, model.getSeniorTopicID(), model.getTopicId(), model.getTopicName(), model.getSeniorClass(), 0, position,mastery,streekProgress,level);
                        else PalContentListingActivity_Mobile.palContentListingActivityMobile.showFoundationalTopicDialog(Util.getUserId(context), subject, model.getSeniorTopicID(), model.getTopicId(), model.getTopicName(), model.getSeniorClass(), 0, position,mastery,streekProgress,level);
                    }
                    else {
                        ((PalContentListingActivity)context).firsttime=true;
                        for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                            PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                            if (data.getTopicId().equals(model.getTopicId())) {
                                mastery=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getScore());
                                level=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                streekProgress= PalContentListingActivity.practiceScoreModelArrayList.get(o).getStreakProgress();
                                break;
                            }
                        }
                        ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), subject, model.getSeniorTopicID(), model.getTopicId(), model.getTopicName(), model.getSeniorClass(), 0, position,mastery,streekProgress,level);
                    }
                }
            }
        });

        messageLayout1.requestFocus();
        currentTopic.setTextColor(Color.parseColor(global.getColor()));


//        messageLayout1.setVisibility(View.GONE);
    }

    public PalVideoListFragment(String contentType) {
        this.categoryID = contentType;
    }

    public void getVideos() {

        if(Util.isPortraitMode(context)) {
            getVideosMobile();
            return;
        }

        ArrayList<HashMap<String, Object>> contentArrayList;

        if (Util.isOfflineMode(context)) {
            contentArrayList=((PalContentListingActivity) context).contentArrayList_;
        } else {
            contentArrayList=((PalContentListingActivity) context).contentArrayList_;
        }
        videoListAdapter = new VideoListAdapter(context, contentArrayList, sClass, subject, language, board, icon, PalVideoListFragment.this, dataMap, categoryID);
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context,getResources().getIdentifier("layout_animation_from_bottom","anim",getPackageName()));
//        recyclerView.setLayoutAnimation(animation);
//        videoListAdapter.dataMap = dataMap;
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));
        videoListAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(videoListAdapter);
        ((PalContentListingActivity)context).hideDialog();


    }

    public void getVideosMobile() {

        try {
            try {
                PalContentListingActivity_Mobile.palContentListingActivityMobile.getFoundationTopicDetails(topicID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            videoListAdapter = new VideoListAdapter(context, PalContentListingActivity_Mobile.palContentListingActivityMobile.contentArrayList_, sClass, subject, language, board, icon, PalVideoListFragment.this, dataMap, categoryID);
            videoListAdapter.notifyDataSetChanged();


            if(refreshMessage && messageLayout1.getVisibility()==View.GONE) showTestLayout(PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagetype,PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagemodel,PalContentListingActivity_Mobile.palContentListingActivityMobile.Messageposition);


            recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));
            recyclerView.scheduleLayoutAnimation();
            recyclerView.setAdapter(videoListAdapter);

            if (Util.getVideoLevel(context) == 0) {
                recyclerView.smoothScrollToPosition(0);
            } else {
                recyclerView.smoothScrollToPosition(Util.getVideoLevel(context));
            }
            ((PalTopicListingActivity)context).hide_connectionlayout();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(PalVideoListFragment.refreshMessage) {
                        try {
                            PalVideoListFragment.palVideoListFragment.showTestLayout(PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagetype, PalContentListingActivity_Mobile.palContentListingActivityMobile.Messagemodel, PalContentListingActivity_Mobile.palContentListingActivityMobile.Messageposition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            },2000);

        }catch (Exception d){
            d.printStackTrace();
        }

//        getVideoReports();
    }

    public static void scrollToCurrentLevel(Context context) {
        try {
            videoListAdapter.notifyDataSetChanged();
            if (Util.getUnlockVideoLevel(context,currentTopicid) == 0) recyclerView.smoothScrollToPosition(0);
            else recyclerView.scrollToPosition(Util.getVideoLevel(context));
//            else recyclerView.scrollToPosition(1);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getVideoReports() {
        if(!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)){
            runBackgroundTask(Util.getUserId(context), board, sClass, Util.getSubject(context), "video_lessons", topicID);
        }else{
            if(topicID==null) return;

//            if(Util.isPortraitMode(context)) sClass =  PalContentListingActivity_Mobile.palContentListingActivityMobile.clss;
            if(sClass==null) return;

            try {
                global.getDatabaseReference()
                        .child(ApplicationConstants.REPORTS)
                        .child("latest_data")
                        .child(Util.getUserId(context))
                        .child(board)
                        .child(sClass)
                        .child(Util.getSubjectName(context))
                        .child("video_lessons")
                        .child(topicID)
                        .child("detail")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {

                                    if (snapshot.getValue() != null) {
                                        dataMap = (HashMap<String, Object>) snapshot.getValue();
                                        System.out.println("------ datamap updated "+dataMap);
                                    } else {
                                        dataMap = null;
                                    }
    //                                videoListAdapter.dataMap = dataMap;
                                    getVideos();

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

    private void runBackgroundTask(String userId, String board, String sClass, String subject, String type, String topicId){
        getList(userId, board, sClass, subject, type, topicId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {
                        ArrayList<ReportsLatestDataVideoModel> list = (ArrayList<ReportsLatestDataVideoModel>) o;
                        HashMap<String, Object> data = new HashMap<>();
                        HashMap<String, String> dataItem = new HashMap<>();
                        for(ReportsLatestDataVideoModel item: list){
                            dataItem.put("time", item.getVTime());
                            dataItem.put("topicName", item.getTopicName());
                            dataItem.put("totalTime", item.getTotalTime());
                            dataItem.put("videoName", item.getVideoName());
                            if(item.getUrl() != null){
                                data.put(item.getUrl(), dataItem);
                            }else{
                                data.put(item.getVId(), dataItem);
                            }
                        }
                        dataMap = data;
//                        videoListAdapter.dataMap = dataMap;
//                        videoListAdapter.notifyDataSetChanged();
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
            return reportsLatestDataVideoRepository.getDetail(userId, board, sClass, subject, type, topicId);
        });
    }

    public void setMarginToRecyclerView(int margin){
        if(recyclerView != null){
            recyclerView.addItemDecoration(new RecyclerViewItemDecoration(margin));
        }
    }

    public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

        int margin;

        public RecyclerViewItemDecoration(int margin){
            this.margin = margin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // only for the last one
            if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = margin/* set your margin here */;
            }else{
                outRect.bottom = 0/* set your margin here */;
            }
        }
    }

}
