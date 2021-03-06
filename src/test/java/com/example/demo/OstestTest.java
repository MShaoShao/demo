package com.example.demo;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Dimension;
import java.net.MalformedURLException;
public class OstestTest {
    private  ChromeDriver driver;
    @Before
    public void setUp() throws MalformedURLException {

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false);
        driver = new ChromeDriver(options);
    }
    @After
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void ostest() {
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
        // 4 | click | id=submitlogin |  |
        driver.findElement(By.id("submitlogin")).click();
    }
}

