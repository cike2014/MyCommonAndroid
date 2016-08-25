package com.mmednet.common.multithread.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import java.util.LinkedHashMap;


import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();
    public static final String ACTION_STOP = "action_stop";
    public static final String ACTION_START = "action_start";
    public static final String ACTION_FINISH = "action_finish";
    public static final String ACTION_UPDATE = "action_update";
    private static final int MSG_INIT = 0;
    public static final String DOWN_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/";

    public Map<Integer,DownloadTask> mTasks = new LinkedHashMap<Integer,DownloadTask>();

    private DownloadTask mTask;

    public DownloadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG,"intent:"+intent);

        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fi = (FileInfo) intent.getSerializableExtra("fileInfo");
//            new InitThread(fi).start();
            //放入线程池执行
            DownloadTask.sExecutorService.execute(new InitThread(fi));

        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fi = (FileInfo) intent.getSerializableExtra("fileInfo");
            mTask = mTasks.get(fi.getId());
            if(mTask!=null){
                mTask.isPause = true;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT:
                    //开启一个下载任务，并把开启的下载任务放入Map中
                    FileInfo mFileInfo = (FileInfo) msg.obj;
                    DownloadTask mTask = new DownloadTask(DownloadService.this,mFileInfo,3);
                    mTask.download();
                    mTasks.put(mFileInfo.getId(),mTask);
                    //发送广播到activity，设置进度条
                    Intent intent = new Intent();
                    intent.setAction(ACTION_START);
                    intent.putExtra("fileInfo",mFileInfo);
                    sendBroadcast(intent);
                    break;
            }
        }
    };

    private class InitThread extends Thread {
        private FileInfo mFileInfo;

        public InitThread(FileInfo fileInfo) {
            this.mFileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("GET");
                int length = -1;
                if (urlConnection.getResponseCode() == 200) {
                    // 获得文件的长度
                    length = urlConnection.getContentLength();
                }
                if (length < 0) {
                    return;
                }

                File dir = new File(DOWN_PATH);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File newFile = new File(dir,mFileInfo.getFileName());
                raf = new RandomAccessFile(newFile,"rwd");
                raf.setLength(length);
                mFileInfo.setLength(length);
                mHandler.obtainMessage(MSG_INIT,mFileInfo).sendToTarget();

            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }finally{
                try{
                    raf.close();
                    urlConnection.disconnect();
                }catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }


        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
