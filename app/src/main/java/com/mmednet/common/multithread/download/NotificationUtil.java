package com.mmednet.common.multithread.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mmednet.common.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alpha on 2016/8/19.
 */
public class NotificationUtil {

    private NotificationManager mManager = null;

    private Context mConText = null;
    private Map<Integer, Notification> mNotifications = null;

    public NotificationUtil(Context context) {
        this.mConText = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifications = new HashMap<Integer, Notification>();
    }

    public void showNotification(FileInfo fileInfo) {
        if (!mNotifications.containsKey(fileInfo.getId())) {
            //创建通知的对象
            Notification notification = new Notification();
            notification.tickerText = fileInfo.getFileName() + "开始下载";
            notification.when = System.currentTimeMillis();
            notification.icon = R.mipmap.ic_launcher;
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            //为Notification设置intent
            Intent intent = new Intent(mConText, DownloadActivity.class);
            PendingIntent pi = PendingIntent.getActivity(mConText, 0, intent, 0);
            notification.contentIntent = pi;
            //创建远程试图
            RemoteViews remoteViews = new RemoteViews(mConText.getPackageName(), R.layout.notification);
            //开始按钮
            Intent intentStart = new Intent(mConText, DownloadService.class);
            intentStart.setAction(DownloadService.ACTION_START);
            PendingIntent piStart = PendingIntent.getActivity(mConText, 0, intentStart, 0);
            remoteViews.setOnClickPendingIntent(R.id.btn_start, piStart);
            //结束按钮
            Intent intentStop = new Intent(mConText, DownloadService.class);
            intentStop.setAction(DownloadService.ACTION_START);
            PendingIntent piStop = PendingIntent.getActivity(mConText, 0, intentStop, 0);
            remoteViews.setOnClickPendingIntent(R.id.btn_stop, piStop);
            //设置textview
            remoteViews.setTextViewText(R.id.tv_fileName,fileInfo.getFileName());
            //设置Notification的视图
            notification.contentView = remoteViews;
            //发出通知
            mManager.notify(fileInfo.getId(),notification);
            //将通知放入集合中
            mNotifications.put(fileInfo.getId(),notification);
        }

    }

    public void cancelNotification(int id){
        mManager.cancel(id);
        mNotifications.remove(id);
    }

    public void updateNotification(int id,int progress){
        Notification notification = mNotifications.get(id);
        if(notification!=null){
            notification.contentView.setProgressBar(R.id.pb_progress,100,progress,false);
            mManager.notify(id,notification);
        }
    }
}
