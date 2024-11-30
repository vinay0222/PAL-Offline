package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.ExtraContentListingActivity;
import com.idreameducation.ipreppal.pal.adapter.ExtraContentBooksListingAdapter;
import com.idreameducation.ipreppal.util.Util;

public class PalBooksListingFragment extends Fragment {
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

    private int posi;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_books_listing, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        if(Util.isPortraitMode(context))
        {
            board = ((com.idreameducation.ipreppal.PalMobile.activity.ExtraContentListingActivity) context).board;
            sClass = com.idreameducation.ipreppal.PalMobile.activity.ExtraContentListingActivity.sClass;
            language = ((com.idreameducation.ipreppal.PalMobile.activity.ExtraContentListingActivity) context).language;

        }
        else
        {
            board = ((ExtraContentListingActivity) context).board;
            sClass = ExtraContentListingActivity.sClass;
            language = ((ExtraContentListingActivity) context).language;

        }

        topicName = view.findViewById(R.id.textViewTopicName);

        ExtraContentBooksListingAdapter extraConetntListInnerAdapter = new ExtraContentBooksListingAdapter(context, type,posi);
        recyclerView.setAdapter(extraConetntListInnerAdapter);

//        extraConetntListInnerAdapter.SetOnItemClickListener(new ExtraContentBooksListingAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//
////                    String url = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("onlineLink");
////                    String name = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("name");
////                    String offlineLink = ((ExtraContentListingActivity) context).contentArrayList.get(position).get("offlineLink");
////
////                    Intent intent = new Intent(context, PalPdfViewerActivity.class);
////
////                    intent.putExtra("onlineLink", url);
////                    intent.putExtra("topicName", name);
////                    intent.putExtra("offlineLink", offlineLink);
////
////                    startActivity(intent);
////                    getActivity().
//
//
//
//            }
//        });
        return view;
    }

    public void refreshAdapter(int position){
        ExtraContentBooksListingAdapter extraConetntListInnerAdapter = new ExtraContentBooksListingAdapter(context, type,position);
        recyclerView.setAdapter(extraConetntListInnerAdapter);
        extraConetntListInnerAdapter.notifyDataSetChanged();
        recyclerView.getAdapter().notifyDataSetChanged();

    }


    public PalBooksListingFragment(String contentType, String name, int pos) {
        this.type = contentType;
        this.name = name;
        this.posi = pos;
    }

    public PalBooksListingFragment() {

    }


}

