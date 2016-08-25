package com.mmednet.common.multithread.download;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mmednet.common.R;

import java.util.List;

/**
 * Created by alpha on 2016/8/19.
 */
public class FileListAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<FileInfo> mFileInfos = null;

    public FileListAdapter(Context mContext, List<FileInfo> mFileInfos) {
        this.mContext = mContext;
        this.mFileInfos = mFileInfos;
    }

    @Override
    public int getCount() {
        return mFileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.listitem,null);
            holder = new ViewHolder();
            holder.mTvFileName = (TextView) convertView.findViewById(R.id.tv_fileName);
            holder.mPbProgress = (ProgressBar) convertView.findViewById(R.id.pb_progress);
            holder.mBtnStart = (Button) convertView.findViewById(R.id.btn_start);
            holder.mBtnStop = (Button) convertView.findViewById(R.id.btn_stop);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final FileInfo info = mFileInfos.get(position);
        holder.mTvFileName.setText(info.getFileName());
        holder.mPbProgress.setProgress(info.getFinished());
        holder.mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", info);
                mContext.startService(intent);
            }
        });
        holder.mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", info);
                mContext.startService(intent);
            }
        });

        return convertView;
    }

    /**
     * 更新某一个item的进度
     * @param posi
     * @param listView
     * @param progress
     */
    public void updataView(int posi, ListView listView,int progress) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.mPbProgress.setProgress(progress);
        }
    }


    static class ViewHolder{
        TextView mTvFileName;
        ProgressBar mPbProgress;
        Button mBtnStart;
        Button mBtnStop;
    }
}
