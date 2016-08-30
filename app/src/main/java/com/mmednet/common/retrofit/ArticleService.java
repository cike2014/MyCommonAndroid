package com.mmednet.common.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alpha on 2016/8/29.
 */
public interface ArticleService {

    @GET("history/content/{pagesize}/{page}")
    Call<Article> listArticles(@Path("pagesize") int pagesize, @Path("page") int page);

    @GET("history/content/2/1")
    Observable<Article> listArticle();

}
