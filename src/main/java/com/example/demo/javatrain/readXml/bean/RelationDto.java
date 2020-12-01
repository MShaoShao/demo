package com.example.demo.javatrain.readXml.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author: yutaoxu
 * @date: 2020/7/10
 */
@Data
public class RelationDto {
    private String from;
    private String to;
    private String id;
    private String relationTypeCode;
    private Map<String, Object> attributes;

}
