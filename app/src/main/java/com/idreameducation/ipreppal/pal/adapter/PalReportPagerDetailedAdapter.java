package com.idreameducation.ipreppal.pal.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idreameducation.ipreppal.pal.fragments.PalReportsPracticeFragment;
import com.idreameducation.ipreppal.pal.fragments.PalReportsTestFragment;
import com.idreameducation.ipreppal.pal.fragments.PalReportsVideoFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PalReportPagerDetailedAdapter extends FragmentPagerAdapter {
    private ArrayList<String> subjectArrayList;
    private ArrayList<String> dateArrayList;
    private ArrayList<String> nameArrayList;
    private ArrayList<HashMap<String, Object>> mainArrayList;
    private String type;

    public PalReportPagerDetailedAdapter(@NonNull FragmentManager fm, ArrayList<String> dateArrayList,ArrayList<String> subjectArrayList, ArrayList<HashMap<String, Object>> mainArrayList, ArrayList<String> nameArrayList,String type) {
        super(fm);
        this.subjectArrayList = subjectArrayList;
        this.mainArrayList = mainArrayList;
        this.dateArrayList = dateArrayList;
        this.nameArrayList = nameArrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ArrayList<String> dateArrayListSubjectWise = new ArrayList<>();
        ArrayList<String> nameArrayListSubjectWise = new ArrayList<>();
        ArrayList<HashMap<String, Object>> mainArrayListSubjectWise = new ArrayList<>();
        HashMap<String, Object> mainArrayHashMap = new HashMap<>();
        for(String date: dateArrayList){
            String[] dateList = date.split("_");
            if(subjectArrayList.get(position).equals(dateList[0])){
                dateArrayListSubjectWise.add(dateList[1]);
            }
        }
        for(String name: nameArrayList){
            String[] nameList = name.split("_");
            if(subjectArrayList.get(position).equals(nameList[0])){
                nameArrayListSubjectWise.add(nameList[1]);
            }
        }
        for(HashMap<String, Object> data: mainArrayList){
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String[] dataList = entry.getKey().split("_");
                if(subjectArrayList.get(position).equals(dataList[0])||subjectArrayList.get(position).equals(dataList[0]+"_"+dataList[1])){
                    if (dataList[0].equals("english"))
                    {
                        mainArrayHashMap.put(dataList[2], entry.getValue());
                        mainArrayListSubjectWise.add(mainArrayHashMap);
                    }else {
                        mainArrayHashMap.put(dataList[1], entry.getValue());
                        mainArrayListSubjectWise.add(mainArrayHashMap);
                    }

                }
            }
        }
        if (type.contains("practice")) {
            return new PalReportsPracticeFragment(subjectArrayList.get(position), mainArrayListSubjectWise, dateArrayListSubjectWise, nameArrayListSubjectWise, type);
        } else if (type.contains("test")) {
            return new PalReportsTestFragment(subjectArrayList.get(position), mainArrayListSubjectWise, dateArrayListSubjectWise, nameArrayListSubjectWise, type);
        } else {
            return new PalReportsVideoFragment(subjectArrayList.get(position), mainArrayListSubjectWise, dateArrayListSubjectWise, nameArrayListSubjectWise, type);
        }
    }

    @Override
    public int getCount() {
        return subjectArrayList.size();
    }
}