package com.idreameducation.ipreppal.pal.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class ReviewModelPaperAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    public static String selectedLanguge;
    public ArrayList<String> scoreArrayList;
    private OnItemClickListener clickListener;
    private ArrayList<HashMap<String, String>> questionsArrayList;
    private Context context;
    private String correct;
    private String incorrect;
    private String unattempted;
    private Global global;
    private String[] textString;

    public ReviewModelPaperAdapter(Context context, ArrayList<HashMap<String, String>> questionsArrayList, String[] textString) {
        this.context = context;
        scoreArrayList = new ArrayList<>();
        this.questionsArrayList = questionsArrayList;
        this.correct = correct;
        this.incorrect = incorrect;
        this.unattempted = unattempted;
        this.textString = textString;
        global = (Global) context.getApplicationContext();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewModelPaperAdapter that = (ReviewModelPaperAdapter) o;

        return scoreArrayList.equals(that.scoreArrayList);
    }

    @Override
    public int hashCode() {
        return scoreArrayList.hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_review_modelpaper, viewGroup, false);
            return new ReviewModelPaperAdapter.ViewItem(view);
        }
        return null;
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ReviewModelPaperAdapter.ViewItem) {
            final ReviewModelPaperAdapter.ViewItem viewHolder = (ReviewModelPaperAdapter.ViewItem) viewHolderValue;
            final String status = questionsArrayList.get(position).get("Status");
            viewHolder.textViewReadMore.setText(textString[6]);
            viewHolder.textViewReadMoreQuestion.setText(textString[6]);
            viewHolder.textViewF.setText(textString[5]);
            if (selectedLanguge.equalsIgnoreCase("English")) {
                viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQ"));
                if (status.equalsIgnoreCase("inCorrect")) {
                    viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("incorrect_feedback"));
                } else {
                    viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("correct_feedback"));
                }
            } else {
                if (global.isFlagEnabled()) {
                    viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQAlt"));
                    if (status.equalsIgnoreCase("inCorrect")) {
                        viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("incorrect_feedback_alt"));
                    } else {
                        viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("correct_feedback_alt"));
                    }
                } else {
                    viewHolder.textViewQuestion.setText(questionsArrayList.get(position).get("questionQ"));
                    if (status.equalsIgnoreCase("inCorrect")) {
                        viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("incorrect_feedback"));
                    } else {
                        viewHolder.textViewFeedback.setText(questionsArrayList.get(position).get("correct_feedback"));
                    }
                }
            }

            viewHolder.textViewQuestion.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = viewHolder.textViewQuestion.getLineCount();
                    if (lineCount > 3) {
                        viewHolder.textViewReadMoreQuestion.setVisibility(View.VISIBLE);
                        viewHolder.textViewReadMoreQuestion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                String question;
                                if (global.isFlagEnabled()) {
                                    question = questionsArrayList.get(position).get("questionQAlt");
                                } else {
                                    question = questionsArrayList.get(position).get("questionQ");
                                }
                                try {
                                    showDetailFeedbackDialog(question, "Question");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        viewHolder.textViewQuestion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                String question;
                                if (global.isFlagEnabled()) {
                                    question = questionsArrayList.get(position).get("questionQAlt");
                                } else {
                                    question = questionsArrayList.get(position).get("questionQ");
                                }
                                try {
                                    showDetailFeedbackDialog(question, "Question");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        viewHolder.textViewReadMoreQuestion.setOnClickListener(null);
                        viewHolder.textViewQuestion.setOnClickListener(null);
                        viewHolder.textViewReadMoreQuestion.setVisibility(View.GONE);
                    }

                }
            });
            viewHolder.textViewFeedback.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = viewHolder.textViewFeedback.getLineCount();
                    if (lineCount > 3) {
                        viewHolder.textViewReadMore.setVisibility(View.VISIBLE);
                        viewHolder.textViewFeedback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDialog(status, position, "Feedback");
                            }
                        });
                        viewHolder.textViewReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDialog(status, position, "Feedback");
                            }
                        });
                    } else {
                        viewHolder.textViewFeedback.setOnClickListener(null);
                        viewHolder.textViewReadMore.setOnClickListener(null);
                        viewHolder.textViewReadMore.setVisibility(View.GONE);
                    }
                }
            });

            if (questionsArrayList.get(position).get("image") != null) {
                viewHolder.questionImageView.setEnabled(true);
                Glide.with(context).load(questionsArrayList.get(position).get("image")).into(viewHolder.questionImageView);
            } else {
                viewHolder.questionImageView.setEnabled(false);
//                viewHolder.questionImageView.setImageResource(android.R.color.transparent);
                try {
                    viewHolder.questionImageView.setImageResource(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String option = questionsArrayList.get(position).get("correctOption");
            String optionSelected = questionsArrayList.get(position).get("OptionSelected");
            String IsAttampted = questionsArrayList.get(position).get("IsAttampted");
            if (status.equalsIgnoreCase("Correct")) {
                viewHolder.textViewCorrectAnswerText.setVisibility(View.GONE);
                viewHolder.textViewyouranswerText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_button, 0);
                viewHolder.incorrectOption.setText(textString[4]);
            } else if (status.equalsIgnoreCase("inCorrect")) {
                viewHolder.incorrectOption.setText(textString[3]);
                viewHolder.textViewyouranswerText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.wrong_button, 0);
            } else {
                viewHolder.textViewyouranswerText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.wrong_button, 0);
                viewHolder.incorrectOption.setText(textString[3]);
                viewHolder.textViewQuestionNotAttempted.setTextColor(Color.parseColor("#f10f29"));
            }
            if (IsAttampted.equalsIgnoreCase("FALSE")) {
                viewHolder.linearYourAnswer.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
                viewHolder.incorrectOption.setText(textString[1]);
            } else {
                viewHolder.linearYourAnswer.setBackgroundResource(R.drawable.incorrect_answer_);
                setIncorrectOption(optionSelected, position, viewHolder);
            }
            viewHolder.textViewCorrectAnswerText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_button, 0);

            viewHolder.textViewCorrectAnswerText.setText(textString[0]);
            viewHolder.textViewyouranswerText.setText(textString[2]);


            if (option.equalsIgnoreCase(optionSelected)) {
                viewHolder.linearYourAnswer.setBackgroundResource(R.drawable.correct_answer_new);
                viewHolder.correctOption.setText(textString[4]);
            } else {
                setCorrectOption(option, position, viewHolder);
            }

            if (questionsArrayList.get(position).get("feedback_image") != null) {
                viewHolder.textViewFeedback.setVisibility(View.GONE);
                viewHolder.textViewReadMore.setVisibility(View.GONE);
                viewHolder.imageViewFeedback.setVisibility(View.VISIBLE);
                Glide.with(context).load(questionsArrayList.get(position).get("feedback_image")).into(viewHolder.imageViewFeedback);
                viewHolder.imageViewFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        global.setFullImage(questionsArrayList.get(position).get("feedback_image"));
                        context.startActivity(new Intent(context, FullProfileImageActivity.class));
                    }
                });
            } else {
                viewHolder.textViewFeedback.setVisibility(View.VISIBLE);
//                viewHolder.textViewReadMore.setVisibility(View.VISIBLE);
                viewHolder.imageViewFeedback.setVisibility(View.GONE);
            }


        }
    }

    private void openDialog(String status, int position, String type) {
        String feedback;
        if (global.isFlagEnabled()) {
            if (status.equalsIgnoreCase("inCorrect")) {
                feedback = questionsArrayList.get(position).get("incorrect_feedback_alt");
            } else {
                feedback = questionsArrayList.get(position).get("correct_feedback_alt");
            }
        } else {
            if (status.equalsIgnoreCase("inCorrect")) {
                feedback = questionsArrayList.get(position).get("incorrect_feedback");
            } else {
                feedback = questionsArrayList.get(position).get("correct_feedback");
            }
        }
        try {
            showDetailFeedbackDialog(feedback, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDetailFeedbackDialog(String feedback, String type) throws Exception {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_feedback_detail);

        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView textViewFeedback = dialog.findViewById(R.id.textViewFeedback);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(textString[5]);
        if (!type.equalsIgnoreCase("Feedback")) {
            textView.setVisibility(View.INVISIBLE);
        }
        textViewFeedback.setText(feedback);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private void setCorrectOption(String option, int position, ReviewModelPaperAdapter.ViewItem viewHolder) {
        String type1 = questionsArrayList.get(position).get("type1");
        String type2 = questionsArrayList.get(position).get("type2");
        String type3 = questionsArrayList.get(position).get("type3");
        String type4 = questionsArrayList.get(position).get("type4");
        switch (option) {
            case "Option1":
                if (type1.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.correctOption.setVisibility(View.GONE);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption2);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option1")).into(viewHolder.imageViewOption2);
                    }


                } else {
                    viewHolder.imageViewOption2.setVisibility(View.GONE);
                    viewHolder.correctOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.correctOption.setText(questionsArrayList.get(position).get("option1"));
                    } else {
                        if (global.isFlagEnabled()) {
                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option1_alt"));
                        } else {

                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option1"));
                        }
                    }
                }
                break;
            case "Option2":
                if (type2.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.correctOption.setVisibility(View.GONE);

                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption2);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option2")).into(viewHolder.imageViewOption2);
                    }

                } else {
                    viewHolder.imageViewOption2.setVisibility(View.GONE);
                    viewHolder.correctOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.correctOption.setText(questionsArrayList.get(position).get("option2"));
                    } else {
                        if (global.isFlagEnabled()) {
                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option2_alt"));
                        } else {

                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option2"));
                        }
                    }
                }


                break;
            case "Option3":

                if (type3.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.correctOption.setVisibility(View.GONE);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption2);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option3")).into(viewHolder.imageViewOption2);
                    }

                    //Glide.with(context).load(questionsArrayList.get(position).get("option3")).into(viewHolder.imageViewOption2);
                } else {
                    viewHolder.imageViewOption2.setVisibility(View.GONE);
                    viewHolder.correctOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.correctOption.setText(questionsArrayList.get(position).get("option3"));
                    } else {
                        if (global.isFlagEnabled()) {

                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option3_alt"));
                        } else {
                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option3"));
                        }
                    }
                }


                break;
            case "Option4":

                if (type4.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                    viewHolder.correctOption.setVisibility(View.GONE);
                    //Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption2);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption2);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption2);
                    }

                } else {
                    viewHolder.imageViewOption2.setVisibility(View.GONE);
                    viewHolder.correctOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.correctOption.setText(questionsArrayList.get(position).get("option4"));
                    } else {
                        if (global.isFlagEnabled()) {

                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option4_alt"));
                        } else {
                            viewHolder.correctOption.setText(questionsArrayList.get(position).get("option4"));

                        }
                    }
                }


                break;
        }

    }

    private void setIncorrectOption(String option, int position, ViewItem viewHolder) {


        String type1 = questionsArrayList.get(position).get("type1");
        String type2 = questionsArrayList.get(position).get("type2");
        String type3 = questionsArrayList.get(position).get("type3");
        String type4 = questionsArrayList.get(position).get("type4");


        switch (option) {
            case "Option1":
                if (type1.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.incorrectOption.setVisibility(View.GONE);
//                    Glide.with(context).load(questionsArrayList.get(position).get("option1")).into(viewHolder.imageViewOption1);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption1);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option1")).into(viewHolder.imageViewOption1);
                    }


                } else {
                    viewHolder.imageViewOption1.setVisibility(View.GONE);
                    viewHolder.incorrectOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option1"));
                    } else {
                        if (global.isFlagEnabled()) {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option1_alt"));
                        } else {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option1"));
                        }
                    }
                }
                break;
            case "Option2":

                if (type2.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.incorrectOption.setVisibility(View.GONE);
//                    Glide.with(context).load(questionsArrayList.get(position).get("option2")).into(viewHolder.imageViewOption1);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption1);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option2")).into(viewHolder.imageViewOption1);
                    }


                } else {
                    viewHolder.imageViewOption1.setVisibility(View.GONE);
                    viewHolder.incorrectOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option2"));
                    } else {
                        if (global.isFlagEnabled()) {

                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option2_alt"));
                        } else {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option2"));

                        }
                    }
                }


                break;
            case "Option3":
                if (type3.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.incorrectOption.setVisibility(View.GONE);
                    //Glide.with(context).load(questionsArrayList.get(position).get("option3")).into(viewHolder.imageViewOption1);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption1);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option3")).into(viewHolder.imageViewOption1);
                    }



                } else {
                    viewHolder.imageViewOption1.setVisibility(View.GONE);
                    viewHolder.incorrectOption.setVisibility(View.VISIBLE);
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option3"));
                    } else {
                        if (global.isFlagEnabled()) {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option3_alt"));
                        } else {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option3"));
                        }
                    }
                }
                break;
            case "Option4":
                if (type4.equalsIgnoreCase("Image")) {
                    viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                    viewHolder.incorrectOption.setVisibility(View.GONE);
//                    Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption1);
                    if (Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("questionID"));
                        Uri uri = Uri.fromFile(file);
                        Glide.with(context).load(uri).into(viewHolder.imageViewOption1);
                    } else {
                        Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption1);
                    }
                } else {
                    if (selectedLanguge.equalsIgnoreCase("English")) {
                        viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option4"));
                    } else {
                        if (global.isFlagEnabled()) {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option4_alt"));
                        } else {
                            viewHolder.incorrectOption.setText(questionsArrayList.get(position).get("option4"));
                        }
                    }
                }
                break;
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewQuestion;
        private TextView textViewReadMoreQuestion;
        private TextView textViewF;
        private TextView textViewCorrectAnswerText;
        private TextView correctOption;
        private TextView textViewQuestionNotAttempted;
        private TextView incorrectOption;
        private TextView textViewyouranswerText;
        private TextView textViewFeedback;
        private ImageView questionImageView;
        private LinearLayout linearYourAnswer;
        private LinearLayout linearCorrectAnswer;
        private TextView textViewReadMore;
        private ImageView imageViewFeedback;
        private ImageView imageViewOption1;
        private ImageView imageViewOption2;

        public ViewItem(View holderView) {
            super(holderView);
            // holderView.setOnClickListener(this);
            imageViewFeedback = holderView.findViewById(R.id.imageViewFeedback);
            textViewFeedback = holderView.findViewById(R.id.textViewFeedback);
            textViewCorrectAnswerText = holderView.findViewById(R.id.textViewCorrectAnswerText);
            textViewyouranswerText = holderView.findViewById(R.id.textViewyouranswerText);
            textViewQuestion = holderView.findViewById(R.id.textViewQuestion);
            correctOption = holderView.findViewById(R.id.correctOption);
            linearYourAnswer = holderView.findViewById(R.id.linearYourAnswer);
            linearCorrectAnswer = holderView.findViewById(R.id.linearCorrectAnswer);
            textViewQuestionNotAttempted = holderView.findViewById(R.id.textViewQuestionNotAttempted);
            incorrectOption = holderView.findViewById(R.id.incorrectOption);
            textViewReadMoreQuestion = holderView.findViewById(R.id.textViewReadMoreQuestion);
            textViewF = holderView.findViewById(R.id.textViewF);
            questionImageView = holderView.findViewById(R.id.questionImageView);
            textViewReadMore = holderView.findViewById(R.id.textViewReadMore);
            imageViewOption1 = holderView.findViewById(R.id.imageViewOption1);
            imageViewOption2 = holderView.findViewById(R.id.imageViewOption2);
            Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");
            textViewQuestion.setTypeface(myTypeface);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}