package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.PalReportPracticeAdapter;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class PalReportsTestFragment extends Fragment {
    private Context context;
    private Global global;
    private String subject;
    private String type;
    private ArrayList<HashMap<String, Object>> mainArrayList;
    private ArrayList<String> dateArrayList;
    private ArrayList<String> nameArrayList;
    private RecyclerView recyclerView;

    private Button startLearningTextView;

    private String boardSelected,classSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_detail_reports, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        assignIds(view);
        return view;
    }

    private void assignIds(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


        boardSelected = Util.getSelectedBoard(context);
        classSelected = Util.getSelectedClass(context);


        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        PalReportPracticeAdapter palReportPracticeAdapter = new PalReportPracticeAdapter(context, mainArrayList, dateArrayList,nameArrayList, subject , type);
        recyclerView.setAdapter(palReportPracticeAdapter);
        palReportPracticeAdapter.SetOnItemClickListener(new PalReportPracticeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    public PalReportsTestFragment(String subject, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> dateArrayList, ArrayList<String> nameArrayList, String type) {
        this.subject = subject;
        this.type = type;
        this.mainArrayList = mainArrayList;
        this.dateArrayList = dateArrayList;
        this.nameArrayList = nameArrayList;
    }



}
