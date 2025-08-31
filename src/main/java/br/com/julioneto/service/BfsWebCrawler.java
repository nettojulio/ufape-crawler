package br.com.julioneto.service;

import br.com.julioneto.config.CrawlerConfig;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implementação do WebCrawler usando o algoritmo de Busca em Largura (BFS).
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 *
 */
public class BfsWebCrawler implements WebCrawler {
    private final Grafo grafo;
    private final CrawlerClient crawlerClient;
    private final Queue<Link> sequentialQueue = new LinkedList<>();
    private final Set<String> sequentialVisitedUrls = new HashSet<>();
    private String initialHost;

    public BfsWebCrawler(Grafo grafo, CrawlerClient crawlerClient) {
        this.grafo = grafo;
        this.crawlerClient = crawlerClient;
    }

    /**
     * Implementação original, que executa a varredura de forma sequencial,
     * processando um link de cada vez.
     */
    @Override
    public void crawl(String startUrl) {
        if (!initialize(startUrl)) return;

        Link startLink = new Link(startUrl);
        startLink.setDepth(1);

        sequentialQueue.add(startLink);
        sequentialVisitedUrls.add(startUrl);
        grafo.adicionarLink(startLink);

        while (!sequentialQueue.isEmpty()) {
            Link currentLink = sequentialQueue.poll();
            if (currentLink.getDepth() > CrawlerConfig.MAX_DEPTH) {
                break;
            }
            processLinkSequentially(currentLink);
        }
    }

    /**
     * Cada link a ser processado é submetido como uma nova tarefa para um Executor,
     * que por sua vez cria uma nova virtual thread para executá-la.
     */
    @Override
    public void crawlConcurrently(String startUrl) {
        if (!initialize(startUrl)) return;

        final Set<String> concurrentVisitedUrls = ConcurrentHashMap.newKeySet();
        final AtomicInteger activeTasks = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Link startLink = new Link(startUrl);
            startLink.setDepth(1);

            grafo.adicionarLink(startLink);
            concurrentVisitedUrls.add(startUrl);

            activeTasks.incrementAndGet();
            executor.submit(() -> processLinkConcurrently(startLink, executor, concurrentVisitedUrls, activeTasks));

            while (activeTasks.get() > 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            System.err.println("O processo de crawling concorrente foi interrompido.");
        }
    }

    private boolean initialize(String startUrl) {
        try {
            this.initialHost = new URI(startUrl).getHost();
            if (this.initialHost == null) {
                System.err.println("URL inicial inválida: não foi possível extrair o host.");
                return false;
            }
        } catch (URISyntaxException e) {
            System.err.println("Erro de sintaxe na URL inicial: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void processLinkSequentially(Link currentLink) {
        System.out.println("[SEQ] Processando (depth: " + currentLink.getDepth() + ") " + currentLink.getUrl());
        if (currentLink.getDepth() > Grafo.getMaxDepth()) {
            Grafo.setMaxDepth(currentLink.getDepth());
        }
        try {
            CrawlResponse response = crawlerClient.fetchPageInfo(currentLink.getUrl());
            updateLinkData(currentLink, response);

            if (currentLink.getDepth() != CrawlerConfig.MAX_DEPTH && response.getStatusCode() == 200 && response.getLinks() != null && response.getLinks().getAvailable() != null) {
                for (String foundUrl : response.getLinks().getAvailable()) {
                    String normalizedUrl = normalizeUrl(foundUrl); // NORMALIZA A URL AQUI
                    if (shouldVisit(normalizedUrl, sequentialVisitedUrls)) {

                        // TODO review approach
                        sequentialVisitedUrls.add(normalizedUrl);

                        //Link destinationLink = new Link(foundUrl);
                        Link destinationLink = new Link(normalizedUrl);//usa a url normalizada para criar um novo link
                        destinationLink.setDepth(currentLink.getDepth() + 1);

                        grafo.adicionarLink(destinationLink);
                        grafo.adicionarAresta(currentLink.getUrl(), destinationLink.getUrl(), "dofollow");

                        sequentialQueue.add(destinationLink);
                    }
                }
            }
        } catch (Exception error) {
            handleProcessingError(currentLink, error);
        }
    }

    private void processLinkConcurrently(Link currentLink, ExecutorService executor, Set<String> visitedUrls, AtomicInteger activeTasks) {
        System.out.println("[CON] Processando (depth: " + currentLink.getDepth() + ") " + currentLink.getUrl());
        if (currentLink.getDepth() > Grafo.getMaxDepth()) {
            Grafo.setMaxDepth(currentLink.getDepth());
        }
        try {
            CrawlResponse response = crawlerClient.fetchPageInfo(currentLink.getUrl());
            updateLinkData(currentLink, response);

            if (currentLink.getDepth() != CrawlerConfig.MAX_DEPTH && response.getStatusCode() == 200 && response.getLinks() != null && response.getLinks().getAvailable() != null) {
                for (String foundUrl : response.getLinks().getAvailable()) {
                    String normalizedUrl = normalizeUrl(foundUrl); // NORMALIZA A URL AQUI
                    if (shouldVisit(foundUrl, visitedUrls) && visitedUrls.add(normalizedUrl)) {
                        Link destinationLink = new Link(foundUrl);//usa a url normalizada
                        destinationLink.setDepth(currentLink.getDepth() + 1);

                        grafo.adicionarLink(destinationLink);
                        grafo.adicionarAresta(currentLink.getUrl(), destinationLink.getUrl(), "dofollow");

                        activeTasks.incrementAndGet();
                        executor.submit(() -> processLinkConcurrently(destinationLink, executor, visitedUrls, activeTasks));
                    }
                }
            }
        } catch (Exception e) {
            handleProcessingError(currentLink, e);
        } finally {
            activeTasks.decrementAndGet();
        }
    }

    private boolean shouldVisit(String url, Set<String> visited) {
        if (url == null || url.isBlank() || !url.startsWith("http") || visited.contains(url)) {
            return false;
        }

        try {
            String foundHost = new URI(url).getHost();
            return foundHost.contains(initialHost);
        } catch (URISyntaxException _) {
            System.err.println("URL inválida encontrada durante verificação: " + url);
            return false;
        }
    }

    private String normalizeUrl(String url) {
        try{
            URI uri = new  URI(url);
            // cria url sem fragmento
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null).toString();
        }catch (URISyntaxException e) {
            System.err.println("Erro ao normalizar a URL: " + url);
            return url;
        }
    }

    private void updateLinkData(Link link, CrawlResponse response) {
        link.setStatusCode(response.getStatusCode());
        link.setTitle(response.getTitle());
        link.setContentType(response.getContentType());
        link.setResponseTime(response.getElapsedTime());
    }

    private void handleProcessingError(Link link, Exception e) {
        System.err.println("Erro ao processar a URL " + link.getUrl() + ": " + e.getMessage());
        if (link.getStatusCode() == 0) {
            link.setStatusCode(500);
        }
    }
}
