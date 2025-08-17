package br.com.julioneto.service;

import br.com.julioneto.models.HealthCheckResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthCheckService {
    private static final String BASE_URL = "http://localhost:8080";

    public static HealthCheckResponse check() throws Exception {
        URL url = new URL(BASE_URL + "/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Healthcheck falhou! HTTP " + connection.getResponseCode());
        }

        try (InputStream in = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, HealthCheckResponse.class);
        }
    }
}
