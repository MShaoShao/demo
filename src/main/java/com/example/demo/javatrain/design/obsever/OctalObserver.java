package com.example.demo.javatrain.design.obsever;

/**
 * 观察者实体类2
 *
 * @author MiaoShaoDong
 * @date 16:14 2020/8/31
 */
public class OctalObserver extends Observer{

    public OctalObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println( "Octal String: "
                + Integer.toOctalString( subject.getState() ) );
    }
}
