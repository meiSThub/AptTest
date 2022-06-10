package com.mei.apttest;

import com.mei.router.api.MyRouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void second(View view) {
        MyRouter.getInstance().navigation(this, "/user/second");
    }

    public void third(View view) {
        MyRouter.getInstance().navigation(this, "/user/third");
    }
}