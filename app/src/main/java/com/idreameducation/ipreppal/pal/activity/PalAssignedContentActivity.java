package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.util.HashMap;

public class PalAssignedContentActivity extends AppCompatActivity {
    private Global global;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.pal_activity_assigned_content);
        assignIds();
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        getAssignedContent();
    }

    private void getAssignedContent() {
        global.getDatabaseReference().child("content_assignement_student_wise").child(Util.getUserId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, Object> assignedHashMap = (HashMap<String, Object>) snapshot.getValue();
                        Util.showToast(context, assignedHashMap.toString());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
