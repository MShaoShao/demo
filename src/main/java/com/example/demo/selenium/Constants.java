package com.example.demo.selenium;

import com.example.demo.selenium.bean.CommandStep;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * 常量定义
 *
 * @author miaoshaodong
 * @date Creater in 16:08 2019/11/12
 */
@Slf4j
class Constants {
    static final String RELATIVE_TOP = "relative=top";
    static final String RELATIVE_PARENT = "relative=parent";
    static final String INDEX_EQUAL = "index=";
    static final String NAME_EQUAL = "name=";
    static final String WIN_SER = "win_ser_";
    static final String LOCAL = "local";
    static final String HANDLEQUAL = "handle=";
    static final String DOUBLE_SLASH = "//";
    private static final Map<String, Keys> KEYS;
    static final String OSNAME = "dows";
    /**
     * 可重入锁
     */
    private static ReentrantLock lock = new ReentrantLock();
    /**
     * 最大缓存大小为1G
     */
    private static final long MAX_CACHE_SIZE = 1;
    /**
     * 缓存单位为1G
     */
    private static final long STANDARD_UNITS = 1024;
    /**
     * 默认删除最近时间为1小时之前的缓存文件
     */
    private static final long LAST_MODIFIED_TIME_MS = MINUTES.toMillis(1);

    /**
     * 变量获取正则表达式
     * <p>
     * 匹配内容为'vars.get("变量名称")'
     */
    private static final String SLASH = "/";
    private static final String MSG_TARGET_ERROR = "目标格式非法";

    static {
        ImmutableMap.Builder<String, Keys> builder = ImmutableMap.builder();
        for (Keys key : Keys.values()) {
            builder.put(key.name(), key);
        }
        KEYS = builder.build();
    }

    private Constants() {
        throw new IllegalAccessError("工具库，不能实例化");
    }

    private static boolean isUrl(String target) {
        return target.startsWith("ftp://") || target.startsWith("http://")
                || target.startsWith("https://");
    }

    /**
     * 合并URL和 PATH
     *
     * @param url  URL
     * @param path 路径
     * @return 合并后的URL
     */
    static String concatUrl(String url, String path) {
        if (Strings.isNullOrEmpty(path)) {
            return url;
        }
        if (isUrl(path)) {
            return path;
        }
        StringBuilder buf = new StringBuilder(url);
        if (!url.endsWith(SLASH)) {
            buf.append(SLASH);
        }
        if (path.startsWith(SLASH)) {
            buf.append(path.substring(1));
        } else {
            buf.append(path);
        }
        return buf.toString();
    }

    /**
     * 获取去掉前缀的字符串部分
     *
     * @param str    待判断字符串
     * @param prefix 前缀
     * @return 去掉前缀的字符串
     */
    static Optional<String> trimPrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return Optional.of(str.substring(prefix.length()));
        }
        return Optional.empty();
    }

    /**
     * 获取中间的字符串部分
     *
     * @param str   待判断字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 中间的字符串
     */
    static Optional<String> clampStr(String str, String start, String end) {
        if (str.startsWith(start) && str.endsWith(end)) {
            String clamped = str.substring(start.length(), str.length() - end.length());
            return Optional.of(clamped);
        }
        return Optional.empty();
    }

    /**
     * 创建webDriver
     *
     * @return driver
     */
    static ChromeDriver createDriver(boolean headlesss, String logPath) {

        String userHomePath = System.getProperty("user.home");
        String separator = File.separator;
        String currentPath = generateCacheFilePath();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(headlesss);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--auto-open-devtools-for-tabs", "--disk-cache-dir=" + currentPath + "", "--blink-settings=imagesEnabled=true", "-–disable-plugins", "-–disable-gpu", "--disk-cache-size=1073741824", "-–media-cache-size=1073741824");
        //options.addArguments("--no-sandbox", "--disable-dev-shm-usage","--auto-open-devtools-for-tabs", "--blink-settings=imagesEnabled=true","–disable-plugins", "–disable-gpu");
        //options.addArguments("--no-sandbox", "--disable-dev-shm-usage","--auto-open-devtools-for-tabs","--disk-cache-dir=C:/Users/"+userName+"/AppData/Local/Google/Chrome/User Data/Default/Cache", "--blink-settings=imagesEnabled=true", "–process-per-site", "–disable-plugins", "–disable-gpu", "-disk-cache-size=5368709120", "–media-cache-size=5368709120");
         System.setProperty("webdriver.chrome.logfile", logPath);
//        DesiredCapabilities caps = DesiredCapabilities.chrome();
//        LoggingPreferences logPrefs = new LoggingPreferences();
//        logPrefs.enable(LogType.BROWSER, Level.ALL);
//        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
//        caps.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver(options);
    }

    /*   private static String generateCacheFilePath(String userHomePath, String separator) {
           String currentPath = userHomePath+separator+"chrome_cache";
           System.out.println(currentPath);
           File file = new File(currentPath);
           if (!file.exists()){
               file.mkdir();
           }
           return currentPath;
       }*/
    private static String generateCacheFilePath() {
        String userHomePath = System.getProperty("user.home");
        String systemName = System.getProperty("os.name");
        File pathFile;
        //判断当前操作系统类型，并设置相应系统的缓存路径
        if (systemName.contains(OSNAME)) {
            pathFile = new File(new File(userHomePath), "chrome_cache");
        } else {
            //判断是否为docker环境
            File dockerFile = new File("\\/home/app.jar");
            String dockerPath = "\\/home/log/chrome_cache";
            if (dockerFile.exists()) {
                pathFile = new File(dockerPath);
            } else {
                pathFile = new File(new File(userHomePath), "chrome_cache");
            }
        }
        if (!pathFile.exists()) {
            checkState(pathFile.mkdir(), "创建目录失败");
        }
        judgeAndCleanCache(pathFile);
        return pathFile.getAbsolutePath();
    }

    private static void judgeAndCleanCache(File pathFile) {
        lock.lock();
        try {
            long fileTotalSize = gainCacheFileSize(pathFile) / STANDARD_UNITS;
            if (fileTotalSize > MAX_CACHE_SIZE) {
                clearPathFile(pathFile);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取缓存文件夹的大小
     *
     * @param file 文件夹路径
     * @return filesSize 文件夹内所有文件的大小
     */
    private static long gainCacheFileSize(File file) {
        if (file.isFile()) {
            return file.length();
        } else {
            long filesSize = 0;
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File fi : files) {
                filesSize += gainCacheFileSize(fi);
            }
            return filesSize;
        }
    }

    /**
     * 清空文件夹内在1小时前的缓存文件
     *
     * @param pathFile 文件夹路径
     */
    private static void clearPathFile(File pathFile) {
        Date date = new Date(System.currentTimeMillis() - LAST_MODIFIED_TIME_MS);
        File[] files = pathFile.listFiles();
        if (files != null) {
            for (File fi : files) {
                if (new Date(fi.lastModified()).before(date)) {
                    fi.delete();
                }
            }
        }
    }


    static ChromeDriver createDriver(String logPath) {
        ChromeDriver driver = createDriver(true, logPath);
        //设置超时，避免慢速网站拖死系统
        WebDriver.Options manage = driver.manage();
        manage.timeouts().pageLoadTimeout(15, TimeUnit.SECONDS)
                .setScriptTimeout(15, TimeUnit.SECONDS);
        return driver;
    }


    static Object str(CommandStep c) {
        return new Object() {
            @Override
            public String toString() {
                return c.getCommand() + "|" + c.getTarget() + "|" + c.getValue() + "|" + c.getComment();
            }
        };
    }

    /**
     * 解析屏幕尺寸
     *
     * @param s 以x分割的额宽度和高度
     * @return 屏幕尺寸
     */
    static Dimension parseDimension(String s) {
        Optional<Map.Entry<String, String>> optional = splitByChar(s, 'x');
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("尺寸格式非法" + s);
        }
        try {
            Map.Entry<String, String> entry = optional.get();
            int width = Integer.parseInt(entry.getKey());
            int height = Integer.parseInt(entry.getValue());
            return new Dimension(width, height);
        } catch (Exception e) {
            throw new IllegalArgumentException("尺寸格式非法" + s + " " + e.getMessage());
        }

    }

    static Optional<Map.Entry<String, String>> splitByChar(String str, char ch) {
        int index = str.indexOf(ch);
        if (index == -1) {
            return Optional.empty();
        }
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(
                str.substring(0, index),
                str.substring(index + 1)
        );
        return Optional.of(entry);
    }

    /**
     * 解析定位By
     *
     * @param s 定位字符串（如：id=xxx）
     * @return 定位By
     */
    static By parseBy(String s) {
        if (Strings.isNullOrEmpty(s)) {
            throw new IllegalArgumentException("必须提供定位target");
        }
        if (s.startsWith(DOUBLE_SLASH)) {
            return By.xpath(s);
        }
        Optional<Map.Entry<String, String>> optional = splitByChar(s, '=');
        if (!optional.isPresent()) {
            throw new IllegalArgumentException(MSG_TARGET_ERROR + s);
        }
        Map.Entry<String, String> entry = optional.get();
        String selector = entry.getValue();
        switch (entry.getKey()) {
            case "id":
                return By.id(selector);
            case "name":
                return By.name(selector);
            case "link":
            case "linkText":
                return By.linkText(selector);
            case "partialLinkText":
                return By.partialLinkText(selector);
            case "css":
                return By.cssSelector(selector);
            case "xpath":
                return By.xpath(selector);
            default:
                throw new IllegalArgumentException(MSG_TARGET_ERROR + s);
        }
    }

    /**
     * 解析定位Selection
     *
     * @param s 定位字符串
     * @return 定位By
     */
    static By parseSelection(String s) {
        if (Strings.isNullOrEmpty(s)) {
            throw new IllegalArgumentException("必须提供定位Selection");
        }
        if (s.startsWith(DOUBLE_SLASH)) {
            return By.xpath(s);
        }
        int index = s.indexOf('=');
        if (index == -1) {
            return By.xpath("//option[.='" + s + "']");
        }
        String type = s.substring(0, index);
        String selector = s.substring(index + 1);
        switch (type) {
            case "id":
                return By.cssSelector("*[id='" + selector + "']");
            case "value":
                return By.cssSelector("*[value='" + selector + "']");
            case "label":
                return By.cssSelector(".//option[normalize-space(.)='" + selector + "']");
            case "index":
                return By.cssSelector("*:nth-child('" + selector + "')");
            default:
                throw new IllegalArgumentException(MSG_TARGET_ERROR + s);
        }
    }

    /**
     * 解析keys
     * <p>
     * 支持两种形式：1、直接字符串 2、Key:Key['ENTER']或：${KEY_ENTER}
     * 注意：现在暂不支持变量
     *
     * @param s 字符串
     * @return Keys
     */
    static String parseKeys(String s) {
        Optional<String> key = clampStr(s, "${KEY_", "}");
        if (key.isPresent()) {
            return parseKey(s, key.get());
        }
        key = clampStr(s, "Key['", "']");
        return key.map(value -> parseKey(s, value)).orElse(s);
    }

    private static String parseKey(String s, String key) {
        Keys keys = KEYS.get(key);
        checkArgument(keys != null, "错误的键定义: {}", s);
        return keys.toString();
    }

    /**
     * 抓取屏幕到文件
     *
     * @param driver 浏览器驱动
     * @param file   文件
     * @throws IOException 操作文件异常
     */
    static void shotToFile(RemoteWebDriver driver, File file) throws IOException {
        byte[] bytes = driver.getScreenshotAs(OutputType.BYTES);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(bytes);
        }
    }

    /**
     * 简化异常信息
     * <p>
     * 方便去除WebDriverException 的消息内容增加的很多上下文信息
     *
     * @param t 异常
     * @return 消息
     */
    static String trimMessage(Throwable t) {
        String message = t.getMessage();
        if (message == null) {
            return t.toString();
        }
        int index = message.indexOf('\n');
        if (index > 0) {
            return message.substring(0, index);
        }
        return message;
    }

    static class TrimLog {
        final Throwable t;

        TrimLog(Throwable t) {
            this.t = t;
        }

        @Override
        public String toString() {
            return trimMessage(t);
        }
    }
}
