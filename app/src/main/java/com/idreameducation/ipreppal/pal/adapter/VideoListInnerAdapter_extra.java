package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.RoundedHorizontalProgressBar;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class VideoListInnerAdapter_extra extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    VideoListInnerAdapter_extra.OnItemClickListener clickListener;
    private HashMap<String, Object> contentArrayList;
    private ArrayList<String> keyArrayList;
    private Context context;
    private RequestOptions requestOptions;
    private HashMap<String, Object> dataMap;

    private ArrayList<String> imageArrayList;

    public VideoListInnerAdapter_extra(Context context, HashMap<String, Object> contentArrayList, ArrayList<String> keyArrayList, HashMap<String, Object> dataMap) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.keyArrayList = keyArrayList;
        this.dataMap = dataMap;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        imageArrayList = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            String subjectId = Util.getSubject(context);
            String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/Subject_video_lisitng_icons/" +subjectId+"/"+subjectId+i+".png";
            imageArrayList.add(completePath);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_inner_video, viewGroup, false);
            return new VideoListInnerAdapter_extra.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof VideoListInnerAdapter_extra.ViewItem) {
            final VideoListInnerAdapter_extra.ViewItem viewHolder = (VideoListInnerAdapter_extra.ViewItem) viewHolderValue;
            HashMap<String, String> innerMap = null;

            String key = keyArrayList.get(position);
            innerMap = (HashMap<String, String>) contentArrayList.get(key);
            //innerMap.put("isSelected","false");



            try {
                String completeUrl = innerMap.get("onlineLink");
                String offlineLink = innerMap.get("offlineLink");
                String videoKey = innerMap.get("key");
                String url;
                String filePath = Util.getSDCardPath(context)+"/.iDream_content/multimedia/"+offlineLink;
                File file = new File(filePath);
                if(file.exists() && Util.isOfflineMode(context)){
                    String filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
                    url = filename.substring(0, filename.lastIndexOf("."));
                }else{
                    String urlArray[] = completeUrl.split("/");
                    url = urlArray[urlArray.length - 1];
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.textViewVideoName.setText(innerMap.get("name"));

            try {

                int randomImages = new Random().nextInt(((imageArrayList.size() - 1) - 0) + 1) + 0;
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(20));
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
        return keyArrayList.size();
    }

    public void SetOnItemClickListener(final VideoListInnerAdapter_extra.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;
        protected ImageView playbutton;
        protected RelativeLayout reletiveTop;
        protected RoundedHorizontalProgressBar progress;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            playbutton = holderView.findViewById(R.id.imageViewThumnail);
            reletiveTop = holderView.findViewById(R.id.reletiveTop);

            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}