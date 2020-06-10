package com.example.demo;

import java.util.*;

public class ChartDataGeneralize {
    public static void main(String[] args) {
        Long ts = 1573142400000L;
        double v=0;
        List<String> charData = new ArrayList<>();

       while (ts<1573228800000L){
           if (ts<1573178400000L){
               v=v+2;
           }else if (ts==1573178400000L){
               v=300;
           }else if (ts>1573178400000L&&ts<1573185600000L){
               v=v-10;
           }else if (ts==1573178400000L){
               v= 100;
           }
           charData.add("{t:"+ts+", v:"+v+"}\n");
           ts +=60000L;
       }
        System.out.println(charData);
    }
}
