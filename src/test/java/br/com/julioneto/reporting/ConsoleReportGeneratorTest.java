package br.com.julioneto.reporting;

import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleReportGeneratorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ConsoleReportGenerator reportGenerator;
    private Grafo grafo;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));

        reportGenerator = new ConsoleReportGenerator();
        grafo = new Grafo();

        Grafo.setMaxDepth(0);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);

        Grafo.setMaxDepth(0);
    }

    @Test
    @DisplayName("Deve gerar relatório correto para um grafo com links quebrados")
    void deveGerarRelatorioParaGrafoComLinksQuebrados() {
        Link linkOk = new Link("https://site.com/ok");
        linkOk.setStatusCode(200);

        Link link404 = new Link("https://site.com/not-found");
        link404.setStatusCode(404);

        Link link500 = new Link("https://site.com/server-error");
        link500.setStatusCode(500);

        grafo.adicionarLink(linkOk);
        grafo.adicionarLink(link404);
        grafo.adicionarLink(link500);
        Grafo.setMaxDepth(5);

        String expectedOutput = """
                --- Relatório Final ---
                Total de páginas processadas: 3
                
                --- Links Quebrados por Status ---
                
                Status: 404 | Total: 1
                
                URL: https://site.com/not-found
                
                Status: 500 | Total: 1
                
                URL: https://site.com/server-error
                
                Total de links quebrados: 2
                Depth máximo: 5
                """;

        reportGenerator.generate(grafo);

        assertEquals(normalizeLineEndings(expectedOutput), normalizeLineEndings(outContent.toString()));
    }

    @Test
    @DisplayName("Deve gerar relatório correto para um grafo sem links quebrados")
    void deveGerarRelatorioParaGrafoSemLinksQuebrados() {
        Link linkOk1 = new Link("https://site.com/ok");
        linkOk1.setStatusCode(200);
        Link linkOk2 = new Link("https://site.com/another-ok");
        linkOk2.setStatusCode(200);

        grafo.adicionarLink(linkOk1);
        grafo.adicionarLink(linkOk2);
        Grafo.setMaxDepth(3);

        String expectedOutput = """
                --- Relatório Final ---
                Total de páginas processadas: 2
                
                Nenhum link quebrado encontrado.
                Depth máximo: 3
                """;

        reportGenerator.generate(grafo);

        assertEquals(normalizeLineEndings(expectedOutput), normalizeLineEndings(outContent.toString()));
    }

    @Test
    @DisplayName("Deve gerar relatório correto para um grafo vazio")
    void deveGerarRelatorioParaGrafoVazio() {
        Grafo.setMaxDepth(0);

        String expectedOutput = """
                --- Relatório Final ---
                Total de páginas processadas: 0
                
                Nenhum link quebrado encontrado.
                Depth máximo: 0
                """;

        reportGenerator.generate(grafo);

        assertEquals(normalizeLineEndings(expectedOutput), normalizeLineEndings(outContent.toString()));
    }

    private String normalizeLineEndings(String s) {
        return s.trim().replace("\r\n", "\n");
    }
}