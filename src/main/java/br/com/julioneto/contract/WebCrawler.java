package br.com.julioneto.contract;

public interface WebCrawler {
    void crawl(String startUrl);
    void crawlConcurrently(String startUrl);
}
