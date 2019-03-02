package shanika.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotify.docker.client.messages.CpuStats;
import com.spotify.docker.client.messages.MemoryStats;
import com.spotify.docker.client.messages.NetworkStats;

public class DockerStats {

    @JsonProperty("network")
    private NetworkStats network;

    @JsonProperty("memory_stats")
    private MemoryStats memoryStats;

    @JsonProperty("cpu_stats")
    private CpuStats cpuStats;

    public DockerStats() {
    }

    public DockerStats(NetworkStats network, MemoryStats memoryStats, CpuStats cpuStats) {
        this.network = network;
        this.memoryStats = memoryStats;
        this.cpuStats = cpuStats;
    }

    public NetworkStats getNetwork() {
        return network;
    }

    public void setNetwork(NetworkStats network) {
        this.network = network;
    }

    public MemoryStats getMemoryStats() {
        return memoryStats;
    }

    public void setMemoryStats(MemoryStats memoryStats) {
        this.memoryStats = memoryStats;
    }

    public CpuStats getCpuStats() {
        return cpuStats;
    }

    public void setCpuStats(CpuStats cpuStats) {
        this.cpuStats = cpuStats;
    }
}
