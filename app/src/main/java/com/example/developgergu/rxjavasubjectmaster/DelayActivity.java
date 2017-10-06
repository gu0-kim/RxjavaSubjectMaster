package com.example.developgergu.rxjavasubjectmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static android.util.Log.e;

public class DelayActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private Integer n = new Integer(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
//        fun();
        deferredFun();
    }

    private void fun() {
        Observable<Integer> observable = getInt();
        Log.e(TAG, "onCreate: --before subscribe--");
        n = 11;
        subscribe(observable);
    }

    private void deferredFun() {
        Observable<Integer> deferred = Observable.defer(this::getInt);
        Log.e(TAG, "onCreate: --before subscribe--");
        n = 11;
        subscribe(deferred);
    }

    private void subscribe(Observable<Integer> observable) {
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                e(TAG, "onComplete");
            }
        });
    }


    private Observable<Integer> getInt() {
        return Observable.just(n);
    }
}
