package com.idreameducation.ipreppal.pal.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.adapter.ChatAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ChatModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by apple on 21/02/18.
 */

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private String facilitatorId;
    private Context context;
    private Global global;
    private TextView textViewUserName;
    private EditText messageEditText;
    private ImageView sendButton;
    private String message;
    private ImageView attachmentBtn;

    StorageReference mStorageRef;
    String messageType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        assignids();
        listners();
    }

    private void listners() {
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Util.showToast(context, message);
                } else {
                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(message);
                    chatModel.setUserType("Student");
                    chatModel.setMessageType("Showable");
                    chatModel.setTo(facilitatorId);
                    chatModel.setFrom(Util.getUserId(context));
                    chatModel.setTime(Util.getCurrentDateWithDifferentFormat() + "-" + Util.getTimeAmPm());
                    sendMessages(chatModel);
                }
            }
        });
    }

    private void assignids() {
        context = this;
        global = (Global) getApplicationContext();
        facilitatorId = getIntent().getStringExtra("FacilitatorId");
        global.sendData("Chat", this.getClass().getName());
        messageEditText = findViewById(R.id.messageEditText);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserName.setText(global.getUserNameForChat());
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        sendButton = findViewById(R.id.sendButton);
        messageEditText.setScroller(new Scroller(context));
        messageEditText.setMaxLines(1);
        messageEditText.setVerticalScrollBarEnabled(true);
        messageEditText.setMovementMethod(new ScrollingMovementMethod());
        mRecyclerView = findViewById(R.id.recyclerView);
        attachmentBtn = findViewById(R.id.attachmentBtn);

        mStorageRef=FirebaseStorage.getInstance().getReference();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setStaticText();

        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });


        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMessages();

    }

    private void getMessages() {
        global.getDatabaseReference().child(ApplicationConstants.CHAT).child(Util.getUserId(context)).child(facilitatorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> chatArrayList = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot single : dataSnapshot.getChildren()) {
                        HashMap<String, String> chatHashMap = (HashMap<String, String>) single.getValue();
                        try {
                            if (chatHashMap.get("messageType").equalsIgnoreCase("Showable")) {
                                chatArrayList.add(chatHashMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    ArrayList<HashMap<String, String>> filteredArrayList = new ArrayList<>();
                    String time = null;
                    for (int i = 0; i < chatArrayList.size(); i++) {
                        HashMap<String, String> timeMap = chatArrayList.get(i);
                        if (time != null) {
                            if (!time.equalsIgnoreCase(timeMap.get("time").split("-")[0])) {
                                time = timeMap.get("time").split("-")[0];
                                timeMap.put("date", time);
                            } else {
                                timeMap.put("date", "hide");
                            }
                            filteredArrayList.add(timeMap);

                        } else {
                            time = timeMap.get("time").split("-")[0];
                            timeMap.put("date", time);
                            filteredArrayList.add(timeMap);
                        }


                    }

                    ChatAdapter chatAdapter = new ChatAdapter(context, filteredArrayList);
                    mRecyclerView.setAdapter(chatAdapter);
                    mRecyclerView.scrollToPosition(chatArrayList.size() - 1);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void sendMessages(ChatModel chatModel) {
        chatModel.setType("Sender");
        chatModel.setInputType("Text");
        global.getDatabaseReference().child(ApplicationConstants.CHAT).child(Util.getUserId(context)).child(facilitatorId).push().setValue(chatModel);
        chatModel.setType("Reciever");
        global.getDatabaseReference().child(ApplicationConstants.CHAT).child(facilitatorId).child(Util.getUserId(context)).push().setValue(chatModel);
        messageEditText.setText("");
        global.getDatabaseReference().child("notifications").child("messages").push().setValue(chatModel);

  /* insert data in the target user's database*/
        Map<String, String> notificationMap = new HashMap<>();
        notificationMap.put("message", chatModel.getMessage());
        notificationMap.put("notificationMessage", Util.getUsername(context));
        notificationMap.put("replyable", chatModel.getMessageType());
        notificationMap.put("sender", Util.getUsername(context));
        notificationMap.put("messageTime", Util.getCurrentDateWithDifferentFormat());
        notificationMap.put("senderId", Util.getUserId(context));
        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("Coach").child(facilitatorId).push().setValue(notificationMap);
  /* insert data in the own's database*/
        notificationMap.put("notificationMessage", global.getUserNameForChat());
        notificationMap.put("sender", global.getUserNameForChat());
        notificationMap.put("senderId", facilitatorId);
        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("Student").child(Util.getUserId(context)).push().setValue(notificationMap);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    private void selectPDF(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("application/pdf");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, 101);
    }

    private void selectVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(filePath);
                StorageReference storageReference = mStorageRef.child("images/" + UUID.randomUUID().toString());
                storageReference.putStream(inputStream)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();
                                String downloadUri = String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl());

//                                String link = storageReference;
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                messageType = "Image";
                                messageEditText.setText(downloadUri);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(context, "Upload Failed. Please try again!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else  if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            final String timestamp = "" + System.currentTimeMillis();
            final String messagePushID = timestamp;
            final StorageReference filepath = mStorageRef.child(messagePushID + "." + "pdf");
            Uri imageuri = data.getData();
            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();
                        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        messageEditText.setText(myurl);
                        messageType = "pdf";
                    } else {
                        Toast.makeText(context, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else if (requestCode == 102 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri videouri;
            videouri = data.getData();
            if (videouri != null) {
                // save the selected video in Firebase storage
                final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(videouri));
                reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUri = String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl());

//                        String downloadUri = taskSnapshot.getDownloadUrl().toString();
                        messageType = "video";
                        messageEditText.setText(downloadUri);
                        Toast.makeText(context, "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error, Image not uploaded
                        Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // show the progress bar
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        Toast.makeText(context,progress+"",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private String getfiletype(Uri videouri) {
        ContentResolver r = context.getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    //*****     Setting the text on labels          *****//
    public void setStaticText1() {
        try {
            String json = Util.getLanguageJson(context);
            try {
                JSONObject boardJsonObject = new JSONObject(json);
                JSONArray singleJsonObject = boardJsonObject.getJSONArray("Chat Screen");
                messageEditText.setHint(singleJsonObject.getString(0));
                message = singleJsonObject.getString(1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //*****                   END                    *****//
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
    @Override
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }

}
