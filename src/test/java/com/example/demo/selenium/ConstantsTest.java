package com.example.demo.selenium;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.*;

/**
 * 常量解析单元测试
 *
 * @author miaoshaodong
 * @date Creater in 10:10 2019/11/14
 */
@Slf4j
public class ConstantsTest {

    @Test
    public void testParseDimension() {
        Dimension dimension = Constants.parseDimension("1024x1360");
        assertThat(1024, equalTo(dimension.getWidth()));
        assertThat(1360, equalTo(dimension.getHeight()));
    }

    @Test
    public void testParseBy() {
        By.ById by = (By.ById) Constants.parseBy("id=123");
        assertThat(By.id("123"), equalTo(by));
    }

    @Test
    public void testSplitByChar() {
        Optional<Map.Entry<String, String>> s = Constants.splitByChar("2=t", '=');
        assertThat("2", equalTo(s.get().getKey()));
        assertThat("t", equalTo(s.get().getValue()));
    }
    @Test
    public void testParseKeys(){
        for (String s : ImmutableList.of("${KEY_TAB}", "Key['TAB']")){
            CharSequence charSequence = Constants.parseKeys(s);
            if (charSequence instanceof Keys){
                charSequence = ((Keys) charSequence).name();
            }
            log.info("{},{}",s,charSequence);
        }
    }

    @Test
    public void testConcatUrl(){
        Arrays.asList(
                new SimpleEntry<>("a","b"),
                new SimpleEntry<>("a/","/b"),
                new SimpleEntry<>("a/","b"),
                new SimpleEntry<>("a","/b")
                ).forEach(pair -> {
                    String s = Constants.concatUrl(pair.getKey(),pair.getValue());
                    assertThat("a/b",equalTo(s));
        });
    }

    @Test
    public void testTrimprefix(){
        Optional<String> s = Constants.trimPrefix("${cnd","${");
        assertThat(s.isPresent(),equalTo(true));
        assertThat("cnd",equalTo(s.get()));
    }

    @Test
    public void testClampStr(){
        Optional<String> s = Constants.clampStr("${cnd}","${","}");
        assertThat(s.isPresent(),equalTo(true));
        assertThat("cnd",equalTo(s.get()));
    }
}
