package com.ldoublem.passWordInput;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ldoublem.passWordInput.view.PwdGestureRoundView;
import com.ldoublem.passWordInput.view.PwdGestureView;
import com.ldoublem.passWordInput.view.PwdInputView;
import com.ldoublem.passWordInput.view.SignUpInputView;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    SignUpInputView mSignUpInputView;
    Button btn_Again;
    TextView tv_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_signup);
        setTitle("Sign up");
        mSignUpInputView = (SignUpInputView) findViewById(R.id.sign_btn);
        btn_Again = (Button) findViewById(R.id.btn_again);
        tv_info = (TextView) findViewById(R.id.tv_info);
        mSignUpInputView.setBgPaintColor(Color.BLACK);
        mSignUpInputView.setTextPaintColor(Color.WHITE);
        mSignUpInputView.setTitlePaintColor(Color.GRAY);
        mSignUpInputView.setSetpIcon(R.mipmap.user, R.mipmap.email, R.mipmap.pwd);
        mSignUpInputView.setSetpName("Name", "Email", "PassWord");
        mSignUpInputView.setmButtonText("Sign up");
        mSignUpInputView.setVerifyTypeStep(SignUpInputView.VerifyType.NULL,
                SignUpInputView.VerifyType.EMAIL,
                SignUpInputView.VerifyType.PASSWORD);
        btn_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpInputView.setmButtonText("Sign up");
                mSignUpInputView.setAgain();
                tv_info.setText("");
            }
        });
        mSignUpInputView.setOnGetStepInfo(new SignUpInputView.GetStepAndText() {
            @Override
            public void GetInfo(int step, String stepName, String text) {

                String info = tv_info.getText().toString();


                if (step == mSignUpInputView.getSetpCount() - 1) {
                    info = info + "\n" + step + ":" + stepName + ":" + text;
                    mSignUpInputView.setmButtonText("Welcome");

                } else if (step == -1) {
                    info = info + "\nonClick " + stepName;
                } else if (step == mSignUpInputView.getSetpCount()) {
                    info = info + "\nonClick " + stepName;

                } else {
                    info = info + "\n" + step + ":" + stepName + ":" + text;
                }

                tv_info.setText(info);
            }
        });

    }


}
