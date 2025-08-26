package br.com.julioneto.contract;

import br.com.julioneto.domain.Grafo;

import java.io.IOException;

public interface GraphStorage {
    void save(Grafo grafo, String filePath) throws IOException;
}
