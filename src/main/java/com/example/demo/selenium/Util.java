package com.example.demo.selenium;

import com.alibaba.fastjson.JSON;
import com.example.demo.selenium.bean.CommandStep;
import com.example.demo.selenium.bean.Record;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.any;


/**
 * 工具库
 *
 * @author miaoshaodong
 * @date Creater in 11:01 2019/11/13
 */
@Slf4j
public class Util {
    /**
     * 支持的SIDE录像版本号
     */
    private static final String SUPPORTED_VERSION = "2.0";
    /**
     * 最大并行模拟数量
     * <p>
     * 1G内存，约可支撑20个浏览器
     */
    private static final int MAX_SIMULATE_COUNT = 50;
    /**
     * 并行模拟控制信号量
     * <p>
     * 浏览器运行需要消耗内存（40至60M）,为了避免并行时操作内存不足，引入该信号量控制并发量
     */
    private static final Semaphore SEMAPHORE = new Semaphore(MAX_SIMULATE_COUNT);

    /**
     * 等待文本的总时长（单位：毫秒）
     */
    private static final int TEXT_WAIT = 2000;

    /**
     * 等待文本的总时长（单位：毫秒）
     */
    private static final int TEXT_WAIT_INTERVAL = 100;

    /**
     * 等待文本的循环次数
     */
    private static final int TEXT_WAIT_LOOP = TEXT_WAIT / TEXT_WAIT_INTERVAL;

    private Util() {
        throw new IllegalArgumentException("工具库，不能实例化");
    }

    // static ChromeDriver driver;

    /**
     * 执行web模拟
     *
     * @param simulateConfig 模拟配置（本期为SIDE录像文本）
     * @param successContent 成功内容
     * @return web模拟结果
     * @throws InterruptedException 等待信号量过程中被外部打断
     */
    public static SimulateResult simulate(String simulateConfig, String successContent, String logPath, int failNumber) throws InterruptedException, IOException {
        SEMAPHORE.acquire();
        try {
            Date startTime = new Date();
            SimulateResult result = SimulateResult.init(startTime, 0);
            doSimulate(simulateConfig, successContent, result, logPath, failNumber);
            int usedTime = (int) (System.currentTimeMillis() - startTime.getTime());
            result.setUsedTime(usedTime);
            return result;
        } finally {
            SEMAPHORE.release();
        }
    }

    private static void doSimulate(String simulateConfig, String successContent, SimulateResult result, String logPath, int failNumber) throws IOException, InterruptedException {
        Record record;
        try {
            record = JSON.parseObject(simulateConfig, Record.class);
            String version = record.getVersion();
            if (!SUPPORTED_VERSION.equals(version)) {
                result.setState(2, "模拟配置格式错误，只支持版本" + SUPPORTED_VERSION);
                return;
            }
        } catch (Exception e) {
            result.setState(2, "模拟配置格式错误" + e.getMessage());
            return;
        }
        ChromeDriver driver;
        try {
            driver = Constants.createDriver(logPath);
        } catch (Exception e) {
            String message = "无法加载驱动器驱动，有可能用户没有安装webDriver" + e.getMessage();
            result.setState(2, message);
            return;
        }

        RecordRunner runner = new RecordRunner(driver, record);
        try {
            runner.run();
            //ChecKExpects(driver, successContent, result);
        } catch (Exception e) {
//            ChecKExpects(driver, successContent, result);
//            if (result.getState()==0){
//                return;
//           }else{
            StringBuilder buf = new StringBuilder("录像执行错误: ");
            CommandStep current = runner.getCurrent();
            if (current != null) {
                buf.append("步骤").append(Constants.str(current));
            }
            buf.append("错误内容：").append(e.getMessage());
            result.setState(1, buf.toString());
//            }
        } finally {
            ChecKExpects(driver, successContent, result);
            Constants.shotToFile(driver, new File("D:\\testFailPng\\第" + failNumber + "次页面记录.png"));
            driver.quit();
        }
    }


    private static void ChecKExpects(ChromeDriver driver, String expects, SimulateResult result) {
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
        for (int i = 0; i < TEXT_WAIT_LOOP; i++) {
            try {
                innerText = (String) driver.executeScript("return document.body.innerText;");
                // log.info("页面内容-----{}",innerText);
                if (any(expectList, innerText::contains)) {
                    return;
                }
                Thread.sleep(TEXT_WAIT_INTERVAL);
            } catch (NoSuchSessionException | InterruptedException e) {
                result.setState(1, "脚本录制时关闭了浏览器窗口导致无法判断匹配内容");
                return;
            }
        }
        String message = "无法找到匹配的字符（" + String.join("、", expectList) + "）";
        result.setState(1, message);
        result.setInnerText(innerText);
    }
}
