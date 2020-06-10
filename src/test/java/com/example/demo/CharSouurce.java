package com.example.demo;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharSouurce {
    @Test
    public  void testCharSourceWrap() throws IOException {

        CharSource charSource = CharSource.wrap("i am miaoshaoshao");
        String resultRead = charSource.read();
        assertThat(resultRead,equalTo("i am miaoshaoshao"));
        ImmutableList<String> list = charSource.readLines();
        assertThat(list.size(),equalTo(1));
        assertThat(charSource.length(),equalTo(17L));
        assertThat(charSource.isEmpty(),equalTo(false));
        assertThat(charSource.lengthIfKnown().get(),equalTo(17L));
    }
    @Test
    public  void testLambda() throws InterruptedException {
        new Thread(new Runnable() {
             @Override
             public void run() {
                 System.out.println(Thread.currentThread().getName());
             }
         }).start();

         new Thread(()->System.out.println(Thread.currentThread().getName())).start();
         Thread.currentThread().join();
    }
}
