package br.com.julioneto.service;

import br.com.julioneto.models.Grafo;
import br.com.julioneto.models.Link;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class WebScraperService {
    private Grafo grafo;
    private Queue<Link> filaDeLinks;
    private Set<String> linksVisitados;
    private String hostInicial;
    private static final Set<String> EXTENSOES_PADRAO_IGNORADAS = Set.of(
            ".pdf", ".jpg", ".jpeg", ".png", ".gif", ".zip", ".rar",
            ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            ".mp3", ".mp4"
    );
    // para que cada instancia tenha sua propria lista de links ignorados
    private final Set<String> extensoesIgnoradas;

    public WebScraperService(Grafo grafo) {
        this.grafo = grafo;
        this.filaDeLinks = new LinkedList<>();
        this.linksVisitados = new HashSet<>();
        this.hostInicial = "";
        this.extensoesIgnoradas = new HashSet<>(EXTENSOES_PADRAO_IGNORADAS);
    }

    public void iniciarCrawl(String urlInicial) {
        Link linkInicial = new Link(urlInicial);
        linkInicial.setDepth(1); //raiz do grafo

        try {
            this.hostInicial = new URI(urlInicial).getHost();
        } catch (URISyntaxException e) {
            System.err.println("URL inicial inválida: " + urlInicial);
            return;
        }
        filaDeLinks.add(linkInicial);
        linksVisitados.add(urlInicial);
        grafo.adicionarLink(linkInicial);

        while (!filaDeLinks.isEmpty()) {
            Link linkAtual = filaDeLinks.poll();
            //alterei aqui, troquei o processando
            System.out.println("[Processando:] (depth: " + linkAtual.getDepth() + ") " + linkAtual.getUrl());

            try {
                processarLink(linkAtual);
            } catch (IOException e) {
                linkAtual.setStatusCode(500);
                System.err.println("Erro ao conectar ou ler a URL " + linkAtual.getUrl() + ": " + e.getMessage());
            }
        }
    }

    private void processarLink(Link link) throws IOException {
        long startTime = System.currentTimeMillis();

        Connection.Response response = Jsoup.connect(link.getUrl())
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .followRedirects(true)
                .ignoreContentType(true)
                .execute();

        long endTime = System.currentTimeMillis();

        //informações do link
        link.setStatusCode(response.statusCode());
        link.setContentType(response.contentType());
        link.setResponseTime(endTime - startTime);

        //se status for 200 e o content type for HTML, processa o conteúdo
        if (link.getStatusCode() == 200 && response.contentType().contains("text/html")) {
            //analisa documento uma vez e armazene na variável doc
            Document doc = response.parse();

            //variável doc para extrair o título e os links
            link.setTitle(doc.title());

            Elements linksEncontrados = doc.select("a[href]");

            for (Element el : linksEncontrados) {
                String urlEncontrada = el.absUrl("href");
                if (urlEncontrada.isEmpty() || !urlEncontrada.startsWith("http")) {
                    continue;
                }

                //cria o Link de destino e adiciona ao grafo
                Link linkDestino = grafo.getLink(urlEncontrada);
                if (linkDestino == null) {
                    linkDestino = new Link(urlEncontrada);

                    //define profundidade com base no pai
                    linkDestino.setDepth(link.getDepth() + 1);

                    grafo.adicionarLink(linkDestino);
                }

                //adiciona a aresta ao grafo
                String tipoAresta = el.attr("rel").contains("nofollow") ? "nofollow" : "dofollow";
                grafo.adicionarAresta(link.getUrl(), urlEncontrada, el.text(), tipoAresta);

                //mete o link na fila para ser visitado se for do mesmo dominio inicial e ainda não foi visitado
                if (deveVisitar(urlEncontrada)) {
                    filaDeLinks.add(linkDestino);
                    linksVisitados.add(urlEncontrada);
                }
            }
        }
    }

    private boolean deveVisitar(String url) {
        //filtro para não visitar link já visitado
        if (linksVisitados.contains(url)) {
            return false;
        }
        //não entrar em link com extensões de arquivo ignorados
        String urlMinuscula = url.toLowerCase();
        if (extensoesIgnoradas.stream().anyMatch(urlMinuscula::endsWith)) {
            return false;
        }
        //visitar apenas os link do mesmo host inicial, ignorando http/https
        try {
            String hostEncontrado = new URI(url).getHost();
            if (hostEncontrado == null) {
                // TODO ajustar isso que ta feio
                System.err.println("URL " + url + " veio null");
                return false;
            }
            return hostEncontrado.equals(this.hostInicial);
        } catch (URISyntaxException e) {
            return false;
        } catch (Exception ex) {
            System.err.println("Global exception");
            ex.printStackTrace();
            return false;
        }
    }

}