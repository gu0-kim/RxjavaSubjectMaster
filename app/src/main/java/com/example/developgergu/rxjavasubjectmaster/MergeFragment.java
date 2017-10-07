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

import io.reactivex.Observable;


public class MergeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = ContentValues.TAG;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MergeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MergeFragment newInstance(String param1, String param2) {
        MergeFragment fragment = new MergeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button button = view.findViewById(R.id.button4);
        button.setOnClickListener(this);
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


    public void Merge() {
        Observable ob = Observable.just("1", "22", "333", "4444", "55555");
        Observable obr = Observable.just("555555", "4444", "333", "22", "1");
        Observable.merge(ob, obr).subscribe(o -> {
            Log.d(TAG, "Merge: o");
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button4) {
            Merge();
        }
    }
}