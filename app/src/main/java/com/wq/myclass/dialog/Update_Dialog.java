package com.wq.myclass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wq.myclass.R;

/**
 * Created by wq107 on 2018/1/19.
 */

public class Update_Dialog extends Dialog {
    private TextView restinfotitle;
    private EditText resetinfocontent;
    private Button btnyes;
    private Button btnno;
    private String yesStr, noStr, titleStr, lengthStr;
    private onNoOnclickListener noOnclickListener;
    private onYesOnclickListener yesOnclickListener;

    public Update_Dialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        setCanceledOnTouchOutside(false);
        initViews();
        initData();
        setListeners();
    }

    private void initViews() {
        restinfotitle = findViewById(R.id.resetinfo_title);
        resetinfocontent = findViewById(R.id.resetinfo_content);
        btnyes = findViewById(R.id.resetinfo_ok);
        btnno = findViewById(R.id.resetinfo_cancel);
    }

    private void initData() {
        if (yesStr != null) {
            btnyes.setText(yesStr);
        }
        if (noStr != null) {
            btnno.setText(noStr);
        }
        if (titleStr != null) {
            restinfotitle.setText(titleStr);
        }
        if (lengthStr.equals("email")) {
            resetinfocontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }
        if (lengthStr.equals("phone")) {
            resetinfocontent.setInputType(InputType.TYPE_CLASS_PHONE);
            resetinfocontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }
        if (lengthStr.equals("pass")) {
            resetinfocontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        }
    }

    public String getContent() {
        String s = resetinfocontent.getText().toString();
        return s;
    }

    private void setListeners() {
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void seted(String ob) {
        lengthStr = ob;
    }

    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public void setYesOnclickListener(String str, onYesOnclickListener donYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = donYesOnclickListener;
    }

    public void setNoOnclickListener(String str, onNoOnclickListener donNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = donNoOnclickListener;
    }
}
