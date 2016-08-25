package com.mmednet.common.multithread.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.mmednet.common.R;

public class ProgressBarTest extends AppCompatActivity {


    private final static String TAG = ProgressBarTest.class.getSimpleName();

    private ProgressBar mProgressBar;
    private Button mButtonLoad;
    private MyAsyncTask myAsyncTask =  new MyAsyncTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar_test);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mButtonLoad = (Button) findViewById(R.id.button_load);
        mButtonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsyncTask.execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            for (int i=0;i<100;i++){
                if(isCancelled()){
                    break;
                }
                //调用publishProgress才会执行onProcessUpdate方法
                publishProgress(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Log.e(TAG,"doInBackground:InterruptedException:"+Log.getStackTraceString(e));
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(isCancelled()){
                return;
            }
            mProgressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myAsyncTask!=null && myAsyncTask.getStatus()==AsyncTask.Status.RUNNING){
            //cancel 只是将AsyncTask的设置为cancel状态
            myAsyncTask.cancel(true);
        }
    }
}
