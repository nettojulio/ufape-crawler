package br.com.julioneto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para a resposta do endpoint de health check da API.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCheckResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("version")
    private String version;

    public String getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "HealthCheckResponse{status='" + status + "', version='" + version + "'}";
    }
}
