package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 4/4/2017.
 */

public class PalSubjectsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> subjectsArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final LifecycleOwner lifecycleOwner;

    public PalSubjectsAdapter(Context context, ArrayList<HashMap<String, String>> subjectsArrayList, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.subjectsArrayList = subjectsArrayList;
        this.lifecycleOwner = lifecycleOwner;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_subjects, viewGroup, false);
            return new PalSubjectsAdapter.ViewItem(view);
        }
        return null;
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalSubjectsAdapter.ViewItem) {
            final PalSubjectsAdapter.ViewItem viewHolder = (PalSubjectsAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewSubject.setText(subjectsArrayList.get(position).get("name").replace(" ","\n"));

            /** Set Image on ImageView  using glide */
            if (Util.isOfflineMode(context)) {
                String iconName = subjectsArrayList.get(position).get("id");
                String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + iconName+".png";
                File file = new File(completePath);
                Uri imageUri = Uri.fromFile(file);

                try {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Glide.with(context)
                                        .load(imageUri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                                viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                                viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                                                viewHolder.subjectImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                if(subjectsArrayList.get(position).get("id").equals("science") || subjectsArrayList.get(position).get("id").equals("math") ||
                                                        subjectsArrayList.get(position).get("id").equals("englishgrammar")){
                                                    if(subjectsArrayList.get(position).get("id").equals("englishgrammar")){
                                                        if(Util.isToolTipHomeToBeShown(context)){
    //                                    ((PracticeTopicActivity)context).showFullScreenTransparentBg();
    //                                    String message = "जिन विषयों का आइकॉन है उनमें आपके सीखने के लिए PAL की कार्यक्षमता दी गयी है";
    //                                    String buttonText = "आओ सीखें";
    //                                    String skipText = "";
    //                                    Util.showTooltip(context, lifecycleOwner, ArrowOrientation.LEFT, "right", message, buttonText,
    //                                            skipText, "homeScreen", viewHolder.palTextLayout);
    //                                    Util.setToolTipHomeScreen(context, false);
                                                        }
                                                    }
                                                }
                                                return false;
                                            }
                                        }).into(viewHolder.subjectImageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, 500 );//time in milisecond
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(context).load(subjectsArrayList.get(position).get("icon")).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        viewHolder.shimmer_view_container.stopShimmerAnimation();
                                        viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                        viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.shimmer_view_container.stopShimmerAnimation();
                                        viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                        viewHolder.subjectImageView.setVisibility(View.VISIBLE);
                                        viewHolder.subjectImageView.setBackgroundColor(Color.parseColor("#ffffff"));

                                        return false;
                                    }
                                }).into(viewHolder.subjectImageView);
                    }
                },200);
            }

            viewHolder.shimmer_view_container.startShimmerAnimation();

        }
    }

    @Override
    public int getItemViewType(int position) {return TYPE_ITEM;}

    @Override
    public int getItemCount() {
        return subjectsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {this.clickListener = itemClickListener;}

    public interface OnItemClickListener { void onItemClick(View view, int position);}

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewSubject;
        protected LinearLayout linear_layout_back;
        protected ImageView subjectImageView;
        protected ShimmerFrameLayout shimmer_view_container;
        protected LinearLayout subjectsLayout;
        protected TextView textViewPal;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewSubject = holderView.findViewById(R.id.textViewSubject);
            subjectImageView = holderView.findViewById(R.id.subjectImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            subjectsLayout = holderView.findViewById(R.id.subjectsLayout);
            textViewPal = holderView.findViewById(R.id.textViewPal);
            textViewPal.setTypeface(null, Typeface.BOLD);
            holderView.setOnClickListener(this);
            subjectImageView.setVisibility(View.GONE);
            subjectsLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) subjectsLayout.setBackgroundResource(R.drawable.gray_iprep);
                    else subjectsLayout.setBackgroundResource(0);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }
    }

}