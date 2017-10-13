package com.example.developgergu.rxjavasubjectmaster;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.developgergu.rxjavasubjectmaster.rxbus.Rxbus;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OperationsFragment extends Fragment implements View.OnClickListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  public static final String TAG = ContentValues.TAG;

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public OperationsFragment() {
    // Required empty public constructor
  }

  // TODO: Rename and change types and number of parameters
  public static OperationsFragment newInstance(String param1, String param2) {
    OperationsFragment fragment = new OperationsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_scan, container, false);
    Button button = view.findViewById(R.id.button4);
    RxView.clicks(button).subscribe(o -> syncMultiZip());
    // 注册rxbus
    registerRxbus();
    Button send = view.findViewById(R.id.button5);
    RxView.clicks(send).subscribe(o -> sendBusMsg(1));
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unRegisterBus();
  }

  public void sendBusMsg(int msg) {
    Log.e(TAG, "click ! msg : " + msg);
    Rxbus.getInstance().send(msg);
  }

  public void testChangeSchedule() {
    Observable.create(
            (ObservableOnSubscribe<Integer>)
                e -> {
                  for (int i = 0; i < 20; i++) {
                    Log.e(TAG, "create: " + i + "," + Thread.currentThread());
                    e.onNext(i);
                  }
                  e.onComplete();
                })
        //                .subscribeOn(Schedulers.io())
        .doOnNext(integer -> Log.e(TAG, "doOnNext1: " + integer + "," + Thread.currentThread()))
        //                .observeOn(AndroidSchedulers.mainThread())
        .map(
            integer -> {
              Log.e(TAG, "map: " + integer + "," + Thread.currentThread());
              return integer;
            })
        .doOnNext(integer -> Log.e(TAG, "doOnNext2: " + integer + "," + Thread.currentThread()))
        .subscribe(s -> Log.e(TAG, "accept : " + s + "," + Thread.currentThread()));
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  public void syncZip() {
    Observable.zip(
            getSource(),
            Observable.interval(3, TimeUnit.SECONDS),
            (integer, aLong) -> integer + ",aLong = " + aLong)
        .subscribe(s -> Log.e(TAG, ": zip s= " + s));
  }

  private Disposable disposable;

  public void registerRxbus() {
    disposable = Rxbus.getInstance().reigister(i -> Log.e(TAG, "receive rxbus msg : i= " + i));
  }

  public void unRegisterBus() {
    if (disposable != null) {
      boolean res = Rxbus.getInstance().unregister(disposable);
      Log.e(TAG, "unRegisterBus: result is " + res);
    }
  }

  public void compose() {}

  public void syncMultiZip() {
    Observable.zip(
            getSource(1).subscribeOn(Schedulers.io()),
            getSource(2).subscribeOn(Schedulers.io()),
            getSource(3).subscribeOn(Schedulers.io()),
            Observable.interval(3, TimeUnit.SECONDS),
            (integer, integer2, integer3, aLong) ->
                "(" + integer + "," + integer2 + "," + integer3 + "), time=" + aLong)
        .subscribe(s -> Log.e(TAG, "result : " + s));
  }

  public void asyncZip() {
    getSource()
        .flatMap(integer -> Observable.timer(3, TimeUnit.SECONDS).map(aLong -> integer))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(integer -> Log.e(TAG, "asyncZip: time " + integer));
  }

  public void testSync() {
    Observable.create(
            (ObservableOnSubscribe<Integer>)
                e -> {
                  for (int i = 0; i < 5; i++) {
                    Log.e(TAG, "onCreate : " + i + "," + Thread.currentThread());
                    e.onNext(i);
                  }
                })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(
            integer -> {
              Log.e(TAG, "flatMap: integer" + integer + "," + Thread.currentThread());
              return Observable.zip(Observable.just(integer), Observable.just(1), (i, aLong) -> i);
            })
        .subscribe(o -> Log.e(TAG, "testSync: o= " + o + "," + Thread.currentThread()));
    Log.e(TAG, "testSync: ----over!----");
  }

  public Observable<Integer> getSource(int groupId) {
    Integer[] source = new Integer[5];
    for (int i = 0; i < source.length; i++) {
      source[i] = i;
    }
    return Observable.create(
        e -> {
          for (int i = 0; i < source.length; i++) {
            Log.e(TAG, "group-" + groupId + "- , emit-" + i + "-");
            e.onNext(source[i]);
          }
          e.onComplete();
        });
  }

  public Observable<Integer> getSource() {
    Integer[] source = new Integer[5];
    for (int i = 0; i < source.length; i++) {
      source[i] = i;
    }
    return Observable.fromIterable(Arrays.asList(source));
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button4) {
      //      asyncZip();
      asyncZip();
    }
  }

  public void MergeContainsErro() {
    Observable<String> ob1 =
        Observable.create(
            e -> {
              e.onNext("1");
              e.onNext("22");
              e.onNext("333");
              e.onNext("4444");
              e.onError(new Throwable("throw a error"));
            });
    Observable<String> ob2 =
        Observable.create(
            e -> {
              e.onNext("1");
              e.onNext("22");
              e.onNext("333");
              e.onNext("4444");
              e.onComplete();
            });
    Observable.mergeDelayError(ob1, ob2)
        .subscribe(
            new Observer<String>() {
              @Override
              public void onSubscribe(@NonNull Disposable d) {}

              @Override
              public void onNext(@NonNull String s) {
                Log.e(TAG, "onNext: " + s);
              }

              @Override
              public void onError(@NonNull Throwable e) {
                e.printStackTrace();
              }

              @Override
              public void onComplete() {
                Log.e(TAG, "onComplete");
              }
            });
  }
}
