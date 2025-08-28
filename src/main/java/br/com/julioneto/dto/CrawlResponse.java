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
    private long elapsedTime;

    @JsonProperty("title")
    private String title;

    @JsonProperty("links")
    private LinksResponse links;

    @JsonProperty("details")
    private DetailsResponse details;

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getTitle() {
        return title;
    }

    public LinksResponse getLinks() {
        return links;
    }

    public DetailsResponse getDetails() {
        return details;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinksResponse {
        @JsonProperty("available")
        private List<String> available;

        @JsonProperty("unavailable")
        private List<String> unavailable;

        public List<String> getAvailable() {
            return available;
        }

        public List<String> getUnavailable() {
            return unavailable;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailsResponse {
        @JsonProperty("correctUrl")
        private String correctUrl;

        @JsonProperty("original")
        private DetailInfo original;

        @JsonProperty("modified")
        private DetailInfo modified;

        public String getCorrectUrl() {
            return correctUrl;
        }

        public DetailInfo getOriginal() {
            return original;
        }

        public DetailInfo getModified() {
            return modified;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailInfo {
        @JsonProperty("Scheme")
        private String scheme;
        @JsonProperty("Opaque")
        private String opaque;
        @JsonProperty("User")
        private String user;
        @JsonProperty("Host")
        private String host;
        @JsonProperty("Path")
        private String path;
        @JsonProperty("RawPath")
        private String rawPath;
        @JsonProperty("OmitHost")
        private Boolean omitHost;
        @JsonProperty("ForceQuery")
        private Boolean forceQuery;
        @JsonProperty("RawQuery")
        private String rawQuery;
        @JsonProperty("Fragment")
        private String fragment;
        @JsonProperty("RawFragment")
        private String rawFragment;

        public String getScheme() {
            return this.scheme;
        }

        public String getOpaque() {
            return this.opaque;
        }

        public String getUser() {
            return this.user;
        }

        public String getHost() {
            return this.host;
        }

        public String getPath() {
            return this.path;
        }

        public String getRawPath() {
            return this.rawPath;
        }

        public Boolean getOmitHost() {
            return this.omitHost;
        }

        public Boolean getForceQuery() {
            return this.forceQuery;
        }

        public String getRawQuery() {
            return this.rawQuery;
        }

        public String getFragment() {
            return this.fragment;
        }

        public String getRawFragment() {
            return this.rawFragment;
        }
    }
}
