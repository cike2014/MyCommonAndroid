package com.mmednet.common.utils;

import java.io.File;
import java.util.Hashtable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by alpha on 2016/8/18.
 */
public class Utils {
    public static File initProductDir(AppInfo info) {
        String userDir = "";
        if (SdcardUtil.isSdCardExists()) {
            userDir = Environment.getExternalStorageDirectory().toString() + System.getProperty("file.separator");
        } else {
            userDir = System.getProperty("file.separator");
        }
        File file = new File(userDir + info.getProductName() + System.getProperty("file.separator") + ".data"
                + System.getProperty("file.separator"));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File initTempDir(AppInfo info) {
        String userDir = "";
        if (SdcardUtil.isSdCardExists()) {
            userDir = Environment.getExternalStorageDirectory().toString() + System.getProperty("file.separator");
        } else {
            userDir = System.getProperty("file.separator");
        }
        File file = new File(userDir + info.getProductName() + System.getProperty("file.separator") + "temp"
                + System.getProperty("file.separator"));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int height) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        Log.e("", "listAdapter.getCount() = " + listAdapter.getCount());

        int totalHeight = 0;
        int tmp = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            tmp = listItem.getMeasuredHeight();
        }
        totalHeight += height;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static Hashtable mtables;

    public static void setTables(Hashtable tables) {
        mtables = tables;
    }

    public static Hashtable getTables() {
        return mtables;
    }

    /**
     *
     * @param name
     *            闇€瑕佸皬鍐椤舰寮?
     * @return
     */
    public static String getRealname(String name) {
        String ret = "";
        if (mtables == null) {
            ret = null;
        } else {
            ret = (String) mtables.get(name);
        }
        if (ret == null) {
            return "";
        }
        return ret;
    }

    public static int getLayout(Context context, String name) {
        int layout = context.getResources().getIdentifier(name + "ll", "layout", context.getPackageName());
        if (layout == 0) {
            layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
        }
        return layout;
    }

    /**
     * 灏唅p镄勬暣鏁板舰寮忚浆鎹㈡垚ip褰㈠纺
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 銮峰彇褰揿墠ip鍦板潃
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return "127.0.0.1";
        }
    }
}
