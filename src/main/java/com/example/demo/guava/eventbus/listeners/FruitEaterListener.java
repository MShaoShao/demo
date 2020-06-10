package com.example.demo.guava.eventbus.listeners;

import com.example.demo.guava.eventbus.events.Apple;
import com.example.demo.guava.eventbus.events.Fruit;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 16:51 2019/12/4
 */
public class FruitEaterListener {
    private static final Logger LOGGER= LoggerFactory.getLogger(ConcreteListener.class);
    @Subscribe
    public void eat(Fruit event){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("fruit eat [{}]",event);
        }
    }
    @Subscribe
    public void eat(Apple event){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("apple eat [{}]",event);
        }
    }
}
