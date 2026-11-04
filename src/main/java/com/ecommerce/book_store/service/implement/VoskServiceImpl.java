package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.service.abstraction.VoskService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class VoskServiceImpl implements VoskService {
    @Value("${vosk.model.path:models/vosk-model-small-vn-0.4}")
    private String modelPath;

    private volatile Model model;

    private Model getModel() throws IOException {
        Model current = model;
        if (current != null) return current;

        synchronized (this) {
            if (model == null) {
                Path path = Path.of(modelPath);
                if (!Files.isDirectory(path)) {
                    throw new IllegalStateException(
                            "Vosk model not found at '" + path.toAbsolutePath()
                                    + "'. Set VOSK_MODEL_PATH to the extracted model directory.");
                }
                model = new Model(path.toString());
                log.info("Loaded Vosk model from {}", path.toAbsolutePath());
            }
            return model;
        }
    }

    @Override
    public String transcribeAudio(java.io.InputStream audioStream) {
        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);

        try (Recognizer recognizer = new Recognizer(getModel(), 16000)) {
            recognizer.setWords(true);
            recognizer.setPartialWords(true);

            if (isWavFormat(bufferedStream)) {
                bufferedStream.skip(44);
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder resultBuilder = new StringBuilder();

            while ((bytesRead = bufferedStream.read(buffer)) != -1) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    appendText(resultBuilder, recognizer.getResult());
                }
            }
            appendText(resultBuilder, recognizer.getFinalResult());

            return resultBuilder.toString().trim();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to transcribe audio", e);
        }
    }

    private void appendText(StringBuilder builder, String json) {
        try {
            JsonObject result = JsonParser.parseString(json).getAsJsonObject();
            if (result.has("text")) {
                String text = result.get("text").getAsString();
                if (!text.isBlank()) {
                    builder.append(text).append(' ');
                }
            }
        } catch (Exception e) {
            log.warn("Could not parse Vosk result: {}", e.getMessage());
        }
    }

    private boolean isWavFormat(BufferedInputStream bis) throws IOException {
        bis.mark(12);
        byte[] header = new byte[4];
        int bytesRead = bis.read(header);
        bis.reset();
        if (bytesRead != 4) return false;
        return "RIFF".equals(new String(header, StandardCharsets.UTF_8));
    }

    @PreDestroy
    void close() {
        if (model != null) {
            try {
                model.close();
            } catch (Exception e) {
                log.warn("Failed to close Vosk model", e);
            }
        }
    }
}
