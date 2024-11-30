package com.idreameducation.ipreppal.pal.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;
import com.idreameducation.ipreppal.pal.adapter.ExtraConetntListInnerAdapter;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PalExtraContentListFragment extends Fragment {
    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private String subject;
    private String icon;
    private String language;
    private String type;
    private String name;
    private String topicID;
    private TextView topicName;
    private ExtraConetntListInnerAdapter extraConetntListInnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_video, container, false);
        context = getActivity();
        global = (Global) getApplicationContext();
        System.out.println("±±±±±±±± create");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(manager);

        board = ((ExtraContentListingActivity) context).board;
        sClass = ((ExtraContentListingActivity) context).sClass;
        language = ((ExtraContentListingActivity) context).language;
        topicName = view.findViewById(R.id.textViewTopicName);

        String topicName_ =((ExtraContentListingActivity) context).name;

        if (topicName_ == null)
        {
            topicName.setVisibility(View.GONE);
        }else {
            topicName.setTextColor(Color.parseColor("#000000"));
            topicName.setText(((ExtraContentListingActivity) context).name);
        }



        extraConetntListInnerAdapter = new ExtraConetntListInnerAdapter(context, ((ExtraContentListingActivity) context).contentArrayList, type);
        recyclerView.setAdapter(extraConetntListInnerAdapter);
        extraConetntListInnerAdapter.SetOnItemClickListener(new ExtraConetntListInnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (type.equalsIgnoreCase("books")) {
                    String url = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("onlineLink");
                    String name = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("name");
                    String offlineLink = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("offlineLink");
                    String topicId = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("subjectID");
                    String bookId = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("id");
                    String topic_name_main = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("topicName");

                    Util.preventTwoClick(view);
                    Intent intent = new Intent(context, PalPdfViewerActivity.class);

                    intent.putExtra("onlineLink", url);
                    intent.putExtra("topicName", name);
                    intent.putExtra("offlineLink", offlineLink);
                    intent.putExtra("topicId",topicId);
                    intent.putExtra("bookId",bookId);
                    intent.putExtra("topic_name_main",topic_name_main);

                    startActivity(intent);
                    
                } else {

                    System.out.println("±±±±±±±± videos");

                    String url = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("onlineLink");
                    String offlineLink = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("offlineLink");
                    String name = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("name");

                    String type;

                    String filePath = Util.getSDCardPath(context)+"/.iDream_content/videos/"+offlineLink;
                    File file = new File(filePath);

                    if(file.exists() && Util.isOfflineMode(context)){
                        url = file.toString();
                        type = "local";
                    }else{
                        if (url.contains("youtube")) {
                            type = "youtube";
                        } else {
                            type = "vimeo";
                        }
                    }

                    boolean isFragmentAdded = ((ExtraContentListingActivity) context).isFragmentAdded();
                    if (!isFragmentAdded) {
                        ((ExtraContentListingActivity) context).openFragment(url, name, null,type, false);
                    } else {
                        ((ExtraContentListingActivity) context).removeFragment(false);
                        String finalUrl = url;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((ExtraContentListingActivity) context).openFragment(finalUrl, name, null, type, false);
                            }
                        }, 500);
                    }

                }


            }
        });
        return view;
    }

    private ArrayList<HashMap<String, String>> contentArrayList;

    public PalExtraContentListFragment(String contentType, ArrayList<HashMap<String, String>> contentArrayList, String name) {
        this.type = contentType;
        this.contentArrayList = contentArrayList;
        this.name = name;
    }

    public PalExtraContentListFragment() {

    }

    private void getReports() {
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("latest_data").child(Util.getUserId(context)).child(board).child(sClass).child(Util.getSubject(context)).child("video").child(topicID).child("detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> dataMap;
                    if (snapshot.getValue() != null) {
                        dataMap = (HashMap<String, Object>) snapshot.getValue();
                    } else {
                        dataMap = null;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
