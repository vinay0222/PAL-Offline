package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.ArrayList;

public class TopicVideoListingAdapter  extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    TopicVideoListingAdapter.OnItemClickListener clickListener;
    private ArrayList<String> contentArrayList;
    private Context context;
    private Global global;

    public TopicVideoListingAdapter(Context context, ArrayList<String> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        global = (Global) context.getApplicationContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topic_videos_listing_row, parent, false);
            return new TopicVideoListingAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopicVideoListingAdapter.ViewItem) {
            final TopicVideoListingAdapter.ViewItem viewHolder = (TopicVideoListingAdapter.ViewItem) holder;
            if (position >= 0 && position < 10) {
                viewHolder.textViewSno.setText("0" + (position + 1) + ". ");
            } else {
                viewHolder.textViewSno.setText((position + 1) + ". ");
            }
            viewHolder.textViewSno.setTextColor(Color.parseColor(global.getColor()));
            viewHolder.textViewVideoName.setText(contentArrayList.get(position));
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

    public void SetOnItemClickListener(final TopicVideoListingAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;
        protected TextView textViewSno;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            textViewSno = holderView.findViewById(R.id.textViewSno);
            holderView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
