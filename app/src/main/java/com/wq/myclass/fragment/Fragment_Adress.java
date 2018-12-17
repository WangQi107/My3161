package com.wq.myclass.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wq.myclass.R;
import com.wq.myclass.dialog.Contacts_Dialog;
import com.wq.myclass.sidebar.SideBar;
import com.wq.myclass.sidebar.SortAdapter;
import com.wq.myclass.sidebar.User;
import com.wq.myclass.utils.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wq.myclass.utils.ToastHelper.showToast;

public class Fragment_Adress extends Fragment {
    private ListView listView;
    private SideBar sideBar;
    private ArrayList<User> list;
    HashMap<String,String> tell = new HashMap<>();
    private View v;
    private Contacts_Dialog cdialog;
    private String itemame;
    private URLHelper all;
    private static final int REQUEST_CALL_PHONE = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    list=new ArrayList<>();
                    JSONArray a = (JSONArray) msg.obj;
                    for (int i = 0; i < a.length(); i++) {
                        try {
                            JSONObject o = a.getJSONObject(i);
                            String name = o.getString("uname");
                            String tel = o.getString("utel");
                            User u = new User(name);
                            list.add(u);
                            tell.put(name,tel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(list);
                    SortAdapter adapter = new SortAdapter(getContext(), list);
                    listView.setAdapter(adapter);
            }
            super.handleMessage(msg);
        }
    };

    public Fragment_Adress() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_adress, container, false);
        initView();
        all = new URLHelper();
        setListeners();
        initData();
        return v;
    }

    private void initView() {
        listView = v.findViewById(R.id.listView);
        sideBar = v.findViewById(R.id.side_bar);
        requestPermission();
    }

    private void creatDialog() {
        cdialog = new Contacts_Dialog(getActivity());
        cdialog.setPhoneClikListeners(new Contacts_Dialog.OnPhoneOnclickListener() {
            @Override
            public void OnPhoneClik() {
                callPhone();
                cdialog.dismiss();
            }
        });
        cdialog.setMessageClikListeners(new Contacts_Dialog.OnMessageOnclickListener() {
            @Override
            public void OnMessageClik() {
                sendSMS();
                cdialog.dismiss();
            }
        });
        cdialog.setName(itemame);
        cdialog.setPhone(tell.get(itemame));
        cdialog.setSex("男");
        cdialog.show();
    }


    private void setListeners() {
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemame = list.get(position).getName();
                creatDialog();
            }
        });
    }

    private void initData() {
        if (list == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String uurl = all.getURL("Two");
                    uurl += "getdata";
                    Log.i("URL", uurl);
                    try {
                        URL url = new URL(uurl);
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
                            JSONArray array = new JSONArray(strJson);
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = array;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return;
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tell.get(itemame)));
        startActivity(intent);
    }

    private void sendSMS() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + tell.get(itemame)));
        startActivity(intent);
    }

    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // 申请权限被拒绝
                    Toast.makeText(getActivity(), "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
