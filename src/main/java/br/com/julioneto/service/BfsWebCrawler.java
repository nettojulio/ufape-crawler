package br.com.julioneto.service;

import br.com.julioneto.contract.CrawlerClient;
import br.com.julioneto.contract.WebCrawler;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;
import br.com.julioneto.dto.CrawlResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Implementação do WebCrawler usando o algoritmo de Busca em Largura (BFS).
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 * */
public class BfsWebCrawler implements WebCrawler {
    private final Grafo grafo;
    private final CrawlerClient crawlerClient;
    private final Queue<Link> queue = new LinkedList<>();
    private final Set<String> visitedUrls = new HashSet<>();
    private String initialHost;

    public BfsWebCrawler(Grafo grafo, CrawlerClient crawlerClient) {
        this.grafo = grafo;
        this.crawlerClient = crawlerClient;
    }

    @Override
    public void crawl(String startUrl) {
        try {
            this.initialHost = new URI(startUrl).getHost();
            if (this.initialHost == null) {
                System.err.println("URL inicial inválida: não foi possível extrair o host.");
                return;
            }
        } catch (URISyntaxException e) {
            System.err.println("Erro de sintaxe na URL inicial: " + e.getMessage());
            return;
        }

        Link startLink = new Link(startUrl);
        startLink.setDepth(1);

        queue.add(startLink);
        visitedUrls.add(startUrl);
        grafo.adicionarLink(startLink);

        boolean stopIt = false;
        while (!queue.isEmpty() && !stopIt) {
            Link currentLink = queue.poll();
            processLink(currentLink);
        }
    }

    private void processLink(Link currentLink) {
        try {
            System.out.println(
                    "[Processando] (depth: " + currentLink.getDepth() + ") " + currentLink.getUrl()
            );

            CrawlResponse response = crawlerClient.fetchPageInfo(currentLink.getUrl());
            updateLinkData(currentLink, response);

            if (response.getStatusCode() == 200 && response.getLinks() != null && response.getLinks().getAvailable() != null) {
                for (String foundUrl : response.getLinks().getAvailable()) {
                    if (shouldVisit(foundUrl)) {
                        visitedUrls.add(foundUrl);

                        Link destinationLink = new Link(foundUrl);
                        destinationLink.setDepth(currentLink.getDepth() + 1);

                        grafo.adicionarLink(destinationLink);
                        grafo.adicionarAresta(currentLink.getUrl(), destinationLink.getUrl(), "dofollow");

                        queue.add(destinationLink);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar a URL " + currentLink.getUrl() + ": " + e.getMessage());
            if (currentLink.getStatusCode() == 0) {
                currentLink.setStatusCode(500);
            }
        }
    }

    private void updateLinkData(Link link, CrawlResponse response) {
        link.setStatusCode(response.getStatusCode());
        link.setTitle(response.getTitle());
        link.setContentType(response.getContentType());

        try {
            String timeStr = response.getElapsedTime().replaceAll("[^\\d.]", "");
            double timeInSeconds = Double.parseDouble(timeStr);
            link.setResponseTime(TimeUnit.MILLISECONDS.convert((long) (timeInSeconds * 1000), TimeUnit.MILLISECONDS));
        } catch (NumberFormatException | NullPointerException e) {
            link.setResponseTime(0L);
        }
    }

    private boolean shouldVisit(String url) {
        if (url == null || url.isBlank() || !url.startsWith("http") || visitedUrls.contains(url)) {
            return false;
        }

        try {
            String foundHost = new URI(url).getHost();
            return initialHost.equals(foundHost);
        } catch (URISyntaxException e) {
            System.err.println("URL inválida encontrada durante verificação: " + url);
            return false;
        }
    }
}
