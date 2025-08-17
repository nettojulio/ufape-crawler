package br.com.julioneto.service;

import br.com.julioneto.models.ExternalCrawlerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostService {
    private static final String BASE_URL = "http://localhost:8080";

    public static ExternalCrawlerResponse sendCrawlRequest(String targetUrl) throws Exception {
        URL url = new URL(BASE_URL + "/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        JSONObject body = new JSONObject();
        body.put("url", targetUrl);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("POST falhou! HTTP " + connection.getResponseCode());
        }

        try (InputStream in = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, ExternalCrawlerResponse.class);
        }
    }
}
