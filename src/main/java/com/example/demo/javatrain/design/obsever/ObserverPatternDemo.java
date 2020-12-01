package com.example.demo.javatrain.design.obsever;

/**
 * 观察者模式demo
 *
 * @author MiaoShaoDong
 * @date 16:18 2020/8/31
 */
public class ObserverPatternDemo {
    public static void main(String[] args) {
        Subject subject = new Subject();

        new BinaryObserver(subject);
        new OctalObserver(subject);
        System.out.println("First state change: 20");
        subject.setState(20);
        System.out.println("First state change: 200");
        subject.setState(200);
    }
}
