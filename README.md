# PassWordInput
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

## PwdInputView Usage 使用
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
## About me

An android developer in Hangzhou.

Welcome to [offer me](mailto:1227102260@qq.com). :smiley:

