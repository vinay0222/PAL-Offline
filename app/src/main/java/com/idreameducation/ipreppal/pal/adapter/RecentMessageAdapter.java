package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class RecentMessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private RecentMessageAdapter.OnItemClickListener clickListener;
    private Global global;
    private Context context;
    private ArrayList<HashMap<String, Object>> assignedContentArrayList;


    public RecentMessageAdapter(Context context, ArrayList<HashMap<String, Object>> assignedContentArrayList) {
        this.context = context;
        this.assignedContentArrayList = assignedContentArrayList;

        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_assigned_topic, viewGroup, false);
        return new RecentMessageAdapter.ViewItem(view);


    }

    private String date_;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof RecentMessageAdapter.ViewItem) {
            final RecentMessageAdapter.ViewItem viewHolder = (RecentMessageAdapter.ViewItem) viewHolderValue;

            String date = Util.getCurrentDateWithDifferentFormat().split(" ")[0];


            String dateOfAssignment = (String) assignedContentArrayList.get(position).get("date");
            String dateTocompare = dateOfAssignment.split(" ")[0];
            int diff = Integer.parseInt(date) - Integer.parseInt(dateTocompare);
            switch (diff) {
                case 0:
                    viewHolder.textViewNDay.setText("Today");
                    break;
                case 1:
                    viewHolder.textViewNDay.setText("Yesterday");
                    break;
                default:
                    viewHolder.textViewNDay.setText(dateOfAssignment);
                    break;
            }

            if (date_ == null) {
                date_ = dateTocompare;
                viewHolder.textViewNDay.setVisibility(View.VISIBLE);
                viewHolder.textViewClear.setVisibility(View.VISIBLE);
            } else {
                if (!dateTocompare.equalsIgnoreCase(date_)) {
                    date_ = dateTocompare;
                    viewHolder.textViewNDay.setVisibility(View.VISIBLE);
                    viewHolder.textViewClear.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.textViewNDay.setVisibility(View.GONE);
                    viewHolder.textViewClear.setVisibility(View.GONE);
                }
            }
            String type = (String) assignedContentArrayList.get(position).get("type");
            String messge = "";
            switch (type) {
                case "video":
                    messge = "assign you a Video";
                    break;
                case "biMonthlyTest":
                    messge = "assign you a Bimonthly test";
                    break;
                case "practice":
                    messge = "assign you a Practice";
                    break;
                case "diagnosticTest":
                    messge = "assign you a Diagonostic Test";
                    break;
            }
            String topicName = (String) assignedContentArrayList.get(position).get("topicName");
            String tName = (String) assignedContentArrayList.get(position).get("teacherName");
            String endDate = (String) assignedContentArrayList.get(position).get("date");
            viewHolder.textViewNTitle.setText(tName + " " + messge);
            viewHolder.textViewNotificationMessage.setText("Topic name : "+ topicName);
            viewHolder.textViewtime.setText(endDate);
            boolean isOpened = (boolean) assignedContentArrayList.get(position).get("isopend");
            if (isOpened) {

            } else {

            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return assignedContentArrayList.size();
    }


    public void SetOnItemClickListener(final RecentMessageAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView textViewNTitle;
        private TextView textViewtime;
        private TextView textViewNDay;
        private TextView textViewClear;
        private TextView textViewNotificationMessage;

        public ViewItem(View holderView) {
            super(holderView);

            textViewNotificationMessage = holderView.findViewById(R.id.textViewNotificationMessage);
            textViewClear = holderView.findViewById(R.id.textViewClear);
            textViewtime = holderView.findViewById(R.id.textViewtime);
            textViewNTitle = holderView.findViewById(R.id.textViewNTitle);
            textViewNDay = holderView.findViewById(R.id.textViewNDay);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}