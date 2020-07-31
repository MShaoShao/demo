package com.example.demo.java8;

import lombok.Data;

import java.util.Map;

/**
 * 拨测详细数据
 *
 * @author MiaoShaoDong
 * @date 11:28 2020/7/6
 */
@Data
public class ProbeInfo {
    private Long time;
    private String probeType;
    private Map<String, Object> data;
}

