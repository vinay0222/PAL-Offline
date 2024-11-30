package com.idreameducation.ipreppal.pal.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalClassesActivity;
import com.idreameducation.ipreppal.pal.adapter.ChatAdapter;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.ChatModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by apple on 21/02/18.
 */

public class PalChatFragment extends Fragment {
    private static RecyclerView mRecyclerView;
    private static String facilitatorId;
    private String facilitatorName;
    private static Context context;
    private static Global global;
    private TextView textViewUserName;
    private EditText messageEditText;
    private ImageView sendButton;
    private String messageText;
    private static TextView noChatText;
    private ImageView attachmentBtn;

    StorageReference mStorageRef;
    String messageType="Text";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        assignids(view);
        listners();
        return view;
    }

    private void listners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Util.showToast(context, messageText);
                } else {
                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(message);
                    chatModel.setUserType("Student");
                    chatModel.setMessageType("Showable");
                    chatModel.setTo(facilitatorId);
                    chatModel.setFrom(Util.getUserId(context));
                    chatModel.setStName(Util.getUsernameShowable(context));
                    chatModel.setTime(Util.getCurrentDateWithDifferentFormat() + "-" + Util.getTimeAmPm());
                    sendMessages(chatModel);
                }
            }
        });
    }

    private void assignids(View view) {
        context = getActivity();
        global = (Global)getActivity(). getApplicationContext();
        facilitatorId = global.getChatID();
        facilitatorName = global.getChatName();

        global.sendData("Chat", this.getClass().getName());
        messageEditText = view.findViewById(R.id.messageEditText);
        textViewUserName = view.findViewById(R.id.textViewUserName);
        noChatText = view.findViewById(R.id.noChatText);
        textViewUserName.setText(facilitatorName);
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        sendButton = view.findViewById(R.id.sendButton);
        messageEditText.setScroller(new Scroller(context));
        messageEditText.setMaxLines(1);
        messageEditText.setVerticalScrollBarEnabled(true);
        messageEditText.setMovementMethod(new ScrollingMovementMethod());
        mRecyclerView = view.findViewById(R.id.recyclerView);
        attachmentBtn = view.findViewById(R.id.attachmentBtn);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mStorageRef=FirebaseStorage.getInstance().getReference();

        if(Util.isPortraitMode(context)) attachmentBtn.setVisibility(View.GONE);

        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.image_attachment){
                            selectImage();
                        }
                        else if(item.getItemId() == R.id.video_attachment){
                            selectVideo();
                        }
                        else if(item.getItemId() == R.id.pdf_attachment){
                            selectPDF();
                        }

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.attachment_menu);
                popupMenu.show();
            }
        });

        setStaticText();
        try {
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMessages();
    }

    public static void getMessages() {
        try {
            global.getDatabaseReference().child(ApplicationConstants.CHAT).child(Util.getUserId(context)).child(facilitatorId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<HashMap<String, String>> chatArrayList = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot single : dataSnapshot.getChildren()) {
                            HashMap<String, String> chatHashMap = (HashMap<String, String>) single.getValue();
                            try {
                                chatArrayList.add(chatHashMap);
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
                        noChatText.setVisibility(View.GONE);
                    }
                    else noChatText.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessages(ChatModel chatModel) {
        chatModel.setType("Sender");
        chatModel.setInputType(messageType);
        global.getDatabaseReference().child(ApplicationConstants.CHAT).child(Util.getUserId(context)).child(facilitatorId).push().setValue(chatModel);
        chatModel.setType("Reciever");
        global.getDatabaseReference().child(ApplicationConstants.CHAT).child(facilitatorId).child(Util.getUserId(context)).push().setValue(chatModel);
        messageEditText.setText("");
        global.getDatabaseReference().child("notifications").child("messages").push().setValue(chatModel);

        /* insert data in the target user's database*/
        Map<String, String> notificationMap = new HashMap<>();
        notificationMap.put("message", chatModel.getMessage());
        notificationMap.put("userName", Util.getUsernameShowable(context));
        notificationMap.put("userId", Util.getUserId(context));
        notificationMap.put("batchName", global.getBatchName());
        notificationMap.put("date", setNotificationDateTime());
        notificationMap.put("inputType", messageType);
        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("teacher").child(facilitatorId).child(Util.getUserId(context)).setValue(notificationMap);
        /* insert data in the own's database*/
        notificationMap.put("message", chatModel.getMessage());
        notificationMap.put("userName", facilitatorName);
        notificationMap.put("userId", facilitatorId);
        notificationMap.put("batchName", global.getBatchName());
        notificationMap.put("date", setNotificationDateTime());
        notificationMap.put("inputType", messageType);

        global.getDatabaseReference().child(ApplicationConstants.NOTIFICATION).child("student").child(Util.getUserId(context)).child(facilitatorId).setValue(notificationMap);

        sendNotificationtobatch(context,Util.getUsernameShowable(context),chatModel.getMessage());

        messageType="Text";

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.attachment_menu, menu);
//        return true;
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            sendButton.setEnabled(false);
            messageEditText.setText("Uploading image ....");
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(filePath);
                StorageReference storageReference = mStorageRef.child("images/" + UUID.randomUUID().toString());
                storageReference.putStream(inputStream)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                                Uri downloadUri = taskSnapshot.getDownloadUrl();

                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUrl = uri.toString();
                                        // complete the rest of your code
                                        Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();

                                        messageType = "Image";
                                        messageEditText.setEnabled(false);
                                        sendButton.setEnabled(true);
                                        messageEditText.setText(downloadUrl);

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                sendButton.setEnabled(true);
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
        }
        else if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            final String timestamp = "" + System.currentTimeMillis();
            final String messagePushID = timestamp;
            final StorageReference filepath = mStorageRef.child(messagePushID + "." + "pdf");
            Uri imageuri = data.getData();
            sendButton.setEnabled(false);
            messageEditText.setText("Uploading Pdf ....");
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
                    sendButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();
                        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        messageEditText.setText(myurl);
                        messageType = "pdf";
                        messageEditText.setEnabled(false);
                    } else {
                        Toast.makeText(context, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else if (requestCode == 102 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri videouri;
            videouri = data.getData();
            sendButton.setEnabled(false);
            messageEditText.setText("Uploading video ....");
            if (videouri != null) {
                // save the selected video in Firebase storage
                final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(videouri));
                reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        String downloadUri = String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl());


                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                // complete the rest of your code
                                sendButton.setEnabled(true);
                                String downloadUri = taskSnapshot.getUploadSessionUri().toString();
                                messageType = "video";
                                messageEditText.setText(downloadUrl);
                                messageEditText.setEnabled(false);
                                Toast.makeText(context, "Video Uploaded!!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error, Image not uploaded
                        sendButton.setEnabled(true);
                        Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // show the progress bar
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        Toast.makeText(context,progress+"",Toast.LENGTH_SHORT).show();
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

    private String setNotificationDateTime(){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return inputFormat.format(now);
    }

    public static String notificationUrl="https://fcm.googleapis.com/fcm/send";
    RequestQueue mrequestQue;

    public void sendNotificationtobatch(Context context, String title, String body) {
        mrequestQue = Volley.newRequestQueue(context);
        try {
            JSONObject mainobj= new JSONObject();
            mainobj.put("to","/topics/"+facilitatorId);
//            JSONObject notificationobj=new JSONObject();
//            notificationobj.put("title",title);
//            notificationobj.put("body",body);

            JSONObject data=new JSONObject();
            data.put("type","chat_notification");
            data.put("title",title);
            data.put("body",body);
            data.put("facilitatorId",facilitatorId);
            data.put("userID",Util.getUserId(context));
            data.put("username",Util.getUsernameShowable(context));

            mainobj.put("data",data);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, notificationUrl, mainobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("=== resopnse " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println( "=== error "+error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header =new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","Key=AAAAJnoj22E:APA91bFhtFassESy7oOey1LR6YmAVOzR6Ah2JacJAk2-9CXAJCh3dLevilgH58wa8wNlzl6WnpXgRe8g6WzEbrS2PuUhzQZ12iKNCN9yJR0edKfrNofROKBeTHEVwXEshO5mMPuAgCVI");
                    return header;
                }
            };

            mrequestQue.add(request);
        }
        catch (JSONException j)
        {
            j.printStackTrace();
        }

    }

    //*****                   END                    *****//
    private void setStaticText(){
        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("Chat Screen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot!=null){
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        messageEditText.setHint(textArrayList.get(0));
                        messageText = textArrayList.get(1);
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

}
