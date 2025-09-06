package br.com.julioneto.service;

import br.com.julioneto.domain.Aresta;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonGraphStorageTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @TempDir
    Path tempDir;
    private JsonGraphStorage graphStorage;

    @BeforeEach
    void setUp() {
        graphStorage = new JsonGraphStorage();
    }

    @Test
    @DisplayName("Deve salvar um grafo com nós e arestas corretamente")
    void deveSalvarGrafoCompleto() throws IOException {
        Grafo grafo = new Grafo();
        Link linkA = new Link("https://a.com");
        Link linkB = new Link("https://b.com");
        Link linkC = new Link("https://c.com");

        Aresta arestaAB = new Aresta(linkA, linkB);
        Aresta arestaBC = new Aresta(linkB, linkC);


        linkA.setArestasDeSaida(Map.of("https://b.com", arestaAB));
        linkB.setArestasDeSaida(Map.of("https://c.com", arestaBC));

        grafo.adicionarLink(linkA);
        grafo.adicionarLink(linkB);
        grafo.adicionarLink(linkC);

        String nomeArquivo = "grafo_completo.json";
        Path arquivoDeSaida = tempDir.resolve(nomeArquivo);

        graphStorage.save(grafo, arquivoDeSaida.toString());

        assertTrue(Files.exists(arquivoDeSaida), "O arquivo JSON deveria ter sido criado.");

        String jsonContent = Files.readString(arquivoDeSaida);
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        assertEquals(3, rootNode.path("nodes").size(), "Deveria haver 3 nós no JSON.");
        assertEquals(2, rootNode.path("links").size(), "Deveria haver 2 links (arestas) no JSON.");

        JsonNode primeiroLink = rootNode.path("links").get(0);
        assertEquals("https://a.com", primeiroLink.path("source").asText());
        assertEquals("https://b.com", primeiroLink.path("target").asText());
    }

    @Test
    @DisplayName("Deve salvar um grafo vazio gerando um JSON com arrays vazios")
    void deveSalvarGrafoVazio() throws IOException {
        Grafo grafo = new Grafo();
        String nomeArquivo = "grafo_vazio.json";
        Path arquivoDeSaida = tempDir.resolve(nomeArquivo);

        graphStorage.save(grafo, arquivoDeSaida.toString());

        assertTrue(Files.exists(arquivoDeSaida));

        String jsonContent = Files.readString(arquivoDeSaida);
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        assertTrue(rootNode.path("nodes").isEmpty(), "O array de nós deveria estar vazio.");
        assertTrue(rootNode.path("links").isEmpty(), "O array de links deveria estar vazio.");
    }

    @Test
    @DisplayName("Deve salvar um grafo com nós mas sem arestas")
    void deveSalvarGrafoSemArestas() throws IOException {
        Grafo grafo = new Grafo();
        grafo.adicionarLink(new Link("https://a.com"));
        grafo.adicionarLink(new Link("https://b.com"));
        String nomeArquivo = "grafo_sem_arestas.json";
        Path arquivoDeSaida = tempDir.resolve(nomeArquivo);

        graphStorage.save(grafo, arquivoDeSaida.toString());

        assertTrue(Files.exists(arquivoDeSaida));

        String jsonContent = Files.readString(arquivoDeSaida);
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        assertEquals(2, rootNode.path("nodes").size(), "Deveria haver 2 nós.");
        assertTrue(rootNode.path("links").isEmpty(), "O array de links deveria estar vazio.");
    }

    @Test
    @DisplayName("Deve lançar NullPointerException se o grafo for nulo")
    void deveLancarExcecaoParaGrafoNulo() {
        String nomeArquivo = "qualquer.json";
        Path arquivoDeSaida = tempDir.resolve(nomeArquivo);

        assertThrows(NullPointerException.class, () -> {
            graphStorage.save(null, arquivoDeSaida.toString());
        }, "Deveria lançar NullPointerException para um grafo nulo.");
    }

    @Test
    @DisplayName("Deve lançar IOException para um caminho de arquivo inválido")
    void deveLancarExcecaoParaCaminhoInvalido() {
        Grafo grafo = new Grafo();
        String caminhoInvalido = "/diretorio-nao-existente-e-restrito/grafo.json";

        assertThrows(IOException.class, () -> {
            graphStorage.save(grafo, caminhoInvalido);
        }, "Deveria lançar IOException para um caminho de arquivo inválido.");
    }
}