package com.example.demo.javatrain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 小苗学java第4天 -- socket网络编程训练
 *
 * @author MiaoShaoDong
 * @date 11:16 2020/6/16
 */
@Slf4j
public class SocketTrian {
    public static void main(String[] args) {
        try {
            ipAddressTrain();
            urlTrain();
        }catch (Exception e){
            log.info("socket出现异常，异常信息为：{}", e.getMessage());
        }
    }

    /**
     * 获取本机或其他机器的InetAddress实例
     */
    private static void ipAddressTrain() throws UnknownHostException {
        //获取本机的InetAddress实例
        InetAddress address = InetAddress.getLocalHost();
        //输出计算机名
        System.out.println(address.getHostName());
        //输出IP地址
        System.out.println(address.getHostAddress());
        byte[] bytes = address.getAddress();
        //输出字节数组形式的IP地址，以点分隔的四部分
        System.out.println(Arrays.toString(bytes));

        //获取其他主机或IP地址的InetAddress实例
        InetAddress address3 = InetAddress.getByName("www.baidu.com");
        //输出计算机名
        System.out.println(address3.getHostName());
        //输出IP地址
        System.out.println(address3.getHostAddress());
    }

    /**
     * 创建URL读取网页内容
     */
    private static void urlTrain() throws IOException {
        //创建一个Url实例
        URL urlName = new URL("http://www.baidu.com");
        //?表示参数，#表示锚点
        URL url = new URL(urlName,"/index.html?username=tom#test");
        //输出主机
        System.out.println(url.getHost());
        //输出协议
        System.out.println(url.getProtocol());
        //输出端口号，若无端口号，则返回值为-1
        System.out.println(url.getPort());
        //输出文件路径
        System.out.println(url.getPath());
        //输出文件名,包括文件路径和参数
        System.out.println(url.getFile());
        //输出相对路径，即锚点#号后边的内容
        System.out.println(url.getRef());
        //输出查询的字符串，即参数
        System.out.println(url.getQuery());

        //利用Url读取网页内容
        //通过OpenStream方法获取资源的字节输入流
        InputStream is = url.openStream();
        //将字节输入流转换为字符输入流，若不指定编码，中文可能会乱码
        InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        //为字符输入流添加缓冲，提高读取效率
        BufferedReader br = new BufferedReader(inputStreamReader);
        //读取数据
        String data = br.readLine();
        while ( data != null){
            //输出数据
            System.out.println(data);
            data = br.readLine();
        }
        br.close();
        inputStreamReader.close();
        is.close();
    }
}
