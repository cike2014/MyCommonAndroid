package com.mmednet.common.rxjava;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mmednet.common.R;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by alpha on 2016/9/7.
 */
public class MyView {

    private void test(){
        Observable.from(new String[]{"andy","john"}).create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    Action0 onCompleteAction = new Action0() {
        @Override
        public void call() {

        }
    };

    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {

        }
    };

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {

        }
    };

    private void test2(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).subscribe(onNextAction,onErrorAction,onCompleteAction);
    }

    private void test3(){
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).

                subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {

            }
        });
    }

    public void test3(final Context context){
        final int resId = R.mipmap.ic_launcher;
        Observable.just(resId).map(new Func1<Integer, Bitmap>() {
            @Override
            public Bitmap call(Integer integer) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                return bitmap;
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {

            }
        }, null, new Action0() {
            @Override
            public void call() {

            }
        });
    }

    public void test4(){
        String[] arrs = new String[]{"1","2","3"};
        Observable.just(arrs).map(new Func1<String[], List>() {
            @Override
            public List call(String[] strings) {
                return null;
            }
        }).subscribe(new Subscriber<List>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List list) {

            }
        });
    }

}
