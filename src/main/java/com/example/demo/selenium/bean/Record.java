package com.example.demo.selenium.bean;

import lombok.Data;

import java.util.List;

/**
 * SIDE录像
 * <p>
 * 一个SIDE录像分为多个用例，每个用例包含多个步骤（每个步骤为一个命令）
 * 另外：SIDE除了如下字段外，其余字段暂无用处，未进行设计
 *
 * @author miaoshaodong
 * @date Creater in 15:34 2019/11/12
 */
@Data
public class Record {

    /**
     * ID(UUID)
     */
    private String id;

    /**
     * 版本（当前仅支持2.0）
     */
    private String version;
    /**
     * 名称
     */
    private String name;
    /**
     * 基础URL
     */
    private String url;
    /**
     * 测试用例
     */
    private List<SeleniumTest> tests;
}
