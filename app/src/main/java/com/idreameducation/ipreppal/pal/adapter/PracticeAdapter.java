package com.idreameducation.ipreppal.pal.adapter;

import static com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter.activeFoundationTopic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.fragments.PalPracticeFrament;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class PracticeAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final Global global;

    private String masterytext;


    public PracticeAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;

        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        global = (Global) context.getApplicationContext();
        if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")){
            masterytext="महारत";
        }else{
            masterytext="Mastery";
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_practice, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;

            if(contentArrayList.get(position).get("isFoundation")==null) contentArrayList.get(position).put("isFoundation","false");

            if (contentArrayList.get(position).get("isFoundation").equals("false")) viewHolder.textViewName.setText(contentArrayList.get(position).get("TName"));
                else viewHolder.textViewName.setText("Foundation Class "+contentArrayList.get(position).get("sClass")
                    .replace("_nonmedical_medical","")
                    .replace("_commerce","")
                    .replace("_arts","")+" - "+
                    contentArrayList.get(position).get("TName"));


            viewHolder.textViewName.setTextColor(Color.parseColor(global.getColor()));

            if(Util.isPortraitMode(context)) {
                viewHolder.card.setAlpha(0.5F);
                if(!PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice")) viewHolder.card.setAlpha(1);

            }
            else {
                if(((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice")) viewHolder.card.setAlpha(0.5F);
                else viewHolder.card.setAlpha(1);

            }

            if(contentArrayList.get(position).get("isFoundation").equals("true")) {
                viewHolder.card.setAlpha(0.5F);
                if(activeFoundationTopic.equals(contentArrayList.get(position).get("TopicID"))) viewHolder.card.setAlpha(1);

            }




            ArrayList<PracticeScoreModel> practiceScoreModelArrayList;
            if(!Util.isPortraitMode(context)) practiceScoreModelArrayList= PalContentListingActivity.practiceScoreModelArrayList;
            else practiceScoreModelArrayList= PalContentListingActivity_Mobile.instance.practiceScoreModelArrayList;

            for(PracticeScoreModel item: practiceScoreModelArrayList){
                if(item.getTopicId().equals(contentArrayList.get(position).get("TopicID"))){
                    viewHolder.textViewmastery.setText(masterytext+": " + item.getScore() + "%");
                    PalPracticeFrament.practice_mastry= Integer.parseInt(item.getScore());
                    updateLevel(viewHolder,Integer.parseInt(item.getScore()));
                    break;
                }else{
                    viewHolder.textViewmastery.setText(masterytext+": 0%");
                }
            }
        }
    }

    /** update level */
    private void updateLevel(PracticeAdapter.ViewItem view,int masteryProgress) {
        if(masteryProgress<25) {
            view.level1_icon.setImageResource(R.drawable.un_locked);
            view.level2_icon.setImageResource(R.drawable.locked);
            view.level3_icon.setImageResource(R.drawable.locked);
            view.level4_icon.setImageResource(R.drawable.locked);
        }else if(masteryProgress<50) {
            view.level1_icon.setImageResource(R.drawable.ic_tick);
            view.level2_icon.setImageResource(R.drawable.un_locked);
            view.level3_icon.setImageResource(R.drawable.locked);
            view.level4_icon.setImageResource(R.drawable.locked);
        }else if(masteryProgress<75) {
            view.level1_icon.setImageResource(R.drawable.ic_tick);
            view.level2_icon.setImageResource(R.drawable.ic_tick);
            view.level3_icon.setImageResource(R.drawable.un_locked);
            view.level4_icon.setImageResource(R.drawable.locked);
        }else if(masteryProgress<100) {
            view.level1_icon.setImageResource(R.drawable.ic_tick);
            view.level2_icon.setImageResource(R.drawable.ic_tick);
            view.level3_icon.setImageResource(R.drawable.ic_tick);
            view.level4_icon.setImageResource(R.drawable.un_locked);
        }else if(masteryProgress==100) {
            view.level1_icon.setImageResource(R.drawable.ic_tick);
            view.level2_icon.setImageResource(R.drawable.ic_tick);
            view.level3_icon.setImageResource(R.drawable.ic_tick);
            view.level4_icon.setImageResource(R.drawable.ic_tick);
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
        protected TextView textViewName;
        protected TextView textViewmastery;
        ImageView level1_icon,level2_icon,level3_icon,level4_icon;

        LinearLayout card;

        public ViewItem(View holderView) {
            super(holderView);
            textViewName = holderView.findViewById(R.id.textViewName);
            textViewmastery = holderView.findViewById(R.id.textViewmastery);

            level1_icon = holderView.findViewById(R.id.level1_icon);
            level2_icon = holderView.findViewById(R.id.level2_icon);
            level3_icon = holderView.findViewById(R.id.level3_icon);
            level4_icon = holderView.findViewById(R.id.level4_icon);
            card = holderView.findViewById(R.id.card);

            level1_icon.setImageResource(R.drawable.un_locked);

            if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")){
                textViewmastery.setText("महारत: 0%");
            }else{
                textViewmastery.setText("Mastery: 0%");
            }

            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}