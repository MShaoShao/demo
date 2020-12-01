package com.example.demo.javatrain.design.filter;

/**
 * 实体过滤器2
 *
 * @author MiaoShaoDong
 * @date 16:56 2020/8/31
 */
public class DebugFilter implements Filter{
    @Override
    public void execute(String request){
        System.out.println("Authenticating request: " + request);
    }
}
