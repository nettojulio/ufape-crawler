package br.com.julioneto.dto;

import br.com.julioneto.domain.Link;

public class GraphNode {
    private final String id;
    private final int depth;
    private final int statusCode;
    private final String contentType;
    private final long elapsedTime;
    private final String title;
    private final String domain;

    public GraphNode(Link link) {
        this.id = link.getUrl();
        this.depth = link.getDepth();
        this.statusCode = link.getStatusCode();
        this.elapsedTime = link.getResponseTime();
        this.contentType = link.getContentType();
        this.title = link.getTitle();
        this.domain = link.getDomain();
    }

    public String getId() {
        return id;
    }

    public int getDepth() {
        return depth;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getContentType() {
        return contentType;
    }

    public String getTitle() {
        return title;
    }

    public String getDomain() {
        return domain;
    }
}
