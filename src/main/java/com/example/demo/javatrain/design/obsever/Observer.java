package com.example.demo.javatrain.design.obsever;

/**
 * 创建观察者抽象类
 *
 * @author MiaoShaoDong
 * @date 16:05 2020/8/31
 */
public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
