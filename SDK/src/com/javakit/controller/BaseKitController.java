package com.javakit.controller;

import com.javakit.data.exception.NetError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cdts on 21/03/2017.
 */
public class BaseKitController {

    public Map<String, Object> getResult(Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", NetError.Success.getCode());
        map.put("data", object);
        return map;
    }
    public Map<String, Object> getResultSuccess() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", NetError.Success.getCode());
        map.put("data", new Object());
        return map;
    }
}
