package br.com.julioneto;

import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;
import br.com.julioneto.service.WebScraperService;

public class UfapeWebCrawler {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        WebScraperService scraper = new WebScraperService(grafo);

        String urlInicial = "https://ufape.edu.br";
        scraper.iniciarCrawl(urlInicial);

        System.out.println("\n--- Relatório Final ---");
        for (Link link : grafo.getLinks().values()) {
            System.out.println("URL: " + link.getUrl() + ", Status: " + link.getStatusCode());
        }
    }
}