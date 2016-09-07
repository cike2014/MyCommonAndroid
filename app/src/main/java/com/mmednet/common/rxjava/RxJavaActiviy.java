package com.mmednet.common.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxJavaActiviy extends AppCompatActivity {


    private static final String TAG=RxJavaActiviy.class.getSimpleName();
    @Bind(R.id.bt_rxjava)
    Button mBtRxJava;


    private List<Student> students=new ArrayList<Student>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.bt_rxjava)
    void btRxJavaClick(View view) {
        //都打印出来
//        Observable.just(students).subscribe(new Subscriber<List<Student>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<Student> students) {
//                Log.d(TAG,"students:"+students);
//            }
//        });

        Observable.from(students).flatMap(new Func1<Student, Observable<Student.Course>>() {
            @Override
            public Observable<Student.Course> call(Student student) {
                return Observable.from(student.getCourses());
            }
        }).subscribe(new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student.Course cou) {
                Log.d(TAG,"cou:"+cou.getCname());
            }
        });


    }

    private void initData() {
        for (int i=0; i < 10; i++) {
            Student d=new Student();
            d.setId(i);
            d.setSname("student" + i);

            List<Student.Course> courses=new ArrayList<Student.Course>();

            for (int j=0; j < 3; j++) {
                Student.Course course=d.new Course();
                course.setId(j);
                course.setCname("course" + j);
                courses.add(course);
            }
            d.setCourses(courses);
            students.add(d);
        }
    }
}
