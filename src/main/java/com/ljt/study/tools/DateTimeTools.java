package com.ljt.study.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author LiJingTang
 * @date 2025-03-25 10:03
 */
@Slf4j
public class DateTimeTools {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * returnDirect：工具结果应直接返回给客户端还是传递回模型 （测试调用工具方法之后还是去请求了大模型）
     * resultConverter：用于将工具调用的结果转换为 String 对象以发送回 AI 模型
     */
    @Tool(name = "getCurrentTime", description = "获取当前时间")
    String getCurrentTime() {
        String time = LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).format(DateTimeFormatter.ofPattern(FORMAT));
        log.info("获取当前时间 {}", time);
        return time;
    }

    @Tool(description = "设置闹钟, 时间格式是" + FORMAT)
    void setAlarm(@ToolParam String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(FORMAT));
        log.info("闹钟设置成功 {}", alarmTime);
    }

    @Tool(description = "计算两个数相乘的结果")
    int multiply(@ToolParam int  a, @ToolParam int b) {
        int result = a * b;
        log.info("{} * {} = {}", a, b, result);
        return result;
    }


}
