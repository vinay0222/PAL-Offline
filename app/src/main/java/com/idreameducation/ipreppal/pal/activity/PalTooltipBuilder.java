package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import androidx.lifecycle.LifecycleOwner;
import com.idreameducation.ipreppal.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

public class PalTooltipBuilder {
    public static Balloon getTooltip(Context context, LifecycleOwner lifecycleOwner, ArrowOrientation arrowOrientation){
        return new Balloon.Builder(context)
            .setWidth(420)
            .setHeight(210)
            .setLayout(R.layout.pal_tooltip)
            .setArrowSize(12)
            .setPadding(5)
            .setArrowOrientation(arrowOrientation)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowPosition(0.5f)
            .setCornerRadius(6f)
            .setElevation(6)
            .setBackgroundColorResource(R.color.white)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setDismissWhenTouchOutside(false)
            .setDismissWhenShowAgain(true)
            .setDismissWhenLifecycleOnPause(true)
            .setLifecycleOwner(lifecycleOwner)
            .build();
    }
}
