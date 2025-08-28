package br.com.julioneto;

import br.com.julioneto.config.CrawlerConfig;
import br.com.julioneto.contract.CrawlerClient;
import br.com.julioneto.contract.GraphStorage;
import br.com.julioneto.contract.ReportGenerator;
import br.com.julioneto.contract.WebCrawler;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.reporting.ConsoleReportGenerator;
import br.com.julioneto.service.BfsWebCrawler;
import br.com.julioneto.service.ExternalApiCrawlerClient;
import br.com.julioneto.service.JsonGraphStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UfapeWebCrawlerApplication {

    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        CrawlerClient crawlerClient = new ExternalApiCrawlerClient(CrawlerConfig.API_BASE_URL);
        WebCrawler webCrawler = new BfsWebCrawler(grafo, crawlerClient);
        ReportGenerator reportGenerator = new ConsoleReportGenerator();
        GraphStorage graphStorage = new JsonGraphStorage();

        System.out.println("Iniciando o Web Crawler para o site: " + CrawlerConfig.URL_INICIAL);

        long startTime = System.currentTimeMillis();

        if (CrawlerConfig.USE_THREADS) {
            System.out.println("Executando em modo CONCORRENTE...");
            webCrawler.crawlConcurrently(CrawlerConfig.URL_INICIAL);
        } else {
            System.out.println("Executando em modo SEQUENCIAL...");
            webCrawler.crawl(CrawlerConfig.URL_INICIAL);
        }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        System.out.println("\nTempo total de execução: " + duration + " segundos.");

        reportGenerator.generate(grafo);


        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            String filePath = String.format("grafo_salvo_%s.json", date);

            graphStorage.save(grafo, filePath);
        } catch (Exception e) {
            System.err.println("Falha crítica ao salvar o grafo. A aplicação será encerrada.");
            e.printStackTrace();
        }
    }
}