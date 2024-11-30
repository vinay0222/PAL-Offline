package com.idreameducation.ipreppal.pal.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idreameducation.ipreppal.pal.fragments.PalBooksListingFragment;
import com.idreameducation.ipreppal.pal.fragments.PalExtraContentListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class PalExtraContentPagerAdaper extends FragmentPagerAdapter {

    private final String type;
    private final String name;
    private final ArrayList<HashMap<String , String>> contentArrayList;
    private final int pos;
    public PalExtraContentPagerAdaper(FragmentManager fm, String type, ArrayList<HashMap<String , String>> contentArrayList, String name,int position) {
        super(fm);
        this.type = type;
        this.contentArrayList= contentArrayList;
        this.name = name;
        this.pos = position;
    }

    @Override
    public Fragment getItem(int position) {
        if (type.equals("books"))
        {
            return new PalBooksListingFragment(type, name,pos);
        }else {
            return new PalExtraContentListFragment(type, contentArrayList, name);
        }

    }

    @Override
    public int getCount() {
        return 1;
    }
}