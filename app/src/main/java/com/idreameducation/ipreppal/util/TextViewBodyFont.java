package com.idreameducation.ipreppal.util;

/**
 * Created by Imbibian on 1/18/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


public class TextViewBodyFont extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewBodyFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public TextViewBodyFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TextViewBodyFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public TextViewBodyFont(Context context) {
        super(context);
        init(null);
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        if (text.length() > 0) {
//            text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
//        }
//        super.setText(text, type);
//    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            try {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mukta-Regular.ttf");

                setTypeface(myTypeface);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}