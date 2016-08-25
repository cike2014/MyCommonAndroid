package com.mmednet.common.multithread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;
import com.mmednet.common.multithread.download.DownloadActivity;
import com.mmednet.common.multithread.handlerthread.HandlerThreadActivity;

public class MultiThreadActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mBtDownload;
    private Button mBtHt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_thread);
        mBtDownload = (Button) this.findViewById(R.id.bt_download);
        mBtHt = (Button) this.findViewById(R.id.bt_ht);

        mBtDownload.setOnClickListener(this);
        mBtHt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_download:
                startActivity(new Intent(MultiThreadActivity.this, DownloadActivity.class));
                break;
            case R.id.bt_ht:
                startActivity(new Intent(MultiThreadActivity.this,HandlerThreadActivity.class));
                break;

        }
    }
}
