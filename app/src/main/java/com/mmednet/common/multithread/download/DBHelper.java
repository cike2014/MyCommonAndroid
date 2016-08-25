package com.mmednet.common.multithread.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alpha on 2016/8/18.
 */

/**
 * 数据库帮助类
 *
 * @author Yann
 * @date 2015-8-8 上午10:48:31
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
            "thread_id integer, url text, start integer, end integer, finished integer)";
    private static final String SQL_DROP = "drop table if exists thread_info";
    private static DBHelper dbHelper = null;


    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (null == dbHelper) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

}
