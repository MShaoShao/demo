package com.example.demo.guava.eventbus.events;

import com.example.demo.guava.eventbus.listeners.AbstractListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 16:27 2019/12/4
 */
public class BaseListener extends AbstractListener {
    private static final Logger LOGGER= LoggerFactory.getLogger(BaseListener.class);
    @Subscribe
    public void baseTask(String event){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("the Event [{}] will be handle by {},{}",event,this.getClass().getSimpleName(),"baseTask");
        }
    }
}
