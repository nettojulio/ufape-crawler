package br.com.julioneto.service;

import br.com.julioneto.dto.CrawlResponse;
import br.com.julioneto.dto.HealthCheckResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalApiCrawlerClientTest {

    private static final String BASE_URL = "https://api.fakecrawler.com";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private ObjectMapper realObjectMapper;
    private ExternalApiCrawlerClient crawlerClient;

    @BeforeEach
    void setUp() {
        realObjectMapper = new ObjectMapper();
        crawlerClient = new ExternalApiCrawlerClient(BASE_URL, mockHttpClient, realObjectMapper);
    }

    @Nested
    @DisplayName("Método fetchPageInfo")
    class FetchPageInfoTests {

        @Test
        @DisplayName("Deve retornar CrawlResponse quando a API responde com sucesso (200)")
        void deveRetornarCrawlResponseComSucesso() throws IOException, InterruptedException {
            String urlParaBuscar = "https://example.com";
            String corpoDaRespostaJson = """
                    {
                      "statusCode": 200,
                      "title": "Página de Sucesso"
                    }
                    """;

            when(mockHttpResponse.statusCode()).thenReturn(200);
            when(mockHttpResponse.body()).thenReturn(corpoDaRespostaJson);

            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockHttpResponse);

            CrawlResponse response = crawlerClient.fetchPageInfo(urlParaBuscar);

            assertNotNull(response);
            assertEquals(200, response.getStatusCode());
            assertEquals("Página de Sucesso", response.getTitle());

            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any());
            assertEquals("POST", requestCaptor.getValue().method());
            assertTrue(requestCaptor.getValue().uri().toString().contains(BASE_URL));
        }

        @Test
        @DisplayName("Deve lançar IOException quando a API responde com erro (não 200)")
        void deveLancarIOExceptionParaStatusDeErro() throws IOException, InterruptedException {
            when(mockHttpResponse.statusCode()).thenReturn(500); // Simula um erro de servidor
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockHttpResponse);

            IOException exception = assertThrows(IOException.class, () -> {
                crawlerClient.fetchPageInfo("https://example.com");
            });

            assertEquals("API request failed with status code: 500", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Método checkApiHealth")
    class CheckApiHealthTests {

        @Test
        @DisplayName("Deve retornar HealthCheckResponse quando a API está saudável (200)")
        void deveRetornarHealthCheckComSucesso() throws IOException, InterruptedException {
            String corpoDaRespostaJson = """
                    {
                      "status": "ok",
                      "timestamp": "2025-09-05T20:33:00Z"
                    }
                    """;

            when(mockHttpResponse.statusCode()).thenReturn(200);
            when(mockHttpResponse.body()).thenReturn(corpoDaRespostaJson);
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockHttpResponse);

            HealthCheckResponse response = crawlerClient.checkApiHealth();

            assertNotNull(response);
            assertEquals("ok", response.getStatus());

            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any());

            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("GET", capturedRequest.method());
            assertEquals(BASE_URL + "/", capturedRequest.uri().toString());
        }

        @Test
        @DisplayName("Deve lançar IOException quando o health check falha (não 200)")
        void deveLancarIOExceptionParaHealthCheckComErro() throws IOException, InterruptedException {
            when(mockHttpResponse.statusCode()).thenReturn(503); // Service Unavailable
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockHttpResponse);

            IOException exception = assertThrows(IOException.class, () -> {
                crawlerClient.checkApiHealth();
            });

            assertEquals("API health check failed with status code: 503", exception.getMessage());
        }
    }
}