package com.mmednet.common.retrofit2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.Subscription;

public class BaseActivity extends AppCompatActivity {

    protected Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
