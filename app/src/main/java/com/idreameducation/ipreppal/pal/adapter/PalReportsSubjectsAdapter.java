package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.ReportsActivity;
import com.idreameducation.ipreppal.util.RoundedHorizontalProgressBar;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class PalReportsSubjectsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> subjectsArrayList;
    private final Context context;
    private final RequestOptions requestOptions;

    public PalReportsSubjectsAdapter(Context context, ArrayList<HashMap<String, String>> subjectsArrayList) {
        this.context = context;
        this.subjectsArrayList = subjectsArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_subjects_reports, viewGroup, false);
            return new PalReportsSubjectsAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalReportsSubjectsAdapter.ViewItem) {
            final PalReportsSubjectsAdapter.ViewItem viewHolder = (PalReportsSubjectsAdapter.ViewItem) viewHolderValue;

            viewHolder.textViewTime.setText(subjectsArrayList.get(position).get("time"));

            if(Util.getSelectedLanguage(context).equals("hindi") && subjectsArrayList.get(position).get("name").equals("Books")) viewHolder.textViewSubject.setText("पुस्तकें");
            else if(Util.getSelectedLanguage(context).equals("hindi") && subjectsArrayList.get(position).get("name").equals("Project Videos")) viewHolder.textViewSubject.setText("परियोजना वीडियो");
            else if(Util.getSelectedLanguage(context).equals("hindi") && subjectsArrayList.get(position).get("name").equals("Simulation")) viewHolder.textViewSubject.setText("सिमुलेशन");
            else viewHolder.textViewSubject.setText(subjectsArrayList.get(position).get("name"));

            /** Set Image on ImageView  using glide */
            viewHolder.subjectImageView.setVisibility(View.GONE);

            String iconName = subjectsArrayList.get(position).get("id");
            String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + iconName+".png";
            File file = new File(completePath);
            Uri imageUri = Uri.fromFile(file);

            if(imageUri!=null && Util.isOfflineMode(context)) {
                if (file.exists())
                    Glide.with(context).load(imageUri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            viewHolder.subjectImageView.setVisibility(View.VISIBLE);


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                            viewHolder.subjectImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                            return false;
                        }
                    }).into(viewHolder.subjectImageView);
                else {
                    viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                    if (iconName.equals("Books"))
                        viewHolder.subjectImageView.setImageResource(R.drawable.books);
                    else if (iconName.equals("Project Videos"))
                        viewHolder.subjectImageView.setImageResource(R.drawable.projects);
                }
            }
            else Glide.with(context).load(subjectsArrayList.get(position).get("icon")).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    viewHolder.subjectImageView.setVisibility(View.VISIBLE);

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                    viewHolder.subjectImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                    return false;
                }
            }).into(viewHolder.subjectImageView);

            /** Set Progress  */
            viewHolder.progress.setMax(ReportsActivity.max_progress);
            viewHolder.progress.setProgress(Integer.parseInt(subjectsArrayList.get(position).get("progress")));

            /** progress based on current subject usage time from total time of all subjects  */

        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return subjectsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewSubject;
        protected TextView textViewTime;
        protected LinearLayout linear_layout_back;
        protected ImageView subjectImageView;
        RoundedHorizontalProgressBar progress;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewSubject = holderView.findViewById(R.id.textViewSubject);
            textViewTime = holderView.findViewById(R.id.textViewTime);
            subjectImageView = holderView.findViewById(R.id.subject_icon);
            progress = holderView.findViewById(R.id.progress);

        }

        @Override
        public void onClick(View view) {

        }
    }

}