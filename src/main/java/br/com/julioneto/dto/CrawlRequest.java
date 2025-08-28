package br.com.julioneto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO que representa o corpo (body) da requisição POST para a API de crawling.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
public class CrawlRequest {
    @JsonProperty("url")
    private String url;

    @JsonProperty("timeout")
    private int timeout;

    @JsonProperty("remove_fragment")
    private boolean removeFragment;

    @JsonProperty("allowed_domains")
    private List<String> allowedDomains;

    @JsonProperty("collect_subdomains")
    private boolean collectSubdomains;

    @JsonProperty("lower_case_urls")
    private boolean lowerCaseUrls;

    @JsonProperty("can_retry")
    private boolean canRetry;

    @JsonProperty("max_attempts")
    private int maxAttempts;

    public CrawlRequest(String url, int timeout, boolean removeFragment, List<String> allowedDomains, boolean collectSubdomains, boolean lowerCaseUrls, boolean canRetry, int maxAttempts) {
        this.url = url;
        this.timeout = timeout;
        this.removeFragment = removeFragment;
        this.allowedDomains = allowedDomains;
        this.collectSubdomains = collectSubdomains;
        this.lowerCaseUrls = lowerCaseUrls;
        this.canRetry = canRetry;
        this.maxAttempts = maxAttempts;
    }
}
