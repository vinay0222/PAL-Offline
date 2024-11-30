package com.idreameducation.ipreppal.PalMobile.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.PalCoachPagerAdaper;
import com.idreameducation.ipreppal.util.Util;

import java.util.HashMap;

public class BatchActivity extends AppCompatActivity {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Global global;

    TextView textViewClassID,textViewBatchName;

    String classID;
    public static HashMap<String, String> contentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.setWindowSettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        init();
    }

    private void init() {
        context=this;
        global = (Global) getApplicationContext();


        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        textViewClassID = findViewById(R.id.textViewClassID);
        textViewBatchName = findViewById(R.id.textViewBatchName);

        classID = getIntent().getStringExtra("classID");
        String[] sc = classID.split("_");
        String sclass= sc[0];

        textViewClassID.setText(sclass);

        String batchName = getIntent().getStringExtra("batchName");
        textViewBatchName.setText(batchName);

        setPagerAdapter();
        setTabLayout();
    }



    private void setPagerAdapter() {
        PalCoachPagerAdaper contentPagerAdaper = new PalCoachPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(contentPagerAdaper);
        //viewPager.setOffscreenPageLimit(3);
    }

    private void setTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        // this needs to be dynamic
        tabLayout.getTabAt(0).setText("Messages");
        tabLayout.getTabAt(1).setText("Assigned");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0077FF"));
    }

}