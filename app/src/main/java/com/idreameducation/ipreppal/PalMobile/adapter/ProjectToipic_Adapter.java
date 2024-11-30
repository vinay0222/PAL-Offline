package com.idreameducation.ipreppal.PalMobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.PalMobile.Models.ProjectTopicModel;
import com.idreameducation.ipreppal.PalMobile.activity.ProjectVideos_topic_Activity;
import com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity;
import com.idreameducation.ipreppal.R;

import java.util.ArrayList;

public class ProjectToipic_Adapter extends RecyclerView.Adapter<ProjectToipic_Adapter.hoder> {

    ArrayList<ProjectTopicModel> list;
    Context context;

    public ProjectToipic_Adapter(ArrayList<ProjectTopicModel> list) {
        this.list = list;
    }

    public ProjectToipic_Adapter() {
    }

    @NonNull
    @Override
    public hoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_topic_view, parent, false);
        return new hoder(view);
    }



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull hoder holder, int position) {

        int pos=position+1;

        if(pos<=9)
        {
            holder.count_text.setText("0"+pos+".");
        }
        else
        {
            holder.count_text.setText(pos+".");
        }


        holder.chaptername_text.setText(list.get(position).getName());
        holder.count_text.setTextColor(Color.parseColor(ProjectVideos_topic_Activity.color));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectVideos_topic_Activity.selected_Topic_list=list.get(position).getTopics();
                context.startActivity(new Intent(context, Project_SubTopic_Activity.class)
                        .putExtra("position",pos)
                        .putExtra("topicname",list.get(position).getName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class hoder extends RecyclerView.ViewHolder
    {
        TextView count_text,chaptername_text;
        LinearLayout card ;

        public hoder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            count_text=itemView.findViewById(R.id.count_text);
            chaptername_text=itemView.findViewById(R.id.chaptername_text);
        }
    }
}
