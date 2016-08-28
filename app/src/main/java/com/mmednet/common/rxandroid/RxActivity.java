package com.mmednet.common.rxandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mmednet.common.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = RxActivity.class.getSimpleName();

    private Button mBtLoad;

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        mBtLoad = (Button) findViewById(R.id.bt_load);
        mBtLoad.setOnClickListener(this);

        mIv = (ImageView) findViewById(R.id.iv);
//        obserableCreate();
//
//        observableSimple();

//        testObservableFrom();

//        testObservableIterate();
    }

    private void testObservableIterate() {
        List<String> names = new ArrayList<String>();
        names.add("andy");
        names.add("john");

        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,s);
            }
        });
    }

    private void testObservableFrom() {
        String[] names = new String[]{"andy","john"};

        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,s);
            }
        });
    }


    /**
     * 更简单的一种方式
     */
    private void observableSimple() {
        Observable<String> observable = Observable.just("小机器人，给我一份米饭");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,s);
            }
        });
    }

    /**
     * 比较复杂的观察者和被观察者
     */
    private void obserableCreate() {
        /**
         * 观察者。相当于门口的机器人
         */
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"主人给了我点命令:"+s);
            }
        };

        /**
         * 被观察者。
         */
        Observable<String> observable =Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("机器人给我装点米饭");
            }
        });
        /**
         * 注册
         */
        observable.subscribe(subscriber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_load:

//                testLoadImage();
//                testLoadImage2();

                break;
        }
    }

    private void testLoadImage2() {
        final int resId = R.mipmap.ic_launcher;

        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resId);

                try{
                    subscriber.onNext(bitmap);
                }catch (Exception e){
                    subscriber.onError(e);
                }finally{
                    subscriber.onCompleted();
                }
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mIv.setImageBitmap(bitmap);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(RxActivity.this, "出现问题:"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Action0() {
            @Override
            public void call() {
                Toast.makeText(RxActivity.this, "执行完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testLoadImage() {
        final int resId = R.mipmap.ic_launcher;

        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resId);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxActivity.this,"加载完成了",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {

                mIv.setImageBitmap(bitmap);

            }
        });
    }
}
