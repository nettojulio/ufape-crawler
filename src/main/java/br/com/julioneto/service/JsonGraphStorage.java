package br.com.julioneto.service;

import br.com.julioneto.contract.GraphStorage;
import br.com.julioneto.domain.Grafo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Implementação do GraphStorage que salva o grafo em um arquivo JSON.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 *
 */
public class JsonGraphStorage implements GraphStorage {
    private final ObjectMapper objectMapper;

    public JsonGraphStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void save(Grafo grafo, String filePath) throws IOException {
        try {
            objectMapper.writeValue(new File(filePath), grafo);
            System.out.println("Grafo salvo com sucesso em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o grafo em JSON: " + e.getMessage());
            throw e;
        }
    }
}
