package com.mei.router.api;

import com.mei.router.annotation.RouteMeta;

import java.util.Map;

/**
 * @author mxb
 * @date 2022/6/9
 * @desc
 * @desired
 */
public interface IRouteGroup {

    void loadInfo(Map<String, RouteMeta> groupMap);
}
