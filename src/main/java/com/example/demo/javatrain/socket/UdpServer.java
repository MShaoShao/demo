package com.example.demo.javatrain.socket;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP协议（用户数据报协议）是无连接的、不可靠的、无序的,速度快
 * 进行数据传输时，首先将要传输的数据定义成数据报（Datagram），大小限制在64k，
 * 在数据报中指明数据索要达到的Socket（主机地址和端口号），然后再将数据报发送出去
 * DatagramPacket类:表示数据报包
 * DatagramSocket类：进行端到端通信的类
 * 1、服务器端实现步骤
 * 1.创建DatagramSocket，指定端口号
 * 2.创建DatagramPacket
 * 3.接受客户端发送的数据信息
 * 4.读取数据
 *
 * @author MiaoShaoDong
 * @date 9:46 2020/6/17
 */
@Slf4j
public class UdpServer {
    public static void main(String[] args) {
        try {
            //1、创建服务器端DatagramSocket,并指定相应端口
            DatagramSocket socket = new DatagramSocket(10030);
            //2、创建数据报，用于接收客户端发送的数据
            byte[] bytes = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            //3、接受客户端发送的数据，此方法在接收数据报之前会一直堵塞
            socket.receive(packet);
            //4、读取数据
            String info = new String(bytes, 0, bytes.length);
            System.out.println("我是服务器，客户端告诉我说" + info);

            /*==============================================*/
            //根据相应内容向客户端发送响应数据
            //1、定义客户端的地址、端口号和数据
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] data = "欢迎您！".getBytes();
            //2、创建数据报，包含响应的数据信息
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
            //3、响应客户端
            socket.send(datagramPacket);
            //4、关闭socket资源
            socket.close();
        } catch (Exception e) {
            log.info("exception is {}", e.toString());
        }
    }
}
