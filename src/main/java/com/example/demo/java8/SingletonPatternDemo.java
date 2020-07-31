package com.example.demo.java8;

/**
 * 单例模式实例调用对象
 *
 * @author MiaoShaoDong
 * @date 14:08 2020/7/31
 */
public class SingletonPatternDemo {
    public static void main(String[] args) {
        //获取唯一可用对象
        SingleDesign object = SingleDesign.getInstance();
        //调用单例中的信息
        object.showMessage();
    }
}
