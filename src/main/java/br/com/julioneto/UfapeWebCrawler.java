package br.com.julioneto;

import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;
import br.com.julioneto.service.JsonService;
import br.com.julioneto.service.BuscaEmLarguraService;

import java.util.List;

public class UfapeWebCrawler {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        BuscaEmLarguraService crawler = new BuscaEmLarguraService(grafo);

        String urlInicial = "https://ufape.edu.br/";
        crawler.iniciarCrawl(urlInicial);

        System.out.println("\n--- Relatório Final ---");
        System.out.println("Total de páginas processadas: " + grafo.getLinks().size());

        List<Link> linksQuebrados = grafo.getLinksQuebrados();
        if (!linksQuebrados.isEmpty()) {
            System.out.println("\n--- Links Quebrados (" + linksQuebrados.size() + ") ---");
            for (Link link : linksQuebrados) {
                System.out.println("URL: " + link.getUrl() + ", Status: " + link.getStatusCode());
            }
        } else {
            System.out.println("\nNenhum link quebrado encontrado.");
        }

        JsonService jsonService = new JsonService();
        jsonService.salvarGrafoComoJson(grafo, "grafo_salvo.json");
    }
}