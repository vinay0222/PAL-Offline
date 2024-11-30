package com.idreameducation.ipreppal.PalMobile.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.Models.ProjectSubTopicModel;
import com.idreameducation.ipreppal.PalMobile.Models.ProjectTopicModel;
import com.idreameducation.ipreppal.PalMobile.adapter.ProjectToipic_Adapter;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class ProjectVideos_topic_Activity extends AppCompatActivity {

    private Context context;
    private Global global;
    public String board;
    public String sClass;
    public String language;
    public static String subject;
    public static String subjectName;
    public static String categoryNAme;
    public static String color;
    public static String icon;

    RecyclerView topic_recyclerview;
    ImageView sub_icon,back_image;
    TextView sub_name;

    public static ArrayList<ProjectSubTopicModel> selected_Topic_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_project_videos_topic);
        assignIds();
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();


        topic_recyclerview=findViewById(R.id.topic_recyclerview);
        sub_icon=findViewById(R.id.sub_icon);
        sub_name=findViewById(R.id.subj_name);
        back_image=findViewById(R.id.back_image);

        topic_recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        getDataFromBundle();
        getTopics();
    }

    private void getDataFromBundle() {
        board = getIntent().getStringExtra("board");
        sClass = getIntent().getStringExtra("sClass");
        subject = getIntent().getStringExtra("subject");
        subjectName = getIntent().getStringExtra("subjectName");
        categoryNAme = getIntent().getStringExtra("categoryName");
        color = getIntent().getStringExtra("color");
        icon = getIntent().getStringExtra("icon");
        language = Util.getSelectedLanguage(context);


        sub_name.setTextColor(Color.parseColor(color));
//        if(Util.getSelectedLanguage(context).equals("hindi")) sub_name.setText(subjectName+" परियोजनाएं");
//        else sub_name.setText(subjectName+" Project");
        sub_name.setText(subjectName);

        Glide.with(context).load(icon).into(sub_icon);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getTopics() {

        global.getDatabaseReference().child(ApplicationConstants.EXTRA_CONTENT)
                .child(Util.getSelectedBoard(context)).child(language).child(Util.getSelectedClass(context))
                .child(ApplicationConstants.EXTRA_CONTENT).child("activity_videos").child(subject)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<ProjectTopicModel> list=new ArrayList<>();
                        if(snapshot!=null) {
                            for(DataSnapshot ss : snapshot.getChildren())
                            {
                                ProjectTopicModel projectTopicModel=ss.getValue(ProjectTopicModel.class);
                                projectTopicModel.setKey(ss.getKey());
                                list.add(projectTopicModel);


                            }
                            topic_recyclerview.setAdapter(new ProjectToipic_Adapter(list));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}