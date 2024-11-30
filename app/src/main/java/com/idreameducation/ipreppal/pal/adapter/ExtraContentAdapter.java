package com.idreameducation.ipreppal.pal.adapter;

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
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class ExtraContentAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    ExtraContentAdapter.OnItemClickListener clickListener;
    private final Context context;
    private final ArrayList<String> topicArrayList;
    private final ArrayList<Integer> topicLengthArrayList;
    private final ArrayList<HashMap<String, String>> stemArrayList;
    private final String type;
    private final RequestOptions requestOptions;

    public ExtraContentAdapter(Context context, ArrayList<HashMap<String, String>> stemArrayList, ArrayList<String> topicArrayList, ArrayList<Integer> topicLengthArrayList, String type) {
        this.context = context;
        this.topicLengthArrayList = topicLengthArrayList;
        this.topicArrayList = topicArrayList;
        this.stemArrayList = stemArrayList;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_extra_content, viewGroup, false);
            return new ExtraContentAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ExtraContentAdapter.ViewItem) {
            final ExtraContentAdapter.ViewItem viewHolder = (ExtraContentAdapter.ViewItem) viewHolderValue;
            if (type.equalsIgnoreCase("Videos")) {
//                String filePath = Util.getSDCardPath(context)+"/.iDream_content/Subject_Icons/"+ stemArrayList.get(position).get("name")+".png";



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(Util.isOfflineMode(context)) {
                            String filePath = Util.getSDCardPath(context)+"/.iDream_content/books_icon/"+ stemArrayList.get(position)+".png";
                            File file = new File(filePath);

                            Uri uri = null;

                            if(file.exists()){
                                uri = Uri.fromFile(file);
                            }

                            if(uri != null){
                                Glide.with(context)
                                        .load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                                viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                return false;
                                            }
                                        }).into(viewHolder.itemImageView);
                            }
                        }
                        else {
                            String filePath = "https://www.idreameducation.org/subjects_videos_books_icon/"+ stemArrayList.get(position).get("name")+".png";
                            File file = new File(filePath);

                            Uri uri = null;
                            String url = null;

                            if(file.exists()){
                                uri = Uri.fromFile(file);
                            }else{
                                url = stemArrayList.get(position).get("icon");
                            }

                            if(url == null){
                                Glide.with(context)
                                        .load(filePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                                viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                return false;
                                            }
                                        }).into(viewHolder.itemImageView);
                            }else{
                                Glide.with(context)
                                        .load(filePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                                viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                return false;
                                            }
                                        }).into(viewHolder.itemImageView);
                            }

                        }
                    }
                },200);

                viewHolder.textViewName.setText(stemArrayList.get(position).get("name"));

                //viewHolder.textViewCount.setVisibility(View.GONE);
            } else {

//                String filePath = Util.getSDCardPath(context)+"/.iDream_content/books_icon/"+ topicArrayList.get(position)+".png";
                if(Util.isOfflineMode(context))
                {
                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/books_icon/"+ topicArrayList.get(position)+".png";
                    File file = new File(filePath);

                    Uri uri = null;

                    if(file.exists()){
                        uri = Uri.fromFile(file);
                    }

                    if(uri != null){
                        Glide.with(context)
                                .load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                        viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                        return false;
                                    }
                                }).into(viewHolder.itemImageView);
                    }
                }
                else
                {
                    String filePath = "https://www.idreameducation.org/subjects_videos_books_icon/"+ stemArrayList.get(position).get("name")+".png";
                    File file = new File(filePath);

                    Uri uri = null;
                    String url = null;

                    if(file.exists()){
                        uri = Uri.fromFile(file);
                    }else{
                        url = stemArrayList.get(position).get("icon");
                    }

                    if(url == null){
                        Glide.with(context)
                                .load(filePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                        viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                        return false;
                                    }
                                }).into(viewHolder.itemImageView);
                    }else{
                        Glide.with(context)
                                .load(filePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.itemImageView.setVisibility(View.VISIBLE);
                                        viewHolder.itemImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                        return false;
                                    }
                                }).into(viewHolder.itemImageView);
                    }

                }

                viewHolder.textViewName.setText(topicArrayList.get(position));
                //viewHolder.textViewCount.setText(topicLengthArrayList.get(position) + " " + type);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (type.equalsIgnoreCase("Videos")) {
            return stemArrayList.size();
        } else {
            return topicArrayList.size();
        }


    }

    public void SetOnItemClickListener(final ExtraContentAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewName;
        protected TextView textViewCount;
        protected ImageView itemImageView;

        public ViewItem(View holderView) {
            super(holderView);
            itemImageView = holderView.findViewById(R.id.itemImageView);
            textViewName = holderView.findViewById(R.id.textViewStem);

//            textViewCount = holderView.findViewById(R.id.textViewCount);

            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }


}