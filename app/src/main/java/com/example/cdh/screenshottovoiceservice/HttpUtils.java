package com.example.cdh.screenshottovoiceservice;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Corey on 4/20/2017.
 * based on http://stackoverflow.com/questions/29339565/calling-rest-api-from-an-android-app
 */

public class HttpUtils {
    private static final String BASE_URL = "https://photodescriber-cdheath.c9users.io/";
    private static final int DEFAULT_TIMEOUT = 20 * 1000;


    private static AsyncHttpClient client = new SyncHttpClient();
    private static AsyncHttpClient aClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("Call Path: ", getAbsoluteUrl(url));
        aClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}