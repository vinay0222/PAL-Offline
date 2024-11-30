package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idreameducation.ipreppal.pal.activity.ReportsActivity;

public class PalReportPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public PalReportPagerAdapter(@NonNull FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            default:
                return new ReportsActivity(7, context);
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
}
