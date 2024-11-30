package com.idreameducation.ipreppal.util;

/**
 * Created by apple on 23/01/18.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.idreameducation.ipreppal.R;


/**
 * TODO: document your custom view class.
 */
public class FrameLayoutWithHole extends RelativeLayout {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Paint mOverlayPaint;
    public FrameLayoutWithHole(Context context) {
        super(context);
        init(null, 0);
    }

    public FrameLayoutWithHole(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FrameLayoutWithHole(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FrameLayoutWithHole, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.FrameLayoutWithHole_exampleString);
        if (mExampleString == null){
            mExampleString = "Hello custom view";
        }
        mExampleColor = a.getColor(
                R.styleable.FrameLayoutWithHole_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.FrameLayoutWithHole_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.FrameLayoutWithHole_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.FrameLayoutWithHole_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        setWillNotDraw(false);

        bitmapx = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmapx);
        paint = new Paint();
        paint.setColor(R.mipmap.main_back);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    Bitmap bitmapx;
    private Canvas temp;
    private Paint paint;
    private Paint p = new Paint();
    private Paint transparentPaint;
    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) ,
                paddingTop + (contentHeight + mTextHeight) ,
                mTextPaint);
        int mWidth= this.getResources().getDisplayMetrics().widthPixels;
        int mHeight= this.getResources().getDisplayMetrics().heightPixels;
        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);
        temp.drawCircle(mWidth/2,mHeight/3, 220, transparentPaint);
        canvas.drawBitmap(bitmapx, 0, 0, p);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}