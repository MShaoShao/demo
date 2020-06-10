package com.example.demo.guava.eventbus.events;

import com.google.common.base.MoreObjects;

/**
 * @author miaoshaodong
 * @date Creater in 16:32 2019/12/4
 */
public class Fruit {
    private final String name;

    public Fruit(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name",name).toString();
    }
}
