package com.mmednet.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.multithread.MultiThreadActivity;
import com.mmednet.common.process.ProcessActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtMultiThread;
    private Button mBtProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtMultiThread = (Button) this.findViewById(R.id.bt_multithread);
        mBtProcess = (Button) this.findViewById(R.id.bt_process);

        mBtMultiThread.setOnClickListener(this);
        mBtProcess.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_multithread:

                startActivity(new Intent(this, MultiThreadActivity.class));

                break;
            case R.id.bt_process:

                startActivity(new Intent(this, ProcessActivity.class));

                break;
        }
    }
}
