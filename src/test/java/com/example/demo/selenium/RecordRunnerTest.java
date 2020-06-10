package com.example.demo.selenium;

import com.alibaba.fastjson.JSON;
import com.example.demo.selenium.bean.Record;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.readLines;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Selenium录制回放执行测试
 *
 * @author miaoshaodong
 * @date Creater in 10:10 2019/11/14
 */
@Slf4j
public class RecordRunnerTest {

    /**
     *验证方法映射是否存在
     * <p>
     * 为了避免由于改名导致的映射错误，增加此用例
     */
    @Test
    public void testCommands() throws IOException {
        URL resource = getResource("commands.csv");
        List<String> lines = Resources.readLines(resource,UTF_8);
        Pattern pattern = Pattern.compile("^\"([^\"]+)\".*");
        int count = 0;
        int found = 0;
        for (String line : lines){
            Matcher matcher  = pattern.matcher(line);
            if (matcher.matches()){
                String command = matcher.group(1);
                if ("command".equals(command)){
                    continue;
                }
                Method method = RecordRunner.REGISTRY.get(command);
                //Map.Entry<String,Method> reflect = (Map.Entry<String, Method>) RecordRunner.gatherRegistry();
                if (method == null){
                    log.error("命令未找到：{}", command);
                }else{
                    found++;
                }
                count++;
            }
        }
        log.info("共{}个selenium命令，找到 {}",count,found);
    }

    /**
     *验证方法运行是否成功
     */
    @Test
    public void testRun() throws IOException {
        String text = fileText();
        Record record = JSON.parseObject(text,Record.class);
        String path= "D:\\工作文档\\web模拟\\log";
        ChromeDriver driver = Constants.createDriver(false,path);
        try{
            RecordRunner runner = new RecordRunner(driver,record);
            runner.run();
        }catch (Exception e){
            log.warn("执行失败",e);
        }finally {
            Constants.shotToFile(driver,new File("D:\\工作文档\\web模拟\\xx.png"));
            driver.quit();
        }
    }

    private String resText() throws IOException {
        URL resource = getResource("test.side");
        return Resources.asCharSource(resource,UTF_8).read();
    }
    private String fileText() throws IOException{
        File resource = new File("D:\\工作文档\\web模拟\\testLB.side");
        return Files.asCharSource(resource,UTF_8).read();
    }
}
