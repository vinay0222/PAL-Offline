package com.idreameducation.ipreppal.pal.activity;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.idreameducation.ipreppal.PalMobile.activity.IssueChatActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.SupportChatModel;
import com.idreameducation.ipreppal.model.SupportModel;
import com.idreameducation.ipreppal.pal.adapter.MyIssuesAdapter;
import com.idreameducation.ipreppal.pal.adapter.SupportChatAdapter;
import com.idreameducation.ipreppal.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyIssuesActivity extends AppCompatActivity {

    private String TAG="MyIssuesActivity";
    Context context;
    Global global;
    RecyclerView issuesRecyclerview,chatRecyclerview;
    LinearLayout noIssueLayout;
    EditText messageEditText;
    ImageView backBtn,backBtn2,sendBtn;
    TextView issueNameText;
    MyIssuesAdapter myIssuesAdapter;
    ArrayList<SupportModel> supportModels;

    int oldPosition=0;
    boolean haveChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_my_issues);
        initViews();

    }
    private void initViews() {

        /** init variables */
        context=this;
        global= (Global) getApplicationContext();

        /** init views */
        issuesRecyclerview=findViewById(R.id.issuesRecyclerview);
        chatRecyclerview=findViewById(R.id.chatRecyclerview);
        messageEditText=findViewById(R.id.messageEditText);
        issueNameText=findViewById(R.id.issueNameText);
        noIssueLayout=findViewById(R.id.noIssueLayout);
        sendBtn=findViewById(R.id.sendBtn);
        backBtn=findViewById(R.id.backBtn);
        backBtn2=findViewById(R.id.backBtn2);

        /** setup recyclerview */
        issuesRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        chatRecyclerview.setLayoutManager(new LinearLayoutManager(context));

        getIssues();
        clickListners();
    }
    private void clickListners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haveChat) addMessageInChat(supportModels.get(oldPosition));
                else startNewChat(supportModels.get(oldPosition));
            }
        });
    }
    private void getIssues() {

        /** init firestore ref */
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        /** Create a reference to the support collection */
        CollectionReference citiesRef = db.collection("support");

        /** Create a query against the collection. */
        Query query = citiesRef.whereEqualTo("user_id", Util.getUserId(context))
                .orderBy("timestamp", ASCENDING);

        /** init list & adapter */
        supportModels=new ArrayList<>();

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    supportModels.clear();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        SupportModel model = d.toObject(SupportModel.class);
                        model.setKey(d.getId());
                        model.setSelected(false);
                        supportModels.add(model);
                    }
                    setSelectedIssue(0);

                    if(supportModels.size()==0) noIssueLayout.setVisibility(View.VISIBLE);
                    else noIssueLayout.setVisibility(View.GONE);

                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noIssueLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

    }
    public void setSelectedIssue(int position) {
        if(supportModels.size()<=position) return;

        SupportModel model=supportModels.get(oldPosition);
        model.setSelected(false);
        supportModels.set(oldPosition,model);

        SupportModel model2=supportModels.get(position);
        model2.setSelected(true);
        supportModels.set(position,model2);
        
        myIssuesAdapter=new MyIssuesAdapter(supportModels);
        issuesRecyclerview.setAdapter(myIssuesAdapter);
        oldPosition=position;
        issueNameText.setText(position+1+"");

        getMessages(model2);
    }

    public void openChatInNewActivity(int position) {
        if(supportModels.size()<=position) return;

        SupportModel model=supportModels.get(oldPosition);
        model.setSelected(false);
        supportModels.set(oldPosition,model);
//
        SupportModel model2=supportModels.get(position);
//        model2.setSelected(true);
//        supportModels.set(position,model2);

        oldPosition=position;
        issueNameText.setText(position+1+"");

        IssueChatActivity.model=model2;
        startActivity(new Intent(context, IssueChatActivity.class));


    }

    private void setdefaultChat(SupportModel model2) {

        ArrayList<SupportChatModel> chatModels=new ArrayList<>();
        SupportChatModel chatModel=new SupportChatModel();
        chatModel.setFrom(Util.getUserId(context));
        chatModel.setInputType("Text");
        chatModel.setMessage(model2.getText_by_users());
//        chatModel.setMessage("There is an error in this section. When I click on the subject, it doesn’t open and the app crashes. ");
        chatModel.setMessageType("Showable");
        chatModel.setTime(model2.getDate_of_reported_issue());
        chatModel.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel.setType("Sender");
        chatModel.setUserType("Student");
        chatModel.setImage(model2.getImage_by_users());

        chatModels.add(chatModel);

        SupportChatModel reciverchatModel=new SupportChatModel();
        reciverchatModel.setFrom("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        reciverchatModel.setInputType("Text");
        reciverchatModel.setMessage(model2.getText_by_users());
        if(model2.getRemark()!=null) reciverchatModel.setMessage(model2.getRemark());
        else reciverchatModel.setMessage("Hello, \n" +
                "\n" +
                "Thank you for reporting this issue. We have fixed it and the application should work fine now. \n" +
                "\n" +
                "Thanks, \n" +
                "Jay");
        reciverchatModel.setMessageType("Showable");
        reciverchatModel.setTime(model2.getDate_of_reported_issue());
        reciverchatModel.setTo(Util.getUserId(context));
        reciverchatModel.setType("Reciever");
        reciverchatModel.setUserType("Teacher");

        chatModels.add(reciverchatModel);

        SupportChatAdapter myIssuesAdapter=new SupportChatAdapter(chatModels);
        chatRecyclerview.setAdapter(myIssuesAdapter);

    }
    private void startNewChat(SupportModel model2) {
        String userid=Util.getUserId(context);
        String message=messageEditText.getText().toString();
        if(message.trim().equals("")) {
            Toast.makeText(context, "message is empty", Toast.LENGTH_SHORT).show();
            return;
        }

//        SupportModel model2=supportModels.get(oldPosition);

        SupportChatModel chatModel=new SupportChatModel();
        chatModel.setFrom(Util.getUserId(context));
        chatModel.setInputType("Text");
        chatModel.setMessage(model2.getText_by_users());
//        chatModel.setMessage("There is an error in this section. When I click on the subject, it doesn’t open and the app crashes. ");
        chatModel.setMessageType("Showable");
        chatModel.setTime(model2.getDate_of_reported_issue());
        chatModel.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel.setType("Sender");
        chatModel.setUserType("Student");
        chatModel.setImage(model2.getImage_by_users());

        SupportChatModel reciverchatModel=new SupportChatModel();
        reciverchatModel.setFrom("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        reciverchatModel.setInputType("Text");
        reciverchatModel.setMessage(model2.getText_by_users());
        if(model2.getRemark()!=null) reciverchatModel.setMessage(model2.getRemark());
        else reciverchatModel.setMessage("Hello, \n" +
                "\n" +
                "Thank you for reporting this issue. We have fixed it and the application should work fine now. \n" +
                "\n" +
                "Thanks, \n" +
                "Jay");
        reciverchatModel.setMessageType("Showable");
        reciverchatModel.setTime(model2.getDate_of_reported_issue());
        reciverchatModel.setTo(Util.getUserId(context));
        reciverchatModel.setType("Reciever");
        reciverchatModel.setUserType("Teacher");

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String dateString = formatter.format(new Date(System.currentTimeMillis()));


        SupportChatModel chatModel2=new SupportChatModel();
        chatModel2.setFrom(Util.getUserId(context));
        chatModel2.setInputType("Text");
        chatModel2.setMessage(message);
        chatModel2.setMessageType("Showable");
        chatModel2.setTime(dateString);
        chatModel2.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel2.setType("Sender");
        chatModel2.setUserType("Student");

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model2.getKey())
                .push().setValue(chatModel);

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model2.getKey())
                .push().setValue(reciverchatModel);

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model2.getKey())
                .push().setValue(chatModel2);

        messageEditText.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    private void addMessageInChat(SupportModel model2) {
        String userid=Util.getUserId(context);
        String message=messageEditText.getText().toString();
        if(message.trim().equals("")) {
            Toast.makeText(context, "message is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String dateString = formatter.format(new Date(System.currentTimeMillis()));

        SupportChatModel chatModel=new SupportChatModel();
        chatModel.setFrom(Util.getUserId(context));
        chatModel.setInputType("Text");
        chatModel.setMessage(message);
        chatModel.setMessageType("Showable");
        chatModel.setTime(dateString);
        chatModel.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel.setType("Sender");
        chatModel.setUserType("Student");

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model2.getKey())
                .push().setValue(chatModel);

        messageEditText.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }
    private void getMessages(SupportModel model) {
        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(Util.getUserId(context))
                .child(model.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                haveChat=false;
                ArrayList<SupportChatModel> chatArrayList = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot single : dataSnapshot.getChildren()) {
                        SupportChatModel chatModel=single.getValue(SupportChatModel.class);
                        HashMap<String, String> chatHashMap = (HashMap<String, String>) single.getValue();
                        try {
                            if (chatHashMap.get("messageType").equalsIgnoreCase("Showable")) {
                                chatArrayList.add(chatModel);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    haveChat=true;
                    SupportChatAdapter chatAdapter = new SupportChatAdapter(chatArrayList);
                    chatRecyclerview.setAdapter(chatAdapter);
                    chatRecyclerview.scrollToPosition(chatArrayList.size() - 1);

                }
                else setdefaultChat(model);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}