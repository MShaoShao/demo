package com.example.demo.javatrain.design.filter;

/**
 * 实体过滤器
 *
 * @author MiaoShaoDong
 * @date 16:52 2020/8/31
 */
public class AuthenticationFilter implements Filter{
    @Override
    public void execute(String request){
        System.out.println("Authenticating request: " + request);
    }
}
