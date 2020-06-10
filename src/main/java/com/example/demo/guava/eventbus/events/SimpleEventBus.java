package com.example.demo.guava.eventbus.events;

import com.example.demo.guava.eventbus.listeners.SimpleLisener;
import com.google.common.eventbus.EventBus;

/**
 * @author miaoshaodong
 * @date Creater in 15:45 2019/12/4
 */
public class SimpleEventBus {

    public static void main(String[] args) {
        final EventBus eventBus= new EventBus();
        eventBus.register(new SimpleLisener());
        System.out.println("post the simple event.");
        eventBus.post("Simple Event");
    }
}
