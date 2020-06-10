package com.example.demo.selenium;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.any;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 测试高并发且加载图片下web模拟拨测成功率的方法
 *
 * @author miaoshaodong
 * @date Creater in 11:33 2019/12/11
 */
@Slf4j
public class TestSelenium {
    public static void main(String[] args) throws InterruptedException {
        //当前轮数
        int temp = 0;
        //测试轮数
        int TESTNUMBERS = 1;
        while (temp < TESTNUMBERS) {
            int finalNums = 5;
            Date startTime = new Date();
            for (int i = 0; i < finalNums; i++) {
                int finalNum = i;
                final Runnable testApp = new Runnable() {
                    String logPath = "D:/seleniumLog/seleniumLog10/" + Integer.toString(finalNum) + ".log";

                    @Override
                    public void run() {
                        try {
                            SimulateResult result;
                            if (finalNum < 5) {
                                result = Util.simulate(fileText(), "苗少栋", logPath, finalNum);
                            } else if (finalNum >= 5 && finalNum < 10) {
                                result = Util.simulate(fileText1(), "系统管理员", logPath, finalNum);
                            } else if (finalNum >= 10 && finalNum < 15) {
                                result = Util.simulate(fileText2(), "首页", logPath, finalNum);
                            } else if (finalNum >= 15 && finalNum < 20) {
                                result = Util.simulate(fileText3(), "苗少爷66", logPath, finalNum);
                            } else {
                                result = Util.simulate(fileText4(), "新浪", logPath, finalNum);
                            }
                            log.info("result: {}", result);
                        } catch (InterruptedException e) {
                            log.info("终端信息异常，异常详细为{}", e.getMessage());
                        } catch (IOException e) {
                            log.info("操作IO异常，异常信息为{}", e.getMessage());
                        }
                    }

                };
                new Thread(testApp).start();

            }
            long usedTime =  (System.currentTimeMillis() - startTime.getTime());
            Thread.sleep(usedTime);
            temp++;
        }
    }


    private static String fileText() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\oaTest2.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }

    private static String fileText1() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\liebao.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }

    private static String fileText2() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\160.33.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }

    private static String fileText3() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\baiDu.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }

    private static String fileText4() throws IOException {
        String filePath = "D:\\工作文档\\web模拟\\side脚本\\xinLang.side";
        File file = new File(filePath);
        return Files.asCharSource(file, UTF_8).read();
    }


    public static SimulateResult simulateResult() throws InterruptedException, IOException {
        Date startTime = new Date();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--auto-open-devtools-for-tabs", "--disk-cache-dir=D:\\cache1", "--blink-settings=imagesEnabled=true", "-–disable-plugins", "-–disable-gpu", "--disk-cache-size=1073741824", "-–media-cache-size=1073741824");
        ChromeDriver driver = new ChromeDriver(options);
        WebDriver.Options manage = driver.manage();
        manage.timeouts().pageLoadTimeout(20, TimeUnit.SECONDS)
                .setScriptTimeout(15, TimeUnit.SECONDS);
        SimulateResult result = SimulateResult.init(startTime, 0);
        try {
            // Test name: os_test
            // Step # | name | target | value | comment
            // 1 | open | /login.php?URL=http://oa.ruijie.com.cn/SysAdapter.aspx&reurl=http://oa.ruijie.com.cn/index.aspx |  |
            driver.get("http://sso.ruijie.com.cn/login.php?URL=http://oa.ruijie.com.cn/SysAdapter.aspx&reurl=http://oa.ruijie.com.cn/index.aspx");
            // 2 | setWindowSize | 1552x840 |  |
            driver.manage().window().setSize(new Dimension(1552, 840));
            // 3 | type | id=txtName | miaoshaodong |
            driver.findElement(By.id("txtName")).sendKeys("miaoshaodong");
            // 4 | type | id=txtPass | Miao19930818 |
            driver.findElement(By.id("txtPass")).sendKeys("Miao19930818");
            // 5 | click | id=submitlogin |  |
            driver.findElement(By.id("submitlogin")).click();
            int usedTime = (int) (System.currentTimeMillis() - startTime.getTime());
            result.setUsedTime(usedTime);
            checkException(driver, "苗少栋", result);
        } catch (TimeoutException e) {
            log.info("有超时异常", e);
        } finally {
            driver.quit();
        }
        return result;
    }

    private static void checkException(ChromeDriver driver, String expects, SimulateResult result) throws TimeoutException {
        if (isNullOrEmpty(expects)) {
            return;
        }
        List<String> expectList = Lists.newArrayList(
                Splitter.on(CharMatcher.anyOf("\r\n")).trimResults().omitEmptyStrings().split(expects)
        );
        if (expectList.isEmpty()) {
            return;
        }
        String innerText = "";
        try {
            innerText = (String) driver.executeScript("return document.body.innerText;");
            if (any(expectList, innerText::contains)) {
                return;
            }
            Thread.sleep(1000);
        } catch (NoSuchSessionException | InterruptedException e) {
            result.setState(1, "脚本录制时关闭了浏览器窗口导致无法判断匹配内容");
            return;
        }
        String message = "无法找到匹配的字符（" + String.join("、", expectList) + "）";
        result.setState(1, message);
        result.setInnerText(innerText);
    }

}
