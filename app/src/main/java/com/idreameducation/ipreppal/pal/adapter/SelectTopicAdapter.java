package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.RoundedHorizontalProgressBar;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class SelectTopicAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private static final int STRIPE_ALPHA = 150;
    boolean isOpened = true;
    private OnItemClickListener clickListener;
    private ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList;
    private float comulative_mastery;
    private Global global;
    private String mastery;
    private String takeTest;
    private String start;
    private String score;
    private String assigned;
    private String score_;
    private String latestScore;
    private int numberOfHiddenTopic = 0;
    private Context context;

    public SelectTopicAdapter(Context context, ArrayList<HashMap<String, HashMap<String, String>>> contentArrayList, String mastery, String takeTest, String start, String score, String assigned, String latestScore) {
        this.context = context;
        this.latestScore = latestScore;
        this.contentArrayList = contentArrayList;
        this.mastery = mastery;
        this.takeTest = takeTest;
        this.start = start;
        this.score_ = score;
        this.assigned = assigned;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_select_topic, viewGroup, false);
            return new SelectTopicAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof SelectTopicAdapter.ViewItem) {
            final SelectTopicAdapter.ViewItem viewHolder = (SelectTopicAdapter.ViewItem) viewHolderValue;
            try {
                try {
                    if (contentArrayList.get(position).get("" + position).get("isAssigned").equalsIgnoreCase("true")) {
                        viewHolder.textViewAssigned.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.textViewAssigned.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.textViewAssigned.setVisibility(View.GONE);
                }

                if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels / 2 + 200, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 30;
                    params.rightMargin = 30;
                    params.topMargin = 15;
                    params.bottomMargin = 15;
                    viewHolder.reletiveParent.setGravity(Gravity.CENTER_HORIZONTAL);
                    viewHolder.reletiveParent.setLayoutParams(params);
                }else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels / 2 + 200, 80);
                    params.leftMargin = 30;
                    params.rightMargin = 30;
                    params.topMargin = 15;
                    params.bottomMargin = 15;
                    viewHolder.reletiveParent.setGravity(Gravity.CENTER_HORIZONTAL);
                    viewHolder.reletiveParent.setLayoutParams(params);
                }
                /* Set visibility for the foundational topics */
                if (contentArrayList.get(position).get("" + position).get("enabled").equalsIgnoreCase("False")) {
                    viewHolder.reletiveParent.setVisibility(View.GONE);
                    numberOfHiddenTopic++;
                } else {
                    viewHolder.reletiveParent.setVisibility(View.VISIBLE);
                }
                try {
                    if (contentArrayList.get(position).get("" + position).get("IsModelTestPaper").equalsIgnoreCase("True")) {
                        viewHolder.reletiveParent.setBackgroundResource(R.drawable.yellow_pressed_back);
                        viewHolder.btnStart.setText(takeTest);
                        viewHolder.masteryTextView.setText(latestScore + " : " + contentArrayList.get(position).get("" + position).get("score"));
                    } else {
                        viewHolder.masteryTextView.setText(mastery + " : " + contentArrayList.get(position).get("" + position).get("mastery") + "%");
                        viewHolder.btnStart.setText(start);
                        viewHolder.textViewScore.setVisibility(View.GONE);
                        viewHolder.reletiveParent.setBackgroundResource(R.drawable.white_button_pressed);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.masteryTextView.setText(mastery + " : " + contentArrayList.get(position).get("" + position).get("mastery") + "%");
                    viewHolder.btnStart.setText(start);
                    viewHolder.textViewScore.setVisibility(View.GONE);
                    viewHolder.reletiveParent.setBackgroundResource(R.drawable.white_button_pressed);
                }

                String val = Util.getSelectedLanguage(context);
                if (contentArrayList.get(position).get("" + position).get("isAlternateLanguageAvailable").equalsIgnoreCase("False")) {
                    viewHolder.textViewTopicName.setText(contentArrayList.get(position).get("" + position).get("TName"));
                } else {
                    if (val.equalsIgnoreCase("English")) {
                        viewHolder.textViewTopicName.setText(contentArrayList.get(position).get("" + position).get("TName"));
                    } else {
                        viewHolder.textViewTopicName.setText(contentArrayList.get(position).get("" + position).get("TName_alt"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (contentArrayList.get(position).get("" + position).get("IsModelTestPaper").equalsIgnoreCase("False")) {
                  //  global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(Util.getSelectedBoardName(context)).child(Util.getSelectedClassName(context)).child(Util.getSubject(context)).child(ApplicationConstants.FOUNDATIONAL_TOPIC).child(contentArrayList.get(position).get("" + position).get("TopicID")).child("enabled").setValue(contentArrayList.get(position).get("" + position).get("enabled"));

                }
                if (contentArrayList.get(position).get("" + position).get("IsModelTestPaper").equalsIgnoreCase("False")) {
                    viewHolder.progress_bar_4.setProgressColors(getStripeColor(ResourcesCompat.getColor(context.getResources(), R.color.green_light, null)), ResourcesCompat.getColor(context.getResources(), R.color.green, null));
                } else {
                    viewHolder.progress_bar_4.setProgressColors(getStripeColor(ResourcesCompat.getColor(context.getResources(), R.color.yellow_light, null)), ResourcesCompat.getColor(context.getResources(), R.color.yellow, null));
                }

                viewHolder.progress_bar_4.setProgress(Integer.parseInt(contentArrayList.get(position).get("" + position).get("mastery")));


                comulative_mastery += Float.parseFloat(contentArrayList.get(position).get("" + position).get("mastery"));

            } catch (Exception e) {
                try {
                    if (contentArrayList.get(position).get("" + position).get("IsModelTestPaper").equalsIgnoreCase("False")) {
                        viewHolder.masteryTextView.setText(mastery + " : " + "0%");
                        viewHolder.progress_bar_4.setProgress(0);
                        comulative_mastery += 0.0;
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            } finally {
                int calculations = (int) (comulative_mastery / (contentArrayList.size() - numberOfHiddenTopic));
                global.setCumulativeMastery(calculations);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        try {
            if (contentArrayList != null) {
                size = contentArrayList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private int getStripeColor(@ColorInt int color) {
        return Color.argb(STRIPE_ALPHA, Color.red(color), Color.green(color), Color.blue(color));
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewTopicName;
        protected TextView masteryTextView;
        protected TextView btnStart;
        protected TextView textViewScore;
        protected TextView textViewAssigned;
        RoundedHorizontalProgressBar progress_bar_4;
        private RelativeLayout reltive_layout_back;
        private LinearLayout reletiveParent;

        public ViewItem(View holderView) {
            super(holderView);
            btnStart = holderView.findViewById(R.id.btnStart);
            textViewScore = holderView.findViewById(R.id.textViewScore);
            textViewAssigned = holderView.findViewById(R.id.textViewAssigned);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            progress_bar_4 = holderView.findViewById(R.id.progress_bar_4);
            masteryTextView = holderView.findViewById(R.id.masteryTextView);
            reletiveParent = holderView.findViewById(R.id.reletiveParent);
            reltive_layout_back = (RelativeLayout) holderView.findViewById(R.id.reltive_layout_back);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}