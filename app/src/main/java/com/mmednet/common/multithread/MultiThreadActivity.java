package com.mmednet.common.multithread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;
import com.mmednet.common.multithread.download.DownloadActivity;
import com.mmednet.common.multithread.handlerthread.HandlerThreadActivity;
import com.mmednet.common.multithread.sync.SyncActivity;
import com.mmednet.common.multithread.timer.TimerActivity;

public class MultiThreadActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mBtDownload;
    private Button mBtHt;
    private Button mBtSync;
    private Button mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_thread);
        mBtDownload = (Button) this.findViewById(R.id.bt_download);
        mBtHt = (Button) this.findViewById(R.id.bt_ht);
        mBtSync = (Button) this.findViewById(R.id.bt_sync);
        mTimer = (Button) this.findViewById(R.id.bt_timer);

        mBtDownload.setOnClickListener(this);
        mBtHt.setOnClickListener(this);
        mBtSync.setOnClickListener(this);
        mTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;
            case R.id.bt_ht:
                startActivity(new Intent(this,HandlerThreadActivity.class));
                break;
            case R.id.bt_sync:
                startActivity(new Intent(this, SyncActivity.class));
                break;
            case R.id.bt_timer:
                startActivity(new Intent(this, TimerActivity.class));
                 break;

        }
    }
}
