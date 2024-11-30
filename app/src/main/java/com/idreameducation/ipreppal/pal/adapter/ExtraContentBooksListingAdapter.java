package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtraContentBooksListingAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
//    ExtraContentBooksListingAdapter.OnItemClickListener clickListener;
    ArrayList<HashMap<String, Object>> booksArrayList;
    private final Context context;
    private final String type;
    private final RequestOptions requestOptions;
    private int adapterSize = 1;
    private int pos = -1;

    public ExtraContentBooksListingAdapter(Context context, String type) {
        this.context = context;
        this.booksArrayList = ((ExtraContentListingActivity) context).booksArrayList;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        if(this.booksArrayList.size() > 0){
            this.adapterSize = 1;
        }else{
            this.adapterSize = 0;
        }
    }

    public ExtraContentBooksListingAdapter(Context context, String type,int posi) {
        this.context = context;
        if(Util.isPortraitMode(context)) this.booksArrayList = ((com.idreameducation.ipreppal.PalMobile.activity.ExtraContentListingActivity) context).booksArrayList;
        else this.booksArrayList = ((ExtraContentListingActivity) context).booksArrayList;
        this.type = type;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        this.pos = posi;
        try {
            if(this.booksArrayList.size() > 0){
                this.adapterSize = 1;
            }else{
                this.adapterSize = 0;
            }
        }catch (Exception rr){rr.printStackTrace();}
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_extra_books_listing, viewGroup, false);
            return new ExtraContentBooksListingAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ExtraContentBooksListingAdapter.ViewItem) {
            final ExtraContentBooksListingAdapter.ViewItem viewHolder = (ExtraContentBooksListingAdapter.ViewItem) viewHolderValue;

            ArrayList<HashMap<String, String>> bookList;
            if(pos != -1) {

                bookList = (ArrayList<HashMap<String, String>>) booksArrayList.get(pos).get(ApplicationConstants.TOPICS);

            }else{
                bookList = (ArrayList<HashMap<String, String>>) booksArrayList.get(0).get(ApplicationConstants.TOPICS);
            }

            String topicName = bookList.get(position).get("topicName");
            viewHolder.textViewBooksName.setText(topicName);

            ExtraBookListInnerAdapter extraBookListInnerAdapter = new ExtraBookListInnerAdapter(context, bookList);
            viewHolder.recyclerView_.setAdapter(extraBookListInnerAdapter);
            ArrayList<HashMap<String, String>> finalBookList = bookList;
            extraBookListInnerAdapter.SetOnItemClickListener(new ExtraBookListInnerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Util.preventTwoClick(view);
                    Intent intent = new Intent(context, PalPdfViewerActivity.class);
                    intent.putExtra("onlineLink", finalBookList.get(position).get("onlineLink"));
                    intent.putExtra("topic", finalBookList.get(position).get("onlineLink"));
                    intent.putExtra("offlineLink", finalBookList.get(position).get("offlineLink"));
                    intent.putExtra("topicName", finalBookList.get(position).get("name"));
                    intent.putExtra("name", finalBookList.get(position).get("name"));
                    intent.putExtra("subjectName", finalBookList.get(position).get("subject"));
                    intent.putExtra("bookId", finalBookList.get(position).get("id"));
                    intent.putExtra("categoryID", "books");
                    intent.putExtra("topicId", finalBookList.get(position).get("subjectID"));
                    intent.putExtra("topic_name_main", finalBookList.get(position).get("topicName"));
                    context.startActivity(intent);

                }
            });



        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return adapterSize;
    }

//    public void SetOnItemClickListener(final ExtraContentBooksListingAdapter.OnItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected RecyclerView recyclerView_;
        private final TextView textViewBooksName;

        public ViewItem(View holderView) {
            super(holderView);
            textViewBooksName = holderView.findViewById(R.id.textViewBooksName);
            recyclerView_ = holderView.findViewById(R.id.recyclerView_);
            recyclerView_.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            if(Util.isPortraitMode(context)) recyclerView_.setLayoutManager(new LinearLayoutManager(context));
                else recyclerView_.setLayoutManager(manager);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //clickListener.onItemClick(view, getPosition());
        }
    }



}
