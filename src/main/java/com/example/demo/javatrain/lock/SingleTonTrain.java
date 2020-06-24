package com.example.demo.javatrain.lock;

import lombok.Data;


/**
 * 懒汉模式
 */
@Data
class A {
    private  int num ;
    private static  A a = new A();
    static A getInstance(){
        return a;
    }

}


/**
 * 最优形式
 */
@Data
class SingleTon {
    private static class LazyHolder {
        private static final SingleTon INSTANCE = new SingleTon();
    }
    private SingleTon (){}
    private  int num ;
    public static final SingleTon getInstance() {
        return LazyHolder.INSTANCE;
    }
}

/**
 * 小苗学java5--单例设计模式：一个类在内存中只有一个对象
 * 1、构造函数私有化
 *
 *
 * @author MiaoShaoDong
 * @date 16:36 2020/6/24
 */
public class SingleTonTrain {
    public static void main(String[] args) {
        //懒汉单例
        A a = A.getInstance();
        A b = A.getInstance();
        a.setNum(12);
        b.setNum(123);
        System.out.println(a.getNum());
        System.out.println(b.getNum());
        //最优单例
        SingleTon s = SingleTon.getInstance();
        SingleTon s1 = SingleTon.getInstance();
        s.setNum(1);
        s.setNum(2);
        System.out.println("s is "+s.getNum()+" s1 is "+s1.getNum());
    }
}
