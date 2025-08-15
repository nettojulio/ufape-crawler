package br.com.julioneto.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Link {
    private String url;
    private String domain;
    private int statusCode;
    private String contentType;
    private long responseTime;
    private String title;
    private Set<Aresta> arestasDeSaida;

    public Link(String url) {
        this.url = url;
        this.arestasDeSaida = new HashSet<>();
    }

    // Getters e Setters
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

    public Set<Aresta> getArestasDeSaida() {
        return arestasDeSaida;
    }

    public void adicionarAresta(Aresta aresta) {
        this.arestasDeSaida.add(aresta);
    }

    // Métodos para manipulação de objetos
    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", contentType='" + contentType + '\'' +
                '}';
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
}