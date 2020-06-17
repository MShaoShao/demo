package com.example.demo.javatrain;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Tcp服务端功能实现
 * <p>
 * 1、服务器端创建ServerSocket，循环调用accept()等待客户端连接
 * 2、客户端创建一个socket并请求和服务器端连接
 * 3、服务器端接受苦读段请求，创建socket与该客户建立专线连接
 * 4、建立连接的两个socket在一个单独的线程上对话
 * 5、服务器端继续等待新的连接
 *
 * @author MiaoShaoDong
 * @date 17:13 2020/6/16
 */
@Slf4j
public class TcpServer {
    public static void main(String[] args) {
        try {
            //1、创建socket通道,并声明一个监听标识
            ServerSocket serverSocket = new ServerSocket(10087);
            //2、记录客户端数量
            int count = 0;
            while (true) {
                //实例化一个服务器端的Socket与请求Socket建立连接
                new ServerThread(serverSocket.accept()).start();
                count++;
                log.info("客户端连接的数量：{}", count);
            }
        } catch (Exception e) {
            log.info("exception is {}", e.toString());
        }
    }
}
