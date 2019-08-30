package com.bawei.dyj0830.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    private static HttpUtils httpUtils=null;
    private HttpUtils(){}
    public static HttpUtils getInstance(){
        if (httpUtils == null) {
            synchronized (HttpUtils.class){
                if (httpUtils == null) {
                    httpUtils=new HttpUtils();
                }
            }
        }
        return httpUtils;
    }
    public interface ICallBack{
        void onSuccess(Object obj);
        void onError(String msg);
    }
    private Handler handler=new Handler();
    public boolean isNetWork(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info!=null) {
            return info.isAvailable();
        }
        return false;
    }
    public void getJsonData(final String path, final ICallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if (code==200) {
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len=0;
                        while ((len=inputStream.read(buffer))!=-1){
                            bos.write(buffer,0,len);
                        }
                        bos.close();
                        inputStream.close();
                        final String json = bos.toString();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callBack!=null) {
                                    callBack.onSuccess(json);
                                }
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callBack!=null) {
                                    callBack.onError("请求失败");
                                }
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack!=null) {
                                callBack.onError(e.toString());
                            }
                        }
                    });
                }
            }
        }).start();
    }
    public void getBitmap(final String path, final ICallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if (code==200) {
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callBack!=null) {
                                    callBack.onSuccess(bitmap);
                                }
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callBack!=null) {
                                    callBack.onError("请求失败");
                                }
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack!=null) {
                                callBack.onError(e.toString());
                            }
                        }
                    });
                }
            }
        }).start();
    }
    public void not(){
        handler.removeCallbacksAndMessages(null);
    }

}

