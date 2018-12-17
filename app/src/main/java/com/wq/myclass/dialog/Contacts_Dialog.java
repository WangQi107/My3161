package com.wq.myclass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wq.myclass.R;
import com.wq.myclass.transform.XCRoundImageView;


public class Contacts_Dialog extends Dialog {
    private XCRoundImageView dialogheader;
    private TextView dialogname;
    private TextView dialogtel;
    private ImageButton btnphone;
    private ImageButton btnmessage;

    private String sex=null;
    private String name=null;
    private String phone=null;

    private OnPhoneOnclickListener phoneclik;
    private OnMessageOnclickListener messageclik;

    public Contacts_Dialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_dialog);
        setCanceledOnTouchOutside(false);
        initViews();
        initDatas();
        initEvent();
    }

    private void initViews() {
        dialogheader = findViewById(R.id.dialog_header);
        dialogname = findViewById(R.id.dialog_name);
        dialogtel = findViewById(R.id.dialog_tel);
        btnphone = findViewById(R.id.btn_phone);
        btnmessage = findViewById(R.id.btn_message);
    }

    private void initDatas() {
        if (sex.equals("女")&&sex!=null) {
            dialogheader.setImageResource(R.mipmap.ic_header_girl);
        }
        if (name.equals("刘艳") || name.equals("刘士") || name.equals("刘爽")) {
            dialogheader.setImageResource(R.mipmap.ic_header_teacher);
        }
        if (name != null) {
            dialogname.setText(name);
        }
        if (phone != null) {
            dialogtel.setText(phone);
        }
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private void initEvent() {
        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneclik != null) {
                    phoneclik.OnPhoneClik();
                }
            }
        });
        btnmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageclik != null) {
                    messageclik.OnMessageClik();
                }
            }
        });
    }

    public interface OnPhoneOnclickListener {
        public void OnPhoneClik();
    }

    public interface OnMessageOnclickListener {
        public void OnMessageClik();
    }

    public void setPhoneClikListeners(OnPhoneOnclickListener OnPhoneOnclickListener) {
        this.phoneclik = OnPhoneOnclickListener;
    }

    public void setMessageClikListeners(OnMessageOnclickListener OnMessageOnclickListener) {
        this.messageclik = OnMessageOnclickListener;
    }
}
