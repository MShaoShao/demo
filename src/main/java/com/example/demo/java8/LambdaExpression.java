package com.example.demo.java8;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author miaoshaodong
 * @date Creater in 17:51 2019/11/21
 */
public class LambdaExpression {
    public static void main(String[] args) {
        Comparator<Apple> byColor = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        };
        List<Apple> list = Collections.emptyList();
        list.sort(byColor);
        Comparator<Apple> byColor2 = ( o1,  o2) -> o1.getColor().compareTo(o2.getColor());
        Function<String,Integer> flambda = s ->s.length();
        Runnable r =()->{};
        Function<Apple,Boolean> f = (a)->a.getColor().equals("green");
    }
}
