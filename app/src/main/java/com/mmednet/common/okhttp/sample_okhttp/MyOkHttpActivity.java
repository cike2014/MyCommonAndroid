package com.mmednet.common.okhttp.sample_okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 笔记：http://www.qingpingshan.com/rjbc/az/110232.html
 */
public class MyOkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MyOkHttpActivity.class.getSimpleName();
    private Button mBtGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ok_http);
        mBtGet = (Button) findViewById(R.id.bt_get);
        mBtGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_get:
                requestGet();
                break;

        }
    }
/*

    封装一个okHttpUtils的具体思路：













    */

    private void requestGet() {

        //1、创建OKHttpClient对象
        OkHttpClient mOkhttpClient = new OkHttpClient();
        //2、创建一个Request
        final Request request = new Request.Builder().url("http://gank.io/api/day/history").build();
        //3、new call
        Call call = mOkhttpClient.newCall(request);
        //4、请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG,"thread:"+Thread.currentThread().getName());

                Log.d(TAG,"response:"+response.body().string());

            }
        });


    }
}
