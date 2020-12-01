package com.example.demo.javatrain.readXml.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 同时返回ci和关系
 *
 * @author houyong
 */
@Data
@AllArgsConstructor
public final class CiAndRelationResp {
    /**
     * 所有的组件ci
     */
    private List<CiDto> ci;
    /**
     * 组件之间的关系
     */
    private List<RelationDto> relation;
}
