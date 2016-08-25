package com.mmednet.common.multithread.handlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

public class HandlerThreadActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = HandlerThreadActivity.class.getSimpleName();
    private Button mBtRequest;

    private boolean runHandler = true;
    private HandlerThread mHt = new HandlerThread("NetWorkThread");
    private Handler mHtHandler = null;
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBtRequest.setText(msg.what + "");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        mBtRequest = (Button) this.findViewById(R.id.bt_request);
        mBtRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_request:
                mHt.start();
                mHtHandler = new Handler(mHt.getLooper());///必须在start()之后才能获得Looper
                mHtHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            if (runHandler) {
                                Log.d(TAG, Thread.currentThread().getName() + "--i:" + i);
                                SystemClock.sleep(1000);
                                mMainHandler.sendEmptyMessage(i);
                            }
                        }
                    }
                });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        mHt.quit();//这种方法无法停止。
        runHandler = false;
        super.onDestroy();
    }
}
