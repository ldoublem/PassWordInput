package com.ldoublem.passWordInput.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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

    private int mWidth;
    private int mHigh;

    private float buttonWidth;
    private float buttonMarginTop;
    private float buttonMarginLeft;
    private float buttonPadding;
    private int ArcWidth;
    List<RectF> mRectFButtons = new ArrayList<RectF>();


    Map<String, RectF> mRectFButtons_select = new HashMap<String, RectF>();

    List<String> mPwd = new ArrayList<String>();


    float mlastX = -1;
    float mlastY = -1;
    boolean isDrawLine = true;

    public PwdGestureView(Context context) {

        this(context, null);
    }

    public PwdGestureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PwdGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mPwd.size(); i++) {
            RectF rf = mRectFButtons_select.get(mPwd.get(i));
            canvas.drawCircle(rf.centerX(), rf.centerY(), dip2px(5), mPaint);

//            isDrawLine=false;
            if (isDrawLine) {

                if (i > 0 && i < mPwd.size()) {
                    canvas.drawLine(mRectFButtons_select.get(mPwd.get(i - 1)).centerX(),
                            mRectFButtons_select.get(mPwd.get(i - 1)).centerY(),
                            mRectFButtons_select.get(mPwd.get(i)).centerX(),
                            mRectFButtons_select.get(mPwd.get(i)).centerY(),
                            mPaintLine
                    );
                }
                if (i == mPwd.size() - 1 && i < mRectFButtons.size() - 1) {
                    if (mlastX >= 0 && mlastY >= 0) {
                        canvas.drawLine(
                                mRectFButtons_select.get(mPwd.get(i)).centerX(),
                                mRectFButtons_select.get(mPwd.get(i)).centerY(),
                                mlastX, mlastY,
                                mPaintLine);
                    }
                }
            }

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                addButtonSelect(event.getX(), event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                addButtonSelect(event.getX(), event.getY());
                postInvalidate();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                callGetPwd();
                mRectFButtons_select.clear();
                mRectFButtons.clear();
                mPwd.clear();
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
        mPaint.setColor(Color.rgb(86,171,228));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.argb(200,86,171,228));
        mPaintLine.setStrokeWidth(dip2px(2));

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
                        callGetPwd();
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
        return paint.measureText(str);
    }

    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    public double getDistance(float x, float y, float x1, float y1) {
        double _x = Math.abs(x - x1);
        double _y = Math.abs(y - y1);
        return Math.sqrt(_x * _x + _y * _y);
    }

    private GetPwd mGetPwd;

    public void setIsDrawLine(boolean isDrawLine)
    {
        this.isDrawLine = isDrawLine;
        postInvalidate();

    }

    public void startWork(GetPwd mGetPwd) {
        this.mGetPwd = mGetPwd;
    }


    public interface GetPwd {
        void onGetPwd(String pwd);
    }


    private void callGetPwd()
    {
        if (mGetPwd != null&&mPwd.size()>0) {
            String pwd = "";
            for (String s : mPwd) {
                pwd += s;
            }
            mGetPwd.onGetPwd(pwd);

        }
    }


}
