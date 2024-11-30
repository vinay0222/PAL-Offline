package com.idreameducation.ipreppal.pal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.FullProfileImageActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by sony on 4/4/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    OnItemClickListener clickListener;
    private final ArrayList<HashMap<String, String>> contentArrayList;
    private static final int TYPE_ITEM = 0;
    private final Context context;

    public ChatAdapter(Context context, ArrayList<HashMap<String, String>> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_chat, viewGroup, false);
            return new ChatAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, @SuppressLint("RecyclerView") final int position) {
        if (viewHolderValue instanceof ChatAdapter.ViewItem) {
            final ChatAdapter.ViewItem viewHolder = (ChatAdapter.ViewItem) viewHolderValue;
            if(contentArrayList.get(position).get("inputType")==null) {
                HashMap<String, String> v=contentArrayList.get(position);
                v.put("inputType","Text");
                contentArrayList.set(position,v);
            }
            if(contentArrayList.get(position).get("type").equalsIgnoreCase("Sender")) {
                viewHolder.reletiveReciever.setVisibility(View.GONE);
                viewHolder.reletiveSender.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageSender.setVisibility(View.VISIBLE);
                viewHolder.timeReciever.setVisibility(View.GONE);
                viewHolder.senderImageview.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageSender.setText(contentArrayList.get(position).get("message"));
                viewHolder.textViewMessageReciver.setTextColor(context.getResources().getColor(R.color.text));

                if(contentArrayList.get(position).get("message").toLowerCase().contains("https://")) {
                    String url = contentArrayList.get(position).get("message");
                    viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                    viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openURL(context, Uri.parse(url));
                        }
                    });
                }

                if(contentArrayList.get(position).get("message").toLowerCase().contains("http://")) {
                    String url = contentArrayList.get(position).get("message");
                    viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                    viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openURL(context, Uri.parse(url));
                        }
                    });
                }

                if(contentArrayList.get(position).get("inputType").equals("Image")) {
                    viewHolder.senderImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageSender.setVisibility(View.GONE);
                    setHightWidth(viewHolder.senderImageview,350,220);
                    Glide.with(context)
                            .load(contentArrayList.get(position).get("message"))
                            .into(viewHolder.senderImageview);
                    viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.setImageURL(context,contentArrayList.get(position).get("message"));
                            Intent intent=new Intent(context, FullProfileImageActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }

                else if(contentArrayList.get(position).get("inputType").equals("video")) {
                    viewHolder.senderImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageSender.setVisibility(View.GONE);
                    setHightWidth(viewHolder.senderImageview,100,300);
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_video_24))
                            .into(viewHolder.senderImageview);
                }

                else if(contentArrayList.get(position).get("inputType").equals("pdf")) {
                    viewHolder.senderImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageSender.setVisibility(View.GONE);
                    setHightWidth(viewHolder.senderImageview,100,300);
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_pdf_24))
                            .into(viewHolder.senderImageview);
                }

                else viewHolder.senderImageview.setVisibility(View.GONE);

                try {
                    viewHolder.timeSender.setText(contentArrayList.get(position).get("time").split("-")[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.timeSender.setText(contentArrayList.get(position).get("time").split("-")[0]);
                }
            }
            else {
                viewHolder.timeSender.setVisibility(View.GONE);
                viewHolder.reletiveReciever.setVisibility(View.VISIBLE);
                viewHolder.textViewMessageReciver.setVisibility(View.VISIBLE);
                viewHolder.reletiveSender.setVisibility(View.GONE);
                viewHolder.textViewMessageReciver.setText(contentArrayList.get(position).get("message"));
                viewHolder.textViewMessageReciver.setTextColor(context.getResources().getColor(R.color.text));

                if(contentArrayList.get(position).get("message").toLowerCase().contains("https://")) {
                    String url = contentArrayList.get(position).get("message");
                    viewHolder.textViewMessageReciver.setTextColor(context.getResources().getColor(R.color.linkcolour));
                    viewHolder.reletiveReciever.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openURL(context, Uri.parse(url));
                        }
                    });
                }

                if(contentArrayList.get(position).get("message").toLowerCase().contains("http://")) {
                    String url = contentArrayList.get(position).get("message");
                    viewHolder.textViewMessageSender.setTextColor(context.getResources().getColor(R.color.linkcolour));
                    viewHolder.reletiveSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openURL(context, Uri.parse(url));
                        }
                    });
                }

                if(contentArrayList.get(position).get("inputType").equals("Image")) {
                    viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                    setHightWidth(viewHolder.recieverImageview,350,220);
                    Glide.with(context)
                            .load(contentArrayList.get(position).get("message"))
                            .into(viewHolder.recieverImageview);
                    viewHolder.reletiveReciever.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.setImageURL(context,contentArrayList.get(position).get("message"));
                            Intent intent=new Intent(context, FullProfileImageActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }

                else if(contentArrayList.get(position).get("inputType").equals("video")) {
                    viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                    setHightWidth(viewHolder.recieverImageview,100,300);
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_video_24))
                            .into(viewHolder.recieverImageview);
                }

                else if(contentArrayList.get(position).get("inputType").equals("pdf")) {
                    viewHolder.recieverImageview.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessageReciver.setVisibility(View.GONE);
                    setHightWidth(viewHolder.recieverImageview,100,300);
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_pdf_24))
                            .into(viewHolder.recieverImageview);
                }

                else viewHolder.recieverImageview.setVisibility(View.GONE);

                try {
                    viewHolder.timeReciever.setText(contentArrayList.get(position).get("time").split("-")[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.timeReciever.setText(contentArrayList.get(position).get("time").split("-")[0]);
                }
            }

            if (contentArrayList.get(position).get("messageType").equalsIgnoreCase("Not Showable")) {
                viewHolder.reletiveReciever.setVisibility(View.GONE);
                viewHolder.reletiveSender.setVisibility(View.VISIBLE);
            }
            try {
                if (!contentArrayList.get(position).get("date").equalsIgnoreCase("hide")) {
                    viewHolder.textViewDate.setVisibility(View.VISIBLE);
                    viewHolder.date_layout.setVisibility(View.VISIBLE);
                    viewHolder.textViewDate.setText(contentArrayList.get(position).get("date"));
                }else {
                    viewHolder.textViewDate.setVisibility(View.GONE);
                    viewHolder.date_layout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.textViewDate.setVisibility(View.GONE);
                viewHolder.date_layout.setVisibility(View.GONE);
            }
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
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return contentArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewMessageReciver;
        protected TextView textViewMessageSender;
        protected TextView timeReciever;
        protected TextView timeSender;
        protected TextView textViewDate;
        protected LinearLayout reletiveReciever;
        protected LinearLayout reletiveSender;
        protected LinearLayout date_layout;
        protected ImageView recieverImageview,senderImageview;


        public ViewItem(View holderView) {
            super(holderView);
            textViewMessageReciver = holderView.findViewById(R.id.textViewMessageReciver);
            textViewMessageSender = holderView.findViewById(R.id.textViewMessageSender);
            timeReciever = holderView.findViewById(R.id.timeReciever);
            timeSender = holderView.findViewById(R.id.timeSender);
            reletiveReciever = holderView.findViewById(R.id.reletiveReciever);
            reletiveSender = holderView.findViewById(R.id.reletiveSender);
            textViewDate = holderView.findViewById(R.id.textViewDate);
            date_layout = holderView.findViewById(R.id.date_layout);

            senderImageview = holderView.findViewById(R.id.senderImageview);
            recieverImageview = holderView.findViewById(R.id.recieverImageview);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}