package com.mmednet.common.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * Created by alpha on 2016/8/18.
 */
public class SdcardUtil {
    /**
     * 判断SDcard是否可以用
     *
     * @return true 可用 false 不可用
     */
    public static boolean isSdCardExists() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            Log.e("-->Eastedge", "the Sdcard is not exists");
            return false;
        }
    }

    /***
     * 获取SD卡总大小 需加入权限: <uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * ></uses-permission>
     *
     * @return String 单位M 若为返回null或者空,则sdcard不能使用或无Sdcard
     */
    public static String getSdCardTotalSize() {
        if (!isSdCardExists())
            return "";
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();

        long availableBlocks = stat.getBlockCount();

        return String.valueOf((availableBlocks * blockSize) / 1024 / 1024);
    }

    /**
     * 获取SD卡可用大小 需加入权限:<uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * ></uses-permission>
     *
     * @return 单位M
     *
     */
    public static String getSdcardAvailbleSize() {
        if (!isSdCardExists())
            return "";
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return String.valueOf((availableBlocks * blockSize) / 1024 / 1024);
    }

    /**
     * 创建一个文件夹
     *
     * @return
     */
    public static boolean createMkdir() {
        return true;
    }

    public static boolean createFile() {
        return true;
    }

    /***
     * 获取根目录路径
     *
     * @return String
     */
    public static String getRootPath() {
        if (!isSdCardExists())
            return "";
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取根目录文件列表
     *
     * @return List<File>
     */
    public static File[] getRootFiles() {
        if (getRootPath().equals(""))
            return null;
        List<File> files = new ArrayList<File>();
        File file = new File(getRootPath());
        return file.listRoots();
    }

    public static String GetAllSDPath() {
        String strMountInfo = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (strMountInfo != null && !"".equals(strMountInfo)) {
            File file = new File(strMountInfo);
            if (file.canWrite()) {
                return strMountInfo;
            }
        }

        // 1.首先获得系统已加载的文件系统信息
        try {
            // 创建系统进程生成器对象
            ProcessBuilder objProcessBuilder = new ProcessBuilder();
            // 执行 mount -h 可以看到 mount : list mounted filesystems
            // 这条命令可以列出已加载的文件系统
            objProcessBuilder.command("mount"); // 新的操作系统程序和它的参数
            // 设置错误输出都将与标准输出合并
            objProcessBuilder.redirectErrorStream(true);
            // 基于当前系统进程生成器的状态开始一个新进程，并返回进程实例
            Process objProcess = objProcessBuilder.start();
            // 阻塞线程至到本地操作系统程序执行结束，返回本地操作系统程序的返回值
            objProcess.waitFor();
            // 得到进程对象的输入流，它对于进程对象来说是已与本地操作系统程序的标准输出流(stdout)相连接的
            InputStream objInputStream = objProcess.getInputStream();
            byte[] buffer = new byte[1024];
            // 读取 mount 命令程序返回的信息文本
            while (-1 != objInputStream.read(buffer)) {
                strMountInfo = strMountInfo + new String(buffer);
            }
            // 关闭进程对象的输入流
            objInputStream.close();
            // 终止进程并释放与其相关的任何流
            objProcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2.然后再在系统已加载的文件系统信息里查找 SD 卡路径
        // mount 返回的已加载的文件系统信息是以一行一个信息的形式体现的，
        // 所以先用换行符拆分字符串
        String[] lines = strMountInfo.split("\n");
        // 清空该字符串对象，下面将用它来装载真正有用的 SD 卡路径列表
        strMountInfo = "";
        for (int i = 0; i < lines.length; i++) {
            // 如果该行内有 /mnt/和 vfat 字符串，说明可能是内/外置 SD 卡的挂载路径
            if (-1 != lines[i].indexOf(" vfat ") || -1 != lines[i].indexOf(" sdcardfs ")) // 前后均有空格
            {
                // 再以空格分隔符拆分字符串
                String[] blocks = lines[i].split("\\s"); // \\s 为空格字符
                if (blocks.length > 1 && !"".equals(blocks[1])) {
                    File file = new File(blocks[1]);
                    if (file.canWrite()) {
                        return blocks[1];
                    }
                }
            }
        }
        return strMountInfo;
    }
}
