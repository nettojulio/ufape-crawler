package br.com.julioneto.service;

import br.com.julioneto.models.ExternalCrawlerResponse;
import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BuscaEmLarguraService {
    private Grafo grafo;
    private Queue<Link> filaDeLinks;
    private Set<String> linksVisitados;
    private String hostInicial;

    public BuscaEmLarguraService(Grafo grafo) {
        this.grafo = grafo;
        this.filaDeLinks = new LinkedList<>();
        this.linksVisitados = new HashSet<>();
        this.hostInicial = "";
    }

    public void iniciarCrawl(String urlInicial) {
        try {
            //aqui a gente define o host inicial
            URI uri = new URI(urlInicial);
            this.hostInicial = uri.getHost();
            if (this.hostInicial == null) {
                System.err.println("URL inicial invalida: não foi possivel extrair o host.");
                return;
            }
        } catch (URISyntaxException e) {
            System.err.println(URISyntaxException.class.getName() + ": " + e.getMessage());
            return;
        }

        //aqui eh a raiz do grafo
        Link linkInicial = new Link(urlInicial);
        linkInicial.setDepth(1);

        filaDeLinks.add(linkInicial);
        linksVisitados.add(urlInicial);
        grafo.adicionarLink(linkInicial);

        //aqui eh o loop principal do algoritmo
        while (!filaDeLinks.isEmpty()) {
            Link linkAtual = filaDeLinks.poll();
            System.out.println("[Processando:] (depth: " + linkAtual.getDepth() + ") " + linkAtual.getUrl() + linkAtual.getStatusCode());

            try {
                processarLink(linkAtual);
            } catch (Exception e) {
                System.err.println("Erro ao processar a URL " + linkAtual.getUrl() + ": " + e.getMessage());
                if (linkAtual.getStatusCode() == 0) {
                    linkAtual.setStatusCode(500);
                }
            }
        }
    }

    private void processarLink(Link linkAtual) throws Exception {

        //primeiro a gente chama comunicacao com a API que vai ser esse objeto
        ExternalCrawlerResponse response = PostService.sendCrawlRequest(linkAtual.getUrl());

        //informacoes do link e atribuindo elas
        linkAtual.setStatusCode(response.getStatusCode());
        linkAtual.setTitle(response.getTitle());
        linkAtual.setContentType(response.getContentType());

        try {
            //removemos o "ms" e outros caracteres do responseTime e faz a correcao para long
            String tempStr = response.getElapsedTime().replaceAll("[^\\d.]", "");
            linkAtual.setResponseTime(Long.parseLong(tempStr));
        } catch (NumberFormatException e) {
            linkAtual.setResponseTime(0L); //se der bronca na correcao retorna o valor padrão
        }

        if (response.getStatusCode() == 200 && response.getLinks() != null && response.getLinks().getAvailable() != null) {
            //so nos links avaibles que voltarem
            for (String urlEncontrada : response.getLinks().getAvailable()) {
                if (deveVisitar(urlEncontrada)) {
                    //cria o link destino e adiciona ao grafo e a fila
                    Link linkDestino = new Link(urlEncontrada);
                    linkDestino.setDepth(linkAtual.getDepth() + 1);

                    grafo.adicionarLink(linkDestino);
                    grafo.adicionarAresta(linkAtual.getUrl(), linkDestino.getUrl(), "", "dofollow"); // Tipo padrão

                    filaDeLinks.add(linkDestino);
                    linksVisitados.add(urlEncontrada);
                }
            }
        }
    }

    //metodo p verificar se o link deve ser visitado
    private boolean deveVisitar(String url) {
        if (url == null || url.isEmpty() || !url.startsWith("http")) {
            return false;
        }
        if (linksVisitados.contains(url)) {
            return false;
        }

        try {
            String hostEncontrado = new URI(url).getHost();
            return hostInicial != null && hostInicial.equals(hostEncontrado);
        } catch (Exception e) {
            System.err.println("URL inválida encontrada durante verificação: " + url);
            return false;
        }
    }
}