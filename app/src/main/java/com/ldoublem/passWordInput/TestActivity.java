package com.ldoublem.passWordInput;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.ldoublem.passWordInput.view.PwdGestureRoundView;
import com.ldoublem.passWordInput.view.PwdGestureView;
import com.ldoublem.passWordInput.view.PwdInputView;

public class TestActivity extends AppCompatActivity {

    PwdInputView mPwdInputView;
    PwdGestureRoundView mPwdGestureRoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("SET PASSWORD");
        mPwdInputView = (PwdInputView) findViewById(R.id.pwd_input_view);
        mPwdInputView.setFocusable(false);
        mPwdInputView.setPwdInputViewType(PwdInputView.ViewType.UNDERLINE);
        mPwdInputView.setRadiusBg(-1);//无边框
        mPwdInputView.setNumTextSize(70);
        mPwdInputView.setEnabled(false);
        mPwdInputView.setUnderLineColor(Color.rgb(104, 220, 202));
        mPwdInputView.setNumTextColor(Color.argb(150, 0, 0, 0));
        mPwdInputView.setBgColor(Color.rgb(204, 204, 204));
        mPwdInputView.setShadowPasswords(true);
        mPwdGestureRoundView = (PwdGestureRoundView) findViewById(R.id.pwd_view);
        mPwdGestureRoundView.setIsDrawLine(true);
        mPwdGestureRoundView.setColorNomalBg(Color.rgb(104, 220, 202));
        mPwdGestureRoundView.setColorNomalLineg(Color.argb(150,104, 220, 202));

        mPwdGestureRoundView.startWork(new PwdGestureView.GetPwd() {
            @Override
            public void onGetPwd(String pwd) {
                if (pwd.length() <= mPwdInputView.getMaxLength())
                    mPwdInputView.setText(pwd);
            }
        });


    }


}
