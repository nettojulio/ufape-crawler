package br.com.julioneto;

import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;
import br.com.julioneto.service.JsonService;
import br.com.julioneto.service.WebScraperService;

import java.util.List;

public class UfapeWebCrawler {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        WebScraperService scraper = new WebScraperService(grafo);

        String urlInicial = "https://ufape.edu.br/";
        scraper.iniciarCrawl(urlInicial);

        System.out.println("\n--- Relatório Final ---");
        for (Link link : grafo.getLinks().values()) {
            System.out.println("URL: " + link.getUrl() + ", Status: " + link.getStatusCode());
        }
        System.out.println("Total de paginas visitadas: " + grafo.getLinks().size());

        List<Link> linksQuebrados = grafo.getLinksQuebrados();
        if (!linksQuebrados.isEmpty()) {
            System.out.println("\n--- Links Quebrados (" + linksQuebrados.size() + ") ---");
            for (Link links : linksQuebrados) {
                System.out.println("URL: " + links.getUrl() + ", Status: " + links.getStatusCode());
            }
        } else {
            System.out.println("\nNenhum link quebrado encontrado.");
        }


        JsonService jsonService = new JsonService();
        jsonService.salvarGrafoComoJson(grafo, "grafo_salvo.json");
    }
}