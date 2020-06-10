package com.example.demo.guava.eventbus.events;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 小苗学java第一天，map创建
 *
 * @author miaoshaodong
 * @date Creater in 16:04 2019/12/4
 */
public class MultipleEventBusExample {
    private static final int DEFAULT_SIZE = 16;
    private static final String USER_KEY = "name";
    public static void main(String[] args) {
        //第一种map创建方式
        Map<String, Object> mapTrain = new HashMap<>(DEFAULT_SIZE);
        mapTrain.put("name", "admin");
        mapTrain.put("password", 123456);
        //第二张map创建方式适合5个默认key-value的默认存储
        Map<String, Object> mapTest = ImmutableMap.of("name", "admin", "password", 123456);
        if (mapTest.get(USER_KEY).equals(mapTrain.get(USER_KEY))) {
            System.out.println("登录名验证成功，为：" + mapTrain.get("name"));
            boolean isEqual = mapTest.get("password").equals(mapTrain.get("password"));
            if (isEqual) {
                System.out.println("密码验证成功，密码为：" + mapTrain.get("password"));
            }
        }
    }
}
