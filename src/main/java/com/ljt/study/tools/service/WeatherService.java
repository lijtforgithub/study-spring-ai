package com.ljt.study.tools.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.function.Function;

/**
 * @author LiJingTang
 * @date 2025-03-26 17:21
 */
@Slf4j
public class WeatherService implements Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> {

    public enum Unit { C, F }
    public record WeatherRequest(String x, String y, Unit unit) {}
    public record WeatherResponse(double temp, Unit unit) {}
    public WeatherResponse apply(WeatherRequest request) {
        log.info("WeatherRequest: {}", request);
        return new WeatherResponse(new Random().nextInt(30), Unit.C);
    }

}

