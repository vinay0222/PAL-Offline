package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;


public class ExtraContentTopicAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private ExtraContentTopicAdapter.OnItemClickListener clickListener;
    private final Global global;
    private final Context context;
    private final String type;
    private final ArrayList<String> topicsArrayList;
    private final ArrayList<Integer> topicLengthArrayList;
    private final RequestOptions requestOptions;

    public ExtraContentTopicAdapter(Context context, ArrayList<String> topicsArrayList, ArrayList<Integer> topicLengthArrayList, String type) {
        this.context = context;
        this.topicsArrayList = topicsArrayList;
        this.topicLengthArrayList = topicLengthArrayList;
        this.type = type;
        global = (Global) context.getApplicationContext();
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_topics, viewGroup, false);
        return new ExtraContentTopicAdapter.ViewItem(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ExtraContentTopicAdapter.ViewItem) {
            final ExtraContentTopicAdapter.ViewItem viewHolder = (ExtraContentTopicAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewTopicName.setText(topicsArrayList.get(position));
            viewHolder.downward_arrow.setVisibility(View.GONE);
            viewHolder.lockImage.setVisibility(View.GONE);
            viewHolder.verticalLine.setVisibility(View.GONE);
            viewHolder.textViewSno.setTextColor(Color.parseColor("#C9C9C9"));

            if (position >= 0 && position < 10) {
                viewHolder.textViewSno.setText("0" + (position + 1) + ". ");
            } else {
                viewHolder.textViewSno.setText((position + 1) + ". ");
            }
            if(type.equals("books")){

                viewHolder.linearTop.setBackgroundResource(R.drawable.white_background);

                try {
                    if(((ExtraContentListingActivity)context).selectedTopicName.equals(topicsArrayList.get(position))) {
                        viewHolder.linearTop.setBackgroundResource(R.color.blueTransIprep);
                    }
                }
                catch (Exception d) {

                }



                viewHolder.linearPath.setVisibility(View.GONE);
                if(topicLengthArrayList != null){
                    try
                    {
                        viewHolder.textViewTopicBooksCount.setVisibility(View.VISIBLE);
                        viewHolder.textViewTopicBooksCount.setText(topicLengthArrayList.get(position) + " Books");
                    }catch (Exception r) {
                        r.printStackTrace();
                    }
                }
                viewHolder.textViewSno.setVisibility(View.GONE);
                viewHolder.itemImageView.setVisibility(View.VISIBLE);
                String filePath = Util.getSDCardPath(context)+"/.iDream_content/books_icon/"+ topicsArrayList.get(position)+".png";
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
            }else{
                viewHolder.textViewTopicBooksCount.setVisibility(View.GONE);
                viewHolder.textViewSno.setVisibility(View.VISIBLE);
                viewHolder.itemImageView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return topicsArrayList.size();
    }


    public void SetOnItemClickListener(final ExtraContentTopicAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTopicName;
        protected TextView textViewTopicBooksCount;
        protected TextView textViewSno;
        protected ImageView lockImage;
        protected ImageView downward_arrow;
        protected TextView verticalLine;
        protected ImageView itemImageView;
        protected RelativeLayout linearPath;

        protected LinearLayout linearTop;

        public ViewItem(View holderView) {
            super(holderView);
            lockImage = holderView.findViewById(R.id.lockImage);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewTopicBooksCount = holderView.findViewById(R.id.textViewTopicBooksCount);
            textViewSno = holderView.findViewById(R.id.textViewSno);
            downward_arrow = holderView.findViewById(R.id.downward_arrow);
            verticalLine = holderView.findViewById(R.id.verticalLine);
            itemImageView = holderView.findViewById(R.id.itemImageView);
            linearPath = holderView.findViewById(R.id.linearPath);
            linearTop = holderView.findViewById(R.id.linearTop);

            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }
    }
}