package br.com.julioneto.models;

public class HealthCheckResponse {
    private String status;
    private String version;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "HealthCheckResponse{status='" + status + "', version='" + version + "'}";
    }
}
