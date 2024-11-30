package com.idreameducation.ipreppal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ServiceFloating extends Service implements View.OnTouchListener{

    Context context;
    WindowManager windowManager;
    ViewGroup.LayoutParams layoutParams;
    int _xDelta,_yDelta;
    RelativeLayout rootLayout;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    ImageView iconView;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

        iconView= new ImageView(this);
        rootLayout= new RelativeLayout(this);
//        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        iconView.setImageDrawable(getResources().getDrawable(R.drawable.ic_avatar));
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);


        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams = new WindowManager.LayoutParams(
                90,
                90,
                LAYOUT_FLAG,
                  WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                PixelFormat.TRANSLUCENT);

        rootLayout.addView(iconView, layoutParams);
        windowManager.addView(rootLayout, layoutParams2);

        iconView.setOnTouchListener(this);

//        iconView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("8888888888888");
//            }
//        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(iconView);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        rootLayout.invalidate();
        return true;
    }
}