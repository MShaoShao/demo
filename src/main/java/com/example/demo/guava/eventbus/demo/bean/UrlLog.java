package com.example.demo.guava.eventbus.demo.bean;

import lombok.Data;

/**
 * @author MiaoShaoDong
 * @date 15:07 2020/6/10
 */
@Data
public class UrlLog {
    String url;
    long createTime;
    Boolean status;
    String proType;
}
