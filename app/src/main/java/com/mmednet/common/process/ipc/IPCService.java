package com.mmednet.common.process.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class IPCService extends Service {
    private static  final String TAG = IPCService.class.getSimpleName();
    public static final int ACTION_DOWNLOAD = 0x001;
    public static final int ACTION_FINISH = 0x002;
    private Timer timer = new Timer();
    private Handler mServiceHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case ACTION_DOWNLOAD:
                    Log.d(TAG,"the client's message:"+msg.getData().getString("msg"));
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d(TAG,"我在执行一个特殊的操作:"+System.currentTimeMillis());
                        }
                    },1000,1000);
                    Messenger clientMessenger = msg.replyTo;
                    Message replyMessage = Message.obtain(null,ACTION_FINISH);
                    Bundle bundle = new Bundle();
                    bundle.putString("replyMsg","服务端收到你的邀请了，谢谢你");
                    replyMessage.setData(bundle);
                    try {
                        clientMessenger.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }


        }
    };
    public IPCService() {
    }

    Messenger messenger = new Messenger(mServiceHandler);
    @Override
    public IBinder onBind(Intent intent) {

        return messenger.getBinder();
    }

}
