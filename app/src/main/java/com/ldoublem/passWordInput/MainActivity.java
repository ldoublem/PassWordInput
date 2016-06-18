package com.ldoublem.passWordInput;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.passWordInput.view.PwdGestureView;
import com.ldoublem.passWordInput.view.PwdInputView;

public class MainActivity extends AppCompatActivity {
    PwdGestureView mPwdGestureView;
    PwdInputView mPwdInputView,mPwdInputView2,mPwdInputView3;
    TextView tv_pwd, tv_input_pwd;
    Switch mSw_line, mSw_show;
    RadioButton rb_icon, rb_text, rb_pwd;
    RadioGroup rg_RadioGroup;
    CheckBox cb_set_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPwdGestureView = (PwdGestureView) findViewById(R.id.pwd_view);
        mPwdInputView = (PwdInputView) findViewById(R.id.pwd_input_view);
        mPwdInputView2   = (PwdInputView) findViewById(R.id.pwd_input_view2);
        mPwdInputView3  = (PwdInputView) findViewById(R.id.pwd_input_view3);
        tv_pwd = (TextView) findViewById(R.id.tv_pwd);
        tv_input_pwd = (TextView) findViewById(R.id.tv_input_pwd);
        mSw_line = (Switch) findViewById(R.id.sw_line);
        mSw_show = (Switch) findViewById(R.id.sw_show);
        rb_icon = (RadioButton) findViewById(R.id.rb_icon);
        rb_text = (RadioButton) findViewById(R.id.rb_text);
        rb_pwd = (RadioButton) findViewById(R.id.rb_pwd);
        rg_RadioGroup = (RadioGroup) findViewById(R.id.rg_RadioGroup);
        cb_set_pwd = (CheckBox) findViewById(R.id.cb_set_pwd);

//        mPwdGestureView.setOldPwd("012543");
        mPwdGestureView.setIsDrawLine(mSw_line.isChecked());

        mSw_line.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPwdGestureView.setIsDrawLine(isChecked);

            }
        });
        mSw_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rb_icon.setEnabled(isChecked);
                rb_text.setEnabled(isChecked);
                rb_pwd.setEnabled(isChecked);
                showPwd(isChecked);
            }
        });
        rb_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPwd(mSw_show.isChecked());
            }
        });
        rb_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPwd(mSw_show.isChecked());
            }
        });
        rb_icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPwd(mSw_show.isChecked());
            }
        });

        mPwdGestureView.startWork(new PwdGestureView.GetPwd() {
            @Override
            public void onGetPwd(String pwd) {
                tv_pwd.setText(pwd);
            }
        });
        mPwdInputView.setShadowPasswords(mSw_show.isChecked());
        mPwdInputView.setPwdInputViewType(PwdInputView.ViewType.DEFAULT);
//        mPwdInputView.setRadiusBg(10);
        mPwdInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_input_pwd.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPwdInputView2.setShadowPasswords(mSw_show.isChecked());
        mPwdInputView2.setPwdInputViewType(PwdInputView.ViewType.UNDERLINE);
        mPwdInputView2.setRadiusBg(0);

        mPwdInputView3.setShadowPasswords(mSw_show.isChecked());
        mPwdInputView3.setPwdInputViewType(PwdInputView.ViewType.BIASLINE);
        mPwdInputView3.setRadiusBg(20);




        cb_set_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mPwdGestureView.setOldPwd("012543");
                }
                else
                {
                    mPwdGestureView.setOldPwd(null);
                }
            }
        });



    }

    private void showPwd(boolean show) {

        if (show) {
            if (rb_icon.isChecked()) {
                mPwdInputView.setShadowPasswords(show, R.mipmap.icon_pwd);
                mPwdInputView2.setShadowPasswords(show, R.mipmap.icon_pwd);
            } else if (rb_text.isChecked()) {
                mPwdInputView.setShadowPasswords(show, "密");
                mPwdInputView2.setShadowPasswords(show, "密");

            } else if (rb_pwd.isChecked()) {
                mPwdInputView.setShadowPasswords(show);
                mPwdInputView2.setShadowPasswords(show);

            }
        } else {
            mPwdInputView.setShadowPasswords(show);
            mPwdInputView2.setShadowPasswords(show);


        }


    }


}
