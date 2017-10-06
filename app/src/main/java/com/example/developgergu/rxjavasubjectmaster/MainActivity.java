package com.example.developgergu.rxjavasubjectmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    PublishSubject<String> publishSubject;
    Disposable dp;
    int index;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        publishSubject = PublishSubject.create();
    }

    public void start(View view) {
        funSubject();
    }

    public void emit(View view) {
        publishSubject.onNext("emit:" + index++);
    }

    public void complete(View view) {
        publishSubject.onComplete();
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

            }
        });

    }

    private void funSubject() {

        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                dp = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                Toast.makeText(MainActivity.this, "onNext:" + s, Toast.LENGTH_SHORT).show(); //接收到 as Bridge
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
                if (!dp.isDisposed()) {
                    dp.dispose();
                }
                Toast.makeText(MainActivity.this, "onComplete", Toast.LENGTH_SHORT).show(); //接收到 as Bridge
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!dp.isDisposed()) {
            dp.dispose();
        }
    }
}
