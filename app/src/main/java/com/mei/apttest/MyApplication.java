package com.mei.apttest;

import com.mei.router.api.MyRouter;

import android.app.Application;

/**
 * @author mxb
 * @date 2022/6/9
 * @desc
 * @desired
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyRouter.getInstance().init(this);
    }
}
