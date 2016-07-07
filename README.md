# PassWordInput
a sign up view with animation for Android


![效果.gif](https://github.com/ldoublem/PassWordInput/blob/master/screen/signup.gif)



A custom Password Input with animation for Android

九宫格手势，圆环手势密码和自定义密码样式的输入框

![输入框样式.jpg](https://github.com/ldoublem/PassWordInput/blob/master/screen/inputtype.jpg)
![效果.png](https://github.com/ldoublem/PassWordInput/blob/master/screen/sereen1.png)
![效果.gif](https://github.com/ldoublem/PassWordInput/blob/master/screen/%E6%95%88%E6%9E%9C.gif)


## PwdInputView Usage 使用
```java
       mPwdInputView.setShadowPasswords(ture);//是否显示密码
       //mPwdInputView.setShadowPasswords(true, R.mipmap.icon_pwd);//显示图片
       //mPwdInputView.setShadowPasswords(show, "密");//显示字符
       mPwdInputView.setPwdInputViewType(PwdInputView.ViewType.DEFAULT);//默认圆环
       //mPwdInputView.setPwdInputViewType(PwdInputView.ViewType.UNDERLINE);//下划线
       //mPwdInputView.setPwdInputViewType(PwdInputView.ViewType.BIASLINE);//斜杠
       mPwdInputView.setRadiusBg(0);//圆角半径
```
```java
       mPwdGestureView.setIsDrawLine(true);//显示路径
       mPwdGestureView.setOldPwd("012543");//设置原始密码
         mPwdGestureView.startWork(new PwdGestureView.GetPwd() {
            @Override
            public void onGetPwd(String pwd) {
                //获得手势密码
            }
        });
```
## SignUpInputView Usage 使用
```
        mSignUpInputView.setSetpIcon(R.mipmap.user, R.mipmap.email, R.mipmap.pwd);//set icon
        mSignUpInputView.setSetpName("Name", "Email", "PassWord");//set title
        mSignUpInputView.setmButtonText("Sign up");//button text
        mSignUpInputView.setVerifyTypeStep(SignUpInputView.VerifyType.NULL,
                SignUpInputView.VerifyType.EMAIL,
                SignUpInputView.VerifyType.PASSWORD);
         mSignUpInputView.setOnGetStepInfo(new SignUpInputView.GetStepAndText() {
            @Override
            public void GetInfo(int step, String stepName, String text) {
            
            }
        });
```
you can change the style
```
        mSignUpInputView.setBgPaintColor(Color.BLACK);
        mSignUpInputView.setTextPaintColor(Color.WHITE);
        mSignUpInputView.setTitlePaintColor(Color.GRAY);
```

## About me

An android developer in Hangzhou.

Welcome to [offer me](mailto:1227102260@qq.com). :smiley:
License
=======

    The MIT License (MIT)

	Copyright (c) 2016 ldoublem

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.







