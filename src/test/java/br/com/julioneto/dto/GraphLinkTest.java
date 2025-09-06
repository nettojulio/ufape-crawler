package br.com.julioneto.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphLinkTest {

    @Test
    @DisplayName("Deve construir o objeto e inicializar source e target corretamente")
    void deveConstruirCorretamenteComConstrutor() {
        String sourceUrl = "https://example.com/source";
        String targetUrl = "https://example.com/target";

        GraphLink graphLink = new GraphLink(sourceUrl, targetUrl);

        assertNotNull(graphLink, "O objeto GraphLink não deveria ser nulo.");
        assertEquals(sourceUrl, graphLink.getSource(), "O campo 'source' não foi inicializado corretamente.");
        assertEquals(targetUrl, graphLink.getTarget(), "O campo 'target' não foi inicializado corretamente.");
    }

    @Test
    @DisplayName("Deve permitir a alteração do campo 'source' através do setter")
    void devePermitirAlterarSourceComSetter() {
        GraphLink graphLink = new GraphLink("source-inicial", "target-inicial");
        String novaSource = "https://example.com/nova-source";

        graphLink.setSource(novaSource);

        assertEquals(novaSource, graphLink.getSource(), "O método setSource() não atualizou o campo corretamente.");
    }

    @Test
    @DisplayName("Deve permitir a alteração do campo 'target' através do setter")
    void devePermitirAlterarTargetComSetter() {
        GraphLink graphLink = new GraphLink("source-inicial", "target-inicial");
        String novoTarget = "https://example.com/novo-target";

        graphLink.setTarget(novoTarget);

        assertEquals(novoTarget, graphLink.getTarget(), "O método setTarget() não atualizou o campo corretamente.");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos no construtor e nos setters")
    void deveLidarComValoresNulos() {
        GraphLink linkComNulos = new GraphLink(null, null);

        assertNull(linkComNulos.getSource(), "O campo 'source' deveria ser nulo.");
        assertNull(linkComNulos.getTarget(), "O campo 'target' deveria ser nulo.");

        linkComNulos.setSource("https://not-null.com");
        linkComNulos.setSource(null);

        assertNull(linkComNulos.getSource(), "O setter deveria permitir a atribuição de nulo para 'source'.");
    }
}