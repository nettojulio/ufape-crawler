package br.com.julioneto.service;

import br.com.julioneto.contract.GraphStorage;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;
import br.com.julioneto.dto.GraphLink;
import br.com.julioneto.dto.GraphNode;
import br.com.julioneto.dto.VisualGraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do GraphStorage que salva o grafo em um arquivo JSON.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 *
 */
public class JsonGraphStorage implements GraphStorage {
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void save(Grafo grafo, String filePath) throws IOException {
        try {
            //converte os links do grafo para a lista de nodes
            List<GraphNode> nodes = grafo.getLinks().values().stream().map(GraphNode::new).toList();

            //converte as arestas do grafo para a lista de links
            List<GraphLink> links = new ArrayList<>();
            for(Link linkOrigem : grafo.getLinks().values()){
                if (linkOrigem.getArestasDeSaida() != null) {
                    for (String urlDestino : linkOrigem.getArestasDeSaida().keySet()) {
                        links.add(new GraphLink(linkOrigem.getUrl(), urlDestino));
                    }
                }
            }

            //cria o objeto VisualGraph pra o Json final
            VisualGraph visualGraph = new VisualGraph(nodes, links);

            //salva o grafo em JSON
            objectMapper.writeValue(Paths.get(filePath).toFile(), visualGraph); // <-- Usando filePath
            System.out.println("Grafo salvo em formato de visualização em " + filePath);

        } catch (IOException e) {
            System.err.println("Erro ao salvar o grafo em JSON: " + e.getMessage());
            throw e;
        }
    }
}
