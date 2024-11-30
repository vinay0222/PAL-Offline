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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class StreamsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> streamsArrayList;
    private final Context context;
    private final RequestOptions requestOptions;

    public StreamsAdapter(Context context, ArrayList<HashMap<String, String>> streamsArrayList) {
        this.context = context;
        this.streamsArrayList = streamsArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_streams, viewGroup, false);
            return new StreamsAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof StreamsAdapter.ViewItem) {
            final StreamsAdapter.ViewItem viewHolder = (StreamsAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewStream.setText(streamsArrayList.get(position).get("name"));
            if (streamsArrayList.get(position).get("selected").equalsIgnoreCase("true")) {
                viewHolder.linear_layout_back.setBackgroundResource(R.drawable.blue_iprep);
                viewHolder.selectImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.linear_layout_back.setBackgroundResource(R.drawable.gray_iprep);
                viewHolder.selectImage.setVisibility(View.GONE);
            }


            /* Set Image on ImageView  using glide */
            viewHolder.shimmer_view_container.startShimmerAnimation();
            viewHolder.streamImageView.setVisibility(View.GONE);

            if(Util.isOfflineMode(context))
            {
                String[] name=streamsArrayList.get(position).get("id").split("_");

                String icon=name[1];

                if(name.length==3) icon=name[1]+"_"+name[2];




                String filePath = Util.getSDCardPath(context) + "/.iDream_content/StreamIcons/" + icon + ".png";
                File file = new File(filePath);
                Uri uri = Uri.fromFile(file);


                Glide.with(context)
                        .load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                viewHolder.streamImageView.setVisibility(View.VISIBLE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.shimmer_view_container.stopShimmerAnimation();
                                viewHolder.shimmer_view_container.setVisibility(View.GONE);
                                viewHolder.streamImageView.setVisibility(View.VISIBLE);
                                viewHolder.streamImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                return false;
                            }
                        }).into(viewHolder.streamImageView);
            }
            else Glide.with(context).load(streamsArrayList.get(position).get("icon")).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.streamImageView.setVisibility(View.VISIBLE);

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.streamImageView.setVisibility(View.VISIBLE);
                    viewHolder.streamImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                    return false;
                }
            }).into(viewHolder.streamImageView);


        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return streamsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewStream;
        protected LinearLayout linear_layout_back;
        protected ImageView streamImageView;
        protected ImageView selectImage;
        protected ShimmerFrameLayout shimmer_view_container;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewStream = holderView.findViewById(R.id.textViewStream);
            selectImage = holderView.findViewById(R.id.select_image);
            streamImageView = holderView.findViewById(R.id.streamImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
            Util.preventTwoClick(view);
        }
    }

}