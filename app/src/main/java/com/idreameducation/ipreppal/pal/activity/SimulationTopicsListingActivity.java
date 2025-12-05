package com.idreameducation.ipreppal.pal.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.SelectableModel;
import com.idreameducation.ipreppal.model.simulationModel.SimulationSubjectModel;
import com.idreameducation.ipreppal.model.simulationModel.SimulationTopicsModel;
import com.idreameducation.ipreppal.pal.adapter.simulationAdapters.ChaptersAdapterSimulation;
import com.idreameducation.ipreppal.pal.adapter.simulationAdapters.TopicsAdapterSimulation;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class SimulationTopicsListingActivity extends AppCompatActivity {
    protected RecyclerView chapterRecyclerview,topicsRecyclerview;
    protected TextView subjectNameTextView;
    public String subjectColour,subjectName;

    private LinearLayout chapterLayout,topicLayout;
    protected int selectableTopicPosition=0; // current selected Item in topic recyclerview
    public static SimulationSubjectModel subjectModel;  // we can set Simulation and Audio Book data in this Mode
    public static final int CONTENT_TYPE_SIMULATION=1523;
    public static final int CONTENT_TYPE_AUDIOBOOK=2564;
    public static int contentType;

    /** before calling this activity must set value in subjectModel
     * subjectModel must be Simulation type or AudioBook */
    @SuppressLint("LongLogTag")
    public SimulationTopicsListingActivity() {
        if(subjectModel==null) {
            Log.e("SimulationTopicActivity","DataParsing Error!");
            finish();
        }

        if(contentType==0) {
            Log.e("SimulationTopicActivity","Content Type Error!");
            finish();
        }

        if(subjectModel.getColor()!=null)  subjectColour=subjectModel.getColor();
        else subjectColour=Util.getColorOffline(subjectModel.getSubjectName());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_simulation_topics_listing);
        Util.handleNotch(this);
        init();
    }

    /** init views from layout  */
    private void init() {
        /** init views */
        subjectNameTextView=findViewById(R.id.subjectNameTextView);
        chapterRecyclerview=findViewById(R.id.chapterRecyclerview);
        topicsRecyclerview=findViewById(R.id.topicsRecyclerview);

        chapterLayout=findViewById(R.id.chapterLayout);
        topicLayout=findViewById(R.id.topicLayout);

        chapterRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        if(Util.isPortraitMode(this)) topicsRecyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        else topicsRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        /** setSubjectName */
        subjectNameTextView.setText(Util.capitalStringFirstLetter(subjectModel.getSubjectName()));
        subjectNameTextView.setTextColor(Color.parseColor(subjectModel.getColor()));
        subjectName=subjectModel.getSubjectName();

        /** chapter adapter setup */
        setChapterListing();

        /** Topic adapter setup */
        if(!Util.isPortraitMode(this)) showTopicListing(selectableTopicPosition);
        else {
            chapterLayout.setVisibility(View.VISIBLE);
            topicLayout.setVisibility(View.GONE);
        }

        /** click events */
        findViewById(R.id.backBtn).setOnClickListener(view -> {
            Util.preventTwoClick(view);
            onBackPressed();
        });

    }

    private void setChapterListing() {
        ChaptersAdapterSimulation chaptersAdapterSimulation=new ChaptersAdapterSimulation(getChapters());
        chaptersAdapterSimulation.SetOnItemClickListener((view, position) -> showTopicListing(position));
        chapterRecyclerview.setAdapter(chaptersAdapterSimulation);
    }

    /** get Chapters name in SelectableModel format of current subject */
    private ArrayList<SelectableModel> getChapters() {
        ArrayList<SelectableModel> list=new ArrayList<>();

        /** set all chapters name in SelectableModel to show selectable topic */
        for(int i=0;i<=subjectModel.getChapters().size()-1;i++)
            list.add(new SelectableModel(subjectModel.getChapters().get(i).getName(),i==selectableTopicPosition));

        return list;
    }

    /** get Topics name in SimulationTopicsModel model of given position */
    private ArrayList<SimulationTopicsModel> getTopics(int pos) {
        return subjectModel.getChapters().get(pos).getTopics();
    }

    private void showTopicListing(int pos) {
        selectableTopicPosition=pos;
        /** setup Topic Listing */
        topicsRecyclerview.setAdapter(new TopicsAdapterSimulation(getTopics(pos)));
        setChapterListing();
        chapterRecyclerview.smoothScrollToPosition(selectableTopicPosition);

        if(Util.isPortraitMode(this)) {
            chapterLayout.setVisibility(View.GONE);
            topicLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        if(Util.isPortraitMode(this)) {
            if(topicLayout.getVisibility()==View.VISIBLE) {
                chapterLayout.setVisibility(View.VISIBLE);
                topicLayout.setVisibility(View.GONE);
            }
            else super.onBackPressed();
        }
        else super.onBackPressed();
    }
}