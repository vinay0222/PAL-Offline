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

import androidx.annotation.NonNull;
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
import java.util.Random;

public class StemVideoListingAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private final Context context;
    private final RequestOptions requestOptions;

    ArrayList<String> randomTime = new ArrayList<String>();

    private int[] colors;
    private final ArrayList<String> imageArrayList;

    public StemVideoListingAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        imageArrayList = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            String subjectId = Util.getSubjectId(context);
            if(Util.isOfflineMode(context))
            {
                String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_video_listing_icons/" +subjectId.toLowerCase()+"/"+subjectId.toLowerCase()+i+".png";
                File file = new File(completePath);
                Uri imageUri = Uri.fromFile(file);
                imageArrayList.add(String.valueOf(imageUri));
            }
            else
            {
                if(subjectId!=null) {
                    String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/Subject_video_lisitng_icons/" +subjectId.toLowerCase()+"/"+subjectId.toLowerCase()+i+".png";
                    imageArrayList.add(completePath);
                }
                else if(Util.getSubject(context)!=null){
                    String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/Subject_video_lisitng_icons/" +Util.getSubject(context).toLowerCase()+"/"+Util.getSubject(context).toLowerCase()+i+".png";
                    imageArrayList.add(completePath);
                }

            }




        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.iprep_row_stem_video_listings, parent, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewItem) {



            final ViewItem viewHolder = (ViewItem) holder;
            viewHolder.textViewVideoName.setText(contentArrayList.get(position).get("name"));

            viewHolder.imageViewThumbnail.setVisibility(View.GONE);
            int randomImages = new Random().nextInt(((imageArrayList.size() - 1) - 0) + 1) + 0;

            try {
                Glide.with(context).load(imageArrayList.get(randomImages)).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        viewHolder.imageViewThumbnail.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.imageViewThumbnail.setVisibility(View.VISIBLE);
                        viewHolder.imageViewThumbnail.setBackgroundColor(Color.parseColor("#ffffff"));
                        return false;
                    }
                }).into(viewHolder.imageViewThumbnail);
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

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;

        protected ImageView imageViewThumbnail;
        protected ImageView playbutton;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            playbutton = holderView.findViewById(R.id.playbutton);
            imageViewThumbnail = holderView.findViewById(R.id.imageViewThumnail);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
