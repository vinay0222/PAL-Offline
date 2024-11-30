package com.idreameducation.ipreppal.pal.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.idreameducation.ipreppal.pal.activity.ChatActivity;
import com.idreameducation.ipreppal.pal.activity.NotificationActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sony on 4/4/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0;
    OnItemClickListener clickListener;
    private Global global;
    private ArrayList<HashMap<String, String>> contentArrayList;
    private Context context;
    private String readMore;
    private String assign;
    private String chat;
    private String message;
    private RequestOptions requestOptions;


    public NotificationAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList, String readMore, String assign, String chat, String message) {
        requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        this.context = context;
        this.contentArrayList = contentArrayList;
        this.readMore = readMore;
        this.assign = assign;
        this.chat = chat;
        this.message = message;
        global = (Global) context.getApplicationContext();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_notification, viewGroup, false);
            return new NotificationAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof NotificationAdapter.ViewItem) {
            final NotificationAdapter.ViewItem viewHolder = (NotificationAdapter.ViewItem) viewHolderValue;
            viewHolder.textViewReadMore.setText(readMore);
            viewHolder.textViewTime.setText(contentArrayList.get(position).get("messageTime").split("-")[0]);
            try {
                if (contentArrayList.get(position).get("sender").equalsIgnoreCase("iDream")) {
                    viewHolder.textViewIprep.setVisibility(View.VISIBLE);
                    viewHolder.imageViewFaciitator.setVisibility(View.GONE);
                } else {

//                    if (contentArrayList.get(position).get("category")!=null) {
//                        if (contentArrayList.get(position).get("category").contains("Video Lessons") || contentArrayList.get(position).get("category").contains("व्हिडिओ धडे")) {
//                            Glide.with(context).load(R.mipmap.play_placeholder).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        } else if (contentArrayList.get(position).get("category").equalsIgnoreCase("STEM Projects") || contentArrayList.get(position).get("category").equalsIgnoreCase("Toys & Activities") || contentArrayList.get(position).get("category").equalsIgnoreCase("STEM प्रकल्प")) {
//                            Glide.with(context).load(R.mipmap.activity_videos_black).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        } else if (contentArrayList.get(position).get("category").contains("Syllabus Books") || contentArrayList.get(position).get("category").contains("Ncert Books") || contentArrayList.get(position).get("category").contains("अभ्यासक्रम पुस्तके")) {
//                            Glide.with(context).load(R.mipmap.books).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        } else if (contentArrayList.get(position).get("category").contains("Books") || contentArrayList.get(position).get("category").contains("पुस्तके आणि कथा")) {
//                            Glide.with(context).load(R.mipmap.books).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        } else if (contentArrayList.get(position).get("category").equalsIgnoreCase("Practice")) {
//                            Glide.with(context).load(R.mipmap.practicals).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        } else {
//                            Glide.with(context).load(R.mipmap.play_placeholder).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                        }
//                    }else {
//                        Glide.with(context).load(R.mipmap.practice_cate).apply(requestOptions).into(viewHolder.imageViewFaciitator);
//                    }

                    if (contentArrayList.get(position).get("isClicked").equalsIgnoreCase("false"))
                    {
                        Glide.with(context).load(R.mipmap.check_not).apply(requestOptions).into(viewHolder.imageview);
                    }else {
                        Glide.with(context).load(R.mipmap.filledcheckbox).apply(requestOptions).into(viewHolder.imageview);
                    }


                    viewHolder.textViewIprep.setVisibility(View.GONE);
                    viewHolder.imageViewFaciitator.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (contentArrayList.get(position).get("replyable").equalsIgnoreCase("Not Showable")) {
                    viewHolder.textViewnotificationSenderName.setText(contentArrayList.get(position).get("sender"));
                } else if (contentArrayList.get(position).get("replyable").equalsIgnoreCase("Showable")) {
                    viewHolder.textViewnotificationSenderName.setText(contentArrayList.get(position).get("sender"));
                } else {
                    viewHolder.textViewnotificationSenderName.setText(contentArrayList.get(position).get("sender"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (contentArrayList.get(position).get("replyable").equalsIgnoreCase("AssignTopic")) {

//
                    if (contentArrayList.get(position).get("category")!=null)
                    {
                        String categoryType = contentArrayList.get(position).get("category");
                    if (categoryType.equalsIgnoreCase("Books"))
                    {
                        categoryType ="Book";
                    }else if (categoryType.equalsIgnoreCase("Video Lessons"))
                    {
                        categoryType ="Video Lesson";
                    }else if (categoryType.equalsIgnoreCase("Practice"))
                    {
                        categoryType ="Practice";
                    }

                    if (contentArrayList.get(position).get("topicAssigned")!=null)
                    {
                        viewHolder.textViewnotificationMessage.setText(" Assigned you a  "+ categoryType +" " +" of subject " +contentArrayList.get(position).get("subject")+" > " +contentArrayList.get(position).get("topicAssigned")+" > " + contentArrayList.get(position).get("message").split(":")[1]);
                    }else {
                        viewHolder.textViewnotificationMessage.setText(" Assigned you a  " + categoryType + " " + " of subject " + contentArrayList.get(position).get("subject") + " > " + contentArrayList.get(position).get("message").split(":")[1]);
                    }
                    }else {
                        viewHolder.textViewnotificationMessage.setText(" Assigned you a Practice of subject "+contentArrayList.get(position).get("subject")+" > " + contentArrayList.get(position).get("message").split(":")[1]);

                    }


                } else {
                    viewHolder.textViewnotificationMessage.setText(contentArrayList.get(position).get("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.textViewReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.preventTwoClick(view);
                    global.setUserNameForChat(contentArrayList.get(position).get("sender"));
                    if (contentArrayList.get(position).get("replyable").equalsIgnoreCase("Not Showable")) {
                        ((NotificationActivity) context).showNotificationInDetail(contentArrayList.get(position).get("message"));
                    } else if (contentArrayList.get(position).get("replyable").equalsIgnoreCase("Showable")) {
                        context.startActivity(new Intent(context, ChatActivity.class).putExtra("FacilitatorId", contentArrayList.get(position).get("senderId")));
                    } else {
                        ((NotificationActivity) context).showNotificationInDetail(contentArrayList.get(position).get("notificationMessage") + " " + contentArrayList.get(position).get("message").split(":")[1]);
                    }
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return contentArrayList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewReadMore;
        TextView textViewnotificationSenderName;
        TextView textViewnotificationMessage;
        TextView textViewTime;
        TextView textViewIprep;
        ImageView imageViewFaciitator;
        ImageView imageview;

        public ViewItem(View holderView) {
            super(holderView);
            holderView.setOnClickListener(this);
            textViewReadMore = holderView.findViewById(R.id.textViewReadMore);
            textViewnotificationSenderName = holderView.findViewById(R.id.textViewnotificationSenderName);
            textViewnotificationMessage = holderView.findViewById(R.id.textViewnotificationMessage);
            textViewTime = holderView.findViewById(R.id.textViewTime);
            textViewIprep = holderView.findViewById(R.id.textViewIprep);
            imageViewFaciitator = holderView.findViewById(R.id.imageViewFaciitator);
            imageview = holderView.findViewById(R.id.imageview);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}