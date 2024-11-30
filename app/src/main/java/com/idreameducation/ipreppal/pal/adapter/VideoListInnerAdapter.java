package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.util.RoundedHorizontalProgressBar;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by sony on 4/4/2017.
 */

public class VideoListInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final HashMap<String, Object> contentArrayList;
    private final ArrayList<Integer> keyArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final HashMap<String, Object> dataMap;
    String type;
    private final ArrayList<String> imageArrayList;

    public VideoListInnerAdapter(Context context, HashMap<String, Object> contentArrayList, ArrayList<Integer> keyArrayList, HashMap<String, Object> dataMap,String type) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.keyArrayList = keyArrayList;
        this.dataMap = dataMap;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        imageArrayList = new ArrayList<>();
        String subjectId = Util.getSubject(context);
        for (int i = 1; i < 12; i++) {

            if(Util.isOfflineMode(context))
            {
                String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_video_listing_icons/" +subjectId.toLowerCase()+"/"+subjectId.toLowerCase()+i+".png";
                File file = new File(completePath);
                Uri imageUri = Uri.fromFile(file);
                imageArrayList.add(completePath);
            }
            else
            {

                String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/Subject_video_lisitng_icons/" +subjectId+"/"+subjectId+i+".png";
                imageArrayList.add(completePath);
            }




        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_inner_video, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            HashMap<String, String> innerMap = null;
            viewHolder.progress.setVisibility(View.GONE);
            viewHolder.textViewSelected.setVisibility(View.GONE);
            String key = String.valueOf(keyArrayList.get(position));
            innerMap = (HashMap<String, String>) contentArrayList.get(key);

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
                    String[] urlArray = completeUrl.split("/");
                    url = urlArray[urlArray.length - 1];
                }

                if(completeUrl.contains("vimeo.com")) viewHolder.type_text.setText("iPrep");
                    else viewHolder.type_text.setText("Diksha");

                if(dataMap != null){
                    if(videoKey==null) videoKey=key;
                    HashMap<String, String> reportsMap = (HashMap<String, String>) dataMap.get(videoKey);
                    if (reportsMap != null) {
                        viewHolder.progress.setVisibility(View.VISIBLE);
                        double time = Integer.parseInt(reportsMap.get("time"));
                        double totalTime = Integer.parseInt(reportsMap.get("totalTime"));
                        double percetage = (time / totalTime) * 100;
                        viewHolder.progress.setProgress((int) percetage);
                        viewHolder.progress.setProgressColors(Color.parseColor("#d3d3d3"), Color.parseColor("#a9a9a9"));
                    }else{
                        viewHolder.progress.setVisibility(View.GONE);
                        double percetage = 0;
                        viewHolder.progress.setProgress((int) percetage);
                        viewHolder.progress.setProgressColors(Color.parseColor("#d3d3d3"), Color.parseColor("#a9a9a9"));
                    }
                }else{
                    viewHolder.progress.setVisibility(View.GONE);
                    double percetage = 0;
                    viewHolder.progress.setProgress((int) percetage);
                    viewHolder.progress.setProgressColors(Color.parseColor("#d3d3d3"), Color.parseColor("#a9a9a9"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.textViewVideoName.setText(innerMap.get("name"));
            String isSelected = innerMap.get("isSelected");
//          String completeType = ((PalContentListingActivity) context).completeType;


            // selected current item
            // new code

            if(!type.equals("foundationalTopicVideos")) {
                try
                {
                    if (PalContentListingActivity.keys.equals(key)) {
                        viewHolder.reletiveTop.setBackgroundColor(Color.parseColor("#302196F3"));
                    } else {
                        viewHolder.reletiveTop.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                    }
                }catch (Exception e){
                    PalContentListingActivity.keys="";
                }
            }
            else viewHolder.reletiveTop.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));


            int randomImages = new Random().nextInt(((imageArrayList.size() - 1) - 0) + 1) + 0;
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new FitCenter());

            RequestOptions finalRequestOptions = requestOptions;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Glide.with(context).load(imageArrayList.get(randomImages)).transition(DrawableTransitionOptions.withCrossFade()).apply(finalRequestOptions).listener(new RequestListener<Drawable>() {
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
            },500);

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

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;
        protected ImageView playbutton;
        protected RelativeLayout reletiveTop;
        protected TextView textViewSelected;
        protected TextView type_text;
        protected RoundedHorizontalProgressBar progress;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            textViewSelected = holderView.findViewById(R.id.textViewSelected);
            playbutton = holderView.findViewById(R.id.imageViewThumnail);
            reletiveTop = holderView.findViewById(R.id.reletiveTop);
            progress = holderView.findViewById(R.id.progress);
            type_text = holderView.findViewById(R.id.type_text);
            type_text.setVisibility(View.VISIBLE);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
            Util.preventTwoClick(view);
        }
    }

}
