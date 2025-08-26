package br.com.julioneto.config;

import java.util.List;

/**
 * Classe para centralizar as configurações da aplicação.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 * */
public final class CrawlerConfig {

    public static final String API_BASE_URL = "http://localhost:8080";
    public static final String URL_INICIAL = "https://ufape.edu.br/";
    public static final String OUTPUT_FILENAME = "grafo_salvo.json";
    public static final int API_TIMEOUT_SECONDS = 90;
    public static final List<String> ALLOWED_DOMAINS = List.of("ufape.edu.br");
    private CrawlerConfig() {
    }
}
