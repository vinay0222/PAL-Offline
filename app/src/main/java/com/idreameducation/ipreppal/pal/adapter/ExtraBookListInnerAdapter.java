package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ExtraBookListInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    ExtraBookListInnerAdapter.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private final Context context;
    private String type;
    private final RequestOptions requestOptions;

    private final ArrayList<String> imageArrayList;

    public ExtraBookListInnerAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        imageArrayList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {

            if(Util.isOfflineMode(context))
            {
                String completePath = Util.getSDCardPath(context) + "/.iDream_content/books_icon/books_stories_"+i+".png";
                File file = new File(completePath);
                Uri imageUri = Uri.fromFile(file);
                imageArrayList.add(String.valueOf(imageUri));
            }
            else
            {
                String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/books_listing_icon/book"+i+".png";
                imageArrayList.add(completePath);

            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_books_inner_adapter, viewGroup, false);
            return new ExtraBookListInnerAdapter.ViewItem(view);
        }
        return null;
    }

    private final int icons = 1;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ExtraBookListInnerAdapter.ViewItem) {
            final ExtraBookListInnerAdapter.ViewItem viewHolder = (ExtraBookListInnerAdapter.ViewItem) viewHolderValue;

            String name = contentArrayList.get(position).get("name");
            viewHolder.textViewBooksName.setText(name);
            int randomImages = new Random().nextInt(((imageArrayList.size() - 1) - 0) + 1) + 0;
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));
            try {
                Glide.with(context).load(imageArrayList.get(randomImages)).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.playbutton.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.playbutton.setVisibility(View.VISIBLE);
                        viewHolder.playbutton.setBackgroundColor(Color.parseColor("#ffffff"));
                        return false;
                    }
                }).into(viewHolder.playbutton);
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
        return contentArrayList.size();
    }

    public void SetOnItemClickListener(final ExtraBookListInnerAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewBooksName;
        protected ImageView playbutton;

        public ViewItem(View holderView) {
            super(holderView);
            textViewBooksName = holderView.findViewById(R.id.textViewBooksName);
            playbutton = holderView.findViewById(R.id.imageViewThumnail);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }



}
