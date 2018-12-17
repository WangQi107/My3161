package com.wq.myclass.utils;

/**
 * 这是异步任务完成后调用的接口
 * 有两个方法，成功将调用onFinished方法
 * 失败将调用onFailed方法
 */
public interface OnTaskFinishedListener {
    void onFinished(Object obj);
    void onFailed(Exception ex, String msg);
}
