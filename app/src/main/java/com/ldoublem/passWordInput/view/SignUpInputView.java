package com.ldoublem.passWordInput.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ldoublem.passWordInput.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lumingmin on 16/7/4.
 */

public class SignUpInputView extends EditText {
    private Paint bgPaint;
    private Paint textPaint;
    private Paint titlePaint;


    private int bgColor = Color.BLACK;

    public void setBgPaintColor(int color) {
        bgColor = color;
    }

    private int textColor = Color.WHITE;

    public void setTextPaintColor(int color) {
        textColor = color;
    }

    private int titleColor = Color.WHITE;

    public void setTitlePaintColor(int color) {
        titleColor = color;
    }


    private float mWidth;
    private float mHeight;
    RectF bgRectF = new RectF();
    RectF nextRectF = new RectF();

    private float mPadding = 2f;
    String canvasText = "";
    float canvastextlength = 0f;
    float textWidth = 0f;
    int textcount = 0;
    Bitmap bitmap = null;
    Bitmap bitmapBefore = null;

    InputType MInputType = InputType.BUTTON;

    String mButtonText = "Sign Up";

    public void setmButtonText(String txt) {
        mButtonText = txt;
    }


    Integer[] setpIcon = new Integer[]{};//new int[]{R.mipmap.user, R.mipmap.email, R.mipmap.pwd};

    public enum InputType {
        EDITTEXT, PASSWORD, BUTTON
    }

    public enum VerifyType {
        NULL, PASSWORD, EMAIL, PHONE
    }


    float interpolatedTimeFirst = 0f;
    float interpolatedTimeMiddle = 0f;
    float interpolatedTimeMiddleTonext = 0f;

    float interpolatedTimeLast = 0f;

    public void setSetpIcon(Integer... setpicon) {
        setpIcon = new Integer[setpicon.length];

        for (int i = 0; i < setpicon.length; i++) {
            setpIcon[i] = setpicon[i];
        }
    }

    public int getSetpCount() {
        return setpIcon.length;
    }


    String[] setpName = new String[]{};//new String[]{"Name", "Email", "PassWord"};

    public void setSetpName(String... setpname) {
        setpName = new String[setpname.length];
        for (int i = 0; i < setpname.length; i++) {
            setpName[i] = setpname[i];
        }
    }

//    Integer[] PassWordStep = new Integer[]{};

    VerifyType[] mVerifyType = new VerifyType[]{};

    public void setVerifyTypeStep(VerifyType... verifyType) {
        mVerifyType = new VerifyType[verifyType.length];
        for (int i = 0; i < verifyType.length; i++) {
            mVerifyType[i] = verifyType[i];
        }
    }


    int mStep = 1;
    private boolean inputError = false;
    private int errorColor = Color.RED;

    public SignUpInputView(Context context) {
        this(context, null);
    }

    public SignUpInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignUpInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void drawGoButton(Canvas canvas) {

        if (MInputType == InputType.BUTTON) {
            setEnabled(true);
            return;
        }

        nextRectF.left = bgRectF.right - bgRectF.height();
        nextRectF.right = bgRectF.right;
        nextRectF.top = bgRectF.top;
        nextRectF.bottom = bgRectF.bottom;


        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.DKGRAY);//rgb(80, 80, 80));//GRAY
        canvas.drawCircle(nextRectF.centerX(), nextRectF.centerY(), nextRectF.width() / 4, bgPaint);


        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.go);
        float scale = bitmap.getHeight() / bitmap.getWidth();
        bitmap = setBitmapSize(bitmap, (int) (nextRectF.width() / 4), scale);
        canvas.drawBitmap(bitmap, nextRectF.centerX() - bitmap.getWidth() / 2,
                nextRectF.centerY() - bitmap.getHeight() / 2, bgPaint);


        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(bgColor);


        if (mStep - 2 < setpIcon.length) {
            float positionX = (bgRectF.width() - nextRectF.width()) * interpolatedTimeMiddle;
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), setpIcon[mStep - 2]);
            scale = bitmap.getHeight() / bitmap.getWidth();

            if (mStep - 1 < setpIcon.length) {
                bitmapBefore = BitmapFactory.decodeResource(getContext().getResources(), setpIcon[mStep - 1]);
                bitmapBefore = setBitmapSize(bitmapBefore,
                        (int) (nextRectF.width() * 0.3f*
                                (interpolatedTimeMiddle>0?interpolatedTimeMiddle:0.1f)), scale);

                bgPaint.setColor(bgColor);
                bgPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(bgRectF.left + nextRectF.width() / 2,
                        bgRectF.top + nextRectF.width() / 2,
                        (nextRectF.width() / 2-bgPaint.getStrokeWidth()*4f)*interpolatedTimeMiddle, bgPaint);
                canvas.drawBitmap(bitmapBefore, bgRectF.left + nextRectF.width() / 2 - bitmapBefore.getWidth() / 2,
                        bgRectF.top + nextRectF.width() / 2 - bitmapBefore.getHeight() / 2,
                        bgPaint);
                bgPaint.setColor(Color.WHITE);
                bgPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(bgRectF.left + nextRectF.width() / 2,
                        bgRectF.top + nextRectF.width() / 2,
                        (nextRectF.width() / 2-bgPaint.getStrokeWidth()*4f)*interpolatedTimeMiddle, bgPaint);

            }

            bitmap = setBitmapSize(bitmap, (int) (nextRectF.width() * 0.3f), scale);
            bitmap = setBitmapRotation(bitmap, (int) (405 * interpolatedTimeMiddle));
            bgPaint.setColor(bgColor);
            bgPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(bgRectF.left + nextRectF.width() / 2 + positionX,
                    bgRectF.top + nextRectF.width() / 2,
                    nextRectF.width() / 2-bgPaint.getStrokeWidth()*4f, bgPaint);
            canvas.drawBitmap(bitmap, bgRectF.left + nextRectF.width() / 2 - bitmap.getWidth() / 2 + positionX,
                    bgRectF.top + nextRectF.width() / 2 - bitmap.getHeight() / 2,
                    bgPaint);
            if (inputError)
                bgPaint.setColor(errorColor);
            else
                bgPaint.setColor(Color.WHITE);
            bgPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(bgRectF.left + nextRectF.width() / 2 + positionX,
                    bgRectF.top + nextRectF.width() / 2,
                    nextRectF.width() / 2-bgPaint.getStrokeWidth()*4f, bgPaint);


        } else {

            MInputType = InputType.BUTTON;
            setText("");

        }


    }

    private void drawBg(Canvas canvas) {

        if (setpIcon.length != setpName.length) {
            Toast.makeText(getContext(), "setpIcon length must equal with setpName", Toast.LENGTH_SHORT).show();
            return;
        }


        textWidth = getPaint().measureText(getText().toString().trim());
        float marLeft = 0f;
        if (!toNextStep) {

            float w = toNextStepRectF.width();
            float x = (w - (mWidth / 2 ) )/2f * interpolatedTimeMiddleTonext;
            bgRectF.left = toNextStepRectF.left+x;
            bgRectF.right = toNextStepRectF.right-x;
            bgRectF.top = mPadding;
            bgRectF.bottom = mHeight - mPadding;
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(bgColor);
            canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
            bgPaint.setStyle(Paint.Style.STROKE);

            if (inputError)
                bgPaint.setColor(errorColor);
            else
                bgPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);


        } else {
            if (mWidth - 2 * mPadding < textWidth) {
                marLeft = textWidth - mWidth;
                bgRectF.left = mPadding + marLeft;
                bgRectF.top = mPadding;
                bgRectF.right = mWidth - mPadding + marLeft;
                bgRectF.bottom = mHeight - mPadding;
                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setColor(bgColor);
                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
                bgPaint.setStyle(Paint.Style.STROKE);
                if (inputError)
                    bgPaint.setColor(errorColor);
                else
                    bgPaint.setColor(Color.WHITE);

                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
            } else if (mWidth / 2 >= textWidth) {

                bgRectF.left = mWidth / 2 - mWidth / 4;
                bgRectF.right = mWidth / 2 + mWidth / 4;
                float w = 0f;
                if (mStep == 1) {
                    w = (bgRectF.width() - bgRectF.height()) / 2 * (interpolatedTimeFirst);
                    bgRectF.left = mWidth / 2 - mWidth / 4 + w;
                    bgRectF.right = mWidth / 2 + mWidth / 4 - w;

                } else {//if (mStep == 2) {
                    bgRectF.left = mWidth / 2 - mWidth / 4 - textWidth / 2f;
                    bgRectF.right = mWidth / 2 + mWidth / 4 + textWidth / 2f;

                }

                bgRectF.top = mPadding;
                bgRectF.bottom = mHeight - mPadding;
                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setColor(bgColor);
                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
                bgPaint.setStyle(Paint.Style.STROKE);
                if (inputError)
                    bgPaint.setColor(errorColor);
                else
                    bgPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);


            } else {
                marLeft = 0;
                bgRectF.left = mPadding + marLeft;
                bgRectF.top = mPadding;
                bgRectF.right = mWidth - mPadding + marLeft;
                bgRectF.bottom = mHeight - mPadding;
                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setColor(bgColor);
                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
                bgPaint.setStyle(Paint.Style.STROKE);

                if (inputError)
                    bgPaint.setColor(errorColor);
                else
                    bgPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(bgRectF, bgRectF.height() / 2, bgRectF.height() / 2, bgPaint);
            }
        }
        if (MInputType == InputType.BUTTON) {
            bgPaint.setTextSize(getPaint().getTextSize());
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(textColor);

            if (mStep - 2 == setpIcon.length) {
                if (interpolatedTimeLast == 0) {
                    startLastAnim(250);
                }
                bgPaint.setAlpha((int) (255*interpolatedTimeLast));
                bgPaint.setTextSize(getPaint().getTextSize() * interpolatedTimeLast);
            }

            if (bgRectF.width() >= getFontlength(bgPaint, mButtonText)) {
                canvas.drawText(mButtonText, bgRectF.centerX() - getFontlength(bgPaint, mButtonText) / 2
                        , bgRectF.centerY() + getFontHeight(bgPaint, mButtonText) / 3
                        , bgPaint);
            } else {
                float tsize = getPaint().getTextSize() * bgRectF.width() / getFontlength(bgPaint, mButtonText) * 0.8f;
                bgPaint.setTextSize(tsize);
                canvas.drawText(mButtonText, bgRectF.centerX() - getFontlength(bgPaint, mButtonText) / 2
                        , bgRectF.centerY() + getFontHeight(bgPaint, mButtonText) / 3
                        , bgPaint);
            }


        }


    }


    private void drawTitleAndText(Canvas canvas) {

        if (MInputType == InputType.BUTTON) {
            return;
        }
        canvastextlength = bgRectF.width() - bgRectF.height() * 2 - 10;
        textcount = getText().toString().trim().length();
        titlePaint.setTextSize(getPaint().getTextSize() * 0.5f);
        titlePaint.setColor(titleColor);

        textPaint.setColor(textColor);
        if (mStep - 2 < setpIcon.length) {
            canvas.drawText(setpName[mStep - 2], bgRectF.centerX() - canvastextlength / 2,
                    bgRectF.centerY() - getFontHeight(titlePaint, setpName[mStep - 2]), titlePaint);
            textPaint.setTextSize(getPaint().getTextSize());
        }
        if (canvastextlength < textWidth) {
            int count = (int) (textcount * canvastextlength / textWidth);
            canvasText = getText().toString().trim().substring(textcount - count);
            if (MInputType == InputType.EDITTEXT) {
                canvas.drawText(canvasText, bgRectF.centerX() - getFontlength(textPaint, canvasText) / 2f,
                        bgRectF.centerY() + getFontHeight(textPaint, canvasText) / 2, textPaint);
            } else {


                int maxPwdlength = (int) (canvastextlength / getFontHeight(textPaint, " * ")) / 2;

                String pwd = "";
                for (int i = 0; i < (canvasText.length() > maxPwdlength ? maxPwdlength : canvasText.length()); i++) {
                    pwd = pwd + " * ";
                }

                canvas.drawText(pwd, bgRectF.centerX() - getFontlength(textPaint, pwd) / 2f,
                        bgRectF.centerY() + getFontHeight(textPaint, pwd) * 1.5f, textPaint);


            }
        } else {
            canvasText = getText().toString().trim();
            if (MInputType == InputType.EDITTEXT) {
                canvas.drawText(canvasText,
                        bgRectF.centerX() - getFontlength(textPaint, canvasText) / 2F
                        , bgRectF.centerY() + getFontHeight(textPaint, canvasText) / 2, textPaint);
            } else {

                String pwd = "";
                int maxPwdlength = (int) (canvastextlength / getFontHeight(textPaint, " * ")) / 2;

                for (int i = 0; i < (canvasText.length() > maxPwdlength ? maxPwdlength : canvasText.length()); i++) {
                    pwd = pwd + " * ";
                }

                canvas.drawText(pwd, bgRectF.centerX() - getFontlength(textPaint, pwd) / 2f,
                        bgRectF.centerY() + getFontHeight(textPaint, pwd) * 1.5f, textPaint);

            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawTitleAndText(canvas);
        drawGoButton(canvas);


    }


    protected void initPaint() {

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        bgPaint = new Paint();
        setGravity(Gravity.CENTER_VERTICAL);
        setTextColor(Color.TRANSPARENT);
        setSingleLine();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStrokeWidth(2);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setColor(Color.WHITE);


    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        inputError = false;
        invalidate();

    }

    private Bitmap setBitmapSize(Bitmap b, int w, float s) {
        b = Bitmap.createScaledBitmap(b, w, (int) (w * s), true);
        return b;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (MInputType == InputType.BUTTON) {
                    if (mStep - 2 < setpIcon.length) {
                        if (mGetStepAndText != null) {
                            mGetStepAndText.GetInfo(mStep - 2, mButtonText, mButtonText);
                        }
                        startFirstAnim(350);
                    } else {
                        if (mGetStepAndText != null) {
                            mGetStepAndText.GetInfo(mStep - 2, mButtonText, mButtonText);
                        }
                        return false;

                    }
                } else {
                    if (nextRectF.contains(event.getX(), event.getY())) {
                        if (mStep - 2 < setpIcon.length) {
                            if (!verifyText(getText().toString().trim())) {
//                                inputError = true;
//                                postInvalidate();
                                startErrorAnim();
                                return false;
                            } else {
                                startMiddleAnim(500);

                            }
                            if (mGetStepAndText != null) {
                                mGetStepAndText.GetInfo(mStep - 2, setpName[mStep - 2], getText().toString().trim());
                            }

                            if (mStep - 2 == setpIcon.length - 1) {
                                InputMethodManager imm = (InputMethodManager)
                                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindowToken(), 0); //强制隐藏键盘
                                return false;

                            }
                        }
                    }


                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
            default:
                break;
        }

        return super.onTouchEvent(event);

    }


    private ValueAnimator valueAnimator;

    public void startFirstAnim(int time) {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                return;
            }
        }
        startFirstViewAnim(0f, 1f, time);

    }


    private ValueAnimator startFirstViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                interpolatedTimeFirst = (float) valueAnimator.getAnimatedValue();


                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {


                                      @Override
                                      public void onAnimationEnd(Animator animation) {
                                          super.onAnimationEnd(animation);

                                          MInputType = InputType.EDITTEXT;
                                          if (mStep - 1 < mVerifyType.length) {
                                              for (int i = 0; i < mVerifyType.length; i++) {
                                                  if (mVerifyType[mStep - 1] == VerifyType.PASSWORD) {
                                                      MInputType = InputType.PASSWORD;
                                                  }
                                              }
                                          }

                                          mStep = mStep + 1;


                                      }

                                      @Override
                                      public void onAnimationStart(Animator animation) {
                                          super.onAnimationStart(animation);

                                      }

                                      @Override
                                      public void onAnimationRepeat(Animator animation) {
                                          super.onAnimationRepeat(animation);

                                      }

                                  }

        );
        if (!valueAnimator.isRunning())

        {
            valueAnimator.start();

        }

        return valueAnimator;
    }


    public void startMiddleAnim(int time) {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                return;
            }
        }
        startMiddleViewAnim(0f, 1f, time);

    }

    private boolean toNextStep = true;

    private RectF toNextStepRectF;

    public void startMiddleToNextAnim(int time) {
//        if (valueAnimator != null) {
//            if (valueAnimator.isRunning()) {
//                return;
//            }
//        }
        setText("");
        toNextStepRectF=new RectF();
        toNextStepRectF.top=bgRectF.top;
        toNextStepRectF.bottom=bgRectF.bottom;
        toNextStepRectF.left=bgRectF.left;
        toNextStepRectF.right=bgRectF.right;
        startMiddleToNextViewAnim(0f, 1f, time);

    }


    public void startLastAnim(int time) {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                return;
            }
        }
        startLastViewAnim(0f, 1f, time);

    }


    private ValueAnimator startMiddleViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                interpolatedTimeMiddle = (float) valueAnimator.getAnimatedValue();


                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                MInputType = InputType.EDITTEXT;
                if (mStep - 1 < mVerifyType.length) {
                    for (int i = 0; i < mVerifyType.length; i++) {
                        if (mVerifyType[mStep - 1] == VerifyType.PASSWORD) {
                            MInputType = InputType.PASSWORD;
                        }
                    }
                }
                startMiddleToNextAnim(200);



            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

            }

        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();

        }

        return valueAnimator;
    }


    private ValueAnimator startMiddleToNextViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                interpolatedTimeMiddleTonext = (float) valueAnimator.getAnimatedValue();


                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                MInputType = InputType.EDITTEXT;
//                if (mStep - 1 < mVerifyType.length) {
//                    for (int i = 0; i < mVerifyType.length; i++) {
//                        if (mVerifyType[mStep - 1] == VerifyType.PASSWORD) {
//                            MInputType = InputType.PASSWORD;
//                        }
//                    }
//                }
                toNextStep = true;
                if (mStep - 2 < setpIcon.length) {
                    mStep = mStep + 1;
                    interpolatedTimeMiddle = 0f;
                    setText("");

                }


            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                toNextStep = false;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

            }

        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();

        }

        return valueAnimator;
    }


    private ValueAnimator startLastViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                interpolatedTimeLast = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

            }

        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();

        }

        return valueAnimator;
    }


    public void setAgain() {
        interpolatedTimeFirst = 0f;
        interpolatedTimeMiddle = 0f;
        interpolatedTimeLast = 0f;
        MInputType = InputType.BUTTON;
        mStep = 1;
        setText("");
    }

    GetStepAndText mGetStepAndText = null;

    public void setOnGetStepInfo(GetStepAndText getStepAndText) {
        this.mGetStepAndText = getStepAndText;
        invalidate();
    }

    public interface GetStepAndText {
        void GetInfo(int step, String stepName, String text);

    }

    private TranslateAnimation errorAnim = null;

    private void startErrorAnim() {
        if (errorAnim == null) {
            errorAnim = new TranslateAnimation(-3,
                    3, 0, 0);
            errorAnim.setInterpolator(new CycleInterpolator(2f));
            errorAnim.setDuration(400);
        }
        clearAnimation();
        startAnimation(errorAnim);


    }


    private Bitmap setBitmapRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    private boolean verifyText(String text) {


        if (text.equals("")) {
            return false;
        }
        if (mStep - 2 < mVerifyType.length) {
            if (mVerifyType[mStep - 2] == VerifyType.PASSWORD) {
                if (text.length() < 6) {
                    return false;
                }

            } else if (mVerifyType[mStep - 2] == VerifyType.EMAIL) {

                return isEmail(text);
            } else if (mVerifyType[mStep - 2] == VerifyType.PHONE) {

                return isMobileNO(text);
            }
        }


        return true;
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }
}
