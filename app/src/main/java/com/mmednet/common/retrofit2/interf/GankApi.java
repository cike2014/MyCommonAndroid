package com.mmednet.common.retrofit2.interf;

import com.mmednet.common.retrofit2.Gank;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alpha on 2016/8/31.
 */
public interface GankApi{

    @GET("history/content/{pagesize}/{page}")
    Observable<Gank> getArticles(@Path("pagesize") int pagesize, @Path("page") int page);
}
