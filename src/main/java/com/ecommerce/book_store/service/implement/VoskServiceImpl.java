package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.service.abstraction.VoskService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class VoskServiceImpl implements VoskService {
    private static final String MODEL_PATH = "src/main/resources/models/vosk-model-small-vn-0.4";

    @Override
    public String transcribeAudio(InputStream audioStream) {
        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);

        try (Model model = new Model(MODEL_PATH)) {
            Recognizer recognizer = new Recognizer(model, 16000);

            recognizer.setWords(true);
            recognizer.setPartialWords(true);

            if (isWavFormat(bufferedStream)) {
                bufferedStream.skip(44);
                System.out.println("Phát hiện file WAV, đã bỏ qua header");
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder resultBuilder = new StringBuilder();

            while ((bytesRead = bufferedStream.read(buffer)) != -1) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String segmentResult = recognizer.getResult();
                    System.out.println("Segment result: " + segmentResult);

                    try {
                        JsonObject jsonResult = JsonParser.parseString(segmentResult).getAsJsonObject();
                        if (jsonResult.has("text")) {
                            String text = jsonResult.get("text").getAsString();
                            if (!text.isEmpty()) {
                                resultBuilder.append(text).append(" ");
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi xử lý JSON segment: " + e.getMessage());
                    }
                }
            }

            String finalResult = recognizer.getFinalResult();
            System.out.println("Final result JSON: " + finalResult);

            try {
                JsonObject jsonResult = JsonParser.parseString(finalResult).getAsJsonObject();
                if (jsonResult.has("text")) {
                    String text = jsonResult.get("text").getAsString();
                    if (!text.isEmpty()) {
                        resultBuilder.append(text);
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi xử lý JSON cuối cùng: " + e.getMessage());
            }

            String result = resultBuilder.toString().trim();
            System.out.println("Kết quả cuối cùng: " + result);
            return result;
        } catch (IOException e) {
            System.err.println("Lỗi xử lý file âm thanh: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi xử lý file âm thanh", e);
        }
    }

    private boolean isWavFormat(BufferedInputStream bis) throws IOException {
        bis.mark(12);
        byte[] header = new byte[4];
        int bytesRead = bis.read(header);
        bis.reset();

        if (bytesRead != 4) {
            return false;
        }

        String headerStr = new String(header, StandardCharsets.UTF_8);
        return "RIFF".equals(headerStr);
    }


}
