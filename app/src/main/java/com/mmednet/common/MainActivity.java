package com.mmednet.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.multithread.MultiThreadActivity;
import com.mmednet.common.okhttp.sample_okhttp.OKHttpActivity;
import com.mmednet.common.process.ProcessActivity;
import com.mmednet.common.retrofit2.RetrofitTestActivity;
import com.mmednet.common.rxandroid.RxActivity;
import com.mmednet.common.rxjava.RxJavaActiviy;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtMultiThread;
    private Button mBtProcess;
    private Button mBtRx;
    private Button mBtOkHttp;
    private Button mBtRetrofit;
    private Button mBtRetrofitRxJava;
    private Button mBtRxjava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtMultiThread = (Button) this.findViewById(R.id.bt_multithread);
        mBtProcess = (Button) this.findViewById(R.id.bt_process);
        mBtRx = (Button) this.findViewById(R.id.bt_rx);
        mBtOkHttp = (Button) this.findViewById(R.id.bt_okhttp);
        mBtRetrofit = (Button) this.findViewById(R.id.bt_retrofit);
        mBtRetrofitRxJava = (Button) this.findViewById(R.id.bt_retrofit_rxjava);
        mBtRxjava = (Button) this.findViewById(R.id.bt_rxjava);

        mBtMultiThread.setOnClickListener(this);
        mBtProcess.setOnClickListener(this);
        mBtRx.setOnClickListener(this);
        mBtOkHttp.setOnClickListener(this);
        mBtRetrofit.setOnClickListener(this);
        mBtRetrofitRxJava.setOnClickListener(this);
        mBtRxjava.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_multithread:
                startActivity(new Intent(this, MultiThreadActivity.class));
                break;
            case R.id.bt_process:
                startActivity(new Intent(this, ProcessActivity.class));
                break;
            case R.id.bt_rx:
                startActivity(new Intent(this, RxActivity.class));
                break;
            case R.id.bt_okhttp:
                startActivity(new Intent(this, OKHttpActivity.class));
                break;
            case R.id.bt_retrofit:
                startActivity(new Intent(this, com.mmednet.common.retrofit.MainActivity.class));
                break;
            case R.id.bt_retrofit_rxjava:
                startActivity(new Intent(this, RetrofitTestActivity.class));
                break;
            case R.id.bt_rxjava:
                startActivity(new Intent(this, RxJavaActiviy.class));
                break;
        }
    }
}
