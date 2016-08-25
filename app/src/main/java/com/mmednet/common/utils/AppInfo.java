package com.mmednet.common.utils;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * Created by alpha on 2016/8/18.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.UUID;

/**
 * @ClassName: AppInfo
 * @Description: 该应用信息
 * @date Sep 6, 2015 12:47:15 PM
 */
public class AppInfo {

    private String local;
    private int versionCode;
    private String versionName;
    private String deviceId;
    private File productPath;
    private File tempPath;
    private String userAgent;
    private String productName;
    private String screct_key = "screct";

    private ApplicationInfo applicationInfo;

    public AppInfo(Context context) {
        this.local = Locale.getDefault().toString();
        this.versionCode = Integer.MAX_VALUE;
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            this.versionCode = pinfo.versionCode;
            this.versionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            this.versionCode = Integer.MAX_VALUE;
        }
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            this.productName = applicationInfo.metaData.getString("XMHT_APPNAME");
            if (this.productName == null) {
                throw new IllegalArgumentException("meta-data XMHT_APPNAME not in AndroidManifest.xml");
            }
        } catch (NameNotFoundException e) {
            throw new IllegalArgumentException("meta-data XMHT_APPNAME not in AndroidManifest.xml");
        }
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceUuidString = deviceUuid.toString();

        SharedPreferences deviceSP = context.getSharedPreferences("DEVICE_UNIQ_FILE", Context.MODE_PRIVATE);
        String tempDevice = deviceSP.getString("DEVICE_UNIQ_FILE", "");
        if ("".equals(tempDevice)) {// new device
            Editor edit = deviceSP.edit();
            edit.putString("DEVICE_UNIQ_FILE", deviceUuidString);
            edit.commit();
            this.deviceId = deviceUuidString;
        } else if (tempDevice.equals(deviceUuidString)) {
            this.deviceId = tempDevice;
        } else {
            this.deviceId = tempDevice;
        }

        // "TextCutieAndroid/2.5.3-59 2.3.7;Google Nexus S;10"
        this.userAgent = String.format("%s/%s-%s %s-%s;%s;%s;%s", "TextCutieAndroid", versionName, versionCode,
                android.os.Build.VERSION.RELEASE, android.os.Build.VERSION.SDK_INT, android.os.Build.MODEL,
                this.deviceId, this.local);
        this.productPath = Utils.initProductDir(this);
        this.tempPath = Utils.initTempDir(this);
    }

    public String getStringVersionCode() {
        return Integer.toString(versionCode);
    }

    public String getLocal() {
        return local;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getScrect_key() {
        return screct_key;
    }

    public String getProductName() {
        return productName;
    }

    public void setScrect_key(String screct_key) {
        if (screct_key != null && !screct_key.equals("")) {
            this.screct_key = screct_key;
        }
    }

    public File getProductPath() {
        return productPath;
    }

    public File getTempPath() {
        return tempPath;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

}
