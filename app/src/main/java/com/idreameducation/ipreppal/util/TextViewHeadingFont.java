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


public class TextViewHeadingFont extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewHeadingFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public TextViewHeadingFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TextViewHeadingFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public TextViewHeadingFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            try {

                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Inter-Medium.otf");

                setTypeface(myTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}