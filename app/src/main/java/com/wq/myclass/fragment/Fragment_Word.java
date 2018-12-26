package com.wq.myclass.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wq.myclass.R;
import com.wq.myclass.adapter.Word_Adapter;
import com.wq.myclass.bean.Word;
import com.wq.myclass.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wq.myclass.utils.ToastHelper.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Word extends Fragment {
    private EditText etword;
    private View v;
    private Button btnleave;
    private String date;
    private Word_Adapter adapter;
    private URLHelper all;

    private ListView lvword;
    private List<Word> words;

    public Fragment_Word() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_word, container, false);
        initViews();
        setViews();
        setListeners();
        return v;
    }

    private void initViews() {
        lvword = v.findViewById(R.id.lv_word);
        etword = v.findViewById(R.id.et_word);
        btnleave = v.findViewById(R.id.btn_leave);
    }

    private void setViews() {
        all = new URLHelper();
        String a = all.getURL("Three");
        new getwordsTask(a, words, lvword).execute();
        etword.setCursorVisible(false);
    }

    private void setListeners() {
        etword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etword.setCursorVisible(true);
            }
        });
        btnleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaDatas();
                String a = all.getURL("Three");
                new getwordsTask(a, words, lvword).execute();
            }
        });
    }

    private void getTime() {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        date = format.format(curDate);
    }

    private void savaDatas() {
        String word = etword.getText().toString().trim();
        if (word.equals("")) {
            showToast(getContext(), "请输入要留言的内容");
        } else {
            all = new URLHelper();
            String a = all.getURL("Three");
            getTime();
            SharedPreferences preferences = getActivity().getSharedPreferences("Num", Context.MODE_PRIVATE);
            String name = preferences.getString("Name", "");
            new insertTask(a, word, name, date,etword).execute();
        }
    }

    class getwordsTask extends AsyncTask<Void, Void, String> {
        private String strUrl;
        private List<Word> list;
        private ListView lvwords;

        public getwordsTask(String a, List<Word> words, ListView lvwords) {
            this.strUrl = a;
            this.list = words;
            this.lvwords = lvwords;
        }

        @Override
        protected String doInBackground(Void... voids) {
            strUrl += "getwords";
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
                if (array.length() == 0) {
                    Log.i("Words Info", "Empty");
                } else {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        int id = obj.getInt("bid");
                        String name = obj.getString("uname");
                        String text = obj.getString("btext");
                        String time = obj.getString("btime");
                        Word n = new Word(id, name, time, text, 1);
                        list.add(0,n);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new Word_Adapter(getActivity(), list);
            lvwords.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }

    class insertTask extends AsyncTask<Void, Void, String> {
        private String strUrl;
        private String text;
        private String time;
        private String name;
        private EditText words;

        public insertTask(String strUrl, String text, String name, String time,EditText words) {
            this.strUrl = strUrl;
            this.text = text;
            this.name = name;
            this.time = time;
            this.words=words;
        }

        @Override
        protected String doInBackground(Void... voids) {
            strUrl += "insertwords" + "&name=" + name + "&time=" + time + "&words=" + text;
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
                        sb.append(line);
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
            try {
                JSONObject obj=new JSONObject(s);
                String res=obj.getString("flag");
                if (res.equals("true")){
                    showToast(getContext(),"留言成功");
                    words.setText("");
                }else{
                    showToast(getContext(),"留言失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
