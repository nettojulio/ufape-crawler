package br.com.julioneto.service;

import br.com.julioneto.config.CrawlerConfig;
import br.com.julioneto.contract.CrawlerClient;
import br.com.julioneto.dto.CrawlRequest;
import br.com.julioneto.dto.CrawlResponse;
import br.com.julioneto.dto.HealthCheckResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


/**
 * Implementação do CrawlerClient que se comunica com a API externa.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 *
 */
public class ExternalApiCrawlerClient implements CrawlerClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ExternalApiCrawlerClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CrawlResponse fetchPageInfo(String url) throws IOException, InterruptedException {
        CrawlRequest requestPayload = new CrawlRequest(
                url,
                CrawlerConfig.API_TIMEOUT_SECONDS,
                false,
                CrawlerConfig.ALLOWED_DOMAINS,
                true,
                false,
                true,
                3
        );
        String requestBody = objectMapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API request failed with status code: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), CrawlResponse.class);
    }

    @Override
    public HealthCheckResponse checkApiHealth() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API health check failed with status code: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), HealthCheckResponse.class);
    }
}
