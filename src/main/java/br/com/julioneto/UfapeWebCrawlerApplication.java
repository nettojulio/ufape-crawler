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

public class UfapeWebCrawlerApplication {

    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        CrawlerClient crawlerClient = new ExternalApiCrawlerClient(CrawlerConfig.API_BASE_URL);
        WebCrawler webCrawler = new BfsWebCrawler(grafo, crawlerClient);
        ReportGenerator reportGenerator = new ConsoleReportGenerator();
        GraphStorage graphStorage = new JsonGraphStorage();

        System.out.println("Iniciando o Web Crawler para o site: " + CrawlerConfig.URL_INICIAL);

        webCrawler.crawl(CrawlerConfig.URL_INICIAL);

        reportGenerator.generate(grafo);

        try {
            graphStorage.save(grafo, CrawlerConfig.OUTPUT_FILENAME);
        } catch (Exception e) {
            System.err.println("Falha crítica ao salvar o grafo. A aplicação será encerrada.");
            e.printStackTrace();
        }
    }
}