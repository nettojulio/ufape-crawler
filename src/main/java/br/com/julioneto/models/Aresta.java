package br.com.julioneto.models;

import java.util.Objects;

public class Aresta {
    private Link origem;
    private Link destino;
    private String tipo;

    public Aresta(Link origem, Link destino, String textoDoLink, String tipo) {
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Link getOrigem() {
        return origem;
    }

    public void setOrigem(Link origem) {
        this.origem = origem;
    }

    public Link getDestino() {
        return destino;
    }

    public void setDestino(Link destino) {
        this.destino = destino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Métodos para manipulação de objetos
    @Override
    public String toString() {
        return "Aresta{" +
                "origem=" + origem.getUrl() +
                ", destino=" + destino.getUrl() +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aresta aresta = (Aresta) o;
        return Objects.equals(origem, aresta.origem) &&
                Objects.equals(destino, aresta.destino) &&
                Objects.equals(tipo, aresta.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, tipo);
    }
}