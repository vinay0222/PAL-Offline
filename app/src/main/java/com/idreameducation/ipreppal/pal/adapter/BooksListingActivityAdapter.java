package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksListingActivityAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    BooksListingActivityAdapter.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, Object>> booksArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final int positionSelected;

    public BooksListingActivityAdapter(Context context, ArrayList<HashMap<String, Object>> booksArrayList, int positionSelected) {
        this.context = context;
        this.booksArrayList = booksArrayList;
        this.positionSelected = positionSelected;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.iprep_row_books_list2, parent, false);
            return new BooksListingActivityAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BooksListingActivityAdapter.ViewItem) {
            final BooksListingActivityAdapter.ViewItem viewHolder = (BooksListingActivityAdapter.ViewItem) holder;

            if (booksArrayList.get(position).get("selected").equals("true")) {
                viewHolder.linear_layout_back.setBackgroundResource(R.color.lightbule);
            } else {
                viewHolder.linear_layout_back.setBackgroundResource(R.color.white);
            }

            viewHolder.textViewBook.setText((String)booksArrayList.get(position).get("name"));
            ArrayList<HashMap<String, String>> topicsArrayList = (ArrayList<HashMap<String, String>>) booksArrayList.get(position).get(ApplicationConstants.TOPICS);
            viewHolder.textViewBookCount.setText(topicsArrayList.size() + " Books");




            String booksName = (String) booksArrayList.get(position).get("name");
            //String iconLoad = Util.getSDCardPath(context)+"/.iDream_content/books_icon/"+booksName+".png";
            String iconLoad = "https://www.idreameducation.org/subjects_videos_books_icon/"+booksName+".png";


            viewHolder.shimmer_view_container.startShimmerAnimation();
            viewHolder.bookImageView.setVisibility(View.GONE);
            Glide.with(context)
                    .load(iconLoad).transition(DrawableTransitionOptions.withCrossFade()).
                    apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.bookImageView.setVisibility(View.VISIBLE);

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.bookImageView.setVisibility(View.VISIBLE);
                    viewHolder.bookImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                    return false;
                }
            }).into(viewHolder.bookImageView);

        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public void SetOnItemClickListener(final BooksListingActivityAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewBook;
        protected TextView textViewBookCount;
        private final ImageView bookImageView;
        protected ShimmerFrameLayout shimmer_view_container;
        protected LinearLayout linear_layout_back;

        public ViewItem(View holderView) {
            super(holderView);
            textViewBook = holderView.findViewById(R.id.textViewBook);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewBookCount = holderView.findViewById(R.id.textViewBookCount);
            bookImageView = holderView.findViewById(R.id.bookImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }


    }
}