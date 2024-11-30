package com.idreameducation.ipreppal.PalMobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.SupportChatModel;
import com.idreameducation.ipreppal.model.SupportModel;
import com.idreameducation.ipreppal.pal.adapter.SupportChatAdapter;
import com.idreameducation.ipreppal.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class IssueChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerview;
    private String facilitatorId;
    private Context context;
    private Global global;
    private TextView textViewUserName;
    private EditText messageEditText;
    private ImageView sendButton;
    private String message;
    private ImageView attachmentBtn;
    boolean haveChat;
    StorageReference mStorageRef;
    String messageType;

    TextView textIssueId;

    public static SupportModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_issue_chat);

        if(model==null) {
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        assignids();
    }

    private void assignids() {
        context = this;
        global = (Global) getApplicationContext();

        messageEditText = findViewById(R.id.messageEditText);
        textViewUserName = findViewById(R.id.textViewUserName);
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        sendButton = findViewById(R.id.sendButton);
        messageEditText.setScroller(new Scroller(context));
        messageEditText.setMaxLines(1);
        messageEditText.setVerticalScrollBarEnabled(true);
        messageEditText.setMovementMethod(new ScrollingMovementMethod());
        chatRecyclerview = findViewById(R.id.recyclerView);
        textIssueId = findViewById(R.id.textIssueId);
        attachmentBtn = findViewById(R.id.attachmentBtn);

        textIssueId.setText("Issue Id - "+model.getTimestamp().toString());
        mStorageRef= FirebaseStorage.getInstance().getReference();

        chatRecyclerview.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);

        chatRecyclerview.setLayoutManager(mLayoutManager);
        setStaticText();
        getMessages();
        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haveChat) addMessageInChat();
                else startNewChat();
            }
        });
        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setStaticText() {
        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Chat Screen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot!=null){
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        messageEditText.setHint(textArrayList.get(0));
                        message = textArrayList.get(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    private void getMessages() {
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
                        else setdefaultChat();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setdefaultChat() {

        ArrayList<SupportChatModel> chatModels=new ArrayList<>();
        SupportChatModel chatModel=new SupportChatModel();
        chatModel.setFrom(Util.getUserId(context));
        chatModel.setInputType("Text");
        chatModel.setMessage(model.getText_by_users());
//        chatModel.setMessage("There is an error in this section. When I click on the subject, it doesn’t open and the app crashes. ");
        chatModel.setMessageType("Showable");
        chatModel.setTime(model.getDate_of_reported_issue());
        chatModel.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel.setType("Sender");
        chatModel.setUserType("Student");
        chatModel.setImage(model.getImage_by_users());

        chatModels.add(chatModel);

        SupportChatModel reciverchatModel=new SupportChatModel();
        reciverchatModel.setFrom("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        reciverchatModel.setInputType("Text");
        reciverchatModel.setMessage(model.getText_by_users());
        if(model.getRemark()!=null) reciverchatModel.setMessage(model.getRemark());
        else reciverchatModel.setMessage("Hello, \n" +
                "\n" +
                "Thank you for reporting this issue. We have fixed it and the application should work fine now. \n" +
                "\n" +
                "Thanks, \n" +
                "Jay");
        reciverchatModel.setMessageType("Showable");
        reciverchatModel.setTime(model.getDate_of_reported_issue());
        reciverchatModel.setTo(Util.getUserId(context));
        reciverchatModel.setType("Reciever");
        reciverchatModel.setUserType("Teacher");

        chatModels.add(reciverchatModel);

        SupportChatAdapter myIssuesAdapter=new SupportChatAdapter(chatModels);
        chatRecyclerview.setAdapter(myIssuesAdapter);

    }

    private void startNewChat() {
        String userid=Util.getUserId(context);
        String message=messageEditText.getText().toString();
        if(message.trim().equals("")) {
            Toast.makeText(context, "message is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SupportChatModel chatModel=new SupportChatModel();
        chatModel.setFrom(Util.getUserId(context));
        chatModel.setInputType("Text");
        chatModel.setMessage(model.getText_by_users());
        chatModel.setMessageType("Showable");
        chatModel.setTime(model.getDate_of_reported_issue());
        chatModel.setTo("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        chatModel.setType("Sender");
        chatModel.setUserType("Student");
        chatModel.setImage(model.getImage_by_users());

        SupportChatModel reciverchatModel=new SupportChatModel();
        reciverchatModel.setFrom("rfih8QQqnwe2ab8XhtaY0eAw5ZA3");
        reciverchatModel.setInputType("Text");
        reciverchatModel.setMessage(model.getText_by_users());
        if(model.getRemark()!=null) reciverchatModel.setMessage(model.getRemark());
        else reciverchatModel.setMessage("Hello, \n" +
                "\n" +
                "Thank you for reporting this issue. We have fixed it and the application should work fine now. \n" +
                "\n" +
                "Thanks, \n" +
                "Jay");
        reciverchatModel.setMessageType("Showable");
        reciverchatModel.setTime(model.getDate_of_reported_issue());
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
                .child(model.getKey())
                .push().setValue(chatModel);

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model.getKey())
                .push().setValue(reciverchatModel);

        global.getDatabaseReference().child("chatbot")
                .child(Util.getNGOID(context))
                .child(userid)
                .child(model.getKey())
                .push().setValue(chatModel2);

        messageEditText.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void addMessageInChat() {
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
                .child(model.getKey())
                .push().setValue(chatModel);

        messageEditText.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }

}