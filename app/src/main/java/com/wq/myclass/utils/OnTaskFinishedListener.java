package com.wq.myclass.utils;

import java.util.Map;

public interface OnTaskFinishedListener {
    void onFinished(Object obj);
    void getmap(Map<String,String> map);
    void onFailed(Exception ex, String msg);
}
