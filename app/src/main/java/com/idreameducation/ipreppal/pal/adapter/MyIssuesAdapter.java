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

import com.idreameducation.ipreppal.pal.activity.MyIssuesActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.SupportModel;
import com.idreameducation.ipreppal.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyIssuesAdapter extends RecyclerView.Adapter<MyIssuesAdapter.holder> {

    ArrayList<SupportModel> supportModels;
    Context context;

    public MyIssuesAdapter(ArrayList<SupportModel> supportModels) {
        this.supportModels = supportModels;
    }

    public MyIssuesAdapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.issueview,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, @SuppressLint("RecyclerView") int position) {

        holder.card.setBackground(context.getResources().getDrawable(R.drawable.rounded_background));

        /** set serial no */
        if (position >= 0 && position < 9) holder.nameText.setText("0" + (position + 1) + ". Issue "+supportModels.get(position).getTimestamp().toString());
        else holder.nameText.setText((position + 1) + ". Issue "+supportModels.get(position).getTimestamp().toString());

        /** set Date of issue */
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String dateString = formatter.format(new Date(Long.parseLong(supportModels.get(position).getTimestamp())));
        holder.dateText.setText(dateString);

        /** check selected item */
        if(supportModels.get(position).isSelected()) holder.card.setBackground(context.getResources().getDrawable(R.drawable.rounded_background_selected));
        else holder.card.setBackground(context.getResources().getDrawable(R.drawable.rounded_background));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** handling if mobile view open chat in new activity */
                if(Util.isPortraitMode(context)) ((MyIssuesActivity)context).openChatInNewActivity(position);
                else ((MyIssuesActivity)context).setSelectedIssue(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return supportModels.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView dateText,nameText;
        LinearLayout card;
        public holder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            dateText=itemView.findViewById(R.id.dateText);
            nameText=itemView.findViewById(R.id.nameText);
        }
    }
}
