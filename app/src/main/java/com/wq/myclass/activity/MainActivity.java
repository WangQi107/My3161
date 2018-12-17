package com.wq.myclass.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wq.myclass.R;
import com.wq.myclass.fragment.Fragment_Adress;
import com.wq.myclass.fragment.Fragment_Notice;
import com.wq.myclass.fragment.Fragment_Word;
import com.wq.myclass.transform.XCRoundImageView;

public class MainActivity extends BaseActivity {
    private long exitTime = 0;
    private XCRoundImageView mainheader;
    private TextView maintitle;
    private RadioGroup radioGroup;
    private RadioButton rbnotic;
    private RadioButton rbadress;
    private RadioButton rbword;
    private Handler handler = new Handler() {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setViews();
        setListeners();
    }

    private void initViews() {
        mainheader = findViewById(R.id.main_header);
        radioGroup = findViewById(R.id.radiogroup);
//        rbnotic = findViewById(R.id.rb_notice);
        rbadress = findViewById(R.id.rb_adress);
//        rbword = findViewById(R.id.rb_word);
        maintitle = findViewById(R.id.main_title);
    }

    private void setViews() {
//        SharedPreferences a = this.getSharedPreferences("Name", MODE_PRIVATE);
//        SharedPreferences b = this.getSharedPreferences("Sex", MODE_PRIVATE);
//        final String name = a.getString("name", "");
//        final String sex = b.getString("sex", "");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (sex.equals("女")) {
//                    mainheader.setImageResource(R.mipmap.ic_header_girl);
//                } else {
//                    mainheader.setImageResource(R.mipmap.ic_header_boy);
//                }
//                if (name.equals("刘艳") || name.equals("刘爽") || name.equals("刘士")) {
//                    mainheader.setImageResource(R.mipmap.ic_header_teacher);
//                }
//            }
//        }).start();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fs = fm.beginTransaction();
        Fragment_Notice df01 = new Fragment_Notice();
        fs.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fs.replace(R.id.framgment_one, df01);
        fs.commit();
    }

    private void setListeners() {
        mainheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyInfoActivity.class);
                startActivity(i);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_notice:
                        maintitle.setText("公告");
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction fs = fm.beginTransaction();
                        Fragment_Notice df01 = new Fragment_Notice();
                        fs.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fs.replace(R.id.framgment_one, df01);
                        fs.commit();
                        break;
                    case R.id.rb_adress:
                        maintitle.setText("通讯");
                        FragmentManager ffm = getSupportFragmentManager();
                        FragmentTransaction ffs = ffm.beginTransaction();
                        Fragment_Adress df02 = new Fragment_Adress();
                        ffs.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ffs.replace(R.id.framgment_one, df02);
                        ffs.commit();
                        break;
                    case R.id.rb_word:
                        maintitle.setText("留言");
                        FragmentManager fmm = getSupportFragmentManager();
                        FragmentTransaction fss = fmm.beginTransaction();
                        Fragment_Word df03 = new Fragment_Word();
                        fss.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fss.replace(R.id.framgment_one, df03);
                        fss.commit();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}