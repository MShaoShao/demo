package com.example.demo.javatrain.socket;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 客户端实现步骤
 * <p>
 * 1.定义发送信息
 * 2.创建DatagramPacket，包含将要发送的信息
 * 3.创建DatagramSocket
 * 4.发送数据
 *
 * @author MiaoShaoDong
 * @date 10:29 2020/6/17
 */
@Slf4j
public class UdpClient {
    public static void main(String[] args) {
        try {
            //1、定义服务器的地址、端口号和数据
            InetAddress inetAddress = InetAddress.getByName("localhost");
            int port = 10030;
            byte[] data = "hello 服务端".getBytes();
            //2、创建数据报，包含发送测数据信息
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            //3、创建数据报传输类 DatagramSocket
            DatagramSocket socket = new DatagramSocket();
            //4、向服务器发送数据
            socket.send(datagramPacket);

            //接收服务器端相应数据报
            /*===============================================*/
            //1、创建数据报用于接收服务器响应数据
            byte[] serverData = new byte[1024];
            DatagramPacket packet = new DatagramPacket(serverData,serverData.length);
            //2、接受服务器响应的数据
            socket.receive(packet);
            String reply = new String(serverData,0,packet.getLength());
            System.out.println("我是客户端，服务端说："+reply);
            //3、关闭资源
            socket.close();
        } catch (Exception e) {
            log.info("exception is {}", e.toString());
        }
    }
}
