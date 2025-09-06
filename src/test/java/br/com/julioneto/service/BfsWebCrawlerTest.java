package br.com.julioneto.service;

import br.com.julioneto.config.CrawlerConfig;
import br.com.julioneto.contract.CrawlerClient;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.dto.CrawlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BfsWebCrawlerTest {

    private Grafo grafo;
    private BfsWebCrawler webCrawler;

    @Mock
    private CrawlerClient mockCrawlerClient;

    @BeforeEach
    void setUp() {
        grafo = new Grafo();
        webCrawler = new BfsWebCrawler(grafo, mockCrawlerClient);
        Grafo.setMaxDepth(0);
    }

    @Nested
    @DisplayName("Rastreamento Sequencial (crawl)")
    class CrawlSequencialmente {

        @Test
        @DisplayName("Deve rastrear um site simples e construir o grafo corretamente")
        void deveRastrearSiteSimples() throws IOException, InterruptedException {
            String startUrl = "https://example.com";
            String urlB = "https://example.com/b";
            String urlC = "https://example.com/c";

            CrawlResponse respStart = mock(CrawlResponse.class);
            CrawlResponse.LinksResponse linksStart = mock(CrawlResponse.LinksResponse.class);
            when(mockCrawlerClient.fetchPageInfo(startUrl)).thenReturn(respStart);
            when(respStart.getStatusCode()).thenReturn(200);
            when(respStart.getLinks()).thenReturn(linksStart);
            when(linksStart.getAvailable()).thenReturn(List.of(urlB, urlC));

            CrawlResponse respB = mock(CrawlResponse.class);
            when(mockCrawlerClient.fetchPageInfo(urlB)).thenReturn(respB);
            when(respB.getStatusCode()).thenReturn(200);
            when(respB.getLinks()).thenReturn(null);

            CrawlResponse respC = mock(CrawlResponse.class);
            when(mockCrawlerClient.fetchPageInfo(urlC)).thenReturn(respC);
            when(respC.getStatusCode()).thenReturn(404);

            webCrawler.crawl(startUrl);

            assertEquals(3, grafo.getLinks().size());
            assertTrue(grafo.getLinks().containsKey(startUrl) && grafo.getLinks().containsKey(urlB) && grafo.getLinks().containsKey(urlC));
            assertEquals(404, grafo.getLink(urlC).getStatusCode());

            Set<String> arestasDeSaida = grafo.getLinks().get(startUrl).getArestasDeSaida().keySet();
            assertEquals(2, arestasDeSaida.size());
            assertTrue(arestasDeSaida.contains(urlB) && arestasDeSaida.contains(urlC));

            verify(mockCrawlerClient).fetchPageInfo(startUrl);
            verify(mockCrawlerClient).fetchPageInfo(urlB);
            verify(mockCrawlerClient).fetchPageInfo(urlC);
        }

        @Test
        @DisplayName("Não deve processar links cuja profundidade exceda MAX_DEPTH")
        void naoDeveProcessarAlemDaProfundidadeMaxima() throws IOException, InterruptedException {
            final int MAX_DEPTH = CrawlerConfig.MAX_DEPTH;
            assertTrue(MAX_DEPTH >= 1, "Este teste assume que MAX_DEPTH é pelo menos 1.");

            String url1 = "https://example.com/depth1";
            String url2 = "https://example.com/depth2";
            String url3 = "https://example.com/depth3";

            CrawlResponse resp1 = mock(CrawlResponse.class);
            CrawlResponse.LinksResponse links1 = mock(CrawlResponse.LinksResponse.class);
            when(mockCrawlerClient.fetchPageInfo(url1)).thenReturn(resp1);
            when(resp1.getStatusCode()).thenReturn(200);
            when(resp1.getLinks()).thenReturn(links1);
            when(links1.getAvailable()).thenReturn(List.of(url2));

            CrawlResponse resp2 = mock(CrawlResponse.class);
            CrawlResponse.LinksResponse links2 = mock(CrawlResponse.LinksResponse.class);
            when(mockCrawlerClient.fetchPageInfo(url2)).thenReturn(resp2);
            when(resp2.getStatusCode()).thenReturn(200);
            when(resp2.getLinks()).thenReturn(links2);
            when(links2.getAvailable()).thenReturn(List.of(url3));

            CrawlResponse resp3 = mock(CrawlResponse.class);
            when(mockCrawlerClient.fetchPageInfo(url3)).thenReturn(resp3);
            when(resp3.getStatusCode()).thenReturn(200);

            webCrawler.crawl(url1);

            verify(mockCrawlerClient, times(1)).fetchPageInfo(url1);

            if (MAX_DEPTH >= 2) {
                verify(mockCrawlerClient, times(1)).fetchPageInfo(url2);
            } else {
                verify(mockCrawlerClient, never()).fetchPageInfo(url2);
            }

            if (MAX_DEPTH >= 3) {
                verify(mockCrawlerClient, times(1)).fetchPageInfo(url3);
            } else {
                verify(mockCrawlerClient, never()).fetchPageInfo(url3);
            }
        }
    }

    @Nested
    @DisplayName("Rastreamento Concorrente (crawlConcurrently)")
    class CrawlConcorrentemente {

        @Test
        @DisplayName("Deve produzir o mesmo resultado final que o rastreamento sequencial")
        void deveProduzirMesmoResultadoDoSequencial() throws IOException, InterruptedException {
            String startUrl = "https://example.com";
            String urlB = "https://example.com/b";
            String urlC = "https://example.com/c";

            CrawlResponse respA = mock(CrawlResponse.class);
            CrawlResponse.LinksResponse linksA = mock(CrawlResponse.LinksResponse.class);
            when(respA.getStatusCode()).thenReturn(200);
            when(respA.getLinks()).thenReturn(linksA);
            when(linksA.getAvailable()).thenReturn(List.of(urlB, urlC));

            CrawlResponse respB = mock(CrawlResponse.class);
            when(respB.getStatusCode()).thenReturn(200);
            when(respB.getLinks()).thenReturn(null);

            CrawlResponse respC = mock(CrawlResponse.class);
            when(respC.getStatusCode()).thenReturn(200);
            when(respC.getLinks()).thenReturn(null);

            when(mockCrawlerClient.fetchPageInfo(anyString())).thenAnswer(invocation -> {
                String url = invocation.getArgument(0);
                if (url.equals(startUrl)) return respA;
                if (url.equals(urlB)) return respB;
                if (url.equals(urlC)) return respC;
                return mock(CrawlResponse.class);
            });

            webCrawler.crawlConcurrently(startUrl);

            assertEquals(3, grafo.getLinks().size());
            assertTrue(grafo.getLinks().containsKey(startUrl) && grafo.getLinks().containsKey(urlB) && grafo.getLinks().containsKey(urlC));
            assertEquals(2, grafo.getLinks().get(startUrl).getArestasDeSaida().size());

            verify(mockCrawlerClient, timeout(5000).times(1)).fetchPageInfo(startUrl);
            verify(mockCrawlerClient, timeout(5000).times(1)).fetchPageInfo(urlB);
            verify(mockCrawlerClient, timeout(5000).times(1)).fetchPageInfo(urlC);
        }
    }
}