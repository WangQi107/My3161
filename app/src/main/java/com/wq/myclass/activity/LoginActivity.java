package com.wq.myclass.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wq.myclass.R;
import com.wq.myclass.utils.URLHelper;
import com.wq.myclass.utils.OnTaskFinishedListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.wq.myclass.utils.ToastHelper.showToast;

public class LoginActivity extends BaseActivity {
    private Toolbar logintoolbar;
    private TextView resetpass;
    private Button btnlogin;
    private long exitTime = 0;
    private EditText etlogin;
    private EditText etpass;
    URLHelper all = null;
    private String url;
    String unum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        all = new URLHelper();
        setViews();
        setListeners();
    }

    private void initViews() {
        logintoolbar = findViewById(R.id.login_toolbar);
        resetpass = findViewById(R.id.tv_reset);
        etlogin = findViewById(R.id.et_login);
        btnlogin = findViewById(R.id.btn_login);
        etpass = findViewById(R.id.et_pass);
    }

    private void setViews() {
        setSupportActionBar(logintoolbar);
        etlogin.setCursorVisible(false);
//        etpass.setCursorVisible(false);
    }

    private void setListeners() {
        etpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etpass.setCursorVisible(true);
            }
        });
        etlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlogin.setCursorVisible(true);
            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ResetPassActivity.class);
                startActivityForResult(i, 1);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unum = etlogin.getText().toString().trim();
                String upwd = etpass.getText().toString().trim();
                url = all.getURL("Main");
                new LoginTask(unum, upwd, url, new OnTaskFinishedListener() {
                    @Override
                    public void onFinished(Object obj) {
                        String msg = obj.toString();
                        if ("login success".equals(msg)) {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            SharedPreferences a = getApplicationContext().getSharedPreferences("Num", MODE_PRIVATE);
                            a.edit().putString("Num", unum).commit();
                            startActivity(i);
                            finish();
                        } else if ("pwd wrong".equals(msg)) {
                            showToast(getApplicationContext(), "密码错误");
                        } else if ("users do not exist".equals(msg)) {
                            showToast(getApplicationContext(), "用户不存在");
                        }
                    }

                    @Override
                    public void onFailed(Exception ex, String msg) {
                        Log.i("Error Msg:", msg);
                        showToast(getApplicationContext(), "请求失败");
                    }
                }).execute();
            }
        });
    }

    class LoginTask extends AsyncTask<Void, Void, String> {
        private String num;//登录的学号
        private String pwd;//登录的密码
        private String strUrl;//登录方法的网址
        private OnTaskFinishedListener listener;//回调接口

        //通过构造函数传值
        public LoginTask(String num, String pwd, String url, OnTaskFinishedListener listener) {
            this.num = num;
            this.pwd = pwd;
            this.strUrl = url;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            //包装网址，将字符串网址转成URL类型
            strUrl += "login" + "&unum=" + num + "&upwd=" + pwd;
            Log.i("URL", strUrl);
            try {
                URL url = new URL(strUrl);
                //通过安卓的网络请求对象来读取
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");//设置请求方式为get
                con.setReadTimeout(5000);// 设置读取超时为5秒
                con.setConnectTimeout(10000);// 设置连接网络超时为10秒
                int responseCode = con.getResponseCode();//获取网络连接状态
                Log.i("ResponseCode", String.valueOf(responseCode));
                if (responseCode == 200) {
                    //得到网络的输入流
                    InputStream is = con.getInputStream();
                    //将输入流转化字符串
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {//循环读取输入缓冲流
                        sb.append(line + "\n");
                    }
                    //这是sb这个对象里面存放的就是登录返回的json字符串
                    String strJson = sb.toString();
                    //子线程结束，将数据返回给onPostExecute方法
                    Log.i("JSON:", strJson);
                    return strJson;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String msg = "";
            try {
                //参数s就是异步任务网络请求返回的json字符串
                //通过JSONObject转换成了JSON对像
                JSONObject obj = new JSONObject(s);
                //通过键返回的对应的值
                msg = obj.getString("key");
                listener.onFinished(msg);
                if (msg.equals("login success")) {
                    String name = obj.getString("name");
                    SharedPreferences a = getApplicationContext().getSharedPreferences("Num", MODE_PRIVATE);
                    a.edit().putString("Name", name).commit();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                msg = ex.getMessage();
                listener.onFailed(ex, msg);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 2) {
                    Bundle l = data.getExtras();
                    String n = l.getString("nums").trim();
                    String p = l.getString("passes").trim();
                    etlogin.setText(n);
                    etpass.setText(p);
                    //etpass.setCursorVisible(false);//不出现光标
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*
    双击退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast(this, "再按一次推出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}