package com.idreameducation.ipreppal;

import android.annotation.SuppressLint;
import android.app.Service;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RecordingLiveSessionService extends Service {
    Context context;
    WindowManager windowManager;
    int _xDelta;
    public RelativeLayout relativeLayout;
    int _yDelta;
    //    ViewGroup.LayoutParams layoutParams;
//    LinearLayout linearLayout;
    WindowManager.LayoutParams layoutParams2;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        linearLayout = new LinearLayout(context);
        relativeLayout = new RelativeLayout(context);


        ImageView startRecording = new ImageView(context);

        startRecording.setImageDrawable(getResources().getDrawable(R.drawable.ic_avatar));



        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                150,
                150,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
                PixelFormat.TRANSLUCENT);

        layoutParams2 = new WindowManager.LayoutParams(
                150,
                150,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                PixelFormat.TRANSLUCENT);


        relativeLayout.addView(startRecording,layoutParams);

        windowManager.addView(relativeLayout,layoutParams2);

        startRecording.requestFocus();
        startRecording.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.d("AD","Action Down");
                        initialX = layoutParams2.x;
                        initialY = layoutParams2.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
//                        Log.d("AD","Action Up");
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
//                            if (isViewCollapsed()) {
//                                collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
//                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
//                        Log.d("AD","Action Move");
                        layoutParams2.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParams2.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(relativeLayout, layoutParams2);
                        return true;
                }
                return false;
            }
        });


        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Recording Started", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
//        linearLayout.setVisibility(View.GONE);
        super.onDestroy();
    }



}