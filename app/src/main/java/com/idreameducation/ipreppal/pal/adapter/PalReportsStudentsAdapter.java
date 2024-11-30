package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.ArrayList;
import java.util.HashMap;


public class PalReportsStudentsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private PalReportsStudentsAdapter.OnItemClickListener clickListener;
    private Global global;
    private Context context;
    private ArrayList<HashMap<String, String>> recentChatArrayList;


    public PalReportsStudentsAdapter(Context context, ArrayList<HashMap<String, String>> recentChatArrayList) {
        this.context = context;
        this.recentChatArrayList = recentChatArrayList;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_report_students, viewGroup, false);
        return new PalReportsStudentsAdapter.ViewItem(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalReportsStudentsAdapter.ViewItem) {
            final PalReportsStudentsAdapter.ViewItem viewHolder = (PalReportsStudentsAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewUserName.setText(recentChatArrayList.get(position).get("studentName") + "(" + recentChatArrayList.get(position).get("sClass") + ")");
            if(recentChatArrayList.get(position).get("isSelected").equalsIgnoreCase("no")){
                viewHolder.imageViewselection.setImageResource(R.mipmap.empty_star);
            }else {
                viewHolder.imageViewselection.setImageResource(R.mipmap.right);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return recentChatArrayList.size();
    }


    public void SetOnItemClickListener(final PalReportsStudentsAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewUserName;
        protected TextView textViewMessage;
        protected ImageView imageViewselection;

        public ViewItem(View holderView) {
            super(holderView);
            textViewUserName = holderView.findViewById(R.id.textViewUserName);
            imageViewselection = holderView.findViewById(R.id.imageViewselection);
            textViewMessage = holderView.findViewById(R.id.textViewMessage);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}