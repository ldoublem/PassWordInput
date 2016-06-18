package com.ldoublem.passWordInput.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ldoublem.passWordInput.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lumingmin on 16/6/16.
 */

public class PwdGestureView extends View {


    private Paint mPaint;
    private Paint mPaintLine;
    private Paint mPaintOrientation;
    private int mWidth;
    private int mHigh;

    private float buttonWidth;
    private float buttonMarginTop;
    private float buttonMarginLeft;
    private float buttonPadding;
    private int ArcWidth;
    private float selectButtonCenterR;
    List<RectF> mRectFButtons = new ArrayList<RectF>();
    Map<String, RectF> mRectFButtons_select = new HashMap<String, RectF>();
    List<String> mPwd = new ArrayList<String>();

    int colorNomalBg = Color.rgb(86, 171, 228);
    int colorNomalLine = Color.argb(200, 86, 171, 228);


    int colorErrorBg = Color.rgb(216, 64, 66);
    int colorErrorLine = Color.argb(200, 216, 64, 66);
    boolean isError = false;


    float mlastX = -1;
    float mlastY = -1;
    boolean isDrawLine = true;


    private float triangleLine;
    Bitmap bitmapTriangle = null;
    private String oldPwd = null;

    public PwdGestureView(Context context) {

        this(context, null);
    }

    public PwdGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void drawButtons(Canvas canvas) {

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
        for (int i = 0; i < 3; i++)//行
        {

            for (int j = 0; j < 3; j++)//列
            {
                RectF fButton = new RectF();
                fButton.left = j * buttonWidth + buttonMarginLeft + buttonPadding;
                fButton.right = (j + 1) * buttonWidth + buttonMarginLeft - buttonPadding;
                fButton.top = i * buttonWidth + buttonMarginTop + buttonPadding;
                fButton.bottom = (i + 1) * buttonWidth + buttonMarginTop - buttonPadding;
                canvas.drawCircle(fButton.centerX(), fButton.centerY(), ArcWidth, mPaint);
                mRectFButtons.add(fButton);

            }


        }
    }

    private void drawCircleAndLines(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mPwd.size(); i++) {
            RectF rf = mRectFButtons_select.get(mPwd.get(i));
            canvas.drawCircle(rf.centerX(), rf.centerY(), selectButtonCenterR, mPaint);
            if (isDrawLine) {

                if (i > 0 && i < mPwd.size()) {


                    float x1 = mRectFButtons_select.get(mPwd.get(i - 1)).centerX();
                    float y1 = mRectFButtons_select.get(mPwd.get(i - 1)).centerY();
                    float x2 = mRectFButtons_select.get(mPwd.get(i)).centerX();
                    float y2 = mRectFButtons_select.get(mPwd.get(i)).centerY();
                    drawlinebyAngle(x1, y1, x2, y2, ArcWidth, canvas, false);


                }
                if (i == mPwd.size() - 1 && i < mRectFButtons.size() - 1 && !isError) {
                    if (mlastX >= 0 && mlastY >= 0) {
                        float x1 = mRectFButtons_select.get(mPwd.get(i)).centerX();
                        float y1 = mRectFButtons_select.get(mPwd.get(i)).centerY();
                        drawlinebyAngle(x1, y1, mlastX, mlastY, ArcWidth, canvas, true);
                    }
                }
            }

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButtons(canvas);
        drawCircleAndLines(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                isError = false;
                mRectFButtons_select.clear();
                mPwd.clear();
                addButtonSelect(event.getX(), event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                addButtonSelect(event.getX(), event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                callGetPwd();
                postInvalidate();
                return false;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHigh = getMeasuredHeight();
        buttonPadding = dip2px(5);
        ArcWidth = dip2px(30);
        selectButtonCenterR = dip2px(8);

        triangleLine = dip2px(10);

        if (mWidth > mHigh) {
            buttonWidth = mHigh / 3.0f;
            buttonMarginLeft = Math.abs((mHigh - mWidth) / 2.0f);
            buttonMarginTop = 0f;
        } else {
            buttonWidth = mWidth / 3.0f;
            buttonMarginTop = Math.abs((mHigh - mWidth) / 2.0f);
            buttonMarginLeft = 0f;
        }


    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(colorNomalBg);
        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(colorNomalLine);
        mPaintLine.setStrokeWidth(dip2px(1.5f));


        mPaintOrientation = new Paint();
        mPaintOrientation.setAntiAlias(true);
        mPaintOrientation.setStyle(Paint.Style.STROKE);
        mPaintOrientation.setColor(colorErrorBg);
        mPaintOrientation.setTextSize(dip2px(15));


    }


    private void addButtonSelect(float x, float y) {
        mlastX = x;
        mlastY = y;

        for (int i = 0; i < mRectFButtons.size(); i++) {
            if (mRectFButtons.get(i).contains(x, y)) {
                if (getDistance(mRectFButtons.get(i).centerX(), mRectFButtons.get(i).centerY(), x, y) <= ArcWidth) {
                    if (!mRectFButtons_select.containsKey(String.valueOf(i))) {
                        mRectFButtons_select.put(String.valueOf(i), mRectFButtons.get(i));
                        mPwd.add(String.valueOf(i));
//                        callGetPwd();
                        return;
                    }


                }

            }

        }


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


    public double getDistance(float x, float y, float x1, float y1) {
        double _x = Math.abs(x - x1);
        double _y = Math.abs(y - y1);
        return Math.sqrt(_x * _x + _y * _y);
    }

    private GetPwd mGetPwd;

    public void setIsDrawLine(boolean isDrawLine) {
        this.isDrawLine = isDrawLine;
        postInvalidate();

    }

    public void startWork(GetPwd mGetPwd) {
        this.mGetPwd = mGetPwd;
    }


    public interface GetPwd {
        void onGetPwd(String pwd);
    }


    private void callGetPwd() {
        if (mGetPwd != null && mPwd.size() > 0) {
            String pwd = "";
            for (String s : mPwd) {
                pwd += s;
            }
            if (oldPwd != null) {
                if (oldPwd.equals(pwd)) {
                    mGetPwd.onGetPwd("true");
                    mRectFButtons_select.clear();
                    mPwd.clear();
                } else {
                    isError = true;
                    postInvalidate();
                    mGetPwd.onGetPwd("false");

                }
            } else {
                mGetPwd.onGetPwd(pwd);
                mRectFButtons_select.clear();
                mPwd.clear();
                postInvalidate();
            }
        }


    }


    private void drawlinebyAngle(float x1, float y1, float x2, float y2, float radius, Canvas canvas, boolean isLast) {


        int angle = 0;
        float x = 0f;
        float y = 0f;
        double z = 0f;
        float xPoint1 = 0f;
        float yPoint1 = 0f;
        float xPoint2 = 0f;
        float yPoint2 = 0f;
        x = Math.abs(x1 - x2);
        y = Math.abs(y1 - y2);
        z = Math.sqrt(x * x + y * y);
        if (z < radius)
            return;

        angle = Math.round((float) (Math.asin(y / z) / Math.PI * 180));

        if (x1 == x2 && y1 < y2) {
            xPoint1 = x1;
            yPoint1 = y1 + radius;
            xPoint2 = x2;
            yPoint2 = y2 - radius;
        } else if (x1 == x2 && y1 > y2) {
            xPoint1 = x1;
            yPoint1 = y1 - radius;
            xPoint2 = x2;
            yPoint2 = y2 + radius;
        } else if (x1 < x2 && y1 == y2) {
            xPoint1 = x1 + radius;
            yPoint1 = y1;
            xPoint2 = x2 - radius;
            yPoint2 = y2;
        } else if (x1 > x2 && y1 == y2) {
            xPoint1 = x1 - radius;
            yPoint1 = y1;
            xPoint2 = x2 + radius;
            yPoint2 = y2;


        } else if (x1 < x2 && y1 < y2) {


            xPoint1 = (float) (x1 + (radius) * Math.cos(angle * Math.PI / 180));
            yPoint1 = (float) (y1 + (radius) * Math.sin(angle * Math.PI / 180));
            xPoint2 = (float) (x2 - (radius) * Math.cos(angle * Math.PI / 180));
            yPoint2 = (float) (y2 - (radius) * Math.sin(angle * Math.PI / 180));


        } else if (x1 < x2 && y1 > y2) {


            xPoint1 = (float) (x1 + (radius) * Math.cos(angle * Math.PI / 180));
            yPoint1 = (float) (y1 - (radius) * Math.sin(angle * Math.PI / 180));
            xPoint2 = (float) (x2 - (radius) * Math.cos(angle * Math.PI / 180));
            yPoint2 = (float) (y2 + (radius) * Math.sin(angle * Math.PI / 180));
        } else if (x1 > x2 && y1 < y2) {


            xPoint1 = (float) (x1 - (radius) * Math.cos(angle * Math.PI / 180));
            yPoint1 = (float) (y1 + (radius) * Math.sin(angle * Math.PI / 180));
            xPoint2 = (float) (x2 + (radius) * Math.cos(angle * Math.PI / 180));
            yPoint2 = (float) (y2 - (radius) * Math.sin(angle * Math.PI / 180));
        } else if (x1 > x2 && y1 > y2) {

            xPoint1 = (float) (x1 - (radius) * Math.cos(angle * Math.PI / 180));
            yPoint1 = (float) (y1 - (radius) * Math.sin(angle * Math.PI / 180));
            xPoint2 = (float) (x2 + (radius) * Math.cos(angle * Math.PI / 180));
            yPoint2 = (float) (y2 + (radius) * Math.sin(angle * Math.PI / 180));
        }


        if (isError) {
            Path p = new Path();
            p.moveTo(x1, y1);
            p.lineTo(x2, y2);
            canvas.drawTextOnPath("☞", p, getFontHeight(mPaintOrientation, "☞") *3/2,
                    getFontHeight(mPaintOrientation, "☞") / 2,
                    mPaintOrientation);
        }


        if (isLast)
            canvas.drawLine(xPoint1,
                    yPoint1,
                    x2,
                    y2,
                    mPaintLine


            );
        else
            canvas.drawLine(xPoint1,
                    yPoint1,
                    xPoint2,
                    yPoint2,
                    mPaintLine


            );
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

}
