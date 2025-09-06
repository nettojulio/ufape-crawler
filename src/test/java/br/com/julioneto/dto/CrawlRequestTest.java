package br.com.julioneto.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrawlRequestTest {

    @Test
    @DisplayName("Deve construir o objeto e atribuir todos os valores corretamente")
    void deveConstruirEAtribuirValoresCorretamente() {
        String url = "https://example.com";
        int timeout = 30;
        boolean removeFragment = true;
        List<String> allowedDomains = List.of("example.com", "sub.example.com");
        boolean collectSubdomains = true;
        boolean lowerCaseUrls = false;
        boolean canRetry = true;
        int maxAttempts = 5;

        CrawlRequest request = new CrawlRequest(
                url,
                timeout,
                removeFragment,
                allowedDomains,
                collectSubdomains,
                lowerCaseUrls,
                canRetry,
                maxAttempts
        );

        assertNotNull(request);
        assertEquals(url, request.getUrl());
        assertEquals(timeout, request.getTimeout());
        assertTrue(request.isRemoveFragment());
        assertEquals(allowedDomains, request.getAllowedDomains());
        assertTrue(request.isCollectSubdomains());
        assertFalse(request.isLowerCaseUrls());
        assertTrue(request.isCanRetry());
        assertEquals(maxAttempts, request.getMaxAttempts());
    }
}