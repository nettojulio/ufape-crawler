package br.com.julioneto.dto;

import java.sql.Timestamp;
import java.util.List;

public class VisualGraph {
    private List<GraphNode> nodes;
    private List<GraphLink> links;
    private final Timestamp generatedAt;

    public VisualGraph(List<GraphNode> nodes, List<GraphLink> links) {
        this.nodes = nodes;
        this.links = links;
        this.generatedAt = new Timestamp(System.currentTimeMillis());
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphLink> getLinks() {
        return links;
    }

    public void setLinks(List<GraphLink> links) {
        this.links = links;
    }

    public Timestamp getGeneratedAt() {
        return generatedAt;
    }
}
