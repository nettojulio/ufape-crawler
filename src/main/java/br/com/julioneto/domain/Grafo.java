package br.com.julioneto.domain;

import br.com.julioneto.config.CrawlerConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Estrutura de dados que armazena todos os links e suas conexões.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Grafo {
    private static int MAX_DEPTH = 0;
    private final String approach = CrawlerConfig.USE_THREADS ? "concurrent" : "sequential";
    private final Map<String, Link> links = new ConcurrentHashMap<>();

    public static int getMaxDepth() {
        return MAX_DEPTH;
    }

    public static void setMaxDepth(int maxDepth) {
        MAX_DEPTH = maxDepth;
    }

    public void adicionarLink(Link link) {
        links.putIfAbsent(link.getUrl(), link);
    }

    public Link getLink(String url) {
        return links.get(url);
    }

    public void adicionarAresta(String urlOrigem, String urlDestino, String tipo) {
        Link origem = getLink(urlOrigem);
        Link destino = getLink(urlDestino);

        if (origem != null && destino != null) {
            Aresta aresta = new Aresta(origem, destino, tipo);
            origem.adicionarAresta(aresta);
        }
    }

    public List<Link> getLinksQuebrados() {
        return this.links.values().stream()
                .filter(link -> link.getStatusCode() >= 400 && link.getStatusCode() < 600)
                .toList();
    }

    public Map<Integer, List<Link>> getLinksQuebradosPorStatusCode() {
        return this.links.values().stream()
                .filter(link -> link.getStatusCode() != 200)
                .collect(Collectors.groupingBy(Link::getStatusCode));
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public String getApproach() {
        return approach;
    }
}
