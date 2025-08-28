package br.com.julioneto.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa um nó (uma página da web) no grafo.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "url")
public class Link {
    private String url;
    private String domain;
    private int statusCode;
    private String contentType;
    private long responseTime; // nanoseconds value
    private String title;
    private int depth;

    @JsonManagedReference
    private Map<String, Aresta> arestasDeSaida = new ConcurrentHashMap<>();//alteração para trhead-safe

    public Link() {
    }

    public Link(String url) {
        this.url = url;
        this.domain = extrairDominio(url);
    }

    private String extrairDominio(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            return host != null ? host : "";
        } catch (URISyntaxException _) {
            System.err.println("URL inválida ao extrair domínio: " + url);
            return "";
        }
    }

    public void adicionarAresta(Aresta aresta) {
        if (aresta != null && aresta.getDestino() != null) {
            this.arestasDeSaida.put(aresta.getDestino().getUrl(), aresta);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Map<String,Aresta> getArestasDeSaida() {
        return arestasDeSaida;
    }

    public void setArestasDeSaida(Map<String, Aresta> arestasDeSaida) {
        this.arestasDeSaida = arestasDeSaida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "Link{url='" + url + "', depth=" + depth + ", statusCode=" + statusCode + "}";
    }
}
