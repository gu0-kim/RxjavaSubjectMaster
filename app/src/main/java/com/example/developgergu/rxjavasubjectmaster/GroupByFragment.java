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

import com.jakewharton.rxbinding2.view.RxView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class GroupByFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = ContentValues.TAG;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupByFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GroupByFragment newInstance(String param1, String param2) {
        GroupByFragment fragment = new GroupByFragment();
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
        RxView.drags(button)
                .subscribe(
                        dragEvent -> {
                            Log.e(TAG, "dragEvent : " + dragEvent.getAction());
                        });

        return view;
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

    public void compose() {}

    public void syncMultiZip() {
        Observable.zip(
                        getSource(),
                        getSource(),
                        getSource(),
                        Observable.interval(3, TimeUnit.SECONDS),
                        (integer, integer2, integer3, aLong) ->
                                "integer="
                                        + integer
                                        + ",integer2="
                                        + integer2
                                        + ",integer3="
                                        + integer3
                                        + ",time="
                                        + aLong)
                .subscribe(s -> Log.e(TAG, "syncMultiZip: result : " + s));
    }

    public void asyncZip() {
        getSource()
                .flatMap(
                        integer ->
                                //                Observable.timer(integer, TimeUnit.SECONDS))
                                //                Observable.timer(integer,
                                // TimeUnit.SECONDS).map(aLong -> integer))
                                Observable.just(integer).delay(3, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Integer>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {}

                            @Override
                            public void onNext(@NonNull Integer integer) {
                                Log.d(TAG, "onNext: integer=" + integer);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
    }

    public void testSync() {
        getSource()
                .flatMap(
                        integer ->
                                Observable.zip(
                                        Observable.just(integer),
                                        Observable.timer(3, TimeUnit.SECONDS),
                                        (i, aLong) -> i))
                .subscribe(o -> Log.e(TAG, "testSync: o= " + o));
        Log.e(TAG, "testSync: ----over!----");
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
