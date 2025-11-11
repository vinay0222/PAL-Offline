package com.idreameducation.ipreppal.PalMobile.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.PalMobile.activity.DiagonosticTestActivity_Mobile;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by sony on 4/4/2017.
 */

public class DiagonosticTestAdapter_Mobile extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    public static String selectedLanguge;
    public ArrayList<String> scoreArrayList;
    public ArrayList<HashMap<String, String>> trackArrayList = new ArrayList<>();

    private OnItemClickListener clickListener;
    private final HashMap<String, String> data;
    private final ArrayList<HashMap<Integer, String[]>> contentArrayList;
    public final ArrayList<HashMap<String, String>> questionsArrayList;
    private final RecyclerView mRecyclerView;
    private final String submit;
    private final String submitTest;
    private final String imageInternet;
    private final String lastMessage;
    private final Context context;
    private final int height;

    private final ArrayList<HashMap<Integer, String>> questionAttamptedArrayList;
    private final HashMap<Integer, String> questionAttamptedHashMap;
    private final Global global;
    private int count = 0;

    public DiagonosticTestAdapter_Mobile(Context context, ArrayList<HashMap<Integer, String[]>> contentArrayList, ArrayList<HashMap<String, String>> questionsArrayList, RecyclerView mRecyclerView, String selectedLanguage, int height, String submit, String submitTest, String imageInternet, String lastMessage) {
        this.context = context;
        this.lastMessage = lastMessage;
        questionAttamptedArrayList = new ArrayList<>();
        questionAttamptedHashMap = new HashMap<>();
        scoreArrayList = new ArrayList<>();

        data = new HashMap<>();
        this.contentArrayList = contentArrayList;
        this.questionsArrayList = questionsArrayList;
        this.mRecyclerView = mRecyclerView;
        this.height = height;
        this.submit = submit;
        this.submitTest = submitTest;
        this.imageInternet = imageInternet;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiagonosticTestAdapter_Mobile that = (DiagonosticTestAdapter_Mobile) o;

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
                    .inflate(R.layout.row_model_paper, viewGroup, false);

            return new ViewItem(view);
        }
        return null;
    }


    public void updateAdapter() {
        notifyDataSetChanged();
    }

    Handler handler=new Handler();
    private final boolean isloading=true;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, @SuppressLint("RecyclerView") final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            try {

                final HashMap<Integer, String[]> optionsHashmap = contentArrayList.get(position);
                if (Util.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
//                    viewHolder.parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDisplayMetrics().widthPixels / 3 + 150));
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels / 3, 120);
//                    params.rightMargin = 20;
//                    params.leftMargin = 20;
//                    params.topMargin = 10;
//                    params.bottomMargin = 10;
//                    params.gravity = Gravity.CENTER;
//                    viewHolder.textViewSubmit.setBackgroundResource(R.drawable.blue_pressed_25);
//                    viewHolder.textViewSubmit.setLayoutParams(params);
                } else {
//                    viewHolder.parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDisplayMetrics().heightPixels / 2 + 350));
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
                viewHolder.textViewLastQuestionMessage.setText(lastMessage);


                try {
//                    viewHolder.textViewQuestion.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int count = viewHolder.textViewQuestion.getLineCount();
//                            if (count > 3) {
//                                viewHolder.textViewQuestion.setEnabled(true);
//                                viewHolder.textViewReadMore.setVisibility(View.VISIBLE);
//                                viewHolder.textViewReadMore.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Util.preventTwoClick(view);
//                                        String question;
//                                        if (global.isFlagEnabled()) {
//                                            question = questionsArrayList.get(position).get("questionQAlt");
//                                        } else {
//                                            question = questionsArrayList.get(position).get("questionQ");
//                                        }
//                                        showDetailFeedbackDialog(question);
//                                    }
//                                });
//                                viewHolder.textViewQuestion.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Util.preventTwoClick(view);
//                                        String question;
//                                        if (global.isFlagEnabled()) {
//                                            question = questionsArrayList.get(position).get("questionQAlt");
//                                        } else {
//                                            question = questionsArrayList.get(position).get("questionQ");
//                                        }
//                                        showDetailFeedbackDialog(question);
//                                    }
//                                });
//                            } else {
//                                viewHolder.textViewQuestion.setEnabled(false);
//                                viewHolder.textViewReadMore.setVisibility(View.GONE);
//                            }
//                        }
//                    });


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
                    if (Util.checkInternetConnection(context)) {
//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option1"));
//                        Uri uri = Uri.fromFile(file);
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option1").replace(" ","");
                        viewHolder.textViewOption1.setVisibility(View.GONE);
                        viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen1.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption1FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption1);
                        viewHolder.imageViewOption1FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
//                        viewHolder.textViewOption1.setVisibility(View.GONE);
//                        viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(questionsArrayList.get(position).get("option1")).into(viewHolder.imageViewOption1);
                    } else {
//                    viewHolder.textViewOption1.setVisibility(View.VISIBLE);
//                    viewHolder.imageViewOption1.setVisibility(View.GONE);
//                    viewHolder.textViewOption1.setText(imageInternet);
//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option1"));
//                        Uri uri = Uri.fromFile(file);
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option1").replace(" ","");
                        viewHolder.textViewOption1.setVisibility(View.GONE);
                        viewHolder.imageViewOption1.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen1.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption1FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption1);
                        viewHolder.imageViewOption1FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
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
                    if (Util.checkInternetConnection(context)) {
//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option2"));
//                        Uri uri = Uri.fromFile(file);
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option2").replace(" ","");

                        viewHolder.textViewOption2.setVisibility(View.GONE);
                        viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen2.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption2FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption2);

                        viewHolder.imageViewOption2FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
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

//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option2"));
//                        Uri uri = Uri.fromFile(file);
//                        String image = "https://www.idreameducation.org/subjects_videos_books_icon/math.png";
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option2").replace(" ","");
                        viewHolder.textViewOption2.setVisibility(View.GONE);
                        viewHolder.imageViewOption2.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen2.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption2FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption2);

                        viewHolder.imageViewOption2FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
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
                    if (Util.checkInternetConnection(context)) {
//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option3"));
//                        Uri uri = Uri.fromFile(file);
//                        String image = "https://www.idreameducation.org/subjects_videos_books_icon/math.png";
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option3").replace(" ","");
                        viewHolder.textViewOption3.setVisibility(View.GONE);
                        viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen3.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption3FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption3);

                        viewHolder.imageViewOption3FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
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

//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option3"));
//                        Uri uri = Uri.fromFile(file);
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option3").replace(" ","");
                        viewHolder.textViewOption3.setVisibility(View.GONE);
                        viewHolder.imageViewOption3.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen3.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption3FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption3);

                        viewHolder.imageViewOption3FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
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
                    if (Util.checkInternetConnection(context)) {
//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option4"));
//                        Uri uri = Uri.fromFile(file);
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option4").replace(" ","");
                        viewHolder.textViewOption4.setVisibility(View.GONE);
                        viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen4.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption4FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption4);

                        viewHolder.imageViewOption4FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
//                                global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option4"));
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
//                        viewHolder.textViewOption4.setVisibility(View.GONE);
//                        viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(questionsArrayList.get(position).get("option4")).into(viewHolder.imageViewOption4);
                    } else {
//                    viewHolder.textViewOption4.setVisibility(View.VISIBLE);
//                    viewHolder.imageViewOption4.setVisibility(View.GONE);
//                    viewHolder.textViewOption4.setText(imageInternet);

//                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option4"));
//                        Uri uri = Uri.fromFile(file);
//                        String image = "https://www.idreameducation.org/subjects_videos_books_icon/math.png";
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("option4").replace(" ","");
                        viewHolder.textViewOption4.setVisibility(View.GONE);
                        viewHolder.imageViewOption4.setVisibility(View.VISIBLE);
                        viewHolder.linearFullscreen4.setVisibility(View.VISIBLE);
                        viewHolder.imageViewOption4FullImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(image).into(viewHolder.imageViewOption4);

                        viewHolder.imageViewOption4FullImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
//                                global.setFullImage(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("option4"));
                                global.setFullImage(image);
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
                    if (Util.checkInternetConnection(context)) {
                        checkConnection(false,viewHolder);
//                        viewHolder.progressBar3.setVisibility(View.VISIBLE);
                        viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                        viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                        viewHolder.questionImageView.setVisibility(View.VISIBLE);
                        viewHolder.zoomImageView.setVisibility(View.VISIBLE);
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("image"));
                        Uri uri = Uri.fromFile(file);
//                        viewHolder.progressBar3.setVisibility(View.VISIBLE);
                        viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                        viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                        viewHolder.questionImageView.setVisibility(View.VISIBLE);
                        viewHolder.zoomImageView.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(questionsArrayList.get(position).get("image")).into(viewHolder.questionImageView);
//                        String image = "https://www.idreameducation.org/subjects_videos_books_icon/math.png";
                        String image = "https://download.iprep.in/super_app_content/assessment_images/"+questionsArrayList.get(position).get("image").replace(" ","");
                        Glide.with(context)
                                .load(image)
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
                                                        .load(image)
                                                        .listener(new RequestListener<Drawable>() {
                                                            @Override
                                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                                viewHolder.slow_internet_Text.setText("Slow Internet Connection");
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
                                                        .load(image)
                                                        .listener(new RequestListener<Drawable>() {
                                                            @Override
                                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                                viewHolder.progressBar3.setVisibility(View.GONE);
                                                                viewHolder.downloadingTextView.setVisibility(View.GONE);
                                                                viewHolder.slow_internet_Text.setText("Slow Internet Connection");
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
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });
                        viewHolder.zoomImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.preventTwoClick(view);
                                global.setFullImage(image);
                                global.setImageAdded(true);
                                context.startActivity(new Intent(context, FullProfileImageActivity.class));
                            }
                        });

                    } else {
//                    viewHolder.progressBar3.setVisibility(View.GONE);
//                    viewHolder.downloadingTextView.setVisibility(View.GONE);
//                    viewHolder.NoInternetConnectionTextView.setVisibility(View.VISIBLE);
//                    viewHolder.questionImageView.setVisibility(View.GONE);
//                    viewHolder.NoInternetConnectionTextView.setText(imageInternet);


                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages/Class" + Util.getSelectedClass(context) + "/" + questionsArrayList.get(position).get("image"));
                        Uri uri = Uri.fromFile(file);
//                        viewHolder.progressBar3.setVisibility(View.VISIBLE);
                        viewHolder.downloadingTextView.setVisibility(View.VISIBLE);
                        viewHolder.NoInternetConnectionTextView.setVisibility(View.GONE);
                        viewHolder.questionImageView.setVisibility(View.VISIBLE);
                        viewHolder.zoomImageView.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(questionsArrayList.get(position).get("image")).into(viewHolder.questionImageView);
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
                                                                viewHolder.slow_internet_Text.setText("Slow Internet Connection");
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
                                                                viewHolder.slow_internet_Text.setText("Slow Internet Connection");
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
                if (position == questionsArrayList.size() - 1) {


//                viewHolder.textViewSubmit.setVisibility(View.VISIBLE);
                    viewHolder.linearLayout.setVisibility(View.GONE);
                    viewHolder.textViewSubmit.setText(submit);

                } else {
//                viewHolder.textViewSubmit.setVisibility(View.VISIBLE);
                    viewHolder.linearLayout.setVisibility(View.GONE);
                }
                viewHolder.textViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        ((DiagonosticTestActivity_Mobile) context).finalSubmition();
                    }
                });
                viewHolder.textViewOption1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+1)){
                            optionOneClick(position, "Text");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+1, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+1)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option1");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionOneClick(position, "Text");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });

                viewHolder.imageViewOption1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+1)){
                            optionOneClick(position, "Image");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+1, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+1)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option1");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionOneClick(position, "Image");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
//                        optionOneClick(position, "Image");
                    }
                });

                viewHolder.textViewOption2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+2)){
                            optionTwoClick(position, "Text");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+2, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+2)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option2");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionTwoClick(position, "Text");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                viewHolder.imageViewOption2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+1)){
                            optionTwoClick(position, "Image");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+1, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+1)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option2");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionTwoClick(position, "Image");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                viewHolder.textViewOption3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+3)){
                            optionThirdClick(position, "Text");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+3, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+3)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option3");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionThirdClick(position, "Text");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                viewHolder.imageViewOption3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+1)){
                            optionThirdClick(position, "Image");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+1, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+1)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option3");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionThirdClick(position, "Image");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                viewHolder.textViewOption4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+4)){
                            optionFourthClick(position, "Text");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+4, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+4)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option4");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionFourthClick(position, "Text");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                viewHolder.imageViewOption4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.preventTwoClick(view);
                        boolean exists = true;
                        if(!((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.containsKey(position+"-"+1)){
                            optionFourthClick(position, "Image");
                            ((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.put(position+"-"+1, true);
                            exists = false;
                        }
                        for (Map.Entry<String, Boolean> entry :((DiagonosticTestActivity_Mobile)context).restoreQuestionHashMap.entrySet()) {
                            String key = entry.getKey();
                            boolean value = entry.getValue();
                            if(key.equals(position+"-"+1)){
                                if(exists){
                                    if(value){
                                        restoreToOriginalQuestion(position, "Option4");
                                        entry.setValue(false);
                                        if(((DiagonosticTestActivity_Mobile)context).pos == questionsArrayList.size()){
                                            ((DiagonosticTestActivity_Mobile)context).pos = ((DiagonosticTestActivity_Mobile)context).pos - 1;
                                        }
                                    }else{
                                        optionFourthClick(position, "Image");
                                        entry.setValue(true);
                                    }
                                }
                            }else{
                                String[] separated = key.split("-");
                                if(Integer.parseInt(separated[0]) == position){
                                    entry.setValue(false);
                                }
                            }
                        }
                    }
                });
                String[] options = optionsHashmap.get(position);

                for (int i = 0; i < options.length; i++) {
                    if (options[i].equalsIgnoreCase("TRUE")) {
                        switch (i) {
                            case 0:
                                if (questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")) {
                                    /*Set background to the answers*/
                                    viewHolder.textViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                                    viewHolder.textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_selected, 0, 0, 0);
                                    viewHolder.textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                                    viewHolder.textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                                    viewHolder.textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);

                                    /*Set text color of the answers*/
                                    viewHolder.textViewOption1.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption2.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption3.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption4.setTextColor(Color.parseColor("#000000"));
                                } else {
                                    /*Set background to the answers*/
                                    viewHolder.imageViewOption1.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.imageViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                                }

                                break;
                            case 1:

                                if (questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")) {
                                    /*Set background to the answers*/
                                    viewHolder.textViewOption2.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                                    viewHolder.textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                                    viewHolder.textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_selected, 0, 0, 0);
                                    viewHolder.textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                                    viewHolder.textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);

                                    /*Set text color of the answers*/
                                    viewHolder.textViewOption2.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption1.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption3.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption4.setTextColor(Color.parseColor("#000000"));
                                } else {
                                    /*Set background to the answers*/
                                    viewHolder.imageViewOption2.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.imageViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                                }

                                break;
                            case 2:
                                if (questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")) {
                                    /*Set background to the answers*/
                                    viewHolder.textViewOption3.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption4.setBackgroundResource(R.drawable.gray_iprep);

                                    viewHolder.textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                                    viewHolder.textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                                    viewHolder.textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_selected, 0, 0, 0);
                                    viewHolder.textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);

                                    /*Set text color of the answers*/
                                    viewHolder.textViewOption3.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption2.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption1.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption4.setTextColor(Color.parseColor("#000000"));
                                } else {
                                    /*Set background to the answers*/
                                    viewHolder.imageViewOption3.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.imageViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                                }


                                break;
                            case 3:
                                if (questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")) {
                                    /*Set background to the answers*/
                                    viewHolder.textViewOption4.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.textViewOption1.setBackgroundResource(R.drawable.gray_iprep);

                                    viewHolder.textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                                    viewHolder.textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                                    viewHolder.textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                                    viewHolder.textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_selected, 0, 0, 0);

                                    /*Set text color of the answers*/
                                    viewHolder.textViewOption4.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption2.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption3.setTextColor(Color.parseColor("#000000"));
                                    viewHolder.textViewOption1.setTextColor(Color.parseColor("#000000"));
                                } else {
                                    /*Set background to the answers*/
                                    viewHolder.imageViewOption4.setBackgroundResource(R.drawable.correct_answer_new);
                                    viewHolder.imageViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                                    viewHolder.imageViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                                }
                                break;
                        }
                        return;
                    } else {

                        viewHolder.imageViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.imageViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.imageViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.imageViewOption1.setBackgroundResource(R.drawable.gray_iprep);

                        viewHolder.textViewOption1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_option_s, 0, 0, 0);
                        viewHolder.textViewOption2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.b_option_s, 0, 0, 0);
                        viewHolder.textViewOption3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.c_option_s, 0, 0, 0);
                        viewHolder.textViewOption4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_option_s, 0, 0, 0);

                        /*Set background to the answers*/
                        viewHolder.textViewOption4.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.textViewOption2.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.textViewOption3.setBackgroundResource(R.drawable.gray_iprep);
                        viewHolder.textViewOption1.setBackgroundResource(R.drawable.gray_iprep);
                        /*Set text color of the answers*/
                        viewHolder.textViewOption4.setTextColor(Color.parseColor("#000000"));
                        viewHolder.textViewOption2.setTextColor(Color.parseColor("#000000"));
                        viewHolder.textViewOption3.setTextColor(Color.parseColor("#000000"));
                        viewHolder.textViewOption1.setTextColor(Color.parseColor("#000000"));



                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
//            viewHolder.textViewQuestion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    viewHolder.textViewQuestion.getViewTreeObserver().removeOnPreDrawListener(this);
//                    int lineCount = viewHolder.textViewQuestion.getLineCount();
//                    if (lineCount > 3) {
//                        viewHolder.textViewReadMore.setVisibility(View.VISIBLE);
//                        viewHolder.textViewReadMore.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String question;
//                                if (global.isFlagEnabled()) {
//                                    question = questionsArrayList.get(position).get("questionQAlt");
//                                } else {
//                                    question = questionsArrayList.get(position).get("questionQ");
//                                }
//                                showDetailFeedbackDialog(question);
//                            }
//                        });
//                        viewHolder.textViewQuestion.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String question;
//                                if (global.isFlagEnabled()) {
//                                    question = questionsArrayList.get(position).get("questionQAlt");
//                                } else {
//                                    question = questionsArrayList.get(position).get("questionQ");
//                                }
//                                showDetailFeedbackDialog(question);
//                            }
//                        });
//                    } else {
//                        viewHolder.textViewReadMore.setVisibility(View.GONE);
//                    }
//                    return true;
//                }
//            });


        }
    }

    private void checkConnection(boolean first,ViewItem viewHolderValue) {

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(isloading)
//                {
//                    if(first)
//                    {
//                        viewHolderValue.progressBar3.setVisibility(View.GONE);
//                        viewHolderValue.slow_internet_Text.setText("We are unable to load Test\nDue to Slow Internet Connection");
//                        viewHolderValue.slow_internet_Text.setVisibility(View.VISIBLE);
//                        viewHolderValue.imageViewCrossVideo2.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        checkConnection(true,viewHolderValue);
//                        viewHolderValue.slow_internet_Text.setVisibility(View.VISIBLE);
//                    }
//
//                }
//                else
//                {
//                    viewHolderValue.progressBar3.setVisibility(View.GONE);
//                    viewHolderValue.slow_internet_Text.setVisibility(View.GONE);
//                }
//            }
//        }, 10000 );//time in milisecond

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

    private void moveToNextQuestion(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (position + 1 != questionsArrayList.size()) {
                        ViewItem viewHolder = (ViewItem) mRecyclerView.findViewHolderForAdapterPosition(position);
//                        if(questionsArrayList.get(position).get("type1").equalsIgnoreCase("Text")){
//                            viewHolder.textViewOption1.setFocusable(true);
//                            viewHolder.textViewOption1.setFocusableInTouchMode(true);
//                            viewHolder.textViewOption1.requestFocus();
//                        }else {
//                            viewHolder.imageViewOption1.setFocusable(true);
//                            viewHolder.imageViewOption1.setFocusableInTouchMode(true);
//                            viewHolder.imageViewOption1.requestFocus();
//                        }
                         ((DiagonosticTestActivity_Mobile) context).scroll(position + 1);
                    } else {
                        try {
                            ViewItem viewHolder = (ViewItem) mRecyclerView.findViewHolderForAdapterPosition(position);
                            //  ((DiagonosticTestActivity) context).scroll(position + 1);
                            viewHolder.textViewSubmit.setFocusable(true);
                            viewHolder.textViewSubmit.setFocusableInTouchMode(true);
                            ((DiagonosticTestActivity_Mobile) context).checkItsLastQuestion();
//                            viewHolder.textViewSubmit.requestFocus();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 700);
    }

    private void restoreToOriginalQuestion(int position, String option) {
        count = 0;
        HashMap<Integer, String[]> optionsMap = contentArrayList.get(position);
        contentArrayList.remove(position);
        String[] op = new String[4];
        op[0] = "FALSE";
        op[1] = "FALSE";
        op[2] = "FALSE";
        op[3] = "FALSE";
        optionsMap.put(position, op);
        contentArrayList.add(position, optionsMap);
        HashMap<String, String> optionsHashmap = questionsArrayList.get(position);
        questionsArrayList.remove(position);
        optionsHashmap.put("OptionSelected", "NOT");
        optionsHashmap.put("Status", "PENDING");
        optionsHashmap.put("IsAttampted", "FALSE");
        questionsArrayList.add(position, optionsHashmap);
        scoreArrayList.remove(position + "correct");

        if(questionAttamptedHashMap.containsKey(position) && questionAttamptedHashMap.containsValue(option)){
            questionAttamptedHashMap.remove(position);
            questionAttamptedArrayList.remove(questionAttamptedHashMap);
        }

        for (int i = 0; i < questionsArrayList.size(); i++) {
            if (questionsArrayList.get(i).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                count++;
            }
        }
        ((DiagonosticTestActivity_Mobile) context).setTextOnQuestionAttampted(count);
        ((DiagonosticTestActivity_Mobile) context).setAttemptedQ(count);
        ((DiagonosticTestActivity_Mobile) context).setYetToAttemptQ(questionsArrayList.size() - count);
        ((DiagonosticTestActivity_Mobile) context).removeAttempt(position);
        ((DiagonosticTestActivity_Mobile) context).checkSkippedQ(position, 1);
        ((DiagonosticTestActivity_Mobile) context).refreshQuestionsListingAdapter();
//        ((DiagonosticTestActivity_Mobile) context).updateNextQuestionButtonText(1, position);
        ((DiagonosticTestActivity_Mobile) context).checkItsLastQuestion();
        notifyDataSetChanged();
    }

    private void optionOneClick(int position, String type) {

        count = 0;
        HashMap<Integer, String[]> optionsMap = contentArrayList.get(position);
        contentArrayList.remove(position);
        String[] op = new String[4];
        op[0] = "TRUE";
        op[1] = "FALSE";
        op[2] = "FALSE";
        op[3] = "FALSE";
        optionsMap.put(position, op);
        contentArrayList.add(position, optionsMap);

        HashMap<String, String> optionsHashmap = questionsArrayList.get(position);
        questionsArrayList.remove(position);
        optionsHashmap.put("OptionSelected", "Option1");
        optionsHashmap.put("IsAttampted", "TRUE");


        questionAttamptedHashMap.put(position, "Option1");

        questionAttamptedArrayList.add(questionAttamptedHashMap);

        if (optionsHashmap.get("correctOption").equalsIgnoreCase("Option1")) {
            optionsHashmap.put("Status", "Correct");
            trackArrayList.get(position).put("Status", "Correct");
            if (!scoreArrayList.contains(position + "correct")) {
                scoreArrayList.add(position + "correct");
            }
        } else {
            trackArrayList.get(position).put("Status", "inCorrect");
            optionsHashmap.put("Status", "inCorrect");
            scoreArrayList.remove(position + "correct");
        }
        questionsArrayList.add(position, optionsHashmap);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            if (questionsArrayList.get(i).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                count++;
            }
        }

        ((DiagonosticTestActivity_Mobile) context).setTextOnQuestionAttampted(count);
        ((DiagonosticTestActivity_Mobile) context).setAttemptedQ(count);
        ((DiagonosticTestActivity_Mobile) context).setYetToAttemptQ(questionsArrayList.size() - count);
        ((DiagonosticTestActivity_Mobile) context).attemptedQMap.add(position);
        ((DiagonosticTestActivity_Mobile) context).checkSkippedQ(position, 1);
        ((DiagonosticTestActivity_Mobile) context).refreshQuestionsListingAdapter();
//        ((DiagonosticTestActivity_Mobile) context).updateNextQuestionButtonText(0, position);
        //((DiagonosticTestActivity) context).moveToNextQuestion(((DiagonosticTestActivity) context).pos + 1);
        moveToNextQuestion(position);
        notifyDataSetChanged();
    }

    private void optionThirdClick(int position, String type) {
        count = 0;

        HashMap<Integer, String[]> optionsMap = contentArrayList.get(position);
        contentArrayList.remove(position);
        String[] op = new String[4];
        op[0] = "FALSE";
        op[1] = "FALSE";
        op[2] = "TRUE";
        op[3] = "FALSE";
        optionsMap.put(position, op);
        contentArrayList.add(position, optionsMap);
        HashMap<String, String> optionsHashmap = questionsArrayList.get(position);
        questionsArrayList.remove(position);
        optionsHashmap.put("OptionSelected", "Option3");
        optionsHashmap.put("IsAttampted", "TRUE");
        questionAttamptedHashMap.put(position, "Option3");

        questionAttamptedArrayList.add(questionAttamptedHashMap);


        if (optionsHashmap.get("correctOption").equalsIgnoreCase("Option3")) {
            optionsHashmap.put("Status", "Correct");
            trackArrayList.get(position).put("Status", "Correct");
            if (!scoreArrayList.contains(position + "correct")) {
                scoreArrayList.add(position + "correct");
            }
        } else {
            scoreArrayList.remove(position + "correct");
            trackArrayList.get(position).put("Status", "inCorrect");
            optionsHashmap.put("Status", "inCorrect");
        }
        questionsArrayList.add(position, optionsHashmap);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            if (questionsArrayList.get(i).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                count++;
            }
        }
        ((DiagonosticTestActivity_Mobile) context).setTextOnQuestionAttampted(count);
        ((DiagonosticTestActivity_Mobile) context).setAttemptedQ(count);
        ((DiagonosticTestActivity_Mobile) context).setYetToAttemptQ(questionsArrayList.size() - count);
        ((DiagonosticTestActivity_Mobile) context).attemptedQMap.add(position);
        ((DiagonosticTestActivity_Mobile) context).checkSkippedQ(position, 1);
        ((DiagonosticTestActivity_Mobile) context).refreshQuestionsListingAdapter();
//        ((DiagonosticTestActivity_Mobile) context).updateNextQuestionButtonText(0, position);
        //((DiagonosticTestActivity) context).moveToNextQuestion(((DiagonosticTestActivity) context).pos + 1);
        moveToNextQuestion(position);

        notifyDataSetChanged();
    }

    private void optionFourthClick(int position, String type) {
        String option = "Not";
        count = 0;
//        try {
//            option = questionAttamptedArrayList.get(position).get(position);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (option != null && option.equalsIgnoreCase("option4")) {
//            restoreToOriginalQuestion(position, "option4");
//        } else {
//
//        }
        HashMap<Integer, String[]> optionsMap = contentArrayList.get(position);
        contentArrayList.remove(position);
        String[] op = new String[4];
        op[0] = "FALSE";
        op[1] = "FALSE";
        op[2] = "FALSE";
        op[3] = "TRUE";
        optionsMap.put(position, op);
        contentArrayList.add(position, optionsMap);
        HashMap<String, String> optionsHashmap = questionsArrayList.get(position);
        questionsArrayList.remove(position);
        optionsHashmap.put("OptionSelected", "Option4");
        optionsHashmap.put("IsAttampted", "TRUE");


        questionAttamptedHashMap.put(position, "Option4");
        try {
            //   questionAttamptedArrayList.remove(position);
        } catch (Exception e) {
        }
        questionAttamptedArrayList.add(questionAttamptedHashMap);
        if (optionsHashmap.get("correctOption").equalsIgnoreCase("Option4")) {
            optionsHashmap.put("Status", "Correct");
            trackArrayList.get(position).put("Status", "Correct");
            if (!scoreArrayList.contains(position + "correct")) {
                scoreArrayList.add(position + "correct");
            }
        } else {
            scoreArrayList.remove(position + "correct");
            trackArrayList.get(position).put("Status", "inCorrect");
            optionsHashmap.put("Status", "inCorrect");
        }
        questionsArrayList.add(position, optionsHashmap);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            if (questionsArrayList.get(i).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                count++;
            }
        }
        ((DiagonosticTestActivity_Mobile) context).setTextOnQuestionAttampted(count);
        ((DiagonosticTestActivity_Mobile) context).setAttemptedQ(count);
        ((DiagonosticTestActivity_Mobile) context).setYetToAttemptQ(questionsArrayList.size() - count);
        ((DiagonosticTestActivity_Mobile) context).attemptedQMap.add(position);
        ((DiagonosticTestActivity_Mobile) context).checkSkippedQ(position, 1);
        ((DiagonosticTestActivity_Mobile) context).refreshQuestionsListingAdapter();
//        ((DiagonosticTestActivity_Mobile) context).updateNextQuestionButtonText(0, position);
        //((DiagonosticTestActivity) context).moveToNextQuestion(((DiagonosticTestActivity) context).pos + 1);
        moveToNextQuestion(position);

        notifyDataSetChanged();
    }

    private void optionTwoClick(int position, String type) {
        count = 0;
        String option = "Not";
//        try {
//            option = questionAttamptedArrayList.get(position).get(position);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (option != null && option.equalsIgnoreCase("option2")) {
//            restoreToOriginalQuestion(position, "option3");
//        } else {
//
//        }
        HashMap<Integer, String[]> optionsMap = contentArrayList.get(position);
        contentArrayList.remove(position);
        String[] op = new String[4];
        op[0] = "FALSE";
        op[1] = "TRUE";
        op[2] = "FALSE";
        op[3] = "FALSE";
        optionsMap.put(position, op);
        contentArrayList.add(position, optionsMap);
        HashMap<String, String> optionsHashmap = questionsArrayList.get(position);
        questionsArrayList.remove(position);
        optionsHashmap.put("OptionSelected", "Option2");
        optionsHashmap.put("IsAttampted", "TRUE");

        questionAttamptedHashMap.put(position, "Option2");


        try {
            // questionAttamptedArrayList.remove(position);
        } catch (Exception e) {
        }
        questionAttamptedArrayList.add(questionAttamptedHashMap);


        if (optionsHashmap.get("correctOption").equalsIgnoreCase("Option2")) {
            optionsHashmap.put("Status", "Correct");
            trackArrayList.get(position).put("Status", "Correct");
            if (!scoreArrayList.contains(position + "correct")) {
                scoreArrayList.add(position + "correct");
            }

        } else {
            scoreArrayList.remove(position + "correct");
            trackArrayList.get(position).put("Status", "inCorrect");
            optionsHashmap.put("Status", "inCorrect");
        }
        questionsArrayList.add(position, optionsHashmap);
        for (int i = 0; i < questionsArrayList.size(); i++) {
            if (questionsArrayList.get(i).get("IsAttampted").equalsIgnoreCase("TRUE")) {
                count++;
            }
        }
        ((DiagonosticTestActivity_Mobile) context).setTextOnQuestionAttampted(count);
        ((DiagonosticTestActivity_Mobile) context).setAttemptedQ(count);
        ((DiagonosticTestActivity_Mobile) context).setYetToAttemptQ(questionsArrayList.size() - count);
        ((DiagonosticTestActivity_Mobile) context).attemptedQMap.add(position);
        ((DiagonosticTestActivity_Mobile) context).checkSkippedQ(position, 1);
        ((DiagonosticTestActivity_Mobile) context).refreshQuestionsListingAdapter();
//        ((DiagonosticTestActivity_Mobile) context).updateNextQuestionButtonText(0, position);
        //((DiagonosticTestActivity) context).moveToNextQuestion(((DiagonosticTestActivity) context).pos + 1);
        moveToNextQuestion(position);
        notifyDataSetChanged();
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
                Util.preventTwoClick(view);
                dialog.dismiss();
            }
        });
        TextView textViewFeedback = dialog.findViewById(R.id.textViewFeedback);
        textViewFeedback.setText(question);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewQuestion;
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
        private final ImageView zoomImageView;
        private final RelativeLayout parentLayout;
        private final ImageView imageViewOption1;
        private final ImageView imageViewOption2;
        private final ImageView imageViewOption3;
        private final ImageView imageViewOption4;
        private final ProgressBar progressBar3;
        ImageView imageViewCrossVideo2;
        private final LinearLayout linearFullscreen1;
        private final LinearLayout linearFullscreen2;
        private final LinearLayout linearFullscreen3;
        private final LinearLayout linearFullscreen4;

        private final LinearLayout linearFullImage1;
        private final LinearLayout linearFullImage2;
        private final LinearLayout linearFullImage3;
        private final LinearLayout linearFullImage4;
        private final ImageView imageViewOption1FullImage;
        private final ImageView imageViewOption2FullImage;
        private final ImageView imageViewOption3FullImage;
        private final ImageView imageViewOption4FullImage;

        TextView slow_internet_Text;
        public ViewItem(View holderView) {
            super(holderView);
            // holderView.setOnClickListener(this);
            imageViewCrossVideo2 = holderView.findViewById(R.id.imageViewCrossVideo2);
            slow_internet_Text = holderView.findViewById(R.id.slow_internet_Text);
            textViewQuestion = holderView.findViewById(R.id.textViewQuestion);
            progressBar3 = holderView.findViewById(R.id.progressBar3);
            downloadingTextView = holderView.findViewById(R.id.downloadingTextView);
            progressBar3.setVisibility(View.GONE);
            textViewLastQuestionMessage = holderView.findViewById(R.id.textViewLastQuestionMessage);
            textViewReadMore = holderView.findViewById(R.id.textViewReadMore);
            textViewOption1 = holderView.findViewById(R.id.option1);
//            Util.enableScroll(textViewOption1);
            textViewOption2 = holderView.findViewById(R.id.option2);
//            Util.enableScroll(textViewOption2);
            textViewOption3 = holderView.findViewById(R.id.option3);
//            Util.enableScroll(textViewOption3);
            textViewSubmit = holderView.findViewById(R.id.textViewSubmit);
            reletiveLayoutImage = holderView.findViewById(R.id.reletiveLayoutImage);
            linearLayout = holderView.findViewById(R.id.linearLayout);
            NoInternetConnectionTextView = holderView.findViewById(R.id.NoInternetConnectionTextView);
            textViewOption4 = holderView.findViewById(R.id.option4);
//            Util.enableScroll(textViewOption4);
            parentLayout = holderView.findViewById(R.id.parentLayout);
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


            questionImageView = holderView.findViewById(R.id.questionImageView);
            zoomImageView = holderView.findViewById(R.id.zoomImageView);
            Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");
            textViewQuestion.setTypeface(myTypeface);
        }

        @Override
        public void onClick(View view) {
            Util.preventTwoClick(view);
            clickListener.onItemClick(view, getPosition());
        }
    }

}