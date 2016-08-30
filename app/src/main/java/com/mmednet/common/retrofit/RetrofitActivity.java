package com.mmednet.common.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mmednet.common.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitActivity extends AppCompatActivity {

    private Button mBtHelloRetrofit;
    private Button mBtRxJavaRetrofit;
    private TextView mTvResult;

    private static final String TAG = RetrofitActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mBtHelloRetrofit = (Button) this.findViewById(R.id.bt_hello_retrofit);
        mBtRxJavaRetrofit = (Button) this.findViewById(R.id.bt_rxjava_retrofit);
        mTvResult = (TextView) this.findViewById(R.id.tv_result);


        mBtRxJavaRetrofit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://gank.io/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                ArticleService articleService = retrofit.create(ArticleService.class);
                articleService.listArticle().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Article>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(RetrofitActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(RetrofitActivity.this, "e:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Article article) {
                                mTvResult.setText(article.toString());
                            }
                        });
            }
        });


        mBtHelloRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://gank.io/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ArticleService service = retrofit.create(ArticleService.class);
                Call<Article> articles = service.listArticles(5, 1);

                // 异步调用
                articles.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        Article data = response.body();
                        Log.d(TAG, "data:" + data.toString());
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Log.e(TAG, Log.getStackTraceString(t));
                    }
                });
            }
        });
    }
}
