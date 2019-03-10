package shanika.service;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;
import shanika.dto.CpuStats;
import shanika.dto.DockerStats;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public interface DockerService {

    Pattern TOTAL_MEMORY_PATTERN =
            Pattern.compile("MemTotal:\\s+(\\d+) kB", Pattern.MULTILINE);

    Pattern FREE_MEMORY_PATTERN =
            Pattern.compile("MemFree:\\s+(\\d+) kB", Pattern.MULTILINE);

    Pattern BUFFERS_MEMORY_PATTERN =
            Pattern.compile("Buffers:\\s+(\\d+) kB", Pattern.MULTILINE);

    Pattern CACHED_MEMORY_PATTERN =
            Pattern.compile("Cached:\\s+(\\d+) kB", Pattern.MULTILINE);

    Pattern CPU_JIFFIES_PATTERN =
            Pattern.compile("cpu\\s+(.*)", Pattern.MULTILINE);

    String NETWORK_STATS = "/proc/net/dev";


    /**
     * Get Current Docker version
     *
     * @return {@link Version}
     * @throws DockerException
     * @throws InterruptedException
     */
    Version version() throws DockerException, InterruptedException;

    /**
     * Get Container Statistics
     *
     * @param containerId
     * @return {@link DockerStats}
     * @throws DockerException
     * @throws InterruptedException
     */
    DockerStats getContainerStats(String containerId) throws DockerException, InterruptedException;


    /**
     * Get All Container Statistics
     *
     * @return {@link DockerStats}
     * @throws DockerException
     * @throws InterruptedException
     */
    List<DockerStats> getContainerStats() throws DockerException, InterruptedException;

    long getMemoryStats();

    CpuStats getCpuUsage(long prevTotal, long prevIdle);

    long getNetworkStats();
}
