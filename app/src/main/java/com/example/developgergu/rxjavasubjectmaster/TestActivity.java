package com.example.developgergu.rxjavasubjectmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.contain_view);
        if (fragment == null) {
            fragment = MapFragment.newInstance("scan", "0");
            getSupportFragmentManager().beginTransaction().add(R.id.contain_view, fragment).commit();
        }
    }
}
