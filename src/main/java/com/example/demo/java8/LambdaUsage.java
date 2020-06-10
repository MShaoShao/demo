package com.example.demo.java8;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author miaoshaodong
 * @date Creater in 18:00 2019/11/27
 */
public class LambdaUsage {
    private static List<Apple> filter(List<Apple> source, Predicate<Apple> predicate){
        List<Apple> result = new ArrayList<>();
        for (Apple a : source){
            if(predicate.test(a)){
                result.add(a);
            }

        }
        return result;

    }
    private static String testFunction(Apple apple, Function<Apple,String> fun){
        return fun.apply(apple);
    }

    private static  void simpleTestConsumer(List<Apple> source, Consumer<Apple> consumer){
        for (Apple a : source){
            consumer.accept(a);
        }

    }
    public static void main(String[] args) {
//        Runnable r1 = ()->System.out.println("hello");
//        Runnable r2 = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Hello");
//            }
//        };
//        process(r1);
//        process(r2);
//        process(()->System.out.println("helle"));
        List<Apple> list = Arrays.asList(new Apple("green",123), new Apple("red",112));
        List<Apple> greenList= filter(list,(apple)->apple.getColor().equals("green"));
        System.out.println(greenList);
        String result3 = testFunction(new Apple("yellow",100),(a)->a.toString());
        System.out.println(result3);


    }

    private static void process(Runnable r) {
        r.run();
    }

}
