package com.example.demo.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author miaoshaodong
 * @date Creater in 16:02 2019/12/4
 */
public class MultipleEventListeners {
    public static final Logger logger = LoggerFactory.getLogger(MultipleEventListeners.class);
    @Subscribe
    public  void task1(String event){
        if (logger.isInfoEnabled()){
            logger.info("收到 事件[{}] 并且采取动作 ===task1====",event);
        }
    }

    @Subscribe
    public  void task2(String event){
        if (logger.isInfoEnabled()){
            logger.info("收到 事件[{}] 并且采取动作 ===task2====",event);
        }
    }
    @Subscribe
    public  void intTask(Integer event){
        if (logger.isInfoEnabled()){
            logger.info("收到 事件[{}] 并且采取动作 ===intTask====",event);
        }
    }
}
