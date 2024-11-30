package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.util.Util;

public class DTestCompleteActivity extends AppCompatActivity{
    private TextView buttonPreceed;
    private Context context;
    private String icon;
    private String studentClass;
    private String board;
    private String subject;
    private TextView textViewdExplore;
    private TextView textViewMessage;
    private TextView textViewdUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_d_complete);
        assignIds();
        listners();
    }

    private void listners() {
        findViewById(R.id.buttonPreceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context) || Util.isOfflineMode(context))
                {
                    Intent intent = new Intent(context, DiagonosticTestActivity.class);
                    intent.putExtra("sClass", studentClass);
                    startActivity(intent);

                    finish();

                }
                else {
                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                    {
                        Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                    }else {
                        Util.openGifDialogue(context,"Internet Connection is not working");
                    }
                }
            }
        });

    }

    private void assignIds() {
        context = this;
        textViewMessage = findViewById(R.id.textViewMessage);
        textViewdExplore = findViewById(R.id.textViewdExplore);
        textViewdUserName = findViewById(R.id.textViewdUserName);
        textViewdExplore.setText("Explore " + Util.getTopicNameAlt(context));
        textViewMessage.setText("You have Unlocked " + Util.getTopicNameAlt(context) + ", \nand Unlocked Diagnostic Test for " + Util.getTopicNameAlt(context));
        textViewdUserName.setText(Util.getUsername(context));
        buttonPreceed = findViewById(R.id.buttonPreceed);
        studentClass = getIntent().getStringExtra("sClass");
    }

}
