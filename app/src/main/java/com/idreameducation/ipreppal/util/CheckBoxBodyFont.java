package com.idreameducation.ipreppal.util;

/**
 * Created by Imbibian on 1/18/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;


public class CheckBoxBodyFont extends CheckBox {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckBoxBodyFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public CheckBoxBodyFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CheckBoxBodyFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CheckBoxBodyFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            try {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf");
                setTypeface(myTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}