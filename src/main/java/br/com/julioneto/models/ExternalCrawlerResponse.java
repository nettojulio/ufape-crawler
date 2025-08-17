package br.com.julioneto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExternalCrawlerResponse {
    private int statusCode;
    private String contentType;
    private String elapsedTime;
    private String title;
    private LinksResponse links;
    private Details details;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinksResponse getLinks() {
        return links;
    }

    public void setLinks(LinksResponse links) {
        this.links = links;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "CrawlResponse{" +
                "statusCode=" + statusCode +
                ", contentType='" + contentType + '\'' +
                ", elapsedTime='" + elapsedTime + '\'' +
                ", title='" + title + '\'' +
                ", links=" + links +
                ", details=" + details +
                '}';
    }

    public static class LinksResponse {
        private List<String> available;
        private List<String> unavailable;

        public List<String> getAvailable() {
            return available;
        }

        public void setAvailable(List<String> available) {
            this.available = available;
        }

        public List<String> getUnavailable() {
            return unavailable;
        }

        public void setUnavailable(List<String> unavailable) {
            this.unavailable = unavailable;
        }

        @Override
        public String toString() {
            return "LinksResponse{available=" + available + ", unavailable=" + unavailable + "}";
        }
    }

    public static class Details {
        private String correctUrl;
        private UrlSnapshot original;
        private UrlSnapshot modified;

        public String getCorrectUrl() {
            return correctUrl;
        }

        public void setCorrectUrl(String correctUrl) {
            this.correctUrl = correctUrl;
        }

        public UrlSnapshot getOriginal() {
            return original;
        }

        public void setOriginal(UrlSnapshot original) {
            this.original = original;
        }

        public UrlSnapshot getModified() {
            return modified;
        }

        public void setModified(UrlSnapshot modified) {
            this.modified = modified;
        }

        @Override
        public String toString() {
            return "Details{" +
                    "correctUrl='" + correctUrl + '\'' +
                    ", original=" + original +
                    ", modified=" + modified +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UrlSnapshot {
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
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getOpaque() {
            return opaque;
        }

        public void setOpaque(String opaque) {
            this.opaque = opaque;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRawPath() {
            return rawPath;
        }

        public void setRawPath(String rawPath) {
            this.rawPath = rawPath;
        }

        public Boolean getOmitHost() {
            return omitHost;
        }

        public void setOmitHost(Boolean omitHost) {
            this.omitHost = omitHost;
        }

        public Boolean getForceQuery() {
            return forceQuery;
        }

        public void setForceQuery(Boolean forceQuery) {
            this.forceQuery = forceQuery;
        }

        public String getRawQuery() {
            return rawQuery;
        }

        public void setRawQuery(String rawQuery) {
            this.rawQuery = rawQuery;
        }

        public String getFragment() {
            return fragment;
        }

        public void setFragment(String fragment) {
            this.fragment = fragment;
        }

        public String getRawFragment() {
            return rawFragment;
        }

        public void setRawFragment(String rawFragment) {
            this.rawFragment = rawFragment;
        }

        @Override
        public String toString() {
            return "UrlSnapshot{" +
                    "scheme='" + scheme + '\'' +
                    ", host='" + host + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}
