package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idreameducation.ipreppal.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PalLanguageSelectionAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    PalLanguageSelectionAdapter.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> languageArrayList;
    private final Context context;
    private final RequestOptions requestOptions;

    public PalLanguageSelectionAdapter(Context context, ArrayList<HashMap<String, String>> languageArrayList) {
        this.context = context;
        this.languageArrayList = languageArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_language_selection_item_row, viewGroup, false);
            return new PalLanguageSelectionAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalLanguageSelectionAdapter.ViewItem) {
            final PalLanguageSelectionAdapter.ViewItem viewHolder = (PalLanguageSelectionAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewLanguage.setText(languageArrayList.get(position).get("name"));
            viewHolder.linear_layout_back.setBackgroundResource(R.drawable.gray_iprep);
            if (languageArrayList.get(position).get("selected").equalsIgnoreCase("true")) {
                viewHolder.selectImage.setVisibility(View.VISIBLE);

                if(languageArrayList.get(position).get("id").equals("english")){
                    viewHolder.linear_layout_back.setBackgroundResource(R.drawable.green_iprep);
                }else{
                    viewHolder.linear_layout_back.setBackgroundResource(R.drawable.orange_iprep);
                }

            } else {
                viewHolder.linear_layout_back.setBackgroundResource(R.drawable.gray_iprep);
                viewHolder.selectImage.setVisibility(View.GONE);
            }

            int imageResource;
            int imageResource2;
            if(languageArrayList.get(position).get("id").equals("english")){
                imageResource = R.drawable.ic_eng_icon;
                imageResource2 = R.drawable.ic_eng_tick;
                viewHolder.textViewLanguage.setText("English Medium");

            }else{
                imageResource = R.drawable.ic_hindi_icon;
                imageResource2 = R.drawable.ic_hindi_tick;
                viewHolder.textViewLanguage.setText("हिंदी भाषा");
            }

            /* Set Image on ImageView  using glide */
            viewHolder.shimmer_view_container.startShimmerAnimation();
            viewHolder.languageImageView.setImageResource(imageResource);
            viewHolder.selectImage.setImageResource(imageResource2);


        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return languageArrayList.size();
    }

    public void SetOnItemClickListener(final PalLanguageSelectionAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewLanguage;
        protected LinearLayout linear_layout_back;
        protected ImageView languageImageView;
        protected ImageView selectImage;
        protected ShimmerFrameLayout shimmer_view_container;

        public ViewItem(View holderView) {
            super(holderView);
            linear_layout_back = holderView.findViewById(R.id.linear_layout_back);
            textViewLanguage = holderView.findViewById(R.id.textViewLanguage);
            selectImage = holderView.findViewById(R.id.select_image);
            languageImageView = holderView.findViewById(R.id.languageImageView);
            shimmer_view_container = holderView.findViewById(R.id.shimmer_view_container);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}