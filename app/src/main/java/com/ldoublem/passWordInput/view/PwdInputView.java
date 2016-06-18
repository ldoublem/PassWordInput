package com.ldoublem.passWordInput.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by lumingmin on 16/6/16.
 */

public class PwdInputView extends EditText {
    private Paint mPaint;
    private Paint mPaintContent;
    private Paint mPaintArc;
    private int mPadding = 1;
    private int radiusBg;
    private float radiusArc;
    private float radiusArc_last;
    private int PaintLastArcAnimDuration = 200;
    private int mTextSize;
    private PaintLastArcAnim mPaintLastArcAnim;
    private int textLength;
    private int maxLineSize;
    private boolean addText = true;
    private float interpolatedTime;
    private boolean isShadowPasswords = false;

    private String defaultStr = null;
    private int defalutPicId = -1;

    private ViewType mViewType = ViewType.DEFAULT;

    public PwdInputView(Context context) {
        this(context, null);
    }

    public PwdInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }


    public enum ViewType {
        DEFAULT, UNDERLINE, BIASLINE
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 背景
        RectF rectIn = new RectF(mPadding, mPadding, getMeasuredWidth() - mPadding, getMeasuredHeight() - mPadding);
        canvas.drawRoundRect(rectIn, radiusBg, radiusBg, mPaintContent);
        // 边框
        RectF rect = new RectF(mPadding, mPadding, getMeasuredWidth() - mPadding, getMeasuredHeight() - mPadding);
        mPaint.setStrokeWidth(0.8f);
        canvas.drawRoundRect(rect, radiusBg, radiusBg, mPaint);


        float cx, cy = getMeasuredHeight() / 2;
        float half = getMeasuredWidth() / maxLineSize / 2;

        switch (mViewType) {
            case DEFAULT:
                mPaint.setStrokeWidth(0.5f);
                for (int i = 1; i < maxLineSize; i++) {
                    float x = getMeasuredWidth() * i / maxLineSize;
                    canvas.drawLine(x, 0, x, getMeasuredHeight(), mPaint);
                }
                break;

            case UNDERLINE:
                mPaint.setStrokeWidth(4.0f);
                for (int i = getText().toString().length(); i < maxLineSize; i++) {
                    float x = getMeasuredWidth() * i / maxLineSize + half;
                    canvas.drawLine(x - half / 3,
                            getMeasuredHeight() - getMeasuredHeight() / 4,
                            x + half / 3,
                            getMeasuredHeight() - getMeasuredHeight() / 4,
                            mPaint);
                }
                break;

            case BIASLINE:
                mPaint.setStrokeWidth(3.0f);
                for (int i = getText().toString().length(); i < maxLineSize; i++) {
                    float x = getMeasuredWidth() * i / maxLineSize + half;
                    canvas.drawLine(x + half / 8,
                            getMeasuredHeight() / 2 - half / 4,
                            x - half / 8,
                            getMeasuredHeight() / 2 + half / 4,
                            mPaint);
                }


                break;
            default:
                break;
        }


        if (isShadowPasswords) {

            mPaintArc.setTextSize(mTextSize);

            Bitmap bitmap = null;
            for (int i = 0; i < textLength; i++) {
                cx = getMeasuredWidth() * i / maxLineSize + half;

                if (defalutPicId != -1) {
                    float scale = 1f;
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(getContext().getResources(), defalutPicId);
                        scale = bitmap.getHeight() / bitmap.getWidth();
                        bitmap = setBitmapSize(bitmap, (int) half, scale);
                    }
                    canvas.drawBitmap(bitmap, cx - half / 2, cy - (half * scale) / 2, mPaintArc);
                } else {
                    String text;
                    if (defaultStr == null)
                        text = String.valueOf(getText().toString().charAt(i));
                    else
                        text = defaultStr;
                    canvas.drawText(text, cx - getFontlength(mPaintArc, text) / 2.0f, cy + getFontHeight(mPaintArc, text) / 2.0f, mPaintArc);

                }


            }

        } else {

            for (int i = 0; i < maxLineSize; i++) {
                cx = getMeasuredWidth() * i / maxLineSize + half;

                if (addText) {
                    if (i < textLength - 1) {
                        canvas.drawCircle(cx, cy, radiusArc, mPaintArc);
                    } else if (i == textLength - 1) {

                        canvas.drawCircle(cx, cy, radiusArc * interpolatedTime, mPaintArc);
                    }
                } else {
                    if (i < textLength) {
                        canvas.drawCircle(cx, cy, radiusArc, mPaintArc);
                    } else if (i == textLength) {
                        canvas.drawCircle(cx, cy, radiusArc - radiusArc * interpolatedTime, mPaintArc);
                    }
                }
            }
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().length() - this.textLength >= 0) {
            addText = true;
        } else {
            addText = false;
        }

        this.textLength = text.toString().length();
        if (textLength <= getMaxLength()) {


            if (mPaintLastArcAnim != null) {
                clearAnimation();
                startAnimation(mPaintLastArcAnim);
            } else {
                invalidate();
            }
        }
    }

    private void initPaint() {
        maxLineSize = getMaxLength();
        mPaintLastArcAnim = new PaintLastArcAnim();
        mPaintLastArcAnim.setDuration(PaintLastArcAnimDuration);
        radiusBg = dip2px(4);
        radiusArc = dip2px(6);
        mTextSize = dip2px(15);
        textLength = 0;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);

        mPaintContent = new Paint();
        mPaintContent.setAntiAlias(true);
        mPaintContent.setStyle(Paint.Style.FILL);
        mPaintContent.setColor(Color.WHITE);

        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStyle(Paint.Style.FILL);
        mPaintArc.setColor(Color.argb(155, 0, 0, 0));


    }

    private class PaintLastArcAnim extends Animation {
        @Override
        protected void applyTransformation(float time, Transformation t) {
            super.applyTransformation(time, t);
//            radiusArc_last = radiusArc * interpolatedTime;
            interpolatedTime = time;
            postInvalidate();


        }
    }


    public int getMaxLength() {

        int length = 10;

        try {

            InputFilter[] inputFilters = getFilters();

            for (InputFilter filter : inputFilters) {

                Class<?> c = filter.getClass();

                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {

                    Field[] f = c.getDeclaredFields();

                    for (Field field : f) {

                        if (field.getName().equals("mMax")) {

                            field.setAccessible(true);

                            length = (Integer) field.get(filter);

                        }

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return length;

    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float getFontlength(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    public float getFontHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();

    }


    public void setShadowPasswords(boolean show) {
        this.isShadowPasswords = show;
        this.defalutPicId = -1;
        this.defaultStr = null;
        postInvalidate();

    }


    public void setShadowPasswords(boolean show, String defaultStr) {
        this.isShadowPasswords = show;
        this.defalutPicId = -1;
        this.defaultStr = defaultStr;
        postInvalidate();

    }

    public void setShadowPasswords(boolean show, int picId) {
        this.isShadowPasswords = show;
        this.defalutPicId = picId;
        this.defaultStr = null;
        postInvalidate();

    }

    private Bitmap setBitmapSize(Bitmap b, int w, float s) {
        b = Bitmap.createScaledBitmap(b, w, (int) (w * s), true);
        return b;

    }


    public void setPwdInputViewType(ViewType type) {
        this.mViewType = type;

    }

    public void setRadiusBg(int radiusBg) {
        this.radiusBg = radiusBg;
    }


}
