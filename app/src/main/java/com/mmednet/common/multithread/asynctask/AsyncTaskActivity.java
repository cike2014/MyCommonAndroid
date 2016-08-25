package com.mmednet.common.multithread.asynctask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

public class AsyncTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtProgress;
    private Button mBtImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        mBtProgress = (Button) this.findViewById(R.id.bt_progress);
        mBtImage = (Button) this.findViewById(R.id.bt_image);

        mBtProgress.setOnClickListener(this);
        mBtImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_progress:
                startActivity(new Intent(this, ProgressBarTest.class));
                break;
            case R.id.bt_image:
                startActivity(new Intent(this, ImageTest.class));
                break;
        }
    }
}
