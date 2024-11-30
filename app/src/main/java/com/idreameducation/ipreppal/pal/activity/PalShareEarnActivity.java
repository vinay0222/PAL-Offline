package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONObject;

import java.util.HashMap;

public class PalShareEarnActivity extends AppCompatActivity {
    private Context context;
    private Global global;
    private TextView title, earn_number, invite_number, step1, step2, step3, step4;
    private Button btn_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_share_earn_screen);
        assignIds();
        try {
            setStaticText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        title = findViewById(R.id.title);
        earn_number = findViewById(R.id.earn_number);
        invite_number = findViewById(R.id.invite_number);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        btn_ok = findViewById(R.id.btn_ok);
        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStaticText() throws Exception {
        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/labels.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
//                Util.getSelectedLanguage(context);
                JSONObject object = jsonObject.getJSONObject("english");
                JSONObject object_ = object.getJSONObject("Share_and_Earn_Screen");
                title.setText((String) object_.get("share_earn_title"));
                earn_number.setText((String) object_.get("earned_text"));
                invite_number.setText((String) object_.get("friends_invite_text"));
                step1.setText((String) object_.get("steps_title"));
                step2.setText((String) object_.get("step_1_text"));
                step3.setText((String) object_.get("step_2_text"));
                step4.setText((String) object_.get("step_3_text"));
                btn_ok.setText((String) object_.get("share_btn_text"));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            global.databaseReference.child("pal_test").child("labels").child("english").child("Share_and_Earn_Screen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            HashMap<String, String> dataHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                            title.setText(dataHashMap.get("share_earn_title"));
                            earn_number.setText(dataHashMap.get("earned_text"));
                            invite_number.setText(dataHashMap.get("friends_invite_text"));
                            step1.setText(dataHashMap.get("steps_title"));
                            step2.setText(dataHashMap.get("step_1_text"));
                            step3.setText(dataHashMap.get("step_2_text"));
                            step4.setText(dataHashMap.get("step_3_text"));
                            btn_ok.setText(dataHashMap.get("share_btn_text"));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
