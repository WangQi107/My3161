package com.wq.myclass.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.wq.myclass.utils.ToastHelper.showToast;

import com.wq.myclass.R;
import com.wq.myclass.utils.URLHelper;
import com.wq.myclass.utils.OnTaskFinishedListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetPassActivity extends BaseActivity {
    private Toolbar resettoolbar;
    private EditText resetnum;
    private EditText resetpass;
    private EditText resetpassok;
    private EditText orpass;
    private Button resetbtn;
    private URLHelper all = null;
    String unum;
    String upass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initViews();
        all=new URLHelper();
        setListeners();
        setViews();
    }

    private void initViews() {
        resettoolbar = findViewById(R.id.reset_toolbar);
        resetnum = findViewById(R.id.reset_num);
        resetpass = findViewById(R.id.reset_pass);
        resetpassok = findViewById(R.id.reset_passok);
        resetbtn = findViewById(R.id.reset_btn);
        orpass = findViewById(R.id.or_pass);
    }

    private void setViews() {
        resettoolbar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(resettoolbar);
        resettoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListeners() {
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unum = resetnum.getText().toString();
                upass = resetpass.getText().toString();
                String upassok = resetpassok.getText().toString();
                String uorpass = orpass.getText().toString();
                String url = all.getURL("Main");
                if (unum.equals("") || upass == null) {
                    showToast(getApplicationContext(), "学号为空");
                } else if (uorpass.equals("")) {
                    showToast(getApplicationContext(), "原密码为空");
                } else if (upass.equals("")) {
                    showToast(getApplicationContext(), "密码为空");
                } else if (upass.equals(upassok)) {
                    try {
                        new ResetTask(unum, uorpass, upass, url, new OnTaskFinishedListener() {
                            @Override
                            public void onFinished(Object obj) {
                                String msg = obj.toString();
                                if ("users do not exist or wrong".equals(msg)) {
                                    showToast(getApplicationContext(), "学号错误或者不存在");
                                } else if ("orpwd wrong".equals(msg)) {
                                    showToast(getApplicationContext(), "原密码错误");
                                } else if ("update failed".equals(msg)) {
                                    showToast(getApplicationContext(), "重置密码失败");
                                } else if ("update success".equals(msg)) {
                                    Intent i = new Intent();
                                    Bundle cc = new Bundle();
                                    cc.putString("nums", unum);
                                    cc.putString("passes", upass);
                                    i.putExtras(cc);
                                    setResult(2, i);
                                    showToast(getApplicationContext(), "重置密码成功");
                                    finish();
                                }
                            }

                            @Override
                            public void onFailed(Exception ex, String msg) {
                                Log.i("Error Msg:", msg);
                                showToast(getApplicationContext(), "请求失败");
                            }
                        }).execute();
                    } catch (Exception e) {
                        Log.i("Error Msg", e.toString());
                    }
                } else {
                    showToast(getApplicationContext(), "两次输入的密码不一致");
                }
            }
        });
    }

    class ResetTask extends AsyncTask<Void, Void, String> {
        private String num;//学号
        private String orpwd;//原密码
        private String npwd;//新密码
        private String strUrl;//登录方法的网址
        private OnTaskFinishedListener listener;//回调接口

        public ResetTask(String num, String orpwd, String npwd, String url, OnTaskFinishedListener listener) {
            this.num = num;
            this.orpwd = orpwd;
            this.npwd = npwd;
            this.strUrl = url;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            strUrl += "resetpwd" + "&unum=" + num + "&orpwd=" + orpwd + "&npwd=" + npwd;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                msg = ex.getMessage();
                listener.onFailed(ex, msg);
            }
        }
    }
}
