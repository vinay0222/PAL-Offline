package com.idreameducation.ipreppal.pal.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class FacilitatorListAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private ArrayList<HashMap<String, String>> contentArrayList;
    private ArrayList<String> keyArrayList;
    private Global global;
    private Context context;

    public FacilitatorListAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList, ArrayList<String> keyArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.keyArrayList = keyArrayList;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_facilitator_list, viewGroup, false);
            return new FacilitatorListAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof FacilitatorListAdapter.ViewItem) {
            final FacilitatorListAdapter.ViewItem viewHolder = (FacilitatorListAdapter.ViewItem) viewHolderValue;
            String sClass = contentArrayList.get(position).get("name").split("-")[1];
            String medium = contentArrayList.get(position).get("name").split("-")[2];
            String name = contentArrayList.get(position).get("name").split("-")[3];
            String classSuffix = "";
            switch (sClass) {
                case "1":
                    classSuffix = "st";//
                    break;
                case "2":
                    classSuffix = "nd";
                    break;
                case "3":
                    classSuffix = "rd";
                    break;
                default:
                    if (sClass.contains("11") || sClass.contains("12")) {
                        classSuffix = "";
                    } else {
                        classSuffix = "th";
                    }
                    break;
            }

            viewHolder.textViewClassName.setText("Class: " + sClass + classSuffix +" | Name: " + name);
            viewHolder.textViewClassBorad.setText(contentArrayList.get(position).get("teacherName"));
            try{
                String subject = contentArrayList.get(position).get("subject");
                subject = subject.substring(0, subject.length() - 1);
                subject = subject.replace("_", " , ");
                viewHolder.textLanguage.setText("Subject: " + subject);
            }catch (Exception e){
                e.printStackTrace();
            }

            viewHolder.textViewLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = keyArrayList.get(position);
                    String facId = contentArrayList.get(position).get("facilitatorId");
                    String name = contentArrayList.get(position).get("teacherName");
                    String subject = contentArrayList.get(position).get("subject");
                    /* Remove student from teacher list and remove teacher */
//                    Log.i("Data:" , name);
                    deleteDialog(facId, position, key, name , subject);
                }
            });
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

    private void deleteDialog(final String facID, final int position, final String key, final String facName , String subject) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete_student);
        dialog.setCancelable(false);
        TextView textViewDelete = dialog.findViewById(R.id.textViewDelete);
        TextView text = dialog.findViewById(R.id.text);
        text.setText("Are you sure you want to leave this class?");
        textViewDelete.setText("Leave");
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewCancel.setText("Cancel");
        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(contentArrayList.get(position).get("sBoard")).child(contentArrayList.get(position).get("sClass")).child(contentArrayList.get(position).get("subject")).child(ApplicationConstants.FACILITATOR).removeValue();
                global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(key).removeValue();
                global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facID).child(key +"-"+subject+"-"+ facName).child("List").child(Util.getUserId(context)).removeValue();
                dialog.dismiss();
                contentArrayList.remove(position);
                notifyDataSetChanged();


            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));


    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewClassName;
        protected TextView textLanguage;
        private TextView textViewLeave;
        private TextView textViewClassBorad;

        public ViewItem(View holderView) {
            super(holderView);
            textViewClassBorad = holderView.findViewById(R.id.textViewClassBorad);
            textViewClassName = holderView.findViewById(R.id.textViewClassName);
            textLanguage = holderView.findViewById(R.id.textLanguage);
            textViewLeave = holderView.findViewById(R.id.textViewLeave);
            holderView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}