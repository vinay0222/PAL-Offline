package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.AboutPALModel;
import com.idreameducation.ipreppal.pal.activity.AboutPalActivity;
import com.idreameducation.ipreppal.pal.activity.PalFullScreenVideoActivity;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;

import java.util.ArrayList;

public class AboutPALAdapter extends RecyclerView.Adapter<AboutPALAdapter.holder> {

    Context context;
    String pageType;
    ArrayList<AboutPALModel> models;

    public AboutPALAdapter(ArrayList<AboutPALModel> models,String pageType) {
        this.pageType = pageType;
        this.models = models;
    }

    public AboutPALAdapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context=parent.getContext();
        if(pageType.equals(AboutPalActivity.ABOUTPAL)) view= LayoutInflater.from(parent.getContext()).inflate(R.layout.about_pal_qu_view,parent,false);
        else view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pal_faq_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, @SuppressLint("RecyclerView") int position) {

        if(pageType.equals(AboutPalActivity.FAQ)) {

//            ViewGroup.LayoutParams params = holder.card.getLayoutParams();
//            params.height = 90;
//            holder.card.setLayoutParams(params);

            holder.questionText.setText(models.get(position).getQuestion());
            holder.solutionText.setText(models.get(position).getAnswerKey());

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.solutionText.getVisibility()==View.VISIBLE) {
                        holder.solutionText.setVisibility(View.GONE);
                        holder.arrorImage.setImageResource(R.mipmap.down_arrow_listing);

//                        TransitionManager.beginDelayedTransition(holder.card, new TransitionSet()
//                                .addTransition(new ChangeBounds()));

//                        ViewGroup.LayoutParams params = holder.card.getLayoutParams();
//                        params.height = 90;
//                        holder.card.setLayoutParams(params);

                    }
                    else {

                        holder.arrorImage.setImageResource(R.mipmap.up_arrow_new);
                        holder.solutionText.setVisibility(View.VISIBLE);

//                        TransitionManager.beginDelayedTransition(holder.card, new TransitionSet()
//                                .addTransition(new ChangeBounds()));

//                        ViewGroup.LayoutParams params = holder.card.getLayoutParams();
//                        params.height = 300;
//                        holder.card.setLayoutParams(params);
                    }
                }
            });
        }
        else {

            holder.questionText.setText(models.get(position).getQuestion());

            holder.videoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                    intent.putExtra("onlineLink", models.get(position).getVideoSolution());
                    intent.putExtra("videoName", "title");
                    intent.putExtra("howAppworks", true);
                    context.startActivity(intent);
                }
            });

            holder.pdfBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PalPdfViewerActivity.class);

                    intent.putExtra("onlineLink", models.get(position).getPdfSolution());
                    intent.putExtra("topic", "topic");

                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView questionText,solutionText;
        LinearLayout card;
        ImageView arrorImage;

        CardView videoBtn,pdfBtn;

        public holder(@NonNull View itemView) {
            super(itemView);
//            setIsRecyclable(false);
            if(pageType.equals(AboutPalActivity.FAQ)) {
                card= itemView.findViewById(R.id.card);
                arrorImage= itemView.findViewById(R.id.arrorImage);
                solutionText= itemView.findViewById(R.id.solutionText);
                questionText= itemView.findViewById(R.id.questionText);
            }
            else {
                videoBtn=itemView.findViewById(R.id.videoBtn);
                pdfBtn=itemView.findViewById(R.id.pdfBtn);
                questionText= itemView.findViewById(R.id.questionTextview);
            }

        }
    }
}
