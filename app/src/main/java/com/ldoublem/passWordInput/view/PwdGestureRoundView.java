package com.ldoublem.passWordInput.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lumingmin on 16/6/16.
 */

public class PwdGestureRoundView extends PwdGestureView {


    public PwdGestureRoundView(Context context) {

        this(context, null);
    }

    public PwdGestureRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdGestureRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void drawCircleAndLines(Canvas canvas) {
//        super.drawCircleAndLines(canvas);

        mPaint.setStyle(Paint.Style.FILL);

        mPaintLine.setStrokeWidth(dip2px(2));
        mPaintLine.setColor(colorNomalBg);


        for (int i = 0; i < mPwd.size(); i++) {
            RectF rf = mRectFButtons_select.get(mPwd.get(i));

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(colorNomalLine);

            canvas.drawCircle(rf.centerX(), rf.centerY(), ArcWidth * 0.65f, mPaint);
            mPaint.setColor(colorNomalBg);

            canvas.drawCircle(rf.centerX(), rf.centerY(), ArcWidth * 0.45f, mPaint);

            mPaint.setColor(Color.WHITE);



            Iterator iter = mRectFButtons_select.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val.equals(rf)) {
                    canvas.drawText(String.valueOf(key), rf.centerX() - getFontlength(mPaint, String.valueOf(key)) / 2f,
                            rf.centerY() + getFontHeight(mPaint, String.valueOf(key)) / 2f, mPaint);

                }
            }


            if (isDrawLine) {

                if (i > 0 && i < mPwd.size()) {


                    float x1 = mRectFButtons_select.get(mPwd.get(i - 1)).centerX();
                    float y1 = mRectFButtons_select.get(mPwd.get(i - 1)).centerY();
                    float x2 = mRectFButtons_select.get(mPwd.get(i)).centerX();
                    float y2 = mRectFButtons_select.get(mPwd.get(i)).centerY();
                    drawlinebyAngle(x1, y1, x2, y2, ArcWidth * 0.65f, canvas, false);



                }
                if (i == mPwd.size() - 1 && i < mRectFButtons.size() - 1 && !isError) {
                    if (mlastX >= 0 && mlastY >= 0) {
                        float x1 = mRectFButtons_select.get(mPwd.get(i)).centerX();
                        float y1 = mRectFButtons_select.get(mPwd.get(i)).centerY();
                        drawlinebyAngle(x1, y1, mlastX, mlastY, ArcWidth * 0.65f, canvas, true);
                    }
                }
            }

        }
    }

    @Override
    protected void drawButtons(Canvas canvas) {
//        super.drawButtons(canvas);
        if (isError) {
            mPaint.setColor(colorErrorBg);
            mPaintLine.setColor(colorErrorLine);
        } else {
            mPaint.setColor(colorNomalBg);
            mPaintLine.setColor(colorNomalLine);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(1));


        mRectFButtons.clear();
        ArcWidth = mWidth / 10;
        mPaint.setTextSize(ArcWidth / 2);

        for (int i = 0; i < 10; i++) {
            float x = (float) ((mWidth / 2 - ArcWidth) * Math.cos(((72 + 36 * i) * Math.PI / 180f)));
            float y = (float) ((mWidth / 2 - ArcWidth) * Math.sin(((72 + 36 * i) * Math.PI / 180f)));
            RectF fButton = new RectF();
            fButton.left = mWidth / 2 - x - ArcWidth * 0.65f;
            fButton.right = mWidth / 2 - x + ArcWidth * 0.65f;
            fButton.top = mWidth / 2 - y - ArcWidth * 0.65f;
            fButton.bottom = mWidth / 2 - y + ArcWidth * 0.65f;
            mRectFButtons.add(fButton);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.argb(60, 255, 255, 255));
            canvas.drawCircle(fButton.centerX(), fButton.centerY(), ArcWidth * 0.65f, mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(fButton.centerX(), fButton.centerY(), ArcWidth * 0.45f, mPaint);


            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.GRAY);

            canvas.drawText(String.valueOf(i), fButton.centerX() - getFontlength(mPaint, String.valueOf(i)) / 2f,
                    fButton.centerY() + getFontHeight(mPaint, String.valueOf(i)) / 2f, mPaint);

        }
    }

    @Override
    protected void setTrueOrFalse(float x1, float y1, float x2, float y2, Canvas canvas, boolean isError) {

    }
}
