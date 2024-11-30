package com.idreameducation.ipreppal.pal.adapter;

import static com.idreameducation.ipreppal.pal.activity.PalContentListingActivity.currentTopicid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */


public class VideoListAdapter extends RecyclerView.Adapter {


    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, Object>> contentArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final String classSelected;
    private final String subjectSelected;
    private final String boardSelected;
    private final String languageSelected;
    private final String icon;
    private final Fragment fragment;
    private final HashMap<String, Object> dataMap;
    private String type="";
    private final Global global;
    private long mLastClickTime = 0;
    private String mulbhutText, diagnosticText;

    public VideoListAdapter(Context context, ArrayList<HashMap<String, Object>> contentArrayList, String classSelected, String subjectSelected, String languageSelected, String boardSelected, String icon, Fragment fragment, HashMap<String, Object> dataMap, String type) {
        this.context = context;
        this.fragment = fragment;
        this.contentArrayList = contentArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        this.classSelected = classSelected;
        this.subjectSelected = subjectSelected;
        this.languageSelected = languageSelected;
        this.boardSelected = boardSelected;
        this.icon = icon;
        this.type = type;
        this.dataMap = dataMap ;

        global = (Global) context.getApplicationContext();
    }

    @Override
    public ViewItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_video, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;

            if(Util.isPortraitMode(context))
            {
                viewHolder.card.setAlpha(.5f);
                if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textViewVideoName.setText("स्तर " + (position + 1));
                else viewHolder.textViewVideoName.setText("Level " + (position + 1));


//            viewHolder.textViewVideoName.setTextColor();
                viewHolder.textViewVideoName.setTextColor(Color.parseColor(global.getColor()));
                HashMap<String, Object> videoMap = contentArrayList.get(position);
                ArrayList<String> keyArrayList = new ArrayList<>();
                ArrayList<Integer> keyList = new ArrayList<>();
                for (String key : videoMap.keySet()) {
                    keyArrayList.add(key);
                    keyList.add(Integer.parseInt(key));
                }

                int pos = position + 1;
                VideoListInnerAdapter videoListInnerAdapter = new VideoListInnerAdapter(context, videoMap, keyList, dataMap,type);
                viewHolder.recyclerView.setAdapter(videoListInnerAdapter);

                if(Util.getUnlockVideoLevel(context, PracticeTopicAdapter.activeFoundationTopic)-1>=position) {
                    viewHolder.card.setAlpha(1f);
                    videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            if (Util.isOfflineMode(context)) {
                                try {
                                    boolean isCompleted;
                                    if (type.equals("foundationalTopicVideos")) {
                                        isCompleted = true;
                                    } else {
                                        isCompleted = PalContentListingActivity_Mobile.palContentListingActivityMobile.getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.palContentListingActivity.isCompleted;
                                    }
                                    if (isCompleted) {
                                        try {
                                            for (String keyData : keyArrayList) {
                                                for (int i = 0; i < contentArrayList.size(); i++) {
                                                    HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                    HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                    if (innerMap != null) {
                                                        innerMap.put("isSelected", "false");
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        String key = keyArrayList.get(position);
                                        PalVideoListFragment.current_videoid = key;
                                        PalContentListingActivity.keys = key;
                                        HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key);
                                        innerMap.put("isSelected", "true");
                                        Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                        notifyDataSetChanged();
                                        videoListInnerAdapter.notifyDataSetChanged();
                                        String completeUrl = innerMap.get("onlineLink");
                                        String offlineLink = innerMap.get("offlineLink");

                                        String url;
                                        boolean isLocalFile = false;
                                        if (type.equals("diksha_content")) {
                                            url = completeUrl;
                                            isLocalFile = false;
                                        } else {
                                            String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                            File file = new File(filePath);
                                            if (file.exists() && Util.isOfflineMode(context)) {
                                                url = file.toString();
                                                isLocalFile = true;
                                            } else {
                                                String[] urlArray = completeUrl.split("/");
                                                url = urlArray[urlArray.length - 1];
                                                isLocalFile = false;
                                            }
                                        }

                                        String name = innerMap.get("name");
                                        String topicID = innerMap.get("topicID");
                                        if (type.equals("foundationalTopicVideos")) {
                                            boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                            String type;
                                            if (isLocalFile) {
                                                type = "local";
                                            } else {
                                                type = "vimeo";
                                            }
                                            if (!isFragmentAdded) {
                                                ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                            } else {
                                                ((ExtraContentListingActivity) context).removeFragment(false);
                                                boolean finalIsLocalFile1 = isLocalFile;
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                        ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                    }
                                                }, 500);
                                            }
                                        } else {
                                            boolean isFragmentAdded = PalContentListingActivity_Mobile.palContentListingActivityMobile.isFragmentAdded();
                                            if (!isFragmentAdded) {
                                                PalContentListingActivity_Mobile.videoKey = pos + "-" + position;
                                                com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
//                                                ((PalTopicListingActivity)context).removeVideoActivity();
                                                com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.openFragment(url, offlineLink, name, topicID, isLocalFile, type, key);
                                            } else {
                                                com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
                                                boolean finalIsLocalFile = isLocalFile;
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        PalContentListingActivity_Mobile.videoKey = pos + "-" + position;
                                                        com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
//                                                        ((PalTopicListingActivity)context).removeVideoActivity();
                                                        com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.openFragment(url, offlineLink, name, topicID, finalIsLocalFile, type, key);
                                                    }
                                                }, 500);
                                            }
                                        }


                                    } else {

                                        String completeType = PalContentListingActivity_Mobile.completeType;

                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                            mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
                                            diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                        } else {
                                            mulbhutText = "Please practice basic subject first";
                                            diagnosticText = "Please do diagnostic test first";
                                        }
                                        if (completeType.equalsIgnoreCase("foundationalPractice")) {
                                            //Util.showToast(context, mulbhutText);
                                            Util.openGifDialogue(context, mulbhutText);
                                        } else {
                                            //Util.showToast(context, diagnosticText);
//                                                Util.openGifDialogue(context, diagnosticText);
                                            Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);

                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                if (Util.checkInternetConnection(context)) {
                                    try {
                                        boolean isCompleted;
                                        if (type.equals("foundationalTopicVideos")) {
                                            isCompleted = true;
                                        } else {
                                            isCompleted = PalContentListingActivity_Mobile.palContentListingActivityMobile.getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity.palContentListingActivity.isCompleted;
                                        }
                                        if (isCompleted) {
                                            try {
                                                for (String keyData : keyArrayList) {
                                                    for (int i = 0; i < contentArrayList.size(); i++) {
                                                        HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                        HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                        if (innerMap != null) {
                                                            innerMap.put("isSelected", "false");
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            String key = keyArrayList.get(position);
                                            PalContentListingActivity.keys = key;
                                            HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key);
                                            innerMap.put("isSelected", "true");
                                            Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                            videoListInnerAdapter.notifyDataSetChanged();
                                            String completeUrl = innerMap.get("onlineLink");
                                            String offlineLink = innerMap.get("offlineLink");

                                            if(completeUrl.contains("https://vimeo.com/")) type="video_lessons";
                                            else type="diksha_content";


                                            String url;
                                            boolean isLocalFile = false;
                                            if (type.equals("diksha_content")) {
                                                url = completeUrl;
                                                isLocalFile = false;
                                            } else {
                                                String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                                File file = new File(filePath);
                                                if (file.exists() && Util.isOfflineMode(context)) {
                                                    url = file.toString();
                                                    isLocalFile = true;
                                                } else {
                                                    String[] urlArray = completeUrl.split("/");
                                                    url = urlArray[urlArray.length - 1];
                                                    isLocalFile = false;
                                                }
                                            }

                                            String name = innerMap.get("name");
                                            String topicID = innerMap.get("topicID");
                                            if (type.equals("foundationalTopicVideos")) {
                                                boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                                String type;
                                                if (isLocalFile) {
                                                    type = "local";
                                                } else {
                                                    type = "vimeo";
                                                }
                                                if (!isFragmentAdded) {
                                                    ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                    ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                                } else {
                                                    ((ExtraContentListingActivity) context).removeFragment(false);
                                                    boolean finalIsLocalFile1 = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                            ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                        }
                                                    }, 500);
                                                }
                                            } else {
                                                boolean isFragmentAdded = PalContentListingActivity_Mobile.palContentListingActivityMobile.isFragmentAdded();
                                                if (!isFragmentAdded) {
                                                    PalContentListingActivity_Mobile.videoKey = pos + "-" + position;
                                                    com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
//                                                    ((PalTopicListingActivity)context).removeVideoActivity();
                                                    com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.openFragment(url, offlineLink, name, topicID, isLocalFile, type, key);
                                                } else {
                                                    com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
                                                    boolean finalIsLocalFile = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            PalContentListingActivity_Mobile.videoKey = pos + "-" + position;
                                                            com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.removeFragment();
//                                                            ((PalTopicListingActivity)context).removeVideoActivity();
                                                            com.idreameducation.ipreppal.PalMobile.activity.PalTopicListingActivity.palTopicListingActivity.openFragment(url, offlineLink, name, topicID, finalIsLocalFile, type, key);
                                                        }
                                                    }, 500);
                                                }
                                            }


                                        } else {

                                            String completeType = PalContentListingActivity_Mobile.completeType;

                                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                                mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
                                                diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                            } else {
                                                mulbhutText = "Please practice basic subject first";
                                                diagnosticText = "Please do diagnostic test first";
                                            }
                                            if (completeType.equalsIgnoreCase("foundationalPractice")) {
                                                //Util.showToast(context, mulbhutText);
                                                Util.openGifDialogue(context, mulbhutText);
                                            } else {
                                                //Util.showToast(context, diagnosticText);
//                                                    Util.openGifDialogue(context, diagnosticText);
                                                Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
                                            }

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    openGifDialogue();
                                }
                            }

                        }
                    });
                }
                else {
                    viewHolder.card.setAlpha(.5f);
                    videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
//                    PalContentListingActivity.duration=0;
                            /** also add hindi text */
                            if(Util.getSelectedLanguage(context).equals("hindi")) Util.openGifDialogue(context, "पहले स्तर "+Util.getUnlockVideoLevel(context,currentTopicid)+" पूरा करे");
                            else Util.openGifDialogue(context, "First complete level "+Util.getUnlockVideoLevel(context,PalContentListingActivity_Mobile.currentTopicid));
                        }
                    });

                }
            }
            else
            {
                viewHolder.card.setAlpha(.5f);


                if(Util.getSelectedLanguage(context).equals("hindi")) viewHolder.textViewVideoName.setText("स्तर " + (position + 1));
                else viewHolder.textViewVideoName.setText("Level " + (position + 1));


//            viewHolder.textViewVideoName.setTextColor();
                viewHolder.textViewVideoName.setTextColor(Color.parseColor(global.getColor()));
                HashMap<String, Object> videoMap = contentArrayList.get(position);
                ArrayList<String> keyArrayList = new ArrayList<>();
                ArrayList<Integer> keyList = new ArrayList<>();
                for (String key : videoMap.keySet()) {
                    keyArrayList.add(key);
                    keyList.add(Integer.parseInt(key));
                }

                int pos = position + 1;
                Collections.sort(keyList);
                VideoListInnerAdapter videoListInnerAdapter = new VideoListInnerAdapter(context, videoMap, keyList, dataMap,type);
                viewHolder.recyclerView.setAdapter(videoListInnerAdapter);

                if(!type.equals("foundationalTopicVideos")) {
                    if(Util.getUnlockVideoLevel(context,currentTopicid)-1>=position)
                    {
                        viewHolder.card.setAlpha(1f);
                        videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                    PalContentListingActivity.duration=0;
                                Util.setVideoLevel(context,pos-1);

                                if(Util.isOfflineMode(context))
                                {
                                    try {
                                        boolean isCompleted;
                                        if (type.equals("foundationalTopicVideos")) {
                                            isCompleted = true;
                                        } else {
                                            isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = ((PalContentListingActivity) context).isCompleted;
                                        }
                                        if (isCompleted) {
                                            try {
                                                for (String keyData : keyArrayList) {
                                                    for (int i = 0; i < contentArrayList.size(); i++) {
                                                        HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                        HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                        if (innerMap != null) {
                                                            innerMap.put("isSelected", "false");
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            String key = keyArrayList.get(position);
                                            PalVideoListFragment.current_videoid=key;
                                            PalContentListingActivity.keys=key;
                                            HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key);
                                            innerMap.put("isSelected", "true");
                                            Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                            notifyDataSetChanged();
                                            videoListInnerAdapter.notifyDataSetChanged();
                                            String completeUrl = innerMap.get("onlineLink");
                                            String offlineLink = innerMap.get("offlineLink");
                                            if(completeUrl.contains("https://vimeo.com/")) type="video_lessons";
                                            else type="diksha_content";
                                            String url;
                                            boolean isLocalFile = false;
                                            if (type.equals("diksha_content")) {
                                                url = completeUrl;
                                                isLocalFile = false;
                                            } else {
                                                String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                                File file = new File(filePath);
                                                if (file.exists() && Util.isOfflineMode(context)) {
                                                    url = file.toString();
                                                    isLocalFile = true;
                                                } else {
                                                    String[] urlArray = completeUrl.split("/");
                                                    url = urlArray[urlArray.length - 1];
                                                    isLocalFile = false;
                                                }
                                            }

                                            String name = innerMap.get("name");
                                            String topicID = innerMap.get("topicID");
                                            if (type.equals("foundationalTopicVideos")) {
                                                boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                                String type;
                                                if (isLocalFile) {
                                                    type = "local";
                                                } else {
                                                    type = "vimeo";
                                                }
                                                if (!isFragmentAdded) {
                                                    ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                    ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                                } else {
                                                    ((ExtraContentListingActivity) context).removeFragment(false);
                                                    boolean finalIsLocalFile1 = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                            ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                        }
                                                    }, 500);
                                                }
                                            } else {
                                                boolean isFragmentAdded = ((PalContentListingActivity) context).isFragmentAdded();
                                                if (!isFragmentAdded) {
                                                    PalContentListingActivity.videoKey = pos + "-" + position;
                                                    ((PalContentListingActivity) context).removeFragment();
                                                    ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, isLocalFile, type, key);
                                                } else {
                                                    ((PalContentListingActivity) context).removeFragment(false);
                                                    boolean finalIsLocalFile = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            PalContentListingActivity.videoKey = pos + "-" + position;
                                                            ((PalContentListingActivity) context).removeFragment();
                                                            ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, finalIsLocalFile, type,key);
                                                        }
                                                    }, 500);
                                                }
                                            }


                                        } else {

                                            String completeType = ((PalContentListingActivity) context).completeType;

                                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                            {
                                                mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
                                                diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                            }else {
                                                mulbhutText = "Please practice basic subject first";
                                                diagnosticText = "Please do diagnostic test first";
                                            }
                                            if (completeType.equalsIgnoreCase("foundationalPractice"))
                                            {
                                                //Util.showToast(context, mulbhutText);
                                                Util.openGifDialogue(context, mulbhutText);
                                            }else {
                                                //Util.showToast(context, diagnosticText);
//                                                    Util.openGifDialogue(context, diagnosticText);
                                                Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
                                            }

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {

                                    if(Util.checkInternetConnection(context))
                                    {
                                        try {
                                            boolean isCompleted;
                                            if (type.equals("foundationalTopicVideos")) {
                                                isCompleted = true;
                                            } else {
                                                isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = ((PalContentListingActivity) context).isCompleted;
                                            }
                                            if (isCompleted) {
                                                try {
                                                    for (Integer keyData : keyList) {
                                                        for (int i = 0; i < contentArrayList.size(); i++) {
                                                            HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                            HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                            if (innerMap != null) {
                                                                innerMap.put("isSelected", "false");
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                Integer key = keyList.get(position);
                                                PalContentListingActivity.keys=key+"";

                                                HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key+"");
                                                innerMap.put("isSelected", "true");
                                                videoListInnerAdapter.notifyDataSetChanged();
                                                Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                                String completeUrl = innerMap.get("onlineLink");
                                                String offlineLink = innerMap.get("offlineLink");

                                                Util.setTopicNameAlt(context,innerMap.get("topicName"));

                                                if(completeUrl.contains("https://vimeo.com/")) type="video_lessons";
                                                else type="diksha_content";

                                                String url;
                                                boolean isLocalFile = false;

                                                if (type.equals("diksha_content")) {
                                                    url = completeUrl;
                                                    isLocalFile = false;
                                                } else {
                                                    String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                                    File file = new File(filePath);
                                                    if (file.exists() && Util.isOfflineMode(context)) {
                                                        url = file.toString();
                                                        isLocalFile = true;
                                                    } else {
                                                        String[] urlArray = completeUrl.split("/");
                                                        url = urlArray[urlArray.length - 1];
                                                        isLocalFile = false;
                                                    }
                                                }

                                                String name = innerMap.get("name");
                                                String topicID = innerMap.get("topicID");
                                                if (type.equals("foundationalTopicVideos")) {
                                                    boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                                    String type;
                                                    if (isLocalFile) {
                                                        type = "local";
                                                    } else {
                                                        type = "vimeo";
                                                    }
                                                    if (!isFragmentAdded) {
                                                        ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                        ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                                    } else {
                                                        ((ExtraContentListingActivity) context).removeFragment(false);
                                                        boolean finalIsLocalFile1 = isLocalFile;
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                                ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                            }
                                                        }, 500);
                                                    }
                                                } else {
                                                    boolean isFragmentAdded = ((PalContentListingActivity) context).isFragmentAdded();
                                                    if (!isFragmentAdded) {
                                                        PalContentListingActivity.videoKey = pos + "-" + position;
                                                        ((PalContentListingActivity) context).removeFragment();
                                                        ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, isLocalFile, type, key+"");
                                                    } else {
                                                        ((PalContentListingActivity) context).removeFragment(false);
                                                        boolean finalIsLocalFile = isLocalFile;
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                PalContentListingActivity.videoKey = pos + "-" + position;
                                                                ((PalContentListingActivity) context).removeFragment();
                                                                ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, finalIsLocalFile, type,key+"");
                                                            }
                                                        }, 100);
                                                    }
                                                }


                                            } else {

                                                String completeType = ((PalContentListingActivity) context).completeType;

                                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                                {
                                                    mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
                                                    diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                                }else {
                                                    mulbhutText = "Please practice basic subject first";
                                                    diagnosticText = "Please do diagnostic test first";
                                                }
                                                if (completeType.equalsIgnoreCase("foundationalPractice"))
                                                {
                                                    //Util.showToast(context, mulbhutText);
                                                    Util.openGifDialogue(context, mulbhutText);
                                                }else {
                                                    //Util.showToast(context, diagnosticText);
//                                                        Util.openGifDialogue(context, diagnosticText);
                                                    Util.showAnimatedDialog(context,Util.COMPLETE_DIAGNOSTIC_FIRST);
                                                }

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        openGifDialogue();
                                    }
                                }

                            }
                        });
                    }
                    else
                    {
                        viewHolder.card.setAlpha(.5f);
                        videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                    PalContentListingActivity.duration=0;


//                            if(Util.getSelectedLanguage(context).equals("hindi")) Util.openGifDialogue(context, "पहले स्तर "+Util.getUnlockVideoLevel(context,currentTopicid)+" पूरा करे");
//                                else Util.openGifDialogue(context, "First complete level "+Util.getUnlockVideoLevel(context,currentTopicid));
//
                                Util.showAnimatedDialog(context,Util.COMPLETE_LEVEL+Util.getUnlockVideoLevel(context,currentTopicid));

                            }
                        });
                    }
                }
                else {
                    viewHolder.card.setAlpha(1f);
                    videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
//                    PalContentListingActivity.duration=0;

                            Util.setVideoLevel(context,pos-1);

                            if(Util.isOfflineMode(context))
                            {
                                try {
                                    boolean isCompleted;
                                    if (type.equals("foundationalTopicVideos")) {
                                        isCompleted = true;
                                    } else {
                                        isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = ((PalContentListingActivity) context).isCompleted;
                                    }
                                    if (isCompleted) {
                                        try {
                                            for (String keyData : keyArrayList) {
                                                for (int i = 0; i < contentArrayList.size(); i++) {
                                                    HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                    HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                    if (innerMap != null) {
                                                        innerMap.put("isSelected", "false");
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        String key = keyArrayList.get(position);
                                        PalVideoListFragment.current_videoid=key;
                                        PalContentListingActivity.keys=key;
                                        HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key);
                                        innerMap.put("isSelected", "true");
                                        Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                        notifyDataSetChanged();
                                        videoListInnerAdapter.notifyDataSetChanged();
                                        String completeUrl = innerMap.get("onlineLink");
                                        String offlineLink = innerMap.get("offlineLink");

                                        if(completeUrl.contains("https://vimeo.com/")) type="video_lessons";
                                        else type="diksha_content";

                                        String url;
                                        boolean isLocalFile = false;
                                        if (type.equals("diksha_content")) {
                                            url = completeUrl;
                                            isLocalFile = false;
                                        } else {
                                            String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                            File file = new File(filePath);
                                            if (file.exists() && Util.isOfflineMode(context)) {
                                                url = file.toString();
                                                isLocalFile = true;
                                            } else {
                                                String[] urlArray = completeUrl.split("/");
                                                url = urlArray[urlArray.length - 1];
                                                isLocalFile = false;
                                            }
                                        }

                                        String name = innerMap.get("name");
                                        String topicID = innerMap.get("topicID");
                                        if (type.equals("foundationalTopicVideos")) {
                                            boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                            String type;
                                            if (isLocalFile) {
                                                type = "local";
                                            } else {
                                                type = "vimeo";
                                            }
                                            if (!isFragmentAdded) {
                                                ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                            } else {
                                                ((ExtraContentListingActivity) context).removeFragment(false);
                                                boolean finalIsLocalFile1 = isLocalFile;
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                        ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                    }
                                                }, 500);
                                            }
                                        } else {
                                            boolean isFragmentAdded = ((PalContentListingActivity) context).isFragmentAdded();
                                            if (!isFragmentAdded) {
                                                PalContentListingActivity.videoKey = pos + "-" + position;
                                                ((PalContentListingActivity) context).removeFragment();
                                                ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, isLocalFile, type, key);
                                            } else {
                                                ((PalContentListingActivity) context).removeFragment(false);
                                                boolean finalIsLocalFile = isLocalFile;
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        PalContentListingActivity.videoKey = pos + "-" + position;
                                                        ((PalContentListingActivity) context).removeFragment();
                                                        ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, finalIsLocalFile, type,key);
                                                    }
                                                }, 500);
                                            }
                                        }


                                    } else {

                                        String completeType = ((PalContentListingActivity) context).completeType;

                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                        {
                                            mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
//                                            diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                        }else {
                                            mulbhutText = "Please practice basic subject first";
//                                            diagnosticText = "Please do diagnostic test first";
                                        }
                                        if (completeType.equalsIgnoreCase("foundationalPractice"))
                                        {
                                            //Util.showToast(context, mulbhutText);
//                                            Util.openGifDialogue(context, mulbhutText);
                                            Util.showAnimatedDialog(context, mulbhutText);
                                        }else {
                                            //Util.showToast(context, diagnosticText);
//                                            Util.openGifDialogue(context, diagnosticText);
                                            Util.showAnimatedDialog(context, Util.COMPLETE_DIAGNOSTIC_FIRST);
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {

                                if(Util.checkInternetConnection(context))
                                {
                                    try {
                                        boolean isCompleted;
                                        if (type.equals("foundationalTopicVideos")) {
                                            isCompleted = true;
                                        } else {
                                            isCompleted = ((PalContentListingActivity) context).getTopicCompleteStatus(Util.getTopicID(context));
//                            isCompleted = ((PalContentListingActivity) context).isCompleted;
                                        }
                                        if (isCompleted) {
                                            try {
                                                for (Integer keyData : keyList) {
                                                    for (int i = 0; i < contentArrayList.size(); i++) {
                                                        HashMap<String, Object> videoMapTo = contentArrayList.get(i);
                                                        HashMap<String, String> innerMap = (HashMap<String, String>) videoMapTo.get(keyData);
                                                        if (innerMap != null) {
                                                            innerMap.put("isSelected", "false");
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Integer key = keyList.get(position);
                                            PalContentListingActivity.keys=key+"";

                                            HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key+"");
                                            innerMap.put("isSelected", "true");
                                            Util.setTopicNameAlt(context,innerMap.get("topicName"));
                                            videoListInnerAdapter.notifyDataSetChanged();
                                            String completeUrl = innerMap.get("onlineLink");
                                            String offlineLink = innerMap.get("offlineLink");
                                            if(completeUrl.contains("https://vimeo.com/")) type="video_lessons";
                                            else type="diksha_content";
                                            String url;
                                            boolean isLocalFile = false;
                                            if (type.equals("diksha_content")) {
                                                url = completeUrl;
                                                isLocalFile = false;
                                            } else {
                                                String filePath = Util.getSDCardPath(context) + "/.iDream_content/multimedia/" + offlineLink;
                                                File file = new File(filePath);
                                                if (file.exists() && Util.isOfflineMode(context)) {
                                                    url = file.toString();
                                                    isLocalFile = true;
                                                } else {
                                                    String[] urlArray = completeUrl.split("/");
                                                    url = urlArray[urlArray.length - 1];
                                                    isLocalFile = false;
                                                }
                                            }

                                            String name = innerMap.get("name");
                                            String topicID = innerMap.get("topicID");
                                            if (type.equals("foundationalTopicVideos")) {
                                                boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                                                String type;
                                                if (isLocalFile) {
                                                    type = "local";
                                                } else {
                                                    type = "vimeo";
                                                }
                                                if (!isFragmentAdded) {
                                                    ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                    ExtraContentListingActivity.openFragment(url, name, topicID, type, isLocalFile);
                                                } else {
                                                    ((ExtraContentListingActivity) context).removeFragment(false);
                                                    boolean finalIsLocalFile1 = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ExtraContentListingActivity.videoKey = pos + "-" + position;
                                                            ExtraContentListingActivity.openFragment(url, name, topicID, type, finalIsLocalFile1);
                                                        }
                                                    }, 500);
                                                }
                                            } else {
                                                boolean isFragmentAdded = ((PalContentListingActivity) context).isFragmentAdded();
                                                if (!isFragmentAdded) {
                                                    PalContentListingActivity.videoKey = pos + "-" + position;
                                                    ((PalContentListingActivity) context).removeFragment();
                                                    ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, isLocalFile, type, key+"");
                                                } else {
                                                    ((PalContentListingActivity) context).removeFragment(false);
                                                    boolean finalIsLocalFile = isLocalFile;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            PalContentListingActivity.videoKey = pos + "-" + position;
                                                            ((PalContentListingActivity) context).removeFragment();
                                                            ((PalContentListingActivity) context).openFragment(url,offlineLink, name, topicID, finalIsLocalFile, type,key+"");
                                                        }
                                                    }, 100);
                                                }
                                            }


                                        } else {

                                            String completeType = ((PalContentListingActivity) context).completeType;

                                            if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                            {
                                                mulbhutText = "कृपया पहले मुलभुत विषय का अभ्यास करें";
//                                            diagnosticText = "कृपया पहले डायग्नोस्टिक परिक्षण करें";
                                            }else {
                                                mulbhutText = "Please practice basic subject first";
//                                            diagnosticText = "Please do diagnostic test first";
                                            }
                                            if (completeType.equalsIgnoreCase("foundationalPractice"))
                                            {
                                                //Util.showToast(context, mulbhutText);
//                                            Util.openGifDialogue(context, mulbhutText);
                                                Util.showAnimatedDialog(context, mulbhutText);
                                            }else {
                                                //Util.showToast(context, diagnosticText);
//                                            Util.openGifDialogue(context, diagnosticText);
                                                Util.showAnimatedDialog(context, Util.COMPLETE_DIAGNOSTIC_FIRST);
                                            }

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    openGifDialogue();
                                }
                            }

                        }
                    });

                }
            }



        }
    }

    private void openGifDialogue() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_toast_messages);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        TextView text = dialog.findViewById(R.id.text);

        TextView text_ = dialog.findViewById(R.id.text_);
        TextView textViewRetry = dialog.findViewById(R.id.textViewRetry);


        if(Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            text.setText("इंटरनेट नहीं है");
            text_.setText("कृपया अपना इंटरनेट चेक करे ");
            textViewRetry.setText("इंटरनेट चालू करें");
        }
        else
        {
            text.setText("Oops! No Internet");
            text_.setText("Please check your network connection");
            textViewRetry.setText("Turn on Wifi");
        }


        ImageView imageViewGif = dialog.findViewById(R.id.imageViewGif);
        Glide.with(context)
                .load(R.raw.no_internet)
                .into(imageViewGif);

        textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {

        if (contentArrayList == null) {

            return 0;
        } else {

            return contentArrayList.size();
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;
        protected RecyclerView recyclerView;
        protected LinearLayout card;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            recyclerView = holderView.findViewById(R.id.recyclerView);
            card = holderView.findViewById(R.id.card);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            if(Util.isPortraitMode(context)) recyclerView.setLayoutManager(new LinearLayoutManager(context));
                else recyclerView.setLayoutManager(manager);
//            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}