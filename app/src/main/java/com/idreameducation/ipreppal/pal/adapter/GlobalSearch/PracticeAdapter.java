package com.idreameducation.ipreppal.pal.adapter.GlobalSearch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.CoreContent.PracticeDataModel;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.SubjectInfoModel;
import com.idreameducation.ipreppal.model.SubjectsModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalGlobalSearchActivity;
import com.idreameducation.ipreppal.pal.activity.QuizActivity;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.repository.FoundationalTopicRepository;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.holder> {

    ArrayList<PracticeDataModel> list;
    Context context;
    Global global;

    public List<FoundationalTopicModel> foundationalTopicData;
    FoundationalTopicModel foundationalTopicModel;
    private FoundationalTopicRepository foundationalTopicRepository;
    private Disposable foundationalTopicDisposable;
    Dialog dialog;
    String streek="3";
    String stopicID;
    String sstudentClass,sstreak,sincorrectStreak,sseniorClass,sseniorTopicID,sseniorTopicName,sstartDate;
    int sposition;

    public PracticeAdapter(ArrayList<PracticeDataModel> list) {
        this.list = list;
    }

    public PracticeAdapter() {}

    @NonNull
    @Override
    public PracticeAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        global = (Global) context.getApplicationContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_practice_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeAdapter.holder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameTextView.setText(list.get(position).getTName());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String completeType;
                String topicID=list.get(position).getTopicID();
                String subject=list.get(position).getSubjectID();
                String topicName=list.get(position).getTName();

                String studentClass = Util.getSelectedClass(context);
                String streakProgress = "0";
                String streak = (String) list.get(position).getStreakCount();
                String incorrectStreak = (String) list.get(position).getIncorrectStreak();
//                String seniorClass = (String) list.get(position).getNext_class();
//                String seniorTopicID = (String) list.get(position).getNext_topic_id();
//                String seniorTopicName = (String) list.get(position).getNext_chapter_name();
                Util.setTopicID(context,list.get(position).getTopicID());
                Util.setTopicNameAlt(context,list.get(position).getTName());
                Util.setSubject(context,subject.replace(" ","").toLowerCase());
                Util.setSubjectId(context,list.get(position).getSubjectID());
                Util.setSubject(context, subject);
                Util.setSubjectName(context, Util.getSubjectName(subject));
                global.setColor(Util.getColorOffline(subject));

                Util.setLevel(context,1);
                String smastery="0",stopicID=list.get(position).getTopicID();

                PalContentListingActivity.autoPlayDelay=5000;
                PalContentListingActivity.connectionLayoutDelay=5000;

                SubjectInfoModel subjectsModel = Util.getSubjectInfo(list.get(position).getSubjectID());

                PalContentListingActivity.icon=subjectsModel.getIcon();
                PalContentListingActivity.subject=subjectsModel.getId();
                PalContentListingActivity.subjectName=subjectsModel.getName();
                global.setColor(subjectsModel.getColor());

                /** Check complete Type */
                if ((PalGlobalSearchActivity.palGlobalSearchActivity.isDiagnosticTestAttempted(topicID))) {
                    if (PalGlobalSearchActivity.palGlobalSearchActivity.isPracticeCompleted(topicID)) {
                        if (!PalGlobalSearchActivity.palGlobalSearchActivity.isFinalTestAttempted(topicID)) completeType = "final";
                        else completeType = "all";
                    } else completeType = checkfoundation(topicID);
                } else completeType = "diagnostic";


                if(completeType.equals("diagnostic")) {
                    showDialogue(context,"diagnostic");
                }
                else if(completeType.equals("foundationalPractice")) {
                    Util.setTopicNameAlt(context,foundationalTopicModel.getTopicName());
                    Util.setTopicID(context,foundationalTopicModel.getTopicId());
                    streek=streak;
                    showDialogue(context,"foundationalPractice");

                }
                else {

                    PracticeScoreModel practiceScoreModel=PalGlobalSearchActivity.palGlobalSearchActivity.getPracticeScore(topicID);
                    Util.setLevel(context, 1);
                    if(practiceScoreModel!=null) {
                        smastery = practiceScoreModel.getScore();
                        stopicID = practiceScoreModel.getTopicId();
                        streakProgress = practiceScoreModel.getStreakProgress();
                        Util.setLevel(context, Integer.parseInt(practiceScoreModel.getCurrentLevel()));
                    }
                    
                    Util.setTopicID(context, stopicID);
                    global.setProgress(Integer.parseInt(smastery));
                    context.startActivity(new Intent(context, QuizActivity.class)
                            .putExtra("sClass", studentClass).putExtra("streakProgress", streakProgress).putExtra("streak", streak).putExtra("incorrectStreak", incorrectStreak).putExtra("practiceType", "same")
                            .putExtra("seniorClass", Util.getSelectedClass(context)).putExtra("seniorTopicID", topicID).putExtra("seniorTopicName", topicName).putExtra("testPercentageAchieved", "0"));

                }

            }
        });

    }

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
        else if(type.equals("practice")) {

            messageText.setText("To start the final test, you have to first complete the practice.");
            button.setText("Start Practice");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    PracticeScoreModel practiceScoreModel=PalGlobalSearchActivity.palGlobalSearchActivity.getPracticeScore(stopicID);

                    String mastery = practiceScoreModel.getScore();
                    String topicId = practiceScoreModel.getTopicId();
                    Util.setTopicID(context, topicId);
                    Util.setLevel(context, Integer.parseInt(practiceScoreModel.getCurrentLevel()));

                    String streakProgress = "0";
                    streakProgress = practiceScoreModel.getStreakProgress();
                    global.setProgress(Integer.parseInt(mastery));

                    context.startActivity(new Intent(context, QuizActivity.class).putExtra("sClass", sstudentClass).putExtra("streakProgress", streakProgress).putExtra("streak", sstreak).putExtra("incorrectStreak", sincorrectStreak).putExtra("practiceType", "same")
                            .putExtra("seniorClass", sseniorClass).putExtra("seniorTopicID", sseniorTopicID).putExtra("seniorTopicName", sseniorTopicName).putExtra("testPercentageAchieved", "0"));
                }
            });

        }
        else if(type.equals("Completeddiagnostic")) {

            messageText.setText("You already attempted Diagnostic test of this topic.\nYour last score is sent to assigner");
            button.setText("Okay");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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

    public class holder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private LinearLayout card;

        public holder(@NonNull View itemView) {
            super(itemView);

            card=itemView.findViewById(R.id.card);
            nameTextView=itemView.findViewById(R.id.nameTextView);

        }
    }
}
