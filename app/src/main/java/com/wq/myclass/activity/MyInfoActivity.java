package com.wq.myclass.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wq.myclass.R;
import com.wq.myclass.dialog.MyDialog;
import com.wq.myclass.dialog.Update_Dialog;
import com.wq.myclass.utils.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.wq.myclass.utils.ToastHelper.showToast;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar infotoolbar;
    private TextView infonames;
    private TextView infosex;
    private TextView infostunm;
    private TextView infoemail;
    private TextView infophone;
    private TextView emailupdate;
    private TextView phoneupdate;
    private Button btnexit;
    private Update_Dialog dialog;
    private String contentStr;
    private TextView myip;
    private ImageButton ibshare;
    URLHelper all = null;
    SharedPreferences s = null;
    private String number = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        String res = obj.getString("key");
                        if (res.equals("update success")) {
                            infoemail.setText(contentStr);
                            showToast(getApplicationContext(), "修改成功");
                            dialog.dismiss();
                        } else {
                            showToast(getApplicationContext(), "修改失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        String res = obj.getString("key");
                        if (res.equals("update success")) {
                            infophone.setText(contentStr);
                            showToast(getApplicationContext(), "修改成功");
                            dialog.dismiss();
                        } else {
                            showToast(getApplicationContext(), "修改失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initViews();
        all = new URLHelper();
        setViews();
        setListeners();
        getDatas();
    }

    private void initViews() {
        infotoolbar = findViewById(R.id.info_toolbar);
        infonames = findViewById(R.id.info_names);
        infosex = findViewById(R.id.info_sex);
        infostunm = findViewById(R.id.info_stuno);
        infoemail = findViewById(R.id.info_email);
        infophone = findViewById(R.id.info_phone);
        emailupdate = findViewById(R.id.email_update);
        phoneupdate = findViewById(R.id.phone_update);
        btnexit = findViewById(R.id.info_exit);
        myip = findViewById(R.id.showip);
        ibshare = findViewById(R.id.ib_share);
    }

    private void setViews() {
        infotoolbar.setNavigationIcon(R.mipmap.icon_back);
        infotoolbar.setTitle("");
        setSupportActionBar(infotoolbar);
        infotoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListeners() {
        emailupdate.setOnClickListener(this);
        phoneupdate.setOnClickListener(this);
        btnexit.setOnClickListener(this);
        ibshare.setOnClickListener(this);
    }

    private void getDatas() {
        String url = all.getURL("Main");
        SharedPreferences s = this.getSharedPreferences("Num", MODE_PRIVATE);
        number = s.getString("Num", "");
        if (!number.equals("")) {
            new GetInfoTask(number, url).execute();
        } else {
            Log.i("Num Null", number);
        }
        new GetipTask(myip).execute();
    }

    class GetInfoTask extends AsyncTask<Void, Void, String> {
        private String num;
        private String strUrl;

        public GetInfoTask(String num, String url) {
            this.num = num;
            this.strUrl = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            strUrl += "getinfo" + "&unum=" + num;
            Log.i("URL", strUrl);
            try {
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setConnectTimeout(10000);
                int responseCode = con.getResponseCode();
                Log.i("ResponseCode", String.valueOf(responseCode));
                if (responseCode == 200) {
                    InputStream is = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String strJson = sb.toString();
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
            try {
                JSONObject obj = new JSONObject(s);
                infonames.setText(obj.getString("name"));
                infosex.setText(obj.getString("sex"));
                infostunm.setText(obj.getString("number"));
                infoemail.setText(obj.getString("email"));
                infophone.setText(obj.getString("tel"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class GetipTask extends AsyncTask<Void, Void, String> {
        private TextView myip;

        public GetipTask(TextView myip) {
            this.myip=myip;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://ip-api.com/json");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setConnectTimeout(10000);
                int responseCode = con.getResponseCode();
                Log.i("ResponseCode", String.valueOf(responseCode));
                if (responseCode == 200) {
                    InputStream is = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String strJson = sb.toString();
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
            try {
                JSONObject obj = new JSONObject(s);
                myip.setText("当前IP:"+obj.getString("query")+"---"+"地区:"+obj.getString("country")+obj.getString("regionName")+obj.getString("city")+"---"+"ISP:"+obj.getString("isp"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_update:
                dialog = new Update_Dialog(MyInfoActivity.this);
                dialog.setTitle("请填写新邮箱");
                dialog.seted("email");
                dialog.setNoOnclickListener("取消", new Update_Dialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new Update_Dialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        contentStr = dialog.getContent();
                        if (!contentStr.equals("")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String u = all.getURL("Main");
                                        u += "updateinfo" + "&unum=" + number + "&content=" + contentStr + "&type=email";
                                        Log.i("URL", u);
                                        URL url = new URL(u);
                                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                        con.setRequestMethod("GET");
                                        con.setReadTimeout(5000);
                                        con.setConnectTimeout(10000);
                                        int responseCode = con.getResponseCode();
                                        Log.i("ResponseCode", String.valueOf(responseCode));
                                        if (responseCode == 200) {
                                            InputStream is = con.getInputStream();
                                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                            String line = "";
                                            StringBuilder sb = new StringBuilder();
                                            while ((line = reader.readLine()) != null) {
                                                sb.append(line);
                                            }
                                            String strJson = sb.toString();
                                            Log.i("JSON:", strJson);
                                            JSONObject object = new JSONObject(strJson);
                                            Message msg = handler.obtainMessage();
                                            msg.what = 1;
                                            msg.obj = object;
                                            handler.sendMessage(msg);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showToast(getBaseContext(), "连接服务器失败");
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(getBaseContext(), "请填写正确的邮箱", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.phone_update:
                dialog = new Update_Dialog(MyInfoActivity.this);
                dialog.setTitle("请填写新电话");
                dialog.seted("phone");
                dialog.setNoOnclickListener("取消", new Update_Dialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new Update_Dialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        contentStr = dialog.getContent();
                        if (!contentStr.equals("")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String u = all.getURL("Main");
                                        u += "updateinfo" + "&unum=" + number + "&content=" + contentStr + "&type=phone";
                                        Log.i("URL", u);
                                        URL url = new URL(u);
                                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                        con.setRequestMethod("GET");
                                        con.setReadTimeout(5000);
                                        con.setConnectTimeout(10000);
                                        int responseCode = con.getResponseCode();
                                        Log.i("ResponseCode", String.valueOf(responseCode));
                                        if (responseCode == 200) {
                                            InputStream is = con.getInputStream();
                                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                            String line = "";
                                            StringBuilder sb = new StringBuilder();
                                            while ((line = reader.readLine()) != null) {
                                                sb.append(line);
                                            }
                                            String strJson = sb.toString();
                                            Log.i("JSON:", strJson);
                                            JSONObject object = new JSONObject(strJson);
                                            Message msg = handler.obtainMessage();
                                            msg.what = 2;
                                            msg.obj = object;
                                            handler.sendMessage(msg);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showToast(getBaseContext(), "连接服务器失败");
                                    }
                                }
                            }).start();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.info_exit:
                final MyDialog dialog = new MyDialog(this);
                dialog.setTitle("注销");
                dialog.setMessage("确定要注销登录吗？");
                dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Intent intent = new Intent();
                        intent.setAction("exitapp");
                        sendBroadcast(intent);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
                dialog.show();
                break;
            case R.id.ib_share:
                shareText("分享My3161", null, "App下载链接：http://139.9.34.158:8080/Download/My3161/My3161.apk");
                break;
        }
    }

    private void shareText(String dlgTitle, String subject, String content) {
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        intent.putExtra(Intent.EXTRA_TEXT, content);

        // 设置弹出框标题
        if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
            startActivity(Intent.createChooser(intent, dlgTitle));
        } else { // 系统默认标题
            startActivity(intent);
        }
    }
}
