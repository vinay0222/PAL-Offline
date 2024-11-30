package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassesAdapter_ extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    ClassesAdapter_.OnItemClickListener clickListener;
    private ArrayList<HashMap<String, String>> classesArrayList;
    private Context context;
    private RequestOptions requestOptions;
    private static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st" };

    public ClassesAdapter_(Context context, ArrayList<HashMap<String, String>>  classesArrayList) {
        this.context = context;
        this.classesArrayList = classesArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_classes_, viewGroup, false);
            return new ClassesAdapter_.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ClassesAdapter_.ViewItem) {
            final ClassesAdapter_.ViewItem viewHolder = (ClassesAdapter_.ViewItem) viewHolderValue;
            viewHolder.textViewClass.setText(classesArrayList.get(position).get("name") );
            if (classesArrayList.get(position).get("selected").equalsIgnoreCase("true")) {
                viewHolder.textViewClass.setBackgroundResource(R.drawable.blue_iprep);
                viewHolder.textViewClass.setTextColor(Color.parseColor("#212121"));
            } else {
                viewHolder.textViewClass.setTextColor(Color.parseColor("#666666"));
                viewHolder.textViewClass.setBackgroundResource(R.drawable.gray_iprep);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return classesArrayList.size();
    }

    public void SetOnItemClickListener(final ClassesAdapter_.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewClass;

        public ViewItem(View holderView) {
            super(holderView);
            textViewClass = holderView.findViewById(R.id.textViewClass);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }


}