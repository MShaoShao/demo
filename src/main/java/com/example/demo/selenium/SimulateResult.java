package com.example.demo.selenium;

import lombok.Data;

import java.util.Date;

/**
 * WEB模拟结果
 *
 * @author miaoshaodong
 * @date Creater in 10:47 2019/11/13
 */
@Data
public class SimulateResult {

    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结果状态
     * <p>
     * 0：正常
     * 1：失败（模拟进行了，但是不满足判断条件）
     * 2：异常（无driver/无法执行）
     */
    private int state;
    /**
     * 执行时长（单位：毫秒）
     */
    private int usedTime;
    /**
     * 消息内容（执行情况）
     */
    private String message;

    /**
     * 页面文本（失败时提供，方便排查问题）
     */
    private String innerText;

    public static SimulateResult init(Date startTime, int state) {
        SimulateResult result = new SimulateResult();
        result.startTime = startTime;
        result.state = state;
        return result;
    }

    /**
     * 设置状态和信息
     * <p>
     * 状态和消息一般是同时设置
     *
     * @param
     * @param
     */
    public void setState(int state, String message) {
        this.state = state;
        this.message = message;
    }

}
