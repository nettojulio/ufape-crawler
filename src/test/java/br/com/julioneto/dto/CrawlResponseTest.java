package br.com.julioneto.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CrawlResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve desserializar um JSON completo e popular todos os campos corretamente")
    void deveDesserializarJsonCompleto() throws IOException {
        String jsonCompleto = """
                {
                  "statusCode": 200,
                  "contentType": "text/html; charset=utf-8",
                  "elapsedTime": 123456789,
                  "title": "Página de Exemplo",
                  "links": {
                    "available": [
                      "https://example.com/link1",
                      "https://example.com/link2"
                    ],
                    "unavailable": [
                      "https://example.com/link3"
                    ]
                  },
                  "details": {
                    "correctUrl": "https://example.com/corrigida",
                    "original": {
                      "Host": "example.com",
                      "Path": "/original"
                    },
                    "modified": {
                      "Host": "www.example.com",
                      "Path": "/modificado"
                    }
                  }
                }
                """;

        CrawlResponse response = objectMapper.readValue(jsonCompleto, CrawlResponse.class);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("text/html; charset=utf-8", response.getContentType());
        assertEquals(123456789L, response.getElapsedTime());
        assertEquals("Página de Exemplo", response.getTitle());

        assertNotNull(response.getLinks());
        assertEquals(2, response.getLinks().getAvailable().size());
        assertEquals("https://example.com/link1", response.getLinks().getAvailable().get(0));
        assertEquals(1, response.getLinks().getUnavailable().size());

        assertNotNull(response.getDetails());
        assertEquals("https://example.com/corrigida", response.getDetails().getCorrectUrl());
        assertNotNull(response.getDetails().getOriginal());
        assertEquals("example.com", response.getDetails().getOriginal().getHost());
        assertEquals("/original", response.getDetails().getOriginal().getPath());
        assertNotNull(response.getDetails().getModified());
        assertEquals("www.example.com", response.getDetails().getModified().getHost());
        assertEquals("/modificado", response.getDetails().getModified().getPath());
    }

    @Test
    @DisplayName("Deve lidar com um JSON com campos ausentes sem lançar erro")
    void deveLidarComJsonIncompleto() throws IOException {
        String jsonIncompleto = """
                {
                  "statusCode": 404,
                  "title": "Não Encontrado"
                }
                """;

        CrawlResponse response = objectMapper.readValue(jsonIncompleto, CrawlResponse.class);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
        assertEquals("Não Encontrado", response.getTitle());

        assertNull(response.getContentType());
        assertEquals(0L, response.getElapsedTime());
        assertNull(response.getLinks());
        assertNull(response.getDetails());
    }

    @Test
    @DisplayName("Deve lidar com valores nulos explícitos no JSON")
    void deveLidarComValoresNulosNoJson() throws IOException {
        String jsonComNulos = """
                {
                  "statusCode": 500,
                  "title": "Erro Interno",
                  "links": null,
                  "details": {
                    "correctUrl": null,
                    "original": null
                  }
                }
                """;

        CrawlResponse response = objectMapper.readValue(jsonComNulos, CrawlResponse.class);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode());
        assertEquals("Erro Interno", response.getTitle());

        assertNull(response.getLinks());
        assertNotNull(response.getDetails());
        assertNull(response.getDetails().getCorrectUrl());
        assertNull(response.getDetails().getOriginal());
    }
}