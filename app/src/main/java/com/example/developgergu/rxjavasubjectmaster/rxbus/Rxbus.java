package com.example.developgergu.rxjavasubjectmaster.rxbus;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/** Created by developgergu on 2017/10/13. */
public class Rxbus {
  private static Rxbus rxbus;
  private CompositeDisposable disposable;
  private Subject<Object> mSubject;

  private Rxbus() {
    disposable = new CompositeDisposable();
    mSubject = PublishSubject.create();
  }

  public static Rxbus getInstance() {
    if (rxbus == null) {
      rxbus = new Rxbus();
    }
    return rxbus;
  }

  public void send(Object msg) {
    mSubject.onNext(msg);
  }

  public Disposable reigister(Consumer<Object> onNext) {
    Disposable d =
        mSubject.subscribe(
            onNext, Throwable::printStackTrace, () -> Log.e("TAG", "bus is onComplete!"));
    disposable.add(d);
    return d;
  }

  public boolean unregister(Disposable d) {
    return d == null || disposable.remove(d);
  }

  public void unRegisterAll() {
    if (!disposable.isDisposed()) disposable.clear();
  }
}
