package com.mmednet.common.process.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.mmednet.common.R;

public class IPCActivity extends AppCompatActivity {

    private static final String TAG = IPCActivity.class.getSimpleName();
    private Button mBtSend;
    private Messenger mServiceMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);
        mBtSend = (Button) findViewById(R.id.bt_send);
        Intent intent = new Intent(this,IPCService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch(msg.what){
                case IPCService.ACTION_FINISH:
                    String replyMsg = msg.getData().getString("replyMsg");
                    Log.d(TAG,"客户端收到服务端的反馈了："+replyMsg);
                    break;
            }


        }
    };

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Message msg = Message.obtain(null,IPCService.ACTION_DOWNLOAD);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "a client");
            msg.setData(bundle);

            Messenger clientMessenger = new Messenger(mHandler);
            msg.replyTo = clientMessenger;
            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                Log.d(TAG,Log.getStackTraceString(e));
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
