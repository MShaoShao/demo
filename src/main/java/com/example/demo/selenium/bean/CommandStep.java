package com.example.demo.selenium.bean;

import lombok.Data;

/**
 * Selenium 命令步骤
 *
 * @author miaoshaodong
 * @date Creater in 15:52 2019/11/12
 */
@Data
public class CommandStep {
    /**
     * ID(UUID)
     */
    private String id;
    /**
     * 注释（如果有注释，SIDE仅显示注释）
     */
    private String comment;
    /**
     * 命令（类型）
     * <p>
     * 如click、openWindow等
     */
    private String command;
    /**
     * 目标
     * <p>
     * 一般是命令操作的DOM目标。如：所输入（type）的DOM定位方式。
     */
    private String target;
    /**
     * 操作值
     * <p>
     * 一般是命令操作的值。如：所输入的值。
     */
    private String value;
    /**
     * 操作中是否打开新窗口
     */
    private Boolean opensWindow = false;
    /**
     * 新窗口的暂存变量名称
     * <p>
     * 后续操作通过该变量获取打开的新窗口（如：selectWindow）
     */
    private String windowHandleName;
    /**
     * 打开窗口的超时时间
     */
    private String windowTimeout;
    /**
     * 以下变量暂不清楚其含义
     */
    private Boolean isBreakPoint = false;
    private Boolean openWindowRead = false;
}
