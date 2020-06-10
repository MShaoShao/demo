package com.example.demo.guava.eventbus.demo;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.ObjectUtils.isEmpty;


/**
 * 小苗学java第二天---if判断逻辑训练
 *
 * @author MiaoShaoDong
 * @date 22:13 2020/6/2
 */
@Slf4j
public class JudgeTrainDemo {
    static {
        final String action = "学习java第二天哈哈哈！";
        String name = "苗少栋";
        System.out.println("\n" + name + action);
    }

    public static void main(String[] args) throws Exception {
        String name = "张三";
        //第一种判断逻辑-即：if谓语句,单一if语句适合进行参数验证，如果有其他行为，建议使用if-else，还有一种方法是在满足if条件时
        if (isEmpty(name)) {
            log.error("用户名为空");
            throw new NullPointerException("用户姓名为空");
        }
        //第二种判断方法，定义boolean字段获取判断的结果，经常用该方法获取某些类进行复杂判断的结果或设置默认判断结果
        boolean nameIsEmpty = isEmpty(name);
        if (nameIsEmpty) {
            log.error("用户名为空");
            throw new NullPointerException("用户姓名为空");
        }
        log.info("用户名非空，该用户为：{}", name);
    }
}
