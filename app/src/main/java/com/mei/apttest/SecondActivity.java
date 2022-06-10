package com.mei.apttest;

import com.mei.router.annotation.Route;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

@Route(path = "/user/second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}