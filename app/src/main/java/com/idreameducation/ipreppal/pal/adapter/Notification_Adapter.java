package com.idreameducation.ipreppal.pal.adapter;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.notificationPos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.Notification_model;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.pal.activity.PalFullScreenVideoActivity;
import com.idreameducation.ipreppal.pal.activity.PalPdfViewerActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.notification_holder> {

    ArrayList<Notification_model> notification_modelArrayList;
    Context context;
    Global global;

    public Notification_Adapter(ArrayList<Notification_model> notification_modelArrayList) {
        this.notification_modelArrayList = notification_modelArrayList;
    }

    public Notification_Adapter() {
    }

    @NonNull
    @Override
    public Notification_Adapter.notification_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        global = (Global) parent.getContext().getApplicationContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_view, parent, false);
        return new notification_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_Adapter.notification_holder holder, @SuppressLint("RecyclerView") int position) {

        holder.notification_title.setText(notification_modelArrayList.get(position).getMessage());
        holder.notification_body.setText(notification_modelArrayList.get(position).getNotificationMessage().replace("_"," "));

        if(notification_modelArrayList.get(position).isRead()) {
            holder.notification_title.setTextColor(context.getResources().getColor(R.color.notifidatibgrey));
            holder.notification_body.setTextColor(context.getResources().getColor(R.color.notifidatibgrey));
            holder.viewIndegiator.setVisibility(View.INVISIBLE);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationPos=position;
                    if(notification_modelArrayList.get(position).getType()!=null) {
                        if(notification_modelArrayList.get(position).getType().equals("image_notification")) {
                            global.setFullImage(notification_modelArrayList.get(position).getFileLink());
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                        else if(notification_modelArrayList.get(position).getType().equals("video_notification")) {

                            String videocd = notification_modelArrayList.get(position).getFileLink();

                            String[] videoCode = videocd.split("/");
                            String videoc = videoCode[videoCode.length - 1];

//                        String videoc = notification_modelArrayList.get(position).getFileLink();
                            Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                            intent.putExtra("videoId", videoc);
                            intent.putExtra("vidId", videoc);
//                intent.putExtra("onlineLink",videoc);
                            intent.putExtra("offlineLink", "getOffLineLink(videoc)");
                            intent.putExtra("videoName", "videoName");
                            context.startActivity(intent);
                        }
                        else if(notification_modelArrayList.get(position).getType().equals("pdf_notification")) {
                            Intent intent = new Intent(context, PalPdfViewerActivity.class);

                            intent.putExtra("onlineLink", notification_modelArrayList.get(position).getFileLink());
                            intent.putExtra("topic", "Notification PDF");
                            intent.putExtra("offlineLink", "bookList.get(position).get()");
                            intent.putExtra("topicName", "Notification PDF");
                            intent.putExtra("name", "Notification PDF");
                            intent.putExtra("subjectName", "Notification PDF");
                            intent.putExtra("bookId", "Notification PDF");
                            intent.putExtra("categoryID", "books_ncert");
                            intent.putExtra("topicId", "Notification PDF");
                            intent.putExtra("topic_name_main", "Notification PDF");

                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
        else {
            holder.notification_title.setTextColor(context.getResources().getColor(R.color.blue));
            holder.notification_body.setTextColor(context.getResources().getColor(R.color.textcolour));
            holder.viewIndegiator.setVisibility(View.VISIBLE);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationPos=position;
                    if(notification_modelArrayList.get(position).getType()!=null) {
                        if(notification_modelArrayList.get(position).getType().equals("image_notification")) {
                            global.setFullImage(notification_modelArrayList.get(position).getFileLink());
                            context.startActivity(new Intent(context, FullProfileImageActivity.class));
                        }
                        else if(notification_modelArrayList.get(position).getType().equals("video_notification")) {

                            String videocd = notification_modelArrayList.get(position).getFileLink();

                            String[] videoCode = videocd.split("/");
                            String videoc = videoCode[videoCode.length - 1];

//                        String videoc = notification_modelArrayList.get(position).getFileLink();
                            Intent intent = new Intent(context, PalFullScreenVideoActivity.class);
                            intent.putExtra("videoId", videoc);
                            intent.putExtra("vidId", videoc);
//                intent.putExtra("onlineLink",videoc);
                            intent.putExtra("offlineLink", "getOffLineLink(videoc)");
                            intent.putExtra("videoName", "videoName");
                            context.startActivity(intent);
                        }
                        else if(notification_modelArrayList.get(position).getType().equals("pdf_notification")) {
                            Intent intent = new Intent(context, PalPdfViewerActivity.class);

                            intent.putExtra("onlineLink", notification_modelArrayList.get(position).getFileLink());
                            intent.putExtra("topic", "Notification PDF");
                            intent.putExtra("offlineLink", "bookList.get(position).get()");
                            intent.putExtra("topicName", "Notification PDF");
                            intent.putExtra("name", "Notification PDF");
                            intent.putExtra("subjectName", "Notification PDF");
                            intent.putExtra("bookId", "Notification PDF");
                            intent.putExtra("categoryID", "books_ncert");
                            intent.putExtra("topicId", "Notification PDF");
                            intent.putExtra("topic_name_main", "Notification PDF");

                            context.startActivity(intent);
                        }
                    }
                    FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(context))
                            .child(notification_modelArrayList.get(position).getKey()).child("read").setValue(true);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return notification_modelArrayList.size();
    }

    public class notification_holder extends RecyclerView.ViewHolder{

        TextView notification_title,notification_body;
        View viewIndegiator;
        LinearLayout card;
        public notification_holder(@NonNull View itemView) {
            super(itemView);
            notification_title= itemView.findViewById(R.id.notification_title);
            notification_body= itemView.findViewById(R.id.notification_body);
            viewIndegiator= itemView.findViewById(R.id.viewIndegiator);
            card= itemView.findViewById(R.id.card);
        }
    }
}
