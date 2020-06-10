package com.example.demo.guava.eventbus.listeners;

import com.example.demo.guava.eventbus.events.BaseListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 16:27 2019/12/4
 */
public class ConcreteListener extends BaseListener {
    private static final Logger LOGGER= LoggerFactory.getLogger(ConcreteListener.class);
    @Subscribe
    public void conTask(String event){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("the Event [{}] will be handle by {},{}",event,this.getClass().getSimpleName(),"comTask");
        }
    }
}
