package br.com.julioneto.service;


import br.com.julioneto.models.Aresta;
import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonService {
    private JSONObject converterGrafoParaJson(Grafo grafo) {

        JSONObject jsonGrafo = new JSONObject();
        JSONObject jsonNodes = new JSONObject();

        //itera sobre cada link dos grafos
        for(Map.Entry<String, Link>  entry : grafo.getLinks().entrySet()) {
            Link link = entry.getValue();
            JSONObject jsonLink = new JSONObject();

            //adiciona as propriedades do Link ao JSON
            jsonLink.put("url", link.getUrl());
            jsonLink.put("statusCode", link.getStatusCode());
            jsonLink.put("Content_type", link.getContentType());
            jsonLink.put("Response_time", link.getResponseTime());
            jsonLink.put("title", link.getTitle());

            //cria um array para as arestas link de saida
            JSONArray jsonArestas = new JSONArray();
            if (link.getArestasDeSaida() != null) {
                for (Aresta aresta : link.getArestasDeSaida()) {
                    JSONObject jsonAresta = new JSONObject();
                    jsonAresta.put("destino_url", aresta.getDestino().getUrl()); //adiciona a URTL de destino e o tipo de aresta
                    jsonAresta.put("Tipo", aresta.getTipo());
                    jsonArestas.put(jsonAresta);
                }
            }
            //adiciona o array de aresta ao objeto do link
            jsonLink.put("links", jsonArestas);
            // Adiciona o objeto JSON do link ao objeto principal de nós, usando a URL como chave
            jsonNodes.put(link.getUrl(), jsonLink);
        }
        jsonGrafo.put("nodes", jsonNodes);

        return jsonGrafo;

    }

    public void salvarGrafoComoJson(Grafo grafo, String caminhoArquivo){
        JSONObject jsonGrafo = converterGrafoParaJson(grafo);

        try(FileWriter file = new FileWriter(caminhoArquivo)){
            file.write(jsonGrafo.toString(4));
            System.out.println("Grafo salvo com sucesso em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o grafo em JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
