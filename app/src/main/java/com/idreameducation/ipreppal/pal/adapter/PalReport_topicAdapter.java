package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PalReport_topicAdapter extends RecyclerView.Adapter<PalReport_topicAdapter.topic_holder> {

    Context context;
    PalReportPracticeAdapter.ViewItem viewHolder;
    private ArrayList<HashMap<String, Object>> mainArrayList;
    private ArrayList<String> dateArrayList;
    private ArrayList<String> nameArrayList;
    ArrayList<String> keyArrayList;
    int mainpos;
    int rowindex=-1;
    boolean firstopen=true;

    public PalReport_topicAdapter(Context context, PalReportPracticeAdapter.ViewItem viewHolder, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, ArrayList<String> keyArrayList,int mainpos) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.mainpos = mainpos;
        this.mainArrayList = mainArrayList;
        this.dateArrayList = dateArrayList;
        this.nameArrayList = nameArrayList;
        this.keyArrayList = keyArrayList;
    }

    public PalReport_topicAdapter() {}

    @NonNull
    @Override
    public topic_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_topic_view, viewGroup, false);
        return new topic_holder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull topic_holder holder, int position) {

        holder.text.setTextColor(context.getResources().getColor(R.color.profile_text));

        holder.text.setText(nameArrayList.get(position));

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.text.setTextColor(context.getResources().getColor(R.color.blue));
                PalReportPracticeAdapter.update_position(context,viewHolder,mainArrayList,dateArrayList,nameArrayList,mainpos,position,keyArrayList);
                rowindex=position;
                notifyDataSetChanged();
            }
        });

        if(firstopen) {
            rowindex=nameArrayList.size()-1;
            firstopen=false;
        }

        changeTextColour(holder,position);

    }


    private void changeTextColour(topic_holder holder, int position) {
        if (rowindex == position) holder.text.setTextColor(context.getResources().getColor(R.color.blue));
        else holder.text.setTextColor(context.getResources().getColor(R.color.profile_text));
    }

    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    class topic_holder extends RecyclerView.ViewHolder
    {
        TextView text;
        public topic_holder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
        }
    }

}