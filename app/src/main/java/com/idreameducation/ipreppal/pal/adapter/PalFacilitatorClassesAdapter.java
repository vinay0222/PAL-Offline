package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class PalFacilitatorClassesAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private final Global global;
    private String report;
    private String medium;
    private String classText;
    private final ArrayList<String> keyArrayList;
    private final Context context;
    public int selectedItem = 0;

    public PalFacilitatorClassesAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList, ArrayList<String> keyArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.report = report;
        this.medium = medium;
        this.classText = classText;
        global = (Global) context.getApplicationContext();
        this.keyArrayList = keyArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_batches, viewGroup, false);
            return new PalFacilitatorClassesAdapter.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PalFacilitatorClassesAdapter.ViewItem) {
            final PalFacilitatorClassesAdapter.ViewItem viewHolder = (PalFacilitatorClassesAdapter.ViewItem) viewHolderValue;
            String subject = contentArrayList.get(position).get("subject");
            subject = subject.substring(0, subject.length() - 1);
            subject = subject.replace("_", ", ");
            String classSuffix = "";

            String[] sc = contentArrayList.get(position).get("name").split("-")[3].split("");
            String sclass= sc[0];

            viewHolder.textViewClassName.setText("Batch Name - "+contentArrayList.get(position).get("name").split("-")[3]);
            viewHolder.textViewSubjects.setText(subject);
            viewHolder.textViewClassID.setText(sclass);
            viewHolder.textViewStudentCount.setText("");
            if(contentArrayList.get(position).get("deleted")!=null){
                viewHolder.textViewClassName.setText("Deleted Batch Name - "+contentArrayList.get(position).get("name").split("-")[3]);
            }
            if (selectedItem == position) {
                viewHolder.classLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.content_selected_state));
            }else{
                viewHolder.classLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
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

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setPosition(int position){
        this.selectedItem = position;
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewClassName;
        TextView textLanguage;
        TextView textViewReport;
        ImageView imageViewDelete;
        TextView textStudentCount;
        TextView textViewClassBorad;
        TextView textViewStudents;
        TextView textViewAssignContent;
        TextView textViewChat;
        RelativeLayout relativeLayout;
        RelativeLayout classLayout;
        TextView textViewSubjects;
        TextView textViewStudentCount;
        TextView textViewClassID;


        public ViewItem(View holderView) {
            super(holderView);
            holderView.setOnClickListener(this);
            textViewSubjects = holderView.findViewById(R.id.textViewSubjects);

            textViewReport = holderView.findViewById(R.id.textViewReport);
            imageViewDelete = holderView.findViewById(R.id.imageViewDelete);
            textViewClassID = holderView.findViewById(R.id.textViewClassID);
            textViewClassName = holderView.findViewById(R.id.textViewClassName);
            textViewStudentCount = holderView.findViewById(R.id.textViewStudentCount);
            textLanguage = holderView.findViewById(R.id.textLanguage);
            textStudentCount = holderView.findViewById(R.id.textStudentCount);
            textViewClassBorad = holderView.findViewById(R.id.textViewClassBorad);
            textViewStudents = holderView.findViewById(R.id.textViewStudents);
            textViewAssignContent = holderView.findViewById(R.id.textViewAssignContent);
            textViewChat = holderView.findViewById(R.id.textViewChat);
            relativeLayout = holderView.findViewById(R.id.relativeLayout);
            classLayout = holderView.findViewById(R.id.classLayout);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}