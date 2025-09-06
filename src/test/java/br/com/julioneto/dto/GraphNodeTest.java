package br.com.julioneto.dto;

import br.com.julioneto.domain.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphNodeTest {

    @Test
    @DisplayName("Deve mapear todos os campos de um objeto Link corretamente")
    void deveMapearTodosOsCamposDeUmLink() {
        Link linkFonte = new Link("https://www.example.com/page");
        linkFonte.setDepth(3);
        linkFonte.setStatusCode(200);
        linkFonte.setContentType("text/html; charset=utf-8");
        linkFonte.setResponseTime(123456789L);
        linkFonte.setTitle("Título da Página de Teste");
        linkFonte.setDomain("www.example.com");

        GraphNode graphNode = new GraphNode(linkFonte);

        assertNotNull(graphNode);
        assertEquals("https://www.example.com/page", graphNode.getId(), "O ID deve ser a URL do link.");
        assertEquals(3, graphNode.getDepth(), "O depth deve ser mapeado corretamente.");
        assertEquals(200, graphNode.getStatusCode(), "O statusCode deve ser mapeado corretamente.");
        assertEquals("text/html; charset=utf-8", graphNode.getContentType(), "O contentType deve ser mapeado corretamente.");
        assertEquals(123456789L, graphNode.getElapsedTime(), "O elapsedTime deve ser o responseTime do link.");
        assertEquals("Título da Página de Teste", graphNode.getTitle(), "O title deve ser mapeado corretamente.");
        assertEquals("www.example.com", graphNode.getDomain(), "O domain deve ser mapeado corretamente.");
    }

    @Test
    @DisplayName("Deve lidar corretamente com um Link com campos nulos ou com valores padrão")
    void deveLidarComCamposNulosOuPadrao() {
        Link linkVazio = new Link();
        linkVazio.setUrl("https://empty.com");

        GraphNode graphNode = new GraphNode(linkVazio);

        assertEquals("https://empty.com", graphNode.getId());
        assertEquals(0, graphNode.getDepth());
        assertEquals(0, graphNode.getStatusCode());
        assertEquals(0L, graphNode.getElapsedTime());
        assertNull(graphNode.getContentType(), "ContentType nulo deveria ser mapeado como nulo.");
        assertNull(graphNode.getTitle(), "Title nulo deveria ser mapeado como nulo.");
        assertNull(graphNode.getDomain());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException se o Link de origem for nulo")
    void deveLancarExcecaoParaLinkNulo() {
        Link linkNulo = null;

        assertThrows(NullPointerException.class, () -> {
            new GraphNode(linkNulo);
        }, "O construtor deveria lançar NullPointerException para um Link nulo.");
    }
}