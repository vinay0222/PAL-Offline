package com.idreameducation.ipreppal.pal.adapter.simulationAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.SelectableModel;
import com.idreameducation.ipreppal.pal.activity.SimulationTopicsListingActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class ChaptersAdapterSimulation extends RecyclerView.Adapter<ChaptersAdapterSimulation.holder> {

    ArrayList<SelectableModel> chapterList;
    Context context;
    OnItemClickListener clickListener;

    public ChaptersAdapterSimulation(ArrayList<SelectableModel> chapterList) {
        this.chapterList = chapterList;
    }

    public ChaptersAdapterSimulation() {
    }

    @NonNull
    @Override
    public ChaptersAdapterSimulation.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_chapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersAdapterSimulation.holder holder, int position) {
        holder.card.getBackground().setColorFilter(null);
        holder.chapterNameTextView.setText(chapterList.get(position).getName());

        if(!Util.isPortraitMode(context)) {
            if(chapterList.get(position).isSelectable()) {
                holder.card.getBackground().setColorFilter(Color.parseColor(Util.setColourVisibility(((SimulationTopicsListingActivity)context).subjectColour,20)), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView chapterNameTextView;
        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            chapterNameTextView=itemView.findViewById(R.id.chapterNameTextView);
            itemView.setOnClickListener(this);

            if(Util.isPortraitMode(context)) {
                chapterNameTextView.setTextSize(16);
            }

        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }
    }
}
