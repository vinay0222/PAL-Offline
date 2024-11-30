package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ReviewTestActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class ReportsListAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private ReportsListAdapter.OnItemClickListener clickListener;

    private Global global;
    private Context context;
    private ArrayList<HashMap<String, Object>> videosArrayList;
    private String date;

    private String topicID;


    public ReportsListAdapter(Context context, ArrayList<HashMap<String, Object>> videosArrayList, String date, String type, String topicID) {
        this.context = context;
        this.videosArrayList = videosArrayList;
        global = (Global) context.getApplicationContext();
        this.date = date;

        this.topicID = topicID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_reports, viewGroup, false);
        return new ReportsListAdapter.ViewItem(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ReportsListAdapter.ViewItem) {
            final ReportsListAdapter.ViewItem viewHolder = (ReportsListAdapter.ViewItem) viewHolderValue;
            try {
                Date date = new Date(Long.parseLong((String) videosArrayList.get(position).get("key")));
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateFormatted = formatter.format(date);
                String date_ = (String) videosArrayList.get(position).get("date");
                viewHolder.textViewTime.setText(date_ + " - " + dateFormatted);
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.textViewTime.setText((String) videosArrayList.get(position).get("key"));
            }

            viewHolder.textViewTopicName.setText((String) videosArrayList.get(position).get("topicName"));
            String type = (String) videosArrayList.get(position).get("type");
            if (type.equalsIgnoreCase("practice")) {
                viewHolder.textViewReview.setText((String) videosArrayList.get(position).get("mastery"));
            } else if (type.equalsIgnoreCase("video")) {
                long minutes = (Long.parseLong((String) videosArrayList.get(position).get("time")) / 1000) /
                        60;
                long seconds = (Long.parseLong((String) videosArrayList.get(position).get("time")) / 1000) %
                        60;

                viewHolder.textViewReview.setText(minutes + "Mins " + seconds + "Secs");
            } else {
                viewHolder.textViewReview.setText((String) videosArrayList.get(position).get("scores") + "/" + (String) videosArrayList.get(position).get("totalScores"));
                viewHolder.textViewReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, ReviewTestActivity.class);
                        String date = (String) videosArrayList.get(position).get("date");
                        String type = (String) videosArrayList.get(position).get("type");
                        intent.putExtra("date", date);
                        intent.putExtra("type", type);
                        intent.putExtra("topicID", topicID);
                        intent.putExtra("key", (String) videosArrayList.get(position).get("key"));
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return videosArrayList.size();
    }


    public void SetOnItemClickListener(final ReportsListAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTime;
        protected TextView textViewTopicName;
        protected Button textViewReview;

        public ViewItem(View holderView) {
            super(holderView);
            textViewReview = holderView.findViewById(R.id.textViewReview);
            textViewTime = holderView.findViewById(R.id.textViewTime);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}