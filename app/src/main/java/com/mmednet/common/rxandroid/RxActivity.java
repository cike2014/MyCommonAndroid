package com.mmednet.common.rxandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mmednet.common.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = RxActivity.class.getSimpleName();

    private Button mBtLoad;

    private ImageView mIv;

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        mBtLoad = (Button) findViewById(R.id.bt_load);
        mBtLoad.setOnClickListener(this);

        mIv = (ImageView) findViewById(R.id.iv);

        initData();
//        obserableCreate();
//
//        observableSimple();

//        testObservableFrom();

//        testObservableIterate();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_load:

//                testLoadImage();
//                testLoadImage2();
//                testThreadLoadImage();
//                testMap();
                testFlatMap();
                break;
        }
    }

    private void initData() {

        List<Course> courses = new ArrayList<Course>();

        courses.add(new Course(1, "Math"));
        courses.add(new Course(2, "Chinese"));

        student = new Student(1, "Andy", courses);


    }

    /**
     * 被观察者经过过滤后向观察者发送消息
     */
    private void testFilter (){
        Observable.just(1,2,3,4,5,6).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer<5;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }

    private  void testFlatMap(){
        Observable.just(student).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.courses);
            }
        }).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d(TAG,"course:"+course.cname);
            }
        });
    }

    private void testMap2() {
        Observable.just(student).map(new Func1<Student, List<Course>>() {
            @Override
            public List<Course> call(Student student) {
                List<Course> courses = student.courses;
                return courses;
            }
        }).subscribe(new Observer<List<Course>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Course> courses) {
                for (int i = 0; i < courses.size(); i++) {
                    Log.d(TAG, "course:" + i + ":" + courses.get(i).cname);
                }
            }
        });
    }

    private void testMap() {
        int resid = R.mipmap.ic_launcher;

        Observable.just(resid).map(new Func1<Integer, Bitmap>() {//map方式转换
            @Override
            public Bitmap call(Integer integer) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), integer);
                return bitmap;
            }
        }).subscribe(new Observer<Bitmap>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxActivity.this, "加载完成", Toast.LENGTH_LONG).show();
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


    private void testThreadLoadImage() {
        final int resId = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Log.d(TAG, "当前线程:" + Thread.currentThread().getName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                subscriber.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io()).//在io线程执行网络耗时操作。
                observeOn(AndroidSchedulers.mainThread()).//在主线程进行UI操作
                subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.d(TAG, "当前线程onNext:" + Thread.currentThread().getName());
                        mIv.setImageBitmap(bitmap);
                    }
                });
    }

    private void testObservableIterate() {
        List<String> names = new ArrayList<String>();
        names.add("andy");
        names.add("john");

        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, s);
            }
        });
    }

    private void testObservableFrom() {
        String[] names = new String[]{"andy", "john"};

        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, s);
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
                Log.d(TAG, s);
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
                Log.d(TAG, "主人给了我点命令:" + s);
            }
        };

        /**
         * 被观察者。
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
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


    private void testLoadImage2() {
        final int resId = R.mipmap.ic_launcher;

        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

                try {
                    subscriber.onNext(bitmap);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
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
                Toast.makeText(RxActivity.this, "出现问题:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxActivity.this, "加载完成了", Toast.LENGTH_SHORT).show();
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
