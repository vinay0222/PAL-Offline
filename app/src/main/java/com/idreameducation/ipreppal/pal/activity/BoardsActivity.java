package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalClassesActivity;
import com.idreameducation.ipreppal.pal.adapter.BoardsAdapter;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class BoardsActivity extends AppCompatActivity {


    private Global global;
    private Context context;
    private final String language = "hindi";
    private RecyclerView recyclerView;
    private TextView nextButton;
    private String board;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);
        assignIds();
        listners();
    }

    private void listners() {

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board != null) {
                    Util.preventTwoClick(v);
                    startActivity(new Intent(context, PalClassesActivity.class).putExtra("board", board));

                } else {
                    Util.showToast(context, "Plese select board");
                }
            }
        });
    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        nextButton = findViewById(R.id.nextButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        //hideNavigationBar(getWindow());
        try {
            getBoards();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getBoards() throws Exception {
        if (Util.isOfflineMode(context)) {

            //parseClassJson(language, board);

        } else {
            if (!Util.checkInternetConnection(context)) {
                Util.showInternetConnectioError(context);
                return;
            }
            final ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {

                            ArrayList<HashMap<String, String>> boardsArrayList = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                            for (int i = 0; i < boardsArrayList.size(); i++) {
                                boardsArrayList.get(i).put("selected", "false");
                            }
                            BoardsAdapter boardsAdapter = new BoardsAdapter(context, boardsArrayList);
                            recyclerView.setAdapter(boardsAdapter);
                            boardsAdapter.SetOnItemClickListener(new BoardsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    for (int i = 0; i < boardsArrayList.size(); i++) {
                                        boardsArrayList.get(i).put("selected", "false");
                                    }
                                    boardsArrayList.get(position).put("selected", "true");
                                    boardsAdapter.notifyDataSetChanged();
                                    board = boardsArrayList.get(position).get("abbr");

                                }
                            });
                            Log.i("Boards ArrayList : ", boardsArrayList.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.showInternetConnectioError(context);
                }
            };
            global.getDatabaseReference().child(ApplicationConstants.BOARDS).child(language).addValueEventListener(valueEventListener);
        }

    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }


}
