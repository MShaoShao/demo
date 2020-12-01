package com.example.demo.javatrain.design.obsever;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察设计模式--观察目标类
 *
 * @author MiaoShaoDong
 * @date 15:27 2020/8/31
 */
public class Subject {
    private int state;
    private List<Observer> observers = new ArrayList<Observer>();
    public int getState(){
        return  state;
    }
    public void setState(int state){
        this.state = state;
        notifyAllObserver();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    private void notifyAllObserver() {
        for (Observer observer : observers){
            observer.update();
        }
    }

}
