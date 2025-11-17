package com.idreameducation.ipreppal.pal.adapter;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class PalReportInnerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final HashMap<String, Object> mainArrayList;
    ArrayList<HashMap<String, Object>> mainArrayList1;
    private final ArrayList<String> keyArrayList;
    private final ArrayList<String> nameArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final String subject;
    private final String date;
    private final String type;
    private final String videoAttempts;
    private final String attempts;
    private final String score_;
    private final String mastery_;
    ArrayList<String> videosKeys;

    public PalReportInnerAdapter(Context context, HashMap<String, Object> mainArrayList, ArrayList<String> keyArrayList, ArrayList<String> nameArrayList, String subject, String date, String type) {
        this.context = context;
        this.subject = subject;
        this.type = type;
        this.date = date;
        this.mainArrayList = mainArrayList;
        this.keyArrayList = keyArrayList;
        this.nameArrayList = nameArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        videosKeys=new ArrayList<>();
        if(type.contains("video") || type.equals("simulation_content")) for(String key:mainArrayList.keySet()) videosKeys.add(key);
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
            attempts ="प्रयास";
            videoAttempts ="बार";
            score_ = "स्कोर";
            mastery_ = "महारत";
        }else {
            attempts ="Attempts";
            videoAttempts ="Times";
            score_ = "Score";
            mastery_ = "Mastery";
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_inner_layout, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    int pos = 0;
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            viewHolder.textViewDate.setText(date.substring(0, date.length() - 3));
            if (type.contains("test")) {

                HashMap<String, Object> mainHashMap = sortHashMapByKey((HashMap<String, Object>) mainArrayList.get(keyArrayList.get(position)));
//                viewHolder.textViewAttempts.setText(mainHashMap.size() +" " +attempts);
//                viewHolder.textViewAttempts.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (pos == 0) {
//                            pos = 1;
//                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
//                            viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
//                            viewHolder.linearAttempts.removeAllViews();
//                            int count = 0;
//                            for (String key : mainHashMap.keySet()) {
//                                count++;
//                                View child = null;
//                                HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
//                                HashMap<String, Object> scoreMap = (HashMap<String, Object>) data.get("scores");
//                                for (int i = 0; i < scoreMap.size(); i++) {
//                                    child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
//                                    TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
//                                    TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
//                                    String scores = (String) scoreMap.get("scores");
//                                    String totalScores = (String) scoreMap.get("totalScores");
//                                    String date = (String) scoreMap.get("date");
//                                    String[] splitStr = date.split("\\s+");
//                                    textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
//                                    textViewAttemptCount.setText(score_ +" "+ scores + "/" + totalScores);
//                                }
//                                viewHolder.linearAttempts.addView(child);
//                            }
//
//
//                        } else {
//                            pos = 0;
//                            notifyDataSetChanged();
//                            viewHolder.linearAttempts1.setVisibility(View.GONE);
//                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
//                        }
//
//                    }
//                });
                viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                            pos = 1;
                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                            viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                            viewHolder.linearAttempts.removeAllViews();
                            int count = 0;

                            Set<String> keys = mainHashMap.keySet();
                            List<String> arrayListKeys = new ArrayList<>(keys);
                            Collections.sort(arrayListKeys);
                            for (String key : arrayListKeys) {
                                count++;
                                View child = null;
                                HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                                HashMap<String, Object> scoreMap = (HashMap<String, Object>) data.get("scores");
                                for (int i = 0; i < scoreMap.size(); i++) {
                                    child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                    TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                    TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                    String scores = (String) scoreMap.get("scores");
                                    String totalScores = (String) scoreMap.get("totalScores");
                                    String date = (String) scoreMap.get("date");
                                    String[] splitStr = date.split("\\s+");

                                    textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                    textViewAttemptCount.setText(score_+" " + scores + "/" + totalScores);
                                }
                                viewHolder.linearAttempts.addView(child);
                            }


                        } else {
                            pos = 0;
                            notifyDataSetChanged();
                            viewHolder.linearAttempts1.setVisibility(GONE);
                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                        }

                    }
                });
                for (String key : mainHashMap.keySet()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                    HashMap<String, Object> scoreMap = (HashMap<String, Object>) data.get("scores");
                    for (int i = 0; i < scoreMap.size(); i++) {
                        String scores = (String) scoreMap.get("scores");
                        String totalScores = (String) scoreMap.get("totalScores");
                        String topicName = (String) scoreMap.get("topicName");
                        viewHolder.textViewTopicName.setText(topicName);
                        viewHolder.textViewDetail.setText(score_+" " + scores + "/" + totalScores);
                    }
                }
            }
            else if (type.contains("practice")) {

                viewHolder.image.setImageResource(R.drawable.ic_practice);

                HashMap<String, Object> mainHashMap = (HashMap<String, Object>) mainArrayList.get(keyArrayList.get(position));

                ArrayList<String> keys = new ArrayList<>();
                ArrayList<Long> keysInt = new ArrayList<>();
                for (String key : mainHashMap.keySet()) {
                    keys.add(key);
                    keysInt.add(Long.parseLong(key));
                }

                Collections.reverse(keys);
                Collections.sort(keysInt);


//                viewHolder.textViewAttempts.setText(mainHashMap.size() + " "+attempts);
//                viewHolder.textViewAttempts.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (pos == 0) {
//                            pos = 1;
//                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
//                            viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
//                            viewHolder.linearAttempts.removeAllViews();
//                            int count = 0;
//                            for (String key : mainHashMap.keySet()) {
//                                count++;
//                                View child = null;
//                                HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
//                                for (int i = 0; i < data.size(); i++) {
//                                    child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
//                                    TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
//                                    TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
//                                    String mastery = (String) data.get("mastery");
//                                    String date = (String) data.get("date");
//                                    String[] splitStr = date.split("\\s+");
//                                    textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
//                                    textViewAttemptCount.setText(attempts+" " + count +" " +mastery_+" " + mastery + "%");
//                                }
//                                viewHolder.linearAttempts.addView(child);
//                            }
//
//
//                        } else {
//                            pos = 0;
//                            notifyDataSetChanged();
//                            viewHolder.linearAttempts1.setVisibility(View.GONE);
//                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
//                        }
//
//                    }
//                });
                viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                            pos = 1;
                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                            viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                            viewHolder.linearAttempts.removeAllViews();
                            int count = 0;
                            for(Long keyy:keysInt)
                            {
                                String key=String.valueOf(keyy);
                                count++;
                                View child = null;
                                HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                                child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                String mastery = (String) data.get("mastery");
                                String date = (String) data.get("date");
                                String[] splitStr = date.split("\\s+");
                                textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                textViewAttemptCount.setText(attempts+" " + count +" " +mastery_+" " + mastery + "%");

                                viewHolder.linearAttempts.addView(child);
                            }


                        } else {
                            pos = 0;
                            notifyDataSetChanged();
                            viewHolder.linearAttempts1.setVisibility(GONE);
                            viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                        }

                    }
                });
                for(Long keyy:keysInt)
                {
                    String key=String.valueOf(keyy);
                    HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                    String mastery = (String) data.get("mastery");
                    String topicName = (String) data.get("topicName");
                    viewHolder.textViewTopicName.setText(topicName);
                    viewHolder.textViewDetail.setText(mastery_+" " + mastery + "%");
                }
            }
            else if (type.contains("video") ) {

                if(type.contains("video")) viewHolder.image.setImageResource(R.drawable.ic_video);

//                if(type.contains("simulation_content")) viewHolder.image.setImageResource(R.drawable.ic_video);

                if(!Util.isOfflineMode(context))
                {
                    try {
                        viewHolder.linearAttempts.removeAllViews();
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);


                        HashMap<String, Object> mainHashMap = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));

                        ArrayList<Long> keysInt = new ArrayList<>();
                        for (String key : mainHashMap.keySet()) {
                            keysInt.add(Long.parseLong(key));
                        }

                        Collections.sort(keysInt);

//                        viewHolder.textViewAttempts.setText(mainHashMap.size() +" " +videoAttempts);

                        viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (viewHolder.linearAttempts1.getVisibility()== GONE) {
                                    pos=1;
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                    viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                    viewHolder.linearAttempts.removeAllViews();
                                    for (Long keyy : keysInt) {
                                        String key=String.valueOf(keyy);
                                        View child = null;
                                        HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);

                                        child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                        TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                        TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                        String videoName = (String) data.get("videoName");
                                        String time = (String) data.get("time");
                                        if(time.equals("null")) time="0";
                                        String date = (String) data.get("date");
                                        String timeforSave= millisecondsToTime(Long.parseLong(time));
                                        String[] splitStr = date.split("\\s+");
                                        textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                        textViewAttemptCount.setText(timeforSave +" Sec");

//
                                        viewHolder.linearAttempts.addView(child);

                                    }


                                } else {
                                    pos = 0;
                                    viewHolder.linearAttempts.removeAllViews();
                                    viewHolder.linearAttempts1.setVisibility(GONE);
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    notifyItemChanged(position);
                                    notifyItemChanged(position+1);
                                    notifyItemChanged(position-1);


                                    notifyDataSetChanged();
                                }

                            }
                        });
                        int totaltime=0;
                        for (String key : mainHashMap.keySet()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                            String time = (String) data.get("time");
                            if(time.equals("null")) time="0";
                            totaltime=totaltime+Integer.parseInt(time);
                            String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));


                            for (int i = 0; i < data.size(); i++) {

                                String topicName = (String) data.get("videoName");
                                String topicName_main = (String) data.get("topicName");
                                String subjectName = (String) data.get("subjectName");
                                viewHolder.textViewTopicName.setText(topicName);

                                String timeText="Time";

                                if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

                            }

                        }

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }
                else
                {

                    try {


                        if(type.equals("project_video")) {

                            int totaltime=0;

                            HashMap<String, Object> data2 = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));


                            for(String key:data2.keySet()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key);

                                for(String name : data.keySet()) {
                                    HashMap<String, Object> data22 = (HashMap<String, Object>) data.get(name);

                                    String time = (String) data22.get("time");
                                    if(time.equals("null")) time="0";
                                    totaltime=totaltime+Integer.parseInt(time);
                                    String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));

                                    String topicName = (String) data22.get("videoName");
                                    String topicName_main = (String) data22.get("topicName");
                                    viewHolder.textViewTopicName.setText(topicName);

                                    String timeText="Time";

                                    if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                    viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");
                                }

                            }

                            viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                        pos = 1;
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                        viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                        viewHolder.linearAttempts.removeAllViews();

                                        View child = null;
//                                        HashMap<String, Object> data = mainArrayList.get(ij);
                                        for (String key : data2.keySet()) {
                                            HashMap<String, Object> dataa = (HashMap<String, Object>) data2.get(key);

                                            for(String nam : dataa.keySet()) {
                                                HashMap<String, Object> d = (HashMap<String, Object>) dataa.get(nam);

                                                child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                                TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                                TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                                String time = (String) d.get("time");
                                                if(time.equals("null")) time="0";
                                                String date = (String) d.get("date");
                                                System.out.println("------ date "+date);
                                                String[] splitStr = date.split("\\s+");
                                                textViewAttempt.setText(splitStr[0]+" "+splitStr[1]);


                                                String timeforSave= millisecondsToTime(Long.parseLong(time));
                                                textViewAttemptCount.setText(timeforSave +" Sec");
                                                viewHolder.linearAttempts.addView(child);

                                            }

                                        }



                                    } else {
                                        pos = 0;
                                        notifyDataSetChanged();
                                        viewHolder.linearAttempts1.setVisibility(GONE);
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    }

                                }
                            });

                        }

                        else {
                            int totaltime=0;

                            HashMap<String, Object> data2 = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));


                            for(String key:data2.keySet()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key);
                                String time = (String) data.get("time");
                                if(time.equals("null")) time="0";
                                totaltime=totaltime+Integer.parseInt(time);
                                String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));

                                String topicName = (String) data.get("videoName");
                                String topicName_main = (String) data.get("topicName");
                                viewHolder.textViewTopicName.setText(topicName);

                                String timeText="Time";

                                if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

                            }

                            viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                        pos = 1;
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                        viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                        viewHolder.linearAttempts.removeAllViews();

                                        View child = null;
//                                        HashMap<String, Object> data = mainArrayList.get(ij);
                                        for (String key : data2.keySet()) {
                                            HashMap<String, Object> dataa = (HashMap<String, Object>) data2.get(key);
                                            child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                            TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                            TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                            String time = (String) dataa.get("time");
                                            if(time.equals("null")) time="0";
                                            String date = (String) dataa.get("date");
                                            System.out.println("------ date "+date);
                                            String[] splitStr = date.split("\\s+");
                                            textViewAttempt.setText(splitStr[0]+" "+splitStr[1]);


                                            String timeforSave= millisecondsToTime(Long.parseLong(time));
                                            textViewAttemptCount.setText(timeforSave +" Sec");
                                            viewHolder.linearAttempts.addView(child);
                                        }



                                    } else {
                                        pos = 0;
                                        notifyDataSetChanged();
                                        viewHolder.linearAttempts1.setVisibility(GONE);
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    }

                                }
                            });
                        }

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }


                }

            }
            else if (type.contains("simulation_content")) {

                if(type.contains("video")) viewHolder.image.setImageResource(R.drawable.ic_video);

//                if(type.contains("simulation_content")) viewHolder.image.setImageResource(R.drawable.ic_video);

                if(!Util.isOfflineMode(context))
                {
                    try {
                        viewHolder.linearAttempts.removeAllViews();
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);


                        HashMap<String, Object> mainHashMap = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));

                        ArrayList<Long> keysInt = new ArrayList<>();
                        for (String key : mainHashMap.keySet()) {
                            keysInt.add(Long.parseLong(key));
                        }

                        Collections.sort(keysInt);

//                        viewHolder.textViewAttempts.setText(mainHashMap.size() +" " +videoAttempts);

                        viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (viewHolder.linearAttempts1.getVisibility()== GONE) {
                                    pos=1;
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                    viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                    viewHolder.linearAttempts.removeAllViews();
                                    for (Long keyy : keysInt) {
                                        String key=String.valueOf(keyy);
                                        View child = null;
                                        HashMap<String, Object> data2 = (HashMap<String, Object>) mainHashMap.get(key);

                                        for(String k:data2.keySet()) {
                                            HashMap<String, Object> data = (HashMap<String, Object>) data2.get(k);
                                            child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                            TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                            TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                            String videoName = (String) data.get("videoName");
                                            String time = (String) data.get("time");
                                            if(time.equals("null")) time="0";
                                            String date = (String) data.get("date");
                                            String timeforSave= millisecondsToTime(Long.parseLong(time));
                                            String[] splitStr = date.split("\\s+");
                                            textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                            textViewAttemptCount.setText(timeforSave +" Sec");

//
                                            viewHolder.linearAttempts.addView(child);

                                        }



                                    }


                                } else {
                                    pos = 0;
                                    viewHolder.linearAttempts.removeAllViews();
                                    viewHolder.linearAttempts1.setVisibility(GONE);
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    notifyItemChanged(position);
                                    notifyItemChanged(position+1);
                                    notifyItemChanged(position-1);


                                    notifyDataSetChanged();
                                }

                            }
                        });
                        int totaltime=0;
                        for (String key : mainHashMap.keySet()) {
                            HashMap<String, Object> data2 = (HashMap<String, Object>) mainHashMap.get(key);
                            Set<String> list = data2.keySet();
                            for (String key2 : data2.keySet()) {

                                HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key2);
                                String time = (String) data.get("time");
                                if(time.equals("null")) time="0";
                                totaltime=totaltime+Integer.parseInt(time);
                                String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));


                                for (int i = 0; i < data.size(); i++) {

                                    String topicName = (String) data.get("videoName");
                                    String topicName_main = (String) data.get("topicName");
                                    String subjectName = (String) data.get("subjectName");
                                    viewHolder.textViewTopicName.setText(topicName);

                                    String timeText="Time";

                                    if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                    viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

                                }


                            }

                        }

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }
                else
                {

                    try {


                        if(type.equals("project_video")) {

                            int totaltime=0;

                            HashMap<String, Object> data2 = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));


                            for(String key:data2.keySet()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key);

                                for(String name : data.keySet()) {
                                    HashMap<String, Object> data22 = (HashMap<String, Object>) data.get(name);

                                    String time = (String) data22.get("time");
                                    if(time.equals("null")) time="0";
                                    totaltime=totaltime+Integer.parseInt(time);
                                    String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));

                                    String topicName = (String) data22.get("videoName");
                                    String topicName_main = (String) data22.get("topicName");
                                    viewHolder.textViewTopicName.setText(topicName);

                                    String timeText="Time";

                                    if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                    viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");
                                }

                            }

                            viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                        pos = 1;
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                        viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                        viewHolder.linearAttempts.removeAllViews();

                                        View child = null;
//                                        HashMap<String, Object> data = mainArrayList.get(ij);
                                        for (String key : data2.keySet()) {
                                            HashMap<String, Object> dataa = (HashMap<String, Object>) data2.get(key);

                                            for(String nam : dataa.keySet()) {
                                                HashMap<String, Object> d = (HashMap<String, Object>) dataa.get(nam);

                                                child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                                TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                                TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                                String time = (String) d.get("time");
                                                if(time.equals("null")) time="0";
                                                String date = (String) d.get("date");
                                                System.out.println("------ date "+date);
                                                String[] splitStr = date.split("\\s+");
                                                textViewAttempt.setText(splitStr[0]+" "+splitStr[1]);


                                                String timeforSave= millisecondsToTime(Long.parseLong(time));
                                                textViewAttemptCount.setText(timeforSave +" Sec");
                                                viewHolder.linearAttempts.addView(child);

                                            }

                                        }



                                    } else {
                                        pos = 0;
                                        notifyDataSetChanged();
                                        viewHolder.linearAttempts1.setVisibility(GONE);
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    }

                                }
                            });

                        }

                        else {
                            int totaltime=0;

                            HashMap<String, Object> data2 = (HashMap<String, Object>) mainArrayList.get(videosKeys.get(position));


                            for(String key:data2.keySet()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key);
                                String time = (String) data.get("time");
                                if(time.equals("null")) time="0";
                                totaltime=totaltime+Integer.parseInt(time);
                                String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));

                                String topicName = (String) data.get("videoName");
                                String topicName_main = (String) data.get("topicName");
                                viewHolder.textViewTopicName.setText(topicName);

                                String timeText="Time";

                                if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

                            }

                            viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                        pos = 1;
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                        viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                        viewHolder.linearAttempts.removeAllViews();

                                        View child = null;
//                                        HashMap<String, Object> data = mainArrayList.get(ij);
                                        for (String key : data2.keySet()) {
                                            HashMap<String, Object> dataa = (HashMap<String, Object>) data2.get(key);
                                            child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                            TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                            TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                            String time = (String) dataa.get("time");
                                            if(time.equals("null")) time="0";
                                            String date = (String) dataa.get("date");
                                            System.out.println("------ date "+date);
                                            String[] splitStr = date.split("\\s+");
                                            textViewAttempt.setText(splitStr[0]+" "+splitStr[1]);


                                            String timeforSave= millisecondsToTime(Long.parseLong(time));
                                            textViewAttemptCount.setText(timeforSave +" Sec");
                                            viewHolder.linearAttempts.addView(child);
                                        }



                                    } else {
                                        pos = 0;
                                        notifyDataSetChanged();
                                        viewHolder.linearAttempts1.setVisibility(GONE);
                                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    }

                                }
                            });
                        }

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }


                }

            }
            else if (type.contains("books")) {

                viewHolder.image.setImageResource(R.drawable.ic_books);

                if(!Util.isOfflineMode(context))
                {
                    try {
                        viewHolder.linearAttempts.removeAllViews();
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.linearAttempts1.setVisibility(GONE);
                        viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);


                        HashMap<String, Object> mainHashMap = (HashMap<String, Object>) mainArrayList.get(keyArrayList.get(position));

                        ArrayList<Long> keysInt = new ArrayList<>();
                        for (String key : mainHashMap.keySet()) {
                            keysInt.add(Long.parseLong(key));
                        }

                        Collections.sort(keysInt);

                        viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (viewHolder.linearAttempts1.getVisibility()== GONE) {
                                    pos=1;
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                    viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                    viewHolder.linearAttempts.removeAllViews();
                                    for (Long keyy : keysInt) {
                                        String key=String.valueOf(keyy);
                                        View child = null;
                                        HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);

                                        child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                        TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                        TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                        String videoName = (String) data.get("videoName");
                                        String time = (String) data.get("time");
                                        String date = (String) data.get("date");
                                        String timeforSave= millisecondsToTime(Long.parseLong(time));
                                        String[] splitStr = date.split("\\s+");
                                        textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                        textViewAttemptCount.setText(timeforSave +" Sec");

                                        viewHolder.linearAttempts.addView(child);

                                    }


                                } else {
                                    pos = 0;
                                    viewHolder.linearAttempts.removeAllViews();
                                    viewHolder.linearAttempts1.setVisibility(GONE);
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                    notifyItemChanged(position);
                                    notifyItemChanged(position+1);
                                    notifyItemChanged(position-1);


                                    notifyDataSetChanged();
                                }

                            }
                        });
                        int totaltime=0;
                        for (String key : mainHashMap.keySet()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                            String time = (String) data.get("time");
                            totaltime=totaltime+Integer.parseInt(time);
                            String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));


                            for (int i = 0; i < data.size(); i++) {

                                String topicName = (String) data.get("videoName");
                                String topicName_main = (String) data.get("topicName");
                                String subjectName = (String) data.get("subjectName");
                                viewHolder.textViewTopicName.setText(topicName);

                                String timeText="Time";

                                if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                                viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");
//                                viewHolder.textViewDetail.setText(" Time : "+ timeForSave+" Sec - "+" "+subjectName+" - " +topicName_main);

                            }

                        }

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }
                else
                {
                    try {
                        System.out.println("------  -   mainArrayList 1 "+mainArrayList1);

                        ArrayList<String> keys=new ArrayList<>();

                        for(String key:mainArrayList.keySet())
                        {
                            keys.add(key);
                        }

                        int totaltime=0;

                        HashMap<String, Object> data2 = (HashMap<String, Object>) mainArrayList.get(keys.get(position));
//                        viewHolder.textViewAttempts.setText(data2.size() +" " +videoAttempts);

                        for(String key:data2.keySet()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) data2.get(key);
                            String time = (String) data.get("time");
                            totaltime=totaltime+Integer.parseInt(time);
                            String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));

                            String topicName = (String) data.get("videoName");
                            String topicName_main = (String) data.get("topicName");
                            viewHolder.textViewTopicName.setText(topicName);

                            String timeText="Time";

                            if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                            viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

//                            viewHolder.textViewDetail.setText("Time : "+ timeForSave+" Sec - "+" "+subject+" - " +topicName_main);

                        }

                        viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                    pos = 1;
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                    viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                    viewHolder.linearAttempts.removeAllViews();

                                    View child = null;
//                                        HashMap<String, Object> data = mainArrayList.get(ij);
                                    for (String key : data2.keySet()) {
                                        HashMap<String, Object> dataa = (HashMap<String, Object>) data2.get(key);
                                        child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                        TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                        TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                        String time = (String) dataa.get("time");
                                        String date = (String) dataa.get("date");
                                        System.out.println("------ date "+date);
                                        String[] splitStr = date.split("\\s+");
                                        textViewAttempt.setText(splitStr[0]+" "+splitStr[1]);

                                        String timeforSave= millisecondsToTime(Long.parseLong(time));

                                        textViewAttemptCount.setText(timeforSave +" Sec");
                                        viewHolder.linearAttempts.addView(child);
                                    }

                                } else {
                                    pos = 0;
                                    notifyDataSetChanged();
                                    viewHolder.linearAttempts1.setVisibility(GONE);
                                    viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                                }

                            }
                        });

                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }

            }
            else if (type.contains("diksha")) {

                try {
                    HashMap<String, Object> mainHashMap = (HashMap<String, Object>) mainArrayList.get(keyArrayList.get(position));

                    viewHolder.RelativeClickListener.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (viewHolder.linearAttempts1.getVisibility()==GONE) {
                                pos = 1;
                                viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_pal, 0);
                                viewHolder.linearAttempts1.setVisibility(View.VISIBLE);
                                viewHolder.linearAttempts.removeAllViews();
                                for (String key : mainHashMap.keySet()) {
                                    View child = null;
                                    HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                                    for (int i = 0; i < data.size(); i++) {
                                        child = LayoutInflater.from(context).inflate(R.layout.row_layout_attempts, null);
                                        TextView textViewAttemptCount = child.findViewById(R.id.textViewAttemptCount);
                                        TextView textViewAttempt = child.findViewById(R.id.textViewAttempt);
                                        String videoName = (String) data.get("videoName");
                                        String time = (String) data.get("time");
                                        String date = (String) data.get("date");
                                        String timeforSave= millisecondsToTime(Long.parseLong(time));
                                        String[] splitStr = date.split("\\s+");
                                        textViewAttempt.setText(splitStr[0] +" " +splitStr[1]);
                                        textViewAttemptCount.setText(timeforSave +" Sec");

                                    }
                                    viewHolder.linearAttempts.addView(child);
                                }


                            } else {
                                pos = 0;
                                notifyDataSetChanged();
                                viewHolder.linearAttempts1.setVisibility(GONE);
                                viewHolder.textViewAttempts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_pal, 0);
                            }

                        }
                    });
                    int totaltime=0;


                    for (String key : mainHashMap.keySet()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) mainHashMap.get(key);
                        String time = (String) data.get("time");
                        totaltime=totaltime+Integer.parseInt(time);
                        String timeForSave=millisecondsToTime(Long.parseLong(String.valueOf(totaltime)));


                        for (int i = 0; i < data.size(); i++) {

                            String topicName = (String) data.get("videoName");
                            String topicName_main = (String) data.get("topicName");
                            String subjectName = (String) data.get("subjectName");
                            viewHolder.textViewTopicName.setText(topicName);

                            String timeText="Time";

                            if(Util.getSelectedLanguage(context).equals("hindi")) timeText="समय";

                            viewHolder.textViewDetail.setText(timeText+" : "+ timeForSave+" Sec");

//                            viewHolder.textViewDetail.setText(" Time : "+ timeForSave+" Sec - "+" "+subjectName+" - " +topicName_main);

                        }

                    }

                }
                catch (Exception ee) {
                    ee.printStackTrace();
                }

            }

        }
    }

    public static HashMap<String, Object> sortHashMapByKey(HashMap<String, Object> hashMap) {
        TreeMap<String, Object> sortedMap = new TreeMap<>(hashMap);
        HashMap<String, Object> sortedHashMap = new HashMap<>(sortedMap);
        return sortedHashMap;
    }

    private String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) secs = secondsStr.substring(0, 2);
         else secs = "0" + secondsStr;
        

        return minutes + ":" + secs;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mainArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewDate;
        protected TextView textViewTopicName;
        protected TextView textViewDetail;
        protected TextView textViewAttempts;
        protected ImageView image;
        protected LinearLayout linearAttempts;
        protected LinearLayout linearAttempts1;
        protected RelativeLayout RelativeClickListener;

        public ViewItem(View holderView) {
            super(holderView);
            textViewAttempts = holderView.findViewById(R.id.textViewAttempts);
            textViewTopicName = holderView.findViewById(R.id.textViewTopicName);
            textViewDetail = holderView.findViewById(R.id.textViewDetail);
            textViewDate = holderView.findViewById(R.id.textViewDate);
            image = holderView.findViewById(R.id.image);
            linearAttempts = holderView.findViewById(R.id.linearAttempts);
            linearAttempts1 = holderView.findViewById(R.id.linearAttempts1);
            RelativeClickListener = holderView.findViewById(R.id.RelativeClickListener);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            try {
                clickListener.onItemClick(view, getPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}