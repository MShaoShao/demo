package com.example.demo.javatrain.design.filter;

/**
 * 步骤3 -创建Target：请求处理程序
 *
 * @author MiaoShaoDong
 * @date 16:57 2020/8/31
 */
public class Target {
    public void execute(String request) {
        System.out.println("处理请求为：" + request);
    }
}
