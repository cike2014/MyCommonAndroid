package com.mmednet.common.multithread.sync;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 线程同步的几种方式:
 * 1、join：
 * thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。
 * 比如在线程B中调用了线程A的Join()方法，直到线程A执行完毕后，才会继续执行线程B。
 * 2、CountDownLatch来进行线程的同步。它维护一个计数器，等待CountDownLatch的线程必须等到计数器为0时才可以继续。
 * 方式：在当前执行的线程new出countDownLatch,，然后将CountDownLatch以参数的方式传到子线程，子线程start();子线程执行结束后，
 * 调用countdown()方法。主线程一直在await，每次子线程countdown，主线程都会判断countDownLatch.getCount()==0;如果==0，则结束堵塞，重新执行。
 * 3、Semaphore当前在多线程环境下被扩放使用，操作系统的信号量是个很重要的概念，在进程控制方面都有应用。Java 并发库 的Semaphore 可以很轻松完成信号量控制，Semaphore可以控制某个资源可被同时访问的个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。比如在Windows下可以设置共享文件的最大客户端访问个数。
 * Semaphore实现的功能就类似厕所有5个坑，假如有10个人要上厕所，那么同时只能有多少个人去上厕所呢？同时只能有5个人能够占用，当5个人中 的任何一个人让开后，其中等待的另外5个人中又有一个人可以占用了。另外等待的5个人中可以是随机获得优先机会，也可以是按照先来后到的顺序获得机会，这取决于构造Semaphore对象时传入的参数选项。单个信号量的Semaphore对象可以实现互斥锁的功能，并且可以是由一个线程获得了“锁”，再由另一个线程释放“锁”，这可应用于死锁恢复的一些场合。
 */
public class SyncActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SyncActivity.class.getSimpleName();

    private Button mBtJoin;
    private Button mBtLatch;
    private Button mBtSemaPhore;
    private Button mBtBarrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        mBtJoin = (Button) findViewById(R.id.bt_join);
        mBtLatch = (Button) findViewById(R.id.bt_latch);
        mBtSemaPhore = (Button) findViewById(R.id.bt_semaphore);
        mBtBarrier = (Button) findViewById(R.id.bt_barrier);
        mBtJoin.setOnClickListener(this);
        mBtLatch.setOnClickListener(this);
        mBtSemaPhore.setOnClickListener(this);
        mBtBarrier.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:

                NetThread nt = new NetThread("netThread");

                FileThread ft = new FileThread("fileThread", nt);

                ft.start();

                break;

            case R.id.bt_latch:

                CountDownLatch countDownLatch = new CountDownLatch(2);
                MyThread mt1 = new MyThread(countDownLatch, "myThread1");
                MyThread mt2 = new MyThread(countDownLatch, "myThread2");

                mt1.start();
                mt2.start();


                try {
                    countDownLatch.await();//主线程会一直堵塞，等待子线程调用CountDown()方法，子线程每次调用，主线程都会判断countDownLatch.getCount()是否为0，如果为0则结束堵塞，开始执行主线程的方法。
                    Log.d(TAG, "MainThread :所有工作已经完成");
                } catch (InterruptedException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

                break;
            case R.id.bt_semaphore:

                // 线程池
                ExecutorService exec = Executors.newCachedThreadPool();
                // 只能5个线程同时访问
                final Semaphore semp = new Semaphore(5);
                // 模拟20个客户端访问
                for (int index = 0; index < 20; index++) {
                    final int NO = index;
                    Runnable run = new Runnable() {
                        public void run() {
                            try {
                                // 获取许可
                                semp.acquire();
                                Log.d(TAG,"Accessing: " + NO);
                                Thread.sleep((long) (Math.random() * 10000));
                                // 访问完后，释放。释放后，随机有新的线程进来。
                                semp.release();
                                Log.d(TAG, "-----------------" + semp.availablePermits());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    exec.execute(run);
                }
                // 退出线程池
                exec.shutdown();

                break;

            case R.id.bt_barrier:

                startActivity(new Intent(this,CyclicBarrierActivity.class));
                break;
        }


    }


    //Join start----------------------------------------------------------
    private class NetThread extends Thread {

        public NetThread(String name) {
            super(name);
        }

        @Override
        public void run() {

            Log.d(TAG, Thread.currentThread().getName() + " start...");

            for (int i = 0; i < 10; i++) {

                SystemClock.sleep(1000);

                Log.d(TAG, Thread.currentThread().getName() + ";i:" + i);

            }

            Log.d(TAG, Thread.currentThread().getName() + " over...");

        }
    }

    private class FileThread extends Thread {
        private Thread netThread;

        public FileThread(String name, Thread netThread) {
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
            Log.d(TAG, Thread.currentThread().getName() + " over...");

        }
    }
//Join over---------------------------------------------------------------

//CountLaunch start-------------------------------------------------------


    class MyThread extends Thread {

        private CountDownLatch mLatch;
        private String mName;

        public MyThread(CountDownLatch cdl, String name) {
            this.mLatch = cdl;
            this.mName = name;
        }

        @Override
        public void run() {
            Log.d(TAG, mName + "开始执行....");
            for (int i = 0; i < 10; i++) {

                SystemClock.sleep(100);

                Log.d(TAG, mName + "执行到了" + i);

            }
            Log.d(TAG, mName + "结束执行.....");
            mLatch.countDown();

        }
    }


//CountLaunch end------------------------------------------------------

}
