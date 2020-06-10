package com.example.demo.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 16:18 2019/12/4
 */
public abstract class AbstractListener {
    private static final Logger LOGGER= LoggerFactory.getLogger(AbstractListener.class);
    @Subscribe
    public void commonTask(String event){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("the Event [{}] will be handle by {},{}",event,this.getClass().getSimpleName(),"commonTask");
        }
    }
}
