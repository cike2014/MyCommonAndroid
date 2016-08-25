package com.mmednet.common.multithread.download;

import java.io.Serializable;

/**
 * Created by alpha on 2016/8/18.
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 2661794860150836098L;
    private int id;
    private String fileName;
    private String url;
    private int length;
    private int finished;

    public FileInfo() {

    }

    public FileInfo(int id, String url,String fileName,  int length, int finished) {
        this.id = id;
        this.finished = finished;
        this.length = length;
        this.url = url;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return String.format("fileinfo:[id:%s,filename:%s,url:%s,length:%s,finished:%s]", id, fileName, url, length, finished);
    }
}
