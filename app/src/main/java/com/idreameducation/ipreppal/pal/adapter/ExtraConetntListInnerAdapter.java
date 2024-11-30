package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.RoundedHorizontalProgressBar;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sony on 4/4/2017.
 */

public class ExtraConetntListInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private final Context context;
    private final String type;
    private final RequestOptions requestOptions;

    public ExtraConetntListInnerAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList, String type) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_extra_content_inner, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    private int icons = 1;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;

            String name = contentArrayList.get(position).get("name");
            if (type.equalsIgnoreCase("books")) {

                if (position > (position % 6)) {
                    icons = 1;
                }

                try {
                    String filePath = Util.getSDCardPath(context) + "/.iDream_content/books_icon/books_stories_" + icons + ".png";
                    File file = new File(filePath);
                    Uri uri = Uri.fromFile(file);
                    Glide.with(context).load(uri).into(viewHolder.playbutton);

                    icons++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                RequestOptions options = new RequestOptions()
//                        .placeholder(R.mipmap.rounded_video_new)
//                        .error(R.mipmap.rounded_video_new);
//                Glide.with(context).load(R.mipmap.rounded_video_new).apply(options).into(viewHolder.playbutton);


            } else {
                String filePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_video_lisitng_icons/" + Util.getSubject(context) + "/" + Util.getSubject(context) + "1.png";
                File file = new File(filePath);
                if(Util.isOfflineMode(context) && file.exists()){
                    Uri uri = Uri.fromFile(file);
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.rounded_video_new)
                            .error(R.mipmap.rounded_video_new);
                    Glide.with(context).load(uri).apply(options).into(viewHolder.playbutton);
                }else{
                    String code = getYouTubeId(contentArrayList.get(position).get("onlineLink"));
                    String icon = "https://img.youtube.com/vi/" + code + "/0.jpg";
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.rounded_video_new)
                            .error(R.mipmap.rounded_video_new);
                    Glide.with(context).load(icon).apply(options).into(viewHolder.playbutton);
                }
            }
            viewHolder.textViewVideoName.setText(name);
            viewHolder.progress.setVisibility(View.GONE);
            viewHolder.progress.setProgress(10);
            viewHolder.progress.setProgressColors(Color.parseColor("#F4C999"), Color.parseColor("#FB9E36"));

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
        protected ImageView playbutton;
        protected RelativeLayout reletiveTop;
        protected TextView textViewSelected;
        protected RoundedHorizontalProgressBar progress;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            textViewSelected = holderView.findViewById(R.id.textViewSelected);
            playbutton = holderView.findViewById(R.id.imageViewThumnail);
            reletiveTop = holderView.findViewById(R.id.reletiveTop);
            progress = holderView.findViewById(R.id.progress);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }


}