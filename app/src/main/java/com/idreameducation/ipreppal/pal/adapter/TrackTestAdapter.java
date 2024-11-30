package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.BiMonthlyTestActivity;
import com.idreameducation.ipreppal.pal.activity.DiagonosticTestActivity;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class TrackTestAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0;
    private final Context context;
    private OnItemClickListener clickListener;
    public ArrayList<HashMap<String, String>> questionsArrayList;

    public TrackTestAdapter(Context context, ArrayList<HashMap<String, String>> questionsArrayList) {
        this.context = context;
        this.questionsArrayList = questionsArrayList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_test_track, viewGroup, false);
            return new TrackTestAdapter.ViewItem(view);
        }
        return null;
    }


    public void updateAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof TrackTestAdapter.ViewItem) {
            final TrackTestAdapter.ViewItem viewHolder = (TrackTestAdapter.ViewItem) viewHolderValue;
            try {
                if (position >= 9) {
                    viewHolder.TextViewanswer.setText((position + 1) + ". " + questionsArrayList.get(position).get("questionQ"));
                } else {
                    viewHolder.TextViewanswer.setText("0" + (position + 1) + ". " + questionsArrayList.get(position).get("questionQ"));
                }

                if(context instanceof DiagonosticTestActivity){
                    if(((DiagonosticTestActivity) context).skippedQHashMap.size() > 0 && ((DiagonosticTestActivity) context).skippedQHashMap.containsKey(position)){
                        if(((DiagonosticTestActivity) context).skippedQHashMap.get(position)){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
                        }else{
                            if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                            } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                            }
                        }
                    }else{
                        if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                        } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                        }
                    }
                }
                else if(context instanceof NormalTestActivity){
                    if(((NormalTestActivity) context).skippedQHashMap.size() > 0 && ((NormalTestActivity) context).skippedQHashMap.containsKey(position)){
                        if(((NormalTestActivity) context).skippedQHashMap.get(position)){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
                        }else{
                            if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                            } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                            }
                        }
                    }else{
                        if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                        } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                        }
                    }
                }
                else if(context instanceof BiMonthlyTestActivity){
                    if(((BiMonthlyTestActivity) context).skippedQHashMap.size() > 0 && ((BiMonthlyTestActivity) context).skippedQHashMap.containsKey(position)){
                        if(((BiMonthlyTestActivity) context).skippedQHashMap.get(position)){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
                        }else{
                            if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                            } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                                viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                            }
                        }
                    }else{
                        if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                        } else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")){
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
//                    if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")){
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }else {
//                        viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
//                    }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView TextViewanswer;


        public ViewItem(View holderView) {
            super(holderView);
            TextViewanswer = holderView.findViewById(R.id.TextViewanswer);
            holderView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
            Util.preventTwoClick(view);
        }
    }

}