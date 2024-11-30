package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.ArrayList;
import java.util.HashMap;


public class PalRecentChatAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private PalRecentChatAdapter.OnItemClickListener clickListener;
    private final Global global;
    private final Context context;
    private ArrayList<HashMap<String, String>> recentChatArrayList;
    private ArrayList<HashMap<String, Object>> recentAssignmentList;
    private final String type;

    public PalRecentChatAdapter(Context context, ArrayList<HashMap<String, String>> recentChatArrayList) {
        this.type="Chat";
        this.context = context;
        this.recentChatArrayList = recentChatArrayList;
        global = (Global) context.getApplicationContext();
    }

    public PalRecentChatAdapter(@NonNull Context context, ArrayList<HashMap<String, Object>>  recentAssignmentList,String type) {
        this.type="Assignment";
        this.context = context;
        this.recentAssignmentList = recentAssignmentList;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_recent_chats, viewGroup, false);
        return new PalRecentChatAdapter.ViewItem(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalRecentChatAdapter.ViewItem) {
            final PalRecentChatAdapter.ViewItem viewHolder = (PalRecentChatAdapter.ViewItem) viewHolderValue;

            if(type.equals("Chat")) {
                viewHolder.textViewUserName.setText(recentChatArrayList.get(position).get("userName"));
                if(recentChatArrayList.get(position).get("inputType")==null) {
                    HashMap<String, String> v=recentChatArrayList.get(position);
                    v.put("inputType","Text");
                    recentChatArrayList.remove(position);
                    recentChatArrayList.add(position,v);
                }
                if(recentChatArrayList.get(position).get("inputType").equals("Image"))
                    viewHolder.textViewMessage.setText("Shared a Image");
                else if(recentChatArrayList.get(position).get("inputType").equals("video"))
                    viewHolder.textViewMessage.setText("Shared a Video");
                else if(recentChatArrayList.get(position).get("inputType").equals("pdf"))
                    viewHolder.textViewMessage.setText("Shared a Pdf");
                else viewHolder.textViewMessage.setText(recentChatArrayList.get(position).get("message"));
            }
            else if(type.equals("Assignment")) {
                viewHolder.textViewUserName.setText(recentAssignmentList.get(position).get("type")+" - "+recentAssignmentList.get(position).get("name"));
                viewHolder.textViewMessage.setText(recentAssignmentList.get(position).get("batchClass").toString().replace("_"," "));

                String type=recentAssignmentList.get(position).get("type").toString();
                switch (type) {
                    case "practice":
                    case "biMonthlyTest":
                    case "book": viewHolder.textViewClassID.setImageResource(R.drawable.practice_icon);
                        break;
                    case "video": viewHolder.textViewClassID.setImageResource(R.drawable.video_icon);
                        break;
                    case "diagnostic":
                    case "diagnosticTest":
                    case "finalTest": viewHolder.textViewClassID.setImageResource(R.drawable.test_icon);
                        break;
                }

            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if(recentChatArrayList!=null) return recentChatArrayList.size();
        else return recentAssignmentList.size();
    }


    public void SetOnItemClickListener(final PalRecentChatAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewUserName;
        protected TextView textViewMessage;
        protected ImageView textViewClassID;

        public ViewItem(View holderView) {
            super(holderView);
            textViewUserName = holderView.findViewById(R.id.textViewUserName);
            textViewClassID = holderView.findViewById(R.id.textViewClassID);
            textViewMessage = holderView.findViewById(R.id.textViewMessage);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}