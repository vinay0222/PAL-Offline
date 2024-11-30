package com.idreameducation.ipreppal.pal.adapter.simulationAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.simulationModel.SimulationSubjectModel;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;

public class SimulationSubjectsAdapter extends RecyclerView.Adapter<SimulationSubjectsAdapter.holder> {

    ArrayList<SimulationSubjectModel> subjectList;
    Context context;
    SimulationSubjectsAdapter.OnItemClickListener clickListener;
    public SimulationSubjectsAdapter(ArrayList<SimulationSubjectModel> subjectList) {
        this.subjectList = subjectList;
    }

    public SimulationSubjectsAdapter() {
    }

    @NonNull
    @Override
    public SimulationSubjectsAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext() ;
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_subject_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimulationSubjectsAdapter.holder holder, int position) {
        holder.subjectNameTextView.setText(Util.capitalStringFirstLetter(subjectList.get(position).getSubjectName()));

        Glide.with(context).load(subjectList.get(position).getIcon()).into(holder.subjectIcon);

        String imagePath;

        if(Util.isOfflineMode(context)) {

            String iconName = subjectList.get(position).getId();
            String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_Icons/" + iconName+".png";

            File file = new File(completePath);
            Uri imageUri = Uri.fromFile(file);
            imagePath= String.valueOf(imageUri);
        }
        else imagePath = subjectList.get(position).getIcon();


        Glide.with(context).load(imagePath).transition(DrawableTransitionOptions.withCrossFade()).into(holder.subjectIcon);

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public void SetOnItemClickListener(final SimulationSubjectsAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView subjectNameTextView;
        ImageView subjectIcon;

        public holder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView=itemView.findViewById(R.id.subjectNameTextView);
            subjectIcon=itemView.findViewById(R.id.subjectIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }
    }
}
