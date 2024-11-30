package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PalHomeActivity.hideNavigationBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.DiagonosticTestActivity_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONObject;

import java.util.HashMap;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class DStartActivity extends AppCompatActivity {
    private TextView buttonPreceed;
    private Context context;
    private String studentClass;
    private TextView textViewdTest;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;
    private Global global;
    private int topicPosition;
    String assignedDate,sClass,assignedKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_dstart);
        assignIds();
        listners();

    }

    private void listners() {
        findViewById(R.id.buttonPreceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);

                if(Util.isOfflineMode(context)) {
                    Intent intent = new Intent(context, DiagonosticTestActivity.class);
                    intent.putExtra("sClass", studentClass);
                    intent.putExtra("teacherID", teacherID);
                    intent.putExtra("date", datetostore);
                    intent.putExtra("key", keyTo);
                    intent.putExtra("batchId", batchId);
                    intent.putExtra("isAssigned", isAssigned);
                    intent.putExtra("topicPosition", topicPosition);

                    if(assignedDate!=null){
                        intent.putExtra("sClass", sClass);
                        intent.putExtra("assignedDate",assignedDate);
                        intent.putExtra("assignedKey",assignedKey);
                    }

                    startActivity(intent);

                    finish();

                }
                else {
                    if (Util.isNetworkAvailable(context))
                    {
                        Intent intent;

                        if(Util.isPortraitMode(context)) intent = new Intent(context, DiagonosticTestActivity_Mobile.class);
                        else intent = new Intent(context, DiagonosticTestActivity.class);

                        if(assignedDate!=null){
                            intent.putExtra("sClass", sClass);
                            intent.putExtra("assignedDate",assignedDate);
                            intent.putExtra("assignedKey",assignedKey);
                        }

                        intent.putExtra("sClass", studentClass);
                        intent.putExtra("teacherID", teacherID);
                        intent.putExtra("date", datetostore);
                        intent.putExtra("key", keyTo);
                        intent.putExtra("batchId", batchId);
                        intent.putExtra("isAssigned", isAssigned);
                        intent.putExtra("topicPosition", topicPosition);
                        startActivity(intent);

                        finish();

                    }else {
                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                        {
                            Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                        }else {
                            Util.openGifDialogue(context,"Internet Connection is not working");
                        }
                    }
                }
            }
        });
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        textViewdTest = findViewById(R.id.textViewdTest);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        buttonPreceed = findViewById(R.id.buttonPreceed);
        studentClass = getIntent().getStringExtra("sClass");
        isAssigned = getIntent().getStringExtra("isAssigned");
        batchId = getIntent().getStringExtra("batchId");
        keyTo = getIntent().getStringExtra("key");
        datetostore = getIntent().getStringExtra("date");
        teacherID = getIntent().getStringExtra("teacherID");
        topicPosition = getIntent().getIntExtra("topicPosition", 0);



        assignedDate = getIntent().getStringExtra("assignedDate");
        sClass = getIntent().getStringExtra("sClass");
        assignedKey = getIntent().getStringExtra("assignedKey");






        try {
            Util.setBackButton(context);
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideNavigationBar(getWindow());
    }

    private String isAssigned;
    private String batchId;
    private String keyTo;
    private String datetostore;
    private String teacherID;

    private void setStaticText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object_ = object.getJSONObject("Diagnostic_Test_Start_Screen");
                textViewdTest.setText((String) object_.get("diagnostic_test_title"));
                step1.setText((String) object_.get("point_1_text"));
                step2.setText((String) object_.get("point_2_text"));
                step3.setText((String) object_.get("point_3_text"));
                buttonPreceed.setText((String) object_.get("start_test_btn_text"));

                textViewdTest.setText((String) object_.get("diagnostic_test_title"));
                step2.setText((String) object_.get("point_2_text"));
                step1.setText((String) object_.get("point_1_text"));
                step3.setText((String) object_.get("point_3_text"));
                step4.setText((String) object_.get("point_4_text"));

                if(step4.getText().toString().trim().equals("")) step4.setVisibility(View.GONE);

                buttonPreceed.setText((String) object_.get("start_test_btn_text"));

                buttonPreceed.requestFocus();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.databaseReference.child("pal_test").child("labels").child(Util.getSelectedLanguage(context)).child("Diagnostic_Test_Start_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            String topicName = "<font color='#0077FF'>"+ Util.getTopicNameAlt(context) + "</font>";

                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            System.out.println("---------- dataHashMap "+dataHashMap);

                            if(Util.getSelectedLanguage(context).equals("hindi")) textViewdTest.setText(Html.fromHtml(topicName+" का "+dataHashMap.get("diagnostic_test_title")));
                            else textViewdTest.setText(Html.fromHtml(dataHashMap.get("diagnostic_test_title") +" of "+topicName));
                            step2.setText(dataHashMap.get("point_2_text"));
                            step1.setText(dataHashMap.get("point_1_text"));
                            step3.setText(dataHashMap.get("point_3_text"));
                            step4.setText(dataHashMap.get("point_4_text"));

                            if(step3.getText().toString().trim().equals("")) step3.setVisibility(View.GONE);
                            if(step4.getText().toString().trim().equals("")) step4.setVisibility(View.GONE);

                            buttonPreceed.setText(dataHashMap.get("start_test_btn_text"));

                            buttonPreceed.requestFocus();
                        }
                        else setDefaultText();
                    } catch (Exception e) {
                        setDefaultText();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    setDefaultText();
                }
            });
        }
    }

    private void setDefaultText() {
        String topicName = "<font color='#0077FF'>"+ Util.getTopicNameAlt(context) + "</font>";

        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            step1.setText(Html.fromHtml("प्रत्येक विषय के लिए, आप केवल एक बार ही डायग्नोस्टिक परीक्षा का प्रयास कर सकते हैं "));
            step2.setText(Html.fromHtml("आपके इस परिक्षण के प्रयास के आधार पर आपको आगे बढ़ने के लिए मार्गदर्शित किया जाएगा "));
            buttonPreceed.setText(Html.fromHtml("डायग्नोस्टिक परिक्षण शुरू करें"));
            textViewdTest.setText(Html.fromHtml("आइये समझें की आप " + topicName +" के बारे में कितना जानते हैं?"));
            step3.setText(Html.fromHtml("यदि आप इस परिक्षण में 80 प्रतिशत से ज़्यादा अंक लाएंगे तो आप "+ topicName +" के साथ\nअगले विषय को भी अनलॉक कर उसका डाइग्नोस्टिक परिक्षण कर सकते हैं "));
        }else {
            step1.setText(Html.fromHtml("For each chapter, you can attempt the diagnostic test only once."));
            step2.setText(Html.fromHtml("You will be guided on how to proceed based on your attempt at this test"));
            buttonPreceed.setText(Html.fromHtml("Start diagnostic test"));
            textViewdTest.setText(Html.fromHtml("Let's understand how much you know about " + topicName + "?"));
            step3.setText(Html.fromHtml("If you will get more than 80 percent marks in this test then you can do diagnostic test by unlocking \n next topic with "+topicName));
        }

        buttonPreceed.requestFocus();
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }



}