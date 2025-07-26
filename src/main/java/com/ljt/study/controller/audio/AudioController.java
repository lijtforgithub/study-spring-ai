package com.ljt.study.controller.audio;

import com.alibaba.cloud.ai.dashscope.api.DashScopeSpeechSynthesisApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.audio.transcription.AudioTranscriptionModel;
import jakarta.annotation.Resource;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * @author LiJingTang
 * @date 2025-03-24 15:36
 */
@RestController
@RequestMapping("/audio")
public class AudioController {

    @Resource
    private AudioTranscriptionModel audioTranscriptionModel;
    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    @GetMapping("/toText")
    public AudioTranscriptionResponse toText() {
        var transcriptionOptions = DashScopeAudioTranscriptionOptions.builder()
                .withModel("sensevoice-v1")
                .build();

        var audioFile = new FileSystemResource("/Users/lijingtang/workspace/study/tts.mp3");
        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
        AudioTranscriptionResponse response = audioTranscriptionModel.call(transcriptionRequest);
        return response;
    }

    @GetMapping("/tts")
    public String tts() {
        SpeechSynthesisOptions speechOptions = DashScopeSpeechSynthesisOptions.builder()
                .withSpeed(1.0)
                .withResponseFormat(DashScopeSpeechSynthesisApi.ResponseFormat.MP3)
                .build();

        SpeechSynthesisPrompt speechPrompt = new SpeechSynthesisPrompt("Today is a wonderful day to build something people love!", speechOptions);
        SpeechSynthesisResponse response = speechSynthesisModel.call(speechPrompt);

        ByteBuffer audio = response.getResult().getOutput().getAudio();
        if (audio.hasRemaining()) {
            File file = new File("tts.mp3");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(audio.array());
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        return "OK";
    }

}
