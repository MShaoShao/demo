package com.example.demo.javatrain.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 服务器线程处理
 *
 * @author MiaoShaoDong
 * @date 17:54 2020/6/16
 */
@Slf4j
public class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
        //声明一个socket对象
        this.socket = socket;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("hello.......");
            out.flush();
            while (true) {
                String str = in.readLine();
                if (str == null) {
                    break;
                } else {
                    out.println("嗨，" + str);
                    out.flush();
                    if (str.trim().equalsIgnoreCase("BYE")) {
                        break;
                    }
                }
                out.close();
                in.close();
                socket.close();
            }
        } catch (Exception e) {
            log.info("exception is {}", e.toString());
        }
    }

}
