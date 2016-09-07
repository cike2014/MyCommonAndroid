package com.mmednet.common.retrofit2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alpha on 2016/8/31.
 */
public class Gank {
    @SerializedName("error")
    public boolean error;
    @SerializedName("results")
    public List<Content> results;

    class Content {

        @SerializedName("_id")
        public String _id;
        @SerializedName("content")
        public String content;
        @SerializedName("publishedAt")
        public String publishedAt;
        @SerializedName("title")
        public String title;

        @Override
        public String toString() {
            return "Content{" +
                    "_id='" + _id + '\'' +
                    ", content='" + content + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Gan{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
