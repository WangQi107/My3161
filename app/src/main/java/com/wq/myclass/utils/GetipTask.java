package com.wq.myclass.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.wq.myclass.utils.ToastHelper.showToast;

public class GetipTask extends AsyncTask<Void, Void, String> {
    private OnTaskFinishedListener listener;
    private Context context;

    public GetipTask(Context context, OnTaskFinishedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://ip.360.cn/IPShare/info ");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int Code = con.getResponseCode();
            if (Code == 200) {
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
            } else {
                showToast(context, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Map<String, String> map = new HashMap<>();
        try {
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String ip = obj.getString("ip");
                String location = obj.getString("location");
                map.put("ip", ip);
                map.put("location", location);
            }
            listener.onFinished(map);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailed(e, "failed");
        }
    }
}
