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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


public class JoinFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = ContentValues.TAG;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CompositeDisposable cd;
    List<String> apps = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");

    public JoinFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JoinFragment newInstance(String param1, String param2) {
        JoinFragment fragment = new JoinFragment();
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
        cd = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button button = view.findViewById(R.id.button4);
        button.setOnClickListener(this);
        Button button1 = view.findViewById(R.id.button5);
        button1.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button4) {
            join();
        } else if (view.getId() == R.id.button5) {
            cancel();
        }
    }

    public void join() {
        Observable<String> appsSequence = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .map(position -> {
                    Log.d(TAG, "Position: " + position);
                    return apps.get(position.intValue());
                });
        Observable<Long> tictoc = Observable.interval(1000, TimeUnit.MILLISECONDS);

        cd.add(appsSequence
                .join(
                        tictoc,
                        appInfo -> Observable.timer(2, TimeUnit.SECONDS),
                        time -> Observable.timer(0, TimeUnit.SECONDS),
                        this::updateTitle
                )
                .observeOn(AndroidSchedulers.mainThread())
                .take(22).subscribeWith(getObserver()));
    }


    private String updateTitle(String appInfo, Long time) {
        return time + " " + appInfo;
    }

    public void cancel() {
        cd.clear();
    }

    public DisposableObserver<String> getObserver() {
        return new DisposableObserver<String>() {

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
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        cancel();
    }
}