package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.SupportChatModel;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SupportChatAdapter extends RecyclerView.Adapter<SupportChatAdapter.holder> {

    ArrayList<SupportChatModel> supportChatModels;
    Context context;

    public SupportChatAdapter(ArrayList<SupportChatModel> supportChatModels) {
        this.supportChatModels = supportChatModels;
    }

    public SupportChatAdapter() {
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supportchatview, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder viewHolder, @SuppressLint("RecyclerView") int position) {
        if(supportChatModels.get(position).getInputType()==null) {
            SupportChatModel chatModel=supportChatModels.get(position);
            chatModel.setInputType("Text");
            supportChatModels.set(position,chatModel);
        }
        if(supportChatModels.get(position).getType().equalsIgnoreCase("Sender")) {
            viewHolder.reletiveReciever.setVisibility(View.GONE);
            viewHolder.reletiveSender.setVisibility(View.VISIBLE);
            viewHolder.textViewMessageSender.setVisibility(View.VISIBLE);
            viewHolder.timeReciever.setVisibility(View.GONE);
            viewHolder.senderImageLayout.setVisibility(View.VISIBLE);
            viewHolder.textViewMessageSender.setText(supportChatModels.get(position).getMessage());

            if(supportChatModels.get(position).getMessage().toLowerCase().contains("https://")) {
                String url = supportChatModels.get(position).getMessage();
                viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openURL(context, Uri.parse(url));
                    }
                });
            }

            if(supportChatModels.get(position).getMessage().toLowerCase().contains("http://")) {
                String url = supportChatModels.get(position).getMessage();
                viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openURL(context, Uri.parse(url));
                    }
                });
            }

            if(supportChatModels.get(position).getImage()!=null) {
                viewHolder.senderImageLayout.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageSender.setVisibility(View.VISIBLE);
                setHightWidth(viewHolder.senderImageview,100,220);
                Glide.with(context)
                        .load(supportChatModels.get(position).getImage())
                        .into(viewHolder.senderImageview);
                viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.setImageURL(context,supportChatModels.get(position).getImage());
                        Intent intent=new Intent(context, FullProfileImageActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

            else if(supportChatModels.get(position).getInputType().equals("video")) {
                viewHolder.senderImageLayout.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageSender.setVisibility(View.GONE);
                setHightWidth(viewHolder.senderImageview,100,300);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_video_24))
                        .into(viewHolder.senderImageview);
            }

            else if(supportChatModels.get(position).getInputType().equals("pdf")) {
                viewHolder.senderImageLayout.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageSender.setVisibility(View.GONE);
                setHightWidth(viewHolder.senderImageview,100,300);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_pdf_24))
                        .into(viewHolder.senderImageview);
            }

            else viewHolder.senderImageLayout.setVisibility(View.GONE);

            try {

                viewHolder.timeSender.setText(supportChatModels.get(position).getTime()+" |  You");
            } catch (Exception e) {
                e.printStackTrace();
                String[] tt=supportChatModels.get(position).getTime().split("-");

                viewHolder.timeSender.setText(tt[0]+"  You");
            }
        }
        else {
            viewHolder.timeSender.setVisibility(View.GONE);
            viewHolder.reletiveReciever.setVisibility(View.VISIBLE);
            viewHolder.textViewMessageReciver.setVisibility(View.VISIBLE);
            viewHolder.reletiveSender.setVisibility(View.GONE);
            viewHolder.textViewMessageReciver.setText(supportChatModels.get(position).getMessage());

            if(supportChatModels.get(position).getMessage().toLowerCase().contains("https://")) {
                String url = supportChatModels.get(position).getMessage();
                viewHolder.textViewMessageReciver.setTextColor(context.getResources().getColor(R.color.linkcolour));
                viewHolder.reletiveReciever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openURL(context, Uri.parse(url));
                    }
                });
            }

            if(supportChatModels.get(position).getMessage().toLowerCase().contains("http://")) {
                String url = supportChatModels.get(position).getMessage();
                viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openURL(context, Uri.parse(url));
                    }
                });
            }

            if(supportChatModels.get(position).getImage()!=null) {
                viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                setHightWidth(viewHolder.recieverImageview,350,220);
                Glide.with(context)
                        .load(supportChatModels.get(position).getImage())
                        .into(viewHolder.recieverImageview);
                viewHolder.reletiveReciever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.setImageURL(context,supportChatModels.get(position).getImage());
                        Intent intent=new Intent(context, FullProfileImageActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

            else if(supportChatModels.get(position).getInputType().equals("video")) {
                viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                setHightWidth(viewHolder.recieverImageview,100,300);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_video_24))
                        .into(viewHolder.recieverImageview);
            }

            else if(supportChatModels.get(position).getInputType().equals("pdf")) {
                viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                setHightWidth(viewHolder.recieverImageview,100,300);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_pdf_24))
                        .into(viewHolder.recieverImageview);
            }

            else viewHolder.recieverImageview.setVisibility(View.GONE);

            viewHolder.timeReciever.setText("Support | "+supportChatModels.get(position).getTime());
        }

        if (supportChatModels.get(position).getMessageType().equalsIgnoreCase("Not Showable")) {
            viewHolder.reletiveReciever.setVisibility(View.GONE);
            viewHolder.reletiveSender.setVisibility(View.VISIBLE);
        }
        try {
//            if (!supportChatModels.get(position).get("date").equalsIgnoreCase("hide")) {
//                viewHolder.textViewDate.setVisibility(View.VISIBLE);
//                viewHolder.date_layout.setVisibility(View.VISIBLE);
//                viewHolder.textViewDate.setText(supportChatModels.get(position).get("date"));
//            }else {
//                viewHolder.textViewDate.setVisibility(View.GONE);
//                viewHolder.date_layout.setVisibility(View.GONE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.textViewDate.setVisibility(View.GONE);
            viewHolder.date_layout.setVisibility(View.GONE);
        }
    }

    private void setHightWidth(View view,int height,int width) {
        android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void openURL(Context mContext, Uri uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        Intent browserIntent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setType("text/plain")
                .setData(Uri.fromParts("http", "", null));

        List<ResolveInfo> possibleBrowsers;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            possibleBrowsers = mContext.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (possibleBrowsers.size() == 0) {
                possibleBrowsers = mContext.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_ALL);
            }
        } else {
            possibleBrowsers = mContext.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
        }

        try{
            if (possibleBrowsers.size() > 0) {
                customTabsIntent.intent.setPackage(possibleBrowsers.get(0).activityInfo.packageName);
                customTabsIntent.launchUrl(mContext, uri);
            } else {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(browserIntent2);
            }
        }catch (Exception rr){
            Toast.makeText(mContext, "Not a valid URL", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return supportChatModels.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        protected TextView textViewMessageReciver;
        protected TextView textViewMessageSender;
        protected TextView timeReciever;
        protected TextView timeSender;
        protected TextView textViewDate;
        protected LinearLayout reletiveReciever;
        protected LinearLayout reletiveSender;
        protected LinearLayout date_layout;
        protected LinearLayout senderImageLayout;
        protected ImageView recieverImageview,senderImageview;
        
        public holder(@NonNull View holderView) {
            super(holderView);


            textViewMessageReciver = holderView.findViewById(R.id.textViewMessageReciver);
            textViewMessageSender = holderView.findViewById(R.id.textViewMessageSender);
            timeReciever = holderView.findViewById(R.id.timeReciever);
            timeSender = holderView.findViewById(R.id.timeSender);
            reletiveReciever = holderView.findViewById(R.id.reletiveReciever);
            reletiveSender = holderView.findViewById(R.id.reletiveSender);
            textViewDate = holderView.findViewById(R.id.textViewDate);
            date_layout = holderView.findViewById(R.id.date_layout);
            senderImageLayout = holderView.findViewById(R.id.senderImageLayout);

            senderImageview = holderView.findViewById(R.id.senderImageview);
            recieverImageview = holderView.findViewById(R.id.recieverImageview);
            
        }
    }
}
