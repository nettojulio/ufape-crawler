package br.com.julioneto.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Representa a conexão (aresta) entre dois links (nós) no grafo.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Aresta {
    @JsonBackReference
    private Link origem;
    @JsonIgnore
    private Link destino;
    private String tipo;

    public Aresta() {
    }

    public Aresta(Link origem, Link destino, String tipo) {
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aresta aresta = (Aresta) o;
        return Objects.equals(origem, aresta.origem) && Objects.equals(destino, aresta.destino) && Objects.equals(tipo, aresta.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, tipo);
    }
}
