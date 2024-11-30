package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class StemSubjectsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> stemsubjectArrayList;
    private final Context context;
    private final RequestOptions requestOptions;

    public StemSubjectsAdapter(Context context, ArrayList<HashMap<String, String>> stemsubjectArrayList) {
        this.context = context;
        this.stemsubjectArrayList = stemsubjectArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_stem_list, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, @SuppressLint("RecyclerView") final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            viewHolder.textViewStem.setText(stemsubjectArrayList.get(position).get("name"));

            /* Set Image on ImageView  using glide */
            viewHolder.shimmer_view_container.startShimmerAnimation();
            viewHolder.stemImageView.setVisibility(View.GONE);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (Util.isOfflineMode(context))
                    {
                        String iconName = stemsubjectArrayList.get(position).get("id");
                        String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + iconName+".png";
                        File file = new File(completePath);
                        Uri imageUri = Uri.fromFile(file);

                        try {
                            Glide.with(context).load(imageUri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                    viewHolder.stemImageView.setVisibility(View.VISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                    viewHolder.stemImageView.setVisibility(View.VISIBLE);
                                    viewHolder.stemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                    return false;
                                }
                            }).into(viewHolder.stemImageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else {
                        Glide.with(context).load(stemsubjectArrayList.get(position).get("icon")).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                viewHolder.stemImageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                viewHolder.stemImageView.setVisibility(View.VISIBLE);
                                viewHolder.stemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                return false;
                            }
                        }).into(viewHolder.stemImageView);
                    }

                }
            },200);




        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stemsubjectArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewStem;
        protected LinearLayout linear_layout_back;
        protected ImageView stemImageView;
        protected ShimmerFrameLayout shimmer_view_container;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewStem = holderView.findViewById(R.id.textViewStem);
            stemImageView = holderView.findViewById(R.id.stemImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            shimmer_view_container.setVisibility(View.GONE);
            holderView.setOnClickListener(this);
            holderView.setOnFocusChangeListener(this::onFocusChange);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
            Util.preventTwoClick(view);
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
//                Animation anim = AnimationUtils.loadAnimation(context, R.anim.popupicon);
//                linear_layout_back.startAnimation(anim);
//                anim.setFillAfter(true);
                linear_layout_back.setBackgroundResource(R.drawable.green_solid_new_half_redius);
            } else {
//                Animation anim = AnimationUtils.loadAnimation(context, R.anim.popbackicon);
//                linear_layout_back.startAnimation(anim);
//                anim.setFillAfter(true);
                linear_layout_back.setBackgroundResource(R.drawable.gray_iprep);

            }
        }
    }

}