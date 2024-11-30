package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppIDFetcherActivity extends AppCompatActivity {


    private RelativeLayout reletiveLayout;
    private TextView textViewAppID;
    private Context context;
    private Button buttonRefres;
    private Button buttonGetAppID;
    private EditText editTextTabID;
    private Global global;
    private String appID;
    private String ngoID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appid_fetcher);
        assignIds();
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        editTextTabID = findViewById(R.id.editTextTabID);
        textViewAppID = findViewById(R.id.textViewAppID);
        reletiveLayout = findViewById(R.id.reletiveLayout);
        buttonRefres = findViewById(R.id.buttonRefres);
        buttonRefres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    refreshAppID(ngoID, appID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonGetAppID = findViewById(R.id.buttonGetAppID);

        buttonGetAppID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String tabID = editTextTabID.getText().toString().trim();
                    getAppID(tabID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getAppID(String tabID) {
        global.getDatabaseReference().child("app_tab_relation").child(tabID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        reletiveLayout.setVisibility(View.VISIBLE);
                        HashMap<String, String> hashMap = (HashMap<String, String>) snapshot.getValue();
                        appID = (String) hashMap.get("appID");
                        textViewAppID.setText("APP ID :" + appID);
                        ngoID = (String) hashMap.get("ngoID");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void refreshAppID(String NGOID, String AppID) {
        global.getDatabaseReference().child("app_ngo_relation").child(NGOID).child(AppID).child("isRegistered").removeValue();

    }


}
