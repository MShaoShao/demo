package com.example.demo.javatrain.readXml.bean;

import lombok.Data;

import java.util.List;

/**
 * 告警
 *
 * @author houyong
 */
@Data
public final class Alert {
    /**
     * 告警id
     */
    private Long id;
    /**
     * 级别
     */
    private Integer level;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 来源
     */
    private String source;

    /**
     * 标签
     */
    private List<String> tags;

}
