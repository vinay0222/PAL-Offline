package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.ArrayList;
import java.util.HashMap;

public class StemListingSideAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0;
    private OnItemClickListener clickListener;
    private final String sClass;
    private String color;
    private final String maincolor;
    private final Global global;
    private final Context context;
    private final ArrayList<HashMap<String, Object>> contentArrayList;

    public StemListingSideAdapter(Context context, ArrayList<HashMap<String, Object>> contentArrayList, String sClass,String color) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.sClass = sClass;
        this.color = color;
        this.maincolor = color;
        global = (Global) context.getApplicationContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stem_listing_sidebar, parent, false);
        return new ViewItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) holder;
            if (contentArrayList.get(position).get("selected").equals("true")) {

                if(!color.contains("#2f"))
                {
                    color=color.replace("#","");
                    color="#2f"+color;
                }
                viewHolder.reletiveParent.setBackgroundColor(Color.parseColor(color));
            } else {
                viewHolder.reletiveParent.setBackgroundResource(R.color.white);
            }

            viewHolder.textViewTopicName.setText((String) contentArrayList.get(position).get("name"));
            if (position < 9) {
                viewHolder.textViewSno.setText("0"+(position + 1) + ". ");
            } else {
                viewHolder.textViewSno.setText((position + 1) + ". ");
            }

            viewHolder.textViewSno.setTextColor(Color.parseColor(maincolor));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return contentArrayList.size();
    }


    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTopicName;
        protected TextView textViewSno;
        protected LinearLayout reletiveParent;

        public ViewItem(View holderView) {
            super(holderView);

            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewSno = holderView.findViewById(R.id.textViewSno);
            reletiveParent = holderView.findViewById(R.id.reletiveParent);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
