package com.example.demo.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 15:53 2019/12/4
 */
public class SimpleLisener {

    public static final Logger logger = LoggerFactory.getLogger(SimpleLisener.class);
    @Subscribe
    public  void doAction(final String event){
        if (logger.isInfoEnabled()){
            logger.info("收到 事件[{}] 并且采取动作",event);
        }
    }
}
