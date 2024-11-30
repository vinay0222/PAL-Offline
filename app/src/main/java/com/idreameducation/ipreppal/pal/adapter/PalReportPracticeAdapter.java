////package com.idreameducation.ipreppal.pal.adapter;
////
////import android.content.Context;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.TextView;
////
////import androidx.recyclerview.widget.GridLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.bumptech.glide.load.engine.DiskCacheStrategy;
////import com.bumptech.glide.request.RequestOptions;
////import com.idreameducation.ipreppal.R;
//
//
//
////
////import java.util.ArrayList;
////import java.util.HashMap;
////
////
////public class PalReportPracticeAdapter extends RecyclerView.Adapter {
////
////    private static final int TYPE_ITEM = 0;
////    OnItemClickListener clickListener;
////    private ArrayList<HashMap<String, Object>> mainArrayList;
////    private ArrayList<String> dateArrayList;
////    private ArrayList<String> nameArrayList;
////
////    private Context context;
////    private RequestOptions requestOptions;
////    private String subject;
////    private String type;
////
////    public PalReportPracticeAdapter(Context context, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, String subject, String type) {
////        this.context = context;
////        this.subject = subject;
////        this.type = type;
////        this.mainArrayList = mainArrayList;
////        this.dateArrayList = dateArrayList;
////        this.nameArrayList = nameArrayList;
////        requestOptions = new RequestOptions();
////        requestOptions.dontTransform();
////        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
////    }
////
////    @Override
////    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
////        if (viewType == TYPE_ITEM) {
////            View view = LayoutInflater.from(viewGroup.getContext())
////                    .inflate(R.layout.pal_row_practice_reports, viewGroup, false);
////            return new PalReportPracticeAdapter.ViewItem(view);
////        }
////        return null;
////    }
////
////    @Override
////    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
////        if (viewHolderValue instanceof PalReportPracticeAdapter.ViewItem) {
////            final PalReportPracticeAdapter.ViewItem viewHolder = (PalReportPracticeAdapter.ViewItem) viewHolderValue;
////            viewHolder.textViewdate.setText(dateArrayList.get(position));
////
////            HashMap<String , Object> mainHashMap = (HashMap<String , Object>)mainArrayList.get(position).get(dateArrayList.get(position));
////            ArrayList<String> keyArrayList = new ArrayList<>();
////            for(String key :mainHashMap.keySet()){
////                keyArrayList.add(key);
////            }
////
////            if (type.equals("video_lessons"))
////            {
////                ArrayList<String> keyArrayList_ = new ArrayList<>();
////                HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));
////                for(String key :mainHashMap_.keySet()){
////                    keyArrayList_.add(key);
////
////                }
////                PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList,subject, dateArrayList.get(position) , type);
////                viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
////                palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(View view, int position) {
////
////                    }
////                });
////            }else {
////
////                PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap, keyArrayList, nameArrayList,subject, dateArrayList.get(position) , type);
////                viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
////                palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(View view, int position) {
////
////                    }
////                });
////
////            }
////
////
////
////        }
////    }
////
////    @Override
////    public int getItemViewType(int position) {
////
////        return TYPE_ITEM;
////    }
////
////    @Override
////    public int getItemCount() {
////        return dateArrayList.size();
////    }
////
////    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
////        this.clickListener = itemClickListener;
////    }
////
////    public interface OnItemClickListener {
////        public void onItemClick(View view, int position);
////    }
////
////    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
////        protected TextView textViewdate;
////        protected RecyclerView recyclerView;
////
////        public ViewItem(View holderView) {
////            super(holderView);
////            textViewdate = holderView.findViewById(R.id.textViewdate);
////            recyclerView = holderView.findViewById(R.id.recyclerView);
////            recyclerView.setHasFixedSize(true);
////            GridLayoutManager manager = new GridLayoutManager(context, 2);
////            recyclerView.setLayoutManager(manager);
////            holderView.setOnClickListener(this);
////
////        }
////
////        @Override
////        public void onClick(View view) {
////            clickListener.onItemClick(view, getPosition());
////        }
////    }
////
////}
//package com.idreameducation.ipreppal.pal.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.Target;
//import com.idreameducation.ipreppal.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
//public class PalReportPracticeAdapter extends RecyclerView.Adapter {
//
//    private static final int TYPE_ITEM = 0;
//    OnItemClickListener clickListener;
//    private ArrayList<HashMap<String, Object>> mainArrayList;
//    private ArrayList<String> dateArrayList;
//    private ArrayList<String> nameArrayList;
//
//    private Context context;
//    private RequestOptions requestOptions;
//    private String subject;
//    private String type;
//
//    public PalReportPracticeAdapter(Context context, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, String subject, String type) {
//        this.context = context;
//        this.subject = subject;
//        this.type = type;
//        this.mainArrayList = mainArrayList;
//        this.dateArrayList = dateArrayList;
//        this.nameArrayList = nameArrayList;
//        requestOptions = new RequestOptions();
//        requestOptions.dontTransform();
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if (viewType == TYPE_ITEM) {
//            View view = LayoutInflater.from(viewGroup.getContext())
//                    .inflate(R.layout.pal_row_practice_reports, viewGroup, false);
//            return new PalReportPracticeAdapter.ViewItem(view);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
//        if (viewHolderValue instanceof PalReportPracticeAdapter.ViewItem) {
//            final PalReportPracticeAdapter.ViewItem viewHolder = (PalReportPracticeAdapter.ViewItem) viewHolderValue;
//            viewHolder.textViewdate.setText(dateArrayList.get(position));
//
//            HashMap<String , Object> mainHashMap = (HashMap<String , Object>)mainArrayList.get(position).get(dateArrayList.get(position));
//            ArrayList<String> keyArrayList = new ArrayList<>();
//            for(String key :mainHashMap.keySet()){
//                keyArrayList.add(key);
//            }
//
//            if (type.equals("video_lessons"))
//            {
//
//
//                for (int i=0;i<keyArrayList.size();i++)
//                {
//                    ArrayList<String> keyArrayList_ = new ArrayList<>();
//                    HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(i));
//                    for(String key :mainHashMap_.keySet()){
//                        keyArrayList_.add(key);
//                    }
//                    PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList,subject, dateArrayList.get(position) , type);
//                    viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
//                    palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//
//                        }
//                    });
//                }
//
//
//            }else {
//
//                PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap, keyArrayList, nameArrayList,subject, dateArrayList.get(position) , type);
//                viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
//                palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//                });
//
//            }
//
//
//
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        return TYPE_ITEM;
//    }
//
//    @Override
//    public int getItemCount() {
//        return dateArrayList.size();
//    }
//
//    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }
//
//    public interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//    }
//
//    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
//        protected TextView textViewdate;
//        protected RecyclerView recyclerView;
//
//        public ViewItem(View holderView) {
//            super(holderView);
//            textViewdate = holderView.findViewById(R.id.textViewdate);
//            recyclerView = holderView.findViewById(R.id.recyclerView);
//            recyclerView.setHasFixedSize(true);
//            GridLayoutManager manager = new GridLayoutManager(context, 2);
//            recyclerView.setLayoutManager(manager);
//            holderView.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View view) {
//            clickListener.onItemClick(view, getPosition());
//        }
//    }
//
//}

//package com.idreameducation.ipreppal.pal.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.idreameducation.ipreppal.R;



//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
//public class PalReportPracticeAdapter extends RecyclerView.Adapter {
//
//    private static final int TYPE_ITEM = 0;
//    OnItemClickListener clickListener;
//    private ArrayList<HashMap<String, Object>> mainArrayList;
//    private ArrayList<String> dateArrayList;
//    private ArrayList<String> nameArrayList;
//
//    private Context context;
//    private RequestOptions requestOptions;
//    private String subject;
//    private String type;
//
//    public PalReportPracticeAdapter(Context context, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, String subject, String type) {
//        this.context = context;
//        this.subject = subject;
//        this.type = type;
//        this.mainArrayList = mainArrayList;
//        this.dateArrayList = dateArrayList;
//        this.nameArrayList = nameArrayList;
//        requestOptions = new RequestOptions();
//        requestOptions.dontTransform();
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if (viewType == TYPE_ITEM) {
//            View view = LayoutInflater.from(viewGroup.getContext())
//                    .inflate(R.layout.pal_row_practice_reports, viewGroup, false);
//            return new PalReportPracticeAdapter.ViewItem(view);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
//        if (viewHolderValue instanceof PalReportPracticeAdapter.ViewItem) {
//            final PalReportPracticeAdapter.ViewItem viewHolder = (PalReportPracticeAdapter.ViewItem) viewHolderValue;
//            viewHolder.textViewdate.setText(dateArrayList.get(position));
//
//            HashMap<String , Object> mainHashMap = (HashMap<String , Object>)mainArrayList.get(position).get(dateArrayList.get(position));
//            ArrayList<String> keyArrayList = new ArrayList<>();
//            for(String key :mainHashMap.keySet()){
//                keyArrayList.add(key);
//            }
//
//            if (type.equals("video_lessons"))
//            {
//                ArrayList<String> keyArrayList_ = new ArrayList<>();
//                HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));
//                for(String key :mainHashMap_.keySet()){
//                    keyArrayList_.add(key);
//
//                }
//                PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList,subject, dateArrayList.get(position) , type);
//                viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
//                palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//                });
//            }else {
//
//                PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap, keyArrayList, nameArrayList,subject, dateArrayList.get(position) , type);
//                viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
//                palReportInnerAdapter.SetOnItemClickListener(new PalReportInnerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//                });
//
//            }
//
//
//
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        return TYPE_ITEM;
//    }
//
//    @Override
//    public int getItemCount() {
//        return dateArrayList.size();
//    }
//
//    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }
//
//    public interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//    }
//
//    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
//        protected TextView textViewdate;
//        protected RecyclerView recyclerView;
//
//        public ViewItem(View holderView) {
//            super(holderView);
//            textViewdate = holderView.findViewById(R.id.textViewdate);
//            recyclerView = holderView.findViewById(R.id.recyclerView);
//            recyclerView.setHasFixedSize(true);
//            GridLayoutManager manager = new GridLayoutManager(context, 2);
//            recyclerView.setLayoutManager(manager);
//            holderView.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View view) {
//            clickListener.onItemClick(view, getPosition());
//        }
//    }
//
//}
package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class PalReportPracticeAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, Object>> mainArrayList;
    private final ArrayList<String> dateArrayList;
    private final ArrayList<String> nameArrayList;

    private final Context context;
    private final RequestOptions requestOptions;
    private static String subject,type;

    HashMap<String, Object> mainHashMap_;
    ArrayList<String> keyArrayList_ = new ArrayList<>();

//    PalReportPracticeAdapter.ViewItem viewHolder;

    public PalReportPracticeAdapter(Context context, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, String subject, String type) {
        this.context = context;
        PalReportPracticeAdapter.subject = subject;
        PalReportPracticeAdapter.type = type;
        this.mainArrayList = mainArrayList;
        this.dateArrayList = dateArrayList;
        this.nameArrayList = nameArrayList;
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.pal_row_practice_reports, viewGroup, false);
            return new ViewItem(view);
        }
        return null;
    }

    @SuppressLint({"RecyclerView", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, int position) {
        if (viewHolderValue instanceof ViewItem) {
            final ViewItem viewHolder = (ViewItem) viewHolderValue;
            viewHolder.textViewdate.setText(dateArrayList.get(position));


            viewHolder.linearLayout.removeAllViews();

            HashMap<String , Object> mainHashMap = (HashMap<String , Object>)mainArrayList.get(position).get(dateArrayList.get(position));

            ArrayList<String> keyArrayList = new ArrayList<>();
            for(String key :mainHashMap.keySet()){
                keyArrayList.add(key);

            }


            ArrayList<String> nameList=new ArrayList<>();

            if (type.equals("video_lessons")) {
                mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));
                try {
                    for(String key :mainHashMap_.keySet()) keyArrayList_.add(key);
                    for(int i=0;i<keyArrayList.size();i++) {

                        ArrayList<String> keyArrayList_ = new ArrayList<>();

                        HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(i));

                        for (String key : mainHashMap_.keySet()) {
                            keyArrayList_.add(key);

                        }

                        HashMap<String, Object> mainHashMap4 = (HashMap<String, Object>) mainHashMap_.get(keyArrayList_.get(0));

                        for (String key : mainHashMap4.keySet()) {
                            HashMap<String, Object> mainHashMap5= (HashMap<String, Object>) mainHashMap4.get(key);
                            nameList.add(mainHashMap5.get("topicName").toString());
                            break;
                        }


                        PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList, subject, dateArrayList.get(position), type);
                        viewHolder.recyclerView.setAdapter(palReportInnerAdapter);

                        viewHolder.recyclerView_topic.setAdapter(new PalReport_topicAdapter(context,viewHolder,mainArrayList,dateArrayList,nameList,keyArrayList,position));

                    }
                } catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }
            else if (type.equals("books_ncert") || type.equals("books_stories")) {
                mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));



                try {

                    for(String key :mainHashMap_.keySet()){
                        keyArrayList_.add(key);

                    }

//                    create_topics_text(position,keyArrayList,-1);

                    for(int i=0;i<keyArrayList.size();i++) {

                        ArrayList<String> keyArrayList_ = new ArrayList<>();

                        HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(i));

                        for (String key : mainHashMap_.keySet()) {
                            keyArrayList_.add(key);

                        }

                        nameList.add(nameArrayList.get(i));

                        PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList, subject, dateArrayList.get(position), type);
                        viewHolder.recyclerView.setAdapter(palReportInnerAdapter);

                        viewHolder.recyclerView_topic.setVisibility(View.GONE);
//                        viewHolder.recyclerView_topic.setAdapter(new PalReport_topicAdapter(context,viewHolder,mainArrayList,dateArrayList,nameList,keyArrayList,position));


//                        Button tv = new Button(context);
//                        tv.setText(nameArrayList.get(i));
//                        tv.setTextSize(12);
//                        tv.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
//                        tv.setPadding(15,0,15,0);
//
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,26);
//                        params.setMargins(10,0,10,0);
//
//                        tv.setLayoutParams(params);
//
//
//                        int finalI = i;
//                        tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                update_position(context,viewHolder,mainArrayList,dateArrayList,nameArrayList,position,finalI,keyArrayList);
//
//
//                            }
//                        });
//
//                        viewHolder.linearLayout.addView(tv);

                    }



                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }
            else if (type.equals("diksha_content")) {
                mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));


                try {

                    for(String key :mainHashMap_.keySet()){
                        keyArrayList_.add(key);

                    }

//                    create_topics_text(position,keyArrayList,-1);

                    for(int i=0;i<keyArrayList.size();i++) {

                        ArrayList<String> keyArrayList_ = new ArrayList<>();

                        HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(i));

                        for (String key : mainHashMap_.keySet()) {
                            keyArrayList_.add(key);

                        }

                        nameList.add(nameArrayList.get(i));

                        PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList, subject, dateArrayList.get(position), type);
                        viewHolder.recyclerView.setAdapter(palReportInnerAdapter);

                        viewHolder.recyclerView_topic.setAdapter(new PalReport_topicAdapter(context,viewHolder,mainArrayList,dateArrayList,nameList,keyArrayList,position));


//                        Button tv = new Button(context);
//                        tv.setText(nameArrayList.get(i));
//                        tv.setTextSize(12);
//                        tv.setBackgroundResource(R.drawable.white_solid_shadow_black_border);
//                        tv.setPadding(15,0,15,0);
//
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,26);
//                        params.setMargins(10,0,10,0);
//
//                        tv.setLayoutParams(params);
//
//
//                        int finalI = i;
//                        tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                update_position(context,viewHolder,mainArrayList,dateArrayList,nameArrayList,position,finalI,keyArrayList);
//
//
//                            }
//                        });
//
//                        viewHolder.linearLayout.addView(tv);

                    }



                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }
            else if (type.equals("project_video") || type.equals("simulation_content")) {
                mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(0));


                try {

                    for(String key :mainHashMap_.keySet()){
                        keyArrayList_.add(key);
                    }

                    for(int i=0;i<keyArrayList.size();i++) {

                        ArrayList<String> keyArrayList_ = new ArrayList<>();

                        HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(i));

                        for (String key : mainHashMap_.keySet()) {
                            keyArrayList_.add(key);

                        }

                        nameList.add(nameArrayList.get(i));

                        PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList, subject, dateArrayList.get(position), type);
                        viewHolder.recyclerView.setAdapter(palReportInnerAdapter);

                        viewHolder.recyclerView_topic.setAdapter(new PalReport_topicAdapter(context,viewHolder,mainArrayList,dateArrayList,nameList,keyArrayList,position));

                    }



                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }
            else {

                if(type.equals("video_lessons"))
                {
                    PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList, nameArrayList,subject, dateArrayList.get(position) , type);
                    viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
                }
                else
                {
                    PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap, keyArrayList, nameArrayList,subject, dateArrayList.get(position) , type);
                    viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
                }

            }



        }
    }

    public static void update_position(Context context, ViewItem viewHolder, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, int position, int finalI, ArrayList<String> keyArrayList) {

        ArrayList<String> keyArrayList_ = new ArrayList<>();

        try
        {
            HashMap<String, Object> mainHashMap_ = (HashMap<String, Object>) ((HashMap<String, Object>) mainArrayList.get(position).get(dateArrayList.get(position))).get(keyArrayList.get(finalI));
            for(String key :mainHashMap_.keySet()) keyArrayList_.add(key);
            PalReportInnerAdapter palReportInnerAdapter = new PalReportInnerAdapter(context, mainHashMap_, keyArrayList_, nameArrayList,subject, dateArrayList.get(position) , type);
            viewHolder.recyclerView.setAdapter(palReportInnerAdapter);
        }
        catch (Exception rr) { rr.printStackTrace();}

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return dateArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewdate;
        protected RecyclerView recyclerView,recyclerView_topic;
        LinearLayout linearLayout;

        public ViewItem(View holderView) {
            super(holderView);
            textViewdate = holderView.findViewById(R.id.textViewdate);
            recyclerView = holderView.findViewById(R.id.recyclerView);
            recyclerView_topic = holderView.findViewById(R.id.recyclerView_topic);

            linearLayout =holderView.findViewById(R.id.lll);

            recyclerView.setHasFixedSize(true);
            recyclerView_topic.setHasFixedSize(true);
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            LinearLayoutManager man=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            LinearLayoutManager man2=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
            recyclerView_topic.setLayoutManager(man);
            if(Util.isPortraitMode(context)) recyclerView.setLayoutManager(man2);
            else recyclerView.setLayoutManager(manager);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}