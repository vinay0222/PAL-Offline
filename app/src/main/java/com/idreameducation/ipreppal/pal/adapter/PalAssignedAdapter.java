package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class PalAssignedAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final Global global;
    private final Context context;
    private ArrayList<String> dateArrayList,converedDateArrayList,converedDateArrayListLong=new ArrayList<>();
    private ArrayList<Long> dateArrayListLong=new ArrayList<>();
    private HashMap<String, Object> assignedContentHashMap;

    public PalAssignedAdapter(Context context, HashMap<String, Object> assignedContentHashMap, ArrayList<String> dateArrayList, ArrayList<String> converedDateArrayList) {
        this.context = context;
        this.dateArrayList = dateArrayList;
        this.converedDateArrayList = converedDateArrayList;
        this.assignedContentHashMap = assignedContentHashMap;
        global = (Global) context.getApplicationContext();

        /** creating array to short list by time */
        for(int i=0;i<=dateArrayList.size()-1;i++) {
            /** adding date in assignTime list */
            dateArrayListLong.add(Long.parseLong(dateArrayList.get(i)));
        }

        Collections.sort(dateArrayListLong);
        Collections.reverse(dateArrayListLong);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Collections.reverse(dateArrayListLong);
//            }
//        },1000);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_assigned, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, @SuppressLint("RecyclerView") final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            viewHolder.textViewDate.setText(Util.timestampToDate(Long.valueOf(dateArrayListLong.get(position))));
            String date = dateArrayListLong.get(position).toString();
            HashMap<String, Object> hashMap2 = (HashMap<String, Object>) assignedContentHashMap.get(Util.timestampToDate(Long.valueOf(date)));
            HashMap<String, Object> hashMap = (HashMap<String, Object>) hashMap2.get(date);
            ArrayList<String> keyArrayList = new ArrayList<>();
            for (String key : hashMap2.keySet()) {
                keyArrayList.add(key);
            }
            PalAssignedInnerAdapter palAssignedInnerAdapter = new PalAssignedInnerAdapter(context, hashMap2, keyArrayList, global.getBatchID(),converedDateArrayList.get(position));
            viewHolder.recyclerView.setAdapter(palAssignedInnerAdapter);

//            viewHolder.textViewDate.setText(Util.timestampToDate(Long.valueOf(dateArrayListLong.get(position))));
////            viewHolder.textViewDate.setText(converedDateArrayListLong.get(position));
//            String date = dateArrayListLong.get(position).toString();
//            HashMap<String, Object> hashMap2 = (HashMap<String, Object>) assignedContentHashMap.get(Util.timestampToDate(Long.valueOf(date)));
//            HashMap<String, Object> hashMap = (HashMap<String, Object>) hashMap2.get(date);
//            ArrayList<String> keyArrayList = new ArrayList<>();
//            for (String key : hashMap2.keySet()) {
//                keyArrayList.add(key);
//            }
//            PalAssignedInnerAdapter palAssignedInnerAdapter = new PalAssignedInnerAdapter(context, hashMap2, keyArrayList,0);
//            viewHolder.recyclerView.setAdapter(palAssignedInnerAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return dateArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewDate;
        protected RecyclerView recyclerView;

        public ViewItem(View holderView) {
            super(holderView);
            textViewDate = holderView.findViewById(R.id.textViewDate);
            recyclerView = holderView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(manager);
            // holderView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }


}