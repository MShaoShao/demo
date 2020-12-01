package com.example.demo.javatrain.readXml;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MiaoShaoDong
 * @date 14:15 2020/11/2
 */
public class BeansTest {
    @Test
    public void test1(){
        try {
            Beans bean = new Beans("ss.xml");
            System.out.println(bean);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

}