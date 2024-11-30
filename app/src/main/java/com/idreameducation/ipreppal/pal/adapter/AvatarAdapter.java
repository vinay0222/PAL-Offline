package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.AvatarModel;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.holder>  {

    ArrayList<AvatarModel> avatarModelArrayList;
    Context context;
    private RequestOptions requestOptions;
    public AvatarAdapter(ArrayList<AvatarModel> avatarModelArrayList) {
        this.avatarModelArrayList = avatarModelArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public AvatarAdapter() {
    }

    @NonNull
    @Override
    public AvatarAdapter.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.avatarview, viewGroup, false);
        context= viewGroup.getContext();
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.holder holder, int position) {

        if(!Util.isOfflineMode(context)) Glide.with(context).load(avatarModelArrayList.get(position).getLink()).into(holder.imageview);
        else {
            String filePath = Util.getSDCardPath(context)+"/.iDream_content/avatar/"+ avatarModelArrayList.get(position).getId()+".png";
            File file = new File(filePath);

            Uri uri = null;

            if(file.exists()){
                uri = Uri.fromFile(file);
            }

            if(uri != null){
                Glide.with(context)
                        .load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                holder.imageview.setVisibility(View.VISIBLE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.imageview.setVisibility(View.VISIBLE);
                                holder.imageview.setBackgroundColor(Color.parseColor("#ffffff"));
                                return false;
                            }
                        }).into(holder.imageview);
            }
        }


    }

    AvatarAdapter.OnItemClickListener clickListener;

    @Override
    public int getItemCount() {
        return avatarModelArrayList.size();
    }

    public void SetOnItemClickListener(final AvatarAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageview;

        public holder(@NonNull View itemView) {
            super(itemView);

            imageview=itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }
}
