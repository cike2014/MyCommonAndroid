package com.mmednet.common.multithread.sync;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 类有一个整数初始值，此值表示将在同一点同步的线程数量。当其中一个线程到达确定点，它会调用await() 方法来等待其他线程。当线程调用这个方法，CyclicBarrier阻塞线程进入休眠直到其他线程到达。当最后一个线程调用CyclicBarrier 类的await() 方法，它唤醒所有等待的线程并继续执行它们的任务。
 * 注意比较CountDownLatch和CyclicBarrier：
 * 1.CountDownLatch的作用是允许1或N个线程等待其他线程完成执行；而CyclicBarrier则是允许N个线程相互等待。
 * 2.CountDownLatch的计数器无法被重置；CyclicBarrier的计数器可以被重置后使用，因此它被称为是循环的barrier。
 */
public class CyclicBarrierActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtBarrier;
    private static final String TAG = CyclicBarrierActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclic_barrier);
        mBtBarrier = (Button) findViewById(R.id.bt_barrier);
        mBtBarrier.setOnClickListener(this);
    }


    int threadNum = 5;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_barrier:


                CyclicBarrier barrier = new CyclicBarrier(threadNum, new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "所有的线程都到了呀，报道一下");
                    }
                });

                for (int i = 0; i <= 10; i++) {
                    WorkerThread wt = new WorkerThread(barrier, "Worker Thread " + i);
                    wt.start();
                }

                break;
        }
    }

    class WorkerThread extends Thread {
        CyclicBarrier barrier;
        String name;

        public WorkerThread(CyclicBarrier barrier, String name) {
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            Log.d(TAG, name + "到了...");
            SystemClock.sleep(1000);
            try {
                barrier.await();
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            Log.d(TAG, name + "等待已经结束，开始执行....");
        }
    }
    /*

08-25 23:19:35.702 14775-16218/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 0到了...
08-25 23:19:35.702 14775-16219/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 1到了...
08-25 23:19:35.704 14775-16222/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 4到了...
08-25 23:19:35.704 14775-16220/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 2到了...
08-25 23:19:35.704 14775-16223/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 5到了...
08-25 23:19:35.705 14775-16221/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 3到了...
08-25 23:19:35.709 14775-16224/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 6到了...
08-25 23:19:35.709 14775-16225/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 7到了...
08-25 23:19:35.710 14775-16227/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 9到了...
08-25 23:19:35.713 14775-16226/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 8到了...
08-25 23:19:35.714 14775-16228/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 10到了...///每个线程到了之后，首先await。如果都到了，然后给Cyclic报道一下，然后再按照Barrier的数量执行之后的内容。
08-25 23:19:36.705 14775-16223/com.mmednet.common D/CyclicBarrierActivity: 所有的线程都到了呀，报道一下
08-25 23:19:36.705 14775-16223/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 5等待已经结束，开始执行....
08-25 23:19:36.705 14775-16218/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 0等待已经结束，开始执行....
08-25 23:19:36.705 14775-16222/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 4等待已经结束，开始执行....
08-25 23:19:36.706 14775-16219/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 1等待已经结束，开始执行....
08-25 23:19:36.706 14775-16220/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 2等待已经结束，开始执行....
08-25 23:19:36.714 14775-16226/com.mmednet.common D/CyclicBarrierActivity: 所有的线程都到了呀，报道一下
08-25 23:19:36.715 14775-16224/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 6等待已经结束，开始执行....
08-25 23:19:36.715 14775-16226/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 8等待已经结束，开始执行....
08-25 23:19:36.715 14775-16227/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 9等待已经结束，开始执行....
08-25 23:19:36.717 14775-16221/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 3等待已经结束，开始执行....
08-25 23:19:36.717 14775-16225/com.mmednet.common D/CyclicBarrierActivity: Worker Thread 7等待已经结束，开始执行....




     */
}
