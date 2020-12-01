package com.example.demo.javatrain.readXml.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yutaoxu
 * @date 2020/7/10
 */
@Data
public final class CiDto {
    /**
     * CI的id
     */
    private String id;
    /**
     * 父资产id
     */
    private String resourceId;
    private String typeCode;
    private List<String> tags;
    private List<Alert> alerts;
    private Integer manageStatus;
    /**
     * 属性集
     */
    private Map<String, Object> attributes;
}
