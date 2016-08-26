package com.mmednet.common.multithread.timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtTimer;
    private Button mBtCountDownTimer;


    TimeOut mTimeOut;

    private static final String TAG= TimerActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mBtTimer = (Button) findViewById(R.id.bt_timer);
        mBtCountDownTimer = (Button) findViewById(R.id.bt_countdowntimer);
        mBtTimer.setOnClickListener(this);
        mBtCountDownTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_timer:
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG,"beginTime:"+System.currentTimeMillis());
                        SystemClock.sleep(1000);
                        Log.d(TAG,"endTime:"+System.currentTimeMillis());
                    }
                },1000,1000);
                break;
            case R.id.bt_countdowntimer:

                mTimeOut = new TimeOut(60000,1000);
                mTimeOut.start();


                break;
        }
    }



    class TimeOut extends CountDownTimer{

        public TimeOut(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mBtCountDownTimer.setEnabled(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtCountDownTimer.setText((millisUntilFinished/1000)+"秒后重新发送");
        }

        @Override
        public void onFinish() {
            mBtCountDownTimer.setText("重新发送");
            mBtCountDownTimer.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeOut.cancel();
    }
}
