package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoListAdapterExtra extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    VideoListAdapterExtra.OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, Object>> contentArrayList;
    private final Context context;
    private final RequestOptions requestOptions;
    private final String classSelected;
    private final String subjectSelected;
    private final String boardSelected;
    private final String languageSelected;
    private final String icon;
    private final Fragment fragment;
    public HashMap<String, Object> dataMap;
    private final String type;
    private final Global global;
    private String mulbhutText, diagnosticText;
    public VideoListAdapterExtra(Context context, ArrayList<HashMap<String, Object>> contentArrayList, String classSelected, String subjectSelected, String languageSelected, String boardSelected, String icon, Fragment fragment, HashMap<String, Object> dataMap, String type) {
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
        this.dataMap = dataMap;
        this.type = type;
        global = (Global) context.getApplicationContext();
    }

    @Override
    public VideoListAdapterExtra.ViewItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.iprep_row_video, viewGroup, false);
            return new VideoListAdapterExtra.ViewItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof VideoListAdapterExtra.ViewItem) {
            final VideoListAdapterExtra.ViewItem viewHolder = (VideoListAdapterExtra.ViewItem) viewHolderValue;
            viewHolder.textViewVideoName.setText("Level " + (position + 1));
//            viewHolder.textViewVideoName.setTextColor();
            viewHolder.textViewVideoName.setTextColor(Color.parseColor(global.getColor()));
            HashMap<String, Object> videoMap = (HashMap<String, Object>) contentArrayList.get(position);
            ArrayList<String> keyArrayList = new ArrayList<>();
            for (String key : videoMap.keySet()) {
                keyArrayList.add(key);
            }

            int pos = position + 1;
            VideoListInnerAdapter_extra videoListInnerAdapter = new VideoListInnerAdapter_extra(context, videoMap, keyArrayList, dataMap);
            viewHolder.recyclerView.setAdapter(videoListInnerAdapter);
            videoListInnerAdapter.SetOnItemClickListener(new VideoListInnerAdapter_extra.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
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
                                        HashMap<String, Object> videoMapTo = (HashMap<String, Object>) contentArrayList.get(i);
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
                            HashMap<String, String> innerMap = (HashMap<String, String>) videoMap.get(key);
                            innerMap.put("isSelected", "true");
                            notifyDataSetChanged();
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
                                    ((ExtraContentListingActivity) context).videoKey = pos + "-" + position;
                                    ((ExtraContentListingActivity) context).openFragment(url, name, topicID, type, isLocalFile);
                                } else {
                                    ((ExtraContentListingActivity) context).removeFragment(false);
                                    boolean finalIsLocalFile1 = isLocalFile;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((ExtraContentListingActivity) context).videoKey = pos + "-" + position;
                                            ((ExtraContentListingActivity) context).openFragment(url, name, topicID, type, finalIsLocalFile1);
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
                                Util.openGifDialogue(context, diagnosticText);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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

    public void SetOnItemClickListener(final VideoListInnerAdapter_extra.OnItemClickListener itemClickListener) {
        this.clickListener = (OnItemClickListener) itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewVideoName;
        protected RecyclerView recyclerView;

        public ViewItem(View holderView) {
            super(holderView);
            textViewVideoName = holderView.findViewById(R.id.textViewVideoName);
            recyclerView = holderView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            recyclerView.setLayoutManager(manager);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}