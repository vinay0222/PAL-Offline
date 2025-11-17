package com.idreameducation.ipreppal.PalMobile.adapter;


import static com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.actvePosition;
import static com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.viewPagerPos;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.idreameducation.ipreppal.PalMobile.activity.NormalTestActivity;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.PracticeScoreModel;
import com.idreameducation.ipreppal.model.TestScoreModel;
import com.idreameducation.ipreppal.pal.activity.DStartActivity;
import com.idreameducation.ipreppal.pal.activity.ReportsActivity;
import com.idreameducation.ipreppal.pal.adapter.PracticeTopicAdapter;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.util.TextViewBodyFont;
import com.idreameducation.ipreppal.util.Util;
import com.skydoves.balloon.ArrowOrientation;

import java.util.ArrayList;
import java.util.HashMap;


public class PracticeTopicAdapter_Mobile extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private OnItemClickListener clickListener;
    private final String sClass;
    private final Global global;
    private final Context context;
    public ArrayList<HashMap<String, String>> topicsArrayList;
    private boolean showDefaultTopic;
    private final FragmentActivity activity;
    private View diagnosticTestTextView;
    private View practiceTextView;
    private View finalTestTextView;
    private boolean showTooltip = false;
    private final String practiceText;
    private final String DiagnosticText;
    private final String FinalText;
    private final String scoreText;
    private final String masteryText;
    private final String alreadyAttemptedText;
    private final String foundationalText;
    private final String diagnosticTextFirstText;
    private final String videoText;
    private  ArrayList<String> listArray;


    private float totalsec=0;
    public static int showingPosition=0;
    int SHOW_TOPIC_LISTING=20222;
    int SHOW_ONLY_NAME=2416;
    public PracticeTopicAdapter_Mobile(Context context, ArrayList<HashMap<String, String>> topicsArrayList, String sClass, FragmentActivity activity) {
        this.context = context;
        this.topicsArrayList = topicsArrayList;
        this.sClass = sClass;
        this.activity = activity;
        global = (Global) context.getApplicationContext();

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            practiceText ="रेमेडियल";
            DiagnosticText ="डायग्नोस्टिक परीक्षण";
            FinalText = "अंतिम परीक्षण";
            scoreText = " स्कोर : ";
            masteryText = " महारत : ";
            videoText = "वीडियो";
            foundationalText = "कृपया पहले मुलभुत अध्याय का परिक्षण करें";
            diagnosticTextFirstText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
            alreadyAttemptedText = "आप पहले ही इसका प्रयास कर चुके हैं। आप किसी टॉपिक का डायग्नोस्टिक टेस्ट केवल एक बार ही ले सकते हैं।";
        }else {
            practiceText ="Remedial";
            DiagnosticText ="Diagnostic Test";
            FinalText = "Final Test";
            scoreText = " Score : ";
            videoText = "Video";
            masteryText = " Mastery : ";
            foundationalText = "Please do foundational practice first";
            diagnosticTextFirstText = "Please do diagnostic test first";
            alreadyAttemptedText = "You have already tried it. You can take the diagnostic test for a topic only once.";
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==SHOW_TOPIC_LISTING) return new ViewItem(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pal_row_topics, viewGroup, false));
       else return new ViewItem2(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.topic_listing_view, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, int position) {
        if (viewHolderValue instanceof PracticeTopicAdapter_Mobile.ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;

            showingPosition=position;
            actvePosition=position;

            /** Setup default colour & icons in views */
            viewHolder.textViewPracticeScore.setVisibility(View.GONE);
            viewHolder.textViewDTestScore.setVisibility(View.GONE);
            viewHolder.textViewDNScore.setVisibility(View.GONE);
            viewHolder.startDiagnosticTest.setVisibility(View.GONE);
            viewHolder.topicOptionsLayout2.setVisibility(View.GONE);

            viewHolder.topicListLayout.setEnabled(true);

            viewHolder.textViewdTest.setTextColor(Color.parseColor("#0077FF"));
            viewHolder.textViewDTestScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewdPractice.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewPracticeScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewDNScore.setTextColor(Color.parseColor("#707070"));
            viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#707070"));
            viewHolder.startDiagnosticTest.setTextColor(Color.parseColor("#707070"));

            viewHolder.selectItem1.setImageResource(R.drawable.ic_unlocked);
            viewHolder.selectItem3.setImageResource(R.drawable.ic_locked);
            viewHolder.selectItem4.setImageResource(R.drawable.ic_locked);
            Animation animation= AnimationUtils.loadAnimation(context, R.anim.slide_in_fromup);

            try {
                viewHolder.textViewSno.setTextColor(Color.parseColor(global.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewHolder.textViewSno.setTypeface(null, Typeface.BOLD);

            viewHolder.textViewTopicName.setText(topicsArrayList.get(position).get("TName"));
            viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);

            if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textvideosViewtext.setText("देखे गए वीडियोज़");
            else viewHolder.textvideosViewtext.setText("Learning Time");

            /** set topic serial no */
            if (position >= 0 && position < 9) viewHolder.textViewSno.setText("0" + (position + 1) + ". ");
            else viewHolder.textViewSno.setText((position + 1) + ". ");

            /** Show selected topic */
            if (position == 0) {
                if(this.showDefaultTopic) {
                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                    viewHolder.reletiveParent.setElevation(2);

                    viewHolder.actions_layout.setVisibility(View.VISIBLE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdTest.setVisibility(View.VISIBLE);
                    viewHolder.practiceLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdPractice.setText(practiceText);
                    viewHolder.testLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdNormalTest.setText(FinalText);

                    Util.setSeniorTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
                    Util.setTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
                    PalContentListingActivity_Mobile.palContentListingActivityMobile.topicID=topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");

                    viewHolder.linearPath.setVisibility(View.VISIBLE);
//                    viewHolder.linearPath.startAnimation(animation);
                    viewHolder.linearPath.setVisibility(View.VISIBLE);
                    viewHolder.actions_layout.setVisibility(View.VISIBLE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
//                    viewHolder.actions_layout.startAnimation(animation);
//                    viewHolder.timeLayout.startAnimation(animation);
                    viewHolder.actions_layout.setVisibility(View.VISIBLE);

                    viewHolder.topicListLayout.setEnabled(false);

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
//                        ((PalContentListingActivity) context).showFullTransparentScreen();
                        setTooltipVisibility(false);
                    }
                } else {
                    viewHolder.reletiveParent.setElevation(0.5F);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.down_arrow_listing);
                    viewHolder.linearPath.setVisibility(View.GONE);
                    viewHolder.actions_layout.setVisibility(View.GONE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.GONE);
                    viewHolder.practiceLayout.setVisibility(View.GONE);
                    viewHolder.testLayout.setVisibility(View.GONE);
                    viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);

                    viewHolder.topicListLayout.setEnabled(true);

                }
            } else {
                if (topicsArrayList.get(position).get("needToShow").equalsIgnoreCase("true")) {
                    viewHolder.reletiveParent.setElevation(2F);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
                    viewHolder.actions_layout.setVisibility(View.VISIBLE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.VISIBLE);
                    viewHolder.practiceLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdTest.setVisibility(View.VISIBLE);
                    viewHolder.textViewdPractice.setText(practiceText);
                    viewHolder.testLayout.setVisibility(View.VISIBLE);
                    viewHolder.textViewdNormalTest.setText(FinalText);
                    viewHolder.textViewTopicName.setTypeface(null, Typeface.NORMAL);

                    Util.setTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
                    Util.setSeniorTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
                    PalContentListingActivity_Mobile.palContentListingActivityMobile.topicID=topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                    PalContentListingActivity_Mobile.currentTopicid =topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                    PalContentListingActivity_Mobile.palContentListingActivityMobile.clss=Util.getSelectedClass(context);
                    PalContentListingActivity_Mobile.palContentListingActivityMobile.subjec=Util.getSubject(context);

                    viewHolder.linearPath.setVisibility(View.VISIBLE);
//                    viewHolder.linearPath.startAnimation(animation);
                    viewHolder.linearPath.setVisibility(View.VISIBLE);

                    viewHolder.actions_layout.setVisibility(View.VISIBLE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
//                    viewHolder.actions_layout.startAnimation(animation);
//                    viewHolder.timeLayout.startAnimation(animation);
                    viewHolder.actions_layout.setVisibility(View.VISIBLE);

                    viewHolder.topicListLayout.setEnabled(false);

                } else {
                    viewHolder.reletiveParent.setElevation(0.5F);
                    viewHolder.downward_arrow.setImageResource(R.mipmap.down_arrow_listing);
                    viewHolder.linearPath.setVisibility(View.GONE);
                    viewHolder.actions_layout.setVisibility(View.GONE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
                    viewHolder.diagnosticTestLayout.setVisibility(View.GONE);
                    viewHolder.practiceLayout.setVisibility(View.GONE);
                    viewHolder.testLayout.setVisibility(View.GONE);
                    viewHolder.textViewTopicName.setTypeface(null,Typeface.NORMAL);

                    viewHolder.topicListLayout.setEnabled(true);

                }
            }

            /** Click Listeners  */
            viewHolder.topicListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
//                    Util.setTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
//                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
//                    ((PalContentListingActivity_Mobile)context).onClickHandler(viewHolder.getAdapterPosition());
                }
            });

            /** end all processes if topic is not opened */
            if(viewHolder.topicListLayout.isEnabled()) return;

            /** show score of dignostic , practice and final test */
            try {
                if (topicsArrayList.get(position).containsKey("isCompleted") && topicsArrayList.get(position).get("isCompleted").equalsIgnoreCase("true")) {
                    viewHolder.textViewdTest.setText(DiagnosticText);
                    viewHolder.textViewdTest.setTextColor(Color.parseColor("#212121"));
                    viewHolder.textViewdPractice.setTextColor(Color.parseColor("#0077FF"));
                    viewHolder.selectItem3.setImageResource(R.drawable.ic_unlocked);
                    for(PracticeScoreModel item: ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList){
                        if(item.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))){

                            viewHolder.textViewPracticeScore.setVisibility(View.VISIBLE);
                            viewHolder.textViewPracticeScore.setText(masteryText+item.getScore()+"%");

                            System.out.println("========== item.getScore() "+item.getScore());

                            if(item.getScore().equals("100")){
                                viewHolder.textViewdPractice.setText(practiceText);
                                viewHolder.textViewdPractice.setTextColor(Color.parseColor("#212121"));
                                viewHolder.selectItem3.setImageResource(R.drawable.ic_tick);
                                //For Final Test
                                viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#0077FF"));
                                viewHolder.selectItem4.setImageResource(R.drawable.ic_unlocked);
                            }else{
                                viewHolder.textViewdPractice.setText(practiceText);
                                viewHolder.textViewdPractice.setTextColor(Color.parseColor("#0077FF"));
                                viewHolder.selectItem3.setImageResource(R.drawable.ic_unlocked);
                            }
                        }else{
//                            viewHolder.textViewPracticeScore.setVisibility(View.GONE);
                        }
                    }
                    for(TestScoreModel item: ((PalContentListingActivity_Mobile) context).testScoreModelArrayList){
                        if(item.getTopicId().equals(topicsArrayList.get(position).get("TopicID"))){
                            if(item.getType().equals("diagnostic_test")){
                                viewHolder.textViewDTestScore.setVisibility(View.VISIBLE);
                                viewHolder.textViewDTestScore.setText(scoreText +item.getScore());
                            } else if(item.getType().equals("simple_test")){
                                if(item.getPercentage().equals("100")){
                                    viewHolder.textViewdPractice.setText("");
                                    viewHolder.textViewdPractice.setText(practiceText);
                                    viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#212121"));
                                    viewHolder.selectItem4.setImageResource(R.drawable.ic_tick);
                                }else{
                                    viewHolder.textViewdPractice.setText(practiceText);
                                    viewHolder.textViewdNormalTest.setTextColor(Color.parseColor("#0077FF"));
                                    viewHolder.selectItem4.setImageResource(R.drawable.ic_unlocked);
                                }
                                viewHolder.textViewDNScore.setVisibility(View.VISIBLE);
                                viewHolder.textViewDNScore.setText(scoreText +item.getScore());
                            }else{
                                viewHolder.textViewDTestScore.setVisibility(View.GONE);
                                viewHolder.textViewDNScore.setVisibility(View.GONE);
                                viewHolder.startDiagnosticTest.setVisibility(View.VISIBLE);
                            }
                        }
                    }
//                    viewHolder.lockImage.setImageResource(R.drawable.ic_tick);
//                    viewHolder.reletiveParent.setAlpha(1f);
                    viewHolder.selectItem1.setVisibility(View.VISIBLE);
                    viewHolder.selectItem1.setImageResource(R.drawable.ic_tick);
//                    viewHolder.textViewdTestDate.setVisibility(View.GONE);


                } else {

//                    viewHolder.lockImage.setVisibility(View.GONE);
                    viewHolder.badge.setVisibility(View.GONE);
//                    viewHolder.lockImage.setImageResource(R.drawable.ic_tick);
                    viewHolder.textViewdTest.setText(DiagnosticText);
                    viewHolder.selectItem1.setVisibility(View.VISIBLE);
//                    viewHolder.textViewdTestDate.setVisibility(View.GONE);
                    viewHolder.actions_layout.setVisibility(View.GONE);
                    viewHolder.timeLayout.setVisibility(View.GONE);
                    viewHolder.startDiagnosticTest.setVisibility(View.VISIBLE);
                    viewHolder.diagnosticTestLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.preventTwoClick(v);

                            if(topicsArrayList.get(viewHolder.getAdapterPosition()).containsKey("isCompleted")){
                                if(topicsArrayList.get(viewHolder.getAdapterPosition()).get("isCompleted").equalsIgnoreCase("false")){
//                                    ((com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                                    String name = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                                    String topicID = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                                    Util.setTopicNameAlt(context, name);
                                    Util.setTopicID(context, topicID);

                                    int pos = 0;
                                    if(viewHolder.getAdapterPosition() < topicsArrayList.size() - 1){
                                        pos = viewHolder.getAdapterPosition() + 1;
                                    }
                                    Util.preventTwoClick(v);
                                    Intent intent = new Intent(context, DStartActivity.class);
                                    intent.putExtra("sClass", sClass);
                                    intent.putExtra("topicPosition", pos);
                                    context.startActivity(intent);
                                }
                                else{
                                    Util.openGifDialogue(context,alreadyAttemptedText);
                                    // Toast.makeText(context, alreadyAttemptedText, Toast.LENGTH_LONG).show();
                                }
                            }else{
//                                ((com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                                String name = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                                String topicID = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                                Util.setTopicNameAlt(context, name);
                                Util.setTopicID(context, topicID);

                                int pos = 0;
                                if(viewHolder.getAdapterPosition() < topicsArrayList.size() - 1){
                                    pos = viewHolder.getAdapterPosition() + 1;
                                }
                                Util.preventTwoClick(v);
                                Intent intent = new Intent(context, DStartActivity.class);
                                intent.putExtra("sClass", sClass);
                                intent.putExtra("topicPosition", pos);
                                context.startActivity(intent);
                            }
                        }
                    });
                }

                /** Check for badge */
                for(String item: ((PalContentListingActivity_Mobile) context).finalTestTopicIdArrayList){
                    if(item.equals(topicsArrayList.get(position).get("TopicID"))){
                        viewHolder.badge.setVisibility(View.VISIBLE);
                    }else{
                        viewHolder.badge.setVisibility(View.GONE);
                    }
                }

            } catch (Exception e) {}

            /** Check foundation details */
            try {
                String path = topicsArrayList.get(viewHolder.getAdapterPosition()).get("path");
                boolean first=true;
                boolean textFirst=true;
                boolean firstLine=true;
                if(path != null && ((PalContentListingActivity_Mobile)context).lastTopicId.equals(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))) {
                    String[] pathArray = path.split("-");
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(hasChildren(viewHolder.topicOptionsLayout)) {
                        viewHolder.topicOptionsLayout.removeAllViews();
                    }
                    for (int i = 0; i < pathArray.length; i++) {
                        String pa = pathArray[i];
                        switch (pa) {
                            case "F":
                                listArray = new ArrayList<>();
                                if(((PalContentListingActivity_Mobile)context).foundationalTopicData != null)
                                    for(int f = ((PalContentListingActivity_Mobile)context).foundationalTopicData.size()-1; f>=0; f--) {
                                        FoundationalTopicModel item = ((PalContentListingActivity_Mobile)context).foundationalTopicData.get(f);
                                        viewHolder.topicOptionsLayout2.setVisibility(View.VISIBLE);
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
                                            textViewBodyFont.setLayoutParams(relativeParams3);                           // set params to view

                                            /** params for doted line */
                                            LinearLayout.LayoutParams foundationalTopicLinearParams = new LinearLayout.LayoutParams(
                                                    1,20);
                                            foundationalTopicLinearParams.setMargins(12, 0, 0, 0);
                                            foundationalTopicLine.setLayoutParams(foundationalTopicLinearParams);
                                            foundationalTopicLine.setId(ViewCompat.generateViewId());

                                            /** params for Image view */
                                            RelativeLayout.LayoutParams foundationalTopicRelativeParams2 = new RelativeLayout.LayoutParams(25,25);
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
                                            textViewBodyFont.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                            textViewBodyFont.setTextColor(Color.parseColor("#0077FF"));

                                            /** set default foundation topic locked */
                                            foundationalTopicImageView.setImageResource(R.drawable.ic_locked);
                                            foundationalTopicRelativeLayout.addView(foundationalTopicImageView);


                                            /** Set Score of foundation practice  */
                                            for(int o = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                PracticeScoreModel data=((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o);
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

                                                            PracticeTopicAdapter.activeFoundationTopic=item.getTopicId();
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

                                                    textViewBodyFont.setText(Html.fromHtml(item.getTopicName() +" | "+ score));
                                                }
                                            }

                                            foundationalTopicRelativeLayout.addView(textViewBodyFont);

                                            foundationalTopicLinearLayout.addView(foundationalTopicRelativeLayout);

                                            /** add final layout on parent view */
                                            viewHolder.topicOptionsLayout.addView(foundationalTopicLinearLayout);
                                            foundationalTopicLinearLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Util.preventTwoClick(v);

                                                    try {
                                                        ((PalContentListingActivity_Mobile)context).removeFragment();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    int size = listArray.size()-1;

                                                    if (size!=-1)
                                                        if (listArray.get(0).contains(item.getSClass())) {   System.out.println("======= 1");
                                                            // current live test
                                                            ((PalContentListingActivity_Mobile)context).firsttime=true;

                                                            int mastery = 0 ;
                                                            int level = 1 ;
                                                            String streekProgress = "0" ;
                                                            for(int o = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size()-1; o>=0; o--) {

                                                                PracticeScoreModel data = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o);
                                                                if (data.getTopicId().equals(item.getTopicId())) {
                                                                    mastery=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getScore());
                                                                    level=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                    streekProgress=  ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                    break;
                                                                }
                                                            }
                                                            ((PalContentListingActivity_Mobile)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);

                                                        }
                                                        else {
                                                            // other tests
                                                            for(PracticeScoreModel data: ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList) {

                                                                if (data.getTopicId().equals(item.getTopicId())) {

                                                                    if(data.getScore().equals("100")){
                                                                        // this test is competed
                                                                        Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity_Mobile.twentyOne);
                                                                    }else{
                                                                        // complete previous test first
                                                                        Util.openGifDialogue(context, PalContentListingActivity_Mobile.twentyTwo);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    else
                                                    if (!PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice")) Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity_Mobile.twentyOne);
                                                    else
                                                    if (item.getStreakProgress()!="100") {
                                                        // when test is completed
                                                        ((PalContentListingActivity_Mobile)context).firsttime=true;
                                                        int mastery = 0 ;
                                                        int level = 1 ;
                                                        String streekProgress = "0" ;
                                                        for(int o = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                            PracticeScoreModel data = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o);
                                                            if (data.getTopicId().equals(item.getTopicId())) {
                                                                mastery=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getScore());
                                                                level=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                streekProgress=  ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                break;
                                                            }
                                                        }
                                                        ((PalContentListingActivity_Mobile)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);

                                                    }else Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity_Mobile.twentyThree);

                                                    if(position==((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size()) {
                                                        try {
                                                            PracticeScoreModel data=((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(position);
                                                            if(data.getScore().equals("100")) Util.openGifDialogueSuccess(context,Util.getUsername(context)+" "+ PalContentListingActivity_Mobile.twentyOne);
                                                            else {
                                                                ((PalContentListingActivity_Mobile)context).firsttime=true;
                                                                int mastery = 0 ;
                                                                int level = 1 ;
                                                                String streekProgress = "0" ;
                                                                for(int o = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size()-1; o>=0; o--) {
                                                                    PracticeScoreModel dataa = ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o);
                                                                    if (dataa.getTopicId().equals(item.getTopicId())) {
                                                                        mastery=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getScore());
                                                                        level=  Integer.parseInt(((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getCurrentLevel());
                                                                        streekProgress=  ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.get(o).getStreakProgress();
                                                                        break;
                                                                    }
                                                                }
                                                                ((PalContentListingActivity_Mobile)context).showFoundationalTopicDialog(Util.getUserId(context), Util.getSubject(context), item.getSeniorTopicID(), item.getTopicId(), item.getTopicName(), item.getSeniorClass(), 0, viewHolder.getAdapterPosition(),mastery,streekProgress,level);
                                                            }
                                                        }
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                viewHolder.practiceLayout.setVisibility(View.GONE);
                viewHolder.testLayout.setVisibility(View.GONE);
            }

            /** total learning time */
            final boolean[] showList = {true};
            totalsec=0;
            if (showList[0]) {
                ArrayList<String> videoList = new ArrayList<>();
                showList[0] = false;
                for (VideoModel item : ((PalContentListingActivity_Mobile) context).topicVideoArrayList) {
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
                showList[0] = true;
            }

            viewHolder.practiceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);

                    try {
//                        ((com.idreameducation.ipreppal.PalMobile.activity.com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                        if(PalContentListingActivity_Mobile.instance.getTopicCompleteStatus(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))){
                            if (PalContentListingActivity_Mobile.completeType.equalsIgnoreCase("FoundationalPractice"))
                            {
                                // Util.showToast(context, foundationalText);
                                Util.openGifDialogue(context, foundationalText);
                            }else {
                                PalContentListingActivity_Mobile.instance.startPractice(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
                            }

                        }else{
                            //  Util.showToast(context, diagnosticTextFirstText);
                            Util.openGifDialogue(context, diagnosticTextFirstText);
                        }
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
//                        ((com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity)context).removeFragment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
//                        ((com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity)context).hideTransparentScreenForTopicSelection();
                        if(((PalContentListingActivity_Mobile) context).getTopicCompleteStatus(topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"))){
                            //Add practice 100% check offline
                            String topicId = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID");
                            String topicName = topicsArrayList.get(viewHolder.getAdapterPosition()).get("TName");
                            boolean topicfound=false;

                            int listsize=((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList.size();
                            int itempos=0;

                            for(PracticeScoreModel item: ((PalContentListingActivity_Mobile) context).practiceScoreModelArrayList){

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
                                            if (Util.isNetworkAvailable(context)) {
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
                                        System.out.println("--------- clicked 1 ");
                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                        {
                                            //Util.showToast(context, "कृपया अंतिम परीक्षा देने के लिए "+ topicName +" में 100% महारत हासिल करें");
                                            Util.openGifDialogue(context, "कृपया अंतिम परीक्षा देने के लिए "+ "अभ्यास" +" में 100% महारत हासिल करें");
                                        }else {
                                            //Util.showToast(context, "Please master 100% of "+topicName+" to take the final exam");
                                            Util.openGifDialogue(context, "Please master 100% of "+"Practice Test"+" to take the final exam");

                                        }
                                    }



                                }
                                else
                                {
                                    System.out.println("--------- clicked 2 ");

                                    if(!topicfound)
                                    {
                                        if(listsize==itempos)
                                        {
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
                        }else{
                            System.out.println("--------- clicked 3 ");
                            //Util.showToast(context, diagnosticTextFirstText);
                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                            {
                                //Util.showToast(context, "कृपया अंतिम परीक्षा देने के लिए "+ topicName +" में 100% महारत हासिल करें");
                                Util.openGifDialogue(context, "कृपया अंतिम परीक्षा देने के लिए "+ "अभ्यास" +" में 100% महारत हासिल करें");
                            }else {
                                //Util.showToast(context, "Please master 100% of "+topicName+" to take the final exam");
                                Util.openGifDialogue(context, "Please master 100% of "+"Practice Test"+" to take the final exam");

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.btnReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    String topicID = topicsArrayList.get(position).get("TopicID");
                    //see here
                    Util.setSubject(context, Util.getSubject(context));
                    context.startActivity(new Intent(context, ReportsActivity.class).putExtra("topicID", topicID));

                }
            });

            viewHolder.video_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Intent intent = new Intent(context, PalTopicListingActivity.class);
                    Util.setVideoLevel(context, 0);
                    String subject = PalContentListingActivity_Mobile.subject;
                    String subjectName = PalContentListingActivity_Mobile.subjectName;
                    String color = PalContentListingActivity_Mobile.color;
                    global.setColor(color);
                    viewPagerPos=0;
                    intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                    intent.putExtra("board", Util.getSelectedBoard(context));
                    intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                    intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                    intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                    intent.putExtra("color", PalContentListingActivity_Mobile.color);
                    intent.putExtra("lastTopicId", topicsArrayList.get(position).get("TopicID"));
                    intent.putExtra("backToScreen", false);
                    Util.setSubject(context, subject);
                    Util.setSubjectName(context, subjectName);
                    context.startActivity(intent);
                }
            });

            viewHolder.practice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Intent intent = new Intent(context, PalTopicListingActivity.class);
                    Util.setVideoLevel(context, 0);
                    String subject = PalContentListingActivity_Mobile.subject;
                    String subjectName = PalContentListingActivity_Mobile.subjectName;
                    String color = PalContentListingActivity_Mobile.color;
                    global.setColor(color);
                    viewPagerPos=1;
                    intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                    intent.putExtra("board", Util.getSelectedBoard(context));
                    intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                    intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                    intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                    intent.putExtra("color", PalContentListingActivity_Mobile.color);
                    intent.putExtra("color", PalContentListingActivity_Mobile.color);
                    intent.putExtra("backToScreen", false);
                    Util.setSubject(context, subject);
                    Util.setSubjectName(context, subjectName);
                    context.startActivity(intent);
                }
            });

            viewHolder.finaltest_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Intent intent = new Intent(context, PalTopicListingActivity.class);
                    Util.setVideoLevel(context, 0);
                    String subject = PalContentListingActivity_Mobile.subject;
                    String subjectName = PalContentListingActivity_Mobile.subjectName;
                    String color = PalContentListingActivity_Mobile.color;
                    global.setColor(color);
                    viewPagerPos=2;
                    intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                    intent.putExtra("board", Util.getSelectedBoard(context));
                    intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                    intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                    intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                    intent.putExtra("color", PalContentListingActivity_Mobile.color);
                    intent.putExtra("lastTopicId", topicsArrayList.get(position).get("TopicID"));
                    intent.putExtra("backToScreen", false);
                    Util.setSubject(context, subject);
                    Util.setSubjectName(context, subjectName);
                    context.startActivity(intent);
                }
            });

            viewHolder.books_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    Intent intent = new Intent(context, PalTopicListingActivity.class);
                    Util.setVideoLevel(context, 0);
                    String subject = PalContentListingActivity_Mobile.subject;
                    String subjectName = PalContentListingActivity_Mobile.subjectName;
                    String color = PalContentListingActivity_Mobile.color;
                    global.setColor(color);
                    viewPagerPos=3;
                    intent.putExtra("sClass", PalContentListingActivity_Mobile.sClass);
                    intent.putExtra("board", Util.getSelectedBoard(context));
                    intent.putExtra("subject", PalContentListingActivity_Mobile.subject);
                    intent.putExtra("subjectName", PalContentListingActivity_Mobile.subjectName);
                    intent.putExtra("icon", PalContentListingActivity_Mobile.icon);
                    intent.putExtra("color", PalContentListingActivity_Mobile.color);
                    intent.putExtra("lastTopicId", topicsArrayList.get(position).get("TopicID"));
                    intent.putExtra("backToScreen", false);
                    Util.setSubject(context, subject);
                    Util.setSubjectName(context, subjectName);
                    context.startActivity(intent);
                }
            });

            viewHolder.downward_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.topicListLayout.performClick();
                }
            });

            viewHolder.selectItem1.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.selectItem3.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.selectItem4.setColorFilter(Color.parseColor(global.getColor()));
            viewHolder.downward_arrow.setColorFilter(Color.parseColor(global.getColor()));
        }
        else {
            PracticeTopicAdapter_Mobile.ViewItem2 viewHolder = (PracticeTopicAdapter_Mobile.ViewItem2) viewHolderValue;

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
            for(String item: ((PalContentListingActivity_Mobile) context).finalTestTopicIdArrayList)
                if(item.equals(topicsArrayList.get(position).get("TopicID"))) viewHolder.badge.setVisibility(View.VISIBLE);

            /** Click Listeners */
//            viewHolder.topicListLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.preventTwoClick(v);
//                    Util.setLevel(context,1);
//                    if(!Util.isSubjectboadingDone(context)) Util.setSubjectboadingMode(context,true);
//                    viewHolder.topicListLayout.setEnabled(false);
//                    ((PalContentListingActivity_Mobile)context).onClickHandler(viewHolder.getAdapterPosition());
//                    PalContentListingActivity_Mobile.showingposition=viewHolder.getAdapterPosition();
//                }
//            });

            viewHolder.topicListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.preventTwoClick(v);
                    showingPosition=position;
                    actvePosition=position;
                    Util.setTopicID(context,topicsArrayList.get(viewHolder.getAdapterPosition()).get("TopicID"));
//                    viewHolder.downward_arrow.setImageResource(R.mipmap.up_arrow_new);
//                    viewHolder.topicListLayout.setEnabled(false);
                    ((PalContentListingActivity_Mobile)context).onClickHandler(viewHolder.getAdapterPosition());
                    PalContentListingActivity_Mobile.showingposition=viewHolder.getAdapterPosition();
                }
            });
        }
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

    public void setTooltipVisibility(boolean visibility) {
        this.showTooltip = visibility;
    }

    public void showDefaultTopic(boolean show) {
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
//        return 2;
        return topicsArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTopicName;
        protected TextView textViewSno;
        protected TextView btnReports;
        protected LinearLayout diagnosticTestLayout;
        protected LinearLayout actions_layout,timeLayout;
        protected TextView textViewdTest;
        //        protected LinearLayout videosLayout;
        protected LinearLayout testLayout;
        protected LinearLayout practiceLayout;
        protected TextView textViewPath;
        protected RelativeLayout linearPath;
        protected ImageView badge;
        protected ImageView downward_arrow;
        protected LinearLayout topicListLayout;
        protected RelativeLayout reletiveParent;
        //        protected ImageView downBtn;
//        protected RecyclerView videosRecyclerView;
        protected ImageView selectItem1;
        protected ImageView selectItem3;
        protected ImageView selectItem4;
        protected View textViewdTestDate;
        protected TextView line2;
        protected View line3;
        protected View line4;
        protected LinearLayout topicOptionsLayout,topicOptionsLayout2;
        protected TextView textViewDTestScore,startDiagnosticTest;
        protected TextView textViewPracticeScore;
        protected TextView textViewDNScore;
        protected TextView textViewdNormalTest;
        protected TextView textViewdPractice,textvideosViewtext,textViewvideoWatched;

        protected TextView video_text,practice_text,test_text,book_text;

        protected LinearLayout video_btn,practice_btn,finaltest_btn,books_btn;

        public ViewItem(View holderView) {
            super(holderView);
            video_btn = holderView.findViewById(R.id.video_btn);
            practice_btn = holderView.findViewById(R.id.practice_btn);
            finaltest_btn = holderView.findViewById(R.id.finaltest_btn);
            books_btn = holderView.findViewById(R.id.books_btn);

            video_text = holderView.findViewById(R.id.video_text);
            practice_text = holderView.findViewById(R.id.practice_text);
            test_text = holderView.findViewById(R.id.test_text);
            book_text = holderView.findViewById(R.id.book_text);
            textvideosViewtext = holderView.findViewById(R.id.textvideosViewtext);
            textViewvideoWatched = holderView.findViewById(R.id.textViewvideoWatched);


            actions_layout = holderView.findViewById(R.id.actions_layout);
            timeLayout = holderView.findViewById(R.id.timeLayout);
            badge = holderView.findViewById(R.id.badge);
            downward_arrow = holderView.findViewById(R.id.downward_arrow);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewSno = holderView.findViewById(R.id.textViewSno);
            diagnosticTestLayout = holderView.findViewById(R.id.diagnosticTestLayout);
            textViewdTest = holderView.findViewById(R.id.textViewdTest);
            textViewDTestScore = holderView.findViewById(R.id.textViewDTestScore);
            btnReports = holderView.findViewById(R.id.btnReports);
//            textViewPath = holderView.findViewById(R.id.textViewPath);
//            videosLayout = holderView.findViewById(R.id.videosLayout);
            testLayout = holderView.findViewById(R.id.testLayout);
            textViewdNormalTest = holderView.findViewById(R.id.textViewdNormalTest);
            textViewDNScore = holderView.findViewById(R.id.textViewDNScore);
            linearPath = holderView.findViewById(R.id.linearPath);
            practiceLayout = holderView.findViewById(R.id.practiceLayout);
            textViewdPractice = holderView.findViewById(R.id.textViewdPractice);
            textViewPracticeScore = holderView.findViewById(R.id.textViewPracticeScore);

            startDiagnosticTest = holderView.findViewById(R.id.startDiagnosticTest);
//            downBtn = holderView.findViewById(R.id.downBtn);
//            videosRecyclerView = holderView.findViewById(R.id.videosRecyclerView);
//            videosRecyclerView.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 1);
//            videosRecyclerView.setLayoutManager(manager);
            topicListLayout = holderView.findViewById(R.id.topicListLayout);
            reletiveParent = holderView.findViewById(R.id.reletiveParent);
            selectItem1 = holderView.findViewById(R.id.selectItem1);
            selectItem3 = holderView.findViewById(R.id.selectItem3);
            selectItem4 = holderView.findViewById(R.id.selectItem4);
//            textViewdTestDate = holderView.findViewById(R.id.textViewdTestDate);
//            line2 = holderView.findViewById(R.id.line2);
            line3 = holderView.findViewById(R.id.line3);

            topicOptionsLayout = holderView.findViewById(R.id.topicOptionsLayout);
            topicOptionsLayout2 = holderView.findViewById(R.id.topicOptionsLayout2);
//            holderView.setOnClickListener(this);



            setText();
        }

        @Override
        public void onClick(View view) {
//            clickListener.onItemClick(view, getPosition());
        }

        private void setText() {
            if(Util.getSelectedLanguage(context).equals("hindi"))
            {
                video_text.setText("वीडियो");
                practice_text.setText("रेमेडियल");
                test_text.setText("परीक्षण");
                book_text.setText("पुस्तकें");
            }
            else
            {
                video_text.setText("Videos");
                practice_text.setText("Remedial");
                test_text.setText("Test");
                book_text.setText("Books");
            }
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

    public static boolean hasChildren(ViewGroup viewGroup) {
        return viewGroup.getChildCount() > 0;
    }

}