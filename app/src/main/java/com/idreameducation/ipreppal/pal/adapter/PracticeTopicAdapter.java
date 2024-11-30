package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.NormalTestActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.util.TextViewBodyFont;
import com.idreameducation.ipreppal.util.Util;
import com.skydoves.balloon.ArrowOrientation;

import java.util.ArrayList;
import java.util.HashMap;


public class PracticeTopicAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final Global global;
    private PracticeTopicAdapter.OnItemClickListener clickListener;
    public ArrayList<HashMap<String, String>> topicsArrayList;
    private static final int TYPE_ITEM = 0;
    private final String sClass;
    private boolean showDefaultTopic;
    private final FragmentActivity activity;
    private float totalsec=0;
    private View diagnosticTestTextView,practiceTextView,finalTestTextView;
    private boolean showTooltip = false;
    private final String practiceText,videoText,DiagnosticText,FinalText,scoreText,masteryText,alreadyAttemptedText,foundationalText,diagnosticTextFirstText;
    public static String activeFoundationTopic="";
    public static String openTopicID="";
    public ArrayList<String> listArray;
    public static int showingPosition=0;
    int SHOW_TOPIC_LISTING=20222;
    int SHOW_ONLY_NAME=2416;

    public PracticeTopicAdapter(Context context, ArrayList<HashMap<String, String>> topicsArrayList, String sClass, FragmentActivity activity) {
        this.context = context;
        this.topicsArrayList = topicsArrayList;
        this.sClass = sClass;
        this.activity = activity;
        global = (Global) context.getApplicationContext();

        /** setup offline text */
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            practiceText ="रेमेडियल";
            DiagnosticText ="डायग्नोस्टिक परीक्षण";
            FinalText = "अंतिम परीक्षण";
            scoreText = " स्कोर : ";
            masteryText = " महारत : ";
            videoText = "वीडियो";
            foundationalText = "कृपया पहले मुलभुत अध्याय का परिक्षण करें";
            diagnosticTextFirstText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
            alreadyAttemptedText = "आप पहले ही इसका प्रयास कर चुके हैं। आप किसी टॉपिक का डायग्नोस्टिक टेस्ट केवल एक बार ही ले सकते हैं।";
        }
        else {
            practiceText ="Remedial";
            DiagnosticText ="Diagnostic Test";
            FinalText = "Final Test";
            scoreText = " Score : ";
            videoText = "Video";
            masteryText = " Mastery : ";
            foundationalText = "Please do foundational Remedial first";
            diagnosticTextFirstText = "Please do diagnostic test first";
            alreadyAttemptedText = "You have already tried it. You can take the diagnostic test for a topic only once.";
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==SHOW_TOPIC_LISTING) return new PracticeTopicAdapter.ViewItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.pal_row_topics,parent,false));
        else return new PracticeTopicAdapter.ViewItem2(LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_listing_view,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RecyclerView", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, int position) {
        if (viewHolderValue instanceof PracticeTopicAdapter.ViewItem) {
            PracticeTopicAdapter.ViewItem viewHolder = (PracticeTopicAdapter.ViewItem) viewHolderValue;

            /*** Set Default icons , Text ,  text Colour */
            viewHolder.topicListLayout.setEnabled(true);
            activeFoundationTopic=((PalContentListingActivity)context).lastTopicId;

            viewHolder.textViewPracticeScore.setVisibility(View.GONE);
            viewHolder.topicOptionsLayout2.setVisibility(View.GONE);
            viewHolder.textViewDTestScore.setVisibility(View.GONE);
            viewHolder.startDiagnosticTest.setVisibility(View.GONE);
            viewHolder.textViewDNScore.setVisibility(View.GONE);

            viewHolder.textViewdTest.setTextColor(Color.parseColor("#0077FF"));
            viewHolder.textViewDTestScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.startDiagnosticTest.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewdPractice.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewPracticeScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewDNScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#707070"));

            viewHolder.selectItem1.setImageResource(R.drawable.ic_unlocked);
            viewHolder.selectItem3.setImageResource(R.drawable.ic_locked);
            viewHolder.selectItem4.setImageResource(R.drawable.ic_locked);

            viewHolder.badge.setVisibility(View.GONE);

            if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textvideosViewtext.setText("देखे गए वीडियोज़");
            else viewHolder.textvideosViewtext.setText("Learning Time");

            /*** setting data  */
            viewHolder.textViewTopicName.setText(topicsArrayList.get(position).get("TName"));
            viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);

            if (position >= 0 && position < 9) {
                viewHolder.textViewSno.setText("0" + (position + 1) + ". ");
            } else {
                viewHolder.textViewSno.setText((position + 1) + ". ");
            }
            viewHolder.textViewSno.setTextColor(Color.parseColor(global.getColor()));
            viewHolder.textViewSno.setTypeface(null, Typeface.BOLD);

            /*** Show Chapter Diagnostic Test PracticeTest and all */
            if (position == 0) if(this.showDefaultTopic) {
                for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                    PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                    if (data.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))) {
                        Util.setLevel(context,Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel()));
                        break;
                    }
                }
                viewHolder.reletiveParent.setElevation(0.5F);
                viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                viewHolder.diagnosticTestLayout.setVisibility(View.VISIBLE);
                viewHolder.practiceLayout.setVisibility(View.VISIBLE);
                viewHolder.textViewdTest.setVisibility(View.VISIBLE);
                viewHolder.linearPath.setVisibility(View.VISIBLE);
                viewHolder.testLayout.setVisibility(View.VISIBLE);
                viewHolder.textViewdPractice.setText(practiceText);
                viewHolder.textViewdNormalTest.setText(FinalText);
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));
                PalContentListingActivity.showingposition=viewHolder.getAdapterPosition();
                if(Util.isToolTipContentToBeShown(context) && showTooltip) {
                    //show tooltip
                    String message,buttonText,skipText;

                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        message = "यहाँ पर आपके पाठ्यक्रम के अनुसार सभी अध्याय दिए गए हैं";
                        buttonText = "आगे बढ़ें";
                        skipText = "स्किप";
                    }else {
                        message = "All the chapters are given here according to your syllabus";
                        buttonText = "Go ahead";
                        skipText = "Skip";
                    }

                    Util.showTooltip(context, activity, ArrowOrientation.LEFT, "right", message, buttonText,
                            skipText, "contentScreen", viewHolder.textViewTopicName);
                    setViewForDiagnosticTestTextView(viewHolder.textViewdTest);
                    setViewForPracticeTextView(viewHolder.textViewdPractice);
                    setViewForFinalTestTextView(viewHolder.textViewdNormalTest);
                    ((PalContentListingActivity) context).showFullTransparentScreen();
                    setTooltipVisibility(false);
                }
                viewHolder.topicListLayout.setEnabled(false);

                showTopicInfo(viewHolder,position);
            } else {
                viewHolder.reletiveParent.setElevation(0.5F);
                viewHolder.downward_arrow.setImageResource(R.mipmap.down_arrow_listing);
                viewHolder.diagnosticTestLayout.setVisibility(View.GONE);
                viewHolder.practiceLayout.setVisibility(View.GONE);
                viewHolder.testLayout.setVisibility(View.GONE);
                viewHolder.linearPath.setVisibility(View.GONE);
                viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_unselected_chapter));
                viewHolder.topicListLayout.setEnabled(true);
            }
            else {
                if (topicsArrayList.get(position).get("needToShow").equalsIgnoreCase("true")) {

                    for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                        PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                        if (data.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))) {
                            Util.setLevel(context,Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel()));
                            break;
                        }
                    }
                    viewHolder.reletiveParent.setElevation(0.5F);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                    viewHolder.linearPath.setVisibility(View.VISIBLE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.VISIBLE);
                    viewHolder.practiceLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdTest.setVisibility(View.VISIBLE);
                    viewHolder.textViewdPractice.setText(practiceText);
                    viewHolder.testLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdNormalTest.setText(FinalText);
                    viewHolder.textViewTopicName.setTypeface(null, Typeface.NORMAL);
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));
                    PalContentListingActivity.showingposition=viewHolder.getAdapterPosition();
                    viewHolder.topicListLayout.setEnabled(false);

                    showTopicInfo(viewHolder,position);
                } else {
                    viewHolder.reletiveParent.setElevation(0.5F);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.down_arrow_listing);
                    viewHolder.linearPath.setVisibility(View.GONE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.GONE);
                    viewHolder.practiceLayout.setVisibility(View.GONE);
                    viewHolder.testLayout.setVisibility(View.GONE);
                    viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_unselected_chapter));
                    viewHolder.topicListLayout.setEnabled(true);
                }
            }

            /** Checking Student has badge ??*/
            for(String item: ((PalContentListingActivity) context).finalTestTopicIdArrayList)
                if(item.equals(topicsArrayList.get(position).get("TopicID"))) viewHolder.badge.setVisibility(View.VISIBLE);


            /** Click Listeners */
            viewHolder.topicListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Util.setLevel(context,1);
                    System.out.println("======= clicked ");
                    if(!Util.isSubjectboadingDone(context)) Util.setSubjectboadingMode(context,true);
                    viewHolder.topicListLayout.setEnabled(false);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                    ((PalContentListingActivity)context).onClickHandler(viewHolder.getAdapterPosition());
                    PalContentListingActivity.showingposition=viewHolder.getAdapterPosition();
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));

                }
            });

            viewHolder.selectItem1.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.selectItem3.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.selectItem4.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.downward_arrow.setColorFilter(Color.parseColor(global.getColor()));
        }
        else {
            PracticeTopicAdapter.ViewItem2 viewHolder = (PracticeTopicAdapter.ViewItem2) viewHolderValue;

            /*** setting data  */
            viewHolder.textViewTopicName.setText(topicsArrayList.get(position).get("TName"));
            viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);

            if (position >= 0 && position < 9) {
                viewHolder.textViewSno.setText("0" + (position + 1) + ". ");
            } else {
                viewHolder.textViewSno.setText((position + 1) + ". ");
            }
            viewHolder.textViewSno.setTextColor(Color.parseColor(global.getColor()));
            viewHolder.textViewSno.setTypeface(null, Typeface.BOLD);

            viewHolder.badge.setVisibility(View.GONE);

            /** Checking Student has badge ??*/
            for(String item: ((PalContentListingActivity) context).finalTestTopicIdArrayList)
                if(item.equals(topicsArrayList.get(position).get("TopicID"))) viewHolder.badge.setVisibility(View.VISIBLE);

            /** Click Listeners */
            viewHolder.topicListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Util.setLevel(context,1);
                    if(!Util.isSubjectboadingDone(context)) Util.setSubjectboadingMode(context,true);
                    viewHolder.topicListLayout.setEnabled(false);
//                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                    ((PalContentListingActivity)context).onClickHandler(viewHolder.getAdapterPosition());
                    PalContentListingActivity.showingposition=viewHolder.getAdapterPosition();
//                    viewHolder.topicListLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_selected_chapter));

                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showTopicInfo(PracticeTopicAdapter.ViewItem viewHolder, int position) {

        showingPosition=position;

        boolean dignostic_completed=false;

        activeFoundationTopic=((PalContentListingActivity)context).lastTopicId;

        /** set Open TopicID */
        openTopicID=topicsArrayList.get(position).get("TopicID");

        /** Set Score & Mastery of Tests */
        try {
            if (topicsArrayList.get(position).containsKey("isCompleted") && topicsArrayList.get(position).get("isCompleted").equalsIgnoreCase("true")) {
                viewHolder.textViewdTest.setText(DiagnosticText);
                viewHolder.textViewdTest.setTextColor(Color.parseColor("#212121"));
                viewHolder.textViewdPractice.setTextColor(Color.parseColor("#0077FF"));
                viewHolder.selectItem3.setImageResource(R.drawable.ic_unlocked);
                for(PracticeScoreModel item: PalContentListingActivity.practiceScoreModelArrayList)
                    if(item.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))) {
                        if(item.getScore().equals("100")){
                            viewHolder.textViewdPractice.setText(practiceText);
                            viewHolder.textViewdPractice.setTextColor(Color.parseColor("#212121"));
                            viewHolder.selectItem3.setImageResource(R.drawable.ic_tick);
                            //For Final Test
                            viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#0077FF"));
                            viewHolder.selectItem4.setImageResource(R.drawable.ic_unlocked);
                        }else {
                            viewHolder.textViewdPractice.setText(practiceText);
                            viewHolder.textViewdPractice.setTextColor(Color.parseColor("#0077FF"));
//                            viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#212121"));
                            viewHolder.selectItem3.setImageResource(R.drawable.ic_unlocked);
                            viewHolder.selectItem4.setImageResource(R.drawable.ic_locked);
                        }
                        viewHolder.textViewPracticeScore.setVisibility(View.VISIBLE);
                        viewHolder.textViewPracticeScore.setText(masteryText+item.getScore()+"%");
                        setProgressColor(viewHolder.textViewPracticeScore,Integer.parseInt(item.getScore()),TYPE_MASTERY);}

                for(TestScoreModel item: ((PalContentListingActivity) context).testScoreModelArrayList)
                    if(item.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))){
                        if(item.getType().equals("diagnostic_test")) {
                            viewHolder.textViewDTestScore.setVisibility(View.VISIBLE);
                            viewHolder.textViewDTestScore.setText(scoreText +item.getScore());
                            dignostic_completed=true;
                            String[] d=item.getScore().split("/");
                            setProgressColor(viewHolder.textViewDTestScore,Integer.parseInt(d[0]),TYPE_SCORE);

                        } else if(item.getType().equals("simple_test")){
                            if(Integer.parseInt(item.getPercentage())>=10) {
                                viewHolder.textViewdPractice.setText("");
                                viewHolder.textViewdPractice.setText(practiceText);
                                viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#212121"));
                                viewHolder.selectItem4.setImageResource(R.drawable.ic_tick);
                            }else {
                                viewHolder.textViewdPractice.setText(practiceText);
                                viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#0077FF"));
                                viewHolder.selectItem4.setImageResource(R.drawable.ic_unlocked);
                                viewHolder.textViewdPractice.requestFocus();
                            }
                            String[] d=item.getScore().split("/");
                            viewHolder.textViewDNScore.setVisibility(View.VISIBLE);
                            viewHolder.textViewDNScore.setText(scoreText +item.getScore());
                            setProgressColor(viewHolder.textViewDNScore,Integer.parseInt(d[0]),TYPE_SCORE);
                        }else {
                            viewHolder.textViewDTestScore.setVisibility(View.GONE);
                            viewHolder.startDiagnosticTest.setVisibility(View.VISIBLE);
                            viewHolder.textViewDNScore.setVisibility(View.GONE);
                        }
                    }


                viewHolder.selectItem1.setImageResource(R.drawable.ic_tick);
                viewHolder.lockImage.setImageResource(R.drawable.ic_tick);

                viewHolder.selectItem1.setVisibility(View.VISIBLE);



            } else {
                viewHolder.lockImage.setImageResource(R.drawable.ic_tick);

                viewHolder.selectItem1.setVisibility(View.VISIBLE);
                viewHolder.startDiagnosticTest.setVisibility(View.VISIBLE);
                viewHolder.textViewdTest.setText(DiagnosticText);
                viewHolder.lockImage.setVisibility(View.GONE);
                viewHolder.badge.setVisibility(View.GONE);
                viewHolder.diagnosticTestLayout.requestFocus();

            }
        } catch (Exception e) {}

        /** Checking Foundation Topics */
        try {
            String path = topicsArrayList.get(viewHolder.getAdapterPosition()).get("path");
            viewHolder.topicOptionsLayout.removeAllViews();
            boolean first=true;
            boolean firstLine=true;
            if(path != null && ((PalContentListingActivity)context).lastTopicId.equals(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))) {
                String[] pathArray = path.split("-");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(hasChildren(viewHolder.topicOptionsLayout)) viewHolder.topicOptionsLayout.removeAllViews();
                for(int i = 0; i < pathArray.length; i++) {
                    String pa = pathArray[i];
                    switch (pa) {
                        case "F":
                            listArray = new ArrayList<>();
                            if(((PalContentListingActivity)context).foundationalTopicData != null)
                                for(int f=((PalContentListingActivity)context).foundationalTopicData.size()-1;f>=0;f--) {
                                    FoundationalTopicModel item = ((PalContentListingActivity)context).foundationalTopicData.get(f);
                                    if(item.getSeniorTopicID().equals(topicsArrayList.get(position).get("TopicID"))) {
                                        TypedValue outValue1 = new TypedValue();

                                        /** ------------------ Create views ----------------------------*/

                                        /** Main Layout */
                                        LinearLayout foundationalTopicLinearLayout = new LinearLayout(context);
                                        foundationalTopicLinearLayout.setId(ViewCompat.generateViewId());

                                        /** Layout 2 */
                                        RelativeLayout foundationalTopicRelativeLayout = new RelativeLayout(context);

                                        /** Textview for doted line*/
                                        TextView foundationalTopicLine = new TextView(context);

                                        /** Image view for foundation topic  */
                                        ImageView foundationalTopicImageView = new ImageView(context);
                                        foundationalTopicImageView.setId(ViewCompat.generateViewId());

                                        /** create Text View */
                                        TextViewBodyFont textViewBodyFont = new TextViewBodyFont(context);
                                        textViewBodyFont.setId(ViewCompat.generateViewId());

                                        /** ------------------ LAYOUT PARAMS for views ----------------------------*/

                                        /** params for Main Layout */
                                        RelativeLayout.LayoutParams foundationalTopicRelativeParams = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        foundationalTopicRelativeLayout.setLayoutParams(foundationalTopicRelativeParams);

                                        /** Params for Layout 2 */
                                        LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                        foundationalTopicLinearLayout.setLayoutParams(linearParams2);
                                        foundationalTopicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                        foundationalTopicLinearLayout.setBackgroundResource(outValue1.resourceId);

                                        /** params for textview */
                                        RelativeLayout.LayoutParams relativeParams3 = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        relativeParams3.setMargins(10, 0, 0,0);
                                        relativeParams3.addRule(RelativeLayout.RIGHT_OF, foundationalTopicImageView.getId());
//                                            relativeParams3.addRule(RelativeLayout.CENTER_VERTICAL);
                                        textViewBodyFont.setLayoutParams(relativeParams3);                           // set params to view

                                        /** params for doted line */
                                        LinearLayout.LayoutParams foundationalTopicLinearParams = new LinearLayout.LayoutParams(
                                                1,20);
                                        foundationalTopicLinearParams.setMargins(12, 0, 0, 0);
                                        foundationalTopicLine.setLayoutParams(foundationalTopicLinearParams);
                                        foundationalTopicLine.setId(ViewCompat.generateViewId());

                                        /** params for Image view */
                                        RelativeLayout.LayoutParams foundationalTopicRelativeParams2 = new RelativeLayout.LayoutParams(22,22);
                                        foundationalTopicRelativeParams2.addRule(RelativeLayout.CENTER_VERTICAL);
                                        foundationalTopicRelativeParams2.setMargins(0, 0, 0, 0);
                                        foundationalTopicImageView.setLayoutParams(foundationalTopicRelativeParams2);  // set params to view

                                        ArrayList<String> foundationRecord =  new ArrayList<>();
                                        foundationRecord.add(item.getSClass());

                                        /** ------------------ add views in parent layout ------------------ */

                                        /** add doted line */
                                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) foundationalTopicLine.setBackgroundDrawable(ContextCompat.getDrawable(context,  R.color.line) );
                                        else foundationalTopicLine.setBackground(ContextCompat.getDrawable(context, R.color.line));

                                        if(!firstLine) viewHolder.topicOptionsLayout.addView(foundationalTopicLine);
                                        firstLine=false;

                                        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue1, true);

                                        /** set default textview attributes */
                                        textViewBodyFont.setAllCaps(false);
                                        textViewBodyFont.setText(item.getTopicName());
                                        textViewBodyFont.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                                        textViewBodyFont.setTextColor(Color.parseColor("#0077FF"));

                                        /** set default foundation topic locked */
                                        foundationalTopicImageView.setImageResource(R.drawable.ic_locked);
                                        foundationalTopicRelativeLayout.addView(foundationalTopicImageView);


                                        /** set foundation topic info
                                         * practiceScoreModelArrayList -> contain all foundation topics details */
                                        for(int p = PalContentListingActivity.practiceScoreModelArrayList.size()-1; p>=0; p--) {
                                            PracticeScoreModel data= PalContentListingActivity.practiceScoreModelArrayList.get(p);
                                            if (data.getTopicId().equals(item.getTopicId())) {
                                                /** set score of foundation topic */
                                                String score = "<font color = '#707070'> " + masteryText + " " + data.getScore() + "%" + "</font>";
                                                textViewBodyFont.setText(item.getTopicName());

                                                /** show foundation class */
                                                String sclass = item.getSClass().replace("_nonmedical_medical", "").replace("_commerce", "").replace("_arts", "");

                                                if (data.getScore().equals("100")) {
                                                    /** completed foundation topic */
                                                    textViewBodyFont.setTextColor(Color.parseColor("#212121"));
                                                    foundationalTopicImageView.setImageResource(R.drawable.ic_tick);

                                                }
                                                else {
                                                    if (first) {
                                                        /** Active foundation topic */
                                                        textViewBodyFont.setTextColor(Color.parseColor("#0064C8"));
                                                        foundationalTopicImageView.setImageResource(R.drawable.ic_foundation);
                                                        listArray.add(item.getSClass());
                                                        Util.setTopicNameAlt(context,item.getTopicName());
                                                        activeFoundationTopic=item.getTopicId();
                                                        first = false;
                                                    } else {
                                                        /** Locked foundation topic */
                                                        textViewBodyFont.setTextColor(Color.parseColor("#707070"));
                                                        foundationalTopicImageView.setImageResource(R.drawable.ic_locked);
                                                        viewHolder.textViewdPractice.setTextColor(Color.parseColor("#707070"));
                                                        listArray.add(item.getSClass());
                                                    }

                                                    setClassIcon(foundationalTopicImageView,sclass);

                                                    /** set topic practice disable */
                                                    viewHolder.selectItem3.setImageResource(R.drawable.ic_locked);
                                                    viewHolder.textViewdPractice.setTextColor(Color.parseColor("#707070"));
                                                }

                                                textViewBodyFont.setText(Html.fromHtml(item.getTopicName() +" |"+ score));
                                            }
                                        }

                                        foundationalTopicImageView.setColorFilter(Color.parseColor(global.getColor()));

                                        foundationalTopicRelativeLayout.addView(textViewBodyFont);

                                        foundationalTopicLinearLayout.addView(foundationalTopicRelativeLayout);

                                        /** add final layout on parent view */
                                        viewHolder.topicOptionsLayout.addView(foundationalTopicLinearLayout);

                                        foundationalTopicLinearLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Util.preventTwoClick(v);

                                                try {
                                                    ((PalContentListingActivity)context).removeFragment();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                int size = listArray.size()-1;

                                                if (size!=-1) {
                                                    if (listArray.get(0).contains(item.getSClass())) {
                                                        // current live test
                                                        ((PalContentListingActivity)context).firsttime=true;

                                                        int mastery = 0 ;
                                                        int level = 1 ;
                                                        String streekProgress = "0" ;
                                                        for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                            PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                                                            if (data.getTopicId().equals(item.getTopicId())) {
                                                                mastery=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getScore());
                                                                level=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                streekProgress=  PalContentListingActivity.practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                break;
                                                            }
                                                        }
                                                        ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);
                                                    }
                                                    else
                                                        for(PracticeScoreModel data: PalContentListingActivity.practiceScoreModelArrayList) {
                                                            if (data.getTopicId().equals(item.getTopicId()))
                                                                if(data.getScore().equals("100"))
                                                                    // this test is competed
                                                                    Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity.twentyOne);
                                                                else
                                                                    // complete previous test first
                                                                    Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);
                                                        }
                                                }
                                                else
                                                if (!((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice"))
                                                    Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity.twentyOne);
                                                else {
                                                    if (item.getStreakProgress()!="100")
                                                    {
                                                        // when test is completed
                                                        ((PalContentListingActivity)context).firsttime=true;
                                                        int mastery = 0 ;
                                                        int level = 1 ;
                                                        String streekProgress = "0" ;
                                                        for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                            PracticeScoreModel data = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                                                            if (data.getTopicId().equals(item.getTopicId())) {
                                                                mastery=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getScore());
                                                                level=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                streekProgress=  PalContentListingActivity.practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                break;
                                                            }
                                                        }
                                                        ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);
                                                    }else Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity.twentyThree);
                                                }

                                                if(position== PalContentListingActivity.practiceScoreModelArrayList.size())
                                                {
                                                    try
                                                    {
                                                        PracticeScoreModel data= PalContentListingActivity.practiceScoreModelArrayList.get(position);
                                                        if(data.getScore().equals("100")) Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity.twentyOne);
                                                        else {
                                                            ((PalContentListingActivity)context).firsttime=true;
                                                            int mastery = 0 ;
                                                            int level = 1 ;
                                                            String streekProgress = "0" ;
                                                            for(int o = PalContentListingActivity.practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                                PracticeScoreModel dataa = PalContentListingActivity.practiceScoreModelArrayList.get(o);
                                                                if (dataa.getTopicId().equals(item.getTopicId())) {
                                                                    mastery=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getScore());
                                                                    level=  Integer.parseInt(PalContentListingActivity.practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                    streekProgress=  PalContentListingActivity.practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                    break;
                                                                }
                                                            }
                                                            ((PalContentListingActivity)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);
                                                        }
                                                    }
                                                    catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    viewHolder.topicOptionsLayout2.setVisibility(View.VISIBLE);
                                    /** Reverse Layout*/
//                                        LinearLayout ll = viewHolder.topicOptionsLayout;
//                                        ArrayList<View> views = new ArrayList<View>();
//                                        for(int x = 0; x < ll.getChildCount(); x++) views.add(ll.getChildAt(x));
//                                        ll.removeAllViews();
//                                        for(int x = views.size() - 1; x >= 0; x--) ll.addView(views.get(x));
                                }
                            break;
                    }
                }
            }

        } catch (Exception e) {
            viewHolder.practiceLayout.setVisibility(View.GONE);
            viewHolder.testLayout.setVisibility(View.GONE);
        }

        /** Video Watched Count */
        //            ArrayList<String> videoList = new ArrayList<>();
//            for (VideoModel item : ((PalContentListingActivity) context).topicVideoArrayList) {
//                if (item.getTopicId().equals(topicsArrayList.get(position).get("TopicID")) && !videoList.contains(item.getVideoName())) {
////                    float ss = (float) (Integer.parseInt(item.getTime()) / 1000.0);
////
////                    int _sec = (int) ss % 60;
////                    int _mins = (int) (ss / 60) % 60;
////                    int _hours = (int) (ss / (60 * 60)) % 24;
////
////                    String time;
////                    if (_mins == 0) {
////                        time = _sec + " s";
////                    } else {
////                        time = _hours + " h " + _mins + " m "+ _sec + " s";
////                    }
////
////                    videoList.add(item.getVideoName()+" \n        time - "+time);
//                    videoList.add(item.getVideoName());
//                }
//            }
//            viewHolder.textViewvideoWatched.setText(": "+videoList.size());


        /** Show Watched Video List */
        {
            TextView videoLine = new TextView(context);
            videoLine.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoLinearParams = new RelativeLayout.LayoutParams(1,20);
            videoLinearParams.setMargins(11, 0, 0, 0);
            videoLine.setLayoutParams(videoLinearParams);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                videoLine.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.iprep_vertical_dashed_line) );
            } else {
                videoLine.setBackground(ContextCompat.getDrawable(context, R.drawable.iprep_vertical_dashed_line));
            }
//            videoLine.setBackgroundColor(Color.parseColor("#707070"));
//            viewHolder.topicOptionsLayout.addView(videoLine);
//
            LinearLayout videoLinearLayout = new LinearLayout(context);
            videoLinearLayout.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoLinearParams2 = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            videoLinearParams2.addRule(RelativeLayout.BELOW, videoLine.getId());
            videoLinearParams2.setMargins(-4, 0, 0, 0);
            videoLinearLayout.setOrientation(LinearLayout.VERTICAL);
            videoLinearLayout.setLayoutParams(videoLinearParams2);
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            videoLinearLayout.setBackgroundResource(outValue.resourceId);

            RelativeLayout videoRelativeLayout = new RelativeLayout(context);
            videoRelativeLayout.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoRelativeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            videoRelativeLayout.setLayoutParams(videoRelativeParams);

            ImageView videoImageView = new ImageView(context);
            videoImageView.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoRelativeParams2 = new RelativeLayout.LayoutParams(
                    32,32);
            videoRelativeParams2.addRule(RelativeLayout.CENTER_VERTICAL);
            videoRelativeParams2.setMargins(0, 2, 0, 0);
            videoImageView.setLayoutParams(videoRelativeParams2);

            if(dignostic_completed) videoImageView.setImageResource(R.drawable.ic_unlocked);
            else videoImageView.setImageResource(R.drawable.ic_locked);

            videoRelativeLayout.addView(videoImageView);

            TextViewBodyFont videoTextViewBodyFont = new TextViewBodyFont(context);
            videoTextViewBodyFont.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoRelativeParams3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            videoRelativeParams3.setMargins(15, 0, 0,0);
            videoRelativeParams3.addRule(RelativeLayout.RIGHT_OF, videoImageView.getId());
            videoRelativeParams3.addRule(RelativeLayout.CENTER_VERTICAL);
            videoTextViewBodyFont.setLayoutParams(videoRelativeParams3);
            videoTextViewBodyFont.setAllCaps(false);
            videoTextViewBodyFont.setText(videoText);
            videoTextViewBodyFont.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            if(dignostic_completed) videoTextViewBodyFont.setTextColor(Color.parseColor("#0077FF"));
            else videoTextViewBodyFont.setTextColor(Color.parseColor("#707070"));
            videoRelativeLayout.addView(videoTextViewBodyFont);

            ImageView videoDropDownImageView = new ImageView(context);
            videoDropDownImageView.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams videoDropDownRelativeParams = new RelativeLayout.LayoutParams(
                    40, RelativeLayout.LayoutParams.WRAP_CONTENT);
            videoDropDownRelativeParams.addRule(RelativeLayout.RIGHT_OF, videoTextViewBodyFont.getId());
            videoDropDownRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
            videoDropDownRelativeParams.setMargins(4, 0, 0,0);
            videoDropDownImageView.setLayoutParams(videoDropDownRelativeParams);
            videoDropDownImageView.setPadding(2, 2, 2, 2);
            videoDropDownImageView.setImageResource(R.mipmap.down_pal);
            TypedValue videoOutValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, videoOutValue, true);
            videoDropDownImageView.setBackgroundResource(videoOutValue.resourceId);
            videoRelativeLayout.addView(videoDropDownImageView);

            TextViewBodyFont videoListingTextViewBodyFont = new TextViewBodyFont(context);
            videoListingTextViewBodyFont.setId(ViewCompat.generateViewId());
            RelativeLayout.LayoutParams listViewRelativeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            listViewRelativeParams.setMargins(35, 0, 0,0);
            videoListingTextViewBodyFont.setLayoutParams(listViewRelativeParams);
            videoListingTextViewBodyFont.setAllCaps(false);
            videoListingTextViewBodyFont.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
//            videoListingTextViewBodyFont.setTextColor(Color.parseColor("#0077FF"));
            videoListingTextViewBodyFont.setVisibility(View.GONE);



            final boolean[] showList = {true};
            totalsec=0;
            if (showList[0]) {
                ArrayList<String> videoList = new ArrayList<>();
                showList[0] = false;
                for (VideoModel item : ((PalContentListingActivity) context).topicVideoArrayList) {
                    if (item.getTopicId().equals(topicsArrayList.get(position).get("TopicID")) && !videoList.contains(item.getVideoName())) {
                        float ss = (float) (Integer.parseInt(item.getTime()) / 1000.0);
                        totalsec=totalsec+ss;
//                                videoList.add(item.getVideoName()+" \n        time - "+time);
                    }
                }

                int _sec = (int) totalsec % 60;
                int _mins = (int) (totalsec / 60) % 60;
                int _hours = (int) (totalsec / (60 * 60)) % 24;

                String time;
                if (_mins == 0) {
                    time = _sec + " s";
                } else {
                    time = _hours + " h " + _mins + " m "+ _sec + " s";
                }
                viewHolder.textViewvideoWatched.setText(": "+time);

            } else {
                videoListingTextViewBodyFont.setVisibility(View.GONE);
                showList[0] = true;
            }
        }

        viewHolder.diagnosticTestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);

                if(viewHolder.getAdapterPosition()>topicsArrayList.size()) return;

                if(topicsArrayList.get(viewHolder.getAdapterPosition()).containsKey("isCompleted")) {
                    if(topicsArrayList.get(viewHolder.getAdapterPosition()).get("isCompleted").equalsIgnoreCase("false")) {
                        ((PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                        String name = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                        String topicID = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                        Util.setTopicNameAlt(context, name);
                        Util.setTopicID(context, topicID);

                        int pos = 0;
                        if(viewHolder.getAdapterPosition() < topicsArrayList.size() - 1) pos = viewHolder.getAdapterPosition() + 1;

                        Util.preventTwoClick(v);
                        Intent intent = new Intent(context, DStartActivity.class);
                        intent.putExtra("sClass", sClass);
                        intent.putExtra("topicPosition", pos);
                        context.startActivity(intent);
                    }
                    else Util.showAnimatedDialog(context,Util.DIAGNOSTIC_IS_COMPLETED);
                }
                else {
                    ((PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                    String name = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                    String topicID = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                    Util.setTopicNameAlt(context, name);
                    Util.setTopicID(context, topicID);

                    int pos = 0;
                    if(viewHolder.getAdapterPosition() < topicsArrayList.size() - 1) pos = viewHolder.getAdapterPosition() + 1;
                    Util.preventTwoClick(v);
                    Intent intent = new Intent(context, DStartActivity.class);
                    intent.putExtra("sClass", sClass);
                    intent.putExtra("topicPosition", pos);
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.practiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                try {
                    ((PalContentListingActivity)context).removeFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ((PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                    if(((PalContentListingActivity) context).getTopicCompleteStatus(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))){
                        if (((PalContentListingActivity) context).completeType.equalsIgnoreCase("FoundationalPractice")) Util.showAnimatedDialog(context,Util.COMPLETE_FOUNDATION_FIRST);
                        else ((PalContentListingActivity) context).startPractice();
                    } else Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.testLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                try {
                    ((PalContentListingActivity)context).removeFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ((PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                    if(((PalContentListingActivity) context).getTopicCompleteStatus(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))){
                        //Add practice 100% check offline
                        String topicId = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                        String topicName = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                        boolean topicfound=false;

                        int listsize= PalContentListingActivity.practiceScoreModelArrayList.size();
                        int itempos=0;

                        for(PracticeScoreModel item: PalContentListingActivity.practiceScoreModelArrayList){

                            System.out.println("====== item    "+item.getTopicId());
                            System.out.println("====== topicId "+topicId);
                            itempos++;
                            if(item.getTopicId().equals(topicId)){
                                topicfound=true;
                                if(item.getScore().equals("100")) {

                                    Util.setTopicID(context, topicId);
                                    Util.setTopicNameAlt(context, topicName);
                                    if(Util.isOfflineMode(context))
                                    {
                                        context.startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));
                                    }
                                    else
                                    {
                                        if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                                            context.startActivity(new Intent(context, NormalTestActivity.class).putExtra("sClass", sClass));
                                        } else {
                                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                            {
                                                Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                            }else {
                                                Util.openGifDialogue(context,"Internet Connection is not working");
                                            }
                                        }

                                    }

                                }
                                else {
                                    Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);
//                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
//                                        {
//                                            //Util.showToast(context, "कृपया अंतिम परीक्षा देने के लिए "+ topicName +" में 100% महारत हासिल करें");
//                                            Util.openGifDialogue(context, "कृपया अंतिम परीक्षा देने के लिए "+ "अभ्यास" +" में 100% महारत हासिल करें");
//                                        }else {
//                                            //Util.showToast(context, "Please master 100% of "+topicName+" to take the final exam");
//                                            Util.openGifDialogue(context, "Please master 100% of "+"Practice Test"+" to take the final exam");
//
//                                        }
                                }



                            }
                            else {
                                System.out.println("--------- clicked 2 ");

                                if(!topicfound)
                                {
                                    if(listsize==itempos)
                                    {
                                        Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);
                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                        {
                                            //Util.showToast(context, "कृपया अंतिम परीक्षा देने के लिए "+ topicName +" में 100% महारत हासिल करें");
                                            Util.openGifDialogue(context, "कृपया अंतिम परीक्षा देने के लिए "+ "अभ्यास" +" में 100% महारत हासिल करें");
                                        }else {
                                            //Util.showToast(context, "Please master 100% of "+topicName+" to take the final exam");
                                            Util.openGifDialogue(context, "Please master 100% of "+"Practice Test"+" to take the final exam");

                                        }
                                    }

//                                        Util.openGifDialogue(context, diagnosticTextFirstText);
                                }


                            }
                        }

                        if(listsize==0) Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);

                    }else
                        Util.showAnimatedDialog(context,Util.COMPLETE_PRACTICE_FIRST);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.textvideosViewtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    context.startActivity(new Intent(context, ReportListActivity.class).putExtra("type", "video_lessons").
//                            putExtra("section", viewHolder.textvideosViewtext.getText().toString()));
            }
        });
    }


    private void setClassIcon(ImageView imageView, String sclass) {
        if(sclass.equals("3")) imageView.setImageResource(R.drawable.ic_foundation_3);
        else if(sclass.equals("4")) imageView.setImageResource(R.drawable.ic_foundation_4);
        else if(sclass.equals("5")) imageView.setImageResource(R.drawable.ic_foundation_5);
        else if(sclass.equals("6")) imageView.setImageResource(R.drawable.ic_foundation_6);
        else if(sclass.equals("7")) imageView.setImageResource(R.drawable.ic_foundation_7);
        else if(sclass.equals("8")) imageView.setImageResource(R.drawable.ic_foundation_8);
        else if(sclass.equals("9")) imageView.setImageResource(R.drawable.ic_foundation_9);
        else if(sclass.equals("10")) imageView.setImageResource(R.drawable.ic_foundation_10);
        else if(sclass.equals("11")) imageView.setImageResource(R.drawable.ic_foundation_11);
        else if(sclass.equals("12")) imageView.setImageResource(R.drawable.ic_foundation_12);
        imageView.setColorFilter(Color.parseColor(global.getColor()));
    }

    private String TYPE_MASTERY="Mastery";
    private String TYPE_SCORE="Score";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setProgressColor(TextView view, int progress, String type){
//        if(type.equals(TYPE_MASTERY)){
//            if(progress<=50) {
//                view.setTextColor(context.getResources().getColor(R.color.yellorIprep));
////                view.setBackgroundColor(context.getResources().getColor(R.color.yellorTransIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.yellorTransIprep));
//            }
//            else if(progress<100) {
//                view.setTextColor(context.getResources().getColor(R.color.blueIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.blueTransIprep));
//            }
//            else {
//                view.setTextColor(context.getResources().getColor(R.color.greenIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.greenTransIprep));
//            }
//        }
//        else if(type.equals(TYPE_SCORE)){
//            if(progress<=3) {
//                view.setTextColor(context.getResources().getColor(R.color.yellorIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.yellorTransIprep));
//            }
//            else if(progress<12) {
//                view.setTextColor(context.getResources().getColor(R.color.blueIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.blueTransIprep));
//            }
//            else {
//                view.setTextColor(context.getResources().getColor(R.color.greenIprep));
////                view.getBackground().setTint(context.getResources().getColor(R.color.greenTransIprep));
//            }
//        }
    }

    private void setBackgroundProgressColor(TextView view,int progress,String type){
        if(type.equals(TYPE_MASTERY)){
            if(progress<=50)
                view.setTextColor(context.getResources().getColor(R.color.yellorTransIprep));
            else if(progress<100)
                view.setTextColor(context.getResources().getColor(R.color.blueTransIprep));
            else
                view.setTextColor(context.getResources().getColor(R.color.greenTransIprep));
        }
        else if(type.equals(TYPE_SCORE)){
            if(progress<=3)
                view.setTextColor(context.getResources().getColor(R.color.yellorTransIprep));
            else if(progress<12)
                view.setTextColor(context.getResources().getColor(R.color.blueTransIprep));
            else
                view.setTextColor(context.getResources().getColor(R.color.greenTransIprep));
        }
    }

    public void setTooltipVisibility(boolean visibility){
        this.showTooltip = visibility;
    }

    public void showDefaultTopic(boolean show){
        this.showDefaultTopic = show;
    }

    public boolean getShowDefaultTopic(){
        return this.showDefaultTopic;
    }

    public void setViewForDiagnosticTestTextView(View view){
        this.diagnosticTestTextView = view;
    }

    public View getViewForDiagnosticTestTextView(){
        return diagnosticTestTextView;
    }

    public void setViewForPracticeTextView(View view){
        this.practiceTextView = view;
    }

    public View getViewForPracticeTextView(){
        return practiceTextView;
    }

    public void setViewForFinalTestTextView(View view){
        this.finalTestTextView = view;
    }

    public View getViewForFinalTestTextView(){
        return finalTestTextView;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==showingPosition) return SHOW_TOPIC_LISTING;
        else return SHOW_ONLY_NAME;
    }
    @Override
    public int getItemCount() {
        return topicsArrayList.size();
    }

    public void SetOnItemClickListener(final PracticeTopicAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static boolean hasChildren(ViewGroup viewGroup) {
        return viewGroup.getChildCount() > 0;
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTopicName;
        protected TextView textViewSno;
        protected TextView btnReports;
        protected LinearLayout diagnosticTestLayout;
        protected LinearLayout topicOptionsLayout2;
        protected TextView textViewdTest;
        protected LinearLayout testLayout;
        protected LinearLayout practiceLayout;
        protected TextView textViewPath;
        protected RelativeLayout linearPath;
        protected ImageView lockImage;
        protected ImageView badge;
        protected ImageView downward_arrow;
        protected LinearLayout topicListLayout;
        protected RelativeLayout reletiveParent;
        //        protected ImageView downBtn;
//        protected RecyclerView videosRecyclerView;
        protected ImageView selectItem1;
        protected ImageView selectItem3;
        protected ImageView selectItem4;

        protected TextView line2;
        protected View line3;
        protected View line4;
        protected LinearLayout topicOptionsLayout;
        protected TextView textViewDTestScore,startDiagnosticTest;
        protected TextView textViewPracticeScore;
        protected TextView textViewDNScore;
        protected TextView textViewdNormalTest;
        protected TextView textViewdPractice;
        protected TextView textvideosViewtext;
        protected TextView textViewvideoWatched;

        public ViewItem(View holderView) {
            super(holderView);
            lockImage = holderView.findViewById(R.id.lockImage);
            badge = holderView.findViewById(R.id.badge);
            downward_arrow = holderView.findViewById(R.id.downward_arrow);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewSno = holderView.findViewById(R.id.textViewSno);
            diagnosticTestLayout = holderView.findViewById(R.id.diagnosticTestLayout);
            textViewdTest = holderView.findViewById(R.id.textViewdTest);
            textViewDTestScore = holderView.findViewById(R.id.textViewDTestScore);
            startDiagnosticTest = holderView.findViewById(R.id.startDiagnosticTest);
            btnReports = holderView.findViewById(R.id.btnReports);

            testLayout = holderView.findViewById(R.id.testLayout);
            textViewdNormalTest = holderView.findViewById(R.id.textViewdNormalTest);
            textViewDNScore = holderView.findViewById(R.id.textViewDNScore);
            linearPath = holderView.findViewById(R.id.linearPath);
            practiceLayout = holderView.findViewById(R.id.practiceLayout);
            textViewdPractice = holderView.findViewById(R.id.textViewdPractice);
            textViewPracticeScore = holderView.findViewById(R.id.textViewPracticeScore);

            GridLayoutManager manager = new GridLayoutManager(context, 1);

            topicListLayout = holderView.findViewById(R.id.topicListLayout);
            reletiveParent = holderView.findViewById(R.id.reletiveParent);
            selectItem1 = holderView.findViewById(R.id.selectItem1);
            selectItem3 = holderView.findViewById(R.id.selectItem3);
            selectItem4 = holderView.findViewById(R.id.selectItem4);


            line3 = holderView.findViewById(R.id.line3);

            textViewvideoWatched = holderView.findViewById(R.id.textViewvideoWatched);
            topicOptionsLayout = holderView.findViewById(R.id.topicOptionsLayout);
            textvideosViewtext = holderView.findViewById(R.id.textvideosViewtext);
            topicOptionsLayout2 = holderView.findViewById(R.id.topicOptionsLayout2);


            if(Util.getSelectedLanguage(context).equals("hindi")) startDiagnosticTest.setText("शुरू करे");
            else startDiagnosticTest.setText("Start");

        }

        @Override
        public void onClick(View view) {
//            clickListener.onItemClick(view, getPosition());
        }
    }

    public class ViewItem2 extends RecyclerView.ViewHolder  {

        protected TextView textViewTopicName;
        protected TextView textViewSno;

        protected LinearLayout diagnosticTestLayout;
        protected LinearLayout topicOptionsLayout2;
        protected TextView textViewdTest;
        protected LinearLayout testLayout;
        protected LinearLayout practiceLayout;
        protected TextView textViewPath;
        protected RelativeLayout linearPath;
        protected ImageView lockImage;
        protected ImageView badge;
        protected ImageView downward_arrow;
        protected LinearLayout topicListLayout;
        protected RelativeLayout reletiveParent;
        //        protected ImageView downBtn;
//        protected RecyclerView videosRecyclerView;
        protected ImageView selectItem1;
        protected ImageView selectItem3;
        protected ImageView selectItem4;

        protected TextView line2;
        protected View line3;
        protected View line4;
        protected LinearLayout topicOptionsLayout;
        protected TextView textViewDTestScore,startDiagnosticTest;
        protected TextView textViewPracticeScore;
        protected TextView textViewDNScore;
        protected TextView textViewdNormalTest;
        protected TextView textViewdPractice;
        protected TextView textvideosViewtext;
        protected TextView textViewvideoWatched;

        public ViewItem2 (View holderView) {
            super(holderView);

            topicListLayout=holderView.findViewById(R.id.topicListLayout);
            textViewSno=holderView.findViewById(R.id.textViewSno);
            textViewTopicName=holderView.findViewById(R.id.textViewTopicName);
            badge=holderView.findViewById(R.id.badge);


        }

    }

}