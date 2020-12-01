package com.example.demo.javatrain.design.obsever;

/**
 * 观察者实体类
 *
 * @author MiaoShaoDong
 * @date 16:09 2020/8/31
 */
public class BinaryObserver extends Observer {
    public BinaryObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }
    @Override
    public void update(){
        System.out.println("Binary String: " + Integer.toBinaryString(subject.getState()));
    }
}
