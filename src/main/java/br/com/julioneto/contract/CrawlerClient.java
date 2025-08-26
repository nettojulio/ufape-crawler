package br.com.julioneto.contract;

import br.com.julioneto.dto.CrawlResponse;
import br.com.julioneto.dto.HealthCheckResponse;

import java.io.IOException;

public interface CrawlerClient {
    CrawlResponse fetchPageInfo(String url) throws IOException, InterruptedException;
    HealthCheckResponse checkApiHealth() throws IOException, InterruptedException;
}
