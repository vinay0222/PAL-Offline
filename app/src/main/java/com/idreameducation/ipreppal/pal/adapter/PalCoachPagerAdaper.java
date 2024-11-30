package com.idreameducation.ipreppal.pal.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.idreameducation.ipreppal.pal.fragments.PalAssignedFragment;
import com.idreameducation.ipreppal.pal.fragments.PalChatFragment;

public class PalCoachPagerAdaper extends FragmentStatePagerAdapter {


    public PalCoachPagerAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PalChatFragment();
            case 1:
                return new PalAssignedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}