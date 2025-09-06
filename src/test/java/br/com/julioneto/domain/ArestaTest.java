package br.com.julioneto.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArestaTest {

    private Link linkOrigem;
    private Link linkDestino;
    private Aresta aresta;

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

    @Test
    @DisplayName("Deve permitir a alteração da origem e destino através dos setters")
    void deveAlterarCamposComSetters() {
        Link novaOrigem = new Link("https://example.com/nova-origem");
        Link novoDestino = new Link("https://example.com/novo-destino");

        aresta.setOrigem(novaOrigem);
        aresta.setDestino(novoDestino);

        assertEquals(novaOrigem, aresta.getOrigem(), "O setter de origem falhou.");
        assertEquals(novoDestino, aresta.getDestino(), "O setter de destino falhou.");
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
        @DisplayName("Duas arestas com mesma origem e destino devem ser iguais")
        void deveSerIgualAOutraArestaComMesmosLinks() {
            Aresta outraAresta = new Aresta(linkOrigem, linkDestino);

            assertEquals(aresta, outraAresta, "Arestas com mesma origem e destino deveriam ser iguais.");
        }

        @Test
        @DisplayName("Não deve ser igual a uma aresta com origem diferente")
        void naoDeveSerIgualComOrigemDiferente() {
            Link origemDiferente = new Link("https://example.com/origem-diferente");
            Aresta arestaDiferente = new Aresta(origemDiferente, linkDestino);

            assertNotEquals(aresta, arestaDiferente, "Arestas com origens diferentes não deveriam ser iguais.");
        }

        @Test
        @DisplayName("Não deve ser igual a uma aresta com destino diferente")
        void naoDeveSerIgualComDestinoDiferente() {
            Link destinoDiferente = new Link("https://example.com/destino-diferente");
            Aresta arestaDiferente = new Aresta(linkOrigem, destinoDiferente);

            assertNotEquals(aresta, arestaDiferente, "Arestas com destinos diferentes não deveriam ser iguais.");
        }

        @Test
        @DisplayName("Não deve ser igual a um objeto nulo")
        void naoDeveSerIgualANulo() {
            assertNotEquals(null, aresta, "Aresta não deveria ser igual a nulo.");
        }

        @Test
        @DisplayName("Não deve ser igual a um objeto de outra classe")
        void naoDeveSerIgualAOutroTipoDeObjeto() {
            assertNotEquals(aresta, new Object(), "Aresta não deveria ser igual a um objeto de tipo diferente.");
        }

        @Test
        @DisplayName("Deve ter o mesmo hashCode para arestas iguais")
        void deveTerMesmoHashCodeParaArestasIguais() {
            Aresta outraAresta = new Aresta(linkOrigem, linkDestino);

            assertEquals(aresta.hashCode(), outraAresta.hashCode(), "Arestas iguais devem ter o mesmo hashCode.");
        }

        @Test
        @DisplayName("Deve ter (provavelmente) hashCode diferente para arestas diferentes")
        void deveTerHashCodeDiferenteParaArestasDiferentes() {
            Aresta arestaDiferente = new Aresta(linkOrigem, new Link("https://example.com/outro-destino"));

            assertNotEquals(aresta.hashCode(), arestaDiferente.hashCode(), "Arestas diferentes deveriam ter hashCodes diferentes.");
        }
    }
}