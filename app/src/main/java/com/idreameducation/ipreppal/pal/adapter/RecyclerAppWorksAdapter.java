package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.AppWorksModel;
import com.idreameducation.ipreppal.util.Util;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class RecyclerAppWorksAdapter extends RecyclerView.Adapter<RecyclerAppWorksAdapter.RecViewHolder>  {

    private ArrayList<AppWorksModel> data;
    private Context context;

    public RecyclerAppWorksAdapter(ArrayList<AppWorksModel> recyclerDataArrayList, Context context) {
        this.data = recyclerDataArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iprep_app_works_item, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecViewHolder holder, int position) {
        AppWorksModel appWorksModel = data.get(position);
        holder.bind(appWorksModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                boolean expanded = appWorksModel.isExpanded();
                appWorksModel.setExpanded(!expanded);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return data.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView text;
        private ImageView image;
        private ImageView arrowImage;
        private LinearLayout rootLayout;

        public RecViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            arrowImage = itemView.findViewById(R.id.arrowImage);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }

        private void bind(AppWorksModel appWorksModel) {
            boolean expanded = appWorksModel.isExpanded();
            text.setVisibility(expanded ? View.VISIBLE : View.GONE);
            if(expanded){
                arrowImage.setImageResource(R.mipmap.app_works_up_arrow);
            }else{
                arrowImage.setImageResource(R.mipmap.app_works_down_arrow);
            }
            title.setText(appWorksModel.getTitle());
            text.setText(appWorksModel.getText());
            image.setImageResource(appWorksModel.getImage());
        }
    }
}
