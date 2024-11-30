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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class BoardsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private ArrayList<HashMap<String, String>> boardsArrayList;
    private Context context;
    private RequestOptions requestOptions;

    public BoardsAdapter(Context context, ArrayList<HashMap<String, String>> boardsArrayList) {
        this.context = context;
        this.boardsArrayList = boardsArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_boards, viewGroup, false);
            return new BoardsAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof BoardsAdapter.ViewItem) {
            final BoardsAdapter.ViewItem viewHolder = (BoardsAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewBoard.setText(boardsArrayList.get(position).get("name"));
            if (boardsArrayList.get(position).get("selected").equalsIgnoreCase("true")) {
                viewHolder.linear_layout_back.setBackgroundResource(R.drawable.blue_iprep);
                viewHolder.textViewBoard.setTextColor(Color.parseColor("#212121"));

            } else {
                viewHolder.textViewBoard.setTextColor(Color.parseColor("#666666"));
                viewHolder.linear_layout_back.setBackgroundResource(R.drawable.gray_iprep);
            }



            /* Set Image on ImageView  using glide */
            viewHolder.shimmer_view_container.startShimmerAnimation();
            viewHolder.boardImageView.setVisibility(View.GONE);
            Glide.with(context)
                    .load(boardsArrayList.get(position).get("icon")).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.boardImageView.setVisibility(View.VISIBLE);

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    viewHolder.shimmer_view_container.stopShimmerAnimation();
                    viewHolder.shimmer_view_container.setVisibility(View.GONE);
                    viewHolder.boardImageView.setVisibility(View.VISIBLE);
                    viewHolder.boardImageView.setBackgroundColor(Color.parseColor("#ffffff"));
                    return false;
                }
            }).into(viewHolder.boardImageView);

        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return boardsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewBoard;
        protected LinearLayout linear_layout_back;
        protected ImageView boardImageView;
        protected ShimmerFrameLayout shimmer_view_container;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewBoard = holderView.findViewById(R.id.textViewBoard);
            boardImageView = holderView.findViewById(R.id.boardImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}