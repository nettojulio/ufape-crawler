package br.com.julioneto.service;

import br.com.julioneto.models.ExternalCrawlerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PostService {
    private static final String BASE_URL = "http://localhost:8080";

    private PostService() {

    }

    public static ExternalCrawlerResponse sendCrawlRequest(String targetUrl) throws Exception {
        URL url = URI.create(BASE_URL.concat("/")).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setDoOutput(true);

        List<String> allowedDomainsList = new ArrayList<>(
                List.of("ufape.edu.br")
        );

        JSONObject body = new JSONObject();
        JSONArray allowedDomains = new JSONArray();

        for (String domain : allowedDomainsList) {
            allowedDomains.put(domain);
        }

        body.put("url", targetUrl);
        body.put("timeout", 90); // tempo em segundos
        body.put("remove_fragment", true);
        body.put("allowed_domains", allowedDomains);
        body.put("collect_subdomains", true);
        body.put("lower_case_urls", false);

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
