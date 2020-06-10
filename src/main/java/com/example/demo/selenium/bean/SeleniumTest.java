package com.example.demo.selenium.bean;

import lombok.Data;

import java.util.List;

/**
 * SIDE 测试用例
 *
 * @author miaoshaodong
 * @date Creater in 15:47 2019/11/12
 */
@Data
public class SeleniumTest {
    /**
     * ID(UUID)
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 命令列表（多个步骤）
     */
    private List<CommandStep> commands;
}
