package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.service.abstraction.SpeechToTextService;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Before use just set the GOOGLE_APPLICATION_CREDENTIALS environment variable
 * <h6>Open Terminal with Administrator Permission and type the command below: </h6>
 * <br />
 * <code>[Environment]::SetEnvironmentVariable("GOOGLE_APPLICATION_CREDENTIALS", "path/to/credentials.json", "Machine")</code>
 */
@Service
@Slf4j
public class SpeechToTextServiceImpl implements SpeechToTextService {

    @Value("${google.speech.language:vi-VN}")
    private String languageCode;

    @Override
    public String transcribeAudio(InputStream audioStream) {
        try (SpeechClient speechClient = SpeechClient.create()) {
            // Read the audio stream into a byte array
            byte[] audioBytes = audioStream.readAllBytes();
            ByteString audioData = ByteString.copyFrom(audioBytes);

            // Configure the audio settings
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)  // For WAV files
                    .setSampleRateHertz(16000)  // Common sample rate
                    .setLanguageCode(languageCode)
                    .setEnableAutomaticPunctuation(true)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            // Perform the transcription
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            // Process the results
            if (results.isEmpty()) {
                log.warn("No speech recognition results found");
                return "";
            }

            // Combine all transcriptions
            return results.stream()
                    .flatMap(result -> result.getAlternativesList().stream())
                    .map(SpeechRecognitionAlternative::getTranscript)
                    .collect(Collectors.joining(" "))
                    .trim();

        } catch (IOException e) {
            log.error("Error transcribing audio", e);
            throw new RuntimeException("Error processing audio file", e);
        }
    }
}