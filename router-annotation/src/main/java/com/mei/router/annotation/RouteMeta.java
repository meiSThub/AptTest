package com.mei.router.annotation;

/**
 * @author mxb
 * @date 2022/6/9
 * @desc
 * @desired
 */
public class RouteMeta {

    public String path;

    public Class targetClass;

    public RouteMeta(String path, Class targetClass) {
        this.path = path;
        this.targetClass = targetClass;
    }
}
