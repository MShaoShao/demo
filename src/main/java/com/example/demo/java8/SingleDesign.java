package com.example.demo.java8;

/**
 * 单例设计模式 ---应用
 *
 * @author MiaoShaoDong
 * @date 13:54 2020/7/31
 */
public class SingleDesign {
    /**
     * 步骤1创建一个静态实例对象
     */
    private static SingleDesign instance = new SingleDesign();

    /**
     * 步骤2 构建函数私有化，预防该类被实例化
     */
    private SingleDesign() {
    }

    /**
     * 步骤3 获取唯一可用的对象
     */
    public static SingleDesign getInstance() {
        return instance;
    }

    public void showMessage() {
        System.out.println("你好，世界");
    }


}
