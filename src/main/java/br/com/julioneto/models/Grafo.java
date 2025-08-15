package br.com.julioneto.models;

import java.util.HashMap;
import java.util.Map;

public class Grafo {
    private Map<String, Link> links;

    public Grafo() {
        this.links = new HashMap<>();
    }

    public void adicionarLink(Link link) {
        //putIfAbsent() vai fazer com que nao entrem links duplicados no grafo
        links.putIfAbsent(link.getUrl(), link);
    }

    public Link getLink(String url) {
        return links.get(url);
    }

    public void adicionarAresta(String urlOrigem, String urlDestino, String textoDoLink, String tipo) {
        Link origem = getLink(urlOrigem);
        Link destino = getLink(urlDestino);

        if (origem != null && destino != null) {
            Aresta aresta = new Aresta(origem, destino, textoDoLink, tipo);
            origem.adicionarAresta(aresta);
        }
    }

    // Getters e Setters
    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }
}