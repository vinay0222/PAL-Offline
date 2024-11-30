package com.idreameducation.ipreppal.pal.adapter.simulationAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.simulationModel.SimulationTopicsModel;
import com.idreameducation.ipreppal.pal.activity.SimulationActivity;
import com.idreameducation.ipreppal.pal.activity.SimulationTopicsListingActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.Random;

public class TopicsAdapterSimulation extends RecyclerView.Adapter<TopicsAdapterSimulation.holder>{

    ArrayList<SimulationTopicsModel> list;

    ArrayList<Integer> iconList;
    Context context;

    public TopicsAdapterSimulation(ArrayList<SimulationTopicsModel> list) {
        this.list = list;
        iconList=new ArrayList<Integer>();
        iconList.add(R.drawable.simulation1);
        iconList.add(R.drawable.simulation2);
        iconList.add(R.drawable.simulation3);
        iconList.add(R.drawable.simulation4);
        iconList.add(R.drawable.simulation5);
    }

    public TopicsAdapterSimulation() {
    }

    @NonNull
    @Override
    public TopicsAdapterSimulation.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_topic_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsAdapterSimulation.holder holder, @SuppressLint("RecyclerView") int position) {
        holder.topicNameView.setText(list.get(position).getName());

        Random r = new Random();

        int imageCount= r.nextInt(5 - 1) + 1;

        holder.icon.setImageResource(iconList.get(imageCount));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SimulationTopicsListingActivity.contentType==SimulationTopicsListingActivity.CONTENT_TYPE_SIMULATION) {
                    Intent intent=new Intent(context, SimulationActivity.class);
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("topicName",list.get(position).getTopicName());
                    intent.putExtra("videoName",list.get(position).getName());
                    intent.putExtra("onlineLink",list.get(position).getOnlineLink());
                    intent.putExtra("onlineLink",list.get(position).getName());
                    intent.putExtra("name",list.get(position).getName());
                    intent.putExtra("offlineLink",list.get(position).getOfflineLink());
                    intent.putExtra("subjectName",((SimulationTopicsListingActivity)context).subjectName);
                    Util.setSubjectId(context,list.get(position).getSubjectID());
                    Util.setSubjectName(context,list.get(position).getSubjectID());
                    Util.setSubject(context,((SimulationTopicsListingActivity)context).subjectName);
                    context.startActivity(intent);
                }
//                else if(SimulationTopicsListingActivity.contentType==SimulationTopicsListingActivity.CONTENT_TYPE_AUDIOBOOK) {
//                    Intent intent = new Intent(context, TestingAudioPlayer.class);
//                    intent.putExtra("onlineLink", "ChI_Chidiya_Aur_Chirgun_CVII_DR_ABNCERT.mp3");
//                    intent.putExtra("offlineLink",  "ChI_Chidiya_Aur_Chirgun_CVII_DR_ABNCERT.mp3");
//                    intent.putExtra("subjectName", list.get(position).getName());
//                    intent.putExtra("bookId", list.get(position).getId());
//                    intent.putExtra("topicName", list.get(position).getName());
//                    intent.putExtra("subjectName", list.get(position).getTopicName());
//                    intent.putExtra("topicName__", list.get(position).getTopicName());
//                    intent.putExtra("categoryID", "audioBooks");
//                    intent.putExtra("subjectID", Util.getSubjectId(context));
//                    //    Util.setSubject(context, list.get(position).get(ApplicationConstants.TOPICS)).get(position).get("topicName"));
//                    Util.preventTwoClick(view);
//                    context.startActivity(intent);
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder{

        TextView topicNameView;
        ImageView icon;
        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);
            topicNameView=itemView.findViewById(R.id.topicNameView);
            card=itemView.findViewById(R.id.card);
            icon=itemView.findViewById(R.id.icon);
        }
    }
}
