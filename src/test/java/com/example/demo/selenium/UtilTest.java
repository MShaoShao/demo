package com.example.demo.selenium;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.Iterables.any;
import static com.google.common.io.Resources.getResource;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 工具库测试
 *
 * @author miaoshaodong
 * @date Creater in 10:13 2019/11/14
 */
@Slf4j
public class UtilTest {
    @Test
    public void testSimulate() throws IOException, InterruptedException {
        SimulateResult result = Util.simulate(fileText(), "首页","D:/seleniumLog/1.log",1);
        log.info("result: {}", result);
    }

    private String fileText() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\baiDu.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }

    private String resText() throws IOException {
        URL resource = getResource("test.side");
        return Resources.asCharSource(resource, UTF_8).read();
    }

    @Test
    public void testHaHa() {
        Properties prop = System.getProperties();

        // 获取用户名
        System.out.println("\n当前用户名:" + prop.getProperty("user.name"));
    }

}
