package com.idreameducation.ipreppal.pal.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.LinearLayoutManagerWithSmoothScroller;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.adapter.VideoListAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PalDikshaContentFragment extends Fragment {

    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private RelativeLayout videoLayout;
    private TextView topicName;
    private String board;
    private String sClass;
    private String subject;
    private String icon;
    private String language;
    private final String categoryID;
    private String topicID;
    public static LinearLayout messageLayout;
    public static boolean messageLayoutidvisible=false;
    private TextView textViewText;
    private ImageView textViewContinue;
    public static VideoListAdapter videoListAdapter;
    private String foundational_topics,diagnostic_test,final_test;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_video, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();

        videoLayout = view.findViewById(R.id.videoLayout);
        topicName = view.findViewById(R.id.textViewTopicName);
        topicName.setTextColor(Color.parseColor(global.getColor()));
        topicName.setText(Util.getTopicNameAlt(context));
        messageLayout = view.findViewById(R.id.messageLayout);
        messageLayout.setAlpha(1f);
        messageLayout.setVisibility(View.GONE);
        messageLayoutidvisible=false;
        textViewText = view.findViewById(R.id.textViewText);
        textViewContinue = view.findViewById(R.id.textViewContinue);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));

        board = PalContentListingActivity.board;
        sClass = PalContentListingActivity.sClass;
        subject = PalContentListingActivity.subject;
        language = ((PalContentListingActivity) context).language;
        icon = PalContentListingActivity.icon;
        topicID = ((PalContentListingActivity) context).topic_id;

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            diagnostic_test = Util.getTopicNameAlt(context)+ " का डायग्नोस्टिक परीक्षण शुरू करें";
            foundational_topics = "मूलभूत अभ्यास";
            final_test = "अंतिम परीक्षा";

        }else {
            diagnostic_test = "Start Diagnostic test of " +Util.getTopicNameAlt(context);
            foundational_topics = "Foundational Practice";
            final_test = "Final Test";
        }
        getVideoReports();

        return view;
    }


    public void showTestLayout(String type, FoundationalTopicModel model, int position){
        if(type.equals("diagnostic")){
            messageLayout.setVisibility(View.VISIBLE);
            messageLayoutidvisible=true;
            textViewText.setText(diagnostic_test);
        }else if(type.equals("final")){
            messageLayout.setVisibility(View.VISIBLE);
            messageLayoutidvisible=true;
            textViewText.setText(final_test);
        }else if(type.equals("practice")){
            messageLayout.setVisibility(View.VISIBLE);
            messageLayoutidvisible=true;
            String name = Util.getTopicNameAlt(context);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString redSpannable= new SpannableString(name);
            redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor(global.getColor())), 0, name.length(), 0);
            builder.append(redSpannable);
            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
            {
                textViewText.setText(Html.fromHtml(name +" का अभ्यास जारी रखें "));
            }else {
                textViewText.setText(Html.fromHtml("Keep practicing the "+ name));
            }

        }else if(type.equals("foundationalPractice")){
            messageLayout.setVisibility(View.VISIBLE);
            messageLayoutidvisible=true;
            textViewText.setText(foundational_topics);
        }else if(type.equals("all")){
            messageLayout.setVisibility(View.GONE);
            messageLayoutidvisible=false;
        }
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("diagnostic")){
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", sClass);
                    context.startActivity(intent);

                }else if(type.equals("final")){
                    if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context))
                    {
                    startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));

                    } else {
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                        {
                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                        }else {
                            Util.openGifDialogue(context,"Internet Connection is not working");
                        }
                    }
                }else if(type.equals("practice")){
                    ((PalContentListingActivity)context).startPractice();
                }else if(type.equals("foundationalPractice")){
                    ((PalContentListingActivity)context).firsttime=true;
                    int mastery = 0 ;
                    int level = 1 ;
                    String streekProgress = "0" ;
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
        });
    }

//    public void showTestLayout(String type, FoundationalTopicModel model, int position){
//        if(type.equals("diagnostic")){
//            messageLayout.setVisibility(View.VISIBLE);
//            textViewText.setText(Util.getTopicNameAlt(context)+ " का डायग्नोस्टिक परीक्षण शुरू करें");
//        }else if(type.equals("final")){
//            messageLayout.setVisibility(View.VISIBLE);
//            textViewText.setText("अंतिम परीक्षा");
//        }else if(type.equals("practice")){
//            messageLayout.setVisibility(View.VISIBLE);
//            String name = Util.getTopicNameAlt(context);
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            SpannableString redSpannable= new SpannableString(name);
//            redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor(global.getColor())), 0, name.length(), 0);
//            builder.append(redSpannable);
//            textViewText.setText(Html.fromHtml(name +" का अभ्यास जारी रखें "));
//
//        }else if(type.equals("foundationalPractice")){
//            messageLayout.setVisibility(View.VISIBLE);
//            textViewText.setText("मूलभूत अभ्यास");
//        }else if(type.equals("all")){
//            messageLayout.setVisibility(View.GONE);
//        }
//        messageLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(type.equals("diagnostic")){
//                    Intent intent = new Intent(context, DStartActivity.class);
//                    intent.putExtra("sClass", sClass);
//                    context.startActivity(intent);
//
//                }else if(type.equals("final")){
//                    startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));
//
//                }else if(type.equals("practice")){
//                    ((PalContentListingActivity)context).startPractice();
//                }else if(type.equals("foundationalPractice")){
//                    ((PalContentListingActivity)context).showFoundationalTopicDialog(model.getUserId(), subject, model.getSeniorTopicID(), model.getTopicId(), model.getTopicName(), model.getSeniorClass(), 0, position);
//                }
//            }
//        });
//    }

    public void hideLayout(String topicId, boolean hide, int position){
        if(recyclerView != null){
            if(topicID != null && topicId.equals(topicID) && !hide){
                recyclerView.setAlpha(1f);
            }else{
                recyclerView.setAlpha(.5f);
            }
        }
    }

    public PalDikshaContentFragment(String contentType) {
        this.categoryID = contentType;
    }

    public static HashMap<String, Object> dataMap;

    private void getVideoReports() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubjectName(context)).child("diksha_content").child(topicID).child("detail")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {

                            if (snapshot.getValue() != null) {
                                dataMap = (HashMap<String, Object>) snapshot.getValue();
                            } else {
                                dataMap = null;
                            }
//                            videoListAdapter.dataMap = dataMap;
                            getVideos();
//                            videoListAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        getVideos();
                    }
                });
    }

    private void getVideos() {
//        videoListAdapter = new VideoListAdapter(context, ((PalContentListingActivity) context).dikshaContentArrayList_, sClass, subject, language, board, icon, PalDikshaContentFragment.this, dataMap, categoryID);
//        recyclerView.setAdapter(videoListAdapter);
        ((PalContentListingActivity)context).hideDialog();


    }
}
