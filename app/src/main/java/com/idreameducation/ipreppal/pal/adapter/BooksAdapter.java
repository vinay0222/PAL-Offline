package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    BooksAdapter.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, Object>> booksArrayList;
    private final Context context;
    private RequestOptions requestOptions;
    Global global;


    public BooksAdapter(Context context, ArrayList<HashMap<String, Object>> booksArrayList) {
        this.context = context;
        this.booksArrayList = booksArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));
        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_book, viewGroup, false);
            return new BooksAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof BooksAdapter.ViewItem) {
            final BooksAdapter.ViewItem viewHolder = (BooksAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewBooksName.setText((String) booksArrayList.get(position).get("name"));
            viewHolder.textViewBooksName.setVisibility(View.GONE);
            ArrayList<HashMap<String, String>> bookList = (ArrayList<HashMap<String, String>>) booksArrayList.get(position).get(ApplicationConstants.TOPICS);
            BooksInnerAdapter bookListInnerAdapter = new BooksInnerAdapter(context, bookList);
            viewHolder.textViewBooksName.setTextColor(Color.parseColor(global.getColor()));
            viewHolder.recyclerView.setAdapter(bookListInnerAdapter);
            bookListInnerAdapter.SetOnItemClickListener(new BooksInnerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Util.preventTwoClick(view);
                    Intent intent = new Intent(context, PalPdfViewerActivity.class);

                    intent.putExtra("onlineLink", bookList.get(position).get("onlineLink"));
                    intent.putExtra("topic", bookList.get(position).get("onlineLink"));
                    intent.putExtra("offlineLink", bookList.get(position).get("offlineLink"));
                    intent.putExtra("topicName", bookList.get(position).get("topicName"));
                    intent.putExtra("name", bookList.get(position).get("name"));
                    intent.putExtra("subjectName", bookList.get(position).get("subject"));
                    intent.putExtra("bookId", bookList.get(position).get("id"));
                    intent.putExtra("categoryID", "books_ncert");
                    intent.putExtra("topicId", bookList.get(position).get("subjectID"));
                    intent.putExtra("topic_name_main", bookList.get(position).get("topicName"));

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
        Log.e("Size of data:", "" +booksArrayList.size());
        return booksArrayList.size();
//        return booksArrayList.size();
    }

    public void SetOnItemClickListener(final BooksAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewBooksName;
        protected RecyclerView recyclerView;

        public ViewItem(View holderView) {
            super(holderView);
            textViewBooksName = holderView.findViewById(R.id.textViewBooksName);
            recyclerView = holderView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 2);

            if(Util.isPortraitMode(context)) recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                else recyclerView.setLayoutManager(manager);
            holderView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            clickListener.onItemClick(view, getPosition());
        }
    }

}