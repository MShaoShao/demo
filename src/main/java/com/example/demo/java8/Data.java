package com.example.demo.java8;

import java.util.List;

/**
 * 探测日志数据对象
 *
 * @author MiaoShaoDong
 * @date 11:27 2020/7/6
 */
@lombok.Data
public class Data {
    private String appId;
    private String areaId;
    private String agentId;
    private List<ProbeInfo> probe;
}
