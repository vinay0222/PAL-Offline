package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TestReview_Adapter extends RecyclerView.Adapter<TestReview_Adapter.holder> {

    private Context context;
    public ArrayList<HashMap<String, String>> questionsArrayList;
    private  Global global;
    public static String selectedLanguge;

    public TestReview_Adapter(ArrayList<HashMap<String, String>> questionsArrayList) {
        this.questionsArrayList = questionsArrayList;

    }

    public TestReview_Adapter() {}

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context=viewGroup.getContext();
        global = (Global) context.getApplicationContext();
        selectedLanguge=Util.getSelectedLanguage(context);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_model_paper_review, viewGroup, false);
        return new holder(view);
    }

    String Correct_Answer="Correct Answer";
    String InCorrect_Answer="InCorrect Answer";
    String feedbacktitle_string="InCorrect Answer";

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull holder viewHolder, int position) {

        try {

            viewHolder.textViewOption1.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.imageViewOption1.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.textViewOption2.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.imageViewOption2.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.textViewOption3.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.imageViewOption3.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.textViewOption4.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.imageViewOption4.setBackground(context.getResources().getDrawable(R.drawable.gray_iprep));
            viewHolder.optionTextA.setVisibility(View.GONE);
            viewHolder.optionTextB.setVisibility(View.GONE);
            viewHolder.optionTextC.setVisibility(View.GONE);
            viewHolder.optionTextD.setVisibility(View.GONE);
            viewHolder.feedbackLayout.setVisibility(View.GONE);


            if(Util.getSelectedLanguage(context).equals("hindi"))
            {
                Correct_Answer="सही उत्तर";
                InCorrect_Answer="ग़लत उत्तर";
                feedbacktitle_string="जवाब";
            }
            else
            {
                Correct_Answer="Correct Answer";
                InCorrect_Answer="InCorrect Answer";
                feedbacktitle_string="Feedback";

            }
            viewHolder.feedbacktitle.setText(feedbacktitle_string);

            if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
                viewHolder.parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDisplayMetrics().widthPixels / 3 + 150));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels / 3, 120);
                params.rightMargin = 20;
                params.leftMargin = 20;
                params.topMargin = 10;
                params.bottomMargin = 10;
                params.gravity = Gravity.CENTER;
                viewHolder.textViewSubmit.setBackgroundResource(R.drawable.blue_pressed_25);
                viewHolder.textViewSubmit.setLayoutParams(params);
            } else {
                viewHolder.parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDisplayMetrics().heightPixels / 2 + 350));
            }
            if (selectedLanguge.equalsIgnoreCase("English")) {
                viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQ"));
            } else {
                if (global.isFlagEnabled()) {
                    viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQAlt"));
                } else {
                    viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQ"));
                }
            }

            try {
                viewHolder.textViewQuestion.post(new Runnable() {
                    @Override
                    public void run() {
                        int count = viewHolder.textViewQuestion.getLineCount();
                        if (count > 3) {
                            viewHolder.textViewQuestion.setEnabled(true);
                            viewHolder.textViewReadMore.setVisibility(View.GONE);
                            viewHolder.textViewReadMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String question;
                                    if (global.isFlagEnabled()) {
                                        question = questionsArrayList.get(position).get("questionQAlt");
                                    } else {
                                        question = questionsArrayList.get(position).get("questionQ");
                                    }
                                    showDetailFeedbackDialog(question);
                                }
                            });
                            viewHolder.textViewQuestion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String question;
                                    if (global.isFlagEnabled()) {
                                        question = questionsArrayList.get(position).get("questionQAlt");
                                    } else {
                                        question = questionsArrayList.get(position).get("questionQ");
                                    }
                                    showDetailFeedbackDialog(question);
                                }
                            });
                        } else {
                            viewHolder.textViewQuestion.setEnabled(false);
                            viewHolder.textViewReadMore.setVisibility(View.GONE);
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")) {
                viewHolder.textViewOption1.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption1.setVisibility(View.GONE);
                viewHolder.linearFullscreen1.setVisibility(View.GONE);
                viewHolder.imageViewOption1FullImage.setVisibility(View.GONE);

                if (selectedLanguge.equalsIgnoreCase("English")) {
                    viewHolder.textViewOption1.setText(questionsArrayList.get(position).get("option1"));
                } else {
                    if (global.isFlagEnabled()) {
                        viewHolder.textViewOption1.setText(questionsArrayList.get(position).get("option1_alt"));
                    } else {
                        viewHolder.textViewOption1.setText(questionsArrayList.get(position).get("option1"));
                    }
                }
            } else {
                viewHolder.textViewOption1.setVisibility(View.GONE);
                viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption1FullImage.setVisibility(View.VISIBLE);
                viewHolder.linearFullscreen1.setVisibility(View.VISIBLE);
                if (Util.isOfflineMode(context)) {
                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option1"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption1.setVisibility(View.GONE);
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen1.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption1FullImage.setVisibility(View.VISIBLE);

                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option1").replace(" ","");
                    Glide.with(context).load(image).into(viewHolder.imageViewOption1);
                    viewHolder.imageViewOption1FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });
                } else {
//                    String image=Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option1");
                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option1"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption1.setVisibility(View.GONE);
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen1.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption1FullImage.setVisibility(View.VISIBLE);
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option1").replace(" ","");
                    Glide.with(context).load(image).into(viewHolder.imageViewOption1);
                    viewHolder.imageViewOption1FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });
                }
            }

            if (questionsArrayList.get(position).get("type2").equalsIgnoreCase("Text")) {
                viewHolder.textViewOption2.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption2.setVisibility(View.GONE);
                viewHolder.linearFullscreen2.setVisibility(View.GONE);
                viewHolder.imageViewOption3FullImage.setVisibility(View.GONE);

                if (selectedLanguge.equalsIgnoreCase("English")) {

                    viewHolder.textViewOption2.setText(questionsArrayList.get(position).get("option2"));
                } else {
                    if (global.isFlagEnabled()) {
                        viewHolder.textViewOption2.setText(questionsArrayList.get(position).get("option2_alt"));
                    } else {
                        viewHolder.textViewOption2.setText(questionsArrayList.get(position).get("option2"));
                    }
                }
            } else {
                viewHolder.textViewOption2.setVisibility(View.GONE);
                viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                viewHolder.linearFullscreen2.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption2FullImage.setVisibility(View.VISIBLE);
                if (Util.isOfflineMode(context)) {
                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option2"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption2.setVisibility(View.GONE);
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen2.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption2FullImage.setVisibility(View.VISIBLE);
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option2").replace(" ","");
                    Glide.with(context).load(image).into(viewHolder.imageViewOption2);

                    viewHolder.imageViewOption2FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });
//                        viewHolder.textViewOption2.setVisibility(View.GONE);
//                        viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(questionsArrayList.get(position).get("option2")).into(viewHolder.imageViewOption2);
                } else {
//                    viewHolder.textViewOption2.setVisibility(View.VISIBLE);
//                    viewHolder.imageViewOption2.setVisibility(View.GONE);
//                    viewHolder.textViewOption2.setText(imageInternet);


                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option2"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption2.setVisibility(View.GONE);
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen2.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption2FullImage.setVisibility(View.VISIBLE);
//                    String image = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option2");
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option2").replace(" ","");

                    Glide.with(context).load(image).into(viewHolder.imageViewOption2);

                    viewHolder.imageViewOption2FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });

                }


            }


            if (questionsArrayList.get(position).get("type3").equalsIgnoreCase("Text")) {
                viewHolder.textViewOption3.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption3.setVisibility(View.GONE);
                viewHolder.linearFullscreen3.setVisibility(View.GONE);
                viewHolder.imageViewOption3FullImage.setVisibility(View.GONE);

                if (selectedLanguge.equalsIgnoreCase("English")) {

                    viewHolder.textViewOption3.setText(questionsArrayList.get(position).get("option3"));
                } else {
                    if (global.isFlagEnabled()) {
                        viewHolder.textViewOption3.setText(questionsArrayList.get(position).get("option3_alt"));
                    } else {
                        viewHolder.textViewOption3.setText(questionsArrayList.get(position).get("option3"));
                    }
                }
            } else {
                viewHolder.textViewOption3.setVisibility(View.GONE);
                viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
                viewHolder.linearFullscreen3.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption3FullImage.setVisibility(View.VISIBLE);
                if (Util.isOfflineMode(context)) {
                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option3"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption3.setVisibility(View.GONE);
                    viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen3.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption3FullImage.setVisibility(View.VISIBLE);
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option3").replace(" ","");
                    Glide.with(context).load(image).into(viewHolder.imageViewOption3);

                    viewHolder.imageViewOption3FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });
//                        viewHolder.textViewOption3.setVisibility(View.GONE);
//                        viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(questionsArrayList.get(position).get("option3")).into(viewHolder.imageViewOption3);
                } else {
//                    viewHolder.textViewOption3.setVisibility(View.VISIBLE);
//                    viewHolder.imageViewOption3.setVisibility(View.GONE);
//                    viewHolder.textViewOption3.setText(imageInternet);

                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option3"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption3.setVisibility(View.GONE);
                    viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen3.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption3FullImage.setVisibility(View.VISIBLE);
//                    String image = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option3");
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option3").replace(" ","");

                    Glide.with(context).load(image).into(viewHolder.imageViewOption3);

                    viewHolder.imageViewOption3FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });

                }


            }


            if (questionsArrayList.get(position).get("type4").equalsIgnoreCase("Text")) {
                viewHolder.textViewOption4.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption4.setVisibility(View.GONE);
                viewHolder.linearFullscreen4.setVisibility(View.GONE);
                viewHolder.imageViewOption4FullImage.setVisibility(View.GONE);

                if (selectedLanguge.equalsIgnoreCase("English")) {
                    viewHolder.textViewOption4.setText(questionsArrayList.get(position).get("option4"));
                } else {
                    if (global.isFlagEnabled()) {
                        viewHolder.textViewOption4.setText(questionsArrayList.get(position).get("option4_alt"));
                    } else {
                        viewHolder.textViewOption4.setText(questionsArrayList.get(position).get("option4"));
                    }
                }
            } else {
                viewHolder.textViewOption4.setVisibility(View.GONE);
                viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
                viewHolder.linearFullscreen4.setVisibility(View.VISIBLE);
                viewHolder.imageViewOption4FullImage.setVisibility(View.VISIBLE);
                if (Util.isOfflineMode(context)) {
                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption4.setVisibility(View.GONE);
                    viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen4.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption4FullImage.setVisibility(View.VISIBLE);
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option4").replace(" ","");

                    if(file.exists()) image=Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4");

                    Glide.with(context).load(image).into(viewHolder.imageViewOption4);

                    String finalImage = image;
                    viewHolder.imageViewOption4FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4"));
                            global.setFullImage(finalImage);
                            global.setImageAdded(true);
                            Util.setImageURL(context,finalImage);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });
//                        viewHolder.textViewOption4.setVisibility(View.GONE);
//                        viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption4);
                } else {

                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4"));
                    Uri uri = Uri.fromFile(file);
                    viewHolder.textViewOption4.setVisibility(View.GONE);
                    viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
                    viewHolder.linearFullscreen4.setVisibility(View.VISIBLE);
                    viewHolder.imageViewOption4FullImage.setVisibility(View.VISIBLE);
//                    String image = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4");
                    String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option4").replace(" ","");

                    Glide.with(context).load(image).into(viewHolder.imageViewOption4);

                    viewHolder.imageViewOption4FullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("option4"));
                            global.setFullImage(image);
                            Util.setImageURL(context,image);
                            global.setImageAdded(true);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });

                }
            }


            if (questionsArrayList.get(position).get("image") != null && !TextUtils.isEmpty(questionsArrayList.get(position).get("image"))) {
                viewHolder.questionImageView.setVisibility(View.VISIBLE);
                viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                viewHolder.reletiveLayoutImage.setVisibility(View.VISIBLE);
                viewHolder.questionImageView.setEnabled(true);
                if (Util.isOfflineMode(context)) {
                    viewHolder.progressBar3.setVisibility(View.VISIBLE);
                    viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                    viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                    viewHolder.questionImageView.setVisibility(View.VISIBLE);
                    viewHolder.zoomImageView.setVisibility(View.VISIBLE);
//                    File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
//                    Uri uri = Uri.fromFile(file);
                    viewHolder.progressBar3.setVisibility(View.VISIBLE);
                    viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                    viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                    viewHolder.questionImageView.setVisibility(View.VISIBLE);
                    viewHolder.zoomImageView.setVisibility(View.VISIBLE);

                    String image;

                    if(Util.isOfflineMode(context)) image = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image");
                    else image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");

                    Glide.with(context)
                            .load(image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    viewHolder.progressBar3.setVisibility(View.GONE);
                                    viewHolder.downloadingTextView.setVisibility(View.GONE);
                                    viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);

                                    System.out.println( "----- faled");

                                    e.printStackTrace();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    viewHolder.progressBar3.setVisibility(View.GONE);
                                    viewHolder.downloadingTextView.setVisibility(View.GONE);
                                    viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);

                                    System.out.println( "----- looaded");
                                    return false;
                                }
                            })
                            .into(viewHolder.questionImageView);

                    viewHolder.slow_internet_Text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            viewHolder.progressBar3.setVisibility(View.VISIBLE);
                            viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                            viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                            viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);

                            if(Util.isOfflineMode(context)) {
                                String image = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image");
                                Glide.with(context)
                                        .load(image)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);

                                                System.out.println( "----- faled");

                                                e.printStackTrace();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);

                                                System.out.println( "----- looaded");

                                                if (Util.isOfflineMode(context))
                                                {
                                                    viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                    viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                }else {
                                                    viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(image);
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                    viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(image);
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                }

                                                return false;
                                            }
                                        })
                                        .into(viewHolder.questionImageView);
                            }
                            else {

                                String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");
//                        Glide.with(context).load(questionsArrayList.get(position).get("image")).into(viewHolder.questionImageView);
                                Glide.with(context)
                                        .load(image)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);

                                                System.out.println( "----- faled");

                                                e.printStackTrace();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);

                                                System.out.println( "----- looaded");

                                                if (Util.isOfflineMode(context))
                                                {
                                                    viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                    viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                }else {
                                                    viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(image);
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                    viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            global.setFullImage(image);
                                                            global.setImageAdded(true);
                                                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                                                        }
                                                    });
                                                }

                                                return false;
                                            }
                                        })
                                        .into(viewHolder.questionImageView);
                            }
                        }
                    });

                    viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });

                    viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            global.setFullImage(image);
                            global.setImageAdded(true);
                            Util.setImageURL(context,image);
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                    });


                } else {
//                    viewHolder.progressBar3.setVisibility(View.GONE);
//                    viewHolder.downloadingTextView.setVisibility(View.GONE);
//                    viewHolder.NoInternetConnectionTextView.setVisibility(View.VISIBLE);
//                    viewHolder.questionImageView.setVisibility(View.GONE);
//                    viewHolder.NoInternetConnectionTextView.setText(imageInternet);

                    if(Util.getSDCardPath(context)!=null) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/" + questionsArrayList.get(position).get("image"));
                        Uri uri = Uri.fromFile(file);
                        viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                        viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                        viewHolder.questionImageView.setVisibility(View.VISIBLE);
                        viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(uri)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        viewHolder.progressBar3.setVisibility(View.GONE);
                                        viewHolder.downloadingTextView.setVisibility(View.GONE);
                                        viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);
                                        viewHolder.slow_internet_Text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                                                viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                                                viewHolder.questionImageView.setVisibility(View.VISIBLE);
                                                viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                                                viewHolder.slow_internet_Text.setVisibility(View.GONE);

                                                Glide.with(context)
                                                        .load(uri)
                                                        .listener(new RequestListener<Drawable>() {
                                                            @Override
                                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
//                                                            viewHolder.slow_internet_Text.setText(slowInternetText);
                                                                viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);

                                                                viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                                                                viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                                                                viewHolder.questionImageView.setVisibility(View.VISIBLE);
                                                                viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                                                                viewHolder.slow_internet_Text.setVisibility(View.GONE);

                                                                String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");


                                                                Glide.with(context)
                                                                        .load(image)
                                                                        .listener(new RequestListener<Drawable>() {
                                                                            @Override
                                                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
//                                                                            viewHolder.slow_internet_Text.setText(slowInternetText);
                                                                                viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);
                                                                                return false;
                                                                            }

                                                                            @Override
                                                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                                                viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);
                                                                                return false;
                                                                            }
                                                                        })
                                                                        .into(viewHolder.questionImageView);

                                                                return false;
                                                            }

                                                            @Override
                                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                                viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);
                                                                return false;
                                                            }
                                                        })
                                                        .into(viewHolder.questionImageView);
                                            }
                                        });

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.progressBar3.setVisibility(View.GONE);
                                        viewHolder.downloadingTextView.setVisibility(View.GONE);
                                        viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);
                                        viewHolder.slow_internet_Text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                                                viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                                                viewHolder.questionImageView.setVisibility(View.VISIBLE);
                                                viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                                                viewHolder.slow_internet_Text.setVisibility(View.GONE);

                                                Glide.with(context)
                                                        .load(uri)
                                                        .listener(new RequestListener<Drawable>() {
                                                            @Override
                                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
//                                                            viewHolder.slow_internet_Text.setText(slowInternetText);
                                                                viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);
                                                                return false;
                                                            }

                                                            @Override
                                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                                viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);
                                                                return false;
                                                            }
                                                        })
                                                        .into(viewHolder.questionImageView);
                                            }
                                        });

                                        return false;
                                    }
                                })
                                .into(viewHolder.questionImageView);
                        viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(questionsArrayList.get(position).get("image"));
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
                        viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(questionsArrayList.get(position).get("image"));
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });

                        viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");
                                global.setFullImage(questionsArrayList.get(position).get("image"));
                                global.setImageAdded(true);
                                Util.setImageURL(context,image);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
                    }
                    else {
                        viewHolder.progressBar3.setVisibility(View.GONE);
                        viewHolder.downloadingTextView.setVisibility(View.GONE);
//                                                            viewHolder.slow_internet_Text.setText(slowInternetText);
                        viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);

                        viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                        viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                        viewHolder.questionImageView.setVisibility(View.VISIBLE);
                        viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                        viewHolder.slow_internet_Text.setVisibility(View.GONE);

                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");


                        Glide.with(context)
                                .load(image)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        viewHolder.progressBar3.setVisibility(View.GONE);
                                        viewHolder.downloadingTextView.setVisibility(View.GONE);
//                                                                            viewHolder.slow_internet_Text.setText(slowInternetText);
                                        viewHolder.slow_internet_Text.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        viewHolder.progressBar3.setVisibility(View.GONE);
                                        viewHolder.downloadingTextView.setVisibility(View.GONE);
                                        viewHolder.slow_internet_Text.setVisibility(View.INVISIBLE);
                                        return false;
                                    }
                                })
                                .into(viewHolder.questionImageView);

                        viewHolder.questionImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                Util.setImageURL(context,image);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });

                        viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                Util.setImageURL(context,image);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
                    }



                }

            } else {
                viewHolder.progressBar3.setVisibility(View.GONE);
                viewHolder.downloadingTextView.setVisibility(View.GONE);
                viewHolder.questionImageView.setVisibility(View.GONE);
                viewHolder.zoomImageView.setVisibility(View.GONE);
                viewHolder.reletiveLayoutImage.setVisibility(View.GONE);
                viewHolder.questionImageView.setEnabled(false);
                try {
                    viewHolder.questionImageView.setImageResource(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            String status=questionsArrayList.get(position).get("Status");
            String optionSelected=questionsArrayList.get(position).get("OptionSelected");
            String currectOption=questionsArrayList.get(position).get("correctOption");

            viewHolder.feedbackText.setText(questionsArrayList.get(position).get("correct_feedback"));

            /** Check Status of Question */
            if(status.equals("Correct")) {
                /** Current Question is Correct
                 * Check & mark green Selected Option */
                switch (currectOption) {
                    case "Option1": {
                        viewHolder.textViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextA.setVisibility(View.VISIBLE);
                        viewHolder.optionTextA.setText(Correct_Answer);
                        viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);

                    }break;
                    case "Option2": {
                        viewHolder.textViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.optionTextB.setVisibility(View.VISIBLE);
                        viewHolder.optionTextB.setText(Correct_Answer);
                        viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);
                    }break;
                    case "Option3": {
                        viewHolder.textViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextC.setVisibility(View.VISIBLE);
                        viewHolder.optionTextC.setText(Correct_Answer);
                        viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);
                    }break;
                    case "Option4": {
                        viewHolder.textViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextD.setVisibility(View.VISIBLE);
                        viewHolder.optionTextD.setText(Correct_Answer);
                        viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                    }break;

                    default:  break;
                }

            }
            else if(status.equals("inCorrect")) {
                /** Current Question is inCorrect */

                /** Mark Correct Option Green */
                switch (currectOption) {
                    case "Option1": {
                        viewHolder.textViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextA.setVisibility(View.VISIBLE);
                        viewHolder.optionTextA.setText(Correct_Answer);
                        viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.green2));


                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);

                    }break;
                    case "Option2": {
                        viewHolder.textViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextB.setVisibility(View.VISIBLE);
                        viewHolder.optionTextB.setText(Correct_Answer);
                        viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);
                    }break;
                    case "Option3": {
                        viewHolder.textViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextC.setVisibility(View.VISIBLE);
                        viewHolder.optionTextC.setText(Correct_Answer);
                        viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);

                    }break;
                    case "Option4": {
                        viewHolder.textViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextD.setVisibility(View.VISIBLE);
                        viewHolder.optionTextD.setText(Correct_Answer);
                        viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                    }break;

                    default:  break;
                }
                /** Mark Selected Option Red */
                switch (optionSelected) {
                    case "Option1": {
                        viewHolder.textViewOption1.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.imageViewOption1.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.optionTextA.setVisibility(View.VISIBLE);
                        viewHolder.optionTextA.setText(InCorrect_Answer);
                        viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.red2));
                    }break;
                    case "Option2": {
                        viewHolder.textViewOption2.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.imageViewOption2.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.optionTextB.setVisibility(View.VISIBLE);
                        viewHolder.optionTextB.setText(InCorrect_Answer);
                        viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.red2));
                    }break;
                    case "Option3": {
                        viewHolder.textViewOption3.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.imageViewOption3.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.optionTextC.setVisibility(View.VISIBLE);
                        viewHolder.optionTextC.setText(InCorrect_Answer);
                        viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.red2));
                    }break;
                    case "Option4": {
                        viewHolder.textViewOption4.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.imageViewOption4.setBackground(context.getResources().getDrawable(R.drawable.red_iprep_25));
                        viewHolder.optionTextD.setVisibility(View.VISIBLE);
                        viewHolder.optionTextD.setText(InCorrect_Answer);
                        viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.red2));
                    }break;

                    default:  break;
                }
//                setFeedbackText(viewHolder);
            }
            else if(status.equals("PENDING")) {
                /** Current Question is Correct
                * Check & mark green Selected Option */
                switch (currectOption) {
                    case "Option1": {
                        viewHolder.textViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption1.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextA.setVisibility(View.VISIBLE);
                        viewHolder.optionTextA.setText(Correct_Answer);
                        viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);

                    }break;
                    case "Option2": {
                        viewHolder.textViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption2.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.optionTextB.setVisibility(View.VISIBLE);
                        viewHolder.optionTextB.setText(Correct_Answer);
                        viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);
                    }break;
                    case "Option3": {
                        viewHolder.textViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption3.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextC.setVisibility(View.VISIBLE);
                        viewHolder.optionTextC.setText(Correct_Answer);
                        viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                        viewHolder.optionTextD.setVisibility(View.GONE);
                    }break;
                    case "Option4": {
                        viewHolder.textViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));
                        viewHolder.imageViewOption4.setBackground(context.getResources().getDrawable(R.drawable.green_iprep));

                        viewHolder.optionTextD.setVisibility(View.VISIBLE);
                        viewHolder.optionTextD.setText(Correct_Answer);
                        viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.green2));

                        viewHolder.optionTextB.setVisibility(View.GONE);
                        viewHolder.optionTextC.setVisibility(View.GONE);
                        viewHolder.optionTextA.setVisibility(View.GONE);
                    }break;

                    default:  break;
                }
                viewHolder.feedbackLayout.setVisibility(View.VISIBLE);
            }

            if(questionsArrayList.get(position).get("correct_feedback").replace(" ","").equals("")) viewHolder.feedbackLayout.setVisibility(View.GONE);
            else viewHolder.feedbackLayout.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDetailFeedbackDialog(String question) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_feedback_detail);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText("");
        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView textViewFeedback = dialog.findViewById(R.id.textViewFeedback);
        textViewFeedback.setText(question);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setFeedbackText(holder viewHolder) {

        /** Set Correct Answer text */
        if(viewHolder.textViewOption1.getBackground()==context.getResources().getDrawable(R.drawable.green_iprep))
        {
            viewHolder.optionTextA.setVisibility(View.VISIBLE);
            viewHolder.optionTextA.setText(Correct_Answer);
            viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.green2));


            viewHolder.optionTextB.setVisibility(View.GONE);
            viewHolder.optionTextC.setVisibility(View.GONE);
            viewHolder.optionTextD.setVisibility(View.GONE);
        }
        else if(viewHolder.textViewOption2.getBackground()==context.getResources().getDrawable(R.drawable.green_iprep))
        {
            viewHolder.optionTextB.setVisibility(View.VISIBLE);
            viewHolder.optionTextB.setText(Correct_Answer);
            viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.green2));

            viewHolder.optionTextA.setVisibility(View.GONE);
            viewHolder.optionTextC.setVisibility(View.GONE);
            viewHolder.optionTextD.setVisibility(View.GONE);
        }
        else if(viewHolder.textViewOption3.getBackground()==context.getResources().getDrawable(R.drawable.green_iprep))
        {
            viewHolder.optionTextC.setVisibility(View.VISIBLE);
            viewHolder.optionTextC.setText(Correct_Answer);
            viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.green2));

            viewHolder.optionTextB.setVisibility(View.GONE);
            viewHolder.optionTextA.setVisibility(View.GONE);
            viewHolder.optionTextD.setVisibility(View.GONE);
        }
        else if(viewHolder.textViewOption4.getBackground()==context.getResources().getDrawable(R.drawable.green_iprep))
        {
            viewHolder.optionTextD.setVisibility(View.VISIBLE);
            viewHolder.optionTextD.setText(Correct_Answer);
            viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.green2));

            viewHolder.optionTextB.setVisibility(View.GONE);
            viewHolder.optionTextC.setVisibility(View.GONE);
            viewHolder.optionTextA.setVisibility(View.GONE);
        }

        /** Set InCorrect Answer text */
        if(viewHolder.textViewOption1.getBackground().equals(context.getResources().getDrawable(R.drawable.red_iprep_25)))
        {
            viewHolder.optionTextA.setVisibility(View.VISIBLE);
            viewHolder.optionTextA.setText(InCorrect_Answer);
            viewHolder.optionTextA.setTextColor(context.getResources().getColor(R.color.red2));
        }
        else if(viewHolder.textViewOption2.getBackground().equals(context.getResources().getDrawable(R.drawable.red_iprep_25)))
        {
            viewHolder.optionTextB.setVisibility(View.VISIBLE);
            viewHolder.optionTextB.setText(InCorrect_Answer);
            viewHolder.optionTextB.setTextColor(context.getResources().getColor(R.color.red2));
        }
        else if(viewHolder.textViewOption3.getBackground().equals(context.getResources().getDrawable(R.drawable.red_iprep_25)))
        {
            viewHolder.optionTextC.setVisibility(View.VISIBLE);
            viewHolder.optionTextC.setText(InCorrect_Answer);
            viewHolder.optionTextC.setTextColor(context.getResources().getColor(R.color.red2));
        }
        else if(viewHolder.textViewOption4.getBackground().equals(context.getResources().getDrawable(R.drawable.red_iprep_25)))
        {
            viewHolder.optionTextD.setVisibility(View.VISIBLE);
            viewHolder.optionTextD.setText(InCorrect_Answer);
            viewHolder.optionTextD.setTextColor(context.getResources().getColor(R.color.red2));
        }

    }


    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private final TextView textViewQuestion;
        private final TextView slow_internet_Text;
        private final TextView textViewOption1;
        private final TextView textViewOption2;
        private final TextView textViewOption3;
        private final TextView textViewOption4;
        private final TextView textViewSubmit;
        private final LinearLayout linearLayout;
        private final RelativeLayout reletiveLayoutImage;
        private final TextView NoInternetConnectionTextView;
        private final TextView textViewReadMore;
        private final TextView textViewLastQuestionMessage;
        private final TextView downloadingTextView;
        private final ImageView questionImageView;
        private final RelativeLayout parentLayout;
        private final ImageView imageViewOption1;
        private final ImageView imageViewOption2;
        private final ImageView imageViewOption3;
        private final ImageView imageViewOption4;
        private final ProgressBar progressBar3;
        private final TextView feedbackText;
        private final TextView feedbacktitle;

        private final ImageView zoomImageView;
        private final LinearLayout linearFullscreen1;
        private final LinearLayout linearFullscreen2;
        private final LinearLayout linearFullscreen3;
        private final LinearLayout linearFullscreen4;

        private final LinearLayout feedbackLayout;

        private final LinearLayout linearFullImage1;
        private final LinearLayout linearFullImage2;
        private final LinearLayout linearFullImage3;
        private final LinearLayout linearFullImage4;
        private final ImageView imageViewOption1FullImage;
        private final ImageView imageViewOption2FullImage;
        private final ImageView imageViewOption3FullImage;
        private final ImageView imageViewOption4FullImage;

        private TextView optionTextA,optionTextB,optionTextC,optionTextD;


        public holder(@NonNull View holderView) {
            super(holderView);

            optionTextA = holderView.findViewById(R.id.optionTextA);
            optionTextB = holderView.findViewById(R.id.optionTextB);
            optionTextC = holderView.findViewById(R.id.optionTextC);
            optionTextD = holderView.findViewById(R.id.optionTextD);

            textViewQuestion = holderView.findViewById(R.id.textViewQuestion);
            slow_internet_Text = holderView.findViewById(R.id.slow_internet_Text);
            progressBar3 = holderView.findViewById(R.id.progressBar3);
            downloadingTextView = holderView.findViewById(R.id.downloadingTextView);
            progressBar3.setVisibility(View.GONE);
            textViewLastQuestionMessage = holderView.findViewById(R.id.textViewLastQuestionMessage);
            textViewReadMore = holderView.findViewById(R.id.textViewReadMore);
            textViewOption1 = holderView.findViewById(R.id.option1);
            textViewOption2 = holderView.findViewById(R.id.option2);
            textViewOption3 = holderView.findViewById(R.id.option3);
            textViewSubmit = holderView.findViewById(R.id.textViewSubmit);
            reletiveLayoutImage = holderView.findViewById(R.id.reletiveLayoutImage);
            linearLayout = holderView.findViewById(R.id.linearLayout);
            NoInternetConnectionTextView = holderView.findViewById(R.id.NoInternetConnectionTextView);
            textViewOption4 = holderView.findViewById(R.id.option4);
            parentLayout = holderView.findViewById(R.id.parentLayout);
            feedbacktitle = holderView.findViewById(R.id.feedbacktitle);

            imageViewOption1 = holderView.findViewById(R.id.imageViewOption1);
            imageViewOption2 = holderView.findViewById(R.id.imageViewOption2);
            imageViewOption3 = holderView.findViewById(R.id.imageViewOption3);
            imageViewOption4 = holderView.findViewById(R.id.imageViewOption4);

            linearFullImage1 = holderView.findViewById(R.id.linearFullImage1);
            linearFullImage2 = holderView.findViewById(R.id.linearFullImage2);
            linearFullImage3 = holderView.findViewById(R.id.linearFullImage3);
            linearFullImage4 = holderView.findViewById(R.id.linearFullImage4);

            linearFullscreen1 = holderView.findViewById(R.id.linearFullscreen1);
            linearFullscreen2 = holderView.findViewById(R.id.linearFullscreen2);
            linearFullscreen3 = holderView.findViewById(R.id.linearFullscreen3);
            linearFullscreen4 = holderView.findViewById(R.id.linearFullscreen4);

            imageViewOption1FullImage = holderView.findViewById(R.id.imageViewOption1FullImage);
            imageViewOption2FullImage = holderView.findViewById(R.id.imageViewOption2FullImage);
            imageViewOption3FullImage = holderView.findViewById(R.id.imageViewOption3FullImage);
            imageViewOption4FullImage = holderView.findViewById(R.id.imageViewOption4FullImage);

            textViewReadMore.setVisibility(View.GONE);
            
            feedbackText = holderView.findViewById(R.id.feedbackText);
            feedbackLayout = holderView.findViewById(R.id.feedbackLayout);

            questionImageView = holderView.findViewById(R.id.questionImageView);
            zoomImageView = holderView.findViewById(R.id.zoomImageView);
            Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");
            textViewQuestion.setTypeface(myTypeface);

//            Util.enableScroll(textViewOption1);
//            Util.enableScroll(textViewOption2);
//            Util.enableScroll(textViewOption3);
//            Util.enableScroll(textViewOption4);

        }
    }

}
