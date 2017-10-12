package com.example.developgergu.rxjavasubjectmaster;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        setContentView(R.layout.activity_test);
        DragObserverFragment fragment =
                (DragObserverFragment)
                        getSupportFragmentManager().findFragmentById(R.id.contain_view);
        if (fragment == null) {
            fragment = DragObserverFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contain_view, fragment)
                    .commit();
        }
    }
}
