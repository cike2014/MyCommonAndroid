package com.mmednet.common.multithread.download;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class ThreadDAOImpl implements ThreadDAO {
    private DBHelper mHelper = null;

    public ThreadDAOImpl(Context context) {
        mHelper = DBHelper.getInstance(context);
    }


    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(),
                        threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinished()});
        db.close();
    }


    @Override
    public synchronized void deleteThread(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url = ? ",
                new Object[]{url});
        db.close();
    }


    @Override
    public synchronized void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
                new Object[]{finished, url, thread_id});
        db.close();
    }


    @Override
    public List<ThreadInfo> getThreads(String url) {
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }


    @Override
    public synchronized  boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        boolean exists = false;
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url, thread_id + ""});
        if(cursor.getCount()>0){
            exists = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return exists;
    }
}
