package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AppWorksModel;
import com.idreameducation.ipreppal.pal.adapter.RecyclerAppWorksAdapter;

import java.util.ArrayList;

public class HowIPrepWorksActivity extends AppCompatActivity {

    private Context context;
    private Global global;
    private ImageView back;
    private RecyclerView recyclerView;
    private ArrayList<AppWorksModel> recyclerDataArrayList;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iprep_how_it_works);
        assignIds();
        listners();
    }

    private void listners() {

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        back = findViewById(R.id.imageViewBack);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerDataArrayList = new ArrayList<>();
        recyclerDataArrayList.add(new AppWorksModel("वीडियो सबक", "iPrep में आपके पाठ्यक्रम के अनुसार विभिन्न विषयों पर मनोरंजक एनिमेटेड वीडियो पाठ हैं। नई अवधारणाओं को सीखने या उन विषयों की समझ को गहरा करने के लिए जिन्हें आप पहले ही सीख चुके हैं, वीडियो पाठों पर क्लिक करें और खुशी से सीखें। प्रत्येक वीडियो वास्तविक जीवन की स्थिति से जुड़ा है। इसलिए आप जो सीखेंगे वह केवल अवधारणा नहीं है बल्कि वास्तविक जीवन स्थितियों में इसे कैसे लागू किया जाए।", R.mipmap.app_works_video));
        recyclerDataArrayList.add(new AppWorksModel("पुस्तकें", "", R.mipmap.app_works_books));
        recyclerDataArrayList.add(new AppWorksModel("अभ्यास", "", R.mipmap.app_works_practice));
        recyclerDataArrayList.add(new AppWorksModel("प्रभुत्व", "", R.mipmap.app_works_mastery));
        recyclerDataArrayList.add(new AppWorksModel("परीक्षण", "", R.mipmap.app_works_tests));
        recyclerDataArrayList.add(new AppWorksModel("मेरी रिपोर्ट", "", R.mipmap.app_works_reports));
        recyclerDataArrayList.add(new AppWorksModel("प्रोफ़ाइल प्रबंधित करें", "", R.mipmap.app_works_profile));
        recyclerDataArrayList.add(new AppWorksModel("शेयर करें और कमाएं", "", R.mipmap.app_works_share));

        RecyclerAppWorksAdapter recyclerAppWorksAdapter = new RecyclerAppWorksAdapter(recyclerDataArrayList, context);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAppWorksAdapter);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }
}
