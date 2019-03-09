package shanika.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DockerStats {

    @JsonProperty("usage_memory")
    private long usageMemory;

    @JsonProperty("cpu_usage")
    private double cpu_usage;

    @JsonProperty("network_usage")
    private String network_usage;

    public DockerStats() {
    }

    public long getUsageMemory() {
        return usageMemory;
    }

    public void setUsageMemory(long usageMemory) {
        this.usageMemory = usageMemory;
    }

    public double getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(double cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public String getNetwork_usage() {
        return network_usage;
    }

    public void setNetwork_usage(String network_usage) {
        this.network_usage = network_usage;
    }

    @Override
    public String toString() {
        return "DockerStats{" +
                "usage_memory=" + usageMemory +
                ", cpu_usage=" + cpu_usage +
                ", network_usage='" + network_usage + '\'' +
                '}';
    }
}
