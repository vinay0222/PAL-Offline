package com.idreameducation.ipreppal.PalMobile.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.PalMobile.activity.DiagonosticTestActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class TrackTestAdapter_Mobile extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0;
    private final Context context;
    private OnItemClickListener clickListener;
    public ArrayList<HashMap<String, String>> questionsArrayList;
    public boolean gridView=false;

    public TrackTestAdapter_Mobile(Context context, ArrayList<HashMap<String, String>> questionsArrayList, boolean gridView) {
        this.context = context;
        this.questionsArrayList = questionsArrayList;
        this.gridView = gridView;
    }

    public TrackTestAdapter_Mobile(Context context, ArrayList<HashMap<String, String>> questionsArrayList) {
        this.context = context;
        this.questionsArrayList = questionsArrayList;
        this.gridView = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row_test_track, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            try {
                if(gridView) {
                    if (position >= 9) viewHolder.TextViewanswer.setText((position + 1)+"");
                    else viewHolder.TextViewanswer.setText("0" + (position + 1));
                    viewHolder.TextViewanswer.setGravity(Gravity.CENTER);
                } else {
                    if (position >= 9) viewHolder.TextViewanswer.setText((position + 1) + ". " + questionsArrayList.get(position).get("questionQ"));
                     else viewHolder.TextViewanswer.setText("0" + (position + 1) + ". " + questionsArrayList.get(position).get("questionQ"));
                    viewHolder.TextViewanswer.setGravity(Gravity.CENTER|Gravity.LEFT);
                }
                if(context instanceof DiagonosticTestActivity_Mobile)
                    if(((DiagonosticTestActivity_Mobile) context).skippedQHashMap.size() > 0 && ((DiagonosticTestActivity_Mobile) context).skippedQHashMap.containsKey(position)) {
                        if(((DiagonosticTestActivity_Mobile) context).skippedQHashMap.get(position)) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
                        else {
                            if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                             else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
                        }
                    } else {
                        if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                        else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE"))
                            viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
                    }
                else if(context instanceof NormalTestActivity)
                    if(((NormalTestActivity) context).skippedQHashMap.size() > 0 && ((NormalTestActivity) context).skippedQHashMap.containsKey(position)) {
                        if(((NormalTestActivity) context).skippedQHashMap.get(position)) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.yellow_iprep_question);
                        else {
                            if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                            else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
                        }
                    } else {
                        if (questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("TRUE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.blue_iprep_question);
                         else if(questionsArrayList.get(position).get("IsAttampted").equalsIgnoreCase("FALSE")) viewHolder.TextViewanswer.setBackgroundResource(R.drawable.grey_iprep_question);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
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