package br.com.julioneto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO para mapear a resposta JSON da API de crawling.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrawlResponse {
    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("contentType")
    private String contentType;

    @JsonProperty("elapsedTime")
    private String elapsedTime;

    @JsonProperty("title")
    private String title;

    @JsonProperty("links")
    private LinksResponse links;

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public String getTitle() {
        return title;
    }

    public LinksResponse getLinks() {
        return links;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinksResponse {
        @JsonProperty("available")
        private List<String> available;

        public List<String> getAvailable() {
            return available;
        }
    }
}
