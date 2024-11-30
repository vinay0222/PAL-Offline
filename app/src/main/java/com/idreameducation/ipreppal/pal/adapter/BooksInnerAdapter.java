package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BooksInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    BooksInnerAdapter.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> bookArrayList;

    private ArrayList<String> imageLink=new ArrayList<>();
    private final Context context;
    private RequestOptions requestOptions;
    Global global;

    public BooksInnerAdapter(Context context, ArrayList<HashMap<String, String>> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));

        global=(Global) context.getApplicationContext();

        for(int i=0;i<=bookArrayList.size()-1;i++) {
            Random r = new Random();

            int random = r.nextInt(5 - 1) + 1;

            if(Util.isOfflineMode(context)) {
                String filePath = Util.getSDCardPath(context) + "/.iDream_content/books_icon/books_stories_" + random + ".png";
                imageLink.add(filePath);
            }
            else {
                String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/books_listing_icon/book"+random+".png";
                imageLink.add(completePath);
            }


        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_books, viewGroup, false);
            return new BooksInnerAdapter.ViewItem(view);
        }
        return null;
    }

    int icons = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof BooksInnerAdapter.ViewItem) {
            final BooksInnerAdapter.ViewItem viewHolder = (BooksInnerAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewBooksName.setText(bookArrayList.get(position).get("topicName"));
            viewHolder.textViewBooksName.setTextColor(context.getResources().getColor(R.color.textcolour));

            icons=position;

//            if (position > (position % 6)) icons = position;

            try {

                if(Util.isOfflineMode(context))
                {
                    viewHolder.textViewBooksName.setText(bookArrayList.get(position).get("topicName"));

//                    String filePath = Util.getSDCardPath(context) + "/.iDream_content/books_icon/books_stories_" + icons + ".png";
                    File file = new File(imageLink.get(position));
                    Uri uri = Uri.fromFile(file);
                    Glide.with(context).load(uri).apply(requestOptions).into(viewHolder.playbutton);
                    viewHolder.playbutton.getBackground().setTint(Color.parseColor(global.getColor()));
                }
                else
                {
//                    String completePath = "https://www.idreameducation.org/subjects_videos_books_icon/books_listing_icon/book"+icons+".png";
//                    try {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                Glide.with(context).load(imageLink.get(position)).apply(requestOptions).into(viewHolder.playbutton);
//                                viewHolder.playbutton.getBackground().setTint(Color.parseColor(global.getColor()));
//                            }
//                        },500);
//                    } catch (Exception e) {
//                        // screen is closed
//                    }
                }

            } catch (Exception e) {

            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    public void SetOnItemClickListener(final BooksInnerAdapter.OnItemClickListener itemClickListener) {
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

//            if(Util.isOfflineMode(context)) playbutton.setScaleType(ImageView.ScaleType.CENTER_CROP);

            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }


}