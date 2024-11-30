package com.idreameducation.ipreppal.pal.adapter.loginPage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.StudentInfoModel;
import com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class StudentProfileAdapter extends RecyclerView.Adapter<StudentProfileAdapter.holder>{

    ArrayList<StudentInfoModel> studentInfo;

    private OnItemClickListener clickListener;
    Context context;

    public StudentProfileAdapter(ArrayList<StudentInfoModel> studentInfo) {
        this.studentInfo = studentInfo;
    }

    public StudentProfileAdapter() {
    }

    @NonNull
    @Override
    public StudentProfileAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_profile_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.userNameText.setText(studentInfo.get(position).getUserName().replace("_"," "));
        holder.classTextView.setText("Class "+studentInfo.get(position).getUserClass().replace("nonmedical_medical","Non-med").replace("_"," "));
        holder.rollNoTextVew.setText("RollNo : "+studentInfo.get(position).getUserMobile());
        holder.nameIntroTextView.setText(studentInfo.get(position).getUserName().substring(0,1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.nameIntroTextView.getBackground().setTint(Color.parseColor(Util.generateRandomColor()));
        }
    }

    @Override
    public int getItemCount() {
        return studentInfo.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userNameText,classTextView,rollNoTextVew,nameIntroTextView;

        public holder(@NonNull View itemView) {
            super(itemView);

            userNameText=itemView.findViewById(R.id.userNameText);
            classTextView=itemView.findViewById(R.id.classTextView);
            rollNoTextVew=itemView.findViewById(R.id.rollNoTextVew);
            nameIntroTextView=itemView.findViewById(R.id.nameIntroTextView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
