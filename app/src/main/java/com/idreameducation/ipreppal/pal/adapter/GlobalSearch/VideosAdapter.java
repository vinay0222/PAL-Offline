package com.idreameducation.ipreppal.pal.adapter.GlobalSearch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.CoreContent.VideoDataModel;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.SubjectInfoModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalFullScreenVideoActivity;
import com.idreameducation.ipreppal.pal.activity.PalGlobalSearchActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.pal.fragments.BatchesFragments;
import com.idreameducation.ipreppal.pal.fragments.PalAssignedFragment;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.holder> {

    Context context;

    Global global;

    ArrayList<VideoDataModel> list;

    String streek="3";

    RequestOptions requestOptions = new RequestOptions();

    public VideosAdapter(ArrayList<VideoDataModel> list) {
        this.list = list;
        requestOptions = requestOptions.transforms(new FitCenter());
    }

    public VideosAdapter() {}

    @NonNull
    @Override
    public VideosAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        global = (Global) context.getApplicationContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_video_layout,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.holder holder, @SuppressLint("RecyclerView") int position) {

        holder.videoNameTextView.setText(list.get(position).getName());

        int randomImages = new Random().nextInt(((9 - 1) - 0) + 1) + 0;

        String imagePath;

        if(Util.isOfflineMode(context)) {
            String completePath = Util.getSDCardPath(context) + "/.iDream_content/Subject_video_listing_icons/" + list.get(position).getSubjectID() + "/" + list.get(position).getSubjectID() + randomImages + ".png";
            File file = new File(completePath);
            Uri imageUri = Uri.fromFile(file);
            imagePath= String.valueOf(imageUri);
        }
        else imagePath = "https://www.idreameducation.org/subjects_videos_books_icon/Subject_video_lisitng_icons/" +list.get(position).getSubjectID()+"/"+list.get(position).getSubjectID()+randomImages+".png";


        Glide.with(context).load(imagePath).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.playbutton.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (list.get(position).getSubjectID()) {
                            case "accountancy" : holder.playbutton.setImageResource(R.drawable.accountancy1); break;
                            case "biology" : holder.playbutton.setImageResource(R.drawable.biology1); break;
                            case "business_studies" : holder.playbutton.setImageResource(R.drawable.business_studies1); break;
                            case "chemistry" : holder.playbutton.setImageResource(R.drawable.chemistry1); break;
                            case "civics" : holder.playbutton.setImageResource(R.drawable.civics1); break;
                            case "computer_science" : holder.playbutton.setImageResource(R.drawable.computer_science1); break;
                            case "economics" : holder.playbutton.setImageResource(R.drawable.economics1); break;
                            case "english" : holder.playbutton.setImageResource(R.drawable.english1); break;
                            case "english_grammar" : holder.playbutton.setImageResource(R.drawable.english_grammar1); break;
                            case "english_literature" : holder.playbutton.setImageResource(R.drawable.english_literature1); break;
                            case "geography" : holder.playbutton.setImageResource(R.drawable.geography1); break;
                            case "sociology" : holder.playbutton.setImageResource(R.drawable.sociology1); break;
                            case "social_science" : holder.playbutton.setImageResource(R.drawable.social_science1); break;
                            case "science" : holder.playbutton.setImageResource(R.drawable.science1); break;
                            case "sanskrit" : holder.playbutton.setImageResource(R.drawable.sanskrit1); break;
                            case "psychology" : holder.playbutton.setImageResource(R.drawable.psychology1); break;
                            case "physics" : holder.playbutton.setImageResource(R.drawable.physics1); break;
                            case "math" : holder.playbutton.setImageResource(R.drawable.math1); break;
                            default: holder.playbutton.setImageResource(R.drawable.sociology1);
                        }
                    }
                },200);

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.playbutton.setVisibility(View.VISIBLE);
                holder.playbutton.setBackgroundColor(Color.parseColor("#ffffff"));
                return false;
            }
        }).into(holder.playbutton);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String completeType="";

                /** Check complete Type */
                if ((PalGlobalSearchActivity.palGlobalSearchActivity.isDiagnosticTestAttempted(list.get(position).getAssessmentTopicID()))) {
                    if (PalGlobalSearchActivity.palGlobalSearchActivity.isPracticeCompleted(list.get(position).getAssessmentTopicID())) {
                        if (!PalGlobalSearchActivity.palGlobalSearchActivity.isFinalTestAttempted(list.get(position).getAssessmentTopicID())) completeType = "final";
                        else completeType = "all";
                    } else completeType = checkfoundation(list.get(position).getAssessmentTopicID());
                } else completeType = "diagnostic";

                SubjectInfoModel subjectsModel = Util.getSubjectInfo(list.get(position).getSubjectID());

                PalContentListingActivity.icon=subjectsModel.getIcon();
                PalContentListingActivity.subject=subjectsModel.getId();
                PalContentListingActivity.subjectName=subjectsModel.getName();
                global.setColor(subjectsModel.getColor());
                
                if(completeType.equals("diagnostic")) {
                    Util.setTopicID(context,list.get(position).getAssessmentTopicID());
                    Util.setTopicNameAlt(context,list.get(position).getTopicName());
                    Util.setSubject(context, list.get(position).getSubjectID().replace(" ","").toLowerCase());
                    Util.setSubjectId(context, list.get(position).getSubjectID().replace(" ","").toLowerCase());
                    Util.setSubjectName(context,list.get(position).getSubjectID());
                    showDialogue(context,"diagnosticVideo");
                }
                else if(completeType.equals("foundationalPractice")) {
                    Util.setTopicNameAlt(context,foundationalTopicModel.getTopicName());
                    Util.setTopicID(context,foundationalTopicModel.getTopicId());
                    Util.setSubjectId(context, list.get(position).getSubjectID().replace(" ","").toLowerCase());
                    streek=foundationalTopicModel.getStreakCount();
                    showDialogue(context,"foundationalPractice");
                }
                else {
                    Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                    intent.putExtra("vidId", list.get(position).getOnlineLink().replace("https://vimeo.com/", ""));
                    intent.putExtra("videoName", list.get(position).getName());
                    intent.putExtra("videoId", list.get(position).getAssessmentTopicID());
                    intent.putExtra("category_name", list.get(position).getTopicName());
                    intent.putExtra("subject", list.get(position).getSubjectID());
                    intent.putExtra("topicID", list.get(position).getAssessmentTopicID());
                    intent.putExtra("offlineLink", list.get(position).getOfflineLink());
                    intent.putExtra("videoID_ForReports", list.get(position).getKey());
                    intent.putExtra("from", "search");
                    Util.setSubject(context, list.get(position).getSubjectID());
                    Util.setSubjectId(context, list.get(position).getSubjectID());
                    Util.setSubjectName(context, list.get(position).getSubjectID());
                    Util.setTopicNameAlt(context, list.get(position).getTopicName());
                    context.startActivity(intent);
                }
            }
        });

    }


    public List<FoundationalTopicModel> foundationalTopicData;
    FoundationalTopicModel foundationalTopicModel;
    Dialog dialog;

    public String checkfoundation(String lastTopicId) {

        String completeType="practice";
        List<FoundationalTopicModel> foundationalTopicData = PalGlobalSearchActivity.palGlobalSearchActivity.foundationalTopicData;

        if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
            for (FoundationalTopicModel item : foundationalTopicData) {
                if (lastTopicId.equals(item.getSeniorTopicID())) {
                    if (!PalGlobalSearchActivity.palGlobalSearchActivity.isPracticeCompleted(item.getTopicId())) {
                        int position = 0;
                        foundationalTopicModel=item;
                        completeType = "foundationalPractice";
//                        return completeType;
                    }
                }
            }
        }

        return completeType;
    }

    private void showDialogue(Context context,String type) {

        if(dialog!=null) if(dialog.isShowing()) dialog.dismiss();

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView messageText = dialog.findViewById(R.id.messageText);
        Button button = dialog.findViewById(R.id.btn);


        if(type.equals("diagnostic")) {

            messageText.setText("To start the practice, you have to first complete the diagnostic test. ");
            button.setText("Start Diagnostic Test");


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", Util.getSelectedClass(context));
                    context.startActivity(intent);

                }
            });

        }
        if(type.equals("diagnosticVideo")) {

            messageText.setText("You have to complete the diagnostic test for play this video");
            button.setText("Start Diagnostic Test");


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", Util.getSelectedClass(context));
                    context.startActivity(intent);

                }
            });
        }
        else if(type.equals("foundationalPractice")) {

            messageText.setText("To start the practice, you have to first complete the foundation practice. ");
            button.setText("Start Foundation practice");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    PracticeScoreModel practiceScoreModel=PalGlobalSearchActivity.palGlobalSearchActivity.getPracticeScore(foundationalTopicModel.getTopicId());
                    String mastery="0";
                    String topicId=foundationalTopicModel.getTopicId();
                    Util.setLevel(context, 1);
                    String streakProgress1="0";

                    if(practiceScoreModel!=null)
                    {
                        mastery = practiceScoreModel.getScore();
                        topicId = practiceScoreModel.getTopicId();
                        Util.setLevel(context, Integer.parseInt(practiceScoreModel.getCurrentLevel()));
                        streakProgress1 = practiceScoreModel.getStreakProgress();
                    }

                    Util.setTopicID(context, topicId);
                    global.setProgress(Integer.parseInt(mastery));
                    context.startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", foundationalTopicModel.getSClass()).putExtra("streakProgress", streakProgress1).putExtra("streak", streek).putExtra("incorrectStreak", foundationalTopicModel.getIncorrectStreak())
                            .putExtra("practiceType", foundationalTopicModel.getPracticeType()).putExtra("seniorClass", foundationalTopicModel.getSeniorClass())
                            .putExtra("seniorTopicID", foundationalTopicModel.getSeniorTopicID()).putExtra("seniorTopicName", foundationalTopicModel.getSeniorTopicName())
                            .putExtra("testPercentageAchieved", "0"));


                }
            });

        }


        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialog.show();
        /* Change the background color of the dialog */
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder{

        TextView videoNameTextView;
        ImageView playbutton;

        LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);

            videoNameTextView= itemView.findViewById(R.id.videoNameTextView);
            playbutton= itemView.findViewById(R.id.playbutton);
            card= itemView.findViewById(R.id.card);

        }
    }
}
