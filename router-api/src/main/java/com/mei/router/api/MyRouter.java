package com.mei.router.api;

import com.mei.router.annotation.RouteMeta;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;

/**
 * @author mxb
 * @date 2022/6/9
 * @desc
 * @desired
 */
public class MyRouter {

    private static final MyRouter sMyRouter = new MyRouter();

    private Map<String, RouteMeta> mRouteGroup = new HashMap<>();

    private Context mContext;

    public static MyRouter getInstance() {
        return sMyRouter;
    }

    public void init(Application context) {
        this.mContext = context;
        try {
            Class<?> routeGroupClass = Class.forName("com.mei.router.api.RouteGroup");
            IRouteGroup routeGroup = (IRouteGroup) routeGroupClass.newInstance();
            routeGroup.loadInfo(mRouteGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据路由跳转
     */
    public void navigation(Context context, String path) {
        RouteMeta routeMeta = mRouteGroup.get(path);
        Intent intent = new Intent(mContext, routeMeta.targetClass);
        if (context instanceof Activity) {
            context.startActivity(intent);
        }
    }

    /**
     * 根据路由跳转
     */
    public void navigation(String path) {
        RouteMeta routeMeta = mRouteGroup.get(path);
        Intent intent = new Intent(mContext, routeMeta.targetClass);
        ActivityCompat.startActivity(mContext, intent, null);
    }
}
