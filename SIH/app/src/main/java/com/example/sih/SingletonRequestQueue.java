package com.example.sih;


import android.content.Context;
import android.util.Log;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;


public class SingletonRequestQueue {
    private static final String TAG = SingletonRequestQueue.class.getName();

    private static SingletonRequestQueue mInstance;
    private RequestQueue requestQueue;
    private Context appContext;
    private CookieManager cookieManager;

    private SingletonRequestQueue(Context context) {
        appContext = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
