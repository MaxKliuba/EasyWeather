package com.maxclub.easyweather.utils;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class TextDrawable extends Drawable {

    private Resources mResources;
    private int mColor = Color.WHITE;
    private int mTextSize = 16;
    private float mTextSizeSp;
    private Paint mPaint;
    private CharSequence mText;

    public TextDrawable(Resources res, CharSequence text) {
        mResources = res;
        mText = text;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setColor(mColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        mPaint.setTypeface(typeface);
        setTextSize(mTextSize);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextSizeSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                mTextSize, mResources.getDisplayMetrics());
        mPaint.setTextSize(mTextSizeSp);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawText(mText, 0, mText.length(),
                bounds.centerX(), bounds.centerY() + getIntrinsicHeight() / 2.6f, mPaint);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (mPaint.measureText(mText, 0, mText.length()) + .5);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mTextSizeSp;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }
}