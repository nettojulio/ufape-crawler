package br.com.julioneto.config;

import java.util.List;

/**
 * Classe para centralizar as configurações da aplicação.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 *
 */
public final class CrawlerConfig {

    public static final String URL_INICIAL = "https://ufape.edu.br/";
    public static final int MAX_DEPTH = Integer.MAX_VALUE;

    public static final String API_BASE_URL = "http://localhost:8080";
    public static final int API_TIMEOUT_SECONDS = 300;
    public static final boolean API_REMOVE_FRAGMENTS = false;
    public static final List<String> API_ALLOWED_DOMAINS = List.of("ufape.edu.br");
    public static final boolean API_COLLECT_SUBDOMAINS = true;
    public static final boolean API_LOWER_CASE_URL = false;
    public static final boolean API_CAN_RETRY = true;
    public static final int API_MAX_ATTEMPTS = 3;

    public static final boolean USE_THREADS = false;

    private CrawlerConfig() {
    }
}
