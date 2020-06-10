package com.example.demo.selenium;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author miaoshaodong
 * @date Creater in 16:54 2020/2/5
 */
@Slf4j
public class dssd {
    public static void main(String[] args) {
        //测试轮数
        /*for (int i = 0; i < 5; i++) {
            final Runnable testApp = new Runnable() {
                @Override
                public void run() {
                    try {
                        SimulateResult result = Util.simulate(fileText(), "苗少栋");
                        log.info("result: {}", result);
                    } catch (InterruptedException e) {
                        log.info("终端信息异常，异常详细为{}", e.getMessage());
                    } catch (IOException e) {
                        log.info("操作IO异常，异常信息为{}", e.getMessage());
                    }
                }
            };*/
           // new Thread(testApp).start();
        //}
        System.out.println("这是测试代码");
    }
    private static String fileText() throws IOException {
        String filePath = "D:/工作文档/web模拟/side脚本/oaTest2.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }
}
