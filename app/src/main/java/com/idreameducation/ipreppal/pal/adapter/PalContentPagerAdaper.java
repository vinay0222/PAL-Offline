package com.idreameducation.ipreppal.pal.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idreameducation.ipreppal.pal.fragments.PalBooksFragment;
import com.idreameducation.ipreppal.pal.fragments.PalPracticeFrament;
import com.idreameducation.ipreppal.pal.fragments.PalTestFrament;
import com.idreameducation.ipreppal.pal.fragments.PalVideoListFragment;

public class PalContentPagerAdaper extends FragmentPagerAdapter {


    public PalContentPagerAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // return null to display only the icon
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PalVideoListFragment("video_lessons");
            case 1:
                return new PalPracticeFrament("practice");
            case 2:
                return new PalTestFrament("Test");
            case 3:
                return new PalBooksFragment("books_ncert");

            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 4;
    }
}