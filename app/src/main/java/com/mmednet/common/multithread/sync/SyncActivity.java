package com.mmednet.common.multithread.sync;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

/**
 * 线程同步的几种方式:
 * 1、join：
 * thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。比如在线程B中调用了线程A的Join()方法，直到线程A执行完毕后，才会继续执行线程B。
 */
public class SyncActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SyncActivity.class.getSimpleName();

    private Button mBtJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        mBtJoin = (Button) findViewById(R.id.bt_join);
        mBtJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:

                NetThread nt = new NetThread("netThread");

                FileThread ft = new FileThread("fileThread",nt);

                ft.start();

                break;
        }

    }

    private class NetThread extends Thread {

        public NetThread(String name){
            super(name);
        }

        @Override
        public void run() {

            Log.d(TAG,Thread.currentThread().getName()+" start...");

            for (int i = 0; i < 10; i++) {

                SystemClock.sleep(1000);

                Log.d(TAG,Thread.currentThread().getName()+";i:"+i);

            }

            Log.d(TAG,Thread.currentThread().getName()+" over...");

        }
    }

    private class FileThread extends Thread{
        private Thread netThread;
        public FileThread(String name,Thread netThread){
            super(name);
            this.netThread = netThread;
        }

        @Override
        public void run() {
            netThread.start();
            try {
                netThread.join();//网络线程start后，先执行，执行完成后，再执行FileThread的内容
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG,Thread.currentThread().getName()+" over...");

        }
    }
}
