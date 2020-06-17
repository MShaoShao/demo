package com.example.demo.javatrain;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * Tcp客户端功能实现
 * 1、创建Socket对象，指明需要连接的服务器的地址和端口号
 * 2、连接建立后，通过输出流想服务器端发送请求信息
 * 3、通过输入流获取服务器响应的信息
 * 4、关闭响应资源
 *
 * @author MiaoShaoDong
 * @date 17:12 2020/6/16
 */
@Slf4j
public class TcpClient {
    public static void main(String[] args) {
        try {
            //1、创建客户端Socket,指定服务器地址和端口
            Socket socket = new Socket("localhost", 10087);
            //2、创建输出流，向服务器端发送信息
            OutputStream out = socket.getOutputStream();
            //将输出流包装为打印流
            PrintWriter pw = new PrintWriter(out);
            pw.write("用户名：admin; 密码：123");
            pw.flush();
            socket.shutdownOutput();
            //3、创建输入流，并读取服务器端返回的响应信息
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String info = null;
            while ((info = br.readLine()) != null) {
                log.info("服务器端给出的响应为：{}", info);
            }
            br.close();
            in.close();
            pw.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            log.info("exception is {}", e.toString());
        }
    }
}
