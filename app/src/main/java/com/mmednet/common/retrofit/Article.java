package com.mmednet.common.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alpha on 2016/8/29.
 */
public class Article {

    @SerializedName("error")
    public boolean error;

    @SerializedName("results")
    public List<Result> results;

    class Result {
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
            return "Result{" +
                    "_id='" + _id + '\'' +
                    ", content='" + content + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Article{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
