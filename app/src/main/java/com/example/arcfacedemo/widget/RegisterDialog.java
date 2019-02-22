package com.example.arcfacedemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.example.arcfacedemo.R;


/**
 * 取消或者确认类型的Dialog
 * <p>
 * 作者： 周旭 on 2017/5/27/0027.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class RegisterDialog extends Dialog {

    private EditText nameEdt;

    public RegisterDialog(Context context) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.dialog_register);
        //点击外部可消失
        setCancelable(false);

        nameEdt = findViewById(R.id.person_name_edt);

        findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                //取消
                cancel();
            }
        });

        findViewById(R.id.ok_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                dismiss();
                register();
            }
        });
    }

    public String getName() {
       return nameEdt.getText().toString().trim();
    }

    //注册
    public void register() {
    }
}
