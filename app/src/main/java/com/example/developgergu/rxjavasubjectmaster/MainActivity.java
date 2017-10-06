package com.example.developgergu.rxjavasubjectmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void fun() {
        PublishSubject<String> publishSubject = PublishSubject.create();
        Observable.create(new ObservableOnSubscribe<String>() {
                              @Override
                              public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                  e.onNext("as Bridge");
                                  e.onComplete();
                              }
                          }
        ).subscribe(publishSubject);

        publishSubject.subscribe(new Observer<String>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(String s) {

                Log.e("TAG", "subject:" + s); //接收到 as Bridge
            }
        });

    }
    private void funSubject(){
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("hello");
        publishSubject.onNext("world!");
        publishSubject.onComplete();
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
