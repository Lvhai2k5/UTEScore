package vn.ute.utescore.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.ute.utescore.service.CustomerGeminiChatService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Service gọi Google Gemini API để sinh phản hồi chatbot.
 * Tự động bóc phần "text" ra khỏi JSON để trả lời tự nhiên.
 */
@Service
public class CustomerGeminiChatServiceImpl implements CustomerGeminiChatService {

    private final String apiUrl;

    public CustomerGeminiChatServiceImpl(
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey) {
        this.apiUrl = apiUrl + "?key=" + apiKey;
        System.out.println("🔍 Gemini API endpoint: " + this.apiUrl);
    }

    @Override
    public String askGemini(String prompt) {
        try {
            // ⚙️ Chuẩn bị kết nối
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // 🧠 Tạo body JSON gửi cho Gemini
            String jsonBody = """
                {
                  "contents": [
                    {
                      "role": "user",
                      "parts": [{ "text": "%s" }]
                    }
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\""));

            // 📨 Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            System.out.println("🔍 Gemini HTTP status = " + status);

            InputStream responseStream = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (responseStream == null) {
                return "⚠️ Không thể kết nối đến Gemini API (HTTP " + status + ")";
            }

            // 📥 Đọc phản hồi JSON
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) response.append(line);
            }

            String json = response.toString();
            if (status != 200) {
                return "⚠️ Gemini API trả lỗi (" + status + "): " + json;
            }

            // ✅ Trích phần text bằng Gson
            String text = extractTextFromJson(json);
            return (text != null && !text.isEmpty())
                    ? text
                    : "🤔 Xin lỗi, tôi chưa nhận được phản hồi rõ ràng từ Gemini.";

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Lỗi khi kết nối đến Gemini API: " + e.getMessage();
        }
    }

    /**
     * Trích phần "text" từ JSON phản hồi của Gemini
     */
    private String extractTextFromJson(String json) {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            JsonArray candidates = obj.getAsJsonArray("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JsonObject first = candidates.get(0).getAsJsonObject();
                JsonObject content = first.getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString().trim();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đọc JSON Gemini: " + e.getMessage());
        }
        return null;
    }
}
