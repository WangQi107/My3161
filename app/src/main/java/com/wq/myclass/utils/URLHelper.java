package com.wq.myclass.utils;

public class URLHelper {
    private String URL;

    public URLHelper() {
    }

    public String getURL(String target) {
        if (target.equals("Main")) {
            URL = "http://139.9.34.158:8080/My3161Back/MainServlet?do=";
//            URL = "http://192.168.42.41:8080/My3161Back/MainServlet?do=";
//            URL = "http://192.168.10.186:8080/My3161Back/MainServlet?do=";
        } else if (target.equals("Two")) {
            URL = "http://139.9.34.158:8080/My3161Back/ContactServlet?do=";
//            URL = "http://192.168.42.41:8080/My3161Back/ContactServlet?do=";
//            URL = "http://192.168.10.186:8080/My3161Back/ContactServlet?do=";
        }else if (target.equals("One")) {
            URL = "http://139.9.34.158:8080/My3161Back/NoticeServlet?do=";
//            URL = "http://192.168.42.41:8080/My3161Back/NoticeServlet?do=";
//            URL = "http://192.168.10.186:8080/My3161Back/NoticeServlet?do=";
        }else if (target.equals("Three")) {
            URL = "http://139.9.34.158:8080/My3161Back/WordsServlet?do=";
//            URL = "http://192.168.42.41:8080/My3161Back/WordsServlet?do=";
//            URL = "http://192.168.10.186:8080/My3161Back/WordsServlet?do=";
        }
        return URL;
    }
}
