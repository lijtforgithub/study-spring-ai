package com.ljt.study.controller.image;

import jakarta.annotation.Resource;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJingTang
 * @date 2025-03-21 17:52
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    private ImageModel imageModel;

    @GetMapping("/textTo")
    ImageResponse textToImage(@RequestParam(required = false, defaultValue = "一张搞笑图片") String prompt) {
        ImageResponse response = imageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                                .quality("hd")
                                .N(1)
                                .height(1024)
                                .width(1024).build())

        );

        return response;
    }

}
