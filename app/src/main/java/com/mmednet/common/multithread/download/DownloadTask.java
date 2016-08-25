package com.mmednet.common.multithread.download;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载任务
 */
public class DownloadTask {

    private static final String TAG = DownloadTask.class.getSimpleName();
    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAO threadDAO;
    private int mFinished;
    public boolean isPause = false;
    private int mThreadCount = 1;
    private List<DownloadThread> mDownloadThreadList = new ArrayList<DownloadThread>();
    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();

    private Timer mTimer = new Timer();


    public DownloadTask(Context mContext, FileInfo mFileInfo, int threadCount) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        threadDAO = new ThreadDAOImpl(mContext);
        this.mThreadCount = threadCount;
    }

    public void download() {
        List<ThreadInfo> threadInfos = threadDAO.getThreads(mFileInfo.getUrl());
        if (threadInfos.size() == 0) {
            //每个线程下载长度
            int len = mFileInfo.getLength() / mThreadCount;
            //根据线程数量初始化相应的线程
            for (int i = 0; i < mThreadCount; i++) {
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), i * len, (i + 1) * len - 1, 0);
                if (i == mThreadCount - 1) {//最后一个线程除不尽的情况
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                threadDAO.insertThread(threadInfo);
                threadInfos.add(threadInfo);
            }
        }

        for (int i = 0; i < threadInfos.size(); i++) {
            DownloadThread downloadThread = new DownloadThread(threadInfos.get(i));
//            downloadThread.start();
            //将当前线程放入线程池执行
            DownloadTask.sExecutorService.execute(downloadThread);
            mDownloadThreadList.add(downloadThread);
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                intent.putExtra("id",mFileInfo.getId());//文件id
                mContext.sendBroadcast(intent);
            }
        }, 1000, 1000);

    }


    /**
     * 判断所有的线程是否执行完毕
     *
     * @return void
     * @author Yann
     * @date 2015-8-9 下午1:19:41
     */
    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;


        // 遍历线程集合，判断线程是否都执行完毕
        for (DownloadThread thread : mDownloadThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            threadDAO.deleteThread(mFileInfo.getUrl());
            // 发送广播知道UI下载任务结束
            mTimer.cancel();

            Intent intent = new Intent(DownloadService.ACTION_FINISH);
            intent.putExtra("fileInfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }


    class DownloadThread extends Thread {

        public boolean isFinished = false;//标识线程是否执行完毕
        private ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo threadInfo) {
            this.mThreadInfo = threadInfo;
        }

        @Override
        public void run() {
            if (!threadDAO.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
                threadDAO.insertThread(mThreadInfo);
            }

            HttpURLConnection urlConnection = null;
            RandomAccessFile raf = null;
            InputStream stream = null;

            try {
                URL url = new URL(mThreadInfo.getUrl());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("GET");
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();//获得当前线程下载的其实位置：start+已经下载完成的
                mFinished += mThreadInfo.getFinished();//已经下载完成的
                urlConnection.setRequestProperty("Range",
                        "bytes=" + start + "-" + mThreadInfo.getEnd());
                File file = new File(DownloadService.DOWN_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                if (urlConnection.getResponseCode() == 206) {
                    stream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    while ((len = stream.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                        //累加整个文件的下载进度
                        mFinished += len;
                        //累加整个线程的下载进度
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                        if (isPause) {
                            //保存当前线程的下载进度
                            threadDAO.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mThreadInfo.getFinished());
                            return;
                        }
                    }
                    // 标识线程执行完毕
                    isFinished = true;
                    checkAllThreadFinished();
                }
            } catch (Exception e) {
                Log.e(TAG, "catch:" + Log.getStackTraceString(e));
            } finally {
                try {
                    raf.close();
                    stream.close();
                    urlConnection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "finally:" + Log.getStackTraceString(e));
                }

            }
        }
    }
}
