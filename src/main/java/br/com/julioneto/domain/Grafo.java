package br.com.julioneto.domain;

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
    /**
     * TODO Implementar threads ou mudar Estrutura de dados
     */
    private final Map<String, Link> links = new ConcurrentHashMap<>();

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
                .collect(Collectors.toList());
    }

    public Map<String, Link> getLinks() {
        return links;
    }
}
