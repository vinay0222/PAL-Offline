package com.idreameducation.ipreppal.PalMobile.adapter;

import static com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity.selected_pos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.PalMobile.Models.ProjectSubTopicModel;
import com.idreameducation.ipreppal.PalMobile.activity.Project_SubTopic_Activity;
import com.idreameducation.ipreppal.R;

import java.util.ArrayList;

public class Project_Subtopic_Adapter extends RecyclerView.Adapter<Project_Subtopic_Adapter.holder>{

    Context context;
    ArrayList<ProjectSubTopicModel> list;

    public Project_Subtopic_Adapter(ArrayList<ProjectSubTopicModel> list) {
        this.list = list;
    }

    public Project_Subtopic_Adapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_subtopic_view, parent, false);
        return new holder(view);
    }

    String thumbnaillink="https://img.youtube.com/vi/GDFUdMvacI0/0.jpg";

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.topicname_text.setText(list.get(position).getName());

        String video_id=list.get(position).getOnlineLink().replace("https://www.youtube.com/watch?v=","");

        thumbnaillink="https://img.youtube.com/vi/"+video_id+"/0.jpg";

        System.out.println("------ thumbnaillink "+thumbnaillink);
        Glide.with(context).load(thumbnaillink).into(holder.thumbnail);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Project_SubTopic_Activity.openFragment(video_id, list.get(position).getName(),list.get(position).getOfflineLink(),Integer.parseInt(list.get(position).getId()),position,selected_pos,list.get(position).getName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder{

        TextView topicname_text;
        ImageView thumbnail;
        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            thumbnail=itemView.findViewById(R.id.thumbnail);
            topicname_text=itemView.findViewById(R.id.topicname_text);
        }
    }
}
