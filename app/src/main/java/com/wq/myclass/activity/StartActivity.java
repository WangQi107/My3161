package com.wq.myclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.wq.myclass.R;

public class StartActivity extends AppCompatActivity {
    private long exitTime = 0;
    private ImageView startwelcome;
    private Animation animation;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindows();
        setContentView(R.layout.activity_start);
        initViews();
        Runnable t = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        };
        handler.postDelayed(t, 1800);
    }

    /*
    设置全屏
     */
    private void setWindows() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initViews() {
        startwelcome = findViewById(R.id.start_welcome);
        animation = new AlphaAnimation(0.1f, 1.0f);//设置动画
        animation.setDuration(1500);//设置动画播放时间
        startwelcome.setAnimation(animation);
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
