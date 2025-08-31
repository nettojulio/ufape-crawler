package br.com.julioneto.dto;

public class GraphNode {
    private String id;
    private int depth;
    private int statusCode;

    public GraphNode(String id, int depth, int statusCode){
        this.id = id;
        this.depth = depth;
        this.statusCode = statusCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
