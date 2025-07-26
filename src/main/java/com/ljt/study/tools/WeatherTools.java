package com.ljt.study.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author LiJingTang
 * @date 2025-03-27 09:23
 */
@Slf4j
@Configuration
public class WeatherTools {

    @Tool(name = "getWeather", description = "查询天气")
    String getWeather(@ToolParam(description = "时间") String x, @ToolParam(description = "地点") String y) {
        log.info("查询天气 {} {}", x, y);
        return new Random().nextInt(30) + "度";
    }

}
