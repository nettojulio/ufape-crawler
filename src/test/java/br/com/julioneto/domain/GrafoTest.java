package br.com.julioneto.domain;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    private Grafo grafo;

    @BeforeEach
    void setUp() {
        grafo = new Grafo();
        Grafo.setMaxDepth(0);
    }

    @AfterEach
    void tearDown() {
        Grafo.setMaxDepth(0);
    }

    @Test
    @DisplayName("Deve definir e obter MAX_DEPTH corretamente")
    void deveDefinirEObterMaxDepth() {
        Grafo.setMaxDepth(10);

        assertEquals(10, Grafo.getMaxDepth());
    }

    @Nested
    @DisplayName("Gerenciamento de Nós (Links) e Arestas")
    class GerenciamentoDeNosEArestas {

        @Test
        @DisplayName("Deve adicionar um novo Link ao grafo")
        void deveAdicionarLink() {
            Link link = new Link("https://example.com");
            grafo.adicionarLink(link);

            assertEquals(1, grafo.getLinks().size(), "O grafo deveria conter 1 link.");
            assertEquals(link, grafo.getLink("https://example.com"), "O link recuperado deveria ser o mesmo que foi adicionado.");
        }

        @Test
        @DisplayName("Não deve adicionar um Link duplicado")
        void naoDeveAdicionarLinkDuplicado() {
            Link link1 = new Link("https://example.com");
            Link link2 = new Link("https://example.com");

            grafo.adicionarLink(link1);
            grafo.adicionarLink(link2);

            assertEquals(1, grafo.getLinks().size(), "O grafo não deveria permitir links duplicados.");
        }

        @Test
        @DisplayName("Deve adicionar uma Aresta entre dois Links existentes")
        void deveAdicionarAresta() {
            Link origem = new Link("https://example.com/origem");
            Link destino = new Link("https://example.com/destino");
            grafo.adicionarLink(origem);
            grafo.adicionarLink(destino);

            grafo.adicionarAresta("https://example.com/origem", "https://example.com/destino");

            Link origemNoGrafo = grafo.getLink("https://example.com/origem");
            assertEquals(1, origemNoGrafo.getArestasDeSaida().size(), "O link de origem deveria ter 1 aresta de saída.");
            assertTrue(origemNoGrafo.getArestasDeSaida().containsKey("https://example.com/destino"));
        }

        @Test
        @DisplayName("Não deve adicionar Aresta se a origem não existir")
        void naoDeveAdicionarArestaSemOrigem() {
            Link destino = new Link("https://example.com/destino");
            grafo.adicionarLink(destino);

            grafo.adicionarAresta("https://url-inexistente.com", "https://example.com/destino");

            assertDoesNotThrow(() -> grafo.adicionarAresta("https://url-inexistente.com", "https://example.com/destino"));
        }
    }

    @Nested
    @DisplayName("Métodos de Consulta")
    class MetodosDeConsulta {

        @BeforeEach
        void prepararGrafoComLinksVariados() {
            Link linkOk = new Link("https://site.com/ok");
            linkOk.setStatusCode(200);

            Link linkNotFound = new Link("https://site.com/not-found");
            linkNotFound.setStatusCode(404);

            Link linkServerError = new Link("https://site.com/server-error");
            linkServerError.setStatusCode(500);

            Link linkRedirect = new Link("https://site.com/redirect");
            linkRedirect.setStatusCode(301);

            grafo.adicionarLink(linkOk);
            grafo.adicionarLink(linkNotFound);
            grafo.adicionarLink(linkServerError);
            grafo.adicionarLink(linkRedirect);
        }

        @Test
        @DisplayName("Deve retornar apenas os links com status de erro (4xx e 5xx)")
        void deveRetornarLinksQuebrados() {
            List<Link> linksQuebrados = grafo.getLinksQuebrados();

            assertEquals(2, linksQuebrados.size(), "Deveria encontrar 2 links quebrados.");
            assertTrue(linksQuebrados.stream().anyMatch(l -> l.getUrl().equals("https://site.com/not-found")));
            assertTrue(linksQuebrados.stream().anyMatch(l -> l.getUrl().equals("https://site.com/server-error")));
            assertFalse(linksQuebrados.stream().anyMatch(l -> l.getStatusCode() == 200));
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia se não houver links quebrados")
        void deveRetornarListaVaziaParaNenhumLinkQuebrado() {
            Grafo grafoSemErros = new Grafo();
            Link linkOk = new Link("https://site.com/ok");
            linkOk.setStatusCode(200);
            grafoSemErros.adicionarLink(linkOk);

            List<Link> linksQuebrados = grafoSemErros.getLinksQuebrados();

            assertTrue(linksQuebrados.isEmpty());
        }

        @Test
        @DisplayName("Deve agrupar os links com erro por status code")
        void deveAgruparLinksQuebradosPorStatusCode() {
            Map<Integer, List<Link>> mapaDeErros = grafo.getLinksQuebradosPorStatusCode();

            assertFalse(mapaDeErros.containsKey(200), "Não deveria incluir o status 200.");
            assertTrue(mapaDeErros.containsKey(404), "Deveria ter uma entrada para o status 404.");
            assertTrue(mapaDeErros.containsKey(500), "Deveria ter uma entrada para o status 500.");
            assertTrue(mapaDeErros.containsKey(301), "Deveria ter uma entrada para o status 301.");

            assertEquals(1, mapaDeErros.get(404).size(), "Deveria haver 1 link na lista do status 404.");
            assertEquals("https://site.com/not-found", mapaDeErros.get(404).get(0).getUrl());
        }
    }
}