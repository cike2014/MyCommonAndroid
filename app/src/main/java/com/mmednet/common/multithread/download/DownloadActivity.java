package com.mmednet.common.multithread.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.mmednet.common.R;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {


    private ListView mLvFile;

    private List<FileInfo> mFileInfos = new ArrayList<FileInfo>();
    private FileListAdapter mAdapter;
    private static final int FLAG_REQUEST_READ_PHONE = 0x001;
    private NotificationUtil mNotificationUtil;

    private static final String TAG = DownloadActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mNotificationUtil = new NotificationUtil(this);

        mLvFile = (ListView) findViewById(R.id.lv_download);

        initData();
        mAdapter = new FileListAdapter(this, mFileInfos);
        mLvFile.setAdapter(mAdapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISH);
        filter.addAction(DownloadService.ACTION_START);
        registerReceiver(receiver, filter);
    }

    private void initData() {

        mFileInfos.add(new FileInfo(0,"http://www.imooc.com/mobile/imooc.apk", "imooc.apk", 0, 0));
        mFileInfos.add(new FileInfo(1, "http://down.360safe.com/instmobilemgr.exe", "instmobilemgr", 0, 0));
        mFileInfos.add(new FileInfo(2,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
        mFileInfos.add(new FileInfo(3,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
        mFileInfos.add(new FileInfo(4,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
        mFileInfos.add(new FileInfo(5,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
        mFileInfos.add(new FileInfo(6,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
        mFileInfos.add(new FileInfo(7,
                "http://hot.m.shouji.360tpcdn.com/160815/09315e90650231fccd40953c51cc36d9/com.qihoo.appstore_300050185.apk",
                "com.qihoo.appstore_300050185.apk", 0, 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.ACTION_UPDATE)) {
                int finished = intent.getExtras().getInt("finished");
                int id = intent.getIntExtra("id", -1);
                mAdapter.updataView(id, mLvFile, finished);
                mNotificationUtil.updateNotification(id,finished);
            }
            if (intent.getAction().equals(DownloadService.ACTION_FINISH)) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                mAdapter.updataView(fileInfo.getId(), mLvFile, 0);
                Toast.makeText(DownloadActivity.this, fileInfo.getFileName() + "下载结束", Toast.LENGTH_SHORT).show();
               mNotificationUtil.cancelNotification(fileInfo.getId());
            }
            if(intent.getAction().equals(DownloadService.ACTION_START)){
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                mNotificationUtil.showNotification(fileInfo);
            }
        }
    };


}

