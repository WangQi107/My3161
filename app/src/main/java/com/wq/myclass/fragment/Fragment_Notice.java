package com.wq.myclass.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wq.myclass.R;
import com.wq.myclass.adapter.Notice_Adapter;
import com.wq.myclass.bean.Notice;
import com.wq.myclass.dialog.Homework_dialog;
import com.wq.myclass.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Notice extends Fragment {
    private View v;
    private ListView lvnotice;
    private URLHelper all;
    private List<Notice> notices;
//    private RelativeLayout rr;

    private TextView tvqw;
    private TextView tvtq;
    private ImageView icwe;

    public Fragment_Notice() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notice, container, false);
        lvnotice = v.findViewById(R.id.lv_notice);
        tvqw = v.findViewById(R.id.tv_qw);
        tvtq = v.findViewById(R.id.tv_we);
        icwe = v.findViewById(R.id.ic_we);
//        rr = v.findViewById(R.id.RR);
//        rr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                LayoutInflater inflater = getLayoutInflater();
////                View view = inflater.inflate(R.layout.homework_dialog, null);
////                final Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
////                dialog.setContentView(view);
////                dialog.setCanceledOnTouchOutside(false);
////                dialog.show();
//                final Homework_dialog dialog=new Homework_dialog(getContext());
//                dialog.setOkclike(new Homework_dialog.OnOkOnclickListener() {
//                    @Override
//                    public void OnOkClik() {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });
        setViews();
        new getWeather(tvqw, tvtq, icwe).execute();
        return v;
    }

    private void setViews() {
        all = new URLHelper();
        String a = all.getURL("One");
        new getNoticeTask(a, notices, lvnotice).execute();
    }

    class getWeather extends AsyncTask<Void, Void, String> {
        private TextView tvqw;
        private TextView tvtq;
        private ImageView icwe;

        public getWeather(TextView tvqw, TextView tvtq, ImageView icwe) {
            this.tvqw = tvqw;
            this.tvtq = tvtq;
            this.icwe = icwe;
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("http://api.yytianqi.com/observe?city=CH250101&key=ua590ubl0wu94hoq");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                String sd = object.getString("data");
                JSONObject obj = new JSONObject(sd);
                String tq = obj.getString("tq");
                String qw = obj.getString("qw");
                tvtq.setText(tq);
                tvqw.setText(qw + "°C");
                switch (tq) {
                    case "晴":
                        icwe.setImageResource(R.mipmap.sun);
                        break;
                    case "多云":
                        icwe.setImageResource(R.mipmap.duoyun);
                        break;
                    case "阴":
                        icwe.setImageResource(R.mipmap.yintian);
                        break;
                    case "阵雨":
                        icwe.setImageResource(R.mipmap.rain);
                        break;
                    case "雨夹雪":
                        icwe.setImageResource(R.mipmap.yujiaxue);
                        break;
                    case "雾":
                        icwe.setImageResource(R.mipmap.fog);
                        break;
                    case "刮风":
                        icwe.setImageResource(R.mipmap.guafeng);
                        break;
                    default:
                        icwe.setImageResource(R.mipmap.sun);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    class getNoticeTask extends AsyncTask<Void, Void, String> {
        private String strUrl;
        private List<Notice> list;
        private ListView lvnotice;

        public getNoticeTask(String a, List<Notice> notices, ListView lvnotice) {
            this.strUrl = a;
            this.list = notices;
            this.lvnotice = lvnotice;
        }

        @Override
        protected String doInBackground(Void... voids) {
            strUrl += "getnotice";
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
            list = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("nid");
                    String name = obj.getString("uname");
                    String text = obj.getString("ntext");
                    String time = obj.getString("ntime");
                    String title = obj.getString("ntitle");
                    Notice n = new Notice(id, title, name, time, text);
                    list.add(n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Notice_Adapter adapter = new Notice_Adapter(getActivity(), list);
            lvnotice.setAdapter(adapter);
            super.onPostExecute(s);
        }
    }

}
