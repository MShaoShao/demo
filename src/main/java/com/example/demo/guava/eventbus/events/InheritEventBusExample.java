package com.example.demo.guava.eventbus.events;

import com.example.demo.guava.eventbus.listeners.FruitEaterListener;
import com.google.common.eventbus.EventBus;

/**
 * @author miaoshaodong
 * @date Creater in 16:04 2019/12/4
 */
public class InheritEventBusExample {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        eventBus.register(new FruitEaterListener());
        eventBus.post(new Apple("apple"));
        System.out.println("==================");
        eventBus.post(new Fruit("apple"));
    }
}
