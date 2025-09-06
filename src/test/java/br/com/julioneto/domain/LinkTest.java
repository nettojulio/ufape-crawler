package br.com.julioneto.domain;

import br.com.julioneto.dto.CrawlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkTest {

    private Link linkOrigem;
    private Link linkDestino;
    private Aresta aresta;

    @Mock
    private CrawlResponse mockCrawlResponse;

    @BeforeEach
    void setUp() {
        linkOrigem = new Link("https://example.com/origem");
        linkDestino = new Link("https://example.com/destino");
        aresta = new Aresta(linkOrigem, linkDestino);
    }

    @Test
    @DisplayName("Deve construir o objeto e inicializar os campos corretamente")
    void deveConstruirCorretamente() {
        assertNotNull(aresta, "A aresta não deveria ser nula.");
        assertEquals(linkOrigem, aresta.getOrigem(), "O link de origem não foi definido corretamente.");
        assertEquals(linkDestino, aresta.getDestino(), "O link de destino não foi definido corretamente.");
    }

    @Nested
    @DisplayName("Construtor e Extração de Domínio")
    class ConstrutorTests {

        @ParameterizedTest(name = "Para URL \"{0}\", o domínio deve ser \"{1}\"")
        @CsvSource({
                "https://www.example.com/path/to/resource, www.example.com",
                "http://sub.domain.co.uk, sub.domain.co.uk",
                "https://julioneto.com.br, julioneto.com.br",
                "ftp://server:8080, server"
        })
        @DisplayName("Deve extrair o domínio corretamente de URLs válidas")
        void deveExtrairDominioDeUrlsValidas(String url, String dominioEsperado) {
            Link link = new Link(url);

            assertEquals(dominioEsperado, link.getDomain());
            assertEquals(url, link.getUrl());
        }

        @Test
        @DisplayName("Deve retornar um domínio vazio para uma URL inválida")
        void deveRetornarDominioVazioParaUrlInvalida() {
            String urlInvalida = "isto não é uma url";

            Link link = new Link(urlInvalida);

            assertTrue(link.getDomain().isEmpty(), "O domínio deveria ser vazio para uma URL inválida.");
        }

        @Test
        @DisplayName("Deve usar o construtor padrão sem erros")
        void deveConstruirComConstrutorPadrao() {
            assertDoesNotThrow(() -> {
                Link link = new Link();
                assertNull(link.getUrl());
                assertNull(link.getDomain());
            });
        }
    }

    @Nested
    @DisplayName("Manipulação de Estado")
    class ManipulacaoEstadoTests {

        @Test
        @DisplayName("Deve adicionar uma aresta de saída corretamente")
        void deveAdicionarArestaComSucesso() {
            Link origem = new Link("https://example.com/origem");
            Link destino = new Link("https://example.com/destino");
            Aresta aresta = new Aresta(origem, destino);

            origem.adicionarAresta(aresta);

            assertEquals(1, origem.getArestasDeSaida().size(), "Deveria haver uma aresta de saída.");
            assertTrue(origem.getArestasDeSaida().containsKey(destino.getUrl()), "O mapa de arestas deveria conter a URL de destino como chave.");
            assertEquals(aresta, origem.getArestasDeSaida().get(destino.getUrl()), "A aresta armazenada deveria ser a mesma que foi adicionada.");
        }

        @Test
        @DisplayName("Deve ignorar a adição de uma aresta nula sem lançar exceção")
        void deveIgnorarArestaNula() {
            Link origem = new Link("https://example.com/origem");

            assertDoesNotThrow(() -> {
                origem.adicionarAresta(null);
            }, "Adicionar aresta nula não deveria lançar exceção.");
            assertTrue(origem.getArestasDeSaida().isEmpty(), "O mapa de arestas deveria continuar vazio.");
        }

        @Test
        @DisplayName("Deve ignorar a adição de uma aresta com destino nulo")
        void deveIgnorarArestaComDestinoNulo() {
            Link origem = new Link("https://example.com/origem");
            Aresta arestaSemDestino = new Aresta(origem, null);

            origem.adicionarAresta(arestaSemDestino);

            assertTrue(origem.getArestasDeSaida().isEmpty(), "Não deveria adicionar uma aresta com destino nulo.");
        }

        @Test
        @DisplayName("Deve atualizar os dados do link a partir de um CrawlResponse")
        void deveAtualizarDadosAPartirDeCrawlResponse() {
            Link link = new Link("https://example.com");

            when(mockCrawlResponse.getStatusCode()).thenReturn(200);
            when(mockCrawlResponse.getTitle()).thenReturn("Título da Página de Teste");
            when(mockCrawlResponse.getContentType()).thenReturn("text/html; charset=utf-8");
            when(mockCrawlResponse.getElapsedTime()).thenReturn(98765L);

            link.updateLinkData(mockCrawlResponse);

            assertEquals(200, link.getStatusCode());
            assertEquals("Título da Página de Teste", link.getTitle());
            assertEquals("text/html; charset=utf-8", link.getContentType());
            assertEquals(98765L, link.getResponseTime());
        }
    }

    @Nested
    @DisplayName("Contrato de equals() e hashCode()")
    class EqualsAndHashCodeContract {

        @Test
        @DisplayName("Uma aresta deve ser igual a si mesma")
        void deveSerIgualASiMesma() {
            assertEquals(aresta, aresta, "Um objeto deve ser igual a ele mesmo.");
        }

        @Test
        @DisplayName("Dois links com a mesma URL devem ser considerados iguais")
        void deveSerIgualParaMesmaUrl() {
            Link link1 = new Link("https://example.com");
            Link link2 = new Link("https://example.com");

            assertEquals(link1, link2, "Links com a mesma URL deveriam ser iguais.");
        }

        @Test
        @DisplayName("Dois links com a mesma URL devem ser iguais mesmo com outros atributos diferentes")
        void deveSerIgualParaMesmaUrlApesarDeOutrosAtributos() {
            Link link1 = new Link("https://example.com");
            link1.setStatusCode(200);
            link1.setDepth(1);

            Link link2 = new Link("https://example.com");
            link2.setStatusCode(404);
            link2.setDepth(2);

            assertEquals(link1, link2, "A igualdade deve ser baseada apenas na URL.");
        }

        @Test
        @DisplayName("Dois links com URLs diferentes não devem ser iguais")
        void naoDeveSerIgualParaUrlsDiferentes() {
            Link link1 = new Link("https://example.com/a");
            Link link2 = new Link("https://example.com/b");

            assertNotEquals(link1, link2, "Links com URLs diferentes não deveriam ser iguais.");
        }

        @Test
        @DisplayName("Um link não deve ser igual a nulo")
        void naoDeveSerIgualANulo() {
            Link link = new Link("https://example.com");

            assertNotEquals(null, link);
        }

        @Test
        @DisplayName("Dois links iguais devem ter o mesmo hashCode")
        void deveTerMesmoHashCodeParaLinksIguais() {
            Link link1 = new Link("https://example.com");
            link1.setStatusCode(200);

            Link link2 = new Link("https://example.com");
            link2.setStatusCode(404);

            assertEquals(link1.hashCode(), link2.hashCode(), "Links iguais (mesma URL) devem ter o mesmo hashCode.");
        }

        @Test
        @DisplayName("Dois links diferentes devem ter (provavelmente) hashCodes diferentes")
        void deveTerHashCodeDiferenteParaLinksDiferentes() {
            Link link1 = new Link("https://example.com/a");
            Link link2 = new Link("https://example.com/b");

            assertNotEquals(link1.hashCode(), link2.hashCode());
        }
    }
}