package com.wq.myclass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wq.myclass.R;

public class Homework_dialog extends Dialog {
    private Button btnexit;
    private OnOkOnclickListener okclike;

    public Homework_dialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_dialog);
        setCanceledOnTouchOutside(false);
        initViews();
        setListeners();
    }

    private void setListeners() {
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okclike != null) {
                    okclike.OnOkClik();
                }
            }
        });
    }

    public interface OnOkOnclickListener {
        public void OnOkClik();
    }

    private void initViews() {
        btnexit = findViewById(R.id.btn_exit);
    }

    public void setOkclike(OnOkOnclickListener okclike) {
        this.okclike = okclike;
    }
}
