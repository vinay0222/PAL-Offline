package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.LevelVideoModel;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;

import java.util.ArrayList;

public class VideoLevelAdapter extends RecyclerView.Adapter<VideoLevelAdapter.holder> {

    ArrayList<LevelVideoModel> videoList;
    Context context;

    public VideoLevelAdapter(ArrayList<LevelVideoModel> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoLevelAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videoleve_view, parent, false);
        return new holder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull VideoLevelAdapter.holder holder, int position) {

        if (position >= 0 && position < 9) {
            holder.number_text.setText("0" + (position + 1) + ". ");
        } else {
            holder.number_text.setText((position + 1) + ". ");
        }

        holder.card.setBackground(context.getResources().getDrawable(R.drawable.bg_unselected_chapter));
        holder.topic_text.setText(videoList.get(position).getName());

        if(PalContentListingActivity.keys.equals(videoList.get(position).getKey())) holder.card.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));
        else holder.card.setBackground(context.getResources().getDrawable(R.drawable.bg_unselected_chapter));

        holder.topic_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.card.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));
                QuizActivity.playVideo(context,position,null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class holder extends RecyclerView.ViewHolder{

        TextView number_text,topic_text;
        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);

            number_text=itemView.findViewById(R.id.number_text);
            topic_text=itemView.findViewById(R.id.topic_text);
            card=itemView.findViewById(R.id.card);

        }
    }
}
